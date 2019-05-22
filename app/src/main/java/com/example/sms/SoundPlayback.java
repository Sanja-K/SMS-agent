package com.example.sms;

import android.annotation.TargetApi;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioAttributes;
import android.media.SoundPool;
import android.os.Build;
import android.util.Log;

import java.io.IOException;

public class SoundPlayback extends MainActivity{

    private static final String TAG = "myLogs";

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void createNewSoundPool() {
        AudioAttributes attributes = new AudioAttributes.Builder()
                .setUsage(AudioAttributes.USAGE_GAME)
                .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                .build();
        soundPool = new SoundPool.Builder()
                .setAudioAttributes(attributes)
                .build();
    }


    public int playSound(int sound) {
        int mStreamID = -1;
        if (sound > 0 && soundOn) {
            mStreamID = soundPool.play(sound, 1, 1, 1, 0, 1);
        }
        return mStreamID;
    }

    public int loadSound(String fileName, AssetManager mAssetManager) {

        AssetFileDescriptor afd;

        try {
            afd = mAssetManager.openFd(fileName);
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG,"Не могу загрузить файл "+fileName);
            return -1;
        }
        return soundPool.load(afd, 1);
    }

}
