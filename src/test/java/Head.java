import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
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
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.logging.Level;

public class Head {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest seqlogger;
    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<String> headers2 = new ArrayList<String>();
    WebDriver window;
    @BeforeTest
                public void setUp() {
//            Driver Paths and Setting properties
//        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
//        System.setProperty("webdriver.gecko.driver",ffDriverPath);
            String extentReportFile = System.getProperty("user.dir")+"\\Head.html";

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
            caps.setCapability("goog:loggingPrefs", logs );
            caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
            driver = new ChromeDriver(caps);

        }
    @Test
    public void runTest() throws Exception{
        driver.get("https://www.forbes.com/advisor");
        driver.manage().window().maximize();
        WebElement headerMenu = driver.findElement(By.className("menu-header-forbes-style-container"));
        List e = headerMenu.findElements(By.tagName("ul"));
        System.out.println(e);
        for (int i = 0; i < e.size(); i++) {
            WebElement header = ((WebElement) e.get(i));
            String s[] = header.getText().split("\n");
            for (String k : s) {
                if (k.length() > 1) headers.add(k);
            }
        }            System.out.println(headers);
        List<WebElement> advisor= new ArrayList<>();
        driver.get("https://www.forbes.com");

        headerMenu = driver.findElement(By.className("header__channels"));
        e= headerMenu.findElements(By.tagName("ul"));
        e.remove(e.size()-1);
        List temp = driver.findElement(By.xpath("//ul[@class='header__channels']")).findElements(By.tagName("li"));
        for(int i =0;i<temp.size();i++){
            try {
                String k = ((WebElement) temp.get(i)).findElement(By.className("header__title")).getText();

                if(k.length()>1) {
                    //System.out.println(k);
                    headers2.add(k);
                }
            }            catch (Exception ex){}

        }
        List <WebElement>forbes =  new ArrayList<>();
        System.out.println(headers2);
        System.out.println(headers2.equals(headers));

        if(headers2.equals(headers)) {
            seqlogger= extent.startTest("CHECK SEQUENCE");
            window= new ChromeDriver();
            window.manage().window().maximize();
            window.get("https://www.forbes.com/advisor");
            System.out.println(window.getCurrentUrl());
            System.out.println(driver.getCurrentUrl());

            boolean seq = true;
            List <String> forb = new ArrayList<>();
            List <String> adv = new ArrayList<>();
            int max = forbes.size();
            for (String h : headers2) {
                try {
                    forbes = driver.findElement(By.linkText(h)).findElements(By.xpath("./../div[2]/ul/li"));
                    System.out.println(forbes.size());
                    advisor = window.findElement(By.linkText(h)).findElements(By.xpath("./../div/ul/li"));
                    System.out.println(advisor.size());
                     seq = true;
                      forb = new ArrayList<>();
                      adv = new ArrayList<>();
                     max = forbes.size();
                    if (advisor.size() != forbes.size()){
                        //seqlogger.log(LogStatus.FAIL, "Some items may have been added / Removed in this header");
                    if (max < advisor.size())
                        max = advisor.size();
                    }
                    for (int i = 0; i < max; i++) {
                            String forbesval =forbes.get(i).findElement(By.tagName("a")).getAttribute("innerText").replaceAll("amp;", "").replaceAll("’", "'").trim();
                            String advisorval = advisor.get(i).findElement(By.xpath("./a")).getAttribute("innerHTML").replaceAll("amp;", "").replaceAll("’", "'").trim();
                            adv.add(advisorval);
                            forb.add(forbesval);
                            System.out.println( advisorval+" " + forbesval);
                            if(advisorval.equalsIgnoreCase(forbesval))
                                System.out.println("true");

                            else if(forbesval.equalsIgnoreCase(("All "+h)));
                            else {
                                System.out.println("False!!");
                                seq=false;
                            }
                    }
                    if(seq)
                        seqlogger.log(LogStatus.PASS,"Sequence in order : "+h);
                    else {
                        seqlogger.log(LogStatus.FAIL, "Sequence NOT in order : " + h);
                        seqlogger.log(LogStatus.INFO, "Forbes List : <br />"+forb);
                        seqlogger.log(LogStatus.INFO, "Advisor List : <br />"+adv);
                    }
                }
                catch (Exception ex) {
                        System.out.println("Could not find : " + h);
                }
            }

            advisor = window.findElement(By.xpath(".//a[contains(text(),'Featured')]")).findElements(By.xpath("./../div/ul/li"));
            forbes = driver.findElement(By.xpath(".//span[contains(text(),'Featured')]")).findElements(By.xpath("./../div[2]/ul/li"));
            max = forbes.size();
            if (advisor.size() != forbes.size()){
                //seqlogger.log(LogStatus.FAIL, "Some items may have been added / Removed in this header");
                if (max < advisor.size())
                    max = advisor.size();
            }
            for (int i = 0; i < max; i++) {
                String forbesval =forbes.get(i).findElement(By.tagName("a")).getAttribute("innerText").replaceAll("amp;", "").replaceAll("’", "'").trim();
                String advisorval = advisor.get(i).findElement(By.xpath("./a")).getAttribute("innerHTML").replaceAll("amp;", "").replaceAll("’", "'").trim();
                adv.add(advisorval);
                forb.add(forbesval);
                System.out.println( advisorval+" " + forbesval);
                if(advisorval.equalsIgnoreCase(forbesval))
                    System.out.println("true");
                else {
                    System.out.println("False!!");
                    seq=false;
                }
            }
            if(seq)
                seqlogger.log(LogStatus.PASS,"Sequence in order : Featured");
            else {
                seqlogger.log(LogStatus.FAIL, "Sequence NOT in order : Featured");
                seqlogger.log(LogStatus.INFO, "Forbes List : <br />"+forb);
                seqlogger.log(LogStatus.INFO, "Advisor List : <br />"+adv);
            }


            advisor = window.findElement(By.xpath(".//a[contains(text(),'More')]")).findElements(By.xpath("./../div/ul/li"));
            forbes = driver.findElement(By.xpath(".//span[contains(text(),'More')]")).findElements(By.xpath("./../div[2]/ul/li"));
            max = forbes.size();
            if (advisor.size() != forbes.size()){
                //seqlogger.log(LogStatus.FAIL, "Some items may have been added / Removed in this header");
                if (max < advisor.size())
                    max = advisor.size();
            }
            for (int i = 0; i < max; i++) {
                String forbesval =forbes.get(i).findElement(By.tagName("a")).getAttribute("innerText").replaceAll("amp;", "").replaceAll("’", "'").trim();
                String advisorval = advisor.get(i).findElement(By.xpath("./a")).getAttribute("innerHTML").replaceAll("amp;", "").replaceAll("’", "'").trim();
                adv.add(advisorval);
                forb.add(forbesval);
                System.out.println( advisorval+" " + forbesval);
                if(advisorval.equalsIgnoreCase(forbesval))
                    System.out.println("true");
                else {
                    System.out.println("False!!");
                    seq=false;
                }
            }
            if(seq)
                seqlogger.log(LogStatus.PASS,"Sequence in order : More");
            else {
                seqlogger.log(LogStatus.FAIL, "Sequence NOT in order : More");
                seqlogger.log(LogStatus.INFO, "Forbes List : <br />"+forb);
                seqlogger.log(LogStatus.INFO, "Advisor List : <br />"+adv);
            }

        }
        extent.endTest(seqlogger);
    }
    @AfterTest
    public void closeTest(){
       driver.quit();
       window.quit();
        extent.endTest(seqlogger);
        extent.flush();
        extent.close();
    }
}