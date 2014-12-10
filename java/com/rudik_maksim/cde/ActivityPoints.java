package com.rudik_maksim.cde;

import android.support.v4.app.DialogFragment;
import android.support.v7.app.ActionBarActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.support.v4.widget.DrawerLayout;
import android.widget.Toast;

import com.rudik_maksim.cde.fragments.DialogAboutFragment;
import com.rudik_maksim.cde.fragments.DialogChooseYearFragment;
import com.rudik_maksim.cde.fragments.FragmentPoints;
import com.rudik_maksim.cde.fragments.FragmentProtocol;
import com.rudik_maksim.cde.fragments.FragmentRating;
import com.rudik_maksim.cde.fragments.FragmentRecordCDE;
import com.rudik_maksim.cde.fragments.FragmentSchedule;
import com.rudik_maksim.cde.fragments.FragmentScheduleAttestation;
import com.rudik_maksim.cde.fragments.FragmentScheduleSession;
import com.rudik_maksim.cde.fragments.FragmentSettings;
import com.rudik_maksim.cde.services.NewPointsNotificationService;

public class ActivityPoints extends ActionBarActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks{

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */

    Intent serviceIntent;
    int FragmentNumber = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_points);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        //mTitle = getString(R.string.title_fragmentPoints);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
               (DrawerLayout) findViewById(R.id.drawer_layout));

        serviceIntent = new Intent(this, NewPointsNotificationService.class);

        Global.Application.preferences = getSharedPreferences(Global.Application.SHARED_PREFS_NAME, MODE_PRIVATE);

        if(!Global.Application.authorized){
            //Global.CDEData.clearAllData();
            authorization();
        }
    }

    public void authorization(){
        Global.CDEData.login    = Global.Application.preferences.getString("login",   "");
        Global.CDEData.password = Global.Application.preferences.getString("password","");

        if (!"".equals(Global.CDEData.login) && !"".equals(Global.CDEData.password)){
            Global.Configuration.show_data_on_cur_sem  = Global.Application.preferences.getBoolean("showDataCurrentSemester", true);
            Global.Configuration.push_enabled          = Global.Application.preferences.getBoolean("enablePushNewPoints",     true);
//DELETE THIS AFTER FIX BUG
Global.Configuration.push_enabled = false;
            Global.Configuration.expandListView        = Global.Application.preferences.getBoolean("expandListView",          true);
            // Делаем запрос к серверу на авторизацию
            new AsyncConnection().execute();
        }else{
            Intent i = new Intent(this, ActivityLogin.class);
            startActivity(i);
            return;
        }
    }


    class AsyncConnection extends AsyncTask<Void,Void,Void>{
        private boolean authSuccess = false;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            Global.Application.connection = new Connection(Global.CDEData.login, Global.CDEData.password);
        }

        @Override
        protected Void doInBackground(Void... params){
            try{
                authSuccess = Global.Application.connection.Connect();
            }catch (Exception ex){authSuccess = false;}

            return null;
        }

        @Override
        protected void onPostExecute(Void result){
            if (!authSuccess){
                Intent i = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(i);
            }
            else{
                Global.Application.authorized = true;
            }
        }
    }

    @Override
    public void onBackPressed(){
        super.onBackPressed();
        SharedPreferences.Editor editor = Global.Application.preferences.edit();
        editor.putBoolean("showDataCurrentSemester", Global.Configuration.show_data_on_cur_sem);
        editor.putBoolean("enablePushNewPoints",     Global.Configuration.push_enabled);
        editor.putBoolean("expandListView",          Global.Configuration.expandListView);
        editor.commit();

        if (Global.Configuration.push_enabled){
            serviceIntent.putExtra("login",         Global.CDEData.login);
            serviceIntent.putExtra("password",      Global.CDEData.password);
            serviceIntent.putExtra("file_protocol", Global.Application.FILE_PROTOCOL);
            startService(serviceIntent);
        }
        else
            stopService(serviceIntent);

        DestroyApp();
    }

    public void DestroyApp(){
        moveTaskToBack(true);
        super.onDestroy();
        System.runFinalizersOnExit(true);
        System.exit(0);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        FragmentNumber = position;
        android.support.v4.app.Fragment supportFragment = null;

        switch (position){
            case Global.Configuration.NAV_POINTS:
                supportFragment = new FragmentPoints(); break;
            case Global.Configuration.NAV_PROTOCOL:
                supportFragment = new FragmentProtocol(); break;
            case Global.Configuration.NAV_RATING:
                supportFragment = new FragmentRating(); break;
            case Global.Configuration.NAV_RECORD_CDE:
                supportFragment = new FragmentRecordCDE(); break;
            case Global.Configuration.NAV_SCHEDULE:
                supportFragment = new FragmentSchedule(); break;
            case Global.Configuration.NAV_SESSION_SCHEDULE:
                supportFragment = new FragmentScheduleSession(); break;
            case Global.Configuration.NAV_ATTESTATION_SCHEDULE:
                supportFragment = new FragmentScheduleAttestation(); break;
            case Global.Configuration.NAV_SETTINGS:
                supportFragment = new FragmentSettings(); break;
            case Global.Configuration.NAV_EXIT:
                logout(); break;
        }

        Global.Application.fragment = supportFragment;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu){
        super.onPrepareOptionsMenu(menu);

        if (Global.Application.fragment != null){
            android.support.v4.app.FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.container, Global.Application.fragment)
                    .commit();
        }

        return false;
    }

    public void logout(){
        try {
            Global.Application.preferences
                    .edit()
                    .clear()
                    .commit();

            Global.CDEData.clearAllData();
            Global.Configuration.clear_login_password_edittext = true;
            stopService(serviceIntent);
            Intent i = new Intent(this, ActivityLogin.class);
            startActivity(i);
        }
        catch (Throwable t) {}
    }

    public void onSectionAttached(int number) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            switch (FragmentNumber){
                case Global.Configuration.NAV_POINTS:
                    getMenuInflater().inflate(R.menu.menu_points, menu); break;
                case Global.Configuration.NAV_SETTINGS:
                    getMenuInflater().inflate(R.menu.menu_settings, menu); break;
                case Global.Configuration.NAV_SCHEDULE:
                    getMenuInflater().inflate(R.menu.menu_schedule, menu); break;
            }

            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_settings:
                DialogFragment newFragment = new DialogAboutFragment();
                newFragment.show(getSupportFragmentManager(), "dialog");
                break;
            case R.id.action_choose_year:
                if(Global.DataLoaded.Points){
                    Global.Fragments.fragment_year = new DialogChooseYearFragment();
                    Global.Fragments.fragment_year.show(getSupportFragmentManager(), "dialog1");
                    return true;
                }else{
                    Toast.makeText(this, "Дождитесь загрузки данных", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.action_schedule_teacher:
                Intent i = new Intent(this, ActivityScheduleTeacher.class);
                i.putExtra("search", true);
                startActivity(i);
        }
       return super.onOptionsItemSelected(item);
    }
}