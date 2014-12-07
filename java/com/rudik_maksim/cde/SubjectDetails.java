package com.rudik_maksim.cde;

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
 * Created by Максим on 12.06.14.
 */
public class SubjectDetails extends Connection{
    public void parse(String SYU_ID) throws IOException, ParserConfigurationException, XPathExpressionException {
        if (Global.CDEData.SELECTED_YEAR == null){
            Global.CDEData.SELECTED_YEAR = Global.CDEData.YEARS.get(Global.CDEData.YEARS.size()-1);
        }

        URL de = new URL("https://de.ifmo.ru/servlet/distributedCDE?Rule=eRegisterStudentProgram&UNIVER=1&APPRENTICESHIP="+Global.CDEData.SELECTED_YEAR+"&ST_GRP="+Global.CDEData.GROUP+"&SYU_ID="+SYU_ID);

        TagNode tagNode = new HtmlCleaner().clean(de, "cp1251");
        org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);

        XPath xpath = XPathFactory.newInstance().newXPath();

        NodeList data = (NodeList) xpath.evaluate("//div[@class='d_text'][3]//table[@class='d_table']//tbody//tr//td"
                + "",doc, XPathConstants.NODESET);

        int nextNumber = 0;
        int nextTitle  = 1;
        int nextMaxPoints = 2;
        int nextDate      = 5;
        int nextRate      = 6;
        int step          = 9;

        String tempMaxPoints = "";

        for (int i = 0; i < data.getLength()-3; i++){
            String s = data.item(i).getTextContent().trim();

            if (i == nextNumber){
                Global.CDEData.SD_NUMBER.add(s);
                nextNumber += step;
                continue;
            }

            if (i == nextTitle){
                Global.CDEData.SD_TITLE.add(s);
                nextTitle += step;
                continue;
            }

            if (i == nextMaxPoints){
                tempMaxPoints = s;
                nextMaxPoints += step;
                continue;
            }

            if (i == nextDate){
                if (!"".equals(s))
                    Global.CDEData.SD_DATE.add(s);
                else
                    Global.CDEData.SD_DATE.add("-");
                nextDate += step;
                continue;
            }

            if (i == nextRate){
                if (!"".equals(tempMaxPoints))
                    Global.CDEData.SD_RATE.add(s + " из " + tempMaxPoints);
                else
                    Global.CDEData.SD_RATE.add(s);

                nextRate += step;
                continue;
            }
        }
        Global.CDEData.CURRENT_RATE = data.item(data.getLength()-2).getTextContent().trim();
    }
}
