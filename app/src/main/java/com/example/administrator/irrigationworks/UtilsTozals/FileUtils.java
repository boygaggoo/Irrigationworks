package com.example.administrator.irrigationworks.UtilsTozals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by Administrator on 2017/4/20.
 */
public class FileUtils {
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
    /**
     * activityList:所有activity对象，用于退出时全部finish; Activity走onCreate时，添加到该集合
     */





    /**
     * 删除文件
     *
     * @param file
     */
    public static void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

    /**
     * 判断文件是否存在
     * @return
     */
    public static boolean fileIsExists(String fileName){
        if(fileName!=null){
            File f=new File(fileName);
            if(f.exists()){
                return true;
            }
        }
        return true;
    }

    /**
     * 获取SD卡路径
     *
     * @return
     */
    public static String getSDCardPath() {
        // File file0=new File("/mnt/usbdisk/weatherApp/");



        File file1 = new File("/mnt/usb/sda1/weatherApp/");
        File file2 = new File("/mnt/usb/sdb1/weatherApp/");
        File file3 = new File("/mnt/sdb/sda1/weatherApp/");
        File file4 = new File("/mnt/sdb/sdb1/weatherApp/");
        File file5 = new File("/storage/external_storage/sda1/weatherApp/");
        File file6 = new File("/storage/external_storage/sdb1/weatherApp/");
        // File file7=new File("/sdcard/weatherApp/");

        // if(file0!=null&&file0.exists()){
        // return file0.getAbsolutePath();
        // }else
        if (file1 != null && file1.exists()) {
            return file1.getAbsolutePath();
        } else if (file2 != null && file2.exists()) {
            return file2.getAbsolutePath();
        } else if (file3 != null && file3.exists()) {
            return file3.getAbsolutePath();
        } else if (file4 != null && file4.exists()) {
            return file4.getAbsolutePath();
        } else if (file5 != null && file5.exists()) {
            return file5.getAbsolutePath();
        } else if (file6 != null && file6.exists()) {
            return file6.getAbsolutePath();
        }
        // else if(file7!=null&&file7.exists()){
        // return file7.getAbsolutePath();
        // }
        else {
            String filepath = Environment.getExternalStorageDirectory() + "/weatherApp/";
            File file=new File(filepath);
            if(!file.exists()){
                file.mkdir();
            }
            return filepath;
        }
    }

    /**
     * 创建文件夹及文件
     *
     */
    public static void CreateFile(String path, String fileName) throws IOException {
        File file = new File(path);
        if (!file.exists()) {
            try {
                // 按照指定的路径创建文件夹
                file.mkdirs();
            } catch (Exception e) {
                // TODO: handle exception
                e.printStackTrace();
            }
        }
        File dir = new File(path + fileName);
        if (!dir.exists()) {
            try {
                // 在指定的文件夹中创建文件
                dir.createNewFile();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 将JSON对象写入存储卡
     * @param data
     */
//	public static void writeJSONObjectToSdCard(JSONObject data,String path,String filename) throws IOException {
//		try {
//			CreateFile(path,filename);
//			String fileName = path + filename;
//			// 写入内存卡
//			PrintStream outputStream = null;
//			try {
//				outputStream = new PrintStream(new FileOutputStream(fileName));
//				outputStream.print(data.toString());
//			} catch (FileNotFoundException e) {
//				e.printStackTrace();
//			} finally {
//				if (outputStream != null) {
//					outputStream.close();
//				}
//			}
//
//		} catch (Exception e) {
//			// TODO: handle exception
//		}
//	}

    /**
     * 读文件，返回字符串
     *
     * @param path
     * @return
     */
    public static String ReadFile(String path) {
        File file = new File(path);
        BufferedReader reader = null;
        String laststr = "";
        try {
            // System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                // System.out.println("line " line ": " tempString);
                laststr = laststr + tempString;
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
            }
        }
        return laststr;
    }

    /**
     * 判断是否有网络连接
     * @param context
     * @return
     */
    public static boolean isNetworkConnected(Context context) {
        if (context != null) {
            ConnectivityManager mConnectivityManager = (ConnectivityManager) context
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo mNetworkInfo = mConnectivityManager.getActiveNetworkInfo();
            if (mNetworkInfo != null) {
                return mNetworkInfo.isAvailable();
            }
        }
        return false;
    }


//	/**
//	 * 根据路径获取Bitmap
//	 *
//	 * @param path
//	 * @return
//	 */
//	public synchronized static Bitmap getBitmapByPathx(String path) {
//
//		Bitmap bmp = null;
//		try {
//			BitmapFactory.Options o = new BitmapFactory.Options();
//			o.inJustDecodeBounds = true;
//			o.inSampleSize = 2;// computeSampleSize(o, -1, 128*128);
//			o.inJustDecodeBounds = false;
//
//			File f = new File(path);
//
//			InputStream is = new FileInputStream(f);
//			bmp = BitmapFactory.decodeStream(is, null, o);
//			is.close();
//			return bmp;
//		} catch (OutOfMemoryError err) {
//
//			FileUtils.LogD("out of memorry", err.toString() + path);
//			return null;
//		} catch (FileNotFoundException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//			return null;
//		}
//	}

    /**
     * 以最省内存的方式读取本地资源的图片
     * @param context
     * @param resId
     * @return
     */
    public synchronized static Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
        //获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

    /**
     * 以最省内存的方式读取本地资源的图片 或者SDCard中的图片
     * @param imagePath
     * 图片在SDCard中的路径
     * @return
     */
    public synchronized static Bitmap getSDCardImg(String imagePath){
//		BitmapFactory.Options opt = new BitmapFactory.Options();
//		opt.inPreferredConfig = Bitmap.Config.RGB_565;
//		opt.inPurgeable = true;
//		opt.inInputShareable = true;
//		//获取资源图片
//		return BitmapFactory.decodeFile(imagePath, opt);
        BitmapFactory.Options bfOptions=new BitmapFactory.Options();
        bfOptions.inDither=false;
        bfOptions.inPurgeable=true;
        bfOptions.inInputShareable=true;
        bfOptions.inTempStorage=new byte[32 * 1024];
        File file=new File(imagePath);
        FileInputStream fs=null;
        try {
            fs = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            if(fs!=null)
                return BitmapFactory.decodeFileDescriptor(fs.getFD(), null, bfOptions);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (OutOfMemoryError err) {
            //L.e("out of memorry", err.toString() + imagePath);
            return null;
        } finally{
            if(fs!=null) {
                try {
                    fs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }


    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static double getFileOrFilesSize(String filePath, int sizeType) {
        File file = new File(filePath);
        long blockSize = 0;
        try {
            if (file.isDirectory()) {
                blockSize = getFileSizes(file);
            } else {
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return FormetFileSize(blockSize, sizeType);
    }

    /**
     * 获取指定文件大小
     *
     * @param
     * @return
     * @throws Exception
     */
    private static long getFileSize(File file) throws Exception {
        long size = 0;
        if (file.exists()) {
            FileInputStream fis = null;
            fis = new FileInputStream(file);
            size = fis.available();

        } else {
            file.createNewFile();
        }
        return size;
    }

    /**
     * 获取指定文件夹
     * @param f
     * @return
     * @throws Exception
     */
    private static long getFileSizes(File f) throws Exception {
        long size = 0;
        File flist[] = f.listFiles();
        for (int i = 0; i < flist.length; i++) {
            if (flist[i].isDirectory()) {
                size = size + getFileSizes(flist[i]);
            } else {
                size = size + getFileSize(flist[i]);
            }
        }
        return size;
    }

    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    private static double FormetFileSize(long fileS, int sizeType) {
        DecimalFormat df = new DecimalFormat("#.00");
        double fileSizeLong = 0;
        switch (sizeType) {
            case SIZETYPE_B:
                fileSizeLong = Double.valueOf(df.format((double) fileS));
                break;
            case SIZETYPE_KB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1024));
                break;
            case SIZETYPE_MB:
                fileSizeLong = Double.valueOf(df.format((double) fileS / 1048576));
                break;
            case SIZETYPE_GB:
                fileSizeLong = Double.valueOf(df
                        .format((double) fileS / 1073741824));
                break;
            default:
                break;
        }
        return fileSizeLong;
    }

    public static int compareDate(String DATE1, String DATE2){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() >= dt2.getTime()) {//dt1大于dt2
                return 1;
            } else if (dt1.getTime() <= dt2.getTime()) {//dt1小于dt2
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }

    public static int compareDate1(String DATE1, String DATE2){
        DateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        try {
            Date dt1 = df.parse(DATE1);
            Date dt2 = df.parse(DATE2);
            if (dt1.getTime() >= dt2.getTime()) {//dt1大于dt2
                return 1;
            } else if (dt1.getTime() <= dt2.getTime()) {//dt1小于dt2
                return -1;
            } else {
                return 0;
            }
        } catch (Exception exception) {
            exception.printStackTrace();
        }
        return 0;
    }
    /**
     *  根据路径删除指定的目录或文件，无论存在与否
     *@param filePath  要删除的目录或文件
     *@return 删除成功返回 true，否则返回 false。
     */
    public static boolean DeleteFolder(String filePath) {
        File file = new File(filePath);
        if (!file.exists()) {
            return false;
        } else {
            if (file.isFile()) {
                // 为文件时调用删除文件方法
                return deleteFile(filePath);
            } else {
                // 为目录时调用删除目录方法
                return deleteDirectory(filePath);
            }
        }
    }

    /**
     * 删除单个文件
     * @param   filePath    被删除文件的文件名
     * @return 文件删除成功返回true，否则返回false
     */
    public static boolean deleteFile(String filePath) {
        File file = new File(filePath);
        if (file.isFile() && file.exists()) {
            return file.delete();
        }
        return false;
    }

    /**
     * 删除文件夹以及目录下的文件
     * @param   filePath 被删除目录的文件路径
     * @return  目录删除成功返回true，否则返回false
     */
    public static boolean deleteDirectory(String filePath) {
        boolean flag = false;
        //如果filePath不以文件分隔符结尾，自动添加文件分隔符
        if (!filePath.endsWith(File.separator)) {
            filePath = filePath + File.separator;
        }
        File dirFile = new File(filePath);
        if (!dirFile.exists() || !dirFile.isDirectory()) {
            return false;
        }
        flag = true;
        File[] files = dirFile.listFiles();
        //遍历删除文件夹下的所有文件(包括子目录)
        for (int i = 0; i < files.length; i++) {
            if (files[i].isFile()) {
                //删除子文件
                flag = deleteFile(files[i].getAbsolutePath());
                if (!flag) break;
            } else {
                //删除子目录
                flag = deleteDirectory(files[i].getAbsolutePath());
                if (!flag) break;
            }
        }
        if (!flag) return false;
        //删除当前空目录
        return dirFile.delete();
    }

}
