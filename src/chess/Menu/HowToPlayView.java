/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.Menu;

import chess.Chess;
import chess.util.ViewLib;
import java.awt.Color;
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
 * The view of How To Play
 *
 * @author Asus
 */
public class HowToPlayView extends JPanel {

    //The header (title) of this view
    private final JLabel header;
    //The content
    private final JTextArea howToPlay;
    //The return to menu button
    private final JButton returnBtn;
    //The reference to the Chess object, which will be used to called the handler method
    private final Chess app;

    /**
     * Helper method for setup the layout of the JPanel and its components.
     */
    private void initLayout() {
        //Set the layout of the JPanel (stack vertically)
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //Set the background of JPanel
        setBackground(new Color(240, 240, 240));

        //Add the header and styling
        add(Box.createVerticalStrut(30));
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(header);
        add(Box.createVerticalStrut(30));

        //Add the about us JTextArea to a JScrollPanel (allow scrolling) and styling
        JScrollPane scrollPane = new JScrollPane(howToPlay);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(1200, 500));
        scrollPane.setMaximumSize(new Dimension(1200, 500));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        add(scrollPane);

        //Add return button and styling
        add(Box.createVerticalStrut(30));
        returnBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(returnBtn);

        add(Box.createVerticalGlue());
    }

    /**
     * The constructor of HowToPlayView.
     *
     * @param app - The reference of the Chess object
     */
    public HowToPlayView(Chess app) {
        this.app = app;

        //Initialize the header
        this.header = ViewLib.createLabel("About Us", 36);
        this.header.setForeground(new Color(50, 50, 50));

        //Set content and JTextArea's attribute
        this.howToPlay = new JTextArea();
        this.howToPlay.setEditable(false); //Not editable
        this.howToPlay.setLineWrap(true); //The line will be wrap (if it too long, whe word/character will be pushed to the next line)
        this.howToPlay.setWrapStyleWord(true); //The whole word will be push to the next line instead of breaking the word
        this.howToPlay.setFont(new Font("Arial", Font.PLAIN, 20)); //Set font
        this.howToPlay.setBackground(new Color(250, 250, 250)); //Set background
        this.howToPlay.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //Set the border
        this.howToPlay.setText(
                "RULES\n"
                + "\t1. A Pawn can move forward 1 square ahead, but it can taken opponent piece at the left or right 1 square\n"
                + "\t2. A Rook can move horizontally and vertically freely until it meets an obstacle.\n"
                + "\t3. A Knight can only move in 'L' direction\n"
                + "\t4. A Bishop can move diagonally both way\n"
                + "\t5. A Queen is a combination of Rook and Bishop\n"
                + "\t6. A King are like the Queen, except it can be move 1 square\n"
                + "\t7. The game end where the King is in checkmate, or it cannot move anymore\n"
                + "The point of the game is to protect our king while trying to taken the opponent King\n"
                + "Special moves:\n"
                + "a. The Pawn can move 2 squares ahead when it's at the initial position\n"
                + "b. When the Pawn reach the other side (which means they cannot move anymore), we can promote it to other piece except King\n"
        );

        //Initialize and styling return button
        this.returnBtn = ViewLib.createButton("Return to Menu");
        this.returnBtn.addActionListener((ActionEvent e) -> app.returnToMenu());

        //Setup layout
        initLayout();
    }
}
