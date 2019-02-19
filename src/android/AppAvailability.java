package com.ohh2ahh.appavailability;

import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaPlugin;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.pm.PackageManager;
import android.content.pm.PackageInfo;
import android.content.pm.ApplicationInfo;

public class AppAvailability extends CordovaPlugin {
    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if(action.equals("checkAvailability")) {
            String uri = args.getString(0);
            this.checkAvailability(uri, callbackContext);
            return true;
        }
        return false;
    }
    
    // Thanks to http://floresosvaldo.com/android-cordova-plugin-checking-if-an-app-exists
    public PackageInfo getAppPackageInfo(String uri) {
        Context ctx = this.cordova.getActivity().getApplicationContext();
        final PackageManager pm = ctx.getPackageManager();

        try {
            return pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
        }
        catch(PackageManager.NameNotFoundException e) {
            return null;
        }
    }
    
    public ApplicationInfo getApplicationInfo(String uri) {
        Context ctx = this.cordova.getActivity().getApplicationContext();
        final PackageManager pm = ctx.getPackageManager();

        try {
            return pm.getApplicationInfo(uri, 0);
        }
        catch(PackageManager.NameNotFoundException e) {
            return null;
        }
    }
    
    private void checkAvailability(String uri, CallbackContext callbackContext) {

        PackageInfo pInfo = getAppPackageInfo(uri);
        ApplicationInfo aInfo = getApplicationInfo(uri);
            
        if(pInfo != null && aInfo != null) {
            try {
                callbackContext.success(this.convertPackageInfoToJson(pInfo, aInfo));
            } 
            catch(JSONException e) {
                callbackContext.error("");    
            }
        }
        else {
            callbackContext.error("");
        }
    }

    private JSONObject convertPackageInfoToJson(PackageInfo pInfo, ApplicationInfo aInfo) throws JSONException {
        JSONObject json = new JSONObject();
        json.put("version", pInfo.versionName);
        json.put("appId", pInfo.packageName);
        json.put("enabled", aInfo.enabled);

        return json;
    }
}
