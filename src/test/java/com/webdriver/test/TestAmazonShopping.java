package com.webdriver.test;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import java.util.concurrent.TimeUnit;
import org.openqa.selenium.support.ui.Select;

/**
 * Class to search and add items to shopping cart in Amazon.com
 *
 * @author padma
 */
public class TestAmazonShopping {
    private WebDriver wd;

    @BeforeClass
    private void setUp() {
        //create a firefox web driver object
        wd = new FirefoxDriver();
    }

    @AfterClass
    private void tearDown() {
        // Shutdown the driver
        wd.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        wd.quit();
    }

    @BeforeMethod
    public void openHomePage(){
        //Open the web page before starting any test
        wd.manage().timeouts().implicitlyWait(50, TimeUnit.SECONDS);
        wd.get("http://www.amazon.com");
    }

    /**
     * Test to search for an item, add the first result to shopping cart
     * and verify if the item got added correctly.
     */
    @Test
    public void testAddItemToCart(){
        // 1. Set search category to "Books"
        WebElement category = wd.findElement(By.id("searchDropdownBox"));
        new Select(category).selectByVisibleText("Books");

        // 2. Set search keyword to "Selenium" and hit "Go"
        WebElement text = wd.findElement(By.id("twotabsearchtextbox"));
        text.sendKeys("Selenium");
        WebElement goButton = wd.findElement(By.xpath("//*[@value='Go']"));
        goButton.click();

        // 3. Find the first search result and drill-in
        WebElement firstBook = wd.findElement(By.className("s-access-title"));
        String firstBookName = firstBook.getText();
        firstBook.click();

        // 4. Add the item to shopping cart
        assert (wd.findElement(By.id("productTitle")).getText().equals(firstBookName));
        wd.findElement(By.id("add-to-cart-button")).click();
        assert (wd.findElement(By.xpath(".//*[@id='huc-v2-order-row-messages']")).getText().equals("Added to Cart"));

        // 5. Open shopping cart and verify that item got added correctly
        wd.findElement(By.className("a-button")).click();
        assert (wd.findElement(By.className("a-list-item")).getText().contains(firstBookName));
    }
}
