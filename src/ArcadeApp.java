

import javafx.application.Application;
import javafx.application.Platform;
import javafx.scene.Cursor;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * This is the Arcade App Class. This allows the user
 * to choose between two games to play, Tetris and Minesweeper.
 * This class will display a window that will allow you to play, and
 * allow you to close the window if necessary
 *
 * @author Samuel Mathew <skm77482@uga.edu>, Shreyas Sundararajaran
 * @since 2018-05-03
 */


public class ArcadeApp extends Application {

    /**
     * {@inheritDoc}
     * <p>
     * This method is inherited from the application class and justs prints "Initializing"
     * before the start method is called
     */

    @Override
    public void init() {
        System.out.println("Initializing"); //prints "initializing"
    }//init

    /**
     * Displays the initial menu screen that allows you to select between which
     * game the user wants to play. Takes a stage as a parameter.
     *
     * @param stage {@inheritDoc}
     */

    @Override
    public void start(Stage stage) {

        try {

            MenuBar menuBar = new MenuBar(); //creates MenuBar object
            Menu file = new Menu("File"); //Create Menu Object
            Menu options = new Menu("Options"); //Create Menu Object
            Menu help = new Menu("Help"); //Create Menu Object
            MenuItem fileExit = new MenuItem("Close Window"); //Create MenuItem Object
            StackPane arcade = new StackPane(); //Create StackPane Object
            VBox root = new VBox(menuBar, arcade); //Create VBox with MenuBar and StackPane
            Scene scene = new Scene(root, 640, 480); //Creates Scene with specified width and height


            fileExit.setOnAction(event -> stop()); //if the file exit menu item is called then the program is stopped
            file.getItems().add(fileExit); //adds MenuItem to File Menu
            menuBar.getMenus().addAll(file, options, help); //Adds Menu's to Menu Bar object
            menuBar.prefWidthProperty().bind(stage.widthProperty()); //Makes menu bar the size of the scene

            Image image = new Image("https://i.imgur.com/58FY29L.jpg");//Gets background image from url
            ImageView imageView = new ImageView();
            imageView.setImage(image);//sets Background image to ImageView
            imageView.setFitWidth(640);//sets background width to 640
            imageView.setFitHeight(460);//sets background height to 460

            ImageView tetris = new ImageView(new Image("https://i.imgur.com/MDuyS3b.png")); //creates Background image for tetris button
            ImageView minesweeper = new ImageView(new Image("https://i.imgur.com/pY1FXbg.png")); //creates Background image for minesweeper button


            tetris.setFitWidth(220); //sets tetris background image width
            tetris.setFitHeight(250); //sets tetris background image height
            tetris.setTranslateX(-150); //moves tetris background image in X
            tetris.setTranslateY(0); //moves tetris background image in Y


            tetris.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND)); //changes mouse look when mouse enters tetris button
            tetris.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT)); //changes mouse look when mouse exits tetris button
            tetris.setOnMouseClicked(event -> { //launches the tetris game when clicks

                        Tetris obj = new Tetris(); //creates tetris object
                        obj.display(); //displays tetris game
                    }
            );

            minesweeper.setFitWidth(220); //sets width of minesweeper image
            minesweeper.setFitHeight(250); //sets height of minesweeper image
            minesweeper.setTranslateX(150); //moves minesweeper image in X
            minesweeper.setTranslateY(0); //moves minesweeper image in Y

            minesweeper.setOnMouseEntered(event -> scene.setCursor(Cursor.HAND)); //changes mouse look when mouse enters tetris button
            minesweeper.setOnMouseExited(event -> scene.setCursor(Cursor.DEFAULT)); //changes mouse look when mouse exits tetris button
            minesweeper.setOnMouseClicked(event -> {
                Minesweeper object = new Minesweeper();
                object.display();
            });



            arcade.getChildren().addAll(imageView, tetris, minesweeper); //adds background image to stackpane


            stage.setTitle("cs1302-arcade!"); //sets stage title
            stage.setScene(scene); //sets stage to scene
            stage.sizeToScene(); //sizes stage to scene
            stage.setResizable(false); //makes sure you can't resize stage
            stage.show(); //shows stage

            // the group must request input focus to receive key events
            // @see https://docs.oracle.com/javase/8/javafx/api/javafx/scene/Node.html#requestFocus--
            root.requestFocus();

        } //try
        catch (Exception e) { //catches exception and prints message
            System.out.println(e.getMessage());
            System.out.println(e);
        }//catch

    } // start


    /**
     * This method is inherited from the application class and
     * stops the program when called
     * <p>
     * {@inheritDoc}
     */

    @Override
    public void stop() {
        Platform.exit(); //exits platform
        System.exit(0); //exits system with status 0

    }//stop

    /**
     * This is the main method that runs the application when the program is run
     * Takes in any arguments as a String array
     *
     * @param args
     */

    public static void main(String[] args) {
        try {
            Application.launch(args); //starts application
        } catch (UnsupportedOperationException e) {
            //System.err.println(e);
            System.err.println("If this is a DISPLAY problem, then your X server connection");
            System.err.println("has likely timed out. This can generally be fixed by logging");
            System.err.println("out and logging back in.");
            System.exit(1);
        } // try
    } // main
} // ArcadeApp
