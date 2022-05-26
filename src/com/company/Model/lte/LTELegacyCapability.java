package com.company.Model.lte;

public class LTELegacyCapability extends LTECapability {
    private VoLTECapability volteCap;

    private int[] featureGroupIndicators;
    private int[] featureGroupIndicatorsAddR9;
    private int[] featureGroupIndicatorsR10;
    private int[] featureGroupIndicatorsFDDAddR9;
    private int[] featureGroupIndicatorsTDDAddR9;
    private int[] featureGroupIndicatorsFDDR9;
    private int[] featureGroupIndicatorsTDDR9;
    private int[] featureGroupIndicatorsR10TDD;
    private int[] featureGroupIndicatorsR10FDD;
    private boolean txAntennaSelectionSupport;
    private boolean ueSpecificRefsSigsSupport;
    private boolean eRedirectionGERAN;
    private boolean eRedirectionUTRAN;
    private boolean rachReportSON;

    public LTELegacyCapability(){
        this.volteCap = new VoLTECapability();
    }
    public VoLTECapability getVolteCap() {
        return volteCap;
    }
    public void setVolteCap(VoLTECapability volteCap) {
        this.volteCap = volteCap;
    }
    public int[] getFeatureGroupIndicatorsR10TDD() {
        return featureGroupIndicatorsR10TDD;
    }

    public void setFeatureGroupIndicatorsR10TDD(int[] featureGroupIndicatorsR10TDD) {
        this.featureGroupIndicatorsR10TDD = featureGroupIndicatorsR10TDD;
    }

    public int[] getFeatureGroupIndicatorsR10FDD() {
        return featureGroupIndicatorsR10FDD;
    }

    public void setFeatureGroupIndicatorsR10FDD(int[] featureGroupIndicatorsR10FDD) {
        this.featureGroupIndicatorsR10FDD = featureGroupIndicatorsR10FDD;
    }

    public int[] getFeatureGroupIndicatorsFDDR9() {
        return featureGroupIndicatorsFDDR9;
    }

    public void setFeatureGroupIndicatorsFDDR9(int[] featureGroupIndicatorsFDDR9) {
        this.featureGroupIndicatorsFDDR9 = featureGroupIndicatorsFDDR9;
    }

    public int[] getFeatureGroupIndicatorsTDDR9() {
        return featureGroupIndicatorsTDDR9;
    }

    public void setFeatureGroupIndicatorsTDDR9(int[] featureGroupIndicatorsTDDR9) {
        this.featureGroupIndicatorsTDDR9 = featureGroupIndicatorsTDDR9;
    }

    public int[] getFeatureGroupIndicatorsFDDAddR9() {
        return featureGroupIndicatorsFDDAddR9;
    }

    public void setFeatureGroupIndicatorsFDDAddR9(int[] featureGroupIndicatorsFDDAddR9) {
        this.featureGroupIndicatorsFDDAddR9 = featureGroupIndicatorsFDDAddR9;
    }

    public int[] getFeatureGroupIndicatorsTDDAddR9() {
        return featureGroupIndicatorsTDDAddR9;
    }

    public void setFeatureGroupIndicatorsTDDAddR9(int[] featureGroupIndicatorsTDDAddR9) {
        this.featureGroupIndicatorsTDDAddR9 = featureGroupIndicatorsTDDAddR9;
    }

    public int[] getFeatureGroupIndicatorsR10() {
        return featureGroupIndicatorsR10;
    }

    public void setFeatureGroupIndicatorsR10(int[] featureGroupIndicatorsR10) {
        this.featureGroupIndicatorsR10 = featureGroupIndicatorsR10;
    }

    public int[] getFeatureGroupIndicatorsAddR9() {
        return featureGroupIndicatorsAddR9;
    }

    public void setFeatureGroupIndicatorsAddR9(int[] featureGroupIndicatorsAddR9) {
        this.featureGroupIndicatorsAddR9 = featureGroupIndicatorsAddR9;
    }

    public int[] getFeatureGroupIndicators() {
        return featureGroupIndicators;
    }

    public void setFeatureGroupIndicators(int[] featureGroupIndicators) {
        this.featureGroupIndicators = featureGroupIndicators;
    }

    public boolean isTxAntennaSelectionSupport() {
        return txAntennaSelectionSupport;
    }

    public void setTxAntennaSelectionSupport(int txAntennaSelectionSupport) {
        switch (txAntennaSelectionSupport){
            case 0: this.txAntennaSelectionSupport = false;
                break;
            case 1: this.txAntennaSelectionSupport = true;
                break;
            default: this.txAntennaSelectionSupport = false;
                break;
        }
    }

    public boolean isUeSpecificRefsSigsSupport() {
        return ueSpecificRefsSigsSupport;
    }

    public void setUeSpecificRefsSigsSupport(int ueSpecificRefsSigsSupport) {
        switch (ueSpecificRefsSigsSupport){
            case 0: this.ueSpecificRefsSigsSupport= false;
                break;
            case 1: this.ueSpecificRefsSigsSupport = true;
                break;
            default: this.ueSpecificRefsSigsSupport = true;
                break;
        }
    }

    public boolean iseRedirectionGERAN() {
        return eRedirectionGERAN;
    }

    public void seteRedirectionGERAN(boolean eRedirectionGERAN) {
        this.eRedirectionGERAN = eRedirectionGERAN;
    }

    public boolean iseRedirectionUTRAN() {
        return eRedirectionUTRAN;
    }

    public void seteRedirectionUTRAN(boolean eRedirectionUTRAN) {
        this.eRedirectionUTRAN = eRedirectionUTRAN;
    }

    public boolean isRachReportSON() {
        return rachReportSON;
    }

    public void setRachReportSON(boolean rachReportSON) {
        this.rachReportSON = rachReportSON;
    }
}
