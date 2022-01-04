package com.example.tab_layout;

public class CandleEntry{
    private int count;
    private float mShadowHigh;
    private float mShadowLow;
    private float mOpen;
    private float mClose;
    public CandleEntry(int x, float shadowH, float shadowL, float open, float close) {
        this.count = x;
        this.mShadowHigh = shadowH;
        this.mShadowLow = shadowL;
        this.mOpen = open;
        this.mClose = close;
    }

    public float getValue(){
        return (mShadowHigh+mShadowLow)/2;
    }
}





