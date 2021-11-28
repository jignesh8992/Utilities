package com.example.jdrodi.utilities;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;


import java.io.ByteArrayOutputStream;
import java.io.File;

public class ShareHelper {

    private static String TAG = "JNP__" + ShareHelper.class.getSimpleName();

    public static String FACEBOOK = "com.facebook.katana";
    public static String WHATSAPP = "com.whatsapp";
    public static String INSTAGRAM = "com.instagram.android";

    private static String WHATSAPP_NOT_INSATLLED = "WhatsApp have not been installed...";
    private static String FACEBOOK_NOT_INSATLLED = "Facebook have not been installed...";
    private static String INSATAGRAM_NOT_INSATLLED = "Instagram have not been installed...";


    // TODO IMAGE SHARE BLOCK

    /**
     * ToDo.. Share image form path of image
     *
     * @param mContext The Context
     * @param path     The path of image
     */
    public static void shareImage(Context mContext, String path) {
        try {
            shareImage(mContext, path, null);
        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.toString());
        }
    }

    /**
     * ToDo.. Share image form Uri of image
     *
     * @param mContext    The Context
     * @param path        The path of image
     * @param packageName The packageName of application is to share image on the app
     */
    public static void shareImage(Context mContext, String path, String packageName) {
        try {
            // if app is installed
            if (packageName != null && mContext.getPackageManager().getLaunchIntentForPackage(packageName) == null) {

                // if app is not installed
                String msg = null;
                if (packageName.equals(WHATSAPP)) {
                    msg = WHATSAPP_NOT_INSATLLED;
                } else if (packageName.equals(INSTAGRAM)) {
                    msg = INSATAGRAM_NOT_INSATLLED;
                } else if (packageName.equals(FACEBOOK)) {
                    msg = FACEBOOK_NOT_INSATLLED;
                }
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                // open play store
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("market://details?id=" + packageName));
                mContext.startActivity(intent);

            } else {

                // change message as per app
                // change message as per app
                String text = "Status Saver - Status Downloader For WhatsApp Statuses: ";
                String link = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("image/*");


                File imgPath = new File(path);
                if (packageName != null) intent.setPackage(packageName);
                Bitmap myBitmap = BitmapFactory.decodeFile(imgPath.getAbsolutePath());
                intent.putExtra(Intent.EXTRA_STREAM, getImageUri(mContext, myBitmap));
                intent.putExtra(Intent.EXTRA_TEXT, text + link);
                mContext.startActivity(Intent.createChooser(intent, "Share Image"));

            }


        } catch (Exception e) {
            e.printStackTrace();
            Log.i(TAG, e.toString());
        }
    }

    /**
     * ToDo.. Get Uri of Bitmap Image
     *
     * @param mContext The context
     * @param bitmap   The image bitmap
     * @return The uri of bitmap image
     */
    public static Uri getImageUri(Context mContext, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(
                mContext.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    // TODO VIDEO SHARE BLOCK

    /**
     * ToDo.. Share video form path of video
     *
     * @param mContext The Context
     * @param path     The path of video
     */
    public static void shareVideo(final Context mContext, String path) {
        MediaScannerConnection.scanFile(mContext, new String[]{path},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        shareVideo(mContext, uri, null);
                    }
                });
    }

    /**
     * ToDo.. Share video form path of video
     *
     * @param mContext The Context
     * @param path     The path of video
     */
    public static void shareVideo(final Context mContext, String path, final String packageName) {
        MediaScannerConnection.scanFile(mContext, new String[]{path},
                null, new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                        shareVideo(mContext, uri, packageName);
                    }
                });
    }

    /**
     * ToDo.. Share image form uri of video
     *
     * @param mContext    The Context
     * @param uri         The uri of video
     * @param packageName The packageName of application is to share image on the app
     */
    public static void shareVideo(final Context mContext, Uri uri, String packageName) {

        try {
            // if app is installed
            if (packageName != null && mContext.getPackageManager().getLaunchIntentForPackage(
                    packageName) == null) {

                // if app is not installed
                String msg = null;
                if (packageName.equals(WHATSAPP)) {
                    msg = WHATSAPP_NOT_INSATLLED;
                } else if (packageName.equals(INSTAGRAM)) {
                    msg = INSATAGRAM_NOT_INSATLLED;
                } else if (packageName.equals(FACEBOOK)) {
                    msg = FACEBOOK_NOT_INSATLLED;
                }
                Toast.makeText(mContext, msg, Toast.LENGTH_SHORT).show();

                // open play store
                Intent intent = new Intent("android.intent.action.VIEW");
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.setData(Uri.parse("market://details?id=" + packageName));
                mContext.startActivity(intent);

            } else {

                // change message as per app
                String text = "Status Saver - Status Downloader For WhatsApp Statuses: ";
                String link = "https://play.google.com/store/apps/details?id=" + mContext.getPackageName();

                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("video/*");
                if (packageName != null) intent.setPackage(packageName);
                intent.putExtra(Intent.EXTRA_STREAM, uri);
                intent.putExtra(Intent.EXTRA_TEXT, text + link);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
                mContext.startActivity(Intent.createChooser(intent, "Share Video"));

            }
        } catch (Exception ignored) {
            Log.i(TAG, ignored.toString());
        }
    }


}
