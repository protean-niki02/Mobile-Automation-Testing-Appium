package tests.sprint1;

import framework.BaseTest;
import framework.WaitUtils;
import locators.AndroidLocators;
import org.openqa.selenium.By;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.Map;

public class TC10DobDatePickerTest extends BaseTest {

    private static final By DATE_PICKER =
            By.className("android.widget.DatePicker");

    private static final By DATE_PICKER_NUMBER =
            By.className("android.widget.NumberPicker");

    private static final By DATE_PICKER_HEADER =
            By.xpath(
                    "//*[contains(translate(@text, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), 'select date') "
                            + "or contains(translate(@content-desc, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), 'select date')]"
            );

    private static final By DATE_PICKER_NAVIGATION =
            By.xpath(
                    "//*[contains(translate(@content-desc, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), 'previous month') "
                            + "or contains(translate(@content-desc, "
                            + "'ABCDEFGHIJKLMNOPQRSTUVWXYZ', "
                            + "'abcdefghijklmnopqrstuvwxyz'), 'next month')]"
            );

    private static final By DATE_PICKER_CONFIRMATION =
            By.xpath(
                    "//*[normalize-space(@text)='OK' "
                            + "or normalize-space(@text)='Cancel' "
                            + "or normalize-space(@content-desc)='OK' "
                            + "or normalize-space(@content-desc)='Cancel']"
            );

    @Test(
            description = "BD2M-672 | SC_01_TC_010 - Verify that the Date "
                    + "of Birth / Date of Incorporation field opens a date picker.",
            groups = {"Sprint1", "Login", "Positive"}
    )
    public void verifyDobFieldOpensDatePicker() {
        step("Wait for the initial Bima Sugam onboarding screen");

        if (waitForInitialScreen()) {
            step("Navigate through onboarding to the Login screen");
            for (int screen = 1; screen <= 4; screen++) {
                swipeUp();
            }

            step("Open the Login screen");
            openLoginScreen();
        }

        step("Tap the Date of Birth / Date of Incorporation field");
        clickDobField();

        step("Verify that the date picker is open");
        boolean datePickerOpened = new WebDriverWait(
                driver,
                Duration.ofSeconds(10)
        ).until(ignored ->
                !driver.findElements(DATE_PICKER).isEmpty()
                        || !driver.findElements(DATE_PICKER_NUMBER).isEmpty()
                        || !driver.findElements(DATE_PICKER_HEADER).isEmpty()
                        || !driver.findElements(DATE_PICKER_NAVIGATION).isEmpty()
                        || hasDatePickerActions()
        );

        Assert.assertTrue(
                datePickerOpened,
                "The Date of Birth / Date of Incorporation field did not "
                        + "open a date picker."
        );
    }

    private boolean hasDatePickerActions() {
        return driver.findElements(DATE_PICKER_CONFIRMATION).size() >= 2;
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

    private void clickDobField() {
        try {
            WaitUtils.visible(driver, AndroidLocators.DOB);
            driver.findElement(AndroidLocators.DOB).click();
        } catch (WebDriverException firstAttempt) {
            try {
                WaitUtils.visible(driver, AndroidLocators.DOB_INPUT);
                driver.findElement(AndroidLocators.DOB_INPUT).click();
            } catch (WebDriverException secondAttempt) {
                driver.executeScript(
                        "mobile: clickGesture",
                        Map.of("x", 540, "y", 1280)
                );
            }
        }
    }

    private void openLoginScreen() {
        try {
            WaitUtils.clickable(driver, AndroidLocators.LOGIN);
            driver.findElement(AndroidLocators.LOGIN).click();
        } catch (TimeoutException exception) {
            driver.executeScript(
                    "mobile: clickGesture",
                    Map.of("x", 540, "y", 1920)
            );
        }
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
