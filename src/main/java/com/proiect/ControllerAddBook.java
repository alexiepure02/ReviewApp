package com.proiect;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.*;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

public class ControllerAddBook implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField titleField;

    @FXML
    ComboBox<String> authorBox;

    @FXML
    ComboBox<String> categoryBox;

    @FXML
    TextArea descriptionArea;

    @FXML
    Text imageText;

    @FXML
    Text errorText;

    /**
     * Add the book to the database and exit the scene.
     * FXML element
     */
    @FXML
    Button addButton;
    /**
     * Exit the scene without adding the book to the database.
     * FXML element
     */
    @FXML
    Button exitButton;

    Map<String, Integer> authorsIdName = new HashMap<>();
    Map<String, Integer> categoriesIdName = new HashMap<>();
    File cover;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {

            createAuthorBoxOptions();
            authorBox.getItems().addAll(authorsIdName.keySet());

            createCategoryBoxOptions();
            categoryBox.getItems().addAll(categoriesIdName.keySet());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        addFunctionalityToButtons();
    }

    private void createAuthorBoxOptions() throws SQLException {

        ConnectionClass connection = new ConnectionClass();
        Connection con = connection.getConnection();

        String sqlAuthorsIdName = "SELECT id, name FROM authors";

        try (Statement stmt = con.createStatement()) {
            ResultSet rsAuthors = stmt.executeQuery(sqlAuthorsIdName);
            while (rsAuthors.next()) {
                authorsIdName.put(rsAuthors.getString("name"), rsAuthors.getInt("id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    private void createCategoryBoxOptions() throws SQLException {

        ConnectionClass connection = new ConnectionClass();
        Connection con = connection.getConnection();

        String sqlCategoriesIdName = "SELECT id, name FROM categories";

        try (Statement stmt = con.createStatement()) {
            ResultSet rsCategories = stmt.executeQuery(sqlCategoriesIdName);
            while (rsCategories.next()) {
                categoriesIdName.put(rsCategories.getString("name"), rsCategories.getInt("id"));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    public void addImage(ActionEvent event) {

        FileChooser fc = new FileChooser();
        cover = fc.showOpenDialog(null);

        if(cover != null) {
            imageText.setText(cover.getName());
        }
    }

    public void addFunctionalityToButtons() {

        addButton.setOnAction(event -> {

            String title, description;
            int authorId, categoryId;

            // reset error text

            errorText.setText("");

            // extract data

            title = titleField.getText();
            String selectedAuthor = authorBox.getValue();
            String selectedCategory = categoryBox.getValue();
            description = descriptionArea.getText();

            // check if valid

            if(isValid(title, selectedAuthor, selectedCategory, description, cover)) {

                authorId = authorsIdName.get(authorBox.getValue());
                categoryId = categoriesIdName.get(categoryBox.getValue());

                String sqlInsertBook = "INSERT INTO `books`(`title`, `author_id`, `category_id`, `description`) VALUES ('" + title + "','" + authorId + "','" + categoryId + "','" + description + "')";
                String sqlMaxIdBooks = "SELECT MAX(id) from `books`";

                // insert book into database

                ConnectionClass connection = new ConnectionClass();
                Connection con = connection.getConnection();

                try {
                    Statement statement = con.createStatement();
                    statement.executeUpdate(sqlInsertBook);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // add image to images folder

                try (Statement stmt = con.createStatement()) {
                    ResultSet rsNrBooks = stmt.executeQuery(sqlMaxIdBooks);

                    rsNrBooks.next();

                    int nrBooks = rsNrBooks.getInt("MAX(id)");

                    File destination = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\book" + nrBooks + ".png");

                    try (InputStream is = new FileInputStream(cover); OutputStream os = new FileOutputStream(destination)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = is.read(buffer)) > 0) {
                            os.write(buffer, 0, length);
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
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

    private boolean isValid(String title, String selectedAuthor, String selectedCategory, String description, File cover) {

        if (title.equals("")) {
            errorText.setText("Nu ai completat titlul");
            return false;
        }

        if (selectedAuthor == null) {
            errorText.setText("Nu ai selectat autorul");
            return false;
        }

        if (selectedCategory == null) {
            errorText.setText("Nu ai selectat categoria");
            return false;
        }

        if (description.equals("")) {
            errorText.setText("Nu ai completat descrierea");
            return false;
        }

        if(cover == null) {
            errorText.setText("Nu ai selectat imaginea");
            return false;
        }

        String path = cover.getAbsolutePath();
        String extension = path.substring(path.length() - 4);

        if(!(extension.equals(".jpg") || extension.equals("jpeg") || extension.equals(".png"))) {
            errorText.setText("Imaginea nu e de tip .jpg, .jpeg sau .png");
            return false;
        }

        return true;
    }
}
