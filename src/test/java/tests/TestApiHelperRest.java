package tests;

import com.consol.citrus.annotations.CitrusTest;
import com.consol.citrus.dsl.testng.TestNGCitrusTestRunner;
import org.springframework.test.context.TestContext;
import org.testng.annotations.Test;

public class TestApiHelperRest extends TestNGCitrusTestRunner {
    private TestContext context;

    @Test
    @CitrusTest
    public void GetTextActions(){

        http(httpActionBuilder-> httpActionBuilder
                .client("homeworkRestClient")
                .send()
                .get("users/23")
        );

        http(httpActionBuilder->httpActionBuilder
                .client("homeworkRestClient")
                .receive()
                .response()
                .payload("{}")
        );

    }
}
