package client.gui;

import javax.swing.SwingUtilities;

/**
 * Classe driver della GUI. Inizializza una istanza del GUIController che a sua
 * volta inizializza la logica (il model) e le componenti grafiche (la view).
 */
public class GuiDriver {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GuiModel model = new GuiModel();
                GuiView view = new GuiView();
                new GuiController(model, view);
            }
        });
    }
}