package com.internshala.connectfour;

import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Point2D;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.scene.shape.Shape;

import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.RecursiveAction;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Controller implements Initializable{
	public static final int COLUMNS=7;
	public static final int ROWS=6;
	public static final int CIRCLE_DIA=80;
	public static String discColor1="#24303E";
	public static String discColor2="#4CAA88";
	public static String PLAYER_ONE="Player One";
	public static String PLAYER_TWO="Player Two";
	private boolean isPalyerOneturn=true;
    private  Disc[][] insertedDiscsArray=new Disc[ROWS][COLUMNS];
	@FXML
	public GridPane rootGridPane;
	@FXML
	public Pane insertedDiscsPane;
	@FXML
	public Label playerNameLabel;
	@FXML
	public TextField playerOneTextField;
	@FXML
	public TextField playerTwoTextField;
	@FXML
	public Button setNamesButton;
	private  boolean isAllowedToInsert=true;
	public void createPlayground()
	{
		Platform.runLater(() -> setNamesButton.requestFocus());
		Shape rectangle=new Rectangle((COLUMNS+1)*CIRCLE_DIA,(ROWS+1)*CIRCLE_DIA);
		for(int row=0;row<ROWS;row++)
		{for(int col=0;col<COLUMNS;col++)
		{
			Circle circle = new Circle();
			circle.setRadius(CIRCLE_DIA / 2);
			circle.setCenterX(CIRCLE_DIA / 2);
			circle.setCenterY(CIRCLE_DIA / 2);
			circle.setSmooth(true);
			circle.setTranslateX(col*(CIRCLE_DIA+5)+20);
			circle.setTranslateY(row*(CIRCLE_DIA+5)+20);
			rectangle = Shape.subtract(rectangle, circle);
		}
		}
		rectangle.setFill(Color.WHITE);
		rootGridPane.add(rectangle,0,1);
	    List<Rectangle> rectangleList=createClickableColumns();
	    for(Rectangle rectangle1:rectangleList)
	    {
	    	rootGridPane.add(rectangle1,0,1);
	    }
		setNamesButton.setOnAction(event -> {
			PLAYER_ONE = playerOneTextField.getText();
			PLAYER_TWO = playerTwoTextField.getText();
			playerNameLabel.setText(isPalyerOneturn? PLAYER_ONE : PLAYER_TWO);
		});
	}
		private  List<Rectangle> createClickableColumns()
		{
			List<Rectangle> rectangleList=new ArrayList<>();
			for(int col=0;col<COLUMNS;col++)
			{
				Rectangle rectangle=new Rectangle(CIRCLE_DIA,(ROWS+1)*CIRCLE_DIA);
			rectangle.setFill(Color.TRANSPARENT);
			rectangle.setTranslateX(col*(CIRCLE_DIA+5)+20);
			rectangle.setOnMouseEntered(event -> rectangle.setFill(Color.valueOf("#eeeeee26")));
			rectangle.setOnMouseExited(event -> rectangle.setFill(Color.TRANSPARENT));
			final int column=col;
			rectangle.setOnMouseClicked(event ->
			{
				if(isAllowedToInsert)
				{   isAllowedToInsert=false;
					insertDisc(new Disc(isPalyerOneturn),column);
				}
			});
			rectangleList.add(rectangle);
			}
			return rectangleList;
		}
		private void insertDisc(Disc disc,int column)
		{
          int row=ROWS-1;
          while(row>=0)
          {
          	if(getDiscIfPresent(row,column)==null)
	          {
	          	break;
	          }
          	row--;
          }
          if(row<0)
          	return;
			insertedDiscsArray[row][column]=disc;
          insertedDiscsPane.getChildren().add(disc);
          disc.setTranslateX(column*(CIRCLE_DIA+5)+20);
		   int currentRow=row;
          TranslateTransition translateTransition=new TranslateTransition(javafx.util.Duration.seconds(0.5),disc);
          translateTransition.setToY(row*(CIRCLE_DIA+5)+20);
          translateTransition.setOnFinished(event ->
          {
            isAllowedToInsert=true;
          	if(gameEnded(currentRow,column))
            {
             gameOver();
            }
          	isPalyerOneturn=!isPalyerOneturn;
          	playerNameLabel.setText(isPalyerOneturn?PLAYER_ONE:PLAYER_TWO);
          });
          translateTransition.play();
		}
		private boolean gameEnded(int row,int column)
		{
          //vertical
			List<Point2D> verticalPoints= IntStream.rangeClosed(row-3,row+3)
		          .mapToObj(r-> new Point2D(r,column))
		          .collect(Collectors.toList());
			//horizontal
			List<Point2D> horizontalPoints= IntStream.rangeClosed(column-3,column+3)
					.mapToObj(c-> new Point2D(row,c))
					.collect(Collectors.toList());
			Point2D startPoint1=new Point2D(row-3,column+3);
			List<Point2D> diagonal1Points=IntStream.rangeClosed(0,6)
					.mapToObj(i->startPoint1.add(i,-i))
					.collect(Collectors.toList());
			Point2D startPoint2=new Point2D(row-3,column-3);
			List<Point2D> diagonal2Points=IntStream.rangeClosed(0,6)
					.mapToObj(i->startPoint2.add(i,i))
					.collect(Collectors.toList());
	    boolean isEnded=checkCombinations(verticalPoints)||checkCombinations(horizontalPoints)
			    ||checkCombinations(diagonal1Points)||checkCombinations(diagonal2Points);
			return isEnded;
		}
	private boolean checkCombinations(List<Point2D> points)
	{
    int chain=0;
		for (Point2D point:points)
		{
		int rowIndexForArray=(int) point.getX();
		int columnIndexForarray=(int) point.getY();
		Disc disc=getDiscIfPresent(rowIndexForArray,columnIndexForarray);
		if(disc!=null && disc.isPlayerOneMove==isPalyerOneturn)
		{
			chain++;
			if (chain==4)
			{
				return true;
			}
		}else
			{
			chain=0;
		}
		}return false;
	}
	private  Disc getDiscIfPresent(int row,int column)
	{
		if(row>=ROWS||row<0||column>=COLUMNS||column<0)
			return null;
			return insertedDiscsArray[row][column];
	}
		private void gameOver()
	{
        String winner=isPalyerOneturn?PLAYER_ONE:PLAYER_TWO;
		System.out.println("Winner is: "+winner);
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("Connect Four");
		alert.setHeaderText("The Winner is "+winner);
		alert.setContentText("Want to play more?");
		ButtonType yesBtn=new ButtonType("Yes");
		ButtonType noBtn=new ButtonType("No, Exit");
		Platform.runLater(()->
		{
			alert.getButtonTypes().setAll(yesBtn, noBtn);
			Optional<ButtonType> clickedButton = alert.showAndWait();
			if (clickedButton.isPresent() && clickedButton.get() == yesBtn) {
				resetGame();
			} else {
				Platform.exit();
				System.exit(0);
			}
		});
	}
	public void resetGame()
	{
   insertedDiscsPane.getChildren().clear();
   for(int row=0;row<insertedDiscsArray.length;row++)
   {
   	for(int col=0;col<insertedDiscsArray.length;col++)
    {
    	insertedDiscsArray[row][col]=null;
    }
   }
   isPalyerOneturn=true;
   playerNameLabel.setText(PLAYER_ONE);
   createPlayground();
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

		private static class Disc extends Circle
	{
		private  final boolean isPlayerOneMove;
		public Disc(boolean isPlayerOneMove)
		{
			this.isPlayerOneMove=isPlayerOneMove;
			setRadius(CIRCLE_DIA/2);
			setFill(isPlayerOneMove?Color.valueOf(discColor1):Color.valueOf(discColor2));
			setCenterX(CIRCLE_DIA/2);
			setCenterY(CIRCLE_DIA/2);
		}
	}
}

