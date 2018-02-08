package com.internshala.connectfour;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.awt.*;

import static javafx.application.Platform.exit;

public class Main extends Application {
      private Controller controller;

    @Override
    public void start(Stage primaryStage) throws Exception
    {
     FXMLLoader loader=new FXMLLoader(getClass().getResource("game.fxml"));
	    GridPane rootGridPane=loader.load();
	    controller=loader.getController();
	    controller.createPlayground();
	    MenuBar menuBar=createMenu();
	    Pane menuPane =(Pane) rootGridPane.getChildren().get(0);
	    menuPane.getChildren().add(menuBar);
	    menuBar.prefWidthProperty().bind(primaryStage.widthProperty());
	    Scene scene=new Scene(rootGridPane);
	    primaryStage.setScene(scene);
	    primaryStage.setTitle("Connect Four");
	    primaryStage.setResizable(false);
	    primaryStage.show();
    }


    public static void main(String[] args)
    {
        launch(args);
    }
    private  MenuBar createMenu()
    {
	    Menu fileMenu=new Menu("File");
	    MenuItem newGame=new MenuItem("Name");
	    newGame.setOnAction(event -> controller.resetGame());
	    MenuItem resetGame=new MenuItem("Reset");
	    resetGame.setOnAction(event -> controller.resetGame());
	    SeparatorMenuItem separatorMenuItem=new SeparatorMenuItem();
	    MenuItem exitGame=new MenuItem("Exit");
	    exitGame.setOnAction(event -> exitgame());
	    fileMenu.getItems().addAll(newGame,resetGame,separatorMenuItem,exitGame);
	    Menu helpMenu=new Menu("Help");
	    MenuItem aboutgame=new MenuItem("About Connect4");
	    aboutgame.setOnAction(event -> aboutConnect4());
	    MenuItem aboutMe=new MenuItem("About Me");
	    aboutMe.setOnAction(event -> aboutMe());
	    SeparatorMenuItem separator=new SeparatorMenuItem();
	    helpMenu.getItems().addAll(aboutgame,separator,aboutMe);
	    MenuBar menuBar=new MenuBar();
	    menuBar.getMenus().addAll(fileMenu,helpMenu);
	    return menuBar;
    }

	private void aboutMe()
	{
     Alert alert=new Alert(Alert.AlertType.INFORMATION);
     alert.setTitle("About The Developer");
     alert.setHeaderText("Kumar Vishwajeet");
     alert.setContentText("Hi,myself Kumar Vishwajeet.I am a B.Tech student " +
		     "of National Institute of technology,Srinager.My branch " +
		     "is Electronics and Communication Engineering.But I have a " +
		     "lot  of interest in Computer Science and currently I know 12 " +
		     "computer languages .I like to play with code of different " +
		     "languages so I decided to develop a new and amazing game.");
     alert.show();
	}

	private void aboutConnect4()
	{
		Alert alert=new Alert(Alert.AlertType.INFORMATION);
		alert.setTitle("About Connect Four");
		alert.setHeaderText("How To Play?");
		alert.setContentText("Connect Four is a two-player connection game in which "+
				            "the players first choose a color and then take turns dropping "+
				             "colored discs from the top into a seven-column, six-row "+
				             "vertically suspended grid. The pieces fall straight down, "+
				              "occupying the next available space within the column. "+
				             "The objective of the game is to be the first to form a "+
				              "horizontal, vertical, or diagonal line of four of one's "+
				             "own discs. Connect Four is a solved game. The first player "+
				              "can always win by playing the right moves.");
		alert.show();
	}

	private void exitgame()
	{
		Platform.exit();
		System.exit(0);
		}
}
