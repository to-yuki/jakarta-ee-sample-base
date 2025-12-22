package com.example.model;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * SQLite データベース管理クラス
 */
public class DatabaseManager {
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    private static final String DB_URL = "jdbc:sqlite:webapp.db";
    private static boolean initialized = false;
    
    // 静的イニシャライザーでJDBCドライバーをロード
    static {
        try {
            Class.forName("org.sqlite.JDBC");
        } catch (ClassNotFoundException e) {
            LOGGER.log(Level.SEVERE, "SQLite JDBCドライバーが見つかりません", e);
        }
    }
    
    /**
     * データベースを初期化（テーブル作成とサンプルデータ投入）
     */
    public static synchronized void initialize() {
        if (initialized) {
            return;
        }
        
        try (Connection conn = getConnection()) {
            // usersテーブル作成
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    email TEXT NOT NULL,
                    full_name TEXT NOT NULL
                )
                """;
            
            try (Statement stmt = conn.createStatement()) {
                stmt.execute(createTableSQL);
                
                // サンプルデータが存在するか確認
                String checkSQL = "SELECT COUNT(*) FROM users";
                ResultSet rs = stmt.executeQuery(checkSQL);
                rs.next();
                int count = rs.getInt(1);
                
                // サンプルデータがなければ投入
                if (count == 0) {
                    insertSampleData(conn);
                }
            }
            
            initialized = true;
            LOGGER.info("データベースを初期化しました");
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "データベース初期化エラー", e);
        }
    }
    
    /**
     * サンプルデータを投入
     */
    private static void insertSampleData(Connection conn) throws SQLException {
        String insertSQL = """
            INSERT INTO users (username, password, email, full_name) VALUES
            ('admin', 'admin123', 'admin@example.com', '管理者'),
            ('user1', 'pass123', 'user1@example.com', '山田太郎'),
            ('user2', 'pass456', 'user2@example.com', '佐藤花子')
            """;
        
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertSQL);
            LOGGER.info("サンプルデータを投入しました");
        }
    }
    
    /**
     * データベース接続を取得
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DB_URL);
    }
    
    /**
     * ユーザー認証
     * @param username ユーザー名
     * @param password パスワード
     * @return 認証成功時はUserオブジェクト、失敗時はnull
     */
    public static User authenticate(String username, String password) {
        String sql = "SELECT id, username, email, full_name FROM users WHERE username = ? AND password = ?";
        
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            
            ResultSet rs = pstmt.executeQuery();
            
            if (rs.next()) {
                return new User(
                    rs.getInt("id"),
                    rs.getString("username"),
                    rs.getString("email"),
                    rs.getString("full_name")
                );
            }
            
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "認証エラー", e);
        }
        
        return null;
    }
}
