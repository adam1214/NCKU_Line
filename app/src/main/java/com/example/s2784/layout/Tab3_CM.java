package com.example.s2784.layout;

public class Tab3_CM {

    public interface CtrlUnit {
        void refreshLv();
    }

    private static Tab3_CM mInstance;
    private CtrlUnit ctrlUnit;

    private Tab3_CM() {}

    public static Tab3_CM getInstance() {
        if(mInstance == null) {
            mInstance = new Tab3_CM();
        }
        return mInstance;
    }

    public void setListener(CtrlUnit cu) {
        ctrlUnit = cu;
    }

    public void refreshLv() {
        ctrlUnit.refreshLv();
    }
}
