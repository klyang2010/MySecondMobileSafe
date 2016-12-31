package com.mymobilesafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mrka.mymobilesafe.R;
import com.mymobilesafe.utils.Md5Utils;
import com.mymobilesafe.utils.MyConstants;
import com.mymobilesafe.utils.SpTools;

/**
 * Created by mrka on 16-12-28.
 */

public class HomeActivity extends AppCompatActivity {

    private String[] names = new String[]{"手机防盗", "通讯卫士", "软件管家", "进程管理", "流量统计", "病毒查杀", "缓存清理", "高级工具", "设置中心",};
    ;
    private int[] icons = new int[]{R.drawable.safe, R.drawable.callmsgsafe, R.drawable.app,
            R.drawable.taskmanager, R.drawable.netmanager, R.drawable.trojan,
            R.drawable.sysoptimize, R.drawable.atools, R.drawable.settings};
    ;
    private GridView gv_item;
    private MyAdapter adapter;
    private EditText et_password_one;
    private EditText et_password_two;
    private AlertDialog settingpass_dialog;
    private AlertDialog enterPass_dialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initDate();
        initEvent();
    }

    private void initEvent() {
        gv_item.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                switch (i) {
                    case 0:
                        if (TextUtils.isEmpty(SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, ""))) {
                            showSettingPasswordDialog();
                        } else {
                            showEnterPasswordDialog();
                        }
                }
            }
        });
    }

    private void showEnterPasswordDialog() {
        View view = View.inflate(getApplicationContext(), R.layout.enter_password_dialog, null);
        Button bt_enter = (Button) view.findViewById(R.id.bt_enter_enter);
        Button bt_cancel = (Button) view.findViewById(R.id.bt_enter_cancel);
        final EditText et_enter_pass = (EditText) view.findViewById(R.id.et_enterpassword_enterpass);

        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        builder.setView(view);
        bt_enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(et_enter_pass + "")) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
                } else {
                    if (SpTools.getString(getApplicationContext(), MyConstants.PASSWORD, "").equals(Md5Utils.md5(Md5Utils.md5(et_enter_pass.getText().toString().trim())))) {
                        Intent intent = new Intent(HomeActivity.this, LostFindActivity.class);
                        startActivity(intent);
                        enterPass_dialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "密码不正确", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
        bt_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                enterPass_dialog.dismiss();
            }
        });
        enterPass_dialog = builder.create();
        enterPass_dialog.show();
    }

    private void showSettingPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(HomeActivity.this);
        View view = View.inflate(getApplicationContext(), R.layout.setting_password_dialog, null);
        builder.setView(view);

        et_password_one = (EditText) view.findViewById(R.id.et_item_setting_pass_one);
        et_password_two = (EditText) view.findViewById(R.id.et_item_setting_pass_one);
        Button bt_settingpass_set = (Button) view.findViewById(R.id.bt_settingpass_setpass);
        Button bt_settingpass_cancel = (Button) view.findViewById(R.id.bt_settingpass_cancelpass);

        bt_settingpass_set.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String password_one = et_password_one.getText().toString().trim();
                String password_two = et_password_two.getText().toString().trim();
                if (password_one == null || password_two == null) {
                    Toast.makeText(getApplicationContext(), "密码不能为空", Toast.LENGTH_LONG).show();
                } else if (!password_one.equals(password_two)) {
                    Toast.makeText(getApplicationContext(), "密码不一致", Toast.LENGTH_LONG).show();
                } else {
                    String password = Md5Utils.md5(Md5Utils.md5(password_one));
                    SpTools.putString(getApplicationContext(), MyConstants.PASSWORD, password);
                    settingpass_dialog.dismiss();
                }
            }
        });
        bt_settingpass_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                settingpass_dialog.dismiss();
            }
        });
        settingpass_dialog = builder.create();
        settingpass_dialog.show();
    }

    private void initDate() {
        adapter = new MyAdapter();
        gv_item.setAdapter(adapter);
    }

    private void initView() {
        setContentView(R.layout.activity_home);
        gv_item = (GridView) findViewById(R.id.gv_home_menus);
    }

    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return names.length;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View view_item = View.inflate(HomeActivity.this, R.layout.item_home_gridview, null);
            ImageView iv_item_icon = (ImageView) view_item.findViewById(R.id.iv_item_home_gridview_icon);
            TextView tv_item_name = (TextView) view_item.findViewById(R.id.tv_item_home_gridview_name);
            iv_item_icon.setImageResource(icons[i]);
            tv_item_name.setText(names[i]);

            return view_item;
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }


    }
}
