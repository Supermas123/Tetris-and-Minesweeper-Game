

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;
import java.util.Random;

/**                                                                                                                                              
 * This class represents a Minesweeper game.                                                                                                     
 *                                                                                                                                               
 * @authors Shreyas Sundararajan, Samuel Mathew                                                                                               
 */

public class Minesweeper {
	//declarations
	private Stage stage = new Stage();
	private int ackward = 19; //size of grid square
	private Scene scene;
	private GridPane mainGrid = new GridPane(); // the minesweeper grid
	private Button [][] game = new Button[16][16]; //grid buttons
	private int [][] gameInt = new int[16][16]; //grid numbers
	private int noOfMinesLeft = 40;
	private int scoreInt = 0; //timer
	private Text mines = new Text("0" + noOfMinesLeft);
	private Text score = new Text("00" + scoreInt);
	private ImageView image;
	private ImageView imageO;
	private ImageView imageB;
	private ImageView imageX;
	private Button smiley;
	private boolean isClicked[][] = new boolean[16][16];
	private Timeline t;
	private boolean starting = true;
	private StackPane cool; //minesweeper grid and outline

	/**
	 * Constructs an object instance of the {@link Minesweeper} class. It creates the
	 * starting screen of the new window with the instructions and grid. It also initializes
	 * most variables.
	 */
	Minesweeper(){
		//background
		VBox screen = new VBox();
		screen.setStyle("-fx-background-color:ALICEBLUE");
		scene = new Scene(screen,700,600);
		
		image = new ImageView(new Image("https://i.imgur.com/tvndoyX.jpg"));
		imageB = new ImageView(new Image("https://i.imgur.com/ahSihlJ.jpg"));
		imageO = new ImageView(new Image("https://i.imgur.com/mi2slVa.jpg"));
		imageX = new ImageView(new Image("https://i.imgur.com/8ZsHkTp.jpg"));
		ImageView flag = new ImageView(new Image("https://i.imgur.com/1xIwEc5.jpg"));
		ImageView guess = new ImageView(new Image("https://i.imgur.com/RkhRWgI.png"));
		ImageView mineImage = new ImageView(new Image("https://i.imgur.com/1NnW2MQ.jpg"));
		//exposed = new ImageView(new Image("https://i.imgur.com/Q6s7ojC.png"));
		ImageView exposed = new ImageView(new Image("https://i.imgur.com/1NnW2MQ.jpg"));
		ImageView minesweeper1 = new ImageView(new Image("https://i.imgur.com/OARsHFv.jpg"));
		ImageView minesweeper2 = new ImageView(new Image("https://i.imgur.com/eph1z0M.jpg"));
		ImageView minesweeper3 = new ImageView(new Image("https://i.imgur.com/JhUebjW.jpg"));
		ImageView minesweeper4 = new ImageView(new Image("https://i.imgur.com/TAC0icH.jpg"));
		ImageView minesweeper5 = new ImageView(new Image("https://i.imgur.com/BlUnLQc.jpg"));
		ImageView minesweeper6 = new ImageView(new Image("https://i.imgur.com/mWzDMjy.jpg"));
		ImageView minesweeper7 = new ImageView(new Image("https://i.imgur.com/zvGDtPM.jpg"));
		ImageView minesweeper8 = new ImageView(new Image("https://i.imgur.com/Xwt3ZMp.jpg"));
		
		//setting size of the images
	    image.setFitHeight(30);
	    image.setFitWidth(30);
	    imageB.setFitHeight(30);
	    imageB.setFitWidth(30);
	    imageO.setFitHeight(30);
	    imageO.setFitWidth(30);
	    imageX.setFitHeight(30);
	    imageX.setFitWidth(30);
	    flag.setFitHeight(ackward);
	    flag.setFitWidth(ackward);
	    guess.setFitHeight(ackward);
	    guess.setFitWidth(ackward);
	    mineImage.setFitHeight(ackward);
	    mineImage.setFitWidth(ackward);
	    exposed.setFitHeight(ackward);
	    exposed.setFitWidth(ackward);
	    minesweeper1.setFitHeight(ackward);
	    minesweeper1.setFitWidth(ackward);
	    minesweeper2.setFitHeight(ackward);
	    minesweeper2.setFitWidth(ackward);
	    minesweeper3.setFitHeight(ackward);
	    minesweeper3.setFitWidth(ackward);
	    minesweeper4.setFitHeight(ackward);
	    minesweeper4.setFitWidth(ackward);
	    minesweeper5.setFitHeight(ackward);
	    minesweeper5.setFitWidth(ackward);
	    minesweeper6.setFitHeight(ackward);
	    minesweeper6.setFitWidth(ackward);
	    minesweeper7.setFitHeight(ackward);
	    minesweeper7.setFitWidth(ackward);
	    minesweeper8.setFitHeight(ackward);
	    minesweeper8.setFitWidth(ackward);
	    
	    //setting a menu bar
	    Menu file = new Menu("File");
	    MenuItem fileExit = new MenuItem("Exit");
	    fileExit.setOnAction(event -> {
			Stage stage = (Stage) cool.getScene().getWindow(); //if the exit button is pressed then the screen exits and goes to main display
			stage.close();
	    });
	    file.getItems().add(fileExit);
		MenuBar menu = new MenuBar();
		menu.getMenus().addAll(file);
	    menu.prefWidthProperty().bind(stage.widthProperty());
	    
	    //printing instructions
	    Text header = new Text("\t\tINSTRUCTIONS\n\n");
	    Text instruction1 = new Text("* Left-Click to reveal\n\n\n\n");
	    Text instruction2 = new Text("* Right-Click once to flag, twice to \nguess, thrice to get back an \nempty square\n\n\n\n");
	    Text instruction3 = new Text("* You must click all squares other \nthan the mines to win.");
	    instruction1.setStyle("-fx-font: 20 Arial");
	    instruction2.setStyle("-fx-font: 20 Arial");
	    instruction3.setStyle("-fx-font: 20 Arial");
	    header.setStyle("-fx-font: 20 Arial");
	    instruction1.setFill(Color.BLUEVIOLET);
	    instruction2.setFill(Color.BLUEVIOLET);
	    instruction3.setFill(Color.BLUEVIOLET);
	    header.setFill(Color.BLUEVIOLET);
		VBox instructions = new VBox(header, instruction1, instruction2, instruction3);
		
	    //mines left, score, smiley
	    smiley = new Button();
		smiley.setGraphic(image);
		mines.setStyle("-fx-font: 30 Courier");
		mines.setFill(Color.RED);
		score.setStyle("-fx-font: 30 Courier");
		score.setFill(Color.RED);
		StackPane totalScore = new StackPane(score);
		totalScore.setStyle("-fx-background-color:black");
		StackPane mine = new StackPane(mines);
		mine.setStyle("-fx-background-color:black");
		HBox topPart = new HBox(mine, smiley, totalScore);
		topPart.setStyle("-fx-background-color:lightgray");
		topPart.setMinWidth(ackward*17.6);
		topPart.setMaxWidth(ackward*17.6);
		topPart.setAlignment(Pos.CENTER);
		topPart.setSpacing(88);
		VBox fullGame = new VBox(topPart, mainGrid);
		
		//initializing all buttons to an empty button
		
		for(int i = 0;i < game.length;i++){
			for(int j = 0;j < game[0].length;j++){
				gameInt[i][j]=0;
				isClicked[i][j] = false;
				StackPane[][] rectangle = new StackPane[16][16];
				rectangle[i][j] = new StackPane();
				rectangle[i][j].setStyle("-fx-background-color:gray");
				rectangle[i][j].setMinHeight(20);
				rectangle[i][j].setMaxHeight(20);
				rectangle[i][j].setMinWidth(20);
				rectangle[i][j].setMaxWidth(20);
				int k = i;
				int l = j;
				game[i][j] = new Button("");
				game[i][j].setMaxHeight(21);
				game[i][j].setMinHeight(21);
				game[i][j].setMaxWidth(21);
				game[i][j].setMinWidth(21);
				game[i][j].setOnMousePressed(event -> smiley.setGraphic(imageO));
				game[i][j].setOnMouseReleased(event -> smiley.setGraphic(image));
				game[i][j].setOnMouseClicked(event -> {
                    if(event.getButton()==MouseButton.SECONDARY){
                        markings(k, l);
                    }
                    else if(event.getButton()==MouseButton.PRIMARY){
                        revealSquares(k,l);
                    }
                });
				GridPane.setConstraints(game[i][j], j, i);
				mainGrid.getChildren().add(game[i][j]);
			}
		}
		
		//setting the layout of the grid
		Rectangle r = new Rectangle(ackward*17+20,ackward*17 + 63);
		Rectangle r1 = new Rectangle(ackward*17+30,ackward*17 + 70);
		cool = new StackPane(r,r1, fullGame);
		cool.setAlignment(Pos.CENTER_LEFT);
		cool.setPadding(new Insets(0,0,0,20));
		HBox hbox = new HBox(cool, instructions);
		hbox.setSpacing(20);
		r.setFill(Color.GRAY);
		r.setStroke(Color.WHITE);
		r.setStrokeWidth(2);
		r1.setFill(Color.GRAY);
		r1.setStroke(Color.DIMGRAY);
		mainGrid.setAlignment(Pos.CENTER);
		screen.getChildren().addAll(menu, hbox);
		screen.setSpacing(50);
		fullGame.setAlignment(Pos.CENTER);
	}
	/**
	 * The only method called by the {@link ArcadeApp} class. This method opens a new
	 * window to play Minesweeper. It also creates a timer that is displayed on the top
	 * right of the grid. 
	 */
	public void display(){
		 stage.initModality(Modality.APPLICATION_MODAL);
		 stage.setScene(scene);
		 stage.setTitle("Minesweeper");
		 generateGrid();
		 
		 //setting a timer
		 KeyFrame keyFrame = new KeyFrame(Duration.seconds(1), (ActionEvent event) ->{
			 try{
				 scoreInt++;
				 
				 if(scoreInt%10000 < 10){
					 score.setText("00" + scoreInt);
				 }
				 else if(scoreInt%10000 >= 10 && scoreInt%10000 < 100){
					 score.setText("0" + scoreInt);
				 }
				 else if(scoreInt%10000 >= 100 && scoreInt%10000<10000){
					 score.setText(""+scoreInt);
				 }
				 else{
					 gameLost(-1,-1);
				 }
			 }catch(Exception e){
				 e.getMessage();
			 }
		 });
		 t = new Timeline();
		 t.setCycleCount(Timeline.INDEFINITE);
		 t.getKeyFrames().add(keyFrame);
		 smiley.setOnAction(event-> reset());
		 
		 //setting the size of the window
		 stage.setMinHeight(600);
	     stage.setMinWidth(600);
		 stage.sizeToScene();
		 stage.setResizable(false);
	     stage.show();
	}
	/**
	 * This method allots the locations of all the mines in the grid.
	 */
	private void generateGrid(){
		//generating a mine at a random spot
		Random r = new Random();
		int ct = 0;
		boolean x;
		int buffer[] = new int[40];
		
		//generating 40 mines
		while(ct < 40){
			int buf = r.nextInt(16*16);
			x=true;
			//making sure that a mine does not end up in the same spot multiple times
			for(int i = 0 ;i < ct; i++ ){
				if(buffer[i] == buf){
					x = false;
					break;
				}
			}
			if(!x)
				continue;
			buffer[ct] = buf;
			//setting the mine at the correct location
			gameInt[buf/16][buf%16] = 9;
			ct++;
		}
		generateNumbers();
	}
	/**
	 * This method sets the functionality of clicking the smiley button to reset the game.
	 */
	private void reset(){
		//similar to the constructor
		noOfMinesLeft = 40;
		mines.setText("040");
		scoreInt = 0;
		starting = true;
		smiley.setGraphic(image);
		score.setText("000");
		t.pause();
		for(int i = 0;i < game.length;i++){
			for(int j = 0;j < game[0].length;j++){
				gameInt[i][j]=0;
				isClicked[i][j] = false;
				int k = i;
				int l = j;
				game[i][j] = new Button("");
				game[i][j].setMaxHeight(21);
				game[i][j].setMinHeight(21);
				game[i][j].setMaxWidth(21);
				game[i][j].setMinWidth(21);
				game[i][j].setOnMousePressed(event -> smiley.setGraphic(imageO));
				game[i][j].setOnMouseReleased(event -> smiley.setGraphic(image));
				game[i][j].setOnMouseClicked(event -> {
                    if(event.getButton()==MouseButton.SECONDARY){
                        markings(k, l);
                    }
                    else if(event.getButton()==MouseButton.PRIMARY){
                        revealSquares(k,l);
                    }
                });
				GridPane.setConstraints(game[i][j], j, i);
				mainGrid.getChildren().add(game[i][j]);
			}
		}
		generateGrid();
	}
	/**
	 * This method generates all the numbers on the grid.
	 */
	private void generateNumbers(){
		int ct;
		for(int i=0;i<16;i++){
			for(int j=0;j<16;j++){
				ct=0;
				//do nothing if current square is a mine
				if(gameInt[i][j]==9)
					continue;
				//top left button
				if(i==0 && j==0){
					if(gameInt[i][j+1] == 9)
						ct++;
					if(gameInt[i+1][j] == 9)
						ct++;
					if(gameInt[i+1][j+1] == 9)
						ct++;
				}
				//top right button
				else if(i==0 && j==15){
					if(gameInt[i][j-1] == 9)
						ct++;
					if(gameInt[i+1][j] == 9)
						ct++;
					if(gameInt[i+1][j-1] == 9)
						ct++;
				}
				//bottom left button
				else if(i==15 && j==0){
					if(gameInt[i][j+1] == 9)
						ct++;
					if(gameInt[i-1][j] == 9)
						ct++;
					if(gameInt[i-1][j+1] == 9)
						ct++;
				}
				//bottom right button
				else if(i==15 && j==15){
					if(gameInt[i][j-1] == 9)
						ct++;
					if(gameInt[i-1][j] == 9)
						ct++;
					if(gameInt[i-1][j-1] == 9)
						ct++;
				}
				//first row
				else if(i==0){
					for(int k=i;k<i+2;k++){
						for(int l=j-1;l<j+2;l++){
							if(gameInt[k][l]==9)
								ct++;
						}
					}
				}
				//last row
				else if(i==15){
					for(int k=i;k>i-2;k--){
						for(int l=j-1;l<j+2;l++){
							if(gameInt[k][l]==9)
								ct++;
						}
					}
				}
				//first column
				else if(j==0){
					for(int k=i-1;k<i+2;k++){
						for(int l=j;l<j+2;l++){
							if(gameInt[k][l]==9)
								ct++;
						}
					}
				}
				//last column
				else if(j==15){
					for(int k=i-1;k<i+2;k++){
						for(int l=j-1;l<j+1;l++){
							if(gameInt[k][l]==9)
								ct++;
						}
					}
				}
				else{
					for(int k=i-1;k<i+2;k++){
						for(int l=j-1;l<j+2;l++){
							if(gameInt[k][l]==9)
								ct++;
						}
					}
				}
				gameInt[i][j]=ct;
			}
		}
	}
	/**
	 * This method deals with what happens when you right-click a square for a flag,
	 * guess or empty square.
	 * @param row
	 * @param col
	 */
	private void markings(int row, int col){
		//if the current button is already left clicked do nothing
		if(isClicked[row][col]){
			return;
		}
		//creating a flag button
		if(gameInt[row][col]<10){
			gameInt[row][col]=10;
			ImageView flagNew = new ImageView(new Image("https://i.imgur.com/1xIwEc5.jpg"));
			flagNew.setFitHeight(ackward);
			flagNew.setFitWidth(ackward);
			game[row][col].setGraphic(flagNew);	
			noOfMinesLeft--;
			if(noOfMinesLeft<=-10){
				mines.setText("-" + Math.abs(noOfMinesLeft));
			}
			else if(noOfMinesLeft<0){
				mines.setText("-0" + Math.abs(noOfMinesLeft));
			}
			else if(noOfMinesLeft%10000<10){
				mines.setText("00" + noOfMinesLeft);
			}
			else if(noOfMinesLeft%10000>=10 && noOfMinesLeft%10000<100){
				mines.setText("0" + noOfMinesLeft);
			}
		}
		//creating a guess button (question mark)
		else if(gameInt[row][col]==10){
			gameInt[row][col]=11;
			//flagNew = guess;
			ImageView guessNew = new ImageView(new Image("https://i.imgur.com/RkhRWgI.png"));
			guessNew.setFitHeight(ackward);
			guessNew.setFitWidth(ackward);
			game[row][col].setGraphic(guessNew);
			noOfMinesLeft++;
			if(noOfMinesLeft<=-10){
				mines.setText("-" + Math.abs(noOfMinesLeft));
			}
			else if(noOfMinesLeft<0){
				mines.setText("-0" + Math.abs(noOfMinesLeft));
			}
			else if(noOfMinesLeft%10000<10){
				mines.setText("00" + noOfMinesLeft);
			}
			else if(noOfMinesLeft%10000>=10 && noOfMinesLeft%10000<100){
				mines.setText("0" + noOfMinesLeft);
			}
		}
		//changing the button back to an empty square
		else if(gameInt[row][col]==11){
			gameInt[row][col]=0;
			game[row][col].setGraphic(null);
		}
	}
	/**
	 * This is a recursive method that deals with left-clicking squares to reveal them.
	 * @param row
	 * @param col
	 */
	private void revealSquares(int row, int col){
		//base cases
		
		//start the timer
		if(starting == true){
			t.play();
			starting = false;
		}
		//checking for out of bounds
		if(row<0 || row>15 || col<0 || col>15)
			return;
		//do nothing if button is already clicked
		if(isClicked[row][col])
			return;
		//do nothing if button is a flag or question mark
		if(gameInt[row][col] == 10 || gameInt[row][col] == 11)
			return;
		//if the current button is a number display the number
		if(gameInt[row][col]>0 && gameInt[row][col]<9){
			isClicked[row][col] = true;
			if(gameInt[row][col] == 1){
				ImageView rect = new ImageView(new Image("https://i.imgur.com/OARsHFv.jpg"));
				rect.setFitHeight(ackward);
				rect.setFitWidth(ackward);
				rect.setStyle("-fx-background-color:GRAY");
				mainGrid.getChildren().remove(game[row][col]);
				GridPane.setConstraints(rect,col,row);
				mainGrid.getChildren().add(rect);
			}
			if(gameInt[row][col] == 2){
				ImageView rect = new ImageView(new Image("https://i.imgur.com/eph1z0M.jpg"));
				rect.setFitHeight(ackward);
				rect.setFitWidth(ackward);
				rect.setStyle("-fx-background-color:GRAY");
				mainGrid.getChildren().remove(game[row][col]);
				GridPane.setConstraints(rect,col,row);
				mainGrid.getChildren().add(rect);
			}
			if(gameInt[row][col] == 3){
				ImageView rect = new ImageView(new Image("https://i.imgur.com/JhUebjW.jpg"));
				rect.setFitHeight(ackward);
				rect.setFitWidth(ackward);
				rect.setStyle("-fx-background-color:GRAY");
				mainGrid.getChildren().remove(game[row][col]);
				GridPane.setConstraints(rect,col,row);
				mainGrid.getChildren().add(rect);
			}
			if(gameInt[row][col] == 4){
				ImageView rect = new ImageView(new Image("https://i.imgur.com/TAC0icH.jpg"));
				rect.setFitHeight(ackward);
				rect.setFitWidth(ackward);
				rect.setStyle("-fx-background-color:GRAY");
				mainGrid.getChildren().remove(game[row][col]);
				GridPane.setConstraints(rect,col,row);
				mainGrid.getChildren().add(rect);
			}
			if(gameInt[row][col] == 5){
				ImageView rect = new ImageView(new Image("https://i.imgur.com/BlUnLQc.jpg"));
				rect.setFitHeight(ackward);
				rect.setFitWidth(ackward);
				rect.setStyle("-fx-background-color:GRAY");
				mainGrid.getChildren().remove(game[row][col]);
				GridPane.setConstraints(rect,col,row);
				mainGrid.getChildren().add(rect);
			}
			if(gameInt[row][col] == 6){
				ImageView rect = new ImageView(new Image("https://i.imgur.com/mWzDMjy.jpg"));
				rect.setFitHeight(ackward);
				rect.setFitWidth(ackward);
				rect.setStyle("-fx-background-color:GRAY");
				mainGrid.getChildren().remove(game[row][col]);
				GridPane.setConstraints(rect,col,row);
				mainGrid.getChildren().add(rect);
			}
			if(gameInt[row][col] == 7){
				ImageView rect = new ImageView(new Image("https://i.imgur.com/zvGDtPM.jpg"));
				rect.setFitHeight(ackward);
				rect.setFitWidth(ackward);
				rect.setStyle("-fx-background-color:GRAY");
				mainGrid.getChildren().remove(game[row][col]);
				GridPane.setConstraints(rect,col,row);
				mainGrid.getChildren().add(rect);
			}
			if(gameInt[row][col] == 8){
				ImageView rect = new ImageView(new Image("https://i.imgur.com/Xwt3ZMp.jpg"));
				rect.setFitHeight(ackward);
				rect.setFitWidth(ackward);
				rect.setStyle("-fx-background-color:GRAY");
				mainGrid.getChildren().remove(game[row][col]);
				GridPane.setConstraints(rect,col,row);
				mainGrid.getChildren().add(rect);
			}
			gameWon();
		}
		//if current button is a mine end the game
		if(gameInt[row][col] == 9){
			gameLost(row,col);
		}
		//recursive case
		//if current button is an empty square reveal all neighboring squares
		//and click the current button
		if(gameInt[row][col] == 0){
			//make the current button clicked
			isClicked[row][col] = true;
			Rectangle rect = new Rectangle(20,20);
			rect.setFill(Color.GRAY);
			rect.setStroke(Color.DIMGRAY);
			mainGrid.getChildren().remove(game[row][col]);
			GridPane.setConstraints(rect,col,row);
			mainGrid.getChildren().add(rect);
			//reveal all neighboring squares
			revealSquares(row+1,col-1);
			revealSquares(row+1,col);
			revealSquares(row+1,col+1);
			revealSquares(row,col-1);
			revealSquares(row,col+1);
			revealSquares(row-1,col-1);
			revealSquares(row-1,col);
			revealSquares(row-1,col+1);
		}
	}
	/**
	 * This method reveals the entire grid after losing the game.
	 * @param row
	 * @param col
	 */
	private void gameLost(int row, int col){
		//pause timer
		t.pause();
		//time up scenario
		if(row != -1 && col != -1){
		ImageView rec = new ImageView(new Image("https://i.imgur.com/Q6s7ojC.png"));
		rec.setFitHeight(ackward);
		rec.setFitWidth(ackward);
		rec.setStyle("-fx-background-jor:GRAY");
		mainGrid.getChildren().remove(game[row][col]);
		GridPane.setConstraints(rec,col,row);
		mainGrid.getChildren().add(rec);
		}
		//display all buttons in the grid
		for(int i=0; i<16;i++){
			for(int j=0;j<16;j++){
				isClicked[i][j] = true;
				if(gameInt[i][j] == 0){
					Rectangle rect = new Rectangle(20,20);
					rect.setFill(Color.GRAY);
					rect.setStroke(Color.DIMGRAY);
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
				if(gameInt[i][j] == 1){
					ImageView rect = new ImageView(new Image("https://i.imgur.com/OARsHFv.jpg"));
					rect.setFitHeight(ackward);
					rect.setFitWidth(ackward);
					rect.setStyle("-fx-background-jor:GRAY");
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
				if(gameInt[i][j] == 2){
					ImageView rect = new ImageView(new Image("https://i.imgur.com/eph1z0M.jpg"));
					rect.setFitHeight(ackward);
					rect.setFitWidth(ackward);
					rect.setStyle("-fx-background-jor:GRAY");
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
				if(gameInt[i][j] == 3){
					ImageView rect = new ImageView(new Image("https://i.imgur.com/JhUebjW.jpg"));
					rect.setFitHeight(ackward);
					rect.setFitWidth(ackward);
					rect.setStyle("-fx-background-jor:GRAY");
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
				if(gameInt[i][j] == 4){
					ImageView rect = new ImageView(new Image("https://i.imgur.com/TAC0icH.jpg"));
					rect.setFitHeight(ackward);
					rect.setFitWidth(ackward);
					rect.setStyle("-fx-background-jor:GRAY");
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
				if(gameInt[i][j] == 5){
					ImageView rect = new ImageView(new Image("https://i.imgur.com/BlUnLQc.jpg"));
					rect.setFitHeight(ackward);
					rect.setFitWidth(ackward);
					rect.setStyle("-fx-background-jor:GRAY");
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
				if(gameInt[i][j] == 6){
					ImageView rect = new ImageView(new Image("https://i.imgur.com/mWzDMjy.jpg"));
					rect.setFitHeight(ackward);
					rect.setFitWidth(ackward);
					rect.setStyle("-fx-background-jor:GRAY");
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
				if(gameInt[i][j] == 7){
					ImageView rect = new ImageView(new Image("https://i.imgur.com/zvGDtPM.jpg"));
					rect.setFitHeight(ackward);
					rect.setFitWidth(ackward);
					rect.setStyle("-fx-background-jor:GRAY");
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
				if(gameInt[i][j] == 8){
					ImageView rect = new ImageView(new Image("https://i.imgur.com/Xwt3ZMp.jpg"));
					rect.setFitHeight(ackward);
					rect.setFitWidth(ackward);
					rect.setStyle("-fx-background-jor:GRAY");
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
				if(gameInt[i][j] == 9 && !(row == i && col == j)){
					ImageView rect = new ImageView(new Image("https://i.imgur.com/1NnW2MQ.jpg"));
					rect.setFitHeight(ackward);
					rect.setFitWidth(ackward);
					rect.setStyle("-fx-background-jor:GRAY");
					mainGrid.getChildren().remove(game[i][j]);
					GridPane.setConstraints(rect,j,i);
					mainGrid.getChildren().add(rect);
				}
			}
		}
		smiley.setGraphic(imageX);
		lost();
	}
	/**
	 * This method pops up a message after losing a game.
	 */
	private void lost(){
		Stage losingScreen = new Stage();
		losingScreen.initModality(Modality.APPLICATION_MODAL);
		losingScreen.setTitle("YOU LOST!!!");
		Text lose = new Text("YOU LOST!!! \n:(");
		lose.setStyle("-fx-font: 80 Phosphate");
		lose.setFill(Color.DARKKHAKI);
		VBox group = new VBox(lose);
		group.setMinWidth(500);
		group.setMinHeight(500);
		group.setAlignment(Pos.CENTER);
		group.setStyle("-fx-background: Crimson");
		Scene scene = new Scene(group);
		lose.setTextAlignment(TextAlignment.CENTER);
		losingScreen.setScene(scene);
		losingScreen.setResizable(false);
		losingScreen.setMinWidth(500);
		losingScreen.setMinHeight(500);
		losingScreen.sizeToScene();
		losingScreen.showAndWait();
	}
	/**
	 * This method checks whether you won a game or not. If so, it prints an appropriate
	 * message.
	 */
	private void gameWon(){
		
		int ct = 0;
		//counting how many buttons were left clicked
		for(int i=0; i<16; i++){
			for(int j=0;j<16;j++){
				if(isClicked[i][j] && gameInt[i][j] != 9){
					ct++;
				}
			}
		}
		//if all non-mine buttons are clicked, end the game
		if(ct == 216){
			t.pause();
			smiley.setGraphic(imageB);
			Stage winningScreen = new Stage();
			winningScreen.initModality(Modality.APPLICATION_MODAL);
			winningScreen.setTitle("YOU WON!!!");
			Text lose = new Text("YOU WON!!! \nYOUR SCORE IS: " + scoreInt);
			lose.setStyle("-fx-font: 80 Phosphate");
			lose.setFill(Color.BLUEVIOLET);
			VBox group = new VBox(lose);
			group.setMinWidth(500);
			group.setMinHeight(500);
			group.setAlignment(Pos.CENTER);
			group.setStyle("-fx-background: LIGHTGREEN");
			Scene scene = new Scene(group);
			lose.setTextAlignment(TextAlignment.CENTER);
			winningScreen.setScene(scene);
			winningScreen.setResizable(false);
			winningScreen.setMinWidth(500);
			winningScreen.setMinHeight(500);
			winningScreen.sizeToScene();
			winningScreen.showAndWait();
		}
		
	}
}
