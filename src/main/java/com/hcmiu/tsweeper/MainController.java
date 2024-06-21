package com.hcmiu.tsweeper;

import java.net.URL;
import java.util.ResourceBundle;

import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.util.Duration;
import javafx.scene.text.Text;
import javafx.scene.layout.AnchorPane;
import javafx.scene.image.ImageView;

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
    private AnchorPane leftPane;

    @FXML
    private AnchorPane winPane;

    @FXML
    private ImageView winMenu;

    @FXML
    private Label labelMineLeft;

    @FXML
    private Text textTimeLeft;

    @FXML
    private Text textWinMenu;

    @FXML
    private Text text_RecommendCell;

    // Instance variables for game management
    Minefield minefield = new Minefield();
    QLBot bot;
    ADBot botAd;
    boolean endGame = false;
    boolean success = false;
    private boolean isQLbotActive;

    // Time
    Timeline timeline;
    boolean started;
    int cellSize = 45;
    int i = 0;

    @Override
    public void handle(MouseEvent event) {
        // Handle mouse events if needed
    }

    @FXML
    void initialize() {
        setTextFieldsandLabels();
        generateCBBoxOption();
        winPane.setVisible(false);
        textWinMenu.setVisible(false);
        text_RecommendCell.setVisible(false);
    }

    public void handleBotLogic(){
        if(isQLbotActive && bot != null) {
            bot.QLAlgo();
            bot.updateProbability();
        }
    }

    public void setQLbotActive(boolean isActive){
        isQLbotActive = isActive;
    }

    public void startGame() {
        labelMineLeft.textProperty().set(minefield.numMinesLeft + " Mines");
        minefield.makeMinefield();
        minefield.addMines();

        addMinefieldButtons();
        started = false;
        setTextFieldsandLabels();
        StartButton();

        String selectedBot = cb_BOX_botSelection.getValue();
        if ("Bot Quan Le".equals(selectedBot)) {
            bot = new QLBot(minefield);
            botAd = null;
            setQLbotActive(true);
            text_RecommendCell.setVisible(true);
            QuanLeBot();
        } else if ("Bot Anh Dung".equals(selectedBot)) {
            bot = null;
            botAd = new ADBot(minefield);
            setQLbotActive(false);
            ADBot();
        } else if ("Bot Dinh Quang".equals(selectedBot)) {
            // Handle Bot Dinh Quang logic here
        }else{
            bot = null;
            botAd = null;
            setQLbotActive(false);
        }
    }

    void setTextFieldsandLabels() {
        labelTime.setText("Time");
        labelMineLeft.textProperty().set(minefield.numMinesLeft + " Mines");
        labelMines.setText("Mines Left");
    }

    public void generateCBBoxOption() {
        cb_BOX_botSelection.getItems().addAll("Bot Quan Le", "Bot Anh Dung", "Bot Dinh Quang", "Manual Game");
    }

    @FXML
    public String getCBBoxOption() {
        return cb_BOX_botSelection.getValue();
    }

    @FXML
    int numMinesLeft() {
        labelMines.textProperty().set("Mines Left" + minefield.numMinesLeft);
        labelMineLeft.textProperty().set("Mines Left" + minefield.numMinesLeft);
        return minefield.numMinesLeft;
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

    void addMinefieldButtons() {
        pane_Main.getChildren().clear();
        for (int x = 0; x < minefield.minefieldWidth; x++) {
            for (int y = 0; y < minefield.minefieldHeight; y++) {
                Button button = new Button();
                button.setLayoutX(x * cellSize);
                button.setLayoutY(y * cellSize);
                button.setPrefSize(cellSize, cellSize);
                button.setStyle("-fx-background-insets: 0,1,2");

                pane_Main.getChildren().add(button);
                minefield.addButtonToCell(x, y, button);

                int finalX = x;
                int finalY = y;
                button.setOnMouseClicked(event -> {
                    if (event.getButton() == MouseButton.SECONDARY) {
                        minefield.mark(minefield.minefield[finalX][finalY]);
                        labelMineLeft.textProperty().set(minefield.numMinesLeft + " Mines");
                        handleBotLogic();
                    } else if (event.getButton() == MouseButton.PRIMARY) {
                        minefield.expose(finalX, finalY);
                        handleBotLogic();
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

    private void stop() {
        for (int x = 0; x < minefield.minefieldWidth; x++) {
            for (int y = 0; y < minefield.minefieldHeight; y++) {
                Button button = minefield.minefield[x][y].button;
                if (minefield.exploded || minefield.minefield[x][y].mined) {
                    button.setText("!");
                    stopTimer();
                    winPane.setVisible(true);
                    leftPane.setVisible(false);
                    pane_Main.setVisible(false);
                } else if (minefield.numMinesLeft == 0) {
                    stopTimer();
                    textWinMenu.setVisible(true);
                    winPane.setVisible(false);
                    leftPane.setVisible(true);
                    pane_Main.setVisible(true);
                }
            }
        }
    }

    void startTimer() {
        timeline = new Timeline(new KeyFrame(Duration.seconds(1), e -> {
            i++;
            textTimeLeft.setText(String.valueOf(i));
        }));
        timeline.setCycleCount(Animation.INDEFINITE);
        timeline.play();
    }

    void stopTimer() {
        if (timeline != null) {
            timeline.stop();
        }
    }

    public void QuanLeBot() {
        bot.QLAlgo();
        bot.expose(0, 5);
        String recommend = bot.smallestProbability();
        text_RecommendCell.textProperty().set(recommend);
    }

    public void ADBot() {
        botAd.ADAlgo();
        botAd.expose(0, 5);
    }

    public void handle(ActionEvent event) {
        if (event.getSource() == bt_Start) {
            System.out.println("Reset Button: ");
        }
    }
}




