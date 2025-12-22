package com.example.controller;

// Jakarta EEのサーブレットAPIをインポート
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// サービス層のクラスをインポート
import com.example.model.service.UserService;

// Java標準APIをインポート
import java.io.IOException;

/**
 * トップページを表示するサーブレット
 * アプリケーションのエントリーポイントとなります
 */
@WebServlet("") // ルートパス（/）にマッピング
public class IndexServlet extends HttpServlet {
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
     * トップページ（index.jsp）を表示します
     * 
     * @param request HTTPリクエストオブジェクト
     * @param response HTTPレスポンスオブジェクト
     * @throws ServletException サーブレット例外
     * @throws IOException 入出力例外
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // index.jspにリクエストを転送（フォワード）
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
}
