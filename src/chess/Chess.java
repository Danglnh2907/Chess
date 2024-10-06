/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess;

import chess.Menu.AboutUsView;
import chess.Menu.HowToPlayView;
import chess.Menu.MenuView;
import chess.chessboard.Game;
import chess.util.ModelLib;
import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Component;
import java.awt.Dialog;
import java.awt.Dimension;
import java.awt.Font;
import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;

/**
 * The main class of the program.
 *
 * @author Asus
 */
public class Chess extends JFrame {

    //The layout of container panel
    private final CardLayout layout;
    //The container panel
    private final JPanel container;

    /*The component panels*/
    private final MenuView menuView;
    private final Game gameView;
    private final HowToPlayView howToPlayView;
    private final AboutUsView aboutUsView;

    /**
     * Helper method, used to set up the layout of the JFrame, container panel
     * and component panels.
     */
    private void initLayout() {
        //Set the layout of the frame to Border Layout
        this.setLayout(new BorderLayout());

        //Set the size of the frame to 1300 x 900 pixel
        this.setSize(1300, 900);

        //Set the close button to close the program (if not set, the window will actually hide instead of closing)
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        //Set the window so that it cannot be resized
        this.setResizable(false);

        //Set the frame to be visible
        this.setVisible(true);

        //Set the frame to be center of the screen
        this.setLocationRelativeTo(null);

        //Set the title of the window
        this.setTitle("Ultimate chess");

        //Add component panels to the container panel
        this.container.add(this.menuView, "Menu");
        this.container.add(this.gameView, "Game");
        this.container.add(this.howToPlayView, "HowToPlay");
        this.container.add(this.aboutUsView, "AboutUs");

        //Show the menu as default panel
        this.layout.show(this.container, "Menu");

        //Add the main panel (container) to the frame
        this.add(this.container);
    }

    /**
     * Constructor of class Chess, which initialize all the panels, setup layout
     * and initialize data directory.
     */
    public Chess() {
        //Initialize the container panel and the layout for the container, using CardLayout
        this.layout = new CardLayout();
        this.container = new JPanel(this.layout);

        //Initialize the component panels
        this.menuView = new MenuView(this);
        this.gameView = new Game(this);
        this.howToPlayView = new HowToPlayView(this);
        this.aboutUsView = new AboutUsView(this);

        //Set up the the layout
        initLayout();

        //Initialize the data directory
        ModelLib.initData();
    }

    /**
     * Handler method for button 'Play' in Menu. This method will first show up
     * a JDialog asking for player name, then after submitting, will change into
     * the game view and load the data (if there is record about these 2 players
     * before)
     */
    public void launchGame() {
        //The array to hold the player names
        final String[] players = new String[2];

        //Create the popup for asking players' names
        JDialog popup = new JDialog(
                SwingUtilities.getWindowAncestor(this),
                "Player Name Entry",
                Dialog.ModalityType.APPLICATION_MODAL);
        popup.setLayout(new BorderLayout());
        popup.setSize(400, 350);

        //Use BoxLayout for vertical stacking
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        //Create label and styling
        JLabel label1 = new JLabel("Enter player 1 (WHITE) name:");
        label1.setFont(new Font("Arial", Font.PLAIN, 20));
        label1.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Create the text box for player1 and styling
        JTextField player1Field = new JTextField(15);
        player1Field.setFont(new Font("Arial", Font.PLAIN, 20));
        player1Field.setMaximumSize(new Dimension(300, 40));
        player1Field.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Create label for player2 and styling
        JLabel label2 = new JLabel("Enter player 2 (BLACK) name:");
        label2.setFont(new Font("Arial", Font.PLAIN, 20));
        label2.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Create text box for player2 and styling
        JTextField player2Field = new JTextField(15);
        player2Field.setFont(new Font("Arial", Font.PLAIN, 20));
        player2Field.setMaximumSize(new Dimension(300, 40));
        player2Field.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Create submit button and styling
        JButton submitButton = new JButton("Submit");
        submitButton.setFont(new Font("Arial", Font.BOLD, 20));
        submitButton.setAlignmentX(Component.CENTER_ALIGNMENT);

        //Add action listner (on mouse click)
        submitButton.addActionListener(e -> {
            //Get the value from text box
            players[0] = player1Field.getText();
            players[1] = player2Field.getText();

            //Check if the name is valid (not empty and not the same)
            if (!players[0].isEmpty() && !players[1].isEmpty() && !players[0].equals(players[1])) {
                popup.dispose();
                //After getting the players, we show the game panel and load data for playing
                this.layout.show(this.container, "Game");
                this.gameView.load(players[0], players[1]);
            }
        });

        //Add components to the panel with vertical spacing
        mainPanel.add(label1);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(player1Field);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        mainPanel.add(label2);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        mainPanel.add(player2Field);
        mainPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        mainPanel.add(submitButton);

        //Add the main panel to JDialog and setting the attribute of JDialog
        popup.add(mainPanel);
        popup.setLocationRelativeTo(this);
        popup.setVisible(true);
        popup.setResizable(false);
    }

    /**
     * Handler method for button "How to play" in menu.
     */
    public void gotoHowToPlay() {
        this.layout.show(this.container, "HowToPlay");
    }

    /**
     * Handler method for button "About us" in menu.
     */
    public void gotoAboutUs() {
        this.layout.show(this.container, "AboutUs");
    }

    /**
     * Handler method for button "Return to menu" in other views.
     */
    public void returnToMenu() {
        this.layout.show(this.container, "Menu");
    }

    /**
     * The main method of the application, which will initialize the Chess
     * object.
     *
     * @param args - The command line arguments
     */
    public static void main(String[] args) {
        Chess app = new Chess();
    }

}
