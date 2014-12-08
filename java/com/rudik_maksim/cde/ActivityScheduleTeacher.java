package com.rudik_maksim.cde;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.support.v7.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.rudik_maksim.cde.fragments.FragmentScheduleTeacher;
import com.rudik_maksim.cde.fragments.FragmentScheduleTeacherSearch;

import java.util.ArrayList;

/**
 * Created by Максим on 14.11.2014.
 */
public class ActivityScheduleTeacher extends ActionBarActivity {
    public static String teacherId = "";
    public static ScheduleTeacher scheduleTeacher;
    public static String teacherName = "";
    boolean isSearch = false;
    SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_teacher);

        teacherName = getIntent().getStringExtra("teacherName");
        isSearch = getIntent().getBooleanExtra("search", false);

        if (!isSearch)
            getActionBar().setTitle(teacherName);
        else
            getActionBar().setTitle("Расписание преподавателя");

        getActionBar().setDisplayHomeAsUpEnabled(true);

        if (!isSearch){
            scheduleTeacher = new ScheduleTeacher();
            teacherId = scheduleTeacher.getTeacherId(teacherName);
            int teachersCount = scheduleTeacher.getCountRecords();

            LinearLayout linearLayout = (LinearLayout)findViewById(R.id.activity_schedule_teacher_linLayout);
            ImageView imageView = (ImageView)findViewById(R.id.activity_schedule_teacher_imageView);

            if (teacherId.length() < 5){
                linearLayout.setVisibility(View.VISIBLE);
                imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent send = new Intent(Intent.ACTION_SENDTO);
                        String uriText = "mailto:"   + Uri.encode(getString(R.string.dev_mail)) +
                                "?subject=" + Uri.encode("cde: problem with teacher") +
                                "&body="    + Uri.encode(teacherName);
                        Uri uri = Uri.parse(uriText);

                        send.setData(uri);
                        startActivity(Intent.createChooser(send, "Отправить сообщение"));
                    }
                });
            }
            else{
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FragmentScheduleTeacher())
                        .commit();
            }
        }else{
            // Search
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, new FragmentScheduleTeacherSearch())
                    .commit();
        }
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
        if (id == R.id.search){
            if (Global.Configuration.isScheduleTeacherFragment){
                android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.container, new FragmentScheduleTeacherSearch())
                        .commit();
            }
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_schedule_teacher, menu);

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView = (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (Global.Configuration.isScheduleTeacherFragment){
                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FragmentScheduleTeacherSearch())
                            .commit();
                }

                if (s.length() >= 2)
                    doSearch(s);

                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onNewIntent(Intent intent) {
        // Проверяем, что новый интент передан именно для поиска
        if(Intent.ACTION_SEARCH.equals(intent.getAction())) {
            // Здесь будет храниться то, что пользователь ввёл в поисковой строке
            String search = intent.getStringExtra(SearchManager.QUERY);

        }
    }

    void doSearch(String s){
        final ScheduleTeacher st = new ScheduleTeacher();
        ArrayList<String> found = st.getSimilarTeachers(s);
        int size = found.size();

        final String[] teachers = new String[size];
        found.toArray(teachers);

        FragmentScheduleTeacherSearch.ListViewSearchAdapter adapter = new FragmentScheduleTeacherSearch.ListViewSearchAdapter(this, teachers);

        if (size == 0){
            FragmentScheduleTeacherSearch.textView.setText("Преподаватель не найден");
            FragmentScheduleTeacherSearch.textView.setVisibility(View.VISIBLE);
            FragmentScheduleTeacherSearch.listView.setAdapter(adapter);
        }
        else{
            FragmentScheduleTeacherSearch.textView.setVisibility(View.INVISIBLE);
            FragmentScheduleTeacherSearch.listView.setAdapter(adapter);

            FragmentScheduleTeacherSearch.listView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                teacherId = st.getTeacherId(teachers[position]);
                if (teacherId.length() > 5){
                    searchView.clearFocus();
                    teacherName = teachers[position];

                    android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
                    fragmentManager.beginTransaction()
                            .replace(R.id.container, new FragmentScheduleTeacher())
                            .commit();
                }
                }
            });

        }

    }
}
