package CM;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import org.jetbrains.annotations.NotNull;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;

public class CodeCheckDev {
    String filename="abc.html";
    String filepath="C:\\xampp\\htdocs\\"+filename;

    ExtentReports extent;
    ExtentTest logger;
    String extentReportFile = System.getProperty("user.dir")+"\\CMCodeTestDev.html";
    String blacklist[]={"ad","pe","banner","mn","media.net","mnet","project","about:_blank","about_blank","projectpe","kw","recirc","btf","ads","forbes","ga","google","badabhai",".js","window.onload","line1","loose.dtd","detect text"};
    Config config = new Config();
    String domain = config.domain;

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
    String heightResponsive="<tag:adjust_iframe_height_to_body_definition />";
    String heightScript="<scripttype=\"text/javascript\">functionformatViewAfterLoad(){<tag:adjust_iframe_height_to_body_call/>};window.onresize=function(){<tag:adjust_iframe_height_to_body_call/>};</script>";
    String domainName = "<title><tag:domain_name";
    ArrayList<String>imageList = new ArrayList<>();
    String code ="";

    @BeforeTest
    public void setUp(){
        extent = new ExtentReports(extentReportFile, true);
        extent.loadConfig(new File(System.getProperty("user.dir")+"\\extent-config.xml"));
    }

    @Test
    public void checkCode()throws Exception{
    logger = extent.startTest("Check for Domain Keywords & KBBs");
        try {
            BufferedReader in = new BufferedReader(new FileReader(filepath));
            String str;
            while ((str = in.readLine()) != null) {
                code +=str+"\n";
            }
            in.close();
        } catch (IOException e) {
            System.out.println("Please Recheck File name and File path");
        }

        code=filterCode(code);


        int startComment = code.indexOf("<li");
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
//        logger.log(LogStatus.INFO,comment);

        k++;
        while (code.indexOf("<li",stopComment)!=-1) {
            // System.out.println("\n\nOld : "+startComment +" "+ stopComment+" "+difference+" ");
            startComment = code.indexOf("<li", stopComment + 1);
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
              //  logger.log(LogStatus.INFO,comment);
                k++;
        }

            extent.endTest(logger);
        //Thread.sleep(3000);
    }

    @Test(priority = 1)
    public void checkMustHaves(){
//        logger=extent.startTest("Check for Must have keywords.");
        checkIfPresent(code,docType);
        checkIfPresent(code,xml);
        checkIfPresent(code,stlhead);
        checkIfPresent(code,title);
        checkIfPresent(code,viewport);
        checkIfPresent(code,charset);
        checkIfPresent(code,resetCSS);
        checkIfPresent(code,IOSWidth);
        checkIfPresent(code,tagPost);
        checkIfPresent(code,domainName);
//        checkIfPresent(code,footer);
       try {
           String footter = code.substring(code.indexOf("<a class=\"footer\" href=\""), code.lastIndexOf("</a>") + 4);
       int foot = (code.lastIndexOf("</a>")-code.indexOf("<a class=\"footer\" href=\"")-23);
        if(foot <25) {
            System.out.println("FAIL");
        logger.log(LogStatus.FAIL,"Footer Not present: "+footter.replaceAll("<", "&lt; ").replaceAll(">", "&gt;"));
        }else {
            logger.log(LogStatus.PASS,"Footer Present: "+footter.replaceAll("<", "&lt; ").replaceAll(">", "&gt;"));
            System.out.println("PASS!");
        }System.out.println(foot);
       }
       catch (Exception e){
           logger.log(LogStatus.FAIL,"No footer found");
           System.out.println(e);
       }

//        extent.endTest(logger);
    }
    @Test(priority = 3)
    public void checkKBBImages(){
//        logger= extent.startTest("Check KBB Images Mapping");

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

        }catch (StringIndexOutOfBoundsException se){logger.log(LogStatus.WARNING,"No KBBs found in code"); /*extent.endTest(logger);*/}

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
           // logger.log(LogStatus.INFO, "Num Images : " + imageCount);
            //logger.log(LogStatus.INFO, "Number of KBBs =" + (k - 1));
            if (imageCount == (k - 1))
                logger.log(LogStatus.PASS, "Equal number of KBBs and Images : " + imageCount);
            else
                logger.log(LogStatus.FAIL, "Inconsistent KBBs: " + (k - 1) + " and Images : " + imageCount);
//            extent.endTest(logger);
    }

    @Test(priority = 2)
    public void checkBlacklistedKeyowrds() throws Exception{
//        logger=extent.startTest("Check for blacklisted keywords.");


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
  //                  extent.endTest(logger);

            }

    @Test(priority = 1)
    public void checkScripts(){
    //    logger=extent.startTest("Check Script tags in code");
        String comment ="";
        int startComment = code.indexOf("<script");
        int stopComment = code.indexOf("</script");
        int difference = stopComment-startComment;
        try {
            comment = code.substring(startComment, stopComment + 9);
            System.out.println(comment);
            comment = comment.replace("<", "&lt; ").replace(">", "&gt;");

           // logger.log(LogStatus.INFO, "Script found : <br />" + comment);
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
            //
            // logger.log(LogStatus.INFO, "Script found : <br />" + comment);
        }
      //  extent.endTest(logger);



    }

    @AfterTest
    public void tearDown(){
        extent.endTest(logger);
        extent.flush();
        extent.close();
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

    public String filterCode(String code){
        String copy = code.replaceAll("\n","");
        copy = copy.replaceAll("\t","");
        copy = copy.replaceAll(" ","");
        System.out.println(copy);
        if(copy.contains(heightResponsive)||copy.contains(heightScript)) {
            System.out.println("Present");
            if(copy.contains(heightResponsive)&&copy.contains(heightScript))
                System.out.println("Both Present");
            else
                System.out.println("Height sctipt ussyessssssss");
        }
        else
            System.out.println("Abssent");

        code = (code.replaceAll("(?s)<!--.*?-->", ""));
        code = (code.replaceAll("(?s)/#.*?#/", ""));

        return code;
    }
}