package com.hcmiu.tsweeper;

import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

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
    private StackPane stkPane_Main;

    @FXML
    void initialize() {

    }

}
