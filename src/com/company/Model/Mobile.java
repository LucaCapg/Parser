package com.company.Model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.CellUtil;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.company.Utility;
import com.company.Controller.GUIUtility;
import com.company.Model.gsm.GSMCapability;
import com.company.Model.lte.CACombination;
import com.company.Model.nr.MRDCBandCombination;
import com.company.Model.nr.MRDCBandEutra;
import com.company.Model.nr.MRDCBandNR;
import com.company.Model.umts.UMTSCapability;
import com.company.Utility;

import java.util.*;

/**
 * Model class representing Mobile models
 */
public class Mobile extends LegacyDUT {
    private GSMCapability gsmCap;
    private UMTSCapability umtsCap;

    private int category;
    private int categoryUL;
    private int categoryDL;
    private String categoryDLAsString;
    private String categoryULAsString;
    private String categoriesAsString;

    private ArrayList<CACombination> caBandsCombinations;
    private ArrayList<Band> requestedBands;
    private ArrayList<Band> appliedFreqBandList;
    private ArrayList<MRDCBandNR> nrBands;
    private ArrayList<CACombination> caV10d0BandCombinations;
    private ArrayList<CACombination> caAddR11BandCombinations;
    private ArrayList<CACombination> caV1310BandCombinations;
    public ArrayList<MRDCBandCombination> mrdcBandCombinations;
    private boolean addR11Support = false;
    private int maximumSupportedLayersDL;
    private int maximumSupportedLayersUL;
    private boolean caULSupport = false;
    private boolean ca10i0Support = false;
    private boolean ca1020Support = false;
    private boolean ca1310support = false;
    private boolean channelBW_1530DL_support = false;
    private boolean channelBW_1530UL_support = false;
    private boolean SRVCCToGERANUTRANCapability;
    private boolean interRatPSHOToGERAN;
    private String requestedCCsDL = "False";
    private String requestedCCsUL = "False";
    private Boolean maximumCCsRetrieval = false;
    private Boolean skipFallBackComb = false;
    private Boolean skipFallBackCombReq = false;

    private boolean srb3 = false;
    private boolean splitdrb = false;

    private int NRDCbit = 0;

    public Mobile(){
        super();
        // These values are updated if DUT category > 4
        maximumSupportedLayersUL = 1;
        maximumSupportedLayersDL = 2;
        this.gsmCap = new GSMCapability();
        this.umtsCap = new UMTSCapability();
    }

    public String getCategoriesAsString() {
        if (categoriesAsString == null) {
            categoriesAsString = "";
        }
        return categoriesAsString;
    }

    public void setCategoriesAsString(String categoriesAsString) {
        this.categoriesAsString = categoriesAsString;
    }

    public String getCategoryDLAsString() {
        if (categoryDLAsString == null){
            categoryDLAsString = "";
        }
        return categoryDLAsString;
    }

    public void setCategoryULAsString(String categoryULAsString) {
        this.categoryULAsString = categoryULAsString;
    }

    public void setCategoryDLAsString(String categoryDLAsString) {
        this.categoryDLAsString = categoryDLAsString;
    }

    public String getCategoryULAsString() {
        if (categoryULAsString == null) {
            categoryULAsString = "";
        }
        return categoryULAsString;
    }
    public int getCategory() {
        return this.category;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    /**
     * Setter for downlink category. If categoryDL is present, category field is set to zero
     * because it is basically overwritten by the new DL value (Paired with a UL value).
     * @param categoryDL The downlink category retrieved by the json file
     */
    public void setCategoryDL(int categoryDL) {
        this.categoryDL = categoryDL;
        // if CATEGORY_REGEX DL is present, CATEGORY_REGEX is no more needed
        this.category = 0;
    }

    public int getCategoryUL() {
        return this.categoryUL;
    }
    /**
     * Setter for uplink category. If categoryUL is present, category field is set to zero
     * because it is basically overwritten by the new UL value(paired with a DL value).
     * @param categoryUL The uplink category retrieved by the json file
     */
    public void setCategoryUL(int categoryUL) {
        this.categoryUL = categoryUL;
        // if CATEGORY_REGEX DL is present, CATEGORY_REGEX is no more needed
        this.category = 0;
    }
    public int getCategoryDL() {
        return this.categoryDL;
    }

    public ArrayList<CACombination> getCaBandsCombinations() {
        return caBandsCombinations;
    }
    /**
     * Set band combinations related to v1020 and maximum number of layers in case of devices supporting only 1020
     * @param caBandsCombinations the structure containing the band combinations (still without tm34)
     */
    public void setCaBandsCombinations(ArrayList<CACombination> caBandsCombinations) {
        this.caBandsCombinations = caBandsCombinations;
    }

    public void setCaV1310BandCombinations(ArrayList<CACombination> caV1310BandCombinations){
        this.caV1310BandCombinations = caV1310BandCombinations;
    }

    public ArrayList<CACombination> getCaV1310BandCombinations() {
        return this.caV1310BandCombinations;
    }

    public boolean getchannelBW_1530DL_support() {
        return this.channelBW_1530DL_support;
    }

    public void setChannelBW_1530DL_support(Boolean channelBW_1530DL_support){
        this.channelBW_1530DL_support = channelBW_1530DL_support;
    }

    public boolean getchannelBW_1530UL_support() {
        return this.channelBW_1530UL_support;
    }

    public void setChannelBW_1530UL_support(Boolean channelBW_1530UL_support){
        this.channelBW_1530UL_support = channelBW_1530UL_support;
    }

    public void setRequestedCCsDL(String requestedCCsDL){
        this.requestedCCsDL = requestedCCsDL;
    }

    public String getRequestedCCsDL(){
        return this.requestedCCsDL;
    }

    public void setRequestedCCsUL(String requestedCCsUL){
        this.requestedCCsUL = requestedCCsUL;
    }

    public String getRequestedCCsUL(){
        return this.requestedCCsUL;
    }

    public Boolean getMaximumCCsRetrieval () { return this.maximumCCsRetrieval; }

    public void setMaximumCCsRetrieval(Boolean maximumCCsRetrieval) { this.maximumCCsRetrieval = maximumCCsRetrieval; }

    public Boolean getSkipFallBackComb(){
        return this.skipFallBackComb;
    }

    public void setSkipFallBackComb(Boolean skipFallBackComb){
        this.skipFallBackComb = skipFallBackComb;
    }

    public Boolean getSkipFallBackCombReq(){
        return  this.skipFallBackCombReq;
    }

    public void setSkipFallBackCombReq(Boolean skipFallBackCombReq){
        this.skipFallBackCombReq = skipFallBackCombReq;
    }

    public void orderBandsCombination(){
        // Check maximum number of supported layers
        int supportedLayersDL = 0;
        int supportedLayersDLTM34 = 0;
        int supportedLayersUL = 0;
        int supportedLayersULTM34 = 0;
        int ccQuantityUL = 0;
        // Check maximum of supported layers
        for (int i = 0; i < caBandsCombinations.size(); i++){
            // Uplink
            ccQuantityUL = 0;
            for (int k = 0; k < caBandsCombinations.get(i).getULSingleCombination().size(); k++) {
                supportedLayersUL = supportedLayersUL + caBandsCombinations.get(i).getULSingleCombination().get(k).getSupportedLayers();
                supportedLayersULTM34 = supportedLayersULTM34 + caBandsCombinations.get(i).getULSingleCombination().get(k).getSupportedLayersTM34();
                String ulClass = caBandsCombinations.get(i).getULSingleCombination().get(k).getComponentCarrierClass();
                if (ulClass.equals(Utility.BANDWIDTH_CLASS_A)) {
                    ccQuantityUL = ccQuantityUL + 1;
                } else if (ulClass.equals(Utility.BANDWIDTH_CLASS_B)) {
                    ccQuantityUL = ccQuantityUL + 2;
                } else if (ulClass.equals(Utility.BANDWIDTH_CLASS_C)) {
                    ccQuantityUL = ccQuantityUL + 2;
                } else if (ulClass.equals(Utility.BANDWIDTH_CLASS_D)) {
                    ccQuantityUL = ccQuantityUL + 3;
                }
            }
            caBandsCombinations.get(i).setCcQuantityUL(ccQuantityUL);
            if (ccQuantityUL > 1){
                this.caULSupport = true;
            }
            if (supportedLayersUL >= supportedLayersULTM34) {
                if (supportedLayersUL >= this.maximumSupportedLayersUL) {
                    this.maximumSupportedLayersUL = supportedLayersUL;
                }
            } else {
                if (supportedLayersULTM34 >= this.maximumSupportedLayersUL) {
                    this.maximumSupportedLayersUL = supportedLayersULTM34;
                }
            }
            supportedLayersUL = 0;
            supportedLayersULTM34 = 0;
            // Initialize to one so we neglect UL/DL first band duplication
            int singleMagnitude = 1;
            // Subtract 1 if UL combinations are empty
            if (caBandsCombinations.get(i).getULSingleCombination().size() == 0) {
                singleMagnitude--;
            }
            int totalMagnitude = 0;
            int ccQuantityDL = 0;
            for (int h = 0; h < caBandsCombinations.get(i).getDLSingleCombination().size() ; h++) {
                int currentBandId = caBandsCombinations.get(i).getDLSingleCombination().get(h).getBandID();
                for (int w = h + 1; w < caBandsCombinations.get(i).getDLSingleCombination().size(); w ++) {
                    if (currentBandId == caBandsCombinations.get(i).getDLSingleCombination().get(w).getBandID()) {
                        singleMagnitude++;
                    }
                }
                for (int r = h+1; r < caBandsCombinations.get(i).getULSingleCombination().size(); r++) {
                    if (currentBandId == caBandsCombinations.get(i).getULSingleCombination().get(r).getBandID()) {
                        singleMagnitude++;
                    }
                }
                String ccClass = caBandsCombinations.get(i).getDLSingleCombination().get(h).getComponentCarrierClass();
                if (ccClass.equals(Utility.BANDWIDTH_CLASS_A)) {
                    ccQuantityDL = ccQuantityDL + 1;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_B)) {
                    ccQuantityDL = ccQuantityDL + 2;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_C)) {
                    ccQuantityDL = ccQuantityDL + 2;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_D)) {
                    ccQuantityDL = ccQuantityDL + 3;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_E)) {
                    ccQuantityDL = ccQuantityDL + 4;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_F)) {
                    ccQuantityDL = ccQuantityDL + 5;
                }
            }
            totalMagnitude = caBandsCombinations.get(i).getDLSingleCombination().size()
                    + caBandsCombinations.get(i).getULSingleCombination().size()
                    - singleMagnitude;
            caBandsCombinations.get(i).setCombinationMagnitude(totalMagnitude);
            caBandsCombinations.get(i).setCcQuantityDL(ccQuantityDL);
            // Downlink
            for (int j = 0; j < caBandsCombinations.get(i).getDLSingleCombination().size(); j++) {
                supportedLayersDL = supportedLayersDL + caBandsCombinations.get(i).getDLSingleCombination().get(j).getSupportedLayers();
                supportedLayersDLTM34 = supportedLayersDLTM34 + caBandsCombinations.get(i).getDLSingleCombination().get(j).getSupportedLayersTM34();
            }
            if (supportedLayersDL >= supportedLayersDLTM34) {
                if (supportedLayersDL >= this.maximumSupportedLayersDL) {
                    this.maximumSupportedLayersDL = supportedLayersDL;
                }
            } else {
                if (supportedLayersDLTM34 >= this.maximumSupportedLayersDL) {
                    this.maximumSupportedLayersDL = supportedLayersDLTM34;
                }
            }
            supportedLayersDL = 0;
            supportedLayersDLTM34 = 0;
        }
        // Move PCC
        int primaryBandId = 0;
        for (int i = 0; i < this.caBandsCombinations.size(); i++){
        if (this.caBandsCombinations.get(i).getULSingleCombination().size() != 0){
            primaryBandId = this.caBandsCombinations.get(i).getULSingleCombination().get(0).getBandID();
            for (int y = 0; y < this.caBandsCombinations.get(i).getDLSingleCombination().size(); y++) {
                if (primaryBandId == this.caBandsCombinations.get(i).getDLSingleCombination().get(y).getBandID()){
                    Collections.swap(this.caBandsCombinations.get(i).getDLSingleCombination(),y,0);
                }
            }
        }
        }
    }


    public void orderBandsCombination1310(){
        // Check maximum number of supported layers
        int supportedLayersDL = 0;
        int supportedLayersDLTM34 = 0;
        int supportedLayersUL = 0;
        int supportedLayersULTM34 = 0;
        int ccQuantityUL = 0;
        // Check maximum of supported layers
        for (int i = 0; i < caV1310BandCombinations.size(); i++){
            // Uplink
            ccQuantityUL = 0;
            for (int k = 0; k < caV1310BandCombinations.get(i).getULSingleCombination().size(); k++) {
                supportedLayersUL = supportedLayersUL + caV1310BandCombinations.get(i).getULSingleCombination().get(k).getSupportedLayers();
                supportedLayersULTM34 = supportedLayersULTM34 + caV1310BandCombinations.get(i).getULSingleCombination().get(k).getSupportedLayersTM34();
                String ulClass = caV1310BandCombinations.get(i).getULSingleCombination().get(k).getComponentCarrierClass();
                if (ulClass.equals(Utility.BANDWIDTH_CLASS_A)) {
                    ccQuantityUL = ccQuantityUL + 1;
                } else if (ulClass.equals(Utility.BANDWIDTH_CLASS_B)) {
                    ccQuantityUL = ccQuantityUL + 2;
                } else if (ulClass.equals(Utility.BANDWIDTH_CLASS_C)) {
                    ccQuantityUL = ccQuantityUL + 2;
                } else if (ulClass.equals(Utility.BANDWIDTH_CLASS_D)) {
                    ccQuantityUL = ccQuantityUL + 3;
                }
            }
            caV1310BandCombinations.get(i).setCcQuantityUL(ccQuantityUL);
            if (ccQuantityUL > 1){
                this.caULSupport = true;
            }
            if (supportedLayersUL >= supportedLayersULTM34) {
                if (supportedLayersUL >= this.maximumSupportedLayersUL) {
                    this.maximumSupportedLayersUL = supportedLayersUL;
                }
            } else {
                if (supportedLayersULTM34 >= this.maximumSupportedLayersUL) {
                    this.maximumSupportedLayersUL = supportedLayersULTM34;
                }
            }
            supportedLayersUL = 0;
            supportedLayersULTM34 = 0;
            // Initialize to one so we neglect UL/DL first band duplication
            int singleMagnitude = 1;
            // Subtract 1 if UL combinations are empty
            if (caV1310BandCombinations.get(i).getULSingleCombination().size() == 0) {
                singleMagnitude--;
            }
            int totalMagnitude = 0;
            int ccQuantityDL = 0;
            for (int h = 0; h < caV1310BandCombinations.get(i).getDLSingleCombination().size(); h++) {
                int currentBandId = caV1310BandCombinations.get(i).getDLSingleCombination().get(h).getBandID();
                for (int w = h + 1; w < caV1310BandCombinations.get(i).getDLSingleCombination().size(); w++) {
                    if (currentBandId == caV1310BandCombinations.get(i).getDLSingleCombination().get(w).getBandID()) {
                        singleMagnitude++;
                    }
                }
                for (int r = h + 1; r < caV1310BandCombinations.get(i).getULSingleCombination().size(); r++) {
                    if (currentBandId == caV1310BandCombinations.get(i).getULSingleCombination().get(r).getBandID()) {
                        singleMagnitude++;
                    }
                }
                String ccClass = caV1310BandCombinations.get(i).getDLSingleCombination().get(h).getComponentCarrierClass();
                if (ccClass.equals(Utility.BANDWIDTH_CLASS_A)) {
                    ccQuantityDL = ccQuantityDL + 1;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_B)) {
                    ccQuantityDL = ccQuantityDL + 2;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_C)) {
                    ccQuantityDL = ccQuantityDL + 2;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_D)) {
                    ccQuantityDL = ccQuantityDL + 3;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_E)) {
                    ccQuantityDL = ccQuantityDL + 4;
                } else if (ccClass.equals(Utility.BANDWIDTH_CLASS_F)) {
                    ccQuantityDL = ccQuantityDL + 5;
                }
            }
            totalMagnitude = caV1310BandCombinations.get(i).getDLSingleCombination().size()
                    + caV1310BandCombinations.get(i).getULSingleCombination().size()
                    - singleMagnitude;
            caV1310BandCombinations.get(i).setCombinationMagnitude(totalMagnitude);
            caV1310BandCombinations.get(i).setCcQuantityDL(ccQuantityDL);
            // Downlink
            for (int j = 0; j < caV1310BandCombinations.get(i).getDLSingleCombination().size(); j++) {
                supportedLayersDL = supportedLayersDL + caV1310BandCombinations.get(i).getDLSingleCombination().get(j).getSupportedLayers();
                supportedLayersDLTM34 = supportedLayersDLTM34 + caV1310BandCombinations.get(i).getDLSingleCombination().get(j).getSupportedLayersTM34();
            }
            if (supportedLayersDL >= supportedLayersDLTM34) {
                if (supportedLayersDL >= this.maximumSupportedLayersDL) {
                    this.maximumSupportedLayersDL = supportedLayersDL;
                }
            } else {
                if (supportedLayersDLTM34 >= this.maximumSupportedLayersDL) {
                    this.maximumSupportedLayersDL = supportedLayersDLTM34;
                }
            }
            supportedLayersDL = 0;
            supportedLayersDLTM34 = 0;
        }
        // Move PCC
        int primaryBandId = 0;
        for (int i = 0; i < this.caV1310BandCombinations.size(); i++){
            if (this.caV1310BandCombinations.get(i).getULSingleCombination().size() != 0){
                primaryBandId = this.caV1310BandCombinations.get(i).getULSingleCombination().get(0).getBandID();
                for (int y = 0; y < this.caV1310BandCombinations.get(i).getDLSingleCombination().size(); y++) {
                    if (primaryBandId == this.caV1310BandCombinations.get(i).getDLSingleCombination().get(y).getBandID()){
                        Collections.swap(this.caV1310BandCombinations.get(i).getDLSingleCombination(),y,0);
                    }
                }
            }
        }
    }

    public int getMaximumSupportedLayersDL() {
        return maximumSupportedLayersDL;
    }

    public void setMaximumSupportedLayersDL(int maximumSupportedLayersDL) {
        this.maximumSupportedLayersDL = maximumSupportedLayersDL;
    }

    public int getMaximumSupportedLayersUL() {
        return maximumSupportedLayersUL;
    }

    public void setMaximumSupportedLayersUL(int maximumSupportedLayersUL) {
        this.maximumSupportedLayersUL = maximumSupportedLayersUL;
    }

    public boolean isCaULSupport() {
        return caULSupport;
    }

    public void setCaULSupport(boolean caULSupport) {
        this.caULSupport = caULSupport;
    }

    public ArrayList<Band> getRequestedBands() {
        return requestedBands;
    }

    public void setRequestedBands(ArrayList<Band> requestedBands) {
        this.requestedBands = requestedBands;
    }

    public ArrayList<MRDCBandNR> getNrBands() {
		return nrBands;
	}

	public void setNrBands(ArrayList<MRDCBandNR> nrBands) {
		this.nrBands = nrBands;
	}

	public ArrayList<Band> getAppliedFreqBandList(){
        return appliedFreqBandList;
    }

    public void setAppliedFreqBandList(ArrayList<Band> appliedFreqBandList){
        this.appliedFreqBandList = appliedFreqBandList;
    }

	public boolean isSRVCCToGERANUTRANCapability() {
        return SRVCCToGERANUTRANCapability;
    }

    /**
     * Parse and set the support for SRVCC to GERAN/UTRAN in attach request message
     * @param SRVCCToGERANUTRANCapability the integer code retrieved from the attach request
     */
    public void setSRVCCToGERANUTRANCapability(int SRVCCToGERANUTRANCapability) {
        switch (SRVCCToGERANUTRANCapability) {
            case 0:
                this.SRVCCToGERANUTRANCapability = false;
                break;
            case 1:
                this.SRVCCToGERANUTRANCapability = true;
                break;
            default:
                this.SRVCCToGERANUTRANCapability = false;
                break;
        }
    }

    public GSMCapability getGsmCap() {
        return gsmCap;
    }

    public UMTSCapability getUmtsCap() {
        return umtsCap;
    }

    @Override
    public void setAccessStratumRelease(int accessStratumRelease) {
        this.accessStratumRelease = Utility.accessStratumReleaseDecoder(accessStratumRelease);
    }

    public boolean isCa10i0Support() {
        return ca10i0Support;
    }

    public void setCa10i0Support(boolean ca10i0Support) {
        this.ca10i0Support = ca10i0Support;
    }

    public boolean isCa1020Support() {
        return ca1020Support;
    }

    public void setCa1020Support(boolean ca1020Support) {
        this.ca1020Support = ca1020Support;
    }
    public Boolean getCa1310Support(boolean ca1310support){
         return this.ca1310support;
    }

    public void setCa1310Support(boolean ca1310support){
        this.ca1310support = ca1310support;
    }

    public boolean isCa1310Support(){
        return ca1310support;
    }

    public boolean isInterRatPSHOToGERAN() {
        return interRatPSHOToGERAN;
    }

    public void setInterRatPSHOToGERAN(int interRatPSHOToGERAN) {
        switch (interRatPSHOToGERAN) {
            case 0:
                this.interRatPSHOToGERAN = false;
                break;
            case 1:
                this.interRatPSHOToGERAN = true;
                break;
            default:
                this.interRatPSHOToGERAN = false;
                break;
        }
    }

    public ArrayList<CACombination> getCaV10d0BandCombinations() {
        return caV10d0BandCombinations;
    }

    public void setCaV10d0BandCombinations(ArrayList<CACombination> caV10d0BandCombinations) {
        this.caV10d0BandCombinations = caV10d0BandCombinations;

    }

    public void addAddR11ToCABandCOmbination(ArrayList<CACombination> caAddR11BandCombinations){
        caBandsCombinations.addAll(caAddR11BandCombinations);

    }

    public void setCaAddR11BandCombinations(ArrayList<CACombination> caAddR11BandCombinations) {
        this.caAddR11BandCombinations = caAddR11BandCombinations;
    }

    public ArrayList<CACombination> getCaAddR11BandCombinations() {
        return caAddR11BandCombinations;
    }

    public ArrayList<MRDCBandCombination> getMrdcBandCombinations() {
		return mrdcBandCombinations;
	}

	public void setMrdcBandCombinations(ArrayList<MRDCBandCombination> mrdcBandCombinations) {
		this.mrdcBandCombinations = mrdcBandCombinations;
	}

	public boolean isAddR11Support() {
        return addR11Support;
    }

    public void setAddR11Support(boolean addR11Support) {
        this.addR11Support = addR11Support;
    }

	public boolean isSrb3() {
		return srb3;
	}

	public void setSrb3(boolean srb3) {
		this.srb3 = srb3;
	}

	public boolean isSplitdrb() {
		return splitdrb;
	}

	public void setSplitdrb(boolean splitdrb) {
		this.splitdrb = splitdrb;
	}

    /**
     * Method to print on screen parsed data from DUT
     * @param device Device Under Test
     */
    public static void printDUTCap (Mobile device){

        System.out.println("\n");
        System.out.println("AS Release:  rel" + device.getAccessStratumRelease());
        if(device.getAccessStratumReleaseNR() < 0)
        	System.out.println("AS Release 5G:  -");
        else
        	System.out.println("AS Release 5G:  rel" + device.getAccessStratumReleaseNR());
        System.out.println();
        System.out.println("Dual Connectivity of E-UTRA with NR Capability (DCNR): "+device.getNRDCbit());
        if(device.isSrb3())
        	System.out.println("Signalling Radio Bearer3 (SRB3):  supported");
        else
        	System.out.println("Signalling Radio Bearer3 (SRB3): NOT supported");
        if(device.isSplitdrb())
        	System.out.println("splitDRB-withUL-Both-MCG-SCG: supported");
        else
        	System.out.println("splitDRB-withUL-Both-MCG-SCG: NOT supported");
             System.out.println();

        System.out.println("ESM Info Transfer Flag (EIT): " + device.getLteCap().getEIT());
        if (device.getVoiceDomainPreference() != Utility.NOT_APPLICABLE_CODE) {
            System.out.println("UE Usage Settings: " + device.getUeUsageSettings());
            System.out.println("Additional Update Type: " + device.getAdditionalUpdateType());
            System.out.println("Voice Domain Preference: " + Utility.voiceDomainPreferenceDecoder(device.getVoiceDomainPreference()));
        } else {
            System.out.println("UE Usage Settings: " + device.getUeUsageSettings());
            System.out.println("Voice Domain Preference: " + Utility.voiceDomainPreferenceDecoder(device.getVoiceDomainPreference()));
            System.out.println("Additional Update Type: " + device.getAdditionalUpdateType());
        }
        System.out.println("Attach Type: " + Utility.attachTypeDecoder(device.getAttachType()) );
        System.out.println("PDN Type: " + pdnTypeDecoder(device.getPdnType()));
        System.out.println("SRVCC TO GERAN/UTRAN CAPABILITY: " + device.isSRVCCToGERANUTRANCapability());
        System.out.println();

        System.out.println("Category:  " + device.getCategory() + " As String: " + device.getCategoriesAsString());
        System.out.println("\n");
        System.out.println("Category DL:  " + device.getCategoryDL() + " As String: " + device.getCategoryDLAsString());
        System.out.println("Category UL:  " + device.getCategoryUL() + " As String: " + device.getCategoryULAsString());
        System.out.println("Category As String: " + device.getCategoriesAsString());
        System.out.println("\n");

        String temp = "";
        int[] temp2;
        if (device.getLteCap().getFeatureGroupIndicators() != null) {
            for (int j = 1 ; j < device.getLteCap().getFeatureGroupIndicators().length + 1; j++) {
                temp2 = device.getLteCap().getFeatureGroupIndicators();
                temp = temp.concat("").concat(Integer.toString(temp2[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
        } else {
            temp = Utility.NOT_APPLICABLE;
        }
        System.out.println("fgi (a.k.a fgiRel8):  " + temp);
        System.out.println("\n");
        // Print FGI R9 (Index starts from 1 for well formatting)
        int[] temp3;
        temp = "";
        if (device.getLteCap().getFeatureGroupIndicatorsAddR9() != null) {
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsAddR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsAddR9();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
        } else {
            temp = Utility.NOT_APPLICABLE;
        }
        System.out.println("fgiRel9Add-R9:  " + temp);

        // FDD
        // AddR9
        if (device.getLteCap().getFeatureGroupIndicatorsFDDAddR9() != null) {
            temp = "";
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsFDDAddR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsFDDAddR9();
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
        if (device.getLteCap().getFeatureGroupIndicatorsTDDAddR9() != null) {
            temp = "";
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsTDDAddR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsTDDAddR9();
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
        if (device.getLteCap().getFeatureGroupIndicatorsFDDR9()!= null) {
            temp = "";
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsFDDR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsFDDR9();
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
        if (device.getLteCap().getFeatureGroupIndicatorsTDDR9() != null) {
            temp = "";
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsTDDR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsTDDR9();
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
        if (device.getLteCap().getFeatureGroupIndicatorsR10() == null) {
            System.out.println("fgiRel10-r10:  " + Utility.NOT_APPLICABLE);
        } else {
            temp = "";
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsR10().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsR10();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }

            System.out.println("FGI-Rel10:  " + temp);
        }

        // FGI rel10 FDD TDD
        if (device.getLteCap().getFeatureGroupIndicatorsR10FDD() == null) {
            System.out.println("fgiRel10-r10 (FDD):  " + Utility.NOT_APPLICABLE);
        } else {
            temp = "";
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsR10FDD().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsR10FDD();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
            System.out.println("fgiRel10-r10 (FDD):  " + temp);
        }

        //TDD
        if (device.getLteCap().getFeatureGroupIndicatorsR10TDD() == null) {
            System.out.println("fgiRel10-r10 (TDD):  " + Utility.NOT_APPLICABLE);
        } else {
            temp = "";
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsR10TDD().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsR10TDD();
                temp = temp.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    temp = temp.concat("   ");
                }
            }
            System.out.println("fgiRel10-r10 (TDD):  " + temp);
        }

        System.out.println("\n");
        // Print txAntenna sel. supp.
        System.out.println("Tx Antenna Selection Support: " + device.getLteCap().isTxAntennaSelectionSupport());
        System.out.println("Ue SpecificRefSigsSupported: " + device.getLteCap().isUeSpecificRefsSigsSupport());
        System.out.println("INTER RAT PS HO To GERAN Support: " + device.isInterRatPSHOToGERAN());
        System.out.println("eRedirection to GERAN: "+ device.getLteCap().iseRedirectionGERAN());
        System.out.println("eRedirection to UTRAN: " + device.getLteCap().iseRedirectionUTRAN());
        System.out.println("Rach-Report-R9: " + device.getLteCap().isRachReportSON());
        System.out.println("UMTS FDD Radio Access Cap. Support: " + device.getUmtsCap().isFddRadioAccessTechnCap());
        System.out.println("CDMA 2000 Support: " + device.getUmtsCap().isCdma2000Support());
        System.out.println("UMTS MCPS 384 TDD Support: " + device.getUmtsCap().isMcps384TDDRatCap());
        System.out.println("UMTS MCPS 128 TDD Support: " +device.getUmtsCap().isMcps128TDDRatCap());
        System.out.println("UCS2 Support: " + device.getGsmCap().getUcs2Support());
        System.out.println("SS Screening indicator support: " + device.getGsmCap().getSsScreeningIndicator());
        System.out.println("PFC Feature support: " + device.getGsmCap().isPfcFeatureSupport());
        System.out.println("LCS VA capability support:" + device.getGsmCap().isLcsSupport());
        System.out.println("PS inter-RAT HO from GERAN to UTRAN Iu mode capability support:" + device.getGsmCap().isInterRatPSHOtoUTRANIu());
        System.out.println("PS inter-RAT HO from GERAN to E-UTRAN S1 mode capability support:" + device.getGsmCap().isInterRatPSHOtoUTRANS1());
        System.out.println("EMM Combined procedures capability support:" + device.getGsmCap().isEmmProceduresSupport());
        System.out.println("ISR support:" + device.getGsmCap().isIsrSupport());
        System.out.println("EPC capability support:" + device.getGsmCap().isEpcSupport());
        System.out.println("NF Capability support:" + device.getGsmCap().isNfcSupport());
        System.out.println("ES IND support" + device.getGsmCap().isEsIndSupport());
        System.out.println("GMSK Multislot Power Profile: " + device.getGsmCap().getGmskMultislotPowerProfile());
        System.out.println("8-PSK Multislot Power Profile:" + device.getGsmCap().getPsk8MultislotPowerProfile());
        System.out.println("Downlink Advanced Receiver Performance: " + device.getGsmCap().getAdvancedReceiverPerformance());
        System.out.println("DTM Support: " + device.getGsmCap().isDtmSupport());
        System.out.println("Priority Based Reselection Support: " + device.getGsmCap().isPriorityBasedReselectionSupport());
        System.out.println("GPRS Multi Slot Support: " + device.getGsmCap().getGprsMultiSlotClass());
        System.out.println("EGPRS Multi Slot Support: " + device.getGsmCap().getEgprsMultiSlotClass());
        System.out.println("Single slot DTM support: "+ device.getGsmCap().isDtmSupport());
        System.out.println("8PSK RF Power Capability1: " + device.getGsmCap().getPsk8RFPowerCapability1());
        System.out.println("8PSK RF Power Capability2: " + device.getGsmCap().getPsk8RFPowerCapability2());
        System.out.println("\nRF Power Capability1: " + device.getGsmCap().getRfPowerCapability1());
        System.out.println("RF Power Capability2: " + device.getGsmCap().getRfPowerCapability2());
        int counter = 0;
        System.out.println("PS Capability: " + device.getGsmCap().isPsCapability());
        // Print Supported Bands
        temp = "";
        String temp4 = "";
        if (device.getLteCap().getSupportedBands() != null) {
            for (int j = 1 ; j < device.getLteCap().getSupportedBands().size() + 1; j++) {
                temp = temp.concat(Integer.toString(device.getLteCap().getSupportedBands().get(j-1).getBandID()));
                temp4 = temp4.concat(" ").concat(temp);
                temp = " ";
            }
        } else {
            temp4 = Utility.NOT_APPLICABLE;
        }
        System.out.println("Supported Bands:  " + temp4);
        System.out.println("\n");
        // Print 256 QAM supported bands
        temp = "  ";
        temp4 = "";
        if (device.getLteCap().getSupportedBands() != null) {
            for (int j = 1 ; j < device.getLteCap().getSupportedBands().size() + 1; j++) {
                if ( device.getLteCap().getSupportedBands().get(j-1).isSupport256QAMDL() ){
                    temp = temp.concat(Integer.toString(device.getLteCap().getSupportedBands().get(j-1).getBandID()));
                } else {
                    temp = temp.concat(Integer.toString(device.getLteCap().getSupportedBands().get(j-1).getBandID()));
                    temp = temp.concat(" NO,");
                }
                temp4 = temp4.concat(" ").concat(temp);
                temp = " ";
            }
        } else {
            temp4 = Utility.NOT_APPLICABLE;
        }
        System.out.println("256QAM-DL support:  " + temp4);
        System.out.println("\n");
        // print 64 QAM supported bands
        temp4 = "";
        for (int j = 1 ; j < device.getLteCap().getSupportedBands().size() + 1; j++) {
            if ( device.getLteCap().getSupportedBands().get(j-1).isSupport64QAMUL() ){
                temp = temp.concat(Integer.toString(device.getLteCap().getSupportedBands().get(j-1).getBandID()));
            } else {
                temp = temp.concat(Integer.toString(device.getLteCap().getSupportedBands().get(j-1).getBandID()));
                temp = temp.concat(" NO,");
            }
            temp4 = temp4.concat(" ").concat(temp);
            temp = " ";
        }
        System.out.println("64QAM-UL support:  " + temp4);
        System.out.println("\n");
        // Check if device supports CA
        if(device.isCa1310Support()){
            System.out.println("SupportDLCombination-v1310: TRUE");
        }
        else{
            System.out.println("SupportDLCombination-v1310: FALSE");
        }
        if (device.isCa1020Support()) {
            System.out.println("SupportDLCombination-v1020: TRUE");
            System.out.println("Maximum number of supported layers DL:" + device.getMaximumSupportedLayersDL());
            System.out.println("Maximum number of supported layers UL:" + device.getMaximumSupportedLayersUL());
            System.out.println("CA UL Support: " + device.isCaULSupport());
            if ((device.getCaBandsCombinations() != null) && (device.getCaBandsCombinations().size() != 0)) {
                System.out.println("SupportDLCombination-v10i0: TRUE");
            } else {
                System.out.println("SupportDLCombination-v10i0: FALSE");
            }
            System.out.println("\n");
            // RELEASE 10
            // Downlink CA Combinations
            //BCS=0,1,2
            temp4 = "";
            for (int i = 0; i < device.getCaBandsCombinations().size(); i++) {
                int numberOfcomponentsDL = 0;
                temp4 = temp4.concat("("+Integer.toString(i)+")").concat("CA_");
                for (int j = 0; j < device.getCaBandsCombinations().get(i).getDLSingleCombination().size(); j++) {
                    temp4 = temp4.concat(Integer.toString(device.getCaBandsCombinations().get(i).getDLSingleCombination().get(j).getBandID())).concat("");
                    temp4 = temp4.concat(device.getCaBandsCombinations().get(i).getDLSingleCombination().get(j).getComponentCarrierClass()).concat("");
                    if (device.getCaBandsCombinations().get(i).getDLSingleCombination().get(j).getSupportedLayers() == 4) {
                        temp4 = temp4.concat("(4x4)");
                    } else if (device.getCaBandsCombinations().get(i).getDLSingleCombination().get(j).getSupportedLayers() == 2) {
                        temp4 = temp4.concat("(2x2)");
                    }
                    if ((device.getCaBandsCombinations().get(i).getDLSingleCombination().size()>1)
                            && ( j != device.getCaBandsCombinations().get(i).getDLSingleCombination().size()- 1 ) ) {
                        temp4 = temp4.concat("-");
                        numberOfcomponentsDL++;
                    } else {
                        if (device.getCaBandsCombinations().get(i).getSupportedBCS() != null) {
                            temp4 = temp4.concat("    BCS:").concat(Utility.bcsParser(device.getCaBandsCombinations().get(i).getSupportedBCS()));
                            temp4 = temp4.concat("\t\t\tCC Quantity DL:" + String.valueOf(device.getCaBandsCombinations().get(i).getCcQuantityDL()));
                            temp4 = temp4.concat("\t\t\tCC Quantity UL:" + String.valueOf(device.getCaBandsCombinations().get(i).getCcQuantityUL()));
                        }
                    }


                }
                device.getCaBandsCombinations().get(i).setNumberOfcomponentsDL(numberOfcomponentsDL+1);
                temp4 = temp4.concat("\n");


            }
            System.out.println("Downlink CA Combinations(v1020) and BCS:");
            System.out.println(temp4);
            System.out.println("\n");
            // Uplink CA COmbinations
            temp4 = "";
            System.out.println(device.getCaBandsCombinations().size());
            for (int i = 0; i < device.getCaBandsCombinations().size(); i++) {
                int numberOfComponentsUL = 0;
                // Manage print of empty ul combinations
                if (device.getCaBandsCombinations().get(i).getULSingleCombination() != null) {
                    if ( (device.getCaBandsCombinations().get(i).getULSingleCombination().size() != 0)){
                        temp4 = temp4.concat("("+Integer.toString(i)+")").concat("CA_");
                    } else {
                        temp4 = temp4.concat("(");
                        temp4 = temp4.concat(Integer.toString(i)).concat(")");
                    }
                }
                //temp4 = temp4.concat("("+Integer.toString(i)+")").concat("CA_");
                for (int j = 0; j < device.getCaBandsCombinations().get(i).getULSingleCombination().size(); j++) {
                    temp4 = temp4.concat(Integer.toString(device.getCaBandsCombinations().get(i).getULSingleCombination().get(j).getBandID())).concat("");
                    temp4 = temp4.concat(device.getCaBandsCombinations().get(i).getULSingleCombination().get(j).getComponentCarrierClass()).concat("");
                    if (device.getCaBandsCombinations().get(i).getULSingleCombination().get(j).getSupportedLayers() == 2) {
                        temp4 = temp4.concat("(2x2)");
                    } else if (device.getCaBandsCombinations().get(i).getULSingleCombination().get(j).getSupportedLayers() == 1) {
                        temp4 = temp4.concat("(1x1)");
                    }
                    if (device.getCaBandsCombinations().get(i).getULSingleCombination().get(j).getSupport256QAMUL() == true) {
                        temp4 = temp4.concat(" - (256 QAM Uplink Support: Yes)");
                    }
                    else temp4 = temp4.concat(" - (256 QAM Uplink Support: No)");
                    if ((device.getCaBandsCombinations().get(i).getULSingleCombination().size()>1) && ( j != device.getCaBandsCombinations().get(i).getULSingleCombination().size()- 1 ) ) {
                        temp4 = temp4.concat("-");
                        numberOfComponentsUL++;
                    } else if ( j == device.getCaBandsCombinations().get(i).getULSingleCombination().size() -1) {
                        if ( device.getCaBandsCombinations().get(i).getULSingleCombination().size() > 1) {
                            temp4 = temp4.concat("         CA_UL_SUPPORTED");
                        } else {
                            //temp4 = temp4.concat("         CA_UL_NOT_SUPPORTED");
                        }
                    } else {
                        if (device.getCaBandsCombinations().get(i).getSupportedBCS() != null) {
                            temp4 = temp4.concat("    BCS:").concat(Utility.bcsParser(device.getCaBandsCombinations().get(i).getSupportedBCS()));
                        }
                    }
                }
                device.getCaBandsCombinations().get(i).setNumberOfcomponentsUL(numberOfComponentsUL+1);
                temp4 = temp4.concat("\n");
            }
            System.out.println("Uplink CA Combinations(v1020):");
            System.out.println(temp4);
            System.out.println("\n");

            if (device.isCa10i0Support()) {
                // TM34
                // Downlink CA Combinations
                temp4 = "";
                for (int i = 0; i < device.getCaBandsCombinations().size(); i++) {
                    temp4 = temp4.concat("("+Integer.toString(i)+")").concat("CA_");
                    for (int j = 0; j < device.getCaBandsCombinations().get(i).getDLSingleCombination().size(); j++) {
                        temp4 = temp4.concat(Integer.toString(device.getCaBandsCombinations().get(i).getDLSingleCombination().get(j).getBandID())).concat("");
                        temp4 = temp4.concat(device.getCaBandsCombinations().get(i).getDLSingleCombination().get(j).getComponentCarrierClass()).concat("");
                        if (device.getCaBandsCombinations().get(i).getDLSingleCombination().get(j).getSupportedLayersTM34() == 2) {
                            temp4 = temp4.concat("(2x2)");
                        } else if (device.getCaBandsCombinations().get(i).getDLSingleCombination().get(j).getSupportedLayersTM34() == 4) {
                            temp4 = temp4.concat("(4x4)");
                        }
                        if ((device.getCaBandsCombinations().get(i).getDLSingleCombination().size()>1)
                                && ( j != device.getCaBandsCombinations().get(i).getDLSingleCombination().size()- 1 ) ) {
                            temp4 = temp4.concat("-");
                        } else {
                            if (device.getCaBandsCombinations().get(i).getSupportedBCS() != null) {
                                temp4 = temp4.concat("    BCS:").concat(Utility.bcsParser(device.getCaBandsCombinations().get(i).getSupportedBCS()));
                            }
                            temp4 = temp4.concat("\t\t\tMagnitude: " + String.valueOf(device.getCaBandsCombinations().get(i).getCombinationMagnitude()));
                            temp4 = temp4.concat("\t\t\tCC Quantity DL:" + String.valueOf(device.getCaBandsCombinations().get(i).getCcQuantityDL()));
                            temp4 = temp4.concat("\t\t\tCC Quantity UL:" + String.valueOf(device.getCaBandsCombinations().get(i).getCcQuantityUL()));
                        }
                    }
                    temp4 = temp4.concat("\n");
                }
                System.out.println("Downlink CA Combinations(v10i0) and BCS:");
                System.out.println(temp4);
                System.out.println("\n");
                // Uplink CA COmbinations
                temp4 = "";
                for (int i = 0; i < device.getCaBandsCombinations().size(); i++) {
                    if (device.getCaBandsCombinations().get(i).getULSingleCombination() != null) {
                        if ( (device.getCaBandsCombinations().get(i).getULSingleCombination().size() != 0)){
                            temp4 = temp4.concat("("+Integer.toString(i)+")").concat("CA_");
                        } else {
                            temp4 = temp4.concat("(");
                            temp4 = temp4.concat(Integer.toString(i)).concat(")");
                        }
                    }
                    //temp4 = temp4.concat("("+Integer.toString(i)+")").concat("CA_");
                    for (int j = 0; j < device.getCaBandsCombinations().get(i).getULSingleCombination().size(); j++) {
                        temp4 = temp4.concat(Integer.toString(device.getCaBandsCombinations().get(i).getULSingleCombination().get(j).getBandID())).concat("");
                        temp4 = temp4.concat(device.getCaBandsCombinations().get(i).getULSingleCombination().get(j).getComponentCarrierClass()).concat("");
                        if (device.getCaBandsCombinations().get(i).getULSingleCombination().get(j).getSupportedLayersTM34() == 4) {
                            temp4 = temp4.concat("(4x4)");
                        } else if (device.getCaBandsCombinations().get(i).getULSingleCombination().get(j).getSupportedLayersTM34() == 2){
                            temp4 = temp4.concat("(2x2)");
                        }
                        if ((device.getCaBandsCombinations().get(i).getULSingleCombination().size()>1)
                                && ( j != device.getCaBandsCombinations().get(i).getULSingleCombination().size()- 1 ) ) {
                            temp4 = temp4.concat("-");
                        } else if ( j == device.getCaBandsCombinations().get(i).getULSingleCombination().size() -1) {
                            if ( device.getCaBandsCombinations().get(i).getULSingleCombination().size() > 1) {
                                temp4 = temp4.concat("         CA_UL_SUPPORTED");
                            } else {
                                //temp4 = temp4.concat("         CA_UL_NOT_SUPPORTED");
                            }
                        } else {
                            if (device.getCaBandsCombinations().get(i).getSupportedBCS() != null) {
                                temp4 = temp4.concat("    BCS:").concat(Utility.bcsParser(device.getCaBandsCombinations().get(i).getSupportedBCS()));
                            }
                        }
                    }
                    temp4 = temp4.concat("\n");
                }
                System.out.println("Uplink CA Combinations(v10i0):");
                System.out.println(temp4);
            }
        } else {
            System.out.println("SupportDLCombination-v1020: false");
            System.out.println("SupportDLCombination-v10i0: false");
        }

        System.out.println();


        // Check if device supports v1310
        if (device.ca1310support) {
            // RELEASE 13
            // Downlink CA Combinations
            //BCS=0,1,2
            temp4 = "";
            for (int i = 0; i < device.getCaV1310BandCombinations().size(); i++) {
                int numberOfcomponentsDL = 0;
                temp4 = temp4.concat("(" + Integer.toString(i) + ")").concat("CA_");
                for (int j = 0; j < device.getCaV1310BandCombinations().get(i).getDLSingleCombination().size(); j++) {
                    temp4 = temp4.concat(Integer.toString(device.getCaV1310BandCombinations().get(i).getDLSingleCombination().get(j).getBandID())).concat("");
                    temp4 = temp4.concat(device.getCaV1310BandCombinations().get(i).getDLSingleCombination().get(j).getComponentCarrierClass()).concat("");
                    if (device.getCaV1310BandCombinations().get(i).getDLSingleCombination().get(j).getSupportedLayers() == 4) {
                        temp4 = temp4.concat("(4x4)");
                    } else if (device.getCaV1310BandCombinations().get(i).getDLSingleCombination().get(j).getSupportedLayers() == 2) {
                        temp4 = temp4.concat("(2x2)");
                    }
                    if ((device.getCaV1310BandCombinations().get(i).getDLSingleCombination().size() > 1)
                            && (j != device.getCaV1310BandCombinations().get(i).getDLSingleCombination().size() - 1)) {
                        temp4 = temp4.concat("-");
                        numberOfcomponentsDL++;
                    } else {
                        if (device.getCaV1310BandCombinations().get(i).getSupportedBCS() != null) {
                            temp4 = temp4.concat("    BCS:").concat(Utility.bcsParser(device.getCaV1310BandCombinations().get(i).getSupportedBCS()));
                            temp4 = temp4.concat("\t\t\tCC Quantity DL:" + String.valueOf(device.getCaV1310BandCombinations().get(i).getCcQuantityDL()));
                            temp4 = temp4.concat("\t\t\tCC Quantity UL:" + String.valueOf(device.getCaV1310BandCombinations().get(i).getCcQuantityUL()));
                        }
                        temp4 = temp4.concat("\t\t\tMagnitude: " + String.valueOf(device.getCaV1310BandCombinations().get(i).getCombinationMagnitude()));
                        temp4 = temp4.concat("\t\t\tCC Quantity DL:" + String.valueOf(device.getCaV1310BandCombinations().get(i).getCcQuantityDL()));
                        temp4 = temp4.concat("\t\t\tCC Quantity UL:" + String.valueOf(device.getCaV1310BandCombinations().get(i).getCcQuantityUL()));
                    }


                }
                device.getCaV1310BandCombinations().get(i).setNumberOfcomponentsDL(numberOfcomponentsDL + 1);
                if(device.getCaV1310BandCombinations().get(i).isDifferentFallbackSupported()) {
                    temp4 = temp4.concat("\t\t\t differentFallbackCombination support: " + device.getCaV1310BandCombinations().get(i).isDifferentFallbackSupported());
                }
                temp4 = temp4.concat("\n");


            }
            System.out.println("Downlink CA Combinations(1310)");
            System.out.println(temp4);
            System.out.println("\n");
            // Uplink CA COmbinations
            temp4 = "";
            System.out.println(device.getCaV1310BandCombinations().size());
            for (int i = 0; i < device.getCaV1310BandCombinations().size(); i++) {
                int numberOfComponentsUL = 0;
                // Manage print of empty ul combinations
                if (device.getCaV1310BandCombinations().get(i).getULSingleCombination() != null) {
                    if ((device.getCaV1310BandCombinations().get(i).getULSingleCombination().size() != 0)) {
                        temp4 = temp4.concat("(" + Integer.toString(i) + ")").concat("CA_");
                    } else {
                        temp4 = temp4.concat("(");
                        temp4 = temp4.concat(Integer.toString(i)).concat(")");
                    }
                }
                //temp4 = temp4.concat("("+Integer.toString(i)+")").concat("CA_");
                for (int j = 0; j < device.getCaV1310BandCombinations().get(i).getULSingleCombination().size(); j++) {
                    temp4 = temp4.concat(Integer.toString(device.getCaV1310BandCombinations().get(i).getULSingleCombination().get(j).getBandID())).concat("");
                    temp4 = temp4.concat(device.getCaV1310BandCombinations().get(i).getULSingleCombination().get(j).getComponentCarrierClass()).concat("");
                    if (device.getCaV1310BandCombinations().get(i).getULSingleCombination().get(j).getSupportedLayers() == 2) {
                        temp4 = temp4.concat("(2x2)");
                    } else if (device.getCaV1310BandCombinations().get(i).getULSingleCombination().get(j).getSupportedLayers() == 1) {
                        temp4 = temp4.concat("(1x1)");
                    }
                    if (device.getCaV1310BandCombinations().get(i).getULSingleCombination().get(j).getSupport256QAMUL() == true) {
                        temp4 = temp4.concat(" - (256 QAM Uplink Support: Yes)");
                    } else temp4 = temp4.concat(" - (256 QAM Uplink Support: No)");
                    if ((device.getCaV1310BandCombinations().get(i).getULSingleCombination().size() > 1) && (j != device.getCaV1310BandCombinations().get(i).getULSingleCombination().size() - 1)) {
                        temp4 = temp4.concat("-");
                        numberOfComponentsUL++;
                    } else if (j == device.getCaV1310BandCombinations().get(i).getULSingleCombination().size() - 1) {
                        if (device.getCaV1310BandCombinations().get(i).getULSingleCombination().size() > 1) {
                            temp4 = temp4.concat("         CA_UL_SUPPORTED");
                        } else {
                            //temp4 = temp4.concat("         CA_UL_NOT_SUPPORTED");
                        }
                    } else {
                        if (device.getCaV1310BandCombinations().get(i).getSupportedBCS() != null) {
                            temp4 = temp4.concat("    BCS:").concat(Utility.bcsParser(device.getCaV1310BandCombinations().get(i).getSupportedBCS()));
                        }
                    }
                }
                device.getCaV1310BandCombinations().get(i).setNumberOfcomponentsUL(numberOfComponentsUL + 1);
                temp4 = temp4.concat("\n");
            }
            System.out.println("Uplink CA Combinations(v1310):");
            System.out.println(temp4);
            System.out.println("\n");


        }

        if (device.mrdcBandCombinations != null && device.mrdcBandCombinations.size() > 0) {
            System.out.println("EN-DC Band Combinations");
            int n = 0;
            for(MRDCBandCombination mrdcBandComb : device.mrdcBandCombinations) {
                System.out.println("(" + n++ + ")");
                System.out.print("DOWNLINK\t");
                System.out.print("4G: | ");
                for(MRDCBandEutra mrdcBandEutra : mrdcBandComb.getDownlink4G())
                    System.out.print("" + mrdcBandEutra.getBandID() + mrdcBandEutra.getBandClass() + "(" + mrdcBandEutra.getBandLayer() +") | ");

                System.out.print("\t5G: | ");
                for(MRDCBandNR mrdcBandNR : mrdcBandComb.getDownlink5G())
                    System.out.print("" + mrdcBandNR.getBandID() + mrdcBandNR.getBandClass() + "(" + mrdcBandNR.getBandLayer() + ") "
                            + mrdcBandNR.getBandModulation() + " " + mrdcBandNR.getBandSCS() + " " + mrdcBandNR.getBandWidth() + " "
                            + mrdcBandNR.getChannelBW90Mhz()+ " [ " + mrdcBandNR.getDownlinkSetNR() + " : " +
                            mrdcBandNR.isAdditionalDRMSsupportedByFeatureset() + " ] | ");

                /*
                if(mrdcBandComb.getFeatureSet5G() != null) {

                    System.out.println(mrdcBandComb.getFeatureSet5G().getfSetID() + " " + mrdcBandComb.getFeatureSet5G().getAdditionalDMRSAltDLsupported());
                }

                 */
                System.out.println();
                System.out.print("UPLINK\t\t");
                System.out.print("4G: | ");
                for(MRDCBandEutra mrdcBandEutra : mrdcBandComb.getUplink4G())
                    System.out.print("" + mrdcBandEutra.getBandID() + mrdcBandEutra.getBandClass() + "(" + mrdcBandEutra.getBandLayer() +") | ");

                System.out.print("\t5G: | ");
                for(MRDCBandNR mrdcBandNR : mrdcBandComb.getUplink5G())
                    System.out.print("" + mrdcBandNR.getBandID() + mrdcBandNR.getBandClass() + "(" + mrdcBandNR.getBandLayer() + ") " + mrdcBandNR.getBandModulation() + " " + mrdcBandNR.getBandSCS() + " " + mrdcBandNR.getBandWidth() + " " + mrdcBandNR.getChannelBW90Mhz() + " | ");
                System.out.println();
                System.out.println("---------------------------------------");
            }
        }
        System.out.println("\n");
        counter = 0;
        System.out.println("\nSupported Codecs GSM:\n");
        HashMap<String,Integer> gsmMap = device.getGsmCap().getCodecs();
        //gsmMap.forEach((key, value) -> System.out.println(key + " : " + value));
        for (String keyName: gsmMap.keySet()){
            String key = keyName.toString();
            String value = gsmMap.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                counter++;
            }
        }
        if (counter==0 ) System.out.println(Utility.NOT_APPLICABLE);
        counter = 0;

        System.out.println("\nSupported Codecs UMTS:\n");
        HashMap<String,Integer> umtsMap = device.getUmtsCap().getCodecs();
        //umtsMap.forEach((key, value) -> System.out.println(key + " : " + value));
        for (String keyName: umtsMap.keySet()){
            String key = keyName.toString();
            String value = umtsMap.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                counter++;
            }
        }
        if (counter==0 ) System.out.println(Utility.NOT_APPLICABLE);
        counter = 0;

        System.out.println("\nSupported GSM Ciphering Algorithms:\n");
        HashMap<String,Integer> gsmCiphAlg = device.getGsmCap().getEncrAlg();
        //ciphAlg.forEach((key, value) -> System.out.println(key + " : " + value));
        for (String keyName: gsmCiphAlg.keySet()){
            String key = keyName.toString();
            String value = gsmCiphAlg.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                counter++;
            }
        }
        if (counter==0 ) System.out.println(Utility.NOT_APPLICABLE);
        counter = 0;

        System.out.println();
        // Print Supported Bands GSM
        temp = "";
        temp4 = "";
        for (int j = 1; j < device.getGsmCap().getSupportedBands().size() + 1; j++) {
            temp = temp.concat(device.getGsmCap().getSupportedBands().get(j-1).getGsmBandName());
            temp4 = temp4.concat(" ").concat(temp);
            temp = " ";
        }
        System.out.println("2G - Supported Bands:  " + temp4);
        System.out.println("\n");

        // Print Supported Bands UMTS
        temp = "";
        temp4 = "";
        //check to avoid NullPointerException - v8.0.1
        if(device.isUmtsSupport()) {
            for (int j = 1; j < device.getUmtsCap().getSupportedBands().size() + 1; j++) {
                temp = temp.concat(device.getUmtsCap().getSupportedBands().get(j - 1).getUmtsBandName());
                temp4 = temp4.concat(" ").concat(temp);
                temp = " ";
            }
        }
            System.out.println("3G - Supported Bands:  " + temp4);
            System.out.println("\n");

        temp = "";
        temp4 = "";
        if (device.getRequestedBands() == null) {
            temp4 = Utility.NOT_APPLICABLE;
        } else if (device.getRequestedBands().size() == 0) {
            temp4 = "-";
        } else{
            for (int j = 1 ; j < device.getRequestedBands().size() + 1; j++) {
                temp = temp.concat(String.valueOf(device.getRequestedBands().get(j-1).getBandID()));
                temp4 = temp4.concat(" ").concat(temp);
                temp = " ";
            }
        }
        System.out.println("4G - Requested Bands:  " + temp4);
        System.out.println("\n");

        // 5G
        temp = "";
        temp4 = "";
        if (device.getNrBands() == null) {
            temp4 = Utility.NOT_APPLICABLE;
        } else if (device.getNrBands().size() == 0) {
            temp4 = "-";
        } else{
            for (int j = 1 ; j < device.getNrBands().size() + 1; j++) {
                temp = temp.concat(String.valueOf(device.getNrBands().get(j-1).getBandID()));
                temp4 = temp4.concat(" ").concat(temp);
                temp = " ";
            }
        }
        System.out.println("5G - Supported Bands:  " + temp4);
        System.out.println("\n");
        temp="";
        temp4="";
        if (device.getAppliedFreqBandList() == null) {
            temp4 = Utility.NOT_APPLICABLE;
        } else if (device.getAppliedFreqBandList().size() == 0) {
            temp4 = "-";
        } else{
            for (int j = 1 ; j < device.getAppliedFreqBandList().size() + 1; j++) {
                temp = temp.concat(String.valueOf(device.getAppliedFreqBandList().get(j-1).getBandID()));
                temp4 = temp4.concat(" ").concat(temp);
                temp = " ";
            }
        }
        System.out.println("5G - Applied Frequency Band List:  " + temp4);
        System.out.println("\n");
        System.out.println("5G - Support of ChannelBW 1530 [DL]:  " + device.getchannelBW_1530DL_support());
        System.out.println("5G - Support of ChannelBW 1530 [UL]:  " + device.getchannelBW_1530UL_support());
        System.out.println("\n");
        System.out.println("5G -Support of RateMatchingResrcSetSemiStatic: " +device.isRateMatchingResrcSetSemiStatic());
        System.out.println("\n");
        System.out.println("5G -Support of AdditionalDMRS DL Alt: " +device.isAdditionalDMRSDLAlt());
        System.out.println("\n");
        System.out.println("5G -Support of rateMatchingCtrlResrcSetDynamic: " +device.isRateMatchingCtrlResrcSetDynamic());
        System.out.println("\n");
        System.out.println("5G -Support of rateMatchingResrcSetDynamic: " +device.isRateMatchingResrcSetDynamic());
        System.out.println("\n");
        System.out.println("5G -Support of pdcchMonitoringSingleOccasion: " +device.isPdcchMonitoringSingleOccasion());
        System.out.println("\n");


        System.out.println("4G - Support of Requested CCsDL: " + device.getRequestedCCsDL());
        System.out.println("4G - Support of Requested CCsUL: " + device.getRequestedCCsUL());
        System.out.println("\n");
        System.out.println("4G - Support of Maximum CCs Retrieval: " + device.getMaximumCCsRetrieval());
        System.out.println("\n");
        System.out.println("4G - Support of skipFallBackCombinations: " + device.getSkipFallBackComb());
        System.out.println("\n");
        System.out.println("4G - Support of skipFallBackCombinationsRequested: " + device.getSkipFallBackCombReq());

        System.out.println();
        System.out.println("\nSupported UMTS Ciphering Algorithms:\n");
        HashMap<String,Integer> ciphAlg = device.getUmtsCap().getEncrAlg();
        //ciphAlg.forEach((key, value) -> System.out.println(key + " : " + value));
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

        System.out.println("\nSupported UMTS Integrity Algorithms:\n");
        HashMap<String,Integer> intgrAlg = device.getUmtsCap().getIntegrAlg();
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

        System.out.println("\nSupported LTE Integrity Algorithms:\n");
        HashMap<String,Integer> intgrAlgLTE = device.getLteCap().getIntegrAlg();
        //ciphAlg.forEach((key, value) -> System.out.println(key + " : " + value));
        for (String keyName: intgrAlgLTE.keySet()){
            String key = keyName.toString();
            String value = intgrAlgLTE.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                counter++;
            }
        }
        if (counter==0 ) System.out.println(Utility.NOT_APPLICABLE);

        System.out.println("\nSupported LTE Ciphering Algorithms:\n");
        HashMap<String,Integer> ciphAlgLTE = device.getLteCap().getEncrAlg();
        //ciphAlg.forEach((key, value) -> System.out.println(key + " : " + value));
        for (String keyName: ciphAlgLTE.keySet()){
            String key = keyName.toString();
            String value = ciphAlgLTE.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                counter++;
            }
        }
        if (counter==0 ) System.out.println(Utility.NOT_APPLICABLE);

        /*System.out.println("\nSupported ROHC Profiles:\n");
        HashMap<String,Integer> rohcProfiles = device.getLteCap().getSupportedRohcProfiles();
        //ciphAlg.forEach((key, value) -> System.out.println(key + " : " + value));
        for (String keyName: rohcProfiles.keySet()){
            String key = keyName.toString();
            String value = rohcProfiles.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                counter++;
            }
        }
        if (counter==0 ) System.out.println(Utility.NOT_APPLICABLE);
        counter = 0;
        */
        System.out.println("\nSupported ROHC Profiles_r15:\n");
        HashMap<String,Integer> rohcProfiles_r15 = device.getLteCap().getSupportedRohcProfiles_r15();
        //ciphAlg.forEach((key, value) -> System.out.println(key + " : " + value));
        for (String keyName: rohcProfiles_r15.keySet()){
            String key = keyName.toString();
            String value = rohcProfiles_r15.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                counter++;
            }
        }
        if (counter==0 ) System.out.println(Utility.NOT_APPLICABLE);
        counter = 0;
        System.out.println("\n");
        System.out.println("2G Support: " + device.isGsmSupport());
        System.out.println("3G Support: " + device.isUmtsSupport());
        System.out.println("5G support: "+device.is5Gsupport());

        System.out.println("Add R11 Support: " + String.valueOf(device.isAddR11Support()));


    }

    /**
     * Private support method to print volte dut stats
     * @param d device under testing
     */
    public void printVoLTEDUTCap(Mobile d) {
        // IF VoLTE Device print related fields
        if (Utility.IS_VOLTE) {
            System.out.println();
            System.out.println("VoLTE Device");
            System.out.println();
            System.out.println("P-Early Media Support:  " + d.getLteCap().getVolteCap().getpEarlyMediaSupport());
            System.out.println();
            System.out.println("P-Security Client:");
            if (!(d.getLteCap().getVolteCap().getSupportedAlgorithms() == null)||(d.getLteCap().getVolteCap().getSupportedAlgorithms().size()==0)) {
                for (int i = 0 ; i < d.getLteCap().getVolteCap().getSupportedAlgorithms().size(); i++) {
                    System.out.println("ALG: " + d.getLteCap().getVolteCap().getSupportedAlgorithms().get(i).getAlg() + "  EALG: " +
                            d.getLteCap().getVolteCap().getSupportedAlgorithms().get(i).getEalg());
                }
            } else {
                System.out.println(Utility.NOT_SUPPORTED);
            }

            System.out.println();
            System.out.println("Supported VoLTE Codecs:");
            for (int s = 0 ; s < d.getLteCap().getVolteCap().getSupportedCodecs().size(); s++) {
                System.out.println("Codec #" + String.valueOf(s+1));
                System.out.println("MIME TYPE: " + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getMimeType());
                System.out.println("Sample Rate: " + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getSampleRate());
                System.out.println("Media Format: " + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getMediaFormat());
                if (d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getMimeType().equals(Utility.VOLTE_EVS)) {

                        System.out.println("bw: " + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBw());
                    try {
                        System.out.println("br: " + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBr()[0] + "-"
                                + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBr()[1]);
                    } catch (Exception e) {
                        System.out.println("br: " + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBr()[0]);
                        System.out.println("EVS Declaration not well-formed");
                    }
                } else if (d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getMimeType().equals(Utility.VOLTE_AMR_WB)) {
                    System.out.println("bw: " + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBw());
                    System.out.println("br: " + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBr()[0] + "-"
                            + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBr()[1]);
                } else if (d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getMimeType().equals(Utility.VOLTE_AMR)) {
                    System.out.println("bw: " + d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBw());
                    String tempo = "br: ";
                    for (int q = 0; q < d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBr().length; q++) {
                        tempo = tempo.concat(String.valueOf(d.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBr()[q]));
                        tempo = tempo.concat("-");
                    }
                    tempo = tempo.substring(0,tempo.length()-1);
                    System.out.println(tempo);
                }
                System.out.println();
            }
            System.out.println();
            System.out.println("SIP USER AGENT: "+d.getLteCap().getVolteCap().getUserAgent());
            System.out.println();
            System.out.println("Supported SRVCC(s): ");
            for (int y = 0 ; y < d.getLteCap().getVolteCap().getSupportedSrvccs().size(); y++) {
                System.out.println(d.getLteCap().getVolteCap().getSupportedSrvccs().get(y));
            }
            if (d.getLteCap().getVolteCap().getSupportedSrvccs().size() == 0) {
                System.out.println("Not Supported.");
            }
            System.out.println();
        }
    }

    /**
     * Method to format retrieved info from DUT as a string to be output on a file
     * @param device the instance of the device
     * @return the string containing the info of the device
     */
    public static String printCap(Mobile device){
        String output = "General Info: ";
        output = output.concat("\n4G Support: true").concat("\n3G Support: " + device.isUmtsSupport());
        output = output.concat("\n2G Support: " + device.isGsmSupport()).concat("\nIMEI SV: " + device.getImeiSV());
        output = output.concat("\n\n\n2G - Basic Info".concat("\nUCS2 Support: " + device.getGsmCap().getUcs2Support()));
        output = output.concat("\nSS Screening indicator support: " + device.getGsmCap().getSsScreeningIndicator());
        output = output.concat("\nPFC Feature support: " + device.getGsmCap().isPfcFeatureSupport())
                .concat("\nLCS VA capability support" + device.getGsmCap().isLcsSupport());
        output = output.concat("\nPS inter-RAT HO from GERAN to UTRAN Iu mode capability support" + device.getGsmCap().isInterRatPSHOtoUTRANIu())
                .concat("\nPS inter-RAT HO from GERAN to E-UTRAN S1 mode capability support " + device.getGsmCap().isInterRatPSHOtoUTRANS1());
        output = output.concat("\nEMM Combined procedures capability support" + device.getGsmCap().isEmmProceduresSupport())
                .concat("\nISR Support: " + device.getGsmCap().isIsrSupport());
        output = output.concat("\nEPC capability support" + device.getGsmCap().isEpcSupport())
                .concat("\nNF Capability support:" + device.getGsmCap().isNfcSupport());
        output = output.concat("\nES IND support" + device.getGsmCap().isEsIndSupport())
                .concat("\nRF Power Capability1: " + device.getGsmCap().getRfPowerCapability1());
        output = output.concat("\nRF Power Capability2: " + device.getGsmCap().getRfPowerCapability2())
                .concat("\nPS capability support" + device.getGsmCap().isPsCapability());
        output = output.concat("\nDTM GPRS Multi Slot Class " + device.getGsmCap().getGprsMultiSlotClass())
                .concat("\nDTM EGPRS Multi Slot Class:" + device.getGsmCap().getEgprsMultiSlotClass());
        output = output.concat("\nSingle slot DTM support:"+ device.getGsmCap().isSingleSlotSupport())
                .concat("\n8-PSK RF Power Capability 1:" + device.getGsmCap().getPsk8RFPowerCapability1());
        output = output.concat("\n8-PSK RF Power Capability 2:" + device.getGsmCap().getPsk8RFPowerCapability2())
                .concat("\nDownlink Advanced Receiver Performance: " + device.getGsmCap().getAdvancedReceiverPerformance());
        output = output.concat("\nDTM Enhancements Capability support:" + device.getGsmCap().isDtmSupport())
                .concat("\nPriority-based reselection support:" + device.getGsmCap().isPriorityBasedReselectionSupport());
        output = output.concat("\nGMSK Multislot Power Profile: " + device.getGsmCap().getGmskMultislotPowerProfile())
                .concat("\n8-PSK Multislot Power Profile:" + device.getGsmCap().getPsk8MultislotPowerProfile());
        output = output.concat("\n\n\n3G - Basic Info").concat("\nUMTS FDD Radio Access Cap. Support: " + device.getUmtsCap().isFddRadioAccessTechnCap());
        output = output.concat("\nUMTS MCPS 384 TDD Support: " + device.getUmtsCap().isMcps384TDDRatCap())
                .concat("\nUMTS MCPS 128 TDD Support: " +device.getUmtsCap().isMcps128TDDRatCap());
        output = output.concat("\nCDMA 2000 Support: " + device.getUmtsCap().isCdma2000Support())
                .concat("\n\n\n4G - Basic Info");
        output = output.concat("\nAS Release:  rel" + device.getAccessStratumRelease())
                .concat("\nEIT: " + device.getLteCap().getEIT());
        output = output.concat("\nUE Usage Settings: " + device.getUeUsageSettings())
                .concat("\nVoice Domain Preference: " + Utility.voiceDomainPreferenceDecoder(device.getVoiceDomainPreference()));
        output = output.concat("\nAdditional Update Type: " + device.getAdditionalUpdateType())
                .concat("\nAttach Type: " + Utility.attachTypeDecoder(device.getAttachType()));
        output = output.concat("\nSRVCC TO GERAN/UTRAN CAPABILITY: " + device.isSRVCCToGERANUTRANCapability());
        if (device.getCategory() != 0) {
            output = output.concat("\nCategory:  " + device.getCategory() + " As String: " + device.getCategoriesAsString());
        }
        if ((device.getCategoryUL() != 0) && (device.getCategoryDL() != 0)) {
            output = output.concat("\nCategory DL:  " + device.getCategoryDL() + " As String: " + device.getCategoryDLAsString())
                    .concat("\nCategory UL:  " + device.getCategoryUL() + " As String: " + device.getCategoryULAsString())
                    .concat("\nCategory As String: " + device.getCategoriesAsString());
        }
        output = output.concat("\nSupported ROHC Profiles:\n");
        HashMap<String,Integer> rohcProfiles = device.getLteCap().getSupportedRohcProfiles_r15();
        int counter = 0;
        for (String keyName: rohcProfiles.keySet()){
            String key = keyName.toString();
            String value = rohcProfiles.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                output = output.concat(key).concat(",");
                counter++;
            }
        }
        if (counter==0 ) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            output = output.substring(0,output.length()-1);
        }
        counter = 0;

        output = output.concat("\nTx Antenna Selection Support: " + device.getLteCap().isTxAntennaSelectionSupport())
                .concat("\nTx Antenna Selection Support: " + device.getLteCap().isTxAntennaSelectionSupport());
        output = output.concat("\nUe SpecificRefSigsSupported: " + device.getLteCap().isUeSpecificRefsSigsSupport())
                .concat("\nINTER RAT PS HO To GERAN Support: " + device.isInterRatPSHOToGERAN());
        output = output.concat("\neRedirection to GERAN: "+ device.getLteCap().iseRedirectionGERAN())
                .concat("\neRedirection to UTRAN: " + device.getLteCap().iseRedirectionUTRAN());
        output = output.concat("\nRach-Report-R9: " + device.getLteCap().isRachReportSON())
                .concat("\n\n\n2G - Band Info");
        String temp = "";
        String temp2 = "";
        for (int j = 1; j < device.getGsmCap().getSupportedBands().size() + 1; j++) {
            temp = temp.concat(device.getGsmCap().getSupportedBands().get(j-1).getGsmBandName());
            temp2 = temp2.concat(" ").concat(temp);
            temp = " ";
        }
        output = output.concat("\n2G - Supported Bands:  " + temp2).concat("\n\n\n3G - Band Info");
        temp = "";
        temp2 = "";
        if(device.getLteCap().iseRedirectionUTRAN()) {
            for (int j = 1; j < device.getUmtsCap().getSupportedBands().size() + 1; j++) {
                temp = temp.concat(device.getUmtsCap().getSupportedBands().get(j - 1).getUmtsBandName());
                temp2 = temp2.concat(" ").concat(temp);
                temp = " ";
            }
        }
        output = output.concat("\n3G - Supported Bands:  " + temp2).concat("\n\n\n4G - Band Info");

        if (device.isCa1020Support()) {
            output = output.concat("\nSupportBandCombination-v1020: TRUE");
        }
        if (device.isCa10i0Support()){
            output = output.concat("\nSupportBandCombination-v10i0: TRUE");
        }
        output = output.concat("\nMaximum number of supported layers DL:" + device.getMaximumSupportedLayersDL())
                .concat("\nMaximum number of supported layers UL:" + device.getMaximumSupportedLayersUL())
                .concat("\nCA UL Support: " + device.isCaULSupport());

        output = output.concat("\nSupported bands: " + GUIUtility.supportedLTEBandsUIFormatter(device.getLteCap().getSupportedBands()));
        output = output.concat("\n256QAM DL Support: " + GUIUtility.supportedDLModulationBandsUIFormatter(device.getLteCap().getSupportedBands()))
                .concat("\n64QAM UL Support: " + GUIUtility.supportedULModulationBandsUIFormatter(device.getLteCap().getSupportedBands()))
                .concat("\nFrequency Retrieval support: ");
        temp = "";
        temp2 = "";
        if (device.getRequestedBands() == null) {
            output = output.concat("false");
        } else {
            if (device.getRequestedBands().size() == 0) {
                output = output.concat("true");
                temp2 = "-";
            } else {
                for (int j = 1 ; j < device.getRequestedBands().size() + 1; j++) {
                    temp = temp.concat(String.valueOf(device.getRequestedBands().get(j-1).getBandID()));
                    temp2 = temp2.concat(" ").concat(temp);
                    temp = " ";
                }
            }
            output = output.concat("\nRequested Bands: "+ temp2);
        }

        output = output.concat("\n\nSupported GSM Encryption Algorithms:");
        HashMap<String,Integer> gsmEncrAlg = device.getGsmCap().getEncrAlg();
        for (String keyName: gsmEncrAlg.keySet()){
            String key = keyName.toString();
            String value = gsmEncrAlg.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                output = output.concat(key).concat(",");
                counter++;
            }
        }
        if (counter==0 ) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            output = output.substring(0,output.length()-1);
        }
        counter = 0;

        output = output.concat("\n\nSupported UMTS Encryption Algorithms:");
        HashMap<String,Integer> umtsEncrAlg = device.getUmtsCap().getEncrAlg();
        for (String keyName: umtsEncrAlg.keySet()){
            String key = keyName.toString();
            String value = umtsEncrAlg.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                output = output.concat(key).concat(",");
                counter++;
            }
        }
        if (counter==0 ) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            output = output.substring(0,output.length()-1);
        }
        counter = 0;

        output = output.concat("\n\nSupported LTE Encryption Algorithms:");
        HashMap<String,Integer> lteEncrAlg = device.getLteCap().getEncrAlg();
        for (String keyName: lteEncrAlg.keySet()){
            String key = keyName.toString();
            String value = lteEncrAlg.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                output = output.concat(key).concat(",");
                counter++;
            }
        }
        if (counter==0 ) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            output = output.substring(0,output.length()-1);
        }
        counter = 0;

        output = output.concat("\n\nSupported UMTS Integrity Algorithms:");
        HashMap<String,Integer> intgrAlg = device.getUmtsCap().getIntegrAlg();
        for (String keyName: intgrAlg.keySet()){
            String key = keyName.toString();
            String value = intgrAlg.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                output = output.concat(key).concat(",");
                counter++;
            }
        }
        if (counter==0 ){
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            output = output.substring(0,output.length()-1);
        }

        output = output.concat("\n\nSupported LTE Integrity Algorithms:");
        HashMap<String,Integer> intgrAlgLTE = device.getLteCap().getIntegrAlg();
        for (String keyName: intgrAlgLTE.keySet()){
            String key = keyName.toString();
            String value = intgrAlgLTE.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                output = output.concat(key).concat(",");
                counter++;
            }
        }
        if (counter==0 ) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            output = output.substring(0,output.length()-1);
        }
        output = output.concat("\n\nSupported Codecs GSM:\n");
        HashMap<String,Integer> gsmMap = device.getGsmCap().getCodecs();
        for (String keyName: gsmMap.keySet()){
            String key = keyName.toString();
            String value = gsmMap.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                System.out.println(key);
                output = output.concat(key).concat(",");
                counter++;
            }
        }
        if (counter==0 ) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            output = output.substring(0,output.length()-1);
        }
        counter = 0;

        output = output.concat("\n\nSupported Codecs UMTS:\n");
        HashMap<String,Integer> umtsMap = device.getUmtsCap().getCodecs();
        for (String keyName: umtsMap.keySet()){
            String key = keyName.toString();
            String value = umtsMap.get(key).toString();
            if (Integer.parseInt(value) == 1) {
                output = output.concat(key).concat(",");
                counter++;
            }
        }
        if (counter==0 ) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            output = output.substring(0,output.length()-1);
        }
        counter = 0;

        output = output.concat("\n\nFGIs:").concat("\nfgi (a.k.a fgiRel8):");
        temp = "";
        int[] temp22;
        if (device.getLteCap().getFeatureGroupIndicators() != null) {
            for (int j = 1 ; j < device.getLteCap().getFeatureGroupIndicators().length + 1; j++) {
                temp22 = device.getLteCap().getFeatureGroupIndicators();
                output = output.concat("").concat(Integer.toString(temp22[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }

        output = output.concat("\nfgiRel9Add-R9: ");
        int[] temp3;
        if (device.getLteCap().getFeatureGroupIndicatorsAddR9() != null) {
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsAddR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsAddR9();
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
        output = output.concat("\nfgiRel9Addr9-r9 (FDD):  ");
        if (device.getLteCap().getFeatureGroupIndicatorsFDDAddR9() != null) {
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsFDDAddR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsFDDAddR9();
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
        output = output.concat("\nfgiRel99Add-r9 (TDD):  ");
        if (device.getLteCap().getFeatureGroupIndicatorsTDDAddR9() != null) {
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsTDDAddR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsTDDAddR9();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }

        //R9
        output = output.concat("\nfgi-r9 (FDD):  ");
        if (device.getLteCap().getFeatureGroupIndicatorsFDDR9()!= null) {
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsFDDR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsFDDR9();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }


        // R9
        output = output.concat("\nfgi-r9 (TDD):  ");
        if (device.getLteCap().getFeatureGroupIndicatorsTDDR9() != null) {
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsTDDR9().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsTDDR9();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }

        output = output.concat("\nfgiRel10-r10:  ");
        if (device.getLteCap().getFeatureGroupIndicatorsR10() == null) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsR10().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsR10();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        }

        // FGI rel10 FDD TDD
        output = output.concat("\nfgiRel10-r10 (FDD):  ");
        if (device.getLteCap().getFeatureGroupIndicatorsR10FDD() == null) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsR10FDD().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsR10FDD();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        }

        //TDD
        output = output.concat("\nfgiRel10-r10 (TDD):  ");
        if (device.getLteCap().getFeatureGroupIndicatorsR10TDD() == null) {
            output = output.concat(Utility.NOT_APPLICABLE);
        } else {
            for (int j = 1; j < device.getLteCap().getFeatureGroupIndicatorsR10TDD().length + 1; j++) {
                temp3 = device.getLteCap().getFeatureGroupIndicatorsR10TDD();
                output = output.concat("").concat(Integer.toString(temp3[j-1]));
                if (( j % 8 == 0)) {
                    output = output.concat("\t");
                }
            }
        }
        return output;
    }
    
    /**
     * Method to format retrieved EN-DC Band Combinations info from DUT as a string to be output on excel file
     * @param device the instance of the device
     * @return the string containing the info of the device
     */
    public static XSSFWorkbook printENDCBandCap_Excel(Mobile device){
    	XSSFWorkbook workbook = new XSSFWorkbook();
    	Row row = null;
    	Cell cell = null;
    	String[] intestStart = {"Index","EN-DC Configuration"};
    	String[] intest4G = {"Band","Class","Layer"};
    	String[] intest5G = {"Band","Class","Layer","Modulation","SCS","BandWidth","ChannelBW90Mhz"};
    	int maxNBandsDL4G = 5;
    	int maxNBandsUL4G = 2;
    	int maxNBandsDL5G = 2;
    	int maxNBandsUL5G = 1;
    	int idx4G = intestStart.length;
    	int idx5GDL = intestStart.length + (intest4G.length*maxNBandsDL4G);
    	int idx5GUL = intestStart.length + (intest4G.length*maxNBandsUL4G);
    	
    	String type = "";
    	for(int i=0; i<2; i++) {
    		if(i==0) 
    			type = "DL";
    		else 
    			type = "UL";
    		
	    	int n4G = type.equalsIgnoreCase("DL") ? maxNBandsDL4G : maxNBandsUL4G;
	    	int n5G = type.equalsIgnoreCase("DL") ? maxNBandsDL5G : maxNBandsUL5G;
	    	int idx5G = type.equalsIgnoreCase("DL") ? idx5GDL : idx5GUL;
        	int rowNum = 0;
        	int colNum = 0;
	    	XSSFSheet sheet = workbook.createSheet(type);
	
	    	// row 0 (1), intestazione alta
	    	row = sheet.createRow(rowNum);
	    	colNum = idx4G;
	    	
	    	for(int x = 1; x<=n4G; x++) {
		    	cell = row.createCell(colNum);
		    	cell.setCellValue("4G_" + x);
		    	sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,colNum,colNum+intest4G.length-1));
		    	CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		    	colNum += intest4G.length;
	    	}
	    	for(int x = 1; x<=n5G; x++) {
		    	cell = row.createCell(colNum);
		    	cell.setCellValue("5G_" + x);
		    	sheet.addMergedRegion(new CellRangeAddress(rowNum,rowNum,colNum,colNum+intest5G.length-1));
		    	CellUtil.setAlignment(cell, HorizontalAlignment.CENTER);
		    	colNum += intest5G.length;
	    	}
	
	    	// row 1 (2), intestazione bassa
	    	rowNum++;
	    	colNum = 0;
	    	row = sheet.createRow(rowNum);
	    	for (String colName : intestStart) {
	        	cell = row.createCell(colNum);
	        	cell.setCellValue(colName);
	        	colNum++;			
			}
	    	for(int x = 0; x<n4G; x++) {
	        	for (String colName : intest4G) {
	            	cell = row.createCell(colNum);
	            	cell.setCellValue(colName);
	            	colNum++;			
	    		}
	    	}
	    	for(int x = 0; x<n5G; x++) {
	        	for (String colName : intest5G) {
	            	cell = row.createCell(colNum);
	            	cell.setCellValue(colName);
	            	colNum++;			
	    		}
	    	}
	
	    	//Dati    	
	    	int idx = 0;
	    	for (MRDCBandCombination bandComb : device.getMrdcBandCombinations()) {
		    	rowNum++;
		    	row = sheet.createRow(rowNum);
	        	colNum = 0;
	
	        	String conf = "CA_";
	    		
	    		ArrayList<MRDCBandEutra> bandComb4G;
				ArrayList<MRDCBandNR> bandComb5G;
				if(i==0) {
	    			bandComb4G = bandComb.getDownlink4G();
	    			bandComb5G = bandComb.getDownlink5G();
	    		} else {
	    			bandComb4G = bandComb.getUplink4G();
	    			bandComb5G = bandComb.getUplink5G();	    			
	    		}
	    		
	    		
	    		colNum = idx4G;
	    		for (MRDCBandEutra band : bandComb4G) {
					conf += band.getBandID() + String.valueOf(band.getBandClass()) + "(" + band.getBandLayer() + ")" + "-";
					
					row.createCell(colNum++).setCellValue(band.getBandID());
					row.createCell(colNum++).setCellValue(String.valueOf(band.getBandClass()));
					row.createCell(colNum++).setCellValue(Integer.valueOf(band.getBandLayer().split("x")[0]));
				}
	
	    		colNum = idx5G;
	    		for (MRDCBandNR band : bandComb5G) {
					conf += band.getBandID() + String.valueOf(band.getBandClass()) + "(" + band.getBandLayer() + ")" + band.getBandModulation() + band.getBandSCS() + band.getBandWidth() + "-";
					
					row.createCell(colNum++).setCellValue(band.getBandID());
					row.createCell(colNum++).setCellValue(String.valueOf(band.getBandClass()));
					row.createCell(colNum++).setCellValue(Integer.valueOf(band.getBandLayer().split("x")[0]));
					row.createCell(colNum++).setCellValue(band.getBandModulation());
					row.createCell(colNum++).setCellValue(band.getBandSCS());
					row.createCell(colNum++).setCellValue(band.getBandWidth());
					row.createCell(colNum++).setCellValue(band.getChannelBW90Mhz());
				}
	
	    		conf = conf.substring(0, conf.length()-1);
	        	row.createCell(1).setCellValue(conf); // conf
	        	row.createCell(0).setCellValue(idx++); // Index
			}
    	}
    	
    	return workbook;
    }

    public static String printVolteCap(Mobile device){
        String output = "";
        output = output.concat("VoLTE Basic Info:").concat("\nP-Early Media Support:  " + device.getLteCap().getVolteCap().getpEarlyMediaSupport());
        output = output.concat("\nSIP USER AGENT: "+device.getLteCap().getVolteCap().getUserAgent())
                .concat("\n\n\nP-Security Client:");
        if (!(device.getLteCap().getVolteCap().getSupportedAlgorithms() == null)
                ||(device.getLteCap().getVolteCap().getSupportedAlgorithms().size()==0)) {
            for (int i = 0 ; i < device.getLteCap().getVolteCap().getSupportedAlgorithms().size(); i++) {
                output = output.concat("\nALG:" + device.getLteCap().getVolteCap().getSupportedAlgorithms().get(i).getAlg() + "\tEALG:" +
                        device.getLteCap().getVolteCap().getSupportedAlgorithms().get(i).getEalg());
            }
        } else {
            output = output.concat(Utility.NOT_APPLICABLE);
        }
        output = output.concat("\n\n\nVoLTE Codecs: ");
        for (int s = 0 ; s < device.getLteCap().getVolteCap().getSupportedCodecs().size(); s++) {
            output = output.concat("\n\nCodec #" + String.valueOf(s+1)).concat("\nMIME TYPE: " + device.getLteCap().getVolteCap().getSupportedCodecs().get(s).getMimeType());
            output = output.concat("\nSample Rate: " + device.getLteCap().getVolteCap().getSupportedCodecs().get(s).getSampleRate())
                    .concat("\nMedia Format: " + device.getLteCap().getVolteCap().getSupportedCodecs().get(s).getMediaFormat());
            if (device.getLteCap().getVolteCap().getSupportedCodecs().get(s).getMimeType().equals(Utility.VOLTE_EVS)) {
                output = output.concat("\nbw: " + device.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBw())
                        .concat("\nbr: " + device.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBr()[0] + "-"
                                + device.getLteCap().getVolteCap().getSupportedCodecs().get(s).getBr()[1]);
            }
        }

        output = output.concat("\n\n\n\nSupported SRVCC(s): ");
        for (int y = 0 ; y < device.getLteCap().getVolteCap().getSupportedSrvccs().size(); y++) {
            output = output.concat("\n"+device.getLteCap().getVolteCap().getSupportedSrvccs().get(y));
        }
        if (device.getLteCap().getVolteCap().getSupportedSrvccs().size() == 0) {
            output = output.concat("\n"+Utility.NOT_SUPPORTED);
        }
        return output;
    }


    public int getNRDCbit() {
        return NRDCbit;
    }

    public void setNRDCbit(int NRDCbit) {
        this.NRDCbit = NRDCbit;
    }
}
