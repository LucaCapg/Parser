package com.company.Controller;

import com.company.Controller.panels.CatMPanel;
import com.company.Controller.panels.NBIoTPanel;
import com.company.Controller.panels.GeneralPanel;
import com.company.Controller.panels.VoLTEPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.net.URL;

public class MainTabbedPane extends JPanel {
    private String pcapFileName;

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        // Draw Reply Logo
        URL url = MainTabbedPane.class.getResource("images/logo.png");
        Image image = new ImageIcon(url).getImage();
        //g.drawImage(image, this.getWidth()-500, this.getHeight()-500, 1024, 910, this);
        g.drawImage(image,-150,78,this);
    }

    public MainTabbedPane() {
        super(new GridLayout(1, 1));
        pcapFileName = null;
        JTabbedPane tabbedPane = new JTabbedPane();
        tabbedPane.setPreferredSize(new Dimension(900, 600));

        JComponent rrcPanel = new GeneralPanel();
        tabbedPane.addTab(GUIUtility.RRC_PARSER_TAB,null, rrcPanel,
                GUIUtility.RRC_PARSER_HINT);
        tabbedPane.setMnemonicAt(0, KeyEvent.VK_1);

        JComponent voltePanel = new VoLTEPanel();
        //voltePanel = makeTextPanel("VoLTE");
        tabbedPane.addTab(GUIUtility.VOLTE_PARSER_TAB, null, voltePanel,
                GUIUtility.VOLTE_PARSER_HINT);
        tabbedPane.setMnemonicAt(1, KeyEvent.VK_2);

        JComponent catmPanel = new CatMPanel();
        tabbedPane.addTab(GUIUtility.CATM_PARSER_TAB,null,catmPanel,
                GUIUtility.CATM_PARSER_HINT);
        tabbedPane.setMnemonicAt(2, KeyEvent.VK_3);

        JComponent nbIotPanel = new NBIoTPanel();
        tabbedPane.addTab(GUIUtility.NBIOT_PARSER_BTN, null, nbIotPanel,
                GUIUtility.NBIOT_PARSER_HINT);
        tabbedPane.setMnemonicAt(3, KeyEvent.VK_4);

        add(tabbedPane);
        tabbedPane.setTabLayoutPolicy(JTabbedPane.SCROLL_TAB_LAYOUT);
        tabbedPane.setTabPlacement(JTabbedPane.LEFT);
    }
}
