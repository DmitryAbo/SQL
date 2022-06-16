package ru.netology.Cleaner;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.Connection;
import java.sql.DriverManager;

@UtilityClass
public class DBCleaner {
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
}
