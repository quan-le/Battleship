package com.hcmiu.tsweeper;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.Timer;

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

public class MainController implements EventHandler<MouseEvent>
{

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button bt_Pause;

    @FXML
    private Button bt_Retry;

    @FXML
    private Button bt_Start;

    @FXML
    private ComboBox<?> cb_BOX_botSelection;

    @FXML
    private Label labelMines;

    @FXML
    private Label labelTime;

    @FXML
    private Pane pane_Main;

    @FXML
    private TextField textFieldMines;

    @FXML
    private TextField textFieldTimer;


    Minefield minefield = new Minefield();                        // minefield is the instance, ie... instance.function
    Boolean endGame = false;
    Boolean success = false;
    // Time
    Timer timer;
    double timeAtStart;                         // time since start in milli seconds
    double timeElapsed = 0;                     // ? usage
    boolean started;                            // check game started?
    int cellSize =30;                           //
    public String difficultyLevel ;             // set to Normal?

    @Override
    public void handle(MouseEvent event) {

    }
    void initialize()
    {
        System.out.println("Game Setup Begin ");
        //minefield = new Minefield();
        //difficultyBox();
        setTextFieldsandLabels();
        System.out.println("Game Setup End ");
        startGame();
    }

    public void startGame() {
        System.out.println("begin 'startGame' ");

        //pane_Main.getChildren().clear();
        minefield.makeMinefield();
        Pane pane_Main = new Pane();
        minefield.addMines();

        addMinefieldButtons();
        minefield.printMinefield();
        timer = new Timer();
        started = false;
        setTextFieldsandLabels();
        System.out.println("end game 'startGame' ");

        /* TODO
        minesLeft from makeBoard();
        numCellsUncovered ?
        timeElapsed setValue(0) and update;
        endGame boolean setValue(false);
        Won ? if cells uncovered == total cells - mines NOT if mine exploded
        */
        StartButton();
    }

    //@Override
    public void handle(ActionEvent event)
    {
        if (event.getSource() == bt_Pause){
            System.out.println("Pause Button: ");
        }
        if (event.getSource()== bt_Start ){
            System.out.println("Reset Button: ");
        }
    }

    void setTextFieldsandLabels(){
        textFieldTimer = new TextField();
        textFieldTimer.setText(" This is my new text. method = setTextFieldandLabels() ");
        labelTime = new Label();
        labelTime.setText("Time");
        textFieldMines = new TextField();
        textFieldMines.setText("This is my new text.");
        labelMines = new Label();
        labelMines.setText("MinesLeft");
    }


    int numMinesLeft() {
        return minefield.numMinesLeft;
    }

    void StartButton() {
        Button bt_Start = new Button();
        bt_Start.setText("Say 'Hello World'");
        bt_Start.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                System.out.println("Hello World! - Start Button");
            }
        });
        startOver();   // timer START
        // public void handle() { handle(); }
        // if (mouse.button == clicked )
    }


    // http://www.programcreek.com/java-api-examples/index.php?api=javafx.scene.control.ButtonBuilder
    // http://docs.oracle.com/javafx/2/layout/size_align.htm

    //**************************************
    public void addMinefieldButtons() {
        pane_Main.getChildren().clear();
        for (int x = 0; x < minefield.minefieldWidth; x++) {
            for (int y = 0; y < minefield.minefieldHeight; y++) {
                Button button = new Button();
                button.setLayoutX(x * cellSize);
                button.setLayoutY(y * cellSize);
                button.setPrefSize(cellSize, cellSize);
                button.setStyle("-fx-background-insets: 0,1,2");
                //Adding minefield button into pane
                pane_Main.getChildren().add(button);
                minefield.addButtonToCell(x,y,button);

                int finalX = x;
                int finalY = y;
                button.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) { //mouse have 2
                        minefield.mark(minefield.minefield[finalX][finalY]);
                    }
                    if (event.getButton() == MouseButton.PRIMARY) { //prx something
                        minefield.expose(finalX, finalY);
                    }
                    cellClicked();
                    updateTimer();
                });
            }
        }
    }

    public void cellClicked() {
        if (!started) {
            startOver();
            started = true;
        }
        System.out.println("Mines Left: "+ minefield.getNumMinesLeft());
        if (minefield.exploded) {
            endGame = true;
            success = false;
            pause();
        }
        if (minefield.numMinesLeft == 0) {
            endGame = true;
            success = true;
            pause();
        }
    }

    public void startOver() {
        timeAtStart = System.currentTimeMillis();
    }
    private void updateTimer() {
        if (!(minefield.exploded || (minefield.numMinesLeft == 0)))

            labelTime.setText("Time:  "+ textFieldTimer);
    }

    Integer timeCounter() {
        double timePassed = (System.currentTimeMillis() - timeAtStart)/1000.0;
        return (int) timePassed;        // set start = current clock time, start - current  = time elapsed
    }

    private void pause() {              // set pause button 1) if pressed 2) if mine triggered
        Button pause = new Button();
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
}
