/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import chess.util.ModelLib;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import javax.swing.JPanel;

/**
 * ChessBoard class, the main logic of the game
 *
 * @author Asus
 */
public class ChessBoard extends JPanel {

    //The 2D 8x8 array that acts as the chessboard
    private final Piece[][] board;
    //The selected piece
    private Piece selectedPiece;
    //Since we need to check for Kings status every action so having their references is more efficient that searching through array
    private final Piece[] kings; //First element is BLACK, second element is WHITE
    //The listener object, used to communicate with the Game object
    private Listener listener;

    /**
     * Constructor method of class ChessBoard, which will initialize attribute
     * and setup layout.
     */
    public ChessBoard() {
        //Initialize the 8x8 array 
        this.board = new Piece[8][8];
        //Setting the selected piece to null
        this.selectedPiece = null;
        //Initialize the empty array kings
        this.kings = new Piece[2];
        //Initialize listener
        this.listener = null;
        //Setup the layout
        initLayout();
    }

    /**
     * Helper method, setup the layout of the ChessBoard and adding
     * MouseListener.
     */
    private void initLayout() {
        //Set the panel size to 800 x 800 (pixel)
        setPreferredSize(new Dimension(800, 800));
        //Set the panel to focusable
        setFocusable(true);
        //Set the chessboard visibility
        setVisible(true);
        //Add Mouse event listener
        this.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                //We leave the handling of event for the class Game to process
                listener.onChessboardClick(e.getX(), e.getY());
            }
        });
    }

    /**
     * Method for register the listener object.
     *
     * @param ls - The Listener object for registration.
     */
    public void registerListener(Listener ls) {
        this.listener = ls;
    }

    /**
     * Method for getting a piece at a specific position in the board.
     *
     * @param row - The row where the piece is located
     * @param col - The column where the piece is located
     * @return - The piece at the specific (row, col). If there is no piece at
     * that position, the return value is null
     * @throws IllegalArgumentException - If row or col is invalid, throws
     * IllegalArgumentException
     */
    public Piece getPieceAt(int row, int col) throws IllegalArgumentException {
        //Check if row and col is valid
        if (!ModelLib.isCoorValid(row, col)) {
            throw new IllegalArgumentException("Row or column invalid");
        }
        return this.board[row][col];
    }

    /**
     * Method for setting a piece at a specific position. This method will use
     * the internal position value to determine where to put the piece.
     *
     * @param piece - The piece for placing into the board
     */
    public void setPieceAt(Piece piece) {
        this.board[piece.getPosition().getRow()][piece.getPosition().getCol()] = piece;
    }

    /**
     * Method for clearing the whole board (without allocated new memory for
     * it).
     */
    public void clearBoard() {
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                this.board[row][col] = null;
            }
        }
    }

    /**
     * Getter method of 'selectedPiece' field
     *
     * @return - The selected piece. If null, it means that the current player
     * hasn't selected any piece
     */
    public Piece getSelectedPiece() {
        return selectedPiece;
    }

    /**
     * Setter method of 'selectedPiece' field.
     *
     * @param selectedPiece - The new value of selected piece. This value can be
     * null (release a piece)
     */
    public void setSelectedPiece(Piece selectedPiece) {
        this.selectedPiece = selectedPiece;
    }

    /**
     * Method for getting the reference of the Black king in the board.
     *
     * @return - The Black King piece object
     */
    public Piece getBlackKing() {
        return this.kings[0];
    }

    /**
     * Method for setting the Black King object. Null value is acceptable. This
     * method mostly used for replay/new game.
     *
     * @param king - The new Black King to be set
     */
    public void setBlackKing(Piece king) {
        this.kings[0] = king;
    }

    /**
     * Method for getting the reference of the White King in the board
     *
     * @return - The White King piece object
     */
    public Piece getWhiteKing() {
        return this.kings[1];
    }

    /**
     * Method for setting the White King object. Null value is acceptable. This
     * method mostly used for replay/new game.
     *
     * @param king - The new White King to be set
     */
    public void setWhiteKing(Piece king) {
        this.kings[1] = king;
    }

    /**
     * Method for calculating the potential position a Pawn can move to. This
     * method will populate the candidates list attribute in the Piece object,
     * so there no return value
     *
     * @param pawn - The pawn to be calculated
     */
    private void getPawnMove(Piece pawn) {
        /*
         * Algorithm explain: since the pawn can only move to one direction depend on the size, so we: 
         * 1. Calculate the unit vector depend on 'color' 
         * 2. Check if the square ahead can be moved to
         * 3. Edge case: at row 1 and row 6, a pawn can move to 2 squares ahead, so we also check for it
         * 4. Finally, we check for the two diagonal ahead, if there is an opponent piece there, count that square as valid
         * NOTE: A square that can be moved to must sastifies 3 criterias: 
         * a. That square still in the board 
         * b. There is no piece of our side blocking that square
         * c. The move shouldn't lead to a checked or checkmate 
         * (in this method, we won't check for checked or checkmate here. It will be calculated isChecked and isCheckmate methods)
         */

        //Clear the candidates list
        pawn.clearAllPositions();

        //Get the initial coordinates of pawn
        int row = pawn.getPosition().getRow(), col = pawn.getPosition().getCol();

        //Temporary piece object 
        Piece p;

        //The direction unit vector
        int forward = (pawn.getColor() == Color.BLACK) ? 1 : -1;

        //Check one square forward
        if (ModelLib.isCoorValid(row + forward, col) && getPieceAt(row + forward, col) == null) {
            pawn.addNewPosition(new Point(row + forward, col));

            //Check two squares forward if it's the pawn's first move (row 1 if BLACK or row 6 if WHITE)
            if ((pawn.getColor() == Color.BLACK && row == 1) || (pawn.getColor() == Color.WHITE && row == 6)) {
                if (ModelLib.isCoorValid(row + 2 * forward, col) && getPieceAt(row + 2 * forward, col) == null) {
                    pawn.addNewPosition(new Point(row + 2 * forward, col));
                }
            }
        }

        //Check diagonal captures
        Color oppositeColor = (pawn.getColor() == Color.BLACK) ? Color.WHITE : Color.BLACK;

        //Check right diagonal
        if (ModelLib.isCoorValid(row + forward, col + 1)) {
            p = getPieceAt(row + forward, col + 1);
            if (p != null && p.getColor() == oppositeColor) {
                pawn.addNewPosition(new Point(row + forward, col + 1));
            }
        }

        //Check left diagonal
        if (ModelLib.isCoorValid(row + forward, col - 1)) {
            p = getPieceAt(row + forward, col - 1);
            if (p != null && p.getColor() == oppositeColor) {
                pawn.addNewPosition(new Point(row + forward, col - 1));
            }
        }
    }

    /**
     * Method for calculating the potential position a Rook can move to. This
     * method will populate the candidates list attribute in the Piece object,
     * so there no return value
     *
     * @param rook - The rook to be calculated
     */
    private void getRookMove(Piece rook) {
        /*
         * Algorithm explain: The Rook can move horizontally or vertically, so we handle 4 direction: 
         * North (up -> row decrement),
         * East (right -> col increment),
         * South (down -> row increment),
         * West (left -> col decrement)
         * The flow of logic should be: 
         * 1. Calculate the directional unit vectors of each direction
         * 2. Move along each direction, if the square is valid (can be moved to), we add it to 'candidates' list
         */

        //Clear the moves list
        rook.clearAllPositions();

        //The direction 2D array, respectively: N, E, S, W {row, col}
        int[][] dir = {{-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        //The coordinate of the Rook
        int row = rook.getPosition().getRow();
        int col = rook.getPosition().getCol();
        Piece p;

        //We handle each directionin this order: N -> E-> S -> W
        for (int i = 0; i < 4; i++) {
            while (ModelLib.isCoorValid(row + dir[i][0], col + dir[i][1]) && getPieceAt(row + dir[i][0], col + dir[i][1]) == null) {
                //Update startPoint
                row += dir[i][0];
                col += dir[i][1];
                //Add to moves list
                rook.addNewPosition(new Point(row, col));
            }

            /*
             * If the while loop is break out, then there are two cases: 
             * 1. The NEXT square is outside the board (we don't care about this case)
             * 2. The NEXT square has an obsatcle (we're interested about this case)
             * Whichever the case is, we have to move forward 1 square before checking
             */
            row += dir[i][0];
            col += dir[i][1];

            //Check if the obstacle is our side or opponent side
            if (ModelLib.isCoorValid(row, col) && (p = getPieceAt(row, col)) != null && p.getColor() != rook.getColor()) {
                rook.addNewPosition(new Point(row, col));
            }

            //Reset the x and y coordinate to the rook position
            row = rook.getPosition().getRow();
            col = rook.getPosition().getCol();
        }
    }

    /**
     * Method for calculating the potential position a Knight can move to. This
     * method will populate the candidates list attribute in the Piece object,
     * so there no return value
     *
     * @param knight - The knight to be calculated
     */
    private void getKnightMove(Piece knight) {
        /*
         * Algorithm explain: since the Knight can oly move to AT MOST 8 squares, and the Knight cannot be blocked in its path
         * So we just have to checked for each square if it can be moved to
         */

        //Clear the moves list
        knight.clearAllPositions();

        //Get the intial position of knight
        int row = knight.getPosition().getRow();
        int col = knight.getPosition().getCol();

        //Get the color of the knight
        Color c = knight.getColor();

        //The temporary piece 
        Piece p;

        //(row - 1, col - 2)
        if (ModelLib.isCoorValid(row - 1, col - 2) && ((p = getPieceAt(row - 1, col - 2)) == null || p.getColor() != c)) {
            knight.addNewPosition(new Point(row - 1, col - 2));
        }

        //(row - 2, col - 1)
        if (ModelLib.isCoorValid(row - 2, col - 1) && ((p = getPieceAt(row - 2, col - 1)) == null || p.getColor() != c)) {
            knight.addNewPosition(new Point(row - 2, col - 1));
        }

        //(row - 2, col + 1)
        if (ModelLib.isCoorValid(row - 2, col + 1) && ((p = getPieceAt(row - 2, col + 1)) == null || p.getColor() != c)) {
            knight.addNewPosition(new Point(row - 2, col + 1));
        }

        //(row - 1, col + 2)
        if (ModelLib.isCoorValid(row - 1, col + 2) && ((p = getPieceAt(row - 1, col + 2)) == null || p.getColor() != c)) {
            knight.addNewPosition(new Point(row - 1, col + 2));
        }

        //(row + 1, col + 2)
        if (ModelLib.isCoorValid(row + 1, col + 2) && ((p = getPieceAt(row + 1, col + 2)) == null || p.getColor() != c)) {
            knight.addNewPosition(new Point(row + 1, col + 2));
        }

        //(row + 2, col + 1)
        if (ModelLib.isCoorValid(row + 2, col + 1) && ((p = getPieceAt(row + 2, col + 1)) == null || p.getColor() != c)) {
            knight.addNewPosition(new Point(row + 2, col + 1));
        }

        //(row + 2, col - 1)
        if (ModelLib.isCoorValid(row + 2, col - 1) && ((p = getPieceAt(row + 2, col - 1)) == null || p.getColor() != c)) {
            knight.addNewPosition(new Point(row + 2, col - 1));
        }

        //(row + 1, col - 2)
        if (ModelLib.isCoorValid(row + 1, col - 2) && ((p = getPieceAt(row + 1, col - 2)) == null || p.getColor() != c)) {
            knight.addNewPosition(new Point(row + 1, col - 2));
        }
    }

    /**
     * Method for calculating the potential position a Bishop can move to. This
     * method will populate the candidates list attribute in the Piece object,
     * so there no return value
     *
     * @param bishop - The bishop to be calculated
     */
    private void getBishopMove(Piece bishop) {
        /*
         * Algorithm explain: The Bishop can move diagonally, so we handle 4 direction:
         * North - West (up - left -> row decrement and col decrement),
         * North - East (up - right -> row decrement and col increment),
         * South - East (down - right -> row increment and col increment),
         * South - West (down - left -> row increment and col decrement)
         * The flow of logic should be: 
         * 1. Calculate the directional unit vectors of each direction
         * 2. Move along each direction, if the square is valid (can be moved to), we add it to 'candidates' list
         */

        //Clear the moves list
        bishop.clearAllPositions();

        //The direction 2D array, respectively: NW, NE, SE, SW {row, col}
        int[][] dir = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};

        //The coordinate of the Bishop
        int row = bishop.getPosition().getRow();
        int col = bishop.getPosition().getCol();
        Piece p;

        //We handle each directionin this order: NW -> NE-> SE -> SW
        for (int i = 0; i < 4; i++) {
            while (ModelLib.isCoorValid(row + dir[i][0], col + dir[i][1]) && getPieceAt(row + dir[i][0], col + dir[i][1]) == null) {
                //Update startPoint
                row += dir[i][0];
                col += dir[i][1];
                //Add to move
                bishop.addNewPosition(new Point(row, col));
            }

            /*
             * If the while loop is break out, then there are two cases: 
             * 1. The NEXT square is outside the board (we don't care about this case)
             * 2. The NEXT square has an obsatcle (we're interested about this case)
             * Whichever the case is, we have to move forward 1 square before checking
             */
            //Move to the next square
            row += dir[i][0];
            col += dir[i][1];

            //Check if the obstacle is our side or opponent side
            if (ModelLib.isCoorValid(row, col) && (p = getPieceAt(row, col)) != null && p.getColor() != bishop.getColor()) {
                bishop.addNewPosition(new Point(row, col));
            }

            //Reset the x and y to Bishop position
            row = bishop.getPosition().getRow();
            col = bishop.getPosition().getCol();
        }
    }

    /**
     * Method for calculating the potential position a Queen can move to. This
     * method will populate the candidates list attribute in the Piece object,
     * so there no return value
     *
     * @param queen - The queen to be calculated
     */
    private void getQueenMove(Piece queen) {
        /*
         * Algorithm explain: The Queen can move horizontally, vertically and diagonally, so we handle 8
         * direction:
         * North - West (up - left -> row decrement and col decrement),
         * North - East (up - right -> row decrement and col increment),
         * South - East (down - left -> row increment and col increment),
         * South - West (down - left -> row increment and col decrement),
         * North (up -> row decrement)
         * East (right -> col increment)
         * South (down -> row increment)
         * West (right -> col decrement)
         * The flow of logic should be: 
         * 1. Calculate the directional unit vectors of each direction
         * 2. Move along each direction, if the square is valid (can be moved to), we add it to 'candidates' list
         */

        //Clear the moves list
        queen.clearAllPositions();

        //The direction 2D array, respectively: NW, NE, SE, SW, N, E, S, W {rol, col}
        int[][] dir = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}, {-1, 0}, {0, 1}, {1, 0}, {0, -1}};

        //The coordinate of the Queen
        int row = queen.getPosition().getRow();
        int col = queen.getPosition().getCol();
        Piece p;

        //We handle each directionin this order: NW -> NE-> SE -> SW 
        for (int i = 0; i < 8; i++) {
            while (ModelLib.isCoorValid(row + dir[i][0], col + dir[i][1]) && getPieceAt(row + dir[i][0], col + dir[i][1]) == null) {
                //Update startPoint
                row += dir[i][0];
                col += dir[i][1];
                //Add to move
                queen.addNewPosition(new Point(row, col));
            }

            /*
             * If the while loop is break out, then there are two cases: 
             * 1. The NEXT square is outside the board (we don't care about this case)
             * 2. The NEXT square has an obsatcle (we're interested about this case)
             * Whichever the case is, we have to move forward 1 square before checking
             */
            row += dir[i][0];
            col += dir[i][1];

            //Check if the obstacle is our side or opponent side
            if (ModelLib.isCoorValid(row, col) && (p = getPieceAt(row, col)) != null && p.getColor() != queen.getColor()) {
                queen.addNewPosition(new Point(row, col));
            }

            //Reset the x and y coordinate to queen position
            row = queen.getPosition().getRow();
            col = queen.getPosition().getCol();
        }
    }

    /**
     * Method for calculating the potential position a King can move to. This
     * method will populate the candidates list attribute in the Piece object,
     * so there no return value
     *
     * @param king - The king to be calculated
     */
    private void getKingMove(Piece king) {
        /*
         * Algorithm explain: since the King can oly move to AT MOST 8 squares, and the King cannot be blocked
         * So we just have to checked for each square if it can be moved to
         */

        //Clear the moves list
        king.clearAllPositions();

        //Get the intial position of King
        int row = king.getPosition().getRow();
        int col = king.getPosition().getCol();

        //Get the color of the king
        Color c = king.getColor();

        //Temporary piece
        Piece p;

        //(row - 1, col - 1)
        if (ModelLib.isCoorValid(row - 1, col - 1) && ((p = getPieceAt(row - 1, col - 1)) == null || p.getColor() != c)) {
            king.addNewPosition(new Point(row - 1, col - 1));
        }

        //(row - 1, col)
        if (ModelLib.isCoorValid(row - 1, col) && ((p = getPieceAt(row - 1, col)) == null || p.getColor() != c)) {
            king.addNewPosition(new Point(row - 1, col));
        }

        //(row - 1, col + 1)
        if (ModelLib.isCoorValid(row - 1, col + 1) && ((p = getPieceAt(row - 1, col + 1)) == null || p.getColor() != c)) {
            king.addNewPosition(new Point(row - 1, col + 1));
        }

        //(row, col + 1)
        if (ModelLib.isCoorValid(row, col + 1) && ((p = getPieceAt(row, col + 1)) == null || p.getColor() != c)) {
            king.addNewPosition(new Point(row, col + 1));
        }

        //(row + 1, col + 1)
        if (ModelLib.isCoorValid(row + 1, col + 1) && ((p = getPieceAt(row + 1, col + 1)) == null || p.getColor() != c)) {
            king.addNewPosition(new Point(row + 1, col + 1));
        }

        //(row + 1, col)
        if (ModelLib.isCoorValid(row + 1, col) && ((p = getPieceAt(row + 1, col)) == null || p.getColor() != c)) {
            king.addNewPosition(new Point(row + 1, col));
        }

        //(row + 1, col - 1)
        if (ModelLib.isCoorValid(row + 1, col - 1) && ((p = getPieceAt(row + 1, col - 1)) == null || p.getColor() != c)) {
            king.addNewPosition(new Point(row + 1, col - 1));
        }

        //(row, col - 1)
        if (ModelLib.isCoorValid(row, col - 1) && ((p = getPieceAt(row, col - 1)) == null || p.getColor() != c)) {
            king.addNewPosition(new Point(row, col - 1));
        }
    }

    /**
     * Method for update the candidates list of a piece based on their Rank.
     *
     * @param piece - The piece to be updated
     */
    private void updateMove(Piece piece) {
        switch (piece.getRank()) {
            case PAWN: 
                getPawnMove(piece);
                break;
            case ROOK:
                getRookMove(piece);
                break;
            case KNIGHT:
                getKnightMove(piece);
                break;
            case BISHOP:
                getBishopMove(piece);
                break;
            case QUEEN:
                getQueenMove(piece);
                break;
            case KING:
                getKingMove(piece);
                break;
            default:
                break;
        }
    }

    /**
     * Method for updating all candidates list of all pieces in the board.
     */
    public void updateAllMoves() {
        // Update affected piece
        Piece piece;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                piece = getPieceAt(row, col);
                //If we found a piece, update its candidates list
                if (piece != null) {
                    updateMove(piece);
                }
            }
        }
    }

    /**
     * Method for calculating if a King is being checked
     *
     * @param king - The King for calculation
     * @return - The boolean value, true if the King is being checked, false
     * otherwise
     */
    public boolean isChecked(Piece king) {
        /*
         * Algorithm explain: We perform a loop through the board and check 3 things: 
         * 1. If the current square has no piece or our side (same color), ignore
         * 2. If we found the opponent piece, then we check if that opponent's candidates list contains the king's position
         * 3. If we can find at least one opponent that is currently checking the King, return true, else return false;
         */

        Piece p;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                //Ignore non-oppponent piece
                if ((p = getPieceAt(row, col)) == null || p.getColor() == king.getColor()) {
                    continue;
                }

                //Check if the King cooridnate is in the moves list
                if (p.hasPosition(king.getPosition())) {
                    //If yes, then we simply return here, no need to check more
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * Method for simulating a move.
     *
     * @param piece - The piece for simulate move
     * @param row - The destination row
     * @param col - The destination column
     * @return - The boolean value, true if the move is valid (the move didn't
     * lead to a checked state for the King), false otherwise
     */
    public boolean simulateMove(Piece piece, int row, int col) {
        /*
         * Algorithm explain: The simulate move method, which will check if the move is valid before actualy move the piece
         * The criterias for a valid move are:
         * 1. The destination square must be valid (the new position is in the candidates list)
         * 2. The King is in checked state, and the move save the King
         * 3. The King is not in checked state, and the move didn't lead to checked state
         * Here, we assume that the row and col (destination position) is valid, so we check for the other criterias
         */

        //First, get the old coordinate of the current selected piece
        int oldRow = piece.getPosition().getRow();
        int oldCol = piece.getPosition().getCol();

        /*We perform the simulated move*/
        //Get the king 
        Piece king = piece.getColor() == Color.BLACK ? this.kings[0] : this.kings[1];

        //Set the current square that the selected piece is in to null
        this.board[oldRow][oldCol] = null;

        //Get the taken piece (if existed)
        Piece takenPiece = getPieceAt(row, col);

        //Update the internal coordinate of the selected piece
        piece.setPosition(new Point(row, col));

        //Move the piece 
        this.board[row][col] = piece;

        //Update the moves list of all piece (since the state of the board is changed, so we have to update)
        updateAllMoves();

        //Get the new state of the King
        boolean newState = isChecked(king);

        //Whether or not the move is valid, we RESTORE the ORIGINAL state of the board, since this is just a simulation
        piece.setPosition(new Point(oldRow, oldCol));
        this.board[oldRow][oldCol] = piece;
        this.board[row][col] = takenPiece;
        updateAllMoves();

        return !newState;
    }

    /**
     * Method for calculating if the King is being checkmate.
     *
     * @param king - The King for calculation
     * @return - The boolean, true if the king is being checkmate, false
     * otherwise
     */
    public boolean isCheckmate(Piece king) {
        /*
         * The idea for this algorithm is:
         * 1. First, we check if the king is being checked using isChecked method
         * 2. If yes, then we check that, for all the moves that the King can make, is there a move that can make the
         * King to escaped from being checked
         * 3. If no (all squares around the King can move still make the King checked), then can any piece of our side can capture
         * the opponent
         * 4. If no either, then we finally check that, if any piece in our side can block the path of the opponent 
         * (Note that, the 3. and 4. only true if the King is only checked by at most 1 opponent)
         * NOTE: there is a case, where the King is not checked, but it cannot make any valid moves (Every squares it can move to
         * will lead to a checked). In the official rule, that would be statement, but for simplicity, we still count that as lost 
         */

        //We have to create a copy of King's move instead of using it directly, since the simulateMove is modifying 
        //the king's candidates list directly (by calling updateAllMoves method), so if we the king's candidates list directly
        //it will throw ConcurrentModificationException
        ArrayList<Point> temp = new ArrayList<>(king.getCandidates());
        for (Point p : temp) {
            if (simulateMove(king, p.getRow(), p.getCol())) {
                //If found at least one move to escape, then there is no need to check more
                return false;
            }
        }

        /*
         * Next, we check if we can take the opponent piece to save the King
         * This is only in the case where the King currently be checked
         */
        if (isChecked(king)) {
            //First, we found the current opponent that is checking the king
            Piece opponent = null;
            boolean found = false;
            for (int row = 0; row < 8 && !found; row++) {
                for (int col = 0; col < 8 && !found; col++) {
                    opponent = getPieceAt(row, col);
                    if (opponent != null && opponent.getColor() != king.getColor() && opponent.hasPosition(king.getPosition())) {
                        found = true;
                    }
                }
            }

            //Next, we find a piece in our side that has the opponent position in the candidates list, and perform simulate move
            Piece ourPiece;
            for (int row = 0; row < 8; row++) {
                for (int col = 0; col < 8; col++) {
                    ourPiece = getPieceAt(row, col);
                    //Since the king is being checked -> opponent must exist -> there is no need to handle NullPointerException
                    if (ourPiece != null && ourPiece.getColor() == king.getColor() && ourPiece.hasPosition(opponent.getPosition())) {
                        //We check that the our piece can actually capture the opponent
                        if (ourPiece.hasPosition(new Point(opponent.getPosition().getRow(), opponent.getPosition().getCol()))) {
                            //We check if taking the opponent piece still make the King being checked
                            if (simulateMove(ourPiece, opponent.getPosition().getRow(), opponent.getPosition().getCol())) {
                                return false;
                            }
                        }
                    }
                }
            }

            //Check if we can block the path of the opponent (only for Rook, Bishop or Queen)
            if (opponent.getRank() == Rank.ROOK || opponent.getRank() == Rank.BISHOP || opponent.getRank() == Rank.QUEEN) {
                //Calculate the direction in which the opponent piece is checking the king
                int rowDiff = Integer.compare(king.getPosition().getRow(), opponent.getPosition().getRow());
                int colDiff = Integer.compare(king.getPosition().getCol(), opponent.getPosition().getCol());

                for (int row = 0; row < 8; row++) {
                    for (int col = 0; col < 8; col++) {
                        int curRow = opponent.getPosition().getRow() + rowDiff;
                        int curCol = opponent.getPosition().getCol() + colDiff;

                        while (curRow != king.getPosition().getRow() || curCol != king.getPosition().getCol()) {
                            //For each square along the path, check if one of our pieces can block the check piece
                            ourPiece = getPieceAt(row, col);
                            if (ourPiece != null && ourPiece.getColor() == king.getColor() && ourPiece.hasPosition(new Point(curRow, curCol))) {
                                //Check if the current position in the opponent path can be reach by the current our piece
                                if (ourPiece.hasPosition(new Point(curRow, curCol))) {
                                    //If yes, the we perform simulate move (simulate move is true if that move didn't lead to check)
                                    if (simulateMove(ourPiece, curRow, curCol)) {
                                        return false;
                                    }
                                }
                            }

                            //Move along the path
                            curRow += rowDiff;
                            curCol += colDiff;
                        }
                    }
                }
            }
        } else {
            //If the King is not checked, then it cannot be end game
            return false;
        }

        //If the King cannot make a move, or there is no other piece of our side can solve the checked state, end game
        return true;
    }

    /**
     * Method for moving the selected piece.
     *
     * @param row - The destination row
     * @param col - The destination column
     * @return - The Piece being captured by that move, null if the move didn't
     * capture any piece.
     */
    public Piece movePiece(int row, int col) {
        //Record the old position of selected piece
        int oldRow = this.selectedPiece.getPosition().getRow();
        int oldCol = this.selectedPiece.getPosition().getCol();

        Piece takenPiece = null;
        //Set the old position to null
        this.board[oldRow][oldCol] = null;
        //Update the internal position of the piece
        this.selectedPiece.setPosition(new Point(row, col));
        //We record the taken piece (if has) and return it later
        takenPiece = getPieceAt(row, col);
        //We set the new position to selected piece
        this.board[row][col] = this.selectedPiece;

        return takenPiece;
    }

    /**
     * Method for pawn promotion.
     *
     * @param newRank - The new Rank of the pawn
     */
    public void promote(Rank newRank) {
        /*
         * Algorithm explain: We first found the pawn at the final row. 
         * Logically, whenever a pawn reach the end side, this method must be called. We assume that the logic is handled correctly
         * so there is no need to worry about multiple pawn at the same end 
         * Then we just have to change the Type to whatever the user select, and update it moves list
         */
        Piece pawn;
        for (int col = 0; col < 8; col++) {
            if (this.selectedPiece.getColor() == Color.WHITE) {
                pawn = getPieceAt(0, col);
            } else {
                pawn = getPieceAt(7, col);
            }
            //If we find a pawn, update rank
            if (pawn != null && pawn.getRank() == Rank.PAWN) {
                //Set the type
                pawn.setRank(newRank);
                updateMove(pawn);
                return;
            }
        }
    }

    /**
     * Method for drawing the chessboard.
     *
     * @param g2 - The Graphics2D object for drawing
     */
    private void drawBoard(Graphics2D g2) {
        boolean isLight = true;
        int size = 100; //square size in pixel
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                g2.setColor(isLight ? new java.awt.Color(210, 165, 125) : new java.awt.Color(175, 115, 70));
                g2.fillRect(i * size, j * size, size, size);
                isLight = !isLight;
            }
            isLight = !isLight;
        }
    }

    /**
     * Method for drawing the pieces
     *
     * @param g2 - The Graphics2D object for drawing
     */
    private void drawPieces(Graphics2D g2) {
        Piece p;
        for (int row = 0; row < 8; row++) {
            for (int col = 0; col < 8; col++) {
                p = getPieceAt(row, col);
                //If we found a piece
                if (p != null) {
                    //Get image
                    p.getImage();
                    //Calculate the yOffSet (if the piece is selected piece)
                    int yOffSet = 0;
                    if (p.equals(this.selectedPiece)) {
                        yOffSet = this.selectedPiece.getColor().equals(Color.BLACK) ? 10 : -10;
                    }
                    p.draw(g2, yOffSet);
                }
            }
        }
    }

    /**
     * Method for highlighting the squares that the selected piece can move to
     * (the position in candidates list).
     *
     * @param g2 - The Graphics2D object for drawing
     */
    private void highlight(Graphics2D g2) {
        //Only when a piece is selected that we need highlight
        int size = 100;
        if (this.selectedPiece != null) {
            for (Point pos : this.selectedPiece.getCandidates()) {
                g2.setColor(new java.awt.Color(230, 255, 230, 70));
                //x coordinate is associated with COLUMN and y coordinate is associated with ROW
                g2.fillOval(pos.getCol() * size + size / 4, pos.getRow() * size + size / 4, size / 2, size / 2);
            }
        }
    }

    /**
     * Method for updating the UI, which will called to the paintComponent
     * method internally.
     */
    public void update() {
        //These two method will called to paintComponent is the background
        revalidate();
        repaint();
    }

    /**
     * Overridden paintComponent method, the actual one responsible for drawing
     * the UI
     *
     * @param g The Graphics object for drawing
     */
    @Override
    public void paintComponent(Graphics g) {
        //Called to super method
        super.paintComponent(g);

        //Draw the board, pieces and highlight
        drawBoard((Graphics2D) g);
        drawPieces((Graphics2D) g);
        highlight((Graphics2D) g);
    }
}
