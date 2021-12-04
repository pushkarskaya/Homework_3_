package behaviours;

import com.consol.citrus.dsl.runner.AbstractTestBehavior;
import com.consol.citrus.message.MessageType;
import org.springframework.http.HttpStatus;
import pojo.CreateUserResponse;

public class CreateUserBehaviour extends AbstractTestBehavior {
    String name;
    String job;

    public CreateUserBehaviour(String name, String job) {
        this.name = name;
        this.job = job;

    }

    @Override
    public void apply() {
        http(httpActionBuilder -> httpActionBuilder
                .client("restClientReqres")
                .send()
                .post("users")
                .payload("{\n" +
                        "    \"name\": \"" + name + "\",\n" +
                        "    \"job\": \"" + job + "\"\n" +
                        "}"));
        http(httpActionBuilder -> httpActionBuilder
                .client("restClientReqres")
                .receive()
                .response(HttpStatus.CREATED)
                .messageType(MessageType.JSON)
                .payload(getResponseData(name, job), "objectMapper")
                .extractFromPayload("$.id", "currentId")
                .extractFromPayload("$.createdAt", "createdAt")
                .ignore("$.createdAt")
                .ignore("$.id")

        );
        echo("Behaviour DONE! Created user with currentId = ${currentId} and createdAt = ${createdAt}");
    }

    public CreateUserResponse getResponseData(String name, String job) {

        CreateUserResponse createUserResponse = new CreateUserResponse();
        createUserResponse.setName(name);
        createUserResponse.setJob(job);
        createUserResponse.setId("@isNumber()@");
        createUserResponse.setCreatedAt("unknown");

        return createUserResponse;
    }

}
