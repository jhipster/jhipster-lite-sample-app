package tech.jhipster.lite.sample.account.application;

import tech.jhipster.lite.sample.account.domain.Account;
import tech.jhipster.lite.sample.account.domain.AccountsRepository;
import java.util.Optional;
import org.springframework.stereotype.Service;

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
