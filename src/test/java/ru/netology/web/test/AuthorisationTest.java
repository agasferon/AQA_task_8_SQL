package ru.netology.web.test;

import lombok.val;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;

class AuthorisationTest {

    @BeforeEach
    public void openPage() {
        open("http://localhost:9999");
    }

    @AfterAll
    public static void CleanAllTables() {
        DataHelper.cleanDataBase();
    }

    @Test
    public void shouldLoginToPersonalAccount() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getLastVerificationCode();
        val dashboardPage = verificationPage.validVerify(verificationCode);
        dashboardPage.dashboardPageVisible();
    }

    @Test
    public void shouldNotEntryIfInvalidLogin() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidLogin();
        loginPage.invalidLogin(authInfo);
    }

    @Test
    public void shouldNotLoginIfInvalidPassword() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPassword();
        loginPage.invalidLogin(authInfo);
    }

    @Test
    public void shouldNotLoginIfInvalidVerificationCode() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getAuthInfo();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getInvalidVerificationCode();
        verificationPage.invalidVerify(verificationCode);
    }

    @Test
    public void shouldLockingUserIfInvalidPasswordSeveralTime() {
        val loginPage = new LoginPage();
        val authInfo = DataHelper.getInvalidPassword();
        loginPage.invalidLogin(authInfo);
        loginPage.cleanLoginPageFields();
        loginPage.invalidLogin(authInfo);
        loginPage.cleanLoginPageFields();
        loginPage.invalidLogin(authInfo);
        loginPage.userLockedMessages();
    }
}