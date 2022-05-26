package com.company.Model.lte;

import com.company.Model.lte.LTEBand;
import com.company.Utility;

/**
 * Class to identify single Component Carrier object in CA combination list
 */
public class ComponentCarrier extends LTEBand {

    private String componentCarrierClass;
    private int supportedLayers;
    private int supportedLayersTM34;
    private boolean support256QAMDL;
    private boolean support256QAMUL = false;

    public int getSupportedLayersTM34() {
        return supportedLayersTM34;
    }
    /**
     * Sets and parse supported layers for tm 34
     * @param supportedLayersTM34 number of supported layers as a binary value (e.g., 0 = 2 layers, 1 = 4 layers)
     */
    public void setSupportedLayersTM34(int supportedLayersTM34) {
        this.supportedLayersTM34 = supportedLayersTM34;
    }
    public ComponentCarrier(int bandID, int itemID) {
        super(bandID, itemID);
    }
    public ComponentCarrier() {
        super();
    }
    public String getComponentCarrierClass() {

        return componentCarrierClass;
    }
    /**
     * Sets and translates into string the class of the component carrier.
     * @param componentCarrierClass the id of the class of the component carrier
     */
    public void setComponentCarrierClass(String componentCarrierClass) {
        int ccClass = Integer.parseInt(componentCarrierClass);
        switch (ccClass) {
            case 0: this.componentCarrierClass = Utility.BANDWIDTH_CLASS_A;
                break;
            case 1: this.componentCarrierClass = Utility.BANDWIDTH_CLASS_B;
                break;
            case 2: this.componentCarrierClass = Utility.BANDWIDTH_CLASS_C;
                break;
            case 3: this.componentCarrierClass = Utility.BANDWIDTH_CLASS_D;
                break;
            case 4: this.componentCarrierClass = Utility.BANDWIDTH_CLASS_E;
                break;
            case 5: this.componentCarrierClass = Utility.BANDWIDTH_CLASS_F;
                break;
            case 6: this.componentCarrierClass = Utility.BANDWIDTH_CLASS_I;
                break;
            // TODO what to display as default?
            default: this.componentCarrierClass = Utility.MISSING_CODE;
                break;
        }
    }
    public int getSupportedLayers() {
        return supportedLayers;
    }
    /**
     * Sets and parse the supported layers
     * @param supportedLayers supported layers as binary value (e.g., 0=2 layers, 1=4 layers)
     */
    public void setSupportedLayers(int supportedLayers) {
        this.supportedLayers = supportedLayers;
    }

    @Override
    public void setSupport256QAMDL(boolean support256QAMDL) {
        this.support256QAMDL = support256QAMDL;
    }

   public void setSupport256QAMUL(boolean support256QAMUL){
        this.support256QAMUL = support256QAMUL;
   }

    public boolean getSupport256QAMDL(){
        return this.support256QAMDL;
    }

    public boolean getSupport256QAMUL(){
        return this.support256QAMUL;
    }
}
