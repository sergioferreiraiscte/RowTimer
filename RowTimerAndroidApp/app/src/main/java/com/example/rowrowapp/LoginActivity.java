package com.example.rowrowapp;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;

import com.facebook.FacebookSdk;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        // Initialize the SDK before executing any other operations,
        // especially, if you're using Facebook UI elements.

        setContentView(R.layout.activity_login);
        setTitle(R.string.title_activity_login);

        /**
         // O código abaixo gera e mostra no separador 'Android Monitor -> LOGCAT' o KeyHash para ser adicionado
         // na página facebook de configuração da aplicação (https://developers.facebook.com/apps/1340808619269535/settings/)

         try {
         PackageInfo info = getPackageManager().getPackageInfo(
         "com.example.rowrowapp",
         PackageManager.GET_SIGNATURES);
         for (Signature signature : info.signatures) {
         MessageDigest md = MessageDigest.getInstance("SHA");
         md.update(signature.toByteArray());
         Log.d("KeyHash:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
         }
         } catch (PackageManager.NameNotFoundException e) {

         } catch (NoSuchAlgorithmException e) {

         }
         */
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Logs 'install' and 'app activate' App Events.
        //AppEventsLogger.activateApp(this);
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Logs 'app deactivate' App Event.
        //AppEventsLogger.deactivateApp(this);
    }
}
