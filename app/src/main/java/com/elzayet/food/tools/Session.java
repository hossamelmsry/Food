package com.elzayet.food.tools;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;

import com.elzayet.food.MainActivity;


public class Session {

    public static void updateUserAccount(Context context,String phoneNumber,String userName,String userPassword,String userPinCode,String userRefellar) {
        SharedPreferences pref = ((Activity) context).getSharedPreferences("ACCOUNT", Context.MODE_PRIVATE);
        SharedPreferences.Editor handeler = pref.edit();
        handeler.putString("phoneNumber" ,phoneNumber);
        handeler.putString("userName"    ,userName);
        handeler.putString("userPassword",userPassword);
        handeler.putString("userPincode" ,userPinCode);
        handeler.putString("userRefellar" ,userRefellar);

        handeler.apply();
    }

    public static void loguot(Context context){
        SharedPreferences preferences =((Activity) context).getSharedPreferences("ACCOUNT",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
        Intent intent = new Intent(context, MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        ((Activity) context).finish();
    }

    ////////////////////////////////////
    ////////   backgroundMSG   /////////
    ////////////////////////////////////
//    public static void backgroundMSG(final Activity context  , String msg ) {
//        AlertDialog.Builder builder = new AlertDialog.Builder(context, R.style.AlertDialogStyle);
//        builder.setCancelable(false);
//        builder.setTitle("فريق العمل");
//        builder.setMessage(msg);
//        builder.setPositiveButton("حسنا", (dialog, which) -> {
//            Intent intent = new Intent(context, AccountActivity.class);
//            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//            context.startActivity(intent);
//        });
//        builder.setNegativeButton("لاحقا", (dialog, which) -> dialog.cancel());
//        builder.show();
//    }

//    public static void updateUserInformation(Activity activity,String phoneNumber, String password,String pinCode,String points,String business_name,String owner_name, String category,String logo,String background,String title,String address){
//        SharedPreferences pref = activity.getSharedPreferences(String.valueOf(ACCOUNT), Context.MODE_PRIVATE);
//        SharedPreferences.Editor handeler = pref.edit();
//        handeler.putString(String.valueOf(PHONENUMBER),phoneNumber);
//        handeler.putString(String.valueOf(PASSWORD),password);
//        handeler.putString(String.valueOf(PINCODE),pinCode);
//        handeler.putString(String.valueOf(POINT),points);
//        handeler.putString(String.valueOf(NAME),business_name);
//        handeler.putString(String.valueOf(OWNERNAME),owner_name);
//        handeler.putString(String.valueOf(CATEGORY),category);
//        handeler.putString(String.valueOf(LOGO),logo);
//        handeler.putString(String.valueOf(BACKGROUND),background);
//        handeler.putString(String.valueOf(TITLE),title);
//        handeler.putString(String.valueOf(ADDRESS),address);
//        handeler.apply();
//    }
//    public static void updateUserInformation(Activity activity,String points){
//        SharedPreferences pref = activity.getSharedPreferences(String.valueOf(ACCOUNT), Context.MODE_PRIVATE);
//        SharedPreferences.Editor handeler = pref.edit();
//        handeler.putString(String.valueOf(POINTS),points);
//        handeler.apply();
//    }
//    public static void updateUserInformation(Activity activity,String phoneNumber, String password,String pinCode,String business_name,String owner_name, String category,String logo,String background,String title,String address){
//        SharedPreferences pref = activity.getSharedPreferences(String.valueOf(ACCOUNT), Context.MODE_PRIVATE);
//        SharedPreferences.Editor handeler = pref.edit();
//        handeler.putString(String.valueOf(PHONENUMBER),phoneNumber);
//        handeler.putString(String.valueOf(PASSWORD),password);
//        handeler.putString(String.valueOf(PINCODE),pinCode);
//        handeler.putString(String.valueOf(NAME),business_name);
//        handeler.putString(String.valueOf(OWNERNAME),owner_name);
//        handeler.putString(String.valueOf(CATEGORY),category);
//        handeler.putString(String.valueOf(LOGO),logo);
//        handeler.putString(String.valueOf(BACKGROUND),background);
//        handeler.putString(String.valueOf(TITLE),title);
//        handeler.putString(String.valueOf(ADDRESS),address);
//        handeler.apply();
//    }

    ////////////////////////////////////
    /////  check for avalable ads  /////
    ////////////////////////////////////
//    public static boolean checkForAds(Context context){
//        SharedPreferences pref = context.getSharedPreferences(context.getString(R.string.businessAds),Context.MODE_PRIVATE);
//        if(!pref.contains(context.getString(R.string.sec_LimteTime_1)) || !pref.contains(context.getString(R.string.sec_LimteTime_2))  ||
//        !pref.contains(context.getString(R.string.sec_ToEmpty_1))      || !pref.contains(context.getString(R.string.sec_ToEmpty_2))    ||
//        !pref.contains(context.getString(R.string.sec_Grid_1))         || !pref.contains(context.getString(R.string.sec_Grid_2))       ||
//        !pref.contains(context.getString(R.string.sec_Horizontal_1))   || !pref.contains(context.getString(R.string.sec_Horizontal_2)) ||
//        !pref.contains(context.getString(R.string.sec_Vertical_1))     || !pref.contains(context.getString(R.string.sec_Vertical_2)))
//        { return true; }
//        else{ return false; }
//    }
    //////////////////////////////
    //////     createAd     //////
    //////////////////////////////
//    public static void createAd(Activity activity,String section,String key_proName,String productName,String key_proTimmer,long timmer){
//        SharedPreferences pref = activity.getSharedPreferences(activity.getString(R.string.businessAds),Context.MODE_PRIVATE);
//        SharedPreferences.Editor handeler = pref.edit();
//        handeler.putString(section,section);
//        handeler.putString(key_proName,productName);
//        handeler.putLong(key_proTimmer,timmer);
//        handeler.apply();
//    }
    ////////////////////////////
    //////     removeAd   //////
    ////////////////////////////
//    public static void removeAd(Activity activity,String section,String key_proName,String key_proTimmer){
//        SharedPreferences pref = activity.getSharedPreferences(activity.getString(R.string.businessAds),Context.MODE_PRIVATE);
//        SharedPreferences.Editor handeler = pref.edit();
//        handeler.remove(section);
//        handeler.remove(key_proName);
//        handeler.remove(key_proTimmer);
//        handeler.apply();
//    }
    ////////////////////////////////////
    //////        resetAll        //////
    ////////////////////////////////////
//    public static void removeAllADS(Activity activity){
//        SharedPreferences pref = activity.getSharedPreferences(activity.getString(R.string.businessAds),Context.MODE_PRIVATE);
//        SharedPreferences.Editor handeler = pref.edit();
//        handeler.clear();
//        handeler.apply(); // commit changes
//    }
}