package com.instacart.shopper.db;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import lombok.extern.slf4j.Slf4j;

import org.apache.commons.dbcp2.BasicDataSource;
import org.apache.commons.dbcp2.DataSourceConnectionFactory;
import org.apache.commons.dbcp2.PoolableConnection;
import org.apache.commons.dbcp2.PoolableConnectionFactory;
import org.apache.commons.dbcp2.PoolingDataSource;
import org.apache.commons.pool2.impl.GenericObjectPool;

/**
 * A singleton {@link DataSource} for managing connections to the underlying SQLite data source. It
 * uses a connection pool to manage connections.
 *
 * @author arun
 */
@Slf4j
public enum SQLiteDataSource {
    INSTANCE;

    private static final String DB_LOCATION = "db/development.sqlite3";

    private DataSource dataSource;

    private SQLiteDataSource() {
        BasicDataSource source = new BasicDataSource();
        source.setDriverClassName("org.sqlite.JDBC");
        source.setUrl("jdbc:sqlite:" + DB_LOCATION);

        DataSourceConnectionFactory connFactory = new DataSourceConnectionFactory(source);
        PoolableConnectionFactory poolableConnFactory = new PoolableConnectionFactory(connFactory, null);
        GenericObjectPool<PoolableConnection> connPool = new GenericObjectPool<>(poolableConnFactory);
        poolableConnFactory.setPool(connPool);

        dataSource = new PoolingDataSource<>(connPool);
    }

    public Connection getConnection() throws SQLException {
        log.debug("Vending out a database connection");

        return dataSource.getConnection();
    }
}
