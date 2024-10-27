/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import java.util.Objects;
import chess.util.ModelLib;

/**
 * The Point class, which is used for coordination in the chess game. 
 *
 * @author Danglnh2907
 */
public class Point {

    //0-based value of row
    private int row;
    //0-based value of column
    private int col;

    /**
     * The parametric constructor of class Point.
     *
     * @param row - An integer between 0 and 7, denote the value of row
     * @param col - An integer between 0 and 7, denote the value of column
     * @throws IllegalArgumentException - If row or column is invalid, throw
     * IllegalArgumentException
     */
    public Point(int row, int col) throws IllegalArgumentException {
        //Check if x and y is valid
        if (!ModelLib.isCoorValid(row, col)) {
            throw new IllegalArgumentException("row and colummn must be integers between 0 and 7");
        }

        //Set row and column
        this.row = row;
        this.col = col;
    }

    /**
     * Getter method of 'row' field
     *
     * @return - The integer row
     */
    public int getRow() {
        return this.row;
    }

    /**
     * Setter method of 'row' field
     *
     * @param row - The new integer value of row
     * @throws IllegalArgumentException - If new row is not valid, throw
     * IllegalArgumentException
     */
    public void setRow(int row) throws IllegalArgumentException {
        //Check if row parameter is valid
        if (!ModelLib.isCoorValid(row)) {
            throw new IllegalArgumentException("Row must be an integer between 0 and 7");
        }
        //Set the new row value
        this.row = row;
    }

    /**
     * Getter method of 'col' field
     *
     * @return - The column value
     */
    public int getCol() {
        return this.col;
    }

    /**
     * Setter method of 'col' field
     *
     * @param col - The new integer value of column
     * @throws IllegalArgumentException - If new col is invalid, throw
     * IllegalArgumentException
     */
    public void setCol(int col) throws IllegalArgumentException {
        //Check if col parameter is valid
        if (!ModelLib.isCoorValid(col)) {
            throw new IllegalArgumentException("Column must be an integer between 0 and 7");
        }
        //Set the new column value
        this.col = col;
    }

    /**
     * Overridden method: equals method for deep comparison of Point object.
     *
     * @param obj - The object for comparing
     * @return - A boolean, true if the object is deeply equal, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        //If the argument is literal the same object (same memory), return true
        if (this == obj) {
            return true;
        }

        //If obj is null, or the class is different, return false
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        //Compare the x and y coordinate, if both equals, return true
        Point p = (Point) obj;
        return p.getRow() == this.row && p.getCol() == col;
    }

    /**
     * Overridden method: hashCode method for hashing (used in data structure
     * like HashMap or such).
     *
     * @return - The integer hashed by using row and col value
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.row, this.col);
    }

    /**
     * Overridden method: toString method for express Point object's information
     * in a formatted String.
     *
     * @return - The formatted String of Point object's information
     */
    @Override
    public String toString() {
        //The String would be in format: (row, col)
        return String.format("(%d, %d)", row, col);
    }
}
