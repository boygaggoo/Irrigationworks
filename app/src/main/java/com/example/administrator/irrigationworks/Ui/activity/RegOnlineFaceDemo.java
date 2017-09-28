package com.example.administrator.irrigationworks.Ui.activity;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.BitmapFactory.Options;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.irrigationworks.Application.NimUIKit;
import com.example.administrator.irrigationworks.Factory.FinalTozal;
import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;
import com.example.administrator.irrigationworks.Ui.litepal.RoleBeanLitepal;
import com.example.administrator.irrigationworks.UtilsTozals.FaceUtil;
import com.example.administrator.irrigationworks.UtilsTozals.FileUtilcll;
import com.example.administrator.irrigationworks.UtilsTozals.ImageUpUtils;
import com.example.administrator.irrigationworks.UtilsTozals.UtilsTool;
import com.iflytek.cloud.ErrorCode;
import com.iflytek.cloud.FaceRequest;
import com.iflytek.cloud.RequestListener;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechError;
import com.iflytek.cloud.SpeechUtility;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import me.nereo.multi_image_selector.utils.ImageUtils;

/**
 * 在线人脸示例
 *
 * @author iFlytek &nbsp;&nbsp;&nbsp;<a href="http://http://www.xfyun.cn/">讯飞语音云</a>
 */
public class RegOnlineFaceDemo extends BaseAppCompatActivity implements OnClickListener {
    private final int REQUEST_PICTURE_CHOOSE = 1;
    private final int REQUEST_CAMERA_IMAGE = 2;
    @Bind(R.id.iv_back_left)
    ImageView ivBackLeft;
    @Bind(R.id.toolbar_title)
    TextView toolbarTitle;
    @Bind(R.id.iv_add)
    ImageView ivAdd;
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.online_detect)
    RadioButton onlineDetect;
    @Bind(R.id.online_align)
    RadioButton onlineAlign;
    @Bind(R.id.gps_radio_group)
    RadioGroup gpsRadioGroup;
    @Bind(R.id.online_camera)
    ImageView onlineCamera;
    @Bind(R.id.online_img)
    ImageView onlineImg;
    @Bind(R.id.online_authid)
    EditText onlineAuthid;
    @Bind(R.id.online_pick)
    Button onlinePick;
    @Bind(R.id.online_reg)
    Button onlineReg;
    @Bind(R.id.network_error)
    ImageView networkError;
    @Bind(R.id.online_regs)
    Button onlineRegs;
    @Bind(R.id.rl_chongxin)
    RelativeLayout rlChongxin;
    @Bind(R.id.rl_total)
    RelativeLayout rlTotal;
    private Bitmap mImage = null;
    private byte[] mImageData = null;
    // authid为6-18个字符长度，用于唯一标识用户
    private String mAuthid = null;
    private Toast mToast;
    // 进度对话框
    private ProgressDialog mProDialog;
    // 拍照得到的照片文件
    private File mPictureFile;
    // FaceRequest对象，集成了人脸识别的各种功能
    private FaceRequest mFaceRequest;
    private RadioGroup gps_radio_group;
    //注册人脸识别的
    private ArrayList<String> NetmImagePathDatas;
    private ArrayList<String> mImagePathTasks;
    private List<RoleBeanLitepal> schooldates;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SpeechUtility.createUtility(this, "appid=" + getString(R.string.app_id));
        ButterKnife.bind(this);
        onlinePick.setOnClickListener(RegOnlineFaceDemo.this);
        onlineReg.setOnClickListener(RegOnlineFaceDemo.this);
        rlChongxin.setOnClickListener(RegOnlineFaceDemo.this);
        onlineCamera.setOnClickListener(RegOnlineFaceDemo.this);
        onlineDetect.setOnClickListener(RegOnlineFaceDemo.this);
        onlineAlign.setOnClickListener(RegOnlineFaceDemo.this);
        ivBackLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        toolbarTitle.setText("人脸注册");
        ivBackLeft.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mToast = Toast.makeText(this, "", Toast.LENGTH_SHORT);
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");
        mProDialog.setOnCancelListener(new OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作
                if (null != mFaceRequest) {
                    mFaceRequest.cancel();
                }
            }
        });
        onlineRegs.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                retSUCCESS = ErrorCode.SUCCESS;

                schooldates = new ArrayList<>();
                schooldates = DataSupport.findAll(RoleBeanLitepal.class);
                if (schooldates != null) {
                    if (schooldates.get(0).getRg().length() > 0) {
                        mAuthid = "";
                        mAuthid = "a" + schooldates.get(0).getUserid().substring(0, 6);
                        onlineAuthid.setText(mAuthid);
                    }
                }
//                mAuthid = schooldates.get(0).getUserid()+"";
//                List<MianBuID> secuGetDta=DataSupport.findAll(MianBuID.class);
//                mAuthid = secuGetDta.get(0).getMid();

                Log.d("qqqqq", "此时的面部ID" + mAuthid);
                if (TextUtils.isEmpty(mAuthid)) {
                    showTip("authid不能为空");
                    return;
                }
                //获取IrrigationWordsActitivysa里面的参数


                Log.d("qqqqq", "此时的面部ID" + mImageData);

                if (null != mImageData) {
                    mProDialog = new ProgressDialog(RegOnlineFaceDemo.this);
                    mProDialog.setCancelable(true);
                    mProDialog.setMessage("验证中...");
                    mProDialog.show();
                    // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                    // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                    retSUCCESS = mFaceRequest.sendRequest(mImageData, mRequestListener);
                } else {
                    showTip("请拍照后再验证");
                }
            }
        });
        NetmImagePathDatas = new ArrayList<>();
        mImagePathTasks = new ArrayList<>();
        mFaceRequest = new FaceRequest(this);
        gpsRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                int ret = ErrorCode.SUCCESS;
                switch (checkedId) {

                    case R.id.online_detect:
                        Log.d("qc", "点击了检测中");
                        //点击执行逻辑
                        if (null != mImageData) {
                            mProDialog.setMessage("检测中...");
                            mProDialog.show();
                            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "detect");
                            ret = mFaceRequest.sendRequest(mImageData, mRequestListener);
                        } else {
                            showTip("请选择图片后再检测");
                        }
                        break;
                    case R.id.online_align:
                        Log.d("qc", "点击了聚焦中");
                        if (null != mImageData) {
                            mProDialog.setMessage("聚焦中...");
                            mProDialog.show();
                            mFaceRequest.setParameter(SpeechConstant.WFR_SST, "align");
                            ret = mFaceRequest.sendRequest(mImageData, mRequestListener);
                        } else {
                            showTip("请选择图片后再聚集");
                        }
                        break;
                    default:
                        break;
                }

            }
        });
    }


    @Override
    protected int getLayoutId() {
        return R.layout.activity_regs_online_demo;
    }

    private void register(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("注册失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {

            //注册成功后，调用后台上传

            showData();
//            finish();
        } else {
            showTip("注册失败");
        }
    }

    private AlertDialog mIsSetPwdDialog;

    private void showData() {
        if (NetmImagePathDatas.isEmpty() || NetmImagePathDatas == null) {
            UtilsTool.showShortToast(RegOnlineFaceDemo.this, "图片不能为空");
            mProDialog.dismiss();
            return;
        }
        mProDialog = new ProgressDialog(RegOnlineFaceDemo.this);
        mProDialog.setCancelable(true);
        mProDialog.setMessage("验证中...");
        mProDialog.show();
        for (int i = 0; i < NetmImagePathDatas.size(); i++) {
            Bitmap map = ImageUtils.getSmallBitmap(NetmImagePathDatas.get(i));
            String urlpath = FileUtilcll.saveFile(RegOnlineFaceDemo.this, (System.currentTimeMillis() + 1) + ".jpg", map);
            mImagePathTasks.add(urlpath);
            if (i == NetmImagePathDatas.size() - 1) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // 拍照的图片保存
                        String[] toBeStored = mImagePathTasks.toArray(new String[mImagePathTasks.size()]);
                        Log.d("toBeStored", "toBeStored" + toBeStored);
                        String path = ImageUpUtils.uploadFile(NimUIKit.getToken(), FinalTozal.registFaceInfo, toBeStored);
                        if (path.equals("图片上传失败") || TextUtils.isEmpty(path)) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showTip("注册失败");
                                    mProDialog.dismiss();
                                }
                            });
                            return;
                        } else {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    showTip("注册成功");
                                    mProDialog.dismiss();
                                    AlertDialog.Builder builder = new AlertDialog.Builder(RegOnlineFaceDemo.this);
                                    View view = View.inflate(RegOnlineFaceDemo.this, R.layout.dialog_pic, null);
                                    TextView tvCancal = (TextView) view.findViewById(R.id.bt_cancel);
                                    TextView tvExit = (TextView) view.findViewById(R.id.bt_exit);
                                    tvCancal.setOnClickListener(new OnClickListener() {
                                        @Override
                                        public void onClick(View v) {//取消
                                            mIsSetPwdDialog.dismiss();
                                        }
                                    });
                                    tvExit.setOnClickListener(new OnClickListener() {//确认
                                        @Override
                                        public void onClick(View v) {
                                            //清理数据库
                                            mPictureFile = new File(Environment.getExternalStorageDirectory(),
                                                    "picture" + System.currentTimeMillis() / 1000 + ".jpg");
                                            // 启动拍照,并保存到临时文件
                                            //设置再次验证人脸识别标识
                                            NimUIKit.setFace("再次验证");
                                            mImageData = null;
                                            Intent mIntent = new Intent();
                                            mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                                            mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
                                            mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                                            mIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                                            mIntent.putExtra("autofocus", true); // 自动对焦
                                            mIntent.putExtra("fullScreen", false); // 全屏
                                            startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);

                                        }
                                    });
                                    builder.setView(view);
                                    mIsSetPwdDialog = builder.show();

//                                    finish();
                                }
                            });
                        }
                    }
                }).start();
            }

        }
    }

    private void verify(JSONObject obj) throws JSONException {
        Log.d("qc", "需要验证的信息" + obj);
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("验证失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            Log.d("qc", "需要验证成功的信息" + obj);
            if (obj.getBoolean("verf")) {
                Log.d("qc", "需要验证成功的信息" + obj);
                showTip("注册成功,等待管理员审核");
                NimUIKit.setFace("");
                finish();
            } else {
                showTip("验证不通过");
            }
        } else {
            showTip("验证失败");
        }
    }

    private void detect(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("检测失败");
            return;
        }

        if ("success".equals(obj.get("rst"))) {
            JSONArray faceArray = obj.getJSONArray("face");

            Paint paint = new Paint();
            paint.setColor(Color.RED);
            paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);

            Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
                    mImage.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(mImage, new Matrix(), null);
            for (int i = 0; i < faceArray.length(); i++) {
                float x1 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("left");
                float y1 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("top");
                float x2 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("right");
                float y2 = (float) faceArray.getJSONObject(i)
                        .getJSONObject("position").getDouble("bottom");
                paint.setStyle(Style.STROKE);
                canvas.drawRect(new Rect((int) x1, (int) y1, (int) x2, (int) y2),
                        paint);
            }

            mImage = bitmap;
            ((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
        } else {
            showTip("检测失败");
        }
    }

    @SuppressWarnings("rawtypes")
    private void align(JSONObject obj) throws JSONException {
        int ret = obj.getInt("ret");
        if (ret != 0) {
            showTip("聚焦失败");
            return;
        }
        if ("success".equals(obj.get("rst"))) {
            Paint paint = new Paint();
            paint.setColor(Color.BLUE);
            paint.setStrokeWidth(Math.max(mImage.getWidth(), mImage.getHeight()) / 100f);

            Bitmap bitmap = Bitmap.createBitmap(mImage.getWidth(),
                    mImage.getHeight(), Config.ARGB_8888);
            Canvas canvas = new Canvas(bitmap);
            canvas.drawBitmap(mImage, new Matrix(), null);

            JSONArray faceArray = obj.getJSONArray("result");
            for (int i = 0; i < faceArray.length(); i++) {
                JSONObject landmark = faceArray.getJSONObject(i).getJSONObject(
                        "landmark");
                Iterator it = landmark.keys();
                while (it.hasNext()) {
                    String key = (String) it.next();
                    JSONObject postion = landmark.getJSONObject(key);
                    canvas.drawPoint((float) postion.getDouble("x"),
                            (float) postion.getDouble("y"), paint);
                }
            }

            mImage = bitmap;
            ((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
        } else {
            showTip("聚焦失败");
        }
    }

    private RequestListener mRequestListener = new RequestListener() {

        @Override
        public void onEvent(int eventType, Bundle params) {
        }

        @Override
        public void onBufferReceived(byte[] buffer) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            try {
                String result = new String(buffer, "utf-8");
                Log.d("FaceDemo", result);

                JSONObject object = new JSONObject(result);
                String type = object.optString("sst");
                if ("reg".equals(type)) {
                    register(object);
                } else if ("verify".equals(type)) {
                    verify(object);
                } else if ("detect".equals(type)) {
                    detect(object);
                } else if ("align".equals(type)) {
                    align(object);
                }
            } catch (UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            } catch (JSONException e) {
                // TODO: handle exception
            }
        }

        @Override
        public void onCompleted(SpeechError error) {
            if (null != mProDialog) {
                mProDialog.dismiss();
            }

            if (error != null) {
                switch (error.getErrorCode()) {
                    case ErrorCode.MSP_ERROR_ALREADY_EXIST:
                        showTip("authid已经被注册，请更换后再试");
                        break;

                    default:
                        showTip(error.getPlainDescription(true));
                        break;
                }
            }
        }
    };

    @Override
    public void onClick(View view) {
        int ret = ErrorCode.SUCCESS;
        switch (view.getId()) {
            case R.id.rl_chongxin:
                mPictureFile = new File(Environment.getExternalStorageDirectory(),
                        "picture" + System.currentTimeMillis() / 1000 + ".jpg");
                // 启动拍照,并保存到临时文件
                Intent mIntents = new Intent();
                mIntents.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mIntents.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
                mIntents.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                mIntents.putExtra("android.intent.extras.CAMERA_FACING", 1);
                mIntents.putExtra("autofocus", true); // 自动对焦
                mIntents.putExtra("fullScreen", false); // 全屏
                startActivityForResult(mIntents, REQUEST_CAMERA_IMAGE);
                break;
            case R.id.online_pick:
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_PICK);
                startActivityForResult(intent, REQUEST_PICTURE_CHOOSE);
                break;
            case R.id.online_reg:
                Log.d("qc", "点击了注册");
//                mAuthid = ((EditText) findViewById(R.id.online_authid)).getText().toString());
                schooldates = new ArrayList<>();
                schooldates = DataSupport.findAll(RoleBeanLitepal.class);
                if (schooldates != null) {
                    if (schooldates.get(0).getRg().length() > 10) {
                        mAuthid = "";
                        mAuthid = "a" + schooldates.get(0).getUserid().substring(0, 6);
                        onlineAuthid.setText(mAuthid);

                    }

                }

//                mAuthid = NimUIKit.getMid()+"";
                if (TextUtils.isEmpty(mAuthid)) {
                    showTip("注册图片号码不能为空");
                    return;
                }
                if (null != mImageData) {
                    mProDialog.setMessage("注册中...");
                    mProDialog.show();
                    // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                    // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "reg");
                    mFaceRequest.setParameter("property", "del");

                    ret = mFaceRequest.sendRequest(mImageData, mRequestListener);
                } else {
                    showTip("请选择图片后再注册");
                }
                break;
            case R.id.online_detect:
                break;
            case R.id.online_align:
                break;
            case R.id.online_camera:
                Log.d("qc", "点击了相机");
                // 设置相机拍照后照片保存路径
                mPictureFile = new File(Environment.getExternalStorageDirectory(),
                        "picture" + System.currentTimeMillis() / 1000 + ".jpg");
                // 启动拍照,并保存到临时文件
                Intent mIntent = new Intent();
                mIntent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                mIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mPictureFile));
                mIntent.putExtra(MediaStore.Images.Media.ORIENTATION, 0);
                mIntent.putExtra("android.intent.extras.CAMERA_FACING", 1);
                mIntent.putExtra("autofocus", true); // 自动对焦
                mIntent.putExtra("fullScreen", false); // 全屏
                startActivityForResult(mIntent, REQUEST_CAMERA_IMAGE);
                break;
            default:
                break;
        }//end of switch
        if (ErrorCode.SUCCESS != ret) {
            mProDialog.dismiss();
            showTip("出现错误：" + ret);
        }
    }

    private int retSUCCESS;

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        String fileSrc = null;
        NetmImagePathDatas = null;
        NetmImagePathDatas = new ArrayList<>();
        if (requestCode == REQUEST_PICTURE_CHOOSE) {
            if ("file".equals(data.getData().getScheme())) {
                // 有些低版本机型返回的Uri模式为file
                fileSrc = data.getData().getPath();
            } else {
                // Uri模型为content
                String[] proj = {MediaStore.Images.Media.DATA};
                Cursor cursor = getContentResolver().query(data.getData(), proj,
                        null, null, null);
                cursor.moveToFirst();
                int idx = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                fileSrc = cursor.getString(idx);
                cursor.close();
            }
            // 跳转到图片裁剪页面
            FaceUtil.cropPicture(this, Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == REQUEST_CAMERA_IMAGE) {
            if (null == mPictureFile) {
                showTip("拍照失败，请重试");
                return;
            }
            fileSrc = mPictureFile.getAbsolutePath();
            updateGallery(fileSrc);
            // 跳转到图片裁剪页面
            onlineCamera.setVisibility(View.GONE);
            rlTotal.setVisibility(View.VISIBLE);
            FaceUtil.cropPicture(this, Uri.fromFile(new File(fileSrc)));
        } else if (requestCode == FaceUtil.REQUEST_CROP_IMAGE) {
            // 获取返回数据
            Bitmap bmp = data.getParcelableExtra("data");
            // 若返回数据不为null，保存至本地，防止裁剪时未能正常保存
            if (null != bmp) {
                FaceUtil.saveBitmapToFile(RegOnlineFaceDemo.this, bmp);
            }
            // 获取图片保存路径
            fileSrc = FaceUtil.getImagePath(RegOnlineFaceDemo.this);
            if (fileSrc != null) {
                NetmImagePathDatas.add("" + fileSrc);//添加图片
            }
            Log.d("li", "REQUEST_CROP_IMAGE" + fileSrc);

            // 获取图片的宽和高
            Options options = new Options();
            options.inJustDecodeBounds = true;
            mImage = BitmapFactory.decodeFile(fileSrc, options);
            // 压缩图片
            options.inSampleSize = Math.max(1, (int) Math.ceil(Math.max(
                    (double) options.outWidth / 1024f,
                    (double) options.outHeight / 1024f)));
            options.inJustDecodeBounds = false;
            mImage = BitmapFactory.decodeFile(fileSrc, options);
            // 若mImageBitmap为空则图片信息不能正常获取
            if (null == mImage) {
                showTip("图片信息无法正常获取！");
                return;
            }
            // 部分手机会对图片做旋转，这里检测旋转角度
            int degree = FaceUtil.readPictureDegree(fileSrc);
            if (degree != 0) {
                // 把图片旋转为正的方向
                mImage = FaceUtil.rotateImage(degree, mImage);
            }
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            //可根据流量及网络状况对图片进行压缩
            mImage.compress(Bitmap.CompressFormat.JPEG, 80, baos);
            mImageData = baos.toByteArray();
            if (!TextUtils.isEmpty(NimUIKit.getFace()) && "再次验证".equals(NimUIKit.getFace())) {
                retSUCCESS = ErrorCode.SUCCESS;

                schooldates = new ArrayList<>();
                schooldates = DataSupport.findAll(RoleBeanLitepal.class);
                if (schooldates != null) {
                    if (schooldates.get(0).getRg().length() > 10) {
                        mAuthid = "";
                        mAuthid = "a" + schooldates.get(0).getUserid().substring(0, 6);
                        onlineAuthid.setText(mAuthid);
                    }
                }
//                mAuthid = schooldates.get(0).getUserid()+"";
//                List<MianBuID> secuGetDta=DataSupport.findAll(MianBuID.class);
//                mAuthid = secuGetDta.get(0).getMid();

                Log.d("qqqqq", "此时的面部ID" + mAuthid);
                if (TextUtils.isEmpty(mAuthid)) {
                    showTip("authid不能为空");
                    return;
                }
                //获取IrrigationWordsActitivysa里面的参数


                Log.d("qqqqq", "此时的面部ID" + mImageData);

                if (null != mImageData) {
                    mProDialog = new ProgressDialog(RegOnlineFaceDemo.this);
                    mProDialog.setCancelable(true);
                    mProDialog.setMessage("验证中...");
                    mProDialog.show();
                    // 设置用户标识，格式为6-18个字符（由字母、数字、下划线组成，不得以数字开头，不能包含空格）。
                    // 当不设置时，云端将使用用户设备的设备ID来标识终端用户。
                    mFaceRequest.setParameter(SpeechConstant.AUTH_ID, mAuthid);
                    mFaceRequest.setParameter(SpeechConstant.WFR_SST, "verify");
                    retSUCCESS = mFaceRequest.sendRequest(mImageData, mRequestListener);
                } else {
                    showTip("请拍照后再验证");
                }

            } else {


                ((ImageView) findViewById(R.id.online_img)).setImageBitmap(mImage);
            }

        }

    }

    @Override
    public void finish() {
        if (null != mProDialog) {
            mProDialog.dismiss();
        }
        super.finish();
    }

    private void updateGallery(String filename) {
        MediaScannerConnection.scanFile(this, new String[]{filename}, null,
                new MediaScannerConnection.OnScanCompletedListener() {

                    @Override
                    public void onScanCompleted(String path, Uri uri) {

                    }
                });
    }

    private void showTip(final String str) {
        mToast.setText(str);
        mToast.show();
    }

}
