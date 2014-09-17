package com.wowfly.mediacrypt.mediacrypt;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;


public class MediaCryptActivity extends Activity {
    private static final String TAG = "MediaCryptActivity";
    private ListView listView;
    private ArrayList<MediaItemInfo> mMediaList;
    VideoStreamHttpServer mVSD;

    public class MediaItemInfo {
        int thumb;
        String mName;
        String mPath;

        public MediaItemInfo(int icon, String name, String path) {
            thumb = icon;
            mName = name;
            mPath = path;
        }
    }

    public MediaCryptActivity() {
        mMediaList = new ArrayList<MediaItemInfo>();
        MediaItemInfo ti;
        ti = new MediaItemInfo(R.drawable.wallpaper_00_small, " 示例图片01", "wallpaper_00.aesi");
        mMediaList.add(ti);
        ti = new MediaItemInfo(R.drawable.wallpaper_01_small, " 示例图片02", "wallpaper_01.aesi");
        mMediaList.add(ti);
        ti = new MediaItemInfo(R.drawable.wallpaper_02_small, " 示例图片03", "wallpaper_02.aesi");
        mMediaList.add(ti);
        ti = new MediaItemInfo(R.drawable.wallpaper_03_small, " 示例图片04", "wallpaper_03.aesi");
        mMediaList.add(ti);
        ti = new MediaItemInfo(R.drawable.wallpaper_04_small, " 示例图片05", "wallpaper_04.aesi");
        mMediaList.add(ti);
        ti = new MediaItemInfo(R.drawable.cartoon_small, "视频", "cartoon.aesv");
        mMediaList.add(ti);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_media_crypt);
        listView = (ListView) findViewById(R.id.medialist);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                String resID = mMediaList.get(i).mPath;
                int sep = resID.lastIndexOf('.');
                String ext = resID.substring(sep+1);
                if(ext.equals("aesi")) {
                    Intent intent = new Intent(getBaseContext(), ImageViewer.class);
                    intent.putExtra("filename", resID);
                    startActivity(intent);
                } else if(ext.equals("aesv")) {
                    if(mVSD != null) {
                        mVSD.stop();
                        mVSD = null;
                    }

                    WifiManager mWiFi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                    WifiInfo wi = mWiFi.getConnectionInfo();
                    String mackey = wi.getMacAddress();
                    Log.i(TAG, " MAC address =" + mackey);
                    //InputStream is = MediaCryptApp.decryptAESBuffer(resID, mackey);
                    mVSD = new VideoStreamHttpServer(MediaCryptApp.decryptAESBuffer(resID, mackey));
                    try {
                        mVSD.start();
                    }catch (IOException ioe) {
                        Log.i(TAG, " start http server failed, " + ioe.getMessage());
                    }
/*                    String tempuri;
                    try {
                        File temp = File.createTempFile("aes-decrypt", "fd");
                        tempuri = temp.getAbsolutePath();
                        Log.i(TAG, "temp video path = " + tempuri);
                        FileOutputStream of = new FileOutputStream(temp);
                        BufferedOutputStream bos = new BufferedOutputStream(of);
                        WifiManager mWiFi = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
                        WifiInfo wi = mWiFi.getConnectionInfo();
                        String mackey = wi.getMacAddress();
                        Log.i(TAG, " MAC address =" + mackey);
                        InputStream is = MediaCryptApp.decryptAESBuffer(resID, mackey);


                        byte buf[] = new byte[1024];
                        int total = 0;
                        do {
                            int nread = is.read(buf);
                            if(nread <= 0) {
                                Log.i(TAG, "decrypt video finished");
                                break;
                            }

                            total += nread;
                            Log.i(TAG, "decrypt video stream read " + nread + " total " + total);
                            bos.write(buf, 0, nread);
                        } while(true);

                        //of.close();
                        //is.close();
                        Log.i(TAG, "start video player now!");*/
                    Intent intent = new Intent(getBaseContext(), VideoPlayerActivity.class);
                    intent.putExtra("uri", resID);
                    startActivity(intent);
                }
            }
        });
        listView.setAdapter(new MediaItemAdapter());
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.media_crypt, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private class MediaItemAdapter extends BaseAdapter {
        public int getCount() {
            return mMediaList.size();
        }

        public Object getItem(int position) {
            return null;
        }

        public long getItemId(int position) {
            return position;
        }

        public View getView(int pos, View view, ViewGroup parent) {
            View myview = view;

            if(myview == null) {
                myview = MediaCryptActivity.this.getLayoutInflater().inflate(R.layout.item_list_media, parent, false);

                ImageView icon = (ImageView) myview.findViewById(R.id.media_thumb);
                TextView name = (TextView) myview.findViewById(R.id.media_name);

                icon.setImageResource(mMediaList.get(pos).thumb);
                name.setText(mMediaList.get(pos).mName);
                //myview.setTag(holder);
            } else {
                //holder = (ViewHolder) view.getTag();
            }
            return myview;
        }
    }
}
