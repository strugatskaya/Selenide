import com.codeborne.selenide.SelenideElement;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byText;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;

public class AlphaBankDeliveryTest {
    public LocalDate deliveryDate;
    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");

    public void setDeliveryDate(int days) {
        deliveryDate = LocalDate.now().plusDays(3);
        String formatDate = deliveryDate.format(formatter);
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.CONTROL, "a"));
        $("[data-test-id=date] input").sendKeys(Keys.chord(Keys.DELETE));
        $("[data-test-id=date] input").setValue(formatDate);
    }

    @BeforeEach
    void setUp() {
        open("http://localhost:9999");
    }

    @Test
    void shouldTestEveryFieldsCorrectPositive() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Симферополь");
        setDeliveryDate(1);
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
        form.$("[data-test-id=city] input").setValue("Ярославль");
        setDeliveryDate(30);
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
        setDeliveryDate(29);
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
        setDeliveryDate(0);
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
        setDeliveryDate(15);
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
        setDeliveryDate(2);
        form.$("[data-test-id=name] input").setValue("");
        form.$("[data-test-id=phone] input").setValue("+70000000000");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Поле обязательно для заполнения"));
    }

    @Test
    void shouldTestWrongCityNegative() {
        SelenideElement form = $(".form");
        form.$("[data-test-id=city] input").setValue("Ноябрьск");
        setDeliveryDate(16);
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
        setDeliveryDate(5);
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
        setDeliveryDate(2);
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
        setDeliveryDate(26);
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
        setDeliveryDate(3);
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
        setDeliveryDate(8);
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
        setDeliveryDate(18);
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
        setDeliveryDate(13);
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
        setDeliveryDate(11);
        form.$("[data-test-id=name] input").setValue("Францеска Финдабаир");
        form.$("[data-test-id=phone] input").setValue("");
        form.$("[data-test-id=agreement]").click();
        form.$$("[role=button]").find(exactText("Забронировать")).click();
        form.$(byText("Поле обязательно для заполнения"));
    }
}