package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.DashboardPage;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.*;

class AuthorisationTest {

    @AfterAll
    public static void CleanAllTables() {
        DataHelper.cleanDataBase();
    }

    @Test
    public void shouldLoginToPersonalAccount() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getLastVerificationCode();
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.DashboardPage();
    }

    @Test
    public void shouldNotEntryIfInvalidLogin() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidLogin();
        loginPage.invalidLogin(authInfo);
    }

    @Test
    public void shouldNotLoginIfInvalidPassword() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPassword();
        loginPage.invalidLogin(authInfo);
    }

    @Test
    public void shouldNotLoginIfInvalidVerificationCode() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getInvalidVerificationCode();
        verificationPage.invalidVerify(verificationCode);
    }

    @Test
    public void shouldLockingUserIfInvalidPasswordSeveralTime() {
        open("http://localhost:9999");
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPassword();
        loginPage.multipleInvalidLogin(authInfo);
        assertNotEquals("active", DataHelper.getUserStatus());
    }
}