package com.company.Model;

/**
 * Model class representing a Band
 */
public class Band {
    protected int bandID;
    protected int itemID;

    public Band(){}
    public Band(int bandID, int itemID) {
        this.bandID = bandID;
        this.itemID = itemID;
    }

    public int getBandID() {
        return bandID;
    }
    public void setBandID(int bandID) {
        this.bandID = bandID;
    }
    public int getItemID() {
        return itemID;
    }
    public void setItemID(int itemID) {
        this.itemID = itemID;
    }
}
