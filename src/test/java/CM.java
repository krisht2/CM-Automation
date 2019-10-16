import net.bytebuddy.implementation.bind.annotation.Default;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Wait;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.*;
import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import org.testng.annotations.*;
import org.testng.Assert.*;
import org.openqa.selenium.*;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CM {

    private WebDriver driver;
    Wait wait;
    Config config = new Config();
    String userName=config.userName;
    String email = config.email;
    String password=config.password;
    String mantisPassword=config.mantisPassword;
    String ticketID =config.ticketID;
    String width = config.width;
    String height = config.height;
    String frameworkName=config.frameworkName;
    String smallString =config.smallString;
    String gString =config.gString;
    String largeString = config.largeString;
    String contiuous = config.contiuous;
    String a[] =config.a;
    String domain = config.domain;
    String frameworkID=config.frameworkID;

    //For adType variable, only use the below values
    //hybrid.html
    //ads-only.html
    //cm-native.html
    //keywords-only.html
    String adType ="keywords-only.html";
    int numTags =5;
    String chromeDriverPath =config.chromeDriverPath;
    String ffDriverPath =config.ffDriverPath;
    String ieDriverPath = config.ieDriverPath;

    CMPages c;
    String res = width+" x "+height;
    Actions actions;


    @BeforeClass(alwaysRun = true)
    public void setUp() throws Exception {
        System.setProperty("webdriver.chrome.driver",chromeDriverPath);
        System.setProperty("webdriver.gecko.driver",ffDriverPath);
        System.setProperty("webdriver.ie.driver",ieDriverPath);

                driver = new ChromeDriver();
        //For Firefox
        //driver = new FirefoxDriver();
        driver.manage().window().maximize();
        wait =new WebDriverWait(driver, 30);
        c = new CMPages(driver);
        actions = new Actions(driver);
    }

    @Test
    public void fetchURLS() throws Exception {
        JavascriptExecutor js = (JavascriptExecutor) driver;
        driver.get("http://ui/mantis/");
        c.setMantisID(userName);
        c.setMantisPassword(mantisPassword);
        Thread.sleep(2000);
        c.clickMantisLogin();
        Thread.sleep(2000);

        System.out.println("Logged IN!!");

        String URL = "http://ui/mantis/view.php?id="+ticketID;
        String psmLink="";
        String framworkLink="";
        driver.get(URL);
        Thread.sleep(2000);
        WebElement description =c.getDescription();
       // System.out.println(description.getText());
        List links = description.findElements(By.tagName("a"));
        for (int i =0; i<links.size();i++)
        {
            //System.out.println(((WebElement)links.get(i)).getAttribute("href"));
            String page = ((WebElement)links.get(i)).getAttribute("href");
            if(page.contains("psm")){
                System.out.println("PSM Link = "+page);
                psmLink = page;
            }
            else if (page.contains("cm.internal.reports.mn")){
                System.out.println("Framewrk Link = "+page);
                framworkLink=page;
            }
        }
        if(psmLink.equals("")){
            throw new Exception("NO PSM Link");
        }
        if(framworkLink.equals("")){
            throw new Exception("NO Framework Link");
        }
        driver.get(psmLink);
        driver.get(framworkLink);

        String id=framworkLink.substring(framworkLink.indexOf("=")+1,framworkLink.indexOf("&"));
        String frmLink = "https://cm.internal.reports.mn/template/framework/pagemapping.php?id=";
        frmLink=frmLink+id;
        System.out.println("Framework Internal Link = "+frmLink);

        driver.get(frmLink);
        c.clickSSO();
        c.setSsoID(email);
        c.clickSSOIDNext();
        c.setSsoPassword(password);
        c.clickSSOPasswordNext();
        Thread.sleep(30000);

        //c.clickEditButton();


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

        }

        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        System.out.println(driver.getCurrentUrl());
        String code = c.getSourceCode();
        System.out.println("Actual Dev Source = "+code);
        for(int i=0;i<25;i++){
            if(i%2==0) {
                if (code.contains(domain + i + " />")) {
                    System.out.println("Present : " + domain + i);
                    code = code.replace(domain + i + " />", "<!--" + domain + i + " />-->");
                }
                if (code.contains("dck" + i + " anchortext\">")) {
                    code = code.replace("dck" + i + " anchortext\">", "dck" + i + " anchortext\">" + a[(i % 4)]);
                }
            }
        }
        if(StringUtils.containsIgnoreCase(code,"loose.dtd")|| StringUtils.containsIgnoreCase(code,"window.onload")||StringUtils.containsIgnoreCase(code,"detect text")|| StringUtils.containsIgnoreCase(code,"badabhai.js"))
            System.out.println("Restricted Keywords present");
        else
            System.out.println("All not present things not there");

        if(StringUtils.containsIgnoreCase(code,"<tag:reset_css />")&& StringUtils.containsIgnoreCase(code,"<tag:post_form_html />")&&StringUtils.containsIgnoreCase(code,"viewport"))
            System.out.println("All 3 present");
        else
            System.out.println("Needed keywords not present");
        System.out.println("***********************************\n\nCode = "+code);
        c.clickCancel();
        driver.switchTo().window(tabs.get(0));
        driver.get("https://cm.internal.reports.mn/template/framework/pagemapping.php?id="+frameworkID);
        resolutions = driver.findElements(By.xpath("//center/div/table[@class=\"general\"]//tr"));
        System.out.println("Resolution is : "+ res);
        for(int l =0;l<(resolutions.size()-2);l++)
        {
            wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//body/center/div/table[1]/tbody/tr["+(l+3)+"]/td[1]")));
            WebElement e = driver.findElement(By.xpath("//body/center/div/table[1]/tbody/tr["+(l+3)+"]/td[1]"));
            String type = e.getText();
         //   System.out.println("Value = "+type);

            WebElement size = driver.findElement(By.xpath("//body/center/div/table[1]/tbody/tr["+(l+3)+"]/td[2]"));
            String resolution = size.getText();
          //  System.out.println("Value = "+resolution);

            if(type.contains(adType)&& resolution.contains(height)&&resolution.contains(width)) {
                System.out.println("type = "+ type +" & Reso: "+resolution);
                driver.findElement(By.xpath("//tr["+(l+3)+"]//td[4]//a")).click();
                tabs = new ArrayList<String> (driver.getWindowHandles());
                driver.switchTo().window(tabs.get(1));
                c.setSource(code);
                js.executeScript("window.scrollBy(0,1000)");
                c.clickSubmit();
                js.executeScript("$('input[type=\"submit\"]').click();");
                try {
                    wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//a[contains(text(),'ADD PAGE')]"))));
                    //driver.switchTo().window(tabs.get(0));
                }
                catch (Exception ex){System.out.println("*******************\nSubmit Error!!!\n *******************\n");}


                driver.get("https://empire.reports.mn/8CUN4HUC1/adtags/create");
                c.clickOK();
                String adName ="TESTBOT_"+ticketID+"_"+width+"x"+height+"_"+adType;
                c.setAdTagName(adName);
                c.setAdSize(width+"x"+height+Keys.ENTER);
                c.clickmapTemplate();
                c.setSearchTemplate(frameworkName);
                Thread.sleep(5000);
                c.clickTemplate();
                c.clickMapTemplateButton();

                c.clickmapserp();
                c.setSearchTemplate("fenil");
                Thread.sleep(5000);
                c.clickSerpTemplate();
                c.clickMapTemplateButton();

                c.clickCreateAdTag();
                Thread.sleep(8000);
                driver.get("https://empire.reports.mn/8CUN4HUC1/adtags");
                c.clickOK();
                c.setSearch(adName);
                c.clickTag();
                c.clickSource();
                c.clickSync();
                c.clickGenerateCode();
                String adSourceCode= c.getSource();
                c.clickSaveCode();
                System.out.println("AD Source Code = "+adSourceCode);

                try {
                    String name ="KRISH";
                    File file = new File("C:\\xampp\\htdocs\\AdTags\\"+adName+".html");
                    file.createNewFile();
                    FileReader fileReader = new FileReader(file.getAbsoluteFile());
                    BufferedReader br = new BufferedReader(fileReader);
                    Path path = Paths.get("C:\\xampp\\htdocs\\AdTags\\Template.html");
                    Path actualPath = Paths.get("C:\\xampp\\htdocs\\AdTags\\"+adName+".html");
                    Charset charset = StandardCharsets.UTF_8;
                    String line= br.readLine();
                    String content = new String(Files.readAllBytes(path), charset);
                    System.out.println("String : "+content);
                    content = content.replaceAll("<YOUR SCRIPT HERE>", adSourceCode);
                    int a = content.indexOf("</script>");
                    content= content.substring(0,a)+"\n\tmedianet_requrl='http://MSNtestingforqa.com/?r='+  (Math.ceil((new Date()).getMinutes()/1));\n\n"+content.substring(a,content.length());
                    Files.write(actualPath, content.getBytes(charset));
                    driver.get("http://localhost/AdTags/"+adName+".html");
                    driver.manage().window().maximize();
                    int w =300;
                    int h =300;


                    driver.switchTo().frame(1);
                    List keywords =driver.findElements(By.tagName("a"));
                    System.out.println(keywords.size()+" is the size");
                    for(int k =0;k<keywords.size();k++)
                    {
                        driver.switchTo().defaultContent();
                        driver.switchTo().frame(1);
                        keywords =driver.findElements(By.tagName("a"));
                        System.out.println(keywords.size()+" is the size of the Links inside Loop");
                        System.out.println("inside loop");
                        WebDriverWait wait = new WebDriverWait(driver,30);
                        WebElement linkText =(WebElement) keywords.get(k);
                        System.out.println(linkText.getSize());
                        String href=linkText.getAttribute("href");
                        System.out.println(href+" is the link");
                        if(!href.equalsIgnoreCase(driver.getCurrentUrl()+"#")) {
                            if(k==0){
                                System.out.println("Condition fullfilled");
                                driver.switchTo().parentFrame();
                                driver.switchTo().frame(1);
                                actions.moveToElement(((WebElement) keywords.get(k))).build().perform();
                                File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                                Thread.sleep(1000);
                                FileUtils.copyFile(screenshotFile, new File("C:\\xampp\\htdocs\\AdTags\\Screenshots\\Hover_"+adName+"_"+k+".jpg"));
                                ((WebElement) keywords.get(k)).click();
                                Thread.sleep(500);
                                driver.switchTo().defaultContent();
                                driver.navigate().back();
//                    if(driver.getCurrentUrl().contains("media.net"))
//                        driver.close();
                                Thread.sleep(3000);
                            }
                            if(k>0&& !href.equalsIgnoreCase(((WebElement)keywords.get(k-1)).getAttribute("href"))) {
                                System.out.println("Condition fullfilled");
                                driver.switchTo().parentFrame();
                                driver.switchTo().frame(1);

                                actions.moveToElement(((WebElement) keywords.get(k))).build().perform();
                                File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                                Thread.sleep(1000);
                                FileUtils.copyFile(screenshotFile, new File("C:\\xampp\\htdocs\\AdTags\\Screenshots\\krish"+k+".jpg"));
                                ((WebElement) keywords.get(k)).click();
                                Thread.sleep(500);

                                driver.switchTo().defaultContent();
                                driver.navigate().back();
                                Thread.sleep(5000);
                            }
                        }
                        else
                            System.out.println("Test String found");

                    }

                    for (int i =0;i<5;i++) {

                        Dimension d = new Dimension(w,driver.manage().window().getSize().height);
                        driver.manage().window().setSize(d);
                        driver.navigate().refresh();
                        Thread.sleep(5000);
                        w = w+300;
                        //h =h+100;
                        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                        FileUtils.copyFile(screenshotFile, new File("C:\\xampp\\htdocs\\AdTags\\Screenshots\\"+ticketID+"_"+width+"x"+height+"_"+i+".jpg"));

                    }
                    driver.manage().window().maximize();
                    driver.switchTo().frame(1);
                    keywords =driver.findElements(By.tagName("a"));
                    System.out.println(keywords.size()+" is the size");
                    for(int k =0;k<keywords.size();k++)
                    {
                        driver.switchTo().defaultContent();
                        driver.switchTo().frame(1);
                        keywords =driver.findElements(By.tagName("a"));
                        System.out.println(keywords.size()+" is the size of the Links inside Loop");
                        System.out.println("inside loop");
                        WebDriverWait wait = new WebDriverWait(driver,30);
                        WebElement linkText =(WebElement) keywords.get(k);
                        System.out.println(linkText.getSize());
                        String href=linkText.getAttribute("href");
                        System.out.println(href+" is the link");
                        if(!href.contains(driver.getCurrentUrl())) {
                            if(k==0){
                                System.out.println("Condition fullfilled");
                                driver.switchTo().parentFrame();
                                driver.switchTo().frame(1);
                                actions.moveToElement(((WebElement) keywords.get(k))).build().perform();
                                File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                                Thread.sleep(1000);
                                FileUtils.copyFile(screenshotFile, new File("C:\\xampp\\htdocs\\AdTags\\Screenshots\\krish"+k+".jpg"));
                                ((WebElement) keywords.get(k)).click();
                                Thread.sleep(500);
                                driver.switchTo().defaultContent();
                                driver.navigate().back();
//                    if(driver.getCurrentUrl().contains("media.net"))
//                        driver.close();
                                Thread.sleep(3000);
                            }
                            if(k>0&& !href.equalsIgnoreCase(((WebElement)keywords.get(k-1)).getAttribute("href"))) {
                                System.out.println("Condition fullfilled");
                                driver.switchTo().parentFrame();
                                driver.switchTo().frame(1);

                                actions.moveToElement(((WebElement) keywords.get(k))).build().perform();
                                File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                                Thread.sleep(1000);
                                FileUtils.copyFile(screenshotFile, new File("C:\\xampp\\htdocs\\AdTags\\Screenshots\\krish"+k+".jpg"));
                                ((WebElement) keywords.get(k)).click();
                                Thread.sleep(500);

                                driver.switchTo().defaultContent();
                                driver.navigate().back();
                                Thread.sleep(5000);
                            }
                        }
                        else
                            System.out.println("Test String found");

                    }
                    driver.quit();

                    driver= new FirefoxDriver();
                    driver.get("http://localhost/AdTags/"+adName+".html");
                    driver.manage().window().maximize();
                    c = new CMPages(driver);
                    w =300;
                    h =300;
                    for (int i =0;i<5;i++) {

                        Dimension d = new Dimension(w,driver.manage().window().getSize().height);
                        driver.manage().window().setSize(d);
                        driver.navigate().refresh();
                        Thread.sleep(5000);
                        w = w+300;
                        //h =h+100;
                        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                        FileUtils.copyFile(screenshotFile, new File("C:\\xampp\\htdocs\\AdTags\\Screenshots\\"+ticketID+"_"+width+"x"+height+"_FF_"+i+".jpg"));

                    }
                    driver.quit();

                    driver= new InternetExplorerDriver();
                    driver.get("http://localhost/AdTags/"+adName+".html");
                    driver.manage().window().maximize();
                    c = new CMPages(driver);
                    w =300;
                    h =300;
                    for (int i =0;i<5;i++) {

                        Dimension d = new Dimension(w,driver.manage().window().getSize().height);
                        driver.manage().window().setSize(d);
                        driver.navigate().refresh();
                        Thread.sleep(5000);
                        w = w+300;
                        //h =h+100;
                        File screenshotFile = ((TakesScreenshot)driver).getScreenshotAs(OutputType.FILE);
                        FileUtils.copyFile(screenshotFile, new File("C:\\xampp\\htdocs\\AdTags\\Screenshots\\"+ticketID+"_"+width+"x"+height+"_IE_"+i+".jpg"));

                    }

                } catch (IOException ex) {
                    //System.out.println("File Handling Exception Occurred:");
                    ex.printStackTrace();
                }
                break;
            }
            System.out.println("i = "+l);
            if(l==resolutions.size()-3) {
                l = 0;
                System.out.println("Creating new page!!");
                c.addPage(code,width,height,adType);
                tabs = new ArrayList<String> (driver.getWindowHandles());
                driver.switchTo().window(tabs.get(0));
                driver.navigate().refresh();
                break;

            }
        }

    }

    @AfterClass(alwaysRun = true)
    public void tearDown() throws Exception {
        driver.quit();
    }
}