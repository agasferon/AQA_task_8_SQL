package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private SelenideElement loginField = $("[data-test-id=login] input");
  private SelenideElement passwordField = $("[data-test-id=password] input");
  private SelenideElement loginButton = $("[data-test-id=action-login]");
  private SelenideElement errorNotification = $("[data-test-id='error-notification']");

  public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
    loginField.setValue(authInfo.getLogin());
    passwordField.setValue(authInfo.getPassword());
    loginButton.click();
    return new VerificationPage();
  }

  public void invalidLogin(DataHelper.AuthInfo authInfo) {
    loginField.setValue(authInfo.getLogin());
    passwordField.setValue(authInfo.getPassword());
    loginButton.click();
    errorNotification.shouldBe(Condition.visible);
  }

  public void multipleInvalidLogin(DataHelper.AuthInfo authInfo) {
    loginField.setValue(authInfo.getLogin());
    passwordField.setValue(authInfo.getPassword());
    for (int i = 0; i < DataHelper.getMultiplicityInvalidPasswordToLogin(); i++) {
      loginButton.click();
      errorNotification.shouldBe(Condition.visible);
    }
  }
}