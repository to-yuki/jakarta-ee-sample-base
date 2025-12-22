package com.example.model.service;

import com.example.model.dao.UserDao;

import java.util.logging.Logger;

/**
 * ユーザー関連のビジネスロジックを提供するサービスクラス
 * ServletとDAOの間に位置し、ビジネスロジックを集約します
 */
public class UserService {
    // ログ出力用のロガーインスタンス
    private static final Logger LOGGER = Logger.getLogger(UserService.class.getName());
    
    /**
     * データベースを初期化
     * アプリケーション起動時に1度だけ実行されます
     */
    public void initializeDatabase() {
        LOGGER.info("ユーザーサービス: データベース初期化を開始");
        // DatabaseManagerを呼び出してデータベースを初期化
        com.example.model.dao.DatabaseManager.initialize();
        LOGGER.info("ユーザーサービス: データベース初期化が完了");
    }
    
    /**
     * ユーザー認証を実行
     * 
     * @param username ユーザー名
     * @param password パスワード
     * @return 認証成功時はUserオブジェクト、失敗時はnull
     */
    public User authenticateUser(String username, String password) {
        // 入力値の検証（ビジネスロジック層での追加チェック）
        if (username == null || username.trim().isEmpty()) {
            LOGGER.warning("ユーザーサービス: ユーザー名が空です");
            return null;
        }
        
        if (password == null || password.trim().isEmpty()) {
            LOGGER.warning("ユーザーサービス: パスワードが空です");
            return null;
        }
        
        // DAO層のUserDaoを使用して認証を実行
        LOGGER.info("ユーザーサービス: ユーザー認証を実行中 - ユーザー名: " + username);
        User user = UserDao.authenticate(username, password);
        
        // 認証結果をログに記録
        if (user != null) {
            LOGGER.info("ユーザーサービス: 認証成功 - ユーザーID: " + user.getId());
        } else {
            LOGGER.warning("ユーザーサービス: 認証失敗 - ユーザー名: " + username);
        }
        
        return user;
    }
    
    /**
     * ユーザー情報を取得
     * （将来的な拡張用メソッド）
     * 
     * @param userId ユーザーID
     * @return Userオブジェクト
     */
    public User getUserById(int userId) {
        // TODO: UserDaoに該当メソッドを追加して実装
        LOGGER.info("ユーザーサービス: ユーザー情報取得 - ID: " + userId);
        return null;
    }
}
