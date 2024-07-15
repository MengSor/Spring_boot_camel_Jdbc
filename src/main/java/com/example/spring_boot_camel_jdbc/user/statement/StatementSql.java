package com.example.spring_boot_camel_jdbc.user.statement;


public enum StatementSql {
    FIND_USER_ALL("SELECT * FROM users"),
    FIND_USER_BY_ID("SELECT * FROM users WHERE id = ?"),
    CREATE_USER("INSERT INTO users VALUES (?,?,?)"),
    DELETE_USER("DELETE FROM users WHERE id = ?"),
    UPDATE_USER("UPDATE users SET name = ? , email = ? WHERE id = ?"),
    ID("id");
    private final String statement;

    StatementSql(String statement) {
        this.statement = statement;
    }
    public String getStatement(){
        return statement;
    }
}
