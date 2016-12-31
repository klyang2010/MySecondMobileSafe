package com.mymobilesafe.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.mrka.mymobilesafe.R;
import com.mymobilesafe.utils.MyConstants;
import com.mymobilesafe.utils.SpTools;

/**
 * Created by mrka on 16-12-29.
 */

public class LostFindActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (SpTools.getBooean(getApplicationContext(), MyConstants.ISSETUP, false)) {
            inintView();
        } else {
            Intent intent = new Intent(LostFindActivity.this, Setup1Activity.class);
            startActivity(intent);
            finish();
        }
    }

    private void inintView() {

        setContentView(R.layout.activity_lostfind);
    }
}

