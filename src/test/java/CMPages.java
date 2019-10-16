import org.apache.commons.lang3.StringUtils;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class CMPages {

    WebDriver driver;
    WebDriverWait wait;
    CMPages(WebDriver driver){
        wait =new WebDriverWait(driver, 30);
        this.driver=driver;
    }

    //MANTIS Login page
    By mantisID = By.id("username");
    By mantisPassword = By.id("password");
    By mantisLogin = By.xpath("//input[@class='button']");

    //MANTIS Tickets page
    By description = By.xpath("//td[@class='bug-description']");


   // Google SSO Auth
    By sso = By.xpath("//*[@id=\"buttonGauth\"]");
    By ssoID = By.id("identifierId");
    By ssoIDNext = By.xpath("//*[@id=\"identifierNext\"]/span/span");
    By ssoPassword = By.xpath("//*[@id=\"password\"]/div[1]/div/div[1]/input");
    By getSsoPasswordNext = By.xpath("//*[@id=\"passwordNext\"]/span");


    //Framework Page
    By editButton = By.xpath("//a[contains(text(),'Edit')]");
    By sourceCode = By.xpath("//*[@id=\"txtareasourcecode\"]");
    By cancelFramework = By.xpath("//table[@class='general']//input[2]");
    By submit = By.xpath("//table[@class=\"general\"]//tr[8]//input[1]");
    By addPage = By.xpath("//a[contains(text(),'ADD PAGE')]");
    By adSource =By.xpath("//textarea[@name='htmlsourcecode']");

    By size=By.xpath("//select[@id='sizeid']");
   By type = By.xpath("//select[@id='cmbpagetype']");
    By submitPage = By.xpath("//input[@name='validatePage']");


    //Empire Reports
    By okButton = By.xpath("//*[@id=\"window\"]/button[2]");
    By adTagName = By.xpath("//input[@name='ad_tag_name']");
    By adSize = By.xpath("//span[@id='react-select-2--value']//input");
    By mapTemplate = By.xpath("//div[@class='card form-section form-item']//div[3]//div[1]//div[1]//button[1]");
    By searchTemplate = By.xpath("//input[@placeholder='Search by Name']");
    By template = By.xpath("//ul[@class=\"list-group\"]/li[1]");
    By mapTemplateButton = By.xpath("//button[@class='submit-button align-self-center btn btn-primary']");

    By mapSerpTemplate = By.xpath("//button[@class=\"btn btn-primary\"]");
    By serpTemplate = By.xpath("//li[@class='map-template-list-item list-group-item']");
    By createAdTag = By.xpath("//button[@class='submit-button btn btn-secondary']");


    //FindSource
    By searchTag =By.xpath("//input[@class=\"input-field form-control\"]");
    By adTag = By.xpath("//div[@class=\"d-flex align-items-center flex-row row-1\"]//h2[@class=\"d-inline\"]");
    By source=By.xpath("//div[@class=\"d-flex flex-wrap\"]//i[@class=\"icon-code \"]");
    By sync = By.xpath("//div[contains(@class,'radio-button-group-container')]//div[1]//label[1]");
    By generateCode = By.xpath("//button[contains(@class,'submit-button btn btn-secondary')]");
    By code = By.xpath("//textarea[@class=\"code-space\"]");
    By saveCode = By.xpath("//button[@class=\"save-code-button btn btn-outline-secondary\"]");

    public void setMantisID(String ID)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mantisID));
        driver.findElement(mantisID).sendKeys(ID);
    }
    public void setMantisPassword(String password)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mantisPassword));
        driver.findElement(mantisPassword).sendKeys(password);
    }
    public void clickMantisLogin()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mantisLogin));
        driver.findElement(mantisLogin).click();
    }

    public WebElement getDescription()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(description));
        return driver.findElement(description);
    }

    public void clickSSO()
    {
        wait.until(ExpectedConditions.elementToBeClickable(sso));
        driver.findElement(sso).click();
    }

    public void setSsoID(String ssoid)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ssoID));
        driver.findElement(ssoID).sendKeys(ssoid);
    }
    public void clickSSOIDNext()
    {
        wait.until(ExpectedConditions.elementToBeClickable(ssoIDNext));
        driver.findElement(ssoIDNext).click();
    }

    public void setSsoPassword(String ssoid)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(ssoPassword));
        driver.findElement(ssoPassword).sendKeys(ssoid);
    }

    public void clickSSOPasswordNext()
    {
        wait.until(ExpectedConditions.elementToBeClickable(getSsoPasswordNext));
        driver.findElement(getSsoPasswordNext).click();
    }

    public void clickEditButton()
    {
        wait.until(ExpectedConditions.elementToBeClickable(editButton));
        driver.findElement(editButton).click();
    }

    public String getSourceCode()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sourceCode));
        return driver.findElement(sourceCode).getText();
    }


    public void setSource(String source) throws Exception
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sourceCode));
        driver.findElement(sourceCode).clear();
        driver.findElement(sourceCode).sendKeys(source);
        Thread.sleep(5000);
    }

    public void setSourceCode(String code) throws Exception
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sourceCode));
         driver.findElement(sourceCode).sendKeys(code);

    }

    public void clickCancel()
    {
        wait.until(ExpectedConditions.elementToBeClickable(cancelFramework));
        driver.findElement(cancelFramework).click();
    }

    public void clickSubmit()throws  Exception
    {
        wait.until(ExpectedConditions.elementToBeClickable(submit));
        driver.findElement(submit).submit();
        Thread.sleep(5000);
    }
    public void addPage(String code, String height, String width, String adType) throws  Exception{
        //Click AddPage Button
        wait.until(ExpectedConditions.elementToBeClickable(addPage));
        driver.findElement(addPage).click();
        //Select Page Type
        ArrayList<String> tabs = new ArrayList<String> (driver.getWindowHandles());
        driver.switchTo().window(tabs.get(1));
        wait.until(ExpectedConditions.visibilityOfElementLocated(size));
//Write code to switch tabs

        Select adsize = new Select(driver.findElement(size));
        Select type = new Select(driver.findElement(By.xpath("//select[@id='cmbpagetype']")));

        List<WebElement> sizes= adsize.getOptions();
        for(WebElement size :sizes){
            if(size.getText().contains(height)&&size.getText().contains(width))
                adsize.selectByVisibleText(size.getText());
        }
System.out.println("Size Set Completed!!!");
        //Select Ad Size
        List<WebElement> types= type.getOptions();
       // System.out.println(((WebElement)types).getText());
        String k =adType.substring(0,adType.indexOf("-")-1);
        for(WebElement t :types){
            System.out.println("option ="+t.getText()+" and comparing with "+k);
            if(StringUtils.containsIgnoreCase(t.getText(),k))

                type.selectByVisibleText(t.getText());
        }

        //Enter Source Code
        wait.until(ExpectedConditions.visibilityOfElementLocated(adSource));
        driver.findElement(adSource).sendKeys(code);
        //Click Submit
        wait.until(ExpectedConditions.elementToBeClickable(submitPage));
        driver.findElement(submitPage).click();
        Thread.sleep(5000);
        wait.until(ExpectedConditions.presenceOfElementLocated((By.xpath("//a[contains(text(),'ADD PAGE')]"))));
        driver.close();
    }

    public void clickOK()
    {
        wait.until(ExpectedConditions.elementToBeClickable(okButton));
        driver.findElement(okButton).click();
    }
    public void setAdTagName(String name)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(adTagName));
        driver.findElement(adTagName).sendKeys(name);
    }
    public void setAdSize(String size)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(adSize));
        driver.findElement(adSize).sendKeys(size);
    }
    public void clickmapTemplate()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mapTemplate));
        driver.findElement(mapTemplate).click();
    }

    public void setSearchTemplate(String size)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchTemplate));
        driver.findElement(searchTemplate).sendKeys(size);
    }

    public void clickTemplate()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(template));
        driver.findElement(template).click();
    }

    public void clickMapTemplate()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mapTemplate));
        driver.findElement(mapTemplate).click();
    }

    public void clickMapTemplateButton()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mapTemplateButton));
        driver.findElement(mapTemplateButton).click();
    }
    public void clickmapserp()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(mapSerpTemplate));
        driver.findElement(mapSerpTemplate).click();
    }
    public void clickSerpTemplate()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(serpTemplate));
        driver.findElement(serpTemplate).click();
    }
    public void clickCreateAdTag()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(createAdTag));
        driver.findElement(createAdTag).click();
    }

    public void setSearch(String size)
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(searchTag));
        driver.findElement(searchTag).sendKeys(size);
    }
    public void clickTag()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(adTag));
        driver.findElement(adTag).click();
    }
    public void clickSource()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(source));
        driver.findElement(source).click();
    }
    public void clickSync()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(sync));
        driver.findElement(sync).click();
    }
    public void clickGenerateCode()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(generateCode));
        driver.findElement(generateCode).click();
    }
    public String getSource()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(code));
        return driver.findElement(code).getText();
    }
    public void clickSaveCode()
    {
        wait.until(ExpectedConditions.visibilityOfElementLocated(saveCode));
        driver.findElement(saveCode).click();
    }
}
