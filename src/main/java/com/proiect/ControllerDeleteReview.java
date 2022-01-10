package com.proiect;

import connectivity.ConnectionClass;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class ControllerDeleteReview implements Initializable {

    private Stage stage;
//    private Scene scene;
//    private Parent root;

    @FXML
    ComboBox<String> bookBox;

    @FXML
    ComboBox<String> reviewBox;

    @FXML
    Text errorText;

    /**
     * Delete a review from the database and exit the scene.
     * FXML element
     */
    @FXML
    Button deleteButton;
    /**
     * Exit the scene without deleting a review from the database.
     * FXML element
     */
    @FXML
    Button exitButton;

    Map<String, Integer> booksIdTitle = new HashMap<>();
    Map<String, Integer> reviewsIdName = new HashMap<>();

    int nrReviews = 0;
    int nrBooks = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            createBookBoxOptions();
            bookBox.getItems().addAll(booksIdTitle.keySet());
        } catch (SQLException e) {
            e.printStackTrace();
        }

        bookBox.getSelectionModel().selectedItemProperty().addListener( (v, oldValue, newValue) -> {

            System.out.println("aaa");
            reviewBox.setDisable(false);
            reviewBox.getItems().clear();
            reviewsIdName.clear();

            try {
                createReviewBoxOptions(booksIdTitle.get(newValue));
            } catch (SQLException e) {
                e.printStackTrace();
            }

            reviewBox.getItems().addAll(reviewsIdName.keySet());

        });

        addFunctionalityToButtons();
    }

    private void createBookBoxOptions() throws SQLException {
        ConnectionClass connection = new ConnectionClass();
        Connection con = connection.getConnection();

        String sqlBookIdName = "SELECT id, title FROM books";

        try (Statement stmt = con.createStatement()) {
            ResultSet rsBooks = stmt.executeQuery(sqlBookIdName);
            while (rsBooks.next()) {
                booksIdTitle.put(rsBooks.getString("title"), rsBooks.getInt("id"));
                nrBooks++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    private void createReviewBoxOptions(int bookId) throws SQLException {



        ConnectionClass connection = new ConnectionClass();
        Connection con = connection.getConnection();

        String sqlReviews = "SELECT * FROM reviews WHERE book_id = " + bookId;

        try (Statement stmt = con.createStatement()) {
            ResultSet rsReviews = stmt.executeQuery(sqlReviews);
            while (rsReviews.next()) {

                String name = rsReviews.getString("name") + " - ";
                String review = rsReviews.getString("review");
                if(review.length() <= 10)
                    reviewsIdName.put(name + review, rsReviews.getInt("id"));
                else
                    reviewsIdName.put(name + review.substring(0, 10) + "...", rsReviews.getInt("id"));
                nrReviews++;
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public void addFunctionalityToButtons() {

        deleteButton.setOnAction(event -> {

            // reset error text

            errorText.setText("");

            // check if a review was selected

            String reviewName = "no";
            try {
                reviewName = reviewBox.getValue();
            }catch (Exception ignored) {

            }

            if(reviewName == null)
                errorText.setText("Nu ai selectat o carte");
            else {

                // extract review id

                int reviewId = reviewsIdName.get(reviewBox.getValue());

                String sqlDeleteReview = "DELETE FROM `reviews` WHERE id = " + reviewId;
                String sqlFixAutoIncrement = "ALTER TABLE `reviews` AUTO_INCREMENT = " + nrReviews;
                String sqlFixId = "UPDATE `reviews` SET `id`=`id` - 1 WHERE id > " + reviewId;

                // delete review from database
                // fix id problem (11, 12, x, 14, 15)
                // fix auto increment problem (next added review to have id - 1)

                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.getConnection();

                try(Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(sqlDeleteReview);
                    stmt.executeUpdate(sqlFixId);
                    stmt.executeUpdate(sqlFixAutoIncrement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                stage.close();
            }
        });

        exitButton.setOnAction(event -> {
            stage = (Stage)((Node)event.getSource()).getScene().getWindow();
            stage.close();
        });
    }
}
