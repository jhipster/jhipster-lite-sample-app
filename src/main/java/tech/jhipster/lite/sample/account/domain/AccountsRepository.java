package tech.jhipster.lite.sample.account.domain;

import java.util.Optional;

public interface AccountsRepository {
  Optional<Account> authenticatedUserAccount();
}
