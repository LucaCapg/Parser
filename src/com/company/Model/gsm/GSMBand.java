package com.company.Model.gsm;

import com.company.Model.Band;
import com.company.Utility;

public class GSMBand extends Band {
    private String gsmBandName;

    public GSMBand() {
        super();
    }
    public GSMBand(int bandID, int itemID) {
        super(bandID, itemID);
        this.gsmBandName = bandNamesSetter(bandID);
    }

    public String getGsmBandName() {
        return gsmBandName;
    }
    public void setGsmBandName(String gsmBandName) {
        this.gsmBandName = gsmBandName;
    }
    /**
     * Automatically sets band name as string when building a GSM band object
     * @param bandId
     * @return
     */
    private String bandNamesSetter(int bandId){
        String decodedBand = "";
        switch (bandId) {
            case 0: decodedBand = Utility.GSM_0;
                break;
            case 1: decodedBand = Utility.GSM_1;
                break;
            case 2: decodedBand = Utility.GSM_2;
                break;
            case 3: decodedBand = Utility.GSM_3;
                break;
            case 4: decodedBand = Utility.GSM_4;
                break;
            case 5: decodedBand = Utility.GSM_5;
                break;
            case 6: decodedBand = Utility.GSM_6;
                break;
            case 7: decodedBand = Utility.GSM_7;
                break;
            case 8: decodedBand = Utility.GSM_8;
                break;
            case 9: decodedBand = Utility.GSM_9;
                break;
            case 10: decodedBand = Utility.GSM_10;
                break;
            default: decodedBand = Utility.MISSING_CODE;
                break;

        }
        this.bandID = bandId;
        return decodedBand;
    }
}
