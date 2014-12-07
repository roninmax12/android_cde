package com.rudik_maksim.cde.services;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;

import com.rudik_maksim.cde.ActivityPoints;
import com.rudik_maksim.cde.R;
import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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

/**
 * Created by Максим on 30.04.14.
 */
public class NewPointsNotificationService extends Service {
    final int repeatInterval = 900000; //ms (60000ms = 1min)
    final int NOTIFY_ID = 101;

    CookieManager cManager;
    CookieStore cStore;

    String lastSubject = "", lastDescription = "", lastPoint = "";
    String fileSubject = "", fileDescription = "", filePoint = "";
    String login, password, file_protocol;

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try{
                    Thread.sleep(repeatInterval);
                    Intent serviceIntent = new Intent(getApplicationContext(), NewPointsNotificationService.class);
                    serviceIntent.putExtra("login", login);
                    serviceIntent.putExtra("password", password);
                    serviceIntent.putExtra("file_protocol", file_protocol);
                    startService(serviceIntent);
                }catch (Exception ex){onDestroy();}
            }
        }).start();
    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        try{
            login         = intent.getStringExtra("login");
            password      = intent.getStringExtra("password");
            file_protocol = intent.getStringExtra("file_protocol");

            new AsyncGetData().execute();
        }catch (Exception ex){}
        return super.onStartCommand(intent, flags, startId);
    }

    class AsyncGetData extends AsyncTask<Void,Void,Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            try{
                getData();
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
            }

            return true;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    public void getData() throws IOException, ParserConfigurationException, XPathExpressionException {
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

            //Read data from file_protocol
            try{
                InputStream inStream = openFileInput(file_protocol);

                if (inStream != null) {
                    InputStreamReader tmp = new InputStreamReader(inStream);
                    BufferedReader reader = new BufferedReader(tmp);
                    String s;
                    int i = 0;

                    while ((s = reader.readLine()) != null) {
                        switch (i){
                            case 0:
                                fileSubject     = s.toString(); i++; break;
                            case 1:
                                fileDescription = s.toString(); i++; break;
                            case 2:
                                filePoint       = s.toString(); i++; break;
                        }
                    }
                }
            }catch(Exception ex){onDestroy();}


            if ( (fileSubject.equals("") || fileDescription.equals("") || filePoint.equals("")) ){
                //file is empty
                if ( !(lastSubject.equals("") || lastDescription.equals("") || lastPoint.equals("")) ){
                    //data is not empty
                    setNotification(lastSubject);
                }
            }else{
                //file is not empty
                if ( !(lastSubject.equals("") || lastDescription.equals("") || lastPoint.equals("")) ){
                    //data is not empty
                    if (!lastPoint.equals(filePoint) || !lastSubject.equals(fileSubject) || !lastDescription.equals(fileDescription)){
                        setNotification(lastSubject);
                    }
                }
            }

            onDestroy();
        }
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    public void setNotification(String subject){
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
    }
}
