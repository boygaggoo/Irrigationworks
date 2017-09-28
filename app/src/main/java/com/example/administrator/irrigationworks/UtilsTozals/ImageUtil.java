package com.example.administrator.irrigationworks.UtilsTozals;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Bitmap.CompressFormat;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.LinearGradient;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.ref.SoftReference;
import java.text.DecimalFormat;
import java.util.HashMap;

public class ImageUtil {
    public static final int SIZETYPE_B = 1;//获取文件大小单位为B的double值
    public static final int SIZETYPE_KB = 2;//获取文件大小单位为KB的double值
    public static final int SIZETYPE_MB = 3;//获取文件大小单位为MB的double值
    public static final int SIZETYPE_GB = 4;//获取文件大小单位为GB的double值
    private static HashMap<String, SoftReference<Bitmap>> softCache = new HashMap<String, SoftReference<Bitmap>>();
    public static Bitmap getBitmapFromMemorySoftCache(String url) {
        if (softCache.containsKey(url)) {
            SoftReference<Bitmap> softBitmap = softCache.get(url);
            if (softBitmap != null) {
                Bitmap bitmap = softBitmap.get();
                if (bitmap != null) {
                    return bitmap;
                }
            }
        }

        return null;
    }
    public static void clearBitmap(Bitmap bimatp) {
        try {
            if (bimatp != null && !bimatp.isRecycled()) {
                // 回收并且置为null
                bimatp.recycle();
                bimatp = null;
            }
            System.gc();
        } catch (Exception e) {
            Log.e("bitmap回收失败", "IamgeUtils 371行" + e);
        }


    }

    /**
     * 调用此方法自动计算指定文件或指定文件夹的大小
     * @param filePath 文件路径
     * @return 计算好的带B、KB、MB、GB的字符串
     */
    public static String getAutoFileOrFilesSize(String filePath){
        File file=new File(filePath);
        long blockSize=0;
        try {
            if(file.isDirectory()){
                blockSize = getFileSizes(file);
            }else{
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize);
    }
    /**
     * 转换文件大小,指定转换的类型
     *
     * @param fileS
     * @param sizeType
     * @return
     */
    /**
     * 转换文件大小
     * @param fileS
     * @return
     *
     */
    public static String FormetFileSize(long fileS) {
        DecimalFormat df = new DecimalFormat("#.00");
        String fileSizeString = "";
        String wrongSize = "0B";
        if (fileS == 0) {
            return wrongSize;
        }
        if (fileS < 1024) {
            fileSizeString = df.format((double) fileS) + "B";
        } else if (fileS < 1048576) {
            fileSizeString = df.format((double) fileS / 1024) + "KB";
        } else if (fileS < 1073741824) {
            fileSizeString = df.format((double) fileS / 1048576) + "MB";
        } else {
            fileSizeString = df.format((double) fileS / 1073741824) + "GB";
        }
        return fileSizeString;
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
     * 获取文件指定文件的指定单位的大小
     * @param filePath 文件路径
     * @param sizeType 获取大小的类型1为B、2为KB、3为MB、4为GB
     * @return double值的大小
     */
    public static double getFileOrFilesSize(String filePath, int sizeType){
        File file=new File(filePath);
        long blockSize=0;
        try {
            if(file.isDirectory()){
                blockSize = getFileSizes(file);
            }else{
                blockSize = getFileSize(file);
            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.e("获取文件大小","获取失败!");
        }
        return FormetFileSize(blockSize, sizeType);
    }
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
    public static final int TOP = 0;
    public static final int BOTTOM = 1;
    public static final int LEFT = 2;
    public static final int RIGHT = 3;
    public static final int LEFT_TOP = 4;
    public static final int LEFT_BOTTOM = 5;
    public static final int RIGHT_TOP = 6;
    public static final int RIGHT_BOTTOM = 7;
    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmap(String filePath) {
        //图片所在SD卡的路径
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 210, 210);//自定义一个宽和高 20170516
//                options.inSampleSize = calculateInSampleSize(options, 400, 400);//自定义一个宽和高
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }

    // 根据路径获得图片并压缩，返回bitmap用于显示
    public static Bitmap getSmallBitmapThree(String filePath) {
        //图片所在SD卡的路径
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 500, 500);//自定义一个宽和高 20170516
//                options.inSampleSize = calculateInSampleSize(options, 400, 400);//自定义一个宽和高
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    public static Bitmap getSmallBitmapTwo(String filePath) {
        //图片所在SD卡的路径
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(filePath, options);
        options.inSampleSize = calculateInSampleSize(options, 270, 270);//自定义一个宽和高 20170516
//                options.inSampleSize = calculateInSampleSize(options, 400, 400);//自定义一个宽和高
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(filePath, options);
    }
    //计算图片的缩放值
     public static int calculateInSampleSize(BitmapFactory.Options options, int reqWidth, int reqHeight) {
              final int height = options.outHeight;//获取图片的高
            final int width = options.outWidth;//获取图片的框
              int inSampleSize = 4;
             if (height > reqHeight || width > reqWidth) {
                     final int heightRatio = Math.round((float) height/ (float) reqHeight);
                       final int widthRatio = Math.round((float) width / (float) reqWidth);
                        inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
                   }
              return inSampleSize;//求出缩放值
            }
    /** 
     *
     * @param src
     * @param scaleX
     * @param scaleY
     * @return
     */  
    public static Bitmap zoomBitmap(Bitmap src, float scaleX, float scaleY) {
        Matrix matrix = new Matrix();
        matrix.setScale(scaleX, scaleY);  
        Bitmap t_bitmap = Bitmap.createBitmap(src, 0, 0, src.getWidth(), src.getHeight(), matrix, true);
        return t_bitmap;  
    }  
      
    /** 
     *
     * @param src 
     * @param width 
     * @param height 
     * @return 
     */  
    public static Bitmap zoomBimtap(Bitmap src, int width, int height) {
        return Bitmap.createScaledBitmap(src, width, height, true);
    }  
      
    /** 
     * ��DrawableתΪBitmap���� 
     * @param drawable 
     * @return 
     */  
    public static Bitmap drawableToBitmap(Drawable drawable) {
        return ((BitmapDrawable)drawable).getBitmap();
    }  
      
      
    /** 
     * ��Bitmapת��ΪDrawable���� 
     * @param bitmap 
     * @return 
     */  
    public static Drawable bitmapToDrawable(Bitmap bitmap) {
        Drawable drawable = new BitmapDrawable(bitmap);
        return drawable;  
    }  
      
    /** 
     * Bitmapתbyte[] 
     * @param bitmap 
     * @return 
     */  
    public static byte[] bitmapToByte(Bitmap bitmap) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        bitmap.compress(CompressFormat.PNG, 100, out);
        return out.toByteArray();  
    }  
      
    /** 
     * byte[]תBitmap 
     * @param data 
     * @return 
     */  
    public static Bitmap byteToBitmap(byte[] data) {
        if(data.length != 0) {  
            return BitmapFactory.decodeByteArray(data, 0, data.length);
        }  
        return null;  
    }  
      
    /** 
     * ���ƴ�Բ�ǵ�ͼ�� 
     * @param src 
     * @param radius 
     * @return 
     */  
    public static Bitmap createRoundedCornerBitmap(Bitmap src, int radius) {
        final int w = src.getWidth();  
        final int h = src.getHeight();  
        // ������32λͼ  
        Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmap);
        canvas.drawARGB(0, 0, 0, 0);  
        paint.setColor(0xff424242);  
        // ��ֹ��Ե�ľ��  
        paint.setFilterBitmap(true);  
        Rect rect = new Rect(0, 0, w, h);
        RectF rectf = new RectF(rect);
        // ���ƴ�Բ�ǵľ���  
        canvas.drawRoundRect(rectf, radius, radius, paint);  
          
        //
//paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));  //ԭ���Ĵ���
        paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN));  //�¸�װ��
        // ����ͼ��  
        canvas.drawBitmap(src, rect, rect, paint);  
        return bitmap;  
    }  
      
    /** 
     * ����ѡ�д���ʾͼƬ 
     * @param context 
     * @param srcId 
     * @param tipId 
     * @return 
     */  
    public static Drawable createSelectedTip(Context context, int srcId, int tipId) {
        Bitmap src = BitmapFactory.decodeResource(context.getResources(), srcId);
        Bitmap tip = BitmapFactory.decodeResource(context.getResources(), tipId);
        final int w = src.getWidth();  
        final int h = src.getHeight();  
        Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Paint paint = new Paint();
        Canvas canvas = new Canvas(bitmap);
        //����ԭͼ  
        canvas.drawBitmap(src, 0, 0, paint);  
        //������ʾͼƬ  
        canvas.drawBitmap(tip, (w - tip.getWidth()), 0, paint);  
        return bitmapToDrawable(bitmap);  
    }  
      
    /** 
     * ����Ӱ��ͼ�� 
     * @param src 
     * @return 
     */  
    public static Bitmap createReflectionBitmap(Bitmap src) {
        // ����ͼ���Ŀ�϶  
        final int spacing = 4;  
        final int w = src.getWidth();  
        final int h = src.getHeight();  
        // ���Ƹ�����32λͼ  
        Bitmap bitmap = Bitmap.createBitmap(w, h + h / 2 + spacing, Config.ARGB_8888);
        // ������X��ĵ�Ӱͼ��  
        Matrix m = new Matrix();
        m.setScale(1, -1);  
        Bitmap t_bitmap = Bitmap.createBitmap(src, 0, h / 2, w, h / 2, m, true);
          
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        //  ����ԭͼ��  
        canvas.drawBitmap(src, 0, 0, paint);  
        // ���Ƶ�Ӱͼ��  
        canvas.drawBitmap(t_bitmap, 0, h + spacing, paint);  
        // ������Ⱦ-��Y��ߵ�����Ⱦ  
        Shader shader = new LinearGradient(0, h + spacing, 0, h + spacing + h / 2, 0x70ffffff, 0x00ffffff, Shader.TileMode.MIRROR);
        paint.setShader(shader);  
        // ȡ������ƽ�������ʾ�²㡣  
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // ������Ⱦ��Ӱ�ľ���  
        canvas.drawRect(0, h + spacing, w, h + h / 2 + spacing, paint);  
        return bitmap;  
    }  
      
      
    /** 
     * �����ĵ�Ӱͼ�� 
     * @param src 
     * @return 
     */  
    public static Bitmap createReflectionBitmapForSingle(Bitmap src) {
        final int w = src.getWidth();  
        final int h = src.getHeight();  
        // ���Ƹ�����32λͼ  
        Bitmap bitmap = Bitmap.createBitmap(w, h / 2, Config.ARGB_8888);
        // ������X��ĵ�Ӱͼ��  
        Matrix m = new Matrix();
        m.setScale(1, -1);  
        Bitmap t_bitmap = Bitmap.createBitmap(src, 0, h / 2, w, h / 2, m, true);
  
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        // ���Ƶ�Ӱͼ��  
        canvas.drawBitmap(t_bitmap, 0, 0, paint);  
        // ������Ⱦ-��Y��ߵ�����Ⱦ      
        Shader shader = new LinearGradient(0, 0, 0, h / 2, 0x70ffffff,
                0x00ffffff, Shader.TileMode.MIRROR);
        paint.setShader(shader);  
        // ȡ������ƽ�������ʾ�²㡣  
        paint.setXfermode(new PorterDuffXfermode(Mode.DST_IN));
        // ������Ⱦ��Ӱ�ľ���  
        canvas.drawRect(0, 0, w, h / 2, paint);  
        return bitmap;  
    }  
      
      
    public static Bitmap createGreyBitmap(Bitmap src) {
        final int w = src.getWidth();  
        final int h = src.getHeight();  
        Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        Paint paint = new Paint();
        // ��ɫ�任�ľ���  
        ColorMatrix matrix = new ColorMatrix();
        // saturation ���Ͷ�ֵ����С����Ϊ0����ʱ��Ӧ���ǻҶ�ͼ��Ϊ1��ʾ���ͶȲ��䣬���ô���1������ʾ������  
        matrix.setSaturation(0);  
        ColorMatrixColorFilter filter = new ColorMatrixColorFilter(matrix);
        paint.setColorFilter(filter);  
        canvas.drawBitmap(src, 0, 0, paint);  
        return bitmap;  
    }  
      
    /** 
     * ����ͼƬ 
     * @param src 
     * @param filepath 
     * @param format:[Bitmap.CompressFormat.PNG,Bitmap.CompressFormat.JPEG] 
     * @return 
     */  
    public static boolean saveImage(Bitmap src, String filepath, CompressFormat format) {
        boolean rs = false;  
        File file = new File(filepath);
        try {  
            FileOutputStream out = new FileOutputStream(file);
            if(src.compress(format, 100, out)) {  
                out.flush();    //д����  
            }  
            out.close();  
        } catch (FileNotFoundException e) {
            e.printStackTrace();  
        } catch (IOException e) {
            e.printStackTrace();  
        }  
        return rs;  
    }  
      
    /** 
     * ���ˮӡЧ�� 
     * @param src       Դλͼ 
     * @param watermark ˮӡ 
     * @param direction ���� 
     * @param spacing ��� 
     * @return 
     */  
    public static Bitmap createWatermark(Bitmap src, Bitmap watermark, int direction, int spacing) {
        final int w = src.getWidth();  
        final int h = src.getHeight();  
        Bitmap bitmap = Bitmap.createBitmap(w, h, Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        canvas.drawBitmap(src, 0, 0, null);  
        if(direction == LEFT_TOP) {  
            canvas.drawBitmap(watermark, spacing, spacing, null);  
        } else if(direction == LEFT_BOTTOM){  
            canvas.drawBitmap(watermark, spacing, h - watermark.getHeight() - spacing, null);  
        } else if(direction == RIGHT_TOP) {  
            canvas.drawBitmap(watermark, w - watermark.getWidth() - spacing, spacing, null);  
        } else if(direction == RIGHT_BOTTOM) {  
            canvas.drawBitmap(watermark, w - watermark.getWidth() - spacing, h - watermark.getHeight() - spacing, null);  
        }  
        return bitmap;  
    }  
      
      
    /** 
     * �ϳ�ͼ�� 
     * @param direction 
     * @param bitmaps 
     * @return 
     */  
    public static Bitmap composeBitmap(int direction, Bitmap... bitmaps) {
        if(bitmaps.length < 2) {  
            return null;  
        }  
        Bitmap firstBitmap = bitmaps[0];
        for (int i = 0; i < bitmaps.length; i++) {  
            firstBitmap = composeBitmap(firstBitmap, bitmaps[i], direction);  
        }  
        return firstBitmap;  
    }  
  
    /** 
     * �ϳ�����ͼ�� 
     * @param firstBitmap 
     * @param secondBitmap 
     * @param direction 
     * @return 
     */  
    private static Bitmap composeBitmap(Bitmap firstBitmap, Bitmap secondBitmap,
                                        int direction) {
        if(firstBitmap == null) {  
            return null;  
        }  
        if(secondBitmap == null) {  
            return firstBitmap;  
        }  
        final int fw = firstBitmap.getWidth();  
        final int fh = firstBitmap.getHeight();  
        final int sw = secondBitmap.getWidth();  
        final int sh = secondBitmap.getHeight();  
        Bitmap bitmap = null;
        Canvas canvas = null;
        if(direction == TOP) {  
            bitmap = Bitmap.createBitmap(sw > fw ? sw : fw, fh + sh, Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawBitmap(secondBitmap, 0, 0, null);  
            canvas.drawBitmap(firstBitmap, 0, sh, null);  
        } else if(direction == BOTTOM) {  
            bitmap = Bitmap.createBitmap(fw > sw ? fw : sw, fh + sh, Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawBitmap(firstBitmap, 0, 0, null);  
            canvas.drawBitmap(secondBitmap, 0, fh, null);  
        } else if(direction == LEFT) {  
            bitmap = Bitmap.createBitmap(fw + sw, sh > fh ? sh : fh, Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawBitmap(secondBitmap, 0, 0, null);  
            canvas.drawBitmap(firstBitmap, sw, 0, null);  
        } else if(direction == RIGHT) {  
            bitmap = Bitmap.createBitmap(fw + sw, fh > sh ? fh : sh,
                    Config.ARGB_8888);
            canvas = new Canvas(bitmap);
            canvas.drawBitmap(firstBitmap, 0, 0, null);  
            canvas.drawBitmap(secondBitmap, fw, 0, null);  
        }  
        return bitmap;  
    }




}  


