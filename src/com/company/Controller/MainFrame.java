package com.company.Controller;

import com.company.Utility;

import javax.swing.*;
import java.awt.*;

/**
 * Class to define GUI
 * https://docs.oracle.com/javase/tutorial/uiswing/components/toplevel.html
 */
public class MainFrame extends JFrame {
    public MainFrame() throws HeadlessException {
        setTitle(GUIUtility.PROGRAM_NAME);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(600,500));
        add(new MainTabbedPane(),BorderLayout.CENTER);
        //setResizable(false);
        pack();
        setVisible(true);
    }
}
