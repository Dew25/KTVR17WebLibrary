/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package web;

import java.util.concurrent.TimeUnit;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.WebDriverWait;

/**
 *
 * @author jvm
 */
public class SeleniumTest {
    
    public static void main(String[] args) {
        System.setProperty("webdriver.chrome.driver","lib/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
        WebDriverWait wait = new WebDriverWait(driver, 10, 500);
        driver.get("http://localhost:8080/KTVR17WebLibrary/");
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
        if("Привет Сидор! Вы вошли в систему.".equals(el.getText()))
            System.out.println("Тест на вход прошел успешно!");
        else
            System.out.println("Тест на вход провален");
        
        
        driver.quit();
        
        
    }
    
}
