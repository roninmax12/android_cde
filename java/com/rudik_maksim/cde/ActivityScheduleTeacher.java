package com.rudik_maksim.cde;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.rudik_maksim.cde.fragments.FragmentScheduleTeacher;

/**
 * Created by Максим on 14.11.2014.
 */
public class ActivityScheduleTeacher extends ActionBarActivity {
    public static String teacherId = "";
    public static ScheduleTeacher scheduleTeacher;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_teacher);

        final String teacherName = getIntent().getStringExtra("teacherName");
        getActionBar().setTitle(teacherName);
        getActionBar().setDisplayHomeAsUpEnabled(true);

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
            //Toast.makeText(this, teacherId + " | records: " + teachersCount, Toast.LENGTH_SHORT).show();
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
