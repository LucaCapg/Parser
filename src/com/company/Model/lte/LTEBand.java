package com.company.Model.lte;

import com.company.Model.Band;

public class LTEBand extends Band {
    private boolean support256QAMDL;
    private boolean support64QAMUL;

    public LTEBand() {
    }
    public LTEBand(int bandId){
        this.bandID = bandId;
    }
    public LTEBand(int bandId, int itemId) {
        this.bandID = bandId;
        this.itemID = itemId;
    }

    public boolean isSupport256QAMDL() {
        return support256QAMDL;
    }
    public void setSupport256QAMDL(boolean support256QAMDL) {
        this.support256QAMDL = support256QAMDL;
    }
    public boolean isSupport64QAMUL() {
        return support64QAMUL;
    }
    public void setSupport64QAMUL(boolean support64QAMUL) {
        this.support64QAMUL = support64QAMUL;
    }
}
