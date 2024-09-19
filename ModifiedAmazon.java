package com.example.tests;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import java.io.FileOutputStream;
import java.io.IOException;

public class ModifiedAmazon {
    static WebDriver driver;
    static List<String> colorList = new ArrayList<>(); // List to store colors from hoverAndFetchColors

    @BeforeClass
    public void setUp() {
        System.setProperty("webdriver.chrome.driver",
                "C:\\Users\\HP\\Downloads\\chromedriver-win64 (2)\\chromedriver-win64\\chromedriver.exe");
        driver = (WebDriver) new ChromeDriver();
        driver.manage().window().maximize();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
    }

    @AfterClass
    public void tearDown() {
        driver.quit();
    }

    @Test(priority = 1)
    public void NavigateTotheWebsite() {
        driver.get("https://www.amazon.in/");

        // Validation of homepage of website
        String Actualtext = "Hello, sign in";
        WebElement validationText = driver
                .findElement(By.xpath("/html/body/div[1]/header/div/div[1]/div[3]/div/a[2]/div/span"));
        String fetchtext = validationText.getText();
        Assert.assertEquals(fetchtext, Actualtext);
        System.out.println("Both are matching correct, the webpage is correct....");
        System.out.println("Test Case 1: Navigate to the Website URL...... Completed");
    }

    @Test(priority = 2)
    public void SearchIphone() {
        String searchiphone = "Iphone 12 ";
        WebElement searchbox = driver
                .findElement(By.xpath("/html/body/div[1]/header/div/div[1]/div[2]/div/form/div[2]/div[1]/input"));
        searchbox.sendKeys(searchiphone);
        searchbox.sendKeys(Keys.ENTER);
        System.out.println("The iPhone page is working fine, it is searching as per expectation");
    }

    @Test(priority = 3)
    public void Iphone12Sepecification() throws InterruptedException, IOException {
    	   WebElement iphone12 = driver.findElement(By.xpath(
                   "//div[contains(@class,'rush-component s-featured-result-item')]//span[@class='a-size-medium a-color-base a-text-normal'][normalize-space()='Apple iPhone 12 (128GB) - White']"));
           iphone12.click();

           // Get the current window handle
           String mainWindow = driver.getWindowHandle();
           Set<String> windows = driver.getWindowHandles();
           // Switch to the new window
           for (String window : windows) {
               if (!window.equals(mainWindow)) {
                   driver.switchTo().window(window);
                   break;
               }
           }

           JavascriptExecutor js = (JavascriptExecutor) driver;
           js.executeScript("window.scrollBy(0,500)", "");
           Thread.sleep(3000);

           WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
           WebElement name = wait.until(ExpectedConditions.visibilityOfElementLocated(By.id("productTitle")));

           // Fetch and print the title
           String iphoneName = name.getText();
           System.out.println("Product Name: " + iphoneName);

           // get price
           Thread.sleep(3000);
           WebElement priceofiphone = driver.findElement(By.xpath("//span[normalize-space()='54,899']"));
           String price = priceofiphone.getText();

           js.executeScript("window.scrollBy(0,870)", "");
           Thread.sleep(3000);

           // storageOption
           WebElement storage = driver.findElement(
                   By.xpath("/html/body/div[4]/div/div[3]/div[11]/div[40]/div[1]/div/form/div[2]/ul"));
           String storageavailable = storage.getText();
    	
    	
        // Wait for the color options to be visible
       
        // Find all color swatch elements (assuming they have the class 'imgSwatch')
        List<WebElement> colorOptions = driver.findElements(By.className("imgSwatch"));

        // Initialize Actions for hovering
        Actions actions = new Actions(driver);

        // Loop through each color option, hover, and fetch the product name
        for (WebElement colorOption : colorOptions) {
            // Hover over the color option
            actions.moveToElement(colorOption).perform();

            // Wait for a moment to allow the page to update the product name dynamically
            Thread.sleep(2000); // or use WebDriverWait if the element updates asynchronously

            // Fetch the color name from the alt attribute of the image
            String colorName = colorOption.getAttribute("alt");

            // Add the color name to the colorList
            colorList.add(colorName);

            // Print the color name
            System.out.println("Color: " + colorName);
            
            // Instead of fetching color here, use the colors from hoverAndFetchColors
            String allColors = String.join(", ", colorList); // Join all colors into a single string

            System.out.println(iphoneName + " " + storageavailable + " " + allColors + " " + price);
            

            // Create Excel and write data
            Workbook workbook = new XSSFWorkbook();
            Sheet sheet = workbook.createSheet("iPhone Data");

            // Create header row
            Row headerRow = sheet.createRow(0);
            headerRow.createCell(0).setCellValue("Name");
            headerRow.createCell(1).setCellValue("Storage");
            headerRow.createCell(2).setCellValue("Price");
            headerRow.createCell(3).setCellValue("Color");

            // Write fetched data
            Row dataRow = sheet.createRow(1);
            dataRow.createCell(0).setCellValue(iphoneName);
            dataRow.createCell(1).setCellValue(storageavailable);
            dataRow.createCell(2).setCellValue(price);
            dataRow.createCell(3).setCellValue(allColors); // Add all colors in one cell

            // Save the Excel file
            FileOutputStream fileOut = new FileOutputStream("C:\\Users\\HP\\Desktop\\iphonedata.xlsx");
            workbook.write(fileOut);
            fileOut.close();
            workbook.close();

            System.out.println("Data successfully written to Excel.");
        }
    }

}
