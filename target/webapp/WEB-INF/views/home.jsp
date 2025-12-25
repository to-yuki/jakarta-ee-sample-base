<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.example.model.service.User" %>
<%
    // ログインチェック
    User user = (User) session.getAttribute("user");
    if (user == null) {
        response.sendRedirect(request.getContextPath() + "/app/login");
        return;
    }
    
    java.util.Date loginTime = (java.util.Date) session.getAttribute("loginTime");
%>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>ホーム - Jakarta EE サンプル</title>
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
            padding: 20px;
        }
        
        .container {
            max-width: 800px;
            margin: 0 auto;
        }
        
        .header {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            padding: 30px;
            margin-bottom: 20px;
        }
        
        .header h1 {
            color: #333;
            margin-bottom: 10px;
        }
        
        .welcome-message {
            color: #667eea;
            font-size: 18px;
            margin-bottom: 20px;
        }
        
        .user-info {
            background: white;
            border-radius: 10px;
            box-shadow: 0 10px 25px rgba(0, 0, 0, 0.2);
            padding: 30px;
            margin-bottom: 20px;
        }
        
        .user-info h2 {
            color: #333;
            margin-bottom: 20px;
            font-size: 22px;
            border-bottom: 2px solid #667eea;
            padding-bottom: 10px;
        }
        
        .info-row {
            display: flex;
            padding: 12px 0;
            border-bottom: 1px solid #f0f0f0;
        }
        
        .info-row:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: 600;
            color: #555;
            width: 150px;
        }
        
        .info-value {
            color: #333;
            flex: 1;
        }
        
        .session-info {
            background: #f8f9fa;
            border-radius: 10px;
            padding: 30px;
            margin-bottom: 20px;
        }
        
        .session-info h3 {
            color: #333;
            margin-bottom: 15px;
            font-size: 18px;
        }
        
        .session-id {
            background: white;
            padding: 10px;
            border-radius: 5px;
            font-family: 'Courier New', monospace;
            font-size: 12px;
            word-break: break-all;
            color: #666;
        }
        
        .actions {
            display: flex;
            gap: 10px;
        }
        
        .btn {
            padding: 12px 30px;
            border: none;
            border-radius: 5px;
            font-size: 16px;
            font-weight: 600;
            cursor: pointer;
            transition: transform 0.2s;
            text-decoration: none;
            display: inline-block;
        }
        
        .btn:hover {
            transform: translateY(-2px);
        }
        
        .btn-logout {
            background: linear-gradient(135deg, #f093fb 0%, #f5576c 100%);
            color: white;
        }
        
        .btn-home {
            background: linear-gradient(135deg, #4facfe 0%, #00f2fe 100%);
            color: white;
        }
        
        .stats {
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
            gap: 15px;
            margin-top: 20px;
        }
        
        .stat-card {
            background: white;
            padding: 20px;
            border-radius: 10px;
            box-shadow: 0 5px 15px rgba(0, 0, 0, 0.1);
            text-align: center;
        }
        
        .stat-card .icon {
            font-size: 36px;
            margin-bottom: 10px;
        }
        
        .stat-card .label {
            color: #888;
            font-size: 14px;
            margin-bottom: 5px;
        }
        
        .stat-card .value {
            color: #333;
            font-size: 18px;
            font-weight: 600;
        }
    </style>
</head>
<body>
    <div class="container">
        <div class="header">
            <h1>🏠 ホーム</h1>
            <div class="welcome-message">
                ようこそ、<strong><%= user.getFullName() %></strong> さん！
            </div>
            <div class="actions">
                <form action="${pageContext.request.contextPath}/app/logout" method="post" style="display: inline;">
                    <button type="submit" class="btn btn-logout">ログアウト</button>
                </form>
                <a href="${pageContext.request.contextPath}/" class="btn btn-home">トップページ</a>
            </div>
        </div>
        
        <div class="user-info">
            <h2>👤 ユーザー情報</h2>
            <div class="info-row">
                <div class="info-label">ユーザーID:</div>
                <div class="info-value"><%= user.getId() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">ユーザー名:</div>
                <div class="info-value"><%= user.getUsername() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">氏名:</div>
                <div class="info-value"><%= user.getFullName() %></div>
            </div>
            <div class="info-row">
                <div class="info-label">メールアドレス:</div>
                <div class="info-value"><%= user.getEmail() %></div>
            </div>
        </div>
        
        <div class="stats">
            <div class="stat-card">
                <div class="icon">🕐</div>
                <div class="label">ログイン時刻</div>
                <div class="value"><%= String.format("%tT", loginTime) %></div>
            </div>
            <div class="stat-card">
                <div class="icon">📅</div>
                <div class="label">ログイン日</div>
                <div class="value"><%= String.format("%tF", loginTime) %></div>
            </div>
            <div class="stat-card">
                <div class="icon">⏱️</div>
                <div class="label">セッション状態</div>
                <div class="value" style="color: #28a745;">アクティブ</div>
            </div>
        </div>
        
        <div class="session-info">
            <h3>🔑 セッション情報</h3>
            <div class="session-id">
                Session ID: <%= session.getId() %>
            </div>
        </div>
    </div>
</body>
</html>
