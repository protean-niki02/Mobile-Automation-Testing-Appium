package tests.sprint1;

import io.appium.java_client.AppiumBy;
import io.appium.java_client.AppiumDriver;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import java.util.List;

public class DashboardPage {

    private final AppiumDriver driver;

    private final By homeTab = AppiumBy.accessibilityId("Home");

    private final By notificationBell = AppiumBy.accessibilityId("Notifications");

        private final By notificationBadge = AppiumBy.androidUIAutomator(
            "new UiSelector().className(\"android.widget.TextView\")"
                + ".textMatches(\"[0-9]+\")");

    private final By personalizedGreeting = AppiumBy.androidUIAutomator(
            "new UiSelector().textMatches(\"Namaste\\s+.+\")");

    private final By notificationsHeading = AppiumBy.androidUIAutomator(
            "new UiSelector().text(\"Notifications\")");

    public DashboardPage(AppiumDriver driver) {
        this.driver = driver;
    }

    public void selectHomeTab() {
        driver.findElement(homeTab).click();
    }

    public boolean isPersonalizedGreetingDisplayed() {
        return driver.findElement(personalizedGreeting).isDisplayed();
    }

    public void openNotifications() {
        driver.findElement(notificationBell).click();
    }

    public int getUnreadNotificationCount() {
        List<WebElement> badges = driver.findElements(notificationBadge);
        if (badges.isEmpty()) {
            return 0;
        }
        return Integer.parseInt(badges.get(0).getText());
    }

    public boolean areNotificationsDisplayed() {
        return driver.findElement(notificationsHeading).isDisplayed();
    }
}