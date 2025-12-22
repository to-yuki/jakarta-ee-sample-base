package com.example.controller;

// アプリケーションのモデルクラスをインポート
import com.example.model.User;

// Jakarta EEのサーブレットAPIをインポート
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// Java標準APIをインポート
import java.io.IOException;
import java.util.logging.Logger;

/**
 * ログアウト処理を行うサーブレット
 * このクラスは/logout URLへのリクエストを処理します
 */
@WebServlet("/logout") // このサーブレットを/logout URLにマッピング
public class LogoutServlet extends HttpServlet {
    // ログ出力用のロガーインスタンス（クラス名を使用して初期化）
    private static final Logger LOGGER = Logger.getLogger(LogoutServlet.class.getName());
    
    /**
     * HTTP GETリクエストの処理
     * ユーザーをログアウトさせてセッションを破棄します
     * 
     * @param request HTTPリクエストオブジェクト
     * @param response HTTPレスポンスオブジェクト
     * @throws ServletException サーブレット例外
     * @throws IOException 入出力例外
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 現在のセッションを取得（新規作成はしない）
        HttpSession session = request.getSession(false);
        
        // セッションが存在する場合のみ処理
        if (session != null) {
            // セッションからユーザー情報を取得（ログ出力用）
            User user = (User) session.getAttribute("user");
            // セッションIDを取得（ログ出力用）
            String sessionId = session.getId();
            
            // ログアウト情報をロガーに記録
            if (user != null) {
                // ユーザー情報がある場合：詳細情報を記録（INFOレベル）
                LOGGER.info(String.format("ユーザーがログアウトしました: %s (ID: %d, セッションID: %s)", 
                    user.getUsername(), user.getId(), sessionId));
            } else {
                // ユーザー情報がない場合：セッションIDのみ記録
                LOGGER.info(String.format("セッションが無効化されました: セッションID=%s", sessionId));
            }
            
            // セッションを無効化（セッション内の全データを削除）
            session.invalidate();
        }
        
        // ログイン画面にリダイレクト
        response.sendRedirect(request.getContextPath() + "/login");
    }
    
    /**
     * HTTP POSTリクエストの処理
     * GETリクエストと同じ処理を行います
     * 
     * @param request HTTPリクエストオブジェクト
     * @param response HTTPレスポンスオブジェクト
     * @throws ServletException サーブレット例外
     * @throws IOException 入出力例外
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // POSTリクエストをGETリクエストと同じ処理に委譲
        doGet(request, response);
    }
}
