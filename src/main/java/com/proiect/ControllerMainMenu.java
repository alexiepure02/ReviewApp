package com.proiect;

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
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

/**
 * Controller class to manage the Main Menu.
 *
 * Includes:
 * - title of the program
 * - button to access the Books Menu
 * - button to access the Authors Menu
 * - button to access the Categories Menu
 */
public class ControllerMainMenu {

    private Stage stage;
    private Scene scene;
    private Parent root;

    public void switchToBooks(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("books.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToAuthors(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("authors.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void switchToCategories(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("categories.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
    }

    public void openAdmin(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("admin.fxml"));
        stage = new Stage();
        stage.initModality(Modality.WINDOW_MODAL);
        Stage primaryStage = (Stage)((Node) event.getSource()).getScene().getWindow();
        stage.initOwner(primaryStage);
        scene = new Scene(root);
        stage.setScene(scene);
        stage.setTitle("Admin");
        stage.showAndWait();
    }
}