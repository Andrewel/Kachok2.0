package org.bel.kachok.activities;

/**
 * Created by BEI on 03.04.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.bel.kachok.AudioNoteActivity;
import org.bel.kachok.ChecklistNoteActivity;
import org.bel.kachok.R;
import org.bel.kachok.SketchActivity;
import org.bel.kachok.TextNoteActivity;

public class KachokActivity extends AppCompatActivity  implements View.OnClickListener
{
    FloatingActionsMenu fabMenu;
    ImageButton imageButton2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscles);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);

//set the OnClickListeners
        findViewById(R.id.fab_m1).setOnClickListener(this);
        findViewById(R.id.fab_m2).setOnClickListener(this);
        findViewById(R.id.fab_m3).setOnClickListener(this);
        findViewById(R.id.fab_m4).setOnClickListener(this);
        findViewById(R.id.fab_m5).setOnClickListener(this);
        findViewById(R.id.fab_m6).setOnClickListener(this);
        findViewById(R.id.fab_m7).setOnClickListener(this);
        findViewById(R.id.fab_checklist).setOnClickListener(this);
        findViewById(R.id.imageButton2).setOnClickListener(this);


        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_m1:
                startActivity(new Intent(getApplication(), mActivity1.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_checklist:
                startActivity(new Intent(getApplication(), KachokActivity1.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_m2:
                startActivity(new Intent(getApplication(), mActivity2.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_m3:
                startActivity(new Intent(getApplication(), mActivity3.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_m4:
                startActivity(new Intent(getApplication(), mActivity4.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_m5:
                startActivity(new Intent(getApplication(), mActivity5.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_m6:
                startActivity(new Intent(getApplication(), mActivity6.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_m7:
                startActivity(new Intent(getApplication(), mActivity7.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.imageButton2:
                startActivity(new Intent(getApplication(), KachokActivity1.class));
                fabMenu.collapseImmediately();
                break;
        }
    }
}
