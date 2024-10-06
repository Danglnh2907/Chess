/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import chess.util.ModelLib;
import java.awt.Graphics2D;
import java.awt.Image;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Objects;
import javax.imageio.ImageIO;

/**
 * The Piece class
 *
 * @author Asus
 */
public class Piece {

    //The side of the piece (not determine the actual color of the piece, just used to denote the side)
    private final Color color;
    //The rank of the piece
    private Rank rank;
    //The piece's position: (row, col)
    private Point position;
    //The list for containing the potential position a piece can move to
    private final ArrayList<Point> candidates;
    //The image of the piece for drawing
    private Image image;

    /**
     * The constructor of class Piece
     *
     * @param color - The Color enumerate value of the piece
     * @param rank - The Rank enumerate value of the piece
     * @param row - The row where the piece located
     * @param col - The column where the piece located
     * @throws IllegalArgumentException - If the row and col is invalid, throws
     * IllegalArgumentException
     */
    public Piece(Color color, Rank rank, int row, int col) throws IllegalArgumentException {
        //Check if the row and column is valid
        if (!ModelLib.isCoorValid(row, col)) {
            throw new IllegalArgumentException("Invalid row or column");
        }
        //Set the color (Color is an enum so we don't have to check for validation)
        this.color = color;
        //Set the rank (Rank is an enum so we don't have to check for validation)
        this.rank = rank;
        //Set the position to (row, col)
        this.position = new Point(row, col);
        //Initialize the candidate list as empty array list
        this.candidates = new ArrayList<>();
    }

    /**
     * The method for getting the image of the piece for UI rendering.
     */
    public void getImage() {
        //Get the image path based on color
        String path = String.format("/resource/chess/%s-", this.color == Color.BLACK ? "b" : "w");

        //Get the image path based on the rank of the piece
        switch (this.rank) {
            case PAWN: {
                path += "pawn";
                break;
            }
            case ROOK: {
                path += "rook";
                break;
            }
            case KNIGHT: {
                path += "knight";
                break;
            }
            case BISHOP: {
                path += "bishop";
                break;
            }
            case QUEEN: {
                path += "queen";
                break;
            }
            case KING: {
                path += "king";
                break;
            }
        }

        path += ".png";

        //Get the image using the image path
        try {
            this.image = ImageIO.read(getClass().getResourceAsStream(path));
        } catch (IOException e) {
            System.out.println("Error getting piece's image");
        }
    }

    /**
     * Getter method 'rank' field
     *
     * @return - The Rank of the piece
     */
    public Rank getRank() {
        return rank;
    }

    /**
     * Setter method of 'rank' field. Normally, this method should only be
     * called when the piece is a PAWN, other than that the Rank should not be
     * changed.
     *
     * @param rank - The new Rank of the piece
     */
    public void setRank(Rank rank) {
        this.rank = rank;
    }

    /**
     * Getter method of 'position' field
     *
     * @return - The current position of the piece
     */
    public Point getPosition() {
        return position;
    }

    /**
     * The setter method of 'position' field
     *
     * @param position - The new position of the piece
     */
    public void setPosition(Point position) {
        this.position = position;
    }

    /**
     * Getter method of 'color' field
     *
     * @return - The color (side) of the piece
     */
    public Color getColor() {
        return color;
    }

    /**
     * Getter method of 'candidates' field
     *
     * @return - The candidates ArrayList
     */
    public ArrayList<Point> getCandidates() {
        return candidates;
    }

    /**
     * Method for adding new position to the candidates list
     *
     * @param pos - The new position to be added
     * @throws IllegalArgumentException - If the new position is invalid, throws
     * IllegalArgumentException
     */
    public void addNewPosition(Point pos) throws IllegalArgumentException {
        //Check if new position is valid
        if (!ModelLib.isCoorValid(pos)) {
            throw new IllegalArgumentException("Invalid position in the chessboard");
        }
        this.candidates.add(pos);
    }

    /**
     * Method for removing a position from candidates list.
     *
     * @param pos - The position for removing
     * @throws IllegalArgumentException - If the position to be removed is
     * invalid, throws IllegalArgumentException
     */
    public void removePosition(Point pos) throws IllegalArgumentException {
        //Check if new position is valid
        if (!ModelLib.isCoorValid(pos)) {
            throw new IllegalArgumentException("Invalid position in the chessboard");
        }
        this.candidates.remove(pos);
    }

    /**
     * Method for clearing all the positions in the candidates list.
     */
    public void clearAllPositions() {
        this.candidates.clear();
    }

    /**
     * Method for checking if a position is in the candidates list
     *
     * @param pos - The position for checking
     * @return - The boolean value, true if position contained in the list,
     * false otherwise
     */
    public boolean hasPosition(Point pos) {
        //Check if new position is valid
        if (!ModelLib.isCoorValid(pos)) {
            throw new IllegalArgumentException("Invalid position in the chessboard");
        }
        return this.candidates.contains(pos);
    }

    /**
     * Overridden equals method for comparing Piece object.
     *
     * @param obj - The object for comparison
     * @return - The boolean, true if it's deeply equals, false otherwise
     */
    @Override
    public boolean equals(Object obj) {
        // If the argument is literal the same object (same memory), return true
        if (this == obj) {
            return true;
        }

        // If obj is null, or the class is different, return false
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }

        // Compare the x and y coordinate, if both equals, return true
        Piece piece = (Piece) obj;
        return piece.position.equals(this.position) && piece.color.equals(this.color);
    }

    /**
     * Overridden hashCode method. When override equals method, it's better to
     * override the hashCode method as well, to ensure that the comparison is
     * correct
     *
     * @return - The hash value based on the properties of Piece
     */
    @Override
    public int hashCode() {
        return Objects.hash(this.position, this.color, this.rank);
    }

    /**
     * Overridden toString method
     *
     * @return - The information of Piece in a formatted String
     */
    @Override
    public String toString() {
        //The format is: Color Rank (row, col). For example: BLACK PAWN (3, 4)
        return String.format("%s %s (%d,%d)", this.color, this.rank, this.position.getRow(), this.position.getCol());
    }

    /**
     * The draw method, used to draw the Piece image
     *
     * @param g2 - The Graphics2D object used for drawing the piece
     * @param yOffSet - The integer value for the y coordinate, which was used
     * to draw the effect of the piece slightly up or down (select piece action)
     */
    public void draw(Graphics2D g2, int yOffSet) {
        int size = 100; //The size of the image (which will equal the size of a square in chessboard) in pixel
        /*
         * Row is associated with y coordinate, while col is associated with x coordinate
         * The yOffSet is used to make the piece slightly upwarr or downward when a user select (mouse click) on the piece
         */
        g2.drawImage(this.image, this.position.getCol() * size, this.position.getRow() * size + yOffSet, size, size, null);
    }
}
