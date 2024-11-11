package tech.jhipster.lite.sample.account.infrastructure.primary;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import tech.jhipster.lite.sample.account.application.AccountsApplicationService;

@RestController
@Tag(name = "Accounts")
@RequestMapping("/api")
class AccountsResource {

  private final AccountsApplicationService accounts;

  public AccountsResource(AccountsApplicationService accounts) {
    this.accounts = accounts;
  }

  @GetMapping("authenticated-user-account")
  @Operation(summary = "Get authenticated user account")
  @ApiResponse(responseCode = "200", description = "Account for the current user")
  @ApiResponse(responseCode = "401", description = "The user is not authenticated")
  ResponseEntity<RestAccount> getAuthenticatedUserAccount() {
    return accounts
      .authenticatedUserAccount()
      .map(RestAccount::from)
      .map(ResponseEntity::ok)
      .orElseGet(() -> new ResponseEntity<>(HttpStatus.UNAUTHORIZED));
  }
}
