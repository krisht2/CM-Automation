import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;

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
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.logging.Level;
import java.util.regex.Pattern;



public class CommonTests {
    WebDriver driver;
    ExtentReports extent;
    ExtentTest logger;
    ExtentTest consoleTest;
    String url ="https://www.forbes.com/wheels/l/wheels/top-20-best-auto-loans-in-2019/";
    String domain ="https://www.forbes.com/";

    Boolean DFPEnabled=true;
    @BeforeTest
                    public void setUp() {
                //Driver Paths and Setting properties
    //        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
    //        System.setProperty("webdriver.gecko.driver",ffDriverPath);
                String extentReportFile = System.getProperty("user.dir")+"\\CommonTests.html";

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
        @Test(priority = -1)
        public void analyzeComments() throws Exception
        {
            logger=extent.startTest("Find large comment blocks URL =  "+url);
          //  logger.log(LogStatus.INFO,"Test Message");
            long start = System.currentTimeMillis();
            JavascriptExecutor js = (JavascriptExecutor) driver;

            driver.get(url);
            driver.manage().window().maximize();
            long finish = System.currentTimeMillis();
            long totalTime = finish - start;
            System.out.println("Initial Time for page load - "+totalTime/1000+"Seconds");
            logger.log(LogStatus.INFO,"Initial Time for page load - "+totalTime);

            String source = driver.getPageSource();
            int startComment = source.indexOf("<!--");
            int stopComment = source.indexOf("-->");
            int difference = stopComment-startComment;
            System.out.println("Start = "+startComment+"\n Stop = "+ stopComment+"Difference = "+difference);
            int k =2;
            while (source.indexOf("<!--",stopComment)!=-1)
            {
               // System.out.println("\n\nOld : "+startComment +" "+ stopComment+" "+difference+" ");
                startComment = source.indexOf("<!--",stopComment+1);
                stopComment = source.indexOf("-->",startComment);
                difference = stopComment-startComment;
                String comment="";
                if(difference>4)
                  {
                    System.out.println("New : " + startComment + " " + stopComment + " " + difference + " ");
                    System.out.println("Comment #" + k + ", Length = " + difference);
                    comment = source.substring(startComment, stopComment + 4);
                    System.out.println(comment);
                      k++;
                }
                comment=comment.replace("<", "&lt; ").replace(">", "&gt;");
               // System.out.println(comment);
                //comment=comment.unescapeHtml4(comment);

                if(difference>100) {
                    logger.log(LogStatus.WARNING, "A comment with size "+difference+" is encountered: <br />" + comment);
                    System.out.println("Size ghreater than 100"+ difference);
                }
                if(difference>4)
                logger.log(LogStatus.INFO, "A comment  is encountered: <br />" + comment);
            }
            System.out.println("Checking other comments!!");

          //  System.out.println("\n\n"+htmlContent+"\n\n");
            startComment = source.indexOf("/*");
            stopComment = source.indexOf("*/");
            difference = stopComment-startComment;
            System.out.println("Start = "+startComment+"\n Stop = "+ stopComment+"Difference = "+difference);
            while (source.indexOf("/*",stopComment)!=-1&& difference>1)
            {
                System.out.println("\n\nOld : "+startComment +" "+ stopComment+" "+difference+" ");
                startComment = source.indexOf("/*",stopComment);
                stopComment = source.indexOf("*/",startComment);
                difference = stopComment-startComment;
                System.out.println("New : "+startComment +" "+ stopComment+" "+difference+" ");
                System.out.println("Comment #"+k+ ", Length = "+difference);
                String comment="";
                try {
                    comment= source.substring(startComment, stopComment+2);
                }
                catch(Exception e){}
                System.out.println(comment);
                k++;
                if(difference>100) {
                    logger.log(LogStatus.WARNING, "A comment with size greater than 100 characters is encountered: <br />" + comment);
                    System.out.println("Size greater than 100 ");
                }
                logger.log(LogStatus.INFO, "A comment  is encountered: <br />" + comment);

            }
            extent.endTest(logger);
        }
    @Test()
    public void checkExternalLinks(){
        ExtentTest linkTest = extent.startTest("External TESTS URL =  "+url);

        try{

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
            driver.navigate().refresh();
        }catch (Exception e){linkTest.log(LogStatus.FAIL,"Error in Selenium Script : </br>"+e);}

    }
        @Test
        public void checkBlacklistedKeyowrds() throws Exception{
        logger=extent.startTest("Check for blacklisted keywords. URL = "+url);
            JavascriptExecutor js =(JavascriptExecutor)driver;
            js.executeScript("window.scrollBy(0,300)");
            Thread.sleep(5000);
            String blacklist[]={"ad","pe","banner","overlay","mn","media.net","mnet","project","projectpe","kw","recirc","btf","ads"};
            List elements = driver.findElements(By.cssSelector("*"));
            System.out.println("Number of Elements  = "+elements.size());
            for(int i =0;i<elements.size();i++){
                try{
                   // System.out.println(i);
                    String className = ((WebElement)elements.get(i)).getAttribute("class");
                    String id = ((WebElement)elements.get(i)).getAttribute("id");
                    if(className.equals("")&&id.equals(""));
                    else if (className.equals(" ")&&id.equals(" "));
                    else {
                        String lowID =id.toLowerCase();
                        String lowClass = className.toLowerCase();
                        boolean flag = false;
                        String keyword ="";
                        for(int x =0;x<blacklist.length;x++) {
                            Pattern p = Pattern.compile("(.*([^a-zA-Z]|\\s)+"+blacklist[x]+"([^a-zA-Z]|\\s)+.*)|("+blacklist[x]+"([^a-zA-Z]|\\s)+.*)|(.*([^a-zA-Z]|\\s)+"+blacklist[x]+")|"+blacklist[x]);
                            boolean classMatch = p.matcher(lowClass).matches();
                            boolean idMatch = p.matcher(lowID).matches();
                            if(classMatch||idMatch) {
                                flag = true;
                                keyword=blacklist[x];
//                                if(classMatch)
//                                    System.out.println(lowClass+" Class has matched, Pattern = "+p);
//                                if(idMatch)
//                                    System.out.println(lowID+" ID has matched, Pattern = "+p);
                            }
                        }
                        if(flag){
                            System.out.println(/*"Element Name : " +((WebElement)elements.get(i)).getText() + */" , Class = " + className + ", ID = " + id);
                            logger.log(LogStatus.WARNING,"Keyword Found =<strong>"+keyword+"</strong><br />Element Class = " + className + ", <br />ID = " + id);
                        }
//                        if(lowClass.contains("ad")||lowClass.contains("pe")||lowClass.contains("banner")||lowClass.contains("project")||lowClass.contains("mnet")||lowClass.contains("media.net")||lowClass.contains("kw")||lowClass.contains("btf")||lowClass.contains("recirc")||lowClass.contains("overlay")
//                        ||lowID.contains("ad")||lowID.contains("pe")||lowID.contains("banner")||lowID.contains("project")||lowID.contains("mnet")||lowID.contains("media.net")||lowID.contains("kw")||lowID.contains("btf")||lowID.contains("recirc")||lowID.contains("overlay"))
//                        System.out.println("Element Name : " +((WebElement)elements.get(i)).getText() + " , Class = " + className + ", ID = " + id);

                    }
                }
                catch (Exception e){
                   // e.printStackTrace();
                }
            }
            extent.endTest(logger);
        }
    @Test()
    public void SEOChecks(){

        ExtentTest SEOTest = extent.startTest("SEO TESTS URL =  "+url);

        try{

            if(driver.findElements(By.tagName("h1")).size()>1){
                System.out.println("More than 1 H1 found");
                SEOTest.log(LogStatus.FAIL,"Total number of H1 Tags : "+driver.findElements(By.tagName("h1")).size());
            }
            else
                SEOTest.log(LogStatus.PASS,"Only 1 H1 Tag");
            extent.endTest(SEOTest);
        }catch (Exception e){SEOTest.log(LogStatus.FAIL,"Error in Selenium Script : </br>"+e);extent.endTest(SEOTest);}
        extent.endTest(SEOTest);

    }
    @Test(priority = 3)
    public void pixelTracking() {
        logger = extent.startTest("On page load Pixel tracking");
        String scriptToExecute = "var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {}; var network = performance.getEntries() || {}; return network;";
        String[] pixels = {"tfa.js", "tr.snapchat.com/p","chartbeat.js", "https://widgets.outbrain.com/outbrain.js", "cdn.keywee.co/dist/analytics.min.js", "www.google-analytics.com/analytics.js", "b.scorecardresearch.com", "connect.facebook.net/en_US/fbevents.js", "https://s.yimg.com/wi/ytc.js", "amplify.outbrain.com/cp/obtp.js"};

        String net = ((JavascriptExecutor) driver).executeScript(scriptToExecute).toString();
        List<String> networkCalls = Arrays.asList(net.split("}, \\{"));
        System.out.println(networkCalls);

//        for (String s : pixels) {
//            checkIfPresent(net, s);
//        }
//        System.out.println("numcalls = "+networkCalls.size());
//
//        System.out.println("\n**********************************************************************\n");
//        for(String network : networkCalls){
//
//            network= network.toLowerCase();
//            if(network.contains("tfa.js")||network.contains("chartbeat.js")||network.contains("https://widgets.outbrain.com/outbrain.js")||network.contains("cdn.keywee.co/dist/analytics.min.js")||network.contains("www.google-analytics.com/analytics.js")||network.contains("b.scorecardresearch.com")||network.contains("connect.facebook.net/en_US/fbevents.js")||network.contains("https://s.yimg.com/wi/ytc.js")||network.contains("amplify.outbrain.com/cp/obtp.js")) {
//                System.out.println(network);
//                logger.log(LogStatus.PASS,network+"<br/> <br>"+network.substring(network.indexOf("initiatortype"),network.indexOf("next")));
//                System.out.println(network.substring(network.indexOf("initiatortype"),network.indexOf("next")));
//            }
//        }

        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

        String entire="";
        for(LogEntry le :entries){
            entire = entire+":"+le.toString();
        }
        for (String s : pixels) {
            checkIfPresent(entire, s);
        }
        System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");
        for (LogEntry entry : entries) {
            if (entry.toString().contains("tfa.js") || entry.toString().contains("chartbeat.js") || entry.toString().contains("tr.snapchat.com/p") || entry.toString().contains("https://widgets.outbrain.com/outbrain.js") || entry.toString().contains("cdn.keywee.co/dist/analytics.min.js") || entry.toString().contains("www.google-analytics.com/analytics.js") || entry.toString().contains("b.scorecardresearch.com") || entry.toString().contains("connect.facebook.net/en_US/fbevents.js") || entry.toString().contains("https://s.yimg.com/wi/ytc.js") || entry.toString().contains("amplify.outbrain.com/cp/obtp.js")) {

                System.out.println(new Date(entry.getTimestamp()) + "<br /> " + entry.getLevel() + " <br />" + entry.getMessage());
                logger.log(LogStatus.PASS,new Date(entry.getTimestamp()) + "<br /> " + entry.getLevel() + " <br />" + entry.getMessage());
            }
            extent.endTest(logger);
        }
    }


    @Test(priority = 4)
    public void onClickPixelTracking()throws Exception{
        logger=extent.startTest("On Click Pixel tracking");
        String scriptToExecute = "var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {}; var network = performance.getEntries() || {}; return network;";
        String[] pixels ={"tfa","keywee.co","www.google-analytics.com","tr.snapchat.com/p"};
        String previous=driver.getCurrentUrl();
        int i =1;
        try{

            WebElement w = driver.findElement(By.xpath("//span[@class='media-credit']"));
            ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", w);

            Thread.sleep(5000);
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
            System.out.println(e);
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
                    //System.out.println("NO IFS Default button :"+driver.getCurrentUrl());
                }
        }
        Thread.sleep(3000);
        String net = ((JavascriptExecutor)driver).executeScript(scriptToExecute).toString();
        List <String>networkCalls = Arrays.asList(net.split("}, \\{"));
        System.out.println(networkCalls);

//        for(String s:pixels)
//        {
//            checkIfPresent(net,s);
//        }
//        System.out.println("numcalls = "+networkCalls.size());
//        System.out.println("\n**********************************************************************\n");
//        for(String network : networkCalls){
//
//            network= network.toLowerCase();
//            if(network.contains("tfa.js")||network.contains("cdn.keywee.co/dist/analytics.min.js")||network.contains("www.google-analytics.com/analytics.js")) {
//                System.out.println(network);
//                logger.log(LogStatus.PASS,network+"<br/> <br>"+network.substring(network.indexOf("initiatortype"),network.indexOf("next")));
//                System.out.println(network.substring(network.indexOf("initiatortype"),network.indexOf("next")));
//            }
//        }

        Thread.sleep(15000);
        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

        String entire="";
        for(LogEntry le :entries){
            entire = entire+":"+le.toString();
        }
        for (String s : pixels) {
            checkIfPresent(entire, s);
        }
        System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");
        for (LogEntry entry : entries) {
            if (entry.toString().contains("tfa") || entry.toString().contains("keywee.co") || entry.toString().contains("tr.snapchat.com/p") || entry.toString().contains("www.google-analytics.com")) {
                logger.log(LogStatus.PASS,new Date(entry.getTimestamp()) + "<br /> " + entry.getLevel() + " <br />" + entry.getMessage());

                System.out.println("Matched"+new Date(entry.getTimestamp()) + "<br /> " + entry.getLevel() + " <br />" + entry.getMessage());
            }
            extent.endTest(logger);
        }

        extent.endTest(logger);
    }
    @Test(priority = 5)
    public void checkHTMLEntities() throws Exception {
        logger=extent.startTest("Check for HTML Entities");
        Pattern p = Pattern.compile("(.*&#[0-9]{1,4};.*)|(.*&[a-z A-Z]{1,4};.*)");

        //driver.get("https://www.forbes.com/advisor");
       // WebElement element = driver.findElement(By.tagName("p"));
        //((JavascriptExecutor) driver).executeScript("var ele=arguments[0]; ele.innerText = 'sasdss&#60;sadasdds';", element);



        boolean error = false;
        boolean flag = true;
        while (flag) {
            String body = driver.findElement(By.tagName("body")).getText();
            //System.out.println(body);
            //Matcher m = p.matcher(body);
            //Matcher k = p.matcher("sdfsddf&nbsp;dsdfddf");
            String items[] = body.split("\\n");
            for (String item : items) {
                //    System.out.println(p.matcher(item).matches() + " : " + item);
                if (p.matcher(item).matches()) {
                    System.out.println("Entity found : " + item);
                    error=true;
                    logger.log(LogStatus.FAIL, "HTML Entity found : " + item);
                }
            }
            if(p.matcher(driver.getTitle()).matches()){
                System.out.println(driver.getTitle());
                System.out.println("Entity found : " + driver.getTitle());
                error=true;
                logger.log(LogStatus.FAIL, "HTML Entity found in Title: " + driver.getTitle());

            }
            try{

                WebElement w = driver.findElement(By.xpath("//span[@class='media-credit']"));
                ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView(true);", w);

                Thread.sleep(500);
                driver.findElement(By.xpath("//div[@class='continue']")).click();
            }
            catch (Exception e){
                System.out.println(e);
                try{
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
                    Thread.sleep(500);
                    ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");

                    driver.findElement(By.className("next-slide-btn-text")).click();

                }
                catch (Exception ex) {

                    System.out.println(e);
                    flag=false;
                }

            }
        }
        if(!error){
            logger.log(LogStatus.PASS,"NO HTML Entities found!");
        }
        extent.endTest(logger);
    }
    @Test(priority = 6)
    public void DFPCheck() throws Exception{
        ExtentTest DFPTest = extent.startTest("DF TESTS URL =  "+url);

        try{

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
                 if(topAd.size()==0&&recAds.size()==0)
                {
                    List a = driver.findElements(By.tagName("iframe"));
                   System.out.println("Total Iframes = " + a.size());


                    for (int i = 0; i < a.size(); i++) {
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

                    }
                    a = null;
                    System.out.println("********************************");
                    System.out.println("Total Ad Count = " + adcount);
                    System.out.println("MN Ad Count = " + mnAds);
                    System.out.println("DFP Ad Count = " + dfpAds);
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
        }catch (Exception e){
            System.out.println(e+"\n\n"+e.getMessage()+"\n\n"+e.getStackTrace());
            e.printStackTrace();
            DFPTest.log(LogStatus.FAIL,"Error in Selenium Script : </br>"+e);
            extent.endTest(DFPTest);}

    }
    @Test(priority = 6)
    public void checkLogs(){
        consoleTest = extent.startTest("Check for JS Console Warnings and Errors");
        analyzeLog(driver);
        extent.endTest(consoleTest);
    }

    @Test()
    public void checkAnalytics(){
        ExtentTest AnalyticsTest = extent.startTest("Analytics TESTS URL =  "+url);

        try {

            String source = driver.getPageSource();

            int startGA = source.indexOf("GTM-");
            int stopGA = source.indexOf("GTM-") + 11;
            int difference = stopGA - startGA;
            // System.out.println("Diff ="+difference+"String :" +source.substring(startGA,stopGA)+"\nsTRT = "+startGA+" "+stopGA);
            int k = 2;
            String base = source.substring(startGA, stopGA);
            while (source.indexOf("GTM-", stopGA + 1) != -1) {
                //System.out.println("\n\nOld : "+startGA +" "+ stopGA+" "+difference+" ");
                startGA = source.indexOf("GTM-", stopGA + 1);
                stopGA = startGA + 11;
                difference = stopGA - startGA;
                String ga = "";
                if (difference > 4) {
//                    System.out.println("New : " + startGA + " " + stopGA + " " + difference + " ");
                    System.out.println("GTM #" + k + ", Length = " + difference);

                    ga = source.substring(startGA, stopGA);
                    k++;
                }
                if (!base.equalsIgnoreCase(ga)) {
                    System.out.println("FAILURE!!!!");
                    AnalyticsTest.log(LogStatus.FAIL, "GA found = " + ga + "</br> Expected value = " + base);
                } else
                    AnalyticsTest.log(LogStatus.PASS, "GA found = " + ga + "</br> Expected value = " + base);

                System.out.println(ga);
            }
        }
        catch(Exception ex){
            AnalyticsTest.log(LogStatus.FAIL, "NO GTM Found!!");
            System.out.println("NO GTM");
        }
        try{
            System.out.println("Checking UA");
            String source = driver.getPageSource();
            int startGA = source.indexOf("UA-");
            int stopGA = source.indexOf("UA-")+13;

            int difference = stopGA-startGA;
            String base = source.substring(startGA,stopGA);
            //   System.out.println("Diff ="+difference+"String :" +source.substring(startGA,stopGA)+"\nsTRT = "+startGA+" "+stopGA);
            int k =2;
            if(source.indexOf("UA-",stopGA+1)==-1){
                System.out.println("UA Found only once "+base);
                AnalyticsTest.log(LogStatus.INFO,"UA found only once : "+base);
            }
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

            String websource = driver.getPageSource();
            String ga = "www.google-analytics.com/analytics.js";
            if (websource.contains(ga)) {
                System.out.println("Analytics Script present");
                logger.log(LogStatus.PASS, ga+" : Analytics Script present");
            } else
                logger.log(LogStatus.FAIL, ga + " : Analytics Script not present");

            extent.endTest(AnalyticsTest);
        }catch (StringIndexOutOfBoundsException se){
            AnalyticsTest.log(LogStatus.FAIL,"No UA found!");
            extent.endTest(AnalyticsTest);
        }

        catch (Exception e){AnalyticsTest.log(LogStatus.FAIL,"Error in Selenium Script : </br>"+e);extent.endTest(AnalyticsTest);}

    }

    @AfterTest
    public void closeTest()throws Exception{
        //Thread.sleep(50000);
       driver.quit();
       // logger.getTest().setStatus(LogStatus.PASS);
        extent.endTest(logger);
        //extent.endTest(logger);
        extent.flush();
        extent.close();
    }


    public void checkIfPresent (String code, String key){
        if (code.contains(key)) {
            String mess = key+" : Pixel tracked";
            System.out.println(mess);
            mess=mess.replace("<", "&lt; ").replace(">", "&gt;");
            logger.log(LogStatus.PASS, mess);
        } else {
            String mess = key+": Pixel Not found!";
            System.out.println(mess);
            mess=mess.replace("<", "&lt; ").replace(">", "&gt;");
            logger.log(LogStatus.FAIL, mess);
        }
    }

    public void analyzeLog(WebDriver driver) {
        LogEntries logEntries = driver.manage().logs().get(LogType.BROWSER);
        String errors ="";
        String warnings = "";
        for (LogEntry entry : logEntries) {
            System.out.println(entry);
            if ((!entry.toString().contains("WARNING"))/*&&!entry.toString().contains("forbes")*/)
                errors = errors+(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage()+"<br /><br />");
            else/*((entry.toString().contains("forbes"))||(entry.toString().contains("SEVERE")))*/
                warnings = warnings+(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage()+"<br /><br />");

        }
        System.out.println("Warnings = \n"+warnings);
        System.out.println("Errors = \n"+errors);
        if(!warnings.equals(""))
            consoleTest.log(LogStatus.WARNING,"The page has warnings : <br />"+warnings);
        if(!errors.equals(""))
            consoleTest.log(LogStatus.ERROR,"The page has Errors : <br />"+errors);
//        if(!errors.isEmpty()){
//        driver.quit();
//            Assert.fail("There were Console errors");
//        }
    }
}