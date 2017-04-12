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

public class mActivity1 extends AppCompatActivity  implements View.OnClickListener
{
    FloatingActionsMenu fabMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activityshoulder);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);

//set the OnClickListeners
        findViewById(R.id.imageButton6).setOnClickListener(this);
        findViewById(R.id.imageButton7).setOnClickListener(this);
        findViewById(R.id.imageButton8).setOnClickListener(this);
        findViewById(R.id.imageButton9).setOnClickListener(this);
        findViewById(R.id.imageButton10).setOnClickListener(this);
        findViewById(R.id.imageButton11).setOnClickListener(this);



        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);



    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.imageButton6:
                startActivity(new Intent(getApplication(), TextNoteActivity.class));

                break;
            case R.id.imageButton7:
                startActivity(new Intent(getApplication(), ChecklistNoteActivity.class));

                break;
            case R.id.imageButton8:
                startActivity(new Intent(getApplication(), AudioNoteActivity.class));

                break;
            case R.id.imageButton9:
                startActivity(new Intent(getApplication(), SketchActivity.class));

                break;
            case R.id.imageButton10:
                startActivity(new Intent(getApplication(), SketchActivity.class));

                break;
            case R.id.imageButton11:
                startActivity(new Intent(getApplication(), KachokActivity.class));

                break;

        }
    }
}
