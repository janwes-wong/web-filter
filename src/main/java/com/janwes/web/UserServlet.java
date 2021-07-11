package com.janwes.web;

import com.alibaba.fastjson.JSON;
import com.janwes.entity.Result;
import com.janwes.entity.UserInfo;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Janwes
 * @version 1.0
 * @package com.janwes.web
 * @date 2021/7/11 14:25
 * @description
 */
@WebServlet(name = "userServlet", urlPatterns = "/userServlet")
public class UserServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doGet(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        UserInfo userInfo = new UserInfo("张三丰", "qazwsx147", "xxxx@qq.com", "男", "老张");
        response.setContentType("application/json;charset=utf-8");
        JSON.writeJSONString(response.getWriter(), new Result<>(200, "success", userInfo));
    }
}
