package com.readingnook.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Simple POJO representing a page within a Book document.
 * 
 * This class is NOT a MongoDB document itself, but rather an embedded
 * object that gets stored as part of the Book document structure.
 * Each page contains the original text and its translated version.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Page {

    private int pageNumber;

    private String originalText;

    private String translatedText;

    private String difficulty;
}
