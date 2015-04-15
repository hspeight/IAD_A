package com.itsadate.iad_a;


import android.os.AsyncTask;

import java.io.InputStream;
import java.net.URL;

public class SetWallpaperAsyncTask extends AsyncTask<String, Void, String> {

    @Override
    protected String doInBackground(String... params) {
        String URL = "path/of/remote/image";
        setWallpaper(URL);
        return "Executed";
    }

    @Override
    protected void onPostExecute(String result) {
    }

    @Override
    protected void onPreExecute() {
    }

    @Override
    protected void onProgressUpdate(Void... values) {
    }

    private void setWallpaper(String url) {
        try {
            //WallpaperManager wpm = WallpaperManager.getInstance(getApplicationContext());
            InputStream ins = new URL(url).openStream();
            //wpm.setStream(ins);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}

