package com.example.administrator.irrigationworks.Ui.Retrofit;


import android.database.Observable;
import android.service.media.MediaBrowserService;
import android.support.v4.media.MediaBrowserServiceCompat;

import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.iflytek.cloud.thirdparty.T;
import com.squareup.okhttp.ResponseBody;

import java.util.List;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PartMap;
import retrofit.http.Query;
import retrofit.http.QueryMap;
import retrofit2.http.Part;
import retrofit2.http.Url;

/**
 * Created by Administrator on 2017/8/18.
 */
public interface RequestServes {
    //登录页面
    @FormUrlEncoded
    @POST("user/login")
    Call<ResultCode> getIpInfo(@Query("user.username") String ip, @Query("user.password") String ips);

    @FormUrlEncoded
    @POST("user/login")
    Call<ResultCode> getTaskData(@FieldMap Map<String, Object> options);
    //修改个人信息
    @FormUrlEncoded
    @POST("user/edit")
    Call<ResultCode> getUseredit(@FieldMap Map<String, Object> options);

    //修改密码
    @FormUrlEncoded
    @POST("user/changePassword")
    Call<ResultCode> getChangePassword(@FieldMap Map<String, Object> options);
    //考勤打开
    @FormUrlEncoded
    @POST("workcheck/attendance")
    Call<ResultCode> getCheckWord(@FieldMap Map<String, Object> options);

    //获取工地的信息
    @GET("construction/findConstructionByUser")
    Call<ResultCode> getConstructionData(@Query("token") String ip);

    //获取工地巡查任务
    @FormUrlEncoded
    @POST("patrol/tasklist")
    Call<ResultCode> getCheckTask(@FieldMap Map<String, Object> options);

    //获取工地列表
    @FormUrlEncoded
    @POST("construction/list")
    Call<ResultCode> getConstructionList(@FieldMap Map<String, Object> options);

    //历史巡查任务列表
    @FormUrlEncoded
    @POST("patrol/taskHistorylist")
    Call<ResultCode> getTaskHistory(@Field("token") String ip);
    //考勤记录


    @FormUrlEncoded
    @POST("workcheck/list")
    Call<ResultCode> getAttendanceRecord(@FieldMap Map<String, Object> options);

    //考勤记录

    /**
     * 上传图像
     */
    @FormUrlEncoded
    @Multipart
    @POST("/pubApi/UploadPics.do")
    Call<ResponseBody> uploadPic(@Part List<MultipartBody.Part> partList);

    //如果图片数量不确定
    @FormUrlEncoded

    @Multipart
    @POST()
    Observable<ResponseBody> uploadFiles(
            @Url String url,
            @PartMap() Map<String, RequestBody> maps);

    /**
     * 上传头像
     */
    @FormUrlEncoded
    @Multipart
    @POST("patrol/uploadDangerImage")
//    Call<ResultCode> uploadImg(@PartMap Map<String, RequestBody> params,@Field("token") String token);
    Call<ResultCode> uploadImg(@PartMap Map<String, RequestBody> params);

    /**
     * 上传头像
     */
    @FormUrlEncoded
    @Multipart
    @POST("patrol/uploadDangerImage")
    Call<ResponseBody> uploadMemberIcon(@Part List<MultipartBody.Part> partList);

    @FormUrlEncoded
    @Multipart
    @POST("file/upLoad.do")
    Call<ResultCode> upLoadAgree(@PartMap Map<String, RequestBody> params);

    /**
     * 提交巡查结果
     */
    @FormUrlEncoded
    @POST("patrol/save")
    Call<ResultCode> PostTaskList(@Field("state") String state, @Field("checkitem") String checkitem, @Field("_id") String id, @Field("token") String token);

    /**
     * 上传多张图片
     */
    @Multipart
    @POST("patrol/uploadDangerImage")
    Observable<ResponseBody> uppicLoadAgree(@Url String url, @Part List<MultipartBody.Part> partList);

    /**
     * 上传多张图片
     */
    @Multipart
    @POST("patrol/uploadDangerImage")
    Call<ResponseBody> uploadFile(@Part RequestBody file);

    /**
     * 隐患列表
     */
    @FormUrlEncoded
    @POST("patrol/dangerList")
    Call<ResultCode> getHazardlist(@Field("token") String ip);

    /**
     * 隐患警告
     */
    @FormUrlEncoded
    @POST("patrol/dangerWarn")
    Call<ResultCode> getDangerWarn(@Field("token") String ip);

    /**
     * 采集坐标
     */
    @FormUrlEncoded
    @POST("construction/updateCoordinate")
    Call<ResultCode> getLcation(@FieldMap Map<String, Object> options);
}
