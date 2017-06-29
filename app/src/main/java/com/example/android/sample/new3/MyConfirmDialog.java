package com.example.android.sample.new3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.widget.Toast;

/**
 * Created by endoutaichi on 2017/06/12.
 */


    public class MyConfirmDialog extends ConfirmDialog  {

        protected void onInit(AlertDialog.Builder builder){
            builder.setTitle("確認");
            builder.setMessage("これでいい？");
        }

        protected void onOK(){
           //Toast.makeText( ,"OK", Toast.LENGTH_LONG).show();
        }

        protected void onCancel(){
            //Toast.makeText(this, "Cancel", Toast.LENGTH_LONG).show();
        }


}