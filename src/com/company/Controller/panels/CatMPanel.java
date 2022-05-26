package com.company.Controller.panels;

import com.company.FileManager;
import com.company.Model.eMTC;
import com.company.Controller.GUIUtility;
import com.company.Utility;
import com.company.Parser.eMTCParser;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Controller class to manage view of Catm Parser
 */
public class CatMPanel extends JPanel {
    private JLabel fileNamelabel;
    private JButton browseButton = new JButton(GUIUtility.BROWSE_BTN);
    private JButton parseButton = new JButton(GUIUtility.PARSE_BTN);
    private JPanel outputArea;
    private String pcapFileName;
    private eMTC dut;
    private JButton writeToFileButton = new JButton(GUIUtility.WRITE_TO_FILE_BTN);

    private JButton basicInfoButton = new JButton(GUIUtility.BASIC_INFO_CATM_BTN);
    private JButton fgiButton = new JButton(GUIUtility.FGI_BTN);
    private JButton catmFeaturesButton = new JButton(GUIUtility.CATM_FEATURES_BTN);
    private JButton ciphAlgButton = new JButton(GUIUtility.EPS_ENCR_LABEL);
    private JButton intAlgButton = new JButton(GUIUtility.EPS_INTEGR_LABEL);
    private JButton bandsInfoButton = new JButton(GUIUtility.BAND_INFO_BTN);
    private JButton[] buttons = {basicInfoButton,fgiButton,catmFeaturesButton,ciphAlgButton,intAlgButton,bandsInfoButton};

    public CatMPanel(){
        super(false);
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints layoutContraints = new GridBagConstraints();
        layoutContraints.fill = GridBagConstraints.BOTH;
        // If you don't reset the values, padding remains the same
        layoutContraints.insets = new Insets(10,10,10,10);

        fileNamelabel = new JLabel(GUIUtility.FILE_NAME_BTN);
        layoutContraints.gridx = 0;
        layoutContraints.gridy = 0;
        layoutContraints.weightx = 0.5;
        layoutContraints.weighty = 0.0;
        layoutContraints.gridwidth = 2;
        layoutContraints.anchor = GridBagConstraints.NORTHWEST;
        layout.setConstraints(fileNamelabel,layoutContraints);
        add(fileNamelabel);

        layoutContraints.gridx = 2;
        layoutContraints.gridwidth = 1;
        layoutContraints.weightx = 0.0;
        layoutContraints.weighty = 0.0;
        layout.setConstraints(browseButton,layoutContraints);
        add(browseButton);

        writeToFileButton.setEnabled(false);
        layoutContraints.gridx = 3;
        layoutContraints.gridy = 0;
        layoutContraints.weightx = 0.0;
        layoutContraints.weighty = 0.0;
        layout.setConstraints(writeToFileButton, layoutContraints);
        add(writeToFileButton);

        layoutContraints.gridx = 0;
        layoutContraints.gridy = 1;
        layoutContraints.gridwidth = 4;
        layout.setConstraints(parseButton,layoutContraints);
        parseButton.setEnabled(false);
        add(parseButton);

        outputArea = new JPanel();
        layoutContraints.weightx = 1.0;
        layoutContraints.weighty = 1.0;   //request any extra vertical space
        layoutContraints.anchor = GridBagConstraints.SOUTH; //bottom of space
        layoutContraints.gridy = 2;
        layoutContraints.gridwidth = 4;
        outputArea.setBackground(Color.CYAN);
        layout.setConstraints(outputArea, layoutContraints);
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
                int returnVal = fc.showOpenDialog(CatMPanel.this);
                // Manage ok button
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File fileIn = fc.getSelectedFile();
                    pcapFileName = fileIn.getName();
                    // Set program path
                    Utility.INPUT_FILE_PATH = fileIn.getPath();
                    Utility.INPUT_FILE_PATH = Utility.INPUT_FILE_PATH.substring(0, Utility.INPUT_FILE_PATH.lastIndexOf(File.separator));
                    pcapFileName = Utility.INPUT_FILE_PATH.concat("\\").concat(pcapFileName);
                    Utility.PCAP_RRC_FILE_NAME = pcapFileName;
                    fileNamelabel.setText(Utility.PCAP_RRC_FILE_NAME.replace("\"",""));
                    parseButton.setEnabled(true);
                    writeToFileButton.setEnabled(false);
                    clearView(outputArea);
                    outputArea.removeAll();
                } else if (returnVal == JFileChooser.CANCEL_OPTION) {
                    //System.out.println("Cancel was selected");
                    // Do nothing
                    pcapFileName = null;
                }
            }
        });
        writeToFileButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                // Demonstrate "Save" dialog:
                c.setFileFilter(new FileNameExtensionFilter("txt file","txt"));
                int rVal = c.showSaveDialog(CatMPanel.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    c.getSelectedFile().getName();
                    //dir.setText(c.getCurrentDirectory().toString());
                    FileManager.writeOutputToFile(c.getSelectedFile().getPath(),eMTC.printeMTCCap(dut));
                }
            }
        });
        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseButton.setEnabled(false);
                String apex = "\"";
                File jsonFile = null;
                if (pcapFileName == null) {
                    JOptionPane.showMessageDialog(CatMPanel.this, "No file selected");
                } else {
                    clearView(outputArea);
                    outputArea.removeAll();
                    writeToFileButton.setEnabled(true);
                    File jsonTemp = FileManager.loadInputFile(pcapFileName, Utility.S1AP_TSHARK_FILTER,Utility.TSHARK_OPTION_LEGACY_FILTER);
                    FileReader jsonFileReader = null;
                    try {
                        jsonFileReader = new FileReader(jsonTemp);
                    } catch (FileNotFoundException e1) {
                        // Handle file not found exceptions
                        e1.printStackTrace();
                    }
                    dut = new eMTC();
                    eMTCParser eMTCParser = new eMTCParser(jsonFileReader,dut);
                    boolean packetValidity = eMTCParser.capabilitiesAttachValidator(dut);

                    if (!packetValidity) {
                        JOptionPane.showMessageDialog(CatMPanel.this, Utility.ERROR_CAUSE);
                    } else {
                        dut = (eMTC) eMTCParser.packetParser(dut);
                        catMUISetup(outputArea,dut);
                        eMTC.printCatMDUTCap(dut);
                    }
                }
            }
        });

        setBackground(Color.CYAN);
        setVisible(true);
    }

    /**
     * Private support method to setup UI for catm devices
     * @param container the jpanel containing the output
     * @param device the instance of device under testing
     */
    private void catMUISetup(JPanel container, eMTC device) {
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
        layout.setConstraints(basicInfoButton,constraints);
        container.add(basicInfoButton);

        constraints.gridx = 1;
        layout.setConstraints(fgiButton,constraints);
        container.add(fgiButton);

        constraints.gridx = 2;
        layout.setConstraints(catmFeaturesButton,constraints);
        container.add(catmFeaturesButton);

        constraints.gridx = 0;
        constraints.gridy = 1;
        layout.setConstraints(ciphAlgButton,constraints);
        container.add(ciphAlgButton);

        constraints.gridx = 1;
        layout.setConstraints(intAlgButton,constraints);
        container.add(intAlgButton);

        constraints.gridx = 2;
        layout.setConstraints(bandsInfoButton,constraints);
        container.add(bandsInfoButton);

        basicInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                basicInfoButton.setEnabled(false);
                JTextField attachTypeTextField = new JTextField(Utility.attachTypeDecoder(device.getAttachType()));
                attachTypeTextField.setEditable(false);
                JTextField additionalUpdateTypeTextField = new JTextField(device.getAdditionalUpdateType());
                additionalUpdateTypeTextField.setEditable(false);
                JTextField identityTypeTextField = new JTextField(GUIUtility.identityTypeUIFormatter(device.getIdentityType()));
                identityTypeTextField.setEditable(false);
                JTextField esmTextField = new JTextField(String.valueOf(device.getLteCap().getEIT()));
                esmTextField.setEditable(false);
                JTextField pdnTypeTextField = new JTextField(GUIUtility.pdnTypeUIFormatter(device.getPdnType()));
                pdnTypeTextField.setEditable(false);
                JTextField asRelTextField = new JTextField(String.valueOf(device.getAccessStratumRelease()));
                asRelTextField.setEditable(false);
                JTextField catDLTextField = new JTextField(device.getCategoryDL());
                catDLTextField.setEditable(false);
                JTextField catULTextField = new JTextField(device.getCategoryUL());
                catULTextField.setEditable(false);

                JComponent[] basicInfosCatm = new JComponent[]{
                        new JLabel(GUIUtility.ATTACH_TYPE_LABEL), attachTypeTextField,
                        new JLabel(GUIUtility.ADD_UPDATE_TYPE_LABEL), additionalUpdateTypeTextField,
                        new JLabel(GUIUtility.TYPE_OF_IDENTITY_LABEL), identityTypeTextField,
                        new JLabel(GUIUtility.EIT_LABEL), esmTextField,
                        new JLabel(GUIUtility.PDN_TYPE_LABEL), pdnTypeTextField,
                        new JLabel(GUIUtility.AS_RELEASE_LABEL), asRelTextField,
                        new JLabel(GUIUtility.CATEGORY_DL_LABEL), catDLTextField,
                        new JLabel(GUIUtility.CATEGORY_UL_LABEL), catULTextField
                };

                JOptionPane pane = new JOptionPane(basicInfosCatm);
                // Configure via set methods
                JDialog dialog = pane.createDialog(CatMPanel.this, "Basic Info");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        basicInfoButton.setEnabled(true);
                    }
                });
            }
        });
        fgiButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fgiButton.setEnabled(false);
                JLabel fgiRel8Label = new JLabel(GUIUtility.FGI8_LABEL);
                JTextField fgiRel8TextField = new JTextField(Utility.fgiParser(device.getLteCap().getFeatureGroupIndicators()));
                fgiRel8TextField.setEditable(false);
                JLabel fgi9addR9Label = new JLabel(GUIUtility.FGI9_ADDR9_LABEL);
                JTextField fgi9addR9TextField = new JTextField(Utility.fgiParser(device.getLteCap().getFeatureGroupIndicatorsAddR9()));
                fgi9addR9TextField.setEditable(false);
                JLabel fgi9AddR9TDDLabel = new JLabel(GUIUtility.FGI9_ADDR9_TDD_LABEL);
                JTextField fgi9AddR9TDDTextField = new JTextField(Utility.fgiParser(device.getLteCap().getFeatureGroupIndicatorsTDDAddR9()));
                fgi9AddR9TDDTextField.setEditable(false);
                JLabel fgi9AddR9FDDLabel = new JLabel(GUIUtility.FGI9_ADDR9_FDD_LABEL);
                JTextField fgi9AddR9FDDTextField = new JTextField(Utility.fgiParser(device.getLteCap().getFeatureGroupIndicatorsFDDAddR9()));
                fgi9AddR9FDDTextField.setEditable(false);
                JLabel fgi9FDDLabel = new JLabel(GUIUtility.FGI9_FDD_LABEL);
                JTextField fgi9FDDTextField = new JTextField(Utility.fgiParser(device.getLteCap().getFeatureGroupIndicatorsFDDR9()));
                fgi9FDDTextField.setEditable(false);
                JLabel fgi9TDDLabel = new JLabel(GUIUtility.FGI9_TDD_LABEL);
                JTextField fgi9TDDTextField = new JTextField(Utility.fgiParser(device.getLteCap().getFeatureGroupIndicatorsTDDR9()));
                fgi9TDDTextField.setEditable(false);
                JLabel fgi10Label = new JLabel(GUIUtility.FGI10_LABEL);
                JTextField fgi10TextField = new JTextField(Utility.fgiParser(device.getLteCap().getFeatureGroupIndicatorsR10()));
                fgi10TextField.setEditable(false);
                JLabel fgi10FDDLabel = new JLabel(GUIUtility.FGI10_TDD_LABEL);
                JTextField fgi10FDDTextField = new JTextField(Utility.fgiParser(device.getLteCap().getFeatureGroupIndicatorsR10FDD()));
                fgi10FDDTextField.setEditable(false);
                JLabel fgi10TDDLabel = new JLabel(GUIUtility.FGI10_FDD_LABEL);
                JTextField fgi10TDDTextField = new JTextField(Utility.fgiParser(device.getLteCap().getFeatureGroupIndicatorsR10TDD()));
                fgi10TDDTextField.setEditable(false);

                final JComponent[] fgiComponentsVector = new JComponent[] {
                        fgiRel8Label, fgiRel8TextField,
                        fgi9addR9Label, fgi9addR9TextField,
                        fgi9AddR9TDDLabel, fgi9AddR9TDDTextField,
                        fgi9AddR9FDDLabel, fgi9AddR9FDDTextField,
                        fgi9FDDLabel, fgi9FDDTextField,
                        fgi9TDDLabel, fgi9TDDTextField,
                        fgi10Label, fgi10TextField,
                        fgi10FDDLabel, fgi10FDDTextField,
                        fgi10TDDLabel, fgi10TDDTextField
                };
                JOptionPane pane = new JOptionPane(fgiComponentsVector);
                // Configure via set methods
                JDialog dialog = pane.createDialog(CatMPanel.this, "FGI");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        fgiButton.setEnabled(true);
                    }
                });
            }

        });
        catmFeaturesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                catmFeaturesButton.setEnabled(false);

                JTextField ciotTextField = new JTextField(device.getCiotNetworkBehaviour());
                ciotTextField.setEditable(false);
                JTextField ceModeATextField = new JTextField(GUIUtility.ceModeUIFormatter(device.getCeModeA()));
                ceModeATextField.setEditable(false);
                JTextField ceModeBTextField = new JTextField(GUIUtility.ceModeUIFormatter(device.getCeModeB()));
                ceModeBTextField.setEditable(false);
                JTextField edrxTextField = new JTextField(GUIUtility.missingValueUIFormatter(device.geteDrxValue()));
                edrxTextField.setEditable(false);
                JTextField ptimeTextField = new JTextField(GUIUtility.missingValueUIFormatter(device.getPagingTimeWindow()));
                ptimeTextField.setEditable(false);
                JTextField t3324TextField = new JTextField(GUIUtility.missingValueUIFormatter(device.getT3324()));
                t3324TextField.setEditable(false);
                JTextField t3412TextField = new JTextField(GUIUtility.missingValueUIFormatter(device.getT3412()));
                t3412TextField.setEditable(false);
                JTextField emmRegisteredWoPdnSupport = new JTextField(String.valueOf(device.isEmmRegisteredWoPDNSupport()));
                emmRegisteredWoPdnSupport.setEditable(false);

                JComponent[] catmFeaturesComponents = new JComponent[]{
                        new JLabel(GUIUtility.CIOT_LABEL), ciotTextField,
                        new JLabel(GUIUtility.CE_MODE_A_LABEL), ceModeATextField,
                        new JLabel(GUIUtility.CE_MODE_B_LABEL), ceModeBTextField,
                        new JLabel(GUIUtility.EDRX_LABEL), edrxTextField,
                        new JLabel(GUIUtility.PAGING_TIME_WINDOW_LABEL), ptimeTextField,
                        new JLabel(GUIUtility.T3324_LABEL), t3324TextField,
                        new JLabel(GUIUtility.T3412_LABEL),t3412TextField,
                        new JLabel(GUIUtility.EMM_WO_PDN_CAP_LABEL),emmRegisteredWoPdnSupport
                };
                JOptionPane pane = new JOptionPane(catmFeaturesComponents);
                // Configure via set methods
                JDialog dialog = pane.createDialog(CatMPanel.this, "CatM Features");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        catmFeaturesButton.setEnabled(true);
                    }
                });

            }
        });
        ciphAlgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                ciphAlgButton.setEnabled(false);
                ArrayList<JComponent> ciphComponents = new ArrayList<JComponent>();
                int counter = 0;
                for (String keyName: device.getLteCap().getEncrAlg().keySet()){
                    String key = keyName.toString();
                    String value = device.getLteCap().getEncrAlg().get(key).toString();
                    if (Integer.parseInt(value) == 1) {
                        System.out.println(key);
                        counter++;
                        JTextField temp = new JTextField(key);
                        temp.setEditable(false);
                        ciphComponents.add(temp);
                    }
                }
                if (counter == 0){
                    JTextField empty = new JTextField(Utility.NOT_APPLICABLE);
                    empty.setEditable(false);
                    ciphComponents.add(empty);
                }
                JOptionPane pane = new JOptionPane(ciphComponents.toArray());
                // Configure via set methods
                JDialog dialog = pane.createDialog(CatMPanel.this, "Ciphering Algorithms");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        ciphAlgButton.setEnabled(true);
                    }
                });
            }

        });
        intAlgButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                intAlgButton.setEnabled(false);
                ArrayList<JComponent> intComponents = new ArrayList<JComponent>();
                int counter = 0;
                for (String keyName: device.getLteCap().getIntegrAlg().keySet()){
                    String key = keyName.toString();
                    String value = device.getLteCap().getIntegrAlg().get(key).toString();
                    if (Integer.parseInt(value) == 1) {
                        System.out.println(key);
                        counter++;
                        JTextField temp = new JTextField(key);
                        temp.setEditable(false);
                        intComponents.add(temp);
                    }
                }
                if (counter == 0){
                    JTextField empty = new JTextField(Utility.NOT_APPLICABLE);
                    empty.setEditable(false);
                    intComponents.add(empty);
                }

                JOptionPane pane = new JOptionPane(intComponents.toArray());
                // Configure via set methods
                JDialog dialog = pane.createDialog(CatMPanel.this, "Integrity Algorithms");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        intAlgButton.setEnabled(true);
                    }
                });

            }
        });
        bandsInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bandsInfoButton.setEnabled(false);
                JTextField bandListTextView = new JTextField(GUIUtility.supportedLTEBandsUIFormatter(device.getLteCap().getSupportedBands()));
                bandListTextView.setEditable(false);
                final JComponent[] componentsBand = new JComponent[] {
                        new JLabel(GUIUtility.SUPPORTED_BANDS_LABEL), bandListTextView,
                };
                JOptionPane pane = new JOptionPane(componentsBand);
                // Configure via set methods
                JDialog dialog = pane.createDialog(CatMPanel.this, "Band Info");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        bandsInfoButton.setEnabled(true);
                    }
                });
            }
        });

    }

    /**
     * Private support method to remove all elements and listeners from output area
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
