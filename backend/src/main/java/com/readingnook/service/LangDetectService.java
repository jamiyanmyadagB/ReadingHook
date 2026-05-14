package com.readingnook.service;

import org.springframework.stereotype.Service;

/**
 * Service for detecting language based on Unicode character ranges.
 * 
 * Uses simple heuristic detection by analyzing character patterns
 * in the input text. No external libraries required.
 */
@Service
public class LangDetectService {

    /**
     * Detects the language of the given text using Unicode character range analysis.
     * 
     * @param text The text to analyze for language detection
     * @return Human-readable language name or "Unknown" if detection fails
     */
    public String detectLanguage(String text) {
        if (text == null || text.trim().isEmpty()) {
            return "Unknown";
        }

        // Remove whitespace and convert to analysis format
        String cleanText = text.trim().toLowerCase();
        
        // Count characters in different Unicode ranges
        int devanagariCount = 0;
        int arabicCount = 0;
        int chineseCount = 0;
        int japaneseCount = 0;
        int koreanCount = 0;
        int latinCount = 0;
        int totalChars = 0;

        for (int i = 0; i < cleanText.length(); i++) {
            char c = cleanText.charAt(i);
            
            // Skip whitespace and common punctuation
            if (Character.isWhitespace(c) || isCommonPunctuation(c)) {
                continue;
            }

            totalChars++;

            // Devanagari script (Hindi): U+0900 to U+097F
            if (c >= '\u0900' && c <= '\u097F') {
                devanagariCount++;
            }
            // Arabic script: U+0600 to U+06FF
            else if (c >= '\u0600' && c <= '\u06FF') {
                arabicCount++;
            }
            // CJK Unified Ideographs (Chinese): U+4E00 to U+9FFF
            else if (c >= '\u4E00' && c <= '\u9FFF') {
                chineseCount++;
            }
            // Hiragana: U+3040 to U+309F
            else if (c >= '\u3040' && c <= '\u309F') {
                japaneseCount++;
            }
            // Katakana: U+30A0 to U+30FF
            else if (c >= '\u30A0' && c <= '\u30FF') {
                japaneseCount++;
            }
            // Hangul Syllables (Korean): U+AC00 to U+D7AF
            else if (c >= '\uAC00' && c <= '\uD7AF') {
                koreanCount++;
            }
            // Basic Latin: U+0000 to U+007F
            else if (c >= '\u0000' && c <= '\u007F') {
                latinCount++;
            }
        }

        // If no meaningful characters found
        if (totalChars == 0) {
            return "Unknown";
        }

        // Determine language based on character distribution
        // Use 30% threshold for confident detection
        double threshold = 0.3;

        if (devanagariCount / (double) totalChars >= threshold) {
            return "Hindi";
        }
        if (arabicCount / (double) totalChars >= threshold) {
            return "Arabic";
        }
        if (chineseCount / (double) totalChars >= threshold) {
            return "Chinese";
        }
        if (japaneseCount / (double) totalChars >= threshold) {
            return "Japanese";
        }
        if (koreanCount / (double) totalChars >= threshold) {
            return "Korean";
        }
        if (latinCount / (double) totalChars >= threshold) {
            return "English";
        }

        // Fallback: use highest percentage
        int maxCount = Math.max(
            Math.max(devanagariCount, arabicCount),
            Math.max(chineseCount, Math.max(japaneseCount, Math.max(koreanCount, latinCount))
        ));
        double maxPercentage = maxCount / (double) totalChars;

        // Only return a language if at least 20% of characters match
        if (maxPercentage >= 0.2) {
            if (devanagariCount == maxPercentage * totalChars) return "Hindi";
            if (arabicCount == maxPercentage * totalChars) return "Arabic";
            if (chineseCount == maxPercentage * totalChars) return "Chinese";
            if (japaneseCount == maxPercentage * totalChars) return "Japanese";
            if (koreanCount == maxPercentage * totalChars) return "Korean";
            if (latinCount == maxPercentage * totalChars) return "English";
        }

        return "Unknown";
    }

    /**
     * Checks if a character is common punctuation that should be ignored in analysis.
     * 
     * @param c The character to check
     * @return true if the character is common punctuation
     */
    private boolean isCommonPunctuation(char c) {
        return c == '.' || c == ',' || c == '!' || c == '?' || c == ';' || c == ':' ||
               c == '-' || c == '(' || c == ')' || c == '[' || c == ']' ||
               c == '{' || c == '}' || c == '"' || c == '\'' || c == ' ';
    }
}
