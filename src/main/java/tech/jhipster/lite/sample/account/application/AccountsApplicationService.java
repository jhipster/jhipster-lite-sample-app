package tech.jhipster.lite.sample.account.application;

import java.util.Optional;
import org.springframework.stereotype.Service;
import tech.jhipster.lite.sample.account.domain.Account;
import tech.jhipster.lite.sample.account.domain.AccountsRepository;

@Service
public class AccountsApplicationService {

  private final AccountsRepository accounts;

  public AccountsApplicationService(AccountsRepository accounts) {
    this.accounts = accounts;
  }

  public Optional<Account> authenticatedUserAccount() {
    return accounts.authenticatedUserAccount();
  }
}
