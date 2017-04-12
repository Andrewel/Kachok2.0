package org.bel.kachok.activities;

/**
 * Created by BEI on 05.04.2017.
 */

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

import org.bel.kachok.AudioNoteActivity;
import org.bel.kachok.ChecklistNoteActivity;
import org.bel.kachok.R;
import org.bel.kachok.SketchActivity;
import org.bel.kachok.TextNoteActivity;

public class mActivity2 extends AppCompatActivity  implements View.OnClickListener
{
    FloatingActionsMenu fabMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_muscles1);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);

//set the OnClickListeners
        findViewById(R.id.fab_mb1).setOnClickListener(this);
        findViewById(R.id.fab_mb2).setOnClickListener(this);
        findViewById(R.id.fab_mb3).setOnClickListener(this);
        findViewById(R.id.fab_mb4).setOnClickListener(this);
        findViewById(R.id.fab_mb5).setOnClickListener(this);
        findViewById(R.id.imageButton).setOnClickListener(this);
        findViewById(R.id.fab_checklist2).setOnClickListener(this);


        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.fab_mb1:
                startActivity(new Intent(getApplication(), TextNoteActivity.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_mb2:
                startActivity(new Intent(getApplication(), ChecklistNoteActivity.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_mb3:
                startActivity(new Intent(getApplication(), AudioNoteActivity.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_mb4:
                startActivity(new Intent(getApplication(), SketchActivity.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_mb5:
                startActivity(new Intent(getApplication(), SketchActivity.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.imageButton:
                startActivity(new Intent(getApplication(), KachokActivity.class));
                fabMenu.collapseImmediately();
                break;
            case R.id.fab_checklist2:
                startActivity(new Intent(getApplication(), KachokActivity.class));
                fabMenu.collapseImmediately();
                break;

        }
    }
}
