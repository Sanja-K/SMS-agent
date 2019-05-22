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
    /**TODO Улучшить алгоритм записиси словаря */
    private static final String KEYPHRASE = "андроид";
    private static final String FORECAST_SEARCH = "озвуч";


    /** ArrayMap ContactName хранит в качестве ключей имена(фамилии,отчества) контактов, а в качестве*/
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
/*TODO переписать функцию для перезаписи файлов( данные для записи в файл должны передавтаься как парамметр)Сделано */
    public  void ReadLastLine(File file, String MessagePerson,String nameFile) throws IOException {

        File f = new File( file,nameFile);

        /** заменяю последнюю строку на строку имен отправителей сообщений*/

        PrintWriter prWr = new PrintWriter(new BufferedWriter(new FileWriter(f, false)));

        /**  перезаписываю файл с новой строкой имен отправителей сообщений (всё это должно производиться до инициализации слашателя PocketSpinx) */

        prWr.write(MessagePerson);

        prWr.close();
    }






    public ArrayMap<String, String> RewriteDictionary(ContentResolver cr) {


        Cursor cursor = cr.query(ContactsContract.Data.CONTENT_URI, null, null, null, null);

        int indexDisplayName = cursor.getColumnIndexOrThrow(ContactsContract.CommonDataKinds.StructuredName.DISPLAY_NAME);

        while (cursor.moveToNext()) {

            String display = cursor.getString(indexDisplayName).toLowerCase(); /** выводит имя и фамиилию в одном поле */

            CheckContact(display);

       }

        CheckDublicationName(FORECAST_SEARCH);
        CheckDublicationName(KEYPHRASE);
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
             break;
         }

         case 1:{
                /** Проверка на наличиие пробела в display имени контакта*/

             String contact[]=сontName.split("(\\s)+");

             for (String con:contact){
                 Log.d(LOG_TAG,"Имя контакта "+ con);
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
        }

    }


    public String RewrCont(String Contact){

       char charArrayNameContact[]=Contact.toCharArray();

        String resultStr="";

        for(int i=0;i< charArrayNameContact.length;i++){
            char sum=charArrayNameContact[i];
            String sumbol = Character.toString(sum);

            if(charDict.containsKey(sumbol)){


                resultStr=resultStr+ " " +charDict.get(sumbol);

            }
        }

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

        res= String.valueOf(buffer);

        return res;
    }


}
