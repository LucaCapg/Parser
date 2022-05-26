package com.company.Model;

import com.company.Model.lte.LTELegacyCapability;
import com.company.Utility;

import java.util.HashMap;

/**
 * Model class representing CAT M DUTs
 */
public class eMTC extends LegacyDUT{
    private String categoryUL;
    private String categoryDL;
    private int ceModeA;
    private int ceModeB;

    public eMTC() {
        super();
    }

    public String getCategoryUL() {
        return categoryUL;
    }
    public int getCeModeB() {
        return ceModeB;
    }
    public void setCeModeB(int ceModeB) {
        this.ceModeB = ceModeB;
    }
    public void setCategoryUL(int cat) {
        switch (cat) {
            case 1: this.categoryUL = Utility.CATM_CATEGORY_M1;
                    break;
            default: this.categoryUL = Utility.MISSING_CODE;
                break;
        }
    }
    public String getCategoryDL() {
        return categoryDL;
    }
    public void setCategoryDL(int cat) {
        switch (cat) {
            case 1: this.categoryDL = Utility.CATM_CATEGORY_M1;
                break;
            default: this.categoryDL = Utility.MISSING_CODE;
                break;
        }
    }
    public int getCeModeA() {
        return ceModeA;
    }
    public void setCeModeA(int ceModeA) {
        this.ceModeA = ceModeA;
    }
    /**
     * private support method to parse code of ce Mode r13
     * @param ceCode the code of the related parameter
     * @return the description of the code as a string
     */
    private static String ceModeADecoder(int ceCode) {
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
    @Override
    public void setPagingTimeWindow(String pagingTimeWindow) {
        pagingTimeWindow = pagingTimeWindow.replace("0x","");
        if (pagingTimeWindow.equals(Utility.MISSING_CODE)) {
            this.pagingTimeWindow = Utility.ERROR_CODE;
        } else {
            int decodedValue = Integer.parseInt(pagingTimeWindow,16);
            switch (decodedValue) {
                case 0: this.pagingTimeWindow = 1.28f;
                    break;
                case 1: this.pagingTimeWindow = 2.56f;
                    break;
                case 2: this.pagingTimeWindow = 3.84f;
                    break;
                case 3: this.pagingTimeWindow = 5.12f;
                    break;
                case 4: this.pagingTimeWindow = 6.4f;
                    break;
                case 5: this.pagingTimeWindow = 7.68f;
                    break;
                case 6: this.pagingTimeWindow = 8.96f;
                    break;
                case 7: this.pagingTimeWindow = 10.24f;
                    break;
                case 8: this.pagingTimeWindow = 11.52f;
                    break;
                case 9: this.pagingTimeWindow = 12.8f;
                    break;
                case 10: this.pagingTimeWindow = 14.08f;
                    break;
                case 11: this.pagingTimeWindow = 15.36f;
                    break;
                case 12: this.pagingTimeWindow = 16.64f;
                    break;
                case 13: this.pagingTimeWindow = 17.92f;
                    break;
                case 14: this.pagingTimeWindow = 19.20f;
                    break;
                case 15: this.pagingTimeWindow = 20.48f;
                    break;
                default: this.pagingTimeWindow = Utility.ERROR_CODE;
                    break;
            }
        }
    }

    @Override
    public void setAccessStratumRelease(int accessStratumRelease) {
        super.setAccessStratumRelease(accessStratumRelease);
    }

    public static void printCatMDUTCap(eMTC d){
        System.out.println("Cat M DUT Capabilities");
        System.out.println();
        System.out.println("Attach Type:   " + Utility.attachTypeDecoder(d.getAttachType()));
        System.out.println("Additional Update Type: " + d.getAdditionalUpdateType());
        System.out.println("Type of identity:  "  + typeOfIdentityDecoder(d.getIdentityType()));
        System.out.println("Preferred CIoT network behaviour: " + d.getCiotNetworkBehaviour());
        System.out.println();
        System.out.println("ESM flag:   " + d.getLteCap().getEIT());
        System.out.println("PDN Type:  " + pdnTypeDecoder(d.getPdnType()));
        System.out.println();
        System.out.println("AS Release:  rel" + d.getAccessStratumRelease());
        //System.out.println("Category: " + d.getCategory());
        System.out.println();
        System.out.println("Category DL: " + d.getCategoryDL());
        System.out.println("Category UL: " + d.getCategoryUL());
        System.out.println();
        // Print txAntenna sel. supp.
        System.out.println("Tx Antenna Selection Support: " + d.getLteCap().isTxAntennaSelectionSupport());
        System.out.println("Ue SpecificRefSigsSupported: " + d.getLteCap().isUeSpecificRefsSigsSupport());
        System.out.println("EMM Registered w/o PDN Connectivity: " + d.isEmmRegisteredWoPDNSupport());
        // Print Supported Bands
        String temp = "";
        temp = "";
        String temp4 = "";
        for (int j = 1 ; j < d.getLteCap().getSupportedBands().size() + 1; j++) {
            temp = temp.concat(Integer.toString(d.getLteCap().getSupportedBands().get(j-1).getBandID()));
            temp4 = temp4.concat(" ").concat(temp);
            temp = " ";
        }
        System.out.println("Supported Bands:  " + temp4);

        temp = "";
        int[] temp2;
        for (int j = 1 ; j < d.getLteCap().getFeatureGroupIndicators().length + 1; j++) {
            temp2 = d.getLteCap().getFeatureGroupIndicators();
            temp = temp.concat("").concat(Integer.toString(temp2[j-1]));
            if (( j % 8 == 0)) {
                temp = temp.concat("   ");
            }
        }
        System.out.println("fgi (a.k.a fgiRel8):  " + temp);
        System.out.println("\n");
        // Print FGI R9 (Index starts from 1 for well formatting)

        int[] temp3;
        temp = "";
        if (d.getLteCap().getFeatureGroupIndicatorsAddR9() != null) {
            for (int j = 1; j < d.getLteCap().getFeatureGroupIndicatorsAddR9().length + 1; j++) {
                temp3 = d.getLteCap().getFeatureGroupIndicatorsAddR9();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
        } else {
            temp = Utility.NOT_APPLICABLE;
        }
        System.out.println();
        System.out.println("fgiRel9Add-R9:  " + temp);

        temp = "";
        // FDD
        // AddR9
        if (d.getLteCap().getFeatureGroupIndicatorsFDDAddR9() != null) {
            temp = "";
            for (int j = 1; j < d.getLteCap().getFeatureGroupIndicatorsFDDAddR9().length + 1; j++) {
                temp3 = d.getLteCap().getFeatureGroupIndicatorsFDDAddR9();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
            System.out.println("fgiRel9Addr9-r9 (FDD):  " + temp);
        } else {
            System.out.println("fgiRel9Addr9-r9 (FDD):  " + Utility.NOT_APPLICABLE);
        }
        // TDD
        //Add R9
        if (d.getLteCap().getFeatureGroupIndicatorsTDDAddR9() != null) {
            temp = "";
            for (int j = 1; j < d.getLteCap().getFeatureGroupIndicatorsTDDAddR9().length + 1; j++) {
                temp3 = d.getLteCap().getFeatureGroupIndicatorsTDDAddR9();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
            System.out.println("fgiRel99Add-r9 (TDD):  " + temp);
        } else {
            System.out.println("fgiRel99Add-r9 (TDD):  " + Utility.NOT_APPLICABLE);
        }

        System.out.println("\n");
        //R9
        if (d.getLteCap().getFeatureGroupIndicatorsFDDR9()!= null) {
            temp = "";
            for (int j = 1; j < d.getLteCap().getFeatureGroupIndicatorsFDDR9().length + 1; j++) {
                temp3 = d.getLteCap().getFeatureGroupIndicatorsFDDR9();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
            System.out.println("fgi-r9 (FDD):  " + temp);
        } else {
            System.out.println("fgi-r9 (FDD):  " + Utility.NOT_APPLICABLE);
        }


        // R9
        if (d.getLteCap().getFeatureGroupIndicatorsTDDR9() != null) {
            temp = "";
            for (int j = 1; j < d.getLteCap().getFeatureGroupIndicatorsTDDR9().length + 1; j++) {
                temp3 = d.getLteCap().getFeatureGroupIndicatorsTDDR9();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
            System.out.println("fgi-r9 (TDD):  " + temp);
        } else {
            System.out.println("fgi-r9 (TDD):  " + Utility.NOT_APPLICABLE);
        }


        System.out.println("\n");
        temp = "";
        temp3 = null;
        if (d.getLteCap().getFeatureGroupIndicatorsR10() == null) {
            System.out.println("fgiRel10-r10:  " + Utility.NOT_APPLICABLE);
        } else {
            temp = "";
            for (int j = 1; j < d.getLteCap().getFeatureGroupIndicatorsR10().length + 1; j++) {
                temp3 = d.getLteCap().getFeatureGroupIndicatorsR10();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }

            System.out.println("FGI-Rel10:  " + temp);
        }
        // FGI rel10 FDD TDD
        temp = "";
        temp3= null;
        if (d.getLteCap().getFeatureGroupIndicatorsR10FDD() == null) {
            System.out.println("fgiRel10-r10 (FDD):  " + Utility.NOT_APPLICABLE);
        } else {
            temp = "";
            for (int j = 1; j < d.getLteCap().getFeatureGroupIndicatorsR10FDD().length + 1; j++) {
                temp3 = d.getLteCap().getFeatureGroupIndicatorsR10FDD();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
            System.out.println("fgiRel10-r10 (FDD):  " + temp);
        }

        //TDD
        if (d.getLteCap().getFeatureGroupIndicatorsR10TDD() == null) {
            System.out.println("fgiRel10-r10 (TDD):  " + Utility.NOT_APPLICABLE);
        } else {
            temp = "";
            for (int j = 1; j < d.getLteCap().getFeatureGroupIndicatorsR10TDD().length + 1; j++) {
                temp3 = d.getLteCap().getFeatureGroupIndicatorsR10TDD();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
            System.out.println("fgiRel10-r10 (TDD):  " + temp);
        }

        System.out.println("\n");
        System.out.println("ceModeA-r13:  " + ceModeADecoder(d.getCeModeA()));
        System.out.println("ceModeB-r13:  " + ceModeADecoder(d.getCeModeB()));
        if (d.geteDrxValue() == Utility.ERROR_CODE) {
            System.out.println("eDRX:  " + Utility.NOT_SET);
        } else {
            System.out.println("eDRX:  " + d.geteDrxValue()+"s");
        }
        if (d.getPagingTimeWindow() == Utility.ERROR_CODE) {
            System.out.println("Paging Time Window:  " + Utility.NOT_SET);
        } else {
            System.out.println("Paging Time Window:  " + d.getPagingTimeWindow()+"s");
        }
        if (d.getT3324() == Utility.ERROR_CODE) {
            System.out.println("T3324:  " + Utility.NOT_SET);
        } else {
            System.out.println("T3324:  " + d.getT3324()+"s");
        }
        if (d.getT3412() == Utility.ERROR_CODE){
            System.out.println("T3412:  " + Utility.NOT_SET);
        } else {
            System.out.println("T3412:  " + d.getT3412()+"s");
        }

        System.out.println();
        System.out.println("\nSupported Ciphering Algorithms:\n");
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

        System.out.println();
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

    public static String printeMTCCap(eMTC dut){
        String output = "";
        output = output.concat("eMTC Capabilities").concat("\n\nBasic Info:");
        output = output.concat("\nAttach Type:   " + Utility.attachTypeDecoder(dut.getAttachType()))
                .concat("\nAdditional Update Type: " + dut.getAdditionalUpdateType());
        output = output.concat("\nType of identity:  "  + typeOfIdentityDecoder(dut.getIdentityType()))
                .concat("\nESM flag:   " + dut.getLteCap().getEIT());
        output = output.concat("\nPDN Type:  " + pdnTypeDecoder(dut.getPdnType()))
                .concat("\nAS Release:  rel" + dut.getAccessStratumRelease());
        output = output.concat("\nCategory DL: " + dut.getCategoryDL())
                .concat("\nCategory UL: " + dut.getCategoryUL());
        output = output.concat("\n\nFGIs:").concat("\nfgi (a.k.a fgiRel8):\t");
        String temp = "";
        int[] temp22;
        if (dut.getLteCap().getFeatureGroupIndicators() != null) {
            for (int j = 1 ; j < dut.getLteCap().getFeatureGroupIndicators().length + 1; j++) {
                temp22 = dut.getLteCap().getFeatureGroupIndicators();
                output = output.concat("").concat(Integer.toString(temp22[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }

        output = output.concat("\nfgiRel9Add-R9:\t");
        int[] temp3;
        if (dut.getLteCap().getFeatureGroupIndicatorsAddR9() != null) {
            for (int j = 1; j < dut.getLteCap().getFeatureGroupIndicatorsAddR9().length + 1; j++) {
                temp3 = dut.getLteCap().getFeatureGroupIndicatorsAddR9();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }

        // FDD
        // AddR9
        output = output.concat("\nfgiRel9Addr9-r9 (FDD):\t");
        if (dut.getLteCap().getFeatureGroupIndicatorsFDDAddR9() != null) {
            for (int j = 1; j < dut.getLteCap().getFeatureGroupIndicatorsFDDAddR9().length + 1; j++) {
                temp3 = dut.getLteCap().getFeatureGroupIndicatorsFDDAddR9();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }

        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }
        // TDD
        //Add R9
        output = output.concat("\nfgiRel99Add-r9 (TDD):\t");
        if (dut.getLteCap().getFeatureGroupIndicatorsTDDAddR9() != null) {
            for (int j = 1; j < dut.getLteCap().getFeatureGroupIndicatorsTDDAddR9().length + 1; j++) {
                temp3 = dut.getLteCap().getFeatureGroupIndicatorsTDDAddR9();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }

        //R9
        output = output.concat("\nfgi-r9 (FDD):\t");
        if (dut.getLteCap().getFeatureGroupIndicatorsFDDR9()!= null) {
            for (int j = 1; j < dut.getLteCap().getFeatureGroupIndicatorsFDDR9().length + 1; j++) {
                temp3 = dut.getLteCap().getFeatureGroupIndicatorsFDDR9();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }


        // R9
        output = output.concat("\nfgi-r9 (TDD):\t");
        if (dut.getLteCap().getFeatureGroupIndicatorsTDDR9() != null) {
            for (int j = 1; j < dut.getLteCap().getFeatureGroupIndicatorsTDDR9().length + 1; j++) {
                temp3 = dut.getLteCap().getFeatureGroupIndicatorsTDDR9();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }

        output = output.concat("\nfgiRel10-r10:\t");
        if (dut.getLteCap().getFeatureGroupIndicatorsR10() == null) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            for (int j = 1; j < dut.getLteCap().getFeatureGroupIndicatorsR10().length + 1; j++) {
                temp3 = dut.getLteCap().getFeatureGroupIndicatorsR10();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        }

        // FGI rel10 FDD TDD
        output = output.concat("\nfgiRel10-r10 (FDD):\t");
        if (dut.getLteCap().getFeatureGroupIndicatorsR10FDD() == null) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            for (int j = 1; j < dut.getLteCap().getFeatureGroupIndicatorsR10FDD().length + 1; j++) {
                temp3 = dut.getLteCap().getFeatureGroupIndicatorsR10FDD();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        }

        //TDD
        output = output.concat("\nfgiRel10-r10 (TDD):\t");
        if (dut.getLteCap().getFeatureGroupIndicatorsR10TDD() == null) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            for (int j = 1; j < dut.getLteCap().getFeatureGroupIndicatorsR10TDD().length + 1; j++) {
                temp3 = dut.getLteCap().getFeatureGroupIndicatorsR10TDD();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        }
        output = output.concat("\n\nCatM Features:")
                .concat("\nPreferred CIoT network behaviour: " + dut.getCiotNetworkBehaviour());
        output = output.concat("\nceModeA-r13:  " + ceModeADecoder(dut.getCeModeA()))
                .concat("\nceModeB-r13:  " + ceModeADecoder(dut.getCeModeB()));
        output = output.concat("\neDRX:");
        if (dut.geteDrxValue() == Utility.ERROR_CODE) {
            output = output.concat(Utility.NOT_SET);
        } else {
            output = output.concat(dut.geteDrxValue()+"s");
        }
        output = output.concat("\nPaging Time Window:");
        if (dut.getPagingTimeWindow() == Utility.ERROR_CODE) {
            output = output.concat(Utility.NOT_SET);
        } else {
            output = output.concat(dut.getPagingTimeWindow()+"s");
        }
        output = output.concat("\nT3324:");
        if (dut.getT3324() == Utility.ERROR_CODE) {
            output = output.concat(Utility.NOT_SET);
        } else {
            output = output.concat(dut.getT3324()+"s");
        }
        output = output.concat("\nT3412:");
        if (dut.getT3412() == Utility.ERROR_CODE){
            output = output.concat(Utility.NOT_SET);
        } else {
            output = output.concat(dut.getT3412()+"s");
        }
        return output;
    }
}
