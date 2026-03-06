package pages;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class ValidatorPage {
    // URL тестового сайта
    private static final String BASE_URL = "https://ligior.github.io";

    private final SelenideElement form = $("#validatorForm");
    private final SelenideElement inputField = $("#numberInput");
    private final SelenideElement submitBtn = $("#submitBtn");
    private final SelenideElement resultMessage = $("#resultMessage");
    private final SelenideElement hint = $(".hint");

    public ValidatorPage openPage() {
        open(BASE_URL);
        return this;
    }

    public ValidatorPage verifyPageLoaded() {
        form.shouldBe(Condition.visible);
        hint.shouldHave(Condition.text("число от 5 до 10"));
        return this;
    }

    public ValidatorPage enterNumber(String value) {
        inputField.clear();
        inputField.setValue(value);
        return this;
    }


    public ValidatorPage submitForm() {
        submitBtn.click();
        return this;
    }

    public ValidatorPage submitFormWithEnter() {
        inputField.pressEnter();
        return this;
    }


    public ValidatorPage verifySuccessMessage() {
        resultMessage.shouldHave(Condition.text("✅ Принято"))
                .shouldHave(Condition.cssClass("accepted"));
        return this;
    }

    public ValidatorPage verifyErrorMessage() {
        resultMessage.shouldHave(Condition.text("❌ Ошибка"))
                .shouldHave(Condition.cssClass("error"));
        return this;
    }

    public ValidatorPage verifyInitialMessage() {
        resultMessage.shouldHave(Condition.text("👆 ожидание ввода..."))
                .shouldNotHave(Condition.cssClass("accepted"))
                .shouldNotHave(Condition.cssClass("error"));
        return this;
    }


    public ValidatorPage clearInputField() {
        inputField.clear();
        return this;
    }

    public ValidatorPage focusOnInput() {
        inputField.click();
        return this;
    }

    public ValidatorPage verifyPlaceholder(String placeholder) {
        inputField.shouldHave(Condition.attribute("placeholder", placeholder));
        return this;
    }

    public ValidatorPage verifyInputType() {
        inputField.shouldHave(Condition.attribute("type", "number"));
        return this;
    }

    public ValidatorPage verifyEnterValueMessage() {
        resultMessage.shouldHave(Condition.text("🔹 введите значение"));
        return this;
    }

    public ValidatorPage verifyWaitingMessage() {
        resultMessage.shouldHave(Condition.text("👆 ожидание ввода..."));
        return this;
    }

    public ValidatorPage verifyMessageNotError() {
        // Проверяем, что сообщение изменилось на актуальное для сайта
        resultMessage.shouldNotHave(Condition.cssClass("error"));
        resultMessage.shouldHave(Condition.text("🔹 введите значение")); // <-- ИЗМЕНЕНО
        return this;
    }

    public String getResultMessageText() {
        return resultMessage.getText();
    }

    public String getResultMessageClass() {
        return resultMessage.getAttribute("class");
    }
}