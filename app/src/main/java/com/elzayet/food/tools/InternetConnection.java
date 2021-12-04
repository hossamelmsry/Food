package com.elzayet.food.tools;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;
import android.widget.Toast;
import com.elzayet.food.R;

public class InternetConnection extends BroadcastReceiver {
    private static ConnectivityManager conMan;
    private static NetworkInfo netInfo;

    private static void checkMethod(Context context , boolean wifi){
        conMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if(conMan != null){
            netInfo = conMan.getActiveNetworkInfo();

            if(netInfo != null && netInfo.isConnected()){

                if(netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                    Toast.makeText(context, R.string.phone_data, Toast.LENGTH_SHORT).show();
                }
                else if(wifi && netInfo.getType() == ConnectivityManager.TYPE_WIFI){
                    WifiManager wifiManger = (WifiManager)context.getSystemService(context.WIFI_SERVICE);
                    String wifiName = wifiManger.getConnectionInfo().getSSID();
                    Toast.makeText(context, "تم الاتصال بـ"+wifiName, Toast.LENGTH_LONG).show();
                }
            }else{
                Toast.makeText(context, R.string.disconnected, Toast.LENGTH_LONG).show();
            }
        }else{ }
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        boolean wifi = true;
        checkMethod(context , wifi);
    }

    public static boolean isConnected(Context context) {
        conMan = (ConnectivityManager)context.getSystemService(Context.CONNECTIVITY_SERVICE);
        if(conMan != null){
            netInfo = conMan.getActiveNetworkInfo();
            if(netInfo != null && netInfo.isConnected()){    return true;    }
        }
        return false;
    }

    public static void noConnection(Context context){
//        AlertDialog.Builder builder = new AlertDialog.Builder(((Activity) context), R.style.AlertDialogStyle);
        AlertDialog.Builder builder = new AlertDialog.Builder(((Activity) context));
        builder.setTitle(R.string.no_internet_connection);
        builder.setMessage(R.string.internet_msg);
        builder.setCancelable(false);
        builder.setNegativeButton(R.string.connect, (dialog, which) -> ((Activity) context).startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS)));
        builder.setPositiveButton(R.string.skip, (dialog, which) -> dialog.dismiss());
        builder.show();
    }
}
