import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;


public class Rough {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest SEOTest;
    ExtentTest consoleTest;
    String url ="http://fbadvisor-staging.testingpe.com/reviews/top-ten-article-sample-tt/";
    String domain ="https://www.forbes.com/";

    Boolean DFPEnabled=true;
    @BeforeTest
                public void setUp() {
//            Driver Paths and Setting properties
//        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
//        System.setProperty("webdriver.gecko.driver",ffDriverPath);
            String extentReportFile = System.getProperty("user.dir")+"\\SEO.html";
            String extentReportImage = System.getProperty("user.dir")+"\\extentReportImage.png";

            String chromeDriverPath = "C:\\Users\\krish.t\\Downloads\\chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

            extent = new ExtentReports(extentReportFile, true);
            extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));

            //Logging preferences
            ChromeOptions caps = new ChromeOptions();

            //For Server
           //  caps.addArguments("--headless");
           // caps.addArguments("--no-sandbox");
            LoggingPreferences logs = new LoggingPreferences();
            logs.enable(LogType.BROWSER, Level.ALL);
            logs.enable(LogType.DRIVER, Level.ALL);
            logs.enable(LogType.PERFORMANCE, Level.ALL);
            logs.enable( LogType.PERFORMANCE, Level.ALL );
            caps.setCapability("goog:loggingPrefs", logs );
            caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
            driver = new ChromeDriver(caps);

        }
    @Test
    public void Rough() throws Exception{
        driver.manage().window().maximize();
        driver.get(url);
        String s = driver.getPageSource();
        SEOTest=extent.startTest("TEST");
        int start = s.indexOf("<link rel=\"canonical\" href=");
         int stop=start+2;
        try {
            String key=s.substring(start, s.indexOf(">", stop)) + ">";
            System.out.println(key);
            SEOTest.log(LogStatus.PASS,key.replaceAll("<", "&lt; ").replaceAll(">", "&gt;"));
        }catch (Exception ex){//Testcase fail
            System.out.println("Could not find");
            SEOTest.log(LogStatus.FAIL,"Could not find canonical link");

        }

        start = s.indexOf("<link rel=\"amphtml\" href=");
        stop=start+2;
        try {
            String key=s.substring(start, s.indexOf(">", stop)) + ">";
            System.out.println(key);
            SEOTest.log(LogStatus.PASS,key.replaceAll("<", "&lt; ").replaceAll(">", "&gt;"));

        }catch (Exception ex){//Testcase fail
            System.out.println("Could not find");
            SEOTest.log(LogStatus.FAIL,"could not find AMP version");

        }

        start = s.indexOf("<script type=\"application/ld+json\">");
        stop=start+2;
        try {
            String schema =s.substring(start, s.indexOf("</script>", stop)) + "</script>";
            if(schema.length()>46) {
                System.out.println(schema);
                SEOTest.log(LogStatus.PASS,schema.replaceAll("<", "&lt; ").replaceAll(">", "&gt;"));

            }
            else{
                System.out.println("FAIL");
                SEOTest.log(LogStatus.FAIL,"could not find Schema.org");

            }
        }catch (Exception ex){//Testcase fail
            System.out.println("Could not find");
            SEOTest.log(LogStatus.FAIL,"could not find Schema.org");

        }

        List <WebElement> elements= driver.findElements(By.tagName("meta"));
        checkPresence("og:site_name","property",elements);
        checkPresence("og:title","property",elements);
        checkPresence("og:url","property",elements);
        checkPresence("og:type","property",elements);
        checkPresence("og:description","property",elements);
        checkPresence("og:image","property",elements);
        checkPresence("name","itemprop",elements);
        checkPresence("headline","itemprop",elements);
        checkPresence("description","itemprop",elements);
        checkPresence("image","itemprop",elements);
        checkPresence("author","itemprop",elements);
        checkPresence("twitter:title","name",elements);
        checkPresence("twitter:url","name",elements);
        checkPresence("twitter:description","name",elements);
        checkPresence("twitter:image","name",elements);
        checkPresence("twitter:card","name",elements);
        checkPresence("twitter:site","name",elements);
        checkPresence("description","name",elements);
        checkPresence("robots","name",elements);


    }
    @AfterTest
    public void closeTest()throws Exception{
        //Thread.sleep(50000);
       driver.quit();
       // logger.getTest().setStatus(LogStatus.PASS);
        extent.endTest(SEOTest);
        //extent.endTest(logger);
        extent.flush();
        extent.close();
    }
    public void checkPresence(String key,String attribute,List<WebElement> elements){
        int flag =0;
        WebElement reference=null;
        for(WebElement w : elements) {
            try {
                if (w.getAttribute(attribute).contains(key) && w.getAttribute("content").length() > 0)
                {flag = 1;
                reference=w;
                }
            }catch (Exception e){}
        }
        if(flag ==0) {
        System.out.println("Not found : " + key);
            SEOTest.log(LogStatus.FAIL,"could not find : "+key);

        }
        else {
            System.out.println(" found : " + key);
            System.out.println("Content : " + reference.getAttribute("content"));
            SEOTest.log(LogStatus.PASS,"Found : "+key+"<br/> Content ="+reference.getAttribute("content"));

        }
    }
}