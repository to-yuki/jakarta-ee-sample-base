package com.example.model.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * データベース接続を管理するクラス
 * 全てのDAOクラスで共通のデータベース接続を提供します
 */
public class DatabaseManager {
    // ログ出力用のロガーインスタンス
    private static final Logger LOGGER = Logger.getLogger(DatabaseManager.class.getName());
    
    // データベース接続URL（SQLiteファイルのパス）
    // PostgreSQLの場合: "jdbc:postgresql://localhost:5432/database"
    // MySQLの場合: "jdbc:mysql://localhost:3306/database?useSSL=false&serverTimezone=Asia/Tokyo"
    private static final String DB_URL = "jdbc:sqlite:webapp.db";
    
    // データベース接続用のユーザー名（SQLiteでは不要）
    private static final String DB_USER = null;
    
    // データベース接続用のパスワード（SQLiteでは不要）
    private static final String DB_PASSWORD = null;
    
    // データベース初期化済みフラグ
    private static boolean initialized = false;
    
    // 静的イニシャライザーでJDBCドライバーをロード
    static {
        try {
            // SQLiteのJDBCドライバークラスを明示的にロード
            Class.forName("org.sqlite.JDBC");
            
            // PostgreSQLを使用する場合は以下に変更:
            // Class.forName("org.postgresql.Driver");
            
            // MySQLを使用する場合は以下に変更:
            // Class.forName("com.mysql.cj.jdbc.Driver");
            
            LOGGER.info("JDBCドライバーをロードしました");
        } catch (ClassNotFoundException e) {
            // ドライバーが見つからない場合はエラーログを出力
            LOGGER.log(Level.SEVERE, "JDBCドライバーが見つかりません", e);
        }
    }
    
    /**
     * データベース接続を取得
     * ユーザー名とパスワードが設定されている場合はそれらを使用します
     * 
     * @return データベースコネクション
     * @throws SQLException 接続エラー
     */
    public static Connection getConnection() throws SQLException {
        // ユーザー名とパスワードが設定されているかチェック
        if (DB_USER != null && DB_PASSWORD != null) {
            // PostgreSQL/MySQL用: ユーザー名とパスワードを使用して接続
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } else {
            // SQLite用: 認証なしで接続
            return DriverManager.getConnection(DB_URL);
        }
    }
    
    /**
     * データベースを初期化
     * 各DAOクラスの初期化メソッドを呼び出します
     * スレッドセーフにするためsynchronizedで同期化
     */
    public static synchronized void initialize() {
        // 既に初期化済みの場合は何もしない
        if (initialized) {
            return;
        }
        
        LOGGER.info("データベースマネージャー: 初期化を開始");
        
        try {
            // データベース接続をテスト
            try (Connection conn = getConnection()) {
                LOGGER.info("データベース接続テスト成功: " + conn.getMetaData().getDatabaseProductName());
            }
            
            // 各DAOの初期化を実行
            UserDao.initializeTable();
            
            // 初期化完了フラグを立てる
            initialized = true;
            LOGGER.info("データベースマネージャー: 初期化が完了");
            
        } catch (SQLException e) {
            // データベース接続エラーをログに記録
            LOGGER.log(Level.SEVERE, "データベース初期化エラー", e);
        }
    }
    
    /**
     * データベース接続URLを取得（デバッグ用）
     * 
     * @return データベースURL
     */
    public static String getDatabaseUrl() {
        return DB_URL;
    }
    
    /**
     * データベースが初期化済みかどうかを確認
     * 
     * @return 初期化済みの場合true
     */
    public static boolean isInitialized() {
        return initialized;
    }
}
