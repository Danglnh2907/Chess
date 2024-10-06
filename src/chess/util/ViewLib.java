/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.util;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

/**
 *
 * @author Asus
 */
public class ViewLib {

    /**
     * Method for creating a custom style button.
     *
     * @param text - The text to be displayed in the button
     * @return - The JButton object
     */
    public static JButton createButton(String text) {
        JButton button = new JButton(text);

        //Increase font size and make it bold
        button.setFont(new Font("Arial", Font.BOLD, 24));

        //Set dark gray background
        Color darkGray = new Color(50, 50, 50);
        button.setBackground(darkGray);

        //White text for contrast
        button.setForeground(Color.WHITE);

        //Remove focus border
        button.setFocusPainted(false);

        //Create a subtle border
        button.setBorder(BorderFactory.createLineBorder(new Color(70, 70, 70), 2));

        //Increase button size
        button.setPreferredSize(new Dimension(400, 70));

        //Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setBackground(new Color(70, 70, 70)); // Lighter gray on hover
            }

            @Override
            public void mouseExited(MouseEvent e) {
                button.setBackground(darkGray); // Back to dark gray
            }
        });

        return button;
    }

    /**
     * Method for creating a custom JLabel
     *
     * @param content - The content of label
     * @param fontSize - The preferred font size of the label
     * @return - The custom JLabel
     */
    public static JLabel createLabel(String content, int fontSize) {
        JLabel label = new JLabel(content);
        label.setHorizontalAlignment(SwingConstants.CENTER);
        label.setFont(new Font("Arial", Font.BOLD, fontSize));
        return label;
    }
}
