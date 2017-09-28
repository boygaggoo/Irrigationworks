package com.example.administrator.irrigationworks.UtilsTozals;

import android.util.Log;

import net.sf.ezmorph.bean.MorphDynaBean;
import net.sf.json.JSONObject;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.BeanMap;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2017/9/13.
 */
public class MapUitl {
    public static Object mapToObject(Map<Object, Object> map, Class<?> beanClass) throws Exception {
        if (map == null)
            return null;

        Object obj = beanClass.newInstance();

        Field[] fields = obj.getClass().getDeclaredFields();
        for (Field field : fields) {
            int mod = field.getModifiers();
            if (Modifier.isStatic(mod) || Modifier.isFinal(mod)) {
                continue;
            }

            field.setAccessible(true);
            field.set(obj, map.get(field.getName()));
        }

        return obj;
    }

    /**
     * 从map集合中获取属性值
     *
     * @param <E>
     * @param map          map集合
     * @param key          键对
     * @param defaultValue 默认值
     * @return
     * @author jiqinlin
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    public final static <E> E get(Map map, Object key, E defaultValue) {
        Object o = map.get(key);
        if (o == null)
            return defaultValue;
        return (E) o;
    }


    public static void javaMapAndJsonInterChange(Map map) {

        // map转JSONArray
        JSONObject jsObj = JSONObject.fromObject(map);
        System.out.println(jsObj.toString(4));

        // 从JSON串到JSONObject
        jsObj = JSONObject.fromObject(jsObj.toString());

        //第一种方式：从JSONObject里读取
        // print: json
        System.out.println(jsObj.get("str"));
        // print: address.city = Seattle, WA

        Log.d("常规项目", "常规项目" + ((JSONObject) jsObj.get("address")).get("city"));
        //第二种方式：从动态Bean里读取数据，由于不能转换成具体的Bean，感觉没有多大用处
        MorphDynaBean mdBean = (MorphDynaBean) JSONObject.toBean(jsObj);
        // print: json
        System.out.println(mdBean.get("str"));
        //print: address.city = Seattle, WA
        Log.d("常规项目", "常规项目" + ((MorphDynaBean) mdBean.get("address")).get("city"));

    }

    // Map --> Bean 2: 利用org.apache.commons.beanutils 工具类实现 Map --> Bean
    public static Object transMap2Bean2(Map<Object, Object> map, Object obj) {
        if (map == null || obj == null) {
            return null;
        }
        try {
            BeanUtils.populate(obj, map);
            Log.d("常规项目", "常规项目" + obj);
        } catch (Exception e) {
            Log.d("常规项目", "e常规项目obj.toString()" + e);
            System.out.println("transMap2Bean2 Error " + e);
        }
        Log.d("常规项目", "常规项目obj.toString()" + obj.toString());

        return obj;
    }

    public static Object toJavaBean(Object javabean, Map<Object, Object> data) {
        Method[] methods = javabean.getClass().getDeclaredMethods();
        for (Method method : methods) {
            try {
                if (method.getName().startsWith("set")) {
                    String field = method.getName();
                    field = field.substring(field.indexOf("set") + 3);
                    field = field.toLowerCase().charAt(0) + field.substring(1);
                    method.invoke(javabean, new Object[]
                            {
                                    data.get(field)
                            });
                }
            } catch (Exception e) {
            }
        }

        return javabean;
    }

}
