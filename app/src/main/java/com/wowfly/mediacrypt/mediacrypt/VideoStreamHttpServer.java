package com.wowfly.mediacrypt.mediacrypt;

import android.util.Log;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

import javax.crypto.CipherInputStream;

/**
 * Created by user on 9/12/14.
 */
public class VideoStreamHttpServer extends NanoHTTPD {
    private static final String TAG = "VideoStreamHttpServer";
    CipherInputStream mVIS;

    public VideoStreamHttpServer(CipherInputStream is) {
        super("127.0.0.1", 10086);
        mVIS = is;
    }

    public Response serve(IHTTPSession session) {
        Method method = session.getMethod();
        String uri = session.getUri();
        Response resp = null;

        Log.i(TAG, " method " + method.name() + " uri " + uri);

        //Response.Status.
        //Response resp =  new NanoHTTPD.Response(Response.Status.OK, "video/mp4", mVIS);
        //resp.addHeader("Content-Length", "" + 28179121);
            //resp = new NanoHTTPD.Response(Response.Status.OK, "video/mp4", new FileInputStream(new File("/storage/sdcard0/cartoon.mp4")));
        VideoInputStream vis = new VideoInputStream(mVIS);
        resp =  new NanoHTTPD.Response(Response.Status.OK, "video/mp4", vis);
        return resp;
    }
}
