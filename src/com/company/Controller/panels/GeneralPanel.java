package com.company.Controller.panels;

import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;

import com.company.FileManager;
import com.company.Utility;
import com.company.Controller.GUIUtility;
import com.company.Controller.dialog.BandCombinationDialog;
import com.company.Controller.dialog.ScrollDialog;
import com.company.Model.DUT;
import com.company.Model.Mobile;
import com.company.Parser.MobileParser;

public class GeneralPanel extends JPanel {
    private String pcapFileName = null;
    private JPanel outputArea;
    private JLabel fileNameLabel;
    private JButton browseButton;
    private JButton parseButton;
    private Mobile dut;
    private JButton writeToFileButton = new JButton(GUIUtility.WRITE_TO_FILE_BTN);
    private JButton exportENDCButton = new JButton(GUIUtility.EXPORT_ENDC_BTN);
    

    // Output graphic components
    private JButton basicInfoLTEButton = new JButton(GUIUtility.LTE_BASIC_INFO_BTN);
    private JButton fgiButton = new JButton(GUIUtility.FGI_BTN);
    private JButton gsmCodecsButton = new JButton(GUIUtility.GSM_CODECS_BTN);
    private JButton umtsCodecsButton = new JButton(GUIUtility.UMTS_CODECS_BTN);
    private JButton ciphAlgUMTSButton = new JButton(GUIUtility.UMTS_CIPHERING_ALGORITHMS_BTN);
    private JButton intAlgUMTSButton = new JButton(GUIUtility.UMTS_INTEGRITY_ALGORITHMS_BTN);
    private JButton lteBandInfo = new JButton(GUIUtility.BAND_INFO_LTE_BTN);
    private JButton encrAlgLTEButton = new JButton(GUIUtility.CIPHERING_ALGORITHMS_LTE_BTN);
    private JButton integrAlgLTEButton = new JButton(GUIUtility.INTEGRITY_ALGORITHMS_LTE_BTN);
    private JButton v10i0CombButtonDL = new JButton(GUIUtility.BAND_COMB_DL_10I0_BTN);
    private JButton v10i0CombButtonUL = new JButton(GUIUtility.BAND_COMB_UL_10I0_BTN);
    private JButton v1020CombButtonDL = new JButton(GUIUtility.BAND_COMB_DL_1020_BTN);
    private JButton v1020CombButtonUL = new JButton(GUIUtility.BAND_COMB_UL_1020_BTN);
    private JButton v1310CombButtonDL = new JButton(GUIUtility.BAND_COMB_DL_1310_BTN);
    private JButton v1310CombButtonUL = new JButton(GUIUtility.BAND_COMB_UL_1310_BTN);
    private JButton enNRCombButtonDL = new JButton(GUIUtility.BAND_COMB_DL_EN_NR_BTN);
    private JButton enNRCombButtonUL = new JButton(GUIUtility.BAND_COMB_UL_EN_NR_BTN);
    private JButton gsmBasicInfoButton = new JButton(GUIUtility.GSM_BASIC_INFO_BTN);
    private JButton umtsBasicIfoButton = new JButton(GUIUtility.UMTS_BASIC_INFO_BTN);
    private JButton encrAlgGSMButton = new JButton(GUIUtility.GSM_CIPH_ALG_BTN);
    private JButton gsmBandInfoButton = new JButton(GUIUtility.GSM_BAND_INFO_BTN);
    private JButton umtsBandInfoButton = new JButton(GUIUtility.UMTS_BAND_INFO_BTN);
    private JButton generalButton = new JButton(GUIUtility.GENERAL_INFO_BTN);
    private JButton[] rrcButtons = {basicInfoLTEButton,fgiButton,gsmCodecsButton,umtsCodecsButton, ciphAlgUMTSButton, encrAlgLTEButton, intAlgUMTSButton,
            integrAlgLTEButton ,lteBandInfo,v10i0CombButtonDL,v10i0CombButtonUL,v1020CombButtonDL,v1020CombButtonUL,v1310CombButtonDL,
            v1310CombButtonUL, gsmBasicInfoButton, umtsBasicIfoButton, encrAlgGSMButton,gsmBandInfoButton, umtsBandInfoButton, generalButton,
            enNRCombButtonDL,enNRCombButtonUL};

    public GeneralPanel() {
        super(false);
        GridBagLayout layout = new GridBagLayout();
        this.setLayout(layout);
        GridBagConstraints layoutContraints = new GridBagConstraints();
        layoutContraints.fill = GridBagConstraints.BOTH;
        // If you don't reset the values, padding remains the same
        layoutContraints.insets = new Insets(10,10,10,10);

        fileNameLabel = new JLabel(GUIUtility.FILE_NAME_BTN);
        layoutContraints.weightx = 0.5;
        layoutContraints.weighty = 0.0;
        layoutContraints.gridx = 0;
        layoutContraints.gridy = 0;
        layout.setConstraints(fileNameLabel, layoutContraints);
        add(fileNameLabel);

        browseButton = new JButton(GUIUtility.BROWSE_BTN);
        layoutContraints.gridx = 1;
        layoutContraints.gridy = 0;
        layoutContraints.weightx = 0.0;
        layoutContraints.weighty = 0.0;
        layout.setConstraints(browseButton, layoutContraints);
        add(browseButton);

        writeToFileButton.setEnabled(false);
        layoutContraints.gridx = 2;
        layoutContraints.gridy = 0;
        layoutContraints.weightx = 0.0;
        layoutContraints.weighty = 0.0;
        layout.setConstraints(writeToFileButton, layoutContraints);
        add(writeToFileButton);
        
        exportENDCButton.setEnabled(false);
        layoutContraints.gridx = 3;
        layoutContraints.gridy = 0;
        layoutContraints.weightx = 0.0;
        layoutContraints.weighty = 0.0;
        layout.setConstraints(exportENDCButton, layoutContraints);
        add(exportENDCButton);

        parseButton = new JButton(GUIUtility.PARSE_BTN);
        parseButton.setEnabled(false);
        layoutContraints.weightx = 0.0;
        layoutContraints.weighty = 0.0;
        layoutContraints.gridwidth = 4;
        layoutContraints.gridx = 0;
        layoutContraints.gridy = 1;
        layout.setConstraints(parseButton, layoutContraints);
        add(parseButton);


        outputArea = new JPanel();
        layoutContraints.weightx = 1.0;
        layoutContraints.weighty = 1.0;   //request any extra vertical space
        layoutContraints.anchor = GridBagConstraints.SOUTH; //bottom of space
        layoutContraints.gridy = 2;
        layoutContraints.gridwidth = 4;
        outputArea.setBackground(Color.decode("#79c143"));
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
                int returnVal = fc.showOpenDialog(GeneralPanel.this);
                // Manage ok button
                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    File fileIn = fc.getSelectedFile();
                    // Set program path
                    Utility.INPUT_FILE_PATH = fileIn.getPath();
                    Utility.INPUT_FILE_PATH = Utility.INPUT_FILE_PATH.substring(0, Utility.INPUT_FILE_PATH.lastIndexOf(File.separator));
                    pcapFileName = fileIn.getName();
                    pcapFileName = Utility.INPUT_FILE_PATH.concat("\\").concat(pcapFileName);
                    Utility.PCAP_RRC_FILE_NAME = pcapFileName;
                    fileNameLabel.setText(Utility.PCAP_RRC_FILE_NAME.replace("\"",""));
                    parseButton.setEnabled(true);
                    writeToFileButton.setEnabled(false);
                    clearView(outputArea);
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
                int rVal = c.showSaveDialog(GeneralPanel.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    c.getSelectedFile().getName();
                    //dir.setText(c.getCurrentDirectory().toString());
                    FileManager.writeOutputToFile(c.getSelectedFile().getPath(),Mobile.printCap(dut));
                }
            }
        });
        
        exportENDCButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser c = new JFileChooser();
                // Demonstrate "Save" dialog:
                String ext = "xlsx";
                c.setFileFilter(new FileNameExtensionFilter(ext,"." + ext));
                int rVal = c.showSaveDialog(GeneralPanel.this);
                if (rVal == JFileChooser.APPROVE_OPTION) {
                    c.getSelectedFile().getName();
                    String add = "";
                    if(!c.getSelectedFile().getName().toLowerCase().endsWith(ext.toLowerCase()))
                    	add = "." + ext;
                    FileManager.writeOutputToExcel(c.getSelectedFile().getPath() + add,Mobile.printENDCBandCap_Excel(dut));
                    
                }
            }
        });
        
        parseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File jsonFile = null;
                if (pcapFileName == null) {
                    JOptionPane.showMessageDialog(GeneralPanel.this, "No file selected");
                } else {
                    File jsonTemp = FileManager.loadInputFile(pcapFileName, Utility.S1AP_TSHARK_FILTER,Utility.TSHARK_OPTION_LEGACY_FILTER);
                    FileReader jsonFileReader = null;
                    try {
                        jsonFileReader = new FileReader(jsonTemp);
                    } catch (FileNotFoundException f) {
                        // Handle file not found exceptions
                        f.printStackTrace();
                    }
                    dut = new Mobile();
                    MobileParser parser = new MobileParser(jsonFileReader,dut);
                    boolean packetValidity = parser.capabilitiesAttachValidator(dut);
                    if (!packetValidity) {
                        JOptionPane.showMessageDialog(GeneralPanel.this, Utility.ERROR_CAUSE);
                    } else {
                        dut = (Mobile) parser.packetParser(dut);
                        // Parse sgsap for IMEI SVN
                        jsonTemp = FileManager.loadInputFile(pcapFileName, Utility.SGSAP_TSHARK_FILTER, Utility.TSHARK_OPTION_LEGACY_FILTER);
                        try {
                            jsonFileReader = new FileReader(jsonTemp);
                        } catch (FileNotFoundException ff) {
                            ff.printStackTrace();
                        }

                        MobileParser sgsParser = new MobileParser(jsonFileReader, dut);
                        try {
                            dut = (Mobile) sgsParser.svnParser(dut);
                        } catch (UnsupportedEncodingException ex) {
                            ex.printStackTrace();
                        }
                        if (dut != null) {
                            writeToFileButton.setEnabled(true);
                            if(dut.getMrdcBandCombinations()!=null && dut.getMrdcBandCombinations().size()>0)
                            	exportENDCButton.setEnabled(true);
                            //Mobile.printCAComb(dut);
                            Mobile.printDUTCap(dut);
                            DUT.printIMEISV(dut);
                            final Mobile device = dut;
                            parseButton.setEnabled(false);
                            clearView(outputArea);
                            GridBagLayout innerPanelLayout = new GridBagLayout();
                            outputArea.setLayout(innerPanelLayout);
                            GridBagConstraints innerLayoutContraints = new GridBagConstraints();

                            // 0,0
                            innerLayoutContraints.fill = GridBagConstraints.BOTH;
                            innerLayoutContraints.insets = new Insets(10, 10, 10, 10);
                            innerLayoutContraints.weightx = 1;
                            innerLayoutContraints.weighty = 1;
                            innerLayoutContraints.anchor = GridBagConstraints.NORTH; //bottom of space
                            innerLayoutContraints.gridheight = 6;
                            innerLayoutContraints.gridwidth = 1;
                            innerLayoutContraints.gridy = 0;
                            // gridx is the column
                            innerLayoutContraints.gridx = 0;
                            innerPanelLayout.setConstraints(generalButton, innerLayoutContraints);
                            outputArea.add(generalButton);

                            innerLayoutContraints.gridheight = 2;
                            innerLayoutContraints.gridx = 2;
                            innerLayoutContraints.gridy = 0;
                            innerPanelLayout.setConstraints(gsmBasicInfoButton, innerLayoutContraints);
                            outputArea.add(gsmBasicInfoButton);

                            innerLayoutContraints.gridy = 2;
                            innerPanelLayout.setConstraints(umtsBasicIfoButton, innerLayoutContraints);
                            outputArea.add(umtsBasicIfoButton);

                            innerLayoutContraints.gridy = 4;
                            innerPanelLayout.setConstraints(basicInfoLTEButton, innerLayoutContraints);
                            outputArea.add(basicInfoLTEButton);

                            // Separator
                            innerLayoutContraints.gridheight = 20;
                            innerLayoutContraints.gridwidth = 1;
                            innerLayoutContraints.gridy = 0;
                            innerLayoutContraints.gridx = 1;
                            JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
                            innerPanelLayout.addLayoutComponent(sep, innerLayoutContraints);
                            outputArea.add(sep);

                            // Second square
                            innerLayoutContraints.gridx = 4;
                            innerLayoutContraints.gridy = 0;
                            innerLayoutContraints.gridheight = 2;
                            innerLayoutContraints.gridwidth = 1;
                            innerPanelLayout.setConstraints(gsmBandInfoButton, innerLayoutContraints);
                            outputArea.add(gsmBandInfoButton);

                            innerLayoutContraints.gridx = 4;
                            innerLayoutContraints.gridy = 2;
                            innerLayoutContraints.gridheight = 2;
                            innerLayoutContraints.gridwidth = 1;
                            innerPanelLayout.setConstraints(umtsBandInfoButton, innerLayoutContraints);
                            outputArea.add(umtsBandInfoButton);

                            innerLayoutContraints.gridx = 4;
                            innerLayoutContraints.gridy = 4;
                            innerLayoutContraints.gridheight = 2;
                            innerLayoutContraints.gridwidth = 1;
                            innerPanelLayout.setConstraints(lteBandInfo, innerLayoutContraints);
                            outputArea.add(lteBandInfo);

                            // Separator
                            innerLayoutContraints.gridheight = 20;
                            innerLayoutContraints.gridwidth = 1;
                            innerLayoutContraints.gridy = 0;
                            innerLayoutContraints.gridx = 3;
                            JSeparator sep2 = new JSeparator(SwingConstants.VERTICAL);
                            innerPanelLayout.addLayoutComponent(sep2, innerLayoutContraints);
                            outputArea.add(sep2);

                            // Third square
                            innerLayoutContraints.gridx = 0;
                            innerLayoutContraints.gridy = 7;
                            innerLayoutContraints.gridheight = 2;
                            innerLayoutContraints.gridwidth = 1;
                            innerPanelLayout.setConstraints(encrAlgGSMButton, innerLayoutContraints);
                            outputArea.add(encrAlgGSMButton);

                            innerLayoutContraints.gridx = 0;
                            innerLayoutContraints.gridy = 9;
                            innerLayoutContraints.gridheight = 2;
                            innerLayoutContraints.gridwidth = 1;
                            innerPanelLayout.setConstraints(ciphAlgUMTSButton, innerLayoutContraints);
                            outputArea.add(ciphAlgUMTSButton);

                            innerLayoutContraints.gridx = 0;
                            innerLayoutContraints.gridy = 11;
                            innerPanelLayout.setConstraints(encrAlgLTEButton, innerLayoutContraints);
                            outputArea.add(encrAlgLTEButton);

                            // Separator
                            innerLayoutContraints.gridheight = 1;
                            innerLayoutContraints.gridwidth = 20;
                            innerLayoutContraints.gridy = 6;
                            innerLayoutContraints.gridx = 0;
                            JSeparator sep3 = new JSeparator(SwingConstants.HORIZONTAL);
                            innerPanelLayout.addLayoutComponent(sep3, innerLayoutContraints);
                            outputArea.add(sep3);

                            // Fourth square
                            innerLayoutContraints.gridx = 2;
                            innerLayoutContraints.gridy = 7;
                            innerLayoutContraints.gridheight = 3;
                            innerLayoutContraints.gridwidth = 1;
                            innerPanelLayout.setConstraints(intAlgUMTSButton, innerLayoutContraints);
                            outputArea.add(intAlgUMTSButton);

                            innerLayoutContraints.gridy = 10;
                            innerPanelLayout.setConstraints(integrAlgLTEButton, innerLayoutContraints);
                            outputArea.add(integrAlgLTEButton);

                            // Fifth square
                            innerLayoutContraints.gridx = 4;
                            innerLayoutContraints.gridy = 7;
                            innerLayoutContraints.gridheight = 3;
                            innerLayoutContraints.gridwidth = 1;
                            innerPanelLayout.setConstraints(gsmCodecsButton, innerLayoutContraints);
                            outputArea.add(gsmCodecsButton);

                            innerLayoutContraints.gridx = 4;
                            innerLayoutContraints.gridy = 10;
                            innerLayoutContraints.gridheight = 3;
                            innerLayoutContraints.gridwidth = 1;
                            innerPanelLayout.setConstraints(umtsCodecsButton, innerLayoutContraints);
                            outputArea.add(umtsCodecsButton);

                            // Separator
                            innerLayoutContraints.gridheight = 1;
                            innerLayoutContraints.gridwidth = 20;
                            innerLayoutContraints.gridy = 13;
                            innerLayoutContraints.gridx = 0;
                            JSeparator sep4 = new JSeparator(SwingConstants.HORIZONTAL);
                            innerPanelLayout.addLayoutComponent(sep4, innerLayoutContraints);
                            outputArea.add(sep4);

                            innerLayoutContraints.gridheight = 1;
                            innerLayoutContraints.gridwidth = 1;
                            innerLayoutContraints.gridy = 14;
                            innerLayoutContraints.gridx = 0;
                            innerPanelLayout.setConstraints(fgiButton, innerLayoutContraints);
                            outputArea.add(fgiButton);
                            
                            innerLayoutContraints.gridx = 0;
                            innerLayoutContraints.gridy = 17;
                            innerLayoutContraints.gridheight = 1;
                            innerLayoutContraints.gridwidth = 1;
                            innerPanelLayout.setConstraints(enNRCombButtonDL, innerLayoutContraints);
                            outputArea.add(enNRCombButtonDL);

                            innerLayoutContraints.gridheight = 1;
                            innerLayoutContraints.gridwidth = 1;
                            innerLayoutContraints.gridx = 0;
                            innerLayoutContraints.gridy = 18;
                            innerPanelLayout.setConstraints(enNRCombButtonUL, innerLayoutContraints);
                            outputArea.add(enNRCombButtonUL);
                            
                            if (device.isCa1020Support()) {
                                innerLayoutContraints.gridx = 2;
                                innerLayoutContraints.gridy = 14;
                                innerLayoutContraints.gridheight = 3;
                                innerLayoutContraints.gridwidth = 1;
                                innerPanelLayout.setConstraints(v1020CombButtonDL, innerLayoutContraints);
                                outputArea.add(v1020CombButtonDL);

                                innerLayoutContraints.gridheight = 3;
                                innerLayoutContraints.gridwidth = 1;
                                innerLayoutContraints.gridx = 2;
                                innerLayoutContraints.gridy = 17;
                                innerPanelLayout.setConstraints(v1020CombButtonUL, innerLayoutContraints);
                                outputArea.add(v1020CombButtonUL);

                                if (device.isCa10i0Support()) {
                                    innerLayoutContraints.gridx = 4;
                                    innerLayoutContraints.gridy = 14;
                                    innerPanelLayout.setConstraints(v10i0CombButtonDL, innerLayoutContraints);
                                    outputArea.add(v10i0CombButtonDL);
                                    innerLayoutContraints.gridx = 4;
                                    innerLayoutContraints.gridy = 17;
                                    innerPanelLayout.setConstraints(v10i0CombButtonUL, innerLayoutContraints);
                                    outputArea.add(v10i0CombButtonUL);
                                }
                            }
                            ////// 1310
                            else if (device.isCa1310Support()) {
                                innerLayoutContraints.gridx = 2;
                                innerLayoutContraints.gridy = 14;
                                innerLayoutContraints.gridheight = 3;
                                innerLayoutContraints.gridwidth = 1;
                                innerPanelLayout.setConstraints(v1310CombButtonDL, innerLayoutContraints);
                                outputArea.add(v1310CombButtonDL);

                                innerLayoutContraints.gridheight = 3;
                                innerLayoutContraints.gridwidth = 1;
                                innerLayoutContraints.gridx = 2;
                                innerLayoutContraints.gridy = 17;
                                innerPanelLayout.setConstraints(v1310CombButtonUL, innerLayoutContraints);
                                outputArea.add(v1310CombButtonUL);
                            }

                            basicInfoLTEButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    basicInfoLTEButton.setEnabled(false);
                                    JTextField asReleaseTextField = new JTextField(String.valueOf(device.getAccessStratumRelease()));
                                    asReleaseTextField.setEditable(false);
                                    JTextField eitTextField = new JTextField(String.valueOf(device.getLteCap().getEIT()));
                                    eitTextField.setEditable(false);
                                    JTextField voiceDomainTextField = new JTextField(Utility.voiceDomainPreferenceDecoder(device.getVoiceDomainPreference()));
                                    voiceDomainTextField.setEditable(false);
                                    JTextField additionalUpdateTextField = new JTextField(device.getAdditionalUpdateType());
                                    additionalUpdateTextField.setEditable(false);
                                    JTextField attachTypeTextField = new JTextField(Utility.attachTypeDecoder(device.getAttachType()));
                                    attachTypeTextField.setEditable(false);
                                    JTextField SRVCCCapabilityTextField = new JTextField(String.valueOf(device.isSRVCCToGERANUTRANCapability()));
                                    SRVCCCapabilityTextField.setEditable(false);
                                    JTextField ueUsageSettingsTextField = new JTextField(device.getUeUsageSettings());
                                    ueUsageSettingsTextField.setEditable(false);
                                    JTextField supportedRohcPoriflesTextField = new JTextField(GUIUtility.supportedROHCProfilesUIFOrmatter(device.getLteCap().getSupportedRohcProfiles_r15()));
                                    supportedRohcPoriflesTextField.setEditable(false);
                                    JTextField ueTxAntennaSelSuppTextField = new JTextField(String.valueOf(device.getLteCap().isTxAntennaSelectionSupport()));
                                    ueTxAntennaSelSuppTextField.setEditable(false);
                                    JTextField ueSpecRefSigsTextField = new JTextField(String.valueOf(device.getLteCap().isUeSpecificRefsSigsSupport()));
                                    ueSpecRefSigsTextField.setEditable(false);
                                    JTextField psHOGERANTextField = new JTextField(String.valueOf(device.isInterRatPSHOToGERAN()));
                                    psHOGERANTextField.setEditable(false);
                                    JTextField eRedirectionGERANTextField = new JTextField(String.valueOf(device.getLteCap().iseRedirectionGERAN()));
                                    eRedirectionGERANTextField.setEditable(false);
                                    JTextField eRedirectionUTRANTextField = new JTextField(String.valueOf(device.getLteCap().iseRedirectionUTRAN()));
                                    eRedirectionUTRANTextField.setEditable(false);
                                    JTextField sonRACHTextField = new JTextField(String.valueOf(device.getLteCap().isRachReportSON()));
                                    sonRACHTextField.setEditable(false);

                                    if (device.getCategory() != 0) {
                                        JTextField categoriesTextField = new JTextField(String.valueOf(device.getCategory()) + " - ( " + device.getCategoriesAsString() + " )");
                                        categoriesTextField.setEditable(false);

                                        final JComponent[] basicInfosSingleCat = new JComponent[]{
                                                new JLabel(GUIUtility.AS_RELEASE_LABEL), asReleaseTextField,
                                                new JLabel(GUIUtility.EIT_LABEL), eitTextField,
                                                new JLabel(GUIUtility.UE_USAGE_SETTINGS_LABEL), ueUsageSettingsTextField,
                                                new JLabel(GUIUtility.VOICE_DOMAIN_LABEL), voiceDomainTextField,
                                                new JLabel(GUIUtility.ADD_UPDATE_TYPE_LABEL), additionalUpdateTextField,
                                                new JLabel(GUIUtility.ATTACH_TYPE_LABEL), attachTypeTextField,
                                                new JLabel(GUIUtility.SRVCC_GERAN_UTRAN_LABEL), SRVCCCapabilityTextField,
                                                new JLabel(GUIUtility.CATEGORY_LABEL), categoriesTextField,
                                                new JLabel(GUIUtility.SUPPORTED_ROHC_PROFILES_LABEL), supportedRohcPoriflesTextField,
                                                new JLabel(GUIUtility.UE_TX_ANTENNA_SEL_SUPP_LABEL),ueTxAntennaSelSuppTextField,
                                                new JLabel(GUIUtility.UE_SPECIFIC_REF_SIGS_SUPP_LABEL),ueSpecRefSigsTextField,
                                                new JLabel(GUIUtility.INTER_RAT_PS_HO_GERAN_LABEL), psHOGERANTextField,
                                                new JLabel(GUIUtility.E_REDIRECTION_GERAN_LABEL),eRedirectionGERANTextField,
                                                new JLabel(GUIUtility.E_REDIRECTION_UTRAN_LABEL),eRedirectionUTRANTextField,
                                                new JLabel(GUIUtility.SON_PARAMETER_RACH_REPORT_LABEL),sonRACHTextField
                                        };
                                        ScrollDialog dialog = new ScrollDialog(basicInfosSingleCat,"Basic Info");
                                        dialog.addWindowListener(new WindowAdapter() {
                                            @Override
                                            public void windowClosing(WindowEvent e) {
                                                super.windowClosing(e);
                                                basicInfoLTEButton.setEnabled(true);
                                            }
                                        });

                                    } else { // Cat DL should be displayed
                                        JLabel catDLLabel = new JLabel(GUIUtility.CATEGORY_DL_LABEL);
                                        JTextField catDLTextField = new JTextField(String.valueOf(device.getCategoryDL()) +
                                                " - ( " + device.getCategoryDLAsString().substring(0, device.getCategoryDLAsString().length() - String.valueOf(device.getCategoryDL()).length()) + " )");
                                        catDLTextField.setEditable(false);
                                        JLabel catULLabel = new JLabel(GUIUtility.CATEGORY_UL_LABEL);
                                        JTextField catULTextField = new JTextField(device.getCategoryULAsString());
                                        catULTextField.setEditable(false);
                                        JTextField categoriesTextField = new JTextField("( " + device.getCategoriesAsString() + " )");
                                        categoriesTextField.setEditable(false);
                                        final JComponent[] basicInfos = new JComponent[]{
                                                new JLabel(GUIUtility.AS_RELEASE_LABEL), asReleaseTextField,
                                                new JLabel(GUIUtility.EIT_LABEL), eitTextField,
                                                new JLabel(GUIUtility.UE_USAGE_SETTINGS_LABEL), ueUsageSettingsTextField,
                                                new JLabel(GUIUtility.VOICE_DOMAIN_LABEL), voiceDomainTextField,
                                                new JLabel(GUIUtility.ADD_UPDATE_TYPE_LABEL), additionalUpdateTextField,
                                                new JLabel(GUIUtility.ATTACH_TYPE_LABEL), attachTypeTextField,
                                                new JLabel(GUIUtility.SRVCC_GERAN_UTRAN_LABEL), SRVCCCapabilityTextField,
                                                catDLLabel, catDLTextField,
                                                catULLabel, catULTextField,
                                                new JLabel(GUIUtility.CATEGORY_LABEL), categoriesTextField,
                                                new JLabel(GUIUtility.SUPPORTED_ROHC_PROFILES_LABEL), supportedRohcPoriflesTextField,
                                                new JLabel(GUIUtility.UE_TX_ANTENNA_SEL_SUPP_LABEL),ueTxAntennaSelSuppTextField,
                                                new JLabel(GUIUtility.UE_SPECIFIC_REF_SIGS_SUPP_LABEL),ueSpecRefSigsTextField,
                                                new JLabel(GUIUtility.INTER_RAT_PS_HO_GERAN_LABEL), psHOGERANTextField,
                                                new JLabel(GUIUtility.E_REDIRECTION_GERAN_LABEL),eRedirectionGERANTextField,
                                                new JLabel(GUIUtility.E_REDIRECTION_UTRAN_LABEL),eRedirectionUTRANTextField,
                                                new JLabel(GUIUtility.SON_PARAMETER_RACH_REPORT_LABEL),sonRACHTextField
                                        };
                                        ScrollDialog dialog = new ScrollDialog(basicInfos,"Basic Info");
                                        dialog.addWindowListener(new WindowAdapter() {
                                            @Override
                                            public void windowClosing(WindowEvent e) {
                                                super.windowClosing(e);
                                                basicInfoLTEButton.setEnabled(true);
                                            }
                                        });
                                    }
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

                                    final JComponent[] fgiComponentsVector = new JComponent[]{
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

                                    ScrollDialog dialog = new ScrollDialog(fgiComponentsVector,"FGI");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            fgiButton.setEnabled(true);
                                        }
                                    });

                                }
                            });
                            gsmCodecsButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    gsmCodecsButton.setEnabled(false);
                                    ArrayList<JComponent> fgiComponents = new ArrayList<JComponent>();
                                    int counter = 0;
                                    for (String keyName : device.getGsmCap().getCodecs().keySet()) {
                                        String key = keyName.toString();
                                        String value = device.getGsmCap().getCodecs().get(key).toString();
                                        if (Integer.parseInt(value) == 1) {
                                            //System.out.println(key);
                                            counter++;
                                            JTextField temp = new JTextField(key);
                                            temp.setEditable(false);
                                            fgiComponents.add(temp);
                                        }
                                    }
                                    if (counter == 0) {
                                        JTextField empty = new JTextField(Utility.NOT_APPLICABLE);
                                        empty.setEditable(false);
                                        fgiComponents.add(empty);
                                    }

                                    JComponent[] fgiVec = new JComponent[fgiComponents.size()];
                                    for (int j = 0 ; j < fgiComponents.size(); j++) {
                                        fgiVec[j] = fgiComponents.get(j);
                                    }
                                    ScrollDialog dialog = new ScrollDialog(fgiVec,"GSM Codecs");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            gsmCodecsButton.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            umtsCodecsButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    umtsCodecsButton.setEnabled(false);
                                    ArrayList<JComponent> fgiComponents = new ArrayList<JComponent>();
                                    int counter = 0;
                                    for (String keyName : device.getUmtsCap().getCodecs().keySet()) {
                                        String key = keyName.toString();
                                        String value = device.getUmtsCap().getCodecs().get(key).toString();
                                        if (Integer.parseInt(value) == 1) {
                                            counter++;
                                            JTextField temp = new JTextField(key);
                                            temp.setEditable(false);
                                            fgiComponents.add(temp);
                                        }
                                    }
                                    if (counter == 0) {
                                        JTextField empty = new JTextField(Utility.NOT_APPLICABLE);
                                        empty.setEditable(false);
                                        fgiComponents.add(empty);
                                    }
                                    JComponent[] fgiVec = new JComponent[fgiComponents.size()];
                                    for (int j = 0 ; j < fgiComponents.size(); j++) {
                                        fgiVec[j] = fgiComponents.get(j);
                                    }
                                    ScrollDialog dialog = new ScrollDialog(fgiVec,"3G Codecs");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            umtsCodecsButton.setEnabled(true);
                                        }
                                    });

                                }
                            });
                            ciphAlgUMTSButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    ciphAlgUMTSButton.setEnabled(false);
                                    ArrayList<JComponent> fgiComponents = new ArrayList<JComponent>();
                                    int counter = 0;
                                    for (String keyName : device.getUmtsCap().getEncrAlg().keySet()) {
                                        String key = keyName.toString();
                                        String value = device.getUmtsCap().getEncrAlg().get(key).toString();
                                        if (Integer.parseInt(value) == 1) {
                                            counter++;
                                            JTextField temp = new JTextField(key);
                                            temp.setEditable(false);
                                            fgiComponents.add(temp);
                                        }
                                    }
                                    if (counter == 0) {
                                        JTextField empty = new JTextField(Utility.NOT_APPLICABLE);
                                        empty.setEditable(false);
                                        fgiComponents.add(empty);
                                    }
                                    JComponent[] fgiVec = new JComponent[fgiComponents.size()];
                                    for (int j = 0 ; j < fgiComponents.size(); j++) {
                                        fgiVec[j] = fgiComponents.get(j);
                                    }
                                    ScrollDialog dialog = new ScrollDialog(fgiVec,"3G - Encryption Algorithms");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            ciphAlgUMTSButton.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            intAlgUMTSButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    intAlgUMTSButton.setEnabled(false);
                                    ArrayList<JComponent> fgiComponents = new ArrayList<JComponent>();
                                    int counter = 0;
                                    for (String keyName : device.getUmtsCap().getIntegrAlg().keySet()) {
                                        String key = keyName.toString();
                                        String value = device.getUmtsCap().getIntegrAlg().get(key).toString();
                                        if (Integer.parseInt(value) == 1) {
                                            counter++;
                                            JTextField temp = new JTextField(key);
                                            temp.setEditable(false);
                                            fgiComponents.add(temp);
                                        }
                                    }
                                    if (counter == 0) {
                                        JTextField empty = new JTextField(Utility.NOT_APPLICABLE);
                                        empty.setEditable(false);
                                        fgiComponents.add(empty);
                                    }

                                    JComponent[] fgiVec = new JComponent[fgiComponents.size()];
                                    for (int j = 0 ; j < fgiComponents.size(); j++) {
                                        fgiVec[j] = fgiComponents.get(j);
                                    }
                                    ScrollDialog dialog = new ScrollDialog(fgiVec,"3G - Integrity Algorithms");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            intAlgUMTSButton.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            integrAlgLTEButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    integrAlgLTEButton.setEnabled(false);
                                    ArrayList<JComponent> intAlgLTEComponents = new ArrayList<JComponent>();
                                    int counter = 0;
                                    for (String keyName : device.getLteCap().getIntegrAlg().keySet()) {
                                        String key = keyName.toString();
                                        String value = device.getLteCap().getIntegrAlg().get(key).toString();
                                        if (Integer.parseInt(value) == 1) {
                                            counter++;
                                            JTextField temp = new JTextField(key);
                                            temp.setEditable(false);
                                            intAlgLTEComponents.add(temp);
                                        }
                                    }
                                    if (counter == 0) {
                                        JTextField empty = new JTextField(Utility.NOT_APPLICABLE);
                                        empty.setEditable(false);
                                        intAlgLTEComponents.add(empty);
                                    }
                                    JComponent[] integrAlgVect = new JComponent[intAlgLTEComponents.size()];
                                    for (int j = 0 ; j < intAlgLTEComponents.size(); j++) {
                                        integrAlgVect[j] = intAlgLTEComponents.get(j);
                                    }
                                    ScrollDialog dialog = new ScrollDialog(integrAlgVect,"4G - Integrity Algorithms");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            integrAlgLTEButton.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            encrAlgLTEButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    encrAlgLTEButton.setEnabled(false);
                                    ArrayList<JComponent> ciphAlgLTEComponents = new ArrayList<JComponent>();
                                    int counter = 0;
                                    for (String keyName : device.getLteCap().getEncrAlg().keySet()) {
                                        String key = keyName.toString();
                                        String value = device.getLteCap().getEncrAlg().get(key).toString();
                                        if (Integer.parseInt(value) == 1) {
                                            counter++;
                                            JTextField temp = new JTextField(key);
                                            temp.setEditable(false);
                                            ciphAlgLTEComponents.add(temp);
                                        }
                                    }
                                    if (counter == 0) {
                                        JTextField empty = new JTextField(Utility.NOT_APPLICABLE);
                                        empty.setEditable(false);
                                        ciphAlgLTEComponents.add(empty);
                                    }
                                    JComponent[] encrAlgVect = new JComponent[ciphAlgLTEComponents.size()];
                                    for (int j = 0 ; j < ciphAlgLTEComponents.size(); j++) {
                                        encrAlgVect[j] = ciphAlgLTEComponents.get(j);
                                    }
                                    ScrollDialog dialog = new ScrollDialog(encrAlgVect,"4G - Encryption Algorithms");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            encrAlgLTEButton.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            lteBandInfo.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    lteBandInfo.setEnabled(false);
                                    JTextField support1020TextField = new JTextField(String.valueOf(device.isCa1020Support()));
                                    support1020TextField.setEditable(false);
                                    JTextField support1310TextField = new JTextField(String.valueOf(device.isCa1310Support()));
                                    support1020TextField.setEditable(false);
                                    JTextField support10i0TextField = new JTextField(String.valueOf(device.isCa10i0Support()));
                                    support10i0TextField.setEditable(false);
                                    JTextField maxLayDL = new JTextField(String.valueOf(device.getMaximumSupportedLayersDL()));
                                    maxLayDL.setEditable(false);
                                    JTextField maxLayUL = new JTextField(String.valueOf(device.getMaximumSupportedLayersUL()));
                                    maxLayUL.setEditable(false);
                                    JTextField bandListTextView = new JTextField(GUIUtility.supportedLTEBandsUIFormatter(device.getLteCap().getSupportedBands()));
                                    bandListTextView.setEditable(false);
                                    JTextField bandListDLTextView = new JTextField(GUIUtility.supportedDLModulationBandsUIFormatter(device.getLteCap().getSupportedBands()));
                                    bandListDLTextView.setEditable(false);
                                    JTextField bandListULTextView = new JTextField(GUIUtility.supportedULModulationBandsUIFormatter(device.getLteCap().getSupportedBands()));
                                    bandListULTextView.setEditable(false);
                                    JTextField caULSupportTextField = new JTextField(String.valueOf(device.isCaULSupport()));
                                    caULSupportTextField.setEditable(false);
                                    JTextField addR11SupportTextField = new JTextField(String.valueOf(device.isAddR11Support()));
                                    addR11SupportTextField.setEditable(false);
                                    boolean frequencyRetrievalSupport = false;
                                    if (device.getRequestedBands() != null) {
                                        frequencyRetrievalSupport = true;
                                    }
                                    JTextField frequencyRetrievalSupportTextField = new JTextField(String.valueOf(frequencyRetrievalSupport));
                                    frequencyRetrievalSupportTextField.setEditable(false);
                                    JTextField frequencyRetrievalBandListTextField = new JTextField(GUIUtility.supportedBandsUIFormatter(device.getRequestedBands()));
                                    frequencyRetrievalBandListTextField.setEditable(false);
                                    JComponent[] componentsBand;
                                    if (device.getRequestedBands() != null) {
                                        componentsBand = new JComponent[]{
                                                new JLabel(GUIUtility.SUPPORT_1020_LABEL), support1020TextField,
                                                new JLabel(GUIUtility.SUPPORT_10I0_LABEL), support10i0TextField,
                                                new JLabel(GUIUtility.SUPPORT_1310_LABEL), support1310TextField,
                                                new JLabel(GUIUtility.CA_UL_SUPPORT_LABEL), caULSupportTextField,
                                                new JLabel(GUIUtility.MAXIMUM_LAYERS_DL_LABEL), maxLayDL,
                                                new JLabel(GUIUtility.MAXIMUM_LAYERS_UL_LABEL), maxLayUL,
                                                new JLabel(GUIUtility.SUPPORTED_BANDS_LABEL), bandListTextView,
                                                new JLabel(GUIUtility.SUPPORTED_BANDS_MODULATION_DL_LABEL), bandListDLTextView,
                                                new JLabel(GUIUtility.SUPPORTED_BANDS_MODULATION_UL_LABEL), bandListULTextView,
                                                new JLabel(GUIUtility.FREQUENCY_RETRIEVAL_SUPPORT_LABEL), frequencyRetrievalSupportTextField,
                                                new JLabel(GUIUtility.FREQUENCY_BAND_LIST_LABEL), frequencyRetrievalBandListTextField,
                                                new JLabel(GUIUtility.ADD_R11_SUPPORT_LABEL),addR11SupportTextField
                                        };
                                    } else {
                                        componentsBand = new JComponent[]{
                                                new JLabel(GUIUtility.SUPPORT_1020_LABEL), support1020TextField,
                                                new JLabel(GUIUtility.SUPPORT_10I0_LABEL), support10i0TextField,
                                                new JLabel(GUIUtility.SUPPORT_1310_LABEL), support1310TextField,
                                                new JLabel(GUIUtility.CA_UL_SUPPORT_LABEL), caULSupportTextField,
                                                new JLabel(GUIUtility.MAXIMUM_LAYERS_DL_LABEL), maxLayDL,
                                                new JLabel(GUIUtility.MAXIMUM_LAYERS_UL_LABEL), maxLayUL,
                                                new JLabel(GUIUtility.SUPPORTED_BANDS_LABEL), bandListTextView,
                                                new JLabel(GUIUtility.SUPPORTED_BANDS_MODULATION_DL_LABEL), bandListDLTextView,
                                                new JLabel(GUIUtility.SUPPORTED_BANDS_MODULATION_UL_LABEL), bandListULTextView,
                                                new JLabel(GUIUtility.FREQUENCY_RETRIEVAL_SUPPORT_LABEL), frequencyRetrievalSupportTextField
                                        };
                                    }

                                    ScrollDialog dialog = new ScrollDialog(componentsBand,"4G - Band Info");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            lteBandInfo.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            v1020CombButtonDL.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    v1020CombButtonDL.setEnabled(false);
                                    BandCombinationDialog dialog = new BandCombinationDialog(device, GUIUtility.DL_1020);
                                    dialog.setModal(false); // this says not to block background components
                                    dialog.setVisible(true);
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            v1020CombButtonDL.setEnabled(true);
                                        }
                                    });


                                }
                            });
                            v1020CombButtonUL.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    v1020CombButtonUL.setEnabled(false);
                                    BandCombinationDialog dialog = new BandCombinationDialog(device, GUIUtility.UL_1020);
                                    dialog.setModal(false); // this says not to block background components
                                    dialog.setVisible(true);
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            v1020CombButtonUL.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            v1310CombButtonDL.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    v1310CombButtonDL.setEnabled(false);
                                    BandCombinationDialog dialog = new BandCombinationDialog(device, GUIUtility.DL_1310);
                                    dialog.setModal(false); // this says not to block background components
                                    dialog.setVisible(true);
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            v1310CombButtonDL.setEnabled(true);
                                        }
                                    });


                                }
                            });
                            v1310CombButtonUL.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    v1310CombButtonUL.setEnabled(false);
                                    BandCombinationDialog dialog = new BandCombinationDialog(device, GUIUtility.UL_1310);
                                    dialog.setModal(false); // this says not to block background components
                                    dialog.setVisible(true);
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            v1310CombButtonUL.setEnabled(true);
                                        }
                                    });
                                }
                            });

                            v10i0CombButtonDL.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    v10i0CombButtonDL.setEnabled(false);
                                    BandCombinationDialog dialog = new BandCombinationDialog(device, GUIUtility.DL_10i0);
                                    dialog.setModal(false); // this says not to block background components
                                    dialog.setVisible(true);
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            v10i0CombButtonDL.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            v10i0CombButtonUL.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    v10i0CombButtonUL.setEnabled(false);
                                    BandCombinationDialog dialog = new BandCombinationDialog(device, GUIUtility.UL_10i0);
                                    dialog.setModal(false); // this says not to block background components
                                    dialog.setVisible(true);
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            v10i0CombButtonUL.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            enNRCombButtonDL.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                	enNRCombButtonDL.setEnabled(false);
                                    BandCombinationDialog dialog = new BandCombinationDialog(device, GUIUtility.DL_ENNR);
                                    dialog.setModal(false); // this says not to block background components
                                    dialog.setVisible(true);
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            enNRCombButtonDL.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            enNRCombButtonUL.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                	enNRCombButtonUL.setEnabled(false);
                                    BandCombinationDialog dialog = new BandCombinationDialog(device, GUIUtility.UL_ENNR);
                                    dialog.setModal(false); // this says not to block background components
                                    dialog.setVisible(true);
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            enNRCombButtonUL.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            generalButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    generalButton.setEnabled(false);
                                    String imeiSV = "";
                                    if (device.getImeiSV().equals(Utility.NOT_SET)) {
                                        imeiSV = "-";
                                    } else {
                                        imeiSV = device.getImeiSV();
                                    }
                                    JTextField svnTextField = new JTextField(imeiSV);
                                    svnTextField.setEditable(false);
                                    JTextField gsmSupportTextField = new JTextField(String.valueOf(device.isGsmSupport()));
                                    gsmSupportTextField.setEditable(false);
                                    JTextField umtsSupportTextField = new JTextField(String.valueOf(device.isUmtsSupport()));
                                    umtsSupportTextField.setEditable(false);
                                    JTextField lteSupportTextField = new JTextField(String.valueOf(true));
                                    lteSupportTextField.setEditable(false);
                                    final JComponent[] basicInfosSingleCat = new JComponent[]{
                                            new JLabel(GUIUtility.LTE_SUPPORT_LABEL), lteSupportTextField,
                                            new JLabel(GUIUtility.UMTS_SUPPORT_LABEL), umtsSupportTextField,
                                            new JLabel(GUIUtility.GSM_SUPPORT_LABEL), gsmSupportTextField,
                                            new JLabel(GUIUtility.SVN_LABEL), svnTextField
                                    };

                                    ScrollDialog dialog = new ScrollDialog(basicInfosSingleCat,"General Info");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            generalButton.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            encrAlgGSMButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    encrAlgGSMButton.setEnabled(false);
                                    ArrayList<JComponent> ciphAlgGSM = new ArrayList<JComponent>();
                                    int counter = 0;
                                    for (String keyName : device.getGsmCap().getEncrAlg().keySet()) {
                                        String key = keyName.toString();
                                        String value = device.getGsmCap().getEncrAlg().get(key).toString();
                                        if (Integer.parseInt(value) == 1) {
                                            counter++;
                                            JTextField temp = new JTextField(key);
                                            temp.setEditable(false);
                                            ciphAlgGSM.add(temp);
                                        }
                                    }
                                    if (counter == 0) {
                                        JTextField empty = new JTextField(Utility.NOT_APPLICABLE);
                                        empty.setEditable(false);
                                        ciphAlgGSM.add(empty);
                                    }
                                    JComponent[] encrAlgVect = new JComponent[ciphAlgGSM.size()];
                                    for (int j = 0 ; j < ciphAlgGSM.size(); j++) {
                                        encrAlgVect[j] = ciphAlgGSM.get(j);
                                    }
                                    ScrollDialog dialog = new ScrollDialog(encrAlgVect,"2G - Encryption Algorithms");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            encrAlgGSMButton.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            gsmBandInfoButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    gsmBandInfoButton.setEnabled(false);

                                    JTextField bandListTextView = new JTextField(GUIUtility.supportedGSMBandsUIFormatter(device.getGsmCap().getSupportedBands()));
                                    bandListTextView.setEditable(false);
                                    final JComponent[] componentsBand = new JComponent[]{
                                            new JLabel(GUIUtility.SUPPORTED_BANDS_LABEL), bandListTextView,
                                    };

                                    ScrollDialog dialog = new ScrollDialog(componentsBand,"2G - Band Info");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            gsmBandInfoButton.setEnabled(true);
                                        }
                                    });
                                }

                            });
                            umtsBandInfoButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    umtsBandInfoButton.setEnabled(false);

                                    JTextField bandListTextView = new JTextField(GUIUtility.supportedUMTSBandsUIFormatter(device.getUmtsCap().getSupportedBands()));
                                    bandListTextView.setEditable(false);
                                    final JComponent[] componentsBand = new JComponent[]{
                                            new JLabel(GUIUtility.SUPPORTED_UMTS_FDD_BANDS_LABEL), bandListTextView,
                                    };
                                    ScrollDialog dialog = new ScrollDialog(componentsBand,"3G - Band Info");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            umtsBandInfoButton.setEnabled(true);
                                        }
                                    });
                                }
                            });
                            umtsBasicIfoButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    umtsBasicIfoButton.setEnabled(false);
                                    JTextField umtsFDDRadioAccCapTextField = new JTextField(String.valueOf(device.getUmtsCap().isFddRadioAccessTechnCap()));
                                    umtsFDDRadioAccCapTextField.setEditable(false);
                                    JTextField umts384MCPSTDDRadioAccCapTextField = new JTextField(String.valueOf(device.getUmtsCap().isMcps384TDDRatCap()));
                                    umts384MCPSTDDRadioAccCapTextField.setEditable(false);
                                    JTextField umts128MCPSTDDRadioAccCapTextField = new JTextField(String.valueOf(device.getUmtsCap().isMcps128TDDRatCap()));
                                    umts128MCPSTDDRadioAccCapTextField.setEditable(false);
                                    JTextField cdma200SupportTextField = new JTextField(String.valueOf(device.getUmtsCap().isCdma2000Support()));
                                    cdma200SupportTextField.setEditable(false);
                                    final JComponent[] umtsBasicInfo = new JComponent[]{
                                            new JLabel(GUIUtility.UMTS_FDD_RADIO_ACC_CAP_LABEL), umtsFDDRadioAccCapTextField,
                                            new JLabel(GUIUtility.UMTS_384_MCPS_TDD_RADIO_ACC_CAP_LABEL),umts384MCPSTDDRadioAccCapTextField,
                                            new JLabel(GUIUtility.UMTS_128_MCPS_TDD_RADIO_ACC_CAP_LABEL), umts128MCPSTDDRadioAccCapTextField,
                                            new JLabel(GUIUtility.CDMA_2000_LABEL), cdma200SupportTextField
                                    };
                                    ScrollDialog dialog = new ScrollDialog(umtsBasicInfo,"3G - Basic Info");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            umtsBasicIfoButton.setEnabled(true);
                                        }
                                    });

                                }
                            });
                            gsmBasicInfoButton.addActionListener(new ActionListener() {
                                @Override
                                public void actionPerformed(ActionEvent e) {
                                    gsmBasicInfoButton.setEnabled(false);
                                    JTextField ucs2SupportTextField = new JTextField(device.getGsmCap().getUcs2Support());
                                    ucs2SupportTextField.setEditable(false);
                                    JTextField ssScreeningIndicatorTextField = new JTextField(String.valueOf(device.getGsmCap().getSsScreeningIndicator()));
                                    ssScreeningIndicatorTextField.setEditable(false);
                                    JTextField pfcFeatureTextField = new JTextField(GUIUtility.pfcFeaturesValueFormatter(device.getGsmCap().isPfcFeatureSupport()));
                                    pfcFeatureTextField.setEditable(false);
                                    JTextField lcsTextField = new JTextField(String.valueOf(device.getGsmCap().isLcsSupport()));
                                    lcsTextField.setEditable(false);
                                    JTextField psIuTextField = new JTextField(String.valueOf(device.getGsmCap().isInterRatPSHOtoUTRANIu()));
                                    psIuTextField.setEditable(false);
                                    JTextField psS1TextField = new JTextField(String.valueOf(device.getGsmCap().isInterRatPSHOtoUTRANS1()));
                                    psS1TextField.setEditable(false);
                                    JTextField emmCombinedProcedureTextField = new JTextField(String.valueOf(device.getGsmCap().isEmmProceduresSupport()));
                                    emmCombinedProcedureTextField.setEditable(false);
                                    JTextField isrSupportTextField = new JTextField(String.valueOf(device.getGsmCap().isIsrSupport()));
                                    isrSupportTextField.setEditable(false);
                                    JTextField epcCapabilityTextField = new JTextField(String.valueOf(device.getGsmCap().isEpcSupport()));
                                    epcCapabilityTextField.setEditable(false);
                                    JTextField nfCapabilityTextField = new JTextField(String.valueOf(device.getGsmCap().isNfcSupport()));
                                    nfCapabilityTextField.setEditable(false);
                                    JTextField esIndTextField = new JTextField(String.valueOf(device.getGsmCap().isEsIndSupport()));
                                    esIndTextField.setEditable(false);
                                    JTextField rfPowCap1TextField = new JTextField(GUIUtility.rfPowerCapabilityValuesFormatter(device.getGsmCap().getRfPowerCapability1()));
                                    rfPowCap1TextField.setEditable(false);
                                    JTextField rfPowCap2TextField = new JTextField(GUIUtility.rfPowerCapabilityValuesFormatter(device.getGsmCap().getRfPowerCapability2()));
                                    rfPowCap2TextField.setEditable(false);
                                    JTextField psSupportTextField = new JTextField(String.valueOf(device.getGsmCap().isPsCapability()));
                                    psSupportTextField.setEditable(false);
                                    JTextField gprsMultislotTextField = new JTextField(GUIUtility.fromZeroToNAValuesFormatter(device.getGsmCap().getGprsMultiSlotClass()));
                                    gprsMultislotTextField.setEditable(false);
                                    JTextField egprsMultislotTextField = new JTextField(GUIUtility.fromZeroToNAValuesFormatter(device.getGsmCap().getEgprsMultiSlotClass()));
                                    egprsMultislotTextField.setEditable(false);
                                    JTextField singleSlotDTMTextField = new JTextField(String.valueOf(device.getGsmCap().isSingleSlotSupport()));
                                    singleSlotDTMTextField.setEditable(false);
                                    JTextField psk8RFPowCap1TextField = new JTextField(device.getGsmCap().getPsk8RFPowerCapability1());
                                    psk8RFPowCap1TextField.setEditable(false);
                                    JTextField psk8RFPowCap2TextField = new JTextField(device.getGsmCap().getPsk8RFPowerCapability2());
                                    psk8RFPowCap2TextField.setEditable(false);
                                    JTextField gmskMultislotPowerProfTextField = new JTextField(String.valueOf(device.getGsmCap().getGmskMultislotPowerProfile()));
                                    gmskMultislotPowerProfTextField.setEditable(false);
                                    JTextField gmsk8pskMultislotPowerProfTextField = new JTextField(String.valueOf(device.getGsmCap().getPsk8MultislotPowerProfile()));
                                    gmsk8pskMultislotPowerProfTextField.setEditable(false);
                                    JTextField downlinkAdvRecTextField = new JTextField(device.getGsmCap().getAdvancedReceiverPerformance());
                                    downlinkAdvRecTextField.setEditable(false);
                                    JTextField dtmSupportTextField = new JTextField(String.valueOf(device.getGsmCap().isDtmSupport()));
                                    dtmSupportTextField.setEditable(false);
                                    JTextField prioBasResSuppTextField = new JTextField(String.valueOf(device.getGsmCap().isPriorityBasedReselectionSupport()));
                                    prioBasResSuppTextField.setEditable(false);

                                    JPanel panel = new JPanel();
                                    panel.setLayout(new GridLayout(0,1));
                                    panel.add(new JLabel(GUIUtility.UCS2_SUPPORT_LABEL));
                                    panel.add(ucs2SupportTextField);
                                    panel.add(new JLabel(GUIUtility.SS_SCREENING_INDICATOR_LABEL));
                                    panel.add(ssScreeningIndicatorTextField);
                                    panel.add(new JLabel(GUIUtility.PFC_FEATURE_LABEL));
                                    panel.add(pfcFeatureTextField);
                                    panel.add(new JLabel(GUIUtility.LCS_VA_CAPABILITY_LABEL));
                                    panel.add(lcsTextField);
                                    panel.add(new JLabel(GUIUtility.PS_IU_HANDOVER_LABEL));
                                    panel.add(psIuTextField);
                                    panel.add(new JLabel(GUIUtility.PS_S1_HANDOVER_LABEL));
                                    panel.add(psS1TextField);
                                    panel.add(new JLabel(GUIUtility.EMM_COMBINED_PROCEDURE_LABEL));
                                    panel.add(emmCombinedProcedureTextField);
                                    panel.add(new JLabel(GUIUtility.ISR_SUPPORT_LABEL));
                                    panel.add(isrSupportTextField);
                                    panel.add(new JLabel(GUIUtility.EPC_CAPABILITY_SUPPORT));
                                    panel.add(epcCapabilityTextField);
                                    panel.add(new JLabel(GUIUtility.NF_CAPABILITY_LABEL));
                                    panel.add(nfCapabilityTextField);
                                    panel.add(new JLabel(GUIUtility.ES_IND_LABEL));
                                    panel.add(esIndTextField);
                                    panel.add(new JLabel(GUIUtility.RF_POW_CAP1_LABEL));
                                    panel.add(rfPowCap1TextField);
                                    panel.add(new JLabel(GUIUtility.RF_POW_CAP2_LABEL));
                                    panel.add(rfPowCap2TextField);
                                    panel.add(new JLabel(GUIUtility.PS_SUPPORT_LABEL));
                                    panel.add(psSupportTextField);
                                    panel.add(new JLabel(GUIUtility.GPRS_MULTISLOT_CAPABILITY_LABEL));
                                    panel.add(gprsMultislotTextField);
                                    panel.add(new JLabel(GUIUtility.EGPRS_MULTISLOT_CAPABILITY_LABEL));
                                    panel.add(egprsMultislotTextField);
                                    panel.add(new JLabel(GUIUtility.SINGLE_SLOT_DTM_LABEL));
                                    panel.add(singleSlotDTMTextField);
                                    panel.add(new JLabel(GUIUtility.PSK8_RF_POW1_LABEL));
                                    panel.add(psk8RFPowCap1TextField);
                                    panel.add(new JLabel(GUIUtility.PSK8_RF_POW2_LABEL));
                                    panel.add(psk8RFPowCap2TextField);
                                    panel.add(new JLabel(GUIUtility.DOWNLINK_ADVANCED_RX_PERFORMANCE_LABEL));
                                    panel.add(downlinkAdvRecTextField);
                                    panel.add(new JLabel(GUIUtility.DTM_SUPPORT_LABEL));
                                    panel.add(dtmSupportTextField);
                                    panel.add(new JLabel(GUIUtility.PRIORITY_BASED_RESELECTION_SUPPORT_LABEL));
                                    panel.add(prioBasResSuppTextField);
                                    panel.validate();
                                    panel.add(new JLabel(GUIUtility.GMSK_MULTISLOT_POWER_PROFILE_LABEL));
                                    panel.add(gmskMultislotPowerProfTextField);
                                    panel.add(new JLabel(GUIUtility.PSK8_MULTISLOT_POWER_PROFILE_LABEL));
                                    panel.add(gmsk8pskMultislotPowerProfTextField);

                                    ScrollDialog dialog = new ScrollDialog(panel,"2G - Basic Info");
                                    dialog.addWindowListener(new WindowAdapter() {
                                        @Override
                                        public void windowClosing(WindowEvent e) {
                                            super.windowClosing(e);
                                            gsmBasicInfoButton.setEnabled(true);
                                        }
                                    });
                                }
                            });
                        }
                    }// else packet validity
                }// else pcapfilename==null
            }// listener
        });

        setBackground(Color.decode("#79c143"));
    }

    /**
     * Private static method to clear view of rrc panel from button action listeners
     * @param panel rrc panel
     */
    private void clearView(JPanel panel){
        // Reset view
        for( JButton currentComponent: rrcButtons ) {
            for( ActionListener al : currentComponent.getActionListeners() ) {
                currentComponent.removeActionListener( al );
            }
        }
        panel.removeAll();
        panel.updateUI();
    }

}
