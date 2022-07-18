package com.github.hukojiayc.sme.graph.dto;

import com.github.hukojiayc.sme.graph.utils.ApplicationUtils;

public class SqlTables {

  public enum Users {
    create(""
        + "CREATE TABLE IF NOT EXISTS users (\n"
        + "    telegram_id INTEGER PRIMARY KEY,\n"
        + "    token TEXT,\n"
        + "    role TEXT,\n"
        + "    full_name TEXT,\n"
        + "    is_active INTEGER\n"
        + ");"
    ),
    add(""
        + "INSERT OR IGNORE INTO users (\n"
        + "    telegram_id,\n"
        + "    role,\n"
        + "    full_name,\n"
        + "    is_active\n"
        + ") VALUES (\n"
        + "    {},\n"
        + "    '{}',\n"
        + "    '{}',\n"
        + "    {}\n"
        + ");"
    ),
    get("SELECT * FROM users"),
    getById("SELECT * FROM users WHERE telegram_id={}"),
    getByToken("SELECT * FROM users WHERE token='{}'");

    private final String sql;

    Users(String sql) {
      this.sql = sql;
    }

    public String getSql() {
      return sql;
    }

    public String getSql(Object... args) {
      return ApplicationUtils.addArgsToValue(this.sql, args).replace("{}", "?");
    }
  }

  public enum Visits {
    create(""
        + "CREATE TABLE IF NOT EXISTS visits (\n"
        + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
        + "    date_start INTEGER,\n"
        + "    date_end INTEGER,\n"
        + "    tb TEXT,\n"
        + "    osb TEXT,\n"
        + "    directors_id TEXT,\n"
        + "    leaders_id TEXT,\n"
        + "    leaders_id_on_confirmation TEXT,\n"
        + "    creator_id INTEGER NOT NULL,\n"
        + "    creation_date INTEGER NOT NULL,\n"
        + "    FOREIGN KEY (creator_id) REFERENCES users(telegram_id)\n"
        + ");"
    ),
    get("SELECT * FROM visits"),
    add(""
        + "INSERT INTO visits (\n"
        + "    date_start,\n"
        + "    date_end,\n"
        + "    tb,\n"
        + "    osb,\n"
        + "    directors_id,\n"
        + "    leaders_id,\n"
        + "    leaders_id_on_confirmation,\n"
        + "    creator_id,\n"
        + "    creation_date\n"
        + ") VALUES (\n"
        + "    {},\n"
        + "    {},\n"
        + "    '{}',\n"
        + "    '{}',\n"
        + "    '{}',\n"
        + "    '{}',\n"
        + "    '{}',\n"
        + "    {},\n"
        + "    {}\n"
        + ");"),
    update("UPDATE visits SET {} = '{}' WHERE id = {};");

    private final String sql;

    Visits(String sql) {
      this.sql = sql;
    }

    public String getSql() {
      return sql;
    }

    public String getSql(Object... args) {
      return ApplicationUtils.addArgsToValue(this.sql, args).replace("{}", "?");
    }
  }
}
