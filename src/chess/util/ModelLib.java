/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.util;

import chess.chessboard.Point;
import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author Asus
 */
public class ModelLib {

    /**
     * Helper method, use to check if one of the coordinate is valid
     *
     * @param coor - The integer coordinate for checking
     * @return - A boolean signifies if coordinate is valid
     */
    public static boolean isCoorValid(int coor) {
        return 0 <= coor && coor <= 7;
    }

    /**
     * Helper method, use to check if a pair of coordinates is valid.
     *
     * @param row - The integer coordinate of the row for checking
     * @param col - The integer coordinate of column for checking
     * @return - A boolean signifies if pair of coordinates is valid
     */
    public static boolean isCoorValid(int row, int col) {
        return 0 <= row && row <= 7 && 0 <= col && col <= 7;
    }

    /**
     * Helper method, use to check if a point is valid
     *
     * @param point - The point object for checking
     * @return - A boolean signifies if point is valid
     */
    public static boolean isCoorValid(Point point) {
        return 0 <= point.getRow() && point.getRow() <= 7 && 0 <= point.getCol() && point.getCol() <= 7;
    }

    /**
     * Method for initialize the data directory for storing players history.
     */
    public static void initData() {
        //Create the data folder
        String dir = "./data";
        File file = new File(dir);
        file.mkdir();

        //Create the incomplete folder: holds game history of unfinished matches
        dir = "./data/incomplete";
        file = new File(dir);
        file.mkdir();

        //Create the complete folder: holds game history of finished matches
        dir = "./data/complete";
        file = new File(dir);
        file.mkdir();
    }

    /**
     * Method for getting the current date time in formatted.
     *
     * @return - The current date time String in formatted
     */
    public static String getCurrentDateTime() {
        LocalDateTime now = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss");
        return now.format(formatter);
    }
}
