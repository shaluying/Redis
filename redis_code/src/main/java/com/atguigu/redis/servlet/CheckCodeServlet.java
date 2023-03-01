package com.atguigu.redis.servlet;

import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

//处理验证验证码请求的Servlet
@WebServlet("/CheckCodeServlet")
public class CheckCodeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
       //获取手机号和验证码
        String phoneNoInput = request.getParameter("phone_no");
        String codeInput = request.getParameter("verify_code");

        //验空
        if (phoneNoInput == null || codeInput == null || "".equals(phoneNoInput) || "".equals(codeInput)){
            return;
        }

        //创建jedis对象连接redis
        Jedis jedis = new Jedis("192.168.6.100", 6379);

        //获取手机号对应的验证码
        String code = jedis.get(phoneNoInput+":code");

        //验证
        if (codeInput.equals(code)){//验证成功
            //销毁验证码
            jedis.del(phoneNoInput+":code");

            //响应前台true
            response.getWriter().write("true");
        }

        //关闭jedis
        jedis.close();
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }
}
