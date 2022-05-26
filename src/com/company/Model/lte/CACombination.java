package com.company.Model.lte;

import java.util.ArrayList;

/**
 * Model class to identify a single element in the CA band combination tree
 */
public class CACombination {
    private int itemIndex;
    private ArrayList<ComponentCarrier> DLSingleCombination;
    private ArrayList<ComponentCarrier> ULSingleCombination;
    // Bandwidth Combination Set
    private int[] supportedBCS;
    private int combinationMagnitude = 0;
    private int ccQuantityDL = 0;
    private int ccQuantityUL = 0;
    private int numberOfComponentsDL;
    private int numberOfComponentsUL;
    private boolean differentFallbackSupported = false;


    /**
     * Parse and make an integer array to set bandwidth band combination supported
     * @param bitStringSupportedBandBandwidth the string retrieve from the json file
     */
    public void setSupportedBCS(String bitStringSupportedBandBandwidth) {
        this.supportedBCS = new int[32];
        bitStringSupportedBandBandwidth = bitStringSupportedBandBandwidth.replace(":","");
        String tmp = "";
        String tmp2 = "0X";
        int marker = 0;
        for (int i = 0; i < bitStringSupportedBandBandwidth.length(); i++) {
            tmp = bitStringSupportedBandBandwidth.substring(i,i+1);
            // needed to set number as hexadecimal
            tmp = tmp2.concat(tmp);
            int supp = Integer.decode(tmp);
            tmp = Integer.toBinaryString(supp);
            tmp = String.format("%4s", tmp).replace(" ","0");
            //tmp = Integer.toHexString(Integer.parseInt(bitStringSupportedBandBandwidth.substring(i,i+1)));
            for (int j = marker; j < 4; j++) {
                this.supportedBCS[marker+j]= Integer.parseInt(tmp.substring(j,j+1));
            }
            marker = marker+4;
        }
        for (int s = marker; s < 32-marker; s++) {
            this.supportedBCS[marker+s] = 0;
        }
    }
    public void setItemIndex(int itemIndex) {
        this.itemIndex = itemIndex;
    }
    public ArrayList<ComponentCarrier> getDLSingleCombination() {
        return DLSingleCombination;
    }
    public void setDLSingleCombination(ArrayList<ComponentCarrier> DLSingleCombination) {
        this.DLSingleCombination = DLSingleCombination;
    }
    public ArrayList<ComponentCarrier> getULSingleCombination() {
        return ULSingleCombination;
    }
    public void setULSingleCombination(ArrayList<ComponentCarrier> ULSingleCombination) {
        this.ULSingleCombination = ULSingleCombination;
    }
    public int getItemIndex() {
        return itemIndex;
    }
    public int[] getSupportedBCS() {
        return supportedBCS;
    }

    public int getCombinationMagnitude() {
        return combinationMagnitude;
    }


    public void setCombinationMagnitude(int combinationMagnitude) {
        this.combinationMagnitude = combinationMagnitude;
    }

    public void setNumberOfcomponentsDL(int numberOfComponentsDL){
        this.numberOfComponentsDL = numberOfComponentsDL;
    }

    public int getNumberOfcomponentsDL() {
        return numberOfComponentsDL;
    }
    public void setNumberOfcomponentsUL(int numberOfComponentsUL){
        this.numberOfComponentsUL = numberOfComponentsUL;
    }

    public int getNumberOfcomponentsUL() {
        return numberOfComponentsUL;
    }
    public int getCcQuantityDL() {
        return ccQuantityDL;
    }

    public void setCcQuantityDL(int ccQuantityDL) {
        this.ccQuantityDL = ccQuantityDL;
    }

    public int getCcQuantityUL() {
        return ccQuantityUL;
    }

    public void setCcQuantityUL(int ccQuantityUL) {
        this.ccQuantityUL = ccQuantityUL;
    }

    public void setDifferentFallbackSupported(boolean differentFallbackSupported) {
        this.differentFallbackSupported = differentFallbackSupported;
    }

    public boolean isDifferentFallbackSupported() {
        return differentFallbackSupported;
    }
}
