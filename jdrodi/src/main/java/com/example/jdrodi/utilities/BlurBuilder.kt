package com.example.jdrodi.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.os.Build;
import android.renderscript.Allocation;
import android.renderscript.Element;
import android.renderscript.RenderScript;
import android.renderscript.ScriptIntrinsicBlur;
import android.view.View;

import androidx.annotation.RequiresApi;

public class BlurBuilder {
    private static final float BITMAP_SCALE = 0.4f;
    private static final float BLUR_RADIUS = 7.5f;

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(View view) {
        return blur(view.getContext(), getScreenshot(view), BLUR_RADIUS);
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    public static Bitmap blur(Context context, Bitmap bitmap, float f) {
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, Math.round(((float) bitmap.getWidth()) * BITMAP_SCALE), Math.round(((float) bitmap.getHeight()) * BITMAP_SCALE), false);
        Bitmap createBitmap = Bitmap.createBitmap(createScaledBitmap);
        RenderScript create = RenderScript.create(context);
        ScriptIntrinsicBlur create2 = ScriptIntrinsicBlur.create(create, Element.U8_4(create));
        Allocation createFromBitmap = Allocation.createFromBitmap(create, createScaledBitmap);
        Allocation createFromBitmap2 = Allocation.createFromBitmap(create, createBitmap);
        create2.setRadius(f);
        create2.setInput(createFromBitmap);
        create2.forEach(createFromBitmap2);
        createFromBitmap2.copyTo(createBitmap);
        return createBitmap;
    }

    private static Bitmap getScreenshot(View view) {
        Bitmap createBitmap = Bitmap.createBitmap(view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        view.draw(new Canvas(createBitmap));
        return createBitmap;
    }
}