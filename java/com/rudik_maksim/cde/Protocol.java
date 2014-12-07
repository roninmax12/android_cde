package com.rudik_maksim.cde;

import android.os.AsyncTask;
import android.util.Log;

import org.htmlcleaner.CleanerProperties;
import org.htmlcleaner.DomSerializer;
import org.htmlcleaner.HtmlCleaner;
import org.htmlcleaner.TagNode;
import org.w3c.dom.NodeList;

import java.io.IOException;
import java.net.URL;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

/**
 * Created by Максим on 10.04.14.
 */
public class Protocol extends Connection {
    public void parse() throws IOException, ParserConfigurationException, XPathExpressionException {
        int lengthYears = Global.CDEData.YEARS.size();
        String currentYear = Global.CDEData.YEARS.get(lengthYears-1);
        URL de = new URL(host + "servlet/distributedCDE?Rule=eRegisterGetProtokolVariable&UNIVER=1&APPRENTICESHIP="+currentYear+"&ST_GRP="+Global.CDEData.CUR_GROUP+"&PERSONID="+login+"&PROGRAMID=-&PERIOD=28");

        TagNode tagNode = new HtmlCleaner().clean(de, "cp1251");
        org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);

        XPath xpath = XPathFactory.newInstance().newXPath();

        //SUBJECTS
        NodeList subjects = (NodeList) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr//td[3]//text()",doc, XPathConstants.NODESET);
        for (int i = 0; i < subjects.getLength(); i++){
            Global.CDEData.P_SUBJECTS.add(subjects.item(i).getTextContent().trim());
        }

        //DESCRIPTION
        NodeList description = (NodeList) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr//td[4]//text()",doc, XPathConstants.NODESET);
        for (int i = 0; i < description.getLength(); i++){
            Global.CDEData.P_DESCRIPTION.add(description.item(i).getTextContent().trim());
        }

        //USERPOINTS
        NodeList userPoints = (NodeList) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr//td[5]//text()",doc, XPathConstants.NODESET);
        for (int i = 0; i < userPoints.getLength(); i++){
            Global.CDEData.P_USERPOINTS.add(userPoints.item(i).getTextContent().trim());
        }
    }

    public void parseBackground() throws IOException, ParserConfigurationException, XPathExpressionException{
        URL de = new URL("https://de.ifmo.ru/servlet/distributedCDE?Rule=eRegisterGetProtokolAllVar");
        TagNode tagNode = new HtmlCleaner().clean(de, "cp1251");
        org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);
        XPath xpath = XPathFactory.newInstance().newXPath();

        try{
            Global.CDEData.first_P_subject = (String) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr[2]//td[3]//text()",doc, javax.xml.xpath.XPathConstants.STRING);
            Global.CDEData.first_P_description = (String) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr[2]//td[4]//text()",doc, javax.xml.xpath.XPathConstants.STRING);
            Global.CDEData.first_P_point = (String) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr[2]//td[5]//text()",doc, javax.xml.xpath.XPathConstants.STRING);
        }catch (Exception ex){}

        int fisrtIndex = Global.CDEData.first_P_subject.indexOf('(');
        Global.CDEData.first_P_subject = Global.CDEData.first_P_subject.substring(0,fisrtIndex-1);
    }
}
