package com.example.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import com.example.model.DatabaseManager;

import java.io.IOException;

/**
 * トップページを表示するサーブレット
 */
@WebServlet("")
public class IndexServlet extends HttpServlet {
    
    @Override
    public void init() throws ServletException {
        super.init();
        // 初回起動時にデータベースを初期化
        DatabaseManager.initialize();
    }
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        request.getRequestDispatcher("/WEB-INF/views/index.jsp").forward(request, response);
    }
}
