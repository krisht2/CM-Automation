package Advisor;

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
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;

import java.io.File;
import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ScheduledExecutorService;
import java.util.logging.Level;


public class AdvisorCheckMultiple {
   WebDriver driver;
   ExtentReports extent;
   ExtentTest pdpTest;
    ExtentTest imageTest;
    ExtentTest linkTest;
    ExtentTest DFPTest;
    ExtentTest AnalyticsTest;
    ExtentTest SEOTest;

    String url = "https://www.forbes.com/advisor/credit-cards/best/balance-transfer/";
    String domain ="https://www.forbes.com/";
    Boolean topten=true;
    Boolean DFPEnabled=true;
    List<String> urls=new ArrayList<>();
    int id = 0;

   @BeforeTest
                   public void setUp() {
               //Driver Paths and Setting properties
   //        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
   //        System.setProperty("webdriver.gecko.driver",ffDriverPath);

               String extentReportFile = System.getProperty("user.dir")+"\\AdvisorHomepage.html";
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
            driver = new ChromeDriver(caps);
       url ="https://www.forbes.com/advisor/";
       driver.get(url);
       driver.manage().window().maximize();
       List <WebElement> featured =driver.findElement(By.className("row-container")).findElements(By.tagName("a"));
       String prev = "";
       for(WebElement w:featured) {
           if(w.getAttribute("href").contains("https://www.forbes.com/advisor/")&&!(w.getAttribute("href").equalsIgnoreCase(prev))) {
               System.out.println(w.getAttribute("href") + "\n");
               String u =w.getAttribute("href");
               urls.add(u);
               prev =w.getAttribute("href");
           }
       }

           }

           @Test(priority = -2)
           public void imageCheck()throws Exception{
       try{
               imageTest=extent.startTest("Broken Image Checks");

               int count =0;
               ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
               Thread.sleep(3000);
               List <WebElement> featured =driver.findElements(By.tagName("a"));
               System.out.println("Size ="+featured.size());
               List<String> links = new ArrayList<>();
               for(WebElement w : featured){
                   try{
                       ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", w);
                       Thread.sleep(100);
                       if(w.getAttribute("style").contains("url"))
                       {
                           count++;
                           String style = w.getAttribute("style");
                           System.out.println(style);
                           String link = style.substring(style.indexOf("(")+2,style.indexOf(")")-1);
                           links.add(link);
                           System.out.println(link+"\n");
                       }
                   }catch (Exception e){e.printStackTrace();};
               }


               List<WebElement>bann=driver.findElements(By.className("main-column__img"));
               for(WebElement w:bann){
                   count++;
                   links.add(w.getAttribute("data-bg-img-src"));
               }

               for(int i =0;i<links.size();i++){
                   driver.get(links.get(i));
                   if(driver.getTitle().contains("404")) {
                       System.out.println("Broken URL = " + links.get(i));
                      imageTest.log(LogStatus.FAIL,"Broken URL = " + links.get(i));
                   }
                   else {
                       System.out.println("Image Exists : " + driver.getCurrentUrl());
                       imageTest.log(LogStatus.PASS,"Image Exists : " + driver.getCurrentUrl());
                   }
               }
               System.out.println(count);
               extent.endTest(imageTest);
           }catch (Exception e){imageTest.log(LogStatus.FAIL,"Error in Selenium Script : </br>"+e); extent.endTest(imageTest);}
   }

    @Test(invocationCount = 7,priority=-1)
    public void PDPTest() throws Exception
    {
        try {
            pdpTest = extent.startTest("PDP Checks: " + id);
            //  logger.log(LogStatus.INFO,"Test Message");
            url = urls.get(id);
            System.out.println("ID = " + id + " URL = " + url);
            pdpTest.log(LogStatus.INFO, " URL = " + url);
            id++;
            driver.get(url);
            String urlid = url.substring(url.indexOf("tid") + 4);
            if (urlid.length() > url.length())
                urlid = "NOTID";
            String tid1 = "";
            String tid2 = "";
            String tid3 = "";
            for (Cookie ck : driver.manage().getCookies())
                if (ck.getName().equalsIgnoreCase("fadve2etid"))
                    tid3 = ck.getValue();
            System.out.println("Cookie value of \'fadve2etid\' = " + tid3);
            if (topten) {
                List<WebElement> l1 = driver.findElements(By.className("image__box-widget"));
                //System.out.println("Size = "+l1.size());
                List<WebElement> l2 = driver.findElements(By.className("apply-now__box-widget"));
                //System.out.println("Size = "+l2.size());
                List<WebElement> cards = driver.findElements(By.className("loan-card-wrapper"));
                System.out.println("Total cards =" + cards.size());
                if (cards.size() == 0 && (url.contains("review") || url.contains("details"))) {
                    String applyNow = "";
                    try {
                        applyNow = driver.findElement(By.className("cc-apply-now")).findElement(By.tagName("a")).getAttribute("href");
                    } catch (NoSuchElementException e) {
                        applyNow = driver.findElement(By.className("apply-now-cta")).findElement(By.tagName("a")).getAttribute("href");
                    }
                    System.out.println(applyNow);
                    tid1 = applyNow.substring(applyNow.indexOf("tid") + 4);
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

                    if (tid1.equals(tid2) && tid1.equals(tid3)) {
                        System.out.println("TID Consistent");
                    } else
                        System.out.println("COOKIE ERROR!!!!!!" + tid3);
                    extent.endTest(pdpTest);
                }
                if (l1.size() == l2.size() && l1.size() == cards.size())
                    for (int i = 0; i < l1.size(); i++) {
                        System.out.println(cards.get(i).findElement(By.className("mob-hide")).getText());

                        String applynow = l1.get(i).findElement(By.tagName("a")).getAttribute("href");
                        tid1 = applynow.substring(applynow.indexOf("tid") + 4);
//                        if(tid1.equals(urlid))
//                            System.out.println("Equal: "+tid1);
//                        else
//                            System.out.println("Error : "+tid1);
                        applynow = l2.get(i).findElement(By.tagName("a")).getAttribute("href");
                        tid2 = applynow.substring(applynow.indexOf("tid") + 4);
                        String cardType = "";
                        if (applynow.contains("brcclx"))
                            cardType = "Affiliate Card";
                        else
                            cardType = "Non Affiliate Card";
                        if (tid2.equals(tid1))
                            if (tid2.equals(tid3))
                                System.out.println("TID matches in Cards and Cookies \nCard tid = " + tid1 + "\nApplynow tid = " + tid2 + "\nType of card = " + cardType + "\nURL =" + applynow + "\n\n");
                            else
                                System.out.println("TID different from cookies \nCard tid = " + tid1 + "\nApplynow tid = " + tid2 + "\nType of card = " + cardType + "\nURL =" + applynow + "\n\n");
                        else
                            System.out.println("TID Error \nCard tid = " + tid1 + "\nApplynow tid = " + tid2 + "\nType of card =  \n\n");
                    }
                else
                    System.out.println("Some error in Matching the size of cards and links!!!");
            } else {
                String applyNow = "";
                try {
                    applyNow = driver.findElement(By.className("cc-apply-now")).findElement(By.tagName("a")).getAttribute("href");
                } catch (NoSuchElementException e) {
                    applyNow = driver.findElement(By.className("apply-now-cta")).findElement(By.tagName("a")).getAttribute("href");
                }
                System.out.println(applyNow);
                tid1 = applyNow.substring(applyNow.indexOf("tid") + 4);
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

                if (tid1.equals(tid2) && tid1.equals(tid3)) {
                    System.out.println("TID Consistent");
                } else
                    System.out.println("COOKIE ERROR!!!!!!" + tid3);
                extent.endTest(pdpTest);

            }
            extent.endTest(pdpTest);
        }catch (Exception e){pdpTest.log(LogStatus.FAIL,"Error in Selenium Script: </br>"+e);extent.endTest(pdpTest);}

    }
    @Test(invocationCount = 7)
    public void checkExternalLinks(){
       try{
        linkTest=extent.startTest("External link Checks: "+id);

        if(id==7)id=0;
        url=urls.get(id);
        System.out.println("ID = "+id+" URL = "+url);
        linkTest.log(LogStatus.INFO," URL = "+url);

        id++;
        driver.get(url);
        List<WebElement> links =driver.findElements(By.tagName("a"));
        for(int i =0;i<links.size();i++){
//            System.out.println(urls.size()+" ; "+i);
            try{
                if(!links.get(i).getAttribute("href").startsWith(domain)&&links.get(i).getAttribute("href").length()>1)
                {
                    // System.out.println("*************************************");
                    String rel = links.get(i).getAttribute("rel");
                    String target = links.get(i).getAttribute("target");
                    if(!rel.equalsIgnoreCase("nofollow")||!target.equalsIgnoreCase("_blank")) {
                        System.out.println(links.get(i).getAttribute("href"));
                        System.out.println("Rel = " + rel + "\nTarget = " + target);
                        System.out.println("*************************************");


                        linkTest.log(LogStatus.FAIL,links.get(i).getAttribute("href")+"</br>Rel = " + rel + "</br>Target = " + target);

                    }
                }
            }catch (NullPointerException ne){}
        }
        extent.endTest(linkTest);
       }catch (Exception e){linkTest.log(LogStatus.FAIL,"Error in Selenium Script : </br>"+e);}

    }
    @Test(invocationCount = 7)
    public void checkAnalytics(){
       try{
        AnalyticsTest=extent.startTest("Analytics Script Test : "+ id);
        if(id==7)id=0;
        url=urls.get(id);
        System.out.println("ID = "+id+" URL = "+url);
        AnalyticsTest.log(LogStatus.INFO," URL = "+url);
        id++;
        driver.get(url);
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
            if(!base.equalsIgnoreCase(ga)){
                System.out.println("FAILURE!!!!");
                AnalyticsTest.log(LogStatus.FAIL,"GA found = "+ga+"</br> Expected value = "+base);
            }
            else
                AnalyticsTest.log(LogStatus.PASS,"GA found = "+ga+"</br> Expected value = "+base);

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
            if (!base.equals(ga))
                {
                    System.out.println("FAILURE!!!!");
                    AnalyticsTest.log(LogStatus.FAIL,"UA found = "+ga+"</br> Expected value = "+base);
                }
            else
                AnalyticsTest.log(LogStatus.PASS,"UA found = "+ga+"</br> Expected value = "+base);
        }
        extent.endTest(AnalyticsTest);
       }catch (Exception e){AnalyticsTest.log(LogStatus.FAIL,"Error in Selenium Script : </br>"+e);extent.endTest(AnalyticsTest);}

    }

    @Test(priority = 1,invocationCount = 7)
    public void DFPCheck() throws Exception{
       try{
        if(id==7)id=0;
        url=urls.get(id);

        System.out.println("ID = "+id+" URL = "+url);
        DFPTest=extent.startTest("DFP AD Check");
        id++;
        DFPTest.log(LogStatus.INFO," URL = "+url);
        driver.get(url);
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
                    DFPTest.log(LogStatus.PASS,adData);
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
               new WebDriverWait(driver,30).until(ExpectedConditions.visibilityOfElementLocated(By.tagName("iframe")));
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
                    DFPTest.log(LogStatus.PASS,adData);
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
            String adCounts ="";
           if(!(driver.getCurrentUrl().contains("review")||driver.getCurrentUrl().contains("detail"))) {
                System.out.println("Revies found"+(driver.getCurrentUrl().contains("review")));
                System.out.println("Media Credit = " + driver.findElement(By.className("media-credit")).getText());
                adCounts = "Total Ad Count = " + adcount + "<br >MN Ad Count = " + mnAds + "<br >DFP Ad Count = " + dfpAds + "<br >Media Credit = " + driver.findElement(By.className("media-credit")).getText();

            }
            else
                adCounts = "Total Ad Count = " + adcount + "<br >MN Ad Count = " + mnAds + "<br >DFP Ad Count = " + dfpAds + "<br >Media Credit = NO Credits on Review Page";

            DFPTest.log(LogStatus.INFO, adCounts);
        }
        else {
            System.out.println("DFP Check skipped from the test. [set DFPEnabled to true in order to perform this check on DFP Enabled pages!]");
            DFPTest.log(LogStatus.SKIP,"DFP Test Skipped!! </br>[set DFPEnabled to true in order to perform this check on DFP Enabled pages!]");
        }extent.endTest(DFPTest);
       }catch (Exception e){DFPTest.log(LogStatus.FAIL,"Error in Selenium Script : </br>"+e);
       System.out.println(e);
       extent.endTest(DFPTest);}

    }
    @Test(invocationCount = 7)
    public void SEOChecks(){
       try{
       SEOTest = extent.startTest("SEO TESTS : "+id);
        if(id==7)id=0;
        url=urls.get(id);
        System.out.println("ID = "+id+" URL = "+url);
        SEOTest.log(LogStatus.INFO," URL = "+url);
        id++;
        driver.get(url);
       if(driver.findElements(By.tagName("h1")).size()>1){
           System.out.println("More than 1 H1 found");
           SEOTest.log(LogStatus.FAIL,"Total number of H1 Tags : "+driver.findElements(By.tagName("h1")).size());
       }
       else if(driver.findElements(By.tagName("h1")).size()==1)
           SEOTest.log(LogStatus.PASS,"Only 1 H1 Tag");
       else
           SEOTest.log(LogStatus.FAIL,"Total number of H1 Tags : "+driver.findElements(By.tagName("h1")).size());
       extent.endTest(SEOTest);
       }catch (Exception e){SEOTest.log(LogStatus.FAIL,"Error in Selenium Script : </br>"+e);extent.endTest(SEOTest);}

        if(driver.getTitle().length()<1)
            SEOTest.log(LogStatus.FAIL,"NO Title"+driver.getTitle());
        else
            SEOTest.log(LogStatus.PASS,"Title : "+driver.getTitle());

        String s= driver.getPageSource();
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

        String code2 = driver.getPageSource().replaceAll("amp;","");
        System.out.println(code2);
        String script = "<script async=\"async\" src=\"//consent.trustarc.com/notice?domain=forbes.com&js=nj&noticeType=bb&c=teconsent\" crossorigin";
        if(code2.toLowerCase().contains(script.toLowerCase()))
            SEOTest.log(LogStatus.PASS,"CCPA Script present");
        else
            SEOTest.log(LogStatus.FAIL,"CCPA Not Script present");

    }
   @AfterTest
   public void closeTest(){
       driver.quit();
       extent.flush();
       extent.close();
       try {
            sendEmail();
       }
       catch (Exception e){System.out.println(e);}
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
    public void sendEmail() throws EmailException {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(System.getProperty("user.dir")+"\\AdvisorHomepage.html");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        String to []={"krish.t@media.net","saili.m@media.net","fenil.g@media.net"};

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("krish.t@media.net", "fstomkzuhqlyvcuv"));
        email.setSSLOnConnect(true);
        email.addTo("krish.t@media.net");
        email.setFrom("krish.t@media.net", "Krish");
        email.setSubject("Advisor Tracking - Automation Report");
        email.setMsg("PFA The below Advisor Tracking");

        email.attach(attachment);

        email.send();
        System.out.println("Email method executed");
    }


}