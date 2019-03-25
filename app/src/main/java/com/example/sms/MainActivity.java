package com.example.sms;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.pocketsphinx.Assets;

import static com.example.sms.EntryFile.ContactName;


//TODO  исправить: Кидает разрешения только для одного случая (Исправлено!)

public class MainActivity extends AppCompatActivity {


    private ToggleButton toggle;
    private CompoundButton.OnCheckedChangeListener toggleListener;

    private static final String TAG = "myLogs";
    private static final int PERMISSION_REQUEST_CODE =123 ;

    private BroadcastReceiver smsReceiver;
    public AssetManager mAssetManager;
    public static ArrayMap<String, String> PersonMessage;

    public ContentResolver cr;//если не будет работать убрать это
    public Message mess;//если не будет работать убрать это
    public EntryFile entryFile;


    String nameFileGram="menu.gram";
    String nameFileDict="contact.dict";

    String textFilegram= "#JSGF V1.0;"+"\n"+"grammar menu;"+"\n"+"public <item> = озвуч | ";

    //SmsMessage message = SmsMessage.createFromPdu(pdu);

  //  OnPermissionRequested mPermissionRequest; //для интерфейса проверки разрешений
  // public static  ArrayMap<String,String> Permission; // добавть вместо ключей название разрешений

    public static  ArrayMap<String , Integer> ArrayPermission= new ArrayMap<>();

   static ArrayList<String> deniedPermission= new ArrayList<>();
   // String[] mice = { "4", "8", "10", "12", "16" };


   /* public SoundPool soundPool;
    public static boolean soundOn= true;*/
   // EntryFile entryfile=new EntryFile();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ArrayPermission.put("android.permission.READ_EXTERNAL_STORAGE", 2);// разрешения первой очереди
        ArrayPermission.put("android.permission.WRITE_EXTERNAL_STORAGE",2);
        ArrayPermission.put("android.permission.READ_CONTACTS",2);

        ArrayPermission.put("android.permission.READ_SMS",2);// разрешения второй очереди
        ArrayPermission.put("android.permission.RECEIVE_SMS",2);
        ArrayPermission.put("android.permission.RECORD_AUDIO",2);



      final int indexResol;
       // cr=getContentResolver();//если не будет работать убрать это
      //  mess=new Message();//если не будет работать убрать это


       // MainActivity.PersonMessage = mess.isGiveMessage(cr);
       // final Button button = findViewById(R.id.buttonSMS);

        entryFile=new EntryFile();
        mess=new Message();//если не будет работать убрать это
        mAssetManager = getAssets();
        cr=getContentResolver();//если не будет работать убрать это

/*        *//* проверяю статус разрешения и записываею его в значение ArrayMap, ключом является название разрешения *//*
        for (int i=0;i<ArrayPermission.size();i++){
        int statusPermission =ContextCompat.checkSelfPermission(this, ArrayPermission.keyAt(i));
         ArrayPermission.setValueAt(i,statusPermission) ;
        }*/

   /*     StatusPermiss("android.permission.READ_EXTERNAL_STORAGE");
        StatusPermiss("android.permission.WRITE_EXTERNAL_STORAGE");
        StatusPermiss("android.permission.READ_CONTACTS");*/

        if( StatusPermiss("android.permission.READ_EXTERNAL_STORAGE") &
                StatusPermiss("android.permission.WRITE_EXTERNAL_STORAGE") &
                StatusPermiss("android.permission.READ_CONTACTS")){
           // button.setEnabled(true);
            Toast.makeText(getApplicationContext()," Всё разрешения получены! Можете продолжать работу",Toast.LENGTH_SHORT);



        }else{
          //  button.setEnabled(false);

            String[] arr = (String[]) deniedPermission.toArray(new String[deniedPermission.size()]);//
            Log.d(TAG, "deniedPermissiondeniedPermissiondeniedPermissiondeniedPermissiondeniedPermission"+deniedPermission);
            Log.d(TAG, "arrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarr "+arr);
            ActivityCompat.requestPermissions(this,
                     arr, PERMISSION_REQUEST_CODE); // не уверен что будет работать
            deniedPermission.clear();// что бы не переполнялся одинаковыми значениями

        }




/*if(ArrayPermission.get("android.permission.READ_EXTERNAL_STORAGE")==PackageManager.PERMISSION_GRANTED &&
        ArrayPermission.get("android.permission.WRITE_EXTERNAL_STORAGE")==PackageManager.PERMISSION_GRANTED &&
        ArrayPermission.get("android.permission.READ_CONTACTS")==PackageManager.PERMISSION_GRANTED){
    Toast.makeText(getApplicationContext()," Всё разрешения получены! Можете продолжать работу",Toast.LENGTH_SHORT);

}else{




}*/

   /*     requestPermission(Manifest.permission.READ_CONTACTS,Manifest.permission.RECEIVE_SMS,
                new OnPermissionRequested() {
                    @Override
                    public void onGranted() {
                        // what you want to do
                        Log.d(TAG,"активноактивноактивноактивноактивноактивноактивноактивноактивноактивноактивноактивноактивноактивно  ");
                    }
                });*/


/*
        if(ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_SMS") == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.RECEIVE_SMS") == PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(getBaseContext(), "android.permission.READ_CONTACTS") == PackageManager.PERMISSION_GRANTED)  {

            indexResol=1;
          //  button.setEnabled(true);
            button.setText("активно");
            Log.d(TAG,"активноактивноактивноактивноактивноактивноактивноактивноактивноактивноактивноактивноактивноактивно  ");
        }else {
            final int REQUEST_CODE_ASK_PERMISSIONS=123;
            indexResol=0;
            Log.d(TAG,"неактивнонеактивнонеактивнонеактивнонеактивнонеактивнонеактивнонеактивнонеактивнонеактивнонеактивно");
            ActivityCompat.requestPermissions(MainActivity.this, new String[]{"android.permission.READ_SMS", "android.permission.RECEIVE_SMS","android.permission.READ_CONTACTS"}, REQUEST_CODE_ASK_PERMISSIONS);
          //  button.setEnabled(false);
            button.setText("неактивно");
        }
*/

        toggle = (ToggleButton)findViewById(R.id.speechToggle);
        toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if(isChecked){
                    //     button.setEnabled(true);
                    MainActivity.PersonMessage = mess.isGiveMessage(cr);
                    Log.d(TAG, "111111111111111111111111111111111"+PersonMessage);
                    // entryFile.RewriteDictionary(cr);
                    PatchFile(cr,nameFileDict);
                    //entryFile.CombineContactString(cr);

                    //toggle.setEnabled(false);


                    //  entryFile.ReadLastLine(CombineContactString(cr),nameFileDict);
                }else{

                }
            }
        };


/*        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {


               //     button.setEnabled(true);
                    MainActivity.PersonMessage = mess.isGiveMessage(cr);
                    Log.d(TAG, "111111111111111111111111111111111"+PersonMessage);
                   // entryFile.RewriteDictionary(cr);
                    PatchFile(cr,nameFileDict);
                    //entryFile.CombineContactString(cr);
                    button.setEnabled(false);


              //  entryFile.ReadLastLine(CombineContactString(cr),nameFileDict);
            }
        });*/




    }

    public boolean StatusPermiss (String NamePermission){
        /* проверяю статус разрешения и записываею его в значение ArrayMap, ключом является название разрешения */
            int statusPermission =ContextCompat.checkSelfPermission(this, NamePermission);
         //   ArrayPermission.indexOfKey(NamePermission) ;
        //    ArrayPermission.setValueAt(ArrayPermission.indexOfKey(NamePermission), statusPermission);
        Log.d(TAG, "StatusPermissStatusPermissStatusPermissStatusPermissStatusPermissStatusPermiss "+ NamePermission + " STATUS " +statusPermission);
       // ArrayPermission.get(NamePermission)==PackageManager.PERMISSION_GRANTED;
        //записываю имена на которым не даны разрешения
        if(statusPermission!=PackageManager.PERMISSION_GRANTED){
            deniedPermission.add(NamePermission);

            Log.d(TAG, "StatusPermissStatusPermissdeniedPermissiondeniedPermissiondenied "+deniedPermission);
            return false;
        }
        boolean st=statusPermission==PackageManager.PERMISSION_GRANTED;
        Log.d(TAG, "StatusPermissSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSsS "+ st);

        return true;

    }



    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){

        switch (requestCode){
            case PERMISSION_REQUEST_CODE:{
                Log.d(TAG, "PermissionPermissionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn          11111111111111  " +permissions);
                for(int i=0;grantResults.length>0 && i<grantResults.length;i++ ){
                    Log.d(TAG, "PermissionPermissionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn          2222222222222222222");
                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        deniedPermission.add(permissions[i]);
                        Log.d(TAG, "PermissionPermissionnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnnn"+permissions[i]);
                    }
                }
               // break;
            }
            default:{
                Toast.makeText(this,"ОШИБКА ! requestCode и PERMISSION_REQUEST_CODE не совпадают.",
                                Toast.LENGTH_SHORT).show();
                break;
            }

        }

        if(deniedPermission.size()>0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)/*Возможно длинна эрэй листа работает некорректно */{

            showNoStoragePermissionSnackbar();

        }else {
          Toast.makeText(this,"onRequestPermissionsResult Всё норм работяги, проверка",Toast.LENGTH_SHORT).show();
//разрешения получены ,можно продолжать работу
        }
    }
/*
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        boolean allowed = true;
        switch (requestCode) {
            case PERMISSION_REQUEST_CODE :
                for (int res: grantResults){
                    allowed = allowed && (res == PackageManager.PERMISSION_GRANTED);
                }

                break;

            default:
                allowed=false;
                break;
        }

        if (allowed){
            //user granted all permissions we can perform our task.
           // makeFolder();
        }
        else {
            // we will give warning to user that they haven't granted permissions.
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                if (shouldShowRequestPermissionRationale(Manifest.permission.WRITE_EXTERNAL_STORAGE)){
                    Toast.makeText(this, "Разрешения не получены!.", Toast.LENGTH_SHORT).show();

                } else {
                  //  showNoStoragePermissionSnackbar();
                }
            }
        }


    }
    */



    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(MainActivity.this.findViewById(R.id.activity_view), "Без разрешений приложение не может работать" , Snackbar.LENGTH_INDEFINITE)
                .setAction("Настройки", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(),
                                "Пожалуйста, дайте все необходимые разрешения :"+deniedPermission, /* вставитть массив с имена разрешений, которым нужен доступ*/
                                Toast.LENGTH_SHORT);
                    }
                })
                .show();
    }

    public void openApplicationSettings() {
        Intent appSettingsIntent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                Uri.parse("package:" + getPackageName()));
        startActivityForResult(appSettingsIntent, PERMISSION_REQUEST_CODE);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PERMISSION_REQUEST_CODE) {
          //  makeFolder();
            Toast.makeText(this,"onActivityResult все норм короче ,работяги",Toast.LENGTH_SHORT).show();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }




    public void PatchFile(ContentResolver cr, String nameFile){

        EntryFile entryfile=new EntryFile();
      //  String MessagePerson=isCreateStringContacts(cr,message);

        try {
            Assets assets = new Assets(MainActivity.this);
            File assetDir = assets.syncAssets();
            entryfile.ReadLastLine(assetDir, entryfile.CombineContactString(cr),nameFile);

        } catch (IOException e) {
        System.out.println("Ошибка при записи в файл :"+ nameFile);
        }
    }



/*    public void requestPermissionWithRationale() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                String.valueOf(deniedPermission))) {// поставить массив из разрешений, убрать валъю оф есл ине будет работать
            final String message = "Разрешения необходимы для функционированя приложения";
            Snackbar.make(MainActivity.this.findViewById(R.id.activity_view), message, Snackbar.LENGTH_LONG)
                    .setAction("GRANT", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            requestPerms();
                        }
                    })
                    .show();
        } else {
            requestPerms();
        }
    }

    private void requestPerms(){// поставить массив из разрешений
      //  String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
            ActivityCompat.requestPermissions(this,new  String[]{String.valueOf(deniedPermission)},
                    PERMISSION_REQUEST_CODE); //подозрительная строка String.valueOf(deniedPermission)
                                                // -deniedPermission динамический массив с имена запрещённых разрешений
        }
    }

    */





   /* public interface OnPermissionRequested {
        void onGranted();
    }



    protected void requestPermission(String readContacts, String permission, OnPermissionRequested listener) {

        mPermissionRequest = listener;

        // Here, thisActivity is the current activity
        if (ContextCompat.checkSelfPermission(this, permission)
                != PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(getBaseContext(), readContacts) == PackageManager.PERMISSION_GRANTED)
        {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
                Toast.makeText(this, "У вас не достаточно разрешений для функциониования приложения.", Toast.LENGTH_SHORT).show();
            } else {
                // request the permission.
                ActivityCompat.requestPermissions(this,
                        new String[]{permission}, 2456);
            }
        } else {
            mPermissionRequest.onGranted();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (grantResults.length > 0
                && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            if (mPermissionRequest != null)
                mPermissionRequest.onGranted();
        }
    }
*/

/*    private void registerSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }*/



/*    public void getAllSms(Context context) {

        ContentResolver cr = context.getContentResolver();
        Cursor c = cr.query(Telephony.Sms.CONTENT_URI, null, null, null, null);
        int totalSMS = 0;
        if (c != null) {
            totalSMS = c.getCount();
            if (c.moveToFirst()) {
                for (int j = 0; j < totalSMS; j++) {
                    String smsDate = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.DATE));
                    String number = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.ADDRESS));
                    String body = c.getString(c.getColumnIndexOrThrow(Telephony.Sms.BODY));
                    Date dateFormat= new Date(Long.valueOf(smsDate));
                    String type;
                    switch (Integer.parseInt(c.getString(c.getColumnIndexOrThrow(Telephony.Sms.TYPE)))) {
                        case Telephony.Sms.MESSAGE_TYPE_INBOX:
                            type = "inbox";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_SENT:
                            type = "sent";
                            break;
                        case Telephony.Sms.MESSAGE_TYPE_OUTBOX:
                            type = "outbox";
                            break;
                        default:
                            break;
                    }


                    c.moveToNext();
                }
            }

            c.close();

        } else {
            Toast.makeText(this, "No message to show!", Toast.LENGTH_SHORT).show();
        }
    }*/

/*    // Create Inbox box URI
    Uri inboxURI = Uri.parse("content://sms/inbox");

    // List required columns
    String[] reqCols = new String[] { "_id", "address", "body" };

    // Get Content Resolver object, which will deal with Content Provider
    ContentResolver cr = getContentResolver();

    // Fetch Inbox SMS Message from Built-in Content Provider
    Cursor c = cr.query(inboxURI, reqCols, null, null, null);*/


/*
    public class SmsReceiver extends BroadcastReceiver {

        private static final String SMS_RECEIVED = "android.provider.Telephony.SMS_RECEIVED";

        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(SMS_RECEIVED)) {
                Bundle bundle = intent.getExtras();
                if (bundle != null) {
                    // get sms objects
                    Object[] pdus = (Object[]) bundle.get("pdus");
                    if (pdus.length == 0) {
                        return;
                    }
                    // large message might be broken into many
                    SmsMessage[] messages = new SmsMessage[pdus.length];
                    StringBuilder sb = new StringBuilder();
                    for (int i = 0; i < pdus.length; i++) {
                        messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i]);
                        sb.append(messages[i].getMessageBody());
                    }
                    String sender = messages[0].getOriginatingAddress();
                    String message = sb.toString();
                    Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
                    // prevent any other broadcast receivers from receiving broadcast
                    // abortBroadcast();
                }
            }
        }
    }*/





}
