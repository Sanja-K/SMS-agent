package com.example.sms;


import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.provider.ContactsContract;
import android.support.v4.util.ArrayMap;
import android.util.Log;


/**
 * Created by 1 on 25.05.2017.А
 */



public class Message extends MainActivity {

    final ArrayMap<String, String> MessagePerson = new ArrayMap<>();

    private static final String TAG = "myLogs";

    /** Сравнивает телефон отправителя с контактами */
    public String getContactName(String phone, ContentResolver cr ){
        String ContactName=null;
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(phone));
        String projection[] = new String[]{ContactsContract.Data.DISPLAY_NAME};
        Cursor cursor = cr.query(uri, projection, null, null, null);
        if(cursor.moveToFirst()){

            ContactName= cursor.getString(0).toLowerCase();
            return  ContactName;
        }else {
            return phone;
        }
    }

    public String MessageOutput(String ContactName, ContentResolver cr){
        ArrayMap<String, String> PersonEntry =isGiveMessage(cr);
        ArrayMap<String, String> Person=new ArrayMap<>();

        for (int i=0;i<PersonEntry.size();i++) {
            String key = PersonEntry.keyAt(i).replaceAll(" ","");
            String value = PersonEntry.valueAt(i);
            Person.put(key,value);
        }

        ContactName =ContactName.replaceAll(" ","");

        if(Person.containsKey(ContactName)){

            return Person.get(ContactName);
        }
        else
        {
            return null;
        }
    }

/** Вытаскивает сообщения из бд и записывает в ArrayMap "MessagePerson' номер отправителя и тело сообщения*/
    public ArrayMap<String, String> isGiveMessage(ContentResolver cr){

            Uri inboxURI = Uri.parse("content://sms/inbox");
            String[] reqCols = new String[] { "_id", "address", "body" };
            Cursor c = cr.query(inboxURI, reqCols, null, null, null);

            if(c.moveToFirst()){
                do {
                    String contactName=getContactName(c.getString(1),cr);
                    String Contact= c.getString(1).toLowerCase();

                    if(contactName!=null){

                        Contact =contactName.toLowerCase();
                    }

                    MessagePerson.put(Contact,c.getString(2));

                }while (c.moveToNext());
            }

return MessagePerson;
    }

}
