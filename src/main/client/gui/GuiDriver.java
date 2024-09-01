package client.gui;

import javax.swing.SwingUtilities;

public class GuiDriver {    
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                GuiModel model = new GuiModel();
                GuiView view = new GuiView();
                GuiController controller = new GuiController(model, view);
            }
        });
    }
}