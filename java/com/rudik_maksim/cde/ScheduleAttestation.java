package com.rudik_maksim.cde;

import android.util.Log;
import android.util.Xml;

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
import java.util.Calendar;
import java.util.HashMap;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Максим on 30.11.2014.
 */
public class ScheduleAttestation {
    HashMap<String, ArrayList<String>> weeks = new HashMap<String, ArrayList<String>>();
    ArrayList<String> test = new ArrayList<String>();

    public void parse(){
        executePost("http://de.ifmo.ru/m/index.php?page=schedule",
                                      "semester=" + getSemesterForGroup() +
                                      "&title=" + Global.CDEData.CUR_GROUP +
                                      "&view=groupsDisp" +
                                      "&year=" + getYear() +
                                      "&year_desystem=" + getYearDeSystem() +
                                      "&semester_desystem=" + getSemesterDeSystem()
        );

        try{
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
        }catch (Exception ex){}
    }

    String getSemesterDeSystem(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        if (month >= 2 && month <= 8){
            // even semester
            return "2,4,6,8,10";
        }else{
            // oven semester
            return "1,3,5,7,9,11";
        }
    }

    String getYearDeSystem(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int currentYear  = calendar.get(Calendar.YEAR);

        if (month >= 2 && month <= 8){
            int prevYear = currentYear - 1;
            return prevYear + "/" + currentYear;
        }else{
            int nextYear = currentYear + 1;
            return currentYear + "/" + nextYear;
        }
    }

    String getYear(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);
        int year  = calendar.get(Calendar.YEAR);

        if (month >= 2 && month <= 8){
            // even semester
            return Integer.toString(year);
        }else{
            if (month == 1){
                year = year - 1;
                return Integer.toString(year);
            }else{
                return Integer.toString(year);
            }
        }
    }

    String getSemesterForGroup(){
        Calendar calendar = Calendar.getInstance();
        int month = calendar.get(Calendar.MONTH);

        if (month >= 2 && month <= 8){
            // even semester
            return "1";
        }else{
            // oven semester
            return "2";
        }
    }

    String currentSubject = "";
    String prevSubject = "";
    String currentWeek = "";
    String prevWeek = "";

    boolean insertToGlobalHashMap = false;
    boolean insertToHashMap = false;
    boolean createNewWeeks = false;

    int lastWeekIndex = 0;


    private void executePost(String targetURL, String urlParameters){
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
            connection.setRequestProperty("Content-Language", "ru-RU");

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

            try{
                TagNode tagNode = new HtmlCleaner().clean(is, "cp1251");
                org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
                XPath xpath = XPathFactory.newInstance().newXPath();

                NodeList all = (NodeList) xpath.evaluate("html/body/div[2]/div[1]/ul/li//text()",doc, XPathConstants.NODESET);
                for (int i = 0; i < all.getLength(); i++){
                    String str = all.item(i).getTextContent().trim();

                    if (!str.contains("&quot")){
                        if (insertToGlobalHashMap){
                            Global.CDEData.SA_DATA.put(prevSubject, weeks);
                            insertToGlobalHashMap = false;
                            createNewWeeks = true;
                        }

                        if (str.contains("неделя")){

                            if (currentWeek.length() > 1){
                                insertToHashMap = true;
                                prevWeek = currentWeek;
                            }

                            if (insertToHashMap){
                                weeks.put(prevWeek, test);
                                insertToHashMap = false;
                                test = new ArrayList<String>();
                            }

                            if (createNewWeeks){
                                weeks = new HashMap<String, ArrayList<String>>();
                                createNewWeeks = false;
                            }

                            currentWeek = str;
                            continue;
                        }

                        if (str.contains("Тест")){
                            test.add(str);
                            continue;
                        }

                        //New Subject
                        if (currentSubject.length() > 1){
                            insertToGlobalHashMap = true;
                            prevSubject = currentSubject;
                        }

                        currentSubject = str;
                    }
                }

                weeks.put(currentWeek, test);
                Global.CDEData.SA_DATA.put(currentSubject, weeks);

            }catch (Exception ex){Log.d(Global.Debug.LOG_TAG, ex.toString());}

            //return response.toString();

        } catch (Exception e) {

            e.printStackTrace();
            return;

        } finally {

            if(connection != null) {
                connection.disconnect();
            }
        }
    }
}
