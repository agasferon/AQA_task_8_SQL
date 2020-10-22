package ru.netology.web.test;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import ru.netology.web.data.DataHelper;
import ru.netology.web.request.RestRequest;

import static org.junit.jupiter.api.Assertions.*;

class TransactionTest {

    @AfterAll
    public static void cleanAllTables() {
        DataHelper.cleanDataBase();
    }

    @Test
    public void shouldTransactionFromTheFirstCardToSecond() {
        RestRequest.loginUser(DataHelper.getAuthInfo());
        String token = RestRequest.passVerification(DataHelper.getVerificationInfo());
        DataHelper.Card[] cards = RestRequest.getCards(token);

        int startBalanceOnFirstCard = cards[0].getBalance();
        int startBalanceOnSecondCard = cards[1].getBalance();
        int transactionAmount = 2500;

        DataHelper.TransactionInfo transactionInfo = new DataHelper.TransactionInfo();
        transactionInfo.setFrom(cards[0].getNumber());
        transactionInfo.setTo(cards[1].getNumber());
        transactionInfo.setAmount(transactionAmount);
        RestRequest.setTransaction(transactionInfo);

        int expectedBalanceOnFirstCard = startBalanceOnFirstCard - transactionAmount;
        int expectedBalanceOnSecondCard = startBalanceOnSecondCard + transactionAmount;

        DataHelper.Card[] cardsAfterTransaction = RestRequest.getCards(token);
        assertEquals(expectedBalanceOnFirstCard, cardsAfterTransaction[0].getBalance());
        assertEquals(expectedBalanceOnSecondCard, cardsAfterTransaction[1].getBalance());
    }

    @Test
    public void shouldTransactionFromTheSecondCardToFirst() {
        RestRequest.loginUser(DataHelper.getAuthInfo());
        String token = RestRequest.passVerification(DataHelper.getVerificationInfo());
        DataHelper.Card[] cards = RestRequest.getCards(token);

        int startBalanceOnFirstCard = cards[0].getBalance();
        int startBalanceOnSecondCard = cards[1].getBalance();
        int transactionAmount = 4700;

        DataHelper.TransactionInfo transactionInfo = new DataHelper.TransactionInfo();
        transactionInfo.setFrom(cards[1].getNumber());
        transactionInfo.setTo(cards[0].getNumber());
        transactionInfo.setAmount(transactionAmount);
        RestRequest.setTransaction(transactionInfo);

        int expectedBalanceOnFirstCard = startBalanceOnFirstCard + transactionAmount;
        int expectedBalanceOnSecondCard = startBalanceOnSecondCard - transactionAmount;

        DataHelper.Card[] cardsAfterTransaction = RestRequest.getCards(token);
        assertEquals(expectedBalanceOnFirstCard, cardsAfterTransaction[0].getBalance());
        assertEquals(expectedBalanceOnSecondCard, cardsAfterTransaction[1].getBalance());
    }

    @Test
    public void shouldNotTransactionIfAmountOverBalance() {
        RestRequest.loginUser(DataHelper.getAuthInfo());
        String token = RestRequest.passVerification(DataHelper.getVerificationInfo());
        DataHelper.Card[] cards = RestRequest.getCards(token);

        int startBalanceOnFirstCard = cards[0].getBalance();
        int startBalanceOnSecondCard = cards[1].getBalance();
        int transactionAmount = startBalanceOnFirstCard * 2;

        DataHelper.TransactionInfo transactionInfo = new DataHelper.TransactionInfo();
        transactionInfo.setFrom(cards[1].getNumber());
        transactionInfo.setTo(cards[0].getNumber());
        transactionInfo.setAmount(transactionAmount);
        RestRequest.setTransaction(transactionInfo);

        DataHelper.Card[] cardsAfterTransaction = RestRequest.getCards(token);
        assertEquals(startBalanceOnFirstCard, cardsAfterTransaction[0].getBalance());
        assertEquals(startBalanceOnSecondCard, cardsAfterTransaction[1].getBalance());
    }

}