package biz;


import db.dao.DAO;
import junit.framework.TestCase;
import model.Account;
import model.Role;
import model.User;
import model.exceptions.OperationIsNotAllowedException;
import model.exceptions.UserUnnkownOrBadPasswordException;
import model.operations.OperationType;
import model.operations.PaymentIn;
import model.operations.Withdraw;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.SQLException;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class AccountManagerTest extends TestCase {
    @InjectMocks
    @Spy
    Account account = new Account();

    @Mock
    User user = new User();

    Role role = new Role();

    DAO dao = mock(DAO.class);

    @InjectMocks
    @Spy
    BankHistory bankHistory;

    @InjectMocks
    @Spy
    AuthenticationManager authenticationManager;

    @InjectMocks
    @Spy
    InterestOperator interestOperator;

    @InjectMocks
    @Spy
    AccountManager accountManager;

    double startAmount;


    double amountInn;
    double withdrawAmount;

    String description;

    @Before
    public void setUp() throws Exception {
        startAmount = 0;
         amountInn = 350;
         withdrawAmount = 400;
        description = "testDescription";
    }

    @Test
    public void paymentIn() throws SQLException {
        account.setAmmount(startAmount + 2003);
        account.setOwner(user);
        when(dao.findAccountById(account.getId())).thenReturn(account);
        when(dao.updateAccountState(account)).thenReturn(true);

        accountManager.paymentIn(user, amountInn, description, account.getId());


        //then
        assertEquals(account.getAmmount(), amountInn + startAmount + 2003);
        assertEquals(account.getOwner(), user);

        then(dao).should().updateAccountState((account));
        then(bankHistory).should().logOperation(any(PaymentIn.class), eq(true));

    }
    // mozemy zwrocic tylko wynik stanu operacji, ciężko wykonać test dla nulla
    @Test
    public void paymentIn_nullAccount() throws SQLException {
        account.setAmmount(startAmount + 2003);
        account.setOwner(user);
        double accountId = 2;
        when(dao.findAccountById(account.getId())).thenReturn(null);
        boolean wynik = accountManager.paymentIn(user, amountInn, "null", 0);
        //then
        assertFalse(wynik);
        then(bankHistory).shouldHaveNoMoreInteractions();
//        when(accountManager.paymentIn(user, amountInn, "null", 0)).thenReturn(dao.updateAccountState(account));
    }



    @Test
    public void paymentOut() throws SQLException, OperationIsNotAllowedException {
        User user2 = new User();
        Account account2 = new Account();
        Role role = new Role();
        account2.setOwner(user2);
        role.setName("Admin");
        user2.setRole(role);
        account2.setAmmount(startAmount + 300);

        when(dao.findAccountById(account2.getId())).thenReturn(account2);
        when(dao.updateAccountState(account2)).thenReturn(true);

        System.out.println(account2.getAmmount());

//        accountManager.paymentOut(user, amountInn, description, account.getId());

        boolean wynik = accountManager.paymentOut(user2, 200, "", account2.getId());
        System.out.println(account2.getAmmount());

        //then
        assertTrue(wynik);
        assertEquals(100.0,account2.getAmmount());
        then(bankHistory).should().logOperation(any(Withdraw.class), eq(true));


    }

    @Test
    // BUG: Wartość zmiennej success zostanie nadpisana i zwórcony będzie tylko stan operacji :31
    public void testPaymentIn_correctAmount() throws SQLException {
        // given
        double initialAmount = 123.45;
        double amountIn = 1000.56;
        User user = new User();
        Account account = new Account();
        account.setAmmount(initialAmount);
        account.setOwner(user);
        String description = "My first payment in";
        given(dao.findAccountById(account.getId())).willReturn(account);
        given(dao.updateAccountState(account)).willReturn(true);

        // when
        boolean result = accountManager.paymentIn(user, amountIn, description, account.getId());
        System.out.println(account.getAmmount());

        // then
        assertEquals(account.getAmmount(), initialAmount + amountIn);
        assertTrue(result);
        verify(bankHistory).logOperation(isA(PaymentIn.class), eq(true));
    }



    @Test
    public void internalPayment() {
    }

    @Test
    public void buildBank() {
    }

    @Test
    public void logIn() {
    }

    @Test
    public void logOut() {
    }

    @Test
    public void getLoggedUser() {
    }
}