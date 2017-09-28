package com.example.administrator.irrigationworks.Ui.frament;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.net.http.SslError;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.webkit.SslErrorHandler;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.irrigationworks.R;
import com.example.administrator.irrigationworks.Ui.BaseAppCompatActivity;

import butterknife.Bind;
import butterknife.ButterKnife;


/**
 * 发送通知界面
 * Created by Administrator on 2016/9/12 0012.
 */
public class NotificationActivity extends BaseAppCompatActivity {
    public static final String EXTRA_WORD_PATH = "EXTRA_WORD_PATH";
    @Bind(R.id.btn_left)
    ImageView btnLeft;
    @Bind(R.id.title)
    TextView title;
    @Bind(R.id.btn_right)
    ImageView btnRight;
    @Bind(R.id.layout_search_bar)
    LinearLayout layoutSearchBar;
    @Bind(R.id.web_view)
    WebView wvShopShow;
private String path;
    // 进度对话框
    private ProgressDialog mProDialog;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.bind(this);
        btnLeft.setImageResource(R.mipmap.nav_menu_fanhui);
        btnLeft.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        mProDialog = new ProgressDialog(this);
        mProDialog.setCancelable(true);
        mProDialog.setTitle("请稍后");
        mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialog) {
                // cancel进度框时,取消正在进行的操作

            }
        });
        mProDialog.setMessage("请稍后...");
        mProDialog.show();
        title.setText("公告内容");
        path=getIntent().getStringExtra("path");
        initView();

//
    }
    private void initView() {
        try {
            //  sdcard/test2.docx为本地doc文件的路径
//            Intent intent =  OpenFiles.getWordFileIntent(getFromAssets());
//            startActivity(intent);
        } catch (Exception e) {
            //没有安装第三方的软件会提示
            Toast toast = Toast.makeText(NotificationActivity.this, "没有找到打开该文件的应用程序", Toast.LENGTH_SHORT);
            toast.show();
        }
        try {
//            mProDialog = new ProgressDialog(this);
//            mProDialog.setCancelable(true);
//            mProDialog.setTitle("请稍后");
//            mProDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
//                @Override
//                public void onCancel(DialogInterface dialog) {
//                    // cancel进度框时,取消正在进行的操作
//
//                }
//            });
//            mProDialog.setMessage("请稍后...");
//            mProDialog.show();
            wvShopShow.loadUrl(path);
            wvShopShow.setHorizontalScrollBarEnabled(false);//水平不显示
            wvShopShow.setVerticalScrollBarEnabled(false); //垂直不显示
            WebSettings settings = wvShopShow.getSettings();
            settings.setJavaScriptEnabled(true);
            wvShopShow.getSettings().setBlockNetworkImage(false);//解决图片不显示
            wvShopShow.getSettings().setDomStorageEnabled(true);//有可能是DOM储存API没有打开
            wvShopShow.setHorizontalScrollBarEnabled(false);//水平不显示
            wvShopShow.setVerticalScrollBarEnabled(false); //垂直不显示
            //设置缓存模式
            wvShopShow.getSettings().setCacheMode(WebSettings.LOAD_DEFAULT);
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                wvShopShow.getSettings().setMixedContentMode(WebSettings.MIXED_CONTENT_ALWAYS_ALLOW);
            }

            wvShopShow.setWebViewClient(new WebViewClient() {
                @Override
                public boolean shouldOverrideUrlLoading(WebView view, String url) {


                    return true;
                }

                @Override
                public void onReceivedSslError(WebView view, SslErrorHandler handler, SslError error) {
                    super.onReceivedSslError(view, handler, error);
                    handler.proceed(); // 接受所有网站的证书
                }
            });
            //加载完成
            wvShopShow.setWebViewClient(new WebViewClient()
            {
                @Override
                public void onPageFinished(WebView view, String url)
                {
                    mProDialog.dismiss();
                    //开始
                    super.onPageFinished(view, url);
                }
                @Override
                public void onPageStarted(WebView view, String url, Bitmap favicon)
                {

                    //结束
                    super.onPageStarted(view, url, favicon);

                }
            });

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ButterKnife.unbind(this);
    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_get_message_look;
    }
}
