package com.example.administrator.irrigationworks.Ui.Retrofit;


import android.os.Handler;
import android.os.Looper;


import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Jpush.ThreadManager;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

/** 
 * Http请求的工具类 
 *  
 * @author zhy 
 *  
 */  
public class HttpUtils  
{
    public static class AsyncRun {
        public static void run(Runnable runnable){
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(runnable);
        }
    }
    /**
     * 获取回调
     */
    public interface HttpResponseCallBack {
        void onSuccess(String result);

        void onFailure(String result, Exception e);
    }
    public static void get(final String url, final Map<String, String> params, final HttpResponseCallBack callBack) {
        if (callBack != null) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    try {
                        //拼装请求参数列表
                        final StringBuilder sb = new StringBuilder(64);
                        if (params != null) {
                            sb.append("?");
                            for (Map.Entry<String, String> entry : params.entrySet()) {
                                sb.append(entry.getKey());
                                sb.append("=");
                                sb.append(entry.getValue());
                                sb.append("&");
                            }
                            sb.deleteCharAt(sb.length() - 1);
                        }

                        //构建新连接
                        URL httpUrl = new URL(url + sb.toString());
                        HttpURLConnection conn = (HttpURLConnection) httpUrl.openConnection();
                        conn.setConnectTimeout(3000);
                        conn.setReadTimeout(3000);
                        conn.setRequestMethod("GET");
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(conn.getInputStream(), "UTF-8"));

                        //接受返回数据
                        sb.setLength(0);
                        String strTemp;
                        while ((strTemp = br.readLine()) != null) {
                            sb.append(strTemp).append('\n');
                        }

                        conn.disconnect();

                        AsyncRun.run(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onSuccess(sb.toString());
                            }
                        });

                    } catch (final IOException e) {
                        AsyncRun.run(new Runnable() {
                            @Override
                            public void run() {
                                callBack.onFailure("失败信息：", e);
                            }
                        });
                    }
                }
            }).start();
        }
    }

    private static final int TIMEOUT_IN_MILLIONS = 60000;  
  
    public interface CallBack  
    {  
        void onRequestComplete(String result);
    }  
  
  
    /** 
     * 异步的Get请求 
     *
     * @param urlStr
     * @param callBack
     */  
    public static void doGetAsyn(final String urlStr, final CallBack callBack)
    {

        ThreadManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try
                {
                    String result = doGet(urlStr);
                    if (callBack != null)
                    {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

    }
  
    /** 
     * 异步的Post请求 
     * @param urlStr 
     * @param params 
     * @param callBack 
     * @throws Exception
     */  
    public static void doPostAsyn(final String urlStr, final String params,
                                  final CallBack callBack) throws Exception
    {
        ThreadManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try
                {
                    String result = doPost(urlStr, params);
                    if (callBack != null)
                    {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });

  
    }
    public static void doCallbackPostAsyn(final String urlStr, final String params,
                                          final DataCallBack callBack) throws Exception
    {
        ThreadManager.getThreadPool().execute(new Runnable() {
            @Override
            public void run() {
                try
                {
                    String result = doPost(urlStr, params);
                    if (callBack != null)
                    {
                        callBack.onRequestComplete(result);
                    }
                } catch (Exception e)
                {
                    e.printStackTrace();
                }
            }
        });


    }

     public interface DataCallBack {
         void onRequestComplete(String result);
       }

    /** 
     * Get请求，获得返回数据 
     *  
     * @param urlStr 
     * @return 
     * @throws Exception
     */  
    public static String doGet(String urlStr)
    {  
        URL url = null;
        HttpURLConnection conn = null;
        InputStream is = null;
        ByteArrayOutputStream baos = null;
        try  
        {  
            url = new URL(urlStr);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);  
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);  
            conn.setRequestMethod("GET");  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            if (conn.getResponseCode() == 200)  
            {  
                is = conn.getInputStream();  
                baos = new ByteArrayOutputStream();
                int len = -1;  
                byte[] buf = new byte[128];  
  
                while ((len = is.read(buf)) != -1)  
                {  
                    baos.write(buf, 0, len);  
                }  
                baos.flush();  
                return baos.toString();  
            } else  
            {  
                throw new RuntimeException(" responseCode is not 200 ... ");
            }  
  
        } catch (Exception e)
        {  
            e.printStackTrace();  
        } finally  
        {  
            try  
            {  
                if (is != null)  
                    is.close();  
            } catch (IOException e)
            {  
            }  
            try  
            {  
                if (baos != null)  
                    baos.close();  
            } catch (IOException e)
            {  
            }  
            conn.disconnect();  
        }  
          
        return null ;  
  
    }  
  
    /**  
     * 向指定 URL 发送POST方法的请求  
     *   
     * @param url  
     *            发送请求的 URL  
     * @param param  
     *            请求参数，请求参数应该是 name1=value1&name2=value2 的形式。  
     * @return 所代表远程资源的响应结果  
     * @throws Exception
     */  
    public static String doPost(String url, String param)
    {
        PrintWriter out = null;
        BufferedReader in = null;
        String result = "";
        try  
        {  
            URL realUrl = new URL(url);
            // 打开和URL之间的连接  
            HttpURLConnection conn = (HttpURLConnection) realUrl
                    .openConnection();  
            // 设置通用的请求属性  
            conn.setRequestProperty("accept", "*/*");  
            conn.setRequestProperty("connection", "Keep-Alive");  
            conn.setRequestMethod("POST");  
            conn.setRequestProperty("Content-Type","application/json;charset=UTF-8");
            conn.setRequestProperty("Charset", "utf-8");
            conn.setRequestProperty("token", NimUIKit.getToken());
            conn.setRequestProperty("charset", "utf-8");  
            conn.setUseCaches(false);  
            // 发送POST请求必须设置如下两行  
            conn.setDoOutput(true);  
            conn.setDoInput(true);  
            conn.setReadTimeout(TIMEOUT_IN_MILLIONS);  
            conn.setConnectTimeout(TIMEOUT_IN_MILLIONS);  
  
            if (param != null && !param.trim().equals(""))  
            {  
                // 获取URLConnection对象对应的输出流  
                out = new PrintWriter(conn.getOutputStream());
                // 发送请求参数  
                out.print(param);
                // flush输出流的缓冲  
                out.flush();  
            }  
            // 定义BufferedReader输入流来读取URL的响应  
            in = new BufferedReader(
                    new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = in.readLine()) != null)  
            {  
                result += line;  
            }  
        } catch (Exception e)
        {  
            e.printStackTrace();  
        }  
        // 使用finally块来关闭输出流、输入流  
        finally  
        {  
            try  
            {  
                if (out != null)  
                {  
                    out.close();  
                }  
                if (in != null)  
                {  
                    in.close();  
                }  
            } catch (IOException ex)
            {  
                ex.printStackTrace();  
            }  
        }  
        return result;  
    }  
}  