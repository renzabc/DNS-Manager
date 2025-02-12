package com.dnsmanager.dnsmanager.configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;

@Configuration
public class HostConfiguration {

    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Value("${spring.datasource.username}")
    private String username;

    @Value("${spring.datasource.password}")
    private String password;

    @Value("${app.database.name}")
    private String dbName;

    @Value("${app.database.user}")
    private String dbUser;

    @Value("${app.database.user.password}")
    private String dbUserPassword;

    // @PostConstruct
    @EventListener(ContextRefreshedEvent.class)
    public void initialize() {
        createDatabase();
    }

    private void createDatabase() {
        try (Connection connection = DriverManager.getConnection(databaseUrl, username, password); Statement statement = connection.createStatement()) {

            // Create database
            String createDatabaseSql = "CREATE DATABASE " + dbName;
            statement.executeUpdate(createDatabaseSql);

            // Create user
            String createUserSql = "CREATE USER " + dbUser + " WITH PASSWORD '" + dbUserPassword + "'";
            statement.executeUpdate(createUserSql);

            // Grant privileges
            String grantPrivilegesSql = "GRANT ALL PRIVILEGES ON DATABASE " + dbName + " TO " + dbUser;
            statement.executeUpdate(grantPrivilegesSql);

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
