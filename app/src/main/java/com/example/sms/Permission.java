package com.example.sms;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import edu.cmu.pocketsphinx.Assets;

public class Permission extends AppCompatActivity implements ActivityCompat.OnRequestPermissionsResultCallback{
    private static final String TAG = "myLogs";
    private static final int PERMISSION_REQUEST_CODE =123 ;

    static ArrayList<String> deniedPermission= new ArrayList<>();





    public void StatusPermiss (Context context,String NamePermiss[]){


        for(String namePer :NamePermiss){

            int statusPermission = ContextCompat.checkSelfPermission(context, namePer);

            if(statusPermission!= PackageManager.PERMISSION_GRANTED){
                deniedPermission.add(namePer);
                Log.d(TAG, "StatusPermissStatusPermissStatusPermissStatusPermissStatusPermissStatusPermiss "+ namePer + " STATUS " +statusPermission);
                Log.d(TAG, "StatusPermissStatusPermissdeniedPermissiondeniedPermissiondenied "+deniedPermission);
            }
        }
        /* проверяю статус разрешения и записываею его в значение ArrayMap, ключом является название разрешения */

        //   ArrayPermission.indexOfKey(NamePermission) ;
        //    ArrayPermission.setValueAt(ArrayPermission.indexOfKey(NamePermission), statusPermission);

        // ArrayPermission.get(NamePermission)==PackageManager.PERMISSION_GRANTED;
        //записываю имена на которым не даны разрешения
     /*   if(statusPermission!= PackageManager.PERMISSION_GRANTED){
            deniedPermission.add(NamePermission);

            Log.d(TAG, "StatusPermissStatusPermissdeniedPermissiondeniedPermissiondenied "+deniedPermission);
            return false;
        }*/
       // boolean st=statusPermission==PackageManager.PERMISSION_GRANTED;
        //Log.d(TAG, "StatusPermissSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSSsS "+ st);
                if(deniedPermission.size()>0){
                    String[] arr = (String[]) deniedPermission.toArray(new String[deniedPermission.size()]);//
                    Log.d(TAG, "deniedPermissiondeniedPermissiondeniedPermissiondeniedPermissiondeniedPermission"+deniedPermission);
                    Log.d(TAG, "arrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarrarr "+arr);
                    ActivityCompat.requestPermissions((Activity)context,
                            arr, PERMISSION_REQUEST_CODE);


                    // deniedPermission.clear();// что бы не переполнялся одинаковыми значениями
                }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        Log.d(TAG, "onRequestPermissionsResultonRequestPermissionsResultonRequestPermissionsResultonRequestPermissionsResultonRequestPermissionsResult          11111111111111  " +permissions);
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



    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(this.findViewById(R.id.activity_view), "Не хватает разрешений" , Snackbar.LENGTH_INDEFINITE)
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
        Toast.makeText(this,"onActivityResultonActivityResultonActivityResultonActivityResultonActivityResult",Toast.LENGTH_SHORT).show();
        if (requestCode == PERMISSION_REQUEST_CODE) {
            //  makeFolder();
            Toast.makeText(this,"onActivityResult все норм короче ,работяги",Toast.LENGTH_SHORT).show();
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
