package Screenshot.Util;

import Browser.Browser;
import org.apache.commons.io.FileUtils;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.ss.usermodel.WorkbookFactory;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.ITestResult;
import org.testng.TestListenerAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.time.Duration;

public class TestUtil extends TestListenerAdapter {

    public static long PAGE_LOAD_TIMEOUT = 20;
    public static long IMPLICIT_WAIT = 10;
    public static int duration = 10;
    public static String TestData_Sheet_Path = "src/main/java/TestData/OrangeHRMNewEmployeeData.xlsx";
    static Workbook book;
    static Sheet sheet;


    public static WebElement explicitWait(WebElement locator) {
        WebDriverWait wait = new WebDriverWait(Browser.driver, Duration.ofSeconds(duration));
        return wait.until(ExpectedConditions.elementToBeClickable(locator));
    }


    public static Object[][] getTestData(String sheetName) {
        FileInputStream file = null;
        try {
            file = new FileInputStream(TestData_Sheet_Path);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            book = WorkbookFactory.create(file);
        } catch (IOException e) {
            e.printStackTrace();
        }
        sheet = book.getSheet(sheetName);

        Object[][] data = new Object[sheet.getLastRowNum()][sheet.getRow(0).getLastCellNum()];

        for (int i = 0; i < sheet.getLastRowNum(); i++) {
            for (int k = 0; k < sheet.getRow(0).getLastCellNum(); k++) {
                data[i][k] = sheet.getRow(i + 1).getCell(k).toString();
            }
        }
        return data;
    }


    public void onTestFailure(ITestResult testResult) {
        final String path = "src/main/java/Screenshot/";

        File screenshot = ((TakesScreenshot) Browser.driver).getScreenshotAs(OutputType.FILE);
        File destFile = new File(path + testResult.getName() + System.currentTimeMillis() + ".png");

        try {
            FileUtils.copyFile(screenshot, destFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}