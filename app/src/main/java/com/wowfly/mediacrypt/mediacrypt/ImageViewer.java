package com.wowfly.mediacrypt.mediacrypt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Rect;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.ActionMode;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import uk.co.senab.photoview.PhotoViewAttacher;

/**
 * Created by user on 9/11/14.
 */
public class ImageViewer extends Activity {
    private static final String TAG = "ImageViewer";
    private PhotoViewAttacher mAttacher;
    private ImageView imageView;

    public void onCreate(Bundle saved) {
        super.onCreate(saved);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_imageviewer);
        imageView = (ImageView) findViewById(R.id.imageviewer);
        mAttacher = new PhotoViewAttacher(imageView);

        WifiManager mWiFi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

        Intent i = getIntent();
        String filename = i.getStringExtra("filename");

        //Log.i(TAG, "WifiState = " + mWiFi.getWifiState());
        WifiInfo wi = mWiFi.getConnectionInfo();
        //String mackey = wi.getMacAddress().replace(":", "");
        //mackey = mackey.toLowerCase();
        String mackey = wi.getMacAddress();
        Log.i(TAG, " MAC address =" + mackey);
        InputStream is = MediaCryptApp.decryptAESBuffer(filename, mackey);

        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inSampleSize = 2;
        opt.inDither = true;
        Rect _rt = new Rect();
        Bitmap bm = BitmapFactory.decodeStream(is, _rt, opt);

        imageView.setImageBitmap(bm);
        mAttacher.update();
    }


}
