package tests.sprint1;

import framework.BaseTest;
import org.testng.Assert;
import org.testng.annotations.Test;

public class DashboardHomeTest extends BaseTest {

    @Test(description = "BD2M-669 - TC01 - Dashboard - Home Tab")
    public void TC01_dashboardHomeTab() {

        DashboardPage dashboardPage = new DashboardPage(driver);

        step("Tap Home tab");
        dashboardPage.selectHomeTab();

        step("Verify personalized greeting is displayed in the Home tab top bar");
        Assert.assertTrue(
            dashboardPage.isPersonalizedGreetingDisplayed(),
            "Personalized greeting 'Namaste [Name]' should be displayed in the Home tab top bar");
    }

    @Test(description = "BD2M-669 - SC_01_TC_003 - Open Notifications")
    public void TC03_notificationBellOpensNotifications() {

        DashboardPage dashboardPage = new DashboardPage(driver);

        step("Tap the notification bell");
        dashboardPage.openNotifications();

        step("Verify Notifications is open");
        Assert.assertTrue(
                dashboardPage.areNotificationsDisplayed(),
                "Notifications should open after tapping the notification bell");
    }

    @Test(description = "BD2M-669 - SC_01_TC_004 - Notification Bell Unread Badge")
    public void TC04_notificationBellShowsUnreadCount() {

        DashboardPage dashboardPage = new DashboardPage(driver);

        step("Land on the Home tab");
        dashboardPage.selectHomeTab();

        step("Read the notification bell badge");
        int unreadNotificationCount = dashboardPage.getUnreadNotificationCount();

        step("Verify the badge reflects unread notifications");
        Assert.assertTrue(
                unreadNotificationCount > 0,
                "Notification bell badge should show a count greater than zero");
    }
}