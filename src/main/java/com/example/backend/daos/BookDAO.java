package com.example.backend.daos;

import java.util.List;

import com.example.backend.models.Book;

public interface BookDAO {

  public Book findById(int id);

  public List<Book> findAll();

  public boolean save(Book book);

  public boolean delete(int id);

  public List<Book> findByTitle(String title);

  public List<Book> findByAuthor(String author);
}
