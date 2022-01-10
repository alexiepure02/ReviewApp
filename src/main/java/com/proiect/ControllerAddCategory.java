package com.proiect;

import connectivity.ConnectionClass;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
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
import java.util.ResourceBundle;

public class ControllerAddCategory  implements Initializable {

    private Stage stage;
    private Scene scene;
    private Parent root;

    @FXML
    TextField nameField;

    @FXML
    TextArea descriptionArea;

    @FXML
    Text imageText;

    @FXML
    Text errorText;

    /**
     * Add the category to the database and exit the scene.
     * FXML element
     */
    @FXML
    Button addButton;
    /**
     * Exit the scene without adding the category to the database.
     * FXML element
     */
    @FXML
    Button exitButton;

    File image;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addFunctionalityToButtons();
    }

    public void addImage(ActionEvent event) {
        FileChooser fc = new FileChooser();
        image = fc.showOpenDialog(null);

        if(image != null) {
            imageText.setText(image.getName());
        }
    }

    public void addFunctionalityToButtons() {

        addButton.setOnAction(event -> {

            String name, description;

            // reset error text

            errorText.setText("");

            // extract data

            name = nameField.getText();
            description = descriptionArea.getText();

            // check if valid

            if(isValid(name, description, image)) {

                String sqlInsertCategory = "INSERT INTO `categories`(`name`, `description`) VALUES ('" + name + "','" +  description + "')";
                String sqlMaxIdCategories = "SELECT MAX(id) from `categories`";

                // insert category into database

                ConnectionClass connection = new ConnectionClass();
                Connection con = connection.getConnection();

                try {
                    Statement statement = con.createStatement();
                    statement.executeUpdate(sqlInsertCategory);
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                // add image to images folder

                try (Statement stmt = con.createStatement()) {
                    ResultSet rsNrCategories = stmt.executeQuery(sqlMaxIdCategories);

                    rsNrCategories.next();

                    int nrCategories = rsNrCategories.getInt("MAX(id)");

                    File destination = new File("D:\\Program Files\\facultate\\an2\\P3\\proiect_fx\\src\\images\\category" + nrCategories + ".png");

                    try (InputStream is = new FileInputStream(image); OutputStream os = new FileOutputStream(destination)) {
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

    private boolean isValid(String name, String description, File cover) {

        if (name.equals("")) {
            errorText.setText("Nu ai completat numele");
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

