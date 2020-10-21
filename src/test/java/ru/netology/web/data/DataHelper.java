package ru.netology.web.data;

import com.github.javafaker.Faker;
import lombok.Value;
import lombok.val;
import org.apache.commons.dbutils.QueryRunner;
import org.apache.commons.dbutils.handlers.ScalarHandler;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Locale;

public class DataHelper {
  private static String jdbcUrl = "jdbc:mysql://localhost:3306/app";
  private static String user = "app";
  private static String password = "pass";

  private DataHelper() {}

  @Value
  public static class AuthInfo {
    private String login;
    private String password;
  }

  public static AuthInfo getAuthInfo() {
    return new AuthInfo("vasya", "qwerty123");
  }

  public static AuthInfo getInvalidLogin() {
    Faker faker = new Faker(new Locale("EN"));
    String invalidLogin = faker.name().username();
    return new AuthInfo(invalidLogin, "qwerty123");
  }

  public static AuthInfo getInvalidPassword() {
    Faker faker = new Faker(new Locale("EN"));
    String invalidPassword = faker.internet().password(8,10,false,false,true);
    return new AuthInfo("vasya", invalidPassword);
  }

  @Value
  public static class VerificationCode {
    private String code;
  }

  public static String getLastVerificationCode() {
    val queryAuthCodeInSQL = "SELECT code FROM auth_codes WHERE created >= DATE_SUB(NOW() , INTERVAL 5 SECOND);";
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

  public static String getInvalidVerificationCode() {
    return String.valueOf(1234);
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