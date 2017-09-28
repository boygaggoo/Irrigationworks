package com.example.administrator.irrigationworks.UtilsTozals;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * 上传图片工具类
 */
public class ImageUpUtils {
	public static String main(String token,String uri,String[] file) {
        return new ImageUpUtils().uploadFile(token,uri,file);
	}
	public static String uploadFile(String token,String actionUrl, String[] uploadFilePaths) {
        String end = "\r\n";
        String twoHyphens = "--";
        String boundary = "*****";

        DataOutputStream ds = null;
        InputStream inputStream = null;
        InputStreamReader inputStreamReader = null;
        BufferedReader reader = null;
        StringBuffer resultBuffer = new StringBuffer();
        String tempLine = null;
        HttpURLConnection httpURLConnection;
        String result="";
        try {
            // ͳһ��Դ
            URL url = new URL(actionUrl);
            // ������ĸ��࣬������
            URLConnection urlConnection = url.openConnection();
            // http��������
             httpURLConnection = (HttpURLConnection) urlConnection;

            // �����Ƿ��httpUrlConnection���룬Ĭ���������true;
            httpURLConnection.setDoInput(true);
            // �����Ƿ���httpUrlConnection���
            httpURLConnection.setDoOutput(true);
            // Post ������ʹ�û���
            httpURLConnection.setUseCaches(false);
            // �趨����ķ�����Ĭ����GET
            httpURLConnection.setRequestMethod("POST");
            // �����ַ��������Ӳ���
            httpURLConnection.setRequestProperty("Connection", "Keep-Alive");
            // �����ַ�����
            httpURLConnection.setRequestProperty("Charset", "UTF-8");
            // ����������������
            httpURLConnection.setRequestProperty("Content-Type", "multipart/form-data;boundary=" + boundary);

            httpURLConnection.setRequestProperty("token", token);
            // ����DataOutputStream
            ds = new DataOutputStream(httpURLConnection.getOutputStream());
            for (int i = 0; i < uploadFilePaths.length; i++) {
                String uploadFile = uploadFilePaths[i];
                String filename = uploadFile.substring(uploadFile.lastIndexOf("//") + 1);
                ds.writeBytes(twoHyphens + boundary + end);
                ds.writeBytes("Content-Disposition: form-data; " + "name=\"file\";filename=\"" + filename
                        + "\"" + end);
                ds.writeBytes("Content-Type: application/octet-stream;chartset=UTF-8"+end);
                ds.writeBytes(end);

                FileInputStream fStream = new FileInputStream(uploadFile);
                int bufferSize = 1024;
                byte[] buffer = new byte[bufferSize];
                int length = -1;
                while ((length = fStream.read(buffer)) != -1) {
                    ds.write(buffer, 0, length);
                }
                ds.writeBytes(end);
                /* close streams */
                fStream.close();
            }
            ds.writeBytes(twoHyphens + boundary + twoHyphens + end);
            /* close streams */
            ds.flush();



            if (httpURLConnection.getResponseCode() >= 300) {
                throw new Exception(
                        "HTTP Request is not success, Response code is " + httpURLConnection.getResponseCode());
            }

            if (httpURLConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = httpURLConnection.getInputStream();
                inputStreamReader = new InputStreamReader(inputStream);
                reader = new BufferedReader(inputStreamReader);
                tempLine = null;
                resultBuffer = new StringBuffer();
                while ((tempLine = reader.readLine()) != null) {
                    resultBuffer.append(tempLine);
                    resultBuffer.append("\n");
                }
                System.err.println(resultBuffer);
            }
            int res = httpURLConnection.getResponseCode();
            int ss;
            InputStream input = null;
                input = httpURLConnection.getInputStream();
                while ((ss = input.read()) != -1) {
                    resultBuffer.append((char) ss);
                }
            result = resultBuffer.toString();
                Log.d("bh", "图片上传成功resultresult" + result);
                return result;

        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.d("qcc","图片上传失败"+e.toString());
            e.printStackTrace();
            return "图片上传失败";
        } finally {
            if (ds != null) {
                try {
                    ds.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d("li", "图片上传失败" + e.toString());
                    e.printStackTrace();
                }
            }
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                    Log.d("li", "图片上传失败" + e.toString());
                }
            }
            if (inputStreamReader != null) {
                try {
                    inputStreamReader.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d("qcc", "图片上传失败" + e.toString());
                    e.printStackTrace();
                }
            }
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    Log.d("li", "图片上传失败" + e.toString());
                    e.printStackTrace();
                }
            }
            Log.d("li", "图片上传成功resultBuffer" + resultBuffer);

            return result;
        }
    }



}
