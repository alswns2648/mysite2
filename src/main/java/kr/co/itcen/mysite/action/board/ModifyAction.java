package kr.co.itcen.mysite.action.board;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import kr.co.itcen.mysite.dao.BoardDao;
import kr.co.itcen.mysite.vo.BoardVo;
import kr.co.itcen.mysite.vo.UserVo;
import kr.co.itcen.web.WebUtils;
import kr.co.itcen.web.mvc.Action;

public class ModifyAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		HttpSession session = request.getSession();
		if(session==null) {
			WebUtils.redirect(request, response, request.getContextPath());
			return;
		}
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		
		String no = request.getParameter("no");
		BoardVo vo = new BoardDao().get(Long.parseLong(no));
		if(authUser.getNo()!=vo.getUser_no()) {
			WebUtils.redirect(request, response, request.getContextPath());
			return;
		}
		
		String title = request.getParameter("title");
		String contents =request.getParameter("contents");
		
		vo.setNo(Long.parseLong(no));
		vo.setTitle(title);
		vo.setContents(contents);
		
		new BoardDao().modify(vo);
		WebUtils.redirect(request, response, request.getContextPath()+"/board?a=view&no="+no);
	}

}
