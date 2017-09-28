package com.example.administrator.irrigationworks.Factory;

/**
 * Created by Administrator on 2017/4/13.
 */
public class FinalTozal {
//    public static String domain="192.168.1.6:8090";//"192.168.1.77:81";//
//    public static String domain="192.168.1.7:8050";//测试网址
//    public static String domain="202.105.25.138:20465/";//1.7服务器
    public static String domain="121.201.43.239:9000";//外网
    public static String host="http://"+domain+"/ConstructionPatrol/";
    //登录URL
    public static String LOGON="http://"+domain+"/ConstructionPatrol/";
    public static String SHOWPIC=host+"/patrol/uploadDangerImage";///外网
//    public static String SHOWPIC="http://192.168.1.7:8050/ConstructionPatrol/patrol/uploadDangerImage";///外网
    public static String getNearbyData=host+"patrol/save";
    public static String Hidden_trouble_report=host+"/patrol/reportDanger";//隐患上报

    public static String getCheckTask=host+"/patrol/tasklist";//5、巡查任务列表
    public static String getConstructionList=host+"/construction/list";//工地列表
    public static String getHazardlist=host+"/patrol/dangerList";//隐患列表
    public static String getDangerWarn=host+"patrol/dangerWarn";//隐患警告
    public static String setPassword=host+"/user/forgetpassword";//修改密码
    public static String checkworkStatistic=host+"/workcheck/statistic";//考勤次数
    public static String patrolStatistics=host+"patrol/statistics";//巡查次数
    public static String constructionlist=host+"/construction/list";//历史巡查
    public static String patrollist=host+"/patrol/list";//查看当前巡查记录
    public static String patrolpmsubmit =host+"/patrol/pm_submit";//项目经理提交整改
    public static String pm_confirm =host+"patrol/pm_confirm";//整改的接口
    public static String noticeList =host+"notice/list";//通知公告
    public static String notice_rea =host+"notice/notice_read";//获取通知公告接口列表
    public static String patrolreaded =host+"/patrol/readed";//设置隐患是否已读
    public static String user_edit=host+"user/edit";//
    public static String registFaceInfo =host+"user/registFaceInfo";//人脸识别注册接口
    public static String construction_buildatlas =host+"construction/construction_buildatlas";//11.工地建设中图册
    public static String construction_buildinfo =host+"construction/construction_buildinfo";//12.上传工地建设图片
    public static String chiefsupervisorsubmit =host+"/patrol/chiefsupervisor_submit";//总监理师确认整改完成
    public static String getUserAuditStatus =host+"user/getUserAuditStatus";//考勤审核状态
    public static String GotoLogin =host+"user/login";//登录
    public static String findConstructionByUser =host+"construction/findConstructionByUser";//获取工地的信息
    public static String useredit =host+"user/edit";//获取工地的信息

    public static String getwedview ="http://202.105.25.138:20465/buildSite/#/";//通知公告
    public static String updateUrlapp="http://www.fsjdwl.cn/appupdate/irrigationupdate.xml";//下载更新，啊勇的服务器
    public static String Appupdate="http://www.fsjdwl.cn/appupdate/irrigationworks.apk";//apk下载地址
    public static int requestTimeOut = 10000;//请求超时
    public static int readTimeOut = 10000;//读取超时
    //工地巡查项目
    public static String SITE_INSPECTION_PIC= "";//图片路径
}
