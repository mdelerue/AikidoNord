package com.aikidonord.utils;

/*
Copyright (C) 2013  Marc Delerue

This program is free software; you can redistribute it and/or
modify it under the terms of the GNU General Public License
as published by the Free Software Foundation; either version 2
of the License, or (at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program; if not, write to the Free Software
Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301, USA.
 */

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

public class DrawableOperation {


    public static Bitmap getBitmapFromURL(String src) {
        try {
            //Log.e("src",src);
            URL url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);
            //Log.e("Bitmap","returned");
            return myBitmap;
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("Exception", e.getMessage());
            return null;
        }
    }


    public static Bitmap getBitmapFromStorage(String id, Date date, Context context) {

        String filename = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AikidoNord/" + id + "_" + date.getTime();
        try {
            Bitmap myBitmap = BitmapFactory.decodeFile(filename);
            return myBitmap;
        } catch (Exception e) {
            return null;
        }
    }

    public static String saveThumbnailOnStorage(String src, String id, Date date, Context context) {

        URL url;
        try {
            // get the Bitmap
            url = new URL(src);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap myBitmap = BitmapFactory.decodeStream(input);

            // create the directory if needed and set the filename

            String fullPath = Environment.getExternalStorageDirectory().getAbsolutePath() + "/AikidoNord";

            File m_Dir = new File(fullPath);
            if (!m_Dir.exists()) {
                m_Dir.mkdir();
            }


            OutputStream fOut = null;
            File file = new File(m_Dir, id + "_" + date.getTime());
            file.createNewFile();
            fOut = new FileOutputStream(file);

            // 100 means no compression, the lower you go, the stronger the compression
            myBitmap.compress(Bitmap.CompressFormat.PNG, 50, fOut);
            fOut.flush();
            fOut.close();

            MediaStore.Images.Media.insertImage(context.getContentResolver(), file.getAbsolutePath(), file.getName(), file.getName());


            return file.getAbsolutePath();


        } catch (Exception e) {
            return null;
        }


    }

}
