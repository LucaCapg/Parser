package com.company.Parser;

import com.company.Model.DUT;
import com.company.Model.eMTC;
import com.company.Utility;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class eMTCParser extends Parser implements ParserInterface {
    private eMTC dut;

    public eMTCParser(FileReader jsonFile, eMTC dut) {
        this.builder = new GsonBuilder();
        this.gson = this.builder.create();
        this.builder.setPrettyPrinting();
        this.jsonFile = jsonFile;
        this.pcapJsonArray = this.gson.fromJson((jsonFile), JsonArray.class);
        if (dut == null) {
            this.dut = new eMTC();
        }
    }
    /**
     * Private support method to manage parsing of attach request message
     * @param objectIndex the current packet in the pcapjson array
     * @return the updated Cat M DUT
     */
    @Override
    public DUT attachParser(int objectIndex, DUT dut) {
        eMTC device = (eMTC) dut;
        ArrayList<String> keyValues = ParserUtility.attachJsonParsingTree;
        JsonObject temp1 = (JsonObject) pcapJsonArray.get(objectIndex);
        JsonObject temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0 ; k < keyValues.size()-1; k ++) {
            temp1 = temp1.getAsJsonObject(keyValues.get(k));
            temp2 = temp1.getAsJsonObject(keyValues.get(k+1));
        }
        // Check if it is an attach
        if (temp2.get(ParserUtility.NAS_EPS_MESSAGE_TYPE).getAsInt() == ParserUtility.ATTACH_REQUEST_NAS_CODE) {
            // Attach Type
            device.setAttachType(temp2.get(ParserUtility.NAS_EPS_ATTACH_TYPE).getAsInt());
            // Type of identity
            device.setIdentityType(temp2.getAsJsonObject(ParserUtility.EPS_MOBILE_IDENTITY).get(ParserUtility.NAS_EPS_TYPE_ID).getAsInt());
            // ESM information transfer flag
            if (temp2.has(ParserUtility.ESM_MESSAGE_CONTAINER)) {
                if (temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).has(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE) &&
                        temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE)
                                .has(ParserUtility.ESM_INFORMATION_TRANSFER_FLAG)) {
                    // PDN TYPE
                    device.setPdnType(temp2.getAsJsonObject(ParserUtility.ESM_MESSAGE_CONTAINER).getAsJsonObject(ParserUtility.NAS_EPS_EMM_ESM_MSG_CONT_TREE)
                            .get(ParserUtility.NAS_EPS_ESM_PDN_TYPE).getAsInt());
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
            }
            // Additional Update Type
            if (temp2.has(Utility.ADDITIONAL_UPDATE_TYPE)) {
                device.setAdditionalUpdateType(temp2.getAsJsonObject(Utility.ADDITIONAL_UPDATE_TYPE).get(ParserUtility.NAS_EPS_ADD_UPD_TYPE).getAsInt());
                device.setCiotNetworkBehaviour(temp2.getAsJsonObject(Utility.ADDITIONAL_UPDATE_TYPE).get(ParserUtility.NAS_EPS_PNB_CIOT).getAsInt());
            } else {
                device.setAdditionalUpdateType(Utility.NOT_APPLICABLE_CODE);
                device.setCiotNetworkBehaviour(0);
            }
            // Extended DRX Parameters
            if (temp2.has(ParserUtility.EXTENDED_DRX_PARAMETERS)) {
                // eDRX, Paging Time Window
                device.setPagingTimeWindow(temp2.getAsJsonObject(ParserUtility.EXTENDED_DRX_PARAMETERS).get(ParserUtility.GSM_GMM_PAGING_TIME_WINDOW).getAsString());
                device.seteDrxValue(temp2.getAsJsonObject(ParserUtility.EXTENDED_DRX_PARAMETERS).get(ParserUtility.GSM_GMM_EDRX_VALUE).getAsString());
            } else {
                device.seteDrxValue(Utility.MISSING_CODE);
                device.setPagingTimeWindow(Utility.MISSING_CODE);
            }
            // T3324
            if (temp2.has(ParserUtility.GPRS_TIMER_T3324)) {
                device.setT3324(temp2.getAsJsonObject(ParserUtility.GPRS_TIMER_T3324).getAsJsonObject(ParserUtility.T3324_TREE).get(ParserUtility.T3324_TIMER_VALUE).getAsInt(),
                        temp2.getAsJsonObject(ParserUtility.GPRS_TIMER_T3324).getAsJsonObject(ParserUtility.T3324_TREE).get(ParserUtility.T3324_TIMER_UNIT).getAsInt());
            } else {
                device.setT3324(Utility.ERROR_CODE,Utility.ERROR_CODE);
            }
            // T3412
            if (temp2.has(ParserUtility.GPRS_TIMER_T3412)) {
                device.setT3412(temp2.getAsJsonObject(ParserUtility.GPRS_TIMER_T3412).getAsJsonObject(ParserUtility.T3412_TREE).get(ParserUtility.T3412_TIMER_VALUE).getAsInt(),
                        temp2.getAsJsonObject(ParserUtility.GPRS_TIMER_T3412).getAsJsonObject(ParserUtility.T3412_TREE).get(ParserUtility.T3412_TIMER_UNIT).getAsInt());
            } else {
                device.setT3412(Utility.ERROR_CODE,Utility.ERROR_CODE);
            }
            // Ciphering and Integrity Algorithms
            if (temp2.has(ParserUtility.UE_NETWORK_CAPABILITY)){
                HashMap encrAlg = device.getLteCap().getEncrAlg();
                HashMap integrAlg = device.getLteCap().getIntegrAlg();
                JsonObject ciphAlgTree = temp2.getAsJsonObject(ParserUtility.UE_NETWORK_CAPABILITY);
                encrAlg.replace(Utility.EEA0, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA0).getAsInt());
                encrAlg.replace(Utility.EEA1128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EEA1).getAsInt());
                encrAlg.replace(Utility.EEA2128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EEA2).getAsInt());
                encrAlg.replace(Utility.EEA3, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA3).getAsInt());
                encrAlg.replace(Utility.EEA4, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA4).getAsInt());
                encrAlg.replace(Utility.EEA5, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA5).getAsInt());
                encrAlg.replace(Utility.EEA6, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA6).getAsInt());
                encrAlg.replace(Utility.EEA7, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA7).getAsInt());

                integrAlg.replace(Utility.EIA0, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA0).getAsInt());
                integrAlg.replace(Utility.EIA1128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EIA1).getAsInt());
                integrAlg.replace(Utility.EIA2128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EIA2).getAsInt());
                integrAlg.replace(Utility.EIA3, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA3).getAsInt());
                integrAlg.replace(Utility.EIA4, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA4).getAsInt());
                integrAlg.replace(Utility.EIA5, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA5).getAsInt());
                integrAlg.replace(Utility.EIA6, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA6).getAsInt());
                integrAlg.replace(Utility.EIA7, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA7).getAsInt());

                int emmRegistered = ciphAlgTree.get(ParserUtility.NAS_EPS_EMM_WO_PDN_CAP).getAsInt();
                if (emmRegistered == 1) device.setEmmRegisteredWoPDNSupport(true);
            }
        }
        return device;
    }

    @Override
    public DUT capabilitiesSpecificParser(DUT dut,int objectIndex) {
        eMTC device = (eMTC) dut;
        ArrayList<String> item3Values = ParserUtility.ueCapJsonGeneralParsingTreeItem3;
        JsonObject temp1 = (JsonObject) pcapJsonArray.get(objectIndex);
        JsonObject temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0 ; k < item3Values.size()-1; k ++) {
            temp1 = temp1.getAsJsonObject(item3Values.get(k));
            temp2 = temp1.getAsJsonObject(item3Values.get(k+1));
        }
        device = ceModeParser(temp2,device);
        ArrayList<String> keyValues = ParserUtility.ueCapJsonGeneralParsingTree;
        temp1 = (JsonObject) pcapJsonArray.get(objectIndex);
        temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0 ; k < keyValues.size()-1; k ++) {
            temp1 = temp1.getAsJsonObject(keyValues.get(k));
            temp2 = temp1.getAsJsonObject(keyValues.get(k+1));
        }

        keyValues = Utility.ueCapJsonParsingTree;
        temp1 = temp2;
        temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0 ; k < keyValues.size()-1; k ++) {
            temp1 = temp1.getAsJsonObject(keyValues.get(k));
            temp2 = temp1.getAsJsonObject(keyValues.get(k+1));
        }

        device = (eMTC) fgiRel8Parser(temp2,device);
        device.setAccessStratumRelease(Utility.accessStratumReleaseDecoder(temp2.get(ParserUtility.RRC_ACCESS_STRATUM_RELEASE).getAsInt()));
        // Ue tx antenna selection support
        if (temp2.has(ParserUtility.LTE_RRC_PHY_LAYERS_ELEMENT)){
            device.getLteCap().setTxAntennaSelectionSupport(temp2.getAsJsonObject(ParserUtility.LTE_RRC_PHY_LAYERS_ELEMENT)
                    .get(ParserUtility.LTE_RRC_TX_ANTENNA_SEL_SUPPORT).getAsInt());
            device.getLteCap().setUeSpecificRefsSigsSupport(temp2.getAsJsonObject(ParserUtility.LTE_RRC_PHY_LAYERS_ELEMENT)
                    .get(ParserUtility.LTE_RRC_UE_SPECIFIC_REFS_SIG_SUPPORTED).getAsInt());
        } else {
            device.getLteCap().setTxAntennaSelectionSupport(Utility.ERROR_CODE);
            device.getLteCap().setUeSpecificRefsSigsSupport(Utility.ERROR_CODE);
        }

        device = (eMTC) supportedBandsParser(temp2,device);
        JsonObject temp4 = null;
        JsonObject temp3 = null;
        //Cycle on non critical extensions elements
        boolean flag = true;
        temp1 = temp2.getAsJsonObject(ParserUtility.RRC_NON_CRITICAL_EXTENSION_ELEMENT);
        temp4 = temp1.getAsJsonObject(ParserUtility.RRC_LATE_NONCRITICALEXTENSION_TREE);
        temp3 = temp1.getAsJsonObject(ParserUtility.RRC_RFPARAMETERS_V1250_ELEMENT);
        while (flag) {
            if (temp1 == null) {
                flag = false;
                // check previous tree
                temp1 = temp2;
            } else {
                if ((temp1.has(ParserUtility.RRC_FGI_REL10_TREE)) && (temp1.has(ParserUtility.RRC_FGI_REL10_TREE))) device = (eMTC) fgiRel10Parser(temp1,device);
                // no matter if overwrite is done
                device = categoryParser(temp1,device);
                if (temp1.has(ParserUtility.LTE_RRC_FDD_ADD_EUTRA_CAP_1060)) device = (eMTC) fgirel10TDDFDDParser(temp1,device);
                if (temp4 != null) fgiRel9AddR9Parser(temp4,device);
                temp4 = temp1.getAsJsonObject(ParserUtility.RRC_LATE_NONCRITICALEXTENSION_TREE);
                // temp1 switch to next element
                try {
                    temp1 = temp1.getAsJsonObject(ParserUtility.RRC_NON_CRITICAL_EXTENSION_ELEMENT);
                } catch (Exception e){
                    temp1 = null;
                    //e.printStackTrace();
                }
                temp3 = temp1.getAsJsonObject(ParserUtility.RRC_RFPARAMETERS_V1250_ELEMENT);
            }
        }

        return device;
    }

    @Override
    public Integer capabilityPacketParser(JsonObject cap, int i) {
        return null;
    }

    /**
     * Private support method to retrieve ceMode from ue capabilities parsing tree
     * @param s current json object in the parsing tree
     */
    private eMTC ceModeParser(JsonObject s,eMTC dut) {
        ArrayList<String> keyValues = ParserUtility.ueCapCatMJsonParsingTree;
        JsonObject temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0 ; k < keyValues.size()-1; k ++) {
            s = s.getAsJsonObject(keyValues.get(k));
            temp2 = s.getAsJsonObject(keyValues.get(k+1));
        }
        dut.setCeModeA(temp2.get(ParserUtility.LTE_RRC_CE_MODEA_R13).getAsInt());
        if (temp2.has(ParserUtility.LTE_RRC_CE_MODEB_R13)) {
            dut.setCeModeB(temp2.get(ParserUtility.LTE_RRC_CE_MODEB_R13).getAsInt());
        } else {
            dut.setCeModeB(1);
        }
        return dut;
    }
    /**
     * Private support method to manage parsing of catM categories
     * @param j current object in json parsing tree
     */
    private eMTC categoryParser(JsonObject j, eMTC dut) {
        Set<Map.Entry<String, JsonElement>> entries = j.entrySet();
        for (Map.Entry<String, JsonElement> entry: entries) {
            String cat = Utility.CATEGORY_REGEX;
            String catDL = Utility.CATEGORY_DL_REGEX;
            String catUL = Utility.CATEGORY_UL_REGEX;
            // Parse key string to check if it is related to DL,UL or general
            if (entry.getKey().toLowerCase().matches(cat)) {
                if (entry.getKey().matches(catDL)) {
                    dut.setCategoryDL(entry.getValue().getAsInt());
                } else if (entry.getKey().matches(catUL)) {
                    dut.setCategoryUL(entry.getValue().getAsInt());
                }
            }
        }// for
        return dut;
    }
}
