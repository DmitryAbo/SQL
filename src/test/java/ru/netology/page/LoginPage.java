package ru.netology.page;

import com.codeborne.selenide.Condition;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;
import org.openqa.selenium.Keys;
import ru.netology.data.DataHelper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;

public class LoginPage {
    public VerificationPage Login(DataHelper.AuthInfo info) {
        $("[data-test-id=login] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=login] input").setValue(info.getLogin());
        $("[data-test-id=password] input").sendKeys(Keys.chord(Keys.SHIFT, Keys.HOME), Keys.BACK_SPACE);
        $("[data-test-id=password] input").setValue(info.getPassword());
        $("[data-test-id=action-login]").click();
        return new VerificationPage();
    }

    public void checkNotificationError() {
        $("div[data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    public void checkLoginPage() {
        $("[data-test-id=login] input").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }

    @SneakyThrows
    public boolean checkActiveClientStatus(String login) {
        boolean isActive;
        QueryRunner runner = new QueryRunner();
        String usersStatusSQL = "SELECT status FROM users where login = ?";

        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app",
                        "app_user",
                        "password")
        ) {
            return isActive = runner.query(conn, usersStatusSQL, login, new ScalarHandler<>()).equals("active");
        }
    }
}
