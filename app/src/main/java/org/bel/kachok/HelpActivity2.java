package org.bel.kachok;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.getbase.floatingactionbutton.FloatingActionsMenu;

public class HelpActivity2 extends AppCompatActivity  implements View.OnClickListener
         {
    FloatingActionsMenu fabMenu;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.app_bar_main2);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar2);
        //setSupportActionBar(toolbar);

//set the OnClickListeners
        findViewById(R.id.fab_text).setOnClickListener(this);
        findViewById(R.id.fab_checklist).setOnClickListener(this);
        findViewById(R.id.fab_audio).setOnClickListener(this);
        findViewById(R.id.fab_sketch).setOnClickListener(this);


        fabMenu = (FloatingActionsMenu) findViewById(R.id.fab_menu);



    }
             @Override
             public void onClick(View v) {
                 switch (v.getId()) {
                     case R.id.fab_text:
                         startActivity(new Intent(getApplication(), TextNoteActivity.class));
                         fabMenu.collapseImmediately();
                         break;
                     case R.id.fab_checklist:
                         startActivity(new Intent(getApplication(), ChecklistNoteActivity.class));
                         fabMenu.collapseImmediately();
                         break;
                     case R.id.fab_audio:
                         startActivity(new Intent(getApplication(), AudioNoteActivity.class));
                         fabMenu.collapseImmediately();
                         break;
                     case R.id.fab_sketch:
                         startActivity(new Intent(getApplication(), SketchActivity.class));
                         fabMenu.collapseImmediately();
                         break;
                 }
             }
}
