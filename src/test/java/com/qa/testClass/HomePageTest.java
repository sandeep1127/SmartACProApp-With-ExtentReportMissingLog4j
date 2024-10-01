package com.qa.testClass;
import org.testng.annotations.Test;
import com.qa.baseClass.BaseTest;
import com.qa.pages.HomePage;
import com.qa.pages.LoginPage;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeClass;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
public class HomePageTest extends BaseTest { // 1st -> We need to do this to initialize our Driver. Doing this, when TestNG will execute LoginPage Test class it will 1st go to BASE class and will run @beforeMethod and will initialize driver
	  
	//Initialize Login Page and then Homeapge page at class level
LoginPage loginPage;
HomePage homePage;
	
// below 2 variables created for reading JSON file which is created for test data 
 //InputStream datais;	  // We created this to use JSON Test DATA file we created in src/test/Resources . This is called adding ABSTRACTION layer to our Test Data
JSONObject loginUsers;	 // we're creating JSON Object . It will contain all the Test Data of JSON.
	
	
	
  @BeforeMethod
  public void beforeMethod(Method m) {                          // To Print the name of method running in the CONSOLE. So for this we're using METHOD Class
	  loginPage = new LoginPage();      
	  System.out.println("\n" + "******* Starting test:" + m.getName() + "*******" + "\n");     // This method "GetName()" will give the name of the testMethod which is currently executing
	  
  }

  @AfterMethod
  public void afterMethod() {
  }

  @BeforeClass
  public void beforeClass() throws IOException {
	  
	  InputStream datais= null;	  // We created this to use JSON Test DATA file we created in src/test/Resources . This is called adding ABSTRACTION layer to our Test Data . We moved this object from Global level to Method level for Thread Safety for parallel execution.
	  
	  // Purpose of below code to read the JSON login test data from "json File" created in 'TestData' folder . 
	 try {
		 String dataFileName = "TestData/loginUsers.json" ;   // created STRING to store filePath. This is JSON file path which contains test data ie credentials for testing.  
		  datais = getClass().getClassLoader().getResourceAsStream(dataFileName);  // we're reading the JSON file via InputSTream Method's
		  JSONTokener tokener = new JSONTokener(datais); // Now we need to create a JSON tokkener and then we need to pass the InputStream to the Tokenner
		  loginUsers = new JSONObject(tokener);          // creating object of JSON and pass the Tokenner to  JSON Object .we need Tokenner to get our JSON object
		  // Now Best practise is to close the InputStream so we used TRY CATCH block and we'll close it using FINALLY block . AFter this we will extract the TEST DATA in our Test cases in Test classes.
	 }
	 catch(Exception e){
		e.printStackTrace();  
		throw e; // If we don't write this, then in case Exception occurs testNG will think that since we are catching them & it will not throw it and will continue with the cases. This line will throw the exception..
	 }
	 finally {
		if(datais != null) {   // doing a null check to close the InputStream if its open or not.
			datais.close();    // we're closing the InputStream when its not null after making us of it
			
			
		}
			
	 }
	  
  }

  @AfterClass
  public void afterClass() {
  }

  
  
  
  //Creating Test cases:-
  
  //FYI 1. We stored all our TEST DATA credentials in a JSON File named "loginUsers.json" and we fetch data from there via using "getJSONObject" and "getString" methods used in test cases below
  //    2. We stored all our Expected Results statements  in a XML File named "expectedResults.xml" and we fetch data from there via  'Document Builder Factory' which we created as a method "parseStringXML" in UTILITY class which returns the HashMap with key|value pair . It will read the xml file as File Input Stream > Then initalized FileInputStream in BASECLASS and then used ExpectedResultStrings object here which contains key-value of Xml file.
   
  
}
