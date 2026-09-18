package locators;

import io.appium.java_client.AppiumBy;
import org.openqa.selenium.By;

public class AndroidLocators {

    // =========================================
    // WELCOME / SPLASH SCREEN
    // =========================================

    public static final By LOGIN =
            AppiumBy.accessibilityId("Login");

    public static final By WELCOME_HEADING =
            AppiumBy.accessibilityId("Welcome to Bima Sugam");


    // =========================================
    // LOGIN SCREEN
    // =========================================

    public static final By LOGIN_HEADING =
            AppiumBy.xpath(
                    "//*[normalize-space(@text)='Login' "
                            + "or normalize-space(@content-desc)='Login']"
            );

    public static final By OTP_HINT =
            AppiumBy.accessibilityId(
                    "Your details will be verified by a secure OTP"
            );

    public static final By MOBILE_NUMBER =
            AppiumBy.accessibilityId(
                    "Mobile Number*"
            );

    public static final By COUNTRY_CODE_SELECTOR =
            AppiumBy.xpath(
                    "(//android.widget.EditText)[1]"
                            + "/preceding::android.widget.ImageView[1]"
            );

    public static final By ALTERNATE_COUNTRY_CODE =
            AppiumBy.xpath(
                    "//*[starts-with(normalize-space(@text), '+') "
                            + "and normalize-space(@text) != '+91']"
            );

    public static final By MOBILE_NUMBER_INPUT =
            AppiumBy.xpath("(//android.widget.EditText)[1]");

    public static final By DOB =
            AppiumBy.accessibilityId(
                    "Date of Birth / Date of Incorporation*"
            );

    public static final By DOB_INPUT =
            AppiumBy.androidUIAutomator(
                    "new UiSelector()" +
                    ".className(\"android.widget.EditText\")" +
                    ".instance(1)"
            );

    public static final By LOGIN_VIA_OTP =
            AppiumBy.accessibilityId(
                    "Login via OTP"
            );


    // =========================================
    // PRODUCTS
    // =========================================

    public static final By EXPLORE_PRODUCTS =
            AppiumBy.accessibilityId(
                    "Explore Our Products"
            );

    public static final By PRIVATE_CAR_INSURANCE =
            AppiumBy.accessibilityId(
                    "Private Car Insurance"
            );

    public static final By PRIVATE_BIKE_INSURANCE =
            AppiumBy.accessibilityId(
                    "Private Bike Insurance"
            );

    public static final By COMMERCIAL_VEHICLE_INSURANCE =
            AppiumBy.accessibilityId(
                    "Commercial Vehicle Insurance"
            );

    public static final By HEALTH_INSURANCE =
            AppiumBy.accessibilityId(
                    "Health Insurance"
            );

    public static final By TERM_LIFE_INSURANCE =
            AppiumBy.accessibilityId(
                    "Term Life Insurance"
            );


    // =========================================
    // OTP SCREEN
    // =========================================

    public static final By OTP_VERIFICATION =
            AppiumBy.accessibilityId(
                    "OTP Verification"
            );

    public static final By OTP_INPUT =
            AppiumBy.androidUIAutomator(
                    "new UiSelector()" +
                    ".className(\"android.widget.EditText\")" +
                    ".instance(0)"
            );

    public static final By RESEND_OTP =
            AppiumBy.accessibilityId(
                    "Resend OTP"
            );

    public static final By VALIDATE =
            AppiumBy.accessibilityId(
                    "Validate"
            );


    // =========================================
    // COMMON
    // =========================================

    public static final By CANCEL =
            AppiumBy.accessibilityId(
                    "Cancel"
            );

    public static final By BACK =
            AppiumBy.accessibilityId(
                    "Back"
            );

    public static final By CLOSE =
            AppiumBy.accessibilityId(
                    "Close"
            );
}