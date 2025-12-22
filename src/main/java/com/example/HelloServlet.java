package com.example;

// Jakarta EEのサーブレットAPIをインポート
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

// Java標準APIをインポート
import java.io.IOException;

/**
 * 簡単なHello World表示用のサーブレット
 * サーブレットの基本的な使い方を示すサンプルクラスです
 */
@WebServlet("/hello") // このサーブレットを/app/hello URLにマッピング
public class HelloServlet extends HttpServlet {
    
    /**
     * HTTP GETリクエストの処理
     * ブラウザから/app/helloにアクセスした際に実行されます
     * 
     * @param request HTTPリクエストオブジェクト（クライアントからの情報）
     * @param response HTTPレスポンスオブジェクト（クライアントへの返答）
     * @throws ServletException サーブレット例外
     * @throws IOException 入出力例外
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        // レスポンスのコンテンツタイプを設定（HTML形式、文字コードはUTF-8）
        response.setContentType("text/html;charset=UTF-8");
        
        // PrintWriterを使ってHTMLを出力（try-with-resources文で自動クローズ）
        try (var out = response.getWriter()) {
            // HTML文書の開始
            out.println("<!DOCTYPE html>");
            out.println("<html>");
            out.println("<head>");
            out.println("<title>Hello Servlet</title>");
            out.println("</head>");
            out.println("<body>");
            
            // メインコンテンツの出力
            out.println("<h1>Hello Servlet!</h1>");
            out.println("<p>このサーブレットは /webapp/hello でアクセスできます。</p>");
            
            // 現在の日時を表示（動的コンテンツの例）
            out.println("<p>現在時刻: " + new java.util.Date() + "</p>");
            
            // ナビゲーションリンク
            out.println("<p><a href=\"" + request.getContextPath() + "/\">トップページへ</a></p>");
            
            // HTML文書の終了
            out.println("</body>");
            out.println("</html>");
        } // try-with-resourcesにより、ここでPrintWriterが自動的にクローズされる
    }
    
    /**
     * HTTP POSTリクエストの処理
     * POSTリクエストもGETと同じ処理を行います
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
