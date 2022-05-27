//MobileParser SA
package com.company.Parser;

import com.company.FileManager;
import com.company.Model.Band;
import com.company.Model.DUT;
import com.company.Model.Mobile;
import com.company.Model.gsm.GSMBand;
import com.company.Model.lte.AlgCouple;
import com.company.Model.lte.CACombination;
import com.company.Model.lte.ComponentCarrier;
import com.company.Model.lte.LTEBand;
import com.company.Model.nr.MRDCBandCombination;
import com.company.Model.nr.MRDCBandEutra;
import com.company.Model.nr.MRDCBandNR;
import com.company.Model.umts.UMTSBand;
import com.company.Utility;
import com.google.gson.*;

import java.io.FileReader;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.company.Parser.ParserUtility.*;
import static com.company.Utility.*;


/**
 * JSON Parser class to parse smartphone capabilities
 */
public class MobileParser extends Parser implements ParserInterface {
    private Mobile dut;

    public MobileParser(FileReader jsonFile, Mobile dut) {
        ParserUtility.createWiresharkDuplicateKeysStorage();
        ParserUtility.resetSupportVariables();
        this.builder = new GsonBuilder();
        this.gson = this.builder.create();
        this.builder.setPrettyPrinting();
        this.jsonFile = jsonFile;
        this.pcapJsonArray = this.gson.fromJson(jsonFile, JsonArray.class);
        if (dut == null) {
            this.dut = new Mobile();
        }
    }
    public DUT attachAcceptParser(DUT dut, int objectIndex){

        attachAcceptJsonParsingTree.add(0, SOURCE);
        attachAcceptJsonParsingTree.add(1, LAYERS);
        attachAcceptJsonParsingTree.add(2, S1AP);
        attachAcceptJsonParsingTree.add(3, S1AP_PDU_TREE);
        attachAcceptJsonParsingTree.add(4, S1AP_INITIATING_MESSAGE_ELEMENT);
        Mobile device = (Mobile) dut;
        ArrayList<String> keyValues = ParserUtility.attachAcceptJsonParsingTree;
        JsonObject temp1 = (JsonObject) pcapJsonArray.get(objectIndex);
        JsonObject temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0; k < keyValues.size() - 1; k++) {
            temp1 = temp1.getAsJsonObject(keyValues.get(k));
            temp2 = temp1.getAsJsonObject(keyValues.get(k + 1));
        }

        if(temp2.has(ERAB_ID)){
            device.seteRAB_ID(temp2.getAsJsonPrimitive(ERAB_ID).getAsInt());
        }

        if(temp2.getAsJsonObject(ERAB_LEVEL_QOS_PARAMETERS_ELEMENT).has(QCI)){
            device.setQCI(temp2.getAsJsonObject(ERAB_LEVEL_QOS_PARAMETERS_ELEMENT).getAsJsonPrimitive(QCI).getAsInt());
        }

        if(temp2.getAsJsonObject(NAS_EPS).has(TAI_LIST)){
            device.setTAC(temp2.getAsJsonObject(NAS_EPS).getAsJsonObject(TAI_LIST).getAsJsonPrimitive(TAC).getAsInt());
        }

        //ESM_MESSAGE_CONTAINER
        if(temp2.getAsJsonObject(NAS_EPS).has(ESM_MESSAGE_CONTAINER)){
            device.setAPN(temp2.getAsJsonObject(NAS_EPS).getAsJsonObject(ESM_MESSAGE_CONTAINER).getAsJsonObject(NAS_EPS_EMM_ESM_MSG_CONT_TREE)
                    .getAsJsonObject(APN).getAsJsonPrimitive(GSM_A_GM_SM_APN).getAsString());
        }

        if(temp2.getAsJsonObject(NAS_EPS).has(EPS_NW_FEATURE_SUPPORT)){
            System.out.println(temp2.getAsJsonObject(NAS_EPS));
            device.setSupportOfIMS_EC(temp2.getAsJsonObject(NAS_EPS).getAsJsonObject(EPS_NW_FEATURE_SUPPORT).getAsJsonPrimitive(EMERGENCY_BEARER_SERVICES_S1_MODE).getAsInt());
            device.setSupportOfIMSvoiceOverPSsession(temp2.getAsJsonObject(NAS_EPS).getAsJsonObject(EPS_NW_FEATURE_SUPPORT).getAsJsonPrimitive(IMS_OVER_PS_SESSION_S1_MODE).getAsInt());
        }

        return dut;
    }

    @Override
    public DUT attachParser(int objectIndex, DUT dut) {
        Mobile device = (Mobile) dut;
        ArrayList<String> keyValues = ParserUtility.attachJsonParsingTree;
        JsonObject temp1 = (JsonObject) pcapJsonArray.get(objectIndex);
        JsonObject temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0; k < keyValues.size() - 1; k++) {
            temp1 = temp1.getAsJsonObject(keyValues.get(k));
            temp2 = temp1.getAsJsonObject(keyValues.get(k + 1));
        }
        //5G
        if (temp2.has(ParserUtility.UE_ADDITIONAL_SECURITY_CAPABILITY)) {
            dut.setSupport5G(true);
            System.out.println("UE Additional Security IE found = "+true+ "--> DUT supports NR");
            //check capabilitiesInformation messages if more than 1
        } else dut.setSupport5G(false);
        //if dut has EIT set to 0
        if (temp2.has(ParserUtility.ESM_MESSAGE_CONTAINER)) {
            if (temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).has(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE)
                    &&
                    !(temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE).
                            has(ParserUtility.ESM_INFORMATION_TRANSFER_FLAG))) {
                device.setPdnType(temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE)
                        .get(ParserUtility.NAS_EPS_ESM_PDN_TYPE).getAsInt());
            }
        }
        if (temp2.has(ParserUtility.ESM_MESSAGE_CONTAINER)) {
            if (temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).has(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE) &&
                    temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE).
                            has(ParserUtility.ESM_INFORMATION_TRANSFER_FLAG)) {
                //PDN TYPE
                device.setPdnType(temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE)
                        .get(ParserUtility.NAS_EPS_ESM_PDN_TYPE).getAsInt());
                // Retrieve information transfer flag
                // Workaround to support Wireshark 2.6.2
                JsonObject fix = temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE)
                        .getAsJsonObject(ParserUtility.ESM_INFORMATION_TRANSFER_FLAG);
                if (fix.has(ParserUtility.NAS_EPS_ESM_EIT)) {
                    device.getLteCap().setEIT(temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE)
                            .getAsJsonObject(ParserUtility.ESM_INFORMATION_TRANSFER_FLAG).get(ParserUtility.NAS_EPS_ESM_EIT).getAsInt());
                } else if (fix.has(ParserUtility.NAS_EPS_EMM_EIT)) {
                    device.getLteCap().setEIT(temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE)
                            .getAsJsonObject(ParserUtility.ESM_INFORMATION_TRANSFER_FLAG).get(ParserUtility.NAS_EPS_EMM_EIT).getAsInt());
                }
            }

            if (temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).has(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE)) {
                if (temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE).
                        has(ParserUtility.PROTOCOL_OPTIONS_CONFIGURATION)) {
                    if (temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE).
                            getAsJsonObject(ParserUtility.PROTOCOL_OPTIONS_CONFIGURATION).has(ParserUtility.PCO_PID_TREE)) {
                        if (temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE).
                                getAsJsonObject(ParserUtility.PROTOCOL_OPTIONS_CONFIGURATION).getAsJsonObject(ParserUtility.PCO_PID_TREE).has(ParserUtility.PCO_LENGTH) &&
                                temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE).
                                        getAsJsonObject(ParserUtility.PROTOCOL_OPTIONS_CONFIGURATION).getAsJsonObject(ParserUtility.PCO_PID_TREE).has(ParserUtility.PCO_PID_PS_DATAOFF)) {

                            }
                        }
                    }
            }
        }
        // retrieve voice domain preference and ue's usage settings
        if (temp2.has(ParserUtility.VOICE_DOMAIN_PREFERENCE_UE_USAGE_SETTING)) {
            int usageSettings = temp2.getAsJsonObject(ParserUtility.VOICE_DOMAIN_PREFERENCE_UE_USAGE_SETTING)
                    .get(ParserUtility.UE_USAGE_SETTING).getAsInt();
            device.setUeUsageSettings(usageSettings);
            switch (usageSettings) {
                case 0:
                    device.setVoiceDomainPreference(temp2.getAsJsonObject(ParserUtility.VOICE_DOMAIN_PREFERENCE_UE_USAGE_SETTING)
                            .get(ParserUtility.GSM_GMM_VOICE_DOMAIN_PREFERENCE_EUTRAN).getAsInt());
                    device.setAdditionalUpdateType(Utility.NOT_APPLICABLE_CODE);
                    break;
                case 1:
                    if (temp2.getAsJsonObject(ParserUtility.VOICE_DOMAIN_PREFERENCE_UE_USAGE_SETTING)
                            .has(ParserUtility.GSM_GMM_VOICE_DOMAIN_PREFERENCE_EUTRAN)) {
                        device.setVoiceDomainPreference(temp2.getAsJsonObject(ParserUtility.VOICE_DOMAIN_PREFERENCE_UE_USAGE_SETTING)
                                .get(ParserUtility.GSM_GMM_VOICE_DOMAIN_PREFERENCE_EUTRAN).getAsInt());
                        device.setAdditionalUpdateType(Utility.NOT_APPLICABLE_CODE);
                    } else {
                        device.setVoiceDomainPreference(Utility.NOT_APPLICABLE_CODE);
                        device.setAdditionalUpdateType(temp2.getAsJsonObject(Utility.ADDITIONAL_UPDATE_TYPE).get(ParserUtility.NAS_EPS_ADD_UPD_TYPE).getAsInt());
                    }
                    break;
                default: // TODO what if usage settings is different from 0/1? suggestion: we go fuck ourselves
                    break;
            }
        } else {
            device.setUeUsageSettings(NOT_APPLICABLE_CODE);
            device.setVoiceDomainPreference(NOT_APPLICABLE_CODE);
            if (temp2.has(Utility.ADDITIONAL_UPDATE_TYPE)) {
                device.setAdditionalUpdateType(temp2.getAsJsonObject(Utility.ADDITIONAL_UPDATE_TYPE).get(ParserUtility.NAS_EPS_ADD_UPD_TYPE).getAsInt());
            } else {
                device.setAdditionalUpdateType(Utility.NOT_APPLICABLE_CODE);
            }
        }
        if (temp2.has(ParserUtility.SUPPORTED_CODEC_LIST)) {

            HashMap map = null;
            HashMap map2 = null;
            try {
                if (ParserUtility.codecIds.get(0).equals(Utility.GSM)) {
                    map = device.getGsmCap().getCodecs();
                    map2 = device.getUmtsCap().getCodecs();
                } else if (ParserUtility.codecIds.get(1).equals(Utility.GSM)) {
                    map2 = device.getGsmCap().getCodecs();
                    map = device.getUmtsCap().getCodecs();
                }
            } catch (Exception e) {
                System.out.println("Only codecs of 1 RAT are present");
                if (ParserUtility.codecIds.get(0).equals(Utility.UMTS)) {
                    map2 = device.getUmtsCap().getCodecs();
                }
            }


            try {
                if (temp2.getAsJsonObject(ParserUtility.SUPPORTED_CODEC_LIST).has(ParserUtility.CODEC_SUPPORTED_1)) {
                    JsonObject gsmCodecsTree = temp2.getAsJsonObject(ParserUtility.SUPPORTED_CODEC_LIST).getAsJsonObject(ParserUtility.CODEC_SUPPORTED_1);
                    map.replace(Utility.TDMA_EFR, gsmCodecsTree.get(Utility.CODEC_TDMA_EFR).getAsInt());
                    map.replace(Utility.UMTS_AMR, gsmCodecsTree.get(Utility.CODEC_UMTS_AMR).getAsInt());
                    map.replace(Utility.UMTS_AMR_2, gsmCodecsTree.get(Utility.CODEC_UMTS_AMR_2).getAsInt());
                    map.replace(Utility.HR_AMR, gsmCodecsTree.get(Utility.CODEC_HR_AMR).getAsInt());
                    map.replace(Utility.FR_AMR, gsmCodecsTree.get(Utility.CODEC_FR_AMR).getAsInt());
                    map.replace(Utility.GSM_EFR, gsmCodecsTree.get(Utility.CODEC_GSM_EFR).getAsInt());
                    map.replace(Utility.GSM_HR, gsmCodecsTree.get(Utility.CODEC_GSM_HR).getAsInt());
                    map.replace(Utility.GSM_FR, gsmCodecsTree.get(Utility.CODEC_GSM_FR).getAsInt());
                    map.replace(Utility.UMTS_EVS, gsmCodecsTree.get(Utility.CODEC_UMTS_EVS).getAsInt());
                    map.replace(Utility.OHR_AMR_WB, gsmCodecsTree.get(Utility.CODEC_OHR_AMR_WB).getAsInt());
                    map.replace(Utility.OFR_AMR_WB, gsmCodecsTree.get(Utility.CODEC_OFR_AMR_WB).getAsInt());
                    map.replace(Utility.OHR_AMR, gsmCodecsTree.get(Utility.CODEC_OHR_AMR).getAsInt());
                    map.replace(Utility.UMTS_AMR_WB, gsmCodecsTree.get(Utility.CODEC_UMTS_AMR_WB).getAsInt());
                    map.replace(Utility.FR_AMR_WB, gsmCodecsTree.get(Utility.CODEC_FR_AMR_WB).getAsInt());
                    map.replace(Utility.PDC_EFR, gsmCodecsTree.get(Utility.CODEC_PDC_EFR).getAsInt());
                }
            } catch (Exception e) {
                System.err.println("GSM Codecs not present");
            }
            try {
                if (temp2.getAsJsonObject(ParserUtility.SUPPORTED_CODEC_LIST).has(ParserUtility.CODEC_SUPPORTED_2)) {
                    // Set supported codecs 3G
                    JsonObject umtsCodecsTree = temp2.getAsJsonObject(ParserUtility.SUPPORTED_CODEC_LIST).getAsJsonObject(ParserUtility.CODEC_SUPPORTED_2);
                    map2.replace(Utility.TDMA_EFR, umtsCodecsTree.get(Utility.CODEC_TDMA_EFR).getAsInt());
                    map2.replace(Utility.UMTS_AMR, umtsCodecsTree.get(Utility.CODEC_UMTS_AMR).getAsInt());
                    map2.replace(Utility.UMTS_AMR_2, umtsCodecsTree.get(Utility.CODEC_UMTS_AMR_2).getAsInt());
                    map2.replace(Utility.HR_AMR, umtsCodecsTree.get(Utility.CODEC_HR_AMR).getAsInt());
                    map2.replace(Utility.FR_AMR, umtsCodecsTree.get(Utility.CODEC_FR_AMR).getAsInt());
                    map2.replace(Utility.GSM_EFR, umtsCodecsTree.get(Utility.CODEC_GSM_EFR).getAsInt());
                    map2.replace(Utility.GSM_HR, umtsCodecsTree.get(Utility.CODEC_GSM_HR).getAsInt());
                    map2.replace(Utility.GSM_FR, umtsCodecsTree.get(Utility.CODEC_GSM_FR).getAsInt());
                    map2.replace(Utility.UMTS_EVS, umtsCodecsTree.get(Utility.CODEC_UMTS_EVS).getAsInt());
                    map2.replace(Utility.OHR_AMR_WB, umtsCodecsTree.get(Utility.CODEC_OHR_AMR_WB).getAsInt());
                    map2.replace(Utility.OFR_AMR_WB, umtsCodecsTree.get(Utility.CODEC_OFR_AMR_WB).getAsInt());
                    map2.replace(Utility.OHR_AMR, umtsCodecsTree.get(Utility.CODEC_OHR_AMR).getAsInt());
                    map2.replace(Utility.UMTS_AMR_WB, umtsCodecsTree.get(Utility.CODEC_UMTS_AMR_WB).getAsInt());
                    map2.replace(Utility.FR_AMR_WB, umtsCodecsTree.get(Utility.CODEC_FR_AMR_WB).getAsInt());
                    map2.replace(Utility.PDC_EFR, umtsCodecsTree.get(Utility.CODEC_PDC_EFR).getAsInt());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            if (codecIds.size() == 1) {
                if (temp2.getAsJsonObject(ParserUtility.SUPPORTED_CODEC_LIST).has(ParserUtility.CODEC_SUPPORTED_1)) {
                    // Set supported codecs 3G
                    JsonObject umtsCodecsTree = temp2.getAsJsonObject(ParserUtility.SUPPORTED_CODEC_LIST).getAsJsonObject(ParserUtility.CODEC_SUPPORTED_1);
                    map2.replace(Utility.TDMA_EFR, umtsCodecsTree.get(Utility.CODEC_TDMA_EFR).getAsInt());
                    map2.replace(Utility.UMTS_AMR, umtsCodecsTree.get(Utility.CODEC_UMTS_AMR).getAsInt());
                    map2.replace(Utility.UMTS_AMR_2, umtsCodecsTree.get(Utility.CODEC_UMTS_AMR_2).getAsInt());
                    map2.replace(Utility.HR_AMR, umtsCodecsTree.get(Utility.CODEC_HR_AMR).getAsInt());
                    map2.replace(Utility.FR_AMR, umtsCodecsTree.get(Utility.CODEC_FR_AMR).getAsInt());
                    map2.replace(Utility.GSM_EFR, umtsCodecsTree.get(Utility.CODEC_GSM_EFR).getAsInt());
                    map2.replace(Utility.GSM_HR, umtsCodecsTree.get(Utility.CODEC_GSM_HR).getAsInt());
                    map2.replace(Utility.GSM_FR, umtsCodecsTree.get(Utility.CODEC_GSM_FR).getAsInt());
                    map2.replace(Utility.UMTS_EVS, umtsCodecsTree.get(Utility.CODEC_UMTS_EVS).getAsInt());
                    map2.replace(Utility.OHR_AMR_WB, umtsCodecsTree.get(Utility.CODEC_OHR_AMR_WB).getAsInt());
                    map2.replace(Utility.OFR_AMR_WB, umtsCodecsTree.get(Utility.CODEC_OFR_AMR_WB).getAsInt());
                    map2.replace(Utility.OHR_AMR, umtsCodecsTree.get(Utility.CODEC_OHR_AMR).getAsInt());
                    map2.replace(Utility.UMTS_AMR_WB, umtsCodecsTree.get(Utility.CODEC_UMTS_AMR_WB).getAsInt());
                    map2.replace(Utility.FR_AMR_WB, umtsCodecsTree.get(Utility.CODEC_FR_AMR_WB).getAsInt());
                    map2.replace(Utility.PDC_EFR, umtsCodecsTree.get(Utility.CODEC_PDC_EFR).getAsInt());
                }
            }
        }

        if (temp2.has(ParserUtility.UE_NETWORK_CAPABILITY)) {
            HashMap encrMapUmts = device.getUmtsCap().getEncrAlg();
            HashMap integrMapUMTS = device.getUmtsCap().getIntegrAlg();
            HashMap encrMapLte = device.getLteCap().getEncrAlg();
            HashMap integrMapLTE = device.getLteCap().getIntegrAlg();

            JsonObject ciphAlgTree = temp2.getAsJsonObject(ParserUtility.UE_NETWORK_CAPABILITY);
            encrMapLte.replace(Utility.EEA0, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA0).getAsInt());
            encrMapLte.replace(Utility.EEA1128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EEA1).getAsInt());
            encrMapLte.replace(Utility.EEA2128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EEA2).getAsInt());
            encrMapLte.replace(Utility.EEA3, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA3).getAsInt());
            encrMapLte.replace(Utility.EEA4, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA4).getAsInt());
            encrMapLte.replace(Utility.EEA5, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA5).getAsInt());
            encrMapLte.replace(Utility.EEA6, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA6).getAsInt());
            encrMapLte.replace(Utility.EEA7, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA7).getAsInt());
            encrMapUmts.replace(Utility.UEA0, ciphAlgTree.get(ParserUtility.NAS_EPS_UEA0).getAsInt());
            encrMapUmts.replace(Utility.UEA1, ciphAlgTree.get(ParserUtility.NAS_EPS_UEA1).getAsInt());
            encrMapUmts.replace(Utility.UEA2, ciphAlgTree.get(ParserUtility.NAS_EPS_UEA2).getAsInt());
            encrMapUmts.replace(Utility.UEA3, ciphAlgTree.get(ParserUtility.NAS_EPS_UEA3).getAsInt());
            encrMapUmts.replace(Utility.UEA4, ciphAlgTree.get(ParserUtility.NAS_EPS_UEA4).getAsInt());
            encrMapUmts.replace(Utility.UEA5, ciphAlgTree.get(ParserUtility.NAS_EPS_UEA5).getAsInt());
            encrMapUmts.replace(Utility.UEA6, ciphAlgTree.get(ParserUtility.NAS_EPS_UEA6).getAsInt());
            encrMapUmts.replace(Utility.UEA7, ciphAlgTree.get(ParserUtility.NAS_EPS_UEA7).getAsInt());

            integrMapLTE.replace(Utility.EIA0, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA0).getAsInt());
            integrMapLTE.replace(Utility.EIA1128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EIA1).getAsInt());
            integrMapLTE.replace(Utility.EIA2128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EIA2).getAsInt());
            integrMapLTE.replace(Utility.EIA3, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA3).getAsInt());
            integrMapLTE.replace(Utility.EIA4, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA4).getAsInt());
            integrMapLTE.replace(Utility.EIA5, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA5).getAsInt());
            integrMapLTE.replace(Utility.EIA6, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA6).getAsInt());
            integrMapLTE.replace(Utility.EIA7, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA7).getAsInt());
            integrMapUMTS.replace(Utility.UIA1, ciphAlgTree.get(ParserUtility.NAS_EPS_UIA1).getAsInt());
            integrMapUMTS.replace(Utility.UIA2, ciphAlgTree.get(ParserUtility.NAS_EPS_UIA2).getAsInt());
            integrMapUMTS.replace(Utility.UIA3, ciphAlgTree.get(ParserUtility.NAS_EPS_UIA3).getAsInt());
            integrMapUMTS.replace(Utility.UIA4, ciphAlgTree.get(ParserUtility.NAS_EPS_UIA4).getAsInt());
            integrMapUMTS.replace(Utility.UIA5, ciphAlgTree.get(ParserUtility.NAS_EPS_UIA5).getAsInt());
            integrMapUMTS.replace(Utility.UIA6, ciphAlgTree.get(ParserUtility.NAS_EPS_UIA6).getAsInt());
            integrMapUMTS.replace(Utility.UIA7, ciphAlgTree.get(ParserUtility.NAS_EPS_UIA7).getAsInt());
        }
        // Attach Type
        device.setAttachType(temp2.get(ParserUtility.NAS_EPS_ATTACH_TYPE).getAsInt());
        // SRVCC to GERAN UTRAN
        device.setSRVCCToGERANUTRANCapability(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_CAP_SRVCC_GERAN).getAsInt());
        // NRDC bit
        device.setNRDCbit(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(DUAL_CONNECTIVITY).getAsInt());
        // GSM Additional features
        device.getGsmCap().setUcs2Support(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_UCS2).getAsInt());
        device.getGsmCap().setSsScreeningIndicator(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_SS_SCR_IND).getAsString());
        device.getGsmCap().setPfcFeatureSupport(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_PFC).getAsInt());
        device.getGsmCap().setLcsSupport(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_LCS).getAsInt());
        device.getGsmCap().setInterRatPSHOtoUTRANIu(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_PS_HO_UTRAN_IU).getAsInt());
        device.getGsmCap().setInterRatPSHOtoUTRANS1(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_PS_HO_UTRAN_S1).getAsInt());
        device.getGsmCap().setEmmProceduresSupport(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_COMB_PROC).getAsInt());
        device.getGsmCap().setIsrSupport(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_ISR).getAsInt());
        device.getGsmCap().setEpcSupport(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_EPC).getAsInt());
        device.getGsmCap().setNfcSupport(temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY).get(ParserUtility.GSM_GMM_NET_CAP_NF).getAsInt());
        // MS CLassmark2
        device.getGsmCap().setEsIndSupport(temp2.getAsJsonObject(ParserUtility.MOBILE_STATION_CLASSMARK_2).get(ParserUtility.GSM_ES_IND).getAsInt());
        device.getGsmCap().setPsCapability(temp2.getAsJsonObject(ParserUtility.MOBILE_STATION_CLASSMARK_2).get(ParserUtility.GSM_PS_SUP_CAP).getAsInt());

        // 2G - Encr.
        HashMap<String, Integer> gsmEncrAlgMap = device.getGsmCap().getEncrAlg();
        JsonObject ciphAlgTree = temp2.getAsJsonObject(ParserUtility.MS_NETWORK_CAPABILITY);
        gsmEncrAlgMap.replace(Utility.GEA1, ciphAlgTree.get(ParserUtility.GSM_CAP_GEA1).getAsInt());
        ciphAlgTree = ciphAlgTree.getAsJsonObject(ParserUtility.GSM_CAP_EXT_GEA_TREE);
        gsmEncrAlgMap.replace(Utility.GEA2, ciphAlgTree.get(ParserUtility.GSM_CAP_GEA2).getAsInt());
        gsmEncrAlgMap.replace(Utility.GEA3, ciphAlgTree.get(ParserUtility.GSM_CAP_GEA3).getAsInt());
        gsmEncrAlgMap.replace(Utility.GEA4, ciphAlgTree.get(ParserUtility.GSM_CAP_GEA4).getAsInt());
        gsmEncrAlgMap.replace(Utility.GEA5, ciphAlgTree.get(ParserUtility.GSM_CAP_GEA5).getAsInt());
        gsmEncrAlgMap.replace(Utility.GEA6, ciphAlgTree.get(ParserUtility.GSM_CAP_GEA6).getAsInt());
        gsmEncrAlgMap.replace(Utility.GEA7, ciphAlgTree.get(ParserUtility.GSM_CAP_GEA7).getAsInt());
        // A5 Algorithms
        JsonObject msClassmark2CiphTree = temp2.getAsJsonObject(ParserUtility.MOBILE_STATION_CLASSMARK_2);
        // A5 algorithm is displayed in negative form (1= not available, 0 = available)
        int isA5Supported = msClassmark2CiphTree.get(ParserUtility.GSM_A5_1).getAsInt();
        if (isA5Supported == 1) {
            isA5Supported = 0;
        } else {
            isA5Supported = 1;
        }
        gsmEncrAlgMap.replace(Utility.A51, isA5Supported);
        gsmEncrAlgMap.replace(Utility.A52, msClassmark2CiphTree.get(ParserUtility.GSM_A5_2).getAsInt());
        gsmEncrAlgMap.replace(Utility.A53, msClassmark2CiphTree.get(ParserUtility.GSM_A5_3).getAsInt());
        JsonObject msClassmark3CiphTree = temp2.getAsJsonObject(ParserUtility.MOBILE_STATION_CLASSMARK_3).getAsJsonObject(ParserUtility.GSM_CLASSMARK_A5_TREE);

        gsmEncrAlgMap.replace(Utility.A54, msClassmark3CiphTree.get(ParserUtility.GSM_A5_4).getAsInt());
        gsmEncrAlgMap.replace(Utility.A55, msClassmark3CiphTree.get(ParserUtility.GSM_A5_5).getAsInt());
        gsmEncrAlgMap.replace(Utility.A56, msClassmark3CiphTree.get(ParserUtility.GSM_A5_6).getAsInt());
        gsmEncrAlgMap.replace(Utility.A57, msClassmark3CiphTree.get(ParserUtility.GSM_A5_7).getAsInt());

        // UMTS FDD Radio access capability
        JsonObject msClassmark3Tree = temp2.getAsJsonObject(ParserUtility.MOBILE_STATION_CLASSMARK_3);
        device.getUmtsCap().setFddRadioAccessTechnCap(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_UMTS_FDD_RAT_CAP).getAsInt());
        device.getUmtsCap().setMcps384TDDRatCap(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_UMTS_384_MCPS_TDD_RAT_CAP).getAsInt());
        device.getUmtsCap().setCdma2000Support(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_CDMA_2000_RAT_CAP).getAsInt());
        device.getUmtsCap().setMcps128TDDRatCap(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_UMTS_128_TDD_RAT_CAP).getAsInt());
        if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_GMSK_MULTISLOT_POWER_PROF)) {
            device.getGsmCap().setGmskMultislotPowerProfile(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_GMSK_MULTISLOT_POWER_PROF).getAsInt());
        } else {
            device.getGsmCap().setGmskMultislotPowerProfile(Utility.NOT_APPLICABLE_CODE);
        }
        if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_8PSK_MULTISLOT_POWER_PROF)) {
            device.getGsmCap().setPsk8MultislotPowerProfile(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_8PSK_MULTISLOT_POWER_PROF).getAsInt());
        } else {
            device.getGsmCap().setPsk8MultislotPowerProfile(Utility.NOT_APPLICABLE_CODE);
        }
        device.getGsmCap().setAdvancedReceiverPerformance(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_DOWNLINK_ADV_RECEIVER_PERF).getAsInt());
        device.getGsmCap().setDtmSupport(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_DTM_ENHANCEMENTS_CAP).getAsInt());
        device.getGsmCap().setPriorityBasedReselectionSupport(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_PRIO_BASED_RESEL_SUPPORT).getAsInt());
        if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_DTM_EGPRS_MULTI_SLOT_CLASS_PRESENT)) {
            if (msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_DTM_EGPRS_MULTI_SLOT_CLASS_PRESENT).getAsInt() == 1) {
                device.getGsmCap().setEgprsMultiSlotCLass(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_DTM_EGPRS_MULTI_SLOT_CLASS).getAsInt());
            } else {
                device.getGsmCap().setEgprsMultiSlotCLass(Utility.NOT_APPLICABLE_CODE);
            }
        } else {
            device.getGsmCap().setEgprsMultiSlotCLass(Utility.NOT_APPLICABLE_CODE);
        }
        if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_DTM_E_GPRS_MULTI_SLOT_INFO_PRESENT)) {
            if (msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_DTM_E_GPRS_MULTI_SLOT_INFO_PRESENT).getAsInt() == 1) {
                device.getGsmCap().setGprsMultiSlotClass(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_DTM_GPRS_MULTI_SLOT_CLASS).getAsInt());
            } else {
                device.getGsmCap().setGprsMultiSlotClass(Utility.NOT_APPLICABLE_CODE);
            }
        } else {
            device.getGsmCap().setGprsMultiSlotClass(Utility.NOT_APPLICABLE_CODE);
        }
        if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_SINGLE_SLOT_DTM_SUPPORTED))
            device.getGsmCap().setSingleSlotSupport(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_SINGLE_SLOT_DTM_SUPPORTED).getAsInt());
        if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_8PSK_STRUCT_TREE)) {
            JsonObject psk8StructTree = msClassmark3Tree.getAsJsonObject(ParserUtility.GSM_CLASSMARK3_8PSK_STRUCT_TREE);
            if (psk8StructTree.has(ParserUtility.GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY1_PRESENT)) {
                if (psk8StructTree.get(ParserUtility.GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY1_PRESENT).getAsInt() == 1) {
                    device.getGsmCap().setPsk8RFPowerCapability1(Integer.parseInt(psk8StructTree.get(ParserUtility.GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY1).getAsString().replace("0x", ""), 16));
                } else {
                    device.getGsmCap().setPsk8RFPowerCapability2(Utility.NOT_APPLICABLE_CODE);
                }
            } else {
                device.getGsmCap().setPsk8RFPowerCapability2(Utility.NOT_APPLICABLE_CODE);
            }
            if (psk8StructTree.has(ParserUtility.GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY2_PRESENT)) {
                if (psk8StructTree.get(ParserUtility.GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY2_PRESENT).getAsInt() == 1) {
                    device.getGsmCap().setPsk8RFPowerCapability2(Integer.parseInt(psk8StructTree.get(ParserUtility.GSM_CLASSMARK3_8PSK_RF_POWER_CAPABILITY2).getAsString().replace("0x", ""), 16));
                } else {
                    device.getGsmCap().setPsk8RFPowerCapability1(Utility.NOT_APPLICABLE_CODE);
                }
            } else {
                device.getGsmCap().setPsk8RFPowerCapability1(Utility.NOT_APPLICABLE_CODE);
            }
        } else {
            device.getGsmCap().setPsk8RFPowerCapability1(Utility.NOT_APPLICABLE_CODE);
            device.getGsmCap().setPsk8RFPowerCapability2(Utility.NOT_APPLICABLE_CODE);
        }
        if (temp2.getAsJsonObject(ParserUtility.MOBILE_STATION_CLASSMARK_2).get(ParserUtility.GSM_RF_POWER_CAPABILITY).getAsInt() == 7 &&
                msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_ASS_RADIO_CAP2)) {
            // RF Power Capability
            int associatedRadioCapability2 = msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_ASS_RADIO_CAP2).getAsInt();
            if (associatedRadioCapability2 > 2) {
                if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_GSM_400_BAND_INFO_PRESENT)) {
                    if (msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_GSM_400_BAND_INFO_PRESENT).getAsInt() == 1)
                        device.getGsmCap().setRfPowerCapability1(Integer.parseInt(msClassmark3Tree
                                .get(ParserUtility.GSM_CLASSMARK3_GSM_400_ASSOC_RADIO_CAP).getAsString().replace("0x", ""), 16));
                } else if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_GSM_850_BAND_INFO_PRESENT)) {
                    if (msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_GSM_850_BAND_INFO_PRESENT).getAsInt() == 1) {
                        device.getGsmCap().setRfPowerCapability1(Integer.parseInt(msClassmark3Tree
                                .get(ParserUtility.GSM_CLASSMARK3_GSM_850_ASSOC_RADIO_CAP).getAsString().replace("0x", ""), 16));
                    }
                } else if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_GSM_710_BAND_INFO_PRESENT)) {
                    if (msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_GSM_710_BAND_INFO_PRESENT).getAsInt() == 1) {
                        device.getGsmCap().setRfPowerCapability1(Integer.parseInt(msClassmark3Tree
                                .get(ParserUtility.GSM_CLASSMARK3_GSM_710_ASSOC_RADIO_CAP).getAsString().replace("0x", ""), 16));
                    }

                } else if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_GSM_750_BAND_INFO_PRESENT)) {
                    if (msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_GSM_750_BAND_INFO_PRESENT).getAsInt() == 1) {
                        device.getGsmCap().setRfPowerCapability1(Integer.parseInt(msClassmark3Tree
                                .get(ParserUtility.GSM_CLASSMARK3_GSM_750_ASSOC_RADIO_CAP).getAsString().replace("0x", ""), 16));
                    }

                } else if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_GSM_810_BAND_INFO_PRESENT)) {
                    if (msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_GSM_810_BAND_INFO_PRESENT).getAsInt() == 1) {
                        device.getGsmCap().setRfPowerCapability1(Integer.parseInt(msClassmark3Tree
                                .get(ParserUtility.GSM_CLASSMARK3_GSM_810_ASSOC_RADIO_CAP).getAsString().replace("0x", ""), 16));
                    }

                } else if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_GSM_900_BAND_INFO_PRESENT)) {
                    if (msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_GSM_900_BAND_INFO_PRESENT).getAsInt() == 1) {
                        device.getGsmCap().setRfPowerCapability1(Integer.parseInt(msClassmark3Tree
                                .get(ParserUtility.GSM_CLASSMARK3_GSM_900_ASSOC_RADIO_CAP).getAsString().replace("0x", ""), 16));
                    }

                }
                if (msClassmark3Tree.has(ParserUtility.GSM_CLASSMARK3_GSM_1900_BAND_INFO_PRESENT)) {
                    if (msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_GSM_1900_BAND_INFO_PRESENT).getAsInt() == 1) {
                        device.getGsmCap().setRfPowerCapability2(Integer.parseInt(msClassmark3Tree
                                .get(ParserUtility.GSM_CLASSMARK3_GSM_1900_ASSOC_RADIO_CAP).getAsString().replace("0x", ""), 16));
                    }
                }
            } else {
                device.getGsmCap().setRfPowerCapability2(associatedRadioCapability2);
                device.getGsmCap().setRfPowerCapability1(msClassmark3Tree.get(ParserUtility.GSM_CLASSMARK3_ASS_RADIO_CAP1).getAsInt());
            }
        } else {
            // TODO Delirium tremens. You have to check the other bands. You can't check gsm400 due to a wireshark bug
            // NOTE: this else is if associated radio capability 2 field is ABSENT
        }
        return device;
    }



    @Override
    public DUT capabilitiesSpecificParser(DUT dut, int objectIndex) {
        Mobile device = (Mobile) dut;
        ArrayList<String> keyValues = ParserUtility.ueCapJsonGeneralParsingTree;
        JsonObject temp1 = (JsonObject) pcapJsonArray.get(objectIndex);
        JsonObject temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0; k < keyValues.size() - 1; k++) {
            temp1 = temp1.getAsJsonObject(keyValues.get(k));
            temp2 = temp1.getAsJsonObject(keyValues.get(k + 1));
        }

        // START Add 5G
        JsonObject tmpObj = temp2; // save this object and restore it after parse 5G elements
        try {
            keyValues = Utility.ueCapJsonNRParsingTree;
            temp1 = temp2;
            temp2 = null;
            // -1 so that we do not encounter null pointer exceptions

            boolean check = true;
            int kk = 0;
            while(check && kk < keyValues.size()-1){
                temp1 = temp1.getAsJsonObject(keyValues.get(kk));
                temp2 = temp1.getAsJsonObject(keyValues.get(kk + 1));
                kk++;
                if (temp2 == null) {
                    check = false;
                }
            }
                temp1 = temp2;



            // get featureSetsEUTRA_r15_element if exists (used for MRDC Band Combinations)
            JsonObject featureSetsEUTRA_r15 = null;
            try {

                JsonObject obj = temp1.getAsJsonObject(ParserUtility.ITEM_0)
                        .getAsJsonObject(ParserUtility.LTE_RRC_UE_CAPABILITYRAT_CONTAINER_ELEMENT)
                        .getAsJsonObject(ParserUtility.LTE_RRC_UECAPABILITYRAT_CONTAINER_TREE)
                        .getAsJsonObject(ParserUtility.LTE_RRC_UE_EUTRA_CAPABILITY_ELEMENT);

                while (obj != null && !obj.has(ParserUtility.LTE_RRC_FEATURESETSEUTRA_R15_ELEMENT)) {
                    obj = obj.getAsJsonObject(ParserUtility.LTE_RRC_NONCRITICALEXTENSION_ELEMENT);
                }

                if (obj != null && obj.has(ParserUtility.LTE_RRC_FEATURESETSEUTRA_R15_ELEMENT))
                    featureSetsEUTRA_r15 = obj.getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETSEUTRA_R15_ELEMENT);
            } catch (Exception e) {
            } // featureSetsEUTRA_r15 not found


            boolean nrfound = true;
            JsonObject nr_rcc_UECapability = null;
            JsonObject mrdcCapability = null;
            //search NR Capabilities and MRDC Capability
            for (int k = 1; nrfound; k++) { // Items
                temp2 = temp1.getAsJsonObject(ParserUtility.ITEM_N + k);
                if (temp2 == null) { // Item k not exists
                    nrfound = false;
                    break;
                }
                // searching NR Capabilities
                if (nr_rcc_UECapability == null)
                    nr_rcc_UECapability = temp2.getAsJsonObject(ParserUtility.RRC_UECAPABILITY_RAT_CONTAINER_ELEMENT)
                            .getAsJsonObject(ParserUtility.RRC_UECAPABILITY_RAT_CONTAINER_TREE)
                            .getAsJsonObject(ParserUtility.NR_RRC_UECAPABILITY_ELEMENT);
                // searching MRDC Capability
                if (mrdcCapability == null)
                    mrdcCapability = temp2.getAsJsonObject(ParserUtility.RRC_UECAPABILITY_RAT_CONTAINER_ELEMENT)
                            .getAsJsonObject(ParserUtility.RRC_UECAPABILITY_RAT_CONTAINER_TREE)
                            .getAsJsonObject(ParserUtility.NR_RRC_UE_MRDC_CAPABILITY_ELEMENT);
            }


            if (mrdcCapability != null) {
                // read generalParametersMRDC
                JsonObject mrdcGeneralParams = mrdcCapability.getAsJsonObject(ParserUtility.NR_RRC_GENERALPARAMENTERSMRDC_ELEMENT);
                if (mrdcGeneralParams != null) {
                    // srb3
                    JsonPrimitive srb3 = mrdcGeneralParams.getAsJsonPrimitive(ParserUtility.NR_RRC_SRB3);
                    if (srb3 != null && srb3.getAsString().equals("0"))
                        device.setSrb3(true);
                    else
                        device.setSrb3(false);

                    // splitDRB-withUL-Both-MCG-SCG
                    JsonPrimitive splitdrb = mrdcGeneralParams.getAsJsonPrimitive(ParserUtility.NR_RRC_SPLITDRB_WITH_UL_BOTH_MCG_SCG);
                    if (splitdrb != null && splitdrb.getAsString().equals("0"))
                        device.setSplitdrb(true);
                    else
                        device.setSplitdrb(false);
                }

                // MRDC Band Combinations
                JsonObject rfParametersMRDC = mrdcCapability.getAsJsonObject(ParserUtility.NR_RRC_RF_PARAMETERSMRDC_ELEMENT);
                if (rfParametersMRDC != null) {
                    int m = 0;
                    device.setMrdcBandCombinations(new ArrayList<>());
                    int nBandCombinations = rfParametersMRDC.getAsJsonPrimitive(ParserUtility.NR_RRC_SUPPORTEDBANDCOMBINATIONLIST).getAsInt();

                    JsonObject suppBandsTree = rfParametersMRDC.getAsJsonObject(ParserUtility.NR_RRC_SUPPORTEDBANDCOMBINATIONLIST_TREE);
                    for (int i = 0; i < nBandCombinations; i++) {
                        JsonObject bandCombination = suppBandsTree.getAsJsonObject(ParserUtility.ITEM_N + i).getAsJsonObject(ParserUtility.NR_RRC_BANDCOMBINATION_ELEMENT);

                        int featureSetComb = bandCombination.getAsJsonPrimitive(ParserUtility.NR_RRC_FEATURESETCOMBINATION).getAsInt();

                        int nbands = bandCombination.getAsJsonPrimitive(ParserUtility.NR_RRC_BANDLIST).getAsInt();
                        JsonObject bands = bandCombination.getAsJsonObject(ParserUtility.NR_RRC_BANDLIST_TREE);

                        int set = 0;
                        int nSetsPerBand = -1;
                        do {
                            MRDCBandCombination bandComb = new MRDCBandCombination();
                            bandComb.setFeatureSetCombination(featureSetComb);

                            for (int j = 0; j < nbands; j++) {
                                JsonObject featureSetsPerBand = mrdcCapability.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETCOMBINATIONS_TREE).getAsJsonObject(ParserUtility.ITEM_N + featureSetComb)
                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETCOMBINATION_TREE).getAsJsonObject(ParserUtility.ITEM_N + j);
                                if (nSetsPerBand < 0)
                                    nSetsPerBand = featureSetsPerBand.getAsJsonPrimitive(ParserUtility.NR_RRC_FEATURESETSPERBAND).getAsInt();

                                JsonObject band = bands.getAsJsonObject(ParserUtility.ITEM_N + j);

                                // bandParam = 0 -> eutra, 1 -> nr
                                int bandParam = band.getAsJsonPrimitive(ParserUtility.NR_RRC_BANDPARAMETERS).getAsInt();
                                if (bandParam == 0) { //EUTRA
                                    JsonObject eutra = band.getAsJsonObject(ParserUtility.NR_RRC_BANDPARAMETERS_TREE).getAsJsonObject(ParserUtility.NR_RRC_EUTRA_ELEMENT);
                                    int bandID = eutra.getAsJsonPrimitive(ParserUtility.NR_RRC_BANDEUTRA).getAsInt();
                                    if (eutra.has(ParserUtility.NR_RRC_BANDWITHCLASSDL_EUTRA)) {
                                        MRDCBandEutra mrdcBandEutra = new MRDCBandEutra();
                                        mrdcBandEutra.setBandID(bandID);
                                        char bandClass = Utility.bandClassDecoder(eutra.getAsJsonPrimitive(ParserUtility.NR_RRC_BANDWITHCLASSDL_EUTRA).getAsInt());
                                        mrdcBandEutra.setBandClass(bandClass);
                                        int idx_featureSetEUTRA = featureSetsPerBand.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETSPERBAND_TREE).getAsJsonObject(ParserUtility.ITEM_N + set)
                                                .getAsJsonObject(ParserUtility.NR_RRC_FEATURESET_TREE).getAsJsonObject(ParserUtility.NR_RRC_EUTRA_ELEMENT)
                                                .getAsJsonPrimitive(ParserUtility.NR_RRC_DOWNLINKSETEUTRA).getAsInt();
                                        if (featureSetsEUTRA_r15 != null) {
                                            try {
                                                int featuresetdl_percc_id = featureSetsEUTRA_r15.getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETSDL_R15_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_N + (idx_featureSetEUTRA - 1))
                                                        .getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETSDL_R15_ELEMENT)
                                                        .getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETPERCC_LISTDL_R15_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_0)
                                                        .getAsJsonPrimitive(ParserUtility.LTE_RRC_FEATURESETDL_PERCC_ID_R15).getAsInt();
                                                JsonPrimitive layer = featureSetsEUTRA_r15.getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETSDL_PERCC_R15_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_N + featuresetdl_percc_id)
                                                        .getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETSDL_PERCC_R15_ELEMENT)
                                                        .getAsJsonPrimitive(ParserUtility.LTE_RRC_SUPPORTEDMIMO_CAPABILITYDL_MRDC_R15);
                                                mrdcBandEutra.setBandLayer(Utility.bandLayerDecoder(layer));
                                            } catch (Exception e) {
                                                System.err.println("Exception getting Feature DL PerCC info:\n" +
                                                        "\tBand combination: " + i + "/" + nBandCombinations + "\n" +
                                                        "\tFeature Set EUTRA " + (idx_featureSetEUTRA - 1));
                                                mrdcBandEutra.setBandLayer("-");
                                            }

                                        }
                                        bandComb.getDownlink4G().add(mrdcBandEutra);
                                    }
                                    if (eutra.has(ParserUtility.NR_RRC_BANDWITHCLASSUL_EUTRA)) {
                                        MRDCBandEutra mrdcBandEutra = new MRDCBandEutra();
                                        mrdcBandEutra.setBandID(bandID);
                                        char bandClass = Utility.bandClassDecoder(eutra.getAsJsonPrimitive(ParserUtility.NR_RRC_BANDWITHCLASSUL_EUTRA).getAsInt());
                                        mrdcBandEutra.setBandClass(bandClass);

                                        int idx_featureSetEUTRA = featureSetsPerBand.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETSPERBAND_TREE).getAsJsonObject(ParserUtility.ITEM_N + set)
                                                .getAsJsonObject(ParserUtility.NR_RRC_FEATURESET_TREE).getAsJsonObject(ParserUtility.NR_RRC_EUTRA_ELEMENT)
                                                .getAsJsonPrimitive(ParserUtility.NR_RRC_UPLINKSETEUTRA).getAsInt();
                                        if (featureSetsEUTRA_r15 != null) {
                                            try {
                                                int featuresetul_percc_id = featureSetsEUTRA_r15.getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETSUL_R15_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_N + (idx_featureSetEUTRA - 1))
                                                        .getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETSUL_R15_ELEMENT)
                                                        .getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETPERCC_LISTUL_R15_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_0)
                                                        .getAsJsonPrimitive(ParserUtility.LTE_RRC_FEATURESETUL_PERCC_ID_R15).getAsInt();
                                                JsonPrimitive layer = featureSetsEUTRA_r15.getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETSUL_PERCC_R15_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_N + featuresetul_percc_id)
                                                        .getAsJsonObject(ParserUtility.LTE_RRC_FEATURESETSUL_PERCC_R15_ELEMENT)
                                                        .getAsJsonPrimitive(ParserUtility.LTE_RRC_SUPPORTEDMIMO_CAPABILITYUL_MRDC_R15);

                                                mrdcBandEutra.setBandLayer(Utility.bandLayerDecoder(layer));
                                            } catch (Exception e) {
                                                System.err.println("Exception getting Feature UL PerCC info:\n" +
                                                        "\tBand combination: " + i + "/" + nBandCombinations + "\n" +
                                                        "\tFeature Set EUTRA " + (idx_featureSetEUTRA - 1));
                                                mrdcBandEutra.setBandLayer("-");
                                            }
                                        }
                                        bandComb.getUplink4G().add(mrdcBandEutra);
                                    }

                                } else if (bandParam == 1) { //NR
                                    //System.out.println();
                                    //System.out.println("featureSetCombination: "+featureSetComb);
                                    JsonObject nr = band.getAsJsonObject(ParserUtility.NR_RRC_BANDPARAMETERS_TREE).getAsJsonObject(ParserUtility.NR_RRC_NR_ELEMENT);
                                    int bandID = nr.getAsJsonPrimitive(ParserUtility.NR_RRC_BANDNR).getAsInt();
                                    if (nr.has(ParserUtility.NR_RRC_BANDWITHCLASSDL_NR)) {
                                        MRDCBandNR mrdcBandNR = new MRDCBandNR();
                                        mrdcBandNR.setBandID(bandID);
                                        char bandClass = Utility.bandClassDecoder(nr.getAsJsonPrimitive(ParserUtility.NR_RRC_BANDWITHCLASSDL_NR).getAsInt());
                                        mrdcBandNR.setBandClass(bandClass);
                                        int idx_featureSetNR = featureSetsPerBand.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETSPERBAND_TREE).getAsJsonObject(ParserUtility.ITEM_N + set)
                                                .getAsJsonObject(ParserUtility.NR_RRC_FEATURESET_TREE).getAsJsonObject(ParserUtility.NR_RRC_NR_ELEMENT)
                                                .getAsJsonPrimitive(ParserUtility.NR_RRC_DOWNLINKSETNR).getAsInt();
                                        //System.out.println("DownlinkSetNR: "+idx_featureSetNR);
                                        if (nr_rcc_UECapability != null) {
                                            try {
                                                int featuresetdl_percc_id = nr_rcc_UECapability.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETS_ELEMENT)
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETSDOWNLINK_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_N + (idx_featureSetNR - 1))
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETDOWNLINK_ELEMENT)
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETLISTPERDOWNLINKCC_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_0)
                                                        .getAsJsonPrimitive(ParserUtility.NR_RRC_FEATURESETDOWNLINKPERCC_ID).getAsInt();


                                                JsonObject featureset = nr_rcc_UECapability.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETS_ELEMENT)
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETSDOWNLINKPERCC_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_N + (featuresetdl_percc_id - 1))
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETDOWNLINKPERCC_ELEMENT);

                                                JsonObject featurest_v1540 = nr_rcc_UECapability.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETS_ELEMENT)
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETDOWNLINK_V1540_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_N + (featuresetdl_percc_id - 1))
                                                        .getAsJsonObject(NR_RRC_FEATURESETDOWNLINK_V1540_ELEMENT);

                                                mrdcBandNR.setDownlinkSetNR(idx_featureSetNR);

                                                if (featurest_v1540 != null) {
                                                    if (nr_rcc_UECapability.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETS_ELEMENT)
                                                            .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETDOWNLINK_V1540_TREE)
                                                            .getAsJsonObject(ParserUtility.ITEM_N + (featuresetdl_percc_id - 1))
                                                            .getAsJsonObject(NR_RRC_FEATURESETDOWNLINK_V1540_ELEMENT).has(NR_RRC_ADDITIONALDMRS_DL_ALT)) {
                                                        mrdcBandNR.setAdditionalDRMSsupportedByFeatureset(true);
                                                    }
                                                }

                                                JsonPrimitive layer = featureset.getAsJsonPrimitive(ParserUtility.NR_RRC_MAXNUMBERMIMO_LAYERSPDSCH);
                                                mrdcBandNR.setBandLayer(Utility.bandLayerDecoder(layer));

                                                int modulation = featureset.getAsJsonPrimitive(ParserUtility.NR_RRC_SUPPORTEDMODULATIONORDERDL).getAsInt();
                                                mrdcBandNR.setBandModulation(Utility.bandModulationDecoder(modulation));

                                                int scs = featureset.getAsJsonPrimitive(ParserUtility.NR_RRC_SUPPORTEDSUBCARRIERSPACINGDL).getAsInt();
                                                mrdcBandNR.setBandSCS(Utility.bandSCSDecoder(scs));

                                                int fr = featureset.getAsJsonPrimitive(ParserUtility.NR_RRC_SUPPORTEDBANDWIDTHDL).getAsInt();
                                                int width = featureset.getAsJsonObject(ParserUtility.NR_RRC_SUPPORTEDBANDWIDTHDL_TREE)
                                                        .getAsJsonPrimitive(ParserUtility.NR_RRC_FR_N + (fr == 0 ? 1 : 2)).getAsInt();
                                                mrdcBandNR.setBandWidth(Utility.bandWidthDecoder(fr, width));

                                                JsonPrimitive channelBW90Mhz = featureset.getAsJsonPrimitive(ParserUtility.NR_RRC_CHANNELBW_90MHZ);
                                                if (channelBW90Mhz != null)
                                                    mrdcBandNR.setChannelBW90Mhz("Y");
                                                else
                                                    mrdcBandNR.setChannelBW90Mhz("N");

                                            } catch (Exception e) {
                                                System.err.println("Exception getting Feature DL PerCC info:\n" +
                                                        "\tBand combination: " + i + "/" + nBandCombinations + "\n" +
                                                        "\tFeature Set NR " + (idx_featureSetNR - 1));
                                                mrdcBandNR.setBandLayer("-");
                                                mrdcBandNR.setBandModulation("-");
                                                mrdcBandNR.setBandSCS("-");
                                                mrdcBandNR.setBandWidth("-");
                                                mrdcBandNR.setChannelBW90Mhz("-");
                                            }
                                        }
                                        bandComb.getDownlink5G().add(mrdcBandNR);
                                    }
                                    if (nr.has(ParserUtility.NR_RRC_BANDWITHCLASSUL_NR)) {
                                        MRDCBandNR mrdcBandNR = new MRDCBandNR();
                                        mrdcBandNR.setBandID(bandID);
                                        char bandClass = Utility.bandClassDecoder(nr.getAsJsonPrimitive(ParserUtility.NR_RRC_BANDWITHCLASSUL_NR).getAsInt());
                                        mrdcBandNR.setBandClass(bandClass);
                                        int idx_featureSetNR = featureSetsPerBand.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETSPERBAND_TREE).getAsJsonObject(ParserUtility.ITEM_N + set)
                                                .getAsJsonObject(ParserUtility.NR_RRC_FEATURESET_TREE).getAsJsonObject(ParserUtility.NR_RRC_NR_ELEMENT)
                                                .getAsJsonPrimitive(ParserUtility.NR_RRC_UPLINKSETNR).getAsInt();

                                        if (nr_rcc_UECapability != null) {
                                            try {
                                                int featuresetul_percc_id = nr_rcc_UECapability.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETS_ELEMENT)
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETSUPLINK_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_N + (idx_featureSetNR - 1))
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETUPLINK_ELEMENT)
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETLISTPERUPLINKCC_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_0)
                                                        .getAsJsonPrimitive(ParserUtility.NR_RRC_FEATURESETUPLINKPERCC_ID).getAsInt();


                                                JsonObject featureset = nr_rcc_UECapability.getAsJsonObject(ParserUtility.NR_RRC_FEATURESETS_ELEMENT)
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETSUPLINKPERCC_TREE)
                                                        .getAsJsonObject(ParserUtility.ITEM_N + (featuresetul_percc_id - 1))
                                                        .getAsJsonObject(ParserUtility.NR_RRC_FEATURESETUPLINKPERCC_ELEMENT);

                                                JsonPrimitive layer = featureset.getAsJsonObject(ParserUtility.NR_RRC_MIMO_CB_PUSCH_ELEMENT)
                                                        .getAsJsonPrimitive(ParserUtility.NR_RRC_MAXNUMBERMIMO_LAYERSCB_PUSCH);
                                                mrdcBandNR.setBandLayer(Utility.bandLayerDecoderUL(layer));

                                                int modulation = featureset.getAsJsonPrimitive(ParserUtility.NR_RRC_SUPPORTEDMODULATIONORDERUL).getAsInt();
                                                mrdcBandNR.setBandModulation(Utility.bandModulationDecoder(modulation));

                                                int scs = featureset.getAsJsonPrimitive(ParserUtility.NR_RRC_SUPPORTEDSUBCARRIERSPACINGUL).getAsInt();
                                                mrdcBandNR.setBandSCS(Utility.bandSCSDecoder(scs));

                                                int fr = featureset.getAsJsonPrimitive(ParserUtility.NR_RRC_SUPPORTEDBANDWIDTHUL).getAsInt();
                                                int width = featureset.getAsJsonObject(ParserUtility.NR_RRC_SUPPORTEDBANDWIDTHUL_TREE)
                                                        .getAsJsonPrimitive(ParserUtility.NR_RRC_FR_N + (fr == 0 ? 1 : 2)).getAsInt();
                                                mrdcBandNR.setBandWidth(Utility.bandWidthDecoder(fr, width));

                                                JsonPrimitive channelBW90Mhz = featureset.getAsJsonPrimitive(ParserUtility.NR_RRC_CHANNELBW_90MHZ);
                                                if (channelBW90Mhz != null)
                                                    mrdcBandNR.setChannelBW90Mhz("Y");
                                                else
                                                    mrdcBandNR.setChannelBW90Mhz("N");
                                            } catch (Exception e) {
                                                System.err.println("Exception getting Feature UL PerCC info:\n" +
                                                        "\tBand combination: " + i + "/" + nBandCombinations + "\n" +
                                                        "\tFeature Set NR " + (idx_featureSetNR - 1));
                                                mrdcBandNR.setBandLayer("-");
                                                mrdcBandNR.setBandModulation("-");
                                                mrdcBandNR.setBandSCS("-");
                                                mrdcBandNR.setBandWidth("-");
                                                mrdcBandNR.setChannelBW90Mhz("-");

                                            }
                                        }
                                        bandComb.getUplink5G().add(mrdcBandNR);
                                    }
                                } else {
                                    System.out.println("Band Parameter not recognized:" + bandParam);
                                }
                            }
                            device.getMrdcBandCombinations().add(bandComb);
                            set++;
                        } while (set < nSetsPerBand);
                    }
                    //appliedfreqband
                    ArrayList<Band> appliedFreqBandList = new ArrayList<>();
                    int nFreqBandListFilter = rfParametersMRDC.getAsJsonPrimitive(ParserUtility.NR_RRC_APPLIED_FREQBANDLIST_FILTER).getAsInt();
                    JsonObject suppFreqBandListFilterTree = rfParametersMRDC.getAsJsonObject(ParserUtility.NR_RRC_APPLIED_FREQBANDLIST_FILTER_TREE);
                    for (int i = 0; i < nFreqBandListFilter; i++) {
                        if (suppFreqBandListFilterTree.getAsJsonObject(ParserUtility.ITEM_N + i).getAsJsonObject(ParserUtility.NR_RRC_FREQBANDINFO_TREE)
                                .has(ParserUtility.NR_RRC_BANDINFORMATION_EUTRA_ELEMENT)) {
                            JsonPrimitive bandEUTRA = suppFreqBandListFilterTree.getAsJsonObject(ParserUtility.ITEM_N + i).getAsJsonObject(ParserUtility.NR_RRC_FREQBANDINFO_TREE)
                                    .getAsJsonObject(ParserUtility.NR_RRC_BANDINFORMATION_EUTRA_ELEMENT).getAsJsonPrimitive(ParserUtility.NR_RRC_BANDEUTRA);
                            Band tempBand = new Band(bandEUTRA.getAsInt(), i);
                            appliedFreqBandList.add(tempBand);
                        }
                        if (suppFreqBandListFilterTree.getAsJsonObject(ParserUtility.ITEM_N + i).getAsJsonObject(ParserUtility.NR_RRC_FREQBANDINFO_TREE)
                                .has(ParserUtility.NR_RRC_BANDINFORMATION_NR_ELEMENT)) {
                            JsonPrimitive bandNR = suppFreqBandListFilterTree.getAsJsonObject(ParserUtility.ITEM_N + i).getAsJsonObject(ParserUtility.NR_RRC_FREQBANDINFO_TREE)
                                    .getAsJsonObject(ParserUtility.NR_RRC_BANDINFORMATION_NR_ELEMENT).getAsJsonPrimitive(NR_RRC_BANDNR);
                            Band tempBand = new Band(bandNR.getAsInt(), i);
                            appliedFreqBandList.add(tempBand);
                        }
                    }
                    device.setAppliedFreqBandList(appliedFreqBandList);

                }

            }


            if (nr_rcc_UECapability != null) {
                // PARSE accessStratumRelease 5G
                device.setAccessStratumReleaseNR(nr_rcc_UECapability.get(ParserUtility.NR_RRC_ACCESS_STRATUM_RELEASE).getAsInt());
                temp1 = nr_rcc_UECapability.getAsJsonObject(ParserUtility.NR_RRC_RF_PARAMETERS_ELEMENT);
                //temp2 = null;
                int nbands = temp1.get(ParserUtility.NR_RCC_SUPPORTEDBANDLISTNR).getAsInt();
                temp1 = temp1.getAsJsonObject(ParserUtility.NR_RRC_SUPPORTEDBANDLISTNR_TREE);

                //rateMatchingResrcSemi-Static
                try {
                    JsonObject temp5 = nr_rcc_UECapability.getAsJsonObject(ParserUtility.RRC_NR_PHY_PARAMETERS_ELEMENT);
                    if (temp5.has(RRC_NR_PHY_PARAMETERSCOMMON_ELEMENT)) {
                        if (temp5.getAsJsonObject(RRC_NR_PHY_PARAMETERSCOMMON_ELEMENT).has(RRC_NR_RATEMATCHINGSEMISTATIC)) {
                            dut.setRateMatchingResrcSetSemiStatic(true);
                        }
                        if (temp5.getAsJsonObject(RRC_NR_PHY_PARAMETERSCOMMON_ELEMENT).has(RRC_NR_RATEMATCHINGCTRLRESRCSETDYNAMIC)) {
                            dut.setRateMatchingCtrlResrcSetDynamic(true);
                        }
                        if (temp5.getAsJsonObject(RRC_NR_PHY_PARAMETERSCOMMON_ELEMENT).has(RRC_NR_RATEMATCHINGRESRSCSETDYNAMIC)) {
                            dut.setRateMatchingResrcSetDynamic(true);
                        }
                        if (temp5.getAsJsonObject(NR_RRC_PHY_PARAMETERS_FR1_ELEMENT).has(NR_RRC_PDCCH_MONITORING_SINGLE_OCCASION)) {
                            dut.setPdcchMonitoringSingleOccasion(true);
                        }

                    }
                } catch (Exception e) {
                    System.out.println("Parameters common not found");
                }


                // PARSE bandNR
                ArrayList<MRDCBandNR> nrBands = new ArrayList<>();
                HashSet<Integer> presentBands = new HashSet<>();
                HashMap<Integer, Integer> scs90MHz = new HashMap<>();
                HashMap<Integer, Integer> scs100MHz = new HashMap<>();
                System.out.println();
                System.out.println("NR Bands Informations: ");
                for (int i = 0; i < nbands; i++) {
                    MRDCBandNR band = new MRDCBandNR();
                    temp2 = temp1.getAsJsonObject(ParserUtility.ITEM_N + i);
                    temp2 = temp2.getAsJsonObject(ParserUtility.NR_RRC_BANDNR_ELEMENT);
                    band.setBandID(i);
                    band.setBandID(temp2.get(ParserUtility.NR_RRC_BANDNR).getAsInt());
                    System.out.println();
                    nrBands.add(band);
                    System.out.println("====================================");
                    System.out.println("           NR Band:" + temp2.get(ParserUtility.NR_RRC_BANDNR).getAsInt());
                    System.out.println("====================================");

                    if (temp2.has(NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT)) {
                        if (temp2.getAsJsonObject(NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT)
                                .getAsJsonObject(NR_RRC_CODEBOOKPARAMETERS_ELEMENT)
                                .has(NR_RRC_TYPE_1)) {
                            JsonObject type1 = temp2.getAsJsonObject(NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT)
                                    .getAsJsonObject(NR_RRC_CODEBOOKPARAMETERS_ELEMENT)
                                    .getAsJsonObject(NR_RRC_TYPE_1);
                            if (type1.getAsJsonObject(NR_RRC_SINGLEPANEL_ELEMENT).has(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST)) {
                                JsonPrimitive rsResList = type1.getAsJsonObject(NR_RRC_SINGLEPANEL_ELEMENT)
                                        .getAsJsonPrimitive(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST);
                                int itemsNumber = rsResList.getAsInt();
                                JsonObject singlePanel = temp2.getAsJsonObject(NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT)
                                        .getAsJsonObject(NR_RRC_CODEBOOKPARAMETERS_ELEMENT)
                                        .getAsJsonObject(NR_RRC_TYPE_1).getAsJsonObject(NR_RRC_SINGLEPANEL_ELEMENT);
                                System.out.println("Single Panel Values");
                                for (int k = 0; k < itemsNumber; k++) {
                                    try {
                                        JsonObject list = singlePanel
                                                .getAsJsonObject(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST_TREE)
                                                .getAsJsonObject(ITEM_N + k)
                                                .getAsJsonObject(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST_ELEMENT);
                                        int maxNumberTxPortsPerResource = list.
                                                getAsJsonPrimitive(NR_RRC_MAXNUMBERTXPORTSSRC).getAsInt();
                                        int maxNumberResourcesPerBand = list.
                                                getAsJsonPrimitive(NR_RRC_MAXNUMBERRESOURCESPERBAND).getAsInt();
                                        int maxTotalNumPortsPerBand = list.getAsJsonPrimitive(NR_RRC_TOTALNUMTXPORTSPERBAND)
                                                .getAsInt();
                                        System.out.println("[Item " + k + "]");
                                        System.out.println("    - SupportedCSI RS Resource -");
                                        System.out.println("        Max number port src: " + supportedCSIRSresourceMapper(maxNumberTxPortsPerResource));
                                        System.out.println("        Max number rsrc per band: " + maxNumberResourcesPerBand);
                                        System.out.println("        Max number ports per band: " + maxTotalNumPortsPerBand);
                                        System.out.println();
                                    } catch (Exception e) {
                                        System.err.println("Single Panel not supported");
                                    }
                                }
                                if (type1.has(NR_RRC_MULTIPANEL_ELEMENT)) {
                                    System.out.println("Multi Panel Values");
                                    if (type1.getAsJsonObject(NR_RRC_MULTIPANEL_ELEMENT).has(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST)) {
                                        JsonPrimitive rsResList2 = type1.getAsJsonObject(NR_RRC_MULTIPANEL_ELEMENT)
                                                .getAsJsonPrimitive(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST);
                                        int itemsNumber2 = rsResList2.getAsInt();
                                        JsonObject multiPanel = temp2.getAsJsonObject(NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT)
                                                .getAsJsonObject(NR_RRC_CODEBOOKPARAMETERS_ELEMENT)
                                                .getAsJsonObject(NR_RRC_TYPE_1).getAsJsonObject(NR_RRC_MULTIPANEL_ELEMENT);
                                        System.out.println();
                                        for (int w = 0; w < itemsNumber2; w++) {
                                            try {
                                                JsonObject list = multiPanel
                                                        .getAsJsonObject(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST_TREE)
                                                        .getAsJsonObject(ITEM_N + w)
                                                        .getAsJsonObject(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST_ELEMENT);
                                                int maxNumberTxPortsPerResource = list.
                                                        getAsJsonPrimitive(NR_RRC_MAXNUMBERTXPORTSSRC).getAsInt();
                                                int maxNumberResourcesPerBand = list.
                                                        getAsJsonPrimitive(NR_RRC_MAXNUMBERRESOURCESPERBAND).getAsInt();
                                                int maxTotalNumPortsPerBand = list.getAsJsonPrimitive(NR_RRC_TOTALNUMTXPORTSPERBAND)
                                                        .getAsInt();
                                                System.out.println("[Item " + w + "]");
                                                System.out.println("    - SupportedCSI RS Resource -");
                                                System.out.println("        Max number port src: " + supportedCSIRSresourceMapper(maxNumberTxPortsPerResource));
                                                System.out.println("        Max number rsrc per band: " + maxNumberResourcesPerBand);
                                                System.out.println("        Max number ports per band: " + maxTotalNumPortsPerBand);
                                                System.out.println();
                                            } catch (Exception e) {
                                                System.out.println("Band " + band.getBandID() + " does not support multi panel codebook parameters");
                                            }

                                        }
                                    }

                                } else {
                                    System.out.println("Multi Panel not supported");
                                    System.out.println();
                                }
                            }
                        }




/*
                    if(temp2.has(NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT)){
                        if(temp2.getAsJsonObject(NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT)
                                .getAsJsonObject(NR_RRC_CODEBOOKPARAMETERS_ELEMENT)
                                .getAsJsonObject(NR_RRC_TYPE_1).getAsJsonObject(NR_RRC_SINGLEPANEL_ELEMENT)
                                .has(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST)){
                            JsonPrimitive rsResList = temp2.getAsJsonObject(NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT)
                                    .getAsJsonObject(NR_RRC_CODEBOOKPARAMETERS_ELEMENT)
                                    .getAsJsonObject(NR_RRC_TYPE_1).getAsJsonObject(NR_RRC_SINGLEPANEL_ELEMENT)
                                    .getAsJsonPrimitive(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST);
                            int itemsNumber = rsResList.getAsInt();
                            JsonObject singlePanel =  temp2.getAsJsonObject(NR_RRC_MIMOPARAMETERSPERBAND_ELEMENT)
                                    .getAsJsonObject(NR_RRC_CODEBOOKPARAMETERS_ELEMENT)
                                    .getAsJsonObject(NR_RRC_TYPE_1).getAsJsonObject(NR_RRC_SINGLEPANEL_ELEMENT);
                            for(int k = 0;k<itemsNumber;k++){
                                try {
                                    JsonObject list = singlePanel
                                            .getAsJsonObject(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST_TREE)
                                            .getAsJsonObject(ITEM_N + k)
                                            .getAsJsonObject(NR_RRC_SUPPORTEDCSI_RS_RESOURCE_LIST_ELEMENT);
                                    int maxNumberTxPortsPerResource = list.
                                            getAsJsonPrimitive(NR_RRC_MAXNUMBERTXPORTSSRC).getAsInt();
                                    int maxNumberResourcesPerBand = list.
                                            getAsJsonPrimitive(NR_RRC_MAXNUMBERRESOURCESPERBAND).getAsInt();
                                    int maxTotalNumPortsPerBand = list.getAsJsonPrimitive(NR_RRC_TOTALNUMTXPORTSPERBAND)
                                            .getAsInt();
                                    System.out.println("ITEM "+k);
                                    System.out.println("SupportedCSI-RS-Resource");
                                    System.out.println("Max number port src:"+maxNumberTxPortsPerResource);
                                    System.out.println("Max number rsrc per band:"+maxNumberResourcesPerBand);
                                    System.out.println("Max number ports per band:"+maxTotalNumPortsPerBand);
                                    System.out.println();
                                }
                                catch (Exception e){
                                    System.out.println("This band doesn't support codebook parameters");
                                }
                            }
                        }

 */
                    }


                    //rateMatchingLTE_CRS
                    if (temp2.has(NR_RRC_RATEMATCHINGLTE_CRS)) {
                        band.setRateMatchingLTE_CRS(true);
                    }
                    System.out.println("RateMatchingLTE-CRS support: " + band.isRateMatchingLTE_CRS());
                    System.out.println();
                    for (MRDCBandCombination mrdcBandComb : device.getMrdcBandCombinations()) {
                        for (MRDCBandNR mrdcBandNR : mrdcBandComb.getDownlink5G()) {
                            if (!presentBands.contains(mrdcBandNR.getBandID())) {
                                presentBands.add(mrdcBandNR.getBandID());
                                if (mrdcBandNR.getChannelBW90Mhz().equals("Y"))
                                    scs90MHz.put(mrdcBandNR.getBandID(), 1);
                                else
                                    scs90MHz.put(mrdcBandNR.getBandID(), 0);
                                if (mrdcBandNR.getBandWidth().equals("mhz100"))
                                    scs100MHz.put(mrdcBandNR.getBandID(), 1);
                                else
                                    scs100MHz.put(mrdcBandNR.getBandID(), 0);
                            }
                        }
                    }

                    JsonObject channelbw_1530DL = temp2.getAsJsonObject(ParserUtility.NR_RRC_CHANNELBWS_DL_v1530_TREE);
                    JsonObject channelbw_1530UL = temp2.getAsJsonObject(ParserUtility.NR_RRC_CHANNELBWS_UL_v1530_TREE);

                    System.out.println("************************");
                    System.out.println("Sub Carrier Spacing informations for band: " + band.getBandID());
                    System.out.println("************************");
                    //DL Parsing
                    if (channelbw_1530DL != null && channelbw_1530DL.has(ParserUtility.NR_RRC_FR1_ELEMENT)) {
                        device.setChannelBW_1530DL_support(true);
                        JsonObject temp3 = channelbw_1530DL.getAsJsonObject(ParserUtility.NR_RRC_FR1_ELEMENT);
                        String scs15khzToString = "";
                        String scs30khzToString = "";
                        String scs60khzToString = "";
                        int decValueOf15kHz = 0;
                        int decValueOf30kHz = 0;
                        int decValueOf60kHz = 0;
                        ArrayList<String> scs15khzValues = new ArrayList<>();
                        ArrayList<String> scs30khzValues = new ArrayList<>();
                        ArrayList<String> scs60khzValues = new ArrayList<>();
                        JsonPrimitive scs15khz = temp3.getAsJsonPrimitive(ParserUtility.NR_RRC_SCS_15KHZ);
                        JsonPrimitive scs30khz = temp3.getAsJsonPrimitive(ParserUtility.NR_RRC_SCS_30KHZ);
                        JsonPrimitive scs60khz = temp3.getAsJsonPrimitive(ParserUtility.NR_RRC_SCS_60KHZ);
                        if (scs15khz != null) {
                            scs15khzToString = scs15khz.getAsString();
                        }
                        if (scs30khz != null) {
                            scs30khzToString = scs30khz.getAsString();
                        }
                        if (scs60khz != null) {
                            scs60khzToString = scs60khz.getAsString();
                        }
                        scs15khzToString = scs15khzToString.replaceAll("[:]", "");
                        scs30khzToString = scs30khzToString.replaceAll("[:]", "");
                        scs60khzToString = scs60khzToString.replaceAll("[:]", "");

                        //why /64? it works, only god knows why
                        if (!scs15khzToString.equals(""))
                            decValueOf15kHz = Integer.parseInt(scs15khzToString, 16) / 64;
                        if (!scs30khzToString.equals(""))
                            decValueOf30kHz = Integer.parseInt(scs30khzToString, 16) / 64;
                        if (!scs60khzToString.equals(""))
                            decValueOf60kHz = Integer.parseInt(scs60khzToString, 16) / 64;
                        //introduce padding in order to have 10 digits binaries
                        String scs15khzToStringBin = String.format("%10s", Integer.toBinaryString(decValueOf15kHz)).replace(' ', '0');
                        String scs30khzToStringBin = String.format("%10s", Integer.toBinaryString(decValueOf30kHz)).replace(' ', '0');
                        String scs60khzToStringBin = String.format("%10s", Integer.toBinaryString(decValueOf60kHz)).replace(' ', '0');

                        // if there are values in the string && they're not all 0s
                        if (scs15khzToString != null && scs15khzToStringBin.length() != 1) {
                            if (decValueOf15kHz != 0) {
                                System.out.println("[DL] 15kHz: ");
                                if (scs15khzToStringBin.length() > 1) {
                                    band.setSCS_15KHz_values((scs15khzToStringBin));
                                    for (int k = 0; k < scs15khzToStringBin.length(); k++) {
                                        scs15khzValues.add(scsDecoder(scs15khzToStringBin.charAt(k), k));
                                        //System.out.println(scs15khzValues.get(k));
                                    }
                                    boolean flag = false;
                                    for (Map.Entry<Integer, Integer> entry : scs90MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs15khzToStringBin = scs15khzToStringBin.concat(String.valueOf(entry.getValue()));
                                            scs15khzValues.add(scsDecoder(entry.getValue(), 10));
                                            band.setSCS_15KHz_values((scs15khzToStringBin));
                                            //System.out.println(scsDecoder(entry.getValue(), 10));
                                            band.setScs15_string_val(scs15khzValues);
                                        }
                                    }
                                    for (Map.Entry<Integer, Integer> entry : scs100MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs15khzToStringBin = scs15khzToStringBin.concat(String.valueOf(entry.getValue()));
                                            scs15khzValues.add(scsDecoder(entry.getValue(), 11));
                                            band.setSCS_15KHz_values((scs15khzToStringBin));
                                            //System.out.println(scsDecoder(entry.getValue(), 11));
                                            band.setScs15_string_val(scs15khzValues);
                                        }
                                    }
                                    if (flag == false) {
                                        scs15khzValues.add(scsDecoder(-1, 10));
                                        scs15khzValues.add(scsDecoder(-1, 11));
                                        scs15khzToStringBin = scs15khzToStringBin.concat("*").concat("*");
                                        band.setSCS_15KHz_values((scs15khzToStringBin));
                                        band.setScs15_string_val(scs15khzValues);
                                    }
                                }
                                System.out.println(scs15khzToStringBin);
                            } else if (decValueOf15kHz == 0) {
                                scsDecoder(0, 10);
                                scsDecoder(0, 11);
                                band.setSCS_15KHz_values(scs15khzToStringBin.concat(scsDecoder(0, 10).concat(scsDecoder(0, 11))));
                            } else if (scs15khzToStringBin.length() == 1 || scs15khzToStringBin.isEmpty()) {
                                System.out.println("[DL] 15kHz: Not Supported");
                            }
                        }
                        if (scs30khzToString != null && scs30khzToStringBin.length() != 1) {
                            if (decValueOf30kHz != 0) {
                                System.out.println("[DL] 30kHz: ");
                                if (scs30khzToStringBin.length() > 1) {
                                    band.setSCS_30Khz_values((scs30khzToStringBin));
                                    for (int k = 0; k < scs30khzToStringBin.length(); k++) {
                                        scs30khzValues.add(scsDecoder(scs30khzToStringBin.charAt(k), k));
                                        // System.out.println(scs30khzValues.get(k));
                                    }
                                    band.setScs30_string_val(scs30khzValues);
                                    boolean flag = false;
                                    for (Map.Entry<Integer, Integer> entry : scs90MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs30khzToStringBin = scs30khzToStringBin.concat(String.valueOf(entry.getValue()));
                                            scs30khzValues.add(scsDecoder(entry.getValue(), 10));
                                            band.setSCS_30Khz_values((scs30khzToStringBin));
                                            //System.out.println(scsDecoder(entry.getValue(), 10));
                                            band.setScs30_string_val(scs30khzValues);
                                        }
                                    }
                                    for (Map.Entry<Integer, Integer> entry : scs100MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs30khzToStringBin = scs30khzToStringBin.concat(String.valueOf(entry.getValue()));
                                            scs30khzValues.add(scsDecoder(entry.getValue(), 11));
                                            band.setSCS_30Khz_values((scs30khzToStringBin));
                                            //System.out.println(scsDecoder(entry.getValue(), 11));
                                            band.setScs30_string_val(scs30khzValues);
                                        }
                                    }
                                    if (flag == false) {
                                        scs30khzValues.add(scsDecoder(-1, 10));
                                        scs30khzValues.add(scsDecoder(-1, 11));
                                        scs30khzToStringBin = scs30khzToStringBin.concat("*").concat("*");
                                        band.setSCS_30Khz_values((scs30khzToStringBin));
                                        band.setScs30_string_val(scs30khzValues);
                                    }
                                }
                                System.out.println(scs30khzToStringBin);
                            } else if (decValueOf30kHz == 0) {
                                scs30khzToStringBin = scs30khzToStringBin.concat("0").concat("0");
                                band.setSCS_30Khz_values(scs30khzToStringBin);
                                scsDecoder(0, 10);
                                scsDecoder(0, 11);
                            } else if (scs30khzToStringBin.length() == 1 || scs30khzToStringBin.isEmpty()) {
                                System.out.println("[DL] 30kHz: Not Supported");
                            }
                        }
                        if (scs60khzToString != null && scs60khzToStringBin.length() != 1) {
                            if (decValueOf60kHz != 0) {
                                System.out.println("[DL] 60kHz: ");
                                if (scs60khzToStringBin.length() > 1) {
                                    band.setSCS_60KHz_values((scs60khzToStringBin));
                                    for (int k = 0; k < scs60khzToStringBin.length(); k++) {
                                        scs60khzValues.add(scsDecoder(scs60khzToStringBin.charAt(k), k));
                                        // System.out.println(scs60khzValues.get(k));
                                    }
                                    band.setScs60_string_val(scs60khzValues);
                                    boolean flag = false;
                                    for (Map.Entry<Integer, Integer> entry : scs90MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs60khzToStringBin = scs60khzToStringBin.concat(String.valueOf(entry.getValue()));
                                            scs60khzValues.add(scsDecoder(entry.getValue(), 10));
                                            band.setSCS_60KHz_values((scs60khzToStringBin));
                                            // System.out.println(scsDecoder(entry.getValue(), 10));
                                            band.setScs60_string_val(scs60khzValues);
                                        }
                                    }
                                    for (Map.Entry<Integer, Integer> entry : scs100MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs60khzToStringBin = scs60khzToStringBin.concat(String.valueOf(entry.getValue()));
                                            scs60khzValues.add(scsDecoder(entry.getValue(), 11));
                                            band.setSCS_60KHz_values((scs60khzToStringBin));
                                            // System.out.println(scsDecoder(entry.getValue(), 11));
                                            band.setScs60_string_val(scs60khzValues);
                                        }
                                    }
                                    if (flag == false) {
                                        scs60khzValues.add(scsDecoder(-1, 10));
                                        scs60khzValues.add(scsDecoder(-1, 11));
                                        scs60khzToStringBin = scs60khzToStringBin.concat("*").concat("*");
                                        band.setSCS_60KHz_values((scs60khzToStringBin));
                                        band.setScs60_string_val(scs60khzValues);
                                    }
                                }
                            } else if (decValueOf60kHz == 0) {
                                band.setSCS_60KHz_values(scs60khzToStringBin);
                                scsDecoder(0, 10);
                                scsDecoder(0, 11);
                            } else if (scs60khzToStringBin.length() == 1 || scs60khzToStringBin.isEmpty()) {
                                System.out.println("[DL] 60kHz: Not Supported");
                            }
                        }
                    } else if (channelbw_1530DL == null) {
                        device.setChannelBW_1530DL_support(false);
                        band.setSCS_15KHz_values("-");
                        band.setSCS_30Khz_values("-");
                        band.setSCS_60KHz_values("-");
                    }

                    // UL Parsing
                    if (channelbw_1530UL != null && channelbw_1530UL.has(ParserUtility.NR_RRC_FR1_ELEMENT)) {
                        device.setChannelBW_1530UL_support(true);
                        JsonObject temp4 = channelbw_1530UL.getAsJsonObject(ParserUtility.NR_RRC_FR1_ELEMENT);
                        String scs15khzToString_UL = "";
                        String scs30khzToString_UL = "";
                        String scs60khzToString_UL = "";
                        int decValueOf15kHz_UL = 0;
                        int decValueOf30kHz_UL = 0;
                        int decValueOf60kHz_UL = 0;
                        ArrayList<String> scs15khzValues_UL = new ArrayList<>();
                        ArrayList<String> scs30khzValues_UL = new ArrayList<>();
                        ArrayList<String> scs60khzValues_UL = new ArrayList<>();

                        JsonPrimitive scs15khz_UL = temp4.getAsJsonPrimitive(ParserUtility.NR_RRC_SCS_15KHZ);
                        JsonPrimitive scs30khz_UL = temp4.getAsJsonPrimitive(ParserUtility.NR_RRC_SCS_30KHZ);
                        JsonPrimitive scs60khz_UL = temp4.getAsJsonPrimitive(ParserUtility.NR_RRC_SCS_60KHZ);

                        if (scs15khz_UL != null) {
                            scs15khzToString_UL = scs15khz_UL.getAsString();
                        }
                        if (scs30khz_UL != null) {
                            scs30khzToString_UL = scs30khz_UL.getAsString();
                        }
                        if (scs60khz_UL != null) {
                            scs60khzToString_UL = scs60khz_UL.getAsString();
                        }

                        scs15khzToString_UL = scs15khzToString_UL.replaceAll("[:]", "");
                        scs30khzToString_UL = scs30khzToString_UL.replaceAll("[:]", "");
                        scs60khzToString_UL = scs60khzToString_UL.replaceAll("[:]", "");

                        if (!scs15khzToString_UL.equals(""))
                            decValueOf15kHz_UL = Integer.parseInt(scs15khzToString_UL, 16) / 64;
                        if (!scs30khzToString_UL.equals(""))
                            decValueOf30kHz_UL = Integer.parseInt(scs30khzToString_UL, 16) / 64;
                        if (!scs60khzToString_UL.equals(""))
                            decValueOf60kHz_UL = Integer.parseInt(scs60khzToString_UL, 16) / 64;
                        //introduce padding in order to have 10 digits binaries
                        String scs15khzToStringBin_UL = String.format("%10s", Integer.toBinaryString(decValueOf15kHz_UL)).replace(' ', '0');
                        String scs30khzToStringBin_UL = String.format("%10s", Integer.toBinaryString(decValueOf30kHz_UL)).replace(' ', '0');
                        String scs60khzToStringBin_UL = String.format("%10s", Integer.toBinaryString(decValueOf60kHz_UL)).replace(' ', '0');

                        if (scs15khzToString_UL != null && scs15khzToStringBin_UL.length() != 1) {
                            if (decValueOf15kHz_UL != 0) {
                                System.out.println("[UL] 15kHz: ");
                                if (scs15khzToStringBin_UL.length() > 1) {
                                    band.setSCS_15KHz_values_UL((scs15khzToStringBin_UL));
                                    for (int k = 0; k < scs15khzToStringBin_UL.length(); k++) {
                                        scs15khzValues_UL.add(scsDecoder(scs15khzToStringBin_UL.charAt(k), k));
                                        //  System.out.println(scs15khzValues_UL.get(k));
                                    }
                                    band.setScs15_string_val_UL(scs15khzValues_UL);
                                    boolean flag = false;
                                    for (Map.Entry<Integer, Integer> entry : scs90MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs15khzToStringBin_UL = scs15khzToStringBin_UL.concat(String.valueOf(entry.getValue()));
                                            scs15khzValues_UL.add(scsDecoder(entry.getValue(), 10));
                                            band.setSCS_15KHz_values_UL((scs15khzToStringBin_UL));
                                            //  System.out.println(scsDecoder(entry.getValue(), 10));
                                            band.setScs15_string_val_UL(scs15khzValues_UL);
                                        }
                                    }
                                    for (Map.Entry<Integer, Integer> entry : scs100MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs15khzToStringBin_UL = scs15khzToStringBin_UL.concat(String.valueOf(entry.getValue()));
                                            scs15khzValues_UL.add(scsDecoder(entry.getValue(), 11));
                                            band.setSCS_15KHz_values_UL((scs15khzToStringBin_UL));
                                            //    System.out.println(scsDecoder(entry.getValue(), 11));
                                            band.setScs15_string_val_UL(scs15khzValues_UL);
                                        }
                                    }
                                    if (flag == false) {
                                        scs15khzValues_UL.add(scsDecoder(-1, 10));
                                        scs15khzValues_UL.add(scsDecoder(-1, 11));
                                        scs15khzToStringBin_UL = scs15khzToStringBin_UL.concat("*").concat("*");
                                        band.setSCS_15KHz_values_UL((scs15khzToStringBin_UL));
                                        band.setScs15_string_val_UL(scs15khzValues_UL);
                                    }
                                }
                                System.out.println(scs15khzToStringBin_UL);
                            } else if (decValueOf15kHz_UL == 0) {
                                band.setSCS_15KHz_values_UL(scs15khzToStringBin_UL);
                                scsDecoder(0, 10);
                                scsDecoder(0, 11);
                            } else if (scs15khzToStringBin_UL.length() == 1 || scs15khzToStringBin_UL.isEmpty()) {
                                System.out.println("[UL] 15kHz: Not Supported");
                            }

                        }
                        if (scs30khzToString_UL != null && scs30khzToStringBin_UL.length() != 1) {
                            if (decValueOf30kHz_UL != 0) {
                                System.out.println("[UL] 30kHz: ");
                                if (scs30khzToStringBin_UL.length() > 1) {
                                    band.setSCS_30KHz_values_UL((scs30khzToStringBin_UL));
                                    for (int k = 0; k < scs30khzToStringBin_UL.length(); k++) {
                                        scs30khzValues_UL.add(scsDecoder(scs30khzToStringBin_UL.charAt(k), k));
                                        //System.out.println(scs30khzValues_UL.get(k));
                                    }
                                    band.setScs30_string_val_UL(scs30khzValues_UL);
                                    boolean flag = false;
                                    for (Map.Entry<Integer, Integer> entry : scs90MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs30khzToStringBin_UL = scs30khzToStringBin_UL.concat(String.valueOf(entry.getValue()));
                                            scs30khzValues_UL.add(scsDecoder(entry.getValue(), 10));
                                            band.setSCS_30KHz_values_UL((scs30khzToStringBin_UL));
                                            // System.out.println(scsDecoder(entry.getValue(), 10));
                                            band.setScs30_string_val_UL(scs30khzValues_UL);
                                        }
                                    }
                                    for (Map.Entry<Integer, Integer> entry : scs100MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs30khzToStringBin_UL = scs30khzToStringBin_UL.concat(String.valueOf(entry.getValue()));
                                            scs30khzValues_UL.add(scsDecoder(entry.getValue(), 11));
                                            band.setSCS_30KHz_values_UL((scs30khzToStringBin_UL));
                                            //System.out.println(scsDecoder(entry.getValue(), 11));
                                            band.setScs30_string_val_UL(scs30khzValues_UL);
                                        }
                                    }
                                    if (flag == false) {
                                        scs30khzValues_UL.add(scsDecoder(-1, 10));
                                        scs30khzValues_UL.add(scsDecoder(-1, 11));
                                        scs30khzToStringBin_UL = scs30khzToStringBin_UL.concat("*").concat("*");
                                        band.setSCS_30KHz_values_UL((scs30khzToStringBin_UL));
                                        band.setScs30_string_val_UL(scs30khzValues_UL);
                                    }
                                }
                                System.out.println(scs30khzToStringBin_UL);
                            } else if (decValueOf30kHz_UL == 0) {
                                band.setSCS_30KHz_values_UL(scs30khzToStringBin_UL);
                                scsDecoder(0, 10);
                                scsDecoder(0, 11);
                            } else if (scs30khzToStringBin_UL.length() == 1 || scs30khzToStringBin_UL.isEmpty()) {
                                System.out.println("[UL] 30kHz: Not Supported");
                            }
                        }

                        if (scs60khzToString_UL != null && scs60khzToStringBin_UL.length() != 1) {
                            if (decValueOf60kHz_UL != 0) {
                                System.out.println("[UL] 60kHz: ");
                                if (scs60khzToStringBin_UL.length() > 1) {
                                    band.setSCS_60KHz_values_UL((scs60khzToStringBin_UL));
                                    for (int k = 0; k < scs60khzToStringBin_UL.length(); k++) {
                                        scs60khzValues_UL.add(scsDecoder(scs60khzToStringBin_UL.charAt(k), k));
                                        // System.out.println(scs60khzValues_UL.get(k));
                                    }
                                    band.setScs60_string_val(scs60khzValues_UL);
                                    boolean flag = false;
                                    for (Map.Entry<Integer, Integer> entry : scs90MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs60khzToStringBin_UL = scs30khzToStringBin_UL.concat(String.valueOf(entry.getValue()));
                                            scs60khzValues_UL.add(scsDecoder(entry.getValue(), 10));
                                            band.setSCS_60KHz_values_UL((scs60khzToStringBin_UL));
                                            // System.out.println(scsDecoder(entry.getValue(), 10));
                                            band.setScs60_string_val_UL(scs60khzValues_UL);
                                        }
                                    }
                                    for (Map.Entry<Integer, Integer> entry : scs100MHz.entrySet()) {
                                        if (band.getBandID() == entry.getKey()) {
                                            flag = true;
                                            scs60khzToStringBin_UL = scs60khzToStringBin_UL.concat(String.valueOf(entry.getValue()));
                                            scs60khzValues_UL.add(scsDecoder(entry.getValue(), 11));
                                            band.setSCS_60KHz_values_UL((scs60khzToStringBin_UL));
                                            // System.out.println(scsDecoder(entry.getValue(), 11));
                                            band.setScs60_string_val_UL(scs60khzValues_UL);
                                        }
                                    }
                                    if (flag == false) {
                                        scs60khzValues_UL.add(scsDecoder(-1, 10));
                                        scs60khzValues_UL.add(scsDecoder(-1, 11));
                                        scs60khzToStringBin_UL = scs60khzToStringBin_UL.concat("*").concat("*");
                                        band.setSCS_60KHz_values_UL((scs60khzToStringBin_UL));
                                        band.setScs60_string_val_UL(scs60khzValues_UL);
                                    }
                                }
                                // System.out.println(scs60khzValues_UL);
                            } else if (decValueOf60kHz_UL == 0) {
                                band.setSCS_60KHz_values_UL(scs60khzToStringBin_UL);
                                scsDecoder(0, 10);
                                scsDecoder(0, 11);
                            } else if (scs60khzToStringBin_UL.length() == 1 || scs60khzToStringBin_UL.isEmpty()) {
                                System.out.println("[UL] 60kHz: Not Supported");
                            }
                        }

                    } else if (channelbw_1530UL == null) {
                        device.setChannelBW_1530UL_support(false);
                        band.setSCS_15KHz_values_UL("-");
                        band.setSCS_30KHz_values_UL("-");
                        band.setSCS_60KHz_values_UL("-");
                    }
                    device.setNrBands(nrBands);
                    System.out.println();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            temp2 = tmpObj; // restore
        }

        // END 5G

        keyValues = Utility.ueCapJsonParsingTree;
        temp1 = temp2;
        temp2 = null;
        boolean check = true;
        int kk = 0;
        while(check && kk < keyValues.size() -1){
            temp1 = temp1.getAsJsonObject(keyValues.get(kk));
            temp2 = temp1.getAsJsonObject(keyValues.get(kk+1));
            kk++;
            if (temp2 == null) {
                check = false;
            }
        }
        /*
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0; k < keyValues.size() - 1; k++) {
            temp1 = temp1.getAsJsonObject(keyValues.get(k));
            temp2 = temp1.getAsJsonObject(keyValues.get(k + 1));
        }

         */
        // PARSE FGI, AS Release
        device = (Mobile) fgiRel8Parser(temp2, device);
        device.setAccessStratumRelease(temp2.get(ParserUtility.RRC_ACCESS_STRATUM_RELEASE).getAsInt());
        // ue tx antenna selection support
        try {
            if (temp2.has(ParserUtility.LTE_RRC_PHY_LAYERS_ELEMENT)) {
                device.getLteCap().setTxAntennaSelectionSupport(temp2.getAsJsonObject(ParserUtility.LTE_RRC_PHY_LAYERS_ELEMENT).get(ParserUtility.LTE_RRC_TX_ANTENNA_SEL_SUPPORT).getAsInt());
                device.getLteCap().setUeSpecificRefsSigsSupport(temp2.getAsJsonObject(ParserUtility.LTE_RRC_PHY_LAYERS_ELEMENT).get(ParserUtility.LTE_RRC_UE_SPECIFIC_REFS_SIG_SUPPORTED).getAsInt());
            } else {
                device.getLteCap().setTxAntennaSelectionSupport(Utility.ERROR_CODE);
                device.getLteCap().setUeSpecificRefsSigsSupport(Utility.ERROR_CODE);
            }
        } catch (Exception e) {

        }
        device = (Mobile) supportedBandsParser(temp2, device);
        device.setCategory(temp2.get(ParserUtility.RRC_UE_CATEGORY).getAsInt());
        String tmp = device.getCategoriesAsString();
        device.setCategoriesAsString(tmp.concat(" ").concat(temp2.get(ParserUtility.RRC_UE_CATEGORY).getAsString()));
        JsonObject temp4 = null;
        JsonObject temp3 = null;
        // GSM Supported bands
        if (temp2.getAsJsonObject(ParserUtility.LTE_RRC_INTER_RAT_ELEMENT).has(ParserUtility.LTE_RRC_GERAN_ELEMENT)) {
            device.setGsmSupport(true);
            ArrayList<GSMBand> gsmBands = new ArrayList<>();
            JsonObject gsmBandsTree = temp2.getAsJsonObject(ParserUtility.LTE_RRC_INTER_RAT_ELEMENT).getAsJsonObject(ParserUtility.LTE_RRC_GERAN_ELEMENT)
                    .getAsJsonObject(ParserUtility.LTE_RRC_SUPPORTED_BAND_LIST_GERAN_TREE);
            int supportedBandsNumber = 0;
            try {
                supportedBandsNumber = temp2.getAsJsonObject(ParserUtility.LTE_RRC_INTER_RAT_ELEMENT).getAsJsonObject(ParserUtility.LTE_RRC_GERAN_ELEMENT)
                        .get(ParserUtility.LTE_RRC_SUPPORTED_BAND_LIST_GERAN).getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonObject tempBand;
            String itemIndex = "Item ";
            for (int r = 0; r < supportedBandsNumber; r++) {
                tempBand = gsmBandsTree.getAsJsonObject(itemIndex.concat(Integer.toString(r)));
                GSMBand b = new GSMBand(tempBand.get(ParserUtility.LTE_RRC_SUPPORTED_BAND_GERAN).getAsInt(), r);
                gsmBands.add(r, b);
            }
            device.getGsmCap().setSupportedBands(gsmBands);
            // Inter rat ps ho support
            device.setInterRatPSHOToGERAN(temp2.getAsJsonObject(ParserUtility.LTE_RRC_INTER_RAT_ELEMENT)
                    .getAsJsonObject(ParserUtility.LTE_RRC_GERAN_ELEMENT).get(ParserUtility.LTE_RRC_INTER_RAT_PS_HO_TO_GERAN).getAsInt());
        } else {
            device.setGsmSupport(false);
            device.setInterRatPSHOToGERAN(Utility.ERROR_CODE);
        }
        // UMTS Supported Bands
        if (temp2.getAsJsonObject(ParserUtility.LTE_RRC_INTER_RAT_ELEMENT).has(ParserUtility.LTE_RRC_UTRA_FDD_ELEMENT)) {
            device.setUmtsSupport(true);
            ArrayList<UMTSBand> umtsBands = new ArrayList<>();
            JsonObject umtsBandsTree = temp2.getAsJsonObject(ParserUtility.LTE_RRC_INTER_RAT_ELEMENT).getAsJsonObject(ParserUtility.LTE_RRC_UTRA_FDD_ELEMENT)
                    .getAsJsonObject(ParserUtility.LTE_RRC_SUPPORTED_BAND_LIST_UTRA_TREE);
            int umtsSupportedBandsNumber = 0;
            try {
                umtsSupportedBandsNumber = temp2.getAsJsonObject(ParserUtility.LTE_RRC_INTER_RAT_ELEMENT).getAsJsonObject(ParserUtility.LTE_RRC_UTRA_FDD_ELEMENT)
                        .get(ParserUtility.LTE_RRC_SUPPORTED_BAND_LIST_UTRA).getAsInt();
            } catch (Exception e) {
                e.printStackTrace();
            }
            JsonObject tempBandUmts;
            String itemIndex = "Item ";
            for (int r = 0; r < umtsSupportedBandsNumber; r++) {
                tempBandUmts = umtsBandsTree.getAsJsonObject(itemIndex.concat(Integer.toString(r)));
                UMTSBand b = new UMTSBand(tempBandUmts.get(ParserUtility.LTE_RRC_SUPPORTED_BAND_UTRA_FDD).getAsInt() + 1, r);
                umtsBands.add(r, b);
            }
            device.getUmtsCap().setSupportedBands(umtsBands);
        } else {
            device.setUmtsSupport(false);
        }

        // Rohc Profiles r15
        HashMap<String, Integer> rohcProfiles_r15 = device.getLteCap().getSupportedRohcProfiles_r15();
        JsonObject rohcProfilesTree_r15 = temp2.getAsJsonObject(ParserUtility.LTE_RRC_PARAMETER_PDCP_ELEMENT)
                .getAsJsonObject(ParserUtility.LTE_RRC_SUPPORTED_ROHC_PROFILES_ELEMENT);
        rohcProfiles_r15.replace(Utility.ROHC_1_r15, rohcProfilesTree_r15.get(ParserUtility.LTE_RRC_ROHC_PROFILE_1_r15).getAsInt());
        rohcProfiles_r15.replace(Utility.ROHC_2_r15, rohcProfilesTree_r15.get(ParserUtility.LTE_RRC_ROHC_PROFILE_2_r15).getAsInt());
        rohcProfiles_r15.replace(Utility.ROHC_3_r15, rohcProfilesTree_r15.get(ParserUtility.LTE_RRC_ROHC_PROFILE_3_r15).getAsInt());
        rohcProfiles_r15.replace(Utility.ROHC_4_r15, rohcProfilesTree_r15.get(ParserUtility.LTE_RRC_ROHC_PROFILE_4_r15).getAsInt());
        rohcProfiles_r15.replace(Utility.ROHC_6_r15, rohcProfilesTree_r15.get(ParserUtility.LTE_RRC_ROHC_PROFILE_6_r15).getAsInt());
        rohcProfiles_r15.replace(Utility.ROHC_101_r15, rohcProfilesTree_r15.get(ParserUtility.LTE_RRC_ROHC_PROFILE_101_r15).getAsInt());
        rohcProfiles_r15.replace(Utility.ROHC_102_r15, rohcProfilesTree_r15.get(ParserUtility.LTE_RRC_ROHC_PROFILE_102_r15).getAsInt());
        rohcProfiles_r15.replace(Utility.ROHC_103_r15, rohcProfilesTree_r15.get(ParserUtility.LTE_RRC_ROHC_PROFILE_103_r15).getAsInt());
        rohcProfiles_r15.replace(Utility.ROHC_104_r15, rohcProfilesTree_r15.get(ParserUtility.LTE_RRC_ROHC_PROFILE_104_r15).getAsInt());
        int supportedRohcCounter_r15 = 0;
        for (String keyName : rohcProfiles_r15.keySet()) {
            String key = keyName.toString();
            String value = rohcProfiles_r15.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                supportedRohcCounter_r15++;
            }
        }
        // Set support for uncompress header
        if (supportedRohcCounter_r15 > 0) rohcProfiles_r15.replace(Utility.ROHC_0_r15, 1);
        device.getLteCap().setSupportedRohcProfiles_r15(rohcProfiles_r15);


        //Cycle on non critical extensions elements and non late critical extensions elements
        boolean flag = true;
        temp1 = temp2.getAsJsonObject(ParserUtility.RRC_NON_CRITICAL_EXTENSION_ELEMENT);
        temp4 = temp1.getAsJsonObject(ParserUtility.RRC_LATE_NONCRITICALEXTENSION_TREE);
        // string to find JsonObject of supported bands (256, 64qam)
        temp3 = temp1.getAsJsonObject(ParserUtility.RRC_RFPARAMETERS_V1250_ELEMENT);
        // NON CRITICAL EXTENSIONS
        while (flag) {
            // Manage end of packet
            if (temp1 == null) {
                flag = false;
                // check previous tree if packet is finished
                temp1 = temp2;
            } else {
                try {
                    if (temp1.has(ParserUtility.LTE_RRC_SON_PARAMETERS_R9_ELEMENT))
                        sonParametersR9Parser(temp1, device);
                    if (temp1.has(ParserUtility.LTE_RRC_INTER_RAT_PARAMETERS_GERAN_920_ELEMENT))
                        interRatParametersGERAN920Parser(device, temp1);
                    if (temp1.has(ParserUtility.LTE_RRC_INTER_RAT_PARAMETERS_UTRA_920_ELEMENT))
                        interRatParametersUTRAN920Parser(device, temp1);
                    if ((temp1.has(ParserUtility.RRC_FGI_REL10_TREE)) && (temp1.has(ParserUtility.RRC_FGI_REL10_TREE)))
                        fgiRel10Parser(temp1, device);
                    if (temp1.has(ParserUtility.RRC_RF_PARAMETERS_V1020)) bandCombinationsParser1020(temp1, device);
                    if (temp1.has(ParserUtility.RRC_RF_PARAMETERS_V1060)) bcsParser(temp1, device);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                // no matter if overwrite is done
                device = categoryParser(temp1, device);
                // R10 FDD TDD FGIs
                if (temp1.has(ParserUtility.LTE_RRC_FDD_ADD_EUTRA_CAP_1060)) fgirel10TDDFDDParser(temp1, device);
                // RF Parameters 1180
                if (temp1.has(ParserUtility.LTE_RRC_RF_PARAMETERS_1180_ELEMENT)) rf1180ParametersParser(temp1, device);
                if (temp1.has(ParserUtility.RRC_RF_PARAMETERS_V1430)) rfParametersv1430(temp1, device);
                if (temp1.has(LTE_RRC_RFPARAMETERS_V1310_ELEMENT)) {
                    try {
                        rfParametersv1310(temp1, device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if (temp4 != null) bandCombinationParser10i0(temp4, device);
                if (temp4 != null) bandCombinationParser10d0(temp4, device);


                if ((temp3 != null) && temp3.has(ParserUtility.RRC_SUPPORTED_BAND_LIST_EUTRA_v1250_TREE))
                    supportedModulationsParser(temp3, device);
                temp4 = temp1.getAsJsonObject(ParserUtility.RRC_LATE_NONCRITICALEXTENSION_TREE);
                // in non late critical extensions
                if (temp4 != null) device = (Mobile) fgiRel9AddR9Parser(temp4, device);
                // temp1,3 switch to next element
                //if (temp1 instanceof JsonObject)
                try {
                    temp1 = temp1.getAsJsonObject(ParserUtility.RRC_NON_CRITICAL_EXTENSION_ELEMENT);
                } catch (Exception e) {
                    System.out.println("End of non critical extensions");
                    break;
                }
                if (temp1 != null) temp3 = temp1.getAsJsonObject(ParserUtility.RRC_RFPARAMETERS_V1250_ELEMENT);
            }
        }
        if (device.isCa10i0Support() || device.isCa1020Support()) {
            device.orderBandsCombination();
        } else if (device.isCa1310Support()) {
            device.orderBandsCombination1310();
        }
        return device;
    }

    @Override
    public Integer capabilityPacketParser(JsonObject cap, int i) {


        boolean nrfound = true;
        ArrayList<String> keyValues = ParserUtility.ueCapJsonGeneralParsingTree;
        JsonObject temp1 = (JsonObject) pcapJsonArray.get(i);
        JsonObject temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0; k < keyValues.size() - 1; k++) {
            temp1 = temp1.getAsJsonObject(keyValues.get(k));
            temp2 = temp1.getAsJsonObject(keyValues.get(k + 1));
        }
        JsonObject tmpObj = temp2;
        try {
            keyValues = Utility.ueCapJsonNRParsingTree;
            temp1 = temp2;
            temp2 = null;
            // -1 so that we do not encounter null pointer exceptions
            for (int k = 0; k < keyValues.size() - 1; k++) {
                temp1 = temp1.getAsJsonObject(keyValues.get(k));
                temp2 = temp1.getAsJsonObject(keyValues.get(k + 1));
            }
            temp1 = temp2; //ItemsList
            JsonObject nr_rcc_UECapability = null;
            JsonObject mrdcCapability = null;
            //search NR Capabilities and MRDC Capability
            for (int k = 1; nrfound; k++) { // Items
                temp2 = temp1.getAsJsonObject(ParserUtility.ITEM_N + k);
                if (temp2 == null) { // Item k not exists
                    nrfound = false;
                    break;
                }
                // searching NR Capabilities
                if (nr_rcc_UECapability == null)
                    nr_rcc_UECapability = temp2.getAsJsonObject(ParserUtility.RRC_UECAPABILITY_RAT_CONTAINER_ELEMENT)
                            .getAsJsonObject(ParserUtility.RRC_UECAPABILITY_RAT_CONTAINER_TREE)
                            .getAsJsonObject(ParserUtility.NR_RRC_UECAPABILITY_ELEMENT);
                // searching MRDC Capability
                if (mrdcCapability == null)
                    mrdcCapability = temp2.getAsJsonObject(ParserUtility.RRC_UECAPABILITY_RAT_CONTAINER_ELEMENT)
                            .getAsJsonObject(ParserUtility.RRC_UECAPABILITY_RAT_CONTAINER_TREE)
                            .getAsJsonObject(ParserUtility.NR_RRC_UE_MRDC_CAPABILITY_ELEMENT);
                if (nr_rcc_UECapability != null || mrdcCapability != null) {
                    return i;
                }
                if (( //parsed packet index == last index of capability messages
                        nr_rcc_UECapability == null && mrdcCapability == null) && k == keyValues.size()) {
                    return -i;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    /**
     * Private method to check "CATEGORY_REGEX" field in UE Cap
     *
     * @param j current JsonObject in json tree
     */
    private Mobile categoryParser(JsonObject j, Mobile dut) {
        Set<Map.Entry<String, JsonElement>> entries = j.entrySet();
        for (Map.Entry<String, JsonElement> entry : entries) {
            String cat = Utility.CATEGORY_REGEX;
            String catDL = Utility.CATEGORY_DL_REGEX;
            String catUL = Utility.CATEGORY_UL_REGEX;
            // Parse key string to check if it is related to DL,UL or general
            if (entry.getKey().toLowerCase().matches(cat)) {
                if (entry.getKey().matches(catDL)) {
                    dut.setCategoryDL(entry.getValue().getAsInt());
                    String tmp = dut.getCategoryDLAsString();
                    dut.setCategoryDLAsString(tmp.concat(" ").concat(Integer.toString(entry.getValue().getAsInt())));
                } else if (entry.getKey().matches(catUL)) {
                    dut.setCategoryUL(entry.getValue().getAsInt());
                    String tmp = dut.getCategoryULAsString();
                    dut.setCategoryULAsString(tmp.concat(" ").concat(Integer.toString(entry.getValue().getAsInt())));
                } else {
                    dut.setCategory(entry.getValue().getAsInt());
                    String tmp = dut.getCategoriesAsString();
                    dut.setCategoriesAsString(tmp.concat(" ").concat(Integer.toString(entry.getValue().getAsInt())));
                }
            }
            // SPECIAL CATEGORY VALUES PARSING
            // v1310 DL

            if
            (entry.getKey().equalsIgnoreCase(ParserUtility.RRC_UE_CATEGORY_DL_v1310)) {
                System.out.println(entry.getKey() + "  " + entry.getValue());
                String cat1310dl = Integer.toString(entry.getValue().getAsInt());
                System.out.println(cat1310dl.getClass().getName());
                if (cat1310dl.equals("0")) {
                    dut.setCategoryDLAsString("oneBis");
                    dut.setCategoryDL(0);
                }
            }

            // v1350 DL
            if
            (entry.getKey().equalsIgnoreCase(ParserUtility.RRC_UE_CATEGORY_DL_v1350)) {
                //System.out.println(entry.getKey() +"  "+ entry.getValue());
                String cat1350dl = Integer.toString(entry.getValue().getAsInt());
                //System.out.println(cat1350dl.getClass().getName());
                if (cat1350dl.equals("0")) {
                    dut.setCategoryDLAsString("oneBis");
                    dut.setCategoryDL(0);
                }
            }
            // v1350 UL
            if
            (entry.getKey().equalsIgnoreCase(ParserUtility.RRC_UE_CATEGORY_UL_v1350)) {
                //System.out.println(entry.getKey() +"  "+ entry.getValue());
                String cat1350ul = Integer.toString(entry.getValue().getAsInt());
                //System.out.println(cat1350ul.getClass().getName());
                if (cat1350ul.equals("0")) {
                    dut.setCategoryULAsString("oneBis");
                    dut.setCategoryUL(0);
                }
            }
            // v1430 UL
            if
            (entry.getKey().equalsIgnoreCase(ParserUtility.RRC_UE_CATEGORY_UL_V1430)) {
                //System.out.println(entry.getKey() +"  "+ entry.getValue());
                String cat1430ul = Integer.toString(entry.getValue().getAsInt());
                //System.out.println(cat1430ul.getClass().getName());
                if (cat1430ul.equals("0")) {
                    dut.setCategoryULAsString("n16");
                    dut.setCategoryUL(0);
                }
                if (cat1430ul.equals("2")) {
                    dut.setCategoryULAsString("n18");
                    dut.setCategoryUL(2);
                }
            }
        }//for
        return dut;
    }


    /**
     * Private support method to parse supported modulation on each band
     *
     * @param k current jsonobject
     */
    private Mobile supportedModulationsParser(JsonObject k, Mobile dut) {
        JsonObject supportedBandsJson = k.getAsJsonObject(ParserUtility.RRC_SUPPORTED_BAND_LIST_EUTRA_v1250_TREE);
        int supportedBandsNum = k.get(ParserUtility.RRC_SUPPORTED_BAND_LIST_EUTRA_V1250).getAsInt();
        ArrayList<LTEBand> supportedBands = dut.getLteCap().getSupportedBands();
        String item = "Item ";
        JsonObject r;
        boolean dl256 = false;
        boolean ul64 = false;
        for (int i = 0; i < supportedBandsNum; i++) {
            r = supportedBandsJson.getAsJsonObject(item.concat(Integer.toString(i)));
            r = r.getAsJsonObject(ParserUtility.RRC_SUPPORTED_BAND_EUTRA_V1250_ELEMENT);
            if (r.has(ParserUtility.RRC_DL_256_QAM_R12)) {
                if (r.get(ParserUtility.RRC_DL_256_QAM_R12).getAsInt() == 0) {
                    dl256 = true;
                } else {
                    dl256 = false;
                }
            }
            if (r.has(ParserUtility.RRC_UL_64_QAM_R12)) {
                if (r.get(ParserUtility.RRC_UL_64_QAM_R12).getAsInt() == 0) {
                    ul64 = true;
                } else {
                    ul64 = false;
                }
            }
            // ArrayList maintains elements in order so itemIndex is the same as the ArrayList index
            supportedBands.get(i).setSupport256QAMDL(dl256);
            supportedBands.get(i).setSupport64QAMUL(ul64);
            ul64 = false;
            dl256 = false;
        }
        return dut;
    }

    /**
     * Private support method to parse ca bandwidth combinations rf 1060
     *
     * @param j current jsonobject in the parsing tree
     */
    private Mobile bcsParser(JsonObject j, Mobile dut) {
        j = j.getAsJsonObject(ParserUtility.RRC_RF_PARAMETERS_V1060);
        JsonObject t;
        String item = "Item ";
        ArrayList<CACombination> supportedCACombinations = dut.getCaBandsCombinations();
        int supportedBandsNumber = j.get(ParserUtility.RRC_SUPPORTED_BAND_COMBINATION_R10).getAsInt();
        j = j.getAsJsonObject(ParserUtility.RRC_SUPPORTED_BAND_COMBINATION_EXT_R10_TREE);
        for (int i = 0; i < supportedBandsNumber; i++) {
            t = j.getAsJsonObject(item.concat(Integer.toString(i))).getAsJsonObject(ParserUtility.RRC_BAND_COMBINATION_PARAMETERS_EXT_R10_ELEMENT);
            if (t.get(ParserUtility.PER_OPTIONAL_FIELD_BIT).getAsInt() == 1) {
                // Due to a wireshark bug, this fields has to be retrieved as a string
                String fieldValue = t.get(ParserUtility.RRC_SUPPORTED_BANDWIDTH_COMBINATIONSET_R10).getAsString();
                supportedCACombinations.get(i).setSupportedBCS(fieldValue);
            } else if (t.get(ParserUtility.PER_OPTIONAL_FIELD_BIT).getAsInt() == 0) {
                supportedCACombinations.get(i).setSupportedBCS("0");
            }
        }
        return dut;
    }


    /**
     * Method to check validity of the input file in case of VoLTE devices
     *
     * @param dut the instance of the Device
     * @return true if the packet is valid, false if it does not fulfill requested requirements
     */
    public boolean volteValidator(Mobile dut) {
        boolean isFine = true;
        boolean flag = true;
        boolean is200OKFound = false;
        boolean isInviteFound = false;
        boolean isRegisterFound = false;
        boolean wrongImsi = false;
        int i = 0;
        JsonObject tmp;
        int restartCounter = 0;
        if (pcapJsonArray.size() == 0) {
            isFine = false;
            Utility.ERROR_CAUSE = Utility.EMPTY_FILE;
        } else {
            while (flag) {
                if (i >= pcapJsonArray.size()) {
                    // Restart
                    i = 0;
                    restartCounter++;
                }
                if (pcapJsonArray.get(i).isJsonObject()) {
                    tmp = (JsonObject) pcapJsonArray.get(i);
                    tmp = tmp.getAsJsonObject(ParserUtility.SOURCE).getAsJsonObject(ParserUtility.LAYERS);
                    // Use an has so that eventually we can substitute it with UDP
                    if (tmp.has(ParserUtility.TCP_KEY)) {
                        // check TCP or UDP
                        // FOR TCP PORT:   "tcp"  -->   "tcp.dstport"
                    }
                    // first two controls are for invite and register
                    // enter only when 200 OK to first register has been found
                    if (tmp.has(ParserUtility.SIP_KEY)) {
                        JsonObject sipObjectTree = tmp.getAsJsonObject(ParserUtility.SIP_KEY);
                        if (sipObjectTree.has(ParserUtility.SIP_REQUEST_LINE_TREE)) {
                            String method = sipObjectTree.getAsJsonObject(ParserUtility.SIP_REQUEST_LINE_TREE).get(ParserUtility.SIP_METHOD).getAsString();
                            if (method.equals(ParserUtility.SIP_REGISTER) && is200OKFound) {
                                // Found a register
                                String pcapImsi = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE).getAsJsonObject(ParserUtility.SIP_FROM_TREE)
                                        .getAsJsonObject(ParserUtility.SIP_FROM_ADDR_TREE).get(ParserUtility.SIP_FROM_USER).getAsString();
                                // Check TCP port to check if it is the first register
                                if (pcapImsi.equals(dut.getImsi())) {
                                    // UDP also could be used
                                    if (tmp.has(ParserUtility.TCP_KEY)) {
                                        int tcpPort = tmp.getAsJsonObject(ParserUtility.TCP_KEY).get(ParserUtility.TCP_DST_PORT).getAsInt();
                                        if (tcpPort == Utility.SERVER_TCP_PORT) {
                                            // PARSE REGISTER FIELDS
                                            isRegisterFound = true;
                                            wrongImsi = false;
                                        }
                                    } else if (tmp.has(ParserUtility.UDP_KEY)) {
                                        int udpPort = tmp.getAsJsonObject(ParserUtility.UDP_KEY).get(ParserUtility.UDP_DST_PORT).getAsInt();
                                        if (udpPort == Utility.SERVER_TCP_PORT) {
                                            // PARSE REGISTER FIELDS
                                            isRegisterFound = true;
                                            wrongImsi = false;
                                        }
                                    }
                                }
                            } else if (method.equals(ParserUtility.SIP_INVITE)) {
                                try {
                                    JsonObject invite = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE);
                                    String moNumber = invite.getAsJsonObject(ParserUtility.SIP_FROM_TREE).getAsJsonObject(ParserUtility.SIP_FROM_ADDR_TREE).get(ParserUtility.SIP_FROM_USER).getAsString();
                                    Pattern p = Pattern.compile(Utility.MSISDN_REGEX);
                                    Matcher m = p.matcher(moNumber);
                                    if (m.find()) {
                                        moNumber = m.group(1);
                                        if (moNumber.equals(dut.getMsisdn())) {
                                            JsonObject j = (JsonObject) pcapJsonArray.get(i);
                                            Utility.INVITE_FRAME_NUMBER = j.getAsJsonObject(ParserUtility.SOURCE).getAsJsonObject(ParserUtility.LAYERS)
                                                    .getAsJsonObject(ParserUtility.FRAME).get(ParserUtility.FRAME_NUMBER).getAsInt();
                                            isInviteFound = true;
                                            wrongImsi = false;
                                        }
                                    }
                                } catch (Exception e) {
                                    System.out.println("Re-INVITE found.");
                                }
                            }
                        } else if (sipObjectTree.has(ParserUtility.SIP_STATUS_LINE_TREE)) {
                            int responseCode = sipObjectTree.getAsJsonObject(ParserUtility.SIP_STATUS_LINE_TREE).get(ParserUtility.SIP_STATUS_CODE).getAsInt();
                            if (sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE)
                                    .getAsJsonObject(ParserUtility.SIP_CSEQ_TREE).get(ParserUtility.SIP_CSEQ_METHOD).getAsString().equals(ParserUtility.SIP_REGISTER)) {
                                if (responseCode == ParserUtility.SIP_200_OK_RESPONSE_CODE) {
                                    // a 200 OK has been found
                                    if (i == 39) {
                                        int hhh = 99;
                                    }
                                    String ok200Imsi = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE)
                                            .getAsJsonObject(ParserUtility.SIP_FROM_TREE).getAsJsonObject(ParserUtility.SIP_FROM_ADDR_TREE).get(ParserUtility.SIP_FROM_USER).getAsString();
                                    if (ok200Imsi.equals(dut.getImsi())) {
                                        // check if it is the wanted register by checking p associated URI
                                        if (sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE).has(ParserUtility.SIP_P_ASSOCIATED_URI)) {
                                            String msisdn = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE).get(ParserUtility.SIP_P_ASSOCIATED_URI).getAsString();
                                            Pattern p = Pattern.compile(Utility.MSISDN_REGEX);
                                            Matcher m = p.matcher(msisdn);
                                            if (m.find()) {
                                                String match = m.group(1);
                                                dut.setMsisdn(match);
                                                wrongImsi = false;
                                                is200OKFound = true;
                                            }
                                            String ipAddress = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE).getAsJsonObject(ParserUtility.SIP_VIA_TREE).get(ParserUtility.SIP_VIA_SENT_BY_ADDR).getAsString();
                                            dut.setIpAddress(ipAddress);
                                        }
                                    } else {
                                        wrongImsi = true;
                                    }
                                }
                            }
                        } // if SIP packet
                        i++;
                    }
                    if (restartCounter > 3) {
                        flag = false;
                        isFine = false;
                        FileManager.deleteTempFile(jsonFile);
                        if (wrongImsi) {
                            Utility.ERROR_CAUSE = WRONG_IMSI;
                        } else if (!is200OKFound) {
                            Utility.ERROR_CAUSE = NO_200_OK;
                        } else if (!isInviteFound) {
                            Utility.ERROR_CAUSE = NO_INVITE;
                        } else if (!isRegisterFound) {
                            Utility.ERROR_CAUSE = NO_REGISTER;
                        }
                    } else if (is200OKFound && isInviteFound && isRegisterFound) {
                        flag = false;
                        Utility.ERROR_CAUSE = "";
                    }
                }
            }// while
        } // else not empty file
        return isFine;
    }

    /**
     * Method to parse REGISTER in the input SIP file
     *
     * @param dut the instance of the device
     * @return the updated instance of the device
     */
    public Mobile volteRegisterParser(Mobile dut) {
        boolean flag = true;
        boolean isRegisterFound = false;
        int i = 0;
        JsonObject tmp;
        int restartCounter = 0;
        while (flag) {
            if (i >= pcapJsonArray.size()) {
                // Restart
                i = 0;
                restartCounter++;
            }
            if (pcapJsonArray.get(i).isJsonObject()) {
                tmp = (JsonObject) pcapJsonArray.get(i);
                tmp = tmp.getAsJsonObject(ParserUtility.SOURCE).getAsJsonObject(ParserUtility.LAYERS);
                // Use an has so that eventually we can substitute it with UDP
                if (tmp.has(ParserUtility.TCP_KEY)) {
                    // check TCP or UDP
                    // FOR TCP PORT:   "tcp"  -->   "tcp.dstport"
                }
                // first two controls are for invite and register
                // enter only when 200 OK to first register has been found
                if (tmp.has(ParserUtility.SIP_KEY)) {
                    JsonObject sipObjectTree = tmp.getAsJsonObject(ParserUtility.SIP_KEY);
                    if (sipObjectTree.has(ParserUtility.SIP_REQUEST_LINE_TREE)) {
                        String method = sipObjectTree.getAsJsonObject(ParserUtility.SIP_REQUEST_LINE_TREE).get(ParserUtility.SIP_METHOD).getAsString();
                        if (method.equals(ParserUtility.SIP_REGISTER)) {
                            // Found a register
                            String pcapImsi = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE).getAsJsonObject(ParserUtility.SIP_FROM_TREE)
                                    .getAsJsonObject(ParserUtility.SIP_FROM_ADDR_TREE).get(ParserUtility.SIP_FROM_USER).getAsString();
                            // Check TCP port to check if it is the first register
                            if (pcapImsi.equals(dut.getImsi())) {
                                // UDP also could be used
                                if (tmp.has(ParserUtility.TCP_KEY)) {
                                    int tcpPort = tmp.getAsJsonObject(ParserUtility.TCP_KEY).get(ParserUtility.TCP_DST_PORT).getAsInt();

                                    if (tcpPort == Utility.SERVER_TCP_PORT) {
                                        // PARSE REGISTER FIELDS
                                        isRegisterFound = true;
                                        if (sipObjectTree.has(ParserUtility.SIP_MSG_HDR_TREE)) {
                                            JsonObject msgHdrTree = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE);
                                            if (msgHdrTree.has(ParserUtility.SIP_SECURITY_CLIENT)) {
                                                String p = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE).get(ParserUtility.SIP_SECURITY_CLIENT).getAsString();
                                                dut.getLteCap().getVolteCap().setSupportedAlgorithms(pSecurityClientParser(p));
                                            } else {
                                                dut.getLteCap().getVolteCap().setSupportedAlgorithms(new ArrayList<>());
                                            }
                                        } else {
                                            dut.getLteCap().getVolteCap().setSupportedAlgorithms(new ArrayList<>());
                                        }
                                    }
                                } else if (tmp.has(ParserUtility.UDP_KEY)) {
                                    int udpPort = tmp.getAsJsonObject(ParserUtility.UDP_KEY).get(ParserUtility.UDP_DST_PORT).getAsInt();
                                    if (udpPort == Utility.SERVER_TCP_PORT) {
                                        isRegisterFound = true;
                                        if (sipObjectTree.has(ParserUtility.SIP_MSG_HDR_TREE)) {
                                            JsonObject msgHdrTree = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE);
                                            if (msgHdrTree.has(ParserUtility.SIP_SECURITY_CLIENT)) {
                                                String p = sipObjectTree.getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE).get(ParserUtility.SIP_SECURITY_CLIENT).getAsString();
                                                dut.getLteCap().getVolteCap().setSupportedAlgorithms(pSecurityClientParser(p));
                                            } else {
                                                dut.getLteCap().getVolteCap().setSupportedAlgorithms(new ArrayList<>());
                                            }
                                        } else {
                                            dut.getLteCap().getVolteCap().setSupportedAlgorithms(new ArrayList<>());
                                        }
                                    }
                                }
                            }
                        }

                    } // if SIP packet
                    i++;
                }
                if (restartCounter > 3) {
                    flag = false;
                    Utility.ERROR_CAUSE = UNKNOWN_ERROR;
                } else if (!isRegisterFound) {
                    flag = false;
                    Utility.ERROR_CAUSE = NO_REGISTER;
                }
            }
        }// while
        FileManager.deleteTempFile(jsonFile);
        return dut;
    }

    /**
     * Public support method to parse invite after all pcap file has been loaded in memory
     *
     * @param device the instance of the device under test
     * @return
     */
    public Mobile volteInviteParser(Mobile device) {
        JsonObject invite = ((JsonObject) pcapJsonArray.get(0)).getAsJsonObject(ParserUtility.SOURCE).getAsJsonObject(ParserUtility.LAYERS).getAsJsonObject(ParserUtility.SIP_KEY)
                .getAsJsonObject(ParserUtility.SIP_MSG_HDR_TREE);
        String supportPEarlyMedia = invite.get(ParserUtility.SIP_P_EARLY_MEDIA).getAsString();
        device.getLteCap().getVolteCap().setpEarlyMediaSupport(supportPEarlyMedia);
        String sipUserAgent = "";
        if (invite.has(ParserUtility.SIP_USER_AGENT)) {
            sipUserAgent = invite.get(ParserUtility.SIP_USER_AGENT).getAsString();
        } else {
            sipUserAgent = Utility.NOT_SUPPORTED;
        }
        device.getLteCap().getVolteCap().setUserAgent(sipUserAgent);
        String srvcc = invite.get(ParserUtility.SIP_CONTACT).getAsString();
        ArrayList<String> srvccSupportVector = supportedSrvccsParser(srvcc);
        device.getLteCap().getVolteCap().setSupportedSrvccs(srvccSupportVector);
        FileManager.deleteTempFile(jsonFile);
        return device;
    }

    /**
     * Private support method to perform parsing and checking of alg and ealg strings using regexes
     *
     * @param p the string to parse
     * @return an arraylist containing the couples of the algorithms
     */
    private ArrayList<AlgCouple> pSecurityClientParser(String p) {
        String[] securityVector = p.split(",");
        ArrayList<AlgCouple> supportedAlgorithms = new ArrayList<AlgCouple>();
        for (int j = 0; j < securityVector.length; j++) {
            AlgCouple currentCouple = new AlgCouple();
            Pattern algPattern = Pattern.compile(Utility.ALG_REGEX);
            Pattern ealgPattern = Pattern.compile(Utility.EALG_REGEX);
            Matcher algMatcher = algPattern.matcher(securityVector[j]);
            Matcher ealgMatcher = ealgPattern.matcher(securityVector[j]);
            if (ealgMatcher.find()) {
                String ealg = ealgMatcher.group(1).replace(Utility.EALG_STRING, "").replace("=", "").replace(";", "");
                currentCouple.setEalg(ealg);
            }
            if (algMatcher.find()) {
                String alg = algMatcher.group(1).replace(Utility.ALG_STRING, "").replace("=", "").replace(";", "");
                currentCouple.setAlg(alg);
            }
            algMatcher.reset();
            ealgMatcher.reset();
            supportedAlgorithms.add(currentCouple);
        }
        return supportedAlgorithms;
    }

    /**
     * Private support method to perform parsing of the peculiar srvcc support in invite packet
     *
     * @param srvccSupportParser The string "sip.Contact" from which the parsing is done
     * @return a vector of strings with the supported srvccs
     */
    private ArrayList<String> supportedSrvccsParser(String srvccSupportParser) {
        String[] tmp = srvccSupportParser.split(";");
        ArrayList<String> supportedSrvcc = new ArrayList<>();
        for (int i = 0; i < tmp.length; i++) {
            Pattern srvccPattern = Pattern.compile(Utility.SRVCC_SUPPORT_REGEX);
            Pattern icsiSrvccPattern = Pattern.compile(Utility.SRVCC_SUPPORT_REGEX_ICSI);
            Matcher srvccMatcher = srvccPattern.matcher(tmp[i]);
            Matcher srvccIcsiMatcher = icsiSrvccPattern.matcher(tmp[i]);
            if (srvccIcsiMatcher.find()) {
                // Should match first entry
                // not of our interest
            } else if (srvccMatcher.find()) {
                String s = srvccMatcher.group(1);
                s = s.replace("+g.3gpp.", "");
                //temporary(?) fix to avoid accesstype being included in SRVCCs list
                if (!s.contains("accesstype")) {
                    supportedSrvcc.add(s);
                }
            }
        }
        return supportedSrvcc;
    }

    /**
     * Private support method to parse inter rat parameters 920
     *
     * @param device the instance of the smartphone
     * @param obj    the json object in the parsing tree
     * @return the instance of the device correctly updated
     */
    private Mobile interRatParametersGERAN920Parser(Mobile device, JsonObject obj) {
        obj = obj.getAsJsonObject(ParserUtility.LTE_RRC_INTER_RAT_PARAMETERS_GERAN_920_ELEMENT);
        if (obj.has(ParserUtility.LTE_RRC_REDIRECTION_GERAN_920)) {
            int geranRedirection = obj.get(ParserUtility.LTE_RRC_REDIRECTION_GERAN_920).getAsInt();

            if (geranRedirection == 0) {
                device.getLteCap().seteRedirectionGERAN(true);
            }
        } else {
            device.getLteCap().seteRedirectionGERAN(false);
        }
        return device;
    }

    /**
     * Private support method to parse inter RAT UTRA parameters 920
     *
     * @param device the instance of the device to update
     * @param obj    the json object in the parsing tree
     * @return the instance of the device updated
     */
    private Mobile interRatParametersUTRAN920Parser(Mobile device, JsonObject obj) {
        obj = obj.getAsJsonObject(ParserUtility.LTE_RRC_INTER_RAT_PARAMETERS_UTRA_920_ELEMENT);
        if (obj.has(ParserUtility.LTE_RRC_REDIRECTION_UTRA_920)) {
            int utraRedirection = obj.get(ParserUtility.LTE_RRC_REDIRECTION_UTRA_920).getAsInt();
            if (utraRedirection == 0) {
                device.getLteCap().seteRedirectionUTRAN(true);
            }
        } else {
            device.getLteCap().seteRedirectionUTRAN(false);
        }
        return device;
    }

    private Mobile bandCombinationParser10d0(JsonObject j, Mobile dut) {
        JsonObject tmp, itemIterator, innerItemIterator;
        JsonObject tmp2 = null;
        boolean flag = true;
        tmp = j.getAsJsonObject(ParserUtility.RRC_UECAPABILITY_V9A0_ELEMENT).getAsJsonObject(ParserUtility.RRC_NON_CRITICAL_EXTENSION_ELEMENT);
        while (flag) {
            if ((tmp == null) || (tmp2 != null)) {
                flag = false;
            } else {
                tmp2 = tmp.getAsJsonObject(ParserUtility.LTE_RRC_RF_PARAMETERS_V11D0_ELEMENT);
                tmp = tmp.getAsJsonObject(ParserUtility.RRC_NON_CRITICAL_EXTENSION_ELEMENT);
            }
        }
        if (tmp2 != null) {
            // parse tree of UE cap Rel 10
            int combinationsNumber = tmp2.get(ParserUtility.LTE_RRC_SUPPORTED_BAND_COMBINATION_ADD_V11D0).getAsInt();
            ArrayList<CACombination> bwCombo = new ArrayList<>(combinationsNumber);
            tmp2 = tmp2.getAsJsonObject(ParserUtility.LTE_RRC_SUPPORTED_BANDCOMBINATION_ADD_V11D0_TREE);
            String item = "Item ";
            String con = "";

            for (int i = 0; i < combinationsNumber; i++) {
                bwCombo.add(i, new CACombination());
                CACombination currentCombo = bwCombo.get(i);
                ArrayList<ComponentCarrier> downlinkCC = new ArrayList<>();
                ArrayList<ComponentCarrier> uplinkCC = new ArrayList<>();
                // UPlink combinations not managed by now (TM34 carries information only
                // over the used layers in DL)
                itemIterator = tmp2.getAsJsonObject(item.concat(Integer.toString(i)));
                itemIterator = itemIterator.getAsJsonObject(ParserUtility.RRC_BAND_COMBINATION_PARAMETERS_V10I0_ELEMENT);
                // check if object is null, if it is, no info are present in this element
                if (itemIterator.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERS_LIST_V10I0_TREE) != null) {
                    // retrieve number before making itemIterator go on
                    int ccNumber = itemIterator.get(ParserUtility.RRC_BAND_PARAMETERS_LIST_V10I0).getAsInt();
                    itemIterator = itemIterator.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERS_LIST_V10I0_TREE);
                    for (int k = 0; k < ccNumber; k++) {
                        downlinkCC.add(k, new ComponentCarrier());
                        ComponentCarrier cc = downlinkCC.get(k);
                        innerItemIterator = itemIterator.getAsJsonObject(item.concat(Integer.toString(k)));
                        innerItemIterator = innerItemIterator.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERS_V10I0_ELEMENT);
                        if (innerItemIterator.has(ParserUtility.RRC_BAND_PARAMETERSUL_V10I0)) {
                            uplinkCC.add(k, new ComponentCarrier());
                            ComponentCarrier ccUL = uplinkCC.get(k);
                            int uplinkCAPresent = innerItemIterator.get(ParserUtility.RRC_BAND_PARAMETERSUL_V10I0).getAsInt();
                            if (uplinkCAPresent == 1) {
                                ccUL.setSupportedLayersTM34(0);
                            }
                        } else {
                            if (k <= uplinkCC.size() - 1) {
                                ComponentCarrier ccUL = uplinkCC.get(k);
                                ccUL.setSupportedLayersTM34(1);
                            }
                        }
                        innerItemIterator = innerItemIterator.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERSDL_V10I0_TREE).getAsJsonObject(ParserUtility.ITEM_0)
                                .getAsJsonObject(ParserUtility.RRC_CA_MIMO_PARAMETERSDL_V10I0_ELEMENT);
                        if (innerItemIterator.has(ParserUtility.RRC_FOURLAYERTM34_R10)) {
                            int isFourLayersSupported = innerItemIterator.get(ParserUtility.RRC_FOURLAYERTM34_R10).getAsInt();
                            if (isFourLayersSupported == 0) {
                                cc.setSupportedLayersTM34(4);
                            } else {
                                cc.setSupportedLayersTM34(2);
                            }
                        } else {
                            cc.setSupportedLayersTM34(2);
                        }
                    }
                } else {
                    // no info are present, set default layers
                    for (int z = 0; z < downlinkCC.size(); z++) {
                        downlinkCC.set(z, new ComponentCarrier());
                        downlinkCC.get(z).setSupportedLayersTM34(2);
                    }
                    for (int zz = 0; zz < uplinkCC.size(); zz++) {
                        uplinkCC.set(zz, new ComponentCarrier());
                        uplinkCC.get(zz).setSupportedLayersTM34(1);
                    }
                }
                currentCombo.setDLSingleCombination(downlinkCC);
                currentCombo.setULSingleCombination(uplinkCC);
            }
            dut.setCaV10d0BandCombinations(bwCombo);
        }
        return dut;
    }

    /**
     * Private method to retrieve Carrier Aggregation band combination Trasmission mode 3-4
     *
     * @param j current JsonObject extracted from the pcap
     */
    private Mobile bandCombinationParser10i0(JsonObject j, Mobile dut) {
        JsonObject tmp, itemIterator, innerItemIterator;
        JsonObject tmp2 = null;
        boolean flag = true;
        tmp = j.getAsJsonObject(ParserUtility.RRC_UECAPABILITY_V9A0_ELEMENT).getAsJsonObject(ParserUtility.RRC_NON_CRITICAL_EXTENSION_ELEMENT);
        ArrayList<CACombination> bwCombo = new ArrayList<>();
        while (flag) {
            if ((tmp == null) || (tmp2 != null)) {
                flag = false;
            } else {
                tmp2 = tmp.getAsJsonObject(ParserUtility.RRC_RF_PARAMETERS_V10I0);
                tmp = tmp.getAsJsonObject(ParserUtility.RRC_NON_CRITICAL_EXTENSION_ELEMENT);
            }
        }
        if (tmp2 != null) {
            bwCombo = dut.getCaBandsCombinations();
            dut.setCa10i0Support(true);
            // parse tree of UE cap Rel 10
            int combinationsNumber = tmp2.get(ParserUtility.RRC_SUPPORTED_BAND_COMBINATION_V10I0).getAsInt();
            tmp2 = tmp2.getAsJsonObject(ParserUtility.RRC_SUPPORTED_BAND_COMBINATION_V10I0_TREE);
            String item = "Item ";
            String con = "";
            for (int i = 0; i < combinationsNumber; i++) {
                CACombination currentCombo = dut.getCaBandsCombinations().get(i);
                ArrayList<ComponentCarrier> downlinkCC = currentCombo.getDLSingleCombination();
                ArrayList<ComponentCarrier> uplinkCC = currentCombo.getULSingleCombination();
                itemIterator = tmp2.getAsJsonObject(item.concat(Integer.toString(i)));
                itemIterator = itemIterator.getAsJsonObject(ParserUtility.RRC_BAND_COMBINATION_PARAMETERS_V10I0_ELEMENT);
                // check if object is null, if it is, no info are present in this element
                if (itemIterator.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERS_LIST_V10I0_TREE) != null) {
                    // retrieve number before making itemIterator go on
                    int ccNumber = itemIterator.get(ParserUtility.RRC_BAND_PARAMETERS_LIST_V10I0).getAsInt();
                    itemIterator = itemIterator.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERS_LIST_V10I0_TREE);
                    for (int k = 0; k < ccNumber; k++) {
                        ComponentCarrier cc = downlinkCC.get(k);
                        innerItemIterator = itemIterator.getAsJsonObject(item.concat(Integer.toString(k)));
                        innerItemIterator = innerItemIterator.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERS_V10I0_ELEMENT);
                        if (innerItemIterator.has(ParserUtility.RRC_BAND_PARAMETERSUL_V10I0)) {
                            ComponentCarrier ccUL = currentCombo.getULSingleCombination().get(k);
                            int uplinkCAPresent = innerItemIterator.get(ParserUtility.RRC_BAND_PARAMETERSUL_V10I0).getAsInt();
                            if (uplinkCAPresent == 1) {
                                ccUL.setSupportedLayersTM34(0);
                            }
                        } else {
                            if (k <= uplinkCC.size() - 1) {
                                ComponentCarrier ccUL = uplinkCC.get(k);
                                ccUL.setSupportedLayersTM34(1);
                            }
                        }
                        innerItemIterator = innerItemIterator.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERSDL_V10I0_TREE).getAsJsonObject(ParserUtility.ITEM_0)
                                .getAsJsonObject(ParserUtility.RRC_CA_MIMO_PARAMETERSDL_V10I0_ELEMENT);
                        if (innerItemIterator.has(ParserUtility.RRC_FOURLAYERTM34_R10)) {
                            int isFourLayersSupported = innerItemIterator.get(ParserUtility.RRC_FOURLAYERTM34_R10).getAsInt();
                            if (isFourLayersSupported == 0) {
                                cc.setSupportedLayersTM34(4);
                            } else {
                                cc.setSupportedLayersTM34(2);
                            }
                        } else {
                            cc.setSupportedLayersTM34(2);
                        }
                    }
                } else {
                    // no info are present, set default layers
                    for (int z = 0; z < downlinkCC.size(); z++) {
                        downlinkCC.get(z).setSupportedLayersTM34(2);
                    }
                    for (int zz = 0; zz < uplinkCC.size(); zz++) {
                        uplinkCC.get(zz).setSupportedLayersTM34(1);
                    }
                }
                currentCombo.setDLSingleCombination(downlinkCC);
                currentCombo.setULSingleCombination(uplinkCC);
            }
            dut.setCaBandsCombinations(bwCombo);
        } else {
            dut.setCa10i0Support(false);

        }
        return dut;
    }

    /**
     * Private support method to parse CA rel10 bands combinations supported
     *
     * @param j Object of the json file tree in ue cap
     */
    private Mobile bandCombinationsParser1020(JsonObject j, Mobile dut) {
        JsonObject UL, DL = null;
        ArrayList<CACombination> bwCombo = null;
        JsonObject k, tmp, tmp2, iter = null;
        String item = "Item ";
        String t, u = "";
        k = j.getAsJsonObject(ParserUtility.RRC_RF_PARAMETERS_V1020).getAsJsonObject(ParserUtility.RRC_SUPPORTED_BAND_COMBINATION_TREE);
        if (k != null) {
            dut.setCa1020Support(true);
            bwCombo = new ArrayList<>();
            int supportedCombinations = j.getAsJsonObject(ParserUtility.RRC_RF_PARAMETERS_V1020).get(ParserUtility.RRC_SUPPORTED_BAND_COMBINATION_NUMBER).getAsInt();
            tmp = j.getAsJsonObject(ParserUtility.RRC_RF_PARAMETERS_V1020).getAsJsonObject(ParserUtility.RRC_SUPPORTED_BAND_COMBINATION_TREE);
            // First for: cycle over ca combinations
            for (int i = 0; i < supportedCombinations; i++) {
                CACombination singleCombo = new CACombination();
                t = item.concat(Integer.toString(i));
                // set item index in the pcap file
                singleCombo.setItemIndex(i);
                iter = tmp.getAsJsonObject(t).getAsJsonObject(ParserUtility.RRC_BAND_COMBINATION_PARAMETERS_R10TREE);
                int ccNumber = tmp.getAsJsonObject(t).get(ParserUtility.RRC_BAND_COMBINATION_PARAMETERS_R10).getAsInt();
                // DL and UL arraylist arraylists
                ArrayList<ComponentCarrier> currentDLComb = new ArrayList<>();
                ArrayList<ComponentCarrier> currentULComb = new ArrayList<>();
                // manage CA combinations with multiple items
                for (int r = 0; r < ccNumber; r++) {
                    ComponentCarrier currentBandDL = new ComponentCarrier();
                    ComponentCarrier currentBandUL = new ComponentCarrier();
                    u = item.concat(Integer.toString(r));
                    tmp2 = iter.getAsJsonObject(u).getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERS_R10_ELEMENT);
                    currentBandDL.setBandID(tmp2.get(ParserUtility.RRC_BANDEUTRA_R10).getAsInt());
                    currentBandUL.setBandID(tmp2.get(ParserUtility.RRC_BANDEUTRA_R10).getAsInt());
                    // Manage DL/UL
                    UL = tmp2.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERS_UL_R10_TREE);
                    DL = tmp2.getAsJsonObject(ParserUtility.RRC_BAND_PARAMETERS_DL_R10_TREE);
                    if (UL == null) {
                        // if Only DL combination is present
                        DL = DL.getAsJsonObject(ParserUtility.ITEM_0).getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSDL_R10_ELEMENT_DL);
                        if (DL.has(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_dl_REL10)) {
                            currentBandDL.setComponentCarrierClass(Integer.toString(DL.get(ParserUtility.RRC_BANDWIDTH_CLASS_DL_R10).getAsInt()));
                            currentBandDL.setSupportedLayers(DL.get(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_dl_REL10).getAsInt());
                            int tempLayersDL = DL.get(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_dl_REL10).getAsInt();
                            if (tempLayersDL == 0) {
                                currentBandDL.setSupportedLayers(2);
                            } else if (tempLayersDL == 1) {
                                currentBandDL.setSupportedLayers(4);
                            } else if (tempLayersDL == 2) {
                                currentBandDL.setSupportedLayers(8);
                            }
                        } else {
                            currentBandDL.setSupportedLayers(1);
                            currentBandDL.setComponentCarrierClass(Integer.toString(DL.get(ParserUtility.RRC_BANDWIDTH_CLASS_DL_R10).getAsInt()));
                        }
                        currentDLComb.add(currentBandDL);
                    } else if (DL != null) {
                        UL = UL.getAsJsonObject(ParserUtility.ITEM_0).getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSUL_R10_ELEMENT_UL);
                        currentBandUL.setComponentCarrierClass(Integer.toString(UL.get(ParserUtility.RRC_BANDWIDTH_CLASS_UL_R10).getAsInt()));
                        if (UL.has(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_ul_REL10)) {
                            int tempLayersUL = UL.get(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_ul_REL10).getAsInt();
                            if (tempLayersUL == 0) {
                                currentBandUL.setSupportedLayers(2);
                            } else if (tempLayersUL == 1) {
                                currentBandUL.setSupportedLayers(4);
                            }
                        } else {
                            currentBandUL.setSupportedLayers(1);
                        }
                        currentULComb.add(currentBandUL);
                        DL = DL.getAsJsonObject(ParserUtility.ITEM_0).getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSDL_R10_ELEMENT_DL);
                        if (DL.has(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_dl_REL10)) {
                            currentBandDL.setComponentCarrierClass(Integer.toString(DL.get(ParserUtility.RRC_BANDWIDTH_CLASS_DL_R10).getAsInt()));
                            int tempLayersDL = DL.get(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_dl_REL10).getAsInt();
                            if (tempLayersDL == 0) {
                                currentBandDL.setSupportedLayers(2);
                            } else if (tempLayersDL == 1) {
                                currentBandDL.setSupportedLayers(4);
                            } else if (tempLayersDL == 2) {
                                currentBandDL.setSupportedLayers(8);
                            }
                        } else {
                            currentBandDL.setSupportedLayers(1);
                            currentBandDL.setComponentCarrierClass(Integer.toString(DL.get(ParserUtility.RRC_BANDWIDTH_CLASS_DL_R10).getAsInt()));
                        }
                        currentDLComb.add(currentBandDL);
                    }
                    tmp2 = null;
                    u = "";
                }
                singleCombo.setULSingleCombination(currentULComb);
                singleCombo.setDLSingleCombination(currentDLComb);
                bwCombo.add(singleCombo);
                t = "";
            }
        }
        if (bwCombo != null) {
            dut.setCaBandsCombinations(bwCombo);
        }


        return dut;
    }

    /**
     * Private support method to parse interfrequency band retrieval capabilities and
     * bandwidth combinations set add r11
     *
     * @param j   JsonObject containing 1180 rf parameters
     * @param dut the instance of the current smartphone
     */
    private Mobile rf1180ParametersParser(JsonObject j, Mobile dut) {
        ArrayList<Band> requestedBands = null;
        String item = "Item ";
        JsonObject t = j.getAsJsonObject(ParserUtility.LTE_RRC_RF_PARAMETERS_1180_ELEMENT);
        if (t.has(ParserUtility.LTE_RRC_REQUESTED_BANDS_R11)) {
            requestedBands = new ArrayList<>();
            int requestedBandsNumber = j.getAsJsonObject(ParserUtility.LTE_RRC_RF_PARAMETERS_1180_ELEMENT).get(ParserUtility.LTE_RRC_REQUESTED_BANDS_R11).getAsInt();
            j = j.getAsJsonObject(ParserUtility.LTE_RRC_RF_PARAMETERS_1180_ELEMENT).getAsJsonObject(ParserUtility.LTE_RRC_REQUESTED_BANDS_TREE);
            for (int i = 0; i < requestedBandsNumber; i++) {
                Band tempBand = new Band(j.getAsJsonObject(item.concat(String.valueOf(i))).get(ParserUtility.LTE_RRC_FREQBANDINDICATOR_R11).getAsInt(), i);
                requestedBands.add(tempBand);
            }
        } else if (t.has(ParserUtility.LTE_RRC_FREQBAND_RETRIEVAL_R11)) {
            requestedBands = new ArrayList<>();
        }
        dut.setRequestedBands(requestedBands);
        // Parse additional band combinations r11
        ArrayList<CACombination> bwCombo = null;
        String tempItem = "";
        String itemZero = "Item 0";
        JsonObject tempObj, ccObj;
        if (t.has(ParserUtility.LTE_RRC_SUPPORTED_BAND_COMBINATION_ADD_R11)) {
            dut.setAddR11Support(true);
            int combinationLength = t.get(ParserUtility.LTE_RRC_SUPPORTED_BAND_COMBINATION_ADD_R11).getAsInt();
            tempObj = t.getAsJsonObject(ParserUtility.LTE_RRC_SUPPORTED_BAND_COMBINATION_ADD_R11_TREE);


            if (dut.getCaV10d0BandCombinations() == null && !dut.isCa10i0Support()) {

                System.out.println("V10d0 not supported");
                System.out.println(combinationLength);
                System.out.println(dut.getCaBandsCombinations().size() + combinationLength);

                JsonObject itemIterator, innerItemIterator;
                bwCombo = dut.getCaBandsCombinations();

                int uploadBandID = 0;
                String uploadBandClass = "";
                int uploadBandLayers = 0;

                item = "Item ";

                for (int i = 0; i < combinationLength; i++) {
                    CACombination currentCombo = new CACombination();
                    ArrayList<ComponentCarrier> downlinkCC = new ArrayList<>();
                    ArrayList<ComponentCarrier> uplinkCC = new ArrayList<>();
                    // TODO: 30-Oct-19 figure out why the fuck itemIterator is set to null and how
                    // the fuck innerItemIterator should be used
                    itemIterator = tempObj.getAsJsonObject(item.concat(Integer.toString(i)));
                    itemIterator = itemIterator
                            .getAsJsonObject(ParserUtility.LTE_RRC_BANDCOMBINATIONPARAMETERS_R11_ELEMENT);
                    if (itemIterator.getAsJsonObject(ParserUtility.LTE_RRC_BANDPARAMETERLIST_R11_TREE) != null) {
                        int ccNumber = itemIterator.get(ParserUtility.LTE_RRC_BANDPARAMETERLIST_R11).getAsInt();
                        itemIterator = itemIterator.getAsJsonObject(ParserUtility.LTE_RRC_BANDPARAMETERLIST_R11_TREE);
                        for (int k = 0; k < ccNumber; k++) {
//                            downlinkCC.add(k, new ComponentCarrier());
//                            ComponentCarrier cc = downlinkCC.get(k);
//                            uplinkCC.add(k, new ComponentCarrier());
//                            ComponentCarrier ccUL = uplinkCC.get(k);
                            ComponentCarrier cc = new ComponentCarrier();
                            ComponentCarrier ccUL = new ComponentCarrier();
                            innerItemIterator = itemIterator.getAsJsonObject(item.concat(Integer.toString(k)));
                            innerItemIterator = innerItemIterator
                                    .getAsJsonObject(ParserUtility.LTE_RRC_BANDPARAMETERS_R11_ELEMENT);

                            if (innerItemIterator.has(ParserUtility.LTE_RRC_SUPPORTED_BANDWIDTH_COMBINATION_SET_R11)) {
                                currentCombo.setSupportedBCS(
                                        innerItemIterator.get(ParserUtility.LTE_RRC_SUPPORTED_BANDWIDTH_COMBINATION_SET_R11).getAsString());
                            } else {
                                currentCombo.setSupportedBCS("0");
                            }

                            int bandID = innerItemIterator.getAsJsonPrimitive(LTE_RRC_BAND_EUTRA_R11).getAsInt();
                            cc.setBandID(bandID);

                            if (innerItemIterator
                                    .getAsJsonObject(ParserUtility.LTE_RRC_BAND_PARAMETERS_DL_R11_TREE)
                                    .getAsJsonObject(ParserUtility.ITEM_0)
                                    .getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSDL_R10_ELEMENT_DL).has(ParserUtility.RRC_BANDWIDTH_CLASS_DL_R10)) {
                                int downlinkCAPresent_class = innerItemIterator
                                        .getAsJsonObject(ParserUtility.LTE_RRC_BAND_PARAMETERS_DL_R11_TREE)
                                        .getAsJsonObject(ParserUtility.ITEM_0)
                                        .getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSDL_R10_ELEMENT_DL)
                                        .getAsJsonPrimitive(ParserUtility.RRC_BANDWIDTH_CLASS_DL_R10).getAsInt();
                                cc.setComponentCarrierClass(Integer.toString(downlinkCAPresent_class));
                            }
                            if (innerItemIterator
                                    .getAsJsonObject(ParserUtility.LTE_RRC_BAND_PARAMETERS_DL_R11_TREE)
                                    .getAsJsonObject(ParserUtility.ITEM_0)
                                    .getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSDL_R10_ELEMENT_DL).has(ParserUtility.RRC_BANDWIDTH_CLASS_DL_R10)) {
                                int downlinkCAPresent_supportedMIMOr10DL = innerItemIterator
                                        .getAsJsonObject(ParserUtility.LTE_RRC_BAND_PARAMETERS_DL_R11_TREE)
                                        .getAsJsonObject(ParserUtility.ITEM_0)
                                        .getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSDL_R10_ELEMENT_DL)
                                        .getAsJsonPrimitive(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_dl_REL10).getAsInt();
                                if (downlinkCAPresent_supportedMIMOr10DL == 0) {
                                    cc.setSupportedLayers(4);
                                } else {
                                    cc.setSupportedLayers(2);
                                }
                            }


                            if (innerItemIterator.has(ParserUtility.LTE_RRC_BAND_PARAMETERS_UL_R11_TREE)) {
                                //innerItemIterator.getAsJsonPrimitive(LTE_RRC_BAND_EUTRA_R11).getAsInt();
                                ccUL.setBandID(innerItemIterator.getAsJsonPrimitive(LTE_RRC_BAND_EUTRA_R11).getAsInt());
                                uploadBandID = innerItemIterator.getAsJsonPrimitive(LTE_RRC_BAND_EUTRA_R11).getAsInt();
                                //System.out.println(innerItemIterator);

                                if (innerItemIterator.getAsJsonObject(ParserUtility.LTE_RRC_BAND_PARAMETERS_UL_R11_TREE)
                                        .getAsJsonObject(ParserUtility.ITEM_0)
                                        .has(ParserUtility.RRC_BAND_MIMO_PARAMETERSUL_R10_ELEMENT_UL)) {
                                    int uplinkCAPresent_class = innerItemIterator
                                            .getAsJsonObject(ParserUtility.LTE_RRC_BAND_PARAMETERS_UL_R11_TREE)
                                            .getAsJsonObject(ParserUtility.ITEM_0)
                                            .getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSUL_R10_ELEMENT_UL)
                                            .getAsJsonPrimitive(ParserUtility.RRC_BANDWIDTH_CLASS_UL_R10).getAsInt();
                                    ccUL.setComponentCarrierClass(Integer.toString(uplinkCAPresent_class));
                                    uploadBandClass = String.valueOf(uplinkCAPresent_class);
                                }
                                JsonPrimitive uplinkCAPresent = innerItemIterator
                                        .getAsJsonObject(ParserUtility.LTE_RRC_BAND_PARAMETERS_UL_R11_TREE)
                                        .getAsJsonObject(ParserUtility.ITEM_0)
                                        .getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSUL_R10_ELEMENT_UL)
                                        .getAsJsonPrimitive(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_ul_REL10);
                                //TODO check other items
                                if (uplinkCAPresent == null) {
                                    ccUL.setSupportedLayers(1); //TODO null -> 1x1
                                    uploadBandLayers = 0;
                                } else if (uplinkCAPresent.getAsInt() == 0) {
                                    ccUL.setSupportedLayers(1); //TODO check
                                    uploadBandLayers = 0;
                                }
                                //System.out.println("The upload band for this combination is: " +ccUL.getBandID()+" "+ccUL.getComponentCarrierClass()+" "+
                                //  ccUL.getSupportedLayers());
								/*
								for(int iter=0;i<uplinkCC.size()-1;iter++){
								    System.out.println(uplinkCC.size());
                                }

								 */

                                //TODO aggiungere casistiche
//							} else {
//								if (k <= uplinkCC.size() - 1) {
//									// TODO check
//									ComponentCarrier ccUL2 = uplinkCC.get(k);
//									ccUL2.setSupportedLayers(1);
//                                    uploadBandLayers = 2;
//								}
                            }

                            if (cc.getBandID() != 0)
                                downlinkCC.add(cc);
                            if (ccUL.getBandID() != 0)
                                uplinkCC.add(ccUL);

                        }

                        //System.out.println("Combination #"+i+" has DL size:"+downlinkCC.size());
                        //System.out.println();
                        /*


                         */
                    } else {
                        // no info are present, set default layers
                        for (int z = 0; z < downlinkCC.size(); z++) {
                            downlinkCC.set(z, new ComponentCarrier());
                            downlinkCC.get(z).setSupportedLayersTM34(2);
                        }
                        for (int zz = 0; zz < uplinkCC.size(); zz++) {
                            uplinkCC.set(zz, new ComponentCarrier());
                            uplinkCC.get(zz).setSupportedLayersTM34(1);
                        }
                    }
                    currentCombo.setDLSingleCombination(downlinkCC);
                    currentCombo.setULSingleCombination(uplinkCC);

                    bwCombo.add(currentCombo);
                }
//				dut.setCaAddR11BandCombinations(bwCombo);
//				dut.addAddR11ToCABandCOmbination(bwCombo);
                /*
				System.out.println(bwCombo.size());
				int nC = 0;
				for (CACombination caCombination : bwCombo) {
					System.out.print("\n(" + nC++ + ") ");
					for (ComponentCarrier comCarr : caCombination.getULSingleCombination()) {
						System.out.print(comCarr.getBandID() + comCarr.getComponentCarrierClass() + comCarr.getSupportedLayers());
					}
				}

                 */
//				dut.setCaBandsCombinations(bwCombo); // v1020
            }


            if (dut.getCaV10d0BandCombinations() != null) {
                bwCombo = dut.getCaV10d0BandCombinations();
                for (int i = 0; i < combinationLength; i++) {
                    CACombination singleCombo = bwCombo.get(i);
                    tempItem = item.concat(Integer.toString(i));
                    JsonObject outerItem = tempObj.getAsJsonObject(tempItem);
                    singleCombo.setItemIndex(i);
                    outerItem = outerItem.getAsJsonObject(ParserUtility.LTE_RRC_BANDCOMBINATIONPARAMETERS_R11_ELEMENT);
                    // BCS
                    if (outerItem.has(ParserUtility.LTE_RRC_SUPPORTED_BANDWIDTH_COMBINATION_SET_R11)) {
                        singleCombo.setSupportedBCS(
                                outerItem.get(ParserUtility.LTE_RRC_SUPPORTED_BANDWIDTH_COMBINATION_SET_R11).getAsString());
                    } else {
                        singleCombo.setSupportedBCS("0");
                    }
                    int ccNumber = outerItem.get(ParserUtility.LTE_RRC_BANDPARAMETERLIST_R11).getAsInt();
                    JsonObject currentItemObject = outerItem
                            .getAsJsonObject(ParserUtility.LTE_RRC_BANDPARAMETERLIST_R11_TREE);
                    ArrayList<ComponentCarrier> currentDLComb = singleCombo.getDLSingleCombination();
                    ArrayList<ComponentCarrier> currentULComb = singleCombo.getULSingleCombination();

                    if (currentDLComb.size() == 0) {
                        for (int h = 0; h < ccNumber; h++) {
                            ComponentCarrier tempCarrierDL = new ComponentCarrier();
                            tempCarrierDL.setSupportedLayersTM34(2);
                            currentDLComb.add(h, tempCarrierDL);
                        }
                    } else if (currentDLComb.size() < ccNumber) {
                        for (int h = currentDLComb.size() - 1; h < ccNumber - currentDLComb.size(); h++) {
                            ComponentCarrier tempCarrierDL = new ComponentCarrier();
                            tempCarrierDL.setSupportedLayersTM34(2);
                            currentDLComb.add(h, tempCarrierDL);
                        }
                    }
                    int ulCOunter = 0;
                    for (int k = 0; k < ccNumber; k++) {
                        ComponentCarrier ccDL = currentDLComb.get(k);

                        JsonObject singleCCObj = currentItemObject.getAsJsonObject(item.concat(Integer.toString(k)));
                        singleCCObj = singleCCObj.getAsJsonObject(ParserUtility.LTE_RRC_BANDPARAMETERS_R11_ELEMENT);
                        int bandId = singleCCObj.get(LTE_RRC_BAND_EUTRA_R11).getAsInt();
                        // Uplink element
                        if (singleCCObj.has(ParserUtility.LTE_RRC_BAND_PARAMETERS_UL_R11_TREE)) {
                            ulCOunter++;
                            ComponentCarrier ccUL = null;
                            if (currentULComb.size() == 0) {
                                ccUL = new ComponentCarrier();
                                ccUL.setSupportedLayersTM34(1);
                                currentULComb.add(ccUL);
                            } else if (currentULComb.size() - 1 < ulCOunter) {
                                for (int h = currentULComb.size() - 1; h < ulCOunter - currentULComb.size(); h++) {
                                    ComponentCarrier tempCarrier = new ComponentCarrier();
                                    tempCarrier.setSupportedLayersTM34(1);
                                    currentULComb.add(ulCOunter - 1, tempCarrier);
                                }
                            }
                            ccUL = currentULComb.get(ulCOunter - 1);
                            ccObj = singleCCObj.getAsJsonObject(ParserUtility.LTE_RRC_BAND_PARAMETERS_UL_R11_TREE)
                                    .getAsJsonObject(itemZero)
                                    .getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSUL_R10_ELEMENT_UL);
                            ccUL.setBandID(bandId);
                            ccUL.setComponentCarrierClass(
                                    Integer.toString(ccObj.get(ParserUtility.RRC_BANDWIDTH_CLASS_UL_R10).getAsInt()));
                            if (ccObj.has(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_ul_REL10)) {
                                int tempLayersUL = ccObj.get(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_ul_REL10)
                                        .getAsInt();
                                if (tempLayersUL == 0) {
                                    ccUL.setSupportedLayers(2);
                                } else if (tempLayersUL == 1) {
                                    ccUL.setSupportedLayers(4);
                                } else if (tempLayersUL == 2) {
                                    ccUL.setSupportedLayers(8);
                                }
                            } else {
                                ccUL.setSupportedLayers(1);
                            }
                            currentULComb.set(ulCOunter - 1, ccUL);
                        }
                        // Downlink element
                        if (singleCCObj.has(ParserUtility.LTE_RRC_BAND_PARAMETERS_DL_R11_TREE)) {
                            ccObj = singleCCObj.getAsJsonObject(ParserUtility.LTE_RRC_BAND_PARAMETERS_DL_R11_TREE)
                                    .getAsJsonObject(itemZero)
                                    .getAsJsonObject(ParserUtility.RRC_BAND_MIMO_PARAMETERSDL_R10_ELEMENT_DL);
                            ccDL.setBandID(bandId);
                            ccDL.setComponentCarrierClass(
                                    Integer.toString(ccObj.get(ParserUtility.RRC_BANDWIDTH_CLASS_DL_R10).getAsInt()));
                            if (ccObj.has(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_dl_REL10)) {
                                int tempLayersDL = ccObj.get(ParserUtility.RRC_SUPPORTED_MIMO_CAPABILITY_dl_REL10)
                                        .getAsInt();
                                if (tempLayersDL == 0) {
                                    ccDL.setSupportedLayers(2);
                                } else if (tempLayersDL == 1) {
                                    ccDL.setSupportedLayers(4);
                                } else if (tempLayersDL == 2) {
                                    ccDL.setSupportedLayers(8);
                                }
                            } else {
                                ccDL.setSupportedLayers(2);
                            }
                            currentDLComb.set(k, ccDL);
                        }
                    } // end for
                    singleCombo.setULSingleCombination(currentULComb);
                    singleCombo.setDLSingleCombination(currentDLComb);
                    bwCombo.set(i, singleCombo);
                }
                dut.setCaAddR11BandCombinations(bwCombo);
                dut.addAddR11ToCABandCOmbination(bwCombo);
                // System.out.println(bwCombo.size());
            }
        }

        return dut;

    }

    private void rfParametersv1310(JsonObject j, Mobile dut) {
        j = j.getAsJsonObject(LTE_RRC_RFPARAMETERS_V1310_ELEMENT);
        JsonObject enbreq = j.getAsJsonObject(LTE_RRC_ENB_REQPARAMETERS_R13_ELEMENT);
        if (enbreq != null) {
            if (enbreq.has(LTE_RRC_REQUESTEDCCSDL_R13)) {
                dut.setRequestedCCsDL((enbreq.getAsJsonPrimitive(LTE_RRC_REQUESTEDCCSDL_R13)).getAsString());
                dut.setRequestedCCsUL((enbreq.getAsJsonPrimitive(LTE_RRC_REQUESTEDCCSUL_R13)).getAsString());
            }
            if (enbreq.has(LTE_RRC_SKIPFALLBACKCOMBREQUESTED_R13)) {
                dut.setSkipFallBackCombReq(true);
            }
        }

        if (j.has(LTE_RRC_MAXIMUMCCS_RETRIEVAL)) {
            dut.setMaximumCCsRetrieval(true);
        }
        if (j.has(LTE_RRC_SKIPFALLBACKCOMBINATIONS_R13)) {
            dut.setSkipFallBackComb(true);
        }
        if (j.has(LTE_RRC_SUPPORTEDBANDCOMBREDUCED_R13)) {
            bandComb1310Parser(dut, j);
        }
    }

    private void bandComb1310Parser(Mobile dut, JsonObject j) {
        JsonObject UL, DL = null;
        ArrayList<CACombination> bwCombo = null;
        JsonObject k, tmp, tmp2, iter = null;
        String item = "Item ";
        String t, u = "";
        int supportedCombinations = j.getAsJsonPrimitive(LTE_RRC_SUPPORTEDBANDCOMBREDUCED_R13).getAsInt();
        k = j.getAsJsonObject(ParserUtility.LTE_RRC_SUPPORTEDBANDCOMBREDUCED_R13_TREE);
        if (k != null) {
            dut.setCa1310Support(true);
            bwCombo = new ArrayList<>();
            tmp = j.getAsJsonObject(LTE_RRC_SUPPORTEDBANDCOMBREDUCED_R13_TREE);//
            // First for: cycle over ca combinations
            for (int i = 0; i < supportedCombinations; i++) {
                CACombination singleCombo = new CACombination();
                t = item.concat(Integer.toString(i));
                // set item index in the pcap file
                singleCombo.setItemIndex(i);
                iter = tmp.getAsJsonObject(t).getAsJsonObject(RRC_BAND_COMBINATION_PARAMETERS_R13_ELEMENT)
                        .getAsJsonObject(LTE_RRC_BAND_PARAMETERS_R13_TREE);
                if (tmp.getAsJsonObject(t).getAsJsonObject(RRC_BAND_COMBINATION_PARAMETERS_R13_ELEMENT)
                        .has(LTE_RRC_DIFFERENTFALLBACK_SUPPORTED_R13)) {
                    singleCombo.setDifferentFallbackSupported(true);
                }
                int ccNumber = tmp.getAsJsonObject(t).getAsJsonObject(RRC_BAND_COMBINATION_PARAMETERS_R13_ELEMENT)
                        .getAsJsonPrimitive(LTE_RRC_BAND_PARAMETERS_LIST_R13).getAsInt();
                // DL and UL arraylist arraylists
                ArrayList<ComponentCarrier> currentDLComb = new ArrayList<>();
                ArrayList<ComponentCarrier> currentULComb = new ArrayList<>();
                // manage CA combinations with multiple items
                for (int r = 0; r < ccNumber; r++) {
                    ComponentCarrier currentBandDL = new ComponentCarrier();
                    ComponentCarrier currentBandUL = new ComponentCarrier();
                    u = item.concat(Integer.toString(r));
                    tmp2 = iter.getAsJsonObject(u).getAsJsonObject(ParserUtility.LTE_RRC_BANDPARAMETERS_R13_ELEMENT);
                    currentBandDL.setBandID(tmp2.get(LTE_RRC_BANDEUTRA_R13).getAsInt());
                    currentBandUL.setBandID(tmp2.get(LTE_RRC_BANDEUTRA_R13).getAsInt());
                    // Manage DL/UL
                    UL = tmp2.getAsJsonObject(LTE_RRC_BAND_PARAMETERS_UL_R13_ELEMENT);
                    DL = tmp2.getAsJsonObject(LTE_RRC_BAND_PARAMETERS_DL_R13_ELEMENT);
                    if (UL == null) {
                        // if Only DL combination is present
                        currentBandDL.setComponentCarrierClass(Integer.toString(DL.get(LTE_RRC_CA_BANDWIDTHCLASS_DL_R13).getAsInt()));
                        if (DL.has(LTE_RRC_SUPPORTEDMIMO_CAPABILITYDL_R13)) {
                            currentBandDL.setSupportedLayers(DL.get(ParserUtility.LTE_RRC_SUPPORTEDMIMO_CAPABILITYDL_R13).getAsInt());
                            int tempLayersDL = DL.get(LTE_RRC_SUPPORTEDMIMO_CAPABILITYDL_R13).getAsInt();
                            if (tempLayersDL == 0) {
                                currentBandDL.setSupportedLayers(2);
                            } else if (tempLayersDL == 1) {
                                currentBandDL.setSupportedLayers(4);
                            } else if (tempLayersDL == 2) {
                                currentBandDL.setSupportedLayers(8);
                            }
                        } else {
                            currentBandDL.setSupportedLayers(1);
                            currentBandDL.setComponentCarrierClass(Integer.toString(DL.get(LTE_RRC_CA_BANDWIDTHCLASS_DL_R13).getAsInt()));
                        }
                        currentDLComb.add(currentBandDL);
                    } else if (DL != null) {
                        currentBandUL.setComponentCarrierClass(Integer.toString(UL.get(RRC_BANDWIDTH_CLASS_UL_R10).getAsInt()));
                        if (UL.has(RRC_SUPPORTED_MIMO_CAPABILITY_ul_REL10)) {
                            int tempLayersUL = UL.get(RRC_SUPPORTED_MIMO_CAPABILITY_ul_REL10).getAsInt();
                            if (tempLayersUL == 0) {
                                currentBandUL.setSupportedLayers(2);
                            } else if (tempLayersUL == 1) {
                                currentBandUL.setSupportedLayers(4);
                            }
                        } else {
                            currentBandUL.setSupportedLayers(1);
                        }
                        currentULComb.add(currentBandUL);
                        if (DL.has(LTE_RRC_SUPPORTEDMIMO_CAPABILITYDL_R13)) {
                            currentBandDL.setComponentCarrierClass(Integer.toString(DL.get(LTE_RRC_CA_BANDWIDTHCLASS_DL_R13).getAsInt()));
                            int tempLayersDL = DL.get(LTE_RRC_SUPPORTEDMIMO_CAPABILITYDL_R13).getAsInt();
                            if (tempLayersDL == 0) {
                                currentBandDL.setSupportedLayers(2);
                            } else if (tempLayersDL == 1) {
                                currentBandDL.setSupportedLayers(4);
                            } else if (tempLayersDL == 2) {
                                currentBandDL.setSupportedLayers(8);
                            }
                        } else {
                            currentBandDL.setSupportedLayers(1);
                            currentBandDL.setComponentCarrierClass(Integer.toString(DL.get(LTE_RRC_CA_BANDWIDTHCLASS_DL_R13).getAsInt()));
                        }
                        currentDLComb.add(currentBandDL);
                    }
                    tmp2 = null;
                    u = "";
                }
                singleCombo.setULSingleCombination(currentULComb);
                singleCombo.setDLSingleCombination(currentDLComb);
                bwCombo.add(singleCombo);
                t = "";
            }
        }
        dut.setCaV1310BandCombinations(bwCombo);
    }


    /**
     * method to check which band in r10 bandcombination supports 256QAM in Uplink
     *
     * @param j
     * @param dut
     */
    private void rfParametersv1430(JsonObject j, Mobile dut) {
        String item = "Item ";
        JsonObject itemIterator;
        JsonObject innerIterator;
        JsonObject t = j.getAsJsonObject(LTE_RRC_RF_PARAMETERS_1430_ELEMENT);
        JsonObject t2 = j.getAsJsonObject(LTE_RRC_RF_PARAMETERS_1430_ELEMENT);
        if (t.has(LTE_RRC_SUPPORTEDBANDCOMBINATIONS_1430)) {
            int numBnds = t.getAsJsonPrimitive(LTE_RRC_SUPPORTEDBANDCOMBINATIONS_1430).getAsInt();
            if (t.getAsJsonObject(LTE_RRC_SUPPORTEDBANDCOMBINATIONS_v1430_TREE) != null) {
                t = t.getAsJsonObject(LTE_RRC_SUPPORTEDBANDCOMBINATIONS_v1430_TREE);
                for (int i = 0; i < numBnds; i++) {
                    try {
                        itemIterator = t.getAsJsonObject(item.concat(Integer.toString(i)));
                        if (itemIterator.has(LTE_RRC_BANDCOMBINATIONPARAMETERS_v1430_ELEMENT)) {
                            int compNumber = itemIterator.getAsJsonObject(LTE_RRC_BANDCOMBINATIONPARAMETERS_v1430_ELEMENT)
                                    .getAsJsonPrimitive(LTE_RRC_BANDPARAMETERLIST_v1430).getAsInt();
                            if (itemIterator.getAsJsonObject(LTE_RRC_BANDCOMBINATIONPARAMETERS_v1430_ELEMENT).
                                    has(LTE_RRC_BANDPARAMETERLIST_v1430_TREE)) {
                                for (int k = 0; k < compNumber; k++) {
                                    innerIterator = itemIterator.getAsJsonObject(LTE_RRC_BANDCOMBINATIONPARAMETERS_v1430_ELEMENT)
                                            .getAsJsonObject(LTE_RRC_BANDPARAMETERLIST_v1430_TREE)
                                            .getAsJsonObject((item.concat(Integer.toString(k))));
                                    if (innerIterator.has(LTE_RRC_BANDPARAMETERS_v1430_ELEMENT)) {
                                        if (innerIterator.getAsJsonObject(LTE_RRC_BANDPARAMETERS_v1430_ELEMENT).has(LTE_RRC_UL_256_QAM)) {
                                            addSupportFor256QAMUL(i, dut);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("Band: " + i + " does not support UL 256 QAM");
                    }
                }
            }
        }
        //dut supports addR11 bandcombinations
        if (t2.has(LTE_RRC_SUPPORTEDBANDCOMBINATIONADD_v1430)) {
            System.out.println();
            int numBndsAdd = t2.getAsJsonPrimitive(LTE_RRC_SUPPORTEDBANDCOMBINATIONADD_v1430).getAsInt();
            System.out.println("Supported Band Combination add_v1430: (size " + numBndsAdd + " )");
            if (t2.getAsJsonObject(LTE_RRC_SUPPORTEDBANDCOMBINATIONS_v1430_TREE) != null) {
                t2 = t2.getAsJsonObject(LTE_RRC_SUPPORTEDBANDCOMBINATIONS_v1430_TREE);
                for (int i = 0; i < numBndsAdd; i++) {
                    try {
                        itemIterator = t2.getAsJsonObject(item.concat(Integer.toString(i)));
                        if (itemIterator.has(LTE_RRC_BANDCOMBINATIONPARAMETERS_v1430_ELEMENT)) {
                            int compNumber = itemIterator.getAsJsonObject(LTE_RRC_BANDCOMBINATIONPARAMETERS_v1430_ELEMENT)
                                    .getAsJsonPrimitive(LTE_RRC_BANDPARAMETERLIST_v1430).getAsInt();
                            if (itemIterator.getAsJsonObject(LTE_RRC_BANDCOMBINATIONPARAMETERS_v1430_ELEMENT).
                                    has(LTE_RRC_BANDPARAMETERLIST_v1430_TREE)) {
                                for (int k = 0; k < compNumber; k++) {
                                    innerIterator = itemIterator.getAsJsonObject(LTE_RRC_BANDCOMBINATIONPARAMETERS_v1430_ELEMENT)
                                            .getAsJsonObject(LTE_RRC_BANDPARAMETERLIST_v1430_TREE)
                                            .getAsJsonObject((item.concat(Integer.toString(k))));
                                    if (innerIterator.has(LTE_RRC_BANDPARAMETERS_v1430_ELEMENT)) {
                                        if (innerIterator.getAsJsonObject(LTE_RRC_BANDPARAMETERS_v1430_ELEMENT).has(LTE_RRC_UL_256_QAM)) {
                                            addSupportFor256QAMULadd(i, dut);
                                        }
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {
                        System.out.println("256 QAM UL not supported");
                    }
                }
            }
        }
    }

    private void addSupportFor256QAMUL(int combIndex, Mobile device) {
        if (device.getCaBandsCombinations().get(combIndex).getULSingleCombination() != null) {
            if (device.getCaBandsCombinations().get(combIndex).getULSingleCombination().size() != 0) {
                for (int j = 0; j < device.getCaBandsCombinations().get(combIndex).getULSingleCombination().size(); j++) {
                    device.getCaBandsCombinations().get(combIndex).
                            getULSingleCombination().get(j).setSupport256QAMUL(true);
                }
            }
        }
    }

    private void addSupportFor256QAMULadd(int combIndex, Mobile device) {
        combIndex = combIndex + 127;
        if (device.getCaBandsCombinations().get(combIndex).getULSingleCombination() != null) {
            if (device.getCaBandsCombinations().get(combIndex).getULSingleCombination().size() != 0) {
                for (int j = 0; j < device.getCaBandsCombinations().get(combIndex).getULSingleCombination().size(); j++) {
                    device.getCaBandsCombinations().get(combIndex).
                            getULSingleCombination().get(j).setSupport256QAMUL(true);
                }
            }
        }
    }
}




