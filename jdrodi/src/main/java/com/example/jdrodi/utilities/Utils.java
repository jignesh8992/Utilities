package com.example.jdrodi.utilities;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Typeface;
import com.google.android.material.tabs.TabLayout;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class Utils {

    public static void changeTabsFont(Context mContext, TabLayout tabs,String fontPath) {
        ViewGroup vg = (ViewGroup) tabs.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    AssetManager mgr = mContext.getAssets();
                    Typeface tf = Typeface.createFromAsset(mgr,fontPath);
                    //Font file in /assets
                    ((TextView) tabViewChild).setTypeface(tf);
                }
            }
        }
    }
}
