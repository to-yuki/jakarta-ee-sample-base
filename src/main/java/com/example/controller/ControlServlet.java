package com.example.controller;

// Jakarta EEのサーブレットAPIをインポート
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import com.example.model.service.User;
import com.example.model.service.UserService;

// Java標準APIをインポート
import java.io.IOException;
import java.util.logging.Logger;

/**
 * フロントコントローラーパターンを実装した統合サーブレット
 * 全てのリクエストを受け取り、URLパスに基づいて適切な処理に振り分けます
 */
@WebServlet(urlPatterns = {"", "/app/*"}) // ルートパスと/app配下の全てのパスにマッピング
public class ControlServlet extends HttpServlet {
    // ログ出力用のロガーインスタンス
    private static final Logger LOGGER = Logger.getLogger(ControlServlet.class.getName());
    
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
        // UserServiceを通じてデータベースを初期化
        userService.initializeDatabase();
        LOGGER.info("ControlServlet: 初期化完了");
    }
    
    /**
     * HTTP GETリクエストの処理
     * URLパスに基づいて適切な処理メソッドを呼び出します
     * 
     * @param request HTTPリクエストオブジェクト
     * @param response HTTPレスポンスオブジェクト
     * @throws ServletException サーブレット例外
     * @throws IOException 入出力例外
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // URLパスから処理を判断
        String pathInfo = request.getPathInfo();
        
        // ルートパス（"/"）または空の場合はトップページへ
        if (pathInfo == null || pathInfo.equals("/") || pathInfo.equals("")) {
            showIndex(request, response);
            return;
        }
        
        // パスに基づいて処理を振り分け
        switch (pathInfo) {
            case "/login":
                showLoginPage(request, response);
                break;
            case "/logout":
                processLogout(request, response);
                break;
            case "/home":
                showHomePage(request, response);
                break;
            default:
                // 存在しないパスの場合は404エラー
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                break;
        }
    }
    
    /**
     * HTTP POSTリクエストの処理
     * フォーム送信などの処理を行います
     * 
     * @param request HTTPリクエストオブジェクト
     * @param response HTTPレスポンスオブジェクト
     * @throws ServletException サーブレット例外
     * @throws IOException 入出力例外
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // URLパスから処理を判断
        String pathInfo = request.getPathInfo();
        
        if (pathInfo == null) {
            response.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        
        // パスに基づいて処理を振り分け
        switch (pathInfo) {
            case "/login":
                processLogin(request, response);
                break;
            case "/logout":
                processLogout(request, response);
                break;
            default:
                // POSTが許可されていないパスの場合は405エラー
                response.sendError(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
                break;
        }
    }
    
    // ========== 各機能の実装メソッド ==========
    
    /**
     * トップページ（index.jsp）を表示
     */
    private void showIndex(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        LOGGER.info("トップページを表示");
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
    
    /**
     * ログイン画面を表示
     * 既にログイン済みの場合はホーム画面にリダイレクト
     */
    private void showLoginPage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // 現在のセッションを取得（新規作成はしない）
        HttpSession session = request.getSession(false);
        
        // セッションが存在し、かつuser属性が設定されている＝ログイン済み
        if (session != null && session.getAttribute("user") != null) {
            // 既にログイン済みの場合、ホーム画面にリダイレクト
            response.sendRedirect(request.getContextPath() + "/app/home");
            return;
        }
        
        // ログイン画面（JSP）を表示
        request.getRequestDispatcher("/WEB-INF/views/login.jsp").forward(request, response);
    }
    
    /**
     * ログイン処理
     * フォームから送信されたユーザー名とパスワードで認証を行います
     */
    private void processLogin(HttpServletRequest request, HttpServletResponse response) 
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
            // ログイン時刻を保存
            session.setAttribute("loginTime", new java.util.Date());
            
            // ログイン成功をロガーに記録（INFOレベル）
            LOGGER.info(String.format("ユーザーがログインしました: %s (ID: %d, セッションID: %s)", 
                user.getUsername(), user.getId(), session.getId()));
            
            // ホーム画面にリダイレクト
            response.sendRedirect(request.getContextPath() + "/app/home");
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
    
    /**
     * ログアウト処理
     * セッションを無効化してログイン画面にリダイレクト
     */
    private void processLogout(HttpServletRequest request, HttpServletResponse response) 
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
        response.sendRedirect(request.getContextPath() + "/app/login");
    }
    
    /**
     * ホーム画面を表示
     * ログイン済みユーザーのみアクセス可能
     */
    private void showHomePage(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // ログインチェック：セッションが存在しユーザー情報があるか確認
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("user") == null) {
            // ログインしていない場合、ログイン画面にリダイレクト
            response.sendRedirect(request.getContextPath() + "/app/login");
            return;
        }
        
        // ホーム画面（JSP）を表示
        request.getRequestDispatcher("/WEB-INF/views/home.jsp").forward(request, response);
    }
}
