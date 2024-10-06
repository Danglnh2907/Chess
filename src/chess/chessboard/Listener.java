/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package chess.chessboard;

/**
 * The Listener interface, which was used for the the component class
 * (ChessBoard and SidePanel) to communicate with the container class (Game). It
 * used the Observer pattern.
 *
 * @author Asus
 */
public interface Listener {

    /**
     * Abstract handler method of ChessBoard.
     *
     * @param x - The x coordinate where the mouse click
     * @param y - The y coordinate where the mouse click
     */
    public abstract void onChessboardClick(int x, int y);

    /**
     * Abstract handler method of the button "Save game" in side panel.
     */
    public abstract void onSaveGame();
}
