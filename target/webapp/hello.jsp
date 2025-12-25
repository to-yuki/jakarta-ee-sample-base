<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Hello JSP</title>
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
            max-width: 500px;
            width: 100%;
            text-align: center;
        }
        
        h1 {
            color: #333;
            margin-bottom: 20px;
            font-size: 36px;
        }
        
        .message {
            color: #666;
            font-size: 18px;
            margin-bottom: 20px;
            line-height: 1.6;
        }
        
        .info {
            background-color: #f0f0f0;
            padding: 15px;
            border-radius: 5px;
            margin-top: 20px;
            text-align: left;
        }
        
        .info-item {
            padding: 8px 0;
            color: #555;
            border-bottom: 1px solid #ddd;
        }
        
        .info-item:last-child {
            border-bottom: none;
        }
        
        .info-label {
            font-weight: 600;
            color: #667eea;
        }
        
        .btn {
            display: inline-block;
            margin-top: 20px;
            padding: 12px 30px;
            background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
            color: white;
            text-decoration: none;
            border-radius: 5px;
            font-weight: 600;
            transition: transform 0.2s;
        }
        
        .btn:hover {
            transform: translateY(-2px);
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>👋 Hello Jakarta EE JSP!</h1>
        <p class="message">
            これは webapp 直下に配置された JSP ファイルです。<br>
            WEB-INF 外にあるため、直接 URL でアクセスできます。
        </p>
        
        <div class="info">
            <div class="info-item">
                <span class="info-label">現在時刻:</span> 
                <%= new java.util.Date() %>
            </div>
            <div class="info-item">
                <span class="info-label">サーバー情報:</span> 
                <%= application.getServerInfo() %>
            </div>
            <div class="info-item">
                <span class="info-label">セッションID:</span> 
                <%= session.getId() %>
            </div>
            <div class="info-item">
                <span class="info-label">コンテキストパス:</span> 
                <%= request.getContextPath() %>
            </div>
        </div>
        
        <a href="${pageContext.request.contextPath}/" class="btn">トップページへ</a>
    </div>
</body>
</html>
