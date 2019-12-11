import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.eclipse.jetty.util.AbstractTrie;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.server.handler.DeleteSession;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import javax.swing.text.html.HTML;
import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;

public class Archives {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest consoleTest;
    String url ="https://www3.forbes.com/lifestyle/the-lonely-planet-top-ten-tourism-countries-for-2020-ifs-vue/";
    String domain ="https://www.forbes.com/";
    ChromeOptions caps;
    Boolean DFPEnabled=true;
    List<String> urls = new ArrayList<>();

    @BeforeTest
                public void setUp() {
//            Driver Paths and Setting properties
//        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
//        System.setProperty("webdriver.gecko.driver",ffDriverPath);
            String extentReportFile = System.getProperty("user.dir")+"\\Archive.html";
            String extentReportImage = System.getProperty("user.dir")+"\\extentReportImage.png";

            String chromeDriverPath = "C:\\Users\\krish.t\\Downloads\\chromedriver.exe";
            System.setProperty("webdriver.chrome.driver", chromeDriverPath);

            extent = new ExtentReports(extentReportFile, true);
            extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));

            //Logging preferences
             caps = new ChromeOptions();

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
    public void archiveTest() throws Exception {
        driver.get("https://www3.forbes.com/archive-articles/");

        try {
           while (true)
           driver.findElement(By.className("show-more-archive")).click();
       }catch (Exception e){}

        SimpleDateFormat formatter = new SimpleDateFormat("EEEE MMM dd, yyyy");
        List <WebElement>articles = driver.findElements(By.className("post-item"));
        for(WebElement post : articles){
            System.out.println();
            String date=post.findElement(By.tagName("div")).getText();

            Date d = formatter.parse(date);
            Date today=formatter.parse(formatter.format(new Date()));
            System.out.println(d.compareTo(today));
            if(d.compareTo(today)==0){
            System.out.println(today);
            System.out.println(d);
            System.out.println(post.findElement(By.tagName("a")).getText());
                urls.add(post.findElement(By.tagName("a")).getAttribute("href"));
        }else break;
        }
        System.out.println(urls);
        for(String link:urls){
            ExtentTest DFPTest = extent.startTest("DFP Ads :"+link);
            ExtentTest html=extent.startTest("HTML Entities : "+link);

            boolean flag = true;
           driver.quit();
           driver = new ChromeDriver(caps);
           driver.manage().window().maximize();
           driver.get(link);
            int i =1;
            String previous ="";
           while(flag){

               HTMLEntityCheck(link,html);
                DFPCheck(link,DFPTest);
               try{

                   WebElement w = driver.findElement(By.xpath("//span[@class='media-credit']"));
                   ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", w);

                   Thread.sleep(500);
                   try {
                       driver.findElement(By.xpath("//div[@class='continue']")).click();
                   }
                   catch (Exception ec){
                       System.out.println("Default VanillaJS not found"+driver.getCurrentUrl());

                       ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);",driver.findElement(By.className("media-credit")));
                       ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,300)");
                       Thread.sleep(1000);
                       driver.findElement(By.xpath("//button[contains(text(),'Continue')]")).click();
                   }
                   System.out.println("Clicked Defualt Template button :"+driver.getCurrentUrl());

               }
               catch (Exception e){
                  // System.out.println(e);
                   List<WebElement> medias = driver.findElements(By.className("article-title"));
                   ((JavascriptExecutor)driver).executeScript("arguments[0].scrollIntoView(true);",medias.get(i));
                   ((JavascriptExecutor)driver).executeScript("window.scrollBy(0,300)");
                   Thread.sleep(2000);
                   if(!previous.equalsIgnoreCase(driver.getCurrentUrl())){
                       System.out.println(driver.getCurrentUrl());
                       previous=driver.getCurrentUrl();
                       i++;
                   }
                   else
                   try{
                       System.out.println("Default Vue not found");
                       ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
                       Thread.sleep(500);
                       ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");

                       driver.findElement(By.className("next-slide-btn-text")).click();
                       System.out.println("Clicked IFSVue button :"+driver.getCurrentUrl());
                   }
                   catch (Exception ex) {

                               flag=false;
                       //System.out.println("NO IFS Default button :"+driver.getCurrentUrl());
                   }
               }
           }
            extent.endTest(DFPTest);
            extent.endTest(html);
        }
    }
    @AfterTest
    public void closeTest()throws Exception{
       driver.quit();
        extent.flush();
        extent.close();
    }

public void DFPCheck(String link,ExtentTest DFPTest){

    int adcount = 0;
    int mnAds = 0;
    int dfpAds = 0;
    String adCounts="";
    List a = driver.findElements(By.tagName("iframe"));
    System.out.println("Total Iframes = " + a.size());

    for (int i = 0; i < a.size(); i++) {
        try {
            if (!((WebElement) a.get(i)).getAttribute("width").equalsIgnoreCase("0") && !((WebElement) a.get(i)).getAttribute("height").equalsIgnoreCase("0") && !((WebElement) a.get(i)).getAttribute("width").equalsIgnoreCase("") && !((WebElement) a.get(i)).getAttribute("height").equalsIgnoreCase("")) {
                String str;
                WebElement parent = ((WebElement) a.get(i)).findElement(By.xpath("./../.."));
                System.out.println("ID of Parent Element = " + parent.getAttribute("id") + "\n Class String value = " + parent.getAttribute("Class"));
                System.out.println(((WebElement) a.get(i)).getSize());
                System.out.println(((WebElement) a.get(i)).getAttribute("width"));
                System.out.println(((WebElement) a.get(i)).getAttribute("height"));
                System.out.println(((WebElement) a.get(i)).getAttribute("id"));

                String adData = ((WebElement) a.get(i)).getSize() + "<br />" + ((WebElement) a.get(i)).getAttribute("width") + "<br />" + ((WebElement) a.get(i)).getAttribute("height") + "<br />" + ((WebElement) a.get(i)).getAttribute("id");

                System.out.println("_____________________________________");
                adcount++;
                if (((WebElement) a.get(i)).getAttribute("id").contains("_mN"))
                    mnAds++;
                else if (((WebElement) a.get(i)).getAttribute("id").contains("google"))
                    dfpAds++;
            }
        }catch(Exception e){}
    }
    System.out.println("********************************");
    System.out.println("Total Ad Count = " + adcount);
    System.out.println("MN Ad Count = " + mnAds);
    System.out.println("DFP Ad Count = " + dfpAds);


    adCounts = "Total Ad Count = " + adcount + "<br >MN Ad Count = " + mnAds + "<br >DFP Ad Count = " + dfpAds;
    DFPTest.log(LogStatus.INFO, adCounts);
    extent.endTest(DFPTest);
}

public void HTMLEntityCheck(String link,ExtentTest html){
    Pattern p = Pattern.compile("(.*&#[0-9]{1,4};.*)|(.*&[a-z A-Z]{1,4};.*)");
    boolean error = false;

    String body = driver.findElement(By.tagName("body")).getText();

    String items[] = body.split("\\n");
    for (String item : items) {
        //    System.out.println(p.matcher(item).matches() + " : " + item);
        if (p.matcher(item).matches()) {
            System.out.println("Entity found : " + item);
            error=true;
            html.log(LogStatus.FAIL, "HTML Entity found : " + item+" URL: "+driver.getCurrentUrl());
        }
    }
    if(p.matcher(driver.getTitle()).matches()){
        System.out.println(driver.getTitle());
        System.out.println("Entity found : " + driver.getTitle());
        error=true;
        html.log(LogStatus.FAIL, "HTML Entity found in Title: " + driver.getTitle());

    }

    if(!error){
        System.out.println("NO HTML Elements found");
        html.log(LogStatus.PASS,"NO HTML Entities found!");
    }
    extent.endTest(html);
}
}