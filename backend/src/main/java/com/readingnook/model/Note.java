package com.readingnook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

/**
 * MongoDB document representing a user note for a specific word or phrase in a book.
 * 
 * Notes are stored separately from books to allow flexible annotation and
 * vocabulary building features. Each note is linked to a book via bookId.
 */
@Document(collection = "notes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Note {

    @Id
    private String id;

    private String bookId;

    private String selectedWord;

    private String originalText;

    private String translatedText;

    private String hinglishExplanation;

    private String difficulty;

    private LocalDateTime createdAt;
}
