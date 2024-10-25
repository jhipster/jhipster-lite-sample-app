package tech.jhipster.lite.sample.account.infrastructure.primary;

import static org.assertj.core.api.Assertions.*;

import tech.jhipster.lite.sample.UnitTest;
import tech.jhipster.lite.sample.account.application.AccountsApplicationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;

@UnitTest
@ExtendWith(MockitoExtension.class)
class AccountsResourceTest {

  @Mock
  private AccountsApplicationService applicationService;

  @InjectMocks
  private AccountsResource accounts;

  @Test
  void shouldBeUnauthorizedForUnknownAccount() {
    assertThat(accounts.getAuthenticatedUserAccount().getStatusCode()).isEqualTo(HttpStatus.UNAUTHORIZED);
  }
}
