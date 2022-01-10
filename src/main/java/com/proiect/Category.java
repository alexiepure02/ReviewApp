package com.proiect;

import java.util.List;

/**
 * This class contains books from a certain category
 *
 * Functionalities:
 * - show the name of the category
 * - show a description of the category
 * - show books from a category
 *
 * Admin functionalities:
 * - edit the name and the description of the category
 */

public class Category {

    /**
     * Category's id
     */
    private int id;
    /**
     * Category's name
     */
    private String name;
    /**
     * Category's description
     */
    private String description;

    /**
     * Category's average rating
     */
    private double averageRating;
    /**
     * Category's books
     */
    private List<Book> books;

    /**
     * Constructor used for initializing the name, description and books of an author
     * @param id            Category's id
     * @param name          Category's name
     * @param description   Category's description
     * @param averageRating Category's average rating
     * @param books         Category's books
     */
    public Category(int id, String name, String description, double averageRating, List<Book> books) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.averageRating = averageRating;
        this.books = books;
    }

    public Category() {}

    /**
     * @return Category's id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Category's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Category's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Category's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Category's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description Category's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Category's average rating
     */
    public double getAverageRating() { return averageRating; }

    /**
     * @param averageRating Category's average rating
     */
    public void setAverageRating(double averageRating) { this.averageRating = averageRating; }

    /**
     * @return Category's books
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * @param books Category's books
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Override toString() method
     */
    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", books=" + books +
                '}';
    }


}

