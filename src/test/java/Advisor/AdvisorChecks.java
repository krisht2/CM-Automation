package Advisor;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;


 public class AdvisorChecks {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest logger;
    String url = "https://www.forbes.com/advisor/credit-cards/best/balance-transfer/";
    String domain ="https://www.forbes.com/";
    Boolean topten=true;
    Boolean DFPEnabled=false;


    @BeforeTest
                    public void setUp() {
                //Driver Paths and Setting properties
    //        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
    //        System.setProperty("webdriver.gecko.driver",ffDriverPath);

                String extentReportFile = System.getProperty("user.dir")+"\\AdvisorChecks.html";
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

                caps.setCapability(CapabilityType.LOGGING_PREFS, logs);
                driver = new ChromeDriver(caps);

        long start = System.currentTimeMillis();
        JavascriptExecutor js = (JavascriptExecutor) driver;

        driver.get(url);
        driver.manage().window().maximize();
        long finish = System.currentTimeMillis();
        long totalTime = finish - start;
        logger=extent.startTest("PDP Checks");

        System.out.println("Initial Time for page load - "+totalTime/1000+"Seconds");
        logger.log(LogStatus.INFO,"Initial Time for page load - "+totalTime);


    }
        @Test(priority=-1)
        public void PDPTest() throws Exception
        {

          //  logger.log(LogStatus.INFO,"Test Message");

            String urlid=url.substring(url.indexOf("tid")+4);
            if(urlid.length()>url.length())
                urlid="NOTID";
            String tid1 ="";
            String tid2 ="";
            String tid3="";
            for(Cookie ck : driver.manage().getCookies())
                if(ck.getName().equalsIgnoreCase("fadve2etid"))
                    tid3=ck.getValue();
            System.out.println("Cookie value of \'fadve2etid\' = "+tid3);
            if(topten){
                List<WebElement> l1 = driver.findElements(By.className("image__box-widget"));
                //System.out.println("Size = "+l1.size());
                List<WebElement> l2 = driver.findElements(By.className("apply-now__box-widget"));
                //System.out.println("Size = "+l2.size());
                List <WebElement> cards = driver.findElements(By.className("loan-card-wrapper"));
                System.out.println("Total cards ="+cards.size());
                if(l1.size()==l2.size()&&l1.size()==cards.size())
                    for(int i =0;i<l1.size();i++){
                        System.out.println(cards.get(i).findElement(By.className("mob-hide")).getText());

                        String applynow =l1.get(i).findElement(By.tagName("a")).getAttribute("href");
                        tid1=applynow.substring(applynow.indexOf("tid")+4);
//                        if(tid1.equals(urlid))
//                            System.out.println("Equal: "+tid1);
//                        else
//                            System.out.println("Error : "+tid1);
                        applynow =l2.get(i).findElement(By.tagName("a")).getAttribute("href");
                        tid2=applynow.substring(applynow.indexOf("tid")+4);
                        String cardType="";
                        if(applynow.contains("brcclx"))
                            cardType="Affiliate Card";
                        else
                            cardType="Non Affiliate Card";
                        if(tid2.equals(tid1))
                            if(tid2.equals(tid3))
                                System.out.println("TID matches in Cards and Cookies \nCard tid = "+ tid1 +"\nApplynow tid = "+tid2+"\nType of card = "+cardType+"\nURL ="+applynow+"\n\n");
                            else
                                System.out.println("TID different from cookies \nCard tid = "+ tid1 +"\nApplynow tid = "+tid2+"\nType of card = "+cardType+"\nURL ="+applynow+"\n\n");
                        else
                            System.out.println("TID Error \nCard tid = "+ tid1 +"\nApplynow tid = "+tid2+"\nType of card =  \n\n");
                    }
                else
                    System.out.println("Some error in Matching the size of cards and links!!!");
            }
            else {
                String applyNow = "";
                try {
                    applyNow = driver.findElement(By.className("cc-apply-now")).findElement(By.tagName("a")).getAttribute("href");
                } catch (NoSuchElementException e) {
                    applyNow = driver.findElement(By.className("apply-now-cta")).findElement(By.tagName("a")).getAttribute("href");
                }
                System.out.println(applyNow);
                tid1= applyNow.substring(applyNow.indexOf("tid") + 4);
                String image = "";
                try {
                    image = driver.findElement(By.className("cc-img-wrap")).findElement(By.tagName("a")).getAttribute("href");
                } catch (NoSuchElementException e) {
                    image = driver.findElement(By.className("cc-card_imgwrap")).findElement(By.tagName("a")).getAttribute("href");
                }
                System.out.println(image);
                tid2 = image.substring(image.indexOf("tid") + 4);
                System.out.println(tid1);
                System.out.println(tid2);

                if(tid1.equals(tid2)&&tid1.equals(tid3))
                {
                    System.out.println("TID Consistent");
                }
                else
                    System.out.println("COOKIE ERROR!!!!!!"+tid3);
                extent.endTest(logger);
            }


        }
        @Test
        public void checkExternalLinks(){

            List<WebElement> urls =driver.findElements(By.tagName("a"));
        for(int i =0;i<urls.size();i++){
//            System.out.println(urls.size()+" ; "+i);
            try{
           if(!urls.get(i).getAttribute("href").startsWith(domain)&&urls.get(i).getAttribute("href").length()>1)
           {
              // System.out.println("*************************************");
               String rel = urls.get(i).getAttribute("rel");
               String target = urls.get(i).getAttribute("target");
               if(!rel.equalsIgnoreCase("nofollow")||!target.equalsIgnoreCase("_blank")) {
                   System.out.println(urls.get(i).getAttribute("href"));
                   System.out.println("Rel = " + rel + "\nTarget = " + target);
                   System.out.println("*************************************");
               }
           }
        }catch (NullPointerException ne){}
        }
        }
        @Test
        public void checkAnalytics(){
        String source = driver.getPageSource();

            int startGA = source.indexOf("GTM-");
            int stopGA = source.indexOf("GTM-")+11;
            int difference = stopGA-startGA;
           // System.out.println("Diff ="+difference+"String :" +source.substring(startGA,stopGA)+"\nsTRT = "+startGA+" "+stopGA);
            int k =2;
            String base =source.substring(startGA,stopGA);
            while (source.indexOf("GTM-",stopGA+1)!=-1)
            {
                 //System.out.println("\n\nOld : "+startGA +" "+ stopGA+" "+difference+" ");
                startGA = source.indexOf("GTM-",stopGA+1);
                stopGA = startGA+11;
                difference = stopGA-startGA;
                String ga="";
                if(difference>4)
                {
//                    System.out.println("New : " + startGA + " " + stopGA + " " + difference + " ");
                    System.out.println("GTM #" + k + ", Length = " + difference);
                    ga = source.substring(startGA, stopGA);
                    k++;
                }
                if(!base.equalsIgnoreCase(ga))
                    System.out.println("FAILURE!!!!");
                 System.out.println(ga);
            }

             startGA = source.indexOf("UA-");
             stopGA = source.indexOf("UA-")+13;
             difference = stopGA-startGA;
             base = source.substring(startGA,stopGA);
         //   System.out.println("Diff ="+difference+"String :" +source.substring(startGA,stopGA)+"\nsTRT = "+startGA+" "+stopGA);
             k =2;
            while (source.indexOf("UA-",stopGA+1)!=-1) {
                //System.out.println("\n\nOld : "+startGA +" "+ stopGA+" "+difference+" ");
                startGA = source.indexOf("UA-", stopGA + 1);
                stopGA = startGA + 13;
                difference = stopGA - startGA;
                String ga = "";
                if (difference > 4) {
//                    System.out.println("New : " + startGA + " " + stopGA + " " + difference + " ");
                    System.out.println("UA #" + k + ", Length = " + difference);
                    ga = source.substring(startGA, stopGA);
                    k++;
                }
                System.out.println(ga);
                if (!base.equals(ga)) {
                    System.out.println("FAILURE!!!!"+base+" "+ga);
                }
            }

        }
        @Test(priority = 1 )
        public void DFPCheck() throws Exception{
            if(DFPEnabled){
                int adcount = 0;
                int mnAds = 0;
                int dfpAds = 0;

                List<WebElement> topAd =driver.findElements(By.className("top-ed-placeholder"));
                for(WebElement w:topAd){
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", w);
                    Thread.sleep(1000);
                    WebElement ad = w.findElement(By.tagName("iframe"));

                    if (!ad.getAttribute("width").equalsIgnoreCase("0") && !ad.getAttribute("height").equalsIgnoreCase("0") && !ad.getAttribute("width").equalsIgnoreCase("") && !ad.getAttribute("height").equalsIgnoreCase("")) {

                        String str;
                        WebElement parent = ad.findElement(By.xpath("./../.."));
                        System.out.println("ID of Parent Element = " + parent.getAttribute("id") + "\n Class String value = " + parent.getAttribute("Class"));
                        System.out.println(ad.getSize());
                        System.out.println(ad.getAttribute("width"));
                        System.out.println(ad.getAttribute("height"));
                        System.out.println(ad.getAttribute("id"));

                        String adData = ad.getSize() + "<br />" + ad.getAttribute("width") + "<br />" + ad.getAttribute("height") + "<br />" + ad.getAttribute("id");

                        System.out.println("_____________________________________");
                        adcount++;
                        if (ad.getAttribute("id").contains("_mN"))
                            mnAds++;
                        else if (ad.getAttribute("id").contains("google"))
                            dfpAds++;

                    }

                }


                List<WebElement> recAds =driver.findElements(By.className("rail-recx-ed-placeholder"));
                for(WebElement w:recAds){
                    ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", w);
                    Thread.sleep(1000);
                   WebElement ad = w.findElement(By.tagName("iframe"));

                    if (!ad.getAttribute("width").equalsIgnoreCase("0") && !ad.getAttribute("height").equalsIgnoreCase("0") && !ad.getAttribute("width").equalsIgnoreCase("") && !ad.getAttribute("height").equalsIgnoreCase("")) {

                        String str;
                        WebElement parent = ad.findElement(By.xpath("./../.."));
                        System.out.println("ID of Parent Element = " + parent.getAttribute("id") + "\n Class String value = " + parent.getAttribute("Class"));
                        System.out.println(ad.getSize());
                        System.out.println(ad.getAttribute("width"));
                        System.out.println(ad.getAttribute("height"));
                        System.out.println(ad.getAttribute("id"));

                        String adData = ad.getSize() + "<br />" + ad.getAttribute("width") + "<br />" + ad.getAttribute("height") + "<br />" + ad.getAttribute("id");

                        System.out.println("_____________________________________");
                        adcount++;
                        if (ad.getAttribute("id").contains("_mN"))
                            mnAds++;
                        else if (ad.getAttribute("id").contains("google"))
                            dfpAds++;

                    }

                }
                System.out.println("********************************");
                System.out.println("Total Ad Count = " + adcount);
                System.out.println("MN Ad Count = " + mnAds);
                System.out.println("DFP Ad Count = " + dfpAds);
                System.out.println("Media Credit = " + driver.findElement(By.className("media-credit")).getText());

                String adCounts = "Total Ad Count = " + adcount + "<br >MN Ad Count = " + mnAds + "<br >DFP Ad Count = " + dfpAds + "<br >Media Credit = " + driver.findElement(By.className("media-credit")).getText();
                logger.log(LogStatus.INFO, adCounts);

            }
            else
                System.out.println("DFP Check skipped from the test. [set DFPEnabled to true in order to perform this check on DFP Enabled pages!]");

        }

     @Test()
     public void SEOChecks(){
         if(driver.findElements(By.tagName("h1")).size()>1)
             System.out.println("More than 1 H1 found");
         else
             System.out.println("Only 1 H1");

     }

    @AfterTest
    public void closeTest(){
        driver.quit();
       // logger.getTest().setStatus(LogStatus.PASS);
        //extent.endTest(logger);
        //extent.endTest(logger);
        extent.flush();
        extent.close();
    }
}