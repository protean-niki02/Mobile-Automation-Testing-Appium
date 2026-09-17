package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.testng.Assert;
import org.testng.annotations.Test;
import org.openqa.selenium.By;

public class LoginCardBrandLogoTest extends BaseTest {

    @Test(
            description = "BD2M-672 - App - Pre-Login / Login Screen "
                    + "(OTP Login + Explore Products) - "
                    + "US-UCM-01 - SC_01 - SC_01_TC_001 - "
                    + "Verify that the login card displays the brand logo"
    )
    public void verifyLoginCardDisplaysBrandLogo() {
        step("Launch the app and land on the pre-login screen");
        By login = AppiumBy.accessibilityId("Login");
        WaitUtils.clickable(driver, login);
        driver.findElement(login).click();

        step("Open the login card");
        WaitUtils.visible(
                driver,
                AppiumBy.id("com.insurance.bimasugam:id/brand_logo")
        );

        step("Check the brand logo on the login card");

        Assert.assertTrue(
                driver.findElement(
                        AppiumBy.id("com.insurance.bimasugam:id/brand_logo")
                ).isDisplayed(),
                "Login card should display the brand logo"
        );
    }
}
