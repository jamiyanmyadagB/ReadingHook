package com.readingnook.controller;

import com.readingnook.model.Book;
import com.readingnook.model.Page;
import com.readingnook.repository.BookRepository;
import com.readingnook.service.LangDetectService;
import com.readingnook.service.PdfService;
import com.readingnook.service.TranslateService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

/**
 * REST controller for managing books in the ReadingNook application.
 * 
 * Provides endpoints for uploading PDF books, listing books,
 * and retrieving individual books with full content.
 */
@RestController
@RequestMapping("/api/books")
@CrossOrigin(origins = "*")
public class BookController {

    private static final Logger logger = LoggerFactory.getLogger(BookController.class);

    private final BookRepository bookRepository;
    private final PdfService pdfService;
    private final LangDetectService langDetectService;
    private final TranslateService translateService;

    public BookController(BookRepository bookRepository,
                      PdfService pdfService,
                      LangDetectService langDetectService,
                      TranslateService translateService) {
        this.bookRepository = bookRepository;
        this.pdfService = pdfService;
        this.langDetectService = langDetectService;
        this.translateService = translateService;
    }

    /**
     * Uploads and processes a PDF file to create a new book.
     * 
     * @param file PDF file to upload
     * @return Created book with translated content
     */
    @PostMapping("/upload")
    public ResponseEntity<Book> uploadBook(@RequestParam("file") MultipartFile file) {
        try {
            logger.info("Starting book upload: {}", file.getOriginalFilename());

            // Step 1: Extract pages from PDF
            List<Page> pages = pdfService.extractPages(file);
            logger.info("Extracted {} pages from PDF", pages.size());

            // Step 2: Detect source language from first page
            String sourceLanguage = langDetectService.detectLanguage(
                pages.get(0).getOriginalText()
            );
            logger.info("Detected source language: {}", sourceLanguage);

            // Step 3: Translate all pages
            List<Page> translatedPages = translateService.translatePages(pages, sourceLanguage);
            logger.info("Completed translation of {} pages", translatedPages.size());

            // Step 4: Create and save book
            Book book = Book.builder()
                    .title(file.getOriginalFilename().replace(".pdf", ""))
                    .originalLanguage(sourceLanguage)
                    .translatedLanguage("English")
                    .difficulty(calculateOverallDifficulty(translatedPages))
                    .pages(translatedPages)
                    .createdAt(LocalDateTime.now())
                    .build();

            Book savedBook = bookRepository.save(book);
            logger.info("Successfully saved book with ID: {}", savedBook.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedBook);

        } catch (PdfService.PdfProcessingException e) {
            logger.error("PDF processing failed: {}", e.getMessage());
            return ResponseEntity.badRequest().build();
        } catch (TranslateService.TranslationException e) {
            logger.error("Translation failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE).build();
        } catch (Exception e) {
            logger.error("Unexpected error during book upload: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves all books with metadata only (no page content).
     * 
     * @return List of books without page content for better performance
     */
    @GetMapping("/")
    public ResponseEntity<List<BookMetadata>> getAllBooks() {
        try {
            List<Book> books = bookRepository.findAllByOrderByCreatedAtDesc();
            
            // Convert to metadata-only response
            List<BookMetadata> bookMetadata = books.stream()
                    .map(this::convertToMetadata)
                    .collect(Collectors.toList());

            logger.info("Retrieved {} books (metadata only)", bookMetadata.size());
            return ResponseEntity.ok(bookMetadata);

        } catch (Exception e) {
            logger.error("Failed to retrieve books: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves a specific book with full content including all pages.
     * 
     * @param ID of the book to retrieve
     * @return Complete book with all page content
     */
    @GetMapping("/{id}")
    public ResponseEntity<Book> getBookById(@PathVariable String id) {
        try {
            return bookRepository.findById(id)
                    .map(book -> {
                        logger.info("Retrieved book: {} ({} pages)", 
                                   book.getTitle(), book.getPages().size());
                        return ResponseEntity.ok(book);
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            logger.error("Failed to retrieve book {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Calculates overall difficulty based on page difficulties.
     * 
     * @param pages List of translated pages
     * @return Overall difficulty level
     */
    private String calculateOverallDifficulty(List<Page> pages) {
        if (pages.isEmpty()) {
            return "medium";
        }

        long easyCount = pages.stream()
                .filter(page -> "easy".equals(page.getDifficulty()))
                .count();

        long mediumCount = pages.stream()
                .filter(page -> "medium".equals(page.getDifficulty()))
                .count();

        long hardCount = pages.stream()
                .filter(page -> "hard".equals(page.getDifficulty()))
                .count();

        // Determine overall difficulty based on majority
        if (easyCount > mediumCount && easyCount > hardCount) {
            return "easy";
        } else if (hardCount > easyCount && hardCount > mediumCount) {
            return "hard";
        } else {
            return "medium";
        }
    }

    /**
     * Converts Book to metadata-only response.
     * 
     * @param book Full book object
     * @return Book metadata without page content
     */
    private BookMetadata convertToMetadata(Book book) {
        return new BookMetadata(
                book.getId(),
                book.getTitle(),
                book.getOriginalLanguage(),
                book.getTranslatedLanguage(),
                book.getDifficulty(),
                book.getCreatedAt(),
                book.getPages() != null ? book.getPages().size() : 0
        );
    }

    /**
     * DTO for book metadata response (without page content).
     */
    public record BookMetadata(
            String id,
            String title,
            String originalLanguage,
            String translatedLanguage,
            String difficulty,
            LocalDateTime createdAt,
            int pageCount
    ) {}
}
