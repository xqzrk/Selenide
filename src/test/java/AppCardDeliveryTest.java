import com.codeborne.selenide.Condition;
import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.time.DayOfWeek;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;


public class AppCardDeliveryTest {

    public String generateDate(long addDays, String pattern) {
        return LocalDate.now().plusDays(addDays).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void shouldTest() {
        String planningDate = generateDate(4, "dd.MM.yyyy");
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        $x("//span[@data-test-id='city']//input").setValue("Москва");
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").setValue(planningDate);
        $x("//span[@data-test-id='name']//input").setValue("Иванов-Петровский Иван");
        $x("//span[@data-test-id='phone']//input").setValue("+79998887766");
        $x("//span[@class='checkbox__box']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(visible);
    }

    public int dayOfMeet(int addDays) {
        int potentialDay = LocalDate.now().plusDays(addDays).getDayOfMonth();
        if (LocalDate.now().plusDays(addDays).getDayOfWeek() == DayOfWeek.SATURDAY) {
            potentialDay = potentialDay + 2;
        }
        if (LocalDate.now().plusDays(addDays).getDayOfWeek() == DayOfWeek.SUNDAY) {
            potentialDay = potentialDay + 1;
        }
        return potentialDay;
    }

    public int meetMonthV2(int addDays) {
        return LocalDate.now().plusDays(addDays).getMonthValue();
    }

    public boolean monthCheck() {
        int currentMonth = LocalDate.now().getMonthValue();
        if (currentMonth < meetMonthV2(7)) {
            return true;
        } else {
            return false;
        }
    }

    public String dateOfMeet(int addDays, String pattern) {
        int addDaysPlus = addDays;

        if (LocalDate.now().plusDays(addDays).getDayOfWeek() == DayOfWeek.SATURDAY) {
            addDaysPlus = addDays + 2;
        }
        if (LocalDate.now().plusDays(addDays).getDayOfWeek() == DayOfWeek.SUNDAY) {
            addDaysPlus = addDays + 1;
        }
        return LocalDate.now().plusDays(addDaysPlus).format(DateTimeFormatter.ofPattern(pattern));
    }

    @Test
    public void shouldTestV2() {
        String planningDate = dateOfMeet(7, "dd.MM.yyyy");
        int date = dayOfMeet(7);
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        $x("//span[@data-test-id='city']//input").setValue("Мо");
        $x("//span[contains(text(), 'Москва')]").click();
        $x("//span[@class='input__icon']").click();
        if (monthCheck() == true) {
            $x("//div[@data-step='1']").click();
            $x("//td[contains(text(), '" + date + "')]").click();
        } else {
            $x("//td[contains(text(), '" + date + "')]").click();
        }
        $x("//span[@data-test-id='name']//input").setValue("Иванов-Петровский Иван");
        $x("//span[@data-test-id='phone']//input").setValue("+79998887766");
        $x("//span[@class='checkbox__box']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[@class='notification__content']").shouldHave(Condition.text("Встреча успешно забронирована на " + planningDate), Duration.ofSeconds(15)).shouldBe(visible);
    }
}
