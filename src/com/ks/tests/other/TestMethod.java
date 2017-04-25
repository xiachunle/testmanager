package com.ks.tests.other;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Result;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.sax.SAXTransformerFactory;
import javax.xml.transform.sax.TransformerHandler;
import javax.xml.transform.stream.StreamResult;

import org.dom4j.Attribute;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.OutputFormat;
import org.dom4j.io.SAXReader;
import org.dom4j.io.XMLWriter;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.AttributesImpl;

public class TestMethod {

	public static void main(String[] args) throws Exception {
		
		SimpleDateFormat sf=new SimpleDateFormat("yyyyMMdd_HH");
		File file = new File("./src/com/ks/tests/testng.xml");

       SAXReader reader = new SAXReader();
       Document document = reader.read(file);
       Element rootElem = document.getRootElement();

       Element contactElem = rootElem.element("parameter");

       Attribute nameAttr = contactElem.attribute("value");
       nameAttr.setValue("123456");

       OutputFormat format = OutputFormat.createPrettyPrint();
     
       try {
       FileWriter fileWriter = new FileWriter(file.getAbsolutePath());
       XMLWriter writer = new XMLWriter(fileWriter,format);
       writer.write(document);
       writer.close();
       }catch(Exception e){
    	   e.printStackTrace();
       }
	}

}
