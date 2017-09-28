package com.example.administrator.irrigationworks.Ui.UpData;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.util.Xml;


import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.nostra13.universalimageloader.utils.L;

import org.xmlpull.v1.XmlPullParser;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class UpdateOperation {
	
	private final int UPDATA_NONEED = 0;
	private final int UPDATA_CLIENT = 1;
	private final int GET_UNDATAINFO_ERROR = 2;
	private final int SDCARD_NOMOUNTED = 3;
	private final int SDCARD_HINT = 5;
	private final int DOWN_ERROR = 4;
	public static boolean showed=false;
	private final String TAG = "UPDATE:";
	private UpdataInfo info;
	private Context activity;
	
	private String cmd_install = "pm install -r ";
    String apkLocation = Environment.getExternalStorageDirectory().toString()
            + "/";
	
	/* 
	 * 用pull解析器解析服务器返回的xml文件 (xml封装了版本号) 
	 */  


    public static String replaceBlank(String str) {

        String dest = "";

        if (str!=null) {

            Pattern p = Pattern.compile("\\s*|\t|\r|\n");

            Matcher m = p.matcher(str);

            dest = m.replaceAll("");

        }

        return dest;

    }
	public UpdateOperation(Context activity){
		this.activity=activity;
		//checkUpdate();
	}
	public void checkUpdate(){
		try {
			CheckVersionTask cv = new CheckVersionTask();
			L.d("test", "updates ");
			new Thread(cv).start();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			L.d("error",e.toString());
		}
	}
	public static UpdataInfo getUpdataInfo(InputStream is) throws Exception{
	    XmlPullParser  parser = Xml.newPullParser();
	    parser.setInput(is, "utf-8");//设置解析的数据源
	    int type = parser.getEventType();
	    UpdataInfo info = new UpdataInfo();//实体
//	    while(type != XmlPullParser.END_DOCUMENT ){
		final int depth = parser.getDepth();

		while ((type != XmlPullParser.END_TAG || parser
				.getDepth() > depth) && type != XmlPullParser.END_DOCUMENT) {
			switch (type) {
	        case XmlPullParser.START_TAG:
	            if("version".equals(parser.getName())){
	                info.setVersion(replaceBlank(parser.nextText())); //获取版本号
	            }else if ("url".equals(parser.getName())){
	                info.setUrl(replaceBlank(parser.nextText())); //获取要升级的APK文件
	            }else if ("description".equals(parser.getName())){
	                info.setDescription(replaceBlank(parser.nextText())); //获取该文件的信息
	            }
	            break;
	        }
	        type = parser.next();
	    }
	    return info;  
	}
	
	/* 
	 * 从服务器获取xml解析并进行比对版本号  
	 */  
	private int getversionCode() throws Exception{  
	    //获取packagemanager的实例   
	    PackageManager packageManager = activity.getPackageManager();  
	    //getPackageName()是你当前类的包名，0代表是获取版本信息  
	    PackageInfo packInfo = packageManager.getPackageInfo(activity.getPackageName(), 0);  
	    return packInfo.versionCode;   
	}  
	
	public class CheckVersionTask implements Runnable{  
	    public void run() {  
	        try {
	            //从资源文件获取服务器 地址   
	            String path = FinalTozal.updateUrlapp;//activity.getResources().getString(R.string.serverurl);
	            //包装成url的对象   
	            URL url = new URL(path);
	            HttpURLConnection conn =  (HttpURLConnection) url.openConnection();
	            conn.setConnectTimeout(FinalTozal.requestTimeOut);
	            conn.setReadTimeout(FinalTozal.readTimeOut);
	            InputStream is =conn.getInputStream();
	            info =  getUpdataInfo(is);
	            int netVersion = Integer.parseInt(info.getVersion());
	            if(netVersion > getversionCode()){
	            	Log.i(TAG,"版本号不同 ,提示用户升级 ");  
	                Message msg = new Message();  
	                msg.what = UPDATA_CLIENT;  
	                handler.sendMessage(msg);
	            }else{
					Message msg = new Message();
					msg.what = SDCARD_HINT;
					handler.sendMessage(msg);

					Log.i(TAG,"无需升级");
	            }
	        } catch (Exception e) {
	            // 待处理
				Log.d("qc","更新失败的原因"+e.toString());
	            Message msg = new Message();
	            msg.what = GET_UNDATAINFO_ERROR;
	            handler.sendMessage(msg);
	            e.printStackTrace();
	        }
	    }  
	}  
	  
	Handler handler = new Handler(){  
	      
	    @Override  
	    public void handleMessage(Message msg) {  
	        // TODO Auto-generated method stub  
	        super.handleMessage(msg);  
	        switch (msg.what) {  
	        case UPDATA_CLIENT:  
//	            //对话框通知用户升级程序   
	            showUpdataDialog();
	            
	            /*
	            //后台自动升级程序
	        	new Thread(){  
	    	        @Override  
	    	        public void run() {  
	    	            try {  
	    	                File file = DownLoadManager.getFileFromServer(info.getUrl());  
//	    	                sleep(3000); //    /storage/emulated/0/weather.apk
//	    	                installApk(file);
	    	                
//	    	                silentInstallApk(file.getPath().toString());
	    	                String cmd = cmd_install + file.getPath().toString();
	    	                excuteSuCMD(cmd);
	    	                
//	    	                pd.dismiss(); //结束掉进度条对话框  
	    	            } catch (Exception e) {  
	    	                Message msg = new Message();  
	    	                msg.what = DOWN_ERROR;  
	    	                handler.sendMessage(msg);  
	    	                e.printStackTrace();  
	    	                showed=false;
	    	            }  
	    	        }}.start();
	    	        */
	            break;  
	        case GET_UNDATAINFO_ERROR:  
	            //服务器超时   
	           UtilsTool.showShortToast(activity.getApplicationContext(), "获取服务器更新信息失败");
	            
	            break;    
	        case DOWN_ERROR:  
	            //下载apk失败  
				UtilsTool.showShortToast(activity.getApplicationContext(), "下载新版本失败");
	              
	            break;
				case SDCARD_HINT:
					//下载apk失败
					UtilsTool.showShortToast(activity.getApplicationContext(), "已经是最新版本，无需升级");

					break;

			}
	    }  
	};  
	  
	/* 
	 *  
	 * 弹出对话框通知用户更新程序  
	 *  
	 * 弹出对话框的步骤： 
	 *  1.创建alertDialog的builder.   
	 *  2.要给builder设置属性, 对话框的内容,样式,按钮 
	 *  3.通过builder 创建一个对话框 
	 *  4.对话框show()出来   
	 */  
	protected void showUpdataDialog() {
		if(!showed){
		    Builder builer = new Builder(activity) ;
		    builer.setTitle("版本升级");  
		    builer.setMessage(info.getDescription());  
		    //当点确定按钮时从服务器上下载 新的apk 然后安装
			//封全，不要选择，自动更新
//		    builer.setPositiveButton("确定", new OnClickListener() {
//		    public void onClick(DialogInterface dialog, int which) {
//		            Log.i(TAG,"下载apk,更新");
//		            downLoadApk();
//		        }
//		    });
			downLoadApk();
		    //当点取消按钮时进行登录  
//		    builer.setNegativeButton("取消", new OnClickListener() {
//		        public void onClick(DialogInterface dialog, int which) {
//		        	showed=true;
//		        }
//		    });
		    try{
		    	AlertDialog dialog = builer.create();  
		    	dialog.show(); 
		    }catch(Exception ex){
		    	
		    }
		    showed=true; 
		}
	}  
	  
	/* 
	 * 从服务器中下载APK 
	 */  
	protected void downLoadApk() {  
	    final ProgressDialog pd;    //进度条对话框  
	    pd = new  ProgressDialog(activity);  
	    pd.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);  
	    pd.setMessage("正在下载更新");  
	    pd.show();  
	    new Thread(){  
	        @Override  
	        public void run() {  
	            try {  
	                File file = DownLoadManager.getFileFromServer(info.getUrl(), pd);  
	                sleep(3000);  
	                installApk(file);  
	                pd.dismiss(); //结束掉进度条对话框  
	            } catch (Exception e) {  
	                Message msg = new Message();  
	                msg.what = DOWN_ERROR;  
	                handler.sendMessage(msg);  
	                e.printStackTrace();  
	                showed=false;
	            }  
	        }}.start();  
	}  
	  
	//安装apk   
	protected void installApk(File file) {  
	    Intent intent = new Intent();  
	    //执行动作  
	    intent.setAction(Intent.ACTION_VIEW);  
	    //执行的数据类型  
	    intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");  
	    activity.startActivity(intent);  
	    showed=false;
	}
	
	/**
	 * 
	 * 静默安装apk
	 * @param apkAbsolutePath 安装应用的路径
	 */
	protected void silentInstallApk(String apkAbsolutePath) {  
		//初始化pm指令
        String[] args = {"pm", "install", "-r", apkAbsolutePath};
        //进程生成器，将指令封装成独立的进程，进行调用
        ProcessBuilder processBuilder = new ProcessBuilder(args);
        //进程
        Process process = null;
        InputStream errIs = null;
        InputStream inIs = null;
        try {
            //字节流，临时存储控制台的输出内容
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int read = -1;
            //执行安装apk的pm指令
            process = processBuilder.start();
            //接收控制台输出的异常情况，输入流
            errIs = process.getErrorStream();
            while ((read = errIs.read()) != -1) {
                baos.write(read);
            }
            baos.write("\r\n".getBytes());
            //正常输出
            inIs = process.getInputStream();
            while ((read = inIs.read()) != -1) {
                baos.write(read);
            }
            byte[] data = baos.toByteArray();
            //将控制台输出转化为字符串返回
//            String result = new String(data);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {//最后必须将所有输入输出流关闭
            try {
                if (errIs != null) {
                    errIs.close();
                }
                if (inIs != null) {
                    inIs.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
            if (process != null) {
                process.destroy();
            }
        }
	    showed=false;
	}
	
	//执行shell命令
    protected int excuteSuCMD(String cmd) {
        try {
            Process process = Runtime.getRuntime().exec("su");
            DataOutputStream dos = new DataOutputStream(
                    (OutputStream) process.getOutputStream());
            // 部分手机Root之后Library path 丢失，导入library path可解决该问题
            dos.writeBytes((String) "export LD_LIBRARY_PATH=/vendor/lib:/system/lib\n");
            cmd = String.valueOf(cmd);
            dos.writeBytes((String) (cmd + "\n"));
            dos.flush();
            dos.writeBytes("exit\n");
            dos.flush();
            process.waitFor();
            int result = process.exitValue();
            showed=false;
            return (Integer) result;
        } catch (Exception localException) {
            localException.printStackTrace();
            return -1;
        }
    }
}
