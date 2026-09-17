package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class LoginHeadingTest extends BaseTest {

    private static final By ONBOARDING_LOGIN =
            AppiumBy.accessibilityId("Login");

    private static final By LOGIN_HEADING =
            By.xpath("//*[@text='Login' or @content-desc='Login']");

    @Test(
            description = "BD2M-672 - App - Pre-Login / Login Screen "
                    + "(OTP Login + Explore Products) - "
                    + "US-UCM-01 - SC_01 - SC_01_TC_002 - "
                    + "Verify that the login card displays the Login heading"
    )
    public void verifyLoginCardDisplaysLoginHeading() {
        step("Launch the app and land on the pre-login screen");
        WaitUtils.clickable(driver, ONBOARDING_LOGIN);
        driver.findElement(ONBOARDING_LOGIN).click();

        step("Check the Login heading on the login card");
        WaitUtils.visible(driver, LOGIN_HEADING);

        Assert.assertTrue(
                driver.findElement(LOGIN_HEADING).isDisplayed(),
                "Login card should display the Login heading"
        );
    }
}
