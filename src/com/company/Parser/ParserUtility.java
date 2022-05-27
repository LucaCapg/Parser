package com.company.Parser;

import com.company.Model.lte.VoLTECodec;
import com.company.Utility;
import com.google.gson.internal.LinkedTreeMap;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class ParserUtility {
    protected final static String SIP_REGISTER = "REGISTER";
    //SIP METHODS NAME
    protected final static String SIP_INVITE = "INVITE";
    protected final static String SIP_CONTACT = "sip.Contact";
    protected final static String SIP_USER_AGENT = "sip.User-Agent";
    public final static String SDP_MEDIA_ATTR = "sdp.media_attr";
    // Wireshark decodes json (BADLY)
    public final static String SDP_MEDIA_ATTR_TREE = "sdp.media_attr_tree";
    protected final static String SIP_VIA_SENT_BY_ADDR = "sip.Via.sent-by.address";
    protected final static String SIP_VIA_TREE = "sip.Via_tree";
    protected final static String SDP_MEDIA = "sdp.media";
    protected final static String SDP_KEY = "sdp";
    protected final static String SIP_MSG_BODY = "sip.msg_body";
    protected final static String SIP_CSEQ = "sip.CSeq";
    protected final static String SIP_CSEQ_TREE = "sip.CSeq_tree";
    protected final static String SIP_CSEQ_METHOD = "sip.CSeq.method";
    protected final static String SIP_P_EARLY_MEDIA = "sip.P-Early-Media";
    protected final static String SIP_SECURITY_CLIENT = "sip.Security-Client";
    protected final static String SIP_P_ASSOCIATED_URI = "sip.P-Associated-URI";
    protected final static String SIP_STATUS_CODE = "sip.Status-Code";
    protected final static String SIP_STATUS_LINE_TREE = "sip.Status-Line_tree";
    protected final static String TCP_DST_PORT = "tcp.dstport";
    protected final static String UDP_DST_PORT = "udp.dstport";
    protected final static String SIP_FROM_USER = "sip.from.user";
    protected final static String SIP_FROM_ADDR_TREE = "sip.from.addr_tree";
    protected final static String SIP_FROM_TREE = "sip.From_tree";
    protected final static String SIP_MSG_HDR_TREE = "sip.msg_hdr_tree";
    protected final static String SIP_METHOD = "sip.Method";
    protected final static String SIP_REQUEST_LINE_TREE = "sip.Request-Line_tree";
    protected final static String SIP_KEY = "sip";
    // SIP SIGNALING
    protected final static String TCP_KEY = "tcp";
    protected final static String UDP_KEY = "udp";
    protected final static String LTE_RRC_AS_RELEASE_R13 = "lte-rrc.accessStratumRelease_r13";
    protected final static String LTE_RRC_SUPPORTED_BAND_LIST_R13_TREE = "lte-rrc.supportedBandList_r13_tree";
    protected final static String LTE_RRC_BAND_R13 = "lte-rrc.band_r13";
    protected final static String LTE_RRC_SUPPORTED_BAND_NB_ELEMENT = "lte-rrc.SupportedBand_NB_r13_element";
    protected final static String LTE_RRC_SUPPORTED_BAND_LIST_R13 = "lte-rrc.supportedBandList_r13";
    protected final static String LTE_RRC_RF_PARAMETERS_R13_ELEMENT = "lte-rrc.rf_Parameters_r13_element";
    protected final static String LTE_RRC_MULTICARRIER_R13 = "lte-rrc.multiCarrier_r13";
    protected final static String LTE_RRC_MULTITONE_R13 = "lte-rrc.multiTone_r13";
    protected final static String LTE_RRC_PHY_LAY_R13_ELEMENT = "lte-rrc.phyLayerParameters_r13_element";
    protected final static String LTE_RRC_UE_CATEGORY_NB_R13 = "lte-rrc.ue_Category_NB_r13";
    protected final static String LTE_RRC_UE_CAP_INFO_NB_R13_ELEMENT = "lte-rrc.UE_Capability_NB_r13_element";
    protected final static String LTE_RRC_UE_RADIO_CAP_INFO_R13_TREE = "lte-rrc.ue_RadioAccessCapabilityInfo_r13_tree";
    protected final static String LTE_RRC_UE_RADIO_CAP_INFORMATION_R13_ELEMENT = "lte-rrc.ueRadioAccessCapabilityInformation_r13_element";
    // NB-IoT Strings
    protected final static String LTE_RRC_UE_RADIO_CAP_INFORMATION_NB = "lte-rrc.UERadioAccessCapabilityInformation_NB_element";
    protected final static String T3412_TIMER_VALUE = "gsm_a.gm.gmm.gprs_timer3_value";
    protected final static String T3412_TIMER_UNIT = "gsm_a.gm.gmm.gprs_timer3_unit";
    protected final static String T3412_TREE = "gsm_a.gm.gmm.gprs_timer3_tree";
    protected final static String GPRS_TIMER_T3412 = "GPRS Timer 3 - T3412 extended value";
    protected final static String T3324_TIMER_VALUE = "gsm_a.gm.gmm.gprs_timer2_value";
    protected final static String T3324_TIMER_UNIT = "gsm_a.gm.gmm.gprs_timer2_unit";
    protected final static String T3324_TREE = "gsm_a.gm.gmm.gprs_timer2_tree";
    protected final static String GPRS_TIMER_T3324 = "GPRS Timer 2 - T3324 value";
    protected final static String GSM_GMM_EDRX_VALUE = "gsm_a.gm.gmm.edrx_value";
    protected final static String GSM_GMM_PAGING_TIME_WINDOW = "gsm_a.gm.gmm.paging_time_window";
    protected final static String EXTENDED_DRX_PARAMETERS = "Extended DRX Parameters";
    protected final static String LTE_RRC_CE_MODEB_R13 = "lte-rrc.ce_ModeB_r13";
    protected final static String LTE_RRC_CE_MODEA_R13 = "lte-rrc.ce_ModeA_r13";
    protected final static String LTE_RRC_UE_RADIO_PAGING_INFO_R12_ELEMENT = "lte-rrc.UE_RadioPagingInfo_r12_element";
    protected final static String LTE_RRC_UE_RADIO_CAPABILITY_PAGING_R12_TREE = "lte-rrc.ue_RadioPagingInfo_r12_tree";
    protected final static String LTE_RRC_UE_RADIO_CAPABILITY_PAGING_INFORMATION_R12_ELEMENT = "lte-rrc.ueRadioPagingInformation_r12_element";
    protected final static String LTE_RRC_UE_RADIO_CAPABILITY_FOR_PAGING_INFO_ELEMENT = "lte-rrc.UERadioPagingInformation_element";
    protected final static String S1AP_UE_RADIO_CAPABILITY_FOR_PAGING_TREE = "s1ap.UERadioCapabilityForPaging_tree";
    protected final static String ITEM3_ID_RADIO_CAPABILITY = "Item 3: id-UERadioCapabilityForPaging";
    protected final static String NAS_EPS_ESM_PDN_TYPE = "nas_eps.esm_pdn_type";
    protected final static String NAS_EPS_TYPE_ID = "nas_eps.emm.type_of_id";
    protected final static String EPS_MOBILE_IDENTITY = "EPS mobile identity";
    protected final static String NAS_EPS_EMM_WO_PDN_CAP = "nas_eps.emm.er_wo_pdn_cap";
    // Cat M Strings
    protected final static String NAS_EPS_PNB_CIOT = "nas_eps.emm.pnb_ciot";
    protected final static String GSM_CAP_GEA7 = "gsm_a.gm.gmm.net_cap.gea7";
    protected final static String GSM_CAP_GEA6 = "gsm_a.gm.gmm.net_cap.gea6";
    protected final static String GSM_CAP_GEA5 = "gsm_a.gm.gmm.net_cap.gea5";
    protected final static String GSM_CAP_GEA4 = "gsm_a.gm.gmm.net_cap.gea4";
    protected final static String GSM_CAP_GEA3 = "gsm_a.gm.gmm.net_cap.gea3";
    protected final static String GSM_CAP_GEA2 = "gsm_a.gm.gmm.net_cap.gea2";
    protected final static String GSM_CAP_EXT_GEA_TREE = "gsm_a.gm.gmm.net_cap.ext_gea_bits_tree";
    // 2G Enc. Algorithms Support strings
    protected final static String GSM_CAP_GEA1 = "gsm_a.gm.gmm.net_cap.gea1";
    protected final static String GSM_A5_7 = "gsm_a.A5_7_algorithm_sup";
    protected final static String GSM_A5_6 = "gsm_a.A5_6_algorithm_sup";
    protected final static String GSM_A5_5 = "gsm_a.A5_5_algorithm_sup";
    protected final static String GSM_A5_4 = "gsm_a.A5_4_algorithm_sup";
    protected final static String GSM_CLASSMARK_A5_TREE = "gsm_a.classmark3.a5_bits_tree";
    protected final static String MOBILE_STATION_CLASSMARK_3 = "Mobile station classmark 3";
    protected final static String GSM_A5_3 = "gsm_a.A5_3_algorithm_sup";
    protected final static String GSM_A5_2 = "gsm_a.A5_2_algorithm_sup";
    protected final static String GSM_A5_1 = "gsm_a.A5_1_algorithm_sup";
    // A5 Algorithms
    protected final static String MOBILE_STATION_CLASSMARK_2 = "Mobile station classmark 2";
    protected final static String LTE_RRC_ROHC_PROFILE_104 = "lte-rrc.profile0x0104";
    protected final static String LTE_RRC_ROHC_PROFILE_103 = "lte-rrc.profile0x0103";
    protected final static String LTE_RRC_ROHC_PROFILE_102 = "lte-rrc.profile0x0102";
    protected final static String LTE_RRC_ROHC_PROFILE_101 = "lte-rrc.profile0x0101";
    protected final static String LTE_RRC_ROHC_PROFILE_6 = "lte-rrc.profile0x0006";
    protected final static String LTE_RRC_ROHC_PROFILE_4 = "lte-rrc.profile0x0004";
    protected final static String LTE_RRC_ROHC_PROFILE_3 = "lte-rrc.profile0x0003";
    protected final static String LTE_RRC_ROHC_PROFILE_2 = "lte-rrc.profile0x0002";
    protected final static String LTE_RRC_ROHC_PROFILE_1 = "lte-rrc.profile0x0001";
    protected final static String LTE_RRC_SUPPORTED_ROHC_PROFILES_ELEMENT = "lte-rrc.supportedROHC_Profiles_element";
    protected final static String LTE_RRC_ROHC_PROFILE_104_r15 = "lte-rrc.profile0x0104_r15";
    protected final static String LTE_RRC_ROHC_PROFILE_103_r15 = "lte-rrc.profile0x0103_r15";
    protected final static String LTE_RRC_ROHC_PROFILE_102_r15 = "lte-rrc.profile0x0102_r15";
    protected final static String LTE_RRC_ROHC_PROFILE_101_r15 = "lte-rrc.profile0x0101_r15";
    protected final static String LTE_RRC_ROHC_PROFILE_6_r15 = "lte-rrc.profile0x0006_r15";
    protected final static String LTE_RRC_ROHC_PROFILE_4_r15 = "lte-rrc.profile0x0004_r15";
    protected final static String LTE_RRC_ROHC_PROFILE_3_r15 = "lte-rrc.profile0x0003_r15";
    protected final static String LTE_RRC_ROHC_PROFILE_2_r15 = "lte-rrc.profile0x0002_r15";
    protected final static String LTE_RRC_ROHC_PROFILE_1_r15 = "lte-rrc.profile0x0001_r15";
    // Rohc profiles
    protected final static String LTE_RRC_PARAMETER_PDCP_ELEMENT = "lte-rrc.pdcp_Parameters_element";
    protected final static String NAS_EPS_UIA7 = "nas_eps.emm.uia7";
    protected final static String NAS_EPS_UIA6 = "nas_eps.emm.uia6";
    protected final static String NAS_EPS_UIA5 = "nas_eps.emm.uia5";
    protected final static String NAS_EPS_UIA4 = "nas_eps.emm.uia4";
    protected final static String NAS_EPS_UIA3 = "nas_eps.emm.uia3";
    protected final static String NAS_EPS_UIA2 = "nas_eps.emm.uia2";
    protected final static String NAS_EPS_UIA1 = "nas_eps.emm.uia1";
    protected final static String NAS_EPS_EIA7 = "nas_eps.emm.eia7";
    protected final static String NAS_EPS_EIA6 = "nas_eps.emm.eia6";
    protected final static String NAS_EPS_EIA5 = "nas_eps.emm.eia5";
    protected final static String NAS_EPS_EIA4 = "nas_eps.emm.eia4";
    protected final static String NAS_EPS_EIA3 = "nas_eps.emm.eia3";
    protected final static String NAS_EPS_128EIA2 = "nas_eps.emm.128eia2";
    protected final static String NAS_EPS_128EIA1 = "nas_eps.emm.128eia1";
    // INTEGRITY
    protected final static String NAS_EPS_EIA0 = "nas_eps.emm.eia0";
    protected final static String NAS_EPS_UEA7 = "nas_eps.emm.uea7";
    protected final static String NAS_EPS_UEA6 = "nas_eps.emm.uea6";
    protected final static String NAS_EPS_UEA5 = "nas_eps.emm.uea5";
    protected final static String NAS_EPS_UEA4 = "nas_eps.emm.uea4";
    protected final static String NAS_EPS_UEA3 = "nas_eps.emm.uea3";
    protected final static String NAS_EPS_UEA2 = "nas_eps.emm.uea2";
    protected final static String NAS_EPS_UEA1 = "nas_eps.emm.uea1";
    protected final static String NAS_EPS_UEA0 = "nas_eps.emm.uea0";
    protected final static String NAS_EPS_EEA7 = "nas_eps.emm.eea7";
    protected final static String NAS_EPS_EEA6 = "nas_eps.emm.eea6";
    protected final static String NAS_EPS_EEA5 = "nas_eps.emm.eea5";
    protected final static String NAS_EPS_EEA4 = "nas_eps.emm.eea4";
    protected final static String NAS_EPS_EEA3 = "nas_eps.emm.eea3";
    protected final static String NAS_EPS_128EEA2 = "nas_eps.emm.128eea2";
    protected final static String NAS_EPS_128EEA1 = "nas_eps.emm.128eea1";
    protected final static String NAS_EPS_EEA0 = "nas_eps.emm.eea0";
    // CIPHERING ALGORITHMS in attach request
    protected final static String UE_NETWORK_CAPABILITY = "UE network capability";
    protected final static String RRC_FOURLAYERTM34_R10 = "lte-rrc.fourLayerTM3_TM4_r10";
    protected final static String RRC_CA_MIMO_PARAMETERSDL_V10I0_ELEMENT = "lte-rrc.CA_MIMO_ParametersDL_v10i0_element";
    protected final static String RRC_BAND_PARAMETERSDL_V10I0_TREE = "lte-rrc.bandParametersDL_v10i0_tree";
    protected final static String RRC_BAND_PARAMETERSUL_V10I0_TREE = "lte-rrc.bandParametersUL_v10i0_tree";
    protected final static String RRC_BAND_PARAMETERSUL_V10I0 = "lte-rrc.bandParametersUL_v10i0";
    protected final static String RRC_BAND_PARAMETERS_V10I0_ELEMENT = "lte-rrc.BandParameters_v10i0_element";
    protected final static String RRC_BAND_PARAMETERS_LIST_V10I0 = "lte-rrc.bandParameterList_v10i0";
    protected final static String RRC_BAND_PARAMETERS_LIST_V10I0_TREE = "lte-rrc.bandParameterList_v10i0_tree";//
    protected final static String RRC_BAND_COMBINATION_PARAMETERS_V10I0_ELEMENT = "lte-rrc.BandCombinationParameters_v10i0_element";
    protected final static String RRC_SUPPORTED_BAND_COMBINATION_V10I0 = "lte-rrc.supportedBandCombination_v10i0";
    protected final static String RRC_SUPPORTED_BAND_COMBINATION_V10I0_TREE = "lte-rrc.supportedBandCombination_v10i0_tree";
    // TM34 LTEBand Combination Parser
    protected final static String RRC_RF_PARAMETERS_V10I0 = "lte-rrc.rf_Parameters_v10i0_element";
    protected final static String LTE_RRC_SUPPORTED_BAND_UTRA_FDD = "lte-rrc.SupportedBandUTRA_FDD";
    protected final static String LTE_RRC_SUPPORTED_BAND_LIST_UTRA_TREE = "lte-rrc.supportedBandListUTRA_FDD_tree";
    protected final static String LTE_RRC_SUPPORTED_BAND_LIST_UTRA = "lte-rrc.supportedBandListUTRA_FDD";
    // addr11 band combination parser
    protected final static  String LTE_RRC_SUPPORTED_BAND_COMBINATION_ADD_R11 = "lte-rrc.supportedBandCombinationAdd_r11";
    protected final static String LTE_RRC_SUPPORTED_BAND_COMBINATION_ADD_R11_TREE = "lte-rrc.supportedBandCombinationAdd_r11_tree";
    protected final static String LTE_RRC_BANDCOMBINATIONPARAMETERS_R11_ELEMENT = "lte-rrc.BandCombinationParameters_r11_element";
    protected final static String LTE_RRC_BANDPARAMETERLIST_R11 = "lte-rrc.bandParameterList_r11";
    protected final static String LTE_RRC_BANDPARAMETERLIST_R11_TREE = "lte-rrc.bandParameterList_r11_tree";
    protected final static String LTE_RRC_BANDPARAMETERS_R11_ELEMENT = "lte-rrc.BandParameters_r11_element";
    protected final static String LTE_RRC_BAND_EUTRA_R11 = "lte-rrc.bandEUTRA_r11";
    protected final static String LTE_RRC_BAND_PARAMETERS_UL_R11_TREE = "lte-rrc.bandParametersUL_r11_tree";
    protected final static String LTE_RRC_BAND_PARAMETERS_DL_R11_TREE = "lte-rrc.bandParametersDL_r11_tree";
    protected final static String LTE_RRC_BAND_PARAMETERS_DL_R11= "lte-rrc.bandParametersDL_r11";
    protected final static String LTE_RRC_BAND_PARAMETERS_UL_R11 = "lte-rrc.bandParametersUL_r11";
    protected final static String LTE_RRC_SUPPORTED_BANDWIDTH_COMBINATION_SET_R11 = "lte-rrc.supportedBandwidthCombinationSet_r11";
    protected final static String LTE_RRC_RF_PARAMETERS_V11D0_ELEMENT = "lte-rrc.rf_Parameters_v11d0_element";
    protected final static String LTE_RRC_SUPPORTED_BAND_COMBINATION_ADD_V11D0 = "lte-rrc.supportedBandCombinationAdd_v11d0";
    protected final static String LTE_RRC_SUPPORTED_BANDCOMBINATION_ADD_V11D0_TREE = "lte-rrc.supportedBandCombinationAdd_v11d0_tree";

    // 3G Supported bands
    protected final static String LTE_RRC_UTRA_FDD_ELEMENT = "lte-rrc.utraFDD_element";
    protected final static String LTE_RRC_SUPPORTED_BAND_GERAN = "lte-rrc.SupportedBandGERAN";
    protected final static String LTE_RRC_SUPPORTED_BAND_LIST_GERAN_TREE = "lte-rrc.supportedBandListGERAN_tree";
    protected final static String LTE_RRC_SUPPORTED_BAND_LIST_GERAN = "lte-rrc.supportedBandListGERAN";
    protected final static String LTE_RRC_GERAN_ELEMENT = "lte-rrc.geran_element";
    // 2G Supported bands
    protected final static String LTE_RRC_INTER_RAT_ELEMENT = "lte-rrc.interRAT_Parameters_element";
    protected final static String LTE_RRC_FREQBANDINDICATOR_R11 = "lte-rrc.FreqBandIndicator_r11";
    protected final static String LTE_RRC_REQUESTED_BANDS_TREE = "lte-rrc.requestedBands_r11_tree";
    protected final static String LTE_RRC_REQUESTED_BANDS_R11 = "lte-rrc.requestedBands_r11";
    protected final static String LTE_RRC_RF_PARAMETERS_1180_ELEMENT = "lte-rrc.rf_Parameters_v1180_element";
    protected final static String LTE_RRC_RF_PARAMETERS_1430_ELEMENT = "lte-rrc.rf_Parameters_v1430_element";
    protected final static String LTE_RRC_SUPPORTEDBANDCOMBINATIONS_1430 = "lte-rrc.supportedBandCombination_v1430";
    protected final static String LTE_RRC_SUPPORTEDBANDCOMBINATIONADD_v1430 = "lte-rrc.supportedBandCombinationAdd_v1430";
    protected final static String LTE_RRC_SUPPORTEDBANDCOMBINATIONS_v1430_TREE = "lte-rrc.supportedBandCombination_v1430_tree";
    protected final static String LTE_RRC_BANDPARAMETERLIST_v1430 = "lte-rrc.bandParameterList_v1430";
    protected final static String LTE_RRC_BANDCOMBINATIONPARAMETERS_v1430_ELEMENT = "lte-rrc.BandCombinationParameters_v1430_element";
    protected final static String LTE_RRC_BANDPARAMETERLIST_v1430_TREE = "lte-rrc.bandParameterList_v1430_tree";
    protected final static String LTE_RRC_BANDPARAMETERS_v1430_ELEMENT = "lte-rrc.BandParameters_v1430_element";
    protected final static String LTE_RRC_UL_256_QAM = "lte-rrc.ul_256QAM_r14";
    protected final static String RRC_SUPPORTED_BANDWIDTH_COMBINATIONSET_R10 = "lte-rrc.supportedBandwidthCombinationSet_r10";
    protected final static String PER_OPTIONAL_FIELD_BIT = "per.optional_field_bit";
    protected final static String RRC_BAND_COMBINATION_PARAMETERS_EXT_R10_ELEMENT = "lte-rrc.BandCombinationParametersExt_r10_element";
    protected final static String RRC_SUPPORTED_BAND_COMBINATION_EXT_R10_TREE = "lte-rrc.supportedBandCombinationExt_r10_tree";
    protected final static String RRC_SUPPORTED_BAND_COMBINATION_R10 = "lte-rrc.supportedBandCombinationExt_r10";
    protected final static String RRC_RF_PARAMETERS_V1060 = "lte-rrc.rf_Parameters_v1060_element";
    protected final static String ITEM2_ID_UE_RADIO_CAPABILITY = "Item 2: id-UERadioCapability";
    protected final static String S1AP_UE_CAP_INFO_INDICATION_ELEMENT = "s1ap.UECapabilityInfoIndication_element";
    protected final static String RRC_UE_CATEGORY = "lte-rrc.ue_Category";
    protected final static String RRC_UE_CATEGORY_DL_v1310 = "lte-rrc.ue_CategoryDL_v1310";
    protected final static String LTE_RRC_SKIPFALLBACKCOMBINATIONS_R13 = "lte-rrc.skipFallbackCombinations_r13";
    protected final static String LTE_RRC_SKIPFALLBACKCOMBREQUESTED_R13 = "lte-rrc.skipFallbackCombRequested_r13";
    protected final static String LTE_RRC_RFPARAMETERS_V1310_ELEMENT = "lte-rrc.rf_Parameters_v1310_element";
    protected final static String LTE_RRC_MAXIMUMCCS_RETRIEVAL = "lte-rrc.maximumCCsRetrieval_r13";
    protected final static String LTE_RRC_REQUESTEDCCSDL_R13 = "lte-rrc.requestedCCsDL_r13";
    protected final static String LTE_RRC_REQUESTEDCCSUL_R13 = "lte-rrc.requestedCCsUL_r13";
    protected final static String LTE_RRC_ENB_REQPARAMETERS_R13_ELEMENT = "lte-rrc.eNB_RequestedParameters_r13_element";
    protected final static String LTE_RRC_SUPPORTEDBANDCOMBREDUCED_R13 = "lte-rrc.supportedBandCombinationReduced_r13";
    protected final static String LTE_RRC_SUPPORTEDBANDCOMBREDUCED_R13_TREE = "lte-rrc.supportedBandCombinationReduced_r13_tree";
    protected final static String LTE_RRC_BAND_PARAMETERS_UL_R13_ELEMENT = "lte-rrc.bandParametersUL_r13_element";
    protected final static String LTE_RRC_BAND_PARAMETERS_DL_R13_ELEMENT = "lte-rrc.bandParametersDL_r13_element";
    protected final static String RRC_UE_CATEGORY_DL_v1350 = "lte-rrc.ue_CategoryDL_v1350";
    protected final static String RRC_UE_CATEGORY_UL_v1350= "lte-rrc.ue_CategoryUL_v1350";
    protected final static String RRC_UE_CATEGORY_UL_V1430 = "lte-rrc.ue_CategoryUL_v1430";
    protected final static String RRC_FGI_TREE = "lte-rrc.featureGroupIndicators_tree";
    protected final static String RRC_SUPPORTED_MIMO_CAPABILITY_ul_REL10 = "lte-rrc.supportedMIMO_CapabilityUL_r10";
    protected final static String RRC_SUPPORTED_MIMO_CAPABILITY_dl_REL10 = "lte-rrc.supportedMIMO_CapabilityDL_r10";
    protected final static String RRC_BANDWIDTH_CLASS_DL_R10 = "lte-rrc.ca_BandwidthClassDL_r10";
    protected final static String RRC_BANDWIDTH_CLASS_UL_R10 = "lte-rrc.ca_BandwidthClassUL_r10";
    protected final static String RRC_BAND_EUTRA = "lte-rrc.bandEUTRA";
    protected final static String RRC_BANDEUTRA_R10 = "lte-rrc.bandEUTRA_r10";
    protected final static String RRC_BAND_MIMO_PARAMETERSDL_R10_ELEMENT_DL = "lte-rrc.CA_MIMO_ParametersDL_r10_element";
    protected final static String RRC_BAND_MIMO_PARAMETERSUL_R10_ELEMENT_UL = "lte-rrc.CA_MIMO_ParametersUL_r10_element";
    protected final static String RRC_BAND_PARAMETERS_DL_R10_TREE = "lte-rrc.bandParametersDL_r10_tree";
    protected final static String RRC_BAND_PARAMETERS_UL_R10_TREE = "lte-rrc.bandParametersUL_r10_tree";
    protected final static String RRC_BAND_PARAMETERS_R10_ELEMENT = "lte-rrc.BandParameters_r10_element";
    protected final static String RRC_NON_CRITICAL_EXTENSION_ELEMENT = "lte-rrc.nonCriticalExtension_element";
    protected final static String RRC_CA_TREE = "lte-rrc.c1_tree";
    protected final static String RRC_LATE_NONCRITICALEXTENSION_TREE = "lte-rrc.lateNonCriticalExtension_tree";
    protected final static String LTE_RRC_FREQBAND_RETRIEVAL_R11 = "lte-rrc.freqBandRetrieval_r11";
    // UE capabilities
    protected final static String CRITICAL_EXTENSIONS_TREE = "lte-rrc.criticalExtensions_tree";
    protected final static String LTE_RRC_FGI_REL10_V1060_TREE = "lte-rrc.featureGroupIndRel10_v1060_tree";
    protected final static String LTE_RRC_TDD_ADD_EUTRA_CAP_1060 = "lte-rrc.tdd_Add_UE_EUTRA_Capabilities_v1060_element";
    // Rel 10
    protected final static String LTE_RRC_FDD_ADD_EUTRA_CAP_1060 = "lte-rrc.fdd_Add_UE_EUTRA_Capabilities_v1060_element";
    protected final static String LTE_RRC_TDD_EUTRA_CAP_R9_ELEMENT = "lte-rrc.tdd_Add_UE_EUTRA_Capabilities_r9_element";
    protected final static String LTE_RRC_FGI_R9_TREE = "lte-rrc.featureGroupIndicators_r9_tree";
    // TDD FDD FGIs
    protected final static String LTE_RRC_FDD_EUTRA_CAP_R9_ELEMENT = "lte-rrc.fdd_Add_UE_EUTRA_Capabilities_r9_element";
    protected final static String RRC_BAND_COMBINATION_PARAMETERS_R10TREE = "lte-rrc.BandCombinationParameters_r10_tree";
    protected final static String RRC_BAND_COMBINATION_PARAMETERS_R10 = "lte-rrc.BandCombinationParameters_r10";
    protected final static String RRC_SUPPORTED_BAND_COMBINATION_NUMBER = "lte-rrc.supportedBandCombination_r10";
    protected final static String RRC_SUPPORTED_BAND_COMBINATION_TREE = "lte-rrc.supportedBandCombination_r10_tree";
    protected final static String RRC_BAND_COMBINATION_PARAMETERS_R13_ELEMENT = "lte-rrc.BandCombinationParameters_r13_element";
    protected final static String LTE_RRC_DIFFERENTFALLBACK_SUPPORTED_R13= "lte-rrc.differentFallbackSupported_r13";
    protected final static String LTE_RRC_BAND_PARAMETERS_R13_TREE= "lte-rrc.bandParameterList_r13_tree";
    protected final static String LTE_RRC_BAND_PARAMETERS_LIST_R13 = "lte-rrc.bandParameterList_r13";
    protected final static String LTE_RRC_BANDPARAMETERS_R13_ELEMENT= "lte-rrc.BandParameters_r13_element";
    protected final static String LTE_RRC_BANDEUTRA_R13 = "lte-rrc.bandEUTRA_r13";
    protected final static String LTE_RRC_CA_BANDWIDTHCLASS_DL_R13= "lte-rrc.ca_BandwidthClassDL_r13";
    protected final static String LTE_RRC_SUPPORTEDMIMO_CAPABILITYDL_R13= "lte-rrc.supportedMIMO_CapabilityDL_r13";
    protected final static String RRC_RF_PARAMETERS_V1020 = "lte-rrc.rf_Parameters_v1020_element";
    protected final static String RRC_RF_PARAMETERS_V1430 = "lte-rrc.rf_Parameters_v1430_element";
    protected final static String RRC_RFPARAMETERS_V1250_ELEMENT = "lte-rrc.rf_Parameters_v1250_element";
    protected final static String RRC_CAP_FEAT_GROUP_IND = "lte-rrc.eutra_cap_feat_group_ind_";
    protected final static String RRC_FGI_REL10_TREE = "lte-rrc.featureGroupIndRel10_r10_tree";
    protected final static String RRC_FGI_ADD_REL9_TREE = "lte-rrc.featureGroupIndRel9Add_r9_tree";
    protected final static String RRC_UECAPABILITY_V9A0_ELEMENT = "lte-rrc.UE_EUTRA_Capability_v9a0_IEs_element";
    protected final static String RRC_UECAPABILITY_ELEMENT = "lte-rrc.UE_EUTRA_Capability_element";
    protected final static String RRC_UECAPABILITY_ELEMENT_NR = "nr-rrc.UE_NR_Capability_element";
    protected final static String RRC_UECAPABILITY_ELEMENT_MRDC = "nr-rrc.UE_MRDC_Capability_element";
    protected final static String NR_RRC_UECAPABILITY_ELEMENT = "nr-rrc.UE_NR_Capability_element";
    protected final static String RRC_UECAPABILITY_RAT_CONTAINER_TREE = "lte-rrc.ueCapabilityRAT_Container_tree";
    protected final static String RRC_UECAPABILITY_RAT_CONTAINER_ELEMENT = "lte-rrc.UE_CapabilityRAT_Container_element";
    protected final static String NR_RRC_UE_MRDC_CAPABILITY_ELEMENT = "nr-rrc.UE_MRDC_Capability_element";
    protected final static String NR_RRC_GENERALPARAMENTERSMRDC_ELEMENT = "nr-rrc.generalParametersMRDC_element";
    protected final static String NR_RRC_SRB3 = "nr-rrc.srb3";
    protected final static String NR_RRC_SPLITDRB_WITH_UL_BOTH_MCG_SCG = "nr-rrc.splitDRB_withUL_Both_MCG_SCG";
    protected final static String NR_RRC_RF_PARAMETERSMRDC_ELEMENT = "nr-rrc.rf_ParametersMRDC_element";
    protected final static String NR_RRC_SUPPORTEDBANDCOMBINATIONLIST = "nr-rrc.supportedBandCombinationList";
    protected final static String NR_RRC_SUPPORTEDBANDCOMBINATIONLIST_TREE = "nr-rrc.supportedBandCombinationList_tree";
    protected final static String NR_RRC_APPLIED_FREQBANDLIST_FILTER = "nr-rrc.appliedFreqBandListFilter";
    protected final static String NR_RRC_APPLIED_FREQBANDLIST_FILTER_TREE = "nr-rrc.appliedFreqBandListFilter_tree";
    protected final static String NR_RRC_FREQBAND_INFORMATION = "nr-rrc.FreqBandInformation";
    protected final static String NR_RRC_BANDINFORMATION_EUTRA_ELEMENT = "nr-rrc.bandInformationEUTRA_element";
    protected final static String NR_RRC_BANDINFORMATION_NR_ELEMENT = "nr-rrc.bandInformationNR_element";
    protected final static String NR_RRC_FREQBANDINFO_TREE = "nr-rrc.FreqBandInformation_tree";
    protected final static String NR_RRC_BANDCOMBINATION_ELEMENT = "nr-rrc.BandCombination_element";
    protected final static String NR_RRC_FEATURESETCOMBINATION = "nr-rrc.featureSetCombination";
    protected final static String NR_RRC_BANDLIST = "nr-rrc.bandList";
    protected final static String NR_RRC_BANDLIST_TREE = "nr-rrc.bandList_tree";
    protected final static String NR_RRC_BANDPARAMETERS = "nr-rrc.BandParameters";
    protected final static String NR_RRC_BANDPARAMETERS_TREE = "nr-rrc.BandParameters_tree";
    protected final static String NR_RRC_EUTRA_ELEMENT = "nr-rrc.eutra_element";
    protected final static String NR_RRC_BANDEUTRA = "nr-rrc.bandEUTRA";
    protected final static String NR_RRC_BANDWITHCLASSDL_EUTRA = "nr-rrc.ca_BandwidthClassDL_EUTRA";
    protected final static String NR_RRC_BANDWITHCLASSUL_EUTRA = "nr-rrc.ca_BandwidthClassUL_EUTRA";
    protected final static String NR_RRC_NR_ELEMENT = "nr-rrc.nr_element";
    protected final static String NR_RRC_BANDWITHCLASSDL_NR = "nr-rrc.ca_BandwidthClassDL_NR";
    protected final static String NR_RRC_BANDWITHCLASSUL_NR = "nr-rrc.ca_BandwidthClassUL_NR";
    protected final static String NR_RRC_FEATURESETCOMBINATIONS_TREE = "nr-rrc.featureSetCombinations_tree";
    protected final static String NR_RRC_FEATURESETSPERBAND = "nr-rrc.FeatureSetsPerBand";
    protected final static String NR_RRC_FEATURESETCOMBINATION_TREE = "nr-rrc.FeatureSetCombination_tree";
    protected final static String NR_RRC_FEATURESETSPERBAND_TREE = "nr-rrc.FeatureSetsPerBand_tree";
    protected final static String NR_RRC_FEATURESET_TREE = "nr-rrc.FeatureSet_tree";
    protected final static String NR_RRC_DOWNLINKSETEUTRA = "nr-rrc.downlinkSetEUTRA";
    protected final static String NR_RRC_UPLINKSETEUTRA = "nr-rrc.uplinkSetEUTRA";
    protected final static String NR_RRC_DOWNLINKSETNR = "nr-rrc.downlinkSetNR";
    protected final static String NR_RRC_UPLINKSETNR = "nr-rrc.uplinkSetNR";
    protected final static String NR_RRC_FEATURESETS_ELEMENT = "nr-rrc.featureSets_element";
    protected final static String NR_RRC_FEATURESETSDOWNLINK_TREE = "nr-rrc.featureSetsDownlink_tree";
    protected final static String NR_RRC_FEATURESETDOWNLINK_ELEMENT = "nr-rrc.FeatureSetDownlink_element";
    protected final static String NR_RRC_FEATURESETLISTPERDOWNLINKCC_TREE = "nr-rrc.featureSetListPerDownlinkCC_tree";
    protected final static String NR_RRC_FEATURESETDOWNLINKPERCC_ID = "nr-rrc.FeatureSetDownlinkPerCC_Id";
    protected final static String NR_RRC_FEATURESETSDOWNLINKPERCC_TREE = "nr-rrc.featureSetsDownlinkPerCC_tree";
    protected final static String NR_RRC_FEATURESETDOWNLINKPERCC_ELEMENT = "nr-rrc.FeatureSetDownlinkPerCC_element";
    protected final static String NR_RRC_SUPPORTEDSUBCARRIERSPACINGDL = "nr-rrc.supportedSubcarrierSpacingDL";
    protected final static String NR_RRC_SUPPORTEDBANDWIDTHDL = "nr-rrc.supportedBandwidthDL";
    protected final static String NR_RRC_SUPPORTEDBANDWIDTHDL_TREE = "nr-rrc.supportedBandwidthDL_tree";
    protected final static String NR_RRC_FEATURESETSUPLINK_TREE = "nr-rrc.featureSetsUplink_tree";
    protected final static String NR_RRC_FEATURESETUPLINK_ELEMENT = "nr-rrc.FeatureSetUplink_element";
    protected final static String NR_RRC_FEATURESETLISTPERUPLINKCC_TREE = "nr-rrc.featureSetListPerUplinkCC_tree";
    protected final static String NR_RRC_FEATURESETUPLINKPERCC_ID = "nr-rrc.FeatureSetUplinkPerCC_Id";
    protected final static String NR_RRC_FEATURESETSUPLINKPERCC_TREE = "nr-rrc.featureSetsUplinkPerCC_tree";
    protected final static String NR_RRC_FEATURESETUPLINKPERCC_ELEMENT = "nr-rrc.FeatureSetUplinkPerCC_element";
    protected final static String NR_RRC_SUPPORTEDSUBCARRIERSPACINGUL = "nr-rrc.supportedSubcarrierSpacingUL";
    protected final static String NR_RRC_SUPPORTEDBANDWIDTHUL = "nr-rrc.supportedBandwidthUL";
    protected final static String NR_RRC_SUPPORTEDBANDWIDTHUL_TREE = "nr-rrc.supportedBandwidthUL_tree";
    protected final static String NR_RRC_FR_N = "nr-rrc.fr";
    protected final static String NR_RRC_MAXNUMBERMIMO_LAYERSPDSCH = "nr-rrc.maxNumberMIMO_LayersPDSCH";
    protected final static String NR_RRC_CHANNELBW_90MHZ = "nr-rrc.channelBW_90mhz";
    protected final static String NR_RRC_CHANNELBWS_DL_v1530_TREE = "nr-rrc.channelBWs_DL_tree";
    protected final static String NR_RRC_CHANNELBWS_UL_v1530_TREE = "nr-rrc.channelBWs_UL_tree";
    protected final static String NR_RRC_FR1_ELEMENT = "nr-rrc.fr1_element";
    protected final static String NR_RRC_SCS_15KHZ = "nr-rrc.scs_15kHz";
    protected final static String NR_RRC_SCS_30KHZ = "nr-rrc.scs_30kHz";
    protected final static String NR_RRC_SCS_60KHZ = "nr-rrc.scs_60kHz";

    protected final static String NR_RRC_SUPPORTEDMODULATIONORDERDL = "nr-rrc.supportedModulationOrderDL";
    protected final static String NR_RRC_SUPPORTEDMODULATIONORDERUL = "nr-rrc.supportedModulationOrderUL";
    protected final static String NR_RRC_MIMO_CB_PUSCH_ELEMENT = "nr-rrc.mimo_CB_PUSCH_element";
    protected final static String NR_RRC_MAXNUMBERMIMO_LAYERSCB_PUSCH = "nr-rrc.maxNumberMIMO_LayersCB_PUSCH";
    protected final static String LTE_RRC_UE_CAPABILITYRAT_CONTAINER_ELEMENT = "lte-rrc.UE_CapabilityRAT_Container_element";
    protected final static String LTE_RRC_UECAPABILITYRAT_CONTAINER_TREE = "lte-rrc.ueCapabilityRAT_Container_tree";
    protected final static String LTE_RRC_UE_EUTRA_CAPABILITY_ELEMENT = "lte-rrc.UE_EUTRA_Capability_element";
    protected final static String LTE_RRC_NONCRITICALEXTENSION_ELEMENT = "lte-rrc.nonCriticalExtension_element";
    protected final static String LTE_RRC_FEATURESETSEUTRA_R15_ELEMENT = "lte-rrc.featureSetsEUTRA_r15_element";
    protected final static String LTE_RRC_FEATURESETSDL_R15_TREE = "lte-rrc.featureSetsDL_r15_tree";
    protected final static String LTE_RRC_FEATURESETSDL_R15_ELEMENT = "lte-rrc.FeatureSetDL_r15_element";
    protected final static String LTE_RRC_FEATURESETPERCC_LISTDL_R15_TREE = "lte-rrc.featureSetPerCC_ListDL_r15_tree";
    protected final static String LTE_RRC_FEATURESETDL_PERCC_ID_R15 = "lte-rrc.FeatureSetDL_PerCC_Id_r15";
    protected final static String LTE_RRC_FEATURESETSDL_PERCC_R15_TREE = "lte-rrc.featureSetsDL_PerCC_r15_tree";
    protected final static String LTE_RRC_FEATURESETSDL_PERCC_R15_ELEMENT = "lte-rrc.FeatureSetDL_PerCC_r15_element";
    protected final static String LTE_RRC_SUPPORTEDMIMO_CAPABILITYDL_MRDC_R15 = "lte-rrc.supportedMIMO_CapabilityDL_MRDC_r15";
    protected final static String LTE_RRC_FEATURESETSUL_R15_TREE = "lte-rrc.featureSetsUL_r15_tree";
    protected final static String LTE_RRC_FEATURESETSUL_R15_ELEMENT = "lte-rrc.FeatureSetUL_r15_element";
    protected final static String LTE_RRC_FEATURESETPERCC_LISTUL_R15_TREE = "lte-rrc.featureSetPerCC_ListUL_r15_tree";
    protected final static String LTE_RRC_FEATURESETUL_PERCC_ID_R15 = "lte-rrc.FeatureSetUL_PerCC_Id_r15";
    protected final static String LTE_RRC_FEATURESETSUL_PERCC_R15_TREE = "lte-rrc.featureSetsUL_PerCC_r15_tree";
    protected final static String LTE_RRC_FEATURESETSUL_PERCC_R15_ELEMENT = "lte-rrc.FeatureSetUL_PerCC_r15_element";
    protected final static String LTE_RRC_SUPPORTEDMIMO_CAPABILITYUL_MRDC_R15 = "lte-rrc.supportedMIMO_CapabilityUL_MRDC_r15";
    protected final static String ITEM_0 = "Item 0";
    protected final static String ITEM_N = "Item ";
    protected final static String NR_RRC_RF_PARAMETERS_ELEMENT = "nr-rrc.rf_Parameters_element";
    protected final static String NR_RRC_SUPPORTEDBANDLISTNR_TREE = "nr-rrc.supportedBandListNR_tree";
    protected final static String NR_RRC_BANDNR_ELEMENT = "nr-rrc.BandNR_element";
    protected final static String S1AP_UECAPABILITY_CONTAINERlIST_TREE = "lte-rrc.ue_CapabilityRAT_ContainerList_tree";
    protected final static String S1AP_UECAPABILITYINFORMATION_R8_ELEMENT = "lte-rrc.ueCapabilityInformation_r8_element";
    protected final static String S1AP_UECAPABILITY_INFORMATION_ELEMENT = "lte-rrc.UECapabilityInformation_element";
    protected final static String S1AP_UERADIOCAPABILITY_INFOTREE = "lte-rrc.uERadioAccessCapabilityInformation_r8_IEs.ue_RadioAccessCapabilityInfo_tree";
    protected final static String S1AP_LTE_R8_UERADIOACCESSCAPABILITY_INFORMATION_ELEMENT = "lte-rrc.ueRadioAccessCapabilityInformation_r8_element";
    protected final static String S1AP_LTE_UERADIOACCESSCAPABILITY_INFORMATION_ELEMENT = "lte-rrc.UERadioAccessCapabilityInformation_element";
    protected final static String S1AP_UERADIOCAPABILITY_TREE = "s1ap.UERadioCapability_tree";
    protected final static String S1AP_VALUE_ELEMENT = "s1ap.value_element";
    protected final static String SGSAP_IMEISV = "sgsap.imeisv";
    protected final static String SGSAP_MSG_TYPE = "sgsap.msg_type";
    protected final static String SGSAP = "sgsap";
    protected final static String S1AP = "s1ap";
    protected final static String FRAME_NUMBER = "frame.number";
    protected final static String FRAME = "frame";
    protected final static String LAYERS = "layers";
    // KEY STRINGS IN JSON FILE:
    // Common:
    protected final static String SOURCE = "_source";
    protected final static String UE_USAGE_SETTING = "gsm_a.gm.gmm.ue_usage_setting";
    protected final static String NAS_EPS_MESSAGE_TYPE = "nas_eps.nas_msg_emm_type";
    protected final static String GSM_GMM_VOICE_DOMAIN_PREFERENCE_EUTRAN = "gsm_a.gm.gmm.voice_domain_pref_for_eutran";
    protected final static String VOICE_DOMAIN_PREFERENCE_UE_USAGE_SETTING = "Voice Domain Preference and UE's Usage Setting";
    protected final static String NAS_EPS_ESM_EIT = "nas_eps.esm.eit";
    protected final static String NAS_EPS_EMM_EIT = "nas_eps.emm.eit";
    protected final static String ESM_INFORMATION_TRANSFER_FLAG = "ESM information transfer flag";
    protected final static String NAS_EPS_EMM_ESM_MSG_CONT_TREE = "nas_eps.emm.esm_msg_cont_tree";
    protected final static String ESM_MESSAGE_CONTAINER = "ESM message container";
    public final static String PROTOCOL_OPTIONS_CONFIGURATION = "Protocol Configuration Options";
    public final static String PCO_PID = "gsm_a.gm.sm.pco_pid";
    public final static String PCO_LENGTH="gsm_a.gm.sm.pco.length";
    public final static String PCO_PID_TREE = "gsm_a.gm.sm.pco_pid_tree";
    public final static String PCO_PID_PS_DATAOFF = "gsm_a.gm.sm.pco.3gpp_data_off_ue_status";
    protected final static String NAS_EPS = "nas-eps";
    protected final static String S1AP_INITIAL_UE_MESSAGE_ELEMENT = "s1ap.InitialUEMessage_element";

    //attachAccept
    protected final static String INITIAL_CONTEXT_SETUP_REQ = "s1ap.InitialContextSetupRequest_element";

    protected final static String ITEM0_ID_ERAB_TOBESETUPLIST = "Item 0: id-E-RABToBeSetupItemCtxtSUReq";

    protected final static String ITEM3_ID_ERAB_TOBESETUPLIST = "Item 3: id-E-RABToBeSetupListCtxtSUReq";

    protected final static String ERAB_TOBESETUP_CTXTLISTTREE= "s1ap.E_RABToBeSetupListCtxtSUReq_tree";

    protected final static String PROTOCOL_LTE_SINGLE_CONTAINER_ELEMENT = "s1ap.ProtocolIE_SingleContainer_element";

    protected final static String ERAB_TOBESETUPITEM_CTXTSUREQ_ELEMENT= "s1ap.E_RABToBeSetupItemCtxtSUReq_element";

    protected final static String ERAB_ID = "s1ap.e_RAB_ID";

    protected final static String ERAB_LEVEL_QOS_PARAMETERS_ELEMENT = "s1ap.e_RABlevelQoSParameters_element";

    protected final static String QCI = "s1ap.qCI";

    protected final static String TAI_LIST = "Tracking area identity list - TAI list";

    protected final static String TAC  = "nas_eps.emm.tai_tac";

    protected final static String APN = "Access Point Name";

    protected final static String GSM_A_GM_SM_APN= "gsm_a.gm.sm.apn";

    protected final static String EPS_NW_FEATURE_SUPPORT = "EPS network feature support";

    protected final static String EMERGENCY_BEARER_SERVICES_S1_MODE = "nas_eps.emm.emc_bs";

    protected final static String IMS_OVER_PS_SESSION_S1_MODE = "nas_eps.emm.ims_vops";
    protected final static String NAS_EPS_ADD_UPD_TYPE = "nas_eps.emm.add_upd_type";
    protected final static String SUPPORTED_BAND_EUTRA_ELEMENT = "lte-rrc.SupportedBandEUTRA_element";
    protected final static String RRC_SUPPORTED_BAND_LIST_EUTRA_TREE = "lte-rrc.supportedBandListEUTRA_tree";
    protected final static String RRC_SUPPORTED_BAND_LIST_EUTRA = "lte-rrc.supportedBandListEUTRA";
    protected final static String RRC_RF_PARAMETERS_ELEMENT = "lte-rrc.rf_Parameters_element";
    protected final static String RRC_UL_64_QAM_R12 = "lte-rrc.ul_64QAM_r12";
    protected final static String RRC_DL_256_QAM_R12 = "lte-rrc.dl_256QAM_r12";
    protected final static String RRC_SUPPORTED_BAND_EUTRA_V1250_ELEMENT = "lte-rrc.SupportedBandEUTRA_v1250_element";
    protected final static String RRC_SUPPORTED_BAND_LIST_EUTRA_V1250 = "lte-rrc.supportedBandListEUTRA_v1250";
    protected final static String RRC_SUPPORTED_BAND_LIST_EUTRA_v1250_TREE = "lte-rrc.supportedBandListEUTRA_v1250_tree";
    protected final static String GSM_CAP_SRVCC_GERAN = "gsm_a.gm.gmm.net_cap.srvcc_to_geran";

    protected final static String DUAL_CONNECTIVITY = "gsm_a.gm.gmm.net_cap.dc_eutra_nr_cap";
    protected final static String MS_NETWORK_CAPABILITY = "MS Network Capability";
    protected final static String NAS_EPS_ATTACH_TYPE = "nas_eps.emm.eps_att_type";
    public final static String GSM_DTAP_SYSID = "gsm_a.dtap.sysid";
    protected final static String CODEC_SUPPORTED_2 = "Codec Bitmap for SysID 2";
    protected final static String CODEC_SUPPORTED_1 = "Codec Bitmap for SysID 1";
    protected final static String SUPPORTED_CODEC_LIST = "Supported Codec List - Supported Codecs";
    protected final static String RRC_ACCESS_STRATUM_RELEASE = "lte-rrc.accessStratumRelease";
    protected final static String NR_RRC_ACCESS_STRATUM_RELEASE = "nr-rrc.accessStratumRelease";
    protected final static String RRC_NR_PHY_PARAMETERS_ELEMENT = "nr-rrc.phy_Parameters_element";
    protected final static String RRC_NR_PHY_PARAMETERSCOMMON_ELEMENT = "nr-rrc.phy_ParametersCommon_element";
    protected final static String RRC_NR_RATEMATCHINGSEMISTATIC = "nr-rrc.rateMatchingResrcSetSemi_Static";
    protected final static String RRC_NR_RATEMATCHINGCTRLRESRCSETDYNAMIC = "nr-rrc.rateMatchingCtrlResrcSetDynamic";
    protected final static String RRC_NR_RATEMATCHINGRESRSCSETDYNAMIC = "nr-rrc.rateMatchingResrcSetDynamic";
    protected final static String NR_RCC_SUPPORTEDBANDLISTNR = "nr-rrc.supportedBandListNR";
    protected final static String NR_RRC_BANDNR = "nr-rrc.bandNR";
    protected final static String NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT = "nr-rrc.mimo_ParametersPerBand_element";
    protected final static String NR_RRC_CODEBOOKPARAMETERS_ELEMENT = "nr-rrc.codebookParameters_element";
    protected final static String NR_RRC_TYPE_1 = "nr-rrc.type1_element";
    protected final static String NR_RRC_SINGLEPANEL_ELEMENT = "nr-rrc.singlePanel_element";
    protected final static String NR_RRC_MULTIPANEL_ELEMENT = "nr-rrc.multiPanel_element";
    protected final static String NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST = "nr-rrc.supportedCSI_RS_ResourceList";
    protected final static String NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST_TREE = "nr-rrc.supportedCSI_RS_ResourceList_tree";
    protected final static String NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST_ELEMENT = "nr-rrc.SupportedCSI_RS_Resource_element";
    protected final static String NR_RRC_MAXNUMBERTXPORTSSRC = "nr-rrc.maxNumberTxPortsPerResource";
    protected final static String NR_RRC_MAXNUMBERRESOURCESPERBAND = "nr-rrc.maxNumberResourcesPerBand";
    protected final static String NR_RRC_TOTALNUMTXPORTSPERBAND = "nr-rrc.totalNumberTxPortsPerBand";
    protected final static String NR_RRC_RATEMATCHINGLTE_CRS = "nr-rrc.rateMatchingLTE_CRS";
    protected final static String NR_RRC_FEATURESETSDOWNLINK_V1540 = "nr-rrc.featureSetsDownlink_v1540";
    protected final static String NR_RRC_FEATURESETDOWNLINK_V1540_ELEMENT = "nr-rrc.FeatureSetDownlink_v1540_element";
    protected final static String NR_RRC_FEATURESETDOWNLINK_V1540_TREE = "nr-rrc.featureSetsDownlink_v1540_tree";
    protected final static String NR_RRC_ADDITIONALDMRS_DL_ALT = "nr-rrc.additionalDMRS_DL_Alt";
    protected final static String NR_RRC_PHY_PARAMETERS_FR1_ELEMENT = "nr-rrc.phy_ParametersFR1_element";
    protected final static String NR_RRC_PDCCH_MONITORING_SINGLE_OCCASION = "nr-rrc.pdcch_MonitoringSingleOccasion";
    protected final static String S1AP_PROTOCOLIE_FIELD_ELEMENT = "s1ap.ProtocolIE_Field_element";
    protected final static String ITEM1_IDNASPDU = "Item 1: id-NAS-PDU";
    protected final static String S1AP_PROTOCOLIES_TREE = "s1ap.protocolIEs_tree";
    protected final static String S1AP_PROCEDURE_CODE = "s1ap.procedureCode";
    protected final static String S1AP_INITIATING_MESSAGE_ELEMENT = "s1ap.initiatingMessage_element";
    // ue tx antenna selection support
    protected final static String LTE_RRC_PHY_LAYERS_ELEMENT = "lte-rrc.phyLayerParameters_element";
    protected final static String LTE_RRC_TX_ANTENNA_SEL_SUPPORT = "lte-rrc.ue_TxAntennaSelectionSupported";
    protected final static String LTE_RRC_UE_SPECIFIC_REFS_SIG_SUPPORTED = "lte-rrc.ue_SpecificRefSigsSupported";
    protected final static String LTE_RRC_INTER_RAT_PS_HO_TO_GERAN = "lte-rrc.interRAT_PS_HO_ToGERAN";
    protected final static String LTE_RRC_INTER_RAT_PARAMETERS_GERAN_920_ELEMENT = "lte-rrc.interRAT_ParametersGERAN_v920_element";
    protected final static String LTE_RRC_REDIRECTION_GERAN_920 = "lte-rrc.e_RedirectionGERAN_r9";
    protected final static String LTE_RRC_INTER_RAT_PARAMETERS_UTRA_920_ELEMENT = "lte-rrc.interRAT_ParametersUTRA_v920_element";
    protected final static String LTE_RRC_REDIRECTION_UTRA_920 = "lte-rrc.e_RedirectionUTRA_r9";
    protected final static String LTE_RRC_SON_PARAMETERS_R9_ELEMENT = "lte-rrc.son_Parameters_r9_element";
    protected final static String LTE_RRC_RACH_REPORT_R9 = "lte-rrc.rach_Report_r9";
    protected final static String GSM_CLASSMARK3_UMTS_FDD_RAT_CAP = "gsm_a.classmark3.umts_fdd_rat_cap";
    protected final static String GSM_CLASSMARK3_UMTS_384_MCPS_TDD_RAT_CAP = "gsm_a.classmark3.umts_384_mcps_tdd_rat_cap";
    protected final static String GSM_CLASSMARK3_CDMA_2000_RAT_CAP = "gsm_a.classmark3.cdma_2000_rat_cap";
    protected final static String GSM_CLASSMARK3_UMTS_128_TDD_RAT_CAP = "gsm_a.classmark3.umts_128_mcps_tdd_rat_cap";
    //S1AP-ATTACHREQUEST
    protected final static String S1AP_PDU_TREE = "s1ap.S1AP_PDU_tree";
    protected final static int LOCATION_UPDATE_REQUEST_CODE = 9;
    protected final static int ATTACH_REQUEST_NAS_CODE = 65;
    // GSM additional features
    protected final static String GSM_GMM_NET_CAP_UCS2 = "gsm_a.gm.gmm.net_cap.ucs2";
    protected final static String GSM_GMM_NET_CAP_SS_SCR_IND = "gsm_a.gm.gmm.net_cap.ss_scr_ind";
    protected final static String GSM_GMM_NET_CAP_PFC = "gsm_a.gm.gmm.net_cap.pfc";
    protected final static String GSM_GMM_NET_CAP_LCS = "gsm_a.gm.gmm.net_cap.lcs";
    protected final static String GSM_GMM_NET_CAP_PS_HO_UTRAN_IU = "gsm_a.gm.gmm.net_cap.ps_irat_iu";
    protected final static String GSM_GMM_NET_CAP_PS_HO_UTRAN_S1 = "gsm_a.gm.gmm.net_cap.ps_irat_s1";
    protected final static String GSM_GMM_NET_CAP_COMB_PROC = "gsm_a.gm.gmm.net_cap.comb_proc";
    protected final static String GSM_GMM_NET_CAP_ISR = "gsm_a.gm.gmm.net_cap.isr";
    protected final static String GSM_GMM_NET_CAP_EPC = "gsm_a.gm.gmm.net_cap.epc";
    protected final static String GSM_GMM_NET_CAP_NF = "gsm_a.gm.gmm.net_cap.nf";
    protected final static String GSM_ES_IND = "gsm_a.ES_IND";
    protected final static String GSM_PS_SUP_CAP = "gsm_a.ps_sup_cap";
    protected final static String GSM_RF_POWER_CAPABILITY = "gsm_a.RF_power_capability";
    protected final static String GSM_CLASSMARK3_GSM_400_BAND_INFO_PRESENT = "gsm_a.classmark3.gsm_400_band_info_present";
    protected final static String GSM_CLASSMARK3_GSM_400_ASSOC_RADIO_CAP = "gsm_a.classmark3.gsm_400_assoc_radio_cap";
    protected final static String GSM_CLASSMARK3_GSM_850_BAND_INFO_PRESENT = "gsm_a.classmark3.gsm_850_assoc_radio_cap_present";
    protected final static String GSM_CLASSMARK3_GSM_850_ASSOC_RADIO_CAP = "gsm_a.classmark3.gsm_850_assoc_radio_cap";
    protected final static String GSM_CLASSMARK3_GSM_1900_BAND_INFO_PRESENT = "gsm_a.classmark3.gsm_1900_assoc_radio_cap_present";
    protected final static String GSM_CLASSMARK3_GSM_1900_ASSOC_RADIO_CAP = "gsm_a.classmark3.gsm_1900_assoc_radio_cap";
    protected final static String GSM_CLASSMARK3_GSM_710_BAND_INFO_PRESENT = "gsm_a.classmark3.gsm_710_assoc_radio_cap_present";
    protected final static String GSM_CLASSMARK3_GSM_710_ASSOC_RADIO_CAP = "gsm_a.classmark3.gsm_710_assoc_radio_cap";
    protected final static String GSM_CLASSMARK3_GSM_750_BAND_INFO_PRESENT = "gsm_a.classmark3.gsm_750_assoc_radio_cap_present";
    protected final static String GSM_CLASSMARK3_GSM_750_ASSOC_RADIO_CAP = "gsm_a.classmark3.gsm_750_assoc_radio_cap";
    protected final static String GSM_CLASSMARK3_GSM_810_BAND_INFO_PRESENT = "gsm_a.classmark3.t_gsm_810_assoc_radio_cap_present";
    protected final static String GSM_CLASSMARK3_GSM_810_ASSOC_RADIO_CAP = "gsm_a.classmark3.t_gsm_810_assoc_radio_cap";
    protected final static String GSM_CLASSMARK3_GSM_900_BAND_INFO_PRESENT = "gsm_a.classmark3.t_gsm_900_assoc_radio_cap_present";
    protected final static String GSM_CLASSMARK3_GSM_900_ASSOC_RADIO_CAP = "gsm_a.classmark3.t_gsm_900_assoc_radio_cap";

    protected final static String GSM_CLASSMARK3_GMSK_MULTISLOT_POWER_PROF = "gsm_a.classmark3.gmsk_multislot_power_prof";
    protected final static String GSM_CLASSMARK3_8PSK_MULTISLOT_POWER_PROF = "gsm_a.classmark3.8_psk_multislot_power_prof";
    protected final static String GSM_CLASSMARK3_DOWNLINK_ADV_RECEIVER_PERF = "gsm_a.classmark3.downlink_adv_receiver_perf";
    protected final static String GSM_CLASSMARK3_DTM_ENHANCEMENTS_CAP = "gsm_a.classmark3.dtm_enhancements_capability";
    protected final static String GSM_CLASSMARK3_PRIO_BASED_RESEL_SUPPORT = "gsm_a.classmark3.prio_based_resel_support";
    protected final static String GSM_CLASSMARK3_DTM_E_GPRS_MULTI_SLOT_INFO_PRESENT = "gsm_a.classmark3.dtm_e_gprs_multi_slot_info_present";
    protected final static String GSM_CLASSMARK3_DTM_GPRS_MULTI_SLOT_CLASS = "gsm_a.classmark3.dtm_gprs_multi_slot_class";
    protected final static String GSM_CLASSMARK3_DTM_EGPRS_MULTI_SLOT_CLASS_PRESENT = "gsm_a.classmark3.dtm_egprs_multi_slot_class_present";
    protected final static String GSM_CLASSMARK3_DTM_EGPRS_MULTI_SLOT_CLASS = "gsm_a.classmark3.dtm_egprs_multi_slot_class";
    protected final static String GSM_CLASSMARK3_SINGLE_SLOT_DTM_SUPPORTED = "gsm_a.classmark3.single_slot_dtm_supported";
    protected final static String GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY1_PRESENT = "gsm_a.classmark3.8_psk_rf_power_capability_1_present";
    protected final static String GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY1 = "gsm_a.classmark3.8_psk_rf_power_capability_1";
    protected final static String GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY2_PRESENT = "gsm_a.classmark3.8_psk_rf_power_capability_2_present";
    protected final static String GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY2 = "gsm_a.classmark3.8_psk_rf_power_capability_2";
    protected final static String GSM_CLASSMARK3_8PSK_STRUCT_TREE = "gsm_a.classmark3.8_psk_struct_tree";
    protected final static String GSM_CLASSMARK3_ASS_RADIO_CAP2 = "gsm_a.classmark3.ass_radio_cap2";
    protected final static String GSM_CLASSMARK3_ASS_RADIO_CAP1 = "gsm_a.classmark3.ass_radio_cap1";

    //CODES
    protected final static int SIP_200_OK_RESPONSE_CODE = 200;
    public static ArrayList<String> attachJsonParsingTree = null;

    public static ArrayList<String> attachAcceptJsonParsingTree = null;
    public static ArrayList<String> singleInstanceCheckerTree = null;
    public static ArrayList<String> ueCapJsonGeneralParsingTree = null;
    public static ArrayList<String> duplicatedKeysWiresharkMediaAttr = null;
    public static ArrayList<String> ueCapJsonGeneralParsingTreeItem3 = null;
    public static ArrayList<String> ueCapJsonGeneralParsingTreeItem2 = null;
    public static ArrayList<String> ueCapCatMJsonParsingTree = null;
    public static String firstDuplicatedKeyWireshark = null;
    public static ArrayList<String> codecIds = null;
    public static ArrayList<String> ueCapJsonParsingTreeFor5G = null;

    //5G
    public static String UE_ADDITIONAL_SECURITY_CAPABILITY = "UE additional security capability";

    /**
     * Initialize support variables needed for the program
     */
    public static void initializeSupportVariables () {
        // ArrayList to quickly parse UE Capabilities
        Utility.ueCapJsonParsingTree = new ArrayList<String>(Arrays.asList(S1AP_VALUE_ELEMENT, S1AP_UERADIOCAPABILITY_TREE, S1AP_LTE_UERADIOACCESSCAPABILITY_INFORMATION_ELEMENT,
                CRITICAL_EXTENSIONS_TREE, RRC_CA_TREE, S1AP_LTE_R8_UERADIOACCESSCAPABILITY_INFORMATION_ELEMENT, S1AP_UERADIOCAPABILITY_INFOTREE, S1AP_UECAPABILITY_INFORMATION_ELEMENT,
                CRITICAL_EXTENSIONS_TREE, RRC_CA_TREE, S1AP_UECAPABILITYINFORMATION_R8_ELEMENT,
                S1AP_UECAPABILITY_CONTAINERlIST_TREE, ITEM_0, RRC_UECAPABILITY_RAT_CONTAINER_ELEMENT,
                RRC_UECAPABILITY_RAT_CONTAINER_TREE, RRC_UECAPABILITY_ELEMENT));
        Utility.ueCapJsonNRParsingTree = new ArrayList<String>(Arrays.asList(S1AP_VALUE_ELEMENT, S1AP_UERADIOCAPABILITY_TREE, S1AP_LTE_UERADIOACCESSCAPABILITY_INFORMATION_ELEMENT,
                CRITICAL_EXTENSIONS_TREE, RRC_CA_TREE, S1AP_LTE_R8_UERADIOACCESSCAPABILITY_INFORMATION_ELEMENT, S1AP_UERADIOCAPABILITY_INFOTREE, S1AP_UECAPABILITY_INFORMATION_ELEMENT,
                CRITICAL_EXTENSIONS_TREE, RRC_CA_TREE, S1AP_UECAPABILITYINFORMATION_R8_ELEMENT, S1AP_UECAPABILITY_CONTAINERlIST_TREE));

        attachJsonParsingTree = new ArrayList<String>(Arrays.asList(SOURCE, LAYERS, S1AP, S1AP_PDU_TREE, S1AP_INITIATING_MESSAGE_ELEMENT,
                S1AP_VALUE_ELEMENT, S1AP_INITIAL_UE_MESSAGE_ELEMENT, S1AP_PROTOCOLIES_TREE, ITEM1_IDNASPDU, S1AP_PROTOCOLIE_FIELD_ELEMENT,
                S1AP_VALUE_ELEMENT, NAS_EPS));

        attachAcceptJsonParsingTree = new ArrayList<String>(Arrays.asList(S1AP_VALUE_ELEMENT,
                INITIAL_CONTEXT_SETUP_REQ, S1AP_PROTOCOLIES_TREE,ITEM3_ID_ERAB_TOBESETUPLIST,S1AP_PROTOCOLIE_FIELD_ELEMENT,S1AP_VALUE_ELEMENT,ERAB_TOBESETUP_CTXTLISTTREE,
                ITEM0_ID_ERAB_TOBESETUPLIST,PROTOCOL_LTE_SINGLE_CONTAINER_ELEMENT,S1AP_VALUE_ELEMENT, ERAB_TOBESETUPITEM_CTXTSUREQ_ELEMENT));

        singleInstanceCheckerTree = new ArrayList<String>(Arrays.asList(S1AP_VALUE_ELEMENT, S1AP_INITIAL_UE_MESSAGE_ELEMENT,
                S1AP_PROTOCOLIES_TREE, ITEM1_IDNASPDU, S1AP_PROTOCOLIE_FIELD_ELEMENT, S1AP_VALUE_ELEMENT,
                NAS_EPS, Utility.EOF));
        ueCapJsonGeneralParsingTree = new ArrayList<String>(Arrays.asList(SOURCE, LAYERS, S1AP, S1AP_PDU_TREE, S1AP_INITIATING_MESSAGE_ELEMENT,
                S1AP_VALUE_ELEMENT, S1AP_UE_CAP_INFO_INDICATION_ELEMENT, S1AP_PROTOCOLIES_TREE,
                ITEM2_ID_UE_RADIO_CAPABILITY, S1AP_PROTOCOLIE_FIELD_ELEMENT));
        ueCapJsonGeneralParsingTreeItem3 = new ArrayList<String>(Arrays.asList(SOURCE, LAYERS, S1AP, S1AP_PDU_TREE, S1AP_INITIATING_MESSAGE_ELEMENT,
                S1AP_VALUE_ELEMENT, S1AP_UE_CAP_INFO_INDICATION_ELEMENT, S1AP_PROTOCOLIES_TREE,
                ITEM3_ID_RADIO_CAPABILITY, S1AP_PROTOCOLIE_FIELD_ELEMENT));
        ueCapJsonGeneralParsingTreeItem2 = new ArrayList<String>(Arrays.asList(SOURCE, LAYERS, S1AP, S1AP_PDU_TREE, S1AP_INITIATING_MESSAGE_ELEMENT,
                S1AP_VALUE_ELEMENT, S1AP_UE_CAP_INFO_INDICATION_ELEMENT, S1AP_PROTOCOLIES_TREE, ITEM2_ID_UE_RADIO_CAPABILITY, S1AP_PROTOCOLIE_FIELD_ELEMENT,
                S1AP_VALUE_ELEMENT, S1AP_UERADIOCAPABILITY_TREE, LTE_RRC_UE_RADIO_CAP_INFORMATION_NB, CRITICAL_EXTENSIONS_TREE,
                RRC_CA_TREE, LTE_RRC_UE_RADIO_CAP_INFORMATION_R13_ELEMENT, LTE_RRC_UE_RADIO_CAP_INFO_R13_TREE, LTE_RRC_UE_CAP_INFO_NB_R13_ELEMENT));
        ueCapCatMJsonParsingTree = new ArrayList<String>(Arrays.asList(S1AP_VALUE_ELEMENT, S1AP_UE_RADIO_CAPABILITY_FOR_PAGING_TREE, LTE_RRC_UE_RADIO_CAPABILITY_FOR_PAGING_INFO_ELEMENT,
                CRITICAL_EXTENSIONS_TREE, RRC_CA_TREE, LTE_RRC_UE_RADIO_CAPABILITY_PAGING_INFORMATION_R12_ELEMENT, LTE_RRC_UE_RADIO_CAPABILITY_PAGING_R12_TREE, LTE_RRC_UE_RADIO_PAGING_INFO_R12_ELEMENT));
        codecIds = new ArrayList<String>();
        // Retrieve absolute path of file by creating a new file and retrieving its current path
        File file = new File("temp");
        String absolutePath = file.getAbsolutePath();
        file.delete();
        Utility.PROGRAM_PATH = absolutePath.substring(0,absolutePath.lastIndexOf(File.separator));
    }

    /**
     * Method to reset support variables
     */
    public static void resetSupportVariables() {
        codecIds = new ArrayList();
        Utility.IS_VOLTE = true;
    }

    // Workaround for duplicate keys in json
    public static void createWiresharkDuplicateKeysStorage() {
        duplicatedKeysWiresharkMediaAttr = new ArrayList();
    }

    /**
     * Workaround to solve Wireshark json objects with repeated keys.
     * it is called by LinkedTreeMapTree every time it finds a duplicate in json file
     * @param a the value in the duplicate key
     */
    public static void setWiresharkDuplicateKeys(String a){
        if (duplicatedKeysWiresharkMediaAttr !=null) {
            duplicatedKeysWiresharkMediaAttr.add(a);
        }
    }

    /**
     * Workaround to solve wireshark json objects with repeated keys.
     * parse the arraylist of repeated values with same keys
     * @param codecs list of strings related to DUT supported codecs
     * @return Returns the
     */
    public static ArrayList<VoLTECodec> wiresharkWorkaroundCodecsParser(ArrayList<String> codecs){
        // First element (up to index 3) is different
        ArrayList<VoLTECodec> parsedCodecs = new ArrayList<>();
        VoLTECodec firstCodec = new VoLTECodec();
        String tmp = codecs.get(0).replace("rtpmap:","");
        tmp = tmp.replace(SDP_MEDIA_ATTR.concat("="),"");
        tmp = tmp.replace("\"","");
        Pattern firstCodecEVSPattern = Pattern.compile(Utility.EVS_REGEX);
        Matcher firstCodecEVSMatcher = firstCodecEVSPattern.matcher(tmp);
        Pattern firstCodecAMRWBPattern = Pattern.compile(Utility.AMR_WB_REGEX);
        Matcher firstCodecAMRWBMatcher = firstCodecAMRWBPattern.matcher(tmp);
        Pattern firstCodecAMRPattern = Pattern.compile(Utility.AMR_REGEX);
        Matcher firstCodecAMRMatcher = firstCodecAMRPattern.matcher(tmp);

        Pattern firstSampleRatePattern = Pattern.compile(Utility.INSIDE_SLAH_REGEX);
        Matcher firstSampleRateMatcher = firstSampleRatePattern.matcher(tmp);
        firstSampleRateMatcher.find();

        Pattern firstMediaFormatPattern = Pattern.compile(Utility.MEDIA_FORMAT_FIRST_CODEC_REGEX);
        Matcher firstMediaFormatMatcher = firstMediaFormatPattern.matcher(tmp);
        firstMediaFormatMatcher.find();
        int j = 1;
        if (firstCodecEVSMatcher.find()) {
            j++;
            firstCodec.setMimeType(Utility.VOLTE_EVS);
            firstCodec.setSampleRate(Integer.parseInt(firstSampleRateMatcher.group(0).replace("/","")));
            firstCodec.setMediaFormat(Integer.parseInt(firstMediaFormatMatcher.group(0).replace(" ","")));
            //System.out.println(firstCodec.getMediaFormat());

            //loop through codecs arraylist to find the right mediaFormat
            // str1.toLowerCase().contains(str2.toLowerCase())
            String tmp2 = codecs.get(1);
            if(tmp2.contains("fmtp:"+(firstCodec.getMediaFormat()))){
                tmp2 = codecs.get(1);
            }
            else {
                for (int i = j; i < codecs.size(); i++) {
                    if (codecs.get(i).contains("fmtp:" + (firstCodec.getMediaFormat()))) {
                        tmp2 = codecs.get(i);
                        break;
                    }
                }
            }
            try {
                Pattern bwPattern = Pattern.compile(Utility.BW_REGEX);
                Matcher bwMatcher = bwPattern.matcher(tmp2);
                bwMatcher.find();
                firstCodec.setBw(bwMatcher.group(0).replace(Utility.BW_, ""));
                Pattern brPattern = Pattern.compile(Utility.BR_REGEX);
                Matcher brMatcher = brPattern.matcher(tmp2);
                brMatcher.find();
                String[] splitted = brMatcher.group(0).replace(Utility.BR_, "").replace("\"", "").split("-");
                float[] br = new float[splitted.length];
                br[0] = Float.parseFloat(splitted[0]);
                try {
                    br[1] = Float.parseFloat(splitted[1]);
                }
                catch (Exception e) {
                    System.out.println("Only one value declared.");
                }
                firstCodec.setBr(br);


            }
            catch (Exception e){
                System.out.println("There was a problem with EVS declaration");
                firstCodec.setBw("-");
                //e.printStackTrace();
            }
        } else if (firstCodecAMRWBMatcher.find()) {
            firstCodec.setMimeType(Utility.VOLTE_AMR_WB);
            firstCodec.setSampleRate(Integer.parseInt(firstSampleRateMatcher.group(0).replace("/","")));
            firstCodec.setMediaFormat(Integer.parseInt(firstMediaFormatMatcher.group(0).replace("/","").replace(" ","")));

            Pattern modeSetPattern = Pattern.compile(Utility.MODE_SET_REGEX);
            Matcher modeSetMatcher = modeSetPattern.matcher(codecs.get(1));

            if (modeSetMatcher.find()){
                String toExplode = modeSetMatcher.group(0).replace("mode-set=","");
                String[] exploded = toExplode.split(";");
                int[] supportedModeSets = new int[exploded.length];
                for (int k = 0; k < exploded.length; k ++) {
                    supportedModeSets[k] = Integer.parseInt(exploded[k]);
                }
                boolean contains0 = IntStream.of(supportedModeSets).anyMatch(x -> x == 0);
                boolean contains1 = IntStream.of(supportedModeSets).anyMatch(x -> x == 1);
                boolean contains2 = IntStream.of(supportedModeSets).anyMatch(x -> x == 2);
                boolean contains4 = IntStream.of(supportedModeSets).anyMatch(x -> x == 4);
                boolean contains7 = IntStream.of(supportedModeSets).anyMatch(x -> x == 7);
                boolean contains8 = IntStream.of(supportedModeSets).anyMatch(x -> x == 8);

                if (contains0 && contains1 && contains2 && parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR_WB)) {
                    float[] brs = new float[2];
                    brs[0] = (float)6.6;
                    brs[1] = (float)12.65;
                    firstCodec.setBr(brs);


                }
                if (contains0 && contains1 && contains2 && contains8 && parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR_WB)){
                    float[] brs = new float[4];
                    brs[0] = (float)6.6;
                    brs[1] = (float)8.85;
                    brs[2] = (float)12.65;
                    brs[3] = (float)23.85;
                    firstCodec.setBr(brs);
                }
                else if (contains0 && contains2 && contains4 && contains7 && parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR)){
                    float[] brs = new float[4];
                    brs[0] = (float)4.75;
                    brs[1] = (float)5.9;
                    brs[2] = (float)7.4;
                    brs[3] = (float)12.20;
                    firstCodec.setBr(brs);
                }

            } else {
                float[] brs = new float[2];
                brs[0] = (float)6.6;
                brs[1] = (float)23.85;
                firstCodec.setBr(brs);
            }
        } else if (firstCodecAMRMatcher.find()) {
            firstCodec.setMimeType(Utility.VOLTE_AMR);
            firstCodec.setSampleRate(Integer.parseInt(firstSampleRateMatcher.group(0).replace("/","")));
            firstCodec.setMediaFormat(Integer.parseInt(firstMediaFormatMatcher.group(0).replace("/","").replace(" ","")));

            Pattern modeSetPattern = Pattern.compile(Utility.MODE_SET_REGEX);
            Matcher modeSetMatcher = modeSetPattern.matcher(codecs.get(1));

            if (modeSetMatcher.find()){
                String toExplode = modeSetMatcher.group(0).replace("mode-set=","");
                String[] exploded = toExplode.split(";");
                int[] supportedModeSets = new int[exploded.length];
                for (int k = 0; k < exploded.length; k ++) {
                    supportedModeSets[k] = Integer.parseInt(exploded[k]);
                }
                boolean contains0 = IntStream.of(supportedModeSets).anyMatch(x -> x == 0);
                boolean contains1 = IntStream.of(supportedModeSets).anyMatch(x -> x == 1);
                boolean contains2 = IntStream.of(supportedModeSets).anyMatch(x -> x == 2);
                boolean contains4 = IntStream.of(supportedModeSets).anyMatch(x -> x == 4);
                boolean contains7 = IntStream.of(supportedModeSets).anyMatch(x -> x == 7);
                boolean contains8 = IntStream.of(supportedModeSets).anyMatch(x -> x == 8);

                if (contains0 && contains1 && contains2 && parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR_WB)) {
                    float[] brs = new float[2];
                    brs[0] = (float)6.6;
                    brs[1] = (float)12.65;
                    firstCodec.setBr(brs);
                }
                if (contains0 && contains1 && contains2 && contains8 && parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR_WB)){
                    float[] brs = new float[4];
                    brs[0] = (float)6.6;
                    brs[1] = (float)8.85;
                    brs[2] = (float)12.65;
                    brs[3] = (float)23.85;
                    firstCodec.setBr(brs);
                }
                else if (contains0 && contains2 && contains4 && contains7 && parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR)){
                    float[] brs = new float[4];
                    brs[0] = (float)4.75;
                    brs[1] = (float)5.9;
                    brs[2] = (float)7.4;
                    brs[3] = (float)12.20;
                    firstCodec.setBr(brs);
                }
            } else {
                float[] brs = new float[2];
                brs[0] = (float)4.75;
                brs[1] = (float)12.2;
                firstCodec.setBr(brs);
            }
        }
        tmp = tmp.concat(" ").concat(codecs.get(1)).replace("\"","");

        parsedCodecs.add(firstCodec);
        boolean flag = true;
        //int j = 3;
        while (flag) {
            tmp = "";
            Pattern codecPattern = Pattern.compile(Utility.RTPMAP_REGEX);
            Matcher codecMatcher = codecPattern.matcher(codecs.get(j));

            Pattern telEvPattern = Pattern.compile(Utility.TELEPHONE_EVENT_REGEX);
            Matcher telEvMatcher = telEvPattern.matcher(codecs.get(j));

            Pattern fmtpPattern = Pattern.compile(Utility.FMTP_REGEX);
            Matcher fmtpMatcher =  fmtpPattern.matcher(codecs.get(j));

            if (telEvMatcher.find()) {
                //System.out.println("Telephone Event, do nothing");
                // TODO telephone event are here captured. In a future development they may be needed
                // For the moment we just throw them away
            } else if (codecMatcher.find()) {
                String t;
                t = codecs.get(j).replace("rtpmap:","");
                t = t.replace("\"","").replace(",",";").concat(" ");
                String currentCodec = t.concat(codecs.get(j+1)).concat(codecs.get(j+2)).replace("{"," ")
                        .replace("}","").replace("\"","").replace(",",";");
                String[] exploded = currentCodec.split(";");
                VoLTECodec tempCodec = new VoLTECodec();
                for (int i = 0; i < exploded.length; i++) {
                    Pattern mimeTypePattern = Pattern.compile(Utility.SDP_MIME_TYPE_REGEX);
                    Matcher mimeTypeMatcher = mimeTypePattern.matcher(exploded[i]);

                    Pattern sampleRatePattern = Pattern.compile(Utility.SAMPLE_RATE_REGEX);
                    Matcher sampleRateMatcher = sampleRatePattern.matcher(exploded[i]);

                    Pattern mediaFormatPattern = Pattern.compile(Utility.MEDIA_FORMAT_REGEX);
                    Matcher mediaFormatMatcher = mediaFormatPattern.matcher(exploded[i]);

                    Pattern brPattern = Pattern.compile(Utility.BR_REGEX);
                    Matcher brMatcher = brPattern.matcher(exploded[i]);

                    Pattern bwPattern = Pattern.compile(Utility.BW_REGEX);
                    Matcher bwMatcher = bwPattern.matcher(exploded[i]);

                    if (mimeTypeMatcher.find()) {
                        tempCodec.setMimeType(exploded[i].replace(Utility.SDP_MIME_TYPE,""));
                    } else if (sampleRateMatcher.find()) {
                        // Sample rate found
                        tempCodec.setSampleRate(Integer.parseInt(exploded[i].replace(Utility.SDP_SAMPLE_RATE,"")));
                    } else if (mediaFormatMatcher.find()) {
                        // Media format found
                        tempCodec.setMediaFormat(Integer.parseInt(exploded[i].replace(Utility.SDP_MEDIA_FORMAT,"")));
                    } else if (brMatcher.find()) {
                        String[] splitted = brMatcher.group(0).replace(Utility.BR_,"").replace("\"","").split("-");
                        float[] br = new float[2];
                        br[0] = Float.parseFloat(splitted[0]);
                        br[1] = Float.parseFloat(splitted[1]);
                        tempCodec.setBr(br);
                    } else if (bwMatcher.find()) {
                        tempCodec.setBw(bwMatcher.group(0).replace(Utility.BW_,""));
                    }
                }
                parsedCodecs.add(tempCodec);
            } else if(fmtpMatcher.find()) {
                Pattern modeSetPattern = Pattern.compile(Utility.MODE_SET_REGEX);
                Matcher modeSetMatcher = modeSetPattern.matcher(codecs.get(j));
                if (modeSetMatcher.find()){
                    String toExplode = modeSetMatcher.group(0).replace("mode-set=","");
                    System.out.println(toExplode);
                    String[] exploded = toExplode.split(";");
                    for(String e : exploded) {
                        System.out.println(e);
                    }
                    //exploded = exploded.replaceAll("\"""\")
                    int[] supportedModeSets = new int[exploded.length];
                    try {
                        for (int k = 0; k < exploded.length; k++) {
                            supportedModeSets[k] = Integer.parseInt(exploded[k]);
                        }
                    } catch(Exception e){
                        break;
                    }
                    boolean contains0 = IntStream.of(supportedModeSets).anyMatch(x -> x == 0);
                    boolean contains1 = IntStream.of(supportedModeSets).anyMatch(x -> x == 1);
                    boolean contains2 = IntStream.of(supportedModeSets).anyMatch(x -> x == 2);
                    boolean contains4 = IntStream.of(supportedModeSets).anyMatch(x -> x == 4);
                    boolean contains7 = IntStream.of(supportedModeSets).anyMatch(x -> x == 7);
                    boolean contains8 = IntStream.of(supportedModeSets).anyMatch(x -> x == 8);

                    if (contains0 && contains1 && contains2 && parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR_WB)) {
                        int itemIndex = parsedCodecs.size()-1;
                        float[] brs = new float[2];
                        brs[0] = (float)6.6;
                        brs[1] = (float)12.65;
                        parsedCodecs.get(itemIndex).setBr(brs);
                    }
                    if (contains0 && contains1 && contains2 && contains8 && parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR_WB)){
                        int itemIndex = parsedCodecs.size()-1;
                        float[] brs = new float[4];
                        brs[0] = (float)6.6;
                        brs[1] = (float)8.85;
                        brs[2] = (float)12.65;
                        brs[3] = (float)23.85;
                        parsedCodecs.get(itemIndex).setBr(brs);
                    }
                    else if (contains0 && contains2 && contains4 && contains7 && parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR)){
                        int itemIndex = parsedCodecs.size()-1;
                        float[] brs = new float[4];
                        brs[0] = (float)4.75;
                        brs[1] = (float)5.9;
                        brs[2] = (float)7.4;
                        brs[3] = (float)12.20;
                        parsedCodecs.get(itemIndex).setBr(brs);
                    }
                } else if (parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR_WB) ||
                        (parsedCodecs.get(parsedCodecs.size()-1).getMimeType().equals(Utility.VOLTE_AMR))){
                    int itemIndex = parsedCodecs.size()-1;
                    float[] brs = new float[2];
                    if (parsedCodecs.get(itemIndex).getMimeType().equals(Utility.VOLTE_AMR_WB)){
                        brs[0] = (float)6.6;
                        brs[1] = (float)23.85;
                    } else if (parsedCodecs.get(itemIndex).getMimeType().equals(Utility.VOLTE_AMR)) {
                        brs[0] = (float)4.75;
                        brs[1] = (float)12.20;
                    }
                    parsedCodecs.get(itemIndex).setBr(brs);
                }
            } else if (!fmtpMatcher.find() && parsedCodecs.get(parsedCodecs.size()-1).getBr() == null){
                int itemIndex = parsedCodecs.size()-1;
                float[] brs = new float[2];
                if (parsedCodecs.get(itemIndex).getMimeType().equals(Utility.VOLTE_AMR_WB)){
                    brs[0] = (float)6.6;
                    brs[1] = (float)23.85;
                } else if (parsedCodecs.get(itemIndex).getMimeType().equals(Utility.VOLTE_AMR)) {
                    brs[0] = (float)4.75;
                    brs[1] = (float)12.20;
                }
                parsedCodecs.get(itemIndex).setBr(brs);
            }
            j++;
            if (j>=codecs.size()) {
                flag = false;
            }
        }
        return parsedCodecs;
    }

    /**
     * Wireshark workaround to solve objects with duplicate keys (int this case 2G/3G codecs in attach request)
     * @param codecId id of the codecs in attach request as strings
     */
    public static void wiresharkWorkaroundGsmUmtsCodecIdsParser(String codecId){
        try {
            codecId = codecId.replace("\"", "");
            int ratId = Integer.decode(codecId);
            switch (ratId) {
                case 0: codecIds.add(Utility.GSM);
                    break;
                case 4: codecIds.add(Utility.UMTS);
                    break;
                default: System.out.println("2G/3G codec decoding error");
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static String getFirstDuplicatedKeyWireshark() {
        return firstDuplicatedKeyWireshark;
    }

    public static void setFirstDuplicatedKeyWireshark(String firstDuplicatedKeyWireshark) {
        duplicatedKeysWiresharkMediaAttr.add(firstDuplicatedKeyWireshark);
    }

    public static String supportedCSIRSresourceMapper(int suppCSIRSres){
        if(suppCSIRSres == 0) return "p2";
        else if(suppCSIRSres == 1) return "p4";
        else if(suppCSIRSres == 2) return "p8";
        else if(suppCSIRSres == 3) return "p12";
        else if(suppCSIRSres == 4) return "p16";
        else if(suppCSIRSres == 5) return "p24";
        else return "p32";
    }
}
