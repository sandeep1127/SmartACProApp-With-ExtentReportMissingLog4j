package com.qa.baseClass;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.PageFactory;

import io.appium.java_client.pagefactory.AndroidFindBy;
import io.appium.java_client.pagefactory.AppiumFieldDecorator;

public class TopMenu3Dots extends BaseTest {    // We'll extend all those specific CLASS PAGES ie onboarding classes to this CLASS where "TopMenu3Dots" page is common for.  

	
	@AndroidFindBy (accessibility = "email")             //For ANDROID , we use "accessibility" when parameter is "accessibility Id" 
	//@iOSXCUITFindBy(id="test-Username")                // For IOS, we use "id" when parameter is "accessibility Id" 
	private WebElement usernameTxtfld;                 // common name of the web element for ISO and ANDROID
	
	
	
	
	public TopMenu3Dots() {
		
		PageFactory.initElements(new AppiumFieldDecorator(getDriver()), this);  // With the version of java client 9.1, now we need to initialize Page factory in every Page Class.	
	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
