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

            }
        }

                if(deniedPermission.size()>0){
                    String[] arr = (String[]) deniedPermission.toArray(new String[deniedPermission.size()]);//

                    ActivityCompat.requestPermissions((Activity)context,
                            arr, PERMISSION_REQUEST_CODE);



                }

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

         switch (requestCode){
            case PERMISSION_REQUEST_CODE:{

                for(int i=0;grantResults.length>0 && i<grantResults.length;i++ ){

                    if(grantResults[i] != PackageManager.PERMISSION_GRANTED){
                        deniedPermission.add(permissions[i]);

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

        }
    }



    public void showNoStoragePermissionSnackbar() {
        Snackbar.make(this.findViewById(R.id.activity_view), "Не хватает разрешений" , Snackbar.LENGTH_INDEFINITE)
                .setAction("Настройки", new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        openApplicationSettings();

                        Toast.makeText(getApplicationContext(),
                                "Пожалуйста, дайте все необходимые разрешения :"+deniedPermission,
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
            return;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }



}
