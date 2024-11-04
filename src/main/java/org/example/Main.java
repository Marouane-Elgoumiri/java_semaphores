package org.example;

import org.example.gui.SemaphoreGUI;
import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new SemaphoreGUI().setVisible(true);
        });
    }
}