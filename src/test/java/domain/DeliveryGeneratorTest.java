package domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selenide.*;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;

public class DeliveryGeneratorTest {

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
    void setup() {
        open("http://127.0.0.1:9999");
    }

    @Test
    void shouldTestAllFieldsCorrectPositive01() {
        $("[data-test-id=city] input").setValue("Севастополь");
        setDeliveryDate(1);
        $("[data-test-id=name] input").setValue("Джон Константин");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15));
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Встреча успешно забронирована на"));
    }

    @Test
    void shouldTestAllFieldsCorrectWithDash30() {
        $("[data-test-id=city] input").setValue("Салехард");
        setDeliveryDate(30);
        $("[data-test-id=name] input").setValue("Джон Киану-Константин");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Встреча успешно забронирована на"));
    }

    @Test
    void shouldTestAllFieldsCorrect29Days() {
        $("[data-test-id=city] input").setValue("Санкт-Петербург");
        setDeliveryDate(29);
        $("[data-test-id=name] input").setValue("Нео Плащев");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Встреча успешно забронирована на"));

    }

    @Test
    void shouldTestAllFieldsCorrect0ays() {
        $("[data-test-id=city] input").setValue("Тверь");
        setDeliveryDate(0);
        $("[data-test-id=name] input").setValue("Тринити Степанова");
        $("[data-test-id=phone] input").setValue("+70000000000");
        $("[data-test-id=agreement]").click();
        $$("button").find(exactText("Забронировать")).click();
        $("[data-test-id=notification]").shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text("Встреча успешно забронирована на"));
    }
}