package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC03UpdatedSplashScreenImagesTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    @Test(
            description = "BD2M-577 - Welcome Screen - "
                    + "US-UCM-01 - SC_01 - SC_01_TC_003 - "
                    + "Verify that the updated splash screen images "
                    + "are displayed when the app is launched"
    )
    public void SC_01_TC_003_verifyUpdatedSplashScreen() {

        step("Launch the Bima Sugam application");

        driver.activateApp(APP_PACKAGE);

        step("Wait for the updated splash/welcome screen to be displayed");

        WaitUtils.visible(
                driver,
                WELCOME_HEADING
        );

        step("Verify that the Bima Sugam welcome screen is displayed");

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "Updated splash screen should be displayed when the app is launched"
        );

        step("Verify that the application is in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam application should remain in the foreground"
        );

        step("Updated splash screen validation completed successfully");
    }
}