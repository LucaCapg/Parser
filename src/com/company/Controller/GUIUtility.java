package com.company.Controller;

import com.company.Model.Band;
import com.company.Model.DUT;
import com.company.Model.Mobile;
import com.company.Model.gsm.GSMBand;
import com.company.Model.lte.LTEBand;
import com.company.Model.umts.UMTSBand;
import com.company.Utility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Utility class storing all values related to GUI such as
 * Strings, button names, labels, etc.
 */
public class GUIUtility {
    protected final static String PROGRAM_NAME = "Capability Parser";

    // TABS TITLES
    protected final static String RRC_PARSER_TAB = "2G/3G/4G/5G Parser";
    protected final static String VOLTE_PARSER_TAB = "VoLTE Parser";
    protected final static String CATM_PARSER_TAB = "eMTC Parser";
    protected final static String NBIOT_PARSER_BTN = "NBIoT Parser";

    // Hints
    protected final static String RRC_PARSER_HINT = "Upload an RRC pcap file to be parsed";
    protected final static String VOLTE_PARSER_HINT = "Upload a SIP pcap file to be parsed";
    protected final static String CATM_PARSER_HINT = "Upload a RRC pcap file containing eMTC features to be parsed";
    protected final static String NBIOT_PARSER_HINT = "Upload a RRC pcap file containing NBIoT features to be parsed";

    // TABLE TITLES
    public final static String BAND_COMBINATIONS_V1020_DL_TITLE = "Band Combinations v1020 DL";
    public final static String BAND_COMBINATIONS_V1020_UL_TITLE = "Band Combinations v1020 UL";
    public final static String BAND_COMBINATIONS_V10I0_DL_TITLE = "Band Combinations v10I0 DL";
    public final static String BAND_COMBINATIONS_V10I0_UL_TITLE = "Band Combinations v10I0 UL";
    public final static String BAND_COMBINATIONS_V1310_DL_TITLE = "Band Combinations v1020 DL";
    public final static String BAND_COMBINATIONS_V1310_UL_TITLE = "Band Combinations v1020 UL";

    // Support codes
    final public static int DL_10i0 = 42;
    final public static int UL_10i0 = 43;
    final public static int DL_1020 = 44;
    final public static int UL_1020 = 45;
    final public static int DL_ENNR = 46;
    final public static int UL_ENNR = 47;
    final public static int DL_1310 = 48;
    final public static int UL_1310 = 49;

    // COMMON BUTTONS
    public final static String FILE_NAME_BTN = "File Name";
    public final static String BROWSE_BTN = "Browse";
    public final static String PARSE_BTN = "Parse";
    public final static String BAND_INFO_LTE_BTN = "4G - Band Info";
    public final static String WRITE_TO_FILE_BTN = "Write to File";
    public final static String EXPORT_ENDC_BTN = "Export EN-DC";
    

    // LABELS RRC (BUTTONS)
    public final static String GSM_CODECS_BTN = "2G - Codecs";
    public final static String UMTS_CODECS_BTN = "3G - Codecs";
    public final static String LTE_BASIC_INFO_BTN = "4G - Basic Info";
    public final static String BAND_COMB_DL_1020_BTN = "4G - Band Comb. v1020-DL";
    public final static String BAND_COMB_UL_1020_BTN = "4G - Band Comb. v1020-UL";
    public final static String BAND_COMB_DL_10I0_BTN = "4G - Band Comb. v10i0-DL";
    public final static String BAND_COMB_UL_10I0_BTN = "4G - Band Comb. v10i0-UL";
    public final static String BAND_COMB_DL_1310_BTN = "4G - Band Comb. v1310-DL";
    public final static String BAND_COMB_UL_1310_BTN = "4G - Band Comb. v1310-UL";
    public final static String BAND_COMB_DL_EN_NR_BTN = "EN-DC Band Combination - DL";
    public final static String BAND_COMB_UL_EN_NR_BTN = "EN-DC Band Combination - UL";
    public final static String GENERAL_INFO_BTN = "General Info";
    public final static String AS_RELEASE_LABEL = "accessStratumRelease";
    public final static String EIT_LABEL = "EIT";
    public final static String VOICE_DOMAIN_LABEL = "Voice Domain Preference";
    public final static String ADD_UPDATE_TYPE_LABEL = "Additional Update Type";
    public final static String SVN_LABEL = "SVN";
    public final static String LTE_SUPPORT_LABEL = "4G Support";
    public final static String GSM_SUPPORT_LABEL = "2G Support";
    public final static String UMTS_SUPPORT_LABEL = "3G Support";
    public final static String ATTACH_TYPE_LABEL = "Attach Type";
    public final static String SRVCC_GERAN_UTRAN_LABEL = "SRVCC to GERAN/UTRAN support";
    public final static String CATEGORY_LABEL = "Category";
    public final static String CATEGORY_DL_LABEL = "Category-DL";
    public final static String CATEGORY_UL_LABEL = "Category-UL";
    public final static String FGI8_LABEL = "F.G.I. rel8";
    public final static String FGI9_ADDR9_LABEL = "F.G.I. Add R9";
    public final static String FGI9_ADDR9_TDD_LABEL = "F.G.I. AddR9 TDD";
    public final static String FGI9_ADDR9_FDD_LABEL = "F.G.I. AddR9 FDD";
    public final static String FGI9_FDD_LABEL = "F.G.I.9 FDD";
    public final static String FGI9_TDD_LABEL = "F.G.I.9 TDD";
    public final static String FGI10_LABEL = "F.G.I. 10";
    public final static String FGI10_FDD_LABEL = "F.G.I. 10 FDD";
    public final static String FGI10_TDD_LABEL = "F.G.I. 10 TDD";
    public final static String IMSI_LABEL = "IMSI:";
    public final static String UE_USAGE_SETTINGS_LABEL = "UE Usage Settings:";
    public final static String SUPPORTED_BANDS_LABEL = "Supported Bands";
    public final static String SUPPORTED_BANDS_MODULATION_DL_LABEL = "256 QAM DL support";
    public final static String SUPPORTED_BANDS_MODULATION_UL_LABEL = "64 QAM UL Support";
    public final static String SUPPORT_1020_LABEL = "SupportBandCombination-v1020";
    public final static String SUPPORT_10I0_LABEL = "SupportBandCombination-v10i0";
    public final static String SUPPORT_1310_LABEL = "SupportBandCombination-v1310";
    public final static String MAXIMUM_LAYERS_DL_LABEL = "Maximum number of supported layers DL";
    public final static String MAXIMUM_LAYERS_UL_LABEL = "Maximum number of supported layers UL";
    public final static String GSM_BASIC_INFO_BTN = "2G - Basic Info";
    public final static String UMTS_BASIC_INFO_BTN = "3G - Basic Info";
    public final static String GSM_CIPH_ALG_BTN = "2G - Encryption Algorithms";
    public final static String GSM_BAND_INFO_BTN = "2G - Band Info";
    public final static String UMTS_BAND_INFO_BTN = "3G - Band Info";
    public final static String SUPPORTED_ROHC_PROFILES_LABEL = "Supported ROHC Profiles";
    public final static String SUPPORTED_UMTS_FDD_BANDS_LABEL = "Supported Band UTRA-FDD";
    public final static String FREQUENCY_RETRIEVAL_SUPPORT_LABEL = "Frequency Retrieval Support";
    public final static String FREQUENCY_BAND_LIST_LABEL = "Frequency Retrieval Band List";
    public final static String CA_UL_SUPPORT_LABEL = "CA UL Support";
    public final static String UE_TX_ANTENNA_SEL_SUPP_LABEL = "ue-TxAntennaSelection support";
    public final static String UE_SPECIFIC_REF_SIGS_SUPP_LABEL = "ue-SpecificRefSigs support";
    public final static String INTER_RAT_PS_HO_GERAN_LABEL = "interRAT-PS-HO-ToGERAN support";
    public final static String E_REDIRECTION_GERAN_LABEL = "e-RedirectionGERAN-r9 support";
    public final static String E_REDIRECTION_UTRAN_LABEL = "e-RedirectionUTRA-r9 support";
    public final static String SON_PARAMETER_RACH_REPORT_LABEL = "rach-Report-r9 support";
    public final static String ADD_R11_SUPPORT_LABEL = "Add R11 Support";

    // LABELS UMTS
    public final static String UMTS_FDD_RADIO_ACC_CAP_LABEL = "UMTS FDD Radio Access Technology Capability";
    public final static String UMTS_384_MCPS_TDD_RADIO_ACC_CAP_LABEL = "UMTS 3.84 Mcps TDD Radio Access Technology Capability support";
    public final static String CDMA_2000_LABEL = "CDMA 2000 Radio Access Technology Capability support";
    public final static String UMTS_128_MCPS_TDD_RADIO_ACC_CAP_LABEL = "UMTS 1.28 Mcps TDD Radio Access Technology Capability support";
    public final static String DECODED_PFC_0 = "MS does not support BSS packet flow procedures";
    public final static String DECODED_PFC_1 = "MS does support BSS packet flow procedures";

    // LABELS GSM
    public final static String UCS2_SUPPORT_LABEL = "UCS2 Support";
    public final static String SS_SCREENING_INDICATOR_LABEL = "SS Screening Indicator";
    public final static String PFC_FEATURE_LABEL = "PFC Feature";
    public final static String LCS_VA_CAPABILITY_LABEL = "LCS VA capability support";
    public final static String PS_IU_HANDOVER_LABEL = "PS inter-RAT HO from GERAN to UTRAN Iu mode capability support";
    public final static String PS_S1_HANDOVER_LABEL = "PS inter-RAT HO from GERAN to E-UTRAN S1 mode capability support ";
    public final static String EMM_COMBINED_PROCEDURE_LABEL = "EMM Combined procedures capability support";
    public final static String ISR_SUPPORT_LABEL = "ISR support";
    public final static String EPC_CAPABILITY_SUPPORT = "EPC capability support";
    public final static String NF_CAPABILITY_LABEL = "NF Capability support";
    public final static String ES_IND_LABEL = "ES IND support";
    public final static String RF_POW_CAP1_LABEL = "RF Power Capability 1";
    public final static String RF_POW_CAP2_LABEL = "RF Power Capability 2";
    public final static String PS_SUPPORT_LABEL = "PS capability support";
    public final static String GPRS_MULTISLOT_CAPABILITY_LABEL = "DTM GPRS Multi Slot Class ";
    public final static String EGPRS_MULTISLOT_CAPABILITY_LABEL = "DTM EGPRS Multi Slot Class";
    public final static String SINGLE_SLOT_DTM_LABEL = "Single slot DTM Support";
    public final static String PSK8_RF_POW1_LABEL = "8-PSK RF Power Capability 1";
    public final static String PSK8_RF_POW2_LABEL = "8-PSK RF Power Capability 2";
    public final static String GMSK_MULTISLOT_POWER_PROFILE_LABEL = "GMSK Multislot Power Profile";
    public final static String PSK8_MULTISLOT_POWER_PROFILE_LABEL = "8-PSK Multislot Power Profile";
    public final static String DOWNLINK_ADVANCED_RX_PERFORMANCE_LABEL = "Downlink Advanced Receiver Performance Support";
    public final static String DTM_SUPPORT_LABEL = "DTM Enhancements Capability support";
    public final static String PRIORITY_BASED_RESELECTION_SUPPORT_LABEL = "Priority-based reselection support";

    //LABELS VOLTE
    public final static String EARLY_MEDIA_SUPPORT_LABEL = "P-Early-Media";
    public final static String USER_AGENT_LABEL = "User-Agent";

    // BUTTONS VoLTE
    final public static String BASIC_INFO_VOLTE_BTN = "VoLTE Basic Info";
    final public static String SECURITY_CLIENT_BTN = "Security Client";
    final public static String VOLTE_CODECS_BTN = "VoLTE Codecs";
    final public static String SRVCC_BTN = "Supported SRVCCs";

    // LABELS CATM
    public final static String TYPE_OF_IDENTITY_LABEL = "Type of Identity";
    public final static String PDN_TYPE_LABEL = "PDN Type";
    public final static String CIOT_LABEL = "CIoT Network Behaviour";
    public final static String CE_MODE_A_LABEL = "CeModeA";
    public final static String CE_MODE_B_LABEL = "CeModeB";
    public final static String EDRX_LABEL = "eDRX";
    public final static String PAGING_TIME_WINDOW_LABEL = "Paging Time Windows";
    public final static String T3324_LABEL = "T3324";
    public final static String T3412_LABEL = "T3412";
    public final static String EMM_WO_PDN_CAP_LABEL = "EMM Registered w/o PDN Support";

    // BUTTONS CATM
    public final static String BAND_INFO_BTN = "4G - Band Info";
    public final static String BASIC_INFO_CATM_BTN = "Basic Info CatM";
    public final static String CATM_FEATURES_BTN = "CatM Features";
    public final static String FGI_BTN = "4G - FGI";
    public final static String UMTS_CIPHERING_ALGORITHMS_BTN = "3G - Encryption Algorithms";
    public final static String UMTS_INTEGRITY_ALGORITHMS_BTN = "3G - Integrity Algortihms";
    public final static String CIPHERING_ALGORITHMS_LTE_BTN = "4G - Encryption Algorithms";
    public final static String INTEGRITY_ALGORITHMS_LTE_BTN = "4G - Integrity Algorithms";
    
    //LABELS NBIOT
    public final static String MULTICARRIER_LABEL = "Multicarrier Support";
    public final static String MULTITONE_LABEL = "Multitone Support";
    public final static String EPS_ENCR_LABEL = "EPS Encryption Algorithms";
    public final static String EPS_INTEGR_LABEL = "EPS Integrity Algorithms";

    //BUTTONS NBIOT
    public final static String BASIC_INFO_NBIOT_BTN = "Basic Info NBIoT";
    public final static String NBIOT_FEATURES_BTN = "NBIoT features";

    /**
     * Method to prepare data to be formatted from table
     * @param device the instance of DUT class to be used
     * @param purpose the purpose of the data formatting (1020,10i0,DL,UL)
     * @return the formatted data
     */
    public static Object[][] tableDataFormatter (Mobile device, int purpose) {
        Object[][] data = null;
        switch (purpose) {
            case GUIUtility.DL_1020:
                data = new Object[device.getCaBandsCombinations().size()][19];
                for (int i = 0 ; i < device.getCaBandsCombinations().size(); i++) {
                    int currentCCNumber = device.getCaBandsCombinations().get(i).getDLSingleCombination().size();
                    int ccNavigator = 0;
                    data[i][0] = String.valueOf(i);
                    data[i][2] = Utility.bcsParser(device.getCaBandsCombinations().get(i).getSupportedBCS());
                    data[i][3] = String.valueOf(device.getCaBandsCombinations().get(i).getCcQuantityDL());
                    String eutraCaConfiguration = "CA_";
                    for (int j = 3 ; j < 3 + currentCCNumber*3 ; j = j + 3) {
                        data[i][j+1] = device.getCaBandsCombinations().get(i).getDLSingleCombination().get(ccNavigator).getBandID();
                        eutraCaConfiguration = eutraCaConfiguration.concat(String.valueOf(device.getCaBandsCombinations()
                                .get(i).getDLSingleCombination().get(ccNavigator).getBandID()));
                        eutraCaConfiguration = eutraCaConfiguration.concat(device.getCaBandsCombinations()
                                .get(i).getDLSingleCombination().get(ccNavigator).getComponentCarrierClass());
                        data[i][j+2] = device.getCaBandsCombinations().get(i).getDLSingleCombination().get(ccNavigator).getComponentCarrierClass();
                        int tempLayers = 0;
                        tempLayers = device.getCaBandsCombinations().get(i).getDLSingleCombination().get(ccNavigator).getSupportedLayers();
                        if (tempLayers == 2) {
                            eutraCaConfiguration = eutraCaConfiguration.concat("(2x2)");
                        } else if (tempLayers == 4){
                            eutraCaConfiguration = eutraCaConfiguration.concat("(4x4)");
                        } else if (tempLayers == 1){
                            // Do not concat anything
                        }
                        eutraCaConfiguration = eutraCaConfiguration.concat("-");
                        data[i][j+3] = tempLayers;
                        ccNavigator++;
                    }
                    eutraCaConfiguration = eutraCaConfiguration.substring(0,eutraCaConfiguration.length()-1);
                    data[i][1] = eutraCaConfiguration;
                    ccNavigator = 0;
                }
                break;
            case GUIUtility.UL_1020:
                data = new Object[device.getCaBandsCombinations().size()][18];
                for (int i = 0 ; i < device.getCaBandsCombinations().size(); i++) {
                    int currentCCNumber = device.getCaBandsCombinations().get(i).getULSingleCombination().size();
                    int ccNavigator = 0;
                    data[i][0] = String.valueOf(i);
                    data[i][2] = String.valueOf(device.getCaBandsCombinations().get(i).getCcQuantityUL());
                    String eutraCaConfiguration = "CA_";
                    for (int j = 2 ; j < 2 + currentCCNumber*3 ; j = j + 3) {
                        eutraCaConfiguration = eutraCaConfiguration.concat(String.valueOf(device.getCaBandsCombinations()
                                .get(i).getULSingleCombination().get(ccNavigator).getBandID()));
                        eutraCaConfiguration = eutraCaConfiguration.concat(device.getCaBandsCombinations()
                                .get(i).getULSingleCombination().get(ccNavigator).getComponentCarrierClass());
                        data[i][j+1] = device.getCaBandsCombinations().get(i).getULSingleCombination().get(ccNavigator).getBandID();
                        data[i][j+2] = device.getCaBandsCombinations().get(i).getULSingleCombination().get(ccNavigator).getComponentCarrierClass();
                        int tempLayers = 0;
                        tempLayers = device.getCaBandsCombinations().get(i).getULSingleCombination().get(ccNavigator).getSupportedLayers();
                        if (tempLayers == 2) {
                            eutraCaConfiguration = eutraCaConfiguration.concat("(2x2)");
                        } else if (tempLayers == 4){
                            eutraCaConfiguration = eutraCaConfiguration.concat("(4x4)");
                        } else if (tempLayers == 1){
                            // Do not concat anything
                        }
                        data[i][j+3] = tempLayers;
                        eutraCaConfiguration = eutraCaConfiguration.concat("-");
                        ccNavigator++;
                    }
                    eutraCaConfiguration = eutraCaConfiguration.substring(0,eutraCaConfiguration.length()-1);
                    data[i][1] = eutraCaConfiguration;
                    ccNavigator = 0;
                }
                break;

            case GUIUtility.DL_1310:
                data = new Object[device.getCaV1310BandCombinations().size()][19];
                for (int i = 0 ; i < device.getCaV1310BandCombinations().size(); i++) {
                    int currentCCNumber = device.getCaV1310BandCombinations().get(i).getDLSingleCombination().size();
                    int ccNavigator = 0;
                    data[i][0] = String.valueOf(i);
                    /*
                    if(Utility.bcsParser(device.getCaV1310BandCombinations().get(i).getSupportedBCS()) != "") {
                        data[i][2] = Utility.bcsParser(device.getCaV1310BandCombinations().get(i).getSupportedBCS());
                    }
                    else {
                        data[i][2] = "-";
                    }

                     */
                    data[i][2] = String.valueOf(device.getCaV1310BandCombinations().get(i).getCcQuantityDL());
                    String eutraCaConfiguration = "CA_";
                    for (int j = 3 ; j < 3 + currentCCNumber*3 ; j = j + 3) {
                        data[i][j+1] = device.getCaV1310BandCombinations().get(i).getDLSingleCombination().get(ccNavigator).getBandID();
                        eutraCaConfiguration = eutraCaConfiguration.concat(String.valueOf(device.getCaV1310BandCombinations()
                                .get(i).getDLSingleCombination().get(ccNavigator).getBandID()));
                        eutraCaConfiguration = eutraCaConfiguration.concat(device.getCaV1310BandCombinations()
                                .get(i).getDLSingleCombination().get(ccNavigator).getComponentCarrierClass());
                        data[i][j+2] = device.getCaV1310BandCombinations().get(i).getDLSingleCombination().get(ccNavigator).getComponentCarrierClass();
                        int tempLayers = 0;
                        tempLayers = device.getCaV1310BandCombinations().get(i).getDLSingleCombination().get(ccNavigator).getSupportedLayers();
                        if (tempLayers == 2) {
                            eutraCaConfiguration = eutraCaConfiguration.concat("(2x2)");
                        } else if (tempLayers == 4){
                            eutraCaConfiguration = eutraCaConfiguration.concat("(4x4)");
                        } else if (tempLayers == 1){
                            // Do not concat anything
                        }
                        eutraCaConfiguration = eutraCaConfiguration.concat("-");
                        data[i][j+3] = tempLayers;
                        ccNavigator++;
                    }
                    eutraCaConfiguration = eutraCaConfiguration.substring(0,eutraCaConfiguration.length()-1);
                    data[i][1] = eutraCaConfiguration;
                    ccNavigator = 0;
                }
                break;
            case GUIUtility.UL_1310:
                data = new Object[device.getCaV1310BandCombinations().size()][18];
                for (int i = 0 ; i < device.getCaV1310BandCombinations().size(); i++) {
                    int currentCCNumber = device.getCaV1310BandCombinations().get(i).getULSingleCombination().size();
                    int ccNavigator = 0;
                    data[i][0] = String.valueOf(i);
                    data[i][2] = String.valueOf(device.getCaV1310BandCombinations().get(i).getCcQuantityUL());
                    String eutraCaConfiguration = "CA_";
                    for (int j = 2 ; j < 2 + currentCCNumber*3 ; j = j + 3) {
                        eutraCaConfiguration = eutraCaConfiguration.concat(String.valueOf(device.getCaV1310BandCombinations()
                                .get(i).getULSingleCombination().get(ccNavigator).getBandID()));
                        eutraCaConfiguration = eutraCaConfiguration.concat(device.getCaV1310BandCombinations()
                                .get(i).getULSingleCombination().get(ccNavigator).getComponentCarrierClass());
                        data[i][j+1] = device.getCaV1310BandCombinations().get(i).getULSingleCombination().get(ccNavigator).getBandID();
                        data[i][j+2] = device.getCaV1310BandCombinations().get(i).getULSingleCombination().get(ccNavigator).getComponentCarrierClass();
                        int tempLayers = 0;
                        try {
                            tempLayers = device.getCaV1310BandCombinations().get(i).getULSingleCombination().get(ccNavigator).getSupportedLayers();
                        }
                        catch (Exception e){
                            System.out.println("upload not supported on this band");
                        }
                        if (tempLayers == 2) {
                            eutraCaConfiguration = eutraCaConfiguration.concat("(2x2)");
                        } else if (tempLayers == 4){
                            eutraCaConfiguration = eutraCaConfiguration.concat("(4x4)");
                        } else if (tempLayers == 1){
                            // Do not concat anything
                        }
                        data[i][j+3] = tempLayers;
                        eutraCaConfiguration = eutraCaConfiguration.concat("-");
                        ccNavigator++;
                    }
                    eutraCaConfiguration = eutraCaConfiguration.substring(0,eutraCaConfiguration.length()-1);
                    data[i][1] = eutraCaConfiguration;
                    ccNavigator = 0;
                }
                break;



            case GUIUtility.DL_10i0:
                data = new Object[device.getCaBandsCombinations().size()][18];
                for (int i = 0 ; i < device.getCaBandsCombinations().size(); i++) {
                    int currentCCNumber = device.getCaBandsCombinations().get(i).getDLSingleCombination().size();
                    int ccNavigator = 0;
                    data[i][0] = String.valueOf(i);
                    data[i][2] = Utility.bcsParser(device.getCaBandsCombinations().get(i).getSupportedBCS());
                    data[i][3] = String.valueOf(device.getCaBandsCombinations().get(i).getCcQuantityDL());
                    String eutraCaConfiguration = "CA_";
                    for (int j = 3 ; j < 3 + currentCCNumber*3 ; j = j + 3) {
                        eutraCaConfiguration = eutraCaConfiguration.concat(String.valueOf(device.getCaBandsCombinations()
                                .get(i).getDLSingleCombination().get(ccNavigator).getBandID()));
                        eutraCaConfiguration = eutraCaConfiguration.concat(device.getCaBandsCombinations()
                                .get(i).getDLSingleCombination().get(ccNavigator).getComponentCarrierClass());
                        data[i][j+1] = device.getCaBandsCombinations().get(i).getDLSingleCombination().get(ccNavigator).getBandID();
                        data[i][j+2] = device.getCaBandsCombinations().get(i).getDLSingleCombination().get(ccNavigator).getComponentCarrierClass();
                        int tempLayers = 0;
                        tempLayers = device.getCaBandsCombinations().get(i).getDLSingleCombination().get(ccNavigator).getSupportedLayersTM34();
                        if (tempLayers == 2) {
                            eutraCaConfiguration = eutraCaConfiguration.concat("(2x2)");
                        } else if (tempLayers == 4){
                            eutraCaConfiguration = eutraCaConfiguration.concat("(4x4)");
                        } else if (tempLayers == 1){
                            // Do not concat anything
                        }

                        eutraCaConfiguration = eutraCaConfiguration.concat("-");
                        data[i][j+3] = tempLayers;
                        ccNavigator++;
                    }
                    eutraCaConfiguration = eutraCaConfiguration.substring(0,eutraCaConfiguration.length()-1);
                    data[i][1] = eutraCaConfiguration;
                    ccNavigator = 0;
                }
                break;
            case GUIUtility.UL_10i0:
                data = new Object[device.getCaBandsCombinations().size()][17];
                for (int i = 0 ; i < device.getCaBandsCombinations().size(); i++) {
                    int currentCCNumber = device.getCaBandsCombinations().get(i).getULSingleCombination().size();
                    int ccNavigator = 0;
                    data[i][0] = String.valueOf(i);
                    data[i][2] = String.valueOf(device.getCaBandsCombinations().get(i).getCcQuantityUL());
                    String eutraCaConfiguration = "CA_";
                    for (int j = 2 ; j < 2 + currentCCNumber*3 ; j = j + 3) {
                        eutraCaConfiguration = eutraCaConfiguration.concat(String.valueOf(device.getCaBandsCombinations()
                                .get(i).getULSingleCombination().get(ccNavigator).getBandID()));
                        eutraCaConfiguration = eutraCaConfiguration.concat(device.getCaBandsCombinations()
                                .get(i).getULSingleCombination().get(ccNavigator).getComponentCarrierClass());
                        data[i][j+1] = device.getCaBandsCombinations().get(i).getULSingleCombination().get(ccNavigator).getBandID();
                        data[i][j+2] = device.getCaBandsCombinations().get(i).getULSingleCombination().get(ccNavigator).getComponentCarrierClass();
                        int tempLayers = 0;
                        tempLayers = device.getCaBandsCombinations().get(i).getULSingleCombination().get(ccNavigator).getSupportedLayersTM34();
                        if (tempLayers == 2) {
                            eutraCaConfiguration = eutraCaConfiguration.concat("(2x2)");
                        } else if (tempLayers == 4){
                            eutraCaConfiguration = eutraCaConfiguration.concat("(4x4)");
                        } else if (tempLayers == 1){
                            // Do not concat anything
                        }
                        eutraCaConfiguration = eutraCaConfiguration.concat("-");
                        data[i][j+3] = tempLayers;
                        ccNavigator++;
                    }
                    eutraCaConfiguration = eutraCaConfiguration.substring(0,eutraCaConfiguration.length()-1);
                    data[i][1] = eutraCaConfiguration;
                    ccNavigator = 0;
                }
                break;
            case GUIUtility.DL_ENNR:
                data = new Object[device.getMrdcBandCombinations().size()][47];
                for (int i = 0 ; i < device.getMrdcBandCombinations().size(); i++) {
                    int sizeDL4G = device.getMrdcBandCombinations().get(i).getDownlink4G().size();
                    int sizeDL5G = device.getMrdcBandCombinations().get(i).getDownlink5G().size();
                    data[i][0] = String.valueOf(i);
                    String config = "CA_";

                    int bandNavigator = 0;
                    for (int j = 1 ; j < 1 + sizeDL4G*3 ; j = j + 3, bandNavigator++) {
                    	config += device.getMrdcBandCombinations().get(i).getDownlink4G().get(bandNavigator).getBandID();
                    	config += device.getMrdcBandCombinations().get(i).getDownlink4G().get(bandNavigator).getBandClass();
                    	config += "(" + device.getMrdcBandCombinations().get(i).getDownlink4G().get(bandNavigator).getBandLayer() + ")";
                        config += "-";

                        data[i][j+1] = device.getMrdcBandCombinations().get(i).getDownlink4G().get(bandNavigator).getBandID();
                        data[i][j+2] = device.getMrdcBandCombinations().get(i).getDownlink4G().get(bandNavigator).getBandClass();
                        data[i][j+3] = device.getMrdcBandCombinations().get(i).getDownlink4G().get(bandNavigator).getBandLayer().split("x")[0];
                    }
                    
                    bandNavigator = 0;
                    for (int j = 16 ; j < 16 + sizeDL5G*3 ; j = j + 6, bandNavigator++) {
                    	config += device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandID();
                    	config += device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandClass();
                    	config += "(" + device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandLayer() + ")";
                    	config += device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandModulation();
                    	config += device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandSCS();
                    	config += device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandWidth();
                        config += "-";

                        data[i][j+1] = device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandID();
                        data[i][j+2] = device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandClass();
                        data[i][j+3] = device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandLayer().split("x")[0];
                        data[i][j+4] = device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandModulation();
                        data[i][j+5] = device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandSCS();
                        data[i][j+6] = device.getMrdcBandCombinations().get(i).getDownlink5G().get(bandNavigator).getBandWidth();
                    }

                    config = config.substring(0,config.length()-1);
                    data[i][1] = config;
                    bandNavigator = 0;
                }
                break;
            case GUIUtility.UL_ENNR:
                data = new Object[device.getMrdcBandCombinations().size()][47];
                for (int i = 0 ; i < device.getMrdcBandCombinations().size(); i++) {
                    int sizeDL4G = device.getMrdcBandCombinations().get(i).getUplink4G().size();
                    int sizeDL5G = device.getMrdcBandCombinations().get(i).getUplink5G().size();
                    data[i][0] = String.valueOf(i);
                    String config = "CA_";

                    int bandNavigator = 0;
                    for (int j = 1 ; j < 1 + sizeDL4G*3 ; j = j + 3, bandNavigator++) {
                    	config += device.getMrdcBandCombinations().get(i).getUplink4G().get(bandNavigator).getBandID();
                    	config += device.getMrdcBandCombinations().get(i).getUplink4G().get(bandNavigator).getBandClass();
                    	config += "(" + device.getMrdcBandCombinations().get(i).getUplink4G().get(bandNavigator).getBandLayer() + ")";
                        config += "-";

                        data[i][j+1] = device.getMrdcBandCombinations().get(i).getUplink4G().get(bandNavigator).getBandID();
                        data[i][j+2] = device.getMrdcBandCombinations().get(i).getUplink4G().get(bandNavigator).getBandClass();
                        data[i][j+3] = device.getMrdcBandCombinations().get(i).getUplink4G().get(bandNavigator).getBandLayer().split("x")[0];
                    }
                    
                    bandNavigator = 0;
                    for (int j = 16 ; j < 16 + sizeDL5G*3 ; j = j + 6, bandNavigator++) {
                    	config += device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandID();
                    	config += device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandClass();
                    	config += "(" + device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandLayer() + ")";
                    	config += device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandModulation();
                    	config += device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandSCS();
                    	config += device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandWidth();
                        config += "-";

                        data[i][j+1] = device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandID();
                        data[i][j+2] = device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandClass();
                        data[i][j+3] = device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandLayer().split("x")[0];
                        data[i][j+4] = device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandModulation();
                        data[i][j+5] = device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandSCS();
                        data[i][j+6] = device.getMrdcBandCombinations().get(i).getUplink5G().get(bandNavigator).getBandWidth();
                    }

                    config = config.substring(0,config.length()-1);
                    data[i][1] = config;
                    bandNavigator = 0;
                }
                break;
            default: data = null;
                break;
        }

        return data;
    }

    /**
     * UI support method to format supported band list
     * @param bandList arraylist of the supported bands
     * @return the formatted string with the list of bands
     */
    public static String supportedBandsUIFormatter (ArrayList<Band> bandList){
        String formattedBandList = "";
        if ((bandList == null) || (bandList.size() == 0)) {
            formattedBandList= "-";
        } else {
            for (int i = 0; i < bandList.size(); i++) {
                formattedBandList = formattedBandList.concat(String.valueOf(bandList.get(i).getBandID())).concat(",");
            }
            formattedBandList = formattedBandList.substring(0,formattedBandList.length()-1);
        }
        return formattedBandList;
    }
    /**
     * UI support method to format supported band list
     * @param bandList arraylist of the supported bands
     * @return the formatted string with the list of bands
     */
    public static String supportedLTEBandsUIFormatter (ArrayList<LTEBand> bandList){
        String formattedBandList = "";
        if ((bandList == null) || (bandList.size() == 0)) {
            formattedBandList= "-";
        } else {
            for (int i = 0; i < bandList.size(); i++) {
                formattedBandList = formattedBandList.concat(String.valueOf(bandList.get(i).getBandID())).concat(",");
            }
            formattedBandList = formattedBandList.substring(0,formattedBandList.length()-1);
        }
        return formattedBandList;
    }

    /**
     * UI support method to format supported umts band list
     * @param bandList arraylist of the supported bands
     * @return formatted string with the list of bands
     */
    public static String supportedUMTSBandsUIFormatter (ArrayList<UMTSBand> bandList){
        String formattedBandList = "";
        for (int i = 0; i < bandList.size(); i++) {
            // UMTS bands are indexed starting from zero
            formattedBandList = formattedBandList.concat(String.valueOf(bandList.get(i).getBandID())).concat(",");
        }
        formattedBandList = formattedBandList.substring(0,formattedBandList.length()-1);
        return formattedBandList;
    }

    /**
     * UI support method to format supported band list
     * @param bandList arraylist of the supported GSM bands
     * @return the formatted string with the list of GSM bands
     */
    public static String supportedGSMBandsUIFormatter(ArrayList<GSMBand> bandList){
        String formattedBandList = "";
        for (int i = 0; i < bandList.size(); i++) {
            formattedBandList = formattedBandList.concat(String.valueOf(bandList.get(i).getGsmBandName())).concat(",");
        }
        formattedBandList = formattedBandList.substring(0,formattedBandList.length()-1);
        return formattedBandList;
    }
    /**
     * UI support method to format 256 qam dl support string to be displayed in UI
     * @param bandList the band list of dut
     * @return the formatted string containing the supported bands
     */
    public static String supportedDLModulationBandsUIFormatter(ArrayList<LTEBand> bandList){
        String formattedBandList = "";
        for (int i = 0; i < bandList.size(); i++) {
            formattedBandList = formattedBandList.concat(String.valueOf(bandList.get(i).getBandID()));
            if (bandList.get(i).isSupport256QAMDL()) {
                formattedBandList = formattedBandList.concat(" Yes,");
            } else {
                formattedBandList = formattedBandList.concat(" No,");
            }
        }
        formattedBandList = formattedBandList.substring(0,formattedBandList.length()-1);
        return formattedBandList;
    }

    /**
     * UI support method to format 64 qam ul support string to be displayed in UI
     * @param bandList the band list of dut
     * @return the formatted string containing the supported bands
     */
    public static String supportedULModulationBandsUIFormatter(ArrayList<LTEBand> bandList){
        String formattedBandList = "";
        for (int i = 0; i < bandList.size(); i++) {
            formattedBandList = formattedBandList.concat(String.valueOf(bandList.get(i).getBandID()));
            if (bandList.get(i).isSupport64QAMUL()) {
                formattedBandList = formattedBandList.concat(" Yes,");
            } else {
                formattedBandList = formattedBandList.concat(" No,");
            }
        }
        formattedBandList = formattedBandList.substring(0,formattedBandList.length()-1);
        return formattedBandList;
    }

    /**
     * UI support method to parse identity type and decode it as string
     * @param identity the integer describing the type of identity
     * @return the identity decoded as String
     */
    public static String identityTypeUIFormatter(int identity) {
        String t = "";
        switch(identity) {
            case 1: t = Utility.IMSI;
                break;
            case 6: t = Utility.GUTI;
                break;
            default: t = Utility.MISSING_CODE;
                break;
        }
        return t;
    }

    /**
     * UI support method to parse pdn type as string
     * @param pdnType the pdn type retrieved as integer
     * @return the string describing the pdn type
     */
    public static String pdnTypeUIFormatter(int pdnType) {
        String t = "";
        switch (pdnType) {
            case 1: t =Utility.IPV4_ADDRESSING;
                break;
            case 2: t = Utility.IPV6_ADDRESSING;
                break;
            case 3: t = Utility.DUAL_STACK_IPV4V6;
                break;
            default: t = Utility.MISSING_CODE;
                break;
        }
        return t;
    }

    /**
     * Private support method to format support for ceMode catm devices
     * @param ceCode the ceMode code as integer
     * @return the string describing the support for cemode
     */
    public static String ceModeUIFormatter(int ceCode) {
        String t = "";
        switch (ceCode) {
            case 0:  t = Utility.TRUE;
                break;
            case 1: t = Utility.FALSE;
                break;
            default: t = Utility.MISSING_CODE;
                break;
        }
        return t;
    }

    /**
     * UI Support method to format values with missing values as string
     * @param value the value as float
     * @return the decoded value as string
     */
    public static String missingValueUIFormatter (float value){
        String formattedString = "";
        if (value == Utility.ERROR_CODE) {
            formattedString = Utility.NOT_APPLICABLE;
        } else {
            formattedString = String.valueOf(value);
        }
        return formattedString;
    }

    /**
     * Public support method to correctly display supported ROHC profiles in GUI
     * @param rohcMap the hashmap containing the supported rohc profiles of DUT
     * @return the string containing the rohc profiles well formatted
     */
    public static String supportedROHCProfilesUIFOrmatter (HashMap<String,Integer> rohcMap){
        String formattedROHCProfiles = "";
        int counter = 0;
        //ciphAlg.forEach((key, value) -> System.out.println(key + " : " + value));
        for (String keyName: rohcMap.keySet()){
            String key = keyName.toString();
            String value = rohcMap.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                formattedROHCProfiles = formattedROHCProfiles.concat(key).concat(",");
                counter++;
            }
        }
        if (counter==0 ) {
            formattedROHCProfiles = Utility.NOT_SUPPORTED;
        } else {
            formattedROHCProfiles = formattedROHCProfiles.substring(0,formattedROHCProfiles.length()-1);
        }
        return formattedROHCProfiles;
    }

    /**
     * Public method to format zero values into strings containing N/A code
     * @param value the integer value to decode
     * @return the decoded string
     */
    public static String fromZeroToNAValuesFormatter(int value){
        String t = "";
        if ((value == 0) || (value == Utility.ERROR_CODE)){
            t = Utility.NOT_APPLICABLE;
        } else{
            t = String.valueOf(value);
        }
        return t;
    }

    /**
     * RF Power Capability Formatter for GUI
     * @param powerClass
     * @return
     */
    public static String rfPowerCapabilityValuesFormatter(int powerClass){
        return "Class " + String.valueOf(powerClass);
    }

    /**
     * GUI support method to display PFC features
     * @param pfcFeature the int describing the pfc feature
     * @return the string describing pfc feature value
     */
    public static String pfcFeaturesValueFormatter(int pfcFeature){
        String t = "";
        switch (pfcFeature){
            case 0: t = DECODED_PFC_0;
                break;
            case 1: t = DECODED_PFC_1;
                break;
            default: t = Utility.MISSING_CODE;
                break;
        }
        return t;
    }
}
