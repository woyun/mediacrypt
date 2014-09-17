package com.wowfly.mediacrypt.mediacrypt;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.security.Security;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 * Created by user on 9/11/14.
 */

//openssl enc -aes-128-cbc -in wallpaper_03.jpg -out wallpaper_03.aes -p -K 0008224a90f2 -iv 0008224a90f2

public class MediaCryptApp extends Application {
    private static final String TAG = "MediaCryptApp";

    public static CipherInputStream mCIS;
    public MediaCryptApp() {

    }

    public static CipherInputStream decryptAESBuffer(String filename, String mackey) {
        byte [] mIV = new byte[16];
        for(int idx=0; idx<16; idx++)
            mIV[idx] = 0;

        String[] macitem = mackey.split(":");
        Log.i(TAG, "macitem " + macitem);
        for(int idx=0; idx<macitem.length; idx++) {
            mIV[idx] = (byte)Integer.parseInt(macitem[idx], 16);
        }

        String entrypt_file_path = Environment.getExternalStorageDirectory() + "/cryptdata/" + filename;
        Log.i(TAG, " entrypt_file_path " + entrypt_file_path + " key = " + mIV[0] + mIV[1] + mIV[2] + mIV[3] + mIV[4]+mIV[5]+mIV[6]);
        try {
            FileInputStream fis = new FileInputStream(entrypt_file_path);
            SecretKeySpec sks = new SecretKeySpec(mIV, "AES");
            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //byte[] salt = new byte[8];
            IvParameterSpec ivParameterSpec = new IvParameterSpec(mIV);
            cipher.init(Cipher.DECRYPT_MODE, sks, ivParameterSpec);
            if(mCIS != null) {
                try {
                    mCIS.close();
                    mCIS = null;
                }catch (IOException e) {

                }
            }
            mCIS = new CipherInputStream(fis, cipher);
            //cis.
            return mCIS;
        }catch (FileNotFoundException e) {
            Log.i(TAG, " " + entrypt_file_path + " not found");
        } catch (NoSuchPaddingException e) {
            Log.i(TAG, " decrypt media file failed, NoSuchPaddingException");
        } catch (NoSuchAlgorithmException e) {
            Log.i(TAG, " decrypt media file failed, NoSuchAlgorithmException");
        } catch (InvalidKeyException e) {
            Log.i(TAG, " decrypt media file failed, InvalidKeyException");
        } catch (InvalidAlgorithmParameterException e) {
            Log.i(TAG, " decrypt media file failed, InvalidAlgorithmParameterException" + e.getMessage());
        }

        return null;
    }
}
