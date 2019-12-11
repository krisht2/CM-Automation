package Trial;

import Devices.Devices;
import com.google.gson.*;
import com.mongodb.util.JSON;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import com.relevantcodes.extentreports.LogStatus;
import jdk.nashorn.internal.objects.Global;
import jdk.nashorn.internal.parser.JSONParser;
import org.jetbrains.annotations.NotNull;
import org.openqa.selenium.*;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.json.Json;
import org.openqa.selenium.logging.LogEntry;
import org.openqa.selenium.logging.LogType;
import org.openqa.selenium.logging.LoggingPreferences;
import org.openqa.selenium.remote.CapabilityType;

import java.util.*;
import java.util.logging.Level;

public class Rough3 {
    static ExtentReports extent;
    static ExtentTest logger;


    public static void main(String args[]) throws Exception{

        String ffDriverPath ="C:\\Users\\krish.t\\Downloads\\geckodriver.exe";
        System.setProperty("webdriver.gecko.driver",ffDriverPath);

        System.setProperty("webdriver.chrome.driver", "C:\\Users\\krish.t\\Downloads\\chromedriver.exe");

        for(Devices d : Devices.allDevices){
            Map<String,Object> emulator = Devices.sendEmulator(d);
            System.out.println("Device : "+d+" :"+emulator);
        }


        ChromeOptions caps = new ChromeOptions();
        Devices phone = Devices.iPhone5;

        Map<String,Object> mobileEmulation= Devices.sendEmulator(phone);
        int width = phone.width;
        int height = phone.height;
        //caps.setExperimentalOption("mobileEmulation", mobileEmulation);
        LoggingPreferences logPrefs = new LoggingPreferences();
        logPrefs.enable( LogType.PERFORMANCE, Level.ALL );
        caps.setCapability("goog:loggingPrefs", logPrefs );
        WebDriver driver = new ChromeDriver(caps);
        driver.manage().window().setSize(new Dimension(width,height));

        String url ="https://www3.forbes.com/business/37-new-cars-to-avoid-ifs-3-nb";
        driver.get(url);


        driver.manage().window().maximize();
        String scriptToExecute = "var performance = window.performance || window.mozPerformance || window.msPerformance || window.webkitPerformance || {}; var network = performance.getEntries() || {}; return network;";
        String[] pixels ={"tfa.js","chartbeat.js","https://widgets.outbrain.com/outbrain.js","cdn.keywee.co/dist/analytics.min.js","www.google-analytics.com/analytics.js","b.scorecardresearch.com","connect.facebook.net/en_US/fbevents.js","https://s.yimg.com/wi/ytc.js","amplify.outbrain.com/cp/obtp.js"};

        String net = ((JavascriptExecutor)driver).executeScript(scriptToExecute).toString();
        List <String>networkCalls = Arrays.asList(net.split("}, \\{"));
        System.out.println(networkCalls);

        List<LogEntry> entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();

        System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");
        for (LogEntry entry : entries) {
            if(entry.toString().contains("tfa.js"))
            System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
        }

        for(String s:pixels)
        {
            checkIfPresent(net,s);
        }
        System.out.println("\n**********************************************************************\n");
        for(String network : networkCalls){

            network= network.toLowerCase();
            if(network.contains("tfa.js")||network.contains("chartbeat.js")||network.contains("https://widgets.outbrain.com/outbrain.js")||network.contains("cdn.keywee.co/dist/analytics.min.js")||network.contains("www.google-analytics.com/analytics.js")||network.contains("b.scorecardresearch.com")||network.contains("connect.facebook.net/en_US/fbevents.js")||network.contains("https://s.yimg.com/wi/ytc.js")||network.contains("amplify.outbrain.com/cp/obtp.js")) {
                System.out.println("\n"+network);
                System.out.println(network.substring(network.indexOf("initiatortype"),network.indexOf("next")));
            }
        }

System.out.println("**************PAGE @****************");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
        Thread.sleep(500);
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
        ((JavascriptExecutor) driver).executeScript("window.scrollTo(0,document.body.scrollHeight);");
        driver.findElement(By.className("next-slide-btn-text")).click();
        Thread.sleep(8000);
        entries = driver.manage().logs().get(LogType.PERFORMANCE).getAll();
        System.out.println(entries.size() + " " + LogType.PERFORMANCE + " log entries found");
        for (LogEntry entry : entries) {
            if(entry.toString().contains("tfa.js"))
                System.out.println(new Date(entry.getTimestamp()) + " " + entry.getLevel() + " " + entry.getMessage());
        }

        Thread.sleep(2000);
        net = ((JavascriptExecutor)driver).executeScript(scriptToExecute).toString();
        networkCalls = Arrays.asList(net.split("}, \\{"));

        for(String network : networkCalls){

            network= network.toLowerCase();
            if(network.contains("tfa.js")||network.contains("chartbeat.js")||network.contains("https://widgets.outbrain.com/outbrain.js")||network.contains("cdn.keywee.co/dist/analytics.min.js")||network.contains("www.google-analytics.com/analytics.js")||network.contains("b.scorecardresearch.com")||network.contains("connect.facebook.net/en_US/fbevents.js")||network.contains("https://s.yimg.com/wi/ytc.js")||network.contains("amplify.outbrain.com/cp/obtp.js")) {
                System.out.println("\n"+network);
                System.out.println(network.substring(network.indexOf("initiatortype"),network.indexOf("next")));
            }
        }

//        System.out.println("\n****************************************************************\n");
////        System.out.println(j);
//        List <String>entries = Arrays.asList(j.split("},\\{"));
//        for(String s:pixels)
//        {
//            checkIfPresent(j,s);
//        }
//        for(String entry:entries)
//            if(entry.contains("tfa.js")||entry.contains("chartbeat.js")||entry.contains("https://widgets.outbrain.com/outbrain.js")||entry.contains("cdn.keywee.co/dist/analytics.min.js")||entry.contains("www.google-analytics.com/analytics.js")||entry.contains("b.scorecardresearch.com")||entry.contains("connect.facebook.net/en_US/fbevents.js")||entry.contains("https://s.yimg.com/wi/ytc.js")||entry.contains("amplify.outbrain.com/cp/obtp.js"))
//                System.out.println(entry);

        //System.out.println(g.toJson(netData));
        driver.quit();
    }

    public static void checkIfPresent (String code, String key){
        if (code.contains(key)) {
            String mess = key;
            System.out.println(mess);
            mess=mess.replace("<", "&lt; ").replace(">", "&gt;");
            //logger.log(LogStatus.PASS, mess);
        } else {
            String mess = key+" Not found!";
            System.out.println(mess);
            mess=mess.replace("<", "&lt; ").replace(">", "&gt;");
            //logger.log(LogStatus.FAIL, mess);
        }
    }

}