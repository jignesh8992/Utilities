package com.example.jdrodi.utilities;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.Shader;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class ImageUtils {
    public static float getnewHeight(int i, int i2, float f, float f2) {
        return (((float) i2) * f) / ((float) i);
    }

    public static float getnewWidth(int i, int i2, float f, float f2) {
        return (((float) i) * f2) / ((float) i2);
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context) throws IOException {
        String realPathFromURI = getRealPathFromURI(uri, context);
        try {
            @SuppressLint("WrongConstant") Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
            Point point = new Point();
            defaultDisplay.getSize(point);
            int i = point.x;
            int dpToPx = point.y - dpToPx(context, 110);
            if (i <= dpToPx) {
                i = dpToPx;
            }
            return resampleImage(realPathFromURI, i);
        } catch (Exception e) {
            e.printStackTrace();
            return MediaStore.Images.Media.getBitmap(context.getContentResolver(), Uri.parse(realPathFromURI));
        }
    }

    public static Bitmap getResampleImageBitmap(Uri uri, Context context, int i) throws IOException {
        try {
            return resampleImage(getRealPathFromURI(uri, context), i);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static Bitmap resampleImage(String str, int i) throws Exception {
        int exifRotation;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        BitmapFactory.Options options2 = new BitmapFactory.Options();
        options2.inSampleSize = getClosestResampleSize(options.outWidth, options.outHeight, i);
        Bitmap decodeFile = BitmapFactory.decodeFile(str, options2);
        Matrix matrix = new Matrix();
        if (decodeFile.getWidth() > i || decodeFile.getHeight() > i) {
            BitmapFactory.Options resampling = getResampling(decodeFile.getWidth(), decodeFile.getHeight(), i);
            matrix.postScale(((float) resampling.outWidth) / ((float) decodeFile.getWidth()), ((float) resampling.outHeight) / ((float) decodeFile.getHeight()));
        }
        if (Integer.parseInt(Build.VERSION.SDK) > 4 && (exifRotation = ExifUtils.getExifRotation(str)) != 0) {
            matrix.postRotate((float) exifRotation);
        }
        return Bitmap.createBitmap(decodeFile, 0, 0, decodeFile.getWidth(), decodeFile.getHeight(), matrix, true);
    }

    public static BitmapFactory.Options getResampling(int i, int i2, int i3) {
        float f;
        float f2;
        BitmapFactory.Options options = new BitmapFactory.Options();
        if (i <= i2 && i2 > i) {
            f = (float) i3;
            f2 = (float) i2;
        } else {
            f = (float) i3;
            f2 = (float) i;
        }
        float f3 = f / f2;
        options.outWidth = (int) ((((float) i) * f3) + 0.5f);
        options.outHeight = (int) ((((float) i2) * f3) + 0.5f);
        return options;
    }

    public static int getClosestResampleSize(int i, int i2, int i3) {
        int max = Math.max(i, i2);
        int i4 = 1;
        while (true) {
            if (i4 >= Integer.MAX_VALUE) {
                break;
            } else if (i4 * i3 > max) {
                i4--;
                break;
            } else {
                i4++;
            }
        }
        if (i4 > 0) {
            return i4;
        }
        return 1;
    }

    public static BitmapFactory.Options getBitmapDims(String str) throws Exception {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(str, options);
        return options;
    }

    public static String getRealPathFromURI(Uri uri, Context context) {
        try {
            Cursor query = context.getContentResolver().query(uri, null, null, null, null);
            if (query == null) {
                return uri.getPath();
            }
            query.moveToFirst();
            String string = query.getString(query.getColumnIndex("_data"));
            query.close();
            return string;
        } catch (Exception e) {
            e.printStackTrace();
            return uri.toString();
        }
    }

    public static int dpToPx(Context context, int i) {
        context.getResources();
        return (int) (((float) i) * Resources.getSystem().getDisplayMetrics().density);
    }

    public static float pxToDp(Context context, float f) {
        return f / (((float) context.getResources().getDisplayMetrics().densityDpi) / 160.0f);
    }

    public static Bitmap bitmapmasking(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Paint paint = new Paint(1);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(bitmap2, 0.0f, 0.0f, paint);
        paint.setXfermode(null);
        return createBitmap;
    }

    public static Bitmap getThumbnail(Bitmap bitmap, int i, int i2) {
        Bitmap bitmap2;
        Bitmap copy = bitmap.copy(bitmap.getConfig(), true);
        bitmap.recycle();
        int width = copy.getWidth();
        int height = copy.getHeight();
        if (height > width) {
            bitmap2 = cropCenterBitmap(copy, width, width);
        } else {
            bitmap2 = cropCenterBitmap(copy, height, height);
        }
        return Bitmap.createScaledBitmap(bitmap2, i, i2, true);
    }

    public static Bitmap cropCenterBitmap(Bitmap bitmap, int i, int i2) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        if (width < i && height < i2) {
            return bitmap;
        }
        int i3 = 0;
        int i4 = width > i ? (width - i) / 2 : 0;
        if (height > i2) {
            i3 = (height - i2) / 2;
        }
        if (i > width) {
            i = width;
        }
        if (i2 > height) {
            i2 = height;
        }
        return Bitmap.createBitmap(bitmap, i4, i3, i, i2);
    }

    public static Bitmap cropCenterBitmap(Bitmap bitmap) {
        if (bitmap == null) {
            return null;
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = width > height ? height : width;
        if (width < i && height < i) {
            return bitmap;
        }
        int i2 = 0;
        int i3 = width > i ? (width - i) / 2 : 0;
        if (height > i) {
            i2 = (height - i) / 2;
        }
        if (i <= width) {
            width = i;
        }
        if (i <= height) {
            height = i;
        }
        return Bitmap.createBitmap(bitmap, i3, i2, width, height);
    }

    public static Bitmap mergelogo(Bitmap bitmap, Bitmap bitmap2) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), bitmap.getConfig());
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        float width2 = (float) bitmap2.getWidth();
        float height2 = (float) bitmap2.getHeight();
        float f = width2 / height2;
        float f2 = height2 / width2;
        if (width2 > width) {
            bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int) width, (int) (f2 * width), false);
        } else if (height2 > height) {
            bitmap2 = Bitmap.createScaledBitmap(bitmap2, (int) (f * height), (int) height, false);
        }
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, (Paint) null);
        canvas.drawBitmap(bitmap2, (float) (bitmap.getWidth() - bitmap2.getWidth()), (float) (bitmap.getHeight() - bitmap2.getHeight()), (Paint) null);
        return createBitmap;
    }

    public static Bitmap CropBitmapTransparency(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int i = -1;
        int i2 = -1;
        for (int i3 = 0; i3 < bitmap.getHeight(); i3++) {
            for (int i4 = 0; i4 < bitmap.getWidth(); i4++) {
                if (((bitmap.getPixel(i4, i3) >> 24) & 255) > 0) {
                    if (i4 < width) {
                        width = i4;
                    }
                    if (i4 > i) {
                        i = i4;
                    }
                    if (i3 < height) {
                        height = i3;
                    }
                    if (i3 > i2) {
                        i2 = i3;
                    }
                }
            }
        }
        if (i < width || i2 < height) {
            return null;
        }
        return Bitmap.createBitmap(bitmap, width, height, (i - width) + 1, (i2 - height) + 1);
    }

    public static Bitmap getTiledBitmap(Context context, int i, int i2, int i3) {
        Rect rect = new Rect(0, 0, i2, i3);
        Paint paint = new Paint();
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inScaled = false;
        paint.setShader(new BitmapShader(BitmapFactory.decodeResource(context.getResources(), i, options), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT));
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawRect(rect, paint);
        return createBitmap;
    }

    public static Bitmap getColoredBitmap(int i, int i2, int i3) {
        Bitmap createBitmap = Bitmap.createBitmap(i2, i3, Bitmap.Config.ARGB_8888);
        new Canvas(createBitmap).drawColor(i);
        return createBitmap;
    }


    public static Bitmap getRotatedBitmap(Bitmap bitmap, float f) {
        Bitmap createBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        Matrix matrix = new Matrix();
        matrix.postTranslate((float) ((-bitmap.getWidth()) / 2), (float) ((-bitmap.getHeight()) / 2));
        matrix.postRotate(f);
        matrix.postTranslate((float) (bitmap.getWidth() / 2), (float) (bitmap.getHeight() / 2));
        canvas.drawBitmap(bitmap, matrix, null);
        return createBitmap;
    }

    public static void resampleImageAndSaveToNewLocation(String str, String str2) throws Exception {
        resampleImage(str, 800).compress(Bitmap.CompressFormat.PNG, 100, new FileOutputStream(str2));
    }

    public static Bitmap decodeSampledBitmapFromResource(Resources resources, int i, int i2, int i3) {
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeResource(resources, i, options);
        options.inSampleSize = calculateInSampleSize(options, i2, i3);
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeResource(resources, i, options);
    }

    public static int calculateInSampleSize(BitmapFactory.Options options, int i, int i2) {
        int i3 = options.outHeight;
        int i4 = options.outWidth;
        if (i3 <= i2 && i4 <= i) {
            return 1;
        }
        int round = Math.round(((float) i3) / ((float) i2));
        int round2 = Math.round(((float) i4) / ((float) i));
        return round < round2 ? round : round2;
    }

    public static Bitmap cropBitmapTransparency(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int[] iArr = new int[(width * height)];
        bitmap.getPixels(iArr, 0, width, 0, 0, width, height);
        int i = -1;
        int i2 = width;
        int i3 = height;
        int i4 = -1;
        for (int i5 = 0; i5 < height; i5++) {
            for (int i6 = 0; i6 < width; i6++) {
                if (iArr[(i5 * width) + i6] == 0) {
                    if (i6 < i2) {
                        i2 = i6;
                    }
                    if (i6 > i) {
                        i = i6;
                    }
                    if (i5 < i3) {
                        i3 = i5;
                    }
                    if (i5 > i4) {
                        i4 = i5;
                    }
                }
            }
        }
        return (i < i2 || i4 < i3) ? bitmap : Bitmap.createBitmap(bitmap, i2, i3, (i - i2) + 1, (i4 - i3) + 1);
    }

    public static String saveBitmapObject(Activity activity, Bitmap bitmap) {
        try {
            File file = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "/Story Maker/.data");
            if (!file.exists()) {
                if (!file.mkdirs()) {
                    Log.d("", "Can't create directory to save image.");
                    return null;
                }
            }
            String str = file.getPath() + File.separator + (("raw1_" + System.currentTimeMillis()) + ".png");
            File file2 = new File(str);
            try {
                if (!file2.exists()) {
                    file2.createNewFile();
                }
                FileOutputStream fileOutputStream = new FileOutputStream(file2);
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
                fileOutputStream.flush();
                fileOutputStream.close();
                activity.sendBroadcast(new Intent("android.intent.action.MEDIA_SCANNER_SCAN_FILE", Uri.fromFile(file2)));
            } catch (Exception e) {
                e.printStackTrace();
            }
            return str;
        } catch (Exception e2) {
            e2.printStackTrace();
            return null;
        }
    }

    public static Bitmap cropInRatio(Bitmap bitmap, int i, int i2) {
        float width = (float) bitmap.getWidth();
        float height = (float) bitmap.getHeight();
        float f = getnewHeight(i, i2, width, height);
        float f2 = getnewWidth(i, i2, width, height);
        Bitmap createBitmap = (f2 > width || f2 >= width) ? null : Bitmap.createBitmap(bitmap, (int) ((width - f2) / 2.0f), 0, (int) f2, (int) height);
        if (f <= height && f < height) {
            createBitmap = Bitmap.createBitmap(bitmap, 0, (int) ((height - f) / 2.0f), (int) width, (int) f);
        }
        return (f2 == width && f == height) ? bitmap : createBitmap;
    }
}