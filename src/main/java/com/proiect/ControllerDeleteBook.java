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

public class ControllerDeleteBook implements Initializable {

    private Stage stage;
//    private Scene scene;
//    private Parent root;

    @FXML
    ComboBox<String> bookBox;

    @FXML
    Text errorText;

    /**
     * Delete a book from the database and exit the scene.
     * FXML element
     */
    @FXML
    Button deleteButton;
    /**
     * Exit the scene without deleting a book from the database.
     * FXML element
     */
    @FXML
    Button exitButton;

    Map<String, Integer> booksIdTitle = new HashMap<>();

    int nrBooks = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            createBookBoxOptions();
            bookBox.getItems().addAll(booksIdTitle.keySet());

        } catch (SQLException e) {
            e.printStackTrace();
        }

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

    public void addFunctionalityToButtons() {

        deleteButton.setOnAction(event -> {

            // reset error text

            errorText.setText("");

            // check if a book was selected

            String bookTitle = "no";
            try {
                bookTitle = bookBox.getValue();
            }catch (Exception ignored) {

            }

            if(bookTitle == null)
                errorText.setText("Nu ai selectat o carte");
            else {

                // extract book id

                int bookId = booksIdTitle.get(bookBox.getValue());

                String sqlDeleteBook = "DELETE FROM `books` WHERE id = " + bookId;
                String sqlFixAutoIncrement = "ALTER TABLE `books` AUTO_INCREMENT = " + nrBooks;
                String sqlFixId = "UPDATE `books` SET `id`=`id` - 1 WHERE id > " + bookId;

                // delete cover from images

                File cover = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\book" + bookId + ".png");

                boolean isDeleted = cover.delete();

//                if (isDeleted)
//                    System.out.println("Image deleted successfully");
//                else
//                    System.out.println("Failed to delete the image");

                // in images: fix name of the books in front of the deleted book

                for (int i = bookId + 1; i <= nrBooks; i++) {
                    File bookImage = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\book" + i + ".png");
                    File rename = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\book" + (i - 1) + ".png");

                    boolean isRenamed = bookImage.renameTo(rename);

//                    if (isRenamed)
//                        System.out.println("Cover " + i + " updated to " + (i - 1));
//                    else
//                        System.out.println("Cover " + i + " failed to update");
                }

                // delete book from database
                // fix id problem (11, 12, x, 14, 15)
                // fix auto increment problem (next added book to have id - 1)

                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.getConnection();

                try(Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(sqlDeleteBook);
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
