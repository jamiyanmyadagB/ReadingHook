package com.readingnook.repository;

import com.readingnook.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Spring Data MongoDB repository for Book entities.
 * 
 * Provides CRUD operations and custom query methods for accessing
 * book data in the MongoDB collection. Extends MongoRepository
 * to inherit standard database operations.
 */
@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    /**
     * Finds all books ordered by creation date in descending order.
     * 
     * @return List of books sorted by newest first
     */
    List<Book> findAllByOrderByCreatedAtDesc();
}
