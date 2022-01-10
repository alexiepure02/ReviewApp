package com.proiect;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class to manage the Add Review Menu.
 *
 * Includes:
 * - text field for the user's name
 * - drop down list to select a rating from 1 to 5
 * - text area for the user's review
 * - delete button to exit the stage
 * - add button to add the written review to the book and then exit the stage
 */
public class ControllerAddReview implements Initializable {

    /**
     * Stage, Scene and Root used to switch scenes.
     */
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Text field for the user's name
     * FXML element
     */
    @FXML
    TextField nameField;
    /**
     * Drop down list to select a rating from 1 to 5
     * FXML element
     */
    @FXML
    ComboBox<Integer> ratingBox;
    /**
     * Text area where the user can share his opinion on the book
     * FXML element
     */
    @FXML
    TextArea reviewArea;
    /**
     * Add the review to the database and exit the scene.
     * FXML element
     */
    @FXML
    Button addButton;
    /**
     * Exit the scene without adding the review to the database.
     * FXML element
     */
    @FXML
    Button deleteButton;

    @FXML
    Text errorText;

    /**
     * A global copy of the new review.
     */
    Review newReview;

    /**
     * Function runs before the FXML of the controller has been loaded.
     * @param url               unused
     * @param resourceBundle    unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        createRatingBoxOptions();
    }

    /**
     * Function to add the options to the Rating Combo Box
     */
    public void createRatingBoxOptions() {
        ratingBox.getItems().addAll(1, 2, 3, 4, 5);
    }

    /**
     * Function to create functionalities for the buttons
     * @param id    id of the book that the new review belongs to
     */
    public void createReview(int id) {

        addButton.setOnAction(event -> {

            String name, review;
            int rating = 0;                 // default value

            // reset error text

            errorText.setText("");

            // extract data

            name = nameField.getText();
            try {
                rating = ratingBox.getValue();
            } catch (Exception ignored) {
            }
            review = reviewArea.getText();

            // validate data

            if(!nameIsValid(name)) {
                errorText.setText("Numele trebuie sa contina doar litere, cifre sau spatii.");
            } else if(!ratingIsValid(rating)) {
                errorText.setText("Nu ati selectat o optiune din lista.");
            } else if(!reviewIsValid(review)) {
                errorText.setText("Recenzie invalida: necompletata sau peste 300 de caractere");
            } else {

                // add new review to database

                Review auxReview = new Review(name, rating, review);
                newReview = auxReview;
                FXMLLoader loader = new FXMLLoader(getClass().getResource("books.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                ControllerBooks controllerBooks = loader.getController();
                controllerBooks.addReviewToBook(auxReview, id);
                stage = (Stage)((Node)event.getSource()).getScene().getWindow();
                stage.close();
            }
            });

        deleteButton.setOnAction(event -> {
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        });
    }

    /**
     * Function to determine whether the text written in the Name Text Field is valid or not.
     * The name can include: lowercase letters, capital letters, digits and spaces.
     * @param name  text written in the text field
     * @return      true or false
     */
    public boolean nameIsValid(String name) {
        return name.matches("[a-zA-Z0-9,.' ]+") && name.length() <= 50;
    }

    /**
     * Function to determine whether the user selected an option from the Rating Combo Box or not.
     * If the user did not select any options, the base value will be 0.
     * @param rating  value of chosen option
     * @return      true or false
     */
    public boolean ratingIsValid(int rating) {
        return rating != 0;
    }

    /**
     * Function to determine whether the text written in the Review Text Area is valid or not.
     * The review can include: lowercase letters, capital letters, digits and spaces.
     * @param review    text written in the text area
     * @return          true or false
     */
    public boolean reviewIsValid(String review) {
        return review.length() <= 300;
    }

    /**
     * Function to be called in ControllerBook to update the list of reviews with the new review without accessing the database
     * @return  new review
     */
    public Review getNewReview() {
        return newReview;
    }
}