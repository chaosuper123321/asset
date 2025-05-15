package com.exchange.system.data.db.sqllite;

import com.exchange.system.model.StockData;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DB {
    public static final String URL = "jdbc:sqlite:asset.db";

    public static boolean createDateBaseAndInitData() {
        try(Connection connection = DriverManager.getConnection(URL);
                        Statement statement = connection.createStatement()) {
            statement.setQueryTimeout(30);
            statement.executeUpdate("DROP TABLE IF EXISTS SECURITY_DEFINITIONS");
            statement.executeUpdate(
                    "CREATE TABLE SECURITY_DEFINITIONS ("
                    + "id INTEGER PRIMARY KEY AUTOINCREMENT, "
                    + "type CHAR(32) NOT NULL,  "
                    + "symbol VARCHAR(128) NOT NULL, "
                    + "cur_price REAL NOT NULL,  "
                    + "strike_price REAL NOT NULL,  "
                    + "expected_return REAL NOT NULL,  "
                    + "annualized_standard_deviation REAL NOT NULL,  "
                    + "maturity TEXT NOT NULL)"
            );
            statement.executeUpdate("INSERT INTO SECURITY_DEFINITIONS VALUES("
                    + "'1', 'COMMON_STOCK', 'AAPL', 110, 110, 0.5, 0.2, '2027-01-01')");
            statement.executeUpdate("INSERT INTO SECURITY_DEFINITIONS VALUES("
                    + "'2', 'EUROPEAN_CALL_OPTIONS', 'AAPL-OCT-2020-110-C', 5.55, 110, 0.5, 0.2, '2027-01-01')");
            statement.executeUpdate("INSERT INTO SECURITY_DEFINITIONS VALUES("
                    + "'3', 'EUROPEAN_PUT_OPTIONS', 'AAPL-OCT-2020-110-P', 0.55, 110, 0.5, 0.2, '2027-01-01')");
            statement.executeUpdate("INSERT INTO SECURITY_DEFINITIONS VALUES("
                    + "'4', 'COMMON_STOCK', 'TELSA', 450, 450, 0.5, 0.2, '2027-01-01')");
            statement.executeUpdate("INSERT INTO SECURITY_DEFINITIONS VALUES("
                    + "'5', 'EUROPEAN_CALL_OPTIONS', 'TELSA-NOV-2020-400-C', 27.25, 450, 0.5, 0.2, '2027-01-01')");
            statement.executeUpdate("INSERT INTO SECURITY_DEFINITIONS VALUES("
                    + "'6', 'EUROPEAN_PUT_OPTIONS', 'TELSA-DEC-2020-400-P', 6.35, 450, 0.5, 0.2, '2027-01-01')");
        } catch (SQLException e) {
            e.printStackTrace(System.err);
            return false;
        }
        return true;
    }

    public static List<StockData> getData() {
        List<StockData> data = new ArrayList<>();
        try(Connection connection = DriverManager.getConnection(URL);
                Statement statement = connection.createStatement();) {
            statement.setQueryTimeout(30);
            ResultSet rs = statement.executeQuery("SELECT * FROM SECURITY_DEFINITIONS");
            while (rs.next()) {
                StockData stockData = new StockData.Builder()
                        .productType(rs.getString("type"))
                        .symbol(rs.getString("symbol"))
                        .curPrice(rs.getDouble("cur_price"))
                        .strikePrice(rs.getDouble("strike_price"))
                        .expectedReturn(rs.getDouble("expected_return"))
                        .annualizedStandardDeviation(rs.getDouble("annualized_standard_deviation"))
                        .maturity(rs.getString("maturity"))
                        .build();
                data.add(stockData);
            }
        } catch (SQLException e) {
            e.printStackTrace(System.err);
        }
        return data;
    }
}
