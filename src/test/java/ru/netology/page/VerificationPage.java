package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.SneakyThrows;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;
import java.time.Duration;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.sleep;

public class VerificationPage {
    private SelenideElement codeField = $("[data-test-id=code] input");
    private SelenideElement verifyButton = $("[data-test-id=action-verify]");

    public VerificationPage() {
    }

    ;

    @SneakyThrows
    public String getVerificationCode(String login) {
        QueryRunner runner = new QueryRunner();
        String usersSQL = "SELECT id FROM users where login = ?";
        String authCodesSQL = "SELECT code FROM auth_codes where user_id = ? ORDER BY created DESC LIMIT 1";
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app",
                        "app_user",
                        "password")
        ) {
            String userId = runner.query(conn, usersSQL, login, new ScalarHandler<>());
            String authCode = runner.query(conn, authCodesSQL, userId, new ScalarHandler<>());
            return authCode;
        }
    }

    public String getInvalidVerificationCode() {
        return String.valueOf((int) (Math.random() * 1000000));
    }

    public DashboardPage verify(String login, boolean success) {
        sleep(1000);
        var getVerifyFunction = success == true ? getVerificationCode(login) : getInvalidVerificationCode();
        codeField.setValue(getVerifyFunction);
        verifyButton.click();
        return new DashboardPage();
    }

    public void checkNotificationError() {
        $("div[data-test-id=\"error-notification\"]").shouldBe(Condition.visible, Duration.ofSeconds(15));
    }


}
