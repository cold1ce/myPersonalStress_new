package com.fimrc.mysensornetwork;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Sven on 16.03.2017.
 */

public class L  {
    public static void m(String message){
        Log.d("mySensorNetwork", message);
    }
    public static void s(Context context, String message){
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show();
    }
}
