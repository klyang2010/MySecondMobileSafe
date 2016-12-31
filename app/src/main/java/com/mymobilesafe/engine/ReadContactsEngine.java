package com.mymobilesafe.engine;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.RequiresApi;

import com.mymobilesafe.domain.ContactsBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by mrka on 16-12-30.
 */

public class ReadContactsEngine extends Object {
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static List<ContactsBean> readContacts(Context context) {
        List<ContactsBean> datas = new ArrayList<ContactsBean>();
        Uri uriContacts = Uri.parse("content://com.android.contacts/contacts");
        Uri uriData = Uri.parse("content://com.android.contacts/data");
        Cursor cursor = context.getContentResolver().query(uriContacts, new String[]{"_id"}, null, null, null);
        while (cursor.moveToNext()){
            ContactsBean bean = new ContactsBean();
            String id = cursor.getString(0);
            Cursor cursor1 = context.getContentResolver().query(uriData, new String[]{"data1", "mimetype"}, "raw_contact_id = ?", new String[]{id}, null);
            while (cursor1.moveToNext()){
                String data = cursor1.getString(0);
                String mimeType = cursor1.getString(1);
                if (mimeType.equals("vnd.android.cursor.item/name")){
                    bean.setName(data);
                }else if (mimeType.equals("vnd.android.cursor.item/phone_v2")){
                    bean.setPhone(data);
                }
            }
            cursor1.close();
            datas.add(bean);
        }
        return  datas;
    }

}
