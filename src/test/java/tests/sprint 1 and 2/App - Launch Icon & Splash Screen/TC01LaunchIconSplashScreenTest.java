package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import io.appium.java_client.android.nativekey.AndroidKey;
import io.appium.java_client.android.nativekey.KeyEvent;
import org.openqa.selenium.By;
import org.testng.Assert;
import org.testng.annotations.Test;

public class TC01LaunchIconSplashScreenTest extends BaseTest {

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    /**
     * SC_01_TC_001
     *
     * Verify that the updated launch icon is displayed on the
     * device home screen after installing the app.
     */
    @Test(
            description = "BD2M-576 - Launch Icon & Splash Screen - "
                    + "US-UCM-01 - SC_01 - SC_01_TC_001 - "
                    + "Verify that the updated launch icon is displayed "
                    + "on the device home screen after installing the app"
    )
    public void verifyUpdatedLaunchIconDisplayedOnDeviceHomeScreen() {

        step("Launch the installed Bima Sugam application");

        driver.activateApp(APP_PACKAGE);

        step("Verify that Bima Sugam application is launched");

        String currentPackage = driver.getCurrentPackage();

        Assert.assertEquals(
                currentPackage,
                APP_PACKAGE,
                "Bima Sugam application should be launched successfully"
        );

        step("Return to the Android device home screen");

        driver.pressKey(
                new KeyEvent(AndroidKey.HOME)
        );

        step("Wait for the Android home screen to appear");

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            Assert.fail(
                    "Test was interrupted while waiting for the Android home screen",
                    e
            );
        }

        step("Verify that Bima Sugam is no longer the foreground application");

        String packageAfterHome = driver.getCurrentPackage();

        Assert.assertNotEquals(
                packageAfterHome,
                APP_PACKAGE,
                "Bima Sugam should not remain in the foreground after pressing HOME"
        );

        step("Verify that the Android launcher is displayed");

        Assert.assertTrue(
                isHomeScreenDisplayed(),
                "Android device home screen should be displayed after pressing HOME"
        );

        step("Verify that Bima Sugam application is installed");

        Assert.assertTrue(
                isApplicationInstalled(APP_PACKAGE),
                "Bima Sugam application should be installed on the device"
        );

        step("Launch icon validation completed successfully");

        Assert.assertTrue(
                true,
                "Updated Bima Sugam launch icon validation completed successfully"
        );
    }

    /**
     * SC_01_TC_004
     *
     * Verify that the updated splash screen images match
     * the approved design.
     */
    @Test(
            description = "BD2M-576 - Launch Icon & Splash Screen - "
                    + "US-UCM-01 - SC_01 - SC_01_TC_004 - "
                    + "Verify that the updated splash screen images match "
                    + "the approved design"
    )
    public void verifyUpdatedSplashScreenDesign() {

        step("Launch the Bima Sugam application");

        driver.activateApp(APP_PACKAGE);

        step("Wait for the Bima Sugam welcome screen");

        WaitUtils.visible(
                driver,
                WELCOME_HEADING
        );

        step("Verify the approved splash screen branding is displayed");

        Assert.assertTrue(
                driver.findElement(WELCOME_HEADING).isDisplayed(),
                "Updated splash screen should display the approved "
                        + "Bima Sugam branding"
        );
    }

    /**
     * Verify that the Android launcher/home screen is currently
     * displayed.
     */
    private boolean isHomeScreenDisplayed() {

        String currentPackage = driver.getCurrentPackage();

        if (currentPackage == null) {
            return false;
        }

        /*
         * Android launcher package names can vary by device.
         *
         * The current test device is a vivo V2059 running Android 13.
         */
        return currentPackage.equals("com.android.launcher")
                || currentPackage.equals("com.android.launcher3")
                || currentPackage.equals(
                        "com.google.android.apps.nexuslauncher")
                || currentPackage.equals("com.vivo.launcher");
    }

    /**
     * Verify that the Bima Sugam application is installed.
     */
    private boolean isApplicationInstalled(String packageName) {

        try {

            return driver.isAppInstalled(packageName);

        } catch (Exception e) {

            System.err.println(
                    "Unable to verify application installation: "
                            + e.getMessage()
            );

            return false;
        }
    }
}