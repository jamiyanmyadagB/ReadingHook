package com.readingnook.service;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.readingnook.model.Page;
import okhttp3.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * Service for translating text using Google Gemini 1.5 Flash API.
 * 
 * Provides context-aware translation with difficulty assessment
 * and retry logic for handling rate limits.
 */
@Service
public class TranslateService {

    private static final Logger logger = LoggerFactory.getLogger(TranslateService.class);
    
    private static final String GEMINI_API_URL = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash-latest:generateContent";
    private static final int MAX_RETRIES = 3;
    private static final int RETRY_DELAY_MS = 2000;
    private static final int TIMEOUT_SECONDS = 60;

    private final OkHttpClient httpClient;
    private final Gson gson;
    private final String apiKey;

    public TranslateService(@Value("${gemini.api.key}") String apiKey) {
        this.apiKey = apiKey;
        this.gson = new Gson();
        this.httpClient = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .readTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .writeTimeout(Duration.ofSeconds(TIMEOUT_SECONDS))
                .build();
    }

    /**
     * Translates a list of pages to English with context awareness.
     * 
     * @param pages List of pages to translate
     * @param sourceLanguage Source language of the content
     * @return List of pages with translated text and difficulty assessment
     * @throws TranslationException if translation fails after retries
     */
    public List<Page> translatePages(List<Page> pages, String sourceLanguage) {
        if (pages == null || pages.isEmpty()) {
            return pages;
        }

        logger.info("Starting translation of {} pages from {} to English", pages.size(), sourceLanguage);

        String previousPageText = "";
        
        for (int i = 0; i < pages.size(); i++) {
            Page currentPage = pages.get(i);
            
            try {
                logger.debug("Translating page {} of {}: {} characters", 
                           i + 1, pages.size(), currentPage.getOriginalText().length());
                
                TranslationResult result = translateWithRetry(
                    currentPage.getOriginalText(), 
                    previousPageText, 
                    sourceLanguage
                );
                
                // Update page with translation results
                currentPage.setTranslatedText(result.translatedText());
                currentPage.setDifficulty(result.difficulty());
                
                // Set current page as context for next page
                previousPageText = currentPage.getOriginalText();
                
                logger.debug("Successfully translated page {}: difficulty={}, translation length={}", 
                           i + 1, result.difficulty(), result.translatedText().length());
                
            } catch (Exception e) {
                logger.error("Failed to translate page {} after {} retries: {}", 
                           i + 1, MAX_RETRIES, e.getMessage());
                // Set empty translation to maintain page order
                currentPage.setTranslatedText("");
                currentPage.setDifficulty("unknown");
            }
        }

        logger.info("Completed translation of {} pages", pages.size());
        return pages;
    }

    /**
     * Translates text with retry logic for handling rate limits.
     * 
     * @param text Text to translate
     * @param context Previous page text for context
     * @param sourceLanguage Source language
     * @return Translation result with text and difficulty
     * @throws TranslationException if all retries fail
     */
    private TranslationResult translateWithRetry(String text, String context, String sourceLanguage) 
            throws TranslationException {
        
        Exception lastException = null;
        
        for (int attempt = 1; attempt <= MAX_RETRIES; attempt++) {
            try {
                if (attempt > 1) {
                    logger.info("Retry attempt {} for translation", attempt);
                    Thread.sleep(RETRY_DELAY_MS);
                }
                
                return translateSingle(text, context, sourceLanguage);
                
            } catch (RateLimitException e) {
                logger.warn("Rate limit hit on attempt {}: {}", attempt, e.getMessage());
                lastException = e;
                
                // Exponential backoff for rate limits
                try {
                    long backoffTime = RETRY_DELAY_MS * (long) Math.pow(2, attempt - 1);
                    logger.info("Waiting {} ms before retry", backoffTime);
                    Thread.sleep(backoffTime);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new TranslationException("Translation interrupted", ie);
                }
                
            } catch (Exception e) {
                logger.error("Translation attempt {} failed: {}", attempt, e.getMessage());
                lastException = e;
                
                if (attempt < MAX_RETRIES) {
                    try {
                        Thread.sleep(RETRY_DELAY_MS);
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        throw new TranslationException("Translation interrupted", ie);
                    }
                }
            }
        }
        
        throw new TranslationException("Translation failed after " + MAX_RETRIES + " attempts", lastException);
    }

    /**
     * Performs single translation request to Gemini API.
     * 
     * @param text Text to translate
     * @param context Previous page text for context
     * @param sourceLanguage Source language
     * @return Translation result
     * @throws TranslationException if translation fails
     */
    private TranslationResult translateSingle(String text, String context, String sourceLanguage) 
            throws TranslationException {
        
        try {
            String prompt = buildTranslationPrompt(text, context, sourceLanguage);
            String requestBody = buildRequestBody(prompt);
            
            Request request = new Request.Builder()
                    .url(GEMINI_API_URL + "?key=" + apiKey)
                    .post(RequestBody.create(requestBody, MediaType.get("application/json")))
                    .build();

            try (Response response = httpClient.newCall(request).execute()) {
                if (!response.isSuccessful()) {
                    handleErrorResponse(response);
                }

                String responseBody = response.body().string();
                return parseTranslationResponse(responseBody);
            }
            
        } catch (IOException e) {
            throw new TranslationException("Network error during translation", e);
        }
    }

    /**
     * Builds enterprise-quality translation prompt with context.
     * 
     * @param text Text to translate
     * @param context Previous page text
     * @param sourceLanguage Source language
     * @return Formatted prompt for Gemini API
     */
    private String buildTranslationPrompt(String text, String context, String sourceLanguage) {
        StringBuilder prompt = new StringBuilder();
        
        prompt.append("You are a professional translator specializing in educational content translation. ");
        prompt.append("Translate the following text from ").append(sourceLanguage).append(" to English.\n\n");
        
        if (!context.isEmpty()) {
            prompt.append("CONTEXT FROM PREVIOUS PAGE:\n");
            prompt.append(context).append("\n\n");
        }
        
        prompt.append("TEXT TO TRANSLATE:\n");
        prompt.append(text).append("\n\n");
        
        prompt.append("REQUIREMENTS:\n");
        prompt.append("1. Provide accurate, natural-sounding English translation\n");
        prompt.append("2. Maintain the original meaning and tone\n");
        prompt.append("3. Preserve any technical or academic terminology\n");
        prompt.append("4. Format the response as JSON with exactly these fields:\n");
        prompt.append("   - \"translatedText\": the English translation\n");
        prompt.append("   - \"difficulty\": assess reading difficulty as \"easy\", \"medium\", or \"hard\"\n\n");
        
        prompt.append("DIFFICULTY GUIDELINES:\n");
        prompt.append("- \"easy\": Simple vocabulary, short sentences, basic concepts\n");
        prompt.append("- \"medium\": Moderate vocabulary, some complex sentences, intermediate concepts\n");
        prompt.append("- \"hard\": Advanced vocabulary, complex sentence structure, specialized concepts\n\n");
        
        prompt.append("Respond only with valid JSON, no additional text.");
        
        return prompt.toString();
    }

    /**
     * Builds JSON request body for Gemini API.
     * 
     * @param prompt Translation prompt
     * @return JSON request body string
     */
    private String buildRequestBody(String prompt) {
        JsonObject requestBody = new JsonObject();
        requestBody.addProperty("contents", "[{\"parts\":[{\"text\":\"" + escapeJson(prompt) + "\"}]}]");
        requestBody.addProperty("generationConfig", "{\"temperature\":0.3,\"maxOutputTokens\":2048}");
        
        return requestBody.toString();
    }

    /**
     * Parses Gemini API response to extract translation and difficulty.
     * 
     * @param responseBody JSON response from Gemini API
     * @return Translation result with text and difficulty
     * @throws TranslationException if response parsing fails
     */
    private TranslationResult parseTranslationResponse(String responseBody) throws TranslationException {
        try {
            JsonObject response = gson.fromJson(responseBody, JsonObject.class);
            
            if (response.has("candidates") && response.getAsJsonArray("candidates").size() > 0) {
                JsonObject candidate = response.getAsJsonArray("candidates")
                        .get(0).getAsJsonObject();
                
                if (candidate.has("content")) {
                    JsonObject content = candidate.getAsJsonObject("content");
                    if (content.has("parts") && content.getAsJsonArray("parts").size() > 0) {
                        String text = content.getAsJsonArray("parts")
                                .get(0).getAsJsonObject()
                                .get("text").getAsString();
                        
                        // Parse JSON response from Gemini
                        JsonObject translationJson = gson.fromJson(text, JsonObject.class);
                        
                        String translatedText = translationJson.get("translatedText").getAsString();
                        String difficulty = translationJson.get("difficulty").getAsString();
                        
                        // Validate difficulty
                        if (!difficulty.equals("easy") && !difficulty.equals("medium") && !difficulty.equals("hard")) {
                            difficulty = "medium"; // fallback
                        }
                        
                        return new TranslationResult(translatedText, difficulty);
                    }
                }
            }
            
            throw new TranslationException("Invalid response format from Gemini API");
            
        } catch (Exception e) {
            throw new TranslationException("Failed to parse translation response", e);
        }
    }

    /**
     * Handles error responses from Gemini API.
     * 
     * @param response HTTP response from Gemini API
     * @throws TranslationException with appropriate error details
     */
    private void handleErrorResponse(Response response) throws TranslationException {
        try {
            String errorBody = response.body().string();
            JsonObject errorJson = gson.fromJson(errorBody, JsonObject.class);
            
            if (errorJson.has("error")) {
                JsonObject error = errorJson.getAsJsonObject("error");
                int code = error.get("code").getAsInt();
                String message = error.get("message").getAsString();
                
                if (code == 429) {
                    throw new RateLimitException("Rate limit exceeded: " + message);
                } else if (code == 400) {
                    throw new TranslationException("Bad request: " + message);
                } else if (code == 403) {
                    throw new TranslationException("Forbidden: " + message);
                } else {
                    throw new TranslationException("API error (" + code + "): " + message);
                }
            }
            
            throw new TranslationException("HTTP " + response.code() + ": " + response.message());
            
        } catch (IOException e) {
            throw new TranslationException("HTTP " + response.code() + ": " + response.message(), e);
        }
    }

    /**
     * Escapes special characters in JSON strings.
     * 
     * @param text Text to escape
     * @return Escaped text
     */
    private String escapeJson(String text) {
        return text.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }

    /**
     * Record to hold translation result.
     */
    private record TranslationResult(String translatedText, String difficulty) {}

    /**
     * Custom exception for translation errors.
     */
    public static class TranslationException extends RuntimeException {
        public TranslationException(String message) {
            super(message);
        }
        
        public TranslationException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    /**
     * Custom exception for rate limit errors.
     */
    public static class RateLimitException extends TranslationException {
        public RateLimitException(String message) {
            super(message);
        }
    }
}
