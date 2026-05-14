package com.readingnook.repository;

import com.readingnook.model.Note;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for Note entities.
 * 
 * Provides CRUD operations and custom query methods for accessing
 * note data in the MongoDB collection. Supports book-specific
 * note retrieval and bulk deletion operations.
 */
@Repository
public interface NoteRepository extends MongoRepository<Note, String> {

    /**
     * Finds all notes for a specific book ordered by creation date in descending order.
     * 
     * @param bookId The ID of the book to find notes for
     * @return List of notes for the specified book, sorted by newest first
     */
    List<Note> findByBookIdOrderByCreatedAtDesc(String bookId);

    /**
     * Deletes all notes associated with a specific book.
     * 
     * @param bookId The ID of the book whose notes should be deleted
     */
    void deleteByBookId(String bookId);
}
