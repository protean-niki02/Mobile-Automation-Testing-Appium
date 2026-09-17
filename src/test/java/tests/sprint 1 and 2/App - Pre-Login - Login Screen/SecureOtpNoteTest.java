package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class SecureOtpNoteTest extends BaseTest {

    private static final By ONBOARDING_LOGIN =
            AppiumBy.accessibilityId("Login");

    private static final By SECURE_OTP_NOTE =
            AppiumBy.accessibilityId(
                    "Your details will be verified by a secure OTP"
            );

    @Test(
            description = "BD2M-672 - App - Pre-Login / Login Screen "
                    + "(OTP Login + Explore Products) - "
                    + "US-UCM-01 - SC_01 - SC_01_TC_003 - "
                    + "Verify that the login card displays the secure OTP note"
    )
    public void verifyLoginCardDisplaysSecureOtpNote() {
        step("Launch the app and land on the pre-login screen");
        WaitUtils.clickable(driver, ONBOARDING_LOGIN);
        driver.findElement(ONBOARDING_LOGIN).click();

        step("Check the secure OTP verification note on the login card");
        WaitUtils.visible(driver, SECURE_OTP_NOTE);

        Assert.assertTrue(
                driver.findElement(SECURE_OTP_NOTE).isDisplayed(),
                "Login card should display the note "
                        + "\"Your details will be verified by a secure OTP\""
        );
    }
}
