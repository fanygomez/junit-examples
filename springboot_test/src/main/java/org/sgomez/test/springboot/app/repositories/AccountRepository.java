package org.sgomez.test.springboot.app.repositories;

import org.sgomez.test.springboot.app.models.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {
//    List<Account> findAll();
//    Account findById(Long id);
//    void update(Account account);
    @Query("FROM Account a where a.person =?1 ")
    Optional<Account> findByPerson(String person);
}
