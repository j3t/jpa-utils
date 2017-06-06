package com.github.j3t.jpa.utils.core;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.concurrent.TimeoutException;

/**
 * Helper class for {@link DataSource} objects.
 */
public class DataSourceHelper {

    /**
     * Waits until timeout is reached or the database is available.
     *
     * @param dataSource     {@link DataSource} to the database
     * @param timeoutSeconds Timeout in seconds
     * @throws TimeoutException if the timeout is reached
     */
    public static void waitUntilDatabaseIsAvailable(DataSource dataSource, int timeoutSeconds) throws TimeoutException {
        long start = System.currentTimeMillis();
        long timeout = timeoutSeconds * 1000;
        boolean started = false;

        while (!started) {
            Connection connection = null;
            Statement statement = null;
            try {
                connection = dataSource.getConnection();
                statement = connection.createStatement();
                started = true;
            } catch (SQLException e) {
                if (start + timeout < System.currentTimeMillis())
                    throw new TimeoutException("timeout reached!");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e1) {
                }
            } finally {
                try {
                    if (statement != null)
                        statement.close();
                    if (connection != null)
                        connection.close();
                } catch (SQLException e1) {
                }
            }
        }
    }

}
