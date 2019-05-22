package com.example.sms;

import android.content.Context;
import android.media.AudioManager;
import android.speech.tts.TextToSpeech;

import java.util.HashMap;
import java.util.Locale;

public class Speaker implements TextToSpeech.OnInitListener{

    private TextToSpeech tts;

    private boolean ready = false;

    private boolean allowed = false;

    public Speaker(Context context){
        tts = new TextToSpeech(context, this);
    }

    /** если не будет работать раскоментировать*/
    public boolean isAllowed(){
        return allowed;
    }

    public void allow(boolean allowed){
        this.allowed = allowed;
    }

    @Override
    public void onInit(int status) {
        System.out.println(" Status "+status);
        if(status == TextToSpeech.SUCCESS){
            if(tts.isLanguageAvailable(new Locale(Locale.getDefault().getLanguage()))==
                    TextToSpeech.LANG_AVAILABLE){

                System.out.println(" Locale "+Locale.getDefault()+"   "+TextToSpeech.LANG_AVAILABLE);
                ready = true;
                tts.setLanguage(new Locale(Locale.getDefault().getLanguage()));
            } else{

                Locale locale=new Locale("ru");
                tts.setLanguage(locale);
                ready = true;
            }

        }else{
            ready = false;
        }
    }

    public void speak(String text){

        System.out.println("Проверка доступности русского языка для ТТС " +ready);
        if(ready ) {

            String utteranceId = this.hashCode() + "";
            if(text!=null){

                tts.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId);
            }else{
                text="У вас отсутсвуют присланные сообщения";
                tts.speak(text, TextToSpeech.QUEUE_ADD, null, utteranceId);
            }


        }
    }

    public void pause(int duration){
        tts.playSilence(duration, TextToSpeech.QUEUE_ADD, null);
    }

    public void destroy(){
        tts.shutdown();
    }

}
