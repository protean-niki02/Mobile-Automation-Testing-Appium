package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import locators.AndroidLocators;

import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;

public class TC02LoginHeadingTest extends BaseTest {

@Test(
        description = "BD2M-672 | SC_01_TC_002 - Verify that the login card displays the Login heading.",
        groups = {"Sprint1", "Login", "Positive"}
)
public void verifyLoginCardDisplaysLoginHeading() {

    step("Wait for the initial Bima Sugam onboarding screen");

    boolean onboardingIsDisplayed = waitForInitialScreen();

    if (onboardingIsDisplayed) {

        step("Navigate through onboarding to the Login screen");

        for (int screen = 1; screen <= 4; screen++) {
            swipeUp();
        }

        step("Open the Login screen");
        WaitUtils.clickable(driver, AndroidLocators.LOGIN);
        driver.findElement(AndroidLocators.LOGIN).click();

    } else {

        step("Login screen is already displayed");
    }

    // --------------------------------------------------
    // Verify Login heading
    // --------------------------------------------------

    step("Verify the Login heading on the login card");

    WaitUtils.visible(
            driver,
            AndroidLocators.LOGIN_HEADING
    );

    Assert.assertTrue(
            driver.findElement(AndroidLocators.LOGIN_HEADING).isDisplayed(),
            "The 'Login' heading is not displayed on the login card."
    );

    System.out.println(
            "Login heading is displayed successfully."
    );

    System.out.println("==============================================");
    System.out.println("TC02 PASSED");
    System.out.println("TC ID   : SC_01_TC_002");
    System.out.println("JIRA ID : BD2M-672");
    System.out.println("Result  : Login heading is displayed.");
    System.out.println("==============================================");
}

/**
 * A fresh installation opens onboarding, while a returning user is
 * restored directly to Login. Both states are valid test entry points.
 */
private boolean waitForInitialScreen() {

    new WebDriverWait(
            driver,
            Duration.ofSeconds(20)
    ).until(
            ignored ->
                    !driver.findElements(AndroidLocators.WELCOME_HEADING).isEmpty()
                            || !driver.findElements(AndroidLocators.OTP_HINT).isEmpty()
    );

    boolean onboardingDisplayed =
            !driver.findElements(AndroidLocators.WELCOME_HEADING).isEmpty();

    if (onboardingDisplayed) {

        System.out.println(
                "Initial onboarding screen is displayed."
        );

    } else {

        System.out.println(
                "Login screen is already displayed."
        );
    }

    return onboardingDisplayed;
}

private void swipeUp() {

    driver.executeScript(
            "mobile: swipeGesture",
            java.util.Map.of(
                    "left", 100,
                    "top", 200,
                    "width", 880,
                    "height", 2100,
                    "direction", "up",
                    "percent", 0.70
            )
    );
}
}
