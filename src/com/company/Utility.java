package com.company;

import java.util.ArrayList;

import com.google.gson.JsonPrimitive;

/**
 * Utility class to store all the shared strings (Json file keys), support strings
 * and shared constants
 */
public class Utility {
    // DIRECTORY PATH
    public static String INPUT_FILE_PATH = "";
    public static String PROGRAM_PATH = "";

    // TSHARK FILTER
    final public static String S1AP_TSHARK_FILTER = "-R \"s1ap\" -r ";
    final public static String SGSAP_TSHARK_FILTER = "-R \"sgsap\" -r ";
    final public static String SIP_TSHARK_FILTER = "-Y \"sip\" -r ";
    final public static String TSHARK_OPTION_LEGACY_FILTER = " -o \"s1ap.dissect_lte_container_as: Legacy LTE\" ";
    final public static String FRAME_NUMBER_TSHARK_FILTER = "-Y \"frame.number==";
    final public static String TSHARK_OPTION_NBIOT_FILTER = " -o \"s1ap.dissect_lte_container_as: NB-IoT\"";

    // REGEX
    final public static String CATEGORY_REGEX = ".*category+.*";
    final public static String CATEGORY_DL_REGEX = ".*DL+.*";
    final public static String CATEGORY_UL_REGEX = ".*UL+.*";
    final public static String ALG_REGEX = "[^e](alg=.[^;]*)";
    final public static String EALG_REGEX = "(ealg=.[^;]*)";
    final public static String RTPMAP_REGEX = "(rtpmap)+:+";
    final public static String MSISDN_REGEX = ".*([0-9]{10}).*";
    final public static String ISDIGIT_REGEX = "-?\\d+";
    final public static String SRVCC_SUPPORT_REGEX = "(\\+g\\.3gpp\\.[^;]*)";
    final public static String SRVCC_SUPPORT_REGEX_ICSI = "(\\+g\\.3gpp\\.icsi[^;]*)";
    final public static String FILE_NAME_REGEX = "((\\.(?i)(pcap|pcapng))$)";
    final public static String FILE_NAME_REGEX_json = "((\\.(?i)(json))$)";
    final public static String IMSI_REGEX = "([0-9]{15})";
    final public static String TELEPHONE_EVENT_REGEX = ".*(telephone-event)+.*";
    final public static String FMTP_REGEX = ".*(fmtp)+.*";
    final public static String MODE_SET_REGEX = "(mode-set=.[^;]*)";
    final public static String SDP_MIME_TYPE_REGEX = "(sdp\\.mime\\.type:)";
    final public static String SAMPLE_RATE_REGEX = "(sdp\\.sample_rate:)";
    final public static String MEDIA_FORMAT_REGEX = "(sdp\\.media\\.format:)";
    final public static String EVS_REGEX = "(EVS)";
    final public static String AMR_REGEX = "(AMR)";
    final public static String AMR_WB_REGEX = "(AMR-WB)";
    final public static String INSIDE_SLAH_REGEX = "/([^/]*)([/|$]?)";
    final public static String MEDIA_FORMAT_FIRST_CODEC_REGEX = "\\d+\\s";
    final public static String BW_REGEX = "(bw=.[^;]*)";
    final public static String BR_REGEX = "(br=.[^;]*)";
    final public static String IMEISV_REGEX = "IMEISV{1}";

    final public static int NOT_ASSIGNED = -98;
    final public static int ERROR_CODE = -99;
    final public static int SERVER_TCP_PORT = 5060;
    final public static int NOT_APPLICABLE_CODE = 4;
    final public static int DEACTIVATED = 999;

    // STRING CODES
    final public static String NOT_SET = "Not Set";
    public static boolean IS_VOLTE = true;
    public static boolean IS_SIP_PRESENT = true;
    public static String ERROR_CAUSE = "";
    public static int INVITE_FRAME_NUMBER = 0;
    final public static String MISSING_CODE = "Missing Code";
    final public static String SUPPORTED = "Supported";
    final public static String NOT_SUPPORTED = "Not Supported";
    final public static String DEFAULT_ALPHABET = "Default alphabet over UCS2";
    final public static String NO_PREFERENCE = "No Preference";
    final public static String DEFAULT_VALUE_OF_PHASE1 = "Default value of phase 1";
    final public static String ELLIPSIS_NOTATION_CAPABILITY = "Capability of Handling of ellipsis notation and phase 2 error handling";

    // RF Power Capability
    final public static String PSK8_PC_E1 = "Power class E1";
    final public static String PSK8_PC_E2 = "Power class E2";
    final public static String PSK8_PC_E3 = "Power class E3";

    // ERROR CAUSES
    final public static String EMPTY_FILE = "Empty File";
    final public static String WRONG_IMSI = "No REGISTER with the provided IMSI has been found, are you sure you have inserted the correct one?";
    final public static String NO_200_OK = "No 200 has been found, are you sure the input file meets the right requirements?";
    final public static String NO_INVITE = "No INVITE has been found, please check your input file";
    final public static String NO_REGISTER = "No REGISTER has been found. Please, check your input file";
    final public static String UNKNOWN_ERROR = "Unkwnown";

    // Support strings
    final public static String FGI = "lte-rrc.eutra_cap_feat_group_ind_";
    final public static String EOF = "EOF";
    final public static String ALG_STRING  = "alg";
    final public static String EALG_STRING = "ealg";
    final public static String TRUE = "true";
    final public static String FALSE = "false";

    // Attach types:
    final public static String EPS_ATTACH = "EPS attach";
    final public static String COMBINED_EPS_IMSI_ATTACH = "Combined EPS/IMSI attach";
    final public static String EPS_EMERGENCY_ATTACH = "EPS EMERGENCY ATTACH";
    final public static String RESERVED = "RESERVED";
    final public static String IMSI = "IMSI";
    final public static String GUTI = "GUTI";

    // PDN TYPES
    final public static String IPV4_ADDRESSING = "IPv4";
    final public static String IPV6_ADDRESSING = "IPv6";
    final public static String DUAL_STACK_IPV4V6 = "IPv4v6";

    //Voice domain preference
    final public static String CS_VOICE_ONLY = "CS Voice Only";
    final public static String IMS_PS_VOICE_ONLY = "IMS PS Voice Only";
    final public static String CS_VOICE_PREFERRED = "CS Voice Preferred, IMS PS voice as Secondary";
    final public static String IMS_PS_VOICE_PREFERRED = "IMS PS Voice Preferred, CS Voice as Secondary";
    final public static String VOICE_CENTRIC = "Voice Centric";
    final public static String DATA_CENTRIC = "Data Centric";
    final public static String SMS_ONLY = "SMS ONLY";
    final public static String NOT_APPLICABLE = "N/A";
    final public static String ADDITIONAL_UPDATE_TYPE = "Additional update type";

    // Voice codecs
    final public static String CODEC_TDMA_EFR ="gsm_a.dtap.codec.tdma_efr";
    final public static String CODEC_UMTS_AMR_2 = "gsm_a.dtap.codec.umts_amr_2";
    final public static String CODEC_UMTS_AMR = "gsm_a.dtap.codec.umts_amr";
    final public static String CODEC_HR_AMR = "gsm_a.dtap.codec.hr_amr";
    final public static String CODEC_FR_AMR = "gsm_a.dtap.codec.fr_amr";
    final public static String CODEC_GSM_EFR = "gsm_a.dtap.codec.gsm_efr";
    final public static String CODEC_GSM_HR = "gsm_a.dtap.codec.gsm_hr";
    final public static String CODEC_GSM_FR = "gsm_a.dtap.codec.gsm_fr";
    final public static String CODEC_UMTS_EVS = "gsm_a.dtap.codec.umts_evs";
    final public static String CODEC_OHR_AMR_WB = "gsm_a.dtap.codec.ohr_amr_wb";
    final public static String CODEC_OFR_AMR_WB = "gsm_a.dtap.codec.ofr_amr_wb";
    final public static String CODEC_OHR_AMR = "gsm_a.dtap.codec.ohr_amr";
    final public static String CODEC_UMTS_AMR_WB = "gsm_a.dtap.codec.umts_amr_wb";
    final public static String CODEC_FR_AMR_WB = "gsm_a.dtap.codec.fr_amr_wb";
    final public static String CODEC_PDC_EFR = "gsm_a.dtap.codec.pdc_efr";

    //Volte Codecs Supports strings
    final public static String SDP_MIME_TYPE = "sdp.mime.type:";
    final public static String SDP_SAMPLE_RATE = "sdp.sample_rate:";
    final public static String SDP_MEDIA_FORMAT = "sdp.media.format:";
    final public static String BW_ = "bw=";
    final public static String BR_ = "br=";
    final public static String VOLTE_EVS = "EVS";
    final public static String VOLTE_AMR = "AMR";
    final public static String VOLTE_AMR_WB = "AMR-WB";

    // Voice codecs support strings
    final public static String GSM = "gsm";
    final public static String UMTS = "umts";
    final public static String TDMA_EFR = "TDMA EFR";
    final public static String UMTS_AMR_2 = "UMTS AMR 2";
    final public static String UMTS_AMR = "UMTS AMR";
    final public static String HR_AMR = "HR AMR";
    final public static String FR_AMR = "FR AMR";
    final public static String GSM_EFR = "GSM EFR";
    final public static String GSM_HR = "GSM HR";
    final public static String GSM_FR = "GSM FR";
    final public static String UMTS_EVS = "UMTS EVS";
    final public static String OHR_AMR_WB = "OHR AMR-WB";
    final public static String OFR_AMR_WB = "OFR AMR-WB";
    final public static String OHR_AMR = "OHR AMR";
    final public static String UMTS_AMR_WB = "UMTS AMR-WB";
    final public static String FR_AMR_WB = "FR AMR-WB";
    final public static String PDC_EFR = "PDC EFR";

    // Enc. ALGORITHMS SUPPORT STRINGS LTE
    final public static String EEA0 = "EEA0";
    final public static String EEA1128 = "128-EEA1";
    final public static String EEA2128 = "128-EEA2";
    final public static String EEA3 = "128-EEA3";
    final public static String EEA4 = "EEA4";
    final public static String EEA5 = "EEA5";
    final public static String EEA6 = "EEA6";
    final public static String EEA7 = "EEA7";
    // Integrity
    final public static String EIA0 = "EIA0";
    final public static String EIA1128 = "128-EIA1";
    final public static String EIA2128 = "128-EIA2";
    final public static String EIA3 = "128-EIA3";
    final public static String EIA4 = "EIA4";
    final public static String EIA5 = "EIA5";
    final public static String EIA6 = "EIA6";
    final public static String EIA7 = "EIA7";

    // UMTS Enc.
    final public static String UEA0 = "UEA0";
    final public static String UEA1 = "UEA1";
    final public static String UEA2 = "UEA2";
    final public static String UEA3 = "UEA3";
    final public static String UEA4 = "UEA4";
    final public static String UEA5 = "UEA5";
    final public static String UEA6 = "UEA6";
    final public static String UEA7 = "UEA7";
    // Integrity
    final public static String UIA1 = "UIA1";
    final public static String UIA2 = "UIA2";
    final public static String UIA3 = "UIA3";
    final public static String UIA4 = "UIA4";
    final public static String UIA5 = "UIA5";
    final public static String UIA6 = "UIA6";
    final public static String UIA7 = "UIA7";

    // Enc.
    final public static String GEA1 = "GEA1";
    final public static String GEA2 = "GEA2";
    final public static String GEA3 = "GEA3";
    final public static String GEA4 = "GEA4";
    final public static String GEA5 = "GEA5";
    final public static String GEA6 = "GEA6";
    final public static String GEA7 = "GEA7";
    final public static String A51 = "A5/1";
    final public static String A52 = "A5/2";
    final public static String A53 = "A5/3";
    final public static String A54 = "A5/4";
    final public static String A55 = "A5/5";
    final public static String A56 = "A5/6";
    final public static String A57 = "A5/7";
    // Supported Rohc profiles support strings LTE
    final public static String ROHC_0 = "0x0000";
    final public static String ROHC_1 = "0x0001";
    final public static String ROHC_2 = "0x0002";
    final public static String ROHC_3 = "0x0003";
    final public static String ROHC_4 = "0x0004";
    final public static String ROHC_6 = "0x0006";
    final public static String ROHC_101 = "0x0101";
    final public static String ROHC_102 = "0x0102";
    final public static String ROHC_103 = "0x0103";
    final public static String ROHC_104 = "0x0104";
    //Supported Rohc profiles r15 support strings LTE
    final public static String ROHC_0_r15 = "0x0000-r15";
    final public static String ROHC_1_r15 = "0x0001-r15";
    final public static String ROHC_2_r15 = "0x0002-r15";
    final public static String ROHC_3_r15 = "0x0003-r15";
    final public static String ROHC_4_r15 = "0x0004-r15";
    final public static String ROHC_6_r15 = "0x0006-r15";
    final public static String ROHC_101_r15 = "0x0101-r15";
    final public static String ROHC_102_r15 = "0x0102-r15";
    final public static String ROHC_103_r15 = "0x0103-r15";
    final public static String ROHC_104_r15 = "0x0104-r15";

    // GSM Supported bands
    final public static String GSM_0 = "gsm450";
    final public static String GSM_1 = "gsm480";
    final public static String GSM_2 = "gsm710";
    final public static String GSM_3 = "gsm750";
    final public static String GSM_4 = "gsm810";
    final public static String GSM_5 = "gsm850";
    final public static String GSM_6 = "gsm900P";
    final public static String GSM_7 = "gsm900E";
    final public static String GSM_8 = "gsm900R";
    final public static String GSM_9 = "gsm1800";
    final public static String GSM_10 = "gsm1900";

    // Downlink Advanced Receiver Performances
    final public static String DOWNLINK_ADV_RX_SUPP_1 = "Phase 1";
    final public static String DOWNLINK_ADV_RX_SUPP_2 = "Phase 2";
    final public static String SHALL_NOT_USE = "MS shall not use such a value";
    final public static String DTM_MULTI_SLOT_CLASS_1 = "class 5";
    final public static String DTM_MULTI_SLOT_CLASS_2 = "class 9";
    final public static String DTM_MULTI_SLOT_CLASS_3 = "class 11";

    // UMTS supported bands
    final public static String UMTS_BAND_0 = "bandI";
    final public static String UMTS_BAND_1 = "bandII";
    final public static String UMTS_BAND_2 = "bandIII";
    final public static String UMTS_BAND_3 = "bandIV";
    final public static String UMTS_BAND_4 = "bandV";
    final public static String UMTS_BAND_5 = "bandVI";
    final public static String UMTS_BAND_6 = "bandVII";
    final public static String UMTS_BAND_7 = "bandVIII";
    final public static String UMTS_BAND_8 = "bandIX";
    final public static String UMTS_BAND_9 = "bandX";
    final public static String UMTS_BAND_10 = "bandXI";
    final public static String UMTS_BAND_11 = "bandXII";
    final public static String UMTS_BAND_12 = "bandXIII";
    final public static String UMTS_BAND_13 = "bandXIV";
    final public static String UMTS_BAND_14 = "bandXV";
    final public static String UMTS_BAND_15 = "bandXVI";
    final public static String UMTS_BAND_16 = "bandXVII-8a0";
    final public static String UMTS_BAND_17 = "bandXVIII-8a0";
    final public static String UMTS_BAND_18 = "bandXIX-8a0";
    final public static String UMTS_BAND_19 = "bandXX-8a0";
    final public static String UMTS_BAND_20 = "bandXXI-8a0";
    final public static String UMTS_BAND_21 = "bandXXII-8a0";
    final public static String UMTS_BAND_22 = "bandXXIII-8a0";
    final public static String UMTS_BAND_23 = "bandXXIV-8a0";
    final public static String UMTS_BAND_24 = "bandXXV-8a0";
    final public static String UMTS_BAND_25 = "bandXXVI-8a0";
    final public static String UMTS_BAND_26 = "bandXXVII-8a0";
    final public static String UMTS_BAND_27 = "bandXXVIII-8a0";
    final public static String UMTS_BAND_28 = "bandXXIX-8a0";
    final public static String UMTS_BAND_29 = "bandXXX-8a0";
    final public static String UMTS_BAND_30 = "bandXXXI-8a0";
    final public static String UMTS_BAND_31 = "bandXXXII-8a0";

    //CatM Support strings
    final public static String CATM_CATEGORY_M1 = "m1";

    // NB-IoT Support strings
    final public static String NB_CATEGORY_1 = "nb1";
    final public static String NB_CATEGORY_2 = "nb2";

    // GUI strings
    public static String PCAP_RRC_FILE_NAME = "";

    // SUPPORT VARIABLES
    public static ArrayList<String> ueCapJsonParsingTree = null;
    public static ArrayList<String> ueCapJsonNRParsingTree = null;

    public final static String BANDWIDTH_CLASS_A = "A";
    public final static String BANDWIDTH_CLASS_B = "B";
    public final static String BANDWIDTH_CLASS_C = "C";
    public final static String BANDWIDTH_CLASS_D = "D";
    public final static String BANDWIDTH_CLASS_E = "E";
    public final static String BANDWIDTH_CLASS_F = "F";
    public final static String BANDWIDTH_CLASS_I = "I";

    /**
     * Private support method to retrieve access stratum release from wireshark id codes
     * @param asRel AS release code retrieved from pcap
     */
    public static int accessStratumReleaseDecoder(int asRel) {
        int decodedRelease;
        switch (asRel) {
            case 0: decodedRelease = 8;
                break;
            case 1: decodedRelease = 9;
                break;
            case 2: decodedRelease = 10;
                break;
            case 3: decodedRelease = 11;
                break;
            case 4: decodedRelease = 12;
                break;
            case 5: decodedRelease = 13;
                break;
            case 6: decodedRelease = 14;
                break;
            case 7: decodedRelease = 15;
                break;
            default: decodedRelease = 0;
                break;
        }
        return decodedRelease;
    }
    
    public static int accessStratumReleaseDecoder5G(int asRel) {
        int decodedRelease;
        switch (asRel) {
            case 0: decodedRelease = 15;
                break;
            case 1: decodedRelease = 16;
                break;
            case 2: decodedRelease = 17;
                break;
            case 3: decodedRelease = 18;
                break;
            case 4: decodedRelease = 19;
                break;
            case 5: decodedRelease = 20;
                break;
            case 6: decodedRelease = 21;
                break;
            case 7: decodedRelease = 22;
                break;
            default: decodedRelease = 0;
                break;
        }
        return decodedRelease;
    }
    
    
    /**
     * private support method to retrieve description of the wireshark code
     * @param voicePreference integer retrieved from pcap which describes voice domain preference attribute
     * @return the decoded string
     */
    public static String voiceDomainPreferenceDecoder(int voicePreference) {
        String decodedPreference = "";
        switch (voicePreference) {
            case 0: decodedPreference = CS_VOICE_ONLY;
                break;
            case 1: decodedPreference = IMS_PS_VOICE_ONLY;
                break;
            case 2: decodedPreference = CS_VOICE_PREFERRED;
                break;
            case 3: decodedPreference = IMS_PS_VOICE_PREFERRED;
                break;
            case 4: decodedPreference = NOT_APPLICABLE;
                break;
            default: decodedPreference = null;
                break;
        }
        return decodedPreference;
    }
    /**
     * Private support method to retrieve String related to integer code concerning attach type
     * @param attType integer describing type of attach in attach request
     * @return a string describing the attach type
     */
    public static String attachTypeDecoder(int attType) {
        String attachDescription = "";
        switch (attType) {
            case 1: attachDescription = EPS_ATTACH;
                break;
            case 2: attachDescription = COMBINED_EPS_IMSI_ATTACH;
                break;
            case 6: attachDescription = EPS_EMERGENCY_ATTACH;
                break;
            case 7: attachDescription = RESERVED;
                break;
            default: attachDescription = EOF;
                break;
        }
        return attachDescription;
    }
    /**
     * Private support method to print BCS on screen
     * @param bcs the array containing the bcs parameters
     * @return the bcs formatted as string
     */
    public static String bcsParser(int[] bcs){
        String bcsParsed = "";
        for (int i = 0; i < bcs.length; i++){
            if (bcs[i] == 0 ){
                //bcsParsed = bcsParsed.concat(Integer.toString(bcs[i])).concat(",");
            } else {
                bcsParsed = bcsParsed.concat(Integer.toString(i)).concat(",");
            }
        }
        if (bcsParsed.equals("")) {
            bcsParsed = "0";
        } else {
            // Remove last comma from built string
            bcsParsed = bcsParsed.substring(0, bcsParsed.length() - 1);
        }
        return bcsParsed;
    }
    /**
     * Public method to convert fgi from vector of integers into string
     * @param fgiVector the integer vector containing fgis
     * @return the fgi decoded as string
     */
    public static String fgiParser(int[] fgiVector){
        String fgiAsString = "";
        if (fgiVector == null) {
            fgiAsString = Utility.NOT_APPLICABLE;
        } else {
            for (int i = 0; i < fgiVector.length; i++) {
                fgiAsString = fgiAsString.concat(" " + String.valueOf(fgiVector[i]));
            }
        }
        return fgiAsString;
    }
    
    
    public static char bandClassDecoder(int bandClass){
    	return (char) ((char)'A' + bandClass);
    }
    
    public static String bandLayerDecoder(JsonPrimitive layer) {
    	if (layer == null)
    		return "1x1";

        switch (layer.getAsInt()) {
	        case 0: return "2x2";
	        case 1: return "4x4";
	        case 2: return "8x8";
        }
        return null;
    }
    
    public static String bandLayerDecoderUL(JsonPrimitive layer) {
    	if (layer == null)
    		return null;

        switch (layer.getAsInt()) {
	        case 0: return "1x1";
	        case 1: return "2x2";
	        case 2: return "4x4";
        }
        return null;
    }
    
    public static String bandSCSDecoder(int scs) {
        switch (scs) {
	        case 0: return "kHz15";
	        case 1: return "kHz30";
	        case 2: return "kHz60";
	        case 3: return "kHz120";
	        case 4: return "kHz240";
	        case 5: return "spare3";
	        case 6: return "spare2";
	        case 7: return "spare1";
        }
        return null;
    }

    public static String scsDecoder(int scs, int index){
        switch (scs) {
            case -1:
                switch (index){
                    case 10:
                    case 11: return "*";
                }
            case 0:
                switch (index){
                    case 10:
                    case 11: return "-";
                }
            case 1:
                switch (index){
                    case 10: return "90";
                    case 11: return "100";
                }
            case 48:
                switch (index) {
                    case 0:
                    case 1:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6:
                    case 7:
                    case 8:
                    case 9:
                    case 10:
                    case 11: return "-";
                }
            case 49:
                switch (index) {
                    case 0: return "5";
                    case 1: return "10";
                    case 2: return "15";
                    case 3: return "20";
                    case 4: return "25";
                    case 5: return "30";
                    case 6: return "40";
                    case 7: return "50";
                    case 8: return "60";
                    case 9: return "80";
                    case 10: return "90";
                    case 11: return "100";
                }
        }
        return null;
    }

    
    public static String bandWidthDecoder(int fr, int width) {
    	if(fr == 0) //fr1
	        switch (width) {
		        case 0: return "mhz5";
		        case 1: return "mhz10";
		        case 2: return "mhz15";
		        case 3: return "mhz20";
		        case 4: return "mhz25";
		        case 5: return "mhz30";
		        case 6: return "mhz40";
		        case 7: return "mhz50";
		        case 8: return "mhz60";
		        case 9: return "mhz80";
		        case 10: return "mhz100";
	        }
    	else if(fr == 1) //fr2
	        switch (width) {
		        case 0: return "mhz50";
		        case 1: return "mhz100";
		        case 2: return "mhz200";
		        case 3: return "mhz400";
	        }
        return null;
    }
    
    public static String bandModulationDecoder(int modulation) {
        switch (modulation) {
	        case 0: return "bpsk-halfpi";
	        case 1: return "bpsk";
	        case 2: return "qpsk";
	        case 3: return "qam16";
	        case 4: return "qam64";
	        case 5: return "qam256";
        }
        return null;
    }
}
