package com.qa.utility;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;


import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.qa.baseClass.BaseTest;




public class Utility {

	public static final long wait= 30;
	// TO read EXPECTED RESULTS xml: 1st we created XML file 'expectedResults.xml' > Created This method here to read it via "DocumentBuilderFactory" > Inside BASE CLASS, we will initailize this HASMAP >  use Strings HasMap inside TESTCALSSES test methods.
	
	
	public HashMap<String, String> parseStringXML(InputStream file) throws Exception{ // This method is used to Read XML file "expectedRsults.xml" as INPUTSTREAM which we used to store all our expected results strings and used it in TEST CASES inside Test classes
	       HashMap<String, String> stringMap = new HashMap<String, String>();
	       //Get Document Builder
	       DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
	       DocumentBuilder builder = factory.newDocumentBuilder();
	        
	       //Build Document
	       Document document = builder.parse(file);
	        
	       //Normalize the XML Structure; It's just too important !!
	       document.getDocumentElement().normalize();
	        
	       //Here comes the root node
	       Element root = document.getDocumentElement();
	        
	       //Get all elements
	       NodeList nList = document.getElementsByTagName("string");    // It will look for 'String' tag mentioned in xml of Expected Results, and will load it in NodeList
	    // Iterate through the list of strings in the XML 
	       for (int temp = 0; temp < nList.getLength(); temp++)
	       {
	        Node node = nList.item(temp);
	        if (node.getNodeType() == Node.ELEMENT_NODE)
	        {
	           Element eElement = (Element) node;
	           
	           
	           // *** EXTRA CODE Added by sir to print the KEY-VALUE data of Xml*******
	                     // Get the key and value from the XML element
                     String key = eElement.getAttribute("name");
                     String value = eElement.getTextContent();
                        // Print key-value pairs for debugging
                     System.out.println("Key is: " + key + ", Value is: " + value);
	           	          	           
	           // Store each element key value in map
	           stringMap.put(eElement.getAttribute("name"), eElement.getTextContent());  // Name Attribute is the 'Key' in expected results Xml and Value is the 'text Content'
	        }
	       }
	       return stringMap;  // SO SINCE we will use STRINGS through our automation, we need to initialize HASMAP in BASE CLASS
	    }
	


	
	
        
        public HashMap<String, String> parseStringXML1(FileInputStream fileInputStream) throws Exception { // Refactored code for parseStringXML to use FileInputStream and not InputStream. [This was giving error so updated to "parseStringXML2" which works ] 
            HashMap<String, String> stringMap = new HashMap<>();
     
            // Get Document Builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
     
            // Build Document
            Document document = builder.parse(fileInputStream);
     
            // Normalize the XML Structure
            document.getDocumentElement().normalize();
            // Get root node
          Element root = document.getDocumentElement();
     
            // Get all string elements stored inside the XML file
            NodeList nList = document.getElementsByTagName("string");        //Make sure this matches the corrected XML
            
           // Print the size of NodeList
            System.out.println("Size of NodeList before entering For Loop is: " + nList.getLength());
     
         // Iterate through the list of strings in the XML
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);
                
                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                   
                 // *** EXTRA CODE Added by sir to print the KEY-VALUE data of Xml*******
                    // Get the key and value from the XML element
                String key = eElement.getAttribute("name");
                String value = eElement.getTextContent();
                   
                // Print key-value pairs for debugging
                System.out.println("Key is: " + key + ", Value is: " + value);
                                        
                    // Store each element key-value pair in map
                   stringMap.put(eElement.getAttribute("name"), eElement.getTextContent());
                //stringMap.put(key, value);
                }                                             
            }
            return stringMap;
        }



        public HashMap<String, String> parseStringXML2(FileInputStream fileInputStream) throws Exception {   // Refactored code by ChatGpt to use resource XML file as FileInputStream
            HashMap<String, String> stringMap = new HashMap<>();

            // Get Document Builder
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();

            // Build Document
            Document document = builder.parse(fileInputStream);

            // Normalize the XML Structure
            document.getDocumentElement().normalize();

            // Get all string elements
            NodeList nList = document.getElementsByTagName("String"); // Make sure this matches the correct XML

            // Print the size of NodeList
            System.out.println("Size of NodeList before entering For Loop is: " + nList.getLength());

            // Iterate through the list of strings in the XML
            for (int temp = 0; temp < nList.getLength(); temp++) {
                Node node = nList.item(temp);

                if (node.getNodeType() == Node.ELEMENT_NODE) {
                    Element eElement = (Element) node;
                    
                 // *** EXTRA CODE Added by sir to print the KEY-VALUE data of Xml*******
                    // Get the key and value from the XML element
                    String key = eElement.getAttribute("name");
                    String value = eElement.getTextContent();

                    // Print key-value pairs for debugging
                    System.out.println("Key is: " + key + ", Value is: " + value);

                    // Store each element key-value pair in map
                    stringMap.put(key, value);
                }
            }

            return stringMap;
        }
    

// Method to get the DATE TIME
        
        public String dateTime() {
    		DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");  // It will return date time in this format. Used this method for Saving Screenshots of failed methods.
    		Date date = new Date();
    		return dateFormat.format(date);     
    	}


        public void log(String txt) {             // This method is created to be used while Parallel execution, so that logs of different platforms will be saved in different files.
    		BaseTest base = new BaseTest();
    		String msg = Thread.currentThread().getId() + ":" + base.getPlatform() + ":" + base.getDeviceName() + ":"
    				+ Thread.currentThread().getStackTrace()[2].getClassName() + ":" + txt;
    		
    		System.out.println(msg);
    		
    		String strFile = "PlatformLogs" + File.separator + base.getPlatform() + "_" + base.getDeviceName()   // it will create PlatformLogs folder and will save logs of each device separately in them
    				+ File.separator + base.getDateTime();

    		File logFile = new File(strFile);

    		if (!logFile.exists()) {
    			logFile.mkdirs();
    		}
    		
    		FileWriter fileWriter = null;
    		try {
    			fileWriter = new FileWriter(logFile + File.separator + "log.txt",true);
    		} catch (IOException e) {
    			// TODO Auto-generated catch block
    			e.printStackTrace();
    		}
    	    PrintWriter printWriter = new PrintWriter(fileWriter);
    	    printWriter.println(msg);
    	    printWriter.close();
    	}

    	
        public Logger log()    // Created method for LOGGER 4J2 
        {
        	// Created for Log4J . Here we're creating a log object of type Logger using the get Logger method.
        	
        	return LogManager.getLogger(Thread.currentThread().getStackTrace()[2].getClassName()) ;  // To get the current CLASS name . Now we will update the logging statements to use this method
        	
        }
        
        
    	



}
