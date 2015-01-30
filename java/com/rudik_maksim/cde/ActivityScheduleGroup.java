package com.rudik_maksim.cde;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.rudik_maksim.cde.fragments.FragmentScheduleGroup;
import com.rudik_maksim.cde.fragments.FragmentScheduleGroupSearch;
import com.rudik_maksim.cde.fragments.FragmentScheduleTeacher;
import com.rudik_maksim.cde.fragments.FragmentScheduleTeacherSearch;

/**
 * Created by maksimrudik on 26.12.14.
 */
public class ActivityScheduleGroup extends ActionBarActivity {
    public static String groupNumber = "";
    public static SearchView searchView;
    LinearLayout linearLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_group);

        getActionBar().setTitle("Расписание группы");
        linearLayout = (LinearLayout)findViewById(R.id.activity_schedule_group_linLayout);

        groupNumber = getIntent().getStringExtra("groupNumber");

        try{
            if (groupNumber.length() >= 4){
                linearLayout.setVisibility(View.INVISIBLE);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FragmentScheduleGroup())
                        .commit();
            }
        }catch (NullPointerException ex){}

        getActionBar().setDisplayHomeAsUpEnabled(true);
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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_teacher, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        final MenuItem searchMenuItem = menu.findItem(R.id.search);
        searchView = (SearchView) searchMenuItem.getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchView.clearFocus();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() > 0) {
                    linearLayout.setVisibility(View.INVISIBLE);
                    groupNumber = s;
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FragmentScheduleGroupSearch())
                            .commit();
                }
                return false;
            }
        });

        searchView.setOnQueryTextFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean queryTextFocused) {
                if(!queryTextFocused) {
                    searchMenuItem.collapseActionView();
                    searchView.setQuery("", false);
                }
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNewIntent(Intent intent) {
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String search = intent.getStringExtra(SearchManager.QUERY);
            try {
                TextView text = (TextView) this.findViewById(R.id.search_src_text);
                text.setText(search);
            } catch (IllegalStateException ex){
                Log.d(Global.Debug.LOG_TAG, ex.getStackTrace().toString());}

            //doSearch(search);
        }
    }
}
