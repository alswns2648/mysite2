package kr.co.itcen.mysite.action.user;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.itcen.mysite.dao.UserDao;
import kr.co.itcen.mysite.vo.UserVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class UpdateformAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		//접근 제어 (ACL)
		HttpSession session = request.getSession();
		if(session == null) {
			WebUtils.redirect(request, response, request.getContextPath());
		}
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			WebUtils.redirect(request, response, request.getContextPath());
		} 
		
		UserVo result = new UserDao().get(authUser.getNo());
		request.setAttribute("userinfo", result);

		WebUtils.forward(request, response, "/WEB-INF/views/user/updateform.jsp");

	}

}
