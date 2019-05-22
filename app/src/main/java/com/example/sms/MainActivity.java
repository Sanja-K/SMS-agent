package com.example.sms;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.content.res.AssetManager;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.speech.tts.TextToSpeech;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.util.ArrayMap;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import edu.cmu.pocketsphinx.Assets;
import edu.cmu.pocketsphinx.Hypothesis;
import edu.cmu.pocketsphinx.RecognitionListener;
import edu.cmu.pocketsphinx.SpeechRecognizer;
import edu.cmu.pocketsphinx.SpeechRecognizerSetup;

import static com.example.sms.EntryFile.ContactName;


//TODO  исправить: Кидает разрешения только для одного случая (Исправлено!)
//TODO  Сравнить со старыми методами Сфинкса

public class MainActivity extends Permission implements RecognitionListener {

    /**Нам нужна только ключевая фраза, чтобы начать распознавание, одно меню со списком вариантов,
       и одно слово, которое требуется для метода switchSearch - это позволит распознавателю
       вернуться к прослушиванию ключевой фразы*/

    private HashMap<String, Integer> captions;
    //TODO не забыть записать управляющий фразы в словарь

    private static final String KEYPHRASE = "андроид";/** активационная фраза*/


    private static final String KWS_SEARCH = "wakeup";
    private static final String FORECAST_SEARCH = "озвуч";
    private static String DIGITS_SEARCH = "сообщение";

    /** добавить ещё одну константу приравнивающую  себе MessageKey из isCreateStringContacts*/

    private static final String PHONE_SEARCH = "";
    private static final String MENU_SEARCH = "меню";

    //public static String MESSAGE_SEARCH="светка";
    public static String MESSAGE_SEARCH="";/** Имя контакта, для чтения последнего
                                                присланного им сооьщения*/

    /** Для класса Speaker  */
    private final int CHECK_CODE = 0x1;
    private final int LONG_DURATION = 5000;
    private final int SHORT_DURATION = 1200;

    private Speaker speaker;
    private static final int PERMISSIONS_REQUEST_RECORD_AUDIO = 1;/** для разрешений( надо заменить метод с этой константой*/

    public SoundPool soundPool;
    public SoundPlayback soundPlayback;

    public int soundIdreadiness;
    public int soundIdactivation;
    public static boolean soundOn= true;
    private SpeechRecognizer recognizer;


    private ToggleButton toggle;
    private CompoundButton.OnCheckedChangeListener toggleListener;

    private static final String TAG = "myLogs";
    private static final int PERMISSION_REQUEST_CODE =123 ;

    private BroadcastReceiver smsReceiver;
    public AssetManager mAssetManager;
    public static ArrayMap<String, String> PersonMessage;

    public ContentResolver cr;
    public Message mess;
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


    public static  ArrayMap<String , Integer> ArrayPermission= new ArrayMap<>();

   static ArrayList<String> deniedPermission= new ArrayList<>();


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


        captions = new HashMap<String, Integer>();
        captions.put(KWS_SEARCH, R.string.kws_caption);
        captions.put(MENU_SEARCH, R.string.menu_caption);
        captions.put(DIGITS_SEARCH, R.string.digits_caption);
        captions.put(FORECAST_SEARCH, R.string.forecast_caption);

        entryFile=new EntryFile();
        mess=new Message();
        mAssetManager = getAssets();
        cr=getContentResolver();
        soundPlayback=new SoundPlayback();

       MainActivity.super.StatusPermiss(this,MassPermisiion);

        soundPlayback.createNewSoundPool();
        soundIdactivation=soundPlayback.loadSound("activation.mp3",mAssetManager);
        soundIdreadiness= soundPlayback.loadSound("readiness.wav",mAssetManager);



        CheckBox sounCheckBox = (CheckBox) findViewById(R.id.sounCheckBox);

        sounCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    MainActivity.soundOn=true;
                    soundPlayback.playSound(soundIdactivation);
                }
                else {
                    MainActivity.soundOn=false;

                }
            }
        });

        toggle = (ToggleButton)findViewById(R.id.speechToggle);
        toggleListener = new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton view, boolean isChecked) {
                if(isChecked){
                    //     button.setEnabled(true);
                    ((TextView) findViewById(R.id.caption_text)).setText(R.string.preparation_bot);

                    PersonMessage = mess.isGiveMessage(cr);

                    String nameMessage=isCreateStringContacts(cr,mess);
                    MESSAGE_SEARCH=nameMessage.substring(0,nameMessage.length()-2);

                    Log.d(TAG, "111111111111111111111111111111111"+PersonMessage);

                    PatchFile(entryFile.CombineContactString(cr),nameFileDict);
                    PatchFile(textFilegram+isCreateStringContacts(cr,mess),nameFileGram);

                    checkTTS();
                    runRecognizerSetup();
                    initializeSMSReceiver();
                    registerSMSReceiver();


                }else{
                    ((TextView) findViewById(R.id.caption_text)).setText(R.string.stop_bot);
                    unregisterReceiver(smsReceiver);
                    recognizer.cancel();
                    recognizer.shutdown();
                    /** Останавливай ТТС И бота*/
                }
            }
        };

        toggle.setOnCheckedChangeListener(toggleListener);
    }


    public void PatchFile(String textForEntry, String nameFile){

        EntryFile entryfile=new EntryFile();

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
                MessageKey+= " | "+key;

            }
        }

        MessageKey=MessageKey.substring(3);
        MessageKey+= " ;";
        return MessageKey;
    }




    private void runRecognizerSetup() {

        new AsyncTask<Void, Void, Exception>() {
            @Override
            protected Exception doInBackground(Void... params) {
                try {
                    Assets assets = new Assets(MainActivity.this);
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
                    System.out.println(result.getMessage());
                } else {
                    switchSearch(KWS_SEARCH);
                }
            }
        }.execute();
    }


    private void setupRecognizer(File assetsDir) throws IOException {
        recognizer = SpeechRecognizerSetup.defaultSetup()
                .setAcousticModel(new File(assetsDir, "ru-rus-ptm"))
                .setDictionary(new File(assetsDir, "ru-ru.dict"))

                /** .setRawLogDir(assetsDir) отключаем эту строку, если хотим чтобы прога сохраняла
                                            raw файлы в хранилизе приложения*/
                .setRawLogDir(assetsDir).setKeywordThreshold(1e-20f)/** Чувствительность слушателя*/
                .setFloat("-beam", 1e-30f)
                .getRecognizer();



        recognizer.addListener(this);


        // Create keyword-activation search.
        recognizer.addKeyphraseSearch(KWS_SEARCH, KEYPHRASE);

        // Create your custom grammar-based search
        File menuGrammar = new File(assetsDir, "menu.gram");
        recognizer.addGrammarSearch(MENU_SEARCH, menuGrammar);

        // Create language model search
        File languageModel = new File(assetsDir, "weather.dmp");
        recognizer.addNgramSearch(FORECAST_SEARCH, languageModel);
    }



    @Override
    public void onStop(){
        super.onStop();

        if(recognizer!=null){
            recognizer.cancel();
            recognizer.shutdown();
        }
    }

    @Override
    public void onPartialResult(Hypothesis hypothesis){

        String bodyMessage;

        if(hypothesis==null){ return;}

        String text=hypothesis.getHypstr();

            switch (text){
                case KEYPHRASE:{
                    switchSearch(MENU_SEARCH);
                    soundPlayback.playSound(soundIdactivation);

                    break;
                }

                case FORECAST_SEARCH:{
                    speaker.speak(smsReceiver.getResultData());
                    switchSearch(FORECAST_SEARCH);
                    break;
                }

                default:{
                    if(PersonMessage.containsKey(text)){
                        /** проверить на корректность */
                        bodyMessage=mess.MessageOutput(text,cr);
                        SpeachMessage(bodyMessage);
                        switchSearch(FORECAST_SEARCH);
                    }else {
                        System.out.println("Гипотеза "+ hypothesis.getHypstr());
                    }
                    break;
                }

            }


    }

    public void SpeachMessage(String bodyMessage){
        System.out.println("SpeachMessage "+bodyMessage);
        if(bodyMessage==null){
            speaker.speak(getString(R.string.message_not_found));
        }else{
            speaker.speak(bodyMessage);
        }
    }

    @Override
    public void onResult(Hypothesis hypothesis){
        if(hypothesis!=null){
            System.out.println(" Распознал "+hypothesis.getHypstr());
        }
    }

    @Override
    public void onBeginningOfSpeech ( ) {
    }

    @Override
    public void onEndOfSpeech() {
        if (!recognizer.getSearchName().equals(KWS_SEARCH))
            switchSearch(KWS_SEARCH);
    }

    private void switchSearch(String searchName) {
        recognizer.stop();

        if (searchName.equals(KWS_SEARCH))
            recognizer.startListening(searchName);
        else
            recognizer.startListening(searchName, 10000);

        String caption = getResources().getString(captions.get(searchName));
        ((TextView) findViewById(R.id.caption_text)).setText(caption);
    }


    @Override
    public void onError(Exception error) {
        ((TextView) findViewById(R.id.caption_text)).setText(error.getMessage());
        System.out.println(error.getMessage());
    }

    @Override
    public void onTimeout() {
        switchSearch(KWS_SEARCH);
    }


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

    private void registerSMSReceiver() {
        IntentFilter intentFilter = new IntentFilter("android.provider.Telephony.SMS_RECEIVED");
        registerReceiver(smsReceiver, intentFilter);
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
    public void onPointerCaptureChanged(boolean hasCapture) {

    }


}
