package com.exchange.system.data.sqlite;

import com.exchange.system.model.SecurityData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseManager {
    public static final String SECURITIES_DB_URL = "jdbc:sqlite:exchange_securities.db";

    public static boolean createDatabaseAndInitData() {
        try (Connection connection = DriverManager.getConnection(SECURITIES_DB_URL);
                Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(30);
            statement.executeUpdate("DROP TABLE IF EXISTS SECURITIES_DEFINITIONS");
            statement.executeUpdate(
                    "CREATE TABLE SECURITIES_DEFINITIONS ("
                            + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                            + "security_type CHAR(32) NOT NULL,  "
                            + "symbol VARCHAR(128) NOT NULL, "
                            + "current_price REAL NOT NULL,  "
                            + "strike_price REAL NOT NULL,  "
                            + "expected_return REAL NOT NULL,  "
                            + "annualized_standard_deviation REAL NOT NULL,  "
                            + "maturity TEXT NOT NULL)"
            );
            String insertSql = "INSERT INTO SECURITIES_DEFINITIONS "
                    + "(security_type, symbol, current_price, strike_price, expected_return"
                    + ", annualized_standard_deviation, maturity) "
                    + "VALUES (?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement ps = connection.prepareStatement(insertSql)) {
                ps.setString(1, "COMMON_STOCK");
                ps.setString(2, "AAPL");
                ps.setDouble(3, 110);
                ps.setDouble(4, 110);
                ps.setDouble(5, 0.5);
                ps.setDouble(6, 0.2);
                ps.setString(7, "2028-01-01");
                ps.executeUpdate();

                ps.setString(1, "EUROPEAN_CALL_OPTION");
                ps.setString(2, "AAPL-OCT-2020-110-C");
                ps.setDouble(3, 0.0);
                ps.setDouble(4, 110);
                ps.setDouble(5, 0.5);
                ps.setDouble(6, 0.2);
                ps.setString(7, "2028-01-01");
                ps.executeUpdate();

                ps.setString(1, "EUROPEAN_PUT_OPTION");
                ps.setString(2, "AAPL-OCT-2020-110-P");
                ps.setDouble(3, 0.0);
                ps.setDouble(4, 110);
                ps.setDouble(5, 0.5);
                ps.setDouble(6, 0.2);
                ps.setString(7, "2028-01-01");
                ps.executeUpdate();

                ps.setString(1, "COMMON_STOCK");
                ps.setString(2, "TELSA");
                ps.setDouble(3, 450);
                ps.setDouble(4, 450);
                ps.setDouble(5, 0.5);
                ps.setDouble(6, 0.2);
                ps.setString(7, "2028-01-01");
                ps.executeUpdate();

                ps.setString(1, "EUROPEAN_CALL_OPTION");
                ps.setString(2, "TELSA-NOV-2020-400-C");
                ps.setDouble(3, 0.0);
                ps.setDouble(4, 450);
                ps.setDouble(5, 0.5);
                ps.setDouble(6, 0.2);
                ps.setString(7, "2028-01-01");
                ps.executeUpdate();

                ps.setString(1, "EUROPEAN_PUT_OPTION");
                ps.setString(2, "TELSA-DEC-2020-400-P");
                ps.setDouble(3, 0.0);
                ps.setDouble(4, 450);
                ps.setDouble(5, 0.5);
                ps.setDouble(6, 0.2);
                ps.setString(7, "2028-01-01");
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return false;
        }
        return true;
    }

    public static List<SecurityData> getAllSecurities() {
        List<SecurityData> securities = new ArrayList<>();
        try (Connection connection = DriverManager.getConnection(SECURITIES_DB_URL);
                Statement statement = connection.createStatement();) {
            statement.setQueryTimeout(30);
            ResultSet resultSet = statement.executeQuery(
                    "SELECT * FROM SECURITIES_DEFINITIONS");
            while (resultSet.next()) {
                SecurityData securityData = new SecurityData.Builder()
                        .productType(resultSet.getString("security_type"))
                        .symbol(resultSet.getString("symbol"))
                        .curPrice(resultSet.getDouble("current_price"))
                        .strikePrice(resultSet.getDouble("strike_price"))
                        .expectedReturn(resultSet.getDouble("expected_return"))
                        .annualizedStandardDeviation(
                                resultSet.getDouble("annualized_standard_deviation"))
                        .maturity(resultSet.getString("maturity"))
                        .build();
                securities.add(securityData);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return securities;
    }

    public static boolean updateSecurityPrice(String symbol, double price) {
        int rowsAffected = 0;
        String UPDATE_SQL = "UPDATE SECURITIES_DEFINITIONS SET current_price = ? WHERE symbol = ?";
        try (Connection connection = DriverManager.getConnection(SECURITIES_DB_URL);
                PreparedStatement statement = connection.prepareStatement(UPDATE_SQL)) {
            statement.setDouble(1, price);
            statement.setString(2, symbol);
            statement.setQueryTimeout(30);
            rowsAffected = statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return false;
        }
        return rowsAffected > 0;
    }

    public static SecurityData getSecurityBySymbol(String symbol) {
        String SELECT_SQL = "SELECT * FROM SECURITIES_DEFINITIONS WHERE symbol = ? LIMIT 1";
        try (Connection connection = DriverManager.getConnection(SECURITIES_DB_URL);
                PreparedStatement statement = connection.prepareStatement(SELECT_SQL);) {
            statement.setQueryTimeout(30);
            statement.setString(1, symbol);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                SecurityData securityData = new SecurityData.Builder()
                        .productType(resultSet.getString("security_type"))
                        .symbol(resultSet.getString("symbol"))
                        .curPrice(resultSet.getDouble("current_price"))
                        .strikePrice(resultSet.getDouble("strike_price"))
                        .expectedReturn(resultSet.getDouble("expected_return"))
                        .annualizedStandardDeviation(
                                resultSet.getDouble("annualized_standard_deviation"))
                        .maturity(resultSet.getString("maturity"))
                        .build();
                return securityData;
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return null;
    }
}
