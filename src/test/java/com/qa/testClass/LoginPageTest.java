package com.qa.testClass;
import org.testng.annotations.Test;
import com.qa.baseClass.BaseTest;
import com.qa.pages.HomePage;
import com.qa.pages.LoginPage;
import com.qa.utility.Utility;

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
public class LoginPageTest extends BaseTest { // 1st -> We need to do this to initialize our Driver. Doing this, when TestNG will execute LoginPage Test class it will 1st go to BASE class and will run @beforeMethod and will initialize driver
	  
	//Initialize Login Page and then Homeapge page at class level
LoginPage loginPage;
HomePage homePage;
	
// below 2 variables created for reading JSON file which is created for test data 
//InputStream datais;	  // We created this to use JSON Test DATA file we created in src/test/Resources . This is called adding ABSTRACTION layer to our Test Data . We moved this object from Global level to Method level for Thread Safety for parallel execution.
JSONObject loginUsers;	 // we're creating JSON Object . It will contain all the Test Data of LoginUsers.JSON.  [we don't need to move it to Local variable since we're just reading it]
	
	
	
  @BeforeMethod   
  public void beforeMethod(Method m) {                          // To Print the name of method running in the CONSOLE. So for this we're using METHOD Class
	  loginPage = new LoginPage();  
	
	 System.out.println("\n" + "******* Starting test:" + m.getName() + "*******" + "\n");     // This method "GetName()" will give the name of the testMethod which is currently executing
	 // utils.log().info("\n" + "******* Starting test:" + m.getName() + "*******" + "\n");  // used logger to print the logs instead od above SOP
	  
	  
	 launchApp();
	 System.out.println("\n" +"The app is launched" + "\n");   // before every method the app will get launched
	  //utils.log().info("\n" +"The app is launched" + "\n");  // used logger to print the logs instead od above SOP
  }

  @AfterMethod
  public void afterMethod() {
	  closeApp();
	  System.out.println("\n" +"The app is killed" + "\n");  // after every method the app will get killed
	  //utils.log().info("\n" +"The app is killed" + "\n");  // used logger to print the logs instead od above SOP
  }
 
  @BeforeClass  // These method will always run even if any test case fails due to some exception.
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
   
  @Test
  public void invalidEmailErrText() {
	  //loginPage.enterUserName("xyz@gmail.com");
	  loginPage.enterUserName(loginUsers.getJSONObject("invalidUser").getString("username"));  // Instead of above code where we are hardcoding usernames, we are fetching test data fom JSON testdata File. where Object is "Invaliduser" and its key we need is "username". For this we wrote code in @Before Class
	  loginPage.clickEmailLoginBtn();
	  
	  String actualErrText= loginPage.emailErrText() + "anc";
	 // String expectedErrText= "Email address not found.";
	  String expectedErrText= getExpectedResultsStrings().get("errMsg_invalidEmail" );  // Instead to hardcoding EXPECTED RESULTS strings here, we created a XML file 'expectedResults.xml' and fetch key|value from HashMap string 'ExpectedResultStrings' via 'getMthod() to get object Key.
	  System.out.println("actual error Text is :" + actualErrText + "\n" + "Expected Error text is:" + expectedErrText);
	  
	  Assert.assertEquals(actualErrText, expectedErrText);  
  }
  
  
  @Test
  public void invalidPasswordErrText() {
	 
	  loginPage.enterUserName(loginUsers.getJSONObject("invalidPassword").getString("username"));   // Using JSON object to fetch test data from file loginUsers.Json  . Here InalidPassword is object of which we are fetching value of key 'username'
	  loginPage.clickEmailLoginBtn();
	  loginPage.enterPassword(loginUsers.getJSONObject("invalidPassword").getString("password"));   // Using JSON object to get test data
	  homePage= loginPage.pressLoginBtn();            // Since it will take to Homepage screen so we return Homepage Object . In thi was we have initialized it as well.
	  
	  String actualErrText= loginPage.passwordErrText();
	  // String expectedErrText= "Invalid credentials";
	  String expectedErrText= getExpectedResultsStrings().get("errMsg_invalidPassword"); // Instead to hardcoding EXPECTED RESULTS strings here, we created a XML file 'expectedResults.xml' and fetch key|value from HashMap string 'ExpectedResultStrings' via 'getMthod() to get object Key.
	  System.out.println("actual error Text is :" + actualErrText + "\n" + "Expected Error text is:" + expectedErrText);
	  
	  Assert.assertEquals(actualErrText, expectedErrText); 
	   
  }
  
  @Test
  public void validLogin() {
	  loginPage.enterUserName(loginUsers.getJSONObject("validUser").getString("username"));  // Using JSON object to get test data
	  loginPage.clickEmailLoginBtn();
	  loginPage.enterPassword(loginUsers.getJSONObject("validUser").getString("password"));  // Using JSON object to get test data
	  homePage= loginPage.pressLoginBtn();            // Since it will take to Homepage screen so we return Homepage Object . In thi was we have initialized it as well.
	  
	 
	  Assert.assertEquals(homePage.myTechChatsBtn.isDisplayed(), true);

}
}
