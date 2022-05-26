package com.company.Model.nr;

import com.company.Model.Band;

import java.util.ArrayList;

/**
 * Model class representing a MRDC Band NR (5G)
 */
public class MRDCBandNR extends Band {
    protected int bandID;
    protected char bandClass;
    protected String bandLayer;
    protected String bandModulation;
    protected String bandSCS;
    protected String bandWidth;
    protected String channelBW90Mhz;
	//featureSets values -> which featureSet the nr band belongs to
	protected int downlinkSetNR;
	protected int uplinkSetNR;
	protected boolean isAdditionalDRMSsupportedByFeatureset = false;

    //SCS
	//bit values
	protected String SCS_15KHz_values;
    protected String SCS_30KHz_values;
	protected String SCS_60KHz_values;
	protected String SCS_15KHz_values_UL;
	protected String SCS_30KHz_values_UL;
	protected String SCS_60KHz_values_UL;
	//String values
	protected ArrayList<String> scs15_string_val;
	protected ArrayList<String> scs30_string_val;
	protected ArrayList<String> scs60_string_val;
	protected ArrayList<String> scs15_string_val_UL;
	protected ArrayList<String> scs30_string_val_UL;
	protected ArrayList<String> scs60_string_val_UL;
	//5g
	protected boolean rateMatchingLTE_CRS = false;


    public MRDCBandNR(){}
    
    public MRDCBandNR(int bandID, char bandClass, String bandLayer, String bandModulation, String bandSCS, String bandWidth, String channelBW90Mhz) {
        this.bandID = bandID;
        this.bandClass = bandClass;
        this.bandLayer = bandLayer;
        this.bandModulation = bandModulation;
        this.bandSCS = bandSCS;
        this.bandWidth = bandWidth;
        this.channelBW90Mhz = channelBW90Mhz;
    }

	public int getBandID() {
		return bandID;
	}

	public void setBandID(int bandID) {
		this.bandID = bandID;
	}

	public char getBandClass() {
		return bandClass;
	}

	public void setBandClass(char bandClass) {
		this.bandClass = bandClass;
	}

	public String getBandLayer() {
		return bandLayer;
	}

	public void setBandLayer(String bandLayer) {
		this.bandLayer = bandLayer;
	}

	public String getBandModulation() {
		return bandModulation;
	}

	public void setBandModulation(String bandModulation) {
		this.bandModulation = bandModulation;
	}

	public String getBandSCS() {
		return bandSCS;
	}

	public void setBandSCS(String bandSCS) {
		this.bandSCS = bandSCS;
	}

	public String getSCS_15KHz_values(){
    	return SCS_15KHz_values;
	}
	public void setSCS_15KHz_values(String SCS_15Khz_values){
    	this.SCS_15KHz_values = SCS_15Khz_values;
	}

	public String getSCS_15KHz_values_UL() {
		return SCS_15KHz_values_UL;
	}
	public void setSCS_15KHz_values_UL(String SCS_15KHz_values_UL) {
		this.SCS_15KHz_values_UL = SCS_15KHz_values_UL;
	}

	public void setScs15_string_val(ArrayList<String> scs15_string_val) {
		this.scs15_string_val = scs15_string_val;
	}

	public ArrayList<String> getScs15_string_val() {
		return scs15_string_val;
	}

	public ArrayList<String> getScs15_string_val_UL() {
		return scs15_string_val_UL;
	}
	public void setScs15_string_val_UL(ArrayList<String> scs15_string_val_UL) {
		this.scs15_string_val_UL = scs15_string_val_UL;
	}

	public String getSCS_30KHz_values(){
    	return SCS_30KHz_values;
	}
	public void setSCS_30Khz_values(String SCSvalues_30_KHz){
    	this.SCS_30KHz_values = SCSvalues_30_KHz;
	}

	public String getSCS_30KHz_values_UL() {
		return SCS_30KHz_values_UL;
	}

	public void setSCS_30KHz_values_UL(String SCS_30KHz_values_UL) {
		this.SCS_30KHz_values_UL = SCS_30KHz_values_UL;
	}

	public void setScs30_string_val(ArrayList<String> scs30_string_val) {
		this.scs30_string_val = scs30_string_val;
	}

	public ArrayList<String> getScs30_string_val() {
		return scs30_string_val;
	}

	public ArrayList<String> getScs30_string_val_UL() {
		return scs30_string_val_UL;
	}
	public void setScs30_string_val_UL(ArrayList<String> scs30_string_val_UL) {
		this.scs30_string_val_UL = scs30_string_val_UL;
	}
	public void setSCS_60KHz_values(String SCSvalues_60_Khz){

    	this.SCS_60KHz_values = SCS_60KHz_values;
	}

	public String getSCS_60KHz_values() {
    	return SCS_60KHz_values;
	}

	public String getSCS_60KHz_values_UL() {
		return SCS_60KHz_values_UL;
	}
	public void setSCS_60KHz_values_UL(String SCS_60KHz_values_UL) {
		this.SCS_60KHz_values_UL = SCS_60KHz_values_UL;
	}

	public void setScs60_string_val(ArrayList<String> scs60_string_val) {
		this.scs60_string_val = scs60_string_val;
	}

	public ArrayList<String> getScs60_string_val() {
		return scs60_string_val;
	}

	public ArrayList<String> getScs60_string_val_UL() {
		return scs60_string_val_UL;
	}
	public void setScs60_string_val_UL(ArrayList<String> scs60_string_val_UL) {
		this.scs60_string_val_UL = scs60_string_val_UL;
	}
	public String getBandWidth() {
		return bandWidth;
	}

	public void setBandWidth(String bandWidth) {
		this.bandWidth = bandWidth;
	}

	public String getChannelBW90Mhz() {
		return channelBW90Mhz;
	}

	public void setChannelBW90Mhz(String channelBW90Mhz) {
		this.channelBW90Mhz = channelBW90Mhz;
	}

	public void setRateMatchingLTE_CRS(boolean rateMatchingLTE_CRS){
    	this.rateMatchingLTE_CRS = rateMatchingLTE_CRS;
	}

	public boolean isRateMatchingLTE_CRS() {
		return rateMatchingLTE_CRS;
	}



	public int getDownlinkSetNR() {
		return downlinkSetNR;
	}

	public int getUplinkSetNR() {
		return uplinkSetNR;
	}

	public void setDownlinkSetNR(int downlinkSetNR) {
		this.downlinkSetNR = downlinkSetNR;
	}

	public void setUplinkSetNR(int uplinkSetNR) {
		this.uplinkSetNR = uplinkSetNR;
	}

	public boolean isAdditionalDRMSsupportedByFeatureset() {
		return isAdditionalDRMSsupportedByFeatureset;
	}

	public void setAdditionalDRMSsupportedByFeatureset(boolean additionalDRMSsupportedByFeatureset) {
		isAdditionalDRMSsupportedByFeatureset = additionalDRMSsupportedByFeatureset;
	}
}
