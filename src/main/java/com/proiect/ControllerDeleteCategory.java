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

public class ControllerDeleteCategory  implements Initializable {

    private Stage stage;
//    private Scene scene;
//    private Parent root;

    @FXML
    ComboBox<String> categoryBox;

    @FXML
    Text errorText;

    /**
     * Delete a category from the database and exit the scene.
     * FXML element
     */
    @FXML
    Button deleteButton;
    /**
     * Exit the scene without deleting a category from the database.
     * FXML element
     */
    @FXML
    Button exitButton;

    Map<String, Integer> categoriesIdName = new HashMap<>();

    int nrCategories = 0;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            createCategoryBoxOptions();
            categoryBox.getItems().addAll(categoriesIdName.keySet());

        } catch (SQLException e) {
            e.printStackTrace();
        }

        addFunctionalityToButtons();
    }

    private void createCategoryBoxOptions() throws SQLException {

        ConnectionClass connection = new ConnectionClass();
        Connection con = connection.getConnection();

        String sqlCategoryIdName = "SELECT id, name FROM categories";

        try (Statement stmt = con.createStatement()) {
            ResultSet rsCategories = stmt.executeQuery(sqlCategoryIdName);
            while (rsCategories.next()) {
                categoriesIdName.put(rsCategories.getString("name"), rsCategories.getInt("id"));
                nrCategories++;
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

            //check if an category was selected

            String categoryName = "no";
            try {
                categoryName = categoryBox.getValue();
            }catch (Exception ignored) {

            }

            if(categoryName == null)
                errorText.setText("Nu ai selectat o categorie");
            else {

                //extract category id

                int categoryId = categoriesIdName.get(categoryBox.getValue());

                String sqlDeleteCategory = "DELETE FROM `categories` WHERE id = " + categoryId;
                String sqlFixACategoriesAutoIncrement = "ALTER TABLE `categories` AUTO_INCREMENT = " + nrCategories;
                String sqlFixCategoriesId = "UPDATE `categories` SET `id`=`id` - 1 WHERE id > " + categoryId;

//                String sqlDeleteAuthorBooks = "DELETE FROM `books` WHERE author_id = " + categoryId;
//                String sqlNrAuthorBooks = "SELECT COUNT(*) FROM `books` WHERE author_id = " + categoryId;
//                String sqlNrMaxBooks = "SELECT MAX(id) FROM `books`";
//                String sqlIdBooks = "SELECT id FROM `books` WHERE author_id = " + categoryId;
//                String sqlFixBooksAutoIncrement = "ALTER TABLE `books` AUTO_INCREMENT = ";
//                String sqlFixBooksId = "UPDATE `books` SET `id`=`id` - ";
//                String sqlFixBooksId ="SET  @num := 0; UPDATE `books` SET id = @num := (@num+1);";

                // delete image from images

                File cover = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\category" + categoryId + ".png");

                boolean isDeleted = cover.delete();

//                if (isDeleted)
//                    System.out.println("Image deleted successfully");
//                else
//                    System.out.println("Failed to delete the image");

                // in images: fix name of the categories in front of the deleted category

                for (int i = categoryId + 1; i <= nrCategories; i++) {
                    File categoryImage = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\category" + i + ".png");
                    File rename = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\category" + (i - 1) + ".png");

                    boolean isRenamed = categoryImage.renameTo(rename);

//                    if (isRenamed)
//                        System.out.println("Cover " + i + " updated to " + (i - 1));
//                    else
//                        System.out.println("Cover " + i + " failed to update");
                }

                // delete category from database
                // fix id problem (11, 12, x, 14, 15)
                // fix auto increment problem (next added book to have id - 1)

                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.getConnection();

                try(Statement stmt = connection.createStatement()) {
                    stmt.executeUpdate(sqlDeleteCategory);
                    stmt.executeUpdate(sqlFixCategoriesId);
                    stmt.executeUpdate(sqlFixACategoriesAutoIncrement);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // delete books - in progress

//
//                // delete book images from images
//                // TO DO
//
//                // in images: fix name of the books in front of the deleted book
//
//                int maxBooks = 0;
//
//                try(Statement stmt = connection.createStatement()) {
//                    ResultSet rsMaxBooks = stmt.executeQuery(sqlNrMaxBooks);
//
//                    rsMaxBooks.next();
//                    maxBooks = rsMaxBooks.getInt("MAX(id)");
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//                int id, contor = 0;
//
//                try(Statement stmt = connection.createStatement()) {
//                    ResultSet rsIdBooks = stmt.executeQuery(sqlIdBooks);
//
//                    for (int i = 1; i <= maxBooks; i++) {
//
//                        rsIdBooks.next();
//                        id = rsIdBooks.getInt("id");
//
//                        if(id == i) {
//
//                            contor++;
//                            rsIdBooks.next();
//                        }
//
//                        File bookImage = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\book" + i + ".png");
//                        File rename = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\book" + (i - contor) + ".png");
//
//                        if (bookImage.renameTo(rename))
//                            System.out.println("Cover " + i + " updated to " + (i - contor));
//                        else
//                            System.out.println("Cover " + i + " failed to update");
//                    }
//
//                } catch (SQLException e) {
//                    e.printStackTrace();
//                }
//
//                // delete category's books
//                // fix id problem for books (11, 12, x, 14, 15)
//                // fix auto increment problem for books (next added book to have id - 1)
//
//                try(Statement stmt = connection.createStatement()) {
//
//                    stmt.executeUpdate(sqlDeleteAuthorBooks);
//
//                    ResultSet rsNrBooks = stmt.executeQuery(sqlNrAuthorBooks);
//                    rsNrBooks.next();
////                    sqlFixBooksId += rsNrBooks.getInt("COUNT(*)") + " WHERE id > " + authorId;
////                    System.out.println(sqlFixBooksId);
//                   // stmt.executeUpdate(sqlFixBooksId);
//
//                    stmt.addBatch("SET  @num := 0");
//                    stmt.addBatch("UPDATE `books` SET id = @num := (@num+1)");
//                    stmt.executeBatch();
//
//                    stmt.executeUpdate(sqlFixBooksAutoIncrement + (nrAuthors - rsNrBooks.getInt("COUNT(*)")));
////                    System.out.println(sqlFixBooksAutoIncrement + (nrAuthors - rsNrBooks.getInt("COUNT(*)")));
//
//            } catch (SQLException e) {
//                e.printStackTrace();
//            }

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
