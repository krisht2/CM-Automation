package Trial;

import com.relevantcodes.extentreports.LogStatus;
import org.apache.commons.io.FileUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.logging.Logs;
import org.testng.TestNG;
import org.testng.annotations.*;
import java.io.File;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import org.openqa.selenium.*;
import com.relevantcodes.extentreports.ExtentReports;
import com.relevantcodes.extentreports.ExtentTest;
import org.apache.commons.mail.DefaultAuthenticator;
import org.apache.commons.mail.EmailAttachment;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.MultiPartEmail;
import org.apache.commons.collections4.CollectionUtils;

public class TestChild {
    public static void main(String args[]){
        ArrayList a = new ArrayList();
        a.add(1);
        a.add(2);
        a.add(3);
        a.add(5);
        System.out.println(a);
        ArrayList b = new ArrayList();
        b.add(1);
        b.add(2);
        b.add(3);
        b.add(1);
        b.add(1);b.add(1);b.add(1);
        b.add(4);
        System.out.println(b);

        System.out.println(b.removeAll(a));
System.out.println(b);
        ArrayList<String> list = (ArrayList<String>) CollectionUtils.subtract(b, a);
        System.out.println(list);
    }
}
