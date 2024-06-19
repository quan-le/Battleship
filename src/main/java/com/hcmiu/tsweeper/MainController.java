package com.hcmiu.tsweeper;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;
import javafx.application.Platform;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.beans.binding.Binding;
import javafx.beans.property.LongProperty;
import javafx.beans.property.SimpleLongProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.fxml.Initializable;
import java.net.URL;
import java.util.ResourceBundle;
import static javafx.beans.binding.Binding.*;

public class MainController implements EventHandler<MouseEvent> {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button bt_Start;

    @FXML
    private ComboBox<String> cb_BOX_botSelection;

    @FXML
    private Label labelMines;

    @FXML
    private Label labelTime;

    @FXML
    private Pane pane_Main;

    @FXML
    private Label labelMineLeft;

    @FXML
    private Text textTimeLeft;

    private static MainController instance;

    public MainController() {
        instance = this;
    }

    //Passion will take notices of these
    Minefield minefield = new Minefield();      // minefield is the instance, ie... instance.function
    Boolean endGame = false;                    // When u click the mine
    Boolean success = false;                    // Total Exposed Cells = Total Cells - Cells with Mines

    // Time
    double timeAtStart;                         // time since start in milli seconds
    double timeElapsed = 0;                     // ? usage
    boolean started;                            // check game started?
    int cellSize = 45;                           //
    int i = 0;
    public String BotChosen;

    @Override
    public void handle(MouseEvent event) {

    }

    @FXML
    void initialize() {
        instance = this;
        System.out.println("Game Setup Begin ");
        //minefield = new Minefield();
        setTextFieldsandLabels();
        System.out.println("Game Setup End ");
        //startGame();
    }

    public void startGame()
    {
        System.out.println("begin 'startGame' ");
        labelMineLeft.textProperty().set(minefield.numMinesLeft + " Mines");
        //pane_Main.getChildren().clear();
        minefield.makeMinefield();
        Pane pane_Main = new Pane();
        minefield.addMines();

        addMinefieldButtons();
        minefield.printMinefield();
        started = false;
        setTextFieldsandLabels();
        System.out.println("end game 'startGame' ");
        StartButton();

        //chosing bot
        System.out.println(cb_BOX_botSelection.getValue());
        if(cb_BOX_botSelection.getValue() == "Bot Quan Le")
        {
            QLBot();
        }
        if(cb_BOX_botSelection.getValue() == "Bot Anh Dung")
        {
            QLBot();
        }
        if(cb_BOX_botSelection.getValue() == "Bot Dinh Quang")
        {
            QLBot();
        }

    }

    //@Override
    public void handle(ActionEvent event) {
        if (event.getSource() == bt_Start) {
            System.out.println("Reset Button: ");
        }
    }

    void setTextFieldsandLabels() {
        //textFieldTimer.setText("time");
        labelTime = new Label();
        labelTime.setText("Time");
        //labelMineLeft = new Label();
        labelMineLeft.textProperty().set(minefield.numMinesLeft + " Mines");
        labelMines = new Label();
        labelMines.setText("MinesLeft");
        generateCBBoxOption();
    }
    public void generateCBBoxOption()
    {
        //ComboBox<String> cb_BOX_botSelection = new ComboBox<>();
        cb_BOX_botSelection.getItems().add("Bot Quan Le");
        cb_BOX_botSelection.getItems().add("Bot Anh Dung");
        cb_BOX_botSelection.getItems().add("Bot Dinh Quang");
        cb_BOX_botSelection.getItems().add("Manual Game");
        //return cb_BOX_botSelection.getValue();
    }
    @FXML
    public String getCBBoxOption()
    {
        BotChosen = cb_BOX_botSelection.getValue();
        return cb_BOX_botSelection.getValue();
    }
    @FXML
    int numMinesLeft() {
        labelMines.textProperty().set("Mines Left" + minefield.numMinesLeft);
        labelMineLeft.textProperty().set("Mines Left" + minefield.numMinesLeft);
        return minefield.numMinesLeft;
    }

    int surroundMine(int x, int y)
    {
        int surroundMine = minefield.neighborsMined(x,y);
        return surroundMine;
    }

    void StartButton() {
        Button bt_Start = new Button();
        bt_Start.setText("Say 'Hello World'");
        startTimer();

        bt_Start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Start Passion");
                while (!(minefield.exploded || (minefield.numMinesLeft == 0)))
                {
                    if (!minefield.exploded) {
                        labelMines.setText("Time:" + String.valueOf(minefield.getNumMinesLeft()));
                    }
                }

            }
        });
        // public void handle() { handle(); }
        // if (mouse.button == clicked )
    }

    //**************************************
    public void addMinefieldButtons() {
        pane_Main.getChildren().clear();
        for (int x = 0; x < minefield.minefieldWidth; x++) {
            for (int y = 0; y < minefield.minefieldHeight; y++) {
                Button button = new Button();
                button.setLayoutX(x * cellSize);                    //Passion
                button.setLayoutY(y * cellSize);
                button.setPrefSize(cellSize, cellSize);
                button.setStyle("-fx-background-insets: 0,1,2");
                //Adding minefield button into pane
                pane_Main.getChildren().add(button);
                minefield.addButtonToCell(x, y, button);

                int finalX = x;
                int finalY = y;
                button.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) { // mouse have 2
                        minefield.mark(minefield.minefield[finalX][finalY]);
                        // Update the mine left display
                        labelMineLeft.textProperty().set(minefield.numMinesLeft + " Mines");

                    }
                    if (event.getButton() == MouseButton.PRIMARY) { // primary button click
                        minefield.expose(finalX, finalY);
                    }
                    cellClicked();
                });
            }
        }
    }

    public void cellClicked() {
        if (!started) {
            started = true;
        }
        if (!endGame) {
            System.out.println("Mines Left: " + minefield.getNumMinesLeft());
            labelMineLeft.textProperty().set(minefield.numMinesLeft + " Mines");


        }
        if (minefield.exploded) {
            endGame = true;
            success = false;
            stop();
        }
        if (minefield.numMinesLeft == 0) {
            endGame = true;
            success = true;
            stop();
        }
    }

    private void stop() {                 // set pause button 1) if pressed 2) if mine triggered
        Button pause = new Button();      //timer stop when stop() is called using timeline.stop()
        for (int x = 0; x < minefield.minefieldWidth; x++) {
            for (int y = 0; y < minefield.minefieldHeight; y++) {
                Button button = minefield.minefield[x][y].button;
                if (minefield.exploded || minefield.minefield[x][y].mined)  //mine exploded
                    // Change this to only happen on specified button, all others expose or SHOW
                    button.setText("!");                                    // show remaining mines
                button.setOnMouseClicked(event -> {                         // no click
                    if (event.getButton() == MouseButton.SECONDARY) {
                        //do nothing
                    }
                    button.setDisable(true);  // should disable buttons
                });
            }
        }
    }

    //todoflash3ku: - finish the mineleft() thing
    //- make the endgame()
    //- assign the timer to stop when endgame() is called
    //- make the UI better?



    void startTimer()
    {
        textTimeLeft.setText(String.valueOf(i));
        Timeline timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            i++;
            textTimeLeft.setText(String.valueOf(i));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }
    public void QLBot()
    {
        minefield.expose(9,4);
        minefield.expose(9,5);

        //minefield.mark(minefield.minefield[0][1]);
    }

}



