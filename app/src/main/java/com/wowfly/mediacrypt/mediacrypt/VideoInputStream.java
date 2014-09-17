package com.wowfly.mediacrypt.mediacrypt;

import android.util.Log;

import java.io.IOException;
import java.io.InputStream;

import javax.crypto.CipherInputStream;

/**
 * Created by user on 9/12/14.
 */
public class VideoInputStream extends InputStream {
    private static final String TAG = "VideoInputStream";
    CipherInputStream cis;

    public VideoInputStream(CipherInputStream cipher) {
        cis = cipher;
        //cis.
    }

    public int read() {
        int n;
        try {
            n = cis.read();
            //Log.i(TAG, " read() " + n);
            return n&0xff;
        } catch (IOException e) {

        }
        return 0;
    }

    public int read(byte[] buf) {
        int n;
        try {
            n = cis.read(buf);
            Log.i(TAG, " read(buf[]) " + n + " len " + buf.length);
            return n;
        }catch (IOException e) {

        }
        return 0;
    }

    public long skip(long count) {
        long n;
        try {
            n = cis.skip(count);
            Log.i(TAG, " skip("+ count+") " + n);
            return n;
        } catch (IOException e) {

        }
        return 0;
    }

    public int available() {
        int n;
        try {
            n= cis.available();
            Log.i(TAG, " available " + n);
            return n;
        }catch (IOException e) {

        }
        return 0;
    }


}
