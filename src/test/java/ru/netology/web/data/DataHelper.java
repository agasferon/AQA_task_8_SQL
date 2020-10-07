package ru.netology.web.data;

import lombok.*;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;

public class DataHelper {
  private static String jdbcUrl = "jdbc:mysql://localhost:3306/app";
  private static String user = "app";
  private static String password = "pass";

  @Value
  public static class AuthInfo {
    private String login;
    private String password;
  }

  public static AuthInfo getAuthInfo() {
    return new AuthInfo("vasya", "qwerty123");
  }

  @Value
  public static class VerificationCode {
    private String code;
  }

  @Value
  public static class VerificationInfo {
    private String login;
    private String code;
  }

  public static VerificationInfo getVerificationInfo() {
    return new VerificationInfo(getAuthInfo().login, getLastVerificationCode());
  }

  @Data
  public final class Card {
    private String id;
    private String number;
    private int balance;
  }

  @Data
  public static class TransactionInfo {
    private String from;
    private String to;
    private int amount;
  }

  public static String getLastVerificationCode() {
    val queryAuthCodeInSQL = "SELECT code FROM auth_codes WHERE created >= DATE_SUB(NOW() , INTERVAL 1 SECOND);";
    val runner = new QueryRunner();
    try (
            val conn = DriverManager.getConnection(jdbcUrl, user, password)
    ) {
      val authCode = runner.query(conn, queryAuthCodeInSQL, new ScalarHandler<>());
      return (String) authCode;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public static String getFullCardNumberFromCardId(String cardId) {
    val queryAuthCodeInSQL = "SELECT number FROM cards WHERE id = '" + cardId + "';";
    val runner = new QueryRunner();
    try (
            val conn = DriverManager.getConnection(jdbcUrl, user, password)
    ) {
      val cardNumber = runner.query(conn, queryAuthCodeInSQL, new ScalarHandler<>());
      return (String) cardNumber;
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
    return null;
  }

  public static void cleanDataBase() {
    val runner = new QueryRunner();
    val deleteUsers = "DELETE FROM users";
    val deleteCards = "DELETE FROM cards";
    val deleteAuthCodes = "DELETE FROM auth_codes";
    val deleteCardTrans = "DELETE FROM card_transactions";
    try (
            val conn = DriverManager.getConnection(jdbcUrl, user, password)
    ) {
      runner.update(conn, deleteCardTrans);
      runner.update(conn, deleteAuthCodes);
      runner.update(conn, deleteCards);
      runner.update(conn, deleteUsers);
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    }
  }

}