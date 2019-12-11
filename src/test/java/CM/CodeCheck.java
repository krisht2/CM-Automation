package CM;

import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CodeCheck {
    private WebDriver driver;
    ExtentReports extent;
    ExtentTest logger;
    String extentReportFile = System.getProperty("user.dir")+"\\CMCodeTest.html";
    String extentReportImage = System.getProperty("user.dir")+"\\extentReportImage.png";
    String chromeDriverPath = "C:\\Users\\krish.t\\Downloads\\chromedriver.exe";

    String framworkLink="https://cm.internal.reports.mn/template/framework/preview.php?frm_id=800078864&pagename=keywords-only-1051x470.html\n";
    CMPages c;
    Config config = new Config();
    String password=config.password;
    String domain = config.domain;
    String userName=config.userName;
   String email = config.email;
    String adType ="keywords-only.html";
    String width = config.width;
    String height = config.height;
    WebDriverWait wait;
    String res = width+" x "+height;
    String blacklist[]={"ad","pe","banner","overlay","mn","media.net","mnet","project","about:_blank","projectpe","kw","recirc","btf","ads","forbes","ga","google","badabhai",".js","window.onload","line1","loose.dtd","detect text"};


    String docType="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitional//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">";
    String xml ="<html xmlns=\"http://www.w3.org/1999/xhtml\">";
    String stlhead="<stl_head>";
    String title ="<title><tag:domain_name /></title>";
    String viewport="<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no\">";
    String charset="<meta http-equiv=\"Content-Type\" content=\"text/html; charset=<tag:charset />\">";
    String resetCSS="<tag:reset_css />";
    String IOSWidth =".ios_fix{width: 1px; min-width: 100%; *width: 100%;}";
    String tagPost="<tag:post_form_html />";
    String footer ="<a class=\"footer\" href=\"<tag:mnet_adchoice_link />\" target=\"_blank\"></a>";

    ArrayList<String>imageList = new ArrayList<>();
    String code ="";

    @BeforeTest
    public void setUp(){
        System.setProperty("webdriver.chrome.driver", chromeDriverPath);

        extent = new ExtentReports(extentReportFile, true);
        extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));

        ChromeOptions caps = new ChromeOptions();
        //For Server
        //  caps.addArguments("--headless");
        // caps.addArguments("--no-sandbox");
        driver = new ChromeDriver(caps);
        driver.manage().window().maximize();
        c = new CMPages(driver);
        wait =new WebDriverWait(driver, 30);
    }
    @Test
    public void checkCode()throws Exception{
        logger= extent.startTest("Check for Mandates in Code");
        String id=framworkLink.substring(framworkLink.indexOf("=")+1,framworkLink.indexOf("&"));
        String frmLink = "https://cm.internal.reports.mn/template/framework/pagemapping.php?id=";
        frmLink=frmLink+id;
        System.out.println("Framework Internal Link = "+frmLink);

        driver.get(frmLink);
        //c.clickSSO();
        c.setSsoID(email);
        c.clickSSOIDNext();
        c.setSsoPassword(password);
        c.clickSSOPasswordNext();
        Thread.sleep(30000);

        List resolutions = driver.findElements(By.xpath("//center/div/table[@class=\"general\"]//tr"));
        System.out.println("Resolution is : "+ res);
        for(int l =0;l<(resolutions.size()-2);l++) {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/center/div/table[1]/tbody/tr[" + (l + 3) + "]/td[1]")));
            WebElement e = driver.findElement(By.xpath("//body/center/div/table[1]/tbody/tr[" + (l + 3) + "]/td[1]"));
            String type = e.getText();
            //   System.out.println("Value = "+type);

            WebElement size = driver.findElement(By.xpath("//body/center/div/table[1]/tbody/tr[" + (l + 3) + "]/td[2]"));
            String resolution = size.getText();
            //  System.out.println("Value = "+resolution);

            if (type.contains(adType) && resolution.contains(height) && resolution.contains(width)) {
                System.out.println("type = " + type + " & Reso: " + resolution);
                driver.findElement(By.xpath("//tr[" + (l + 3) + "]//td[4]//a")).click();
                break;
            }


            imageList=new ArrayList();
            List<WebElement> images = driver.findElements(By.xpath("//table[@id=\"imageTable\"]//tr"));
            System.out.println("total images = "+images.size());
            int i =2;
            for(WebElement w:images ){
                if(i<=images.size()){
                    System.out.println(driver.findElement(By.xpath("//table[@id=\"imageTable\"]//tr["+i+"]//td[2]")).getText());
                    imageList.add((driver.findElement(By.xpath("//table[@id=\"imageTable\"]//tr["+i+"]//td[2]")).getText()));
                    i++;

                }
            }
            System.out.println(images);
        }

        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        System.out.println(driver.getCurrentUrl());
        code = c.getSourceCode();
        System.out.println("Actual Dev Source = "+code);
        c.clickSubmit();
        Thread.sleep(10000);



//
//        code="<!DOCTYPE html PUBLIC \"-//W3C//DTD XHTML 1.0 Transitil//EN\" \"http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd\">\n" +
//                "<html xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
//                "<stl_head>\n" +
//                "<title><tag:domain_name /></title>\n" +
//                "<meta name=\"viewport\" content=\"width=device-width,initial-scale=1,maximum-scale=1,user-scalable=no\">\n" +
//                "<meta http-equiv=\"Content-Type\" content=\"text/html; charset=<tag:charset />\">\n" +
//                "<tag:adjust_iframe_height_to_body_definition />\n" +
//                "<tag:reset_css />\n" +
//                "<style type=\"text/css\">\n" +
//                ".ios_fix{width: 1px; min-width: 100%; *width: 100%;}\n" +
//                ".wrapper{position: relative;font-family: <tagd:style name=\"kw\" value=\"Merriweather-Regular\" type=\"font-family\" />, Arial, sans-serif;background: transparent;max-width:1000px;margin: 0 auto;}\n" +
//                ".header{font-size: 12px;color: #000;line-height: 24px;}\n" +
//                ".list_wrapper{overflow: hidden;}\n" +
//                "ul{width: 49.50%;margin-left: 1%;float: left;}\n" +
//                "ul.list_1{margin-left: 0;}\n" +
//                "li{overflow: hidden;position: relative;margin-top: 6px;}\n" +
//                "li:first-child{margin-top: 0;}\n" +
//                "li .content{position: relative;overflow: hidden;display:block;height: 80px;border: 1px solid #dfdfdf;}\n" +
//                "li .img_wrap{position: relative;width: 95px;height: 70px;position: absolute;top: 5px;left: 5px;}\n" +
//                "li .img_wrap .dispImg{position: absolute; height: 100%;width: 100%; background-size: cover;background-position:center;}\n" +
//                "li .kw_wrap{position: relative;overflow: hidden;padding: 8px 0 0 144px;}\n" +
//                "li .number{position: absolute;height:28px;width: 34px;left:100px;font-size: 17px;color: #000;line-height: 22px;border-right:1px solid #dfdfdf;top: 5px;text-align: center;}\n" +
//                "li .arrow{width: 20px;height:20px;background:#333333 url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAkAAAAJCAYAAADgkQYQAAAAAXNSR0IArs4c6QAAAFpJREFUGBljYEAD////rwZifTRhTC5Q0RUg1saUQRMBKvoAxPwgYUYgIxpNHsblADImMTIycrMAGZ4wUSz0P6AhfFjEIUJAyYdALItPwQmgAjN8CqrwKsClEwDRwDGICV45ngAAAABJRU5ErkJggg==') center no-repeat;margin-left: 144px;border-radius: 50%;box-shadow: 0 2px 4px 0 rgba(0, 0, 0, 0.2);}\n" +
//                "li table{table-layout: fixed;width: 100%; word-wrap:break-word;}\n" +
//                "li .anchortext{font-size: 14px;line-height:19px;max-height: 38px;color:#222222;padding: 0 10px 0 0; word-wrap: break-word; overflow: hidden;}\n" +
//                "li .dspn{display: none;}\n" +
//                "li .anchorhref{position: absolute; display: block; width: 100%; height: 100%; left: 0; top: 0; overflow: hidden;z-index: 999; background:url('data:image/png;base64,iVBORw0KGgoAAAANSUhEUgAAAAEAAAABCAYAAAFoEvQfAAAAGXRFWHRTb2Z0d2FyZQBBZG9iZSBJbWFnZVJlYWR5ccllPAAAAA1JREFUeNpj+P//PwMACPwC/njEsrAAAAAASUVORK5CYII=') right top no-repeat}\n" +
//                "li:hover .content{border-color:#333333}\n" +
//                "li:hover .anchortext{text-decoration: underline;}\n" +
//                "a.footer{position:absolute; top: 4px; right:0px; width:15px; height:15px; background:url('data:image/png;base64,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') top right no-repeat}\n" +
//                "a.footer:hover{width: 75px;}\n" +
//                "</style>\n" +
//                "</stl_head>\n" +
//                "<stl_body>\n" +
//                "<tag:post_form_html />\n" +
//                "<div class=\"ios_fix\">\n" +
//                "<div class=\"wrapper\">\n" +
//                "<div class=\"header\">See Also</div>\n" +
//                "<div class=\"list_wrapper\">\n" +
//                "<ul class=\"list_1\">\n" +
//                "<li>\n" +
//                "\t<div class=\"content\">\n" +
//                "\t<div class=\"number\">1</div>\n" +
//                "\t<div class=\"img_wrap\">\n" +
//                "\t\t<kbb_image_bg:1 src='<tag:image_path_new />/fallback1.png' tag='div' width='100' height='75' class=\"dispImg\"><kbb_image_bg:1 />\n" +
//                "\t</div>\n" +
//                "\t<div class=\"kw_wrap\">\n" +
//                "\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"left\">\n" +
//                "\t\t\t<tr><td width=\"100%\" align=\"left\" valign=\"top\" height=\"44\">\n" +
//                "\t\t\t\t<div class=\"dck1 anchortext\"></div>\n" +
//                "\t\t\t</td></tr>\n" +
//                "\t\t</table>\n" +
//                "\t</div>\n" +
//                "\t<div class=\"arrow\"></div>\n" +
//                "\t</div>\n" +
//                "\t<a href=\"#\" class=\"dak1 anchorhref\"></a>\n" +
//                "\t<div class=\"dspn\"><domain_keyword:1 /></div>\n" +
//                "</li>\n" +
//                "<li>\n" +
//                "\t<div class=\"content\">\n" +
//                "\t<div class=\"number\">2</div>\n" +
//                "\t<div class=\"img_wrap\">\n" +
//                "\t\t<kbb_image_bg:2 src='<tag:image_path_new />/fallback2.png' tag='div' width='100' height='75' class=\"dispImg\"><kbb_image_bg:2 />\n" +
//                "\t</div>\n" +
//                "\t<div class=\"kw_wrap\">\n" +
//                "\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"left\">\n" +
//                "\t\t\t<tr><td width=\"100%\" align=\"left\" valign=\"top\" height=\"44\">\n" +
//                "\t\t\t\t<div class=\"dck2 anchortext\"></div>\n" +
//                "\t\t\t</td></tr>\n" +
//                "\t\t</table>\n" +
//                "\t</div>\n" +
//                "\t<div class=\"arrow\"></div>\n" +
//                "\t</div>\n" +
//                "\t<a href=\"#\" class=\"dak2 anchorhref\"></a>\n" +
//                "\t<div class=\"dspn\"><domain_keyword:2 /></div>\n" +
//                "</li>\n" +
//                "<li>\n" +
//                "\t<div class=\"content\">\n" +
//                "\t<div class=\"number\">3</div>\n" +
//                "\t<div class=\"img_wrap\">\n" +
//                "\t\t<kbb_image_bg:3 src='<tag:image_path_new />/fallback3.png' tag='div' width='100' height='75' class=\"dispImg\"><kbb_image_bg:3 />\n" +
//                "\t</div>\n" +
//                "\t<div class=\"kw_wrap\">\n" +
//                "\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"left\">\n" +
//                "\t\t\t<tr><td width=\"100%\" align=\"left\" valign=\"top\" height=\"44\">\n" +
//                "\t\t\t\t<div class=\"dck3 anchortext\"></div>\n" +
//                "\t\t\t</td></tr>\n" +
//                "\t\t</table>\n" +
//                "\t</div>\n" +
//                "\t<div class=\"arrow\"></div>\n" +
//                "\t</div>\n" +
//                "\t<a href=\"#\" class=\"dak3 anchorhref\"></a>\n" +
//                "\t<div class=\"dspn\"><domain_keyword:3 /></div>\n" +
//                "</li>\n" +
//                "</ul>\n" +
//                "<ul>\n" +
//                "<li>\n" +
//                "\t<div class=\"content\">\n" +
//                "\t<div class=\"number\">4</div>\n" +
//                "\t<div class=\"img_wrap\">\n" +
//                "\t\t<kbb_image_bg:4 src='<tag:image_path_new />/fallback4.png' tag='div' width='100' height='75' class=\"dispImg\"><kbb_image_bg:4 />\n" +
//                "\t</div>\n" +
//                "\t<div class=\"kw_wrap\">\n" +
//                "\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"left\">\n" +
//                "\t\t\t<tr><td width=\"100%\" align=\"left\" valign=\"top\" height=\"44\">\n" +
//                "\t\t\t\t<div class=\"dck4 anchortext\"></div>\n" +
//                "\t\t\t</td></tr>\n" +
//                "\t\t</table>\n" +
//                "\t</div>\n" +
//                "\t<div class=\"arrow\"></div>\n" +
//                "\t</div>\n" +
//                "\t<a href=\"#\" class=\"dak4 anchorhref\"></a>\n" +
//                "\t<div class=\"dspn\"><domain_keyword:4 /></div>\n" +
//                "</li>\n" +
//                "<li>\n" +
//                "\t<div class=\"content\">\n" +
//                "\t<div class=\"number\">5</div>\n" +
//                "\t<div class=\"img_wrap\">\n" +
//                "\t\t<kbb_image_bg:5 src='<tag:image_path_new />/fallback5.png' tag='div' width='100' height='75' class=\"dispImg\"><kbb_image_bg:5 />\n" +
//                "\t</div>\n" +
//                "\t<div class=\"kw_wrap\">\n" +
//                "\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"left\">\n" +
//                "\t\t\t<tr><td width=\"100%\" align=\"left\" valign=\"top\" height=\"44\">\n" +
//                "\t\t\t\t<div class=\"dck5 anchortext\"></div>\n" +
//                "\t\t\t</td></tr>\n" +
//                "\t\t</table>\n" +
//                "\t</div>\n" +
//                "\t<div class=\"arrow\"></div>\n" +
//                "\t</div>\n" +
//                "\t<a href=\"#\" class=\"dak5 anchorhref\"></a>\n" +
//                "\t<div class=\"dspn\"><domain_keyword:5 /></div>\n" +
//                "</li>\n" +
//                "<li>\n" +
//                "\t<div class=\"content\">\n" +
//                "\t<div class=\"number\">6</div>\n" +
//                "\t<div class=\"img_wrap\">\n" +
//                "\t\t<kbb_image_bg:6 src='<tag:image_path_new />/fallback6.png' tag='div' width='100' height='75' class=\"dispImg\"><kbb_image_bg:6 />\n" +
//                "\t</div>\n" +
//                "\t<div class=\"kw_wrap\">\n" +
//                "\t\t<table width=\"100%\" cellpadding=\"0\" cellspacing=\"0\" border=\"0\" align=\"left\">\n" +
//                "\t\t\t<tr><td width=\"100%\" align=\"left\" valign=\"top\" height=\"44\">\n" +
//                "\t\t\t\t<div class=\"dck6 anchortext\"></div>\n" +
//                "\t\t\t</td></tr>\n" +
//                "\t\t</table>\n" +
//                "\t</div>\n" +
//                "\t<div class=\"arrow\"></div>\n" +
//                "\t</div>\n" +
//                "\t<a href=\"#\" class=\"dak6 anchorhref\"></a>\n" +
//                "\t<div class=\"dspn\"><domain_keyword:6 /></div>\n" +
//                "</li>\n" +
//                "</ul>\n" +
//                "<a class=\"footer\" href=\"<tag:mnet_adchoice_link />\" target=\"_blank\"></a>\n" +
//                "</div>\n" +
//                "</div>\n" +
//                "</div>\n" +
//                "<script type=\"text/javascript\">\n" +
//                "\tfunction formatViewAfterLoad(){ \t\n" +
//                "\t\t<tag:adjust_iframe_height_to_body_call />\n" +
//                "\t}\n" +
//                "\twindow.onresize = function(){\n" +
//                "\t\t<tag:adjust_iframe_height_to_body_call />\n" +
//                "\t}  \n" +
//                "\t\n" +
//                "\t</script>\n" +
//                "</stl_body>\n" +
//                "</html>";


        int startComment = code.indexOf("<li>");
        int stopComment = code.indexOf("</li>");
        int difference = stopComment-startComment;
        String comment = code.substring(startComment, stopComment + 5);

        int k =1;
        String domainKeyword = domain + k + " />";
        String dak= "\"dak" + k + " anchorhref\">";
        String dck = "\"dck" + k + " anchortext\">";
        String kbb ="kbb_image_bg:"+k;
        checkIfPresent(comment,domainKeyword);
        checkIfPresent(comment,dak);
        checkIfPresent(comment,dck);

        if(comment.contains("kbb"))
            checkIfPresent(comment,kbb);
//        else
//            logger.log(LogStatus.WARNING,"Warning! NO KBB present");
        comment = comment.replace("<", "&lt; ").replace(">", "&gt;");
        logger.log(LogStatus.INFO,comment);

        k++;
        while (code.indexOf("<li>",stopComment)!=-1) {
            // System.out.println("\n\nOld : "+startComment +" "+ stopComment+" "+difference+" ");
            startComment = code.indexOf("<li>", stopComment + 1);
            stopComment = code.indexOf("</li>", startComment);
            difference = stopComment - startComment;



                System.out.println("New : " + startComment + " " + stopComment + " " + difference + " ");
                System.out.println("Comment #" + k + ", Length = " + difference);
                comment = code.substring(startComment, stopComment + 5);

            domainKeyword = domain + k + " />";
            dak= "\"dak" + k + " anchorhref\">";
            dck = "\"dck" + k + " anchortext\">";
            kbb ="kbb_image_bg:"+k;
            checkIfPresent(comment,domainKeyword);
            checkIfPresent(comment,dak);
            checkIfPresent(comment,dck);
            if(comment.contains("kbb"))
                checkIfPresent(comment,kbb);
            else
                logger.log(LogStatus.WARNING,"Warning! NO KBB present");

                comment = comment.replace("<", "&lt; ").replace(">", "&gt;");
                System.out.println(comment);
                logger.log(LogStatus.INFO,comment);


                k++;


        }

            extent.endTest(logger);
        //Thread.sleep(3000);
    }

    @Test(priority = 1)
    public void checkMustHaves(){
        logger=extent.startTest("Check for Must have keywords.");
        checkIfPresent(code,docType);
        checkIfPresent(code,xml);
        checkIfPresent(code,stlhead);
        checkIfPresent(code,title);
        checkIfPresent(code,viewport);
        checkIfPresent(code,charset);
        checkIfPresent(code,resetCSS);
        checkIfPresent(code,IOSWidth);
        checkIfPresent(code,tagPost);
        checkIfPresent(code,footer);
        extent.endTest(logger);
    }
    @Test(priority = 3)
    public void checkKBBImages(){
        logger= extent.startTest("Check KBB Images Mapping");

        String imageFormats[] ={".jpg",".png",".gif",".jpeg"};
        int imageCount=0;
        System.out.println(code);
        System.out.println(code.toLowerCase().contains("<kbb"));
        int startComment = code.indexOf("<kbb");
        int stopComment = code.indexOf("<kbb",startComment+2);
        int difference = stopComment-startComment;
        System.out.println(startComment+" "+stopComment);
        int k=1;
        try {
            String comment = code.substring(startComment, stopComment + 20);
            comment = comment.replace("<", "&lt; ").replace(">", "&gt;");

            String fallback = comment.substring(comment.indexOf("fallback"), comment.indexOf("tag", comment.indexOf("fallback") + 1) - 2);
            //  logger.log(LogStatus.INFO,fallback);
            String kbb = comment.substring(comment.indexOf("<kbb") + 2, comment.indexOf("src") - 1).trim();
            // logger.log(LogStatus.INFO,kbb);
           k = 1;
            String numFallback = fallback.replaceAll("[^0-9]", "");
            String numKBB = kbb.replaceAll("[^0-9]", "");
            if (numFallback.equals(numKBB))
                logger.log(LogStatus.PASS, comment + "<br /><br />Fallback = " + fallback + "<br /><br />KBB =" + kbb);
            else
                logger.log(LogStatus.FAIL, comment + "<br /><br />Fallback = " + fallback + "<br /><br />KBB =" + kbb);

            if (imageList.contains(fallback)) {
                logger.log(LogStatus.PASS, "image uploaded successfully for : " + fallback);
            } else
                logger.log(LogStatus.FAIL, "No image uploaded : " + fallback);

            k++;
            while (code.indexOf("<kbb", stopComment + 2) != -1) {
                // System.out.println("\n\nOld : "+startComment +" "+ stopComment+" "+difference+" ");
                startComment = code.indexOf("<kbb", stopComment + 2);
                stopComment = code.indexOf("<kbb", startComment + 2);
                difference = stopComment - startComment;

                System.out.println("New : " + startComment + " " + stopComment + " " + difference + " ");
                System.out.println("Comment #" + k + ", Length = " + difference);
                comment = code.substring(startComment, stopComment + 20);
                System.out.println(comment);

                fallback = comment.substring(comment.indexOf("fallback"), comment.indexOf("tag", comment.indexOf("fallback") + 1) - 2);
                numFallback = fallback.replaceAll("[^0-9]", "");
                kbb = comment.substring(comment.indexOf("<kbb") + 2, comment.indexOf("src") - 1).trim();
                numKBB = kbb.replaceAll("[^0-9]", "");
                comment = comment.replace("<", "&lt; ").replace(">", "&gt;");

                if (numFallback.equals(numKBB))
                    logger.log(LogStatus.PASS, comment + "<br /><br />Fallback = " + fallback + "<br /><br />KBB =" + kbb);
                else
                    logger.log(LogStatus.FAIL, comment + "<br /><br />Fallback = " + fallback + "<br /><br />KBB =" + kbb);

                if (imageList.contains(fallback)) {
                    logger.log(LogStatus.PASS, "image uploaded successfully for : " + fallback);
                } else
                    logger.log(LogStatus.FAIL, "No image uploaded : " + fallback);
                k++;

            }

        }catch (StringIndexOutOfBoundsException se){logger.log(LogStatus.WARNING,"No KBBs found in code"); extent.endTest(logger);}

        for (int i = 0; i < imageFormats.length; i++) {
                String key = imageFormats[i];
                int last = 0;
                while (code.toLowerCase().indexOf(key, last) > 0) {
                    System.out.println("Key " + key);
                    System.out.println("val = " + code.substring(code.toLowerCase().indexOf(key, last) - 10, code.toLowerCase().indexOf(key, last) + 10));
                    imageCount++;
                    last = code.toLowerCase().indexOf(key, last) + 5;
                }

            }
            logger.log(LogStatus.INFO, "Num Images : " + imageCount);
            logger.log(LogStatus.INFO, "Number of KBBs =" + (k - 1));
            if (imageCount == (k - 1))
                logger.log(LogStatus.PASS, "Equal number of KBBs and Images : " + imageCount);
            else
                logger.log(LogStatus.FAIL, "Inconsistent KBBs: " + (k - 1) + " and Images : " + imageCount);
            extent.endTest(logger);
    }

    @Test(priority = 2)
    public void checkBlacklistedKeyowrds() throws Exception{
        logger=extent.startTest("Check for blacklisted keywords.");


                    String lowCode = code.toLowerCase();
                    String keyword ="";
                    lowCode=lowCode+"/loose.dtd\""+ Arrays.toString(blacklist);
                    System.out.println("Low: "+lowCode);
                    for(int x =0;x<blacklist.length;x++) {
//                        Pattern p = Pattern.compile("(.*([^a-zA-Z]|\\s)+"+blacklist[x]+"([^a-zA-Z]|\\s)+.*)|("+blacklist[x]+"([^a-zA-Z]|\\s)+.*)|(.*([^a-zA-Z]|\\s)+"+blacklist[x]+")|"+blacklist[x]);
//                        boolean classMatch = p.matcher(lowCode).matches();
//                        keyword=blacklist[x];
//                        System.out.println(keyword+": "+classMatch);
//                        if(classMatch) {
////                                if(classMatch)
////                                    System.out.println(lowClass+" Class has matched, Pattern = "+p);
////                                if(idMatch)
////                                    System.out.println(lowID+" ID has matched, Pattern = "+p);
//
//                            System.out.println("keyword "+ keyword+" found in code");
//                            logger.log(LogStatus.FAIL,"Keyword Found =<strong>"+keyword+"</strong><br />");
//
//
//                        }
//                        else {
//                            System.out.println("keyword "+ keyword+" not found in code");
//                            logger.log(LogStatus.PASS,"<strong>"+keyword+"</strong><br /> Keyword not Found");
//                        }
                        checkIfAbsent(code,blacklist[x]);
                    }
                    extent.endTest(logger);

            }

    @Test(priority = 1)
    public void checkScripts(){
        logger=extent.startTest("Check Script tags in code");
        String comment ="";
        int startComment = code.indexOf("<script");
        int stopComment = code.indexOf("</script");
        int difference = stopComment-startComment;
        try {
            comment = code.substring(startComment, stopComment + 9);
            System.out.println(comment);
            comment = comment.replace("<", "&lt; ").replace(">", "&gt;");

            logger.log(LogStatus.INFO, "Script found : <br />" + comment);
        }
        catch (Exception e){}
        int k =2;
        while (code.indexOf("<script",stopComment)!=-1)
        {
            startComment = code.indexOf("<script",stopComment+1);
            stopComment = code.indexOf("</script",startComment);
            difference = stopComment-startComment;


                System.out.println("New : " + startComment + " " + stopComment + " " + difference + " ");
                System.out.println("Comment #" + k + ", Length = " + difference);
                comment = code.substring(startComment, stopComment + 9);
                System.out.println(comment);
                k++;
            comment=comment.replace("<", "&lt; ").replace(">", "&gt;");
             logger.log(LogStatus.INFO, "Script found : <br />" + comment);
        }
        extent.endTest(logger);
    }

    @AfterTest
    public void tearDown(){
        extent.endTest(logger);
        extent.flush();
        extent.close();
        driver.quit();
    }

    public void checkIfPresent(@NotNull String code, String key){
        if (code.contains(key)) {
            String mess = key;
            System.out.println(mess);
            mess=mess.replace("<", "&lt; ").replace(">", "&gt;");
            logger.log(LogStatus.PASS, mess);
        } else {
            String mess = key+" Not found!";
            System.out.println(mess);
            mess=mess.replace("<", "&lt; ").replace(">", "&gt;");
            logger.log(LogStatus.FAIL, mess);
        }
    }


    public void checkIfAbsent(@NotNull String code, String key){
        if (code.contains(key)) {
            String mess = "Found :"+key;
            System.out.println(mess);
            mess=mess.replace("<", "&lt; ").replace(">", "&gt;");
            logger.log(LogStatus.FAIL, mess);
        } else {
            String mess = key+ " Not found!";
            System.out.println(mess);
            mess=mess.replace("<", "&lt; ").replace(">", "&gt;");
            logger.log(LogStatus.PASS, mess);
        }
    }

}
