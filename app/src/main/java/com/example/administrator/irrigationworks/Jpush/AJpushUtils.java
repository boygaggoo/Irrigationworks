package com.example.administrator.irrigationworks.Jpush;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Jpush.jupai.AjPushPojo;

import org.apache.commons.lang.NotImplementedException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Random;
import java.util.Set;

import cn.jpush.api.JPushClient;
import cn.jpush.api.common.resp.APIConnectionException;
import cn.jpush.api.common.resp.APIRequestException;
import cn.jpush.api.push.PushResult;
import cn.jpush.api.push.model.Message;
import cn.jpush.api.push.model.Platform;
import cn.jpush.api.push.model.PushPayload;
import cn.jpush.api.push.model.audience.Audience;
import cn.jpush.api.push.model.notification.AndroidNotification;
import cn.jpush.api.push.model.notification.IosNotification;
import cn.jpush.api.push.model.notification.Notification;

/**
 * Created by Administrator on 2017/8/24.
 */
public class AJpushUtils {
    static Logger logger = LoggerFactory.getLogger(AJpushUtils.class);
    public static AjPushPojo ajPushPojo = null;
    public static JPushClient jpushClient = null;

    /**
     * 发送通知
     *
     * @param
     *
     * @param alert
     *            推送内容
     */
    public static void jSend_notification(String masterSecret, String appKey,
                                          String content, String alert, Map<String, Object> map,
                                          String... alias) {
        logger.info(masterSecret);
        int max=20;
        int min=1;
        Random random = new Random();

        int s = random.nextInt(max)%(max-min+1) + min;
        jpushClient = new JPushClient(masterSecret, appKey,s);
        try {
            ajPushPojo = new AjPushPojo();
            // ajPushPojo.setMessage(content);
            // 根据别名推送通知，不传默认推送全部
            if (alias.length >= 1) {
                ajPushPojo.setAlias(alias);
            }
            ajPushPojo.setNotification(alert, map);

            jpushClient = new JPushClient(masterSecret, appKey, 3);

            PushResult result = jpushClient
                    .sendPush(NimUIKit.getGson().toJson(ajPushPojo));
//            logger.info(result.msg_id + "===" + result.sendno);
            Log.d("qqc","点击了logger");
        } catch (APIConnectionException e) {
            Log.d("qqc","出现故障");

            logger.info(" e: {}", e);
        } catch (APIRequestException e) {
            Log.d("qqc","出现故障getStatus"+e.getStatus());
            Log.d("qqc","出现故障getErrorCode"+e.getErrorCode());
            Log.d("qqc","出现故障getErrorMessage"+e.getErrorMessage());
            logger.info(" e: {}", e);


        }


    }

    //TODO 全平台消息推送
    public static PushPayload buildPushObject_all_alias_alertWithTitle(String... alias){
        return PushPayload.newBuilder()
                .setPlatform(Platform.all())
                .setAudience(Audience.alias(alias))
                .setNotification(Notification.newBuilder()
                        .setAlert("111")
                        .addPlatformNotification(AndroidNotification.newBuilder()
                                .setTitle("111").build())
                        .addPlatformNotification(IosNotification.newBuilder()
                                .incrBadge(1)
                                //extra_key extra_value
                                .addExtra("111","111").build())
                        .build())
                .build();
    }
    public static long sendPushAlias(String masterSecret, String appKey,String... alias){
        //PushPayload payloadAlias = buildPushObject_android_alias_alertWithTitle(alias);
        jpushClient = new JPushClient(masterSecret, appKey, 3);
        PushPayload payloadAlias = buildPushObject_all_alias_alertWithTitle(alias);
        long msgId = 0;
        try{
            PushResult result = jpushClient.sendPush(payloadAlias);
            msgId = result.msg_id;
        }catch(APIConnectionException e){
            System.err.println("[Connection Error]:"+ e.toString());
        }catch(APIRequestException e){
            System.out.println("HTTP Status:"+e.getStatus());
            System.out.println("Error Code:"+e.getErrorCode());
            System.out.println("Error Message:"+e.getErrorMessage());
            System.out.println("Msg ID:"+e.getMsgId());
            msgId = e.getMsgId();
        }
        return msgId;
    }
    /**
     * 发送消息
     *
     * @param
     *
     * @param alert
     *            推送内容
     */
    public static void jSend_Message(String masterSecret, String appKey,
                                     String alert, Map<String, Object> map, String... alias) {
        logger.info(masterSecret);
        jpushClient = new JPushClient(masterSecret, appKey, 3);
        try {
            ajPushPojo = new AjPushPojo();
            // 根据别名推送通知，不传默认推送全部
            if (alias.length >= 1) {
                ajPushPojo.setAlias(alias);
            }
            ajPushPojo.setMessage(alert, map);
            PushResult result = jpushClient
                    .sendPush(NimUIKit.getGson().toJson(ajPushPojo));
            logger.info(result.msg_id + "===" + result.sendno);

        } catch (APIConnectionException e) {
            logger.info(" e: {}", e);
        } catch (APIRequestException e) {
            logger.info(" e: {}", e);

        }
    }

    public static void jSend_notification1(String masterSecret, String appKey,
                                           String content, String alert, Map<String, Object> map,
                                           String... alias) {

    }
}
