// COMMAND to START APPIUM SERVERS on specific Servers : appium server -p <port no>  | eg appium server -p 4723 , appium server -p 4724


package com.qa.baseClass;

import org.testng.annotations.Test;

//import com.aventstack.extentreports.testng.listener.ExtentITestListenerAdapter;
//import com.aventstack.extentreports.testng.listener.ExtentITestListenerClassAdapter;
import com.qa.utility.Utility;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import io.appium.java_client.InteractsWithApps;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.android.options.UiAutomator2Options;
import io.appium.java_client.ios.IOSDriver;
import io.appium.java_client.ios.options.XCUITestOptions;
import io.appium.java_client.screenrecording.CanRecordScreen;
import io.appium.java_client.service.local.AppiumDriverLocalService;
import io.appium.java_client.service.local.AppiumServiceBuilder;
import io.appium.java_client.service.local.flags.GeneralServerFlag;

import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterSuite;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;






// @Listeners(ExtentITestListenerAdapter.class)   //IT WAS USED when we want to use EXTENT REPORT ADAPTER used for Simple reports.  Its from ITestListener interface and its here for Extent Reports . After this we'll need to add report's configurations for which we create "extent.properties" file inside test/Resources in class path and copy code from extent reports website > Then create file 'spark-config.xml' and add website's code in there.

public class BaseTest {
  
	//protected static AppiumDriver driver;  // we made it static coz when we'll run the TestNG file, it will 1st come to TESTCLASS > Since it extends BASE class, it will come to BASE class and will initialize Drive>  then it will go back to TESTCLASS  BEFORE method and will initilize loginPage class where its again extending BASECLASS > It will again go to base class and trying to initializing UI elements and driver become null.
	protected static ThreadLocal <AppiumDriver> driver = new ThreadLocal <AppiumDriver>(); // we need to make the global variable to make them THREAD SAFE so that we can work on Parallel execution
	
	//protected static Properties props;   // This is created to load PROPERTIES files while initializing the driver.
	protected static ThreadLocal <Properties> props = new ThreadLocal <Properties>();
	
	//InputStream inputStream;	        // Created to store PROPERTIES File [To support PARALLEL EXECUTION, we need to shift All GLOBAL variable into LOCAL VARIABLES ]
	protected static ThreadLocal <String> dateTime = new ThreadLocal <String>();     // Created to collect Date and time. Used it in Screenshots of Failed Methods

	protected static ThreadLocal <HashMap<String, String>> ExpectedResultStrings = new ThreadLocal <HashMap<String, String>>() ; // Created this for using when we extract EXPECTED RESULTS data/strings stored inside 'expectedresults.xml' file. we're initializing it in BASE CLASS since we'll be using HasMap STRINGS all our automation. This was added because we added method to read "ExpectedResult" xml file in UTILS class for storing our expected results data in that xml.
	//prot ected static String platform;  // Its also part of reading "ExpectedResults xml' file. We've created it for method "getText" which is used to get the value of the attribute
	//InputStream stringsis;   // Doing it for expectedResults XML file  [Convert it to Loca Variable to support Parallel execution]
	//Utility utils;        // Instance created of UTILS class which we used for EXPECTED RESULTS xml whose method is stored in UTILITY class
	protected static ThreadLocal <String> platform= new ThreadLocal <String>();    // created variable to be used in "getText() method and we assign its value in beforeTest() method
	protected static ThreadLocal <String> deviceName= new ThreadLocal <String>();    // ThreadLocal allows you to store data that is specific to the current thread. This is useful in multi-threaded environments (like when running tests in parallel) to ensure that each thread has its own copy of the deviceName.
	
	Utility utils = new Utility();
	private static AppiumDriverLocalService server;    // created to start the APPIUM SERVER automatically. we'll use it in @BeforeSuite to run the method
	
	
	
	//  ****   creating GETTER AND SETTER methods for all GLOBAL LEVEL VARIABLES for THREAD safety
	
	
	 public AppiumDriver  getDriver() {   // Its created to get the Driver. This method was created to be used in Listener Class for Screenshot functionality,.		 
		 return driver.get();  // get() method will return the value for that particular THREAD. SO thate nsures thread safety
	 } 
	 public void setDriver(AppiumDriver driver2) {
		 driver.set(driver2);   //  set() will set the value of the object for this particular thread
	 }
	 
	// SETTINGS GETTER AND SETTER METHODS   > THREAD safety {for smooth parallel execution of IOS and Android}
	 public Properties getProps() {  // Getter method
		  return props.get();
	  }
	  
	  public void setProps(Properties props2) {  // Setter method
		  props.set(props2);
	  }
	
	  // SETTINGS GETTER AND SETTER methdos for  > THREAD safety
	  public HashMap<String, String> getExpectedResultsStrings() { // Getter method
		  return ExpectedResultStrings.get();
	  }
	  
	  public void setExpectedResultsStrings(HashMap<String, String> strings2) {  // Setter method
		  ExpectedResultStrings.set(strings2);
	  }
	  
	// SETTINGS GETTER AND SETTER methods for  > THREAD safety  
	  public String getPlatform() {             // Getter method
		  return platform.get();
	  }
	  
	  public void setPlatform(String platform2) {   // Setter method
		  platform.set(platform2);
	  }
	    
	  
	  
	  
	  public String getDeviceName() {             // Getter method
		  return deviceName.get();
	  }
	  
	  public void setDeviceName(String deviceName2) {   // Setter method
		  deviceName.set(deviceName2);
	  }
	  
	// SETTINGS GETTER AND SETTER methdos for  > THREAD safety
	  public String getDateTime() {  // Method to find the current Date Time  // Getter method
		  return dateTime.get();
	  }
	  
	  public void setDateTime(String dateTime2) { // Setter method
		  dateTime.set(dateTime2);

		  
		  
		  
	  }
	 
	 
	 // * * * * *  Now We need to remove the Class Level References and create Getter Setter wherever we're using Global parameters. Ex instead of driver, we'll use getDriver() method
 	

	
	// * * * *  Implementing VIDEO RECORDING of FAILED CASES in @Before and @After Methods.  (FOR IOS, we need to add extra dependency "ffmpeg" binary from CMD prompt using command" "brew install ffmpeg"
   @BeforeMethod	 // we are creating below method to RECORD THE VIDEO of  FAILED TEST Cases. Created here so that it can be used by every test class.
	public void beforeMethodForVideo() {
	 //  ((CanRecordScreen) driver).startRecordingScreen();  // Method to start the Recording 
	   ((CanRecordScreen) getDriver()).startRecordingScreen();  // Method to start the Recording . We used getDriver() getter method instead of driver variable.
   }
	
   @AfterMethod      // by adding "synchronized" we made this method synchronized which means at a particular time only 1 thread will use this method and other will wait.
   public synchronized void beforeMethodForVideo(ITestResult result ) throws IOException {  // Method to stop the recording of Failed Test cases
   //String media = ((CanRecordScreen) driver).stopRecordingScreen();  // Method to stop the Recording and we are storing the returned base 64 coded media in String variable. 
	   String media = ((CanRecordScreen) getDriver()).stopRecordingScreen();
   
   // we include IF statement if we want videos of only FAILED CASES . If you want videos of all run cases whether fail/pass, remove IF condistion.
	   if (result.getStatus() ==2){  // here GetSTatus will give the conculsion if test case failed or passed. As per this method, 2 means FAILED so we compared it with 2
		// Now we need to create DIRECTORY STRUCTURE to save the videos and for that we need 'result' of ITestListener so pass that as Argument to this Method.
		   Map <String, String> params = result.getTestContext().getCurrentXmlTest().getAllParameters();  // here we are fetching the testNG global paramters  as we need them for our directory so we use "Result" argument to fetch them which return them in hasMap so we created HashMap to store them
		   
		    //Our Directory will be : \FailedCasesVideos\<platformName>_<platormVersion>_<deviceName>\<datetime\<testClass>
		   String failedCaseVideos = "FailedCasesVideos" + File.separator + params.get("platformName") + "_" + params.get("platformVersion") + "_" + params.get("deviceName") + File.separator +getDateTime() + File.separator + result.getTestClass().getRealClass().getSimpleName();  // This is relative path
		   
		   File videoDir = new File(failedCaseVideos);   // storing in a file
		   
		   // If we want to make any Object synchronized, then use the method synchronized()
		   synchronized (videoDir) { // we made this object 'videoDir' synchronized
			   if(!videoDir.exists()) {     // we're checking if videoDir "failedCaseVideos" doesn't already exist, then create multiple directories
					videoDir.mkdirs();   // create multiple directories if not created, and if they're available it will overwrite them
				}	  
		   }
		   
	   
		   try (FileOutputStream stream = new FileOutputStream(videoDir + File.separator + result.getName() + ".mp4")) {
			stream.write(Base64.getDecoder().decode(media)); // This is different than tutor's code
			// stream.write(base64.decodeBase64(stream));  Tutor's code which gives error

		}catch (FileNotFoundException e) {
			e.printStackTrace();
		}catch (IOException e) {
			e.printStackTrace();													
		   }   
		   
	   }
  
   }
	     
   
   // Below code inside @BeforeSuite is create to run the APPIUM server Automatically
	
   @BeforeSuite
   public void beforeSuite() {
       // Using below method when you machine is MAC and comment method "getAppiumServerDefault" when using MAC machine . 
	    //server = getAppiumService();
	  
	   // Use below method when using WINDOWS System and comment above MAC method
	   server = getAppiumServerDefault(); // already method used to run the APPIUM SERVER. we are just passing that into variable
	  
	  if (!server.isRunning()) {  // To check if Appium server is not running, only then start the server . IN ACTUAL this is not of any user coz in actual Even if Sever is running, it will still not go inside ELSE block. Dnot sure why it happens but tests will run.
		  server.start();  // This will start the Appium server
		  server.clearOutPutStreams();  // This will stop showing all the APPIUM SERVER LOGS inside CONSOLE. Remove this line to get the logs of server
	  
		  System.out.println("\n" +"Appium server is started" + "\n");   
	   }
	   else {
		   System.out.println(" Appium Server was already running");   
	   }
   }
	  
	  @AfterSuite
	   public void afterSuite() {
		  server.stop(); // This will stop the Appium Server
		  
		  System.out.println("\n" +"Appium server was stopped" + "\n");   
  }
	 
   public AppiumDriverLocalService getAppiumServerDefault() {   // This was not working for IOS , so created custom method "AppiumDriverLocalService" to run the appium server on IOS. 
		 
		 return AppiumDriverLocalService.buildDefaultService();
	 }
	
 
   
  //USE below method for MAC coz method AppiumDriverLocalService wasn't working on MAC. For MAC Update the paths as per your Mac setup  [For running the APPIUM SERVER] .  And if you want to use this method for ANDROID, you can do it
	public AppiumDriverLocalService getAppiumService() {
		HashMap<String, String> environment = new HashMap<String, String>();
		environment.put("PATH", "enter_your_path_here" + System.getenv("PATH"));   // To find path type in terminal "echo $PATH"		
        // If you face the issue that PART is already being used up, then you need to close the server youself by using command in MAC <Isof -P|grep ':4723' | awk '{print $2}' | xargs kill -9>
		// The same command for Windows is(2 commands) : netstat -ano|findstr "PID :4723"     > get process id from this command and use it in 2nd cmd to kill  it
		                                            // : taskkill /pid 18264 /f
		environment.put("ANDROID_HOME", "enter_android_home_path");  // It will give error "JAVA_HOME" is not set , so we used this to set it up . YOU CAN GET THIS PATH from Android Studio > SDK manager > Copy path of "android SDK Location"
		return AppiumDriverLocalService.buildService(new AppiumServiceBuilder()
				.usingDriverExecutable(new File("/usr/local/bin/node")) // path used is for MAC   // node installation part . To get this path, just open cmd in windows and type 'where node'
				.withAppiumJS(new File("/usr/local/lib/node_modules/appium/build/lib/main.js")) //path used is for MAC here // appium installation part .  To get this path, just open cmd in windows and type 'where appium'. use path till npm and then go inside node_modules folder > then appium > build >lib > main.js. 
				
				/* TRIED USING IT inside WINDOWS
				 * .usingDriverExecutable(new File("C:\\Program Files\\nodejs\\node.exe")) //
				 * path used is for Windows here .withAppiumJS(new File(
				 * "C:\\Users\\schoudhary\\AppData\\Roaming\\npm\\node_modules\\appium\\lib\\main.js"
				 * )) //path used is for Windows here
				 */				.usingPort(4723)
				.withArgument(GeneralServerFlag.SESSION_OVERRIDE)
//				.withArgument(() -> "--allow-insecure","chromedriver_autodownload")
				.withEnvironment(environment)   // This is done to make the ENVIRONMENT VARIABLES available to JAVA coz in MAC Java has problem accessing system Env variables. So we make env var available within Eclipse i.e within our code
				.withLogFile(new File("ServerLogs/server.log")));  // To get all the APPIUM SERVER logs in a File inside Folder "AppiumServerLogs" . BUT IT DID NOT CREATE Any file automatically.
	}   
   
   
   
   
	
 @Parameters({"emulator", "simulator","platformName", "deviceName", "udid" , "udidEmulator" , "systemPort" , "chromeDriverPort", "WdaLocalPort","webkitDebugProxyPort"  })	 // We're using @PARAMTERS annotation of testNG to fetch our device specific capabilities Parameters from TESTNG file. Now pass these parameters to beforetest() method so that these parametrs acn be used there.
 @BeforeTest
  public void beforeTest( @Optional("androidOnly") String emulator, String simulator,String platformName, String deviceName , String udid,@Optional("androidOnly") String udidEmulator ,@Optional("androidOnly")String systemPort, @Optional("androidOnly") String chromeDriverPort ,@Optional("iOSOnly") String WdaLocalPort,@Optional("iOSOnly") String webkitDebugProxyPort) throws Exception {  // Initialize driver in this method so that driver is available for all TEST classes. We just need to initiate it once and install app once since all test cases will be executed on 1 device only. In SELENIUM we used to initialize it @BefireMethod level coz in there we initiated driver after every test case.,
	
	 // "@optional" parameters of TestNG is used when we want to set any parameters Optional.class eg emulator is not required for IOS so we set it to optional
	 
	//utils.log().info ("this is info message");
	//utils.log().error ("this is error message"); 
	//utils.log().debug ("this is debug message");
	//utils.log().warn ("this is warning message");
	 
	 
	 //dateTime= utils.dateTime();  //storing current dateTime in this variable .  We then staretd using getter and setter , so since we are setting the value we use setter method.
	 setDateTime(utils.dateTime()) ; // we're calling get dateTime method from Utility class and we we the returned value to set the value for global parameter dataTime in SetDataTime method.
	 URL url; // variable for APPIUM URL.  Created here so that can be used in Switch statement for both platforms
	 //platform= platformName;    // assigned the value of PlatformName in this variable so that it can be used in methods like 'getText()' , "closeApp()" 
	 setPlatform(platformName);
	 setDeviceName(deviceName);
	 
	 
	 InputStream stringsis= null;   // Doing it for expectedResults XML file 
	 InputStream inputStream = null;	        // Created to store PROPERTIES File 
	  
	 
	 Properties props = new Properties();
	 AppiumDriver driver;
	 
	 try {
		  // Below 4 lines are to load CONFIG.property file used to store capabilities.
		  
		  
		  props = new Properties();
		  String propFileName= "config.properties";  //  complete file path is not  needed since the property file is directly at root location under ClassPath [ie src/main/resource]. Now we can use "InputStream" to load the properties file.	 		  		
		  inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);     //Created InputStream object to store properties file. Since we created CONFIG file inside src/test/resouces, we can use "Input stream" to load the Config File. 	
		  props.load(inputStream);  // We're loading Properties file here. Now in order to get properties key/value, all we need to do is use "getProperty()" method
		  setProps(props);
		
		  
          //Below  4 lines coding was for reading expectedResult.xml as InputStream which is used for storing expected Results statements but it did not work as it showed InputStream as Null . So we used FilterInputStream and avoided using InputStream :-
    /*
     String xmlFileName = "expectedResults/expectedResults.xml";            // saving our ExpectedResusults xml file as String. This xml we created inside src/test/resources and contains all our expected results data.     
     stringsis = getClass().getClassLoader().getResourceAsStream(xmlFileName);  //  Read the expectedResults xml file as InputStream.
     
     //Printing the complete path of InputStream resource xml file to check if Path is correct
     URL resourceUrl = getClass().getClassLoader().getResource(xmlFileName);
     stringsis = resourceUrl.openStream();                                        // We found that its going to TARGET FOLDER and not to our specified Location, hence it gave Null InputStream. So we need to shift to FilerInputStream
     System.out.println("The path of xml resource file :" + resourceUrl);
     
     utils = new Utility();
     ExpectedResultStrings = utils.parseStringXML(stringsis);
     if (stringsis == null) {                                          // To check if our XML is not null and loader successfully.
			    System.out.println("expectedResults.xml not found!");
			} else {
			    System.out.println("expectedResults.xml loaded successfully.");
			}
		  
     */
     
     
 
		  // ************ This code was asked by Tutor to use when "parseStringXML" was giving error of "InputStream is null" and for this had to update utility method to parseStringXML2  to take input as FileInputStream instead of InputStream.		      
		//Below coding is for reading expectedResult.xml which is used for storing expected Results statements:-
		     
		       String xmlFilePath = System.getProperty("user.dir") + File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "expectedResults" + File.separator + "expectedResults.xml"; 	 // Storing the path of xml file inside String.	     		  		      
		       System.out.println("Path of expectedResults xml file used as FileInputStream is: " + xmlFilePath);  // Printing the path of the XML file		       
		       FileInputStream xmlFileInputStream = new FileInputStream(xmlFilePath);   // storing XML file as FileInputStream
		   
		       if (xmlFileInputStream == null) {           // To check if resource xml file is null or not
			        System.out.println("expectedResults.xml not found!");
			       } 
			       else {
			       System.out.println("expectedResults.xml loaded successfully.");
			        }		   		  		   			        
			      
			       //ExpectedResultStrings = utils.parseStringXML2(xmlFileInputStream);  // using the Ultility method to fetch XML data into a hashMap
		           setExpectedResultsStrings(utils.parseStringXML2(xmlFileInputStream));  //using Setter method.
		  

		  //DesiredCapabilities caps = new DesiredCapabilities();   // DesiredCapabilities is getting depricated, so used Options Class.
		  UiAutomator2Options options = new UiAutomator2Options(); // used for Android
		  XCUITestOptions options1 = new XCUITestOptions();  // Used for IOS
		  
		// ***** We set below 3 Capabilities in TESTNG XML file ********
		  options.setPlatformName(platformName);             
		  options.setDeviceName(deviceName);                 
		      
		                               
		  				   
		  url = new URL (props.getProperty("appiumURL") + "4723/");
		  //url = new URL (props.getProperty("appiumURL"));
		  
		  switch(platformName){
		  case "Android":
			  
			// ***** We set below 3 Capabilities in CONFIG Properties File ********
			  options.setAutomationName(props.getProperty("androidAutomationName"));  // Fetching Capability stored in properties File by key
			  options.setAppPackage(props.getProperty("androidAppPackage"));          // Fetching Capability stored in properties File by key
			  options.setAppActivity(props.getProperty("androidAppActivity"));        // Fetching Capability stored in properties File
			  
			  if(emulator.equalsIgnoreCase("true")) {
				  //options.setPlatformVersion(platformVersion);   // Use this capability when its emulator. We can even use UDID as well [we don't need this capability for real device]
				 // options.setCapability("avd", "deviceName");  // If i have set 'true' value in testNg xml means device is emulator.
				  
				  options.setUdid(udidEmulator);  // instead of above both "platformVersion, avd" capacities we can use this one
			  }
			  else                                            
			  {
				  options.setUdid(udid);    // If my device is real device, use Udid .   
			  }
			  
			//String androidAppUrl = getClass().getResource(props.getProperty("androidAppLocation")).getFile();   // fetching relative location of our pro app from config file. But it won't work it give null value so instead we used direct location.
			  //System.out.println("Url of the app location is:" + androidAppUrl);
			 
			  options.setCapability("systemPort", systemPort);   // specific to Android Platform [used while parallel execution]
			  options.setCapability("chromeDriverPort",chromeDriverPort); //Specific to Android Platform [used while parallel execution]
			  String androidAppUrl = System.getProperty("user.dir")+ File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "app" + File.separator + "app-stage-release.apk";
			  System.out.println("Url of the app location is:" + androidAppUrl); // Instead of this s.o.p we used Logger method
			  //utils.log().info("Url of the app location is:" + androidAppUrl);
		 
			// caps.setCapability("app", "androidAppUrl"); // fetching URL of Pro app to download it to use in this capability.
			  options.setApp(androidAppUrl);
			 //  url = new URL (props.getProperty("appiumURL")); // Fetching Capability stored in "properties File" by key
		   // url = new URL (props.getProperty("appiumURL") + "4723/");   // we changed it coz for Parallel execution, we are running 2 instances of Appium servers for IOS and Android	. // to use 1 appium server instance for parallel execution we will shift this above SWITCH case			
				driver = new AndroidDriver(url, options);
				break;
				
				
		  case "iOS":
			// ***** We set below 3 Capabilities in CONFIG Properties File ********
			  options.setAutomationName(props.getProperty("iOSAutomationName"));  // Fetching Capability stored in properties File by key
			  
			  options.setCapability("wdaLocalPort", WdaLocalPort);   // specific to Android Platform [used while parallel execution]
			  options.setCapability("webkitDebugProxyPort", webkitDebugProxyPort);   // specific to Android Platform [used while parallel execution]
			  if(simulator.equalsIgnoreCase("true")) {
				  
				  options.setCapability("avd", "deviceName");  // If i have set 'true' value in testNg xml means device is emulator. 
				  
			  }
			  else                                            // If my device is real device, use Udid . 
			  {
				  options.setUdid(udid);   
			  }
			  
			  			  
			  
			  String iOSAppUrl = System.getProperty("user.dir")+ File.separator + "src" + File.separator + "test" + File.separator + "resources" + File.separator + "app" + File.separator + "app-stage-release.apk";
			  System.out.println("Url of the app location is:" + iOSAppUrl);
			 // utils.log().info("Url of the app location is:" + iOSAppUrl);
			  options.setCapability("bundleId", props.getProperty("iOSBundleId"));  // When app is already downloaded, use this capabililty
			  options.setApp(iOSAppUrl); // This Capability is used to install the application when app is not already installed
			  
			   //url = new URL (props.getProperty("appiumURL")); // Fetching Capability stored in "properties File" by key
			  // url = new URL (props.getProperty("appiumURL") + "4724/");   // to use 1 appium server instance for parallel execution we will shift this above SWITCH case
			 driver = new IOSDriver(url, options);
				break;

				default :
					throw new Exception("Inavlid Platform !!" + platformName);  // It will throw exception if Platform is neither Ios nor Android
		  }
		  setDriver(driver);  // here we're passing it on to the Global variable so my global driver is also set.
		  
		  
	 
		  
			/*
			 * String appUrlRelativePath = props.getProperty("androidAppLocation"); String
			 * appUrl = System.getProperty("user.dir") + File.separator + "src" +
			 * File.separator + "test" + File.separator + "resources" + File.separator +
			 * appUrlRelativePath;
			 */
		  
		  
		 
			getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
			  
	  }
	 
	  catch(Exception e) {
		  e.printStackTrace();
		  throw e;   // If driver initialization fails, the program should immediately exit and report the error, preventing further execution.
	  }
	finally {      // we're closing both the InputStreams "inputStream and 'stringsis' (which we used for Properties file & EXPECTED RESULTS xml) by using Finally block which we used above in TRY  block. Now we go to TEST class and replace all Expected Results with our STRINGS used in XML of Expected results.
		if(inputStream !=null) {    
			inputStream.close();
			
		}
		if (stringsis !=null) {
			stringsis.close();
		}
	}
  }

  
 
//CREATING METHOD which will be used by DRIVER all around our cases:-

public void waitForVisibility(WebElement e) {
	  
	  WebDriverWait wait = new WebDriverWait(getDriver(),Duration.ofSeconds(Utility.wait)); //  Here we are using DYNAMIC WAIT which is mentioned in UTILITY class
	  wait.until(ExpectedConditions.visibilityOf(e));  
	  }
  
  
//Method for Clicking Element
public void click (WebElement e){
	  waitForVisibility(e);  // calling wait for visibility of element before we click it
	  e.click();
}

//Method for sendKeys 
public void sendKeys (WebElement e, String txt){
	  waitForVisibility(e);  // calling wait for visibility of element before we click it
	  e.sendKeys(txt);
}

//Method for clearing field of Element
public void clear (WebElement e){
	  waitForVisibility(e);  // method for clearing the field. 
	  e.clear();
}


//Method to get the value of any Attribute
public String getAttribute(WebElement e, String attribute) {                              // This method can't be used by both ANDROID and IOS because for Android it is "text" attribute and for IOS its "label". so we'll get the attribute as per PlatformName. We created getTest() method for it.
	  waitForVisibility(e); // calling wait for visibility of element before we click it
	   return e.getAttribute(attribute); 
	   }
  
  
  public String getText(WebElement e) {         // we will get  value of the attribute "text" or " label" as per platform bases.
	  switch (getPlatform()) {
	  case "Android" : 

	  return getAttribute(e, "text");        // use this attribute when platform is android
	  
	  case "iOS" :
		  return getAttribute(e, "label");   // use this attribute when platform is IOS
		  
	  }
	  return null;   
	  
  }


 public void closeApp() {    // killing the app on the basis of PlatformName provided in the XML of testNG
	
	 switch (getPlatform()) {
	 case "Android" : 		
		 ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("androidAppPackage"));
		 break;
		
	 case "iOS":
		 ((InteractsWithApps) getDriver()).terminateApp(getProps().getProperty("iOSBundleId")); 
	 }
 }
  
  
 public void launchApp() {    // launching the app on the basis of PlatformName provided in the XML of testNG
		
	 switch (getPlatform()) {
	 case "Android" : 		
		 ((InteractsWithApps) getDriver()).activateApp(getProps().getProperty("androidAppPackage"));
		 break;
		
	 case "iOS":
		 ((InteractsWithApps) getDriver()).activateApp(getProps().getProperty("iOSBundleId")); 
	 }
 }
 
 
 public WebElement scrollToElement() {       // YOU CAN NOT USE "X-PATH" to find Parent element (the scrolling element) and Child Element(element you need to scroll to).   use "resource ID/ Text Name / Class Name / Content Description which is basically the accessibility Id for Android.
	 
	 
	 return getDriver().findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().description(\"<parent_locator>\")).scrollIntoView(new UiSelector().description(\"<child_locator>\"))" ));  // Just replace the Parent and child locator
	
	 // return driver.findElement(AppiumBy.androidUIAutomator( "new UiScrollable(new UiSelector()" + ".description(\"<parent_locator>")).scrollIntoView("new UiSelector().description(\"<child_locator>\"))" ;);
	// Chat Gpt correct code>  return driver.findElement(AppiumBy.androidUIAutomator("new UiScrollable(new UiSelector().description(\"eParent\")).scrollIntoView(new UiSelector().description(\"<child_locator>\"))" )); // Sir's code giving error> return driver.findElement(AppiumBy.androidUIAutomator( "new UiScrollable(new UiSelector()" + ".description(\"<parent_locator>")).scrollIntoView("new UiSelector().description(\"<child_locator>\"))" ;);				  
	 
 }
 
 
 public WebElement scrollToElement(WebElement eParent, WebElement eChild) {    // updated code of above method to use attributes
	    String parentDescription = eParent.getAttribute("contentDescription"); // or another attribute to identify the parent
	    String childDescription = eChild.getAttribute("contentDescription"); // or another attribute to identify the child

	    return getDriver().findElement(AppiumBy.androidUIAutomator(
	        "new UiScrollable(new UiSelector().description(\"" + parentDescription + "\")).scrollIntoView(new UiSelector().description(\"" + childDescription + "\"))"
	    ));
	}

 
	/*   WE ARE SHIFTING these 2 methods TO GLOBAL LEVEL AND use GETTER AND SETTER Method to support Parallel execution.
	 * public AppiumDriver getDriver() { // Its created to get the Driver. This
	 * method was created to be used in Listener Class for Screenshot
	 * functionality,.
	 * 
	 * return driver; }
	 * 
	 * public String getDateTime() { // Method to find the current Date Time return
	 * dateTime; // we created this varibale at global level and then fetched the
	 * value of Current date and time by using Utility Method and stored in this
	 * Variable. So created this metgod to use anywhere. }
	 */
 
 
  @AfterTest             // runs only once after running all TEST CLASSES under <test> tag in testNG xml.
  public void afterTest() {
	  getDriver().quit();
  }

}
