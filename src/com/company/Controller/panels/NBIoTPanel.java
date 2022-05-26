package com.company.Controller.panels;

import com.company.FileManager;
import com.company.Model.NBIoT;
import com.company.Controller.GUIUtility;
import com.company.Utility;
import com.company.Parser.NBIoTParser;

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
 * Public class to manage panel of the NBIoT devices
 */
public class NBIoTPanel extends JPanel {
    String pcapFileName;
    private NBIoT dut;
    private JLabel fileNameLabel = new JLabel(GUIUtility.FILE_NAME_BTN);
    private JButton browseButton = new JButton(GUIUtility.BROWSE_BTN);
    private JButton parseButton = new JButton(GUIUtility.PARSE_BTN);
    private JPanel outputArea;
    private JButton writeToFileButton = new JButton(GUIUtility.WRITE_TO_FILE_BTN);

    private JButton basicInfoButton = new JButton(GUIUtility.BASIC_INFO_NBIOT_BTN);
    private JButton bandInfoButton = new JButton(GUIUtility.BAND_INFO_BTN);
    private JButton nbIoTInfoButton = new JButton(GUIUtility.NBIOT_FEATURES_BTN);
    private JButton ciphAlgButton = new JButton(GUIUtility.EPS_ENCR_LABEL);
    private JButton intAlgButton = new JButton(GUIUtility.EPS_INTEGR_LABEL);
    private JButton[] buttons = new JButton[]{basicInfoButton, bandInfoButton, nbIoTInfoButton, ciphAlgButton, intAlgButton};

    public NBIoTPanel(){
        super(false);
        GridBagLayout layout = new GridBagLayout();
        setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        // If you don't reset the values, padding remains the same
        constraints.insets = new Insets(10,10,10,10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.gridwidth = 2;
        constraints.weighty = 0.0;
        constraints.weightx = 0.5;
        layout.setConstraints(fileNameLabel,constraints);
        add(fileNameLabel);

        constraints.gridx = 2;
        constraints.gridwidth = 1;
        constraints.weighty = 0.0;
        constraints.weightx = 0.0;
        layout.setConstraints(browseButton,constraints);
        add(browseButton);

        writeToFileButton.setEnabled(false);
        constraints.gridx = 3;
        constraints.gridy = 0;
        constraints.weightx = 0.0;
        constraints.weighty = 0.0;
        layout.setConstraints(writeToFileButton, constraints);
        add(writeToFileButton);

        constraints.gridx = 0;
        constraints.gridy = 1;
        constraints.gridwidth = 4;
        layout.setConstraints(parseButton,constraints);
        parseButton.setEnabled(false);
        add(parseButton);

        outputArea = new JPanel();
        constraints.gridy = 2;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        constraints.anchor = GridBagConstraints.SOUTH;
        outputArea.setBackground(Color.ORANGE);
        layout.setConstraints(outputArea,constraints);
        add(outputArea);

        browseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //Create a file chooser
                // Hide create folder button
                UIManager.put("FileChooser.readOnly", Boolean.TRUE);
                final JFileChooser fc = new JFileChooser();
                // Set file filter
                FileNameExtensionFilter filter = new FileNameExtensionFilter("pcap/pcapng files","pcap","pcapng");
                fc.setFileFilter(filter);
                fc.setCurrentDirectory(null);
                int returnVal = fc.showOpenDialog(NBIoTPanel.this);
                // Manage ok button
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File fileIn = fc.getSelectedFile();
                    pcapFileName = fileIn.getName();
                    // Set program path
                    Utility.INPUT_FILE_PATH = fileIn.getPath();
                    Utility.INPUT_FILE_PATH = Utility.INPUT_FILE_PATH.substring(0, Utility.INPUT_FILE_PATH.lastIndexOf(File.separator));
                    pcapFileName = Utility.INPUT_FILE_PATH.concat("\\").concat(pcapFileName);
                    Utility.PCAP_RRC_FILE_NAME = pcapFileName;
                    fileNameLabel.setText(Utility.PCAP_RRC_FILE_NAME.replace("\"",""));
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
                int rVal = c.showSaveDialog(NBIoTPanel.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    c.getSelectedFile().getName();
                    //dir.setText(c.getCurrentDirectory().toString());
                    FileManager.writeOutputToFile(c.getSelectedFile().getPath(),NBIoT.printNBIoTCap(dut));
                }
            }
        });

        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                parseButton.setEnabled(false);
                if (pcapFileName == null) {
                    JOptionPane.showMessageDialog(NBIoTPanel.this, "No file selected");
                } else {
                    clearView(outputArea);
                    outputArea.removeAll();
                    writeToFileButton.setEnabled(true);
                    File jsonTemp = FileManager.loadInputFile(pcapFileName, Utility.S1AP_TSHARK_FILTER,Utility.TSHARK_OPTION_NBIOT_FILTER);
                    FileReader jsonFileReader = null;
                    try {
                        jsonFileReader = new FileReader(jsonTemp);
                    } catch (FileNotFoundException e1) {
                        // Handle file not found exceptions
                        e1.printStackTrace();
                    }
                    dut = new NBIoT();
                    NBIoTParser parser = new NBIoTParser(jsonFileReader,dut);
                    boolean packetValidity = parser.capabilitiesAttachValidator(dut);
                    if (!packetValidity) {
                        JOptionPane.showMessageDialog(NBIoTPanel.this, Utility.ERROR_CAUSE);
                    } else {
                        dut = (NBIoT) parser.packetParser(dut);
                        nbIoTUISetup(outputArea,dut);
                        NBIoT.printDUTNBIoTCap(dut);
                    }
                }
            }
        });

        setBackground(Color.ORANGE);
        setVisible(true);
    }

    /**
     * Private support method to manage setup of the GUI after parsing step
     * @param container the JPanel containing the output of the program
     * @param device the current instance of nbiot device
     */
    private void nbIoTUISetup(JPanel container, NBIoT device){
        GridBagLayout layout = new GridBagLayout();
        container.setLayout(layout);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.BOTH;
        // If you don't reset the values, padding remains the same
        constraints.insets = new Insets(10,10,10,10);

        constraints.gridx = 0;
        constraints.gridy = 0;
        constraints.weightx = 1.0;
        constraints.weighty = 1.0;
        layout.setConstraints(basicInfoButton,constraints);
        container.add(basicInfoButton);

        constraints.gridx = 1;
        layout.setConstraints(bandInfoButton,constraints);
        container.add(bandInfoButton);

        constraints.gridx = 2;
        layout.setConstraints(nbIoTInfoButton,constraints);
        container.add(nbIoTInfoButton);

        constraints.gridx = 0;
        constraints.gridy = 1;
        layout.setConstraints(ciphAlgButton,constraints);
        container.add(ciphAlgButton);

        constraints.gridx = 1;
        layout.setConstraints(intAlgButton,constraints);
        container.add(intAlgButton);

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
                JTextField pdnTextField = new JTextField(GUIUtility.pdnTypeUIFormatter(device.getPdnType()));
                pdnTextField.setEditable(false);
                JTextField asReleaseTextField = new JTextField(String.valueOf(device.getAccessStratumRelease()));
                asReleaseTextField.setEditable(false);
                JTextField categoryTextField = new JTextField(device.getCategory());
                categoryTextField.setEditable(false);
                JComponent[] basicInfos = new JComponent[]{
                        new JLabel(GUIUtility.ATTACH_TYPE_LABEL), attachTypeTextField,
                        new JLabel(GUIUtility.ADD_UPDATE_TYPE_LABEL), additionalUpdateTypeTextField,
                        new JLabel(GUIUtility.TYPE_OF_IDENTITY_LABEL), identityTypeTextField,
                        new JLabel(GUIUtility.EIT_LABEL), esmTextField,
                        new JLabel(GUIUtility.PDN_TYPE_LABEL), pdnTextField,
                        new JLabel(GUIUtility.AS_RELEASE_LABEL), asReleaseTextField,
                        new JLabel(GUIUtility.CATEGORY_LABEL), categoryTextField
                };
                JOptionPane pane = new JOptionPane(basicInfos);
                // Configure via set methods
                JDialog dialog = pane.createDialog(NBIoTPanel.this, "Basic Info");
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
        bandInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                bandInfoButton.setEnabled(false);
                JTextField bandListTextView = new JTextField(GUIUtility.supportedLTEBandsUIFormatter(device.getLteCap().getSupportedBands()));
                bandListTextView.setEditable(false);
                final JComponent[] componentsBand = new JComponent[] {
                        new JLabel(GUIUtility.SUPPORTED_BANDS_LABEL), bandListTextView,
                };
                JOptionPane pane = new JOptionPane(componentsBand);
                // Configure via set methods
                JDialog dialog = pane.createDialog(NBIoTPanel.this, "Band Info");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        bandInfoButton.setEnabled(true);
                    }
                });
            }
        });
        nbIoTInfoButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                nbIoTInfoButton.setEnabled(false);
                JTextField ciotTextField = new JTextField(device.getCiotNetworkBehaviour());
                ciotTextField.setEditable(false);
                JTextField multitoneTextField = new JTextField(String.valueOf(device.isMultiTone()));
                multitoneTextField.setEditable(false);
                JTextField multiCarrierTextField = new JTextField(String.valueOf(device.isMultiCarrier()));
                multiCarrierTextField.setEditable(false);
                JTextField edrxTextField = new JTextField(GUIUtility.missingValueUIFormatter(device.geteDrxValue()));
                edrxTextField.setEditable(false);
                JTextField pagingTimeTextField = new JTextField(GUIUtility.missingValueUIFormatter(device.getPagingTimeWindow()));
                pagingTimeTextField.setEditable(false);
                JTextField t3324TextField = new JTextField(GUIUtility.missingValueUIFormatter(device.getT3324()));
                t3324TextField.setEditable(false);
                JTextField t3412TextField = new JTextField(GUIUtility.missingValueUIFormatter(device.getT3412()));
                t3412TextField.setEditable(false);
                JTextField emmRegisteredWoPdnSupport = new JTextField(String.valueOf(device.isEmmRegisteredWoPDNSupport()));
                emmRegisteredWoPdnSupport.setEditable(false);

                JComponent[] nbIoTComponents = new JComponent[]{
                        new JLabel(GUIUtility.CIOT_LABEL), ciotTextField,
                        new JLabel(GUIUtility.MULTITONE_LABEL), multitoneTextField,
                        new JLabel(GUIUtility.MULTICARRIER_LABEL), multiCarrierTextField,
                        new JLabel(GUIUtility.EDRX_LABEL), edrxTextField,
                        new JLabel(GUIUtility.PAGING_TIME_WINDOW_LABEL), pagingTimeTextField,
                        new JLabel(GUIUtility.T3324_LABEL), t3324TextField,
                        new JLabel(GUIUtility.T3412_LABEL), t3412TextField,
                        new JLabel(GUIUtility.EMM_WO_PDN_CAP_LABEL),emmRegisteredWoPdnSupport
                };
                JOptionPane pane = new JOptionPane(nbIoTComponents);
                // Configure via set methods
                JDialog dialog = pane.createDialog(NBIoTPanel.this, "Supported features");
                dialog.setModal(false); // this says not to block background components
                dialog.setVisible(true);
                pane.addPropertyChangeListener(new PropertyChangeListener() {
                    @Override
                    public void propertyChange(PropertyChangeEvent evt) {
                        nbIoTInfoButton.setEnabled(true);
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
                JDialog dialog = pane.createDialog(NBIoTPanel.this, "Ciphering Algorithms");
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
                JDialog dialog = pane.createDialog(NBIoTPanel.this, "Integrity Algorithms");
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
