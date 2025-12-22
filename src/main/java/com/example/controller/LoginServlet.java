package com.example.controller;

// Jakarta EEのサーブレットAPIをインポート
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// アプリケーションのモデルクラスとサービスクラスをインポート
import com.example.model.User;
import com.example.model.service.UserService;

// Java標準APIをインポート
import java.io.IOException;
import java.util.logging.Logger;

/**
 * ログイン処理を行うサーブレット
 * このクラスは/login URLへのリクエストを処理します
 */
@WebServlet("/login") // このサーブレットを/login URLにマッピング
public class LoginServlet extends HttpServlet {
    // ログ出力用のロガーインスタンス（クラス名を使用して初期化）
    private static final Logger LOGGER = Logger.getLogger(LoginServlet.class.getName());
    
    // ユーザー関連のビジネスロジックを処理するサービス層のインスタンス
    private final UserService userService = new UserService();
    
    /**
     * サーブレットの初期化メソッド
     * サーブレットが最初にロードされたときに1度だけ呼ばれます
     */
    @Override
    public void init() throws ServletException {
        // 親クラスの初期化処理を実行
        super.init();
        // UserServiceを通じてデータベースを初期化（テーブル作成、サンプルデータ投入）
        userService.initializeDatabase();
    }
    
    /**
     * HTTP GETリクエストの処理
     * ログイン画面を表示するために使用されます
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
        
        // セッションが存在し、かつuser属性が設定されている＝ログイン済み
        if (session != null && session.getAttribute("user") != null) {
            // 既にログイン済みの場合、ホーム画面にリダイレクト
            response.sendRedirect(request.getContextPath() + "/home");
            return; // 処理を終了
        }
        
        // ログイン画面（JSP）を表示
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    /**
     * HTTP POSTリクエストの処理
     * フォームから送信されたログイン情報を処理します
     * 
     * @param request HTTPリクエストオブジェクト
     * @param response HTTPレスポンスオブジェクト
     * @throws ServletException サーブレット例外
     * @throws IOException 入出力例外
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // リクエストパラメータの文字エンコーディングをUTF-8に設定
        request.setCharacterEncoding("UTF-8");
        
        // フォームから送信されたユーザー名とパスワードを取得
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        
        // 入力チェック：ユーザー名とパスワードが空でないか確認
        if (username == null || username.trim().isEmpty() || 
            password == null || password.trim().isEmpty()) {
            // 入力が空の場合、エラーメッセージを設定
            request.setAttribute("error", "ユーザー名とパスワードを入力してください");
            // ログイン画面に戻る
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
            return; // 処理を終了
        }
        
        // UserServiceを通じてユーザー認証を実行（ビジネスロジック層での処理）
        User user = userService.authenticateUser(username, password);
        
        // 認証結果によって処理を分岐
        if (user != null) {
            // === 認証成功 ===
            
            // 新しいセッションを作成（存在しない場合）
            HttpSession session = request.getSession(true);
            // セッションにユーザー情報を保存
            session.setAttribute("user", user);
            // ログイン時刺を保存
            session.setAttribute("loginTime", new java.util.Date());
            
            // ログイン成功をロガーに記録（INFOレベル）
            LOGGER.info(String.format("ユーザーがログインしました: %s (ID: %d, セッションID: %s)", 
                user.getUsername(), user.getId(), session.getId()));
            
            // ホーム画面にリダイレクト
            response.sendRedirect(request.getContextPath() + "/home");
        } else {
            // === 認証失敗 ===
            
            // ログイン失敗をロガーに記録（WARNINGレベル）
            // セキュリティ目的でユーザー名とIPアドレスを記録
            LOGGER.warning(String.format("ログイン失敗: ユーザー名=%s, IPアドレス=%s", 
                username, request.getRemoteAddr()));
            // エラーメッセージを設定
            request.setAttribute("error", "ユーザー名またはパスワードが正しくありません");
            // 入力したユーザー名を保持（再入力の手間を省く）
            request.setAttribute("username", username);
            // ログイン画面に戻る
            request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
        }
    }
}
