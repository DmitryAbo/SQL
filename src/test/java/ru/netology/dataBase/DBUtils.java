package ru.netology.dataBase;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

import static com.codeborne.selenide.Selenide.sleep;

@UtilityClass
public class DBUtils {
    @SneakyThrows
    public static void cleanDB() {
        QueryRunner runner = new QueryRunner();
        String authCodeSQL = "DELETE FROM auth_codes";
        String cardTransactionsSQL = "DELETE FROM card_transactions";
        String cardsSQL = "DELETE FROM cards";
        String usersSQL = "DELETE FROM users";
        try (
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/app",
                        "app_user",
                        "password")
        ) {
            runner.execute(conn, authCodeSQL);
            runner.execute(conn, cardTransactionsSQL);
            runner.execute(conn, cardsSQL);
            runner.execute(conn, usersSQL);
        }
    }

    @SneakyThrows
    public String getVerificationCode(String login) {
        sleep(1000);
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
