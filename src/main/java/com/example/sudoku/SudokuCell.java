package com.example.sudoku;

import javafx.scene.paint.Color;

/**
 * A class that represents a cell of sudo in table
 */
public class SudokuCell {
    private int num; // number in cell
    private Color bg; // the color of the cell

    /**
     * Constructor that sets the cell to be empty and with the color white
     */
    public SudokuCell(){
        this.num = -1;
        this.bg = Color.WHITE;
    }

    //getters & setters
    public int getNum(){
        return this.num;
    }
    public Color getBg(){
        return this.bg;
    }
    public void setBg(Color bg){
        this.bg = bg;
    }

    /**
     * Method which checks if a given string is valid to be place in sudoku table cell
     * @param str the number to set
     * @return true if given string is valid, otherwise false
     */
    public boolean setCellNum(String str){
        if (str.isBlank()) {
            clear();
            return true;
        }

        try{
            int num = Integer.parseInt(str);
            if(num < 1 || num > 9) return false; // invalid number for sudoku

            this.num = num;

            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    /**
     * Method which checks if the current cell is empty
     * @return true if cell is empty, otherwise false
     */
    public boolean isEmpty(){
        return this.num == -1;
    }

    /**
     * Method which clears the current cell(to be empty)
     */
    public void clear(){
        this.num = -1;
    }
}
