package com.atguigu.redis;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Random;

/**
 * 秒杀案例
 */
@WebServlet("/doseckill")
public class SecKillServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//随机生成用户id
		String userid = new Random().nextInt(50000) +"" ;
		//获取商品id
		String prodid =request.getParameter("prodid");
		//秒杀
//		boolean isSuccess=SecKill_redis.doSecKill(userid,prodid);
		boolean isSuccess=SecKill_redisByScript.doSecKill(userid,prodid);
		//给前端响应
		response.getWriter().print(isSuccess);
	}

}
