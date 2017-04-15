package org.bel.kachok.activities;

/**
 * Created by BEI on 13.04.2017.
 */

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import org.bel.kachok.R;

import java.io.IOException;
import java.io.InputStream;

public class GifWebView extends AppCompatActivity {
    private WebView view; //membuat variabel view agar bisa akses method onKeyDown

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        InputStream stream = null;
        try {
            stream = getAssets().open("gif13.gif");
        } catch (IOException e) {
            e.printStackTrace();
        }
        MovieGifView gifView = new MovieGifView(this, stream);
        setContentView(gifView);
    }

}