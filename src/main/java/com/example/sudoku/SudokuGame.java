package com.example.sudoku;

import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;

import java.util.Arrays;

/**
 * A class that implements the *logic* of Sudoku game
 */
public class SudokuGame {
    public static final int BLOCK_SIZE = 3, NUM_BLOCKS = 3;
    public static int boardSize = BLOCK_SIZE * NUM_BLOCKS; // the board size of the sudoku game
    private SudokuCell[][] table; // sudoku table

    /**
     * Constructor that creates an instance of a sudoku game table
     */
    public SudokuGame(){
        this.table = new SudokuCell[boardSize][boardSize];
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                this.table[i][j] = new SudokuCell();
            }
        }
        alternateBlockColors();
    }

    /**
     * Method which returns the background color of a specific cell
     * @param row the row of the desired cell
     * @param col the column of the desired cell
     * @return background color
     */
    public Color getCellBg(int row, int col){
        return this.table[row][col].getBg();
    }

    /**
     * Method which changes the colors of each block in the sudoku table
     */
    private void alternateBlockColors() {
        final Point2D[] a = {
                new Point2D(0,0),
                new Point2D(0,6),
                new Point2D(3,3),
                new Point2D(6,6),
                new Point2D(6,0)
        };

        for (Point2D pnt : a) {
            int x = (int) pnt.getX();
            int y = (int) pnt.getY();

            for (int i = 0; i < BLOCK_SIZE; i++) {
                for (int j = 0; j < BLOCK_SIZE; j++) {
                        SudokuCell curr = this.table[i+y][j+x];
                    curr.setBg(Color.LIGHTGRAY);
                }
            }
        }
    }

    /**
     * Method that gets a sudoku table and checks if it is valid, if it is valid the current sudoku table is updated
     * @param table new sudoku table to check if valid and update to it
     * @return true if the given table is valid, otherwise false
     */
    public boolean updateTable(String[][] table){
        SudokuCell[][] temp = this.table.clone();

        //  check all cell values are valid
        for (int i = 0; i < table.length; i++) {
            for (int j = 0; j < table[0].length; j++) {
                String currStr = table[i][j];

                if(!temp[i][j].setCellNum(currStr)) return false;
            }
        }

        int[] countNumInstances = new int[boardSize+1]; // ignore zero index

        //  check if the whole row is valid (for every row)
        for (int i = 0; i < temp.length; i++) {
            Arrays.fill(countNumInstances, 0);
            for (int j = 0; j < temp[0].length; j++) {
                SudokuCell currCell = temp[i][j];
                if (currCell.isEmpty()) continue;
                if(++countNumInstances[currCell.getNum()] >= 2) return false;
            }
        }

        //  check if the whole col is valid (for every column)
        for (int i = 0; i < temp[0].length; i++) {
            Arrays.fill(countNumInstances, 0);
            for (int j = 0; j < temp.length; j++) {
                SudokuCell currCell = temp[j][i];
                if (currCell.isEmpty()) continue;
                if(++countNumInstances[currCell.getNum()] >= 2) return false;
            }
        }

        //check block validity - no worries this for loop has time complexity of O(n) 😊
        blocks_rows:for (int i = 0; i < temp.length; i+=NUM_BLOCKS) {
            blocks_cols: for (int j = 0; j < temp[0].length; j+=NUM_BLOCKS) {

                Arrays.fill(countNumInstances, 0);
                rows: for (int k = 0; k < BLOCK_SIZE; k++) {
                    cols: for (int l = 0; l < BLOCK_SIZE; l++) {
                        SudokuCell currCell = temp[k+i][l+j];
                        if (currCell.isEmpty()) continue cols;
                        if(++countNumInstances[currCell.getNum()] >= 2) return false;
                    }
                }
            }
        }

        // sudoku table is valid
        this.table = temp;

        return true;
    }

    /**
     * Method which resets the current sudoku table for a new game
     */
    public void reset(){
        for (int i = 0; i < boardSize; i++) {
            for (int j = 0; j < boardSize; j++) {
                this.table[i][j].clear();
            }
        }
    }
}
