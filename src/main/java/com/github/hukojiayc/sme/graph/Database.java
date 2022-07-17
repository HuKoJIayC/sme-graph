package com.github.hukojiayc.sme.graph;

import com.github.hukojiayc.sme.graph.dto.DatabaseConstant;
import com.github.hukojiayc.sme.graph.dto.DatabaseSqlType;
import com.github.hukojiayc.sme.graph.utils.ApplicationUtils;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.sqlite.jdbc4.JDBC4ResultSet;

@Slf4j
public class Database {

  private static Database databaseService = null;

  private final String homePath;
  private final String connectionString;

  private Date currentDate = null;

  public Database() {
    log.info("Initialization database");
    log.debug("Getting jdbc driver");
    try {
      Class.forName(DatabaseConstant.DRIVER_NAME);
    } catch (ClassNotFoundException e) {
      log.error("Driver sqlite not found", e);
      throw new RuntimeException(e);
    }
    homePath = ApplicationUtils.getHomePath(this.getClass()).orElseThrow();
    connectionString = "jdbc:sqlite:" + homePath + DatabaseConstant.DATABASE_NAME;
    log.debug("Creating test connection");
    Optional<Connection> connection = openConnection();
    if (connection.isEmpty()) {
      throw new RuntimeException();
    }
    log.debug("Creating tables");
    execute(DatabaseSqlType.usersCreateTable.getSql());
    execute(DatabaseSqlType.visitsCreateTable.getSql());
    log.debug("Closing test connection");
    if (!closeConnection(connection.get())) {
      throw new RuntimeException();
    }
  }

  public static synchronized Database getInstance() {
    if (databaseService == null) {
      databaseService = new Database();
    }
    return databaseService;
  }

  private Optional<Connection> openConnection() {
    try {
      return Optional.of(DriverManager.getConnection(connectionString));
    } catch (SQLException e) {
      log.error("Incorrect url {}", connectionString, e);
    }
    return Optional.empty();
  }

  private boolean closeConnection(Connection connection) {
    try {
      connection.close();
      return true;
    } catch (SQLException e) {
      log.error("Failed connection closure", e);
    }
    return false;
  }

  public synchronized boolean execute(String... sql) {
    if (currentDate == null || currentDate.getTime() + 1000 * 60 * 60 * 12 < new Date().getTime()) {
      createBackup();
      currentDate = new Date();
    }
    if (sql.length == 0) {
      log.error("SQL is empty");
      return false;
    }
    Optional<Connection> connection = openConnection();
    if (connection.isEmpty()) {
      return false;
    }
    try (Statement statement = connection.get().createStatement()) {
      for (String s : sql) {
        if (s == null || s.length() == 0) {
          continue;
        }
        statement.execute(s);
      }
    } catch (SQLException e) {
      log.error("Error execute sql {}", sql, e);
      return false;
    }
    return closeConnection(connection.get());
  }

  public List<Map<String, Object>> select(String sql) {
    List<Map<String, Object>> list = new ArrayList<>();
    if (sql == null || !sql.toUpperCase().contains("SELECT")) {
      log.error("Incorrect SQL");
      return list;
    }
    Optional<Connection> connection = openConnection();
    if (connection.isEmpty()) {
      return list;
    }
    try (Statement statement = connection.get().createStatement();
        ResultSet resultSet = statement.executeQuery(sql)) {
      while (resultSet.next()) {
        if (!(resultSet instanceof JDBC4ResultSet)) {
          continue;
        }
        JDBC4ResultSet jdbc4ResultSet = (JDBC4ResultSet) resultSet;
        Map<String, Object> map = new LinkedHashMap<>();
        for (int i = 1; i < jdbc4ResultSet.getColumnCount() + 1; i++) {
          String columnType = jdbc4ResultSet.getColumnTypeName(i);
          String columnName = jdbc4ResultSet.getColumnName(i);
          switch (columnType) {
            case "BOOLEAN":
              map.put(columnName, jdbc4ResultSet.getBoolean(i));
              break;
            case "INTEGER":
              map.put(columnName, jdbc4ResultSet.getLong(i));
              break;
            case "REAL":
            case "FLOAT":
              map.put(columnName, jdbc4ResultSet.getFloat(i));
              break;
            case "BLOB":
              map.put(columnName, jdbc4ResultSet.getBlob(i));
              break;
            case "TEXT":
            default:
              map.put(columnName, jdbc4ResultSet.getString(i));
          }
        }
        list.add(map);
      }
      closeConnection(connection.get());
      return list;
    } catch (SQLException e) {
      log.error("Error selected", e);
      return list;
    }
  }

  private void createBackup() {
    log.info("Creating database backup");
    File backupDir = new File(homePath + "backup/");
    if (!backupDir.exists() && !backupDir.mkdir()) {
      return;
    }
    File dbFile = new File(homePath + DatabaseConstant.DATABASE_NAME);
    if (dbFile.exists()) {
      try {
        Files.copy(
            dbFile.toPath(),
            Paths.get(backupDir + "/data_" + new Date().getTime() + ".db"),
            StandardCopyOption.REPLACE_EXISTING
        );
      } catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
