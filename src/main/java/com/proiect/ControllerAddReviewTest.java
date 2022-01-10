package com.proiect;

import org.junit.Test;

import static org.junit.Assert.*;

public class ControllerAddReviewTest {

    ControllerAddReview test = new ControllerAddReview();

    @Test
    public void nameIsValid() {
        assertFalse(test.nameIsValid(""));
        assertFalse(test.nameIsValid(",./;'[]<>?:{}!@#$%^&*()_+-="));
        assertTrue(test.nameIsValid("nume"));
        assertTrue(test.nameIsValid("NUME"));
        assertTrue(test.nameIsValid("Nume cu cifre si spatii 1234"));
    }

    @Test
    public void reviewIsValid() {
        assertFalse(test.reviewIsValid(""));
        assertTrue(test.reviewIsValid("recenzie"));
        assertTrue(test.reviewIsValid("RECENZIE"));
        assertTrue(test.reviewIsValid("Recenzie cu cifre si spatii 1234"));
    }

    @Test
    public void ratingIsValid() {
        assertFalse(test.ratingIsValid(0));
        assertTrue(test.ratingIsValid(1));
        assertTrue(test.ratingIsValid(5));
    }
}