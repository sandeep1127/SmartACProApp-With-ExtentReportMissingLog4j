package com.qa.extentReports;
import java.util.HashMap;
import java.util.Map;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentSparkReporter;
import com.aventstack.extentreports.reporter.configuration.Theme;
public class ExtentReport {   // This is created to implement EXTENT REPORTS. [we added dependency for it]

	
	
	
	
	 // STEP 1 : Created this Class and created these method
	 // STEP 2 : We'll use 'ITestListener' Class so we'll call these methods there. Also, we can use @before/@After method instead of ITestListener
	
        static ExtentReports  extent;
        final static String filePath = "ExtentReport SmartACProApp.html";
        static Map<Integer, ExtentTest> extentTestMap = new HashMap();
        
        public synchronized static ExtentReports  getReporter() {  // we're using synchronized as we need to supoort parallel execution
            if (extent == null) {
            	ExtentSparkReporter spark = new ExtentSparkReporter(filePath);
                spark.config().setDocumentTitle("Appium SmartACProApp"); // setting the title of Extent Report Document
                spark.config().setReportName("MyProApp");
                spark.config().setTheme(Theme.DARK);
                extent = new ExtentReports();               
                extent.attachReporter(spark);
            }
            
            return extent;
        }
	
	
	
	
        public static synchronized ExtentTest getTest() {     // here we're getting test object here 
            return (ExtentTest) extentTestMap.get((int) (long) (Thread.currentThread().getId()));   // we're reading hashMap and we're retriving the test object for the current thread
        }

        public static synchronized ExtentTest startTest(String testName, String desc) {     // Its starts our test case and takes testcase name and description as argument
            ExtentTest test = getReporter().createTest(testName, desc);       
            extentTestMap.put((int) (long) (Thread.currentThread().getId()), test);
            return test;
        }
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}






