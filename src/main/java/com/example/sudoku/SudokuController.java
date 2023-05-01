package com.example.sudoku;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.Pos;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;

import java.util.Arrays;

/**
 * Class which handles the graphic stage of the sudoku game as a grid of TextFields
 */
public class SudokuController {
    private TextField[][] sudokuTblTexts; // grid of TextFields representing the sudoku table
    private SudokuGame sudoku;
    @FXML
    private GridPane gridGame;

    /**
     * function which takes care of initializing the sudoku grid
     * generating TextField for each sudoku cell
     * adding graphics to the grid to look like an authentic sudoku table
     */
    public void initialize(){
        final int boardSize = SudokuGame.boardSize;

        sudoku = new SudokuGame();
        sudokuTblTexts = new TextField[boardSize][boardSize];

        for (int i = 0; i < sudokuTblTexts.length; i++) {
            for (int j = 0; j < sudokuTblTexts[0].length; j++) {
                TextField currCell = new TextField();
                currCell.setPrefSize(gridGame.getPrefWidth()/boardSize, gridGame.getPrefHeight()/boardSize);
                currCell.setAlignment(Pos.CENTER);
                currCell.setFont(Font.font("Verdana", FontWeight.BOLD, 14));
                currCell.setBackground(new Background(new BackgroundFill(sudoku.getCellBg(i,j), CornerRadii.EMPTY, Insets.EMPTY)));
                final double BORDER_WIDTH_STRONG = 2, BORDER_WIDTH_THIN = 0.5;
                double borderBottom = BORDER_WIDTH_THIN, borderRight = BORDER_WIDTH_THIN;
                if(i == 2 || i == 5) borderBottom = BORDER_WIDTH_STRONG;
                if(j == 2 || j == 5) borderRight = BORDER_WIDTH_STRONG;
                currCell.setBorder(new Border(new BorderStroke(Color.web("#333333"),
                        BorderStrokeStyle.SOLID, CornerRadii.EMPTY, new BorderWidths(BORDER_WIDTH_THIN,borderRight, borderBottom,BORDER_WIDTH_THIN))));

                final int currRow = i, currCol = j;
                currCell.setOnKeyPressed(new EventHandler<KeyEvent>() {
                     @Override
                     public void handle(KeyEvent keyEvent) {
                         if(keyEvent.getCode() == KeyCode.ENTER){
                             if(!updateCells()){
                                 Alert err = new Alert(Alert.AlertType.ERROR);
                                 err.setTitle("Invalid input");
                                 err.setContentText("Please type a valid input digit between 1 and 9 which will be valid for a sudoku game.");
                                 err.show();
                                 sudokuTblTexts[currRow][currCol].setText("");
                             }
                         }
                     }
                 });
                sudokuTblTexts[i][j] = currCell;
                gridGame.add(currCell, j, i+1);
            }
        }
    }

    /**
     * button which is resetting the sudoku table when clicked
     */
    @FXML
    void clearPressed(ActionEvent event) {
        for (int i = 0; i < sudokuTblTexts.length; i++) {
            for (int j = 0; j < sudokuTblTexts[0].length; j++) {
                TextField currCell = sudokuTblTexts[i][j];

                currCell.clear();
                currCell.setStyle("-fx-text-fill: black;");
                currCell.setDisable(false);
            }
        }

        sudoku.reset();
    }

    /**
     * button which is setting the sudoku table when clicked,
     * all numbers that are placed in the sudoku table grid will be disabled and now the client can play sudoku
     */
    @FXML
    void setPressed(ActionEvent event) {
        if (!updateCells()) {
            Alert err = new Alert(Alert.AlertType.ERROR);
            err.setTitle("This board is illegal");
            err.setContentText("some of the numbers are violating the rules of sudoku game. \nPlease fix this board numbers before starting the game.");
            err.show();

            return;
        }

        for (int i = 0; i < sudokuTblTexts.length; i++) {
            for (int j = 0; j < sudokuTblTexts[0].length; j++) {
                TextField currCell = sudokuTblTexts[i][j];

                if (currCell.getText().isBlank()) continue;

                currCell.setStyle("-fx-text-fill: red;");
                currCell.setDisable(true);
            }
        }
    }

    /**
     * function that takes care of updating the sudoku table
     * @return true if the current sudoku table is valid, otherwise false
     */
    private boolean updateCells(){
        String[][] tempGrid = new String[SudokuGame.boardSize][SudokuGame.boardSize];

        for (int i = 0; i < sudokuTblTexts.length; i++) {
            for (int j = 0; j < sudokuTblTexts[0].length; j++) {
                String currTxt = sudokuTblTexts[i][j].getText();
                tempGrid[i][j] = currTxt;
            }
        }

        return sudoku.updateTable(tempGrid);
    }

}