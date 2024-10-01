package com.qa.pages;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import com.qa.baseClass.BaseTest;
import com.qa.baseClass.TopMenu3Dots;
import com.qa.utility.Utility;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;
import io.appium.java_client.pagefactory.iOSXCUITFindBy;

public class LoginPage extends BaseTest {   

	
	// For Defining the UI elements, we use @AndroidFindBy for ANDROID and @iOSXCUITFindBy for IOS providedte by PageFactopry Class
	
		// UI Elements
		
		@AndroidFindBy (accessibility = "email")   //For ANDROID , we use "accessibility" when parameter is "accessibility Id" 
		@iOSXCUITFindBy(id="test-Username")                // For IOS, we use "id" when parameter is "accessibility Id" 
		private WebElement usernameTxtfld;                 // common name of the web element for ISO and ANDROID
		
		
		@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"Login\"]/android.widget.TextView") 
	   //@iOSXCUITFindBy(id="test-LOGIN")
		private WebElement EmailLoginBtn;
		
	
		@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"Email, Email address not found.\"]/android.widget.TextView[2]")
		//	@iOSXCUITFindBy(xpath="//android.view.ViewGroup[@content-desc="Email, Email address not found."]/android.widget.TextView[2]")
			 private WebElement errEmailTxt;
		
			
		@AndroidFindBy (accessibility = "password") 
			//@iOSXCUITFindBy(id="test-Password")
			private WebElement passwordTxtfld;        	// common name of the web element for ISO and ANDROID
			
			
			@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"Email, Password, Invalid credentials\"]/android.widget.TextView[3]")
		//	@iOSXCUITFindBy(xpath="//XCUIElementTyoeOther[@name=\"test-Error message\"]/child::XCUITElementTypeStaticText")
			 private WebElement errPasswordTxt;
		
			
			@AndroidFindBy (accessibility = "Forgot email?")
			//	@iOSXCUITFindBy(xpath="//android.view.ViewGroup[@content-desc="Forgot email?"]")
				 private WebElement forgotEmailLink;
			
			@AndroidFindBy (accessibility = "Forgot password?")
			//	@iOSXCUITFindBy(xpath="//android.view.ViewGroup[@content-desc="Forgot email?"]")
				 private WebElement forgotPasswordLink;
			
			@AndroidFindBy (accessibility = "Login") 
			//@iOSXCUITFindBy(id="Login")
			private WebElement loginBtn;
			
			
			@AndroidFindBy (xpath = "//android.view.ViewGroup[@content-desc=\"\"]/android.widget.TextView")
			//	@iOSXCUITFindBy(xpath="//XCUIElementTyoeOther[@name=\"test-Error message\"]/child::XCUITElementTypeStaticText")
				 private WebElement eyeIcon;
			
			
		
		
		
		
		// Initializing UI elements via Page Factory :-
		public LoginPage() {
			
			PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);  // With the version of java client 9.1, now we need to initialize Page factory in every Page Class.	
		}                                                                               // we updated Driver with GetDriver() method since we set Getter and setter Method for Thread safety for Parellel execution.
		
	
	
	
		// CREATING METHODS:-
		
		public LoginPage enterUserName(String username) {
			clear(usernameTxtfld);
			System.out.println("logging in with: "+ username ); // Instead of this s.o.p we used Logger method
			//utils.log().info("logging in with: "+ username);  // used logger to print the logs
			
			sendKeys(usernameTxtfld, username);
		  return this;                                       // Since we will stay on the same screen, so returing the object of same page class
		}
		
		
		public LoginPage clickEmailLoginBtn() {
		System.out.println("Pressing Email Log in button" );
			//utils.log().info("Pressing Email Log in button");
			click(EmailLoginBtn);
			return this;
			
		}
		
		public String emailErrText() {
	    	 return getText(errEmailTxt);    // Fetching the Value of the attribute 'Text' of the webElement 'errPasswordTxt'
	    	 
	     }
		
   	public LoginPage enterPassword(String password) {
   		clear(passwordTxtfld);
			sendKeys(passwordTxtfld, password);
			System.out.println("logging in with: "+ password );
			//utils.log().info("logging in with: "+ password);
			return this;                                       // Since we will stay on the same screen, so returing the object of same page
		}
		
		
     public HomePage pressLoginBtn() {
			click(loginBtn);
			return new HomePage();                                       // Since we remain to HOMEPAGE, so returing the object of HOMEAPGE page class
		
     }
		
	
     public String passwordErrText() {
    	
    	 return getText(errPasswordTxt);    // Fetching the Value of the attribute 'Text' of the webElement 'errPasswordTxt'
    	 
     }
		
     
}
