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

public class UpdateAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		if(session == null) {
			WebUtils.redirect(request, response, request.getContextPath());
		}
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser == null) {
			WebUtils.redirect(request, response, request.getContextPath());
		} 
		
		String name = request.getParameter("name");
		String password = request.getParameter("password");
		String gender = request.getParameter("gender");
		
		if(password.equals("")) {
			new UserDao().update(name, gender, authUser.getNo());
		}else {
			new UserDao().update(name, password, gender, authUser.getNo());
		}
		
		UserVo vo = new UserVo();
		vo.setNo(authUser.getNo());
		vo.setName(name);
		session.setAttribute("authUser", vo);
		
				
		WebUtils.redirect(request, response, request.getContextPath());
	}

}
