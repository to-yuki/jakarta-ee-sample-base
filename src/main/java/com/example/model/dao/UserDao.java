package com.example.model.dao;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.model.service.User;

/**
 * ユーザーデータアクセスオブジェクト（DAO）クラス
 * ユーザーテーブルに対するCRUD操作を提供します
 */
public class UserDao {
    // ログ出力用のロガーインスタンス
    private static final Logger LOGGER = Logger.getLogger(UserDao.class.getName());
    
    /**
     * usersテーブルを初期化（テーブル作成とサンプルデータ投入）
     * DatabaseManagerから呼び出されます
     */
    public static void initializeTable() {
        // try-with-resources文で自動的にコネクションをクローズ
        try (Connection conn = DatabaseManager.getConnection()) {
            // usersテーブルのCREATE文（テキストブロックを使用）
            String createTableSQL = """
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    username TEXT NOT NULL UNIQUE,
                    password TEXT NOT NULL,
                    email TEXT NOT NULL,
                    full_name TEXT NOT NULL
                )
                """;
            
            // SQLステートメントを実行
            try (Statement stmt = conn.createStatement()) {
                // テーブルを作成
                stmt.execute(createTableSQL);
                LOGGER.info("usersテーブルを作成しました");
                
                // サンプルデータの存在を確認
                String checkSQL = "SELECT COUNT(*) FROM users";
                ResultSet rs = stmt.executeQuery(checkSQL);
                rs.next();
                int count = rs.getInt(1);
                
                // データが存在しない場合のみサンプルデータを投入
                if (count == 0) {
                    insertSampleData(conn);
                }
            }
            
        } catch (SQLException e) {
            // SQL実行エラーをログに記録
            LOGGER.log(Level.SEVERE, "usersテーブル初期化エラー", e);
        }
    }
    
    /**
     * サンプルデータを投入
     * 
     * @param conn データベースコネクション
     * @throws SQLException SQL実行エラー
     */
    private static void insertSampleData(Connection conn) throws SQLException {
        // サンプルユーザーデータのINSERT文
        String insertSQL = """
            INSERT INTO users (username, password, email, full_name) VALUES
            ('admin', 'admin123', 'admin@example.com', '管理者'),
            ('user1', 'pass123', 'user1@example.com', '山田太郎'),
            ('user2', 'pass456', 'user2@example.com', '佐藤花子')
            """;
        
        // SQLを実行
        try (Statement stmt = conn.createStatement()) {
            stmt.executeUpdate(insertSQL);
            LOGGER.info("サンプルデータを投入しました");
        }
    }
    
    /**
     * データベース接続を取得
     * DatabaseManagerを使用して接続を取得します
     * 
     * @return データベースコネクション
     * @throws SQLException 接続エラー
     */
    private static Connection getConnection() throws SQLException {
        // DatabaseManagerから接続を取得
        return DatabaseManager.getConnection();
    }
    
    /**
     * ユーザー認証を実行
     * ユーザー名とパスワードでデータベースを検索します
     * 
     * @param username ユーザー名
     * @param password パスワード
     * @return 認証成功時はUserオブジェクト、失敗時はnull
     */
    public static User authenticate(String username, String password) {
        // パラメータ化クエリでSQLインジェクション対策
        String sql = "SELECT id, username, email, full_name FROM users WHERE username = ? AND password = ?";
        
        // try-with-resources文で自動的にリソースをクローズ
        try (Connection conn = getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            
            // プレースホルダーに値を設定
            pstmt.setString(1, username);  // 1番目の?にユーザー名を設定
            pstmt.setString(2, password);  // 2番目の?にパスワードを設定
            
            // クエリを実行して結果を取得
            ResultSet rs = pstmt.executeQuery();
            
            // 結果が存在する場合（認証成功）
            if (rs.next()) {
                // 結果セットからUserオブジェクトを生成して返す
                return new User(
                    rs.getInt("id"),           // ユーザーID
                    rs.getString("username"),   // ユーザー名
                    rs.getString("email"),      // メールアドレス
                    rs.getString("full_name")   // フルネーム
                );
            }
            
        } catch (SQLException e) {
            // SQL実行エラーをログに記録
            LOGGER.log(Level.SEVERE, "認証エラー", e);
        }
        
        // 認証失敗またはエラーの場合はnullを返す
        return null;
    }
}
