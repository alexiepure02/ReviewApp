package com.proiect;

import java.util.List;

/**
 * This class contains information about a certain author.
 *
 * Functionalities:
 * - show image and name
 * - show description about the author
 * - show a general rating based on ratings from his books using an arithmetic mean
 * - show his books
 *
 * Admin functionalities:
 * - edit the name and the description of an author
 */

public class Author {
    /**
     * Author's id
     */
    private int id;
    /**
     * Author's name
     */
    private String name;
    /**
     * Author's description
     */
    private String description;
    /**
     * Author's average rating
     */
    private double averageRating;
    /**
     * Author's books
     */
    private List<Book> books;

    /**
     * Constructor used for initializing the name, description, average rating and books of an author
     *
     * @param id            Author's id
     * @param name          Author's name
     * @param description   Author's description
     * @param averageRating Author's average rating
     * @param books         Author's books
     */
    public Author(int id, String name, String description, double averageRating, List<Book> books) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.averageRating = averageRating;
        this.books = books;
    }

    /**
     * Default constructor
     */
    public Author() {}

    /**
     * @return Author's id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Author's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Author's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name Author's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return Author's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description Author's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Author's average rating
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * @param averageRating Author's average rating
     */
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * @return Author's books
     */
    public List<Book> getBooks() {
        return books;
    }

    /**
     * @param books Author's books
     */
    public void setBooks(List<Book> books) {
        this.books = books;
    }

    /**
     * Override toString() method
     */
    @Override
    public String toString() {
        return "Author{" +
                "id='" + id + '\'' +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", averageRating=" + averageRating +
                ", books=" + books +
                '}';
    }
}
