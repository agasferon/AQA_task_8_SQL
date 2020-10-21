package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
  private SelenideElement codeField = $("[data-test-id=code] input");
  private SelenideElement verifyButton = $("[data-test-id=action-verify]");
  private SelenideElement errorNotification = $("[data-test-id='error-notification']");

  public VerificationPage() {
    codeField.shouldBe(visible);
  }

  public void setVerificationCode(String verificationCode) {
    codeField.setValue(verificationCode);
    verifyButton.click();
  }

  public DashboardPage validVerify(String verificationCode) {
    setVerificationCode(verificationCode);
    return new DashboardPage();
  }

  public void invalidVerify(String verificationCode) {
    setVerificationCode(verificationCode);
    errorNotification.shouldBe(Condition.visible);
  }
}