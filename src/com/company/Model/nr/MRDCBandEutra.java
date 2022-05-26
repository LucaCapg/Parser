package com.company.Model.nr;

/**
 * Model class representing a MRDC Band Eutra (4G)
 */
public class MRDCBandEutra {
    protected int bandID;
    protected char bandClass;
    protected String bandLayer;

    public MRDCBandEutra(){}
    
    public MRDCBandEutra(int bandID, char bandClass, String bandLayer) {
        this.bandID = bandID;
        this.bandClass = bandClass;
        this.bandLayer = bandLayer;
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
    
}
