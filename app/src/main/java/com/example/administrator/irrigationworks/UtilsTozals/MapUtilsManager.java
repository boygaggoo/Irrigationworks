package com.example.administrator.irrigationworks.UtilsTozals;

import android.util.Log;

import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.Enumerate;
import com.example.administrator.irrigationworks.Ui.bean.Demo1;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.ThreadPoolExecutor.AbortPolicy;
import java.util.concurrent.TimeUnit;

/**
 * 线程管理器
 *
 */
public class MapUtilsManager {

	private static HashMap<Object, Object> paramschanggui;//检查参数上传
	private static HashMap<Object, Object> paramsshigongxianchagn;//检查参数上传
	private static HashMap<Object, Object> paramsfengshuidian;//检查参数上传
	private static HashMap<Object, Object> paramsanquanhuju;//检查参数上传
	private static HashMap<Object, Object> paramsanquansheshji;//检查参数上传
	private static HashMap<Object, Object> paramsanhanjie;//检查参数上传
	private static HashMap<Object, Object> paramsjiancha;//检查参数上传
	private static HashMap<Object, Object> paramsqieti;//检查参数上传
	private static HashMap<Object, Object> paramstufang;//检查参数上传
	private static HashMap<Object, Object> paramsgangquan;//检查参数上传

	private static HashMap<Object, Object> params;//全部检查参数上传
	public static HashMap<Object, Object> getThreadPool(String sutupe,String name,String state) {
		if (paramschanggui == null) {
			paramschanggui = new HashMap<Object, Object>();
			paramschanggui.put("监理人员到位", Enumerate.PASS);//是通过
			paramschanggui.put("施工人员到位", Enumerate.PASS);//是通过
			paramschanggui.put("资料同步情况", Enumerate.PASS);//是通过
			paramschanggui.put("贯彻落实上级相关部门安全生产的工作部署及时令性要求", Enumerate.PASS);//是通过
			paramschanggui.put("健全安全生产宣传教育制度", Enumerate.PASS);//是通过
			paramschanggui.put("健全安全生产宣传检查制度", Enumerate.PASS);//是通过
			paramschanggui.put("定期召开安全生产会议", Enumerate.PASS);//是通过
			paramschanggui.put("安全生产设施", Enumerate.PASS);//是通过
			paramschanggui.put("安全生产管理人员、特种人员持证上岗", Enumerate.PASS);//是通过
			paramschanggui.put("应急预案编制及演练", Enumerate.PASS);//是通过
			paramschanggui.put("安全资料报送、收集、归档情况", Enumerate.PASS);//是通过
			paramschanggui.put("及时进行安全隐患整改", Enumerate.PASS);//是通过
			paramschanggui.put("工人工资支付公示情况", Enumerate.PASS);//是通过
		}
		if (paramsshigongxianchagn == null) {//施工现场
			paramsshigongxianchagn = new HashMap<Object, Object>();
			paramsshigongxianchagn.put("现场布置、防扬尘措施", Enumerate.PASS);//是通过
			paramsshigongxianchagn.put("施工道路及交通", Enumerate.PASS);//是通过
			paramsshigongxianchagn.put("职业卫生与环境保护", Enumerate.PASS);//是通过
			paramsshigongxianchagn.put("消防", Enumerate.PASS);//是通过
			paramsshigongxianchagn.put("季节施工", Enumerate.PASS);//是通过
			paramsshigongxianchagn.put("防汛", Enumerate.PASS);//是通过
			paramsshigongxianchagn.put("施工排水", Enumerate.PASS);//是通过
			paramsshigongxianchagn.put("文明施工", Enumerate.PASS);//是通过
			paramsshigongxianchagn.put("线程保卫", Enumerate.PASS);//是通过

		}
		if (paramsfengshuidian == null) {//施工风、水、电及通讯
			paramsfengshuidian = new HashMap<Object, Object>();
			paramsfengshuidian.put("接地、变压器或配电室", Enumerate.PASS);//是通过
			paramsfengshuidian.put("线路敷设", Enumerate.PASS);//是通过
			paramsfengshuidian.put("施工供水", Enumerate.PASS);//是通过
			paramsfengshuidian.put("施工供风", Enumerate.PASS);//是通过
			paramsfengshuidian.put("施工通信", Enumerate.PASS);//是通过
			paramsfengshuidian.put("安全用电方案", Enumerate.PASS);//是通过
		}
		if (paramsanquanhuju == null) {//安全防护用具
			paramsanquanhuju = new HashMap<Object, Object>();

			paramsanquanhuju.put("制造单位的生产许可证或认证证书", Enumerate.PASS);//是通过
			paramsanquanhuju.put("设备进场验收记录", Enumerate.PASS);//是通过
			paramsanquanhuju.put("金属结构、机电设备及安装工程质评资料", Enumerate.PASS);//是通过
		}
		if (paramsanquansheshji == null) {//安全防护设施
			paramsanquansheshji = new HashMap<Object, Object>();
			paramsanquansheshji.put("高处作业", Enumerate.PASS);//是通过
			paramsanquansheshji.put("施工脚手架", Enumerate.PASS);//是通过
			paramsanquansheshji.put("施工走道栈桥与梯子", Enumerate.PASS);//是通过
			paramsanquansheshji.put("安全防护用具", Enumerate.PASS);//是通过
			paramsanquansheshji.put("特种设备管理", Enumerate.PASS);//是通过
			paramsanquansheshji.put("起重与运输管理", Enumerate.PASS);//是通过
		}
		if (paramsanhanjie == null) {//焊接与气割、危险品管理
			paramsanhanjie = new HashMap<Object, Object>();
			paramsanhanjie.put("焊接场地与设备", Enumerate.PASS);//是通过
			paramsanhanjie.put("焊条电弧焊、埋弧焊", Enumerate.PASS);//是	paramsanhanjie.put("气体保护焊", Enumerate.PASS);//是通过
			paramsanhanjie.put("气焊与气割", Enumerate.PASS);//是通过
			paramsanhanjie.put("易燃物品", Enumerate.PASS);//是通过
			paramsanhanjie.put("有毒有害物品", Enumerate.PASS);//是通过
			paramsanhanjie.put("油品（库）管理", Enumerate.PASS);//是通过
		}
		if (paramstufang== null) {//土方
			paramstufang = new HashMap<Object, Object>();
			paramstufang.put("分层压实", Enumerate.PASS);//是通过
			paramstufang.put("回填不带水", Enumerate.PASS);//是通过
			paramstufang.put("回填速率符合设计要求和规范规定", Enumerate.PASS);//是通过
			paramstufang.put("土方质评资料", Enumerate.PASS);//是通过

		}
		if (paramsgangquan== null) {//钢砼
			paramsgangquan = new HashMap<Object, Object>();
			paramsgangquan.put("施工队“三检”制度", Enumerate.PASS);//是通过
			paramsgangquan.put("原材料、中间产品见证送检", Enumerate.PASS);//是通过
			paramsgangquan.put("混泥土的配合比、拌合物的质量、配料计量偏差符合规范规定", Enumerate.PASS);//是通过
			paramsgangquan.put("钢筋的安装质量，钢筋的品种、规格、质量和钢筋的根数符合设计要求，同一截面受力钢筋接头的数量和绑扎接头的搭接长度符合规范规定", Enumerate.PASS);//是通过
			paramsgangquan.put("基坑排水措施到位", Enumerate.PASS);//是通过
			paramsgangquan.put("砼的自然养护条件", Enumerate.PASS);//是通过
			paramsgangquan.put("模板具有足够稳定性、刚度和强度，拼缝严密", Enumerate.PASS);//是通过
			paramsgangquan.put("砼表面无蜂窝、麻面现象", Enumerate.PASS);//是通过
			paramsgangquan.put("钢砼质评资料", Enumerate.PASS);//是通过

		}
		if (paramsqieti== null) {//砌体
			paramsqieti = new HashMap<Object, Object>();
			paramsqieti.put("料石、块石的质量、规格符合设计要求和规范规定", Enumerate.PASS);//是通过
			paramsqieti.put("砌体的砌筑质量，砌体相互错缝，坐实挤紧，空隙嵌实", Enumerate.PASS);//是通过
			paramsqieti.put("浆砌石的砌筑砂浆饱满密实", Enumerate.PASS);//是通过
			paramsqieti.put("砌体质评资料", Enumerate.PASS);//是通过


		}
		if (paramsjiancha== null) {//进度检查
			paramsjiancha = new HashMap<Object, Object>();
			paramsjiancha.put("节点工期实施情况", Enumerate.PASS);//是通过
			paramsjiancha.put("重要部位实施情况防洪度汛实施情况", Enumerate.PASS);//是通过
			paramsjiancha.put("形象进度", Enumerate.PASS);//是通过

		}

		if (params== null) {//保存全部
			params = new HashMap<Object, Object>();

		}
		if("常规项目".equals(sutupe)){
			paramschanggui.put(name,state);
		}
		if("施工现场".equals(sutupe)){
			paramsshigongxianchagn.put(name,state);
		}
		if("施工风、水、电及通讯".equals(sutupe)){
			paramsfengshuidian.put(name,state);
		}
		if("金属结构、机电设备及安装工程".equals(sutupe)){
			paramsanquanhuju.put(name,state);
		}
		if("安全防护设施".equals(sutupe)){
			paramsanquansheshji.put(name,state);
		}
		if("焊接与气割、危险品管理".equals(sutupe)){
			paramsanhanjie.put(name,state);
		}
		if("土方".equals(sutupe)){
			paramstufang.put(name,state);
		}
		if("砌体".equals(sutupe)){
			paramsqieti.put(name,state);
		}
		if("钢砼".equals(sutupe)){
			paramsgangquan.put(name,state);
		}
		if("进度检查".equals(sutupe)){
			paramsjiancha.put(name,state);
		}
//		try {
//			Demo1 objs= (Demo1) MapUitl.mapToObject(paramschanggui,Demo1.class);
//			Log.d("常规项目","objs常规项目"+objs);
//			Object obj=MapUitl.mapToObject(paramschanggui,ResultNameCode.class);
//			Log.d("常规项目","常规项目"+obj);
//		} catch (Exception e) {
//
//			e.printStackTrace();
//		}
		params.put("常规项目", paramschanggui);
		params.put("施工现场", paramsshigongxianchagn);
		params.put("施工风、水、电及通讯", paramsfengshuidian);
		params.put("金属结构、机电设备及安装工程", paramsanquanhuju);
		params.put("安全防护设施", paramsanquansheshji);
		params.put("焊接与气割、危险品管理", paramsanhanjie);
		params.put("土方", paramstufang);
		params.put("砌体", paramsqieti);
		params.put("钢砼", paramsgangquan);
		params.put("进度检查", paramsjiancha);






		return params;
	}

	public static void getClean() {
		 paramschanggui=null;//检查参数上传
		paramsshigongxianchagn=null;//检查参数上传
		 paramsfengshuidian=null;//检查参数上传
		 paramsanquanhuju=null;//检查参数上传
		 paramsanquansheshji=null;//检查参数上传
		 paramsanhanjie=null;//检查参数上传
		 paramsjiancha=null;//检查参数上传
		paramsqieti=null;//检查参数上传
		 paramstufang=null;//检查参数上传
		 paramsgangquan=null;//检查参数上传
	}

}
