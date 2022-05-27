package com.company.Parser;

import com.company.Model.DUT;
import com.company.Model.NBIoT;
import com.company.Model.lte.LTEBand;
import com.company.Utility;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;

public class NBIoTParser extends Parser {
    private NBIoT dut;
    public NBIoTParser(FileReader jsonFile, NBIoT dut){
        this.builder = new GsonBuilder();
        this.gson = this.builder.create();
        this.builder.setPrettyPrinting();
        this.jsonFile = jsonFile;
        this.pcapJsonArray = this.gson.fromJson((jsonFile), JsonArray.class);
        if (dut == null) {
            this.dut = new NBIoT();
        }
    }
    @Override
    public DUT attachParser(int objectIndex, DUT dut) {
        NBIoT device = (NBIoT) dut;
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
            // Extended DRX parameters
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

                HashMap ciphAlgMap = device.getLteCap().getEncrAlg();
                HashMap integrMap = device.getLteCap().getIntegrAlg();
                JsonObject ciphAlgTree = temp2.getAsJsonObject(ParserUtility.UE_NETWORK_CAPABILITY);
                ciphAlgMap.replace(Utility.EEA0, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA0).getAsInt());
                ciphAlgMap.replace(Utility.EEA1128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EEA1).getAsInt());
                ciphAlgMap.replace(Utility.EEA2128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EEA2).getAsInt());
                ciphAlgMap.replace(Utility.EEA3, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA3).getAsInt());
                ciphAlgMap.replace(Utility.EEA4, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA4).getAsInt());
                ciphAlgMap.replace(Utility.EEA5, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA5).getAsInt());
                ciphAlgMap.replace(Utility.EEA6, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA6).getAsInt());
                ciphAlgMap.replace(Utility.EEA7, ciphAlgTree.get(ParserUtility.NAS_EPS_EEA7).getAsInt());

                integrMap.replace(Utility.EIA0, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA0).getAsInt());
                integrMap.replace(Utility.EIA1128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EIA1).getAsInt());
                integrMap.replace(Utility.EIA2128, ciphAlgTree.get(ParserUtility.NAS_EPS_128EIA2).getAsInt());
                integrMap.replace(Utility.EIA3, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA3).getAsInt());
                integrMap.replace(Utility.EIA4, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA4).getAsInt());
                integrMap.replace(Utility.EIA5, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA5).getAsInt());
                integrMap.replace(Utility.EIA6, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA6).getAsInt());
                integrMap.replace(Utility.EIA7, ciphAlgTree.get(ParserUtility.NAS_EPS_EIA7).getAsInt());

                int emmRegistered = ciphAlgTree.get(ParserUtility.NAS_EPS_EMM_WO_PDN_CAP).getAsInt();
                if (emmRegistered == 1) device.setEmmRegisteredWoPDNSupport(true);
            }
        }
        return dut;
    }

    @Override
    public DUT attachAcceptParser(DUT dut, int objectIndex) {
        return null;
    }

    @Override
    public DUT capabilitiesSpecificParser(DUT dut, int objectIndex) {
        NBIoT device = (NBIoT) dut;
        JsonObject UECap = (JsonObject) pcapJsonArray.get(objectIndex);
        ArrayList<String> item3Values = ParserUtility.ueCapJsonGeneralParsingTreeItem2;
        JsonObject temp1 = (JsonObject) pcapJsonArray.get(objectIndex);
        JsonObject temp2 = null;
        // -1 so that we do not encounter null pointer exceptions
        for (int k = 0 ; k < item3Values.size()-1; k ++) {
            temp1 = temp1.getAsJsonObject(item3Values.get(k));
            temp2 = temp1.getAsJsonObject(item3Values.get(k+1));
        }
        if (temp2.has(ParserUtility.LTE_RRC_UE_CATEGORY_NB_R13)) {
            // Category
            device.setCategory(temp2.get(ParserUtility.LTE_RRC_UE_CATEGORY_NB_R13).getAsInt());
        }
        device.setAccessStratumRelease(temp2.get(ParserUtility.LTE_RRC_AS_RELEASE_R13).getAsInt());

        if (temp2.has(ParserUtility.LTE_RRC_PHY_LAY_R13_ELEMENT)) {
            JsonObject phyLay13Element = temp2.getAsJsonObject(ParserUtility.LTE_RRC_PHY_LAY_R13_ELEMENT);
            // These fields should mean supported if present
            if (phyLay13Element.has(ParserUtility.LTE_RRC_MULTITONE_R13)) {
                device.setMultiTone(true);
            } else {
                device.setMultiTone(false);
            }
            if (phyLay13Element.has(ParserUtility.LTE_RRC_MULTICARRIER_R13)) {
                device.setMultiCarrier(true);
            } else {
                device.setMultiCarrier(false);
            }
        }

        JsonObject supportedBandsTree = temp2.getAsJsonObject(ParserUtility.LTE_RRC_RF_PARAMETERS_R13_ELEMENT);
        int supportedBandsNumber = supportedBandsTree.get(ParserUtility.LTE_RRC_SUPPORTED_BAND_LIST_R13).getAsInt();
        ArrayList<LTEBand> supportedBands = new ArrayList<LTEBand>();
        String itemIndex = "Item ";
        JsonObject t = null;
        for (int i = 0; i < supportedBandsNumber; i++) {
            LTEBand currentBand = new LTEBand();
            t = supportedBandsTree.getAsJsonObject(ParserUtility.LTE_RRC_SUPPORTED_BAND_LIST_R13_TREE).getAsJsonObject(itemIndex.concat(Integer.toString(i)))
                    .getAsJsonObject(ParserUtility.LTE_RRC_SUPPORTED_BAND_NB_ELEMENT);
            currentBand.setBandID(t.get(ParserUtility.LTE_RRC_BAND_R13).getAsInt());
            supportedBands.add(currentBand);
        }
        device.getLteCap().setSupportedBands(supportedBands);
        return device;
    }

    @Override
    public Integer capabilityPacketParser(JsonObject cap, int i) {
        return null;
    }
}
