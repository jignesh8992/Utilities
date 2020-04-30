package com.example.jdrodi.utilities

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.MediaStore

private var TAG: String = ChooserHelper::class.java.simpleName
const val REQUEST_PHOTO = 1011
const val REQUEST_VIDEO = 1012

object ChooserHelper {

    /**
     * ToDO.. Open Gallery and select single image
     *
     *
     * Required Permission
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
     *
     * @param REQUEST_PHOTO Application specific request code to match with a result
     */
    fun Context.chooseImage(REQUEST_PHOTO: Int) {
        try {
            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            (this as Activity).startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_PHOTO)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }

    /**
     * ToDO.. Open Gallery and multiple images
     *
     *
     * Required Permission
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
     *
     * @param REQUEST_MULTIPLE_PHOTO Application specific request code to match with a result
     */
    // @Override
    // protected void onActivityResult(int requestCode, int resultCode, Intent
    // data) {
    // if (resultCode == RESULT_OK) {
    // videoPreview.setVisibility(View.GONE);
    // imgPreview.setVisibility(View.VISIBLE);
    // Bitmap bitmap = null;
    // if (data.getData() != null) {
    // Uri selectedImage = data.getData();
    // String path = helper.getRealImagePathFromURI(selectedImage);
    // bitmap = BitmapFactory.decodeFile(path, helper.option());
    // } else {
    // if (data.getClipData() != null) {
    // ClipData mClipData = data.getClipData();
    // for (int i = 0; i < mClipData.getItemCount(); i++) {
    // ClipData.Item item = mClipData.getItemAt(i);
    // Uri selectedImage = item.getUri();
    // String path = helper.getRealImagePathFromURI(selectedImage);
    // bitmap = BitmapFactory.decodeFile(path, helper.option());
    // }
    // }
    // }
    // imgPreview.setImageBitmap(bitmap);
    // } else if (resultCode == RESULT_CANCELED) {
    // Toast.makeText(mContext, "User cancelled image load", 0).show();
    // } else {
    // Toast.makeText(mContext, "Sorry! Failed to load image", 0).show();
    // }
    // }
    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    fun Context.openGalleryMultipleImage(REQUEST_MULTIPLE_PHOTO: Int) {
        val intent = Intent()
        intent.type = "image/*"
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
        intent.action = Intent.ACTION_GET_CONTENT
        (this as Activity).startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_MULTIPLE_PHOTO)
    }

    /**
     * ToDO.. Open Gallery and select video
     *
     *
     * Required Permission
     * <uses-permission android:name="android.permission.WRITE_EXTERNAL_STORAGE"></uses-permission>
     * <uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"></uses-permission>
     *
     * @param REQUEST_VIDEO Application specific request code to match with a result
     */
    fun Context.chooseVideo(REQUEST_VIDEO: Int) {
        try {
            val intent = Intent()
            intent.type = "video/*"
            intent.action = Intent.ACTION_GET_CONTENT
            (this as Activity).startActivityForResult(Intent.createChooser(intent, "Select Video"), REQUEST_VIDEO)
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
        }
    }
}