package com.company.Model.umts;

import com.company.Model.Band;
import com.company.Utility;

/**
 * Model class representing UMTS bands
 */
public class UMTSBand extends Band {
    private String umtsBandName;

    public UMTSBand() {
    }
    public UMTSBand(int bandID, int itemID) {
        super(bandID, itemID);
        this.umtsBandName = bandNamesSetter(bandID);
    }

    public String getUmtsBandName() {
        return umtsBandName;
    }
    public void setUmtsBandName(String umtsBandName) {
        this.umtsBandName = umtsBandName;
    }
    /**
     * Private support method to display umts supported bands as string
     * @param bandId the integer rapresenting the id of the current band
     * @return the string containing the decoded bands
     */
    private String bandNamesSetter(int bandId) {
        String decodedBandName = "";
        switch (bandId) {
            case 1: decodedBandName = Utility.UMTS_BAND_0;
                break;
            case 2: decodedBandName = Utility.UMTS_BAND_1;
                break;
            case 3: decodedBandName = Utility.UMTS_BAND_2;
                break;
            case 4: decodedBandName = Utility.UMTS_BAND_3;
                break;
            case 5: decodedBandName = Utility.UMTS_BAND_4;
                break;
            case 6: decodedBandName = Utility.UMTS_BAND_5;
                break;
            case 7: decodedBandName = Utility.UMTS_BAND_6;
                break;
            case 8: decodedBandName = Utility.UMTS_BAND_7;
                break;
            case 9: decodedBandName = Utility.UMTS_BAND_8;
                break;
            case 10: decodedBandName = Utility.UMTS_BAND_9;
                break;
            case 11: decodedBandName = Utility.UMTS_BAND_10;
                break;
            case 12: decodedBandName = Utility.UMTS_BAND_11;
                break;
            case 13: decodedBandName = Utility.UMTS_BAND_12;
                break;
            case 14: decodedBandName = Utility.UMTS_BAND_13;
                break;
            case 15: decodedBandName = Utility.UMTS_BAND_14;
                break;
            case 16: decodedBandName = Utility.UMTS_BAND_15;
                break;
            case 17: decodedBandName = Utility.UMTS_BAND_16;
                break;
            case 18: decodedBandName = Utility.UMTS_BAND_17;
                break;
            case 19: decodedBandName = Utility.UMTS_BAND_18;
                break;
            case 20: decodedBandName = Utility.UMTS_BAND_19;
                break;
            case 21: decodedBandName = Utility.UMTS_BAND_20;
                break;
            case 22: decodedBandName = Utility.UMTS_BAND_21;
                break;
            case 23: decodedBandName = Utility.UMTS_BAND_22;
                break;
            case 24: decodedBandName = Utility.UMTS_BAND_23;
                break;
            case 25: decodedBandName = Utility.UMTS_BAND_24;
                break;
            case 26: decodedBandName = Utility.UMTS_BAND_25;
                break;
            case 27: decodedBandName = Utility.UMTS_BAND_26;
                break;
            case 28: decodedBandName = Utility.UMTS_BAND_27;
                break;
            case 29: decodedBandName = Utility.UMTS_BAND_28;
                break;
            case 30: decodedBandName = Utility.UMTS_BAND_29;
                break;
            case 31: decodedBandName = Utility.UMTS_BAND_30;
                break;
            case 32: decodedBandName = Utility.UMTS_BAND_31;
                break;
            default: decodedBandName = Utility.MISSING_CODE;
                break;
        }
        this.bandID = bandId;
        return decodedBandName;
    }
}
