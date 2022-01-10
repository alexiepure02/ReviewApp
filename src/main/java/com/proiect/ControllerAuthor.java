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
 * Controller class to manage the Author Menu.
 *
 * Includes:
 * - back button to the Authors Menu
 * - author's face
 * - name of the author
 * - description about the book
 * - average rating based on the ratings of his books
 * - list of books, each containing: cover, title, author, category name, average rating and details button (opens Book Menu)
 */
public class ControllerAuthor {

    /**
     * Stage, Scene and Root used to switch scenes.
     */
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Author's image.
     * FXML element
     */
    @FXML
    private ImageView imageAuthor;
    /**
     * Name of the author.
     * FXML element
     */
    @FXML
    private Text nameAuthor;
    /**
     * Average rating of the author based on his books' reviews.
     * FXML element
     */
    @FXML
    private Text averageRatingAuthor;
    /**
     * A small description about the author.
     * FXML element
     */
    @FXML
    private Text descriptionAuthor;
    /**
     * Grid pane for author's books.
     * FXML element
     */
    @FXML
    private GridPane booksGridPane;

    /**
     * Function to create and insert data about the author into the GUI
     * @param author                      selected author
     */
    public void createAuthor(Author author) {

        // create author's information

        try {
            imageAuthor.setImage(new Image(new FileInputStream("src/images/author" + author.getId() + ".png"), 150, 192, false, false));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        nameAuthor.setText(author.getName());
        descriptionAuthor.setText(author.getDescription());
        descriptionAuthor.setWrappingWidth(800);
        averageRatingAuthor.setText(String.valueOf(author.getAverageRating()) + "/5");

        // create books

        createBooksMenu(author.getBooks());
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
                controllerBook.createBook(books.get(finalI), "authors");               // it works only if 'i' is final
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
     * Function to switch the current scene to the Authors Menu scene
     * @param event         back button's event
     * @throws IOException  error thrown when the FXML doesn't exist
     */
    public void switchToAuthors(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("authors.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
