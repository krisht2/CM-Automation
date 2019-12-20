package Advisor;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.logging.Logs;
import org.testng.annotations.*;
import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;

import org.openqa.selenium.*;
import org.apache.commons.collections4.CollectionUtils;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;

public class AdvisorHeader {

    private WebDriver driver;
    ExtentReports extent;
    ExtentTest logger;
    ExtentTest seqlogger;
    String extentReportFile = System.getProperty("user.dir")+"\\AdvisorHeader.html";
    String extentReportImage = System.getProperty("user.dir")+"\\extentReportImage.png";
    ChromeOptions caps = new ChromeOptions();
    WebDriver window;

    ArrayList<String> headers = new ArrayList<String>();
    ArrayList<String> submenu = new ArrayList<String>();
    ArrayList<String> url = new ArrayList<String>();

    ArrayList<String> headers2 = new ArrayList<String>();
    ArrayList<String> submenu2 = new ArrayList<String>();
    ArrayList<String> url2 = new ArrayList<String>();

    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {



        String chromeDriverPath = "C:\\Users\\krish.t\\Downloads\\chromedriver.exe";
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        extent = new ExtentReports(extentReportFile, true);
        extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));


        //For Server
        //  caps.addArguments("--headless");
        // caps.addArguments("--no-sandbox");
        driver = new ChromeDriver(caps);
        driver.manage().window().maximize();

    }

    @Test
    public void advisorHeaderCheck() throws Exception {
        driver.get("https://www.forbes.com/advisor");
        driver.manage().window().setSize(new Dimension(1366,driver.manage().window().getSize().getHeight()));
        logger=extent.startTest("Compare Headers in Advisor","Check for differences in headers across forbes.com and Advisor page");
//        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
//        FileUtils.copyFile(screenshotFile, new File(extentReportImage));
//        logger.log(LogStatus.INFO,logger.addScreenCapture(extentReportImage));
        WebElement headerMenu = driver.findElement(By.className("menu-header-forbes-style-container"));
        List e= headerMenu.findElements(By.tagName("ul"));
        for(int i =0;i<e.size();i++) {
            WebElement header = ((WebElement) e.get(i));
           String s[]= header.getText().split("\n");
           for(String k : s){if(k.length()>1) headers.add(k);}
           if (header.getText() != ""&&header.getText() !=null&&header.getText() !=" "&& !(header.getText().equalsIgnoreCase("")))
            {

                System.out.println("Header = " + header.getText());
                List subMenu = header.findElements(By.tagName("a"));
                  System.out.println("Size of submenu = "+subMenu.size());
                  //headers.add(header.getText());
                String all = "All "+header.getText();
                System.out.println("All value = "+all);
                for (int j = 0; j < subMenu.size(); j++) {

                    WebElement subMenuItem = (WebElement) subMenu.get(j);
                    String text = subMenuItem.getAttribute("innerHTML");
                    if(!text.contains("<span>")&&!text.contains("<div>")&&!text.equalsIgnoreCase(all)&&!text.contains("<span>")&&!text.contains("Branc"))
                    System.out.println("Submenu item = "+subMenuItem.getAttribute("innerHTML"));
                    System.out.println("Submenu item URL = "+subMenuItem.getAttribute("href"));
                    int flag=0;
                   for(int p =0;p<headers.size();p++){
                      // System.out.println("Comparing "+headers.get(p)+" with : "+subMenuItem.getAttribute("innerHTML"));

                       if(subMenuItem.getAttribute("innerHTML").equalsIgnoreCase(headers.get(p)))
                           flag = 1;
                   }
                   if(flag==0){
                    url.add(subMenuItem.getAttribute("href"));
                    submenu.add(subMenuItem.getAttribute("innerHTML"));
                     }
                    if(subMenuItem.getAttribute("innerHTML").equalsIgnoreCase("Lists"))
                    {
                        url.add("https://www.forbes.com/lists/");
                        submenu.add("All Lists");
                    }
                }
                System.out.println("*********************************************************************");
            }
        }
        System.out.println("Navigating to Industry page");
        System.out.println("**********************************\n\n");

        driver.get("https://www.forbes.com/");

         headerMenu = driver.findElement(By.className("header__channels"));
         e= headerMenu.findElements(By.tagName("ul"));
         e.remove(e.size()-1);
        List temp = driver.findElement(By.xpath("//ul[@class='header__channels']")).findElements(By.tagName("li"));
        for(int i =0;i<temp.size();i++){
            try {
                String k = ((WebElement) temp.get(i)).findElement(By.className("header__title")).getText();

                if(k.length()>1) {
                    System.out.println(k);
                    headers2.add(k);
                }
            }            catch (Exception ex){}

        }
        for(int i =0;i<e.size();i++) {
            WebElement header = ((WebElement) e.get(i));
           // if (header.getText() != ""&&header.getText() !=null&&header.getText() !=" "&& !(header.getText().equalsIgnoreCase("")))
            {
                System.out.println("Header = " + header.getText());
                List subMenu = header.findElements(By.tagName("a"));
                System.out.println("Size of submenu = "+subMenu.size());
                //headers2.add(header.getText());
                for (int j = 0; j < subMenu.size(); j++) {
                    WebElement subMenuItem = (WebElement) subMenu.get(j);
                    String subText= subMenuItem.getAttribute("innerHTML");
                    System.out.println("Submenu item = "+subText);
                    System.out.println("Submenu item URL = "+subMenuItem.getAttribute("href"));
                    url2.add(subMenuItem.getAttribute("href"));
                    if(subText.contains("span")) {
                        //System.out.println("Flagged!!" + subText.substring(subText.indexOf("<span>")+6,subText.indexOf("</span>")));
                        submenu2.add(subText.substring(subText.indexOf("<span>")+6,subText.indexOf("</span>")));
                    }
                    else
                    submenu2.add(subMenuItem.getAttribute("innerHTML"));
                }
                System.out.println("*********************************************************************");
            }
        }
        for(int x =0;x<headers2.size();x++) {
            headers2.set(x,headers2.get(x).trim());
            headers2.set(x, headers2.get(x).replace("’", "'"));
        }
        for(int x =0;x<url2.size();x++){
            url2.set(x,url2.get(x).replace("’","'"));
            url2.set(x,url2.get(x).trim());
            if(url2.get(x).endsWith("/"))
                url2.set(x,url2.get(x).substring(0,url2.get(x).length()-1));
        }
        for(int x =0;x<submenu2.size();x++) {
            submenu2.set(x,submenu2.get(x).trim());
            submenu2.set(x, submenu2.get(x).replace("’", "'"));
            submenu2.set(x, submenu2.get(x).replace("  ", " "));
        }

        for(int x =0;x<headers.size();x++) {
            headers.set(x, headers.get(x).replace("’", "'"));
            headers.set(x,headers.get(x).trim());
        }
        for(int x =0;x<url.size();x++){
            url.set(x,url.get(x).replace("’","'"));
            if(url.get(x).endsWith("/"))
                url.set(x,url.get(x).substring(0,url.get(x).length()-1));
            url.set(x,url.get(x).trim());
        }
        for(int x =0;x<submenu.size();x++) {
            submenu.set(x, submenu.get(x).replace("’", "'"));
            submenu.set(x, submenu.get(x).replace("  ", " "));
            submenu.set(x,submenu.get(x).trim());
        }
        ArrayList<String> differ = (ArrayList<String>) headers.clone();
        ArrayList<String> differ2 = (ArrayList<String>) headers2.clone();
            if(headers2.equals(headers)){
                System.out.println("All the Menu headers are same!!!");
                logger.log(LogStatus.PASS,"All the main header items are same across both the pages");

            }
            else {
                System.out.println(headers);
                System.out.println(headers2);

            headers= (ArrayList<String>) CollectionUtils.subtract(headers,headers2);
            headers2= (ArrayList<String>) CollectionUtils.subtract(headers2,differ);
            System.out.println("\nHeader Mismatch!!! Reporting needed!!!!\n");
            System.out.println("Headers to be removed from Advisor page: "+headers);
            System.out.println("Headers to be added to Advisor page: "+headers2);
            logger.log(LogStatus.FAIL,"Headers to be removed from Advisor page: <br />"+headers);
            logger.log(LogStatus.FAIL,"Headers to be added to Advisor page: <br />"+headers2);


            }
        extent.endTest(logger);
        logger=extent.startTest("SubMenu Items Test","Check for differences in SubMenu Items across forbes.com and Advisor page");

//            System.out.println("Sizes equal : "+submenu.size());
            if(submenu2.equals(submenu)){
                System.out.println("All the Sub Menu headers are same!!!");
                logger.log(LogStatus.PASS,"All the Sub Menu items match. No changes needed");
            }
            else {
            System.out.println(submenu);
            System.out.println(submenu2);
            ArrayList<String> diff = (ArrayList<String>) submenu.clone();
            submenu= (ArrayList<String>) CollectionUtils.subtract(submenu,submenu2);
            submenu2= (ArrayList<String>) CollectionUtils.subtract(submenu2,diff);
                System.out.println("Sub Menu item Mismatch!!!\nFound differences in SubMenu items - \n");
            System.out.println("SubMenu items to be removed from Advisor page: "+submenu);
            System.out.println("SubMenu items to be added to Advisor page: "+submenu2);
            logger.log(LogStatus.FAIL,"SubMenu items to be removed from Advisor page: <br />"+submenu);
            logger.log(LogStatus.FAIL,"SubMenu items to be added to Advisor page: <br />"+submenu2);
        }

        extent.endTest(logger);
        logger=extent.startTest("Compare URLs in Advisor","Check for differences in URLs across forbes.com and Advisor page");
            if(url.equals(url2)){
                System.out.println("All the URLs are same!!!");
                logger.log(LogStatus.PASS,"All the URLs are same. No changes needed!");
            }
            else{
            System.out.println(url);
            System.out.println(url2);
                ArrayList<String> diff = (ArrayList<String>) url.clone();
                url= (ArrayList<String>) CollectionUtils.subtract(url,url2);
                url2= (ArrayList<String>) CollectionUtils.subtract(url2,diff);
                System.out.println("\nURL Mismatch!!! Found differences in URLs - \n");
                System.out.println("URLs to be removed from Advisor page: "+url);
                System.out.println("URLs to be added to Advisor page: "+url2);
                logger.log(LogStatus.FAIL,"URLs to be removed from Advisor page: <br />"+url);
                logger.log(LogStatus.FAIL,"URLs to be added to Advisor page: <br />"+url2);
        }
            HashSet<String> changeHeaders= new HashSet<>();
        extent.endTest(logger);
        logger=extent.startTest("Headers which require changes");
        System.out.println("URL = "+driver.getCurrentUrl());
        for(String key:submenu2){
            for(String h: differ2){
                try {
                WebElement link = driver.findElement(By.linkText(h));
                     for(WebElement el :link.findElement(By.xpath("./../div[2]/ul/li")).findElements(By.xpath("*")))
                     {
                         System.out.println("elements : "+el.getText());
                     }

                    Actions a = new Actions(driver);
                a.moveToElement(link).build().perform();
                System.out.println("Searching for "+key+" in "+h);
                    if (link.findElement(By.xpath("./..")).findElement(By.linkText(key)).isDisplayed()) {
                        String u = (link.findElement(By.xpath("./..")).findElement(By.linkText(key)).getAttribute("href"));
                        System.out.println(key+" exists in : " + h);
                       //To be added to Advisor
                        logger.log(LogStatus.WARNING,"Add : "+key+" <br />["+u+"] <br />to : " + h);
                        changeHeaders.add(h);
                    }
                    else
                        System.out.println("Media Does not exists in : " + h);
                }catch (Exception ex){}
            }
            WebElement link = driver.findElement(By.xpath("//span[contains(text(),'Featured')]"));
            Actions a = new Actions(driver);
            a.moveToElement(link).build().perform();
            try{
                if (link.findElement(By.xpath("./..")).findElement(By.linkText(key)).isDisplayed()) {
                    String u = (link.findElement(By.xpath("./..")).findElement(By.linkText(key)).getAttribute("href"));
                    System.out.println(key+" exists in : Featured");
                    //To be added to Advisor

                    logger.log(LogStatus.WARNING,"Add : "+key+" <br />["+u+"] <br/> to : Featured");
                    changeHeaders.add("Featured");
                }
            }
            catch (Exception ex){}

            link = driver.findElement(By.xpath("//span[contains(text(),'More')]"));
            a.moveToElement(link).build().perform();
            try{
                if (link.findElement(By.xpath("./..")).findElement(By.linkText(key)).isDisplayed()) {
                    String u = (link.findElement(By.xpath("./..")).findElement(By.linkText(key)).getAttribute("href"));
                    System.out.println(key+" exists in : More");
                    //To be added to Advisor

                    logger.log(LogStatus.WARNING,"Add : "+key+" <br />["+u+"]<br />to : More");
                    changeHeaders.add("More");
                }
            }
            catch (Exception ex){}
        }



        driver.get("http://www.forbes.com/advisor");
            for(String key:submenu){
                for(String h: differ){
                    key=key.replaceAll("  "," ");
                     WebElement link = driver.findElement(By.linkText(h));
                    Actions a = new Actions(driver);
                     a.moveToElement(link).build().perform();

                    System.out.println("Searching for "+key+" in "+h);
                    try {
                        if (link.findElement(By.xpath("./..")).findElement(By.linkText(key)).isDisplayed()) {
                           String u = (link.findElement(By.xpath("./..")).findElement(By.linkText(key)).getAttribute("href"));
                            System.out.println(key+" exists in : " + h);
                            //To be Removed from Advisor

                            logger.log(LogStatus.WARNING,"Remove : "+key+"<br/> ["+u+"] <br /> from : " + h);


                            changeHeaders.add(h);
                        }
                        else
                            System.out.println("Media Does not exists in : " + h);
                    }catch (Exception ex){}
                }

               // WebElement link = driver.findElement(By.xpath("//span[contains(text(),'Featured')]"));
                Actions a = new Actions(driver);
//                a.moveToElement(link).build().perform();
//                try{
//                    if (link.findElement(By.xpath("./..")).findElement(By.linkText(key)).isDisplayed()) {
//                        System.out.println(key+" exists in : Featured");
//                        logger.log(LogStatus.WARNING,key+" exists in : Featured");
//                        changeHeaders.add("Featured");
//                    }
//                }
//                catch (Exception ex){}

//               rem
            }
            if(changeHeaders.size()>0){
                System.out.println(changeHeaders);
                    logger.log(LogStatus.FAIL,"We need to change the below sections : </br>"+changeHeaders);
            }
            else
                logger.log(LogStatus.PASS,"No changes!!");
        extent.endTest(logger);
        List <WebElement>forbes =  new ArrayList<>();
        List <WebElement>advisor =  new ArrayList<>();
        if(differ2.equals(differ)) {
            seqlogger= extent.startTest("CHECK SEQUENCE");
            driver.get("https://www.forbes.com");
            window= new ChromeDriver();
            window.manage().window().maximize();
            window.manage().window().setSize(new Dimension(1366,driver.manage().window().getSize().getHeight()));
            window.get("https://www.forbes.com/advisor");
            System.out.println(window.getCurrentUrl());
            System.out.println(driver.getCurrentUrl());

            boolean seq = true;
            List <String> forb = new ArrayList<>();
            List <String> adv = new ArrayList<>();
            int max = forbes.size();
            for (String h : differ2) {
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
                        String advisorval="";
                        String forbesval="";
                        try {
                             forbesval = forbes.get(i).findElement(By.tagName("a")).getAttribute("innerText").replaceAll("amp;", "").replaceAll("’", "'").trim();
                             advisorval = advisor.get(i).findElement(By.xpath("./a")).getAttribute("innerHTML").replaceAll("amp;", "").replaceAll("’", "'").trim();
                        }catch (Exception esc){}
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
                    System.out.println(ex);
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
                String advisorval="";
                String forbesval="";
                try {
                    forbesval = forbes.get(i).findElement(By.tagName("a")).getAttribute("innerText").replaceAll("amp;", "").replaceAll("’", "'").trim();
                    advisorval = advisor.get(i).findElement(By.xpath("./a")).getAttribute("innerHTML").replaceAll("amp;", "").replaceAll("’", "'").trim();
                }
                catch (Exception ex){}
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
                String forbesval="";
                String advisorval="";
                try {
                    forbesval = forbes.get(i).findElement(By.tagName("a")).getAttribute("innerText").replaceAll("amp;", "").replaceAll("’", "'").trim();
                    advisorval = advisor.get(i).findElement(By.xpath("./a")).getAttribute("innerHTML").replaceAll("amp;", "").replaceAll("’", "'").trim();
                }catch (Exception ex){}
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

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        extent.endTest(logger);
        extent.endTest(seqlogger);
        extent.flush();
        extent.close();
        window.quit();
        driver.quit();
        try {
            //if (logger.getTest().getStatus().equals(LogStatus.FAIL))
            //sendEmail();
            System.out.println(logger.getTest().getStatus());
        }
        catch (Exception e){System.out.println(e);}
    }

    public void sendEmail() throws EmailException {
        EmailAttachment attachment = new EmailAttachment();
        attachment.setPath(System.getProperty("user.dir")+"\\AdvisorHeader.html");
        attachment.setDisposition(EmailAttachment.ATTACHMENT);
        String to []={"krish.t@media.net","saili.m@media.net","fenil.g@media.net"};

        MultiPartEmail email = new MultiPartEmail();
        email.setHostName("smtp.googlemail.com");
        email.setSmtpPort(587);
        email.setAuthenticator(new DefaultAuthenticator("krish.t@media.net", "fstomkzuhqlyvcuv"));
        email.setSSLOnConnect(true);
        email.addTo(to);
        email.setFrom("krish.t@media.net", "Krish");
        email.setSubject("Advisor - Header Test - Automation Report");
        email.setMsg("PFA The below Advisor Header Test Report");
        email.attach(attachment);
        email.send();
    }
}