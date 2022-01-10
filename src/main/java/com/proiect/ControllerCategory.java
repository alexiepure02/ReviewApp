package com.proiect;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

/**
 * Controller class to manage the Category Menu.
 *
 * Includes:
 * - back button to the Categories Menu
 * - descriptive image for the category
 * - name of the category
 * - description about the category
 * - average rating based on the ratings of its books
 * - list of books, each containing: cover, title, author, category name, average rating and details button (opens Book Menu)
 */
public class ControllerCategory {

    /**
     * Stage, Scene and Root used to switch scenes.
     */
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Image of the category.
     * FXML element
     */
    @FXML
    private ImageView imageCategory;
    /**
     * Name of the category.
     * FXML element
     */
    @FXML
    private Text nameCategory;
    /**
     * Average rating of the category based on its books' reviews.
     * FXML element
     */
    @FXML
    private Text averageRatingCategory;
    /**
     * A small description about the category
     * FXML element
     */
    @FXML
    private Text descriptionCategory;
    /**
     * Grid pane for category's books.
     * FXML element
     */
    @FXML
    private GridPane booksGridPane;

    /**
     * Function to create and insert data about the category into the GUI
     * @param category                      selected category
     */
    public void createCategory(Category category) {

        // create category's information

        try {
            imageCategory.setImage(new Image(new FileInputStream("src/images/category" + category.getId() + ".png"), 150, 192, false, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        nameCategory.setText(category.getName());
        averageRatingCategory.setText(String.valueOf(category.getAverageRating()) + "/5");
        descriptionCategory.setText(category.getDescription());
        descriptionCategory.setWrappingWidth(800);

        // create books

        createBooksMenu(category.getBooks());
    }

    /**
     * Function to insert books into the grid pane.
     * @param books list of books needed to be put into the grid pane
     */
    public void createBooksMenu(List<Book> books) {

        int n = books.size() / 3, restn = books.size() % 3;

        // create grid cells

        for (int i = 0; i < 3; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 3);
            booksGridPane.getColumnConstraints().add(colConst);
        }

        for (int i = 0; i < n; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / n);
            booksGridPane.getRowConstraints().add(rowConst);
        }

        if (restn != 0) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / n);
            booksGridPane.getRowConstraints().add(rowConst);
        }

        // create books in cells

        for (int i = 0; i < 3 * n + restn; i++) {

            // cover

            Image cover = null;
            try {
                cover = new Image(new FileInputStream("src/images/book" + books.get(i).getId() + ".png"), 130, 192, false, false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // title

            Text title = new Text(books.get(i).getTitle());
            title.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
            title.setWrappingWidth(220);

            // author

            Text author = new Text(books.get(i).getAuthor());
            author.setFont(Font.font(20));
            author.setWrappingWidth(220);

            // average rating

            Text averageRating = new Text(String.valueOf(books.get(i).getAverageRating()) + "/5");
            averageRating.setFont(Font.font(16));

            // details button

            Button button = new Button("Detalii..");
            int finalI = i;
            button.setOnAction(event -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("book.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                ControllerBook controllerBook = loader.getController();
                controllerBook.createBook(books.get(finalI), "categories");               // it works only if 'i' is final
            });

            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10, 10, 10, 10));
            vbox.getChildren().addAll(title, author, averageRating, button);
            VBox.setMargin(button, new Insets(70, 0, 0, 0));

            HBox hbox = new HBox();
            hbox.setPadding(new Insets(10, 10, 10, 10));
            hbox.setAlignment(Pos.TOP_LEFT);
            hbox.getChildren().addAll(new ImageView(cover), vbox);

            booksGridPane.add(hbox, i % 3, i / 3, 1, 1);

        }
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
