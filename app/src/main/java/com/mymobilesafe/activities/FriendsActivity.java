package com.mymobilesafe.activities;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.support.annotation.RequiresApi;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.mrka.mymobilesafe.R;
import com.mymobilesafe.domain.ContactsBean;
import com.mymobilesafe.utils.MyConstants;
import com.mymobilesafe.engine.ReadContactsEngine;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrka on 16-12-30.
 */

public class FriendsActivity extends ListActivity {
    private static final int LOADING = 0;
    private static final int FINISH = 1;
    private ProgressDialog pd;
    private List<ContactsBean> datas = new ArrayList<ContactsBean>();
    private MyAdapter adapter;
    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        listView = getListView();
        initData();
        adapter = new MyAdapter();
        listView.setAdapter(adapter);
        initEvent();
    }

    private void initEvent() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                ContactsBean bean = new ContactsBean();
                bean = datas.get(i);
                String phone = bean.getPhone();
                Intent intent = new Intent();
                intent.putExtra(MyConstants.SAFENUMBER, phone);
                setResult(1, intent);
                finish();
            }
        });
    }

    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case LOADING:
                    pd = new ProgressDialog(FriendsActivity.this);
                    pd.setTitle("注意");
                    pd.setMessage("正在玩命加载数据。。。。");
                    pd.show();
                    break;
                case FINISH:
                    if (pd != null) {
                        pd.dismiss();
                        pd = null;
                    }
                    adapter.notifyDataSetChanged();
                    break;
            }
            super.handleMessage(msg);
        }
    };

    private class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return datas.size();
        }

        @Override
        public Object getItem(int i) {
            return null;
        }

        @Override
        public long getItemId(int i) {
            return 0;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            View list_friends = View.inflate(getApplicationContext(), R.layout.item_friends_listview, null);
            TextView tv_name = (TextView) list_friends.findViewById(R.id.tv_friends_listview_name);
            TextView tv_phone = (TextView) list_friends.findViewById(R.id.tv_friends_listview_phone);
            ContactsBean bean = new ContactsBean();
            bean = datas.get(i);
            tv_name.setText(bean.getName());
            tv_phone.setText(bean.getPhone());

            return list_friends;
        }
    }

    private void initData() {
        new Thread() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void run() {
                Message msg = Message.obtain();
                msg.what = LOADING;
                handler.sendMessage(msg);

                SystemClock.sleep(1000);
                datas = ReadContactsEngine.readContacts(getApplicationContext());
                msg = Message.obtain();
                msg.what = FINISH;
                handler.sendMessage(msg);
            }
        }.start();
    }
}
