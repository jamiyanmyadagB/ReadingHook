package com.readingnook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

/**
 * MongoDB document representing a book in the ReadingNook system.
 * 
 * This entity stores book metadata along with all pages as embedded documents.
 * Pages are embedded rather than stored separately to maintain data consistency
 * and optimize read performance for complete book retrieval.
 */
@Document(collection = "books")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Book {

    @Id
    private String id;

    private String title;

    private String originalLanguage;

    private String translatedLanguage;

    private String difficulty;

    private List<Page> pages;

    private LocalDateTime createdAt;
}
