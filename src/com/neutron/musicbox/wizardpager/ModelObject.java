package com.neutron.musicbox.wizardpager;


import com.neutron.musicbox.R;


public enum ModelObject {

    RED(R.string.red, R.layout.view_wizard1),
    BLUE(R.string.blue, R.layout.view_wizard2),
    GREEN(R.string.green, R.layout.view_wizard3);

    private int mTitleResId;
    private int mLayoutResId;

    ModelObject(int titleResId, int layoutResId) {
        mTitleResId = titleResId;
        mLayoutResId = layoutResId;
    }

    public int getTitleResId() {
        return mTitleResId;
    }

    public int getLayoutResId() {
        return mLayoutResId;
    }

}