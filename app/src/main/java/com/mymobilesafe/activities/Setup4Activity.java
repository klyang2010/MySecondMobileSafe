package com.mymobilesafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.widget.CheckBox;
import android.widget.CompoundButton;

import com.mrka.mymobilesafe.R;
import com.mymobilesafe.services.LostFindService;
import com.mymobilesafe.utils.MyConstants;
import com.mymobilesafe.utils.ServiceUtils;
import com.mymobilesafe.utils.SpTools;

/**
 * Created by mrka on 16-12-29.
 */

public class Setup4Activity extends BaseSetupActivity {

    private CheckBox cb_isprotected;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initView();
        initData();
        initEvent();
    }

    @Override
    public void initData() {
        if (ServiceUtils.isServiceRunning(getApplicationContext(), "com.mymobilesafe.services.LostFindService")) {
            cb_isprotected.setChecked(true);
        } else {
            cb_isprotected.setChecked(false);
        }
    }

    @Override
    public void initEvent() {
        cb_isprotected.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (cb_isprotected.isChecked()) {
                    Intent intent = new Intent(Setup4Activity.this, LostFindService.class);
                    startService(intent);
                } else {
                    System.out.println("false");
                    Intent intent = new Intent(Setup4Activity.this, LostFindService.class);
                    stopService(intent);
                }
            }
        });
    }

    public void initView() {
        setContentView(R.layout.activity_setup4);
        cb_isprotected = (CheckBox) findViewById(R.id.cb_setup4_isprotected);
    }

    @Override
    protected void prevActivity() {
        startActivity(Setup3Activity.class);
    }

    @Override
    protected void nextActivity() {
        SpTools.putBoolean(getApplicationContext(), MyConstants.ISSETUP, true);
        startActivity(LostFindActivity.class);
    }
}
