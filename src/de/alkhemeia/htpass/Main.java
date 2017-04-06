/*
 * @alkhemeia.de
 */

package de.alkhemeia.htpass;

/**
 *
 * @author alkhemeia
 */
public class Main {

    private static HtpassCreatorGui gui = new HtpassCreatorGui();
    
    public static void main(String[] args) {

        HtpassCreator.initGui();
        gui.setVisible(true);
        
    }
    
    public static HtpassCreatorGui getGui() {
        return gui;
    }
    
}
