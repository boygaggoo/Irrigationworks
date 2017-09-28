package com.example.administrator.irrigationworks.UtilsTozals;

import android.annotation.SuppressLint;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;


/**
 * 对象工具
 * 
 * @author xianyl
 */
public class ObjectUtils {

	/**
	 * 比较两个对象是否相同
	 * <ul>
	 * <li>如果两个都为null，返回true</li>
	 * <li>return actual.{@link Object#equals(Object)}</li>
	 * </ul>
	 * 
	 * @param actual
	 * @param expected
	 * @return
	 */
	public static boolean isEquals(Object actual, Object expected) {
		return actual == expected
				|| (actual == null ? expected == null : actual.equals(expected));
	}

	/**
	 * 转换长数整型  基本类型转对象
	 * {@link ObjectUtils#transformLongArray(Long[])}
	 * @param source
	 * @return
	 */
	public static Long[] transformLongArray(long[] source) {
		Long[] destin = new Long[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}

	/**
	 * 转换长数�? 对象类型转基本类�?
	 * {@link ObjectUtils#transformLongArray(long[])
	 * @param source
	 * @return
	 */
	public static long[] transformLongArray(Long[] source) {
		long[] destin = new long[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}

	/**
	 * 转换整型数组  基本类型转对象类
	 * {@link ObjectUtils#transformIntArray(Integer[])
	 * @param source
	 * @return
	 */
	public static Integer[] transformIntArray(int[] source) {
		Integer[] destin = new Integer[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}

	/**
	 * 转换整型数组 对象类型转基本类
	 * {@link ObjectUtils#transformIntArray(int[])
	 * @param source
	 * @return
	 */
	public static int[] transformIntArray(Integer[] source) {
		int[] destin = new int[source.length];
		for (int i = 0; i < source.length; i++) {
			destin[i] = source[i];
		}
		return destin;
	}

	/**
	 * 比较两个对象的大小
	 * <ul>
	 * <strong>About result</strong>
	 * <li>if v1 > v2, return 1</li>
	 * <li>if v1 = v2, return 0</li>
	 * <li>if v1 < v2, return -1</li>
	 * </ul>
	 * <ul>
	 * <strong>About rule</strong>
	 * <li>if v1 is null, v2 is null, then return 0</li>
	 * <li>if v1 is null, v2 is not null, then return -1</li>
	 * <li>if v1 is not null, v2 is null, then return 1</li>
	 * <li>return v1.{@link Comparable#compareTo(Object)}</li>
	 * </ul>
	 * 
	 * @param v1
	 * @param v2
	 * @return
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static <V> int compare(V v1, V v2) {
		return v1 == null ? (v2 == null ? 0 : -1) : (v2 == null ? 1
				: ((Comparable) v1).compareTo(v2));
	}
	
	
	/**
	 * 根据类文件名创建对象实体
	 * @param className  全定向类文件名
	 * @return
	 */
	public static Object newInstanceForClass(String className) {
		Object object = null;
		try {
			object = Class.forName(className).newInstance();
		} catch (InstantiationException e) {
			Log.e("BaseArrayListAdater", "对象实例化失败，该类是一个抽象类或接�?");
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			Log.e("BaseArrayListAdater", "安全权限异常，java在反射时调用了private方法�?导致�?");
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			Log.e("BaseArrayListAdater", className + "类文件找不到");
			e.printStackTrace();
		}
		return object;
	}
	
	/**
	 * 利用反射机制将任意对象的转换为map存储 
	 * @param obj
	 * @return
	 */
	public static Map<String, String> getValueMap(Object obj) {
		if(obj == null){
			return null;
		}
        Map<String, String> map = new HashMap<String, String>();  
        // System.out.println(obj.getClass());  
        // 获取f对象对应类中的所有属性域  
        Field[] fields = obj.getClass().getDeclaredFields();  
        for (int i = 0, len = fields.length; i < len; i++) {  
            String varName = fields[i].getName();  
            try {  
                // 获取原来的访问控制权�?  
                boolean accessFlag = fields[i].isAccessible();  
                // 修改访问控制权限  
                fields[i].setAccessible(true);  
                // 获取在对象f中属性fields[i]对应的对象中的变�?  
                Object o = fields[i].get(obj);  
                if (o != null)  
                    map.put(varName, o.toString());  
                // System.out.println("传入的对象中包含�?个如下的变量�?" + varName + " = " + o);  
                // 恢复访问控制权限  
                fields[i].setAccessible(accessFlag);  
            } catch (IllegalArgumentException ex) {  
                ex.printStackTrace();  
            } catch (IllegalAccessException ex) {  
                ex.printStackTrace();  
            }  
        }  
        return map;  
  
    }  
	
	/**
	 * java反射bean的get方法    
	 * @param objectClass
	 * @param fieldName 
	 * @return
	 */  
	@SuppressLint("DefaultLocale")
	public static Method getGetMethod(Class<? extends Object> objectClass, String fieldName) {	  
	    StringBuffer sb = new StringBuffer(); 
	    sb.append("get"); 
	    sb.append(fieldName.substring(0, 1).toUpperCase());
	    sb.append(fieldName.substring(1));
	    try { 
	        return objectClass.getMethod(sb.toString());
	    } catch (Exception e) {
	    	e.printStackTrace();
	    }  
	    return null;
	}       
	  
	       
	  
	/**
	 * java反射bean的set方法  
	 * @param objectClass 
	 * @param fieldName
	 * @return 
	 */
	@SuppressLint("DefaultLocale")
	@SuppressWarnings("rawtypes")
	public static Method getSetMethod(Class<? extends Object> objectClass, String fieldName) { 
	    try {
	    	//参数
			Class[] parameterTypes = new Class[1]; 
	        Field field = objectClass.getDeclaredField(fieldName);
	        parameterTypes[0] = field.getType();
	        //方法名拼接
	        StringBuffer sb = new StringBuffer();	        
	        sb.append("set");
	        sb.append(fieldName.substring(0, 1).toUpperCase());
	        sb.append(fieldName.substring(1));
	        //获取方法
	        return objectClass.getMethod(sb.toString(), parameterTypes);
	    } catch (Exception e) { 
	        e.printStackTrace();
	    }  
	    return null;       
	  
	}       
	  
	       
	  
	/**执行set方法  
	 * @param o执行对象   
	 * @param fieldName 属性名字 
	 * @param value 属性值
	 */       
	  
	public static void invokeSet(Object o, String fieldName, Object value) {
	    Method method = getSetMethod(o.getClass(), fieldName);
	    try { 
	    	method.setAccessible(true);
	        method.invoke(o, new Object[] { value });
	        method.setAccessible(false);
	    } catch (Exception e) {
	        e.printStackTrace(); 
	    } 
	}       
	  
	       
	  
	/** 执行get方法  
	 * @param o 执行对象 
	 * @param fieldName 属性名字 
	 */  
	public static Object invokeGet(Object o, String fieldName) {
		Object object = null;
	    Method method = getGetMethod(o.getClass(), fieldName);
	    try {
	    	method.setAccessible(true);
	        object = method.invoke(o, new Object[0]);
	        method.setAccessible(false);
	    } catch (Exception e) { 
	        e.printStackTrace(); 
	    }   
	    return object; 
	} 
}
