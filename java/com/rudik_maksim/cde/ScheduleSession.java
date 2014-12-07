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
    public void parse() throws IOException, ParserConfigurationException, XPathExpressionException {
        //URL session = new URL("http://www.ifmo.ru/exam_search/1/"+Global.CDEData.CUR_GROUP+"/exam_gr_"+Global.CDEData.CUR_GROUP+".htm");
        URL session = new URL("http://www.ifmo.ru/ru/exam/0/" + Global.CDEData.CUR_GROUP + "/raspisanie_sessii_" + Global.CDEData.CUR_GROUP + ".htm");
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

            String teacher = "";
            String subject = "";
            String str_exam_date = "";

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
                        Global.CDEData.SS_EXAM_PLACE.add(s);
                        examPlaceRecorded = true;
                        skipNextStep = true;
                        continue;
                    }

                    if (skipNextStep){
                        skipNextStep = false;
                        continue;
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
                        if (!consStartRecord){
                            Global.CDEData.SS_CONS_DATE.add(s);
                            consStartRecord = true;
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
                        Global.CDEData.SS_CONS_PLACE.add(s);
                        break;
                    }

                }else
                    continue;
            }
        }
    }
}
