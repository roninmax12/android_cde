package com.rudik_maksim.cde;

import android.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import com.rudik_maksim.cde.fragments.FragmentSubjectDetails;

public class ActivitySubjectDetails extends ActionBarActivity {
    public static String syu_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_subject_details);

        String title = getIntent().getStringExtra("subject_title");
        syu_id = getIntent().getStringExtra("syu_id");
        int firstIndex = title.indexOf("(");
        title = title.substring(0, firstIndex-1);
        getActionBar().setTitle(title);
        getActionBar().setDisplayHomeAsUpEnabled(true);

        android.app.Fragment fragment = new FragmentSubjectDetails();
        FragmentManager fm = getFragmentManager();
        fm.beginTransaction()
                .replace(R.id.container, fragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.subject_details_info, menu);
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
