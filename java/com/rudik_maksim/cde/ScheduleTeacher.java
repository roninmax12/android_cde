package com.rudik_maksim.cde;

import android.content.Context;
import android.content.res.AssetManager;
import android.util.Log;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.w3c.dom.NodeList;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Максим on 13.11.2014.
 */
public class ScheduleTeacher {
    ArrayList<String> day_week      = new ArrayList<String>();
    ArrayList<String> week_type     = new ArrayList<String>();
    ArrayList<String> time          = new ArrayList<String>();
    ArrayList<String> room          = new ArrayList<String>();
    ArrayList<String> place         = new ArrayList<String>();
    ArrayList<String> title_subject = new ArrayList<String>();
    ArrayList<String> person_title  = new ArrayList<String>();
    ArrayList<String> status        = new ArrayList<String>();
    ArrayList<String> gr            = new ArrayList<String>();

    private HashMap<String, String> teacherIdHashMap = new HashMap<String, String>();

    public ScheduleTeacher(Context ctx){
        AssetManager assetManager = ctx.getAssets();

        try {
            InputStreamReader istream = new InputStreamReader(assetManager.open("hashmap.teacher"));
            BufferedReader in = new BufferedReader(istream);

            String str = "";
            while ((str = in.readLine()) != null) {
                String[] arrKeyValue = str.split(":"); // [0] - key, [1] - value
                teacherIdHashMap.put(arrKeyValue[0], arrKeyValue[1]);
            }

            in.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String getTeacherId(String fio){
        if (teacherIdHashMap.containsKey(fio)){
            return teacherIdHashMap.get(fio);
        }else
            return "";
    }

    public ArrayList<String> getSimilarTeachers(String s){
        ArrayList<String> similar = new ArrayList<String>();
        s = s.toLowerCase();

        for (String key: teacherIdHashMap.keySet()){
            String lowerKey = key.toLowerCase();

            if (lowerKey.contains(s))
                similar.add(key);
        }

        return similar;
    }

    public ArrayList<String> getAllTeachers(){
        ArrayList<String> keys = new ArrayList<String>();

        for (String key: teacherIdHashMap.keySet()){
            keys.add(key);
        }

        return keys;
    }

    public int getCountRecords(){
        return teacherIdHashMap.size();
    }


    public void parse(String pid){
        String response = executePost("http://www.ifmo.ru/mobile/schedule_pid_gr.php", "login=ifmo01&pass=01ifmo04&pid=" + pid);

        JSONParser parser = new JSONParser();
        try{
            Object obj = parser.parse(response);
            JSONArray jsonArray = (JSONArray) obj;

            for (int i = 0; i < jsonArray.size(); i++){
                JSONObject jsonObject = (JSONObject) jsonArray.get(i);

                day_week.add(jsonObject.get("day_week").toString());
                week_type.add(jsonObject.get("week_type").toString());
                time.add(jsonObject.get("time").toString());
                room.add(jsonObject.get("room").toString());
                place.add(jsonObject.get("place").toString());
                title_subject.add(jsonObject.get("title_subject").toString());
                person_title.add(jsonObject.get("person_title").toString());
                status.add(jsonObject.get("status").toString());
                gr.add(jsonObject.get("gr").toString());
            }

            Global.CDEData.ST_DATA.add(day_week);
            Global.CDEData.ST_DATA.add(week_type);
            Global.CDEData.ST_DATA.add(time);
            Global.CDEData.ST_DATA.add(room);
            Global.CDEData.ST_DATA.add(place);
            Global.CDEData.ST_DATA.add(title_subject);
            Global.CDEData.ST_DATA.add(person_title);
            Global.CDEData.ST_DATA.add(status);
            Global.CDEData.ST_DATA.add(gr);

        }catch (Exception ex){
            Log.d(Global.Debug.LOG_TAG, "error parse scheduleNew: " + ex.toString());
        };
    }

    public String executePost(String targetURL, String urlParameters){
        URL url;
        HttpURLConnection connection = null;
        try {
            //Create connection
            url = new URL(targetURL);
            connection = (HttpURLConnection)url.openConnection();
            connection.setRequestMethod("POST");
            connection.setRequestProperty("Content-Type",
                    "application/x-www-form-urlencoded");

            connection.setRequestProperty("Content-Length", "" +
                    Integer.toString(urlParameters.getBytes().length));
            connection.setRequestProperty("Content-Language", "en-US");

            connection.setUseCaches (false);
            connection.setDoInput(true);
            connection.setDoOutput(true);

            //Send request
            DataOutputStream wr = new DataOutputStream (
                    connection.getOutputStream ());
            wr.writeBytes (urlParameters);
            wr.flush ();
            wr.close ();

            //Get Response
            InputStream is = connection.getInputStream();
            BufferedReader rd = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer response = new StringBuffer();
            while((line = rd.readLine()) != null) {
                response.append(line);
                response.append('\r');
            }
            rd.close();
            return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return null;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
