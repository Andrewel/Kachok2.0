package org.bel.kachok.activities;

/**
 * Created by BEI on 05.04.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import android.view.KeyEvent;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;


import com.cunoraz.gifview.library.GifView;

import org.bel.kachok.R;
import org.bel.kachok.fragments.GifFragment;

public class mActivity2 extends AppCompatActivity implements View.OnClickListener {
    private WebView view; //membuat variabel view agar bisa akses method onKeyDown
    private Button pauseButton;
    private Button playButton;
    private Button nextButton;
    private Button dialogButton;
    private Button toastButton;
    private Button fragmentButton;
    private GifView gifView;
    private int index = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.content_maingif);
        //Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

        view = (WebView) this.findViewById(R.id.WebView2);
        view.getSettings().setJavaScriptEnabled(true);
        view.setWebViewClient(new MyBrowser());
        view.loadUrl("https://iq-body.ru/exercises/i-zgim-shtangi-lezga"); //try js alert
        view.setWebChromeClient(new WebChromeClient()); // adding js alert support

        gifView = (GifView) findViewById(R.id.gif1);
        pauseButton = (Button) findViewById(R.id.button_gif_pause);
        playButton = (Button) findViewById(R.id.button_gif_play);
        nextButton = (Button) findViewById(R.id.button_gif_next);
        dialogButton = (Button) findViewById(R.id.button_dialog);
        fragmentButton = (Button) findViewById(R.id.fragment);
        toastButton = (Button) findViewById(R.id.toast);
        getSupportActionBar().hide();

        findViewById(R.id.getback2).setOnClickListener(this);

        fragmentButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openFragment();
            }
        });

        toastButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showToast();

            }
        });

        pauseButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gifView.isPlaying())
                    gifView.pause();
            }
        });
        playButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {

                if (gifView.isPaused())
                    gifView.play();
            }
        });
        nextButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (index % 10){
                    case 0:
                        gifView.setGifResource(R.mipmap.gif12);
                        break;
                    case 1:
                        gifView.setGifResource(R.mipmap.gif10);
                        break;
                    case 2:
                        gifView.setGifResource(R.mipmap.gif11);
                        break;
                    case 3:
                        gifView.setGifResource(R.mipmap.gif13);
                        break;
                    case 4:
                        gifView.setGifResource(R.mipmap.gif14);
                        break;
                    case 5:
                        gifView.setGifResource(R.mipmap.gif15);
                        break;
                    case 6:
                        gifView.setGifResource(R.mipmap.gif16);
                        break;
                    case 7:
                        gifView.setGifResource(R.mipmap.gif17);
                        break;
                    case 8:
                        gifView.setGifResource(R.mipmap.gif18);
                        break;
                    case 9:
                        gifView.setGifResource(R.mipmap.gif19);
                        break;
                }
                index++;
            }
        });

        dialogButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });



    }
    private class MyBrowser extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            view.loadUrl(url);
            return true;
        }
    }

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //ketika disentuh tombol back
        if ((keyCode == KeyEvent.KEYCODE_BACK) && view.canGoBack()) {
            view.goBack(); //method goback() dieksekusi untuk kembali pada halaman sebelumnya
            return true;
        }
        // Jika tidak ada history (Halaman yang sebelumnya dibuka)
        // maka akan keluar dari activity
        return super.onKeyDown(keyCode, event);
    }


    public void openFragment() {
        FragmentTransaction trans = getSupportFragmentManager()
                .beginTransaction();
        GifFragment fragmentLocal =  GifFragment.newInstance();
        fragmentLocal.setHasOptionsMenu(true);
        trans.replace(R.id.frame, fragmentLocal, fragmentLocal.getTAG());
        trans.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        trans.addToBackStack(fragmentLocal.getTAG());
        trans.commitAllowingStateLoss();
    }

    public void showToast() {
        LayoutInflater inflater = getLayoutInflater();
        View layout = inflater.inflate(R.layout.custom_toast, null);
        Toast toastLocal = new Toast(getApplicationContext());
        toastLocal.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toastLocal.setDuration(Toast.LENGTH_LONG);
        toastLocal.setView(layout);
        toastLocal.show();
    }

    public void openDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this, R.style.AppCompatAlertDialogStyle);
        builder.setView(getLayoutInflater().inflate(R.layout.dialog_deleting,null));
        builder.show();

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.getback2:
                startActivity(new Intent(getApplication(), MainActivity.class));
                break;
        }
    }
}
