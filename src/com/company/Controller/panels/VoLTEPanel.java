package com.company.Controller.panels;

import com.company.FileManager;
import com.company.Model.Mobile;
import com.company.Controller.GUIUtility;
import com.company.Utility;
import com.company.Parser.MobileParser;
import com.company.Parser.ParserUtility;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.awt.GridBagConstraints.EAST;
import static java.awt.GridBagConstraints.WEST;


public class VoLTEPanel extends JPanel {

    private Mobile deviceUnderTest;
    private JLabel fileNameLabel;
    private JPanel fileNamePanel = new JPanel();
    private JPanel imsiPanel = new JPanel();
    private JTextField imsiTextField = new JTextField();
    private JLabel imsiLabel = new JLabel(GUIUtility.IMSI_LABEL);
    private JPanel outputArea;
    private String pcapFileName;
    private JButton writeToFileButton = new JButton(GUIUtility.WRITE_TO_FILE_BTN);

    private JButton browseButton = new JButton(GUIUtility.BROWSE_BTN);
    private JButton parseButton = new JButton(GUIUtility.PARSE_BTN);
    private JButton basicVoLTEInfoButton = new JButton(GUIUtility.BASIC_INFO_VOLTE_BTN);
    private JButton securityClientButton = new JButton(GUIUtility.SECURITY_CLIENT_BTN);
    private JButton codecsButton = new JButton(GUIUtility.VOLTE_CODECS_BTN);
    private JButton srvccButton = new JButton(GUIUtility.SRVCC_BTN);
    private JButton[] buttons = {basicVoLTEInfoButton,securityClientButton,codecsButton,srvccButton};

    public VoLTEPanel(){
        super(false);
        deviceUnderTest = new Mobile();
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        // If you don't reset the values, padding remains the same
        constraints.insets = new Insets(10,10,10,10);
        constraints.anchor = WEST;
        fileNameLabel = new JLabel(GUIUtility.FILE_NAME_BTN);
        constraints.weightx = 0.5;
        constraints.weighty = 0;
        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 1;
        //fileNameLabel.setMaximumSize(new Dimension(50,50));
        fileNamePanel.setLayout(new BorderLayout());
        fileNamePanel.setBackground(Color.BLUE);
        fileNamePanel.add(fileNameLabel);
        layout.setConstraints(fileNamePanel, constraints);
        add(fileNamePanel);

        constraints.gridx = 1;
        constraints.gridy = 0;
        //imsiLabel.setLabelFor(imsiTextField);
        constraints.weightx = 0.0;
        imsiPanel.add(imsiLabel);
        imsiPanel.add(imsiTextField);
        imsiTextField.setEnabled(false);
        //imsiTextField.setMinimumSize(new Dimension(50,50));
        imsiTextField.setPreferredSize(new Dimension(100,15));
        constraints.gridwidth = 2;
        imsiPanel.setBackground(Color.BLUE);
        layout.setConstraints(imsiPanel,constraints);
        add(imsiPanel);


        constraints.gridwidth = 1;
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0;
        layout.setConstraints(browseButton, constraints);
        add(browseButton);

        writeToFileButton.setEnabled(false);
        constraints.gridx = 4;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0;
        constraints.gridwidth = 1;
        layout.setConstraints(writeToFileButton, constraints);
        add(writeToFileButton);


        parseButton.setEnabled(false);
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        constraints.gridwidth = 5;
        constraints.gridx = 0;
        constraints.gridy = 1;
        layout.setConstraints(parseButton, constraints);
        add(parseButton);


        outputArea = new JPanel();
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;   //request any extra vertical space
        constraints.anchor = GridBagConstraints.SOUTH; //bottom of space
        constraints.gridy = 2;
        constraints.gridwidth = 5;
        outputArea.setBackground(Color.BLUE);
        layout.setConstraints(outputArea, constraints);
        add(outputArea);

        browseButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                //Create a file chooser
                // Hide create folder button
                UIManager.put("FileChooser.readOnly", Boolean.TRUE);
                final JFileChooser fc = new JFileChooser();
                // Set file filter
                FileNameExtensionFilter filter = new FileNameExtensionFilter("pcap/pcapng files","pcap","pcapng");
                fc.setFileFilter(filter);
                fc.setCurrentDirectory(null);
                int returnVal = fc.showOpenDialog(VoLTEPanel.this);
                // Manage ok button
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File fileIn = fc.getSelectedFile();
                    pcapFileName = fileIn.getName();
                    fileNameLabel.setText(pcapFileName);
                    Utility.INPUT_FILE_PATH = fileIn.getPath();
                    Utility.INPUT_FILE_PATH = Utility.INPUT_FILE_PATH.substring(0, Utility.INPUT_FILE_PATH.lastIndexOf(File.separator));
                    pcapFileName = Utility.INPUT_FILE_PATH.concat("\\").concat(pcapFileName);
                    Utility.PCAP_RRC_FILE_NAME = pcapFileName;
                    imsiTextField.setEnabled(true);
                    // Set program path
                    parseButton.setEnabled(true);
                    writeToFileButton.setEnabled(false);
                    clearView(outputArea);
                    outputArea.removeAll();
                } else if (returnVal == JFileChooser.CANCEL_OPTION) {
                    // Do nothing
                    pcapFileName = null;
                }
            }
        });
        writeToFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                // Show "Save" dialog:
                c.setFileFilter(new FileNameExtensionFilter("txt file","txt"));
                int rVal = c.showSaveDialog(VoLTEPanel.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    c.getSelectedFile().getName();
                    //dir.setText(c.getCurrentDirectory().toString());
                    FileManager.writeOutputToFile(c.getSelectedFile().getPath(),Mobile.printVolteCap(deviceUnderTest));
                }
            }
        });
        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Pattern p = Pattern.compile(Utility.IMSI_REGEX);
                String imsi = imsiTextField.getText();
                Matcher m = p.matcher(imsi);
                if (imsiTextField.getText().isEmpty()) {
                    JOptionPane.showMessageDialog(VoLTEPanel.this, "Please insert IMSI");
                } else {
                    if (!(m.find() && imsi.length() == 15)) {
                        JOptionPane.showMessageDialog(VoLTEPanel.this, "Wrong IMSI format");
                    } else {
                        clearView(outputArea);
                        outputArea.removeAll();
                        writeToFileButton.setEnabled(true);
                        deviceUnderTest.setImsi(imsi);
                        File inputFile = FileManager.loadInputFile(pcapFileName, Utility.SIP_TSHARK_FILTER,"");
                        FileReader volteFileReader = null;
                        try {
                            volteFileReader = new FileReader(inputFile);
                        } catch (FileNotFoundException e1) {
                            e1.printStackTrace();
                        }

                        MobileParser volteParser = new MobileParser(volteFileReader, deviceUnderTest);
                        boolean packetValidity = volteParser.volteValidator(deviceUnderTest);
                        if (packetValidity){
                            deviceUnderTest = volteParser.volteRegisterParser(deviceUnderTest);
                            // Parse invite
                            inputFile = FileManager.loadInputFile(pcapFileName,
                                    Utility.FRAME_NUMBER_TSHARK_FILTER.concat(String.valueOf(Utility.INVITE_FRAME_NUMBER)),
                                    Utility.TSHARK_OPTION_LEGACY_FILTER);
                            try {
                                volteFileReader = new FileReader(inputFile);
                            } catch (FileNotFoundException e1) {
                                e1.printStackTrace();
                            }
                            MobileParser inviteParser = new MobileParser(volteFileReader, deviceUnderTest);
                            deviceUnderTest = inviteParser.volteInviteParser(deviceUnderTest);
                            deviceUnderTest.getLteCap().getVolteCap().setSupportedCodecs(ParserUtility.wiresharkWorkaroundCodecsParser(ParserUtility.duplicatedKeysWiresharkMediaAttr));
                            volteUISetup(outputArea,deviceUnderTest);
                            deviceUnderTest.printVoLTEDUTCap(deviceUnderTest);
                        } else {
                            JOptionPane.showMessageDialog(VoLTEPanel.this, Utility.ERROR_CAUSE);
                        }

                    }
                }

            }
        });
        setBackground(Color.BLUE);
        setVisible(true);
    }

    /**
     * Private support method to set up VoLTE GUI
     * @param container the output area in the VoLTE panel
     * @param device the device under test instance
     */
    private void volteUISetup(JPanel container,Mobile device) {
        GridBagLayout layout = new GridBagLayout();
        container.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        constraints.insets = new Insets(10,10,10,10);
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;   //request any extra vertical space
        constraints.anchor = GridBagConstraints.NORTHWEST; //bottom of space
        constraints.gridy = 0;
        constraints.gridx = 0;
        layout.setConstraints(basicVoLTEInfoButton,constraints);
        container.add(basicVoLTEInfoButton);

        constraints.gridx = 1;
        layout.setConstraints(securityClientButton,constraints);
        container.add(securityClientButton);

        constraints.gridx = 2;
        layout.setConstraints(codecsButton,constraints);
        container.add(codecsButton);

        constraints.gridx = 0;
        constraints.gridy = 1;
        layout.setConstraints(srvccButton,constraints);
        container.add(srvccButton);

        basicVoLTEInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                basicVoLTEInfoButton.setEnabled(false);
                JTextField earlyMediaTextField = new JTextField(device.getLteCap().getVolteCap().getpEarlyMediaSupport());
                earlyMediaTextField.setEditable(false);
                JTextField userAgentTextField = new JTextField(device.getLteCap().getVolteCap().getUserAgent());
                userAgentTextField.setEditable(false);

                final JComponent[] basicInfoVolte = new JComponent[] {
                        new JLabel(GUIUtility.EARLY_MEDIA_SUPPORT_LABEL), earlyMediaTextField,
                        new JLabel(GUIUtility.USER_AGENT_LABEL), userAgentTextField,
                };
                JOptionPane pane = new JOptionPane(basicInfoVolte);
                // Configure via set methods
                JDialog dialog = pane.createDialog(VoLTEPanel.this, "Basic Info");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        basicVoLTEInfoButton.setEnabled(true);
                    }
                });
            }
        });

        securityClientButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                securityClientButton.setEnabled(false);
                Object[][] data = new Object[device.getLteCap().getVolteCap().getSupportedAlgorithms().size()][2];
                String[] columnNames = {"ALG","EALG"};
                for (int i = 0; i < device.getLteCap().getVolteCap().getSupportedAlgorithms().size(); i++) {
                    data[i][0] = device.getLteCap().getVolteCap().getSupportedAlgorithms().get(i).getAlg();
                    data[i][1] = device.getLteCap().getVolteCap().getSupportedAlgorithms().get(i).getEalg();
                }
                JTable table = new JTable(data,columnNames);
                JDialog dialog = new JDialog();
                dialog.add(new JScrollPane(table));
                dialog.setTitle("Security Clients");
                dialog.pack();
                dialog.setVisible(true);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        securityClientButton.setEnabled(true);
                    }
                });
            }
        });

        codecsButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                codecsButton.setEnabled(false);
                Object[][] data = new Object[device.getLteCap().getVolteCap().getSupportedCodecs().size()][5];
                String[] columnNames = {"MIME TYPE","Sample Rate","Media Format","BW","BR"};
                for (int i = 0; i < device.getLteCap().getVolteCap().getSupportedCodecs().size(); i++) {
                    data[i][0] = device.getLteCap().getVolteCap().getSupportedCodecs().get(i).getMimeType();
                    data[i][1] = device.getLteCap().getVolteCap().getSupportedCodecs().get(i).getSampleRate();
                    data[i][2] = device.getLteCap().getVolteCap().getSupportedCodecs().get(i).getMediaFormat();
                    if (device.getLteCap().getVolteCap().getSupportedCodecs().get(i).getBw() != null) {
                        data[i][3] = device.getLteCap().getVolteCap().getSupportedCodecs().get(i).getBw();
                    } else {
                        data[i][3] = "-";
                    }
                    if (device.getLteCap().getVolteCap().getSupportedCodecs().get(i).getBr() != null) {
                        String temp = "";
                        for (int ii = 0; ii <device.getLteCap().getVolteCap().getSupportedCodecs().get(i).getBr().length;ii++ ) {
                            temp = temp.concat(String.valueOf(device.getLteCap().getVolteCap().getSupportedCodecs().get(i).getBr()[ii]).concat("-"));
                        }
                        temp = temp.substring(0,temp.length()-1);
                        data[i][4] = temp;
                    } else {
                        data[i][4] = "-";
                    }
                }

                JTable table = new JTable(data,columnNames);
                JDialog dialog = new JDialog();
                dialog.add(new JScrollPane(table));
                dialog.setTitle("VoLTE Codecs");
                dialog.pack();
                dialog.setVisible(true);
                dialog.addWindowListener(new WindowAdapter() {
                    @Override
                    public void windowClosing(WindowEvent e) {
                        super.windowClosing(e);
                        codecsButton.setEnabled(true);
                    }
                });
            }
        });

        srvccButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                srvccButton.setEnabled(false);
                ArrayList<JComponent> srvccComponents = new ArrayList<JComponent>();
                for (int i = 0; i < device.getLteCap().getVolteCap().getSupportedSrvccs().size(); i++) {
                    JTextField temp = new JTextField(device.getLteCap().getVolteCap().getSupportedSrvccs().get(i));
                    temp.setEditable(false);
                    srvccComponents.add(temp);
                }
                if (device.getLteCap().getVolteCap().getSupportedSrvccs().size() == 0) {
                    srvccComponents.add(new JTextField(Utility.NOT_SUPPORTED));
                }
                JOptionPane pane = new JOptionPane(srvccComponents.toArray());
                // Configure via set methods
                JDialog dialog = pane.createDialog(VoLTEPanel.this, "Supported SRVCCs");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        srvccButton.setEnabled(true);
                    }
                });

            }
        });
    }

    /**
     * Private support method to clear view of the output area
     * @param panel the container
     */
    private void clearView(JPanel panel){
        // Reset view
        for( JButton currentComponent: buttons ) {
            for( ActionListener al : currentComponent.getActionListeners() ) {
                currentComponent.removeActionListener( al );
            }
        }
        panel.removeAll();
        panel.updateUI();
    }
}
