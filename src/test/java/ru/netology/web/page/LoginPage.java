package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import org.openqa.selenium.Keys;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selectors.withText;
import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
  private SelenideElement loginField = $("[data-test-id=login] input");
  private SelenideElement passwordField = $("[data-test-id=password] input");
  private SelenideElement loginButton = $("[data-test-id=action-login]");
  private SelenideElement errorNotification = $("[data-test-id='error-notification']");

  public void cleanLoginPageFields() {
    loginField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);;
    passwordField.sendKeys(Keys.CONTROL + "a", Keys.DELETE);;
  }

  public void setAuthInfo(DataHelper.AuthInfo authInfo) {
    cleanLoginPageFields();
    loginField.setValue(authInfo.getLogin());
    passwordField.setValue(authInfo.getPassword());
    loginButton.click();
  }

  public VerificationPage validLogin(DataHelper.AuthInfo authInfo) {
    setAuthInfo(authInfo);
    return new VerificationPage();
  }

  public void invalidLogin(DataHelper.AuthInfo authInfo) {
    setAuthInfo(authInfo);
    errorNotification.shouldBe(Condition.visible);
  }

  public void userLockedMessages() {
    $(withText("Пользователь заблокирован!")).waitUntil(visible, 5000);
  }
}