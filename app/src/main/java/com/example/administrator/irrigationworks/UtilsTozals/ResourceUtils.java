package com.example.administrator.irrigationworks.UtilsTozals;

import android.content.Context;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * ResourceUtils
 * 
 * @author xianyl
 */

public class ResourceUtils {
	/**
	 * 读输入流的数据转成字符串，一个字节一个字节地读
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String loadStringOnChar(InputStream in) throws IOException {
		StringBuilder builder = new StringBuilder();
		InputStreamReader reader = new InputStreamReader(in);
		char[] buf = new char[1024];
		int numRead = 0;
		while ((numRead = reader.read(buf)) != -1) {
			builder.append(buf, 0, numRead);
		}
		return builder.toString();
	}

	/**
	 * 读输入流的数据转成字符串，一行一行地读
	 * 
	 * @param in
	 * @return
	 * @throws IOException
	 */
	public static String loadStringOnLine(InputStream in) throws IOException {
		StringBuilder s = new StringBuilder();
		BufferedReader br = new BufferedReader(new InputStreamReader(in));
		String line;
		while ((line = br.readLine()) != null) {
			s.append(line);
		}
		return s.toString();
	}

	/**
	 * 获取Assets目录下的资源文件
	 * 
	 * @param context
	 * @param fileName
	 *            在 Assets目录的文件名，文件夹能分层
	 * @return
	 */
	public static String geFileFromAssets(Context context, String fileName) {
		if (context == null || StringUtils.isEmpty(fileName)) {
			return null;
		}

		try {
			InputStream in = context.getResources().getAssets().open(fileName);
			return loadStringOnLine(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 获取Raw目录下的资源文件
	 * 
	 * @param context
	 * @param resId
	 *            资源标识符R.Raw.xxx
	 * @return
	 */
	public static String geFileFromRaw(Context context, int resId) {
		if (context == null) {
			return null;
		}
		try {
			InputStream in = context.getResources().openRawResource(resId);
			return loadStringOnLine(in);
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * same to {@link ResourceUtils#geFileFromAssets(Context, String)}, but
	 * return type is List<String>
	 * 
	 * @param context
	 * @param fileName
	 * @return
	 */
	public static List<String> geFileToListFromAssets(Context context,
			String fileName) {
		if (context == null || StringUtils.isEmpty(fileName)) {
			return null;
		}

		List<String> fileContent = new ArrayList<String>();
		try {
			InputStreamReader in = new InputStreamReader(context.getResources()
					.getAssets().open(fileName));
			BufferedReader br = new BufferedReader(in);
			String line;
			while ((line = br.readLine()) != null) {
				fileContent.add(line);
			}
			br.close();
			return fileContent;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * same to {@link ResourceUtils#geFileFromRaw(Context, int)}, but return
	 * type is List<String>
	 * 
	 * @param context
	 * @param resId
	 * @return
	 */
	public static List<String> geFileToListFromRaw(Context context, int resId) {
		if (context == null) {
			return null;
		}

		List<String> fileContent = new ArrayList<String>();
		BufferedReader reader = null;
		try {
			InputStreamReader in = new InputStreamReader(context.getResources()
					.openRawResource(resId));
			reader = new BufferedReader(in);
			String line = null;
			while ((line = reader.readLine()) != null) {
				fileContent.add(line);
			}
			reader.close();
			return fileContent;
		} catch (IOException e) {
			e.printStackTrace();
			return null;
		}
	}

	/**
	 * 读res/values下string.xml的字符串
	 * 
	 * @param context
	 * @param resId
	 *            资源id
	 * @return
	 * @throws IOException
	 */
	public static String getStringFromRes(Context context, int resId) {
		return context.getResources().getString(resId);
	}

	/**
	 * 读res/values下的arrays.xml字符串数组
	 * 
	 * @param context
	 * @param resId
	 *            资源id
	 * @return
	 * @throws IOException
	 */
	public static String[] getStringArrayFromRes(Context context, int resId) {
		return context.getResources().getStringArray(resId);
	}

	/***
	 * 获取资源id
	 * 
	 * @param t
	 * @param field
	 * @return
	 */
	public static int getResource(Class<?> t, String field) {
		int res = 0;
		try {
			if (field == null || "".equals(field)) {
				return 0;
			}
			res = t.getField(field).getInt(null);
		} catch (IllegalArgumentException e) {
			res = 0;
		} catch (IllegalAccessException e) {
			res = 0;
		} catch (NoSuchFieldException e) {
			res = 0;
		}
		return res;
	}

}
