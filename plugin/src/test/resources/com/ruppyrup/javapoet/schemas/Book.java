package com.ruppyrup.javapoet.schemas;

import java.lang.String;

public class Book {
  private String author = "J R Hartley";

  private String title = "Fly fishing";

  private Book(BookBuilder bookBuilder) {
    this.author = bookBuilder.author;
    this.title = bookBuilder.title;
  }

  public static BookBuilder builder() {
    return new BookBuilder();
  }

  public String getAuthor() {
    return this.author;
  }

  public String getTitle() {
    return this.title;
  }

  public static class BookBuilder {
    private String author = "J R Hartley";

    private String title = "Fly fishing";

    public Book build() {
      return new Book(this);
    }

    public BookBuilder withAuthor(String author) {
      this.author = author;
      return this;
    }

    public BookBuilder withTitle(String title) {
      this.title = title;
      return this;
    }
  }
}
