/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.util.concurrent.TimeUnit;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author jvm
 */
public class WebUnitTest {

    private static WebDriver driver;
    @BeforeClass
    public static void initTest(){
      System.setProperty("webdriver.chrome.driver","lib/chromedriver");
        WebUnitTest.driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, 10, 500);
        driver.get("http://localhost:8080/KTVR17WebLibrary/");  
    }
    @AfterClass
    public static void closeDriver() {
        driver.quit();
    }
    @Test
    public void loginTest(){
        WebElement el = driver.findElement(By.id("login"));
        el.click();
        el = driver.findElement(By.id("login"));
        el.sendKeys("admin");
        el = driver.findElement(By.id("password"));
        el.sendKeys("admin");
        el = driver.findElement(By.id("butSubmit"));
        el.click();
        //wait.until(ExpectedConditions.presenceOfElementLocated(By.id("info")));
        el = driver.findElement(By.id("info"));
        Assert.assertEquals("Привет Сидор! Вы вошли в систему.",el.getText());
    }
    @Test
    public void addAndRemoveReaderTest(){
        WebElement el = driver.findElement(By.id("login"));
        el.click();
        el = driver.findElement(By.id("showregistry"));
        el.click();
        el = driver.findElement(By.id("name"));
        el.sendKeys("ТестИмя");
        el = driver.findElement(By.id("surname"));
        el.sendKeys("ТестФамилия");
        el = driver.findElement(By.id("phone"));
        el.sendKeys("123123123");
        el = driver.findElement(By.id("city"));
        el.sendKeys("Таллинн");
        el = driver.findElement(By.id("login"));
        el.sendKeys("login");
        el = driver.findElement(By.id("password1"));
        el.sendKeys("password");
        el = driver.findElement(By.id("password2"));
        el.sendKeys("password");
        el = driver.findElement(By.id("btnAdd"));
        el.click();
        el = driver.findElement(By.id("info"));
        Assert.assertEquals("Новый пользователь создан!", el.getText());
        
        el = driver.findElement(By.id("login"));
        el.click();
        el = driver.findElement(By.id("login"));
        el.sendKeys("admin");
        el = driver.findElement(By.id("password"));
        el.sendKeys("admin");
        el = driver.findElement(By.id("butSubmit"));
        el.click();
        el = driver.findElement(By.id("showUserRoles"));
        el.click();

        el = driver.findElement(By.id("deleteButton"));
        el.click();
        el = driver.findElement(By.id("info"));
        Assert.assertEquals("тестовый пользователь удален", el.getText());
    }
   
}
