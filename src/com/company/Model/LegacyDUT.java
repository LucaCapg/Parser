package com.company.Model;

import com.company.Model.lte.LTELegacyCapability;

public class LegacyDUT extends DUT {
    private LTELegacyCapability lteCap;
    public LegacyDUT(){
        this.lteCap = new LTELegacyCapability();
    }

    public LTELegacyCapability getLteCap() {
        return lteCap;
    }

    public void setLteCap(LTELegacyCapability lteCap) {
        this.lteCap = lteCap;
    }
}
