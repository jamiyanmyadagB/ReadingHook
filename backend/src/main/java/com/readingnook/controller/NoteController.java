package com.readingnook.controller;

import com.readingnook.model.Note;
import com.readingnook.repository.NoteRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;

/**
 * REST controller for managing user notes in ReadingNook application.
 * 
 * Provides endpoints for creating and retrieving vocabulary notes
 * associated with specific books.
 */
@RestController
@RequestMapping("/api/notes")
@CrossOrigin(origins = "*")
public class NoteController {

    private static final Logger logger = LoggerFactory.getLogger(NoteController.class);

    private final NoteRepository noteRepository;

    public NoteController(NoteRepository noteRepository) {
        this.noteRepository = noteRepository;
    }

    /**
     * Creates a new note for a book.
     * 
     * @param note Note object to create
     * @return Created note with generated ID
     */
    @PostMapping("/")
    public ResponseEntity<Note> createNote(@Valid @RequestBody Note note) {
        try {
            logger.info("Creating note for book: {}, word: {}", 
                       note.getBookId(), note.getSelectedWord());

            // Set creation timestamp if not provided
            if (note.getCreatedAt() == null) {
                note.setCreatedAt(LocalDateTime.now());
            }

            Note savedNote = noteRepository.save(note);
            logger.info("Successfully created note with ID: {}", savedNote.getId());

            return ResponseEntity.status(HttpStatus.CREATED).body(savedNote);

        } catch (Exception e) {
            logger.error("Failed to create note: {}", e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves all notes for a specific book.
     * 
     * @param bookId ID of the book to retrieve notes for
     * @return List of notes sorted by creation date (newest first)
     */
    @GetMapping("/{bookId}")
    public ResponseEntity<List<Note>> getNotesByBookId(@PathVariable String bookId) {
        try {
            logger.info("Retrieving notes for book: {}", bookId);

            List<Note> notes = noteRepository.findByBookIdOrderByCreatedAtDesc(bookId);
            logger.info("Found {} notes for book: {}", notes.size(), bookId);

            return ResponseEntity.ok(notes);

        } catch (Exception e) {
            logger.error("Failed to retrieve notes for book {}: {}", bookId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Deletes all notes for a specific book.
     * 
     * @param bookId ID of the book whose notes should be deleted
     * @return Success response
     */
    @DeleteMapping("/{bookId}")
    public ResponseEntity<Void> deleteNotesByBookId(@PathVariable String bookId) {
        try {
            logger.info("Deleting all notes for book: {}", bookId);

            noteRepository.deleteByBookId(bookId);
            logger.info("Successfully deleted notes for book: {}", bookId);

            return ResponseEntity.noContent().build();

        } catch (Exception e) {
            logger.error("Failed to delete notes for book {}: {}", bookId, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Retrieves a specific note by ID.
     * 
     * @param id ID of the note to retrieve
     * @return Note if found, 404 if not found
     */
    @GetMapping("/note/{id}")
    public ResponseEntity<Note> getNoteById(@PathVariable String id) {
        try {
            return noteRepository.findById(id)
                    .map(note -> {
                        logger.info("Retrieved note: {} for book: {}", 
                                   note.getSelectedWord(), note.getBookId());
                        return ResponseEntity.ok(note);
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            logger.error("Failed to retrieve note {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Updates an existing note.
     * 
     * @param id ID of the note to update
     * @param updatedNote Updated note data
     * @return Updated note if found, 404 if not found
     */
    @PutMapping("/note/{id}")
    public ResponseEntity<Note> updateNote(@PathVariable String id, 
                                        @Valid @RequestBody Note updatedNote) {
        try {
            return noteRepository.findById(id)
                    .map(existingNote -> {
                        logger.info("Updating note: {} for book: {}", 
                                   existingNote.getSelectedWord(), existingNote.getBookId());

                        // Preserve original ID and creation time
                        updatedNote.setId(id);
                        updatedNote.setBookId(existingNote.getBookId());
                        updatedNote.setCreatedAt(existingNote.getCreatedAt());

                        Note savedNote = noteRepository.save(updatedNote);
                        logger.info("Successfully updated note: {}", savedNote.getId());

                        return ResponseEntity.ok(savedNote);
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            logger.error("Failed to update note {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }

    /**
     * Deletes a specific note by ID.
     * 
     * @param id ID of the note to delete
     * @return Success response if found, 404 if not found
     */
    @DeleteMapping("/note/{id}")
    public ResponseEntity<Void> deleteNoteById(@PathVariable String id) {
        try {
            return noteRepository.findById(id)
                    .map(note -> {
                        logger.info("Deleting note: {} for book: {}", 
                                   note.getSelectedWord(), note.getBookId());

                        noteRepository.deleteById(id);
                        logger.info("Successfully deleted note: {}", id);

                        return ResponseEntity.noContent().<Void>build();
                    })
                    .orElse(ResponseEntity.notFound().build());

        } catch (Exception e) {
            logger.error("Failed to delete note {}: {}", id, e.getMessage(), e);
            return ResponseEntity.internalServerError().build();
        }
    }
}
