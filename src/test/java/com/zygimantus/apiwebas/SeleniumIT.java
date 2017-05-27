package com.zygimantus.apiwebas;

import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

/**
 *
 * @author Zygimantus
 */
public class SeleniumIT {

    @Test
    public void testSimple() throws Exception {

        System.setProperty("webdriver.gecko.driver", "/bin/geckodriver");
        WebDriver driver = new FirefoxDriver();

        driver.get("http://localhost:8080");

        String actualTitle = driver.getTitle();

        Assert.assertEquals("Login Apiwebas", actualTitle);

        driver.quit();
    }

}
