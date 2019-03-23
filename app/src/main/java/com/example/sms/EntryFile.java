package com.example.sms;


import android.content.ContentResolver;
import android.database.Cursor;
import android.provider.ContactsContract;
import android.util.ArrayMap;
import android.util.Log;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;


public  class EntryFile extends MainActivity {

  final String LOG_TAG = "myLogs";

    //String result = null;
    /**TODO Этот строка для записиси в файл menu.gram, мы передаём её в классе MainACTIVITY */


    String nameFileGram="menu.gram";
    String nameFileDict="contact.dict";
    /** ArrayMap ContactName хранит в качестве ключей имена имена (фамилии,отчества) контактов, а в качестве*/
  public static final ArrayMap <String,String> ContactName=new ArrayMap<>();


    private static final Map<String, String> charDict = new LinkedHashMap<String, String>() {{

        put("а", "a");
        put("б", "b");
        put("в", "v");
        put("г", "g");
        put("д", "d");
        put("е", "e");
        put("ё", "jo1");
        put("ж", "zh");
        put("з", "z");
        put("и", "i");
        put("й", "j");
        put("к", "k");
        put("л", "l");
        put("м", "m");
        put("н", "n");
        put("о", "o");
        put("п", "p");
        put("р", "r");
        put("с", "s");
        put("т", "t");
        put("у", "u");
        put("ф", "f");
        put("х", "h");
        put("ц", "c");
        put("ч", "ch");
        put("ш", "sh");
        put("щ", "sch");
        put("ы", "y0");
        put("э", "e0");
        put("ю", "u0");
        put("я", "a0");
    }};



/** Перезаписывает файл граматики*/
/*TODO переписать функцию для перезаписи файлов( данные для записи в файл должны передавтаься как парамметр) */
    public  void ReadLastLine(File file, String MessagePerson,String nameFile) throws IOException {

        File f = new File( file,nameFile);

        /** заменяю последнюю строку на строку имен отправителей сообщений*/

        PrintWriter prWr = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));
       /* String result = null;
        result= "#JSGF V1.0;"+"\n"+"grammar menu;"+"\n"+"public <item> = озвуч | "+MessagePerson ;*/

        Log.d(LOG_TAG,"qqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqqq"+MessagePerson);

        /**  перезаписываю файл с новой строкой имен отправителей сообщений (всё это должно производиться до инициализации слашателя PocketSpinx) */

        /*prWr.write(result);*/

        prWr.write(MessagePerson);

        prWr.close();
    }






    public ArrayMap<String, String> RewriteDictionary(ContentResolver cr) {


        Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null, null, null, null);

        int indexGivenName = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredName.GIVEN_NAME);
        int indexFamilyName = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredName.FAMILY_NAME);
        int indexDisplayName = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);

        while (cursor.moveToNext()) {

            String name = cursor.getString(indexGivenName); /** выводит поле с именем контакта (toLowerCase не работает на это поле) */
            String family = cursor.getString(indexFamilyName);/** выводит поле с фамилия контакта (toLowerCase не работает на это поле) */
            String display = cursor.getString(indexDisplayName).toLowerCase(); /** выводит имя и фамиилию в одном поле */

            Log.d(LOG_TAG,"ContactNameContactNameContactNameContactNameContactNameContactName имя "+name +" фамилия "  +family +" дисплей " + display);


            CheckContact(display);


          /*  if(display.matches("^[а-яё\" \"]+$")){

                Log.d(LOG_TAG,"33333333333333333333333333333 " +display);
                ContactName.put(display,RewrCont(display));
            }*/
       }

        Log.d(LOG_TAG,"NameNameNameNameNameNameNameNameName"+ContactName);

/** вывод Имён контактов и их фонетическую транскрипцию*/
       /* for (Map.Entry entry : ContactName.entrySet()) {
            System.out.println("Ключ: " + entry.getKey() + " Значение: "
                    + entry.getValue());
        }*/
       return ContactName;
    }


    public void CheckContact(String сontName){

        сontName=сontName.trim();

        int index=2;

        if (сontName.matches("^[а-яё]+$")){
            index=0;
        }else {
            if (сontName.matches("^[а-яё\" \"]+$")) {
                index = 1;
            }
        }


     switch (index){

         case 0:{
             CheckDublicationName(сontName);

            /* if(ContactName.containsKey(сontName)){
                 ContactName.put(сontName,RewrCont(сontName));
             }*/


             break;
         }

         case 1:{
                /** Проверка на наличиие пробела в display имени контакта*/

             Log.d(LOG_TAG,"Имя контакта сontNameсontNameсontNameсontName "+ сontName);

             String contact[]=сontName.split("(\\s)+");

             Log.d(LOG_TAG,"Имя контакта contactcontactcontactcontact "+ contact);

             for (String con:contact){
                 Log.d(LOG_TAG,"Имя контакта содержит некорректные символы "+ con);
                 CheckDublicationName(con);

             }
             break;
         }
         default:{

             Log.d(LOG_TAG,"Имя контакта содержит некорректные символы");
         }

     }

    }

    /** проверяю контакты на дублирование в arrayMap **/
    public void CheckDublicationName(String ContName){
        if(!ContactName.containsKey(ContName)){
            ContactName.put(ContName,RewrCont(ContName));
            Log.d(LOG_TAG,"CheckDublicationNameCheckDublicationNameCheckDublicationName "+ContactName );
        }

    }


    public String RewrCont(String Contact){

       char charArrayNameContact[]=Contact.toCharArray();

        String resultStr="";

        for(int i=0;i< charArrayNameContact.length;i++){
            char sum=charArrayNameContact[i];
            String sumbol = Character.toString(sum);
            Log.d(LOG_TAG," RewrContRewrContRewrContRewrContRewrContRewrContRewrCont " +
                    "charArrayNameContact[i]charArrayNameContact[i] "+ charArrayNameContact[i]+ "  " +charDict.containsKey(sumbol) );

            if(charDict.containsKey(sumbol)){


                resultStr=resultStr+ " " +charDict.get(sumbol);
                Log.d(LOG_TAG,"resultStr resultStr resultStr resultStr resultStr resultStr "+resultStr);
            }

        }

       // String dictCont= Arrays.toString(charCon);
       // Log.d(LOG_TAG,"dictdictdictdictdictdictdictdictdictdictdictdictdictdict "+dictCont);

        return resultStr;
    }


    public String CombineContactString(ContentResolver cr){

        ArrayMap <String,String> ContacName=RewriteDictionary(cr);
        String res="";

        StringBuffer buffer=new StringBuffer();
        for (Map.Entry entry : ContacName.entrySet()) {
            res= String.valueOf(entry.getKey())+entry.getValue()+"\n";

            buffer.append(res);

            System.out.println("Ключ: " + entry.getKey() + " Значение: "
                    + entry.getValue());
        }


        System.out.println("РезультатРезультатРезультатКонечныйКонечныйКонечныйКонечныйКонечныйКонечныйКонечный: " + buffer);

        res= String.valueOf(buffer);

        return res;
    }


}
