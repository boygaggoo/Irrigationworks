package com.example.administrator.irrigationworks.Ui.UpData;

import android.app.ProgressDialog;
import android.os.Environment;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownLoadManager {

	/**
	 * 从服务器下载apk(带进度条)
	 * @param path
	 * @param pd
	 * @return
	 * @throws Exception
	 */
	public static File getFileFromServer(String path, ProgressDialog pd) throws Exception{
		//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
		if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
			URL url = new URL(path);
			HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
			conn.setConnectTimeout(5000);
			//获取到文件的大小 
			pd.setMax(conn.getContentLength());
			InputStream is = conn.getInputStream();
			File file = new File(Environment.getExternalStorageDirectory(), "weather.apk");
			FileOutputStream fos = new FileOutputStream(file);
			BufferedInputStream bis = new BufferedInputStream(is);
			byte[] buffer = new byte[1024];
			int len ;
			int total=0;
			while((len =bis.read(buffer))!=-1){
				fos.write(buffer, 0, len);
				total+= len;
				//获取当前下载量
				pd.setProgress(total);
			}
			fos.close();
			bis.close();
			is.close();
			return file;
		}
		else{
			return null;
		}
	}
	
	/**
	 * 从服务器下载apk(不带进度条)
	 * @param path
	 * @return
	 * @throws Exception
	 */
	public static File getFileFromServer(String path){
		try{
			//如果相等的话表示当前的sdcard挂载在手机上并且是可用的
			if(Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
				URL url = new URL(path);
				HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(20000);
				InputStream is = conn.getInputStream();
				File file = new File(Environment.getExternalStorageDirectory(), "weather.apk");
				FileOutputStream fos = new FileOutputStream(file);
				BufferedInputStream bis = new BufferedInputStream(is);
				byte[] buffer = new byte[1024];
				int len ;
				while((len =bis.read(buffer))!=-1){
					fos.write(buffer, 0, len);
				}
				fos.close();
				bis.close();
				is.close();
				return file;
			}
			else{
				return null;
			}
		}catch(Exception e){
			e.getStackTrace();
			return null;
		}
	}

}
