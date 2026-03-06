package web;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import pages.ValidatorPage;

import static com.codeborne.selenide.Selenide.closeWebDriver;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ValidatorTest {
    private ValidatorPage validatorPage;

    @BeforeEach
    public void setUp() {
        validatorPage = new ValidatorPage();
        validatorPage.openPage()
                .verifyPageLoaded()
                .verifyInitialMessage();
    }

    @AfterEach
    public void tearDown() {
        closeWebDriver();
    }

    @Test
    @DisplayName("Ввод числа 5 - граничное значение (минимум)")
    public void testValidNumberMin() {
        validatorPage.enterNumber("5")
                .submitForm()
                .verifySuccessMessage();
    }

    @Test
    @DisplayName("Ввод числа 10 - граничное значение (максимум)")
    public void testValidNumberMax() {
        validatorPage.enterNumber("10")
                .submitForm()
                .verifySuccessMessage();
    }

    @Test
    @DisplayName("Ввод числа 7 - среднее значение в диапазоне")
    public void testValidNumberMiddle() {
        validatorPage.enterNumber("7")
                .submitForm()
                .verifySuccessMessage();
    }

    @ParameterizedTest
    @DisplayName("Ввод целых чисел меньше 5")
    @ValueSource(strings = {"4", "0", "-1", "-100"})
    public void testInvalidNumbersLessThanMin(String number) {
        validatorPage.enterNumber(number)
                .submitForm()
                .verifyErrorMessage();
    }

    @ParameterizedTest
    @DisplayName("Ввод целых чисел больше 10")
    @ValueSource(strings = {"11", "100", "1000"})
    public void testInvalidNumbersMoreThanMax(String number) {
        validatorPage.enterNumber(number)
                .submitForm()
                .verifyErrorMessage();
    }

    @Test
    @DisplayName("Попытка ввода дробного числа 4.9")
    public void testDecimalNumberLessThanMin() {
        validatorPage.enterNumber("4.9")
                .submitForm();

        // Проверяем специфическое сообщение для дробных чисел
        validatorPage.verifyWaitingMessage();
    }

    @Test
    @DisplayName("Попытка ввода дробного числа 10.1")
    public void testDecimalNumberMoreThanMax() {
        validatorPage.enterNumber("10.1")
                .submitForm();

        // Проверяем специфическое сообщение для дробных чисел
        validatorPage.verifyWaitingMessage();
    }

    @Test
    @DisplayName("Отправка пустой формы")
    public void testEmptyFormSubmission() {
        validatorPage.clearInputField()
                .submitForm()
                .verifyEnterValueMessage();
    }

    @Test
    @DisplayName("Отправка формы через Enter")
    public void testSubmitWithEnter() {
        validatorPage.enterNumber("8")
                .submitFormWithEnter()
                .verifySuccessMessage();
    }

    @Test
    @DisplayName("Попытка ввода текста")
    public void testTextInput() {
        validatorPage.enterNumber("abc")
                .submitForm();

        // Проверяем сообщение "🔹 введите значение"
        validatorPage.verifyEnterValueMessage();
    }

    @Test
    @DisplayName("Попытка ввода специальных символов")
    public void testSpecialCharactersInput() {
        validatorPage.enterNumber("@#$")
                .submitForm();

        // Проверяем сообщение "🔹 введите значение"
        validatorPage.verifyEnterValueMessage();
    }

    @Test
    @DisplayName("Проверка placeholder")
    public void testPlaceholder() {
        validatorPage.verifyPlaceholder("например 7");
    }

    @Test
    @DisplayName("Проверка типа поля ввода")
    public void testInputType() {
        validatorPage.verifyInputType();
    }

    @Test
    @DisplayName("Несколько проверок подряд")
    public void testMultipleValidations() {
        validatorPage.enterNumber("6")
                .submitForm()
                .verifySuccessMessage()
                .enterNumber("12")
                .submitForm()
                .verifyErrorMessage()
                .enterNumber("8")
                .submitForm()
                .verifySuccessMessage();
    }

    @Test
    @DisplayName("Быстрое нажатие кнопки несколько раз")
    public void testRapidButtonClicks() {
        validatorPage.enterNumber("7");
        for (int i = 0; i < 5; i++) {
            validatorPage.submitForm();
        }
        validatorPage.verifySuccessMessage();
    }

    @Test
    @DisplayName("Ввод числа с пробелами")
    public void testNumberWithSpaces() {
        validatorPage.enterNumber("  7  ")
                .submitForm()
                .verifySuccessMessage();
    }

    @Test
    @DisplayName("Проверка времени ответа")
    public void testResponseTime() {
        long startTime = System.currentTimeMillis();

        validatorPage.enterNumber("7")
                .submitForm()
                .verifySuccessMessage();

        long endTime = System.currentTimeMillis();
        long responseTime = endTime - startTime;

        assertTrue(responseTime < 2000,
                "Время ответа превышает 2 секунды: " + responseTime + "мс");

        System.out.println("✅ Время ответа: " + responseTime + "мс");
    }

    @Test
    @DisplayName("Проверка поведения при фокусе на поле с ошибкой")
    public void testFocusOnFieldWithError() {
        validatorPage.enterNumber("11")
                .submitForm()
                .verifyErrorMessage();

        // Получаем информацию ДО фокуса
        String beforeText = validatorPage.getResultMessageText();
        String beforeClass = validatorPage.getResultMessageClass();
        System.out.println("ДО фокуса - Текст: '" + beforeText + "', Класс: '" + beforeClass + "'");

        // Кликаем на поле
        validatorPage.focusOnInput();

        // Ждем немного
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            // Игнорируем
        }

        // Получаем информацию ПОСЛЕ фокуса
        String afterText = validatorPage.getResultMessageText();
        String afterClass = validatorPage.getResultMessageClass();
        System.out.println("ПОСЛЕ фокуса - Текст: '" + afterText + "', Класс: '" + afterClass + "'");

        // Проверяем, что сообщение НЕ изменилось (так работает сайт)
        Assertions.assertEquals(beforeText, afterText, "Текст не должен меняться при фокусе");
        Assertions.assertEquals(beforeClass, afterClass, "Класс не должен меняться при фокусе");
    }
}