package com.atguigu.redis.servlet;

import redis.clients.jedis.Jedis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalTime;
import java.util.Random;

//处理发送验证码的Servlet
@WebServlet("/SendCodeServlet")
public class SendCodeServlet extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        //获取手机号
        String phoneNo = request.getParameter("phone_no");

        //验空
        if (phoneNo == null || "".equals(phoneNo)){
            return;
        }

        //创建jedis对象连接redis
        Jedis jedis = new Jedis("192.168.6.100", 6379);

        //创建发送次数key
        String countKey = phoneNo+":count";

        //获取发送次数
        String count = jedis.get(countKey);

        //验证发送次数
        if (count == null){//证明今日还没有发送过验证码
            //获取今日剩余秒数
            long theLeftSeconds = getTheLeftSeconds();

            //向redis中保存一个计数器，设置值为1，有效时间为当天
            jedis.setex(countKey,(int)theLeftSeconds,"1");

        }else if ("3".equals(count)){//证明今日已经发送过3次验证码
            //关闭jedis
            jedis.close();

            //响应前台为limit
            response.getWriter().write("limit");

            return;
        }else {//证明竟日发送次数还没有超过3次
            jedis.incr(countKey);
        }

        //创建验证码key
        String codeKey = phoneNo + ":code";

        //生成6位随机验证码
        String code = getCode(6);

        //将验证码key和验证码添加到redis中(有效时间120s)
        jedis.setex(codeKey,120,code);

        //关闭jedis
        jedis.close();

        //响应前台true
        response.getWriter().write("true");
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        doPost(request,response);
    }

    // 随机生成验证码的方法
    private String getCode(int len) {
        String code = "";
        for (int i = 0; i < len; i++) {
            int rand = new Random().nextInt(10);
            code += rand;
        }
        return code;
    }

    //获取当天剩余秒数的方法
    private long getTheLeftSeconds(){
        //获取现在的时间
        LocalTime now = LocalTime.now();
        //获取当日23点59分59秒的时间
        LocalTime end = LocalTime.of(23, 59, 59);
        //获取end与now相差的秒数
        long millis = Duration.between(now, end).toMillis()/1000;
        return millis;
    }
}
