package utilities;


import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeTest;
import pages.US_01_Page;

import java.io.IOException;
import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class TestBase {
    public ExtentReports extentReports;
    public ExtentTest extentTest;
    public ExtentHtmlReporter extentHtmlReporter;

    protected US_01_Page us01Page;
    protected Actions actions;
    protected WebDriverWait wait;

    @BeforeMethod
    public void setup() {
        Driver.getDriver().get(ConfigReader.getProperty("url"));
        us01Page = new US_01_Page();
        actions = new Actions(Driver.getDriver());
        wait = new WebDriverWait(Driver.getDriver(), Duration.ofSeconds(20));
        Driver.getDriver().manage().window().maximize();
        Driver.getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
    }


    @BeforeTest
    public void setUpTest(){
        extentReports = new ExtentReports();
        String filePath = System.getProperty("user.dir")+ "reports/myprojectreport.html";// rapor için adresi belirleyelim.
        extentHtmlReporter = new ExtentHtmlReporter(filePath);
        extentReports.attachReporter(extentHtmlReporter);
        extentReports.setSystemInfo("Environment", "Environment Name");
        extentReports.setSystemInfo("Browser", ConfigReader.getProperty("browser"));
        extentReports.setSystemInfo("Automation Engineer", "Somebody");
        extentHtmlReporter.config().setDocumentTitle("Reports");
        extentHtmlReporter.config().setReportName("Reports");
    }
    @AfterTest
    public void tearDownTest(){
        extentReports.flush();
    }
    @AfterMethod
    public void tearDownMethod(ITestResult result) throws IOException {
        if (result.getStatus() == ITestResult.FAILURE) {//When test case fails, then take the screenshot and attached the report
            String screenshotLocation = ReusableMethods.getScreenshot(result.getName());//getScreenshot is coming from ReusableMethods
            extentTest.fail(result.getName());
            extentTest.addScreenCaptureFromPath(screenshotLocation);//adding the screenshot to the report
            extentTest.fail(result.getThrowable());
        } else if (result.getStatus() == ITestResult.SKIP) {
            extentTest.skip("Test Case is skipped: " + result.getName());
        }
    }

    @AfterMethod
    public void tearDownMethod2(){
        Driver.closeDriver();
    }
}

