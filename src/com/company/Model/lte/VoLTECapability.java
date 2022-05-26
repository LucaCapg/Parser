package com.company.Model.lte;

import java.util.ArrayList;

/**
 * Model class to represent DUT VoLTE Capabilities
 */
public class VoLTECapability {

    private ArrayList<AlgCouple> supportedAlgorithms;
    private ArrayList<VoLTECodec> supportedCodecs;
    private String userAgent;
    private ArrayList<String> supportedSrvccs;
    private String pEarlyMediaSupport;


    public ArrayList<String> getSupportedSrvccs() {
        return supportedSrvccs;
    }

    public void setSupportedSrvccs(ArrayList<String> supportedSrvccs) {
        this.supportedSrvccs = supportedSrvccs;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public ArrayList<VoLTECodec> getSupportedCodecs() {
        return supportedCodecs;
    }

    public void setSupportedCodecs(ArrayList<VoLTECodec> supportedCodecs) {
        this.supportedCodecs = supportedCodecs;
    }

    public ArrayList<AlgCouple> getSupportedAlgorithms() {
        return supportedAlgorithms;
    }

    public void setSupportedAlgorithms(ArrayList<AlgCouple> supportedAlgorithms) {
        this.supportedAlgorithms = supportedAlgorithms;
    }
    public String getpEarlyMediaSupport() {
        return pEarlyMediaSupport;
    }

    public void setpEarlyMediaSupport(String pEarlyMediaSupport) {
        this.pEarlyMediaSupport = pEarlyMediaSupport;
    }

}
