package com.readingnook.service;

import com.readingnook.model.Page;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Service for extracting text content from PDF files using Apache PDFBox.
 * 
 * Processes uploaded PDF files page by page and creates Page objects
 * with original text content for translation processing.
 */
@Service
public class PdfService {

    private static final Logger logger = LoggerFactory.getLogger(PdfService.class);

    /**
     * Extracts text content from uploaded PDF file page by page.
     * 
     * @param file The uploaded PDF file
     * @return List of Page objects containing extracted text
     * @throws PdfProcessingException if PDF processing fails
     */
    public List<Page> extractPages(MultipartFile file) {
         validatePdfFile(file);
         
         List<Page> pages = new ArrayList<>();
         
         try (InputStream inputStream = file.getInputStream();
              PDDocument document = PDDocument.load(inputStream)) {
             
             int pageCount = document.getNumberOfPages();
             logger.info("Processing PDF with {} pages: {}", pageCount, file.getOriginalFilename());
             
             PDFTextStripper textStripper = new PDFTextStripper();
             
             for (int pageNum = 1; pageNum <= pageCount; pageNum++) {
                 try {
                     // Configure text stripper for current page
                     textStripper.setStartPage(pageNum);
                     textStripper.setEndPage(pageNum);
                     
                     // Extract text from current page
                     String pageText = textStripper.getText(document).trim();
                     
                     // Create Page object
                     Page page = Page.builder()
                             .pageNumber(pageNum)
                             .originalText(pageText)
                             .translatedText("") // Will be filled by translation service
                             .build();
                     
                     pages.add(page);
                     
                     logger.debug("Extracted page {}: {} characters", pageNum, pageText.length());
                     
                 } catch (Exception e) {
                     logger.warn("Failed to extract page {} from PDF: {}", pageNum, e.getMessage());
                     // Continue processing other pages
                 }
             }
             
             logger.info("Successfully extracted {} pages from PDF", pages.size());
             
         } catch (IOException e) {
             logger.error("Failed to process PDF file: {}", e.getMessage());
             throw new PdfProcessingException("Failed to extract text from PDF: " + e.getMessage(), e);
         }
         
         if (pages.isEmpty()) {
             throw new PdfProcessingException("No text content could be extracted from the PDF");
         }
         
         return pages;
     }

    /**
     * Validates the uploaded PDF file.
     * 
     * @param file The uploaded file to validate
     * @throws PdfProcessingException if validation fails
     */
    private void validatePdfFile(MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new PdfProcessingException("PDF file is required and cannot be empty");
        }
        
        String filename = file.getOriginalFilename();
        if (filename == null || !filename.toLowerCase().endsWith(".pdf")) {
            throw new PdfProcessingException("Only PDF files are supported");
        }
        
        // Check file size (50MB limit from application.properties)
        if (file.getSize() > 50 * 1024 * 1024) {
            throw new PdfProcessingException("PDF file size exceeds 50MB limit");
        }
        
        logger.debug("PDF file validation passed: {} ({} bytes)", filename, file.getSize());
    }
    
    /**
     * Custom exception for PDF processing errors.
     */
    public static class PdfProcessingException extends RuntimeException {
        public PdfProcessingException(String message) {
            super(message);
        }
        
        public PdfProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
