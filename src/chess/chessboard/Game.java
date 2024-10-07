/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import chess.Chess;
import chess.util.ModelLib;
import chess.util.ViewLib;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import javax.imageio.ImageIO;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

/**
 *
 * @author Asus
 */
public class Game extends JPanel implements Listener {

    //The ChessBoard object
    private final ChessBoard chessboard;
    //The SidePanel object
    private final SidePanel sidePanel;
    //The ArrayList that store the move of players in each game
    private final ArrayList<String> moveRecords;
    //The turn of player
    private boolean isWhiteTurn;
    //The name of players
    private String player1, player2;
    //The reference to the Chess object, for returning to menu operation
    private final Chess app;

    /**
     * Helper method, setup the layout of Game panel.
     */
    private void initLayout() {
        //Set layout to GridBagLayout
        this.setLayout(new GridBagLayout());
        GridBagConstraints gbc = new GridBagConstraints();

        //Set up constraints for ChessBoardView (center panel)
        gbc.gridx = 0;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 20, 20, 20);
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weightx = 0.75;
        gbc.weighty = 1.0;
        this.add(this.chessboard, gbc);

        //Set up constraints for SidePanelView (right panel)
        gbc.gridx = 1;
        gbc.gridy = 0;
        gbc.gridwidth = 1;
        gbc.gridheight = 1;
        gbc.insets = new Insets(20, 0, 20, 20);
        gbc.fill = GridBagConstraints.VERTICAL;
        gbc.weightx = 0.25;
        gbc.weighty = 1.0;
        this.add(this.sidePanel, gbc);
    }

    /**
     * The constructor of Game class, initialize the component objects and setup
     * layout.
     *
     * @param app - The reference to the Chess object
     */
    public Game(Chess app) {
        this.app = app;

        //Initialize the chessboard
        this.chessboard = new ChessBoard();
        //Register the listener for chessboard
        this.chessboard.registerListener(this);

        //Initialize the side panel
        this.sidePanel = new SidePanel();
        //Register this class as the listener to side panel
        this.sidePanel.registerListener(this);

        //Initialize empty history list
        this.moveRecords = new ArrayList<>();

        //Set the turn to WHITE first
        this.isWhiteTurn = true;

        //Initialize the layout
        initLayout();
    }

    /**
     * Helper method, used to setup a new chessboard in default state.
     */
    private void initNewBoard() {
        //Clear the board before setup new board
        this.chessboard.clearBoard();

        //Set pawns at two sides
        for (int col = 0; col < 8; col++) {
            this.chessboard.setPieceAt(new Piece(Color.BLACK, Rank.PAWN, 1, col));
            this.chessboard.setPieceAt(new Piece(Color.WHITE, Rank.PAWN, 6, col));
        }

        //Set rooks at two sides
        this.chessboard.setPieceAt(new Piece(Color.BLACK, Rank.ROOK, 0, 0));
        this.chessboard.setPieceAt(new Piece(Color.BLACK, Rank.ROOK, 0, 7));
        this.chessboard.setPieceAt(new Piece(Color.WHITE, Rank.ROOK, 7, 0));
        this.chessboard.setPieceAt(new Piece(Color.WHITE, Rank.ROOK, 7, 7));

        //Set knights at two sides
        this.chessboard.setPieceAt(new Piece(Color.BLACK, Rank.KNIGHT, 0, 1));
        this.chessboard.setPieceAt(new Piece(Color.BLACK, Rank.KNIGHT, 0, 6));
        this.chessboard.setPieceAt(new Piece(Color.WHITE, Rank.KNIGHT, 7, 1));
        this.chessboard.setPieceAt(new Piece(Color.WHITE, Rank.KNIGHT, 7, 6));

        //Set the bishops at two sides
        this.chessboard.setPieceAt(new Piece(Color.BLACK, Rank.BISHOP, 0, 2));
        this.chessboard.setPieceAt(new Piece(Color.BLACK, Rank.BISHOP, 0, 5));
        this.chessboard.setPieceAt(new Piece(Color.WHITE, Rank.BISHOP, 7, 2));
        this.chessboard.setPieceAt(new Piece(Color.WHITE, Rank.BISHOP, 7, 5));

        //Set the queens at two sides
        this.chessboard.setPieceAt(new Piece(Color.BLACK, Rank.QUEEN, 0, 3));
        this.chessboard.setPieceAt(new Piece(Color.WHITE, Rank.QUEEN, 7, 3));

        //Set the kings at two sides
        Piece blackKing = new Piece(Color.BLACK, Rank.KING, 0, 4);
        this.chessboard.setPieceAt(blackKing);
        Piece whiteKing = new Piece(Color.WHITE, Rank.KING, 7, 4);
        this.chessboard.setPieceAt(whiteKing);

        //Register the kings into kings array
        this.chessboard.setBlackKing(blackKing);
        this.chessboard.setWhiteKing(whiteKing);

        //Update all the pieces candidates list
        this.chessboard.updateAllMoves();
    }

    /**
     * Method for loading data from data directory. If there is no data (new
     * player), it will setup the default board.
     *
     * @param player1 - The player1's name
     * @param player2 - The player2's name
     */
    public void load(String player1, String player2) {
        //By default, player1 is WHITE
        this.player1 = player1;
        this.player2 = player2;

        //Initialize the default board first
        initNewBoard();
        //this.chessboard.update();

        try {
            //Read the files and recalculate the move to get to the state
            Scanner sc = new Scanner(new File(String.format("./data/incomplete/%s_%s.txt", this.player1, this.player2)));
            String[] move;
            String line;
            int oldR, oldC, newR, newC;
            while (sc.hasNextLine()) {
                line = sc.nextLine();
                //Add to move records 
                this.moveRecords.add(line);
                //Split the line to array for processing
                move = line.split("\\s+");
                //Check which action: move or promote
                if (move[0].equalsIgnoreCase("Promote")) {
                    Rank rank = null;
                    if (move[2].equalsIgnoreCase("QUEEN")) {
                        rank = Rank.QUEEN;
                    } else if (move[2].equalsIgnoreCase("BISHOP")) {
                        rank = Rank.BISHOP;
                    } else if (move[2].equalsIgnoreCase("KNIGHT")) {
                        rank = Rank.KNIGHT;
                    } else if (move[2].equalsIgnoreCase("ROOK")) {
                        rank = Rank.ROOK;
                    }
                    this.chessboard.promote(rank);
                } else {
                    //First, select the piece
                    oldR = (int) (move[2].charAt(1) - '0');
                    oldC = (int) (move[2].charAt(3) - '0');
                    this.chessboard.setSelectedPiece(this.chessboard.getPieceAt(oldR, oldC));
                    //Since this is the history record, it was guarantee to be valid move -> simply move the piece
                    newR = (int) (move[5].charAt(1) - '0');
                    newC = (int) (move[5].charAt(3) - '0');
                    this.chessboard.movePiece(newR, newC);
                    //Change turn 
                    this.isWhiteTurn = !this.isWhiteTurn;
                }
            }

            //Release the piece and update all pieces candidates list
            this.chessboard.setSelectedPiece(null);
            this.chessboard.updateAllMoves();
        } catch (FileNotFoundException e) {
        }

        //Update the chessboard and side panel UI
        this.chessboard.update();
        this.moveRecords.forEach(this.sidePanel::addMove);
        String turn = String.format("%s turn", this.isWhiteTurn ? this.player1 + ": WHITE's" : this.player2 + ": BLACK's");
        this.sidePanel.changeTurn(turn);
    }

    /**
     * Method for saving the game history into files.
     *
     * @param path - The path to the file for saving data
     */
    public void save(String path) {
        try {
            FileWriter writer = new FileWriter(path);
            StringBuilder result = new StringBuilder();
            this.moveRecords.forEach(s -> {
                result.append(s);
                result.append("\n");
            });
            writer.write(result.toString());
            writer.close();
        } catch (IOException ex) {
            System.out.println("File not found");
        }
    }

    /**
     * Implemented method in interface Listener: method for handling ChessBoard
     * interaction.
     *
     * @param x - The x coordinate where the mouse is clicking
     * @param y - The y coordinate where the mouse is clicking
     */
    @Override
    public void onChessboardClick(int x, int y) {
        int size = 100; //The size of the square in pixel
        int row = y / size; //y coordinate is associated with row
        int col = x / size; //x coordinate is associated with col

        /*
         * Decide each action should be take based on the game current state and the row and col.
         * The flow woulf be: 
         * 1. Check for move action first
         * 2. Check for release action then
         * 3. Finally, check for selected action
         */
        Piece selectedPiece = this.chessboard.getSelectedPiece();

        //Check for move action
        if (selectedPiece != null && selectedPiece.hasPosition(new Point(row, col))) {
            //First check if the move is valid by running simulate move
            if (this.chessboard.simulateMove(selectedPiece, row, col)) {
                //Add move to move records 
                this.moveRecords.add(String.format("%s moves to (%d,%d)", selectedPiece.toString(), row, col));

                //If this move is valid, perform the actual move
                this.chessboard.movePiece(row, col);

                //If this move leads to Pawn promotion, run Pawn promotion
                boolean hasPromotion = selectedPiece.getRank() == Rank.PAWN && (row == 0 || row == 7);
                Rank promoteRank = null;
                if (hasPromotion) {
                    promoteRank = showPromotionPopup();
                    this.moveRecords.add("Promote to " + promoteRank);
                }
                //Update all piece's candidates list
                this.chessboard.updateAllMoves();

                //After moving, we release the piece
                this.chessboard.setSelectedPiece(null);

                //Change turn
                this.isWhiteTurn = !this.isWhiteTurn;

                /*Update the view*/
                this.chessboard.update();
                String turn = String.format("%s turn", this.isWhiteTurn ? this.player1 + ": WHITE's" : this.player2 + ": BLACK's");
                this.sidePanel.changeTurn(turn);
                int len = this.moveRecords.size();
                if (hasPromotion) {
                    this.sidePanel.addMove(this.moveRecords.get(len - 2));
                }
                this.sidePanel.addMove(this.moveRecords.get(len - 1));

                //If the new move lead to a checkmate, show popup
                if (this.chessboard.isCheckmate(this.isWhiteTurn ? this.chessboard.getWhiteKing() : this.chessboard.getBlackKing())) {
                    //Since this is a checkmate -> end game -> complete folder
                    String path = String.format("./data/complete/%s_%s_%s.txt",
                            this.player1, this.player2, ModelLib.getCurrentDateTime());
                    save(path);
                    showEndgamePopup(!this.isWhiteTurn ? "WHITE" : "BLACK");
                    return;
                }

                //If the new move check the opponent, update it in the side panel (but not adding it to the move records)
                if (this.chessboard.isChecked(this.isWhiteTurn ? this.chessboard.getWhiteKing() : this.chessboard.getBlackKing())) {
                    this.sidePanel.addMove("Checked!");
                }

                //Update the view
                return;
            }
        }

        //If this is not move action, then we check that if the user trying to click the opponent piece (invalid action -> ignore)
        Piece clickedPiece = this.chessboard.getPieceAt(row, col);
        if (clickedPiece == null || clickedPiece.getColor() != (this.isWhiteTurn ? Color.WHITE : Color.BLACK)) {
            return;
        }

        //Else, if the clicked piece is not null, and the selected piece == clicked piece -> release
        if (selectedPiece != null && selectedPiece.equals(clickedPiece)) {
            this.chessboard.setSelectedPiece(null);
            this.chessboard.update();
            return;
        }

        //Finally, if none of the above is correct, then it must be the select action
        this.chessboard.setSelectedPiece(clickedPiece);
        this.chessboard.update();
    }

    /**
     * Method for showing the pawn promotion pop up.
     *
     * @return - The Rank that player selected
     */
    private Rank showPromotionPopup() {
        //We use JDialog for the popup
        JDialog popup = new JDialog(
                SwingUtilities.getWindowAncestor(this), //The ancestor of the popup would be this class (Game)
                "Pawn Promotion", //The title
                Dialog.ModalityType.APPLICATION_MODAL); //Modal: you can't interact with the ancestor unless closing the popup

        //Set the size of the popup to 600 x 400 pixel
        popup.setSize(new Dimension(600, 400));

        //Set the layout of the popup to BorderLayout
        popup.setLayout(new BorderLayout());

        //Create panel for piece selection
        JPanel piecePanel = new JPanel(new GridLayout(1, 4, 10, 10));

        //Load queen image for queen button
        JButton queenBtn = new JButton();
        try {
            String path = String.format("/resource/chess/%s-queen.png", this.isWhiteTurn ? "w" : "b");
            queenBtn.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream(path))));
        } catch (IOException e) {
            System.out.println("Error getiing image");
            System.out.println(e);
        }

        //Load bishop image for bishop button
        JButton bishopBtn = new JButton();
        try {
            String path = String.format("/resource/chess/%s-bishop.png", this.isWhiteTurn ? "w" : "b");
            bishopBtn.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream(path))));
        } catch (IOException e) {
            System.out.println("Error getiing image");
            System.out.println(e);
        }

        //Load knight image for knight button
        JButton knightBtn = new JButton();
        try {
            String path = String.format("/resource/chess/%s-knight.png", this.isWhiteTurn ? "w" : "b");
            knightBtn.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream(path))));
        } catch (IOException e) {
            System.out.println("Error getiing image");
            System.out.println(e);
        }

        //Load rook image for rook button
        JButton rookBtn = new JButton();
        try {
            String path = String.format("/resource/chess/%s-rook.png", this.isWhiteTurn ? "w" : "b");
            rookBtn.setIcon(new ImageIcon(ImageIO.read(getClass().getResourceAsStream(path))));
        } catch (IOException e) {
            System.out.println("Error getiing image");
            System.out.println(e);
        }

        //Add action listeners for selecting pieces
        final Rank[] promoteRank = new Rank[1]; //We need a final variable to use in a lambda expression, so we use a final array
        queenBtn.addActionListener((ActionEvent e) -> {
            this.chessboard.promote(Rank.QUEEN);
            promoteRank[0] = Rank.QUEEN;
            popup.dispose();
        });
        bishopBtn.addActionListener((ActionEvent e) -> {
            this.chessboard.promote(Rank.BISHOP);
            promoteRank[0] = Rank.BISHOP;
            popup.dispose();
        });
        knightBtn.addActionListener((ActionEvent e) -> {
            this.chessboard.promote(Rank.KNIGHT);
            promoteRank[0] = Rank.KNIGHT;
            popup.dispose();
        });
        rookBtn.addActionListener((ActionEvent e) -> {
            this.chessboard.promote(Rank.ROOK);
            promoteRank[0] = Rank.ROOK;
            popup.dispose();
        });

        //Add buttons to the piecePanel
        piecePanel.add(queenBtn);
        piecePanel.add(bishopBtn);
        piecePanel.add(knightBtn);
        piecePanel.add(rookBtn);

        //Add the piecePanel to the popup
        popup.add(piecePanel, BorderLayout.CENTER);

        //Center the dialog to the chessboard view
        popup.setLocationRelativeTo(this.chessboard);
        popup.setVisible(true);

        return promoteRank[0];
    }

    /**
     * Method for showing the endgame pop up.
     *
     * @param winner - The winner to be displayed in the pop up
     */
    private void showEndgamePopup(String winner) {
        //We use the JDialog for this 
        JDialog popup = new JDialog(
                SwingUtilities.getWindowAncestor(this), //The ancestor of the popup would be this class (Game)
                "Game Over", //The title
                Dialog.ModalityType.APPLICATION_MODAL); //Modal: you can't interact with the ancestor unless closing the popup

        //Set up the main panel with a gradient background
        JPanel mainPanel = new JPanel();
//            @Override
//            protected void paintComponent(Graphics g) {
//                super.paintComponent(g);
//                Graphics2D g2d = (Graphics2D) g;
//                int w = getWidth(), h = getHeight();
//                GradientPaint gp = new GradientPaint(0, 0, new java.awt.Color(240, 240, 240), 0, h, new java.awt.Color(200, 200, 200));
//                g2d.setPaint(gp);
//                g2d.fillRect(0, 0, w, h);
//            }
//        };

        //Set the layout of main panel to BoxLayout and stacking vertically
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));

        //Set the border to empty border (still exist but not visible)
        mainPanel.setBorder(new EmptyBorder(20, 20, 20, 20));

        //Create and style label
        JLabel resultLabel = new JLabel(winner + " wins!");
        resultLabel.setFont(new Font("Arial", Font.BOLD, 35));
        resultLabel.setForeground(new java.awt.Color(50, 50, 50));
        resultLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Create and styling the replay button
        JButton replayButton = ViewLib.createButton("Replay");
        replayButton.addActionListener((ActionEvent e) -> {
            //If replay, we reset the state of the game
            initNewBoard();
            this.moveRecords.clear();
            this.isWhiteTurn = true;
            this.chessboard.update();
            this.sidePanel.clearMoves();
            this.sidePanel.changeTurn("WHITE's turn");
            popup.dispose();
        });

        //Create and styling the exit button
        JButton exitButton = ViewLib.createButton("Menu");
        exitButton.addActionListener((ActionEvent e) -> {
            this.app.returnToMenu();
            popup.dispose();
        });

        //Override the size of the button (The method in ViewLib has the button bigger, and we need smaller here)
        Dimension buttonSize = new Dimension(120, 40);
        replayButton.setPreferredSize(buttonSize);
        exitButton.setPreferredSize(buttonSize);

        //Create a panel for buttons with FlowLayout
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 0));
        buttonPanel.setOpaque(false);
        buttonPanel.add(replayButton);
        buttonPanel.add(exitButton);

        //Add components to the main panel
        mainPanel.add(Box.createVerticalGlue());
        mainPanel.add(resultLabel);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 40)));
        mainPanel.add(buttonPanel);
        mainPanel.add(Box.createVerticalGlue());

        //Set up the dialog attributes
        popup.setContentPane(mainPanel);
        popup.setSize(400, 300);
        popup.setLocationRelativeTo(this);
        popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        //Show the dialog
        popup.setVisible(true);
    }

    /**
     * Implemented method in interface Listener: method for handling Save Game
     * button interaction.
     */
    @Override
    public void onSaveGame() {
        //Since we want to save game for later, it will be in the incomplete folder
        String path = String.format("./data/incomplete/%s_%s.txt", this.player1, this.player2);
        save(path);
        System.exit(0);
    }

}
