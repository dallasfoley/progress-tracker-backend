package com.example.backend.daos;

import java.util.List;

import com.example.backend.dtos.BookDetails;
import com.example.backend.models.Book;

public interface BookDAO {

  public Book findById(int id);

  public List<BookDetails> findAll();

  public boolean save(String title, String author, int yearPublished, String genre, double rating, int pageCount,
      String coverUrl, String description);

  public boolean delete(int id);

  public List<Book> findByTitle(String title);

  public List<Book> findByAuthor(String author);

  public boolean update(Book book);
}
