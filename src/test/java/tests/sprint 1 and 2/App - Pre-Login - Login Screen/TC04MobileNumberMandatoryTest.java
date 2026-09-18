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

public class TC04MobileNumberMandatoryTest extends BaseTest {

    private static final By MOBILE_NUMBER_REQUIRED_MESSAGE =
            By.xpath(
                    "//*[contains(translate(@text, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), 'mobile number') "
                            + "and (contains(translate(@text, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), 'required') "
                            + "or contains(translate(@text, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), 'mandatory'))]"
            );

    @Test(
            description = "BD2M-672 | SC_01_TC_004 - Verify that the Mobile "
                    + "Number field is mandatory on the login card.",
            groups = {"Sprint1", "Login", "Negative"}
    )
    public void verifyMobileNumberFieldIsMandatory() {
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

        step("Leave Mobile Number blank and tap Login via OTP");
        WaitUtils.visible(driver, AndroidLocators.MOBILE_NUMBER_INPUT);
        WaitUtils.clickable(driver, AndroidLocators.LOGIN_VIA_OTP);
        driver.findElement(AndroidLocators.LOGIN_VIA_OTP).click();

        step("Verify that Mobile Number is mandatory");
        Assert.assertTrue(
                isDisplayed(MOBILE_NUMBER_REQUIRED_MESSAGE)
                        || isDisplayed(AndroidLocators.MOBILE_NUMBER),
                "Mobile Number should remain mandatory when Login via OTP "
                        + "is tapped with an empty value."
        );
        Assert.assertTrue(
                driver.findElements(AndroidLocators.OTP_VERIFICATION).isEmpty(),
                "The app should not navigate to OTP verification without a "
                        + "Mobile Number."
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