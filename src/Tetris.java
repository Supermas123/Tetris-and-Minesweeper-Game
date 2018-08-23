

import java.io.*;
import java.util.*;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.effect.DropShadow;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

/**
 * This is the Tetris class that allows for the tetris game
 * to start and be played.
 *
 * @author Samuel Mathew <skm77482@uga.edu>, Shreyas Sundararajaran
 * @since 2018-05-03
 */


public class Tetris {

    //declares all necessary instance variables

    private static final int WIDTH = 700;
    private static final int HEIGHT = 800;
    private static Button playButton;
    private static Button instructionsButton;
    private static StackPane tetrisRoot;
    private static Scene scene;
    private static int counter;
    private static Color actualColor;
    private static GridPane tetrisScreen;
    private Timeline timeline;
    private int stopTime;
    private boolean moveDown;
    private int currentShape;
    private int nextShape;
    private GridPane showNextBlock;
    private int[][] nextGridInt;
    private Rectangle[][] nextGridRec;
    private Color nextColor;
    private int linesNum;
    private Label linesLabel;
    private Label scoreLabel;
    private Label levelLabel;
    private Label nextLabel;
    private double score;
    private int level;
    private boolean isGameDone;
    private VBox sideMenu;
    private Button pause;
    private Button instructions;
    private Button returnMenu;
    private int flipperHor;
    private int flipperVert;
    private int lineFlipperHor;
    private int lineFlipperVert;
    private int numberOfLines;
    private double newTime;
    private boolean isGamePaused;
    private Button screenExit;

    /**
     * This is the constructor of the class
     * Initializes all variables when game first begins
     */

    public Tetris() {

        //initializes all necessary instance variables
        playButton = new Button("PLAY");
        instructionsButton = new Button("Instructions");
        screenExit = new Button("Exit");
        tetrisRoot = new StackPane();
        scene = new Scene(tetrisRoot, WIDTH, HEIGHT);
        tetrisScreen = new GridPane();
        stopTime = 0;
        currentShape = 0;
        nextShape = createRandomShape();
        showNextBlock = new GridPane();
        nextGridInt = new int[4][4];
        nextGridRec = new Rectangle[4][4];
        linesNum = 0;
        score = 0;
        level = 1;
        numberOfLines = 0;
        linesLabel = new Label("Lines: 0");
        scoreLabel = new Label("Score: " + 0);
        levelLabel = new Label("Level: " + 1);
        nextLabel = new Label("Next: ");
        pause = new Button("Pause");
        instructions = new Button("How to Play");
        returnMenu = new Button("Return To Main Menu");


    } //Tetris

    /**
     * Displays the initial screen with play, instructions, and exit buttons.
     * Also shows the high score table
     */

    public void display() {
        Stage stage = new Stage(); //creates new stage
        stage.initModality(Modality.APPLICATION_MODAL); //sets modality to Application Modal
        tetrisRoot = createScene();


        playButton.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND)); //sets mouse effect for the play button
        playButton.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));
        instructionsButton.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND)); //sets mouse effect for the instructions button
        instructionsButton.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));
        screenExit.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND)); //sets mouse effect for exit button
        screenExit.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));

        stage.setScene(scene); //sets the scene to stage
        scene.getStylesheets().add(this.getClass().getResource("tetrisStyle.css").toExternalForm()); //retrieves the css file and applies the necessary styles
        stage.setTitle("Tetris"); //sets the title of stage
        stage.setMinHeight(HEIGHT); //sets min height
        stage.setMinWidth(WIDTH); //sets min width
        stage.setResizable(true); //allows user to resize stage
        stage.sizeToScene(); //sizes stage to scene
        stage.show(); //shows stage
    } //display

    /**
     * This creates the StackPane in which the initial screen will be placed in
     *
     * @return StackPane  This is the Stack Pane in which the initial window is displayed in
     */

    private StackPane createScene() {

        VBox main = new VBox(); //initializes main Vbox
        DropShadow shadow = new DropShadow(); //initializes the shadow variable
        VBox highScoreTable = new VBox(); //initializes VBox for high score table
        Text highScoreTitle = new Text("High Scores:"); //initializes the high score table title
        highScoreTitle.setStyle("-fx-font: 80 phosphate"); //sets the style of highscore title
        highScoreTable.getChildren().add(highScoreTitle); //adds high score title to VBox
        highScoreTitle.setFill(Color.CRIMSON); //sets color of high score table title
        highScoreTable.setStyle("-fx-border-style: solid inside;" +
                "-fx-border-width: 2;" +
                "-fx-border-insets: 5;" +
                "-fx-border-radius: 5;" +
                "-fx-border-color: gold;"); //creates border around VBox


        int rank = 1; //initializes the rank variable

        try {
            File highScoreFile = new File("HighScoreTable.txt"); //gets the HighScoreTable.txt file
            FileReader fr = new FileReader(highScoreFile); //creates FileReader
            BufferedReader br = new BufferedReader((fr)); //creates BufferedReader
            ArrayList<Integer> scores = new ArrayList<>(0); //creates score arrayList
            ArrayList<Integer> maxScores = new ArrayList<>(0); //creates maxscore arrayList

            for (String line; (line = br.readLine()) != null; ) { //this loop adds each score to the scores arrayList
                int newInt = (int) Double.parseDouble(line);
                scores.add(newInt);
            }

            scores.sort(Integer::compare);

            for (int i = 0; i < 5; i++) { //this loop adds the top 5 scores to the max score arrayList
                if (scores.size() != 0) {
                    int max = scores.stream().reduce(Integer.MIN_VALUE, Integer::max);
                    scores.remove(scores.size() - 1);
                    maxScores.add(max);
                }
            }


            for (Integer maxScore : maxScores) { //this for loop adds each score to the screen so the user can see it
                Text score1 = new Text("" + rank + ". " + maxScore);
                score1.setStyle("-fx-font: 30 courier");
                score1.setFont(Font.font("Courier", FontWeight.BOLD, 30));
                highScoreTable.getChildren().add(score1);
                rank++;
            }

            highScoreTable.setSpacing(10); //sets spacing of highScoreTable
            highScoreTable.setAlignment(Pos.CENTER); //sets alignment of highScoreTable
            fr.close(); //closes fileReader
            br.close(); //closes bufferedReader

        } catch (Exception e) { //catches any exceptions
            e.getMessage();
            e.printStackTrace();
            System.out.println("error");
        }


        tetrisRoot.setStyle("-fx-background-color:lightblue;"); //sets style for main VBox
        playButton.setId("play-button"); //sets id for playbutton so css can be applied
        instructionsButton.setId("instructions-button"); //sets id for instructions button so css can be applied
        screenExit.setId("instructions-button"); //sets id for exit button so css can be applied

        main.setAlignment(Pos.CENTER); //sets alignment for main VBox

        playButton.setMinWidth(200);
        playButton.setMinHeight(100);
        instructionsButton.setMinHeight(50);    //this section sets the sizes of each of the buttons
        instructionsButton.setMinWidth(50);
        screenExit.setMinHeight(50);
        screenExit.setMinWidth(50);

        main.getChildren().addAll(playButton, instructionsButton, screenExit, highScoreTable); //adds all buttons to the main VBox
        main.setSpacing(10); //sets spacing
        tetrisRoot.getChildren().add(main); //adds VBox to main StackPane

        playButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> playButton.setEffect(shadow));

        playButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> playButton.setEffect(null));

        instructionsButton.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> instructionsButton.setEffect(shadow));          //this section of code adds mouse effects to each of the buttons

        instructionsButton.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> instructionsButton.setEffect(null));

        screenExit.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> screenExit.setEffect(shadow));

        screenExit.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> screenExit.setEffect(null));

        playButton.setOnMouseClicked(event -> {
                    tetrisRoot.getChildren().clear();
                    startGame();
                }
        );

        instructionsButton.setOnMouseClicked(event -> printInstructionsPage()); //if the instructions button is clicked the print instructions page is called

        screenExit.setOnMouseClicked(event -> {
            Stage stage = (Stage) screenExit.getScene().getWindow(); //if the exit button is pressed then the screen exits and goes to main display
            stage.close();
        });

        return tetrisRoot; //returns StackPane

    } //createScene

    /**
     * Starts the Tetris game and changes the scene
     * so that the game can be played
     */

    private void startGame() {

        //initializes all necessary variables for beginning of game
        newTime = 1;
        flipperHor = 0;
        flipperVert = 0;
        sideMenu = new VBox();
        isGameDone = false;
        linesNum = 0;
        score = 0;
        numberOfLines = 0;
        counter = 1;
        level = 1;
        tetrisScreen = new GridPane();
        stopTime = 0;
        currentShape = 0;
        nextShape = createRandomShape();
        showNextBlock = new GridPane();
        nextGridInt = new int[4][4];
        nextGridRec = new Rectangle[4][4];
        isGamePaused = false;
        HBox primary = new HBox();
        VBox bigger = new VBox();
        VBox next = new VBox();
        HBox scoreHBox = new HBox();
        HBox levelHBox = new HBox();
        HBox lines = new HBox();
        DropShadow shadow1 = new DropShadow();

        int nextCols = 4;
        int nextRows = 4;

        for (int i = 0; i < nextCols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(75 / nextCols);             //These two for loops create the sideGridPane so the next block can be shown
            showNextBlock.getColumnConstraints().add((colConst));
        }

        for (int i = 0; i < nextRows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(75 / nextRows);           // These two for loops create the sideGridPane so the next block can be shown
            showNextBlock.getRowConstraints().add(rowConst);
        }

        showNextBlock.setMinWidth(20);
        showNextBlock.setMinHeight(20);
        showNextBlock.setGridLinesVisible(false);  //sets the size, alignment, and background color of the side grid pane
        showNextBlock.setStyle("-fx-background-color:lightblue;");
        showNextBlock.setAlignment(Pos.CENTER);

        for (int i = 0; i < nextGridInt.length; i++) {
            for (int j = 0; j < nextGridInt[0].length; j++) {
                nextGridInt[i][j] = 0;
                nextGridRec[i][j] = new Rectangle(55, 40);
                nextGridRec[i][j].setFill(Color.LIGHTBLUE);               //sets each Rectangle in the nextGridRec Array to a new Rectangle
                GridPane.setConstraints(nextGridRec[i][j], j, i);    //sets each int in nextGridInt to 0
                showNextBlock.getChildren().add(nextGridRec[i][j]);   //adds each element of the nextGridRec array to the GridPane
            }
        }

        tetrisScreen.setGridLinesVisible(false);

        int cols = 10;
        int rows = 20;

        for (int i = 0; i < cols; i++) {
            ColumnConstraints colConst = new ColumnConstraints();
            colConst.setPercentWidth(100.0 / cols);              //These two for loops create the mainGridPane for the actual game
            tetrisScreen.getColumnConstraints().add((colConst));
        }

        for (int i = 0; i < rows; i++) {
            RowConstraints rowConst = new RowConstraints();
            rowConst.setPercentHeight(100.0 / rows);            //These two for loops create the mainGridPane for the actual game
            tetrisScreen.getRowConstraints().add(rowConst);
        }

        instructions.setId("instructions-button");
        returnMenu.setId("instructions-button"); //sets id for return menu button and instructions button

        pause.setId("instructions-button");
        pause.setMinHeight(50);   // sets the height and width of each button
        pause.setMinWidth(50);

        levelLabel.setText("Level: " + level);
        linesLabel.setText("Lines: " + linesNum + "");   //sets the text for the sideMenu in the game
        scoreLabel.setText("Score: " + score);

        linesLabel.setStyle("-fx-font: 40 phosphate;");
        levelLabel.setStyle("-fx-font: 40 phosphate;"); //adds style to these labels
        scoreLabel.setStyle("-fx-font: 40 phosphate;");
        nextLabel.setStyle("-fx-font: 40 phosphate;");

        sideMenu.setSpacing(10); //sets spacing of side menu

        lines.getChildren().add(linesLabel);
        scoreHBox.getChildren().add(scoreLabel);
        levelHBox.getChildren().add(levelLabel);     //adds the labels to the necessary HBox's
        next.getChildren().addAll(nextLabel, showNextBlock); //adds GridPane to sideMenu
        next.setAlignment(Pos.CENTER);

        tetrisScreen.setStyle("-fx-background-color:grey;");
        tetrisScreen.setMinHeight(600);
        tetrisScreen.setMinWidth(300);  //sets dimensions and color of main game grid
        primary.setAlignment(Pos.CENTER);
        sideMenu.setStyle("-fx-background-color:lightblue;");
        sideMenu.setMaxHeight(600);
        sideMenu.setMinWidth(300);  //sets dimensions and color of side menu
        primary.setSpacing(50);

        next.setMinWidth(20);
        next.setMinHeight(20);
        levelHBox.setMinWidth(20);
        levelHBox.setMinHeight(20);
        scoreHBox.setMinWidth(20);    //sets the height and width of all side menu options
        scoreHBox.setMinHeight(20);
        lines.setMinWidth(20);
        lines.setMinHeight(20);

        next.setStyle("-fx-background-color:lightblue;");
        lines.setStyle("-fx-background-color:yellow;");   //sets background color of the side menu items
        scoreHBox.setStyle("-fx-background-color:green;");
        levelHBox.setStyle("-fx-background-color:red;");

        pause.setText("Pause"); //sets text of pause button to 'pause'

        sideMenu.getChildren().addAll(next, scoreHBox, levelHBox, lines, pause); //adds all necessary items to sideMenu

        instructions.setOnMouseClicked(event -> printInstructionsPage()); //if isntructions button is clicked then instructions page is printed

        Label title = new Label("TETRIS!");
        title.setStyle("-fx-font: 80 phosphate;");
        bigger.setAlignment(Pos.TOP_CENTER);   //creates the title of the game that is displayed while game is being played
        bigger.setSpacing(20);

        VBox tetrisScreenVBox = new VBox();
        tetrisScreenVBox.getChildren().add(tetrisScreen);

        primary.getChildren().addAll(tetrisScreenVBox, sideMenu);
        bigger.getChildren().addAll(title, primary);
        tetrisRoot.getChildren().add(bigger);

        Rectangle one = new Rectangle(30, 30);
        one.setFill(Color.ORANGE);
        one.setStroke(Color.BLACK);

        int mainArray[][] = new int[20][10];
        Rectangle mainArray2[][] = new Rectangle[20][10];

        for (int i = 0; i < mainArray2.length; i++) {
            for (int j = 0; j < mainArray2[0].length; j++) {
                mainArray[i][j] = 0;
                mainArray2[i][j] = new Rectangle(35, 35);   //initializes each block in the GridPane to an inactive rectangle
                mainArray2[i][j].setFill(Color.GRAY);
                mainArray2[i][j].setStroke(Color.BLACK);
                GridPane.setConstraints(mainArray2[i][j], j, i);
                tetrisScreen.getChildren().add(mainArray2[i][j]);
            }
        }

        moveDown = false;
        createTimer(newTime, mainArray, mainArray2); //starts game by creating a mew timer

        stopTime = 1;

        Runnable r5 = () -> { //checks for keyboard presses
            scene.setOnKeyPressed(event -> {

                        if (event.getCode() == KeyCode.LEFT && !isGameDone && !isGamePaused) {  //if left arrow is pressed
                            boolean canYouUpdate = true;
                            for (int[] aMainArray : mainArray) { //checks to see if you can move left
                                if (aMainArray[0] == 2) {
                                    canYouUpdate = false;
                                }
                            }
                            if (canYouUpdate) {
                                pressKeyLeft(mainArray, mainArray2); //moves blocks left
                            }
                        }

                        if (event.getCode() == KeyCode.RIGHT && !isGameDone && !isGamePaused) {  //if right arrow key is pressed
                            boolean canYouUpdate = true;

                            for (int[] aMainArray : mainArray) {
                                if (aMainArray[9] == 2) {
                                    canYouUpdate = false; //checks to see if you can move right
                                }
                            }

                            if (canYouUpdate) {
                                pressKeyRight(mainArray, mainArray2);  //moves blocks to the right
                            }
                        }

                        if (event.getCode() == KeyCode.DOWN && !isGameDone && !isGamePaused) {  //if down arrow key is pressed
                            if (!moveDown) {
                                createTimer(0.05, mainArray, mainArray2); //sets the timers time to 0.05 to move blocks down fatser
                                moveDown = true;
                            }
                        }

                        if (event.getCode() == KeyCode.UP && !isGamePaused && !isGameDone) { //if up arrow key is pressed
                            rotateCurrentShape(mainArray, mainArray2, true); //current shape is rotates
                        }

                        if (event.getCode() == KeyCode.Z && !isGamePaused && !isGameDone) { //if z key is pressed
                            rotateCurrentShape(mainArray, mainArray2, false);  //current shape is rotates
                        }

                        if (event.getCode() == KeyCode.X && !isGamePaused && !isGameDone) { //if X key is pressed
                            if (!moveDown) {

                                hardDrop(mainArray, mainArray2); //active blocks are hard dropped
                                moveDown = true;
                            }
                        }
                    }
            );

            scene.setOnKeyReleased(event -> {
                if (event.getCode() == KeyCode.DOWN && !isGameDone && !isGamePaused) { //if the down key is released
                    createTimer(newTime, mainArray, mainArray2); //timer is set back to the original time
                    moveDown = false;
                }

                if (event.getCode() == KeyCode.X && !isGameDone && !isGamePaused) { //if x key is released
                    createTimer(newTime, mainArray, mainArray2); //timer is set back to the original time
                    moveDown = false;
                }

            });

        };
        Thread t5 = new Thread(r5); //creates new thread
        t5.setDaemon(true);
        t5.start(); //starts the tread that checks for key events

        pause.setOnMouseClicked(event -> { //pauses game when pause button is pressed

            if (counter == 1 && !isGameDone) { //if the button is pressed and says pause
                pause.setText("Play");
                sideMenu.getChildren().addAll(instructions, returnMenu); //adds return menu and instructions menu
                counter--;
                timeline.pause(); //pauses timeline
                isGamePaused = true;
            } else if (counter == 0 && !isGameDone) { //if the buttons text is play
                pause.setText("Pause");
                sideMenu.getChildren().remove(instructions); //removes instructions and return menu
                sideMenu.getChildren().remove(returnMenu);
                counter++;
                isGamePaused = false;
                timeline.play(); //starts timeline again
            }


        });

        returnMenu.setOnMouseClicked(event -> { //returns to the main menu of the tetris game and resets game
                    tetrisRoot.getChildren().clear();
                    isGameDone = true;
                    timeline.pause();
                    tetrisRoot = createScene();

                }
        );

        pause.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND));
        pause.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));
        instructions.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND));
        instructions.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));    //this section of code adds mouse effects to each of the buttons
        returnMenu.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND));
        returnMenu.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT));
        pause.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> pause.setEffect(shadow1));

        pause.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> pause.setEffect(null));

        instructions.addEventHandler(MouseEvent.MOUSE_ENTERED,   //this section of code adds shadow effects to each of the buttons
                e -> instructions.setEffect(shadow1));

        instructions.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> instructions.setEffect(null));

        returnMenu.addEventHandler(MouseEvent.MOUSE_ENTERED,
                e -> returnMenu.setEffect(shadow1));

        returnMenu.addEventHandler(MouseEvent.MOUSE_EXITED,
                e -> returnMenu.setEffect(null));


        sideMenu.setPadding(new Insets(0, 20, 1, 0)); //sets padding for sideMenu
        tetrisScreenVBox.setPadding(new Insets(0, 0, 20, 20)); //sets padding for main game


    } //startGame

    /**
     * This method hard drops the currently active blocks to the nearest available stop
     *
     * @param mainArray This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2  This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     */

    private void hardDrop(int[][] mainArray, Rectangle[][] mainArray2) {

        boolean hitBlocks = false; //sets hitBlocks to false

        while (!hitBlocks) { //while hitblocks is false
            for (int i = 0; i < mainArray.length; i++) {
                for (int j = 0; j < mainArray[0].length; j++) {
                    if ((i != mainArray.length - 1) && mainArray[i][j] == 2 && mainArray[i + 1][j] == 1) {
                        hitBlocks = true;
                    } //these if statements check to see if there is an available inactive block below the currently active blocks
                    if ((i == mainArray.length - 1) && mainArray[i][j] == 2) {
                        hitBlocks = true;
                    }//if
                }//for
            }//for

            if (!hitBlocks) {
                moveArray(mainArray, mainArray2); //moves the array if there are still empty blocks below the actibe block
            }//if
        }//while

        updateArray(mainArray, mainArray2);

    }//hardDrop

    /**
     * Finds the pivot point of the currently active shape so that
     * the shape can rotate the properly. Calls the rotation mechanics
     * method which actually rotates the object
     *
     * @param mainArray  This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2  This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     * @param direction  Used to determine if the object is being turned counter clockwise
     *                   or clockwise
     */

    private void rotateCurrentShape(int[][] mainArray, Rectangle[][] mainArray2, boolean direction) {

        //initializes the necessary variables
        Integer[] xCoords = new Integer[4];
        Integer[] yCoords = new Integer[4];
        int pointArrayIndex = 0;
        Point2D[] newPoints = new Point2D[4];
        int minInX;
        int maxInX;
        int minInY;
        int maxInY;

        Point2D pivot;


        for (int i = 0; i < mainArray.length; i++) {
            for (int j = 0; j < mainArray[0].length; j++) {
                if (mainArray[i][j] == 2) {
                    xCoords[pointArrayIndex] = j;    //stores all the xCoords and yCoords of the active points in the corresponding arrays
                    yCoords[pointArrayIndex] = i;
                    pointArrayIndex++;
                }
            }
        }

        if (xCoords[0] == null) {
            return;
        }

        List<Integer> xCoordinates = Arrays.asList(xCoords);
        List<Integer> yCoordinates = Arrays.asList(yCoords);
        minInX = Arrays.stream(xCoords).min(Integer::compare).get(); //finds min of xCoordinates
        maxInX = Arrays.stream(xCoords).max(Integer::compare).get(); //finds max of xCoordinates
        minInY = Arrays.stream(yCoords).min(Integer::compare).get(); //finds min of yCoordinates
        maxInY = Arrays.stream(yCoords).max(Integer::compare).get(); //finds max of yCoordinates
        Integer[] repeatedX = xCoordinates.stream().filter(i -> Collections.frequency(xCoordinates, i) > 1).distinct().toArray(Integer[]::new); //finds the repeated x values
        Integer[] repeatedY = yCoordinates.stream().filter(i -> Collections.frequency(yCoordinates, i) > 1).distinct().toArray(Integer[]::new); //finds the repeated y values


        if (currentShape == 2 || currentShape == 4 || currentShape == 5) { //if the current shape is a 2, 4, or 5

            if (maxInX - minInX == 2) { //if the active shape is horizontal
                pivot = new Point2D(minInX + 1, repeatedY[0]); //Pivot point is determined
            } //if
            else {
                pivot = new Point2D(repeatedX[0], minInY + 1); //if the active shape is vertical, Pivot point is determined
            }//else

            rotateMechanics(pivot, mainArray, mainArray2, yCoords, xCoords, newPoints, direction); //rotates blocks based on pivot

        }//if

        if (currentShape == 6 || currentShape == 7) { //if the currentShape is 6 or 7
            if (maxInX - minInX == 2) { //if the shape is horizontal
                if (flipperHor == 0) {
                    pivot = new Point2D(repeatedX[0], maxInY);
                    flipperHor++;
                } //if
                else {  //determines the pivor point
                    pivot = new Point2D(repeatedX[0], minInY);
                    flipperHor--;
                }//else
            } //if
            else {  //if shape is vertical
                if (flipperVert == 0) {
                    pivot = new Point2D(minInX, repeatedY[0]);
                    flipperVert++;
                } //if
                else { //determines the pivot point
                    pivot = new Point2D(maxInX, repeatedY[0]);
                    flipperVert--;
                }
            }//else
            rotateMechanics(pivot, mainArray, mainArray2, yCoords, xCoords, newPoints, direction); //rotates shape based on pivot point
        }//if

        if (currentShape == 1) { //if shape is 2
            int testValueLength = Arrays.stream(xCoords).distinct().toArray().length;

            if (testValueLength != 1) { //if its verical
                if (lineFlipperHor == 0) {
                    pivot = new Point2D(maxInX - 2, repeatedY[0]);
                    lineFlipperHor++;
                } //if
                else { //determines the pivot point
                    pivot = new Point2D(maxInX - 1, repeatedY[0]);
                    lineFlipperHor--;
                }//else
            } else { //if its horizontal
                if (lineFlipperVert == 0) {
                    pivot = new Point2D(repeatedX[0], maxInY - 2);
                    lineFlipperVert++;
                }//if
                else { //determines the pivot point
                    pivot = new Point2D(repeatedX[0], maxInY - 1);
                    lineFlipperVert--;
                }//else

            }//else

            rotateMechanics(pivot, mainArray, mainArray2, yCoords, xCoords, newPoints, direction); //rotates shape based on pivot point
        }//if

    }//rotateCurrentShape

    /**
     * This method updates the array so that all blocks can be shown on the grid
     * and changes blocks to not active when applicable
     *
     * @param mainArray This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2  This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     */

    private void updateArray(int[][] mainArray, Rectangle[][] mainArray2) {

        //initializes all variables
        int tetrisChecker;
        int lengthCounter = 0;
        int[] rowArray = {-1, -1, -1, -1};
        int rowArrayIndex = 0;

        for (int i = 0; i < mainArray2.length; i++) {
            for (int j = 0; j < mainArray2[0].length; j++) {
                if (mainArray[i][j] == 2) {
                    mainArray2[i][j].setFill(actualColor);   //sets all actibe Blocks to necessary color
                }//if
                if (mainArray[i][j] == 0) {
                    mainArray2[i][j].setFill(Color.GREY); //sets all inactive blocks to grey color
                }//if

            }//for
        }//for

        for (int i = 0; i < nextGridInt.length; i++) {
            for (int j = 0; j < nextGridInt[0].length; j++) {
                if (nextGridInt[i][j] == 2) {
                    nextGridRec[i][j].setFill(nextColor); //sets the color of the side Grid for the next shape
                }//if
            }//for
        }//for

        for (int i = mainArray.length - 1; i >= 0; i--) {
            tetrisChecker = 0;
            for (int j = 0; j < mainArray[0].length; j++) {
                if (mainArray[i][j] == 1) { //checks to see how many blocks in a row are filled
                    tetrisChecker++;
                }//if
            }//for
            if (tetrisChecker == 10) { //if a row is filled
                rowArray[rowArrayIndex] = i; //adds that row the row array
                rowArrayIndex++;
            }//if
        }//for
        for (int aRowArray : rowArray) {
            if (aRowArray != -1) {
                lengthCounter++;
            }//if
        }//for

        if (lengthCounter > 0) {

            linesNum = linesNum + lengthCounter;
            numberOfLines = lengthCounter;

            for (int i = 0; i < lengthCounter; i++) {
                for (int j = 0; j < mainArray[0].length; j++) {
                    mainArray[rowArray[i]][j] = 0;
                    mainArray2[rowArray[i]][j].setFill(Color.GREY); //sets rows that are filled to grey
                }//for
            }//for

            for (int i = 0; i < lengthCounter; i++) {
                for (int k = rowArray[i]; k >= 0; k--) {
                    for (int l = 0; l < mainArray[0].length; l++) {
                        if (mainArray[k][l] == 1) {
                            Paint blockColor;
                            blockColor = mainArray2[k][l].getFill();  //moves the blocks above the filled row down until it reaches the bottom
                            mainArray[k + 1][l] = 1;
                            mainArray[k][l] = 0;
                            mainArray2[k][l].setFill(Color.GREY);
                            mainArray2[k + 1][l].setFill(blockColor);

                        }//if
                    }//for
                }//for
            }//for
        }//if
    }//updateArray

    /**
     * This moves each active block down when needed
     *
     * @param mainArray  This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2  This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     */

    private void moveArray(int[][] mainArray, Rectangle[][] mainArray2) {

        int checker = 0;
        int tester = 0;

        for (int i = mainArray.length - 1; i >= 0; i--) {
            for (int j = 0; j < mainArray[0].length; j++) {
                if (mainArray[i][j] == 2)
                    checker++; //increases checker if a block is active

            }//for
        }//for
        for (int i = 0; i < mainArray.length - 1; i++) {
            for (int j = 0; j < mainArray[0].length; j++) {
                if (mainArray[i][j] == 2 && (mainArray[i + 1][j] == 0 || mainArray[i + 1][j] == 2)) {
                    tester++; // checks to see if there any blocks that are active that have empty blocks below them
                }//if
            }//for
        }//for
        if (tester == checker) { //if tester equals check
            for (int i = mainArray.length - 1; i >= 0; i--) {
                for (int j = 0; j < mainArray[0].length; j++) {
                    if (mainArray[i][j] == 2) {
                        mainArray[i + 1][j] = 2;  //changes the active blocks to empty and moves the blocks down and sets color
                        mainArray[i][j] = 0;
                        mainArray2[i + 1][j].setFill(actualColor);
                        mainArray2[i][j].setFill(Color.GREY);
                    }//if
                }//for
            }//for
        } //if
        else {
            for (int k = mainArray.length - 1; k >= 0; k--) {
                for (int l = 0; l < mainArray[0].length; l++) {
                    if (mainArray[k][l] == 2) {
                        mainArray[k][l] = 1; //sets all blocks dont have any more room to move to inactive
                    }//if
                }//for
            }//for
        }//else
    } //moveArray

    /**
     * Creates a random integer between 1 and 7 that will corresponds to a shape
     *
     * @return int  This integer corresponds to a specific shape.
     */

    private int createRandomShape() {

        Random rand = new Random();
        return rand.nextInt(7) + 1; //returns a random number between 1 and 7 that corresponds to the current shape

    } //createRandomShape

    /**
     * This moves the currently active blocks to the left when the left arrow is clicked
     *
     * @param mainArray  This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2  This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     */

    private void pressKeyLeft(int[][] mainArray, Rectangle[][] mainArray2) {

        for (int[] aMainArray : mainArray) {
            for (int j = 0; j < mainArray[0].length; j++) {
                if (aMainArray[j] == 2) {
                    if (aMainArray[j - 1] == 1) { //checks to see if the left block is already filled
                        return; //if so leave method
                    }//if

                    if (j - 1 < 0) { //checks to see if it is at the edge of the grid
                        return; //if so leave method
                    }//if
                }//if
            }//for
        }//for

        for (int i = 0; i < mainArray.length; i++) {
            for (int j = 0; j < mainArray[0].length; j++) {
                if (mainArray[i][j] == 2) {
                    if (j != 0) {
                        mainArray[i][j - 1] = 2; //sets the block to the left to active
                        mainArray[i][j] = 0; //sets the currently actibe block to inactive and sets color to grey
                        mainArray2[i][j].setFill(Color.GREY);
                    }//if
                }//if
            }//for
        }//for

        updateArray(mainArray, mainArray2); //updates Array and Grid


    } //pressKeyLeft

    /**
     * This moves the currently active blocks to the right when the left arrow is clicked
     *
     * @param mainArray  This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2  This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     */

    private void pressKeyRight(int[][] mainArray, Rectangle[][] mainArray2) {

        for (int[] aMainArray : mainArray) {
            for (int j = 0; j < mainArray[0].length - 1; j++) {
                if (aMainArray[j] == 2) {
                    if (aMainArray[j + 1] == 1) { //checks to see if the left block is already filled
                        return; //if so leave method
                    }//if

                    if (j + 1 > 19) { //checks to see if it is at the edge of the grid
                        return;//if so leave method
                    }//if
                }//if
            }//for
        }//for


        for (int i = 0; i < mainArray.length; i++) {
            for (int j = mainArray[0].length - 2; j >= 0; j--) {
                if (mainArray[i][j] == 2) {
                    if (j != mainArray.length - 1) {
                        mainArray[i][j + 1] = 2; //sets the block to the right to active
                        mainArray[i][j] = 0; //sets the currently actibe block to inactive and sets color to grey
                        mainArray2[i][j].setFill(Color.GREY);
                    }//if
                }//if
            }//for
        }//for

        updateArray(mainArray, mainArray2); //updates array and grid

    } //pressKeyRight

    /**
     * Displays the next shapes at the initial positions after active block is done
     *
     * @param mainArray  This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2  This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     */

    private void displayShapes(int[][] mainArray, Rectangle[][] mainArray2) {

        for (int[] aMainArray : mainArray) {
            for (int l = 0; l < mainArray[0].length; l++) {
                if (aMainArray[l] == 2) { //checks to see if there any active shapes in the grid
                    return; // if so leave method
                }//if
            }//for
        }//for

        currentShape = nextShape;
        nextShape = createRandomShape(); //creates next shape
        for (int k = 0; k < nextGridInt.length; k++) {
            for (int l = 0; l < nextGridInt[0].length; l++) {
                if (nextGridInt[k][l] == 2) {
                    nextGridInt[k][l] = 0;
                    nextGridRec[k][l].setFill(Color.LIGHTBLUE);
                    nextGridRec[k][l].setStroke(Color.LIGHTBLUE); //removes next shape from side Grid
                }//if
            }//for
        }//for

        switch (currentShape) { //checks to see what current shape is
            case 1:

                mainArray[0][3] = 2;
                mainArray[0][4] = 2;
                mainArray[0][5] = 2;
                mainArray[0][6] = 2;
                actualColor = Color.ORANGE;  //sets the inital position if line
                mainArray2[0][3].setFill(actualColor);
                mainArray2[0][4].setFill(actualColor); //sets color
                mainArray2[0][5].setFill(actualColor);
                mainArray2[0][6].setFill(actualColor);

                break;

            case 2:


                mainArray[1][3] = 2;
                mainArray[1][4] = 2;
                mainArray[1][5] = 2;
                mainArray[0][5] = 2;
                actualColor = Color.BLUE; //sets the inital position if L shape
                mainArray2[1][3].setFill(actualColor);
                mainArray2[1][4].setFill(actualColor);//sets color
                mainArray2[1][5].setFill(actualColor);
                mainArray2[0][5].setFill(actualColor);

                break;

            case 3:


                mainArray[0][4] = 2;
                mainArray[0][5] = 2;
                mainArray[1][4] = 2; //sets the initial position if square
                mainArray[1][5] = 2;
                actualColor = Color.GREEN;
                mainArray2[0][4].setFill(actualColor);
                mainArray2[0][5].setFill(actualColor);
                mainArray2[1][4].setFill(actualColor); //sets color
                mainArray2[1][5].setFill(actualColor);
                break;


            case 4:


                mainArray[0][4] = 2;
                mainArray[1][3] = 2;
                mainArray[1][4] = 2; //sets the initial position if t shaped
                mainArray[1][5] = 2;
                actualColor = Color.RED;
                mainArray2[0][4].setFill(actualColor); //sets color
                mainArray2[1][3].setFill(actualColor);
                mainArray2[1][4].setFill(actualColor);
                mainArray2[1][5].setFill(actualColor);

                break;

            case 5:


                mainArray[0][3] = 2;
                mainArray[1][3] = 2;
                mainArray[1][4] = 2;
                mainArray[1][5] = 2;
                actualColor = Color.YELLOW; //sets initial position if J Shape
                mainArray2[0][3].setFill(actualColor);
                mainArray2[1][3].setFill(actualColor); //sets color
                mainArray2[1][4].setFill(actualColor);
                mainArray2[1][5].setFill(actualColor);

                break;

            case 6:


                mainArray[1][3] = 2;
                mainArray[1][4] = 2;
                mainArray[0][4] = 2;
                mainArray[0][5] = 2;
                actualColor = Color.PINK; //sets initial position if s shape
                mainArray2[1][3].setFill(actualColor);
                mainArray2[1][4].setFill(actualColor); //sets color
                mainArray2[0][4].setFill(actualColor);
                mainArray2[0][5].setFill(actualColor);

                break;

            case 7:

                mainArray[0][3] = 2;
                mainArray[0][4] = 2;
                mainArray[1][4] = 2;
                mainArray[1][5] = 2; //sets initial position of z shape
                actualColor = Color.INDIGO;
                mainArray2[0][3].setFill(actualColor); //sets color
                mainArray2[0][4].setFill(actualColor);
                mainArray2[1][4].setFill(actualColor);
                mainArray2[1][5].setFill(actualColor);
                break;


        }//switch


        switch (nextShape) { //checks to see what nextShape is
            case 1:

                nextGridInt[1][0] = 2;
                nextGridInt[1][1] = 2;
                nextGridInt[1][2] = 2; //sets the inital position if line
                nextGridInt[1][3] = 2;
                nextColor = Color.ORANGE;
                nextGridRec[1][0].setFill(nextColor);
                nextGridRec[1][1].setFill(nextColor); //sets color
                nextGridRec[1][2].setFill(nextColor);
                nextGridRec[1][3].setFill(nextColor);
                nextGridRec[1][0].setStroke(Color.BLACK);
                nextGridRec[1][1].setStroke(Color.BLACK);
                nextGridRec[1][2].setStroke(Color.BLACK);
                nextGridRec[1][3].setStroke(Color.BLACK);

                break;

            case 2:

                nextGridInt[1][0] = 2;
                nextGridInt[1][1] = 2;
                nextGridInt[1][2] = 2; //sets the inital position if L shape
                nextGridInt[0][2] = 2;
                nextColor = Color.BLUE;
                nextGridRec[1][0].setFill(nextColor);
                nextGridRec[1][1].setFill(nextColor); //sets color
                nextGridRec[1][2].setFill(nextColor);
                nextGridRec[0][2].setFill(nextColor);
                nextGridRec[1][0].setStroke(Color.BLACK);
                nextGridRec[1][1].setStroke(Color.BLACK);
                nextGridRec[1][2].setStroke(Color.BLACK);
                nextGridRec[0][2].setStroke(Color.BLACK);
                break;

            case 3:

                nextGridInt[1][1] = 2;
                nextGridInt[1][2] = 2;
                nextGridInt[2][1] = 2; //sets the initial position if square
                nextGridInt[2][2] = 2;
                nextColor = Color.GREEN;
                nextGridRec[1][1].setFill(nextColor);
                nextGridRec[1][2].setFill(nextColor);
                nextGridRec[2][1].setFill(nextColor); //sets color
                nextGridRec[2][2].setFill(nextColor);
                nextGridRec[1][1].setStroke(Color.BLACK);
                nextGridRec[1][2].setStroke(Color.BLACK);
                nextGridRec[2][1].setStroke(Color.BLACK);
                nextGridRec[2][2].setStroke(Color.BLACK);
                break;

            case 4:

                nextGridInt[1][2] = 2;
                nextGridInt[2][1] = 2;
                nextGridInt[2][2] = 2;  //sets the initial position if t shaped
                nextGridInt[2][3] = 2;
                nextColor = Color.RED;
                nextGridRec[1][2].setFill(nextColor);
                nextGridRec[2][1].setFill(nextColor); //sets color
                nextGridRec[2][2].setFill(nextColor);
                nextGridRec[2][3].setFill(nextColor);
                nextGridRec[1][2].setStroke(Color.BLACK);
                nextGridRec[2][1].setStroke(Color.BLACK);
                nextGridRec[2][2].setStroke(Color.BLACK);
                nextGridRec[2][3].setStroke(Color.BLACK);
                break;

            case 5:

                nextGridInt[1][1] = 2;
                nextGridInt[2][1] = 2; //sets initial position if J Shape
                nextGridInt[2][2] = 2;
                nextGridInt[2][3] = 2;
                nextColor = Color.YELLOW;
                nextGridRec[1][1].setFill(nextColor);
                nextGridRec[2][1].setFill(nextColor); //sets color
                nextGridRec[2][2].setFill(nextColor);
                nextGridRec[2][3].setFill(nextColor);
                nextGridRec[1][1].setStroke(Color.BLACK);
                nextGridRec[2][1].setStroke(Color.BLACK);
                nextGridRec[2][2].setStroke(Color.BLACK);
                nextGridRec[2][3].setStroke(Color.BLACK);
                break;

            case 6:

                nextGridInt[1][1] = 2;
                nextGridInt[1][2] = 2; //sets initial position if s shape
                nextGridInt[0][2] = 2;
                nextGridInt[0][3] = 2;
                nextColor = Color.PINK;
                nextGridRec[1][1].setFill(nextColor);
                nextGridRec[1][2].setFill(nextColor);
                nextGridRec[0][2].setFill(nextColor); //sets color
                nextGridRec[0][3].setFill(nextColor);
                nextGridRec[1][1].setStroke(Color.BLACK);
                nextGridRec[1][2].setStroke(Color.BLACK);
                nextGridRec[0][2].setStroke(Color.BLACK);
                nextGridRec[0][3].setStroke(Color.BLACK);
                break;

            case 7:
                nextGridInt[1][1] = 2;
                nextGridInt[1][2] = 2;
                nextGridInt[2][2] = 2; //sets initial position of z shape
                nextGridInt[2][3] = 2;
                nextColor = Color.INDIGO;
                nextGridRec[1][1].setFill(nextColor);
                nextGridRec[1][2].setFill(nextColor);
                nextGridRec[2][2].setFill(nextColor); //sets color
                nextGridRec[2][3].setFill(nextColor);
                nextGridRec[1][1].setStroke(Color.BLACK);
                nextGridRec[1][2].setStroke(Color.BLACK);
                nextGridRec[2][2].setStroke(Color.BLACK);
                nextGridRec[2][3].setStroke(Color.BLACK);
                break;

            default:


        }//switch

    } //displayShapes

    /**
     * This creates a timer that causes the game to run and calls the
     * necessary methods to make the blocks move and updates array.
     *
     * @param second  This is a double value that determines the speed at which the KeyFrame goes
     * @param mainArray This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2 This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     */

    private void createTimer(double second, int[][] mainArray, Rectangle[][] mainArray2) {
        if (stopTime == 1) {
            timeline.stop();
        }//if

        Runnable r4 = () -> {
            checkForLoss(mainArray); //checks for losses
            if (isGameDone) {
                Platform.runLater(this::showLossAlert); //if you lose game then show loss alert
            }//if
            displayShapes(mainArray, mainArray2); //display initial shapes if not losing
        };

        KeyFrame keyFrame = new KeyFrame(Duration.seconds(second), event -> { //keyframe does methods every number of seconds
            try {
                if (!isGameDone) { //if game is not done
                    Thread t4 = new Thread(r4); //creates thread
                    t4.setDaemon(true);
                    t4.start(); //starts thread that displays shapes and checks for losses

                    moveArray(mainArray, mainArray2); //moves blocks
                    updateArray(mainArray, mainArray2); //updates array
                    updateScore(); //updates score
                    checkLevelChange(mainArray, mainArray2); //checks level change


                }//if
            }//try
            catch (Exception e) {
                e.getMessage();
            }//catch
        });
        timeline = new Timeline(); //sets timeline
        timeline.setCycleCount(Timeline.INDEFINITE);//cycle is indefinite
        timeline.getKeyFrames().add(keyFrame); //adds keyframe to timeline
        timeline.play(); //plays time line
    } //createTimer

    /**
     * Shows the losing alert page when the user loses the game
     */

    private void showLossAlert() {

        //initializes variables
        isGameDone = true;
        counter = 1;
        sideMenu.getChildren().remove(pause);
        sideMenu.getChildren().addAll(instructions, returnMenu);
        timeline.pause();
        int highScorelength = 0;
        boolean newHighScoreAcieved = false;
        ArrayList<Integer> scores = new ArrayList<>(0);
        ArrayList<Integer> maxScores = new ArrayList<>(0);

        try {
            BufferedReader reader = new BufferedReader((new FileReader(new File("HighScoreTable.txt"))));
            BufferedReader reader2 = new BufferedReader((new FileReader(new File("HighScoreTable.txt")))); //creates file readers and writers
            FileWriter fileWriter = new FileWriter(new File("HighScoreTable.txt"), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);

            for (String line; (line = reader.readLine()) != null; ) {
                highScorelength++; //checks the length of the highscore file
            }//for

            if (highScorelength == 0) {
                newHighScoreAcieved = true; //if length is 0 the newhighScore is achieved
            } //if
            else {
                for (String text; (text = reader2.readLine()) != null; ) { //if length of file is not 0
                    int newInt = (int) Double.parseDouble(text); //take each score and add to the array
                    scores.add(newInt);
                }//for

                scores.sort(Integer::compare); //sorts array

                for (int i = 0; i < 5; i++) { //gets top 5 scores and put in maxArray
                    if (scores.size() != 0) {
                        int max = scores.stream().reduce(Integer.MIN_VALUE, Integer::max);
                        scores.remove(scores.size() - 1);
                        maxScores.add(max);
                    }//if
                }//for

                for (Integer maxScore : maxScores) { //if the current score is above maxScore then high score is achieved
                    if (score > maxScore) {
                        newHighScoreAcieved = true;
                    }//if
                }//for
            }//else


            Stage lossScreen = new Stage(); //stage is created
            Text loss = new Text("YOU LOST");
            loss.setStyle("-fx-font: 80 phosphate");
            Text scoreLost = new Text("Score: " + score);  //the loss page is created with Title and score displayed
            Text sadFace = new Text(" : ( ");
            sadFace.setStyle("-fx-font: 80 Arial ");
            scoreLost.setStyle("-fx-font: 80 phosphate");
            VBox lossPage = new VBox();
            lossPage.setMinWidth(500);
            lossPage.setMinHeight(500); //sets height and width of loss page
            lossPage.getChildren().addAll(loss, scoreLost, sadFace);

            if (newHighScoreAcieved) { //if the highscore is achieved then the loss page displays that they got a highscore
                Text highScore = new Text("YOU GOT A HIGH SCORE");
                highScore.setFont((Font.font("Phosphate", 40)));
                lossPage.getChildren().add(highScore);
                try {
                    bufferedWriter.append("\n");
                    bufferedWriter.write("" + score); //writes the score to the file
                } //try
                catch (Exception e) {
                    e.getMessage();
                    e.printStackTrace();
                }//catch
            }//if

            bufferedWriter.close();
            fileWriter.close(); //closes readers and writers
            reader.close();

            Scene sceneLoss = new Scene(lossPage);
            lossPage.setAlignment(Pos.CENTER);
            lossPage.setStyle("-fx-background:crimson"); //creates scene andsets background color
            loss.setTextAlignment(TextAlignment.CENTER);
            scoreLost.setTextAlignment(TextAlignment.CENTER);

            lossScreen.setScene(sceneLoss); //sets scene to stage
            lossScreen.setTitle("YOU LOSE"); //sets title of stage
            lossScreen.setResizable(false); //sets resizable to false
            lossScreen.setMinWidth(500); //sets min width
            lossScreen.setMinHeight(500); //sets min height
            lossScreen.sizeToScene(); //sizes set to scene
            lossScreen.show(); //show stage

        } //try
        catch (IOException e) {
            e.getMessage();
            e.printStackTrace();
        }//catch

    } //showLossAlert

    /**
     * Actually rotates the active shape based on the calculated pivot point of the shape
     *
     * @param pivot This is a Point2D object that represents the pivot point of the active object
     * @param mainArray This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2 This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     * @param yCoords An array that contains the y coordinates of the active blocks
     * @param xCoords An array that contains the x coordinates of the active blocks
     * @param newPoints An array of Point2D objects that represents the points of the new rotated active block
     * @param direction boolean that distinguishes whether the object is rotated clockwise or counter clockwise
     */

    private void rotateMechanics(Point2D pivot, int[][] mainArray, Rectangle[][] mainArray2, Integer[] yCoords, Integer[] xCoords, Point2D[] newPoints, boolean direction) {
//initializes newpoints array
        int[] newPointsXCoords = new int[4];

        for (int i = 0; i < 4; i++) {

            if (direction) { //if the direction is clockwise

                if ((yCoords[i] == pivot.getY() + 1 || yCoords[i] == pivot.getY() - 1) && xCoords[i] == pivot.getX()) {
                    int xAlias = (int) pivot.getY() - yCoords[i];
                    int yAlias = (int) pivot.getX() - xCoords[i];
                    newPoints[i] = new Point2D(xAlias + pivot.getX(), yAlias + pivot.getY()); //new points of the rotated blocks are created
                } else {
                    int xAlias = (int) pivot.getY() - yCoords[i];
                    int yAlias = ((int) pivot.getX() - xCoords[i]) * -1;
                    newPoints[i] = new Point2D(xAlias + pivot.getX(), yAlias + pivot.getY()); //new points of the rotated blocks are created
                }
            }
            else { //if the direction is counter clock wise
                if ((yCoords[i] == pivot.getY() + 1 || yCoords[i] == pivot.getY() - 1) && xCoords[i] == pivot.getX()) {
                    int xAlias = yCoords[i] - (int) pivot.getY();
                    int yAlias = xCoords[i] - (int) pivot.getX();
                    newPoints[i] = new Point2D(xAlias + pivot.getX(), yAlias + pivot.getY()); //new points of the rotated blocks are created
                } else {
                    int xAlias = yCoords[i] - (int) pivot.getY();
                    int yAlias = (xCoords[i] - (int) pivot.getX()) * -1;
                    newPoints[i] = new Point2D(xAlias + pivot.getX(), yAlias + pivot.getY()); //new points of the rotated blocks are created
                }
            }
        }

        for (int i = 0; i < newPoints.length; i++) {
            newPointsXCoords[i] = (int) newPoints[i].getX(); //gets the xCoords of the newpoints
        }

        int min = Arrays.stream(newPointsXCoords).min().getAsInt(); //gets the min of xCoords
        int max = Arrays.stream(newPointsXCoords).max().getAsInt(); //gets the max of xCoords

        if (min == -2) { //these if statements check if the active shape is on the edge of the grid
            for (int i = 0; i < newPoints.length; i++) {
                newPoints[i] = new Point2D(newPoints[i].getX() + 2, newPoints[i].getY());
            }//for
        } //if
        else if (min == -1) {
            for (int i = 0; i < newPoints.length; i++) {
                newPoints[i] = new Point2D(newPoints[i].getX() + 1, newPoints[i].getY());
            }//for
        } //else if

        if (max == 11) { //these if statements check if the active shape is on the edge of the grid
            for (int i = 0; i < newPoints.length; i++) {
                newPoints[i] = new Point2D(newPoints[i].getX() - 2, newPoints[i].getY());
            }//for
        } //if
        else if (max == 10) {
            for (int i = 0; i < newPoints.length; i++) {
                newPoints[i] = new Point2D(newPoints[i].getX() - 1, newPoints[i].getY());
            }//for
        }//else if

        for (Point2D point : newPoints) { //makes sure the pints arent outside the the grid

            if (point.getY() < 0 || point.getY() > 19) {
                return; //if so leave the method
            } //if

            if (mainArray[(int) point.getY()][(int) point.getX()] == 1) {
                return; //if so leave the method
            }//if

        }//for


        for (int i = 0; i < mainArray.length; i++) {
            for (int j = 0; j < mainArray[0].length; j++) {
                if (mainArray[i][j] == 2) {
                    mainArray[i][j] = 0; //sets all currenlt active blocks to inactive and empty
                }//if
            }//for
        }//for

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                mainArray[(int) newPoints[i].getY()][(int) newPoints[i].getX()] = 2; //sets the new points to active
            }//for
        }//for
        updateArray(mainArray, mainArray2); //updates array and grid
    } //rotateMechanics

    /**
     * Updates the score so that a new score can be displayed on the screen
     */

    private void updateScore() {
        //creates score based on number of lines cleared
        if (numberOfLines != 0) {
            score = score + (Math.pow(2, numberOfLines - 1) * 100);
        }//if

        linesLabel.setText("Lines: " + linesNum + ""); //sets the lines label
        scoreLabel.setText("Score: " + score); //set the scores label
    } //updateScore

    /**
     * Checks to see if the level should be changed and changes it accordingly
     *
     * @param mainArray  This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     * @param mainArray2  This is the Rectangle 2D array that corresponds to the color of
     *                    each grid in the tetris game
     */

    private void checkLevelChange(int[][] mainArray, Rectangle[][] mainArray2) {

        //based on the score the level will change and time of the time line will change
        if (score >= 4000) {
            showWinAlert(); //if score is above 4000 then show win alert
        } //if
        else if (score >= 3000 && numberOfLines != 0) {
            level = 5; //sets level
            levelLabel.setText("Level: " + level); //sets label
            newTime = 0.2; //creates new Time
            createTimer(newTime, mainArray, mainArray2); //creates timer with newTime
            numberOfLines = 0;
        } //else if
        else if (score >= 2000 && numberOfLines != 0) {
            level = 4;
            levelLabel.setText("Level: " + level);
            newTime = 0.4;
            createTimer(newTime, mainArray, mainArray2);
            numberOfLines = 0;
        } //else if
        else if (score >= 1000 && numberOfLines != 0) {
            level = 3;
            levelLabel.setText("Level: " + level);
            newTime = 0.6;
            createTimer(newTime, mainArray, mainArray2);
            numberOfLines = 0;
        } //else if
        else if (score >= 400 && numberOfLines != 0) {
            level = 2;
            levelLabel.setText("Level: " + level);
            newTime = 0.8;
            createTimer(newTime, mainArray, mainArray2);
            numberOfLines = 0;
        } //else if
        else if (score < 400 && numberOfLines == 0) {
            level = 1;
            levelLabel.setText("Level: " + level);
            numberOfLines = 0;

        }//else if

        numberOfLines = 0; //sets number of lines to 0

    } //checkLevelChange

    /**
     * Shows the winning alert page when the user wins the game
     */

    private void showWinAlert() {

        //inializes the variables
        isGameDone = true;
        counter = 1;
        sideMenu.getChildren().remove(pause);
        sideMenu.getChildren().addAll(instructions, returnMenu);
        timeline.pause();

        int highScorelength = 0;
        boolean newHighScoreAcieved = false;
        ArrayList<Integer> scores = new ArrayList<>(0);
        ArrayList<Integer> maxScores = new ArrayList<>(0);

        try {
            BufferedReader reader = new BufferedReader((new FileReader(new File("HighScoreTable.txt"))));
            BufferedReader reader2 = new BufferedReader((new FileReader(new File("HighScoreTable.txt")))); //creates readers and writers
            FileWriter fileWriter = new FileWriter(new File("HighScoreTable.txt"), true);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);


            for (String line; (line = reader.readLine()) != null; ) { //checks to length of the file
                highScorelength++;
            }//for

            if (highScorelength == 0) { //if length of the file is 0 then new highscore is achieved
                newHighScoreAcieved = true;
            } //if
            else {
                for (String text; (text = reader2.readLine()) != null; ) {
                    int newInt = (int) Double.parseDouble(text); //adds each score to the score array
                    scores.add(newInt);
                }//for

                scores.sort(Integer::compare); //sorts array

                for (int i = 0; i < 5; i++) {
                    if (scores.size() != 0) {
                        int max = scores.stream().reduce(Integer.MIN_VALUE, Integer::max); //adds the top 5 max scores to the max array
                        scores.remove(scores.size() - 1);
                        maxScores.add(max);
                    }//if
                }//for

                for (Integer maxScore : maxScores) { //if the current score is above any of the max scores then new high score is acieved
                    if (score > maxScore) {
                        newHighScoreAcieved = true;
                    }//if
                }//for
            }//else


            Stage winScreen = new Stage(); //stage is created

            Text congrats = new Text("CONGRATULATIONS!!!");
            Text win = new Text("YOU WON!!!");
            congrats.setStyle("-fx-font: 80 phosphate");
            win.setStyle("-fx-font: 80 phosphate");
            Text scoreWon = new Text("Score: " + score);  //adds text to the stage. Says the user one and shows score
            Text happyFace = new Text(" : D ");
            happyFace.setStyle("-fx-font: 80 Arial ");
            scoreWon.setStyle("-fx-font: 80 phosphate"); //sets style of text
            VBox winPage = new VBox();
            winPage.setMinWidth(500);//sets the width of stage
            winPage.setMinHeight(500);  //sets the height of stage
            winPage.getChildren().addAll(congrats, win, scoreWon, happyFace);

            if (newHighScoreAcieved) { //if high score is achieved then it lets the user know they recied a high score
                Text highScore = new Text("YOU GOT A HIGH SCORE");
                highScore.setFont((Font.font("Phosphate", 40))); //sets style of text
                winPage.getChildren().add(highScore);
                try {
                    bufferedWriter.append("\n");
                    bufferedWriter.write("" + score); //writes score to the file if high score is achieved

                } //try
                catch (Exception e) { //catches exception
                    e.getMessage();
                    e.printStackTrace();
                }//catch
            }//if


            bufferedWriter.close();
            fileWriter.close(); //closes readers and writers
            reader.close();

            Scene sceneWin = new Scene(winPage); //sets scene
            winPage.setAlignment(Pos.CENTER);
            winPage.setStyle("-fx-background:lightgreen");
            congrats.setTextAlignment(TextAlignment.CENTER); //sets style of scene and alignment
            win.setTextAlignment(TextAlignment.CENTER);
            scoreWon.setTextAlignment(TextAlignment.CENTER);

            winScreen.setScene(sceneWin); //sets scene
            winScreen.setTitle("YOU WIN"); //sets title of stage
            winScreen.setResizable(false); //sets resizable to false
            winScreen.setMinWidth(500); //sets min width
            winScreen.setMinHeight(500); //sets min height
            winScreen.sizeToScene(); //sizes the stage to the scene
            winScreen.show(); //shows the win stage
        } //try
        catch (Exception e) { //catches exception
            e.printStackTrace();
        }//catch

    } //showWinAlert

    /**
     * Checks to see if user loses the game and ends the game if so
     *
     * @param mainArray  This is the integer 2D Array in which alsos the method to see
     *                  whether a clock is empty, active, or still
     */

    private void checkForLoss(int[][] mainArray) {

        //checks to see if there are any blocks in the initial spots for each shape
        if (currentShape == 1) {
            if (mainArray[0][3] == 1 || mainArray[0][4] == 1 || mainArray[0][5] == 1 || mainArray[0][6] == 1) {
                isGameDone = true;
            }//if
        } //if
        else if (currentShape == 2) {
            if (mainArray[1][3] == 1 || mainArray[1][4] == 1 || mainArray[1][5] == 1 || mainArray[0][5] == 1) {
                isGameDone = true;
            }//if
        } //else if
        else if (currentShape == 3) {
            if (mainArray[0][4] == 1 || mainArray[0][5] == 1 || mainArray[1][4] == 1 || mainArray[1][5] == 1) {
                isGameDone = true;
            } //if
        } //else if
        else if (currentShape == 4) {
            if (mainArray[0][4] == 1 || mainArray[1][3] == 1 || mainArray[1][4] == 1 || mainArray[1][5] == 1) {
                isGameDone = true;
            } //if
        } //else if
        else if (currentShape == 5) {
            if (mainArray[0][3] == 1 || mainArray[1][3] == 1 || mainArray[1][4] == 1 || mainArray[1][5] == 1) {
                isGameDone = true;
            } //if
        } //else if
        else if (currentShape == 6) {
            if (mainArray[1][3] == 1 || mainArray[1][4] == 1 || mainArray[0][4] == 1 || mainArray[0][5] == 1) {
                isGameDone = true;
            } //if
        }  //else if
        else if (currentShape == 7) {
            if (mainArray[0][3] == 1 || mainArray[0][4] == 1 || mainArray[1][4] == 1 || mainArray[1][5] == 1) {
                isGameDone = true;
            } //if
        }//else if

    } //checkForLoss

    /**
     * Prints the instructions page for the user if specific buttons are clicked
     */

    private void printInstructionsPage() {

        Stage instructionsScreen = new Stage(); //creates stage
        instructionsScreen.initModality(Modality.APPLICATION_MODAL); //sets modality

        Text instructionsTitle = new Text("How to Play");
        Text toMoveBlockLeft1 = new Text("To Move Block Left: ");
        Text toMoveBlockRight1 = new Text("To Move Block Right: ");
        Text toSpeedBlock1 = new Text("To Soft Drop: ");     //sets the text for the instructions
        Text toRotateBlock1 = new Text("To Rotate Block Clockwise: ");
        Text toRotateBlockCounter1 = new Text("To Rotate Block Counter Clockwise: ");
        Text toHardDrop1 = new Text("To Hard Drop: ");
        Text toWinGame1 = new Text("To Win Game: ");

        Text toMoveBlockLeft2 = new Text("Press the Left Arrow Key");
        Text toMoveBlockRight2 = new Text("Press the Right Arrow Key");
        Text toSpeedBlock2 = new Text("Press Down Arrow Key");  //sets the text for the instructions
        Text toRotateBlock2 = new Text("Press Up Arrow Key");
        Text toRotateBlockCounter2 = new Text("Press Z ");
        Text toHardDrop2 = new Text("Press X");
        Text toWinGame2 = new Text("Achieve a Score of 4000 or above");

        toMoveBlockLeft1.setFont(Font.font("Courier", FontWeight.BOLD, 20));
        toMoveBlockRight1.setFont(Font.font("Courier", FontWeight.BOLD, 20));
        toSpeedBlock1.setFont(Font.font("Courier", FontWeight.BOLD, 20));
        toWinGame1.setFont(Font.font("Courier", FontWeight.BOLD, 20));  //sets font of the text
        toRotateBlock1.setFont(Font.font("Courier", FontWeight.BOLD, 20));
        toRotateBlockCounter1.setFont(Font.font("Courier", FontWeight.BOLD, 20));
        toHardDrop1.setFont(Font.font("Courier", FontWeight.BOLD, 20));

        toMoveBlockLeft2.setFont(Font.font("Courier", 20));
        toMoveBlockRight2.setFont(Font.font("Courier", 20));
        toSpeedBlock2.setFont(Font.font("Courier", 20));  //sets the font of the text
        toWinGame2.setFont(Font.font("Courier", 20));
        toRotateBlock2.setFont(Font.font("Courier", 20));
        toRotateBlockCounter2.setFont(Font.font("Courier", 20));
        toHardDrop2.setFont(Font.font("Courier", 20));

        HBox blockLeft = new HBox();
        HBox blockRight = new HBox();
        HBox speedBlock = new HBox();
        HBox rotateBlock = new HBox();
        HBox winBlock = new HBox();  //creates necessary HBox's
        HBox rotateCounterBlock = new HBox();
        HBox hardBlock = new HBox();

        blockLeft.getChildren().addAll(toMoveBlockLeft1, toMoveBlockLeft2);
        blockRight.getChildren().addAll(toMoveBlockRight1, toMoveBlockRight2);
        speedBlock.getChildren().addAll(toSpeedBlock1, toSpeedBlock2);   //adds text to the necessary HBox's
        rotateBlock.getChildren().addAll(toRotateBlock1, toRotateBlock2);
        winBlock.getChildren().addAll(toWinGame1, toWinGame2);
        rotateCounterBlock.getChildren().addAll(toRotateBlockCounter1, toRotateBlockCounter2);
        hardBlock.getChildren().addAll(toHardDrop1, toHardDrop2);

        instructionsTitle.setStyle("-fx-font: 80 phosphate ");

        VBox instructionsPage = new VBox();
        VBox movements = new VBox();
        movements.getChildren().addAll(blockLeft, blockRight, speedBlock, rotateBlock, rotateCounterBlock, hardBlock, winBlock); //adds all hboxes to the main vbox
        movements.setPadding(new Insets(0, 20, 10, 20));
        instructionsPage.setMinWidth(500);
        instructionsPage.setMinHeight(300); //sets width and height of vbox
        instructionsPage.getChildren().addAll(instructionsTitle, movements); //adds title and instructions to VBox
        movements.setSpacing(10);
        instructionsPage.setSpacing(30);  //sets spacing

        Scene instructionScene = new Scene(instructionsPage);
        instructionsPage.setAlignment(Pos.TOP_CENTER);
        instructionsPage.setStyle("-fx-background:lightyellow");  //creates scene, alignment, and style of scene
        movements.setAlignment(Pos.CENTER);

        instructionsScreen.setScene(instructionScene); //sets scene
        instructionsScreen.setTitle("Instructions"); //sets title of stage
        instructionsScreen.setResizable(false); //sets the resizable
        instructionsScreen.setMinWidth(500); //sets width
        instructionsScreen.setMinHeight(300); //sets height
        instructionsScreen.sizeToScene();// size stage to scene
        instructionsScreen.show(); //shows stage

    }//printInstructionsPage


}//Tetris
