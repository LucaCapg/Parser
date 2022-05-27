package com.company.Parser;

import com.company.FileManager;
import com.company.Model.DUT;
import com.company.Model.LegacyDUT;
import com.company.Model.lte.LTEBand;
import com.company.Utility;
import com.google.gson.*;

import java.io.FileReader;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public abstract class Parser implements ParserInterface {
    protected JsonArray pcapJsonArray;
    protected GsonBuilder builder;
    protected Gson gson;
    protected FileReader jsonFile;
    // Code used to distinguish among packets
    protected int procedureCode;
    protected int UECapCounter;
    protected HashMap<JsonObject, Integer> capabilityPackets = new HashMap<>();
    Map.Entry pair;

    public Parser() {
        super();
    }

    /**
     * Private support method to check at first if the input NAS pcap has only one
     * attach request/Ue capabilities
     * @return true if there is only ONE attach request and ONE UE capabilities information
     */
    public boolean capabilitiesAttachValidator(DUT dut) {

        int n =0;
        boolean singleInstance = true;
        int attachCounter = 0;
        UECapCounter = 0;
        for (int i = 0; i < pcapJsonArray.size(); i++){
            if (pcapJsonArray.get(i).isJsonObject()){
                JsonObject targetPacket = (JsonObject) pcapJsonArray.get(i);
                JsonObject s1ap = targetPacket.getAsJsonObject(ParserUtility.SOURCE).getAsJsonObject(ParserUtility.LAYERS).getAsJsonObject(ParserUtility.S1AP);
                if (s1ap != null) {
                    JsonObject s1apPDUTree = s1ap.getAsJsonObject(ParserUtility.S1AP_PDU_TREE);
                    JsonObject s1apInitiatingMessage = s1apPDUTree.getAsJsonObject(ParserUtility.S1AP_INITIATING_MESSAGE_ELEMENT);
                    if(s1apInitiatingMessage != null) {
                        try {
                            procedureCode = s1apInitiatingMessage.get(ParserUtility.S1AP_PROCEDURE_CODE).getAsInt();
                            if (procedureCode == 12) {
                                ArrayList<String> keyValues = ParserUtility.singleInstanceCheckerTree;
                                JsonObject temp1 = s1apInitiatingMessage;
                                JsonObject temp2 = null;
                                // Cycle over non interesting fields
                                boolean flag = true;
                                int k = 0;
                                while (flag) {
                                    temp1 = temp1.getAsJsonObject(keyValues.get(k));
                                    temp2 = temp1.getAsJsonObject(keyValues.get(k + 1));
                                    k++;
                                    if (temp2 == null) {
                                        flag = false;
                                    }
                                }
                                // WIRESHARK 3.6 update converted fields value from int to hex
                                if (temp1.has(ParserUtility.NAS_EPS_MESSAGE_TYPE)) {
                                    if (toHex(temp1.get(ParserUtility.NAS_EPS_MESSAGE_TYPE).getAsString()) == ParserUtility.ATTACH_REQUEST_NAS_CODE) {
                                        attachCounter++;
                                    }
                                }
                            } else if (procedureCode == 22) {
                                // TODO: 31-Jan-20 optimize capability packets adding based on their actual utility?
                                UECapCounter++;
                                capabilityPackets.put(s1apInitiatingMessage, i);
                            }
                            else if (procedureCode == 9){
                                System.out.println("Attach Accept Found");
                                ArrayList<String> keyValues = ParserUtility.attachAcceptJsonParsingTree;
                                JsonObject temp1 = s1apInitiatingMessage;
                                JsonObject temp2 = null;
                                // Cycle over non interesting fields
                                boolean flag = true;
                                int k = 0;
                                while (flag && k < keyValues.size()-1) {
                                    temp1 = temp1.getAsJsonObject(keyValues.get(k));
                                    temp2 = temp1.getAsJsonObject(keyValues.get(k + 1));
                                    k++;
                                    if (temp2 == null) {
                                        flag = false;
                                    }
                                }
                            }
                        } catch (NullPointerException | UnsupportedEncodingException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }
        if ((attachCounter != 1)) {
            singleInstance = false;
            Utility.ERROR_CAUSE = "Please, input a valid file (e.g., with 1 Attach Request, 1 UE capability Information)." +
                    "Attach Requests in file: " + attachCounter;
        }
        else {
            System.out.println("Number of Attach Requests: "+ attachCounter);
        }
        //2nd to execute
        /*
        if (UECapCounter != 1) {
            System.out.println("Number of CapabilityInfoIndication messages: " + UECapCounter);
            System.out.println();
        }

         */
        System.out.println("Number of UECapabilityInformation messages: " + UECapCounter);
        System.out.println();

        return singleInstance;
    }

    public int toHex(String hexVal) throws UnsupportedEncodingException {
        String hexaValueNoZero = hexVal.substring(2);
        int decValue = Integer.parseInt(hexaValueNoZero, 16);
        return decValue;
    }



    /**
     * Parses Attach and UE capabilities
     */
    public DUT packetParser(DUT d){
        if (d == null) {
            d = new DUT();
        }
        for (int i = 0; i < pcapJsonArray.size(); i++){
            if (pcapJsonArray.get(i).isJsonObject()){
                JsonObject s1ap = ((JsonObject) pcapJsonArray.get(i)).getAsJsonObject(ParserUtility.SOURCE).getAsJsonObject(ParserUtility.LAYERS).getAsJsonObject(ParserUtility.S1AP);
                if (s1ap != null) {
                    JsonObject s1apInitiatingMessage = s1ap.getAsJsonObject(ParserUtility.S1AP_PDU_TREE)
                            .getAsJsonObject(ParserUtility.S1AP_INITIATING_MESSAGE_ELEMENT);
                        try {
                            // Retrieve type of packet (Attach, UECap, etc.)

                                procedureCode = s1apInitiatingMessage.get(ParserUtility.S1AP_PROCEDURE_CODE).getAsInt();
                                if (procedureCode == 12) {
                                    // procedureCode = 12 does not identify uniquely Attach Request packets,
                                    // Further check is needed:
                                    ArrayList<String> keyValues = ParserUtility.singleInstanceCheckerTree;
                                    JsonObject temp1 = s1apInitiatingMessage;
                                    JsonObject temp2 = null;
                                    // Cycle over non interesting fields
                                    boolean flag = true;
                                    int k = 0;
                                    while (flag) {
                                        temp1 = temp1.getAsJsonObject(keyValues.get(k));
                                        temp2 = temp1.getAsJsonObject(keyValues.get(k + 1));
                                        k++;
                                        if (temp2 == null) {
                                            flag = false;
                                        }
                                    }
                                    //if (temp1.get(ParserUtility.NAS_EPS_MESSAGE_TYPE).getAsInt() == ParserUtility.ATTACH_REQUEST_NAS_CODE)
                                    if (toHex(temp1.get(ParserUtility.NAS_EPS_MESSAGE_TYPE).getAsString()) == ParserUtility.ATTACH_REQUEST_NAS_CODE) {
                                        d = attachParser(i, d);
                                    }
                                } else if (procedureCode == 22) {
                                    //3rd to execute
                                    if (UECapCounter == 1) {
                                        d = capabilitiesSpecificParser(d, i);
                                    } else {
                                        //there are >1 capability messages
                                        int cnt = 0;
                                        for (Map.Entry<JsonObject, Integer> entry : capabilityPackets.entrySet()) {
                                            //entire capabilityInfoIndication message is added to hashmap
                                            Integer packetToBeParsedIndex = capabilityPacketParser(entry.getKey(), entry.getValue());
                                            cnt++;
                                            if (packetToBeParsedIndex == 0) {
                                                continue;
                                            }
                                            if (packetToBeParsedIndex >= 0) {
                                                d = capabilitiesSpecificParser(d, packetToBeParsedIndex);
                                                break;
                                            } else if (packetToBeParsedIndex <= 0 && cnt == capabilityPackets.size()) {
                                                d = capabilitiesSpecificParser(d, Math.abs(packetToBeParsedIndex));
                                                break;
                                            }


                                            // do what you have to do here
                                            // In your case, another loop.
                                        }
                                    }
                                }
                                else if (procedureCode == 9) {
                                    attachAcceptParser(d, i);


                                }

                        } catch (NullPointerException | UnsupportedEncodingException e) {
                            // Not a packet of interest
                            //e.printStackTrace();
                        }

                }
            }
        } // for
        FileManager.deleteTempFile(jsonFile);
        return d;
    }



    /**
     * Packet Parser for SGSAP messages
     * @return
     */
    @Override
    public DUT svnParser(DUT dut) throws UnsupportedEncodingException {
        int lauCounter = 0;
        String imeiSV = null;
        for (int i = 0; i < pcapJsonArray.size(); i++){
            if (pcapJsonArray.get(i).isJsonObject()){
                JsonObject targetPacket = (JsonObject) pcapJsonArray.get(i);
                JsonObject source = targetPacket.getAsJsonObject(ParserUtility.SOURCE);
                JsonObject layers = source.getAsJsonObject(ParserUtility.LAYERS);
                JsonObject sgsap = layers.getAsJsonObject(ParserUtility.SGSAP);
                if (sgsap.has(ParserUtility.SGSAP_MSG_TYPE)) {
                    if (toHex(sgsap.get(ParserUtility.SGSAP_MSG_TYPE).getAsString()) == ParserUtility.LOCATION_UPDATE_REQUEST_CODE) {
                        lauCounter++;
                        // Check IMEI SV portion in packet using regex
                        Set<Map.Entry<String, JsonElement>> entries = sgsap.entrySet();
                        for (Map.Entry<String, JsonElement> entry: entries) {
                            String imeisvRegex = Utility.IMEISV_REGEX;
                            // Parse key string to check if it is related to DL,UL or general
                            Pattern p = Pattern.compile(Utility.IMEISV_REGEX);
                            Matcher m = p.matcher(entry.getKey());
                            if (m.find()) {
                                imeiSV  = sgsap.getAsJsonObject(entry.getKey()).get(ParserUtility.SGSAP_IMEISV).getAsString();
                                imeiSV = imeiSV.substring(imeiSV.length() - 2);
                            }
                        }// for
                    }
                }

            }
        }
        // Check number of lau
        if (lauCounter != 1) {
            dut.setImeiSV(Utility.NOT_SET);
            System.out.println("Failed to fetch IMEI SV. Number of LAU in packet: " + lauCounter);
        } else {
            dut.setImeiSV(imeiSV);
        }
        FileManager.deleteTempFile(jsonFile);
        return dut;
    }
    /**
     * Private method to retrieve Feature group indicators of Release 10
     * @param k Current jsonobject in non critical extensions tree
     */
    public LegacyDUT fgiRel8Parser (JsonObject k,LegacyDUT dut){
        LegacyDUT device = (LegacyDUT) dut;
        JsonObject spp = k.getAsJsonObject(ParserUtility.RRC_FGI_TREE);
        int[] fgi = new int[32];
        String tmp = Utility.FGI;
        for (int s = 0; s < fgi.length; s++) {
            fgi[s] = spp.get(tmp.concat(Integer.toString(s+1))).getAsInt();
        }
        device.getLteCap().setFeatureGroupIndicators(fgi);
        return device;
    }
    /**
     * Private support method to retrieve Release 10 FGIs
     * @param k current jsonobject in the json file tree
     */
    public LegacyDUT fgiRel10Parser(JsonObject k, LegacyDUT dut) {
        JsonObject spp = k.getAsJsonObject(ParserUtility.RRC_FGI_REL10_TREE);
        int[] fgi = new int[32];
        String tmp = Utility.FGI;
        String fgiAsString = "";
        for (int s = 0; s < fgi.length; s++) {
            fgi[s] = spp.get(tmp.concat(Integer.toString(s+101))).getAsInt();
            fgiAsString = fgiAsString.concat(Integer.toString(fgi[s]));
        }
        dut.getLteCap().setFeatureGroupIndicatorsR10(fgi);
        return dut;
    }
    public LegacyDUT fgirel10TDDFDDParser(JsonObject obj, LegacyDUT dut) {
        if(obj.has(ParserUtility.LTE_RRC_FDD_ADD_EUTRA_CAP_1060)) {
            JsonObject fddTree = obj.getAsJsonObject(ParserUtility.LTE_RRC_FDD_ADD_EUTRA_CAP_1060);//.getAsJsonObject(ParserUtility.LTE_RRC_FGI_REL10_V1060_TREE);
            if (fddTree.has(ParserUtility.LTE_RRC_FGI_REL10_V1060_TREE)) {
                fddTree = obj.getAsJsonObject(ParserUtility.LTE_RRC_FDD_ADD_EUTRA_CAP_1060).getAsJsonObject(ParserUtility.LTE_RRC_FGI_REL10_V1060_TREE);
                if (fddTree != null) {
                    int[] fgiR10FDD = new int[32];
                    String tmp = ParserUtility.RRC_CAP_FEAT_GROUP_IND;
                    for (int s = 0; s < fgiR10FDD.length; s++) {
                        fgiR10FDD[s] = fddTree.get(tmp.concat(Integer.toString(s + 101))).getAsInt();
                    }
                    dut.getLteCap().setFeatureGroupIndicatorsR10FDD(fgiR10FDD);
                }
            }
        }
        if(obj.has(ParserUtility.LTE_RRC_TDD_ADD_EUTRA_CAP_1060)) {
            JsonObject tddTree = obj.getAsJsonObject(ParserUtility.LTE_RRC_TDD_ADD_EUTRA_CAP_1060);//.getAsJsonObject(ParserUtility.LTE_RRC_FGI_REL10_V1060_TREE);
            if (tddTree.has(ParserUtility.LTE_RRC_FGI_REL10_V1060_TREE)) {
                tddTree = obj.getAsJsonObject(ParserUtility.LTE_RRC_TDD_ADD_EUTRA_CAP_1060).getAsJsonObject(ParserUtility.LTE_RRC_FGI_REL10_V1060_TREE);
                if (tddTree != null) {
                    int[] fgiR10TDD = new int[32];
                    String tmp = ParserUtility.RRC_CAP_FEAT_GROUP_IND;
                    for (int s = 0; s < fgiR10TDD.length; s++) {
                        fgiR10TDD[s] = tddTree.get(tmp.concat(Integer.toString(s + 101))).getAsInt();
                    }
                    dut.getLteCap().setFeatureGroupIndicatorsR10TDD(fgiR10TDD);
                }
            }
        }
        return dut;
    }
    public LegacyDUT fgiRel9AddR9Parser(JsonObject k, LegacyDUT dut) {
        JsonObject spp = k.getAsJsonObject(ParserUtility.RRC_UECAPABILITY_V9A0_ELEMENT);
        // Manage TDD and FDD rel9 FGIs
        if (spp.has(ParserUtility.LTE_RRC_FDD_EUTRA_CAP_R9_ELEMENT)) {
            if(spp.has(ParserUtility.LTE_RRC_FDD_EUTRA_CAP_R9_ELEMENT)) {
                JsonObject fddFgis = spp.getAsJsonObject(ParserUtility.LTE_RRC_FDD_EUTRA_CAP_R9_ELEMENT);
                // Add Rel 9 FDD FGIs
                JsonObject fddAddR9Fgis = fddFgis.getAsJsonObject(ParserUtility.RRC_FGI_ADD_REL9_TREE);
                if (fddAddR9Fgis != null) {
                    int[] fgiAddR9Fdd = new int[32];
                    String tmp = ParserUtility.RRC_CAP_FEAT_GROUP_IND;
                    for (int s = 0; s < fgiAddR9Fdd.length; s++) {
                        fgiAddR9Fdd[s] = fddAddR9Fgis.get(tmp.concat(Integer.toString(s + 33))).getAsInt();
                    }
                    dut.getLteCap().setFeatureGroupIndicatorsFDDAddR9(fgiAddR9Fdd);
                }
                // Rel 9 FDD FGIs
                if (fddFgis.has(ParserUtility.LTE_RRC_FGI_R9_TREE)) {
                    JsonObject fddR9Fgis = fddFgis.getAsJsonObject(ParserUtility.LTE_RRC_FGI_R9_TREE);
                    if (fddR9Fgis != null) {
                        int[] fgiR9Fdd = new int[32];
                        String tmp = ParserUtility.RRC_CAP_FEAT_GROUP_IND;
                        for (int s = 0; s < fgiR9Fdd.length; s++) {
                            fgiR9Fdd[s] = fddR9Fgis.get(tmp.concat(Integer.toString(s + 1))).getAsInt();
                        }
                        dut.getLteCap().setFeatureGroupIndicatorsFDDR9(fgiR9Fdd);
                    }
                }
            }

            // Add Rel 9 TDD FGIs
            if(spp.has(ParserUtility.LTE_RRC_TDD_EUTRA_CAP_R9_ELEMENT)) {
                JsonObject tddFgiTree = spp.getAsJsonObject(ParserUtility.LTE_RRC_TDD_EUTRA_CAP_R9_ELEMENT);
                JsonObject tddFgiAddR9Tree = tddFgiTree.getAsJsonObject(ParserUtility.RRC_FGI_ADD_REL9_TREE);
                if (tddFgiAddR9Tree != null) {
                    int[] fgiAddR9Tdd = new int[32];
                    String tmp = ParserUtility.RRC_CAP_FEAT_GROUP_IND;
                    for (int s = 0; s < fgiAddR9Tdd.length; s++) {
                        fgiAddR9Tdd[s] = tddFgiAddR9Tree.get(tmp.concat(Integer.toString(s + 33))).getAsInt();
                    }
                    dut.getLteCap().setFeatureGroupIndicatorsTDDAddR9(fgiAddR9Tdd);
                }

                // Rel9 TDD FGIs
                JsonObject tddR9Tree = tddFgiTree.getAsJsonObject(ParserUtility.LTE_RRC_FGI_R9_TREE);
                if (tddR9Tree != null) {
                    int[] fgiR9Tdd = new int[32];
                    String tmp = ParserUtility.RRC_CAP_FEAT_GROUP_IND;
                    for (int s = 0; s < fgiR9Tdd.length; s++) {
                        fgiR9Tdd[s] = tddR9Tree.get(tmp.concat(Integer.toString(s + 1))).getAsInt();
                    }
                    dut.getLteCap().setFeatureGroupIndicatorsTDDR9(fgiR9Tdd);
                }
            }
        }

        //Rel9 Add R9 without TDD or FDD
        if(spp.has(ParserUtility.RRC_FGI_ADD_REL9_TREE)) {
        spp = spp.getAsJsonObject(ParserUtility.RRC_FGI_ADD_REL9_TREE);
        if (spp != null) {
            int[] fgiR9 = new int[32];
            String tmp = ParserUtility.RRC_CAP_FEAT_GROUP_IND;
            for (int s = 0; s < fgiR9.length; s++) {
                fgiR9[s] = spp.get(tmp.concat(Integer.toString(s + 33))).getAsInt();
            }
            dut.getLteCap().setFeatureGroupIndicatorsAddR9(fgiR9);
        }
        }
        return dut;
    }
    /**
     * Support method to retrieve supported bands
     * @param k jsonobject in the UE Cap tree
     */
    public LegacyDUT supportedBandsParser (JsonObject k,LegacyDUT dut) {
        int supportedBandsNumber = k.getAsJsonObject(ParserUtility.RRC_RF_PARAMETERS_ELEMENT).get(ParserUtility.RRC_SUPPORTED_BAND_LIST_EUTRA).getAsInt();
        k = k.getAsJsonObject(ParserUtility.RRC_RF_PARAMETERS_ELEMENT).getAsJsonObject(ParserUtility.RRC_SUPPORTED_BAND_LIST_EUTRA_TREE);
        JsonObject tmp;
        ArrayList<LTEBand> supportedBandList = new ArrayList<LTEBand>(supportedBandsNumber);
        String itemIndex = "Item ";
        for (int r = 0; r < supportedBandsNumber; r++) {
            tmp = k.getAsJsonObject(itemIndex.concat(Integer.toString(r)));
            tmp = tmp.getAsJsonObject(ParserUtility.SUPPORTED_BAND_EUTRA_ELEMENT);
            LTEBand b = new LTEBand(tmp.get(ParserUtility.RRC_BAND_EUTRA).getAsInt(), r);
            supportedBandList.add(r,b);
        }
        dut.getLteCap().setSupportedBands(supportedBandList);
        return dut;
    }

    /**
     * Support method to parse son parameters rach report r9
     * @param k jsonobject in the parsing tree
     * @param dut the instance of the device under testing
     * @return the instance of the device correctly updated
     */
    public LegacyDUT sonParametersR9Parser(JsonObject k, LegacyDUT dut){
        if(k.has(ParserUtility.LTE_RRC_SON_PARAMETERS_R9_ELEMENT)) {
            if(k.getAsJsonObject(ParserUtility.LTE_RRC_SON_PARAMETERS_R9_ELEMENT)
                    .get(ParserUtility.LTE_RRC_RACH_REPORT_R9) != null) {
                int sonRachRep = k.getAsJsonObject(ParserUtility.LTE_RRC_SON_PARAMETERS_R9_ELEMENT)
                        .get(ParserUtility.LTE_RRC_RACH_REPORT_R9).getAsInt();
                if (sonRachRep == 0) {
                    dut.getLteCap().setRachReportSON(true);
                }
            }

        }else {
            dut.getLteCap().setRachReportSON(false);
        }
        return dut;
    }
}
