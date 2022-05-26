package com.company.Model.nr;

import java.util.ArrayList;

/**
 * Model class representing a MRDC Band Combination
 */
public class MRDCBandCombination {
    protected ArrayList<MRDCBandEutra> downlink4G;
    protected ArrayList<MRDCBandNR> downlink5G;
    protected ArrayList<MRDCBandEutra> uplink4G;
    protected ArrayList<MRDCBandNR> uplink5G;
    protected int featureSetCombination;

    public MRDCBandCombination(){
    	downlink4G = new ArrayList<MRDCBandEutra>();
    	downlink5G = new ArrayList<MRDCBandNR>();
    	uplink4G = new ArrayList<MRDCBandEutra>();
    	uplink5G = new ArrayList<MRDCBandNR>();
    	featureSetCombination = -1;
    }
    
    public MRDCBandCombination(ArrayList<MRDCBandEutra> downlink4G, ArrayList<MRDCBandNR> downlink5G, ArrayList<MRDCBandEutra> uplink4G, ArrayList<MRDCBandNR> uplink5G) {
    	this.downlink4G = downlink4G;
    	this.downlink5G = downlink5G;
    	this.uplink4G = uplink4G;
    	this.uplink5G = uplink5G;
    }

	public ArrayList<MRDCBandEutra> getDownlink4G() {
		return downlink4G;
	}

	public void setDownlink4G(ArrayList<MRDCBandEutra> downlink4g) {
		downlink4G = downlink4g;
	}

	public ArrayList<MRDCBandNR> getDownlink5G() {
		return downlink5G;
	}

	public void setDownlink5G(ArrayList<MRDCBandNR> downlink5g) {
		downlink5G = downlink5g;
	}

	public ArrayList<MRDCBandEutra> getUplink4G() {
		return uplink4G;
	}

	public void setUplink4G(ArrayList<MRDCBandEutra> uplink4g) {
		uplink4G = uplink4g;
	}

	public ArrayList<MRDCBandNR> getUplink5G() {
		return uplink5G;
	}

	public void setUplink5G(ArrayList<MRDCBandNR> uplink5g) {
		uplink5G = uplink5g;
	}

	public int getFeatureSetCombination() {
		return featureSetCombination;
	}

	public void setFeatureSetCombination(int featureSetCombination) {
		this.featureSetCombination = featureSetCombination;
	}

}

