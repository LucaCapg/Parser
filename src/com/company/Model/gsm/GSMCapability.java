package com.company.Model.gsm;

import com.company.Utility;

import java.util.ArrayList;
import java.util.HashMap;

public class GSMCapability {
    protected HashMap<String,Integer> encrAlg;
    private ArrayList<GSMBand> supportedBands = new ArrayList<GSMBand>();
    protected HashMap<String,Integer> codecs;
    private String ucs2Support;
    private String ssScreeningIndicator;
    private int pfcFeatureSupport;
    private boolean lcsSupport;
    private boolean interRatPSHOtoUTRANIu;
    private boolean interRatPSHOtoUTRANS1;
    private boolean emmProceduresSupport;
    private boolean isrSupport;
    private boolean epcSupport;
    private boolean nfcSupport;
    private boolean esIndSupport;
    private boolean psCapability;
    private int gmskMultislotPowerProfile;
    private int psk8MultislotPowerProfile;
    private String advancedReceiverPerformance;
    private boolean dtmSupport;
    private boolean priorityBasedReselectionSupport;
    private int gprsMultiSlotClass;
    private int egprsMultiSlotClass;
    private boolean singleSlotSupport = false;
    private String psk8RFPowerCapability1;
    private String psk8RFPowerCapability2;
    private int rfPowerCapability1;
    private int rfPowerCapability2;


    public GSMCapability() {
        this.encrAlg = new HashMap<String,Integer>();
        encrAlg.put(Utility.GEA1,0);
        encrAlg.put(Utility.GEA2,0);
        encrAlg.put(Utility.GEA3,0);
        encrAlg.put(Utility.GEA4,0);
        encrAlg.put(Utility.GEA5,0);
        encrAlg.put(Utility.GEA6,0);
        encrAlg.put(Utility.GEA7,0);
        encrAlg.put(Utility.A51,0);
        encrAlg.put(Utility.A52,0);
        encrAlg.put(Utility.A53,0);
        encrAlg.put(Utility.A54,0);
        encrAlg.put(Utility.A55,0);
        encrAlg.put(Utility.A56,0);
        encrAlg.put(Utility.A57,0);
        // Initialize hashmap containing supported codecs in 2G/3G
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
    }

    public HashMap<String, Integer> getEncrAlg() {
        return encrAlg;
    }

    public void setEncrAlg(HashMap<String, Integer> encrAlg) {
        this.encrAlg = encrAlg;
    }

    public ArrayList<GSMBand> getSupportedBands() {
        return supportedBands;
    }

    public void setSupportedBands(ArrayList<GSMBand> supportedBands) {
        this.supportedBands = supportedBands;
    }

    public HashMap<String, Integer> getCodecs() {
        return codecs;
    }

    public String getUcs2Support() {
        return ucs2Support;
    }

    public void setUcs2Support(int ucs2Support) {
        switch(ucs2Support){
            case 0: this.ucs2Support = Utility.DEFAULT_ALPHABET;
                break;
            case 1: this.ucs2Support = Utility.NO_PREFERENCE;
                break;
            default: this.ucs2Support = Utility.NOT_APPLICABLE;
                break;
        }
    }

    public String getSsScreeningIndicator() {
        return ssScreeningIndicator;
    }

    public void setSsScreeningIndicator(String ssScreeningIndicator) {
        int ssScreenInd = Integer.parseInt(ssScreeningIndicator.replace("0x",""),16);
        switch (ssScreenInd){
            case 0: this.ssScreeningIndicator = Utility.DEFAULT_VALUE_OF_PHASE1;
                break;
            case 1: this.ssScreeningIndicator = Utility.ELLIPSIS_NOTATION_CAPABILITY;
                break;
            case 2: this.ssScreeningIndicator = Utility.MISSING_CODE;
                break;
            case 3: this.ssScreeningIndicator = Utility.MISSING_CODE;
                break;
            default: this.ssScreeningIndicator = Utility.MISSING_CODE;
                break;
        }
    }

    public int isPfcFeatureSupport() {
        return pfcFeatureSupport;
    }

    public void setPfcFeatureSupport(int pfcFeatureSupport) {
        this.pfcFeatureSupport = pfcFeatureSupport;
    }

    public boolean isLcsSupport() {
        return lcsSupport;
    }

    public void setLcsSupport(int lcsSupport) {
        switch (lcsSupport){
            case 0: this.lcsSupport = false;
                break;
            case 1: this.lcsSupport = true;
                break;
            default: this.lcsSupport = false;
                break;
        }
    }

    public boolean isInterRatPSHOtoUTRANIu() {
        return interRatPSHOtoUTRANIu;
    }

    public void setInterRatPSHOtoUTRANIu(int interRatPSHOtoUTRANIu) {
        switch (interRatPSHOtoUTRANIu){
            case 0: this.interRatPSHOtoUTRANIu = false;
                break;
            case 1: this.interRatPSHOtoUTRANIu = true;
                break;
            default: this.interRatPSHOtoUTRANIu = false;
                break;
        }
    }

    public boolean isInterRatPSHOtoUTRANS1() {
        return interRatPSHOtoUTRANS1;
    }

    public void setInterRatPSHOtoUTRANS1(int interRatPSHOtoUTRANS1) {
        switch (interRatPSHOtoUTRANS1){
            case 0: this.interRatPSHOtoUTRANS1 = false;
                break;
            case 1: this.interRatPSHOtoUTRANS1 = true;
                break;
            default: this.interRatPSHOtoUTRANS1 = false;
                break;
        }
    }

    public boolean isEmmProceduresSupport() {
        return emmProceduresSupport;
    }

    public void setEmmProceduresSupport(int emmProceduresSupport) {
        switch (emmProceduresSupport){
            case 0: this.emmProceduresSupport = false;
                break;
            case 1: this.emmProceduresSupport = true;
                break;
            default: this.emmProceduresSupport = false;
                break;
        }
    }

    public boolean isIsrSupport() {
        return isrSupport;
    }

    public void setIsrSupport(int isrSupport) {
        switch (isrSupport){
            case 0: this.isrSupport = false;
                break;
            case 1: this.isrSupport = true;
                break;
            default: this.isrSupport = false;
                break;
        }
    }

    public boolean isEpcSupport() {
        return epcSupport;
    }

    public void setEpcSupport(int epcSupport) {
        switch(epcSupport){
            case 0:  this.epcSupport = false;
                break;
            case 1: this.epcSupport = true;
                break;
            default: this.epcSupport = false;
                break;
        }
    }

    public boolean isNfcSupport() {
        return nfcSupport;
    }

    public void setNfcSupport(int nfcSupport) {
        switch (nfcSupport){
            case 0: this.nfcSupport = false;
                break;
            case 1: this.nfcSupport = true;
                break;
            default: this.nfcSupport = false;
                break;
        }
    }

    public boolean isEsIndSupport() {
        return esIndSupport;
    }

    public void setEsIndSupport(int esIndSupport) {
        switch (esIndSupport){
            case 0: this.esIndSupport = false;
                break;
            case 1: this.esIndSupport = true;
                break;
            default: this.esIndSupport = false;
                break;
        }
    }

    public boolean isPsCapability() {
        return psCapability;
    }

    public void setPsCapability(int psCapability) {
        switch (psCapability){
            case 0: this.psCapability = false;
                break;
            case 1: this.psCapability = true;
                break;
            default: this.psCapability = false;
                break;
        }
    }

    public int getGmskMultislotPowerProfile() {
        return gmskMultislotPowerProfile;
    }

    public void setGmskMultislotPowerProfile(int gmskMultislotPowerProfile) {
        this.gmskMultislotPowerProfile = gmskMultislotPowerProfile;
    }

    public int getPsk8MultislotPowerProfile() {
        return psk8MultislotPowerProfile;
    }

    public void setPsk8MultislotPowerProfile(int psk8MultislotPowerProfile) {
        this.psk8MultislotPowerProfile = psk8MultislotPowerProfile;
    }

    public String getAdvancedReceiverPerformance() {
        return advancedReceiverPerformance;
    }

    public void setAdvancedReceiverPerformance(int advancedReceiverPerformance) {
        switch(advancedReceiverPerformance){
            case 0: this.advancedReceiverPerformance = Utility.NOT_SUPPORTED;
                break;
            case 1: this.advancedReceiverPerformance = Utility.DOWNLINK_ADV_RX_SUPP_1;
                break;
            case 2: this.advancedReceiverPerformance = Utility.DOWNLINK_ADV_RX_SUPP_2;
                break;
            case 3: this.advancedReceiverPerformance = Utility.SHALL_NOT_USE;
                break;
            default: this.advancedReceiverPerformance = Utility.MISSING_CODE;
                break;
        }
    }

    public boolean isDtmSupport() {
        return dtmSupport;
    }

    public void setDtmSupport(int dtmSupport) {
        switch(dtmSupport){
            case 0: this.dtmSupport = false;
                break;
            case 1: this.dtmSupport = true;
                break;
            default: this.dtmSupport = false;
                break;
        }
    }

    public boolean isPriorityBasedReselectionSupport() {
        return priorityBasedReselectionSupport;
    }

    public void setPriorityBasedReselectionSupport(int priorityBasedReselectionSupport) {
        switch (priorityBasedReselectionSupport) {
            case 0: this.priorityBasedReselectionSupport = false;
                break;
            case 1: this.priorityBasedReselectionSupport = true;
                break;
            default: this.priorityBasedReselectionSupport = false;
                break;
        }
    }

    public int getGprsMultiSlotClass() {
        return gprsMultiSlotClass;
    }

    public void setGprsMultiSlotClass(int gprsMultiSlotClass) {
        switch (gprsMultiSlotClass){
            case 0: this.gprsMultiSlotClass = Utility.NOT_ASSIGNED;
                break;
            case 1: this.gprsMultiSlotClass = 5;
                break;
            case 2: this.gprsMultiSlotClass = 9;
                break;
            case 3: this.gprsMultiSlotClass = 11;
                break;
            default: this.gprsMultiSlotClass = Utility.ERROR_CODE;
                break;
        }
    }

    public int getEgprsMultiSlotClass() {
        return egprsMultiSlotClass;
    }

    public void setEgprsMultiSlotCLass(int egprsMultiSlotClass) {
        switch (egprsMultiSlotClass){
            case 0: this.egprsMultiSlotClass = Utility.NOT_ASSIGNED;
                break;
            case 1: this.egprsMultiSlotClass = 5;
                break;
            case 2: this.egprsMultiSlotClass = 9;
                break;
            case 3: this.egprsMultiSlotClass = 11;
                break;
            default: this.egprsMultiSlotClass = Utility.ERROR_CODE;
                break;
        }
    }

    public boolean isSingleSlotSupport() {
        return singleSlotSupport;
    }

    public void setSingleSlotSupport(int singleSlotSupport) {
        switch (singleSlotSupport){
            case 0: this.singleSlotSupport = false;
                break;
            case 1: this.singleSlotSupport = true;
                break;
            default: this.singleSlotSupport = false;
                break;
        }
    }

    public String getPsk8RFPowerCapability1() {
        return psk8RFPowerCapability1;
    }

    public void setPsk8RFPowerCapability1(int psk8RFPowerCapability1) {
        switch (psk8RFPowerCapability1){
            case 0: this.psk8RFPowerCapability1 = Utility.RESERVED;
                break;
            case 1: this.psk8RFPowerCapability1 = Utility.PSK8_PC_E1;
                break;
            case 2: this.psk8RFPowerCapability1 = Utility.PSK8_PC_E2;
                break;
            case 3: this.psk8RFPowerCapability1 = Utility.PSK8_PC_E3;
                break;
            default: this.psk8RFPowerCapability1 = Utility.NOT_APPLICABLE;
                break;
        }
    }

    public String getPsk8RFPowerCapability2() {
        return psk8RFPowerCapability2;
    }

    public void setPsk8RFPowerCapability2(int psk8RFPowerCapability2) {
        switch (psk8RFPowerCapability2){
            case 0: this.psk8RFPowerCapability2 = Utility.RESERVED;
                break;
            case 1: this.psk8RFPowerCapability2 = Utility.PSK8_PC_E1;
                break;
            case 2: this.psk8RFPowerCapability2 = Utility.PSK8_PC_E2;
                break;
            case 3: this.psk8RFPowerCapability2 = Utility.PSK8_PC_E3;
                break;
            default: this.psk8RFPowerCapability2 = Utility.NOT_APPLICABLE;
                break;
        }
    }

    public int getRfPowerCapability1() {
        return rfPowerCapability1;
    }

    public void setRfPowerCapability1(int rfPowerCapability1) {
        this.rfPowerCapability1 = rfPowerCapability1 + 1;
    }

    public int getRfPowerCapability2() {
        return rfPowerCapability2;
    }

    public void setRfPowerCapability2(int rfPowerCapability2) {
        this.rfPowerCapability2 = rfPowerCapability2 + 1;
    }
}
