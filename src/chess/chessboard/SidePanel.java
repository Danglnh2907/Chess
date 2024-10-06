/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;

/**
 *
 * @author Asus
 */
public class SidePanel extends JPanel {

    //The label signify player turn
    private final JLabel playerTurn;
    //The text area for displaying each move players make
    private final JTextArea moveRecords;
    //The save game button
    private final JButton saveBtn;
    //The Listener object (in this case, Game class), used to communicate between SidePanel and Game
    private Listener ls;

    /**
     * Helper method, setup layout of JPanel and its components.
     */
    private void initLayout() {
        //Set the size to 400 x 800 pixel
        this.setPreferredSize(new Dimension(400, 800));

        //Set the layout manager (BoxLayout arranges component verticals)
        this.setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));

        //Style the layout of the player turn
        this.playerTurn.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.playerTurn.setFont(new Font("Arial", Font.BOLD, 24));
        this.playerTurn.setBorder(BorderFactory.createEmptyBorder(20, 0, 20, 0));

        //Create the non-editable text area
        this.moveRecords.setEditable(false);
        this.moveRecords.setLineWrap(true);
        this.moveRecords.setWrapStyleWord(true);
        this.moveRecords.setFont(new Font("Arial", Font.PLAIN, 16));
        this.moveRecords.setBorder(BorderFactory.createLineBorder(java.awt.Color.BLACK));

        //Add the text area inside a scroll pane for scrolling
        JScrollPane scrollPane = new JScrollPane(this.moveRecords);
        scrollPane.setPreferredSize(new Dimension(380, 600));

        //Style the save button
        this.saveBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.saveBtn.setFont(new Font("Arial", Font.PLAIN, 18));
        this.saveBtn.setMaximumSize(new Dimension(200, 50));
        this.saveBtn.addActionListener((ActionEvent e) -> {
            this.ls.onSaveGame();
        });

        //Add the components to the panel
        this.add(this.playerTurn);
        this.add(scrollPane);
        this.add(Box.createVerticalStrut(20));
        this.add(this.saveBtn);
    }

    /**
     * Constructor of SidePanel class.
     */
    public SidePanel() {
        this.playerTurn = new JLabel("WHITE's turn");
        this.moveRecords = new JTextArea();
        this.saveBtn = new JButton("Save game");
        initLayout();

        //Initialize listener as null
        this.ls = null;
    }

    /**
     * Method for register a listener object.
     *
     * @param ls - The listener object
     */
    public void registerListener(Listener ls) {
        this.ls = ls;
    }

    /**
     * Method for adding new move to the JTextArea for displaying.
     *
     * @param move - The new move to be added
     */
    public void addMove(String move) {
        this.moveRecords.setText(this.moveRecords.getText() + "\n" + move);
    }

    /**
     * The method for clearing the move records. 
     */
    public void clearMoves() {
        this.moveRecords.setText("");
    }

    /**
     * The method for update the player's turn. 
     * @param playerTurn - Current player turn
     */
    public void changeTurn(String playerTurn) {
        this.playerTurn.setText(playerTurn);
    }
}
