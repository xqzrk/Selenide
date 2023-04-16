import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.Keys;

import java.text.SimpleDateFormat;
import java.time.Duration;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;


public class AppCardDeliveryTest {

    public String meet() {
        Calendar calendar = new GregorianCalendar();
        calendar.add(Calendar.DAY_OF_MONTH, 3);
        Date date = calendar.getTime();
        SimpleDateFormat formatter = new SimpleDateFormat("dd.MM.yyyy");
        String meetDate = formatter.format(date);
        return meetDate;
    }

    public int meetDate() {
        Calendar calendar2 = new GregorianCalendar();
        calendar2.add(Calendar.DAY_OF_MONTH, 7);
        int dayOfWeek = calendar2.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek > 6 & dayOfWeek < 8) {
            calendar2.add(Calendar.DAY_OF_MONTH, 2);
        }
        if (dayOfWeek > 0 & dayOfWeek < 2) {
            calendar2.add(Calendar.DAY_OF_MONTH, 1);
        }
        int meetDate = calendar2.get(Calendar.DATE);
        return meetDate;
    }

    public int meetMonth() {
        Calendar calendar2 = new GregorianCalendar();
        calendar2.add(Calendar.DAY_OF_MONTH, 7);
        int dayOfWeek = calendar2.get(Calendar.DAY_OF_WEEK);
        if (dayOfWeek > 6 & dayOfWeek < 8) {
            calendar2.add(Calendar.DAY_OF_MONTH, 2);
        }
        if (dayOfWeek > 0 & dayOfWeek < 2) {
            calendar2.add(Calendar.DAY_OF_MONTH, 1);
        }
        return calendar2.get(Calendar.MONTH);
    }

    public boolean monthComparison() {
        Calendar actual = new GregorianCalendar();
        int actualMonth = actual.get(Calendar.MONTH);
        if (actualMonth < meetMonth()) {
            return true;
        } else {
            return false;
        }
    }


    @Test
    public void shouldTest() {
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        $x("//span[@data-test-id='city']//input").setValue("Москва");
        $x("//span[@data-test-id='date']//input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $x("//span[@data-test-id='date']//input").setValue(meet());
        $x("//span[@data-test-id='name']//input").setValue("Иванов-Петровский Иван");
        $x("//span[@data-test-id='phone']//input").setValue("+79998887766");
        $x("//span[@class='checkbox__box']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
    }

    @Test
    public void shouldTestV2() {
        int date = meetDate();
        Configuration.holdBrowserOpen = true;
        open("http://localhost:9999");
        $x("//span[@data-test-id='city']//input").setValue("Мо");
        $x("//span[contains(text(), 'Москва')]").click();
        $x("//span[@class='input__icon']").click();
        if (monthComparison() == true) {
            $x("//div[@data-step='1']").click();
            $x("//td[contains(text(), '" + date + "')]").click();
        } else {
            $x("//td[contains(text(), '" + date + "')]").click();
        }
        $x("//span[@data-test-id='name']//input").setValue("Иванов-Петровский Иван");
        $x("//span[@data-test-id='phone']//input").setValue("+79998887766");
        $x("//span[@class='checkbox__box']").click();
        $x("//span[contains(text(), 'Забронировать')]").click();
        $x("//div[contains(text(), 'Успешно!')]").shouldBe(visible, Duration.ofSeconds(15));
    }
}
