package com.company.Model;

import com.company.Model.lte.LTECapability;
import com.company.Utility;

import java.util.HashMap;

/**
 * Model class representing a NarrowBand IoT device
 */
public class NBIoT extends DUT {
    private String category;
    private boolean multiTone;
    private boolean multiCarrier;
    private LTECapability lteCap;

    public NBIoT() {
        super();
        this.lteCap = new LTECapability();
    }

    /**
     * Set and decoded NB-IoT paging time window
     * @param ptwNbIoT
     */
    @Override
    public void setPagingTimeWindow(String ptwNbIoT) {
        if (ptwNbIoT.equals(Utility.MISSING_CODE)) {
            this.pagingTimeWindow = Utility.ERROR_CODE;
        } else {
            ptwNbIoT = ptwNbIoT.replace("0x","");
            int decodedValue = Integer.parseInt(ptwNbIoT,16);
            switch (decodedValue) {
                case 0: this.pagingTimeWindow = 2.56f;
                    break;
                case 1: this.pagingTimeWindow = 5.12f;
                    break;
                case 2: this.pagingTimeWindow = 7.68f;
                    break;
                case 3: this.pagingTimeWindow = 10.24f;
                    break;
                case 4: this.pagingTimeWindow = 12.8f;
                    break;
                case 5: this.pagingTimeWindow = 15.36f;
                    break;
                case 6: this.pagingTimeWindow = 17.92f;
                    break;
                case 7: this.pagingTimeWindow = 20.48f;
                    break;
                case 8: this.pagingTimeWindow = 23.04f;
                    break;
                case 9: this.pagingTimeWindow = 25.6f;
                    break;
                case 10: this.pagingTimeWindow = 28.16f;
                    break;
                case 11: this.pagingTimeWindow = 30.72f;
                    break;
                case 12: this.pagingTimeWindow = 33.28f;
                    break;
                case 13: this.pagingTimeWindow = 35.84f;
                    break;
                case 14: this.pagingTimeWindow = 38.4f;
                    break;
                case 15: this.pagingTimeWindow = 40.96f;
                    break;
                default: this.pagingTimeWindow = Utility.ERROR_CODE;
                    break;
            }
        }
    }
    public String getCategory() {
        return category;
    }
    /**
     * Set and parse nbiot r13 category
     * @param nbIoTCategory the code of the category retrieved from the pcap
     */
    public void setCategory(int nbIoTCategory) {
        switch (nbIoTCategory) {
            case 0: this.category = Utility.NB_CATEGORY_1;
                break;
            case 1: this.category = Utility.NB_CATEGORY_2;
                break;
            default: this.category = Utility.MISSING_CODE;
                break;
        }
    }
    public boolean isMultiTone() {
        return multiTone;
    }
    public void setMultiTone(boolean multiTone) {
        this.multiTone = multiTone;
    }
    public boolean isMultiCarrier() {
        return multiCarrier;
    }
    public void setMultiCarrier(boolean multiCarrier) {
        this.multiCarrier = multiCarrier;
    }
    /**
     * Set and parse nb iot as release
     * @param accessStratumRelease the as release code retrieved from the pcap
     */
    @Override
    public void setAccessStratumRelease(int accessStratumRelease) {
        switch (accessStratumRelease) {
            case 0: this.accessStratumRelease = 13;
                break;
            case 1: this.accessStratumRelease = 14;
                break;
            default: this.accessStratumRelease = Utility.ERROR_CODE;
                break;
        }
    }
    public LTECapability getLteCap() {
        return lteCap;
    }
    public static void printDUTNBIoTCap(NBIoT d ){
        System.out.println("NB-IoT DUT");
        System.out.println("Attach Type: " + Utility.attachTypeDecoder(d.getAttachType()));
        System.out.println("Additional Update Type: " + d.getAdditionalUpdateType());
        System.out.println("Type of identity:  "  + typeOfIdentityDecoder(d.getIdentityType()));
        System.out.println("Preferred CIoT network behaviour: " + d.getCiotNetworkBehaviour());

        System.out.println();
        System.out.println("ESM flag:   " + d.getLteCap().getEIT());
        System.out.println("PDN Type:  " + pdnTypeDecoder(d.getPdnType()));
        System.out.println();

        System.out.println("EMM Registered w/o PDN Connectivity: " + d.isEmmRegisteredWoPDNSupport());

        System.out.println("AS Release:  rel" + d.getAccessStratumRelease());
        System.out.println("Category-NB-r13: " + d.getCategory());
        System.out.println();
        String temp = "";;
        String temp4 = "";
        for (int j = 0 ; j < d.getLteCap().getSupportedBands().size(); j++) {
            temp = temp.concat(Integer.toString(d.getLteCap().getSupportedBands().get(j).getBandID()));
            temp4 = temp4.concat(" ").concat(temp);
            temp = " ";
        }
        System.out.println("Supported Bands:  " + temp4);
        System.out.println("\n");

        System.out.println("Multitone support: " + d.isMultiTone());
        System.out.println("MultiCarrier support: " + d.isMultiCarrier());
        System.out.println("\n");


        if (d.geteDrxValue() == Utility.ERROR_CODE) {
            System.out.println("eDRX:  " + Utility.NOT_SET);
        } else {
            System.out.println("eDRX:  " + d.geteDrxValue() + "s");
        }
        if (d.getPagingTimeWindow() == Utility.ERROR_CODE) {
            System.out.println("Paging Time Window:  " + Utility.NOT_SET);
        } else {
            System.out.println("Paging Time Window:  " + d.getPagingTimeWindow() + "s");
        }
        if (d.getT3324() == Utility.ERROR_CODE) {
            System.out.println("T3324:  " + Utility.NOT_SET);
        } else {
            System.out.println("T3324:  " + d.getT3324() + "s");
        }
        if (d.getT3412() == Utility.ERROR_CODE){
            System.out.println("T3412:  " + Utility.NOT_SET);
        } else {
            System.out.println("T3412:  " + d.getT3412() + "s");
        }

        System.out.println("\n");
        System.out.println("\nSupported Encryption Algorithms:\n");
        HashMap<String,Integer> ciphAlg = d.getLteCap().getEncrAlg();
        int counter = 0;
        for (String keyName: ciphAlg.keySet()){
            String key = keyName.toString();
            String value = ciphAlg.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                counter++;
            }
        }
        if (counter==0 ) System.out.println(Utility.NOT_APPLICABLE);
        counter = 0;

        System.out.println("\nSupported Integrity Algorithms:\n");
        HashMap<String,Integer> intgrAlg = d.getLteCap().getIntegrAlg();
        //ciphAlg.forEach((key, value) -> System.out.println(key + " : " + value));
        for (String keyName: intgrAlg.keySet()){
            String key = keyName.toString();
            String value = intgrAlg.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                counter++;
            }
        }
        if (counter==0 ) System.out.println(Utility.NOT_APPLICABLE);
    }
    public static String printNBIoTCap(NBIoT device){
        String output = "";
        output = output.concat("NBIoT Capabilities:").concat("\n\nBasic Info:");
        output = output.concat("\nAttach Type: " + Utility.attachTypeDecoder(device.getAttachType()))
                .concat("\nAdditional Update Type: " + device.getAdditionalUpdateType());
        output = output.concat("\nType of identity:  "  + typeOfIdentityDecoder(device.getIdentityType()))
                .concat("\nESM flag:   " + device.getLteCap().getEIT());
        output = output.concat("\nPDN Type:  " + pdnTypeDecoder(device.getPdnType()))
                .concat("\nAS Release:  rel" + device.getAccessStratumRelease());
        output = output.concat("\nCategory-NB-r13: " + device.getCategory())
                .concat("\n\n4G - Band Info");
        output = output.concat("\nSupported Bands:");
        String temp = "";
        for (int j = 0 ; j < device.getLteCap().getSupportedBands().size(); j++) {
            temp = temp.concat(Integer.toString(device.getLteCap().getSupportedBands().get(j).getBandID()));
            output = output.concat(" ").concat(temp);
            temp = " ";
        }
        output = output.concat("\n\nNBIoT Supported Features:")
                .concat("\nPreferred CIoT network behaviour: " + device.getCiotNetworkBehaviour());
        output = output.concat("\nMultitone support: " + device.isMultiTone())
                .concat("\nMultiCarrier support: " + device.isMultiCarrier());
        output = output.concat("\neDRX:");
        if (device.geteDrxValue() == Utility.ERROR_CODE) {
            output = output.concat(Utility.NOT_SET);
        } else {
            output = output.concat(device.geteDrxValue() + "s");
        }
        output = output.concat("\nPaging Time Window:");
        if (device.getPagingTimeWindow() == Utility.ERROR_CODE) {
            output = output.concat(Utility.NOT_SET);
        } else {
            output = output.concat(device.getPagingTimeWindow() + "s");
        }
        output = output.concat("\nT3324:");
        if (device.getT3324() == Utility.ERROR_CODE) {
            output = output.concat(Utility.NOT_SET);
        } else {
            output = output.concat(device.getT3324() + "s");
        }
        output = output.concat("\nT3412:");
        if (device.getT3412() == Utility.ERROR_CODE){
            output = output.concat(Utility.NOT_SET);
        } else {
            output = output.concat(device.getT3412() + "s");
        }
        output = output.concat("\n\n\nEPS Encryption Algorithms:");
        HashMap<String,Integer> encrAlg = device.getLteCap().getEncrAlg();
        int counter = 0;
        for (String keyName: encrAlg.keySet()){
            String key = keyName.toString();
            String value = encrAlg.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                output = output.concat("\n").concat(key);
                counter++;
            }
        }
        if (counter==0 ) output = output.concat(Utility.NOT_APPLICABLE);
        counter = 0;

        output = output.concat("\n\n\nEPS Integrity Algorithms:");
        HashMap<String,Integer> intgrAlg = device.getLteCap().getIntegrAlg();
        for (String keyName: intgrAlg.keySet()){
            String key = keyName.toString();
            String value = intgrAlg.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                output = output.concat("\n").concat(key);
                counter++;
            }
        }
        if (counter==0 ) output = output.concat(Utility.NOT_APPLICABLE);

        return output;
    }
}
