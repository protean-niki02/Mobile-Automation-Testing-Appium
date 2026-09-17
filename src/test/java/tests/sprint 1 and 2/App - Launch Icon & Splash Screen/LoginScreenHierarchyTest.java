package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

public class LoginScreenHierarchyTest extends BaseTest {

    private static final String APP_PACKAGE = "com.insurance.bimasugam";

    private static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");

    @Test
    public void dumpLoginScreenHierarchy() {

        step("Launch Bima Sugam");

        driver.activateApp(APP_PACKAGE);

        step("Wait for onboarding screen");

        WaitUtils.visible(driver, WELCOME_HEADING);

        step("Wait for onboarding content");

        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        /*
         * Print the complete Appium UI hierarchy.
         */
        step("Printing current UI hierarchy");

        System.out.println();
        System.out.println("========================================");
        System.out.println("CURRENT APP UI HIERARCHY");
        System.out.println("========================================");
        System.out.println(driver.getPageSource());
        System.out.println("========================================");

        /*
         * Keep the session alive briefly so the hierarchy
         * can be inspected if required.
         */
        try {
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
