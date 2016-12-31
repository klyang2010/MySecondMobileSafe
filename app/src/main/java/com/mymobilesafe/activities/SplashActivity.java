package com.mymobilesafe.activities;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.mrka.mymobilesafe.R;
import com.mymobilesafe.domain.UrlBean;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends AppCompatActivity {

    private static final int LOADMAIN = 0;
    private static final int SHOWUPDATEDIALOG = 1;
    private static final int ERRORCODE = 2;
    private UrlBean json;
    private RelativeLayout rl_root;
    private TextView tv_versionName;
    private int versionCode;
    private long startTimeMillis;
    private AlertDialog dialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ititView();
        initAnimation();
        initData();
        checkVersion();
    }

    private void initData() {
        PackageManager pm = getPackageManager();
        try {
            PackageInfo packageInfo = pm.getPackageInfo(getPackageName(), 0);
            versionCode = packageInfo.versionCode;
            String versionName = packageInfo.versionName;
            tv_versionName.setText(versionName);

        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void initAnimation() {
        ScaleAnimation sa = new ScaleAnimation(0.0f, 1.0f, 0.0f, 1.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        sa.setFillAfter(true);
        sa.setDuration(2000);

        AlphaAnimation aa = new AlphaAnimation(0.0f, 1.0f);
        aa.setDuration(2000);
        aa.setFillAfter(true);

        RotateAnimation ra = new RotateAnimation(0.0f, 360.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        ra.setDuration(2000);
        ra.setFillAfter(true);

        AnimationSet as = new AnimationSet(SplashActivity.this, null);
        as.addAnimation(sa);
        as.addAnimation(aa);
        as.addAnimation(ra);
        rl_root.startAnimation(as);
    }

    private void checkVersion() {
        final int[] errorCode = {-1};
        new Thread() {

            private HttpURLConnection conn;
            private BufferedReader reader;

            @Override
            public void run() {
                try {
                    startTimeMillis = System.currentTimeMillis();
                    URL url = new URL("http://192.168.0.17:8080/mymobilesafe.json");
                    conn = (HttpURLConnection) url.openConnection();
                    conn.setReadTimeout(2000);
                    conn.setConnectTimeout(2000);
                    conn.setRequestMethod("GET");
                    if (conn.getResponseCode() == 200) {
                        InputStream is = conn.getInputStream();
                        reader = new BufferedReader(new InputStreamReader(is));
                        String line = reader.readLine();
                        StringBuffer sbuffer = new StringBuffer();
                        if (line != null) {
                            sbuffer.append(line);
                            line = reader.readLine();
                        }
                        json = parseJSON(sbuffer);
                    }
                } catch (MalformedURLException e) {//url格式异常
                    errorCode[0] = 2000;
                    e.printStackTrace();
                } catch (IOException e) {//网络连接不上
                    errorCode[0] = 1000;
                    e.printStackTrace();
                } catch (JSONException e) {//json格式错误
                    errorCode[0] = 3000;
                    e.printStackTrace();
                } finally {
                    Message msg = Message.obtain();
                    if (errorCode[0] == -1) {
                        msg.what = isNewVersion();
                    } else {
                        msg.what = ERRORCODE;
                        msg.arg1 = errorCode[0];
                    }

                    long endTimeMillis = System.currentTimeMillis();
                    if (endTimeMillis - startTimeMillis < 2000) {
                        SystemClock.sleep(endTimeMillis - startTimeMillis);
                    }
                    handler.sendMessage(msg);

                    if (reader == null || conn == null) {
                        return;
                    } else {
                        try {
                            reader.close();
                            conn.disconnect();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        }

                .

                        start();
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADMAIN:
                    loadMain();
                    break;
                case SHOWUPDATEDIALOG:
                    showUpdateDialog();
                    break;
                case ERRORCODE:
                    switch (msg.arg1) {
                        case 1000:
                            Toast.makeText(getApplicationContext(), "无网络", Toast.LENGTH_LONG).show();
                            loadMain();
                            break;
                        case 2000:
                            Toast.makeText(getApplicationContext(), "url格式错误", Toast.LENGTH_LONG).show();
                            loadMain();
                            break;
                        case 3000:
                            Toast.makeText(getApplicationContext(), "json格式错误", Toast.LENGTH_LONG).show();
                            loadMain();
                            break;
                    }
            }
        }
    };

    private void showUpdateDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(SplashActivity.this, 0);
        builder.setTitle("更新");
        builder.setMessage("应用做了一下更新:" + json.getDesc());
        builder.setPositiveButton("更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                installApk();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("取消更新", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                loadMain();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                loadMain();
            }
        });
        dialog = builder.create();
        dialog.show();
    }


    private void installApk() {
        Intent intent = new Intent("android.intent.action.VIEW");
        intent.addCategory("android.intent.category.DEFAULT");
        File file = new File(Environment.getExternalStorageDirectory(), "mymobilesafe.apk");
        intent.setDataAndType(Uri.fromFile(file), "application/vnd.android.package-archive");
        startActivity(intent);
    }

    private int isNewVersion() {
        int serverCode = json.getVersionCode();

        if (versionCode == serverCode) {
            return LOADMAIN;
        } else {
            return SHOWUPDATEDIALOG;
        }
    }

    private void loadMain() {
        Intent intent = new Intent(SplashActivity.this, HomeActivity.class);
        startActivity(intent);
    }

    private UrlBean parseJSON(StringBuffer sbuffer) throws JSONException {
        UrlBean bean = new UrlBean();
        JSONObject object = new JSONObject(sbuffer + "");
        int versionCode = object.getInt("version");
        String apkPath = object.getString("url");
        String desc = object.getString("desc");
        bean.setVersionCode(versionCode);
        bean.setUrl(apkPath);
        bean.setDesc(desc);

        return bean;
    }

    private void ititView() {
        setContentView(R.layout.activity_splash);
        rl_root = (RelativeLayout) findViewById(R.id.rl_splash_root);
        tv_versionName = (TextView) findViewById(R.id.tv_splash_versionName);
    }
}
