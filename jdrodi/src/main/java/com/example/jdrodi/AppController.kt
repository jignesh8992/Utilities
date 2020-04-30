package com.example.jdrodi

import android.app.Application



/**
 * AppController.kt - A simple application class.
 * @author  Jignesh N Patel
 * @date 07-11-2019
 */

class AppController : Application() {

    override fun onCreate() {
        super.onCreate()
        /* // Initialize Google Ads
         MobileAds.initialize(this, getString(R.string.admob_app_id))

         // Add Test Device id here
         val testDevices: MutableList<String> = ArrayList()
         testDevices.add(AdRequest.DEVICE_ID_EMULATOR)
         testDevices.add("F3DB57B38CD2293DF25DF9C31C158C6A")
         val requestConfiguration = RequestConfiguration.Builder()
             .setTestDeviceIds(testDevices)
             .build()
         MobileAds.setRequestConfiguration(requestConfiguration)

         // Initialize Calligraphy font library
         ViewPump.init(
             ViewPump.builder().addInterceptor(
                 CalligraphyInterceptor(
                     CalligraphyConfig.Builder()
                         .setDefaultFontPath("app_fonts/DIN.ttf")
                         .setFontAttrId(R.attr.fontPath)
                         .build()
                 )
             ).build()
         )

         // Initialize Coil Image Processing Library
         Coil.setDefaultImageLoader {
             ImageLoader(this@AppController) {
                 okHttpClient {
                     Builder().cache(CoilUtils.createDefaultCache(this@AppController)).build()
                 }
             }
         }*/
    }
}