package com.rudik_maksim.cde;

import android.os.AsyncTask;

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
 * Created by Максим on 12.04.14.
 */
public class Rating extends Connection {
    public void parse() throws IOException, ParserConfigurationException, XPathExpressionException {
        URL de = new URL(host + "servlet/distributedCDE?Rule=REP_SHOWREPORTPARAMFORM&REP_ID=1441");

        TagNode tagNode = new HtmlCleaner().clean(de, "cp1251");
        org.w3c.dom.Document doc = new DomSerializer(new CleanerProperties()).createDOM(tagNode);

        XPath xpath = XPathFactory.newInstance().newXPath();

        //FACULTY
        NodeList f = (NodeList) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr//td[1]//text()",doc, XPathConstants.NODESET);
        for (int i = 0; i < f.getLength(); i++){
            Global.CDEData.R_FACULTY.add(f.item(i).getTextContent().trim());
        }

        //COURSE
        NodeList c = (NodeList) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr//td[2]//text()",doc, XPathConstants.NODESET);
        for (int i = 0; i < c.getLength(); i++){
            Global.CDEData.R_COURSE.add(c.item(i).getTextContent().trim());
        }

        //POSITION
        NodeList p = (NodeList) xpath.evaluate("//div[@class='d_text']//table[@class='d_table']//tbody//tr//td[3]//text()",doc, XPathConstants.NODESET);
        for (int i = 0; i < p.getLength(); i++){
            Global.CDEData.R_POSITION.add(p.item(i).getTextContent().trim());
        }
    }
}