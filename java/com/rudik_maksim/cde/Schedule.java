package com.rudik_maksim.cde;

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
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Максим on 30.11.2014.
 */
public class Schedule {
    ArrayList<String> day_week      = new ArrayList<String>();
    ArrayList<String> week_type     = new ArrayList<String>();
    ArrayList<String> time          = new ArrayList<String>();
    ArrayList<String> room          = new ArrayList<String>();
    ArrayList<String> place         = new ArrayList<String>();
    ArrayList<String> title_subject = new ArrayList<String>();
    ArrayList<String> person_title  = new ArrayList<String>();
    ArrayList<String> status        = new ArrayList<String>();

    public void parse(){
        String response = executePost("http://www.ifmo.ru/mobile/schedule.php", "login=ifmo01&pass=01ifmo04&gr=" + Global.CDEData.getRightGroupValue());

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
            }

            Global.CDEData.S_DATA.add(day_week);
            Global.CDEData.S_DATA.add(week_type);
            Global.CDEData.S_DATA.add(time);
            Global.CDEData.S_DATA.add(room);
            Global.CDEData.S_DATA.add(place);
            Global.CDEData.S_DATA.add(title_subject);
            Global.CDEData.S_DATA.add(person_title);
            Global.CDEData.S_DATA.add(status);

            if (Global.CDEData.WeekNumber == 0){
                URL url_parity = new URL("http://ifmo.ru/mobile/");
                TagNode tagNode = new HtmlCleaner().clean(url_parity, "utf8");
                org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
                XPath xpath = XPathFactory.newInstance().newXPath();

                String parity = (String) xpath.evaluate("/html/body/header/nav/div/div/p",doc, XPathConstants.STRING);
                int index = parity.indexOf(".");
                parity = parity.substring(index + 1);
                Global.CDEData.ParityOfWeek = parity;
                Global.CDEData.WeekNumber = Integer.parseInt(parity.substring(0,3).trim());
            }
        }catch (Exception ex){
            Log.d(Global.Debug.LOG_TAG, "error parse scheduleNew: " + ex.toString());
        };
    }

    public void parseGroup(String group){
        String hash = "%D0%B8";

        if (group.contains("и"))
            group = hash + group.substring(1);

        String response = executePost("http://www.ifmo.ru/mobile/schedule.php", "login=ifmo01&pass=01ifmo04&gr=" + group);

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
            }

            Global.CDEData.SG_DATA.add(day_week);
            Global.CDEData.SG_DATA.add(week_type);
            Global.CDEData.SG_DATA.add(time);
            Global.CDEData.SG_DATA.add(room);
            Global.CDEData.SG_DATA.add(place);
            Global.CDEData.SG_DATA.add(title_subject);
            Global.CDEData.SG_DATA.add(person_title);
            Global.CDEData.SG_DATA.add(status);

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
