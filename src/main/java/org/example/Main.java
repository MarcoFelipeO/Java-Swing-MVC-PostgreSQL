package org.example;

import org.example.vista.MenuPrincipalVista;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new MenuPrincipalVista().setVisible(true));
    }
}
