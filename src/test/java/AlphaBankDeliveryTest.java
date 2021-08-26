import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;


public class AlphaBankDeliveryTest {

    @BeforeEach
    void setUp() {
        open("http://127.0.0.1:9999");
    }

    @Test
    void shouldTestEveryFieldsCorrectPositive() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Симферополь");
        form.$("[data-test-id=date] input").setValue("10.10.2021");
        form.$("[data-test-id=name] input").setValue("Геральт ИзРивии");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        $("[data-test-id='notification'] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(text("Встреча успешно забронирована на"));

    }

    @Test
    void shouldTestEveryFieldsCorrectWithDashNamePositive() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Петропавловск-Камчатский");
        form.$("[data-test-id=date] input").setValue("31.12.2021");
        form.$("[data-test-id=name] input").setValue("Геральт-Ведьмак Из-Ривии");
        form.$("[data-test-id=phone] input").setValue("+79999999999");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        $("[data-test-id='notification'] .notification__content").shouldBe(visible, Duration.ofSeconds(15)).
                shouldHave(text("Встреча успешно забронирована на"));
    }

    @Test
    void shouldTestNoRussianNameNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Пермь");
        form.$("[data-test-id=date] input").setValue("01.01.2022");
        form.$("[data-test-id=name] input").setValue("Гepaльт Изpивии");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));

    }

    @Test
    void shouldTestNumbersInNameNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Волгоград");
        form.$("[data-test-id=date] input").setValue("13.12.2020");
        form.$("[data-test-id=name] input").setValue("6Геральт Изривии");
        form.$("[data-test-id=phone] input").setValue("+40000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestSymbolsInNameNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Новосибирск");
        form.$("[data-test-id=date] input").setValue("14.09.2021");
        form.$("[data-test-id=name] input").setValue("Геральт.? Изривии");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Имя и Фамилия указаные неверно. Допустимы только русские буквы, пробелы и дефисы."));
    }

    @Test
    void shouldTestNoNameNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Москва");
        form.$("[data-test-id=date] input").setValue("13.11.2021");
        form.$("[data-test-id=name] input").setValue("");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestWrongDate0Negative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Вологда");
        form.$("[data-test-id=date] input").setValue("00.00.0000");
        form.$("[data-test-id=name] input").setValue("Йеннифер ИзВенгерберга");
        form.$("[data-test-id=phone] input").setValue("+311111111111");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Неверно введена дата"));
    }

    @Test
    void shouldTestWrongDate9Negative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Вологда");
        form.$("[data-test-id=date] input").setValue("99.99.9999");
        form.$("[data-test-id=name] input").setValue("Йеннифер Из-Венгерберга");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Неверно введена дата"));
    }

    @Test
    void shouldTestWrongDayNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Владикавказ");
        form.$("[data-test-id=date] input").setValue("31.11.2021");
        form.$("[data-test-id=name] input").setValue("Весемир Ведьмак");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Неверно введена дата"));
    }

    @Test
    void shouldTestManyNumbersInDateNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Белгород");
        form.$("[data-test-id=date] input").setValue("01.082.2022");
        form.$("[data-test-id=name] input").setValue("Весемир Ведьмак");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Неверно введена дата"));
    }

    @Test
    void shouldTestLettersInDateNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Иркутск");
        form.$("[data-test-id=date] input").setValue("Д1.10.2021");
        form.$("[data-test-id=name] input").setValue("Трисс Мериголд");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Неверно введена дата"));
    }

    @Test
    void shouldTestNotEnoughNumbersInDateNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Ульяновск");
        form.$("[data-test-id=date] input").setValue("1.10.2021");
        form.$("[data-test-id=name] input").setValue("Трисс Мериголд");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Неверно введена дата"));
    }

    @Test
    void shouldTestNoDateNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Ульяновск");
        form.$("[data-test-id=date] input").setValue("");
        form.$("[data-test-id=name] input").setValue("Трисс Мериголд");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Неверно введена дата"));
    }

    @Test
    void shouldTestWrongCityNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Ноябрьск");
        form.$("[data-test-id=date] input").setValue("11.11.2021");
        form.$("[data-test-id=name] input").setValue("Регис Вампир");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestLatinLettersInCityNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Пермl");
        form.$("[data-test-id=date] input").setValue("11.11.2021");
        form.$("[data-test-id=name] input").setValue("Эмгыр Ванэмрейс");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestNoCityNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("");
        form.$("[data-test-id=date] input").setValue("01.12.2021");
        form.$("[data-test-id=name] input").setValue("Кейра Мец");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestNumbersInCityNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Мо6ква");
        form.$("[data-test-id=date] input").setValue("01.12.2024");
        form.$("[data-test-id=name] input").setValue("Филиппа Эйрхарт");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Доставка в выбранный город недоступна"));
    }

    @Test
    void shouldTestNotEnoughNumbersInTeleNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        form.$("[data-test-id=date] input").setValue("01.11.2021");
        form.$("[data-test-id=name] input").setValue("Кагыр Дыффин");
        form.$("[data-test-id=phone] input").setValue("+7000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestTooMuchNumbersInTeleNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        form.$("[data-test-id=date] input").setValue("01.11.2021");
        form.$("[data-test-id=name] input").setValue("Кагыр Дыффин");
        form.$("[data-test-id=phone] input").setValue("+700000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestLettersInTeleNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Санкт-Петербург");
        form.$("[data-test-id=date] input").setValue("01.11.2021");
        form.$("[data-test-id=name] input").setValue("Кагыр Дыффин");
        form.$("[data-test-id=phone] input").setValue("+7000000000h");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
    }

    @Test
    void shouldTestSymbolsInTeleNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Астрахань");
        form.$("[data-test-id=date] input").setValue("09.09.2031");
        form.$("[data-test-id=name] input").setValue("Францеска Финдабаир");
        form.$("[data-test-id=phone] input").setValue("++70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Телефон указан неверно. Должно быть 11 цифр, например, +79012345678."));
        ;
    }

    @Test
    void shouldTestNoTeleNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Астрахань");
        form.$("[data-test-id=date] input").setValue("09.09.2031");
        form.$("[data-test-id=name] input").setValue("Францеска Финдабаир");
        form.$("[data-test-id=phone] input").setValue("");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Поле обязательно для заполнения"));
    }
}