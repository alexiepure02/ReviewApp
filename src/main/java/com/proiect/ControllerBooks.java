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
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.ResourceBundle;

/**
 * Controller class to manage the Books Menu.
 *
 * Includes:
 * - back button to the Main Menu
 * - an empty grid pane that gets filled with books using code
 * - a book has: cover, title, author, category name, average rating and details button (opens Book Menu)
 */
public class ControllerBooks implements Initializable {

    /**
     * Stage, Scene and Root used to switch scenes.
     */
    private Stage stage;
    private Scene scene;
    private Parent root;

    /**
     * Grid Pane where books are shown.
     * FXML element
     */
    @FXML
    private GridPane booksGridPane;

    /**
     * List object where books are saved from the database.
     */
    public ObservableList<Book> books;

    /**
     * Function runs before the FXML of the controller has been loaded.
     * @param url               unused
     * @param resourceBundle    unused
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        ConnectionClass connection = new ConnectionClass();
        Connection con = connection.getConnection();

        try {
            books = getAllRecords(con);
        }
        catch (SQLException e) {
            e.printStackTrace();
        }

        createBooksMenu(books);
    }

    /**
     * Function to extract data regarding books from the database and returns a list of books.
     * @param con               connection to the database
     * @return                  list of books
     * @throws SQLException     database not found
     */
    public static ObservableList<Book> getAllRecords(Connection con) throws SQLException {

        String sqlBooks = "SELECT * FROM `books`";
        String sqlAuthor = "SELECT * FROM `authors` WHERE `id` = ";
        String sqlCategory = "SELECT * FROM `categories` WHERE `id` = ";
        String sqlReviews = "SELECT * FROM `reviews` WHERE `book_id` = ";

        // books

        try(Statement stmt = con.createStatement()) {
            ResultSet rsBooks = stmt.executeQuery(sqlBooks);
            ObservableList<Book> booksList = FXCollections.observableArrayList();

                while(rsBooks.next()) {

                    Book b = new Book();
                    b.setId(rsBooks.getInt("id"));
                    b.setTitle(rsBooks.getString("title"));

                    try (Statement stmt1 = con.createStatement()) {
                        String auxSqlAuthor = sqlAuthor + String.valueOf(rsBooks.getInt("author_id"));
                        ResultSet rsAuthor = stmt1.executeQuery(auxSqlAuthor);
                        rsAuthor.next();
                        b.setAuthor(rsAuthor.getString("name"));

                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        throw throwables;
                    }

                    try (Statement stmt1 = con.createStatement()) {
                        ResultSet rsCategory = stmt1.executeQuery(sqlCategory + rsBooks.getInt("category_id"));
                        rsCategory.next();
                        b.setCategory(rsCategory.getString("name"));

                    } catch (SQLException throwables) {
                        System.out.println("aaa");
                        throwables.printStackTrace();
                        throw throwables;
                    }

                    b.setDescription(rsBooks.getString("description"));

                    // book's reviews

                    String auxSqlReviews = sqlReviews;
                    try (Statement stmt1 = con.createStatement()) {
                        auxSqlReviews += b.getId();
                        ResultSet rsReviews = stmt1.executeQuery(auxSqlReviews);
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

//            for(Book book : booksList)
//                System.out.println(book);

            return booksList;
        }
        catch (SQLException throwables) {
            throwables.printStackTrace();
            throw throwables;
        }
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

        for(int i = 0; i < 3 * n + restn; i++) {

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
                controllerBook.createBook(books.get(finalI), "books");               // it works only if 'i' is final
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
     * Function to add a review to a certain book.
     * @param review    review to be added
     * @param id        id of the book where the review needs to be added
     */
    public void addReviewToBook(Review review, int id) {
        for(Book b : books) {
            if(b.getId() == id) {

                // add in database

                ConnectionClass connectionClass = new ConnectionClass();
                Connection connection = connectionClass.getConnection();

                String sql = "INSERT INTO `reviews`(`book_id`, `name`, `rating`, `review`) VALUES ('" + id + "', '" + review.getName() + "', '" + review.getRating() + "', '" + review.getReview() + "')";

                try {
                    Statement statement = connection.createStatement();
                    statement.executeUpdate(sql);
                }catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * Function to switch the current scene to the Main Menu scene
     * @param event         back button's event
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
