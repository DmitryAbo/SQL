import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.netology.dataBase.DBUtils;
import ru.netology.data.DataHelper;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class UserVerifyTest {
    public DataHelper.AuthInfo authInfoUser1 = DataHelper.getAuthInfo("vasya");
    public DataHelper.AuthInfo authInfoUser2 = DataHelper.getAuthInfo("petya");

    @BeforeEach
    public void startBrowser() {
        open("http://localhost:9999/");
    }

    @AfterAll
    public static void rollBack() {
        DBUtils.cleanDB();
    }

    @Test
    void successVerifyFirstUser() {
        var verificationPage = new LoginPage()
                .Login(authInfoUser1);
        verificationPage.verify(DBUtils.getVerificationCode(authInfoUser1.getLogin())).dashboardPageCheck();
    }

    @Test
    void failedLoginFirstUser() {
        var invalidAuthInfo = DataHelper.getInvalidAuthInfo(authInfoUser1, "login");
        var loginPage = new LoginPage();
        loginPage.Login(invalidAuthInfo);
        loginPage.checkNotificationError();
    }

    @Test
    void failedPasswordFirstUser() {
        var invalidAuthInfo = DataHelper.getInvalidAuthInfo(authInfoUser1, "password");
        var loginPage = new LoginPage();
        loginPage.Login(invalidAuthInfo);
        loginPage.checkNotificationError();
    }

    @Test
    void failedAuthCodedFirstUser() {
        var verificationPage = new LoginPage()
                .Login(authInfoUser1);
        verificationPage.verify(DataHelper.getInvalidVerificationCode());
        verificationPage.checkNotificationError();
    }

    @Test
    void failedPasswordSecondUserBlock() {
        new LoginPage().Login(authInfoUser2).verify(DBUtils.getVerificationCode(authInfoUser2.getLogin())).dashboardPageCheck();
        open("http://localhost:9999/");
        new LoginPage().Login(authInfoUser2).verify(DBUtils.getVerificationCode(authInfoUser2.getLogin())).dashboardPageCheck();
        open("http://localhost:9999/");
        new LoginPage().Login(authInfoUser2).verify(DBUtils.getVerificationCode(authInfoUser2.getLogin())).dashboardPageCheck();
        open("http://localhost:9999/");
        var verificationPage = new LoginPage()
                .Login(authInfoUser2);
        verificationPage.verify(DBUtils.getVerificationCode(authInfoUser2.getLogin()));
        verificationPage.checkNotificationError();
        new LoginPage().checkLoginPage();
    }
}
