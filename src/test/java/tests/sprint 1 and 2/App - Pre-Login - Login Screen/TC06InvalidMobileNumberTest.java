package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import locators.AndroidLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class TC06InvalidMobileNumberTest extends BaseTest {

    private static final String INVALID_MOBILE_NUMBER = "8236453234";

    private static final By INVALID_MOBILE_ERROR =
            By.xpath(
                    "//*[contains(translate(@text, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), 'mobile number') "
                            + "and (contains(translate(@text, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), 'invalid') "
                            + "or contains(translate(@text, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), '10 digit'))]"
            );

    @Test(
            description = "BD2M-672 | SC_01_TC_006 - Verify that an invalid "
                    + "mobile number is rejected in the Mobile Number field.",
            groups = {"Sprint1", "Login", "Negative"}
    )
    public void verifyInvalidMobileNumberIsRejected() {
        step("Wait for the initial Bima Sugam onboarding screen");

        if (waitForInitialScreen()) {
            step("Navigate through onboarding to the Login screen");
            for (int screen = 1; screen <= 4; screen++) {
                swipeUp();
            }

            step("Open the Login screen");
            driver.executeScript(
                    "mobile: clickGesture",
                    Map.of("x", 540, "y", 1920)
            );
        }

        step("Enter an invalid mobile number");
        WaitUtils.visible(driver, AndroidLocators.MOBILE_NUMBER_INPUT);
        var mobileNumber = driver.findElement(
                AndroidLocators.MOBILE_NUMBER_INPUT
        );
        mobileNumber.clear();
        mobileNumber.sendKeys(INVALID_MOBILE_NUMBER);

        step("Tap Login via OTP and verify rejection");
        WaitUtils.clickable(driver, AndroidLocators.LOGIN_VIA_OTP);
        driver.findElement(AndroidLocators.LOGIN_VIA_OTP).click();

        Assert.assertTrue(
                isDisplayed(INVALID_MOBILE_ERROR)
                        || isDisplayed(AndroidLocators.MOBILE_NUMBER),
                "Invalid mobile number should be rejected."
        );
        Assert.assertTrue(
                driver.findElements(AndroidLocators.OTP_VERIFICATION).isEmpty(),
                "The app should not navigate to OTP verification for an "
                        + "invalid mobile number."
        );
    }

    private boolean waitForInitialScreen() {
        new WebDriverWait(driver, Duration.ofSeconds(10)).until(
                ignored ->
                        !driver.findElements(AndroidLocators.WELCOME_HEADING)
                                .isEmpty()
                                || !driver.findElements(AndroidLocators.LOGIN_HEADING)
                                .isEmpty()
                                || !driver.findElements(AndroidLocators.OTP_HINT)
                                .isEmpty()
        );
        return !driver.findElements(AndroidLocators.WELCOME_HEADING).isEmpty();
    }

    private boolean isDisplayed(By locator) {
        return !driver.findElements(locator).isEmpty()
                && driver.findElement(locator).isDisplayed();
    }

    private void swipeUp() {
        driver.executeScript(
                "mobile: swipeGesture",
                Map.of(
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
