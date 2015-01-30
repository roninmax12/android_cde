package com.rudik_maksim.cde;

import android.util.Log;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Максим on 25.04.14.
 */
public class ScheduleSession {
    /*
    public void parse() throws IOException, ParserConfigurationException, XPathExpressionException {
        String gr = "";

        if (Global.CDEData.CUR_GROUP.contains("и")){
            gr = "i" + Global.CDEData.CUR_GROUP.substring(1);
        }else
            gr = Global.CDEData.CUR_GROUP;

        //DELETE THIS
        gr = "5251";

        URL session = new URL("http://www.ifmo.ru/ru/exam/0/" + gr + "/raspisanie_sessii_" + gr + ".htm");
        TagNode tagNode = new HtmlCleaner().clean(session, "utf8");
        org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
        XPath xpath = XPathFactory.newInstance().newXPath();

        NodeList data = (NodeList) xpath.evaluate("//table[@class='rasp_tabl']//tbody",doc, XPathConstants.NODESET);

        for (int i = 0; i < data.getLength(); i++){
            String str = data.item(i).getTextContent().trim();
            String[] arr = str.split(" ");

            boolean examDateRecorded = false;
            boolean examPlaceRecorded = false;
            boolean subjectRecorded = false;
            boolean subjectStartRecord = false;
            boolean teacherRecorded = false; // BE CAREFULLY, teacher may absent
            boolean consRecorded = false;
            boolean consStartRecord = false;
            boolean skipNextStep = false;
            boolean consTimeRecorded = false;
            boolean consPlaceRecorded = false;
            boolean consPlaceRecord = false;
            boolean checkNextItem = false;
            boolean first_v = true;

            String teacher = "";
            String subject = "";
            String str_exam_date = "";
            String place_str = "";
            String consPlace_str = "";

            int skippedStep = 0;

            for (int j = 0; j < arr.length; j++){
                String s = "" + arr[j].toString().trim();
                int len = 0; len = s.length();

                if (len != 0){
                    if (len == 2){
                        try{
                            int val = Integer.parseInt(s);
                        }catch (Exception ex) {continue;}
                    }
                    // Only needs data

                    if (!examDateRecorded){
                        if (str_exam_date.length() == 0){
                            str_exam_date = s;
                            continue;
                        }else{
                            String date2 = s.substring(0, 3);
                            String time = s.substring(3);

                            str_exam_date += " " + date2;

                            Global.CDEData.SS_EXAM_DATE.add(str_exam_date);
                            Global.CDEData.SS_EXAM_TIME.add(time);

                            examDateRecorded = true;

                            continue;
                        }
                    }

                    if (!examPlaceRecorded){
                        place_str = s;
                        //Global.CDEData.SS_EXAM_PLACE.add(s);
                        examPlaceRecorded = true;
                        checkNextItem = true;
                        //skipNextStep = true;
                        continue;
                    }

                    if (checkNextItem){
                        if (place_str.equals(s)){
                            Global.CDEData.SS_EXAM_PLACE.add(place_str);
                            checkNextItem = false;
                            continue;
                        }
                        else{
                            place_str += " " + s;
                            Global.CDEData.SS_EXAM_PLACE.add(place_str);
                            checkNextItem = false;
                            skipNextStep = true;
                            continue;
                        }
                    }

                    if (skipNextStep){
                        skippedStep++;

                        if (skippedStep == 1)
                            continue;

                        if (skippedStep == 2){
                            skipNextStep = false;
                            skippedStep = 0;
                            continue;
                        }
                    }

                    if (!subjectRecorded){
                        char first = s.charAt(0);

                        if (subjectStartRecord){
                            if (Character.isUpperCase(first)){
                                // Фамилия преподавателя или слово "Консультация"
                                Global.CDEData.SS_SUBJECTS.add(subject);
                                subjectRecorded = true;

                                if (s.contains("Консультация")){
                                    Global.CDEData.SS_TEACHERS.add("-");
                                    teacherRecorded = true;
                                    continue;
                                }

                            }else{
                                subject += " " + s;
                                continue;
                            }
                        }else{
                            // Первое вхождение
                            subject = s;
                            subjectStartRecord = true;
                            continue;
                        }
                    }

                    if (!teacherRecorded){
                        if (!s.contains("Консультация")){
                            if (teacher.length() == 0){
                                teacher = s;
                                continue;
                            }else{
                                teacher += " " + s;
                                continue;
                            }
                        }else{
                            Global.CDEData.SS_TEACHERS.add(teacher);
                            teacherRecorded = true;
                            continue;
                        }
                    }

                    if (!consRecorded){
                        if (s.equals("в") && first_v){
                            Global.CDEData.SS_CONS_DATE.add("-");
                            consRecorded = true;
                            continue;
                        }

                        if (!consStartRecord){
                            Global.CDEData.SS_CONS_DATE.add(s);
                            consStartRecord = true;
                            first_v = false;
                            continue;
                        }else{
                            if (s.contains("в")){
                                consRecorded = true;
                                continue;
                            }else{
                                // Time is absent
                                consRecorded = true;
                                consTimeRecorded = true;
                                Global.CDEData.SS_CONS_TIME.add("-");
                                continue;
                            }
                        }
                    }

                    if (!consTimeRecorded) {
                        if (s.contains("Место")){
                            Global.CDEData.SS_CONS_TIME.add("-");
                            consTimeRecorded = true;

                            if (j == arr.length - 1){
                                Global.CDEData.SS_CONS_PLACE.add("-");
                                break;
                            }
                            continue;
                        }

                        Global.CDEData.SS_CONS_TIME.add(s);
                        consTimeRecorded = true;
                        continue;
                    }

                    if (!consPlaceRecorded){
                        consPlaceRecorded = true;

                        if (s.contains("Место")){
                            consPlaceRecord = true;
                            continue;
                        }else{
                            Global.CDEData.SS_CONS_PLACE.add("-");
                            break;
                        }
                    }

                    if (consPlaceRecord){
                        if (consPlace_str.length() > 0)
                            consPlace_str += " " + s;
                        else
                            consPlace_str = s;

                        if (j == arr.length - 1)
                            Global.CDEData.SS_CONS_PLACE.add(consPlace_str);
                    }

                }else
                    continue;
            }
        }
    }*/

    public void parseNew() throws IOException, ParserConfigurationException, XPathExpressionException {
        String gr = "";

        if (Global.CDEData.CUR_GROUP.contains("и")){
            gr = "i" + Global.CDEData.CUR_GROUP.substring(1);
        }else
            gr = Global.CDEData.CUR_GROUP;

        //DELETE THIS
        //gr = "4131";

        URL session = new URL("http://www.ifmo.ru/ru/exam/0/" + gr + "/raspisanie_sessii_" + gr + ".htm");
        TagNode tagNode = new HtmlCleaner().clean(session, "utf8");
        org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
        XPath xpath = XPathFactory.newInstance().newXPath();

        NodeList subjects = (NodeList) xpath.evaluate("//table[@class='rasp_tabl']//tbody//td[@class=\"lesson\"]//dl//dd",doc, XPathConstants.NODESET);

        for (int i = 0; i < subjects.getLength(); i++){
            String subject_name = subjects.item(i).getTextContent().trim();

            String date = (String) xpath.evaluate("//div["+ (i + 1) +"]/table[@class='rasp_tabl']//tbody//tr//th//span//text()", doc, XPathConstants.STRING);
            String time = (String) xpath.evaluate("//div["+ (i + 1) +"]/table[@class='rasp_tabl']//tbody//tr//td[@class=\"time\"]//span//text()", doc, XPathConstants.STRING);
            String room = (String) xpath.evaluate("//div["+ (i + 1) +"]/table[@class='rasp_tabl']//tbody//tr//td[@class=\"room\"]//span//text()", doc, XPathConstants.STRING);
            String teacher = (String) xpath.evaluate("//div["+ (i + 1) +"]/table[@class='rasp_tabl']//tbody//tr//td[@class=\"lesson\"]//dl//dt//b//text()", doc, XPathConstants.STRING);
            String cons_str = (String) xpath.evaluate("//div["+ (i + 1) +"]/table[@class='rasp_tabl']//tbody//tr//td[@class=\"lesson\"]//dl//dt[2]//text()", doc, XPathConstants.STRING);

            if (subject_name.length() < 2) subject_name = "-";
            if (date.length() < 2) date = "-";
            if (time.length() < 2) time = "-";
            if (room.length() < 2) room = "-";
            if (teacher.length() < 2) teacher = "-";

            String cons_date = "-", cons_time = "-", cons_place = "-";

            if (cons_str.length() > 2){
                String[] arr = cons_str.split(" ");

                boolean cons_date_exist = false;
                boolean cons_time_exist = false;
                boolean cons_place_exist = false;

                for (int j = 0; j < arr.length; j++){
                    if (arr[j].contains("Консультация")){
                        cons_date_exist = true;
                        continue;
                    }

                    if (cons_date_exist){
                        cons_date = arr[j];
                        cons_date_exist = false;
                        continue;
                    }

                    if (arr[j].contains("в")){
                        cons_time_exist = true;
                        continue;
                    }

                    if (cons_time_exist){
                        cons_time = arr[j];
                        cons_time_exist = false;
                        continue;
                    }

                    if (arr[j].contains("Место")){
                        cons_place_exist = true;
                        continue;
                    }

                    if (cons_place_exist){
                        cons_place = arr[j];

                        if (arr.length - j > 1)
                            cons_place += " " + arr[j+1];

                        cons_place_exist = false;
                    }
                }
            }

            Global.CDEData.SS_SUBJECTS.add(subject_name);
            Global.CDEData.SS_TEACHERS.add(teacher);

            Global.CDEData.SS_EXAM_DATE.add(date);
            Global.CDEData.SS_EXAM_TIME.add(time);
            Global.CDEData.SS_EXAM_PLACE.add(room);

            Global.CDEData.SS_CONS_DATE.add(cons_date);
            Global.CDEData.SS_CONS_TIME.add(cons_time);
            Global.CDEData.SS_CONS_PLACE.add(cons_place);
        }
    }
}
