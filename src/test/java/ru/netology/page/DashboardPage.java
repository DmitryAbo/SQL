package ru.netology.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.ElementsCollection;
import lombok.val;

import java.time.Duration;

import static com.codeborne.selenide.Condition.visible;
import static com.codeborne.selenide.Selenide.*;

public class DashboardPage {

    public void dashboardPageCheck() {
        $("h2[data-test-id=\"dashboard\"]").shouldBe(visible, Duration.ofSeconds(15));
    }
}
