package com.proiect;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Controller class to manage the Book Menu.
 *
 * Includes:
 * - back button to the Books Menu, Authors Menu and Categories Menu, depending on the previous scene
 * - cover image
 * - title of the book
 * - author that wrote the book
 * - the category that the book falls into
 * - average rating based on the ratings of the reviews
 * - description about the book
 * - list of reviews, each containing: user's name, user's rating (from 1 to 5) and user's review
 */
public class ControllerBook {

    /**
     * Stage, Scene and Root used to switch scenes.
     */
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Button that takes you back to the previous scene.
     * FXML element
     */
    @FXML
    private Button backButton;
    /**
     * Cover of the book.
     * FXML element
     */
    @FXML
    private ImageView coverBook;
    /**
     * Title of the book.
     * FXML element
     */
    @FXML
    private Text titleBook;
    /**
     * Author that wrote the book.
     * FXML element
     */
    @FXML
    private Text authorBook;
    /**
     * Category that the book falls into.
     * FXML element
     */
    @FXML
    private Text categoryBook;
    /**
     * Average rating of the book based on all reviews.
     * FXML element
     */
    @FXML
    private Text averageRatingBook;
    /**
     * A small description of the book.
     * FXML element
     */
    @FXML
    private Text descriptionBook;
    /**
     * The list of reviews that users gave to the book.
     * FXML element
     */
    @FXML
    private VBox reviewsBox;

    /**
     * A global copy of the book for refreshing the list of reviews after adding a review.
     */
    Book auxBook;

    /**
     * Previous scene.
     */
    String parentScene;

    /**
     * Function to create and insert data about the book into the GUI
     * @param book                      selected book
     * @param parentScene               previous scene
     */
    public void createBook(Book book, String parentScene) {

        auxBook = book;
        this.parentScene = parentScene;

        // back button

        switch (parentScene) {
            case "books" -> backButton.setOnAction(event -> {
                try {
                    switchToBooks(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            case "authors" -> backButton.setOnAction(event -> {
                try {
                    switchToAuthors(event, book.getAuthor());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
            case "categories" -> backButton.setOnAction(event -> {
                try {
                    switchToCategories(event);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }

        // create book's information

        try {
            coverBook.setImage(new Image(new FileInputStream("src/images/book" + book.getId() + ".png"), 130, 192, false, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        titleBook.setText(book.getTitle());
        authorBook.setText(book.getAuthor());
        categoryBook.setText(book.getCategory());
        descriptionBook.setText(book.getDescription());
        descriptionBook.setWrappingWidth(800);
        averageRatingBook.setText(String.valueOf(book.getAverageRating()) + "/5");

        // create reviews

        for(Review review : book.getReviews()) {

            Text name = new Text(review.getName());
            Text rating = new Text(String.valueOf(review.getRating()));
            Text rev = new Text(review.getReview());

            VBox reviewBox = new VBox();
            reviewBox.setPadding(new Insets(10, 10,  10, 10));

            reviewBox.getChildren().addAll(name, rating, rev);

            reviewsBox.getChildren().add(reviewBox);
        }
    }

    /**
     * Function to switch to and create the Add Review Menu in another stage
     * @param event         add review button's event
     * @throws IOException  error thrown when the FXML doesn't exist or createBook() throws error
     */
    public void switchToAndAddReview(ActionEvent event) throws IOException {

        FXMLLoader loader = new FXMLLoader(getClass().getResource("addReview.fxml"));
        try {
            root = loader.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        ControllerAddReview controllerAddReview = loader.getController();
        controllerAddReview.createReview(auxBook.getId());

        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();

        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        stage.initOwner(primaryStage);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.showAndWait();

        Review newReview = controllerAddReview.getNewReview();

        // update GUI with new review and average rating

        if(newReview != null) {
            auxBook.addReview(newReview);

            double sumRatings = 0;
            for (Review review : auxBook.getReviews()) {
                sumRatings += review.getRating();
            }
            auxBook.setAverageRating((double) Math.round(sumRatings / auxBook.getReviews().size() * 100) / 100);

            reviewsBox.getChildren().clear();
            createBook(auxBook, parentScene);
        }
    }

    /**
     * Function to switch the current scene to the Books Menu scene
     * @param event         back button's event
     * @throws IOException  error thrown when the FXML doesn't exist
     */
    public void switchToBooks(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("books.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Function to switch the current scene to the Authors Menu scene
     * @param event         back button's event
     * @throws IOException  error thrown when the FXML doesn't exist
     */
    public void switchToAuthors(ActionEvent event, String author) throws IOException {
        root = FXMLLoader.load(getClass().getResource("authors.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Function to switch the current scene to the Categories Menu scene
     * @param event         back button's event
     * @throws IOException  error thrown when the FXML doesn't exist
     */
    public void switchToCategories(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("categories.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
