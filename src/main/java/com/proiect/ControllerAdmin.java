package com.proiect;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ControllerAdmin {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    Button addBookButton;

    @FXML
    Button addAuthorButton;

    @FXML
    Button addCategoryButton;

    @FXML
    Button deleteReviewButton;

    @FXML
    Button deleteBookButton;

    @FXML
    Button deleteAuthorButton;

    @FXML
    Button deleteCategoryButton;

    @FXML
    TextField passwordField;

    @FXML
    Button passwordButton;

    @FXML
    Text errorText;

    public void login(ActionEvent event) {

        errorText.setText("");

        String password = passwordField.getText();
        if(password.equals("1234")) {
            addBookButton.setDisable(false);
            addAuthorButton.setDisable(false);
            addCategoryButton.setDisable(false);
            deleteReviewButton.setDisable(false);
            deleteReviewButton.setOpacity(1);
            deleteBookButton.setDisable(false);
            deleteAuthorButton.setDisable(false);
            deleteCategoryButton.setDisable(false);

            passwordField.setDisable(true);
            passwordButton.setDisable(true);
            passwordField.setOpacity(0);
            passwordButton.setOpacity(0);
        }
        else
            errorText.setText("Parola incorecta");
    }

    public void switchToAddBook(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("addBook.fxml"));
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(primaryStage);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAddAuthor(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("addAuthor.fxml"));
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(primaryStage);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAddCategory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("addCategory.fxml"));
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(primaryStage);        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDeleteReview(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("deleteReview.fxml"));
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(primaryStage);        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDeleteBook(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("deleteBook.fxml"));
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(primaryStage);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDeleteAuthor(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("deleteAuthor.fxml"));
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(primaryStage);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToDeleteCategory(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("deleteCategory.fxml"));
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(primaryStage);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }
}
