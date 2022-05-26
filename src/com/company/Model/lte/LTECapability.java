package com.company.Model.lte;

import com.company.Utility;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Model class storing LTE Capabilities
 */
public class LTECapability {
    private int EIT;

    private HashMap<String,Integer> supportedRohcProfiles;
    private HashMap<String, Integer> supportedRohcProfiles_r15;
    public ArrayList<LTEBand> supportedBands;

    protected HashMap<String,Integer> encrAlg;
    protected HashMap<String,Integer> integrAlg;


    public LTECapability(){

        this.supportedRohcProfiles = new HashMap<>();
        supportedRohcProfiles.put(Utility.ROHC_0,0);
        supportedRohcProfiles.put(Utility.ROHC_1,0);
        supportedRohcProfiles.put(Utility.ROHC_2,0);
        supportedRohcProfiles.put(Utility.ROHC_3,0);
        supportedRohcProfiles.put(Utility.ROHC_4,0);
        supportedRohcProfiles.put(Utility.ROHC_6,0);
        supportedRohcProfiles.put(Utility.ROHC_101,0);
        supportedRohcProfiles.put(Utility.ROHC_102,0);
        supportedRohcProfiles.put(Utility.ROHC_103,0);
        supportedRohcProfiles.put(Utility.ROHC_104,0);

        this.supportedRohcProfiles_r15 = new HashMap<>();
        supportedRohcProfiles_r15.put(Utility.ROHC_0_r15,0);
        supportedRohcProfiles_r15.put(Utility.ROHC_1_r15,0);
        supportedRohcProfiles_r15.put(Utility.ROHC_2_r15,0);
        supportedRohcProfiles_r15.put(Utility.ROHC_3_r15,0);
        supportedRohcProfiles_r15.put(Utility.ROHC_4_r15,0);
        supportedRohcProfiles_r15.put(Utility.ROHC_6_r15,0);
        supportedRohcProfiles_r15.put(Utility.ROHC_101_r15,0);
        supportedRohcProfiles_r15.put(Utility.ROHC_102_r15,0);
        supportedRohcProfiles_r15.put(Utility.ROHC_103_r15,0);
        supportedRohcProfiles_r15.put(Utility.ROHC_104_r15,0);

        this.encrAlg = new HashMap<String,Integer>();
        encrAlg.put(Utility.EEA0,0);
        encrAlg.put(Utility.EEA1128,0);
        encrAlg.put(Utility.EEA2128,0);
        encrAlg.put(Utility.EEA3,0);
        encrAlg.put(Utility.EEA4,0);
        encrAlg.put(Utility.EEA5,0);
        encrAlg.put(Utility.EEA6,0);
        encrAlg.put(Utility.EEA7,0);
        this.integrAlg = new HashMap<String,Integer>();
        integrAlg.put(Utility.EIA0,0);
        integrAlg.put(Utility.EIA1128,0);
        integrAlg.put(Utility.EIA2128,0);
        integrAlg.put(Utility.EIA3,0);
        integrAlg.put(Utility.EIA4,0);
        integrAlg.put(Utility.EIA5,0);
        integrAlg.put(Utility.EIA6,0);
        integrAlg.put(Utility.EIA7,0);
    }

    public HashMap<String, Integer> getSupportedRohcProfiles() {
        return supportedRohcProfiles;
    }
    public HashMap<String, Integer> getSupportedRohcProfiles_r15() {
        return supportedRohcProfiles_r15;
    }

    public void setSupportedRohcProfiles(HashMap<String, Integer> supportedRohcProfiles) {
        this.supportedRohcProfiles = supportedRohcProfiles;
    }

    public void setSupportedRohcProfiles_r15(HashMap<String, Integer> supportedRohcProfiles_r15) {
        this.supportedRohcProfiles_r15 = supportedRohcProfiles_r15;
    }
    public int getEIT() {
        return EIT;
    }

    public void setEIT(int EIT) {
        this.EIT = EIT;
    }

    public ArrayList<LTEBand> getSupportedBands() {
        return this.supportedBands;
    }

    public void setSupportedBands(ArrayList<LTEBand> supportedBands) {
        this.supportedBands = supportedBands;
    }

    public HashMap<String, Integer> getEncrAlg() {
        return encrAlg;
    }

    public HashMap<String, Integer> getIntegrAlg() {
        return integrAlg;
    }

}
