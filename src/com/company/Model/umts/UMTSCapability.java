package com.company.Model.umts;

import com.company.Utility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model class representing UMTS features of DUTs
 */
public class UMTSCapability {
    private ArrayList<UMTSBand> supportedBands;
    private HashMap<String,Integer> codecs;
    private HashMap<String,Integer> encrAlg;
    private HashMap<String,Integer> integrAlg;
    private boolean fddRadioAccessTechnCap;
    private boolean mcps384TDDRatCap;
    private boolean mcps128TDDRatCap;
    private boolean cdma2000Support;

    public UMTSCapability(){

        super();
        this.codecs = new HashMap<String,Integer>();
        codecs.put(Utility.TDMA_EFR,0);
        codecs.put(Utility.UMTS_AMR_2,0);
        codecs.put(Utility.UMTS_AMR,0);
        codecs.put(Utility.HR_AMR,0);
        codecs.put(Utility.FR_AMR,0);
        codecs.put(Utility.GSM_EFR,0);
        codecs.put(Utility.GSM_HR,0);
        codecs.put(Utility.GSM_FR,0);
        codecs.put(Utility.UMTS_EVS,0);
        codecs.put(Utility.OHR_AMR_WB,0);
        codecs.put(Utility.OFR_AMR_WB,0);
        codecs.put(Utility.OHR_AMR,0);
        codecs.put(Utility.UMTS_AMR_WB,0);
        codecs.put(Utility.FR_AMR_WB,0);
        codecs.put(Utility.PDC_EFR,0);

        this.encrAlg = new HashMap<String, Integer>();
        encrAlg.put(Utility.UEA0,0);
        encrAlg.put(Utility.UEA1,0);
        encrAlg.put(Utility.UEA2,0);
        encrAlg.put(Utility.UEA3,0);
        encrAlg.put(Utility.UEA4,0);
        encrAlg.put(Utility.UEA5,0);
        encrAlg.put(Utility.UEA6,0);
        encrAlg.put(Utility.UEA7,0);
        this.integrAlg = new HashMap<String,Integer>();
        integrAlg.put(Utility.UIA1,0);
        integrAlg.put(Utility.UIA2,0);
        integrAlg.put(Utility.UIA3,0);
        integrAlg.put(Utility.UIA4,0);
        integrAlg.put(Utility.UIA5,0);
        integrAlg.put(Utility.UIA6,0);
        integrAlg.put(Utility.UIA7,0);
    }

    public ArrayList<UMTSBand> getSupportedBands() {
        return supportedBands;
    }
    public void setSupportedBands(ArrayList<UMTSBand> supportedBands) {
        this.supportedBands = supportedBands;
    }
    public HashMap<String, Integer> getCodecs() {
        return codecs;
    }
    public HashMap<String, Integer> getEncrAlg() {
        return encrAlg;
    }
    public HashMap<String, Integer> getIntegrAlg() {
        return integrAlg;
    }

    public boolean isFddRadioAccessTechnCap() {
        return fddRadioAccessTechnCap;
    }

    public void setFddRadioAccessTechnCap(int fddRadioAccessTechnCap) {
        switch (fddRadioAccessTechnCap) {
            case 0: this.fddRadioAccessTechnCap = false;
                break;
            case 1: this.fddRadioAccessTechnCap = true;
                break;
            default:
                break;
        }
    }

    public boolean isMcps384TDDRatCap() {
        return mcps384TDDRatCap;
    }

    public void setMcps384TDDRatCap(int mcps384TDDRatCap) {
        switch(mcps384TDDRatCap){
            case 0: this.mcps384TDDRatCap = false;
                break;
            case 1: this.mcps384TDDRatCap = true;
                break;
            default: this.mcps384TDDRatCap = false;
                break;
        }
    }

    public boolean isCdma2000Support() {
        return cdma2000Support;
    }

    public void setCdma2000Support(int cdma2000Support) {
        switch(cdma2000Support){
            case 0: this.cdma2000Support = false;
                break;
            case 1: this.cdma2000Support = true;
                break;
            default: this.cdma2000Support = false;
                break;
        }
    }

    public boolean isMcps128TDDRatCap() {
        return mcps128TDDRatCap;
    }

    public void setMcps128TDDRatCap(int mcps128TDDRatCap) {
        switch (mcps128TDDRatCap){
            case 0: this.mcps128TDDRatCap = false;
                break;
            case 1: this.mcps128TDDRatCap = true;
                break;
            default: this.mcps128TDDRatCap = false;
                break;
        }
    }
}
