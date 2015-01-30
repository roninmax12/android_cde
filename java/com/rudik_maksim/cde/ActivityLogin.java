package com.rudik_maksim.cde;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.content.Intent;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.lang.*;
import android.os.AsyncTask;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

public class ActivityLogin extends ActionBarActivity{
    //private ImageView ivNewAnim;
    private EditText etLogin;
    private EditText etPass;
    private Button btnConnect;
    private Animation animation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new PlaceholderFragment())
                    .commit();
        }

        Global.Application.context = this;
    }

    @Override
    public void onStart(){
        super.onStart();
        //ivNewAnim  = (ImageView)findViewById(R.id.ivIfmoLogo);
        etLogin = (EditText)findViewById(R.id.editTextLogin);
        etPass = (EditText)findViewById(R.id.editTextPassword);
        btnConnect = (Button)findViewById(R.id.btnConnect);
        animation = AnimationUtils.loadAnimation(this, R.anim.animation_logo);

        LinearLayout l = (LinearLayout)findViewById(R.id.fragmentLogin_linearLayout);
        Animation a = AnimationUtils.loadAnimation(this, R.anim.up_top);
        l.startAnimation(a);

        if(readPrefs()) query();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Global.Configuration.clear_login_password_edittext){
            etLogin.setText("");
            etPass.setText("");
            btnConnect.setText("Вход");
            btnConnect.setEnabled(true);
            etLogin.clearFocus();
            etPass.clearFocus();
            Global.Configuration.clear_login_password_edittext = false;
            Global.DataLoaded.Points = false;
            Global.CDEData.SELECTED_YEAR = null;
            Global.Configuration.currentYearChosen = null;
            Global.CDEData.clearAllData();
        }
    }

    @Override
    public void onBackPressed(){
        DestroyApp();
    }

    public void DestroyApp(){
        moveTaskToBack(true);
        super.onDestroy();
        System.runFinalizersOnExit(true);
        System.exit(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return false;
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_login, container, false);
            return rootView;
        }
    }

    private void savePrefs(String login, String password) {
        try {
            SharedPreferences.Editor editor = Global.Application.preferences.edit();
            editor.putString("login", login);
            editor.putString("password", password);
            editor.commit();

            Global.CDEData.login = login;
            Global.CDEData.password = password;
        }
        catch (Throwable t) {
            return;
        }
    }

    private boolean readPrefs(){
        try {
            String shared_login    = Global.Application.preferences.getString("login","");
            String shared_password = Global.Application.preferences.getString("password","");

            if (!"".equals(shared_login) && !"".equals(shared_password)){
                etLogin.setText(shared_login);
                etPass.setText(shared_password);
                Global.CDEData.login = shared_login;
                Global.CDEData.password = shared_password;

                //Global.Configuration.show_data_on_cur_sem  = Global.Application.preferences.getBoolean("showDataCurrentSemester", false);
                //Global.Configuration.push_enabled          = Global.Application.preferences.getBoolean("enablePushNewPoints", false);

                return true;
            }else
                return false;
        }
        catch (Throwable t) {
            return false;
        }
    }

    public void query(){
        if (Global.Application.isOnline()){
            if ("".equals(etLogin.getText().toString()) || "".equals(etPass.getText().toString())){
                Toast.makeText(this, "Введите логин и пароль", Toast.LENGTH_SHORT).show();
            }else
                new AsyncConnection().execute();
        }
        else{
            Toast t = Toast.makeText(this,"Отсутствует подключение к интернету", Toast.LENGTH_SHORT);
            t.show();
        }
    }

    public void onClick(View v){
        // HIDE KEYBORD
        //ivNewAnim.clearAnimation();
        EditText etPass = (EditText)findViewById(R.id.editTextPassword);
        InputMethodManager imm = (InputMethodManager)getSystemService(
                Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(etPass.getWindowToken(), 0);

        query();
    }

    class AsyncConnection extends AsyncTask<Void,Void,Void>{
        private boolean authSuccess = false;
        String login, password;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();

            login = etLogin.getText().toString();
            password = etPass.getText().toString();
            //ivNewAnim.startAnimation(animation);
            btnConnect.setEnabled(false);
            Global.Application.connection = new Connection(login, password);
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
                //ivNewAnim.clearAnimation();
                Toast.makeText(ActivityLogin.this, "Ошибка авторизации", Toast.LENGTH_SHORT).show();
                btnConnect.setEnabled(true);
            }
            else{
                Global.CDEData.clearAllData();

                savePrefs(login, password);
                Global.Application.authorized = true;

                Global.Configuration.show_data_on_cur_sem  = Global.Application.preferences.getBoolean("showDataCurrentSemester", true);
                Global.Configuration.push_enabled          = Global.Application.preferences.getBoolean("enablePushNewPoints",     true);
                Global.Configuration.expandListView        = Global.Application.preferences.getBoolean("expandListView",          true);

                Intent intent = new Intent(ActivityLogin.this, ActivityPoints.class);
                startActivity(intent);
                finish();
            }
        }
    }
}
