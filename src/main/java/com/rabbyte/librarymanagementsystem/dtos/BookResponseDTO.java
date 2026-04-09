package com.rabbyte.librarymanagementsystem.dtos;

import com.rabbyte.librarymanagementsystem.entities.Book;

import java.util.List;

public class BookResponseDTO {
    private List<Book> content;
    private int pageNumber;
    private int pageSize;
    private int totalPages;
    private long totalElements;
    private boolean isLastPage;
}
