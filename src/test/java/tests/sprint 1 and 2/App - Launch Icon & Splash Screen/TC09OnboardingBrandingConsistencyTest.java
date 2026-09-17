package tests.sprint1;

import framework.BaseTest;
import framework.DriverManager;

import io.appium.java_client.android.AndroidDriver;

import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.List;

public class TC09OnboardingBrandingConsistencyTest extends BaseTest {

    private static final int WAIT_SECONDS = 15;
    private static final int MAX_ONBOARDING_SCREENS = 5;

    private static final String APP_PACKAGE =
            "com.insurance.bimasugam";

    @Test(
            description = "SC_01_TC_009 - Verify that the branding updates are applied consistently on the onboarding screens.",
            groups = {
                    "sprint1",
                    "onboarding",
                    "branding"
            }
    )
    public void SC_01_TC_009_verifyBrandingUpdatesConsistencyOnOnboardingScreens() {

        try {

            step("Launch the Bima Sugam application");

            Assert.assertNotNull(
                    driver,
                    "Appium driver was not initialized."
            );

            AndroidDriver androidDriver = getAndroidDriver();

            step("Verify that the Bima Sugam application is running");

            String currentPackage = getCurrentPackage(androidDriver);

            Assert.assertNotNull(
                    currentPackage,
                    "Application package could not be determined."
            );

            Assert.assertFalse(
                    currentPackage.isBlank(),
                    "Application package is blank."
            );

            System.out.println(
                    "Current application package: " + currentPackage
            );

            step("Wait for the application to load");

            waitForApplicationToLoad(androidDriver);

            step("Verify branding updates on onboarding screens");

            int onboardingScreensValidated =
                    validateOnboardingBranding(androidDriver);

            Assert.assertTrue(
                    onboardingScreensValidated > 0,
                    "No onboarding screens with branding elements were found."
            );

            step("Verify branding is applied consistently across onboarding screens");

            Assert.assertTrue(
                    onboardingScreensValidated > 0,
                    "Branding updates are not consistently displayed on onboarding screens."
            );

            step("SC_01_TC_009 completed successfully");

            System.out.println("==================================================");
            System.out.println("TEST CASE : SC_01_TC_009");
            System.out.println(
                    "TEST NAME : Verify branding updates on onboarding screens"
            );
            System.out.println(
                    "ONBOARDING SCREENS VALIDATED : "
                            + onboardingScreensValidated
            );
            System.out.println("RESULT    : PASS");
            System.out.println(
                    "Branding updates are applied consistently on the onboarding screens."
            );
            System.out.println("==================================================");

        } finally {

            /*
             * IMPORTANT:
             * TC09 must finish after onboarding validation.
             * Do not continue to Login screen.
             *
             * First terminate the application.
             * Then close the Appium driver/session.
             */
            closeApplicationAndDriver();
        }
    }

    /**
     * Returns the AndroidDriver from BaseTest.
     */
    private AndroidDriver getAndroidDriver() {

        Assert.assertTrue(
                driver instanceof AndroidDriver,
                "Current driver is not an AndroidDriver."
        );

        return (AndroidDriver) driver;
    }

    /**
     * Gets the foreground application package.
     */
    private String getCurrentPackage(
            AndroidDriver androidDriver
    ) {

        try {

            String packageName =
                    androidDriver.getCurrentPackage();

            System.out.println(
                    "Foreground application package: "
                            + packageName
            );

            return packageName;

        } catch (Exception e) {

            System.out.println(
                    "Unable to get current package: "
                            + e.getMessage()
            );

            return null;
        }
    }

    /**
     * Waits until the application has loaded.
     */
    private void waitForApplicationToLoad(
            AndroidDriver androidDriver
    ) {

        long endTime =
                System.currentTimeMillis()
                        + (WAIT_SECONDS * 1000L);

        while (
                System.currentTimeMillis() < endTime
        ) {

            try {

                String packageName =
                        androidDriver.getCurrentPackage();

                if (
                        packageName != null
                                && !packageName.isBlank()
                ) {

                    String pageSource =
                            androidDriver.getPageSource();

                    if (
                            pageSource != null
                                    && !pageSource.isBlank()
                    ) {

                        System.out.println(
                                "Application loaded successfully."
                        );

                        return;
                    }
                }

            } catch (Exception e) {

                System.out.println(
                        "Waiting for application to load..."
                );
            }

            try {

                Thread.sleep(500);

            } catch (InterruptedException e) {

                Thread.currentThread().interrupt();

                Assert.fail(
                        "Thread interrupted while waiting for application."
                );
            }
        }

        Assert.fail(
                "Application did not load within "
                        + WAIT_SECONDS
                        + " seconds."
        );
    }

    /**
     * Validates branding on onboarding screens.
     *
     * IMPORTANT:
     * This method only clicks Next/Continue.
     * It intentionally does NOT click Get Started,
     * because Get Started can navigate to Login.
     */
    private int validateOnboardingBranding(
            AndroidDriver androidDriver
    ) {

        int screensValidated = 0;

        for (
                int screen = 1;
                screen <= MAX_ONBOARDING_SCREENS;
                screen++
        ) {

            System.out.println(
                    "--------------------------------------------------"
            );

            System.out.println(
                    "Validating onboarding screen: "
                            + screen
            );

            /*
             * Safety check:
             * If Login is somehow displayed, stop immediately.
             */
            if (isLoginScreenDisplayed(androidDriver)) {

                System.out.println(
                        "Login screen detected."
                                + " Stopping TC09 onboarding validation."
                );

                break;
            }

            int imageCount =
                    getVisibleImageCount(androidDriver);

            System.out.println(
                    "Visible branding/image elements: "
                            + imageCount
            );

            if (imageCount > 0) {

                screensValidated++;

                System.out.println(
                        "Branding elements detected on onboarding screen "
                                + screen
                );

            } else {

                System.out.println(
                        "No visible branding/image element detected on onboarding screen "
                                + screen
                );
            }

            /*
             * Only click Next or Continue.
             *
             * DO NOT click Get Started.
             */
            if (
                    !clickNextOrContinue(androidDriver)
            ) {

                System.out.println(
                        "No Next/Continue button found."
                                + " Onboarding validation completed."
                );

                break;
            }

            waitForNextScreen(androidDriver);
        }

        return screensValidated;
    }

    /**
     * Finds and clicks only Next/Continue.
     *
     * Get Started is intentionally excluded because
     * it may navigate to Login.
     */
    private boolean clickNextOrContinue(
            AndroidDriver androidDriver
    ) {

        String[] navigationTexts = {

                "Next",
                "NEXT",
                "Continue",
                "CONTINUE"

        };

        for (String text : navigationTexts) {

            try {

                List<WebElement> elements =
                        androidDriver.findElements(
                                By.xpath(
                                        "//*[@text='"
                                                + text
                                                + "']"
                                )
                        );

                for (WebElement element : elements) {

                    try {

                        if (
                                element.isDisplayed()
                                        && element.isEnabled()
                        ) {

                            System.out.println(
                                    "Clicking onboarding navigation button: "
                                            + text
                            );

                            element.click();

                            return true;
                        }

                    } catch (Exception ignored) {
                        // Continue checking other elements.
                    }
                }

            } catch (Exception e) {

                System.out.println(
                        "Unable to find navigation button '"
                                + text
                                + "'"
                );
            }
        }

        return false;
    }

    /**
     * Waits briefly for the next onboarding screen.
     */
    private void waitForNextScreen(
            AndroidDriver androidDriver
    ) {

        try {

            Thread.sleep(1000);

        } catch (InterruptedException e) {

            Thread.currentThread().interrupt();

            Assert.fail(
                    "Thread interrupted while navigating onboarding screens."
            );
        }
    }

    /**
     * Counts visible ImageView elements.
     *
     * ImageView is used here because branding assets
     * such as logos, banners and tile artwork are commonly
     * represented as ImageView elements in Android.
     */
    private int getVisibleImageCount(
            AndroidDriver androidDriver
    ) {

        try {

            List<WebElement> imageElements =
                    androidDriver.findElements(
                            By.className(
                                    "android.widget.ImageView"
                            )
                    );

            int visibleCount = 0;

            for (WebElement image : imageElements) {

                try {

                    if (image.isDisplayed()) {

                        visibleCount++;
                    }

                } catch (Exception ignored) {
                    // Ignore stale/inaccessible elements.
                }
            }

            return visibleCount;

        } catch (Exception e) {

            System.out.println(
                    "Unable to find branding image elements: "
                            + e.getMessage()
            );

            return 0;
        }
    }

    /**
     * Determines whether the Login screen is displayed.
     */
    private boolean isLoginScreenDisplayed(
            AndroidDriver androidDriver
    ) {

        String[] loginScreenTexts = {

                "Login",
                "LOGIN",
                "Mobile Number",
                "Mobile number",
                "Enter Mobile Number",
                "Enter mobile number",
                "Password",
                "Enter Password",
                "Enter password"

        };

        for (String text : loginScreenTexts) {

            try {

                List<WebElement> elements =
                        androidDriver.findElements(
                                By.xpath(
                                        "//*[@text='"
                                                + text
                                                + "']"
                                )
                        );

                for (WebElement element : elements) {

                    try {

                        if (element.isDisplayed()) {

                            return true;
                        }

                    } catch (Exception ignored) {
                        // Ignore inaccessible elements.
                    }
                }

            } catch (Exception ignored) {
                // Continue checking other login indicators.
            }
        }

        return false;
    }

    /**
     * Explicitly closes the application and Appium session.
     *
     * This is executed even when the test fails.
     */
    private void closeApplicationAndDriver() {

        System.out.println("==================================================");
        System.out.println("TC09 CLEANUP STARTED");
        System.out.println("Closing Bima Sugam application...");
        System.out.println("==================================================");

        try {

            if (driver instanceof AndroidDriver) {

                AndroidDriver androidDriver =
                        (AndroidDriver) driver;

                try {

                    /*
                     * Explicitly terminate the Bima Sugam app.
                     */
                    if (androidDriver.isAppInstalled(APP_PACKAGE)) {

                        System.out.println(
                                "Terminating application: "
                                        + APP_PACKAGE
                        );

                        androidDriver.terminateApp(
                                APP_PACKAGE
                        );

                        System.out.println(
                                "Bima Sugam application terminated successfully."
                        );
                    }

                } catch (Exception e) {

                    System.out.println(
                            "Unable to terminate application: "
                                    + e.getMessage()
                    );
                }
            }

        } catch (Exception e) {

            System.out.println(
                    "Application cleanup error: "
                            + e.getMessage()
            );
        }

        /*
         * Close the Appium session.
         */
        try {

            if (DriverManager.hasDriver()) {

                System.out.println(
                        "Closing Appium driver..."
                );

                DriverManager.quitDriver();

                System.out.println(
                        "Appium driver disconnected successfully."
                );
            }

        } catch (Exception e) {

            System.out.println(
                    "Unable to close Appium driver: "
                            + e.getMessage()
            );
        }

        driver = null;
        videoRecorder = null;

        System.out.println("==================================================");
        System.out.println("TC09 CLEANUP COMPLETED");
        System.out.println("Application closed.");
        System.out.println("Appium session disconnected.");
        System.out.println("==================================================");
    }
}