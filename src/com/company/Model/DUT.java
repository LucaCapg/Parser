package com.company.Model;

import com.company.Model.gsm.GSMCapability;
import com.company.Model.lte.LTECapability;
import com.company.Model.lte.LTELegacyCapability;
import com.company.Model.umts.UMTSCapability;
import com.company.Utility;

import java.util.HashMap;

/**
 * Model class: Device Under Testing
 */
public class DUT {

    private boolean gsmSupport = true;
    private boolean umtsSupport = true;
    private String modelName;
    // TODO REFACTOR VOICE DOMAIN PREFERENCE?
    private int voiceDomainPreference;

    private int attachType;
    private String additionalUpdateType;
    private String ueUsageSettings;
    private int pdnType;
    protected int accessStratumRelease;
    private String ciotNetworkBehaviour;

    private float eDrxValue;
    private int t3412;
    private int t3324;
    protected float pagingTimeWindow;

    private String imsi;
    private String ipAddress;
    private String msisdn;
    private String imeiSV;
    private int identityType;
    private boolean emmRegisteredWoPDNSupport = false;

    //5G
    private boolean support5G;
    protected int accessStratumReleaseNR = -1;
    private boolean rateMatchingResrcSetSemiStatic = false;
    private boolean additionalDMRSDLAlt = false;
    private boolean rateMatchingCtrlResrcSetDynamic = false;
    private boolean rateMatchingResrcSetDynamic = false;
    private boolean pdcchMonitoringSingleOccasion = false;

    public DUT() {
        super();
    }

    public boolean isGsmSupport() {
        return gsmSupport;
    }

    public void setGsmSupport(boolean gsmSupport) {
        this.gsmSupport = gsmSupport;
    }

    public boolean isUmtsSupport() {
        return umtsSupport;
    }

    public void setUmtsSupport(boolean umtsSupport) {
        this.umtsSupport = umtsSupport;
    }

    public boolean is5Gsupport() {
        return support5G;
    }

    public void setSupport5G(boolean support5G){
        this.support5G = support5G;
    }
    public String getImeiSV() {
        return imeiSV;
    }

    public void setImeiSV(String imeiSV) {
        this.imeiSV = imeiSV;
    }

    public String getAdditionalUpdateType() {
        return additionalUpdateType;
    }

    public String getUeUsageSettings() {
        return ueUsageSettings;
    }

    public void setUeUsageSettings(int ueSet) {
        switch (ueSet) {
            case 0: this.ueUsageSettings = Utility.VOICE_CENTRIC;
                break;
            case 1: this.ueUsageSettings = Utility.DATA_CENTRIC;
                break;
            default: this.ueUsageSettings = Utility.NOT_APPLICABLE;
                break;
        }
    }

    public void setAdditionalUpdateType(int additionalUpdateType) {
        switch (additionalUpdateType) {
            case 0: this.additionalUpdateType = "Missing code 0";
                break;
            case 1: this.additionalUpdateType = Utility.SMS_ONLY;
                break;
            case 4: this.additionalUpdateType = Utility.NOT_APPLICABLE;
                break;
            default: this.additionalUpdateType = Utility.MISSING_CODE;
                break;
        }
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getImsi() {
        return imsi;
    }

    public void setImsi(String imsi) {
        this.imsi = imsi;
    }

    public String getMsisdn() {
        return msisdn;
    }

    public void setMsisdn(String msisdn) {
        this.msisdn = msisdn;
    }

    public int getAttachType() {
        return attachType;
    }

    public void setAttachType(int attachType) {
        this.attachType = attachType;
    }


    public int getVoiceDomainPreference() {
        return voiceDomainPreference;
    }

    public void setVoiceDomainPreference(int voiceDomainPreference) {
        this.voiceDomainPreference = voiceDomainPreference;
    }
    public String getModelName() {
        return modelName;
    }

    public void setModelName(String modelName) {
        this.modelName = modelName;
    }

    public int getIdentityType() {
        return identityType;
    }

    public void setIdentityType(int identityType) {
        this.identityType = identityType;
    }
    public int getPdnType() {
        return pdnType;
    }

    public void setPdnType(int pdnType) {
        this.pdnType = pdnType;
    }

    /**
     * method to print imeiSV of the device, if present
     * @param device the device under testing
     */
    public static void printIMEISV (DUT device) {
        if (!device.getImeiSV().equals(Utility.NOT_SET)) {
            System.out.println("\n");
            System.out.println("IMEI SV: " + device.getImeiSV());
        }
    }

    /**
     * support method to parse type of identity
     * @param idType the code of the identity retrieve in the attach request message
     * @return the decoded string
     */
    protected static String typeOfIdentityDecoder (int idType) {
        String t = "";
        switch(idType) {
            case 1: t = Utility.IMSI;
                break;
            case 6: t = Utility.GUTI;
                break;
            default: t = Utility.MISSING_CODE;
                break;
        }
        return t;
    }

    /**
     * support method to decode pdn type in a human readable form
     * @param pdnType the integer representing the PDN type
     * @return The string describing the PDN type
     */
    protected static String pdnTypeDecoder(int pdnType) {
        String t = "";
        switch (pdnType) {
            case 1: t =Utility.IPV4_ADDRESSING;
                break;
            case 2: t = Utility.IPV6_ADDRESSING;
                break;
            case 3: t = Utility.DUAL_STACK_IPV4V6;
                break;
            default: t = Utility.MISSING_CODE;
                break;
        }
        return t;
    }

    public int getT3412() {
        return t3412;
    }

    /**
     * Set and parse t3412 timer value
     * @param t3412 the value of the timer
     * @param multiplier unit to multiply the timer value
     */
    public void setT3412(int t3412, int multiplier) {
        if ((t3412 == Utility.ERROR_CODE) || (multiplier==Utility.ERROR_CODE)) {
            this.t3412 = Utility.ERROR_CODE;
        } else {
            // all is brought in seconds
            switch(multiplier) {
                case 0: this.t3412 = t3412*600;
                    break;
                case 1: this.t3412 = t3412*60*60;
                    break;
                // decihours: 6 minutes
                case 2: this.t3412 = t3412*10*60*60;
                    break;
                case 3: this.t3412 =t3412*2;
                    break;
                case 4: this.t3412 = t3412*30;
                    break;
                case 5: this.t3412 = t3412*60;
                    break;
                case 6: this.t3412 = t3412*320*60*60;
                    break;
                case 7: this.t3412 = Utility.DEACTIVATED;
                    break;
                default: this.t3412 = Utility.ERROR_CODE;
                    break;
            }
        }
    }

    public int getT3324() {
        return t3324;
    }

    /**
     * Set and parse t3324 timer
     * @param t3324 the value of the timer
     * @param multiplier the unit portion of the timer (multiplies the timer by a specific value)
     */
    public void setT3324(int t3324, int multiplier) {
        if ((t3324 == Utility.ERROR_CODE) || (multiplier==Utility.ERROR_CODE)) {
            this.t3324 = Utility.ERROR_CODE;
        } else {
            // all is brought in seconds
            switch(multiplier) {
                case 0: this.t3324 = t3324*2;
                    break;
                case 1: this.t3324 = t3324*60;
                    break;
                // decihours: 6 minutes
                case 2: this.t3324 = t3324*360;
                    break;
                case 7: this.t3324 = Utility.DEACTIVATED;
                    break;
                default: this.t3324 = Utility.ERROR_CODE;
                    break;
            }
        }
    }
    public float getPagingTimeWindow() {
        return pagingTimeWindow;
    }

    public float geteDrxValue() {
        return eDrxValue;
    }

    /**
     * Set and parse eDRX values
     * @param eDrxValue the hexdecimal string extracted from the pcap
     */
    public void seteDrxValue(String eDrxValue) {
        eDrxValue = eDrxValue.replace("0x","");

        if (eDrxValue.equals(Utility.MISSING_CODE)) {
            this.eDrxValue = Utility.ERROR_CODE;
        } else {
            int decodedValue = Integer.parseInt(eDrxValue,16);
            switch (decodedValue) {
                case 0: this.eDrxValue = 5.12f;
                    break;
                case 1: this.eDrxValue = 10.24f;
                    break;
                case 2: this.eDrxValue = 20.48f;
                    break;
                case 3: this.eDrxValue = 40.96f;
                    break;
                case 4: this.eDrxValue = 61.44f;
                    break;
                case 5: this.eDrxValue = 81.92f;
                    break;
                case 6: this.eDrxValue = 102.4f;
                    break;
                case 7: this.eDrxValue = 122.88f;
                    break;
                case 8: this.eDrxValue = 143.36f;
                    break;
                case 9: this.eDrxValue = 163.84f;
                    break;
                case 10: this.eDrxValue = 327.68f;
                    break;
                case 11: this.eDrxValue = 655.36f;
                    break;
                case 12: this.eDrxValue = 1310.72f;
                    break;
                case 13: this.eDrxValue = 2621.44f;
                    break;
                case 14: this.eDrxValue = 5242.88f;
                    break;
                case 15: this.eDrxValue = 10485.76f;
                    break;
                default: this.eDrxValue = Utility.ERROR_CODE;
                    break;
            }
        }
    }

    public void setPagingTimeWindow(String pagingTimeWindow) {
        this.pagingTimeWindow = Float.parseFloat(pagingTimeWindow);
    }
    public int getAccessStratumRelease() {
        return accessStratumRelease;
    }

    public void setAccessStratumRelease(int accessStratumRelease) {
        this.accessStratumRelease = accessStratumRelease;
    }

    public int getAccessStratumReleaseNR() {
		return accessStratumReleaseNR;
	}

	public void setAccessStratumReleaseNR(int accessStratumReleaseNR) {
		this.accessStratumReleaseNR = Utility.accessStratumReleaseDecoder5G(accessStratumReleaseNR);
	}

	public void setAdditionalUpdateType(String additionalUpdateType) {
        this.additionalUpdateType = additionalUpdateType;
    }

    public void setUeUsageSettings(String ueUsageSettings) {
        this.ueUsageSettings = ueUsageSettings;
    }

    public void seteDrxValue(float eDrxValue) {
        this.eDrxValue = eDrxValue;
    }

    public void setT3412(int t3412) {
        this.t3412 = t3412;
    }

    public void setT3324(int t3324) {
        this.t3324 = t3324;
    }

    public void setPagingTimeWindow(float pagingTimeWindow) {
        this.pagingTimeWindow = pagingTimeWindow;
    }
    public String getCiotNetworkBehaviour() {
        return ciotNetworkBehaviour;
    }

    public void setCiotNetworkBehaviour(int ciotNetworkBehaviour) {
        switch (ciotNetworkBehaviour) {
            case 0: this.ciotNetworkBehaviour = Utility.NOT_SUPPORTED;
                break;
            case 1: this.ciotNetworkBehaviour = Utility.SUPPORTED;
                break;
            default: this.ciotNetworkBehaviour = Utility.MISSING_CODE;
                break;
        }
    }

    public boolean isEmmRegisteredWoPDNSupport() {
        return emmRegisteredWoPDNSupport;
    }

    public void setEmmRegisteredWoPDNSupport(boolean emmRegisteredWoPDNSupport) {
        this.emmRegisteredWoPDNSupport = emmRegisteredWoPDNSupport;
    }

    public boolean isRateMatchingResrcSetSemiStatic() {
        return rateMatchingResrcSetSemiStatic;
    }

    public void setRateMatchingResrcSetSemiStatic(boolean rateMatchingResrcSetSemiStatic) {
        this.rateMatchingResrcSetSemiStatic = rateMatchingResrcSetSemiStatic;
    }

    public boolean isAdditionalDMRSDLAlt() {
        return additionalDMRSDLAlt;
    }

    public void setAdditionalDMRSDLAlt(boolean additionalDMRSDLAlt){
        this.additionalDMRSDLAlt = additionalDMRSDLAlt;
    }

    public boolean isRateMatchingCtrlResrcSetDynamic() {
        return rateMatchingCtrlResrcSetDynamic;
    }

    public void setRateMatchingCtrlResrcSetDynamic(boolean rateMatchingCtrlResrcSetDynamic) {
        this.rateMatchingCtrlResrcSetDynamic = rateMatchingCtrlResrcSetDynamic;
    }

    public boolean isRateMatchingResrcSetDynamic() {
        return rateMatchingResrcSetDynamic;
    }

    public void setRateMatchingResrcSetDynamic(boolean rateMatchingResrcSetDynamic) {
        this.rateMatchingResrcSetDynamic = rateMatchingResrcSetDynamic;
    }

    public boolean isPdcchMonitoringSingleOccasion() {
        return pdcchMonitoringSingleOccasion;
    }

    public void setPdcchMonitoringSingleOccasion(boolean pdcchMonitoringSingleOccasion) {
        this.pdcchMonitoringSingleOccasion = pdcchMonitoringSingleOccasion;
    }
}