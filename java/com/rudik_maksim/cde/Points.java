package com.rudik_maksim.cde;

import android.util.Log;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.CookieHandler;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Максим on 19.03.14.
 */
public class Points extends Connection{
    public void parse(String year) throws IOException, ParserConfigurationException, XPathExpressionException {
        URL de;
        if (year == null)
            de = new URL(host + "servlet/distributedCDE?Rule=eRegister");
        else
            de = new URL(host + "servlet/distributedCDE?Rule=eRegister&APPRENTICESHIP="+year);

        TagNode tagNode = new HtmlCleaner().clean(de, "cp1251");
        org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);

        XPath xpath = XPathFactory.newInstance().newXPath();
        //YEARS
        NodeList years = (NodeList) xpath.evaluate("//table[@class='d_table']//tbody//tr//td//select[@name='APPRENTICESHIP']//option/text()",doc, XPathConstants.NODESET);
        for (int i = 0; i < years.getLength(); i++){
            Global.CDEData.YEARS.add(years.item(i).getTextContent().trim());
        }
        //GROUP
        Global.CDEData.GROUP = (String) xpath.evaluate("//table[@class='d_table']//tbody//tr//td//select[@name='ST_GRP']//option/text()",doc, XPathConstants.STRING);
        if (year == null){
            if (Global.CDEData.CUR_GROUP == null || "".equals(Global.CDEData.CUR_GROUP))
                Global.CDEData.CUR_GROUP = Global.CDEData.GROUP;
        }

        //if (Global.CDEData.CUR_GROUP.contains("и"))
        //    Global.CDEData.CUR_GROUP.replace("и","%D0%B8");

        //URLS

        NodeList urls = (NodeList) xpath.evaluate("//form[@id='FormName']//table[@class='d_table']//tbody//tr//td/a/@onclick",doc, XPathConstants.NODESET);
        for (int i = 0; i < urls.getLength(); i++){
            String val = urls.item(i).getTextContent().trim();

            int firstIndex = val.indexOf("'");
            int lastIndex  = val.lastIndexOf("'");
            val = val.substring(firstIndex+1,lastIndex);

            Global.CDEData.URLS.add(val);
        }

        //SUBJECTS1
        NodeList data = (NodeList) xpath.evaluate("//form[@id='FormName']//table[@class='d_table']//tbody//tr//*"
                + "",doc, XPathConstants.NODESET);

        int iteration = 0;
        int nextSubject = 17;
        int nextPoint = 18;
        int nextExam = 19;
        int nextOffset = 20;
        int nextKursovJob = 21;
        int nextKursovProject = 22;
        int step = 13;
        String ViewControl = "";
        boolean firstSem = true;

        for (int i = 0; i < data.getLength(); i++){
            String str = data.item(i).getTextContent();
            iteration++;

            if (iteration > 16){
                if (str.contains("Семестр")){
                    firstSem = false;
                    nextSubject = iteration + 8;
                    nextPoint = nextSubject + 1;
                    nextExam = nextPoint + 1;
                    nextOffset = nextExam + 1;
                    nextKursovJob = nextOffset + 1;
                    nextKursovProject = nextKursovJob + 1;
                    continue;
                }

                if (iteration == nextSubject){
                    if (firstSem)
                        Global.CDEData.SUBJECTS1.add(str);
                    else
                        Global.CDEData.SUBJECTS2.add(str);
                    nextSubject += step;
                    continue;
                }
                if (iteration == nextPoint){
                    if (firstSem)
                        Global.CDEData.POINTS1.add(str);
                    else
                        Global.CDEData.POINTS2.add(str);
                    nextPoint+=step;
                    continue;
                }
                if (iteration == nextExam){
                    if (!str.equals("")){
                        ViewControl = "Экзамен";
                    }
                    nextExam+=step;
                    continue;
                }
                if (iteration == nextOffset){
                    if (!str.equals("")){
                        if (!"".equals(ViewControl))
                            ViewControl += ", Зачёт";
                        else
                            ViewControl = "Зачёт";
                    }
                    nextOffset+=step;
                    continue;
                }
                if (iteration == nextKursovJob){
                    if (!str.equals("")){
                        if (!"".equals(ViewControl))
                            ViewControl += ", Курсовая работа";
                        else
                            ViewControl = "Курсовая работа";
                    }
                    nextKursovJob+=step;
                    continue;
                }
                if (iteration == nextKursovProject){
                    if (!str.equals("")){
                        if (!"".equals(ViewControl))
                            ViewControl += ", Курсовой проект";
                        else
                            ViewControl = "Курсовой проект";
                    }
                    nextKursovProject+=step;

                    if (firstSem)
                        Global.CDEData.CONTROL1.add(ViewControl);
                    else
                        Global.CDEData.CONTROL2.add(ViewControl);
                    ViewControl = "";

                    continue;
                }
            }
        }
    }
}


