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
 * The view of About Us.
 *
 * @author Asus
 */
public class AboutUsView extends JPanel {

    //The header (title) of this view
    private final JLabel header;
    //The content 
    private final JTextArea aboutUs;
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
        this.add(Box.createVerticalStrut(30));
        this.header.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(header);
        this.add(Box.createVerticalStrut(30));

        //Add the about us JTextArea to a JScrollPanel (allow scrolling) and styling
        JScrollPane scrollPane = new JScrollPane(this.aboutUs);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setPreferredSize(new Dimension(1200, 500));
        scrollPane.setMaximumSize(new Dimension(1200, 500));
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(200, 200, 200), 2));
        this.add(scrollPane);

        //Add return button and styling
        this.add(Box.createVerticalStrut(30));
        this.returnBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        this.add(returnBtn);

        this.add(Box.createVerticalGlue());
    }

    /**
     * The constructor of AboutUsView.
     *
     * @param app - The reference of the Chess object
     */
    public AboutUsView(Chess app) {
        this.app = app;

        //Initialize the header
        this.header = ViewLib.createLabel("About Us", 36);
        this.header.setForeground(new Color(50, 50, 50));

        //Set content and JTextArea's attribute
        this.aboutUs = new JTextArea();
        this.aboutUs.setEditable(false); //Not editable
        this.aboutUs.setLineWrap(true); //The line will be wrap (if it too long, whe word/character will be pushed to the next line)
        this.aboutUs.setWrapStyleWord(true); //The whole word will be push to the next line instead of breaking the word
        this.aboutUs.setFont(new Font("Arial", Font.PLAIN, 40)); //Set font
        this.aboutUs.setBackground(new Color(250, 250, 250)); //Set background
        this.aboutUs.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10)); //Set the border
        this.aboutUs.setText(
                "Our Group: Cinocure\n"
                + "Members:\n"
                + "\t1. Le Nguyen Hai Dang\n"
                + "\t2. La Thien Toan\n"
                + "\t3. Ho Quang Thanh\n"
                + "\t4. Phan Van Hoa Thuan\n"
                + "\t5. Nguyen Tri Phong\n"
                + "\t6. Nguyen Hoang Phat"
        );

        //Initialize and styling return button
        this.returnBtn = ViewLib.createButton("Return to Menu");
        this.returnBtn.addActionListener((ActionEvent e) -> app.returnToMenu());

        //Setup layout
        initLayout();
    }
}
