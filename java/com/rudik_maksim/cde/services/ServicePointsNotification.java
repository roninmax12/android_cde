package com.rudik_maksim.cde.services;

import android.annotation.TargetApi;
import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.util.Log;

import com.rudik_maksim.cde.ActivityPoints;
import com.rudik_maksim.cde.Global;
import com.rudik_maksim.cde.R;
import com.rudik_maksim.cde.Schedule;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.CookieStore;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class ServicePointsNotification extends IntentService {
    int repeatInterval = 20000;

    final int NOTIFY_ID = 101;

    CookieManager cManager;
    CookieStore cStore;

    String lastSubject = "", lastDescription = "", lastPoint = "";
    String fileSubject = "", fileDescription = "", filePoint = "";

    String login = "", password = "", file_protocol = "";

    SharedPreferences sharedPreferences = null;

    boolean run = true;

    public ServicePointsNotification(){
        super("ServicePointsNotificationThread");
    }

    @Override
    public void onCreate(){
        run = true;
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.d(Global.Debug.LOG_TAG, "onstart");
        return START_REDELIVER_INTENT;
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        login         = intent.getStringExtra("login");
        password      = intent.getStringExtra("password");
        file_protocol = intent.getStringExtra("file_protocol");

        while (run){
            try{
                //Log.d(Global.Debug.LOG_TAG, login + " " + password + " " + file_protocol);
                doWork();
                Thread.sleep(repeatInterval);
            }catch (Exception ex){ex.printStackTrace();}
        }
    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        run = false;
        Log.d(Global.Debug.LOG_TAG, "onDESTROY---------------");
    }

    void doWork(){

        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String sp_protocol = sharedPreferences.getString("shared_protocol", "-"); // it work :)
        Log.d(Global.Debug.LOG_TAG, "sp_protocol: " + sp_protocol);

        String[] arr_protocol = sp_protocol.split("_");

        if (sp_protocol.length() > 2){
            Log.d(Global.Debug.LOG_TAG, ">2");
            if (arr_protocol.length == 3){
                Log.d(Global.Debug.LOG_TAG, "==3");
                fileSubject = arr_protocol[0];
                fileDescription = arr_protocol[1];
                filePoint = arr_protocol[2];
            }else Log.d(Global.Debug.LOG_TAG, "arr_len: " + arr_protocol.length);
        }


        new AsyncGetData().execute();
    }

    class AsyncGetData extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                getData();
                Log.d(Global.Debug.LOG_TAG, "SERVICE getData()");
            }catch (Exception ex){onDestroy();}
            return null;
        }
    }

    public boolean Connect() throws IOException {
        URL de = new URL("http://de.ifmo.ru/servlet/?Rule=LOGON&LOGIN="+login+"&PASSWD="+password);
        HttpURLConnection conn;
        InputStream stream;

        cManager = new CookieManager();
        cManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cManager);

        conn  = (HttpURLConnection)de.openConnection();

        long len = (long)conn.getContentLength();
        if (len != 0){
            stream = new BufferedInputStream(conn.getInputStream());
            int c;
            ByteArrayOutputStream b = new ByteArrayOutputStream();
            while (((c = stream.read())!=-1)){
                b.write(c);
            }
            String code = b.toString("Cp1251");
            if (code.contains("Invalid"))
                return false;

            cStore =  cManager.getCookieStore();

            stream.close();

            Log.d(Global.Debug.LOG_TAG, "SERVICE connect()");
        }

        return true;
    }

    public void getData() throws IOException, ParserConfigurationException, XPathExpressionException {
        Log.d(Global.Debug.LOG_TAG, "SERVICE getData()");
        /*
        if internet is off then this method will return java.net.UnknownHostException
        */
        if(Connect()){
            URL de = new URL("https://de.ifmo.ru/servlet/distributedCDE?Rule=eRegisterGetProtokolAllVar");
            TagNode tagNode = new HtmlCleaner().clean(de, "cp1251");
            org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
            XPath xpath = XPathFactory.newInstance().newXPath();

            try{
                lastSubject = (String) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr[2]//td[3]//text()",doc, javax.xml.xpath.XPathConstants.STRING);
                lastDescription = (String) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr[2]//td[4]//text()",doc, javax.xml.xpath.XPathConstants.STRING);
                lastPoint = (String) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr[2]//td[5]//text()",doc, javax.xml.xpath.XPathConstants.STRING);
            }catch (Exception ex){onDestroy();}

            int fisrtIndex = lastSubject.indexOf('(');
            lastSubject = lastSubject.substring(0,fisrtIndex-1);

            Log.d(Global.Debug.LOG_TAG, "FILE: " + fileSubject + " " + fileDescription + " " + filePoint);
            Log.d(Global.Debug.LOG_TAG, "LAST: " + lastSubject + " " + lastDescription + " " + lastPoint);


            if ( (fileSubject.equals("") || fileDescription.equals("") || filePoint.equals("")) ){
                //file is empty
                if ( !(lastSubject.equals("") || lastDescription.equals("") || lastPoint.equals("")) ){
                    //data is not empty
                    setNotification(lastSubject);
                    updateFile(lastSubject, lastDescription, lastPoint);
                }
            }else{
                //file is not empty
                if ( !(lastSubject.equals("") || lastDescription.equals("") || lastPoint.equals("")) ){
                    //data is not empty
                    if (!lastPoint.equals(filePoint) || !lastSubject.equals(fileSubject) || !lastDescription.equals(fileDescription)){
                        setNotification(lastSubject);
                        updateFile(lastSubject, lastDescription, lastPoint);
                    }
                }
            }
        }
    }

    public void setNotification(String subject){
        Log.d(Global.Debug.LOG_TAG, "SERVICE setNotification()");
        Context context = getApplicationContext();

        Intent notificationIntent = new Intent(context, ActivityPoints.class);
        PendingIntent contentIntent = PendingIntent.getActivity(context,
                0, notificationIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);

        NotificationManager nm = (NotificationManager) context
                .getSystemService(Context.NOTIFICATION_SERVICE);

        Resources res = context.getResources();
        Notification.Builder builder = new Notification.Builder(context);

        builder.setContentIntent(contentIntent)
                .setSmallIcon(R.drawable.ic_launcher_release)
                .setLargeIcon(BitmapFactory.decodeResource(res, R.drawable.ic_launcher_release))
                .setTicker("Проставлены новые баллы!")
                .setWhen(System.currentTimeMillis()) // java.lang.System.currentTimeMillis()
                .setAutoCancel(true)
                .setContentTitle("ЦДО НИУ ИТМО")
                .setContentText(subject); // Текст уведомленимя

        Notification n = builder.getNotification();
        n.defaults = Notification.DEFAULT_VIBRATE | Notification.DEFAULT_SOUND;

        nm.notify(NOTIFY_ID, n);

        // Необходимо еще обновить информацию в файле
    }

    void updateFile(String lastSubject, String lastDescription, String lastPoint){
        sharedPreferences.edit().putString("shared_protocol", lastSubject + "_" + lastDescription + "_" + lastPoint).commit();
    }

}
