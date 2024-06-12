package com.hcmiu.tsweeper;

import javafx.fxml.FXML;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class HelloController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private GridPane GirdPane_Board;

    @FXML
    private Button bt_Retry;

    @FXML
    private Button bt_Start;

    @FXML
    private ComboBox<?> cb_BOX_botSelection;

    @FXML
    private AnchorPane main_AnchorPane;

    @FXML
    private Label welcomeText;

    @FXML
    void onHelloButtonClick(ActionEvent event)
    {

    }

    @FXML
    void initialize() {

    }

}
