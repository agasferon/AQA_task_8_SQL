package ru.netology.web.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.request.RestRequest;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @AfterAll
    public static void CleanAllTables() {
        DataHelper.cleanDataBase();
    }

    private void shouldLogin() {
        RestRequest.loginUser(DataHelper.getAuthInfo());
        RestRequest.passVerification(DataHelper.getVerificationInfo());
        RestRequest.getCards(RestRequest.token);
    }

    @Test
    public void shouldTransactionFromTheFirstCardToSecond() {
        shouldLogin();
        int startBalanceOnFirstCard = RestRequest.cards[0].getBalance();
        int startBalanceOnSecondCard = RestRequest.cards[1].getBalance();
        int transactionAmount = 2500;

        DataHelper.TransactionInfo transactionInfo = new DataHelper.TransactionInfo();
        transactionInfo.setFrom(RestRequest.cards[0].getNumber());
        transactionInfo.setTo(RestRequest.cards[1].getNumber());
        transactionInfo.setAmount(transactionAmount);
        RestRequest.setTransaction(transactionInfo);

        int expectedBalanceOnFirstCard = startBalanceOnFirstCard - transactionAmount;
        int expectedBalanceOnSecondCard = startBalanceOnSecondCard + transactionAmount;

        assertEquals(expectedBalanceOnFirstCard, RestRequest.cards[0].getBalance());
        assertEquals(expectedBalanceOnSecondCard, RestRequest.cards[1].getBalance());
    }

    @Test
    public void shouldTransactionFromTheSecondCardToFirst() {
        shouldLogin();
        int startBalanceOnFirstCard = RestRequest.cards[0].getBalance();
        int startBalanceOnSecondCard = RestRequest.cards[1].getBalance();
        int transactionAmount = 4700;

        DataHelper.TransactionInfo transactionInfo = new DataHelper.TransactionInfo();
        transactionInfo.setFrom(RestRequest.cards[1].getNumber());
        transactionInfo.setTo(RestRequest.cards[0].getNumber());
        transactionInfo.setAmount(transactionAmount);
        RestRequest.setTransaction(transactionInfo);

        int expectedBalanceOnFirstCard = startBalanceOnFirstCard + transactionAmount;
        int expectedBalanceOnSecondCard = startBalanceOnSecondCard - transactionAmount;

        assertEquals(expectedBalanceOnFirstCard, RestRequest.cards[0].getBalance());
        assertEquals(expectedBalanceOnSecondCard, RestRequest.cards[1].getBalance());
    }

    @Test
    public void shouldNotTransactionIfAmountOverBalance() {
        shouldLogin();
        int startBalanceOnFirstCard = RestRequest.cards[0].getBalance();
        int startBalanceOnSecondCard = RestRequest.cards[1].getBalance();
        int transactionAmount = startBalanceOnFirstCard * 2;

        DataHelper.TransactionInfo transactionInfo = new DataHelper.TransactionInfo();
        transactionInfo.setFrom(RestRequest.cards[1].getNumber());
        transactionInfo.setTo(RestRequest.cards[0].getNumber());
        transactionInfo.setAmount(transactionAmount);
        RestRequest.setTransaction(transactionInfo);

        assertEquals(startBalanceOnFirstCard, RestRequest.cards[0].getBalance());
        assertEquals(startBalanceOnSecondCard, RestRequest.cards[1].getBalance());
    }
}