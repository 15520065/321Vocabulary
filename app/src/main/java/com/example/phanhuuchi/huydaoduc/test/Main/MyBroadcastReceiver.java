package com.example.phanhuuchi.huydaoduc.test.Main;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Admin on 1/1/2018.
 */

public class MyBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String action = intent.getAction();
        // bắt Screen Off action. Nếu bắt được thì chạy Exam Activity. Để sau khi unlock thì nó hiện lên sẵn rồi
        if(action.equals(Intent.ACTION_SCREEN_OFF))
        {
            Intent i = new Intent(context, Exam_Activity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }
    }

}
