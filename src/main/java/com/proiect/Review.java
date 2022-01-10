package com.proiect;

/**
 * This class contains the review for a book.
 *
 * Functionalities:
 * - show the name, the rating (x/5) and the review of the user
 *
 * Admin functionalities:
 * - edit  or delete the review
 */

public class Review {
    /**
     * The name of the user that reviewed the book
     */
    private String name;
    /**
     * The user's rating (x/5) on the book
     */
    private int rating;
    /**
     * The user's opinion on the book
     */
    private String review;

    /**
     * Constructor used for initializing the name, rating and review of a review
     *
     * @param name   User's name
     * @param rating User's rating on the book
     * @param review User's review on the book
     */
    public Review(String name, int rating, String review) {
        this.name = name;
        this.rating = rating;
        this.review = review;
    }

    /**
     * Default constructor
     */
    public Review() {}

    /**
     * @return User's name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name User's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return User's rating on the book
     */
    public int getRating() {
        return rating;
    }

    /**
     * @param rating User's rating on the book
     */
    public void setRating(int rating) {
        this.rating = rating;
    }

    /**
     * @return User's review on the book
     */
    public String getReview() {
        return review;
    }

    /**
     * @param review User's review on the book
     */
    public void setReview(String review) {
        this.review = review;
    }

    /**
     * Override toString() method
     */
    @Override
    public String toString() {
        return "review{" +
                "name='" + name + '\'' +
                ", rating=" + rating +
                ", review='" + review + '\'' +
                '}';
    }
}
