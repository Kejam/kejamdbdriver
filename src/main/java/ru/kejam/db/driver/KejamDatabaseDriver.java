package ru.kejam.db.driver;

import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import java.io.IOException;
import java.sql.*;
import java.util.Properties;
import java.util.logging.Logger;

import static ru.kejam.db.driver.util.DriverURLUtils.parseURL;
import static ru.kejam.db.driver.util.DriverURLUtils.returnClearURL;

@Slf4j
public class KejamDatabaseDriver implements Driver {
    private final OkHttpClient client = new OkHttpClient();
    private final String initURLPoint = "initdb";
    private static final String version = "1.1";
    private static String url;

    public static String getVersion() {
        return version;
    }

    public static String getUrl() {
        if (url == null) {
            log.error("Please init driver");
            return "";
        }
        return url;
    }

    static {
        try {
            register();
        } catch (SQLException e) {
            throw new ExceptionInInitializerError(e);
        }
    }

    @Override
    public Connection connect(String url, Properties info) throws SQLException {
        log.info("Try to init connection with kejam db");
        if (url == null) throw new SQLException("URL is null");
        if (!parseURL(url)) throw new SQLException("URL is wrong!");
        final String clearURL = returnClearURL(url);
        KejamDatabaseDriver.url = clearURL;

        final Request initRequest = new Request.Builder()
                .url(clearURL + initURLPoint)
                .build();

        final Response response;
        final String body;
        try {
             response = client.newCall(initRequest).execute();
             body = response.body().string();
        } catch (IOException e) {
            log.error("Error make init request to db {} ", url, e);
            throw new SQLException("Error make init request to db " + url);
        }

        if (!response.isSuccessful()) {
            log.error("Response is not successful");
            throw new SQLException("Response is not successful " + url);
        } else {
            log.info("Response is  {}", body);
        }

        return new KejamConnection();
    }

    @Override
    public boolean acceptsURL(String url) throws SQLException {
        return parseURL(url);
    }

    @Override
    public DriverPropertyInfo[] getPropertyInfo(String url, Properties info) throws SQLException {
        return new DriverPropertyInfo[0];
    }

    @Override
    public int getMajorVersion() {
        return 1;
    }

    @Override
    public int getMinorVersion() {
        return 1;
    }

    @Override
    public boolean jdbcCompliant() {
        return false;
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return Logger.getLogger("ru.kejam.driver");
    }

    public static void register() throws SQLException {
        log.info("Try to register kejamdbsql driver to DriverManager");
        DriverManager.registerDriver(new KejamDatabaseDriver());
    }


}
