package com.bihaika.android.asimplesudoku.dialog;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.view.Gravity;
import android.widget.TextView;

import com.bihaika.android.asimplesudoku.R;


public class DialogExit {

    public void dialogExit(final Activity activity) {
        AlertDialog.Builder adb = new AlertDialog.Builder(activity);
        adb.setMessage(activity.getString(R.string.exit));
        adb.setPositiveButton(activity.getString(R.string.yes), new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
        adb.setNegativeButton(activity.getString(R.string.no), null);
        AlertDialog dialog = adb.create();
        dialog.setCancelable(false);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
        ((TextView) dialog.findViewById(android.R.id.message)).setGravity(Gravity.CENTER);
    }

}
