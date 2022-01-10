package com.proiect;

import java.util.List;

/**
 * This class contains information about a certain book.
 *
 * Functionalities:
 * - show image and name
 * - show a general rating based on ratings using an arithmetic mean
 * - show description about the book
 * - show list of reviews
 * - add a review
 *
 * Admin functionalities:
 * - edit the name, the author, the category, the description and the reviews of the book
 */

public class Book {
    /**
     * The book's id
     */
    private int id;
    /**
     * The book's title
     */
    private String title;
    /**
     * The author that wrote the book
     */
    private String author;
    /**
     * The category to which the book belongs
     */
    private String category;
    /**
     * The book's description
     */
    private String description;
    /**
     * The average rating of the book based on all reviews
     */
    private double averageRating;
    /**
     * The reviews that the book got
     */
    private List<Review> reviews;

    /**
     * Constructor used for initializing the title, author, category, average rating, description and reviews of a book
     *
     * @param id          Book's id
     * @param title       Book's title
     * @param author      Book's author
     * @param category    Book's category
     * @param description Book's description
     * @param reviews     Book's reviews
     */
    public Book(int id, String title, String author, String category, String description, List<Review> reviews) {
        this.id = id;
        double sumRatings = 0;

        this.title = title;
        this.author = author;
        this.category = category;
        this.description = description;
        for (Review review : reviews) {
            sumRatings += review.getRating();
        }
        this.averageRating = sumRatings / reviews.size();
        this.reviews = reviews;
    }

    /**
     * Default constructor
     */
    public Book() {}

    /**
     * Function to add a review to the book
     */
    public void addReview(Review review) {
        this.reviews.add(review);
    }

    /**
     * @return Book's id
     */
    public int getId() {
        return id;
    }

    /**
     * @param id Book's id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * @return Book's title
     */
    public String getTitle() {
        return title;
    }

    /**
     * @param title Book's title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * @return Book's author
     */
    public String getAuthor() {
        return author;
    }

    /**
     * @param author Book's author
     */
    public void setAuthor(String author) {
        this.author = author;
    }

    /**
     * @return Book's category
     */
    public String getCategory() {
        return category;
    }

    /**
     * @param category Book's category
     */
    public void setCategory(String category) {
        this.category = category;
    }

    /**
     * @return Book's description
     */
    public String getDescription() {
        return description;
    }

    /**
     * @param description Book's description
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return Book's average rating
     */
    public double getAverageRating() {
        return averageRating;
    }

    /**
     * @param averageRating Book's average rating
     */
    public void setAverageRating(double averageRating) {
        this.averageRating = averageRating;
    }

    /**
     * @return Book's reviews
     */
    public List<Review> getReviews() {
        return reviews;
    }

    /**
     * @param reviews Book's reviews
     */
    public void setReviews(List<Review> reviews) {
        this.reviews = reviews;
    }

    /**
     * Override toString() method
     */
    @Override
    public String toString() {
        return "Book{" +
                "id=" + id + '\'' +
                "title='" + title + '\'' +
                ", author='" + author + '\'' +
                ", category='" + category + '\'' +
                ", description='" + description + '\'' +
                ", averageRating=" + averageRating +
                ", reviews=" + reviews +
                '}';
    }
}
