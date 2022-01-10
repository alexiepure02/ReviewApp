package com.proiect;

import connectivity.ConnectionClass;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
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
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class to manage the Authors Menu.
 *
 * Includes:
 * - back button to the Main Menu
 * - an empty grid pane that gets filled with authors using code
 * - an author has: image, name, some of his book titles, average rating based on his books and details button (opens Author Menu)
 */
public class ControllerAuthors implements Initializable {

    /**
     * Stage, Scene and Root used to switch scenes.
     */
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Grid Pane where authors are shown.
     */
    @FXML
    private GridPane authorsGridPane;

    /**
     * List object where books are saved from the database.
     */
    public ObservableList<Author> authors;

    /**
     * Function that runs before the FXML of the controller is being loaded.
     * @param url               unused
     * @param resourceBundle    unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ConnectionClass connection = new ConnectionClass();
        Connection con = connection.getConnection();

        try {
            authors = getAllRecords(con);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        createAuthorsMenu(authors);
    }

    /**
     * Function that extracts data regarding authors from the database and returns a list of authors.
     * @param con               connection to the database
     * @return                  list of authors
     * @throws SQLException     database not found
     */
    public static ObservableList<Author> getAllRecords(Connection con) throws SQLException {

        String sqlAuthors = "SELECT * FROM `authors`";
        String sqlBooks = "SELECT * FROM `books` WHERE `author_id` = ";
        String sqlCategory = "SELECT * FROM `categories` WHERE `id` = ";
        String sqlReviews = "SELECT * FROM `reviews` WHERE `book_id` = ";

        // authors

        try (Statement stmt = con.createStatement()) {
            ResultSet rsAuthors = stmt.executeQuery(sqlAuthors);
            ObservableList<Author> authorsList = FXCollections.observableArrayList();

            while (rsAuthors.next()) {
                Author a = new Author();
                a.setId(rsAuthors.getInt("id"));
                a.setName(rsAuthors.getString("name"));
                a.setDescription(rsAuthors.getString("description"));

                // author's books

                String auxSqlBooks = sqlBooks;
                try (Statement stmt1 = con.createStatement()) {
                    auxSqlBooks += a.getId();
                    ResultSet rsBooks = stmt1.executeQuery(auxSqlBooks);
                    ObservableList<Book> booksList = FXCollections.observableArrayList();

                    while (rsBooks.next()) {
                        Book b = new Book();
                        b.setId(rsBooks.getInt("id"));
                        b.setTitle(rsBooks.getString("title"));
                        b.setAuthor(a.getName());

                        try (Statement stmt2 = con.createStatement()) {
                            ResultSet rsCategory = stmt2.executeQuery(sqlCategory + String.valueOf(rsBooks.getInt("category_id")));
                            rsCategory.next();
                            b.setCategory(rsCategory.getString("name"));
                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            throw throwables;
                        }

                        b.setDescription(rsBooks.getString("description"));

                        // book's reviews

                        String auxSqlReviews = sqlReviews;
                        try (Statement stmt2 = con.createStatement()) {
                            auxSqlReviews += b.getId();
                            ResultSet rsReviews = stmt2.executeQuery(auxSqlReviews);
                            ObservableList<Review> reviewsList = FXCollections.observableArrayList();

                            while (rsReviews.next()) {
                                Review r = new Review();
                                r.setName(rsReviews.getString("name"));
                                r.setRating(rsReviews.getInt("rating"));
                                r.setReview(rsReviews.getString("review"));
                                reviewsList.add(r);
                            }
                            b.setReviews(reviewsList);

                        } catch (SQLException throwables) {
                            throwables.printStackTrace();
                            throw throwables;
                        }

                        // average rating for each book

                        double sumRatings = 0;
                        for (Review review : b.getReviews()) {
                            sumRatings += review.getRating();
                        }
                        b.setAverageRating((double) Math.round(sumRatings / b.getReviews().size() * 100) / 100);

                        booksList.add(b);
                    }

                    a.setBooks(booksList);

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    throw throwables;
                }

                // average rating for all books

                double sumAvgRatings = 0;
                for (Book book : a.getBooks()) {
                    sumAvgRatings += book.getAverageRating();
                }
                a.setAverageRating((double) Math.round(sumAvgRatings / a.getBooks().size() * 100) / 100);

                authorsList.add(a);
            }

//            for(Author author : authorsList)
//                System.out.println(author);

            return authorsList;

        } catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
    }

    /**
     * Function that creates the grid pane inserts the authors in the GUI.
     * @param authors list of authors needed to be put into the grid pane
     */
    private void createAuthorsMenu(List<Author> authors) {

        int n = authors.size() / 3, restn = authors.size() % 3;

        // create grid cells

        for(int i = 0; i < 3; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / 3);
            authorsGridPane.getColumnConstraints().add(colConst);
        }

        for(int i = 0; i < n; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / n);
            authorsGridPane.getRowConstraints().add(rowConst);
        }

        if(restn != 0) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / n);
            authorsGridPane.getRowConstraints().add(rowConst);
        }

        // create authors in cells

        for(int i = 0; i < 3 * n + restn; i++) {

            // image

            Image cover = null;
            try {
                cover = new Image(new FileInputStream("src/images/author" + authors.get(i).getId() + ".png"), 150, 192, false, false);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

            // name

            Text name = new Text(authors.get(i).getName());
            name.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
            name.setWrappingWidth(200);

            // some book titles

            StringBuilder titles = new StringBuilder();
            for (int b = 0; b < 3 && b < authors.get(i).getBooks().size(); b++) {
                titles.append(authors.get(i).getBooks().get(b).getTitle()).append(", ");
            }
            Text bookTitles = new Text();
            if(titles.length() == 0)
                bookTitles.setText("A scris:\n...");
            else
                bookTitles.setText("A scris:\n" + titles.substring(0, titles.length() - 2) + "...");
            bookTitles.setFont(Font.font(15));
            bookTitles.setWrappingWidth(200);

            // average rating

            Text averageRating = new Text(authors.get(i).getAverageRating() + "/5");
            averageRating.setFont(Font.font(16));

            // details button

            Button button = new Button("Detalii..");

            int finalI = i;
            button.setOnAction(event -> {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("author.fxml"));
                try {
                    root = loader.load();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
                scene = new Scene(root);
                stage.setScene(scene);
                stage.show();

                ControllerAuthor controllerAuthor = loader.getController();
                controllerAuthor.createAuthor(authors.get(finalI));               // it works only if 'i' is final
            });

            VBox vbox = new VBox();
            vbox.setPadding(new Insets(10, 10, 10, 10));
            vbox.getChildren().addAll(name, bookTitles, averageRating, button);
            VBox.setMargin(button, new Insets(70, 0, 0, 0));

            HBox hbox = new HBox();
            hbox.setPadding(new Insets(10, 10, 10, 10));
            hbox.setAlignment(Pos.TOP_LEFT);

            hbox.getChildren().addAll(new ImageView(cover), vbox);

            authorsGridPane.add(hbox, i % 3, i / 3, 1, 1);
        }
    }

    /**
     * Function to switch the current scene to the Main Menu scene
     * @param event         button's event
     * @throws IOException  error thrown when the FXML doesn't exist
     */
    public void switchToMenu(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("menu.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
