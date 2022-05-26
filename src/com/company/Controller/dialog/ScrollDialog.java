package com.company.Controller.dialog;

import javax.swing.*;
import java.awt.*;

/**
 * Public class retrieving a panel containing JComponents and displaying them in a dialog
 * using a JScrollBar to make all components fit the GUI
 */
public class ScrollDialog extends JDialog {
    private JScrollPane jscrollPane;
    private JPanel panel;

    public ScrollDialog(JPanel panel, String title){
        super();
        getContentPane().setLayout(new GridLayout(0, 1));
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(title);
        jscrollPane = new JScrollPane(panel);
        add(jscrollPane);
        jscrollPane.setPreferredSize(new Dimension(550,400));
        setLocationRelativeTo(null);
        jscrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setModal(false);
        pack();
        setVisible(true);
    }
    public ScrollDialog(JComponent[] components, String title){
        super();
        GridLayout la = new GridLayout(0,1);
        la.setVgap(15);
        la.setHgap(15);
        getContentPane().setLayout(la);
        setDefaultCloseOperation(DISPOSE_ON_CLOSE);
        setTitle(title);
        panel = new JPanel();
        panel.setLayout(new GridLayout(0,1));
        panel.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        for (int i = 0; i < components.length; i++) {
            panel.add(components[i]);
        }
        jscrollPane = new JScrollPane(panel);
        add(jscrollPane);
        jscrollPane.setPreferredSize(new Dimension(400,200));
        //jscrollPane.setMaximumSize(new Dimension(550,400));
        setLocationRelativeTo(null);
        jscrollPane.setViewportBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));
        setModal(false);
        pack();
        setVisible(true);
    }
}
