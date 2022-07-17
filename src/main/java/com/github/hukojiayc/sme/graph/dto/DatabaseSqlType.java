package com.github.hukojiayc.sme.graph.dto;

import com.github.hukojiayc.sme.graph.utils.ApplicationUtils;

public enum DatabaseSqlType {
  usersCreateTable(""
      + "CREATE TABLE IF NOT EXISTS users (\n"
      + "    telegram_id INTEGER PRIMARY KEY,\n"
      + "    token TEXT,\n"
      + "    role TEXT,\n"
      + "    full_name TEXT,\n"
      + "    is_active INTEGER\n"
      + ");"
  ),
  usersAdd(""
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
  usersGet("SELECT * FROM users"),
  usersGetById("SELECT * FROM users WHERE telegram_id={}"),
  usersGetByToken("SELECT * FROM users WHERE token={}"),
  visitsCreateTable(""
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
  visitsGet("SELECT * FROM visits"),
  visitsAdd(""
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




  /*

  private Date dateStart;
  private Date dateEnd;
  private TbType tb;
  private OsbType osb;
  private List<User> directors;
  private List<User> leaders;
  private List<User> leadersOnConfirmation;
  private User creator;
   */






  addUser(""
      + "INSERT OR IGNORE INTO employees (\n"
      + "    telegram_id,\n"
      + "    last_name,\n"
      + "    first_name\n"
      + ") VALUES (\n"
      + "    {},\n"
      + "    '{}',\n"
      + "    '{}'\n"
      + ");"
  ),
  addEmployee(""
      + "INSERT OR IGNORE INTO employees (\n"
      + "    telegram_id,\n"
      + "    employee_id,\n"
      + "    last_name,\n"
      + "    first_name,\n"
      + "    birthday\n"
      + ") VALUES (\n"
      + "    {},\n"
      + "    {},\n"
      + "    '{}',\n"
      + "    '{}',\n"
      + "    '{}'\n"
      + ");"
  ),
  updateEmployee("UPDATE employees SET {} = {} WHERE telegram_id = {};"),
  getEmployeeById("SELECT * FROM employees WHERE telegram_id={}"),
  getEmployeesCount("SELECT count() FROM employees"),
  getEmployeesWithIdCount("SELECT count() FROM employees WHERE employee_id NOT NULL;"),
  getEmployeesByIsActive("SELECT * FROM employees WHERE employee_id NOT NULL AND is_active = {};"),
  getEmployeesForSync(""
      + "SELECT\n"
      + "    telegram_id,\n"
      + "    employee_id,\n"
      + "    last_name,\n"
      + "    first_name\n"
      + "FROM employees WHERE employee_id NOT NULL"
  ),
  createTableMessages(""
      + "CREATE TABLE IF NOT EXISTS messages (\n"
      + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
      + "    chat_id INTEGER NOT NULL,\n"
      + "    user_id INTEGER NOT NULL,\n"
      + "    message_id INTEGER NOT NULL,\n"
      + "    text TEXT,\n"
      + "    source TEXT,\n"
      + "    date INTEGER,\n"
      + "    FOREIGN KEY (user_id) REFERENCES employees(telegram_id)\n"
      + ");"
  ),
  addMessage(""
      + "INSERT INTO messages (\n"
      + "    chat_id,\n"
      + "    user_id,\n"
      + "    message_id,\n"
      + "    text,\n"
      + "    source,\n"
      + "    date\n"
      + ") VALUES (\n"
      + "    {},\n"
      + "    {},\n"
      + "    {},\n"
      + "    '{}',\n"
      + "    '{}',\n"
      + "    {}\n"
      + ");"),
  getMessage("SELECT * FROM messages WHERE chat_id={} AND user_id = {} AND message_id = {}"),
  getMessageById("SELECT * FROM messages WHERE id = {}"),
  getMessagesMetricCount("" // todo name
      + "SELECT text AS 'Кнопка', count() AS 'Количество переходов' FROM messages \n"
      + "WHERE instr(text, '_')  > 0 AND chat_id > 0 \n"
      + "GROUP BY text;"),
  getMessagesMetricCountBetween(""
      + "SELECT text AS 'Кнопка', count() AS 'Количество переходов' FROM messages \n"
      + "WHERE instr(text, '_')  > 0 AND chat_id > 0 AND \n"
      + "date BETWEEN {} AND {} \n"
      + "GROUP BY text;"),
  getMessagesMetricDate("" // todo name
      + "SELECT text, date FROM messages\n"
      + "WHERE instr(text, '_')  > 0 AND chat_id > 0;"),
  @Deprecated
  createTableQuestionsDeprecated(""
      + "CREATE TABLE IF NOT EXISTS questions (\n"
      + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
      + "    telegram_id INTEGER NOT NULL,\n"
      + "    request_message_id INTEGER NOT NULL,\n"
      + "    response_message_id INTEGER,\n"
      + "    state TEXT NOT NULL,\n"
      + "    FOREIGN KEY (telegram_id) REFERENCES employees(telegram_id),\n"
      + "    FOREIGN KEY (request_message_id) REFERENCES messages(id),\n"
      + "    FOREIGN KEY (response_message_id) REFERENCES messages(id)\n"
      + ");"
  ),
  createTableQuestions(""
      + "CREATE TABLE IF NOT EXISTS questions (\n"
      + "    id INTEGER PRIMARY KEY AUTOINCREMENT,\n"
      + "    telegram_id INTEGER NOT NULL,\n"
      + "    question_message_id INTEGER,\n"
      + "    request_message_id INTEGER NOT NULL,\n"
      + "    response_message_id INTEGER,\n"
      + "    state TEXT NOT NULL,\n"
      + "    FOREIGN KEY (telegram_id) REFERENCES employees(telegram_id),\n"
      + "    FOREIGN KEY (question_message_id) REFERENCES messages(id),\n"
      + "    FOREIGN KEY (request_message_id) REFERENCES messages(id),\n"
      + "    FOREIGN KEY (response_message_id) REFERENCES messages(id)\n"
      + ");"
  ),
  @Deprecated
  addColumnMigration(""
      + "ALTER TABLE questions\n"
      + "ADD question_message_id INTEGER REFERENCES messages(id) ON UPDATE SET NULL;"
  ),
  addQuestion(""
      + "INSERT INTO questions (\n"
      + "    telegram_id,\n"
      + "    question_message_id,\n"
      + "    request_message_id,\n"
      + "    state\n"
      + ") VALUES (\n"
      + "    {},\n"
      + "    {},\n"
      + "    {},\n"
      + "    'OPEN'\n"
      + ");"
  ),
  updateQuestionMessageId("UPDATE questions SET {} = {} WHERE id = {};"),
  addQuestionFull(""
      + "INSERT INTO questions (\n"
      + "    telegram_id,\n"
      + "    question_message_id,\n"
      + "    request_message_id,\n"
      + "    response_message_id,\n"
      + "    state\n"
      + ") VALUES (\n"
      + "    {},\n"
      + "    {},\n"
      + "    {},\n"
      + "    {},\n"
      + "    'OPEN'\n"
      + ");"
  ),
  updateQuestionResponseById(""
      + "UPDATE questions SET response_message_id = {} WHERE id = {};"
  ),
  getQuestionReport(""
      + "SELECT\n"
      + "    rq_user.last_name AS 'Фамилия спрашивающего',\n"
      + "    rq_user.first_name AS 'Имя спрашивающего',\n"
      + "    rq.text AS 'Текст запроса',\n"
      + "    rq.date AS 'Дата запроса',\n"
      + "    rs_user.last_name AS 'Фамилия отвечающего',\n"
      + "    rs_user.first_name AS 'Имя отвечающего',\n"
      + "    rs.text AS 'Текст ответа',\n"
      + "    rs.date AS 'Дата ответа'\n"
      + "FROM questions\n"
      + "JOIN employees AS rq_user ON rq_user.telegram_id = questions.telegram_id\n"
      + "JOIN messages AS rq ON questions.request_message_id = rq.id\n"
      + "JOIN messages AS rs ON questions.response_message_id = rs.id\n"
      + "JOIN employees AS rs_user ON rs_user.telegram_id = rs.user_id"
  ),
  updateQuestionResponse(""
      + "UPDATE questions SET response_message_id = {} \n"
      + "WHERE telegram_id = {} AND request_message_id = {};"
  ),
  getQuestion("SELECT * FROM questions WHERE telegram_id={} AND request_message_id = {}"),
  getQuestionByTelegramId("SELECT * FROM questions WHERE telegram_id={}"),
  ;

  private final String sql;

  DatabaseSqlType(String sql) {
    this.sql = sql;
  }

  public String getSql() {
    return sql;
  }

  public String getSql(Object... args) {
    return ApplicationUtils.addArgsToValue(this.sql, args).replace("{}", "?");
  }
}
