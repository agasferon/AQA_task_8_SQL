package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import ru.netology.web.data.DataHelper;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.$;

public class VerificationPage {
  private SelenideElement codeField = $("[data-test-id=code] input");
  private SelenideElement verifyButton = $("[data-test-id=action-verify]");
  private SelenideElement errorNotification = $("[data-test-id='error-notification']");

  public VerificationPage() {
    codeField.shouldBe(visible);
  }

  public DashboardPage validVerify(String verificationCode) {
    codeField.setValue(DataHelper.getLastVerificationCode());
    verifyButton.click();
    return new DashboardPage();
  }

  public void invalidVerify(String verificationCode) {
    codeField.setValue(DataHelper.getInvalidVerificationCode());
    verifyButton.click();
    errorNotification.shouldBe(Condition.visible);
  }
}