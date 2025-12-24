<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.service.User" %>
<%
    User user = (User) session.getAttribute("user");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Jakarta EE ログインサンプル</title>
    <style>
        * {
            margin: 0;
            padding: 0;
            box-sizing: border-box;
        }
        
        body {
            font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            min-height: 100vh;
            display: flex;
            justify-content: center;
            align-items: center;
            padding: 20px;
        }
        
        .container {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            padding: 40px;
            max-width: 600px;
            width: 100%;
            text-align: center;
        }
        
        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 32px;
        }
        
        .subtitle {
            color: #666;
            margin-bottom: 30px;
            font-size: 16px;
        }
        
        .status {
            padding: 15px;
            border-radius: 5px;
            margin-bottom: 25px;
        }
        
        .logged-in {
            background-color: #d4edda;
            color: #155724;
            border: 1px solid #c3e6cb;
        }
        
        .logged-out {
            background-color: #fff3cd;
            color: #856404;
            border: 1px solid #ffeaa7;
        }
        
        .user-display {
            font-size: 18px;
            font-weight: 600;
            margin-top: 10px;
        }
        
        .btn {
            display: inline-block;
            padding: 12px 30px;
            margin: 10px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            text-decoration: none;
            transition: transform 0.2s;
        }
        
        .btn:hover {
            transform: translateY(-2px);
        }
        
        .btn-primary {
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
        }
        
        .btn-success {
            background: linear-gradient(135deg, #56ab2f 0%, #a8e063 100%);
            color: white;
        }
        
        .btn-danger {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }
        
        .features {
            text-align: left;
            margin-top: 30px;
            padding: 20px;
            background-color: #f8f9fa;
            border-radius: 5px;
        }
        
        .features h3 {
            color: #667eea;
            margin-bottom: 15px;
        }
        
        .features ul {
            list-style: none;
            padding: 0;
        }
        
        .features li {
            padding: 8px 0;
            color: #555;
        }
        
        .features li:before {
            content: "✓ ";
            color: #667eea;
            font-weight: bold;
            margin-right: 8px;
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>🚀 Jakarta EE ログインサンプル</h1>
        <p class="subtitle">MVC パターンを使用した認証システム</p>
        
        <% if (user != null) { %>
            <div class="status logged-in">
                <div>✅ ログイン中</div>
                <div class="user-display"><%= user.getFullName() %> (<%= user.getUsername() %>)</div>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/app/home" class="btn btn-success">ホーム画面へ</a>
                <form action="${pageContext.request.contextPath}/app/logout" method="post" style="display: inline;">
                    <button type="submit" class="btn btn-danger">ログアウト</button>
                </form>
            </div>
        <% } else { %>
            <div class="status logged-out">
                ⚠️ ログインしていません
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/app/login" class="btn btn-primary">ログイン</a>
            </div>
        <% } %>
        
        <div class="features">
            <h3>📋 機能一覧</h3>
            <ul>
                <li>SQLite データベース連携</li>
                <li>初回アクセス時の自動テーブル作成</li>
                <li>セッション管理によるログイン状態の保持</li>
                <li>ユーザー情報の表示</li>
                <li>ログイン/ログアウト機能</li>
                <li>MVC パターンによる実装</li>
                <li>WEB-INF 配下での JSP 管理（セキュア）</li>
            </ul>
        </div>
    </div>
</body>
</html>
