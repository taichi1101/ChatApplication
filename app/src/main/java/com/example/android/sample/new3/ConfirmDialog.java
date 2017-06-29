package com.example.android.sample.new3;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.Toast;

import static java.security.AccessController.getContext;

/**
 * Created by endoutaichi on 2017/06/13.
 */

public class ConfirmDialog implements DialogInterface.OnClickListener {

    public static class OnResultListener {
        public void onOK(){}
        public void onCancel(){}
    }

    private OnResultListener m_Listener = null;

    private Activity m_Activity;

    public void show(Activity activity){
        m_Activity = activity;
        m_Listener = onCreateOnResultListener();
        doShow();
    }

    public void show(Activity activity, OnResultListener l){
        m_Activity = activity;
        m_Listener = (l != null) ? l : onCreateOnResultListener();
        doShow();
    }

    //ここは、ボタンのみ
    protected void doShow(){
        AlertDialog.Builder builder = new AlertDialog.Builder(m_Activity);
        builder.setPositiveButton("OK", this);
        builder.setNegativeButton("Cancel", this);
        onInit(builder);
        builder.create().show();
    }

    protected void onInit(AlertDialog.Builder builder){
    }

    protected OnResultListener onCreateOnResultListener(){
        return new OnResultListener();
    }

    public void onClick(DialogInterface dialog, int which) {
        switch(which){
            case DialogInterface.BUTTON1: m_Listener.onOK(); break;
            case DialogInterface.BUTTON2: m_Listener.onCancel(); break;
        }
    }
}
//この修正により、MyConfirmDialogの実装は次のように変わる。

