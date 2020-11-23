package com.imcub.lume.view;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.view.LayoutInflater;

import com.imcub.lume.R;

public class CarregamentoDialog {

    Activity activity;
    AlertDialog alertDialog;

    CarregamentoDialog(Activity mActivity) {
        activity = mActivity;


    }
    @SuppressLint("InflateParams")
    void inicioCarregamentoDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        LayoutInflater inflater = activity.getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialogcustom,null));
        builder.setCancelable(true);

        alertDialog = builder.create();
        alertDialog.show();
    }

    void  dimissDialog(){
        alertDialog.dismiss();
    }

}
