package com.aikidonord.utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.File;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Date;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;

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
	            Log.e("Exception",e.getMessage());
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
