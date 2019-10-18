package com.bignerdranch.android.forecast;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;

public class AlertDialogFragment extends DialogFragment {
    //to create the dialog
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        //configure the dialog
        //create the AleretDialog
        Context context = getActivity();
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(getString(R.string.dialog_error_title))
        .setMessage(getString(R.string.dialog_error_message))
        .setPositiveButton(getString(R.string.dialog_positioveButton), null);

        //create the AlertDialog object and return it.
        return builder.create();






    }
}
