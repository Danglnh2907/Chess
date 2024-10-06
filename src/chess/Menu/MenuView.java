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
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * The Menu class.
 *
 * @author Asus
 */
public class MenuView extends JPanel {

    //The background of the menu 
    private final ImageIcon background;
    //The header (title) of the menu view
    private final JLabel header;
    /*The 4 buttons*/
    private final JButton playBtn;
    private final JButton howToPlayBtn;
    private final JButton aboutUsBtn;
    private final JButton exitBtn;
    //The reference to the Chess object, which will be used to called the handler method
    private final Chess app;

    /**
     * Helper method for setup the layout of the JPanel and its components.
     */
    private void initLayout() {
        //Set the layout of this panel
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        //If false, the component may not paint some or all of its pixels, allowing the underlying pixels to show through.
        setOpaque(false);

        //Add the header and styling
        add(Box.createVerticalGlue());
        header.setAlignmentX(Component.CENTER_ALIGNMENT);
        add(header);
        add(Box.createVerticalStrut(60));

        //Initializing the button panel, styling and adding 4 buttons to the Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setOpaque(false);
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));
        addButton(buttonPanel, playBtn);
        addButton(buttonPanel, howToPlayBtn);
        addButton(buttonPanel, aboutUsBtn);
        addButton(buttonPanel, exitBtn);

        //Add the button panel to main panel
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }

    /**
     * Helper method, used for adding the individual button to the button panel.
     *
     * @param panel - The button panel for adding
     * @param button - The button for adding
     */
    private void addButton(JPanel panel, JButton button) {
        button.setAlignmentX(Component.CENTER_ALIGNMENT);
        button.setMaximumSize(new Dimension(400, 70)); // Set maximum size
        button.setMinimumSize(new Dimension(400, 70)); // Set minimum size
        button.setPreferredSize(new Dimension(400, 70)); // Set preferred size
        panel.add(button);
        panel.add(Box.createVerticalStrut(30));
    }

    /**
     * The constructor of MenuView class
     *
     * @param app - The reference of the Chess object
     */
    public MenuView(Chess app) {
        this.app = app;

        //Get the background from resource
        background = new ImageIcon(getClass().getResource("/resource/background.jpg"));

        //Set the header and styling
        header = ViewLib.createLabel("Ultimate Chess", 48);
        header.setForeground(Color.WHITE);

        /*Initialize and adding event handler to the 4 buttons*/
        playBtn = ViewLib.createButton("Play");
        this.playBtn.addActionListener((ActionEvent e) -> {
            this.app.launchGame();
        });

        howToPlayBtn = ViewLib.createButton("How to play");
        this.howToPlayBtn.addActionListener((ActionEvent e) -> {
            this.app.gotoHowToPlay();
        });

        aboutUsBtn = ViewLib.createButton("About us");
        this.aboutUsBtn.addActionListener((ActionEvent e) -> {
            this.app.gotoAboutUs();
        });
        exitBtn = ViewLib.createButton("Exit");
        this.exitBtn.addActionListener((ActionEvent e) -> {
            System.exit(0);
        });

        //Setup layout
        initLayout();
    }

    /**
     * The overridden paintComponent method, which will be called internally
     * when the constructor is called. It will draw the UI of the MenuView
     *
     * @param g - The Graphics object used for drawing
     */
    @Override
    protected void paintComponent(Graphics g) {
        //Called the super method from the JPanel's paintComponent method
        super.paintComponent(g);
        //Draw the background
        g.drawImage(background.getImage(), 0, 0, getWidth(), getHeight(), this);
    }
}
