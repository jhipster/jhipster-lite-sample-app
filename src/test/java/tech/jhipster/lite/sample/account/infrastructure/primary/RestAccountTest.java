package tech.jhipster.lite.sample.account.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;
import static tech.jhipster.lite.sample.account.domain.AccountsFixture.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import tech.jhipster.lite.sample.UnitTest;

@UnitTest
class RestAccountTest {

  private static final ObjectMapper json = new ObjectMapper();

  @Test
  void shouldSerializeToJson() throws JsonProcessingException {
    assertThat(json.writeValueAsString(RestAccount.from(account()))).isEqualTo(json());
  }

  private String json() {
    return """
    {\
    "username":"user",\
    "name":"Paul DUPOND",\
    "email":"email@company.fr",\
    "roles":["ROLE_ADMIN"]\
    }\
    """;
  }
}
