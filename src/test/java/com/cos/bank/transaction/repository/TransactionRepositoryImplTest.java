package com.cos.bank.transaction.repository;

import com.cos.bank.account.domain.Account;
import com.cos.bank.account.repository.AccountRepository;
import com.cos.bank.config.dummy.DummyObject;
import com.cos.bank.transaction.domain.Transaction;
import com.cos.bank.user.domain.User;
import com.cos.bank.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@DataJpaTest
class TransactionRepositoryImplTest extends DummyObject {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    EntityManager entityManager;

    @BeforeEach
    public void setUp() {
        autoincrementReset();
        dataSetting();
        entityManager.clear();
    }

    @Test
    void test() {
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.forEach((transaction -> {
            System.out.println(transaction.getId());
            System.out.println(transaction.getTransactionType());
            System.out.println("----------------");
        }));
    }

    @Test
    void test_2() {
        List<Transaction> transactions = transactionRepository.findAll();
        transactions.forEach((transaction -> {
            System.out.println(transaction.getId());
            System.out.println(transaction.getTransactionType());
            System.out.println("----------------");
        }));
    }

    @Test
    public void findTransactionList_all_test() throws Exception {
        // given
        Long accountId = 1L;

        // when
        List<Transaction> transactionList = transactionRepository.findTransactionList(accountId, "ALL",
                0);
        transactionList.forEach((t) -> {
            System.out.println("test check- id: " + t.getId());
            System.out.println("test check- type: " + t.getTransactionType());
            System.out.println("test check- amount: " + t.getAmount());
            System.out.println("test check- sender: " + t.getSender());
            System.out.println("test check- receiver: " + t.getReceiver());
            System.out.println("test check- d account balance: " + t.getDepositAccountBalance());
            System.out.println("test check- w account balance: " + t.getWithdrawAccountBalance());
            System.out.println("----------------");
        });

        // then

    }

    private void dataSetting() {
        User ssar = userRepository.save(newUser("ssar", "first", "last"));
        User cos = userRepository.save(newUser("cos", "first", "last"));
        User love = userRepository.save(newUser("love", "first", "last"));
        User admin = userRepository.save(newUser("admin", "first", "last"));

        Account ssarAccount1 = accountRepository.save(newAccount(1111111111L, ssar));
        Account cosAccount = accountRepository.save(newAccount(222222222L, cos));
        Account loveAccount = accountRepository.save(newAccount(3333333333L, love));
        Account ssarAccount2 = accountRepository.save(newAccount(4444444444L, ssar));

        Transaction withdrawTransaction1 = transactionRepository
                .save(newWithdrawTransaction(ssarAccount1, accountRepository));
        Transaction depositTransaction1 = transactionRepository
                .save(newDepositTransaction(cosAccount, accountRepository));
        Transaction transferTransaction1 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, cosAccount, accountRepository));
        Transaction transferTransaction2 = transactionRepository
                .save(newTransferTransaction(ssarAccount1, loveAccount, accountRepository));
        Transaction transferTransaction3 = transactionRepository
                .save(newTransferTransaction(cosAccount, ssarAccount1, accountRepository));
    }


    private void autoincrementReset() {
        entityManager.createNativeQuery("ALTER TABLE users ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE account ALTER COLUMN id RESTART WITH 1").executeUpdate();
        entityManager.createNativeQuery("ALTER TABLE transaction ALTER COLUMN id RESTART WITH 1").executeUpdate();
    }
}
