package com.company.Model.lte;

/**
 * Model Class representing a couple of algorithms (Auth and Encr.) supported by DUT for IMS registration
 */
public class AlgCouple {
    private String alg;
    private String ealg;

    public String getAlg() {
        return alg;
    }

    public void setAlg(String alg) {
        this.alg = alg;
    }

    public String getEalg() {
        return ealg;
    }

    public void setEalg(String ealg) {
        this.ealg = ealg;
    }
}
