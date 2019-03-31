package com.example.sms;

import android.app.Activity;
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

public class MainActivity extends Permission {


   // public Context mContext;

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

    public Permission perm;

    String nameFileGram="menu.gram";
    String nameFileDict="contact.dict";

    String textFilegram= "#JSGF V1.0;"+"\n"+"grammar menu;"+"\n"+"public <item> = озвуч | ";


    String MassPermisiion[]={"android.permission.READ_EXTERNAL_STORAGE",
                             "android.permission.WRITE_EXTERNAL_STORAGE",
                             "android.permission.READ_CONTACTS",
                             "android.permission.READ_SMS",
                             "android.permission.RECEIVE_SMS",
                             "android.permission.RECORD_AUDIO" };
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
      //  perm =new Permission();

/*        *//* проверяю статус разрешения и записываею его в значение ArrayMap, ключом является название разрешения *//*
        for (int i=0;i<ArrayPermission.size();i++){
        int statusPermission =ContextCompat.checkSelfPermission(this, ArrayPermission.keyAt(i));
         ArrayPermission.setValueAt(i,statusPermission) ;
        }*/

   /*     StatusPermiss("android.permission.READ_EXTERNAL_STORAGE");
        StatusPermiss("android.permission.WRITE_EXTERNAL_STORAGE");
        StatusPermiss("android.permission.READ_CONTACTS");*/
/*
        if( perm.StatusPermiss(MainActivity.this,"android.permission.READ_EXTERNAL_STORAGE") &
                perm.StatusPermiss(MainActivity.this,"android.permission.WRITE_EXTERNAL_STORAGE") &
                perm.StatusPermiss(MainActivity.this,"android.permission.READ_CONTACTS")){
           // button.setEnabled(true);
            Toast.makeText(getApplicationContext()," Всё разрешения получены! Можете продолжать работу",Toast.LENGTH_SHORT);



        }else{
          //  button.setEnabled(false);

            String[] arr = (String[]) deniedPermission.toArray(new String[deniedPermission.size()]);//
            Log.d(TAG, "deniedPermissiondeniedPermissiondeniedPermissiondeniedPermissiondeniedPermission"+deniedPermission);
            Log.d(TAG, "arrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarr "+arr);
            ActivityCompat.requestPermissions(MainActivity.Context(),
                     arr, PERMISSION_REQUEST_CODE); // не уверен что будет работать
            deniedPermission.clear();// что бы не переполнялся одинаковыми значениями

        }*/


       MainActivity.super.StatusPermiss(this,MassPermisiion);





        toggle = (ToggleButton)findViewById(R.id.speechToggle);
        toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if(isChecked){
                    //     button.setEnabled(true);
                    MainActivity.PersonMessage = mess.isGiveMessage(cr);
                    Log.d(TAG, "111111111111111111111111111111111"+PersonMessage);
                    // entryFile.RewriteDictionary(cr);
                    PatchFile(cr,entryFile.CombineContactString(cr),nameFileDict);

                    PatchFile(cr,textFilegram+isCreateStringContacts(cr,mess),nameFileGram);

                    //entryFile.CombineContactString(cr);

                    //toggle.setEnabled(false);


                    //  entryFile.ReadLastLine(CombineContactString(cr),nameFileDict);
                }else{

                    /** Останавливай ТТС И бота*/
                }
            }
        };

    }




/*


    public boolean StatusPermiss (String NamePermission){
        */
/* проверяю статус разрешения и записываею его в значение ArrayMap, ключом является название разрешения *//*

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

        if(deniedPermission.size()>0 && Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)*/
/*Возможно длинна эрэй листа работает некорректно *//*
{

            showNoStoragePermissionSnackbar();

        }else {
          Toast.makeText(this,"onRequestPermissionsResult Всё норм работяги, проверка",Toast.LENGTH_SHORT).show();
//разрешения получены ,можно продолжать работу
        }
    }



    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(MainActivity.this.findViewById(R.id.activity_view), "Не хватает разрешений" , Snackbar.LENGTH_INDEFINITE)
                .setAction("Настройки", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(),
                                "Пожалуйста, дайте все необходимые разрешения :"+deniedPermission, */
/* вставитть массив с имена разрешений, которым нужен доступ*//*

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

*/



    public void PatchFile(ContentResolver cr,String textForEntry, String nameFile){

        EntryFile entryfile=new EntryFile();
      //  String MessagePerson=isCreateStringContacts(cr,message);

        try {
            Assets assets = new Assets(MainActivity.this);
            File assetDir = assets.syncAssets();
            entryfile.ReadLastLine(assetDir,textForEntry,nameFile);

        } catch (IOException e) {
        System.out.println("Ошибка при записи в файл :"+ nameFile);
        }
    }



    public  String isCreateStringContacts(ContentResolver cr,Message message){

        MainActivity.PersonMessage = message.isGiveMessage(cr);
        String MessageKey="";

        for (String key : PersonMessage.keySet()) {
            if(key.matches("^[а-яё\" \"]+$")){
                MessageKey=MessageKey + " | "+ key ;

            }
            Log.d(TAG,"000000000000000000000000000000000000000000000000000000000000000000000000000000000000  "+PersonMessage);
        }
        //   Log.d(TAG,"000000000000000000000000000000000000000000000000000000000000000000000000000000000000  "+MessageKey);

        MessageKey=MessageKey.substring(3);
        Log.d(TAG,"111111111111111111111111111111111111111111111111111111111111111111111111111  "+MessageKey);
        MessageKey= MessageKey +"; ";

        return MessageKey;
    }






/*


    private void initializeSMSReceiver(){

        smsReceiver = new BroadcastReceiver(){
            @Override
            public void onReceive(Context context, Intent intent) {

                Bundle bundle = intent.getExtras();

                String text = null;
                String sender;
                if(bundle!=null){

                    Object[] pdus = (Object[])bundle.get("pdus");
                    for(int i=0;i<pdus.length;i++){
                        byte[] pdu = (byte[])pdus[i];
                        SmsMessage message = SmsMessage.createFromPdu(pdu);

                        text = message.getDisplayMessageBody();
                        sender = mess.getContactName(message.getOriginatingAddress(),cr);

                        speaker.speak("Пришло Сообщение от абонента"+sender+ "!");
                    }
                    setResultData(text);
                }
            }
        };
    }

    private void checkTTS(){
        Intent check = new Intent();
        check.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(check, CHECK_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(requestCode == CHECK_CODE){
            if(resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS){
                speaker = new Speaker(this);
            }else {
                Intent install = new Intent();
                install.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(install);
            }
        }
    }


    private void registerSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
    }

    public void PatchFile(ContentResolver cr,Message message){

        EntryFile entryfile=new EntryFile();
        String MessagePerson=isCreateStringContacts(cr,message);

        try {
            Assets assets = new Assets(PocketSphinxActivity.this);
            File assetDir = assets.syncAssets();
            entryfile.ReadLastLine(assetDir,MessagePerson);

        } catch (IOException e) {

        }
    }


    public void runRecognizerSetup() {
        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(PocketSphinxActivity.this);
                    File assetDir = assets.syncAssets();
                    setupRecognizer(assetDir);
                    recognizer.startListening(KWS_SEARCH);

                    soundPlayback.playSound(soundIdreadiness);

                } catch (IOException e) {
                    return e;
                }
                return null;
            }

            @Override
            protected void onPostExecute(Exception result) {
                if (result != null) {
                    ((TextView) findViewById(R.id.caption_text))
                            .setText(R.string.speach_not_found +" "+ result);
                } else {
                    switchSearch(KWS_SEARCH);

                }
            }
        }.execute();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSIONS_REQUEST_RECORD_AUDIO) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                runRecognizerSetup();
            } else {
                finish();
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        if (recognizer != null) {
            recognizer.cancel();
            recognizer.shutdown();
        }

        if(smsReceiver!=null){
            unregisterReceiver(smsReceiver);
        }
        if(speaker!=null){
            speaker.destroy();
        }
        soundPool.release();
        soundPool = null;
    }
*/

    /**
     * In partial result we get quick updates about current hypothesis. In
     * keyword spotting mode we can react here, in other modes we need to wait
     * for final result in onResult.
     */
 /*   @Override
    public void onPartialResult(Hypothesis hypothesis) {
        Message mess= new Message();
        ContentResolver cr =getContentResolver();
        String bodyMessage;

        if (hypothesis == null)
            return;

        String text = hypothesis.getHypstr();

        if (text.equals(KEYPHRASE)){
            switchSearch(MENU_SEARCH);
            soundPlayback.playSound(soundIdactivation);
            //  soundPlayback.playSound(soundIdreadiness);
        }

        else if (text.equals(FORECAST_SEARCH)){
            speaker.speak(smsReceiver.getResultData());
            switchSearch(FORECAST_SEARCH);
        }

        else if (PersonMessage.containsKey(text)){
            *//** проверить на корректность *//*
            bodyMessage=mess.MessageOutput(text,cr);
            SpeachMessage(bodyMessage);
            switchSearch(FORECAST_SEARCH);
        }
        else{
            //((TextView) findViewById(R.id.result_text)).setText(text);
        }
    }

    public void SpeachMessage(String bodyMessage){
        if(bodyMessage==null){
            speaker.speak(getString(R.string.message_not_found));
        }else{
            speaker.speak(bodyMessage);
        }
    }
*/
    /**
     * This callback is called when we stop the recognizer.
     */
/*    @Override
    public void onResult(Hypothesis hypothesis) {*/
        /** getContentResolver() в будущем надо убрать
         он нужен для Context Который не передаётся из класса Message*/

        //((TextView) findViewById(R.id.result_text)).setText("");
     //   if (hypothesis != null) {
         //   String text = hypothesis.getHypstr();

            // makeText(this, text, Toast.LENGTH_SHORT).show(); /** выводит результат распознавание в тостах */
     //   }
  //  }

  /*  @Override
    public void onBeginningOfSpeech() {
    }*/

    /**
     * We stop recognizer here to get a final result
     */
/*
    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        // If we are not spotting, start listening with timeout (10000 ms or 10 seconds).
        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);

        String caption = getResources().getString(captions.get(searchName));
        ((TextView) findViewById(R.id.caption_text)).setText(caption);
    }

    private void setupRecognizer(File assetsDir) throws IOException {

        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "ru-rus-ptm"))
                .setDictionary(new File(assetsDir, "ru-ru.dict"))
                .setRawLogDir(assetsDir) // To disable logging of raw audio comment out this call (takes a lot of space on the device)

                .setRawLogDir(assetsDir).setKeywordThreshold(1e-20f)
                .setFloat("-beam", 1e-30f)

                .getRecognizer();
        recognizer.addListener(this);
*/

        /** In your application you might not need to add all those searches.
         * They are added here for demonstration. You can leave just one.
         */
/*

        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create grammar-based search for selection between demos
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

        // Create language model search
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);

    }

    @Override
    public void onError(Exception error) {
        ((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }
*/




}
