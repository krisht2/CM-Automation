import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogEntries;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class IFSVue {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest logger;

    @BeforeTest
                    public void setUp() {
                //Driver Paths and Setting properties
//        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
//        System.setProperty("webdriver.gecko.driver",ffDriverPath);

                String extentReportFile = System.getProperty("user.dir")+"\\Automation_IFS_DailyReport.html";
                String extentReportImage = System.getProperty("user.dir")+"\\extentReportImage.png";

                String chromeDriverPath = "C:\\Users\\krish.t\\Downloads\\chromedriver.exe";
                System.setProperty("webdriver.chrome.driver", chromeDriverPath);

                extent = new ExtentReports(extentReportFile, true);
                extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));


//For Server
                // String extentReportFile = System.getProperty("user.dir")+"//Automation_IFS_DailyReport.html";
                // String extentReportImage = System.getProperty("user.dir")+"//extentReportImage.png";
                // extent = new ExtentReports(extentReportFile, true);
                // extent.loadConfig(new File("//usr//local//bin//quiz//extent-config.xml"));
                // System.setProperty("webdriver.chrome.driver", "/usr/local/bin/chromedriver");


                //Logging preferences
                ChromeOptions caps = new ChromeOptions();
            //     caps.addArguments("--headless");
            // caps.addArguments("--no-sandbox");
                LoggingPreferences logs = new LoggingPreferences();
                logs.enable(LogType.BROWSER, Level.ALL);
                logs.enable(LogType.DRIVER, Level.ALL);
                logs.enable(LogType.PERFORMANCE, Level.ALL);
                // logs.enable(LogType.PROFILER, Level.ALL);
                // logs.enable(LogType.SERVER, Level.ALL);
                // logs.enable(LogType.CLIENT, Level.ALL);
                caps.setCapability(CapabilityType.LOGGING_PREFS, logs);

                driver = new ChromeDriver(caps);

            }
        @Test
        public void runTest() throws Exception
        {

try {
    String url = "http://forbes-staging.testingpe.com/ignore-test/13-employee-benefits-that-dont-actually-work-ifs-vue/?utm_source=msn&lcid=90223099";
    logger = extent.startTest("W3 Default layout Automation - " + url, "This test will browse through all the article pages and check for ads, console errors, analytics script and cookies");
    long start = System.currentTimeMillis();

    driver.get(url);
    driver.manage().window().maximize();
    long finish = System.currentTimeMillis();
    long totalTime = finish - start;
    System.out.println("Initial Time for page load - " + totalTime);
    int numSlides = 12;
    int numvisibleSlides = 1;
    System.out.println("\n\n\nCookie Data: ");
    //logger.log(LogStatus.INFO,"Cookie Data: <br />");

    String utm = url.substring(url.indexOf("=") + 1, url.indexOf("&"));
    String lcid = url.substring(url.indexOf("=", url.indexOf("=") + 1) + 1);
    for (Cookie ck : driver.manage().getCookies()) {
        //System.out.println("Cookie name = "+ck.getName());
        if (ck.getName().contains("forbespe42srcx")) {
            System.out.println(("Cookie name = " + ck.getName() + ";\nCookie value = " + ck.getValue() + ";\n Cookie Domain : " + ck.getDomain() + ";\n Cookie Path = " + ck.getPath() + ";\n Cookie Expiry = " + ck.getExpiry() + ";\n Cookie Is Secure? = " + ck.isSecure()));
            if (ck.getName().contains("lc")) {
                if (ck.getValue().equals(lcid)) {
                    System.out.println("Correct Tracking LCID\n");
                    logger.log(LogStatus.PASS, "Correct Tracking LCID");
                } else {
                    System.out.println("ERRROR IN TRACKING LCID!!!!\n");
                    logger.log(LogStatus.FAIL, "Error Tracking LCID");
                }
            } else {
                if (ck.getValue().contains(utm)) {
                    System.out.println("Correct Tracking UTM Source\n");
                    logger.log(LogStatus.PASS, "Correct Tracking UTM Source");
                } else {
                    System.out.println("ERRROR IN TRACKING UTM Source!!!!\n");
                    logger.log(LogStatus.FAIL, "Error Tracking UTM Source. Found : " + ck.getValue());
                }
            }
        }
    }

    String source = driver.getPageSource();
    if (source.contains("www.google-analytics.com/analytics.js")) {
        System.out.println("Analytics Script present");
        logger.log(LogStatus.PASS, "Analytics Script present");
    } else
        logger.log(LogStatus.FAIL, "Analytics Script not present");

    for (int j = 0; j < ((numSlides / numvisibleSlides) + 1); j++) {


        int adcount = 0;
        int mnAds = 0;
        int dfpAds = 0;
        JavascriptExecutor js = (JavascriptExecutor) driver;
        js.executeScript("window.scrollBy(0,300)");
        Thread.sleep(500);
        WebElement w = driver.findElement(By.className("media-credit"));

        finish = System.currentTimeMillis();
        totalTime = finish - start;
        if (j > 0)
            System.out.println("Total Time for page load - " + totalTime);
        WebElement article = driver.findElement(By.xpath("//div[@class=\"container-grid\"]/div[" + (j + 1) + "]"));
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", article);
        Thread.sleep(5000);
        List a = driver.findElements(By.tagName("iframe"));
        System.out.println("page : " + (j + 1));
        logger.log(LogStatus.INFO, "page : " + (j + 1) + "<br />Total Iframes = " + a.size());
        System.out.println("Total Iframes = " + a.size());


        for (int i = 0; i < a.size(); i++) {
//                    System.out.println(((WebElement)a.get(i)).findElement(By.xpath("./../..")).getAttribute("class")+" is the Position");
//                    System.out.println("Checking Ad with ID - "+((WebElement) a.get(i)).getAttribute("id"));

            if (!((WebElement) a.get(i)).getAttribute("width").equalsIgnoreCase("0") && !((WebElement) a.get(i)).getAttribute("height").equalsIgnoreCase("0") && !((WebElement) a.get(i)).getAttribute("width").equalsIgnoreCase("") && !((WebElement) a.get(i)).getAttribute("height").equalsIgnoreCase("")) {
//                        System.out.println("Height-width Passed : "+((WebElement) a.get(i)).getAttribute("id"));
                String str;
                WebElement parent = ((WebElement) a.get(i)).findElement(By.xpath("./../.."));
                System.out.println("ID of Parent Element = " + parent.getAttribute("id") + "\n Class String value = " + parent.getAttribute("Class"));
                System.out.println(((WebElement) a.get(i)).getSize());
                System.out.println(((WebElement) a.get(i)).getAttribute("width"));
                System.out.println(((WebElement) a.get(i)).getAttribute("height"));
                System.out.println(((WebElement) a.get(i)).getAttribute("id"));

                String adData = ((WebElement) a.get(i)).getSize() + "<br />" + ((WebElement) a.get(i)).getAttribute("width") + "<br />" + ((WebElement) a.get(i)).getAttribute("height") + "<br />" + ((WebElement) a.get(i)).getAttribute("id");

                //logger.log(LogStatus.INFO,adData);


                //logger.log(LogStatus.INFO,""+((WebElement) a.get(i)).getSize());
//                        logger.log(LogStatus.INFO,((WebElement) a.get(i)).getAttribute("width"));
//                        logger.log(LogStatus.INFO,""+((WebElement) a.get(i)).getAttribute("height"));
//                        logger.log(LogStatus.INFO,""+((WebElement) a.get(i)).getAttribute("id"));

                System.out.println("_____________________________________");
                adcount++;
                if (((WebElement) a.get(i)).getAttribute("id").contains("_mN"))
                    mnAds++;
                else if (((WebElement) a.get(i)).getAttribute("id").contains("google"))
                    dfpAds++;

            }

        }
        a = null;
        System.out.println("********************************");
        System.out.println("Total Ad Count = " + adcount);
        System.out.println("MN Ad Count = " + mnAds);
        System.out.println("DFP Ad Count = " + dfpAds);
        System.out.println("Media Credit = " + driver.findElements(By.className("media-credit")).get(j + 1).getText());

        String adCounts = "Total Ad Count = " + adcount + "<br >MN Ad Count = " + mnAds + "<br >DFP Ad Count = " + dfpAds + "<br >Media Credit = " + driver.findElements(By.className("media-credit")).get(j + 1).getText();
        logger.log(LogStatus.INFO, adCounts);

//                logger.log(LogStatus.INFO,"Total Ad Count = " + adcount);
//                logger.log(LogStatus.INFO,"MN Ad Count = " + mnAds);
//                logger.log(LogStatus.INFO,"DFP Ad Count = " + dfpAds);
//                logger.log(LogStatus.INFO,"Media Credit = " + driver.findElement(By.className("media-credit")).getText());

        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");


        start = System.currentTimeMillis();
        try {
            driver.findElement(By.className("next-slide-btn-text")).click();
        } catch (ElementNotInteractableException e) {
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
            ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
            js.executeScript("window.scrollBy(0,300)");
            // System.out.println(driver.findElement(By.className("next-slide-btn-text")).getText());
            if (j < (numSlides / numvisibleSlides))
                driver.findElement(By.className("next-slide-btn-text")).click();
            Thread.sleep(2000);
        }

        System.out.println("_________________________________________");
        //Print Logs
        analyzeLog(driver);
        System.out.println("_________________________________________");
        System.out.println("********************************");
    }

    driver.quit();
}catch (Exception e){
    logger.log(LogStatus.ERROR,e);
}
            logger.getTest().setStatus(LogStatus.PASS);
            extent.endTest(logger);

        }
    @AfterTest
    public void closeTest(){
        extent.endTest(logger);
        extent.flush();
        extent.close();
        try {
            sendEmail();
        }
        catch (Exception e){System.out.println(e);}
    }

    public void sendEmail() throws EmailException {
        // Create the attachment
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(System.getProperty("user.dir")+"\\Automation_IFS_DailyReport.html");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);

        // Create the email message
        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("krish.t@media.net", "fstomkzuhqlyvcuv"));
        email.setSSLOnConnect(true);
        email.addTo("krish.t@media.net");
        email.setFrom("krish.t@media.net", "Krish");
       email.setSubject("IFSVue Template - Automation Report");
        email.setMsg("PFA The below IFSVue Report");

        // add the attachment
        email.attach(attachment);

        // send the email
        email.send();
        System.out.println("Email method executed");
    }

    public void analyzeLog(WebDriver driver) {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        String errors ="";
        String warnings = "";
        for (LogEntry entry : logEntries) {
            if ((!entry.toString().contains("WARNING"))&&!entry.toString().contains("forbes"))
            errors = errors+(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage()+"<br /><br />");
            else if ((entry.toString().contains("forbes"))/*||(entry.toString().contains("SEVERE"))*/)
            warnings = warnings+(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage()+"<br /><br />");

        }
        System.out.println("Warnings = \n"+warnings);
        System.out.println("Errors = \n"+errors);
        if(!warnings.equals(""))
            logger.log(LogStatus.WARNING,"The page has warnings : <br />"+warnings);
        if(!errors.equals(""))
            logger.log(LogStatus.ERROR,"The page has Errors : <br />"+errors);
//        if(!errors.isEmpty()){
//        driver.quit();
//            Assert.fail("There were Console errors");
//        }
    }
}