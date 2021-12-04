package tests;

import behaviours.CreateUserBehaviour;
import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.context.TestContext;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import com.consol.citrus.message.MessageType;
import com.consol.citrus.testng.CitrusParameters;
import org.springframework.http.HttpStatus;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class TestDataProviderGetUser extends TestNGCitrusTestRunner {
    private TestContext context;

    @DataProvider(name = "dataProvider")
    public Object[][] DataProvider() {
        return new Object[][] {
                { "1", "George", "Bluth" },
                { "2", "Janet", "Weaver" },
                { "3", "Emma", "Wong" },
                { "4", "Eve", "Holt" },
                { "5", "Charles", "Morris" },
                { "6", "Tracey", "Ramos" },
                { "7", "Michael", "Lawson" },
                { "8", "Lindsay", "Ferguson" },
                { "9", "Tobias", "Funke" },
                { "10", "Byron", "Fields" },
                { "11", "George", "Edwards" },
                { "12", "Rachel", "Howell" }
        };
    }

    @Test(description = "Получение данных о пользователе", dataProvider = "dataProvider")
    @Parameters({ "context" })
    @CitrusTest
    @CitrusParameters({ "id", "name", "surname" })
    public void getTextActions(String id, String name, String surname) {
        this.context = citrus.createTestContext();

        applyBehavior(new CreateUserBehaviour("Tanya", "Tester"));
        http(httpActionBuilder -> httpActionBuilder
                .client("restClientReqres")
                .send()
                .get("users/" + id));

        http(httpActionBuilder -> httpActionBuilder
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.OK)
                .messageType(MessageType.JSON)
                .validate("$.data.id", id)
                .validate("$.data.first_name", name)
                .validate("$.data.last_name", surname));

    }
}
