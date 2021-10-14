package cucumbre.biz;

import db.dao.DAO;
import io.cucumber.datatable.DataTable;
import io.cucumber.java.Before;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import model.Account;
import model.Operation;
import model.User;
import model.exceptions.OperationIsNotAllowedException;
import model.operations.Payment;
import model.operations.PaymentIn;
import model.operations.Withdraw;
import org.mockito.Mock;

import javax.jws.soap.SOAPBinding;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

public class AccountManagerTest {

    @Mock
    DAO dao;
    @Mock
    AuthenticationManager auth;
    @Mock
    BankHistory history;

    AccountManager testObject;

    Map<String, User> users = new HashMap<String,User>();
    boolean result;
    Exception thrownException;

    @Before
    public void initTestScenario(){
        dao = mock(DAO.class);
        auth = mock(AuthenticationManager.class);
        history = mock(BankHistory.class);
        testObject = new AccountManager();
        testObject.dao = dao;
        testObject.auth = auth;
        testObject.history=history;
    }

    @Given("We have user with name {string}")
    public void crateUser(String userName){
        User u = new User();
        u.setName(userName);
        users.put(userName,u);

    }

    @Given("We have account number: {int} with amount: {double}")
    public Account addAccountToDao(int accountId, double amount) throws SQLException {
        Account a = new Account();
        a.setId(accountId);
        a.setAmmount(amount);
        when(dao.findAccountById(accountId)).thenReturn(a);
        return a;
    }

    @Given("All database operations are sucessfull")
    public void setSuccesToAllOnDAO() throws SQLException {
        when(dao.updateAccountState(any(Account.class))).thenReturn(true);
    }

    @Given("All {string} operations are allowed")
    public void allowAllOperationsToUser(String username){
        User u = users.get(username);
        when(auth.canInvokeOperation(any(Operation.class), eq(u))).thenReturn(true);
    }

    @Given("{string} has account number: {int} with amount: {double}")
    public void addAccountToDao(String userName, int accountId, double amount) throws SQLException {
        User user = users.get(userName);
        Account a = addAccountToDao(accountId, amount);
        a.setOwner(user);

    }

    @When("{string} make internal payment from acc: {int} to acc: {int} amount {double}")
    public void makeInternalPayment(String userName, int srcAccID, int dstAccID, double ammount) throws OperationIsNotAllowedException, SQLException {
        User user = users.get(userName);
        try {
            result = testObject.internalPayment(user, ammount, "description", srcAccID, dstAccID);
        }
        catch (Exception e){
            thrownException = e;
        }
    }

    @Then("Expected Exception {string}")
    public void obtainWrongAccounID(String exceptionName) throws ClassNotFoundException {
        Class exceptionClass = this.getClass().getClassLoader().loadClass(exceptionName);
        assertThrows(exceptionClass,() -> {throw thrownException;});
    }

    @Then("Operation is successful")
    public void chceckFinalSucces(){
        assertTrue(result);
    }

    @Then("Operation is unsuccessful")
    public void chceckFinalFailure(){
        assertFalse(result);
    }

    @Then("Account {int} have {double}")
    public void checkAccountAmount(int accountId, double amount) throws SQLException {
        Account a = dao.findAccountById(accountId);
        assertEquals(amount, a.getAmmount(), 0.01);
    }

    @Then("Expect updates only on proper accounts")
    public void checkUpdatesOnAccounts(DataTable dt) throws SQLException {
        List<String> accs =dt.asList();
        for (String elem : accs){
            int id = Integer.parseInt(elem);
            Account a = dao.findAccountById(id);
            //On update per account !!
            verify(dao,times(1)).updateAccountState(a);
        }
        //No additional operations on accounts!!
        verify(dao,times(accs.size())).updateAccountState(any(Account.class));
    }

    @Then("No updates on accounts")
    public void checkNoUpdatesOnAccounts() throws SQLException {
        verify(dao,never()).updateAccountState(any(Account.class));
    }

    @Then("Withdraw was logged properly")
    public void checkLoggedWithdraw() throws SQLException {
        verify(history,times(1)).logOperation(any(Withdraw.class),eq(result));
    }

    @Then("Payment was logged properly")
    public void checkLoggedPayment() throws SQLException {
        verify(history,times(1)).logOperation(any(PaymentIn.class),eq(result));
    }
}
