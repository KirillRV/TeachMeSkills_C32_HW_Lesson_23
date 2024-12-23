package com.teachmeskills.lesson_23.model;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class Book {
    private String title;

    private String author;

    private int year;

    private int pages;

    private String genre;


}