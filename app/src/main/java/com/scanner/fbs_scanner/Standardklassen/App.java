package com.scanner.fbs_scanner.Standardklassen;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.StringRes;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.scanner.fbs_scanner.R;

// Hilfsklasse, die es ermöglicht, auch innerhalb von Non-Activity-Klassen auf die Stringressourcen
// zuzugreifen mit: App.getContext().getResources().getString(R.string.XXXXXX)
public class App extends Application {

    private static Context mContext;

    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;
    }

    public static Context getContext(){
        return mContext;
    }

}
