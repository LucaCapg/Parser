package com.company.Model.lte;

import com.company.Utility;

/**
 * Model class to manage VoLTE audio codecs
 */
public class VoLTECodec {

    private String mimeType;
    private int sampleRate;
    private int mediaFormat;
    private String bw;
    private float[] br;

    public String getBw() {
        return bw;
    }
    public void setBw(String bw) {
        this.bw = bw;
    }
    public float[] getBr() {
        return br;
    }
    public void setBr(float[] br) {
        this.br = br;
    }
    public String getMimeType() {
        return mimeType;
    }
    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
        if (mimeType.equals(Utility.VOLTE_AMR_WB)){
            this.bw = "wb";
        } else if (mimeType.equals(Utility.VOLTE_AMR)){
            this.bw = "nb";
        }
    }
    public int getSampleRate() {
        return sampleRate;
    }
    public void setSampleRate(int sampleRate) {
        this.sampleRate = sampleRate;
    }
    public int getMediaFormat() {
        return mediaFormat;
    }
    public void setMediaFormat(int mediaFormat) {
        this.mediaFormat = mediaFormat;
    }
}
