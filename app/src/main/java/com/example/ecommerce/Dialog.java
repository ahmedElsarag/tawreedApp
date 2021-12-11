package com.example.ecommerce;

import android.content.Context;

import com.example.ecommerce.prevalent.Prevalent;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Dialog {
    SweetAlertDialog pDialog;

    public void loadDialog(Context context) {
        pDialog = new SweetAlertDialog(context, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(context.getResources().getColor(R.color.colorPrimary));
        pDialog.setTitleText("Loading ...");
        pDialog.show();
    }

    public void dismisDialog() {
        pDialog.dismissWithAnimation();
    }
}
