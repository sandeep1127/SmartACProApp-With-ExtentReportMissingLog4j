package com.qa.listeners;

import com.aventstack.extentreports.MediaEntityBuilder; 
import com.aventstack.extentreports.Status;
import com.qa.baseClass.BaseTest;
import com.qa.extentReports.ExtentReport;
import com.qa.utility.Utility;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.OutputType;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;
import org.testng.Reporter;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;



public class TestListener implements ITestListener{// We're creating this LISTENER Class and implementing ITestListener so that we don't have use TRY/CATCH block in every test case to get the exception detail if one occurs.Annotation @listeneer will be used to use this

	// READ the method "invalidPassword" in green code  inside LOGINPAGE test class to understand this java class
	
	Utility utils= new Utility();  // using it in below method to fetch the Logs method to be used in test failure method for parallel execution.
	
	
	
	// * * * * * * * We are implementing this Method so that we get to see the details of exceptions in "console" whenever any test case gives exceptions.* * * * * * *
	
	// To work it, now we'll need to call this LISTENER for testNG xml . Doing it from testNG at Suite level will make this work for all TEST CLASSES automatically other you will need to call it in every class separarely which is not good approach.
 public void onTestFailure(ITestResult result) {   // ITestResult Class > This method will print the Exception details if any test method fails
	 if (result.getThrowable()!=null) {
		 StringWriter sw = new StringWriter();   // If this line was not added, TestNG in case of exception will simply tell that there is error but it won't display detail of exception in results. Adding this line would do it
		 PrintWriter pw = new PrintWriter(sw);
		 result.getThrowable().printStackTrace(pw);  //  using "getThrowable" method we're reading the exception from our test method and printing it to 'PrintWriter'
		// System.out.println(sw.toString()); // Converting the details of exception into String format ie readable format which will be shown in "rests of running suite" tab which from testNG
	    utils.log(sw.toString());               // updated above line for implementing PARALLEL Execution to print the Logs  
	 }
      
	 
	 
	 
// * * * * * * *  Below code is added to take SCREENSHOT if any TESTCASE FAILS. SO we included code in this method itself  * * * * * * * * * 
	 BaseTest base = new BaseTest();  // created object for BasTest class . Now got o BaseTest class and create a method to get the driver.
	 File file = base.getDriver().getScreenshotAs(OutputType.FILE);   // we used the method to get the screenshot and output type is File. We collect it in "File Object"
	 
	 // Below line code is to used for embedding failed SS into Extent Report. Here Base64 method is used to display failed screenshots SS into Extent Report. Here we're converting File Object into Base 64 String.
	 byte[] encoded = null;  // its an array of byte
		try {
			encoded = Base64.encodeBase64(FileUtils.readFileToByteArray(file));  // passing 'file' object to it and reading it byte by byte and encoding it base64 and putting in variable "encoded"
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	 
	 
	 
	  
	 // Below code we are doing to shift our Failed test cases Screenshots to our desired Directory:
	 Map<String, String> params = new HashMap<String, String>();               /// created a hasMap to store the parameters of XML 
	 params = result.getTestContext().getCurrentXmlTest().getAllParameters();  // here we are fetching the testNG global paramters  as we need them for our directory so we use "Result" argument to fetch them which return them in hasMap so we created HashMap to store them
	 
	 /*Below is the directory structure we are going to create for storing the failed screenshots which is being stored in "failedImagePath" .
	                                                             Structure is:  \Screenshots    {This Folder will get automatically created which will contain all below folders }
	 
	  																			\<platformName>_<platormVersion>_<deviceName>   {helps capturing ss in case of parellel excecution. It will create separate device which you'll use to test}
	                                                              				\<datetime   {ensures SS are not overwritten so each time test runs, auto folder will get created - we can also use build number CI/CD}
	                                                              				\<testClass>   {lists all method as per Test classeswhich failed}
	                                                              				\methodName.png {helps identify the failed method}
	 
	 */
	String failedImagePath = "FailedCasesScreenShots" + File.separator + params.get("platformName") + "_" + params.get("deviceName") + File.separator + base.getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName() + File.separator + result.getName() + ".png" ;  // This is relative path
	 
	 
	String completeFailedcaseImagePath = System.getProperty("user.dir") + File.separator + failedImagePath;	 // we used to get the complete path from root directory	adn we are using it in our HTML report location to add the failed cases ss 
	 // Below code is used to copy Screenshot File.
	 try {
		FileUtils.copyFile(file, new File (failedImagePath));      // Now we use FileUtils of which we added dependency to copy the File .  Source will be our File and destination file will be our Image name object thaty used 'new' keyword . This will by default create the screnshot File in the Root Directory which is "SmartACProAPP" which we handle above
	   Reporter.log("This is the Screenshot of failed test cases"); // This code line will simply add the failed test cases screenshot to the default generated HTML Report. If we didn't add this, it would show the console error in reports.
	   Reporter.log("<a href='"+ completeFailedcaseImagePath + "'> <img src='"+ completeFailedcaseImagePath + "' height='100' width='100'/> </a>");  // we gave the image  reference and image source to TestNg Report
	 } catch (IOException e) {
		
		e.printStackTrace();
	 }
		//ExtentReport.getTest().log(Status.FAIL, "Test Failed");   // Extent report fetching the status and passing it into report if method fails. below line covers this
		
       // FilePath Method to fetch the Failed testcase SS from Relative system Path and show in Extent Report. But if you want to use base64 method where SS gets embedded in report itself, we'll 1st need to convert File object into Base64 String. Base64  class is from Apacahe common codec lib
	 ExtentReport.getTest().fail("Test Failed", MediaEntityBuilder.createScreenCaptureFromPath(completeFailedcaseImagePath).build()); // This line will capture the failed test case and will attach the screenshot to the EXTENT REPORT when case fails. So we don't need above line now . This will simply add the Path of screenshot and not paste the ss itself.
		
	 
	 
	 // This Base64 method used to embedded fauiled case SS into report itself. Unlike "FilePath" method, it won't save it in system so will always be visible in report in any system.
	 ExtentReport.getTest().fail("Test Failed",
				MediaEntityBuilder.createScreenCaptureFromBase64String(new String(encoded, StandardCharsets.US_ASCII)).build());  // we are converting Base into String [For Failed case SS into Extent report]
		ExtentReport.getTest().fail(result.getThrowable());  // send the complete exception stack tray occurred while case failed to the  EXTENT REPORT using getThrowable() method.
	 	 
	}           
	 
 
	 
	 
	 
	 
	 
	 
	    
	   
	    
	    public void onTestStart(ITestResult result) {   //  We ca't use this method as this executes before any test method executes.       
	        BaseTest base1 = new BaseTest();
	        ExtentReport.startTest(result.getName(), result.getMethod().getDescription())  // Ensure this returns an object
	            .assignCategory(base1.getPlatform() + "_" + base1.getDeviceName()) 
	            .assignAuthor("SandeepChoudhary");		
	    }  
	    

		
		public void onTestSuccess(ITestResult result) {            // This method is used to 
			ExtentReport.getTest().log(Status.PASS, "Test Passed");
			
		}

		
		public void onTestSkipped(ITestResult result) {
			ExtentReport.getTest().log(Status.SKIP, "Test Skipped");
			
		}

		
		public void onTestFailedButWithinSuccessPercentage(ITestResult result) {
			// TODO Auto-generated method stub
			
		}

		
		public void onStart(ITestContext context) {   // We can't use this method as this executes before any test class executes.
			// TODO Auto-generated method stub
			
		}

		
		public void onFinish(ITestContext context) {   //This will execute after all the test cases are executed.
			
			ExtentReport.getReporter().flush();		 // This will flush the Reporter after all cases are run. This will simply write the report of all test cases in the File
		}
	 
	 
	 
 }
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
	 
 
	




















