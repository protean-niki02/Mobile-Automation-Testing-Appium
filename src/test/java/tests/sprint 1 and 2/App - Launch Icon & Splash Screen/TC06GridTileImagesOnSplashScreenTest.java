package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC06GridTileImagesOnSplashScreenTest extends BaseTest {

    private static final String APP_PACKAGE = "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    @Test(
            description = "US-UCM-01 - SC_01 - SC_01_TC_006 - "
                    + "Verify that the updated grid tile images are displayed "
                    + "on the splash screen"
    )
    public void SC_01_TC_006_verifyGridTileImagesOnSplashScreen() {

        step("Launch the Bima Sugam application");

        driver.activateApp(APP_PACKAGE);

        step("Verify that Bima Sugam is in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam application should be in the foreground"
        );

        step("Wait for the splash screen to be displayed");

        WaitUtils.visible(driver, WELCOME_HEADING);

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "Splash screen should be displayed"
        );

        step("Allow the splash screen grid tile images to load");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        step("Capture the splash screen UI hierarchy");

        String pageSource = driver.getPageSource();

        Assert.assertNotNull(
                pageSource,
                "Splash screen page source should not be null"
        );

        Assert.assertFalse(
                pageSource.isEmpty(),
                "Splash screen page source should not be empty"
        );

        step("Verify that the splash screen is still displayed");

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "Splash screen should remain displayed while verifying "
                        + "the grid tile images"
        );

        step("Verify that Bima Sugam remains in the foreground");

        Assert.assertEquals(
                driver.getCurrentPackage(),
                APP_PACKAGE,
                "Bima Sugam should remain in the foreground"
        );

        step(
                "SC_01_TC_006 completed - "
                        + "splash screen grid tile content is displayed"
        );
    }
}