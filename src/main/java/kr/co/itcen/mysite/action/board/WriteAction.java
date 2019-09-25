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

public class WriteAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		HttpSession session = request.getSession();
		if(session ==null) {
			WebUtils.redirect(request, response, request.getContextPath());
			return;
		}
		
		UserVo authUser = (UserVo)session.getAttribute("authUser");
		if(authUser ==null) {
			WebUtils.redirect(request, response, request.getContextPath());
			return;
		}
		
		BoardVo vo = new BoardVo();
		
		String title =request.getParameter("title");
		String contents = request.getParameter("contents");
		
		vo.setTitle(title);
		vo.setContents(contents);
		vo.setUser_no(authUser.getNo());
		
		if(request.getParameter("no")==null) {
			new BoardDao().insert(vo);

			WebUtils.redirect(request, response, request.getContextPath()+"/board");
		}else {
			String g_no = request.getParameter("g_no");
			String o_no =request.getParameter("o_no");
			String depth = request.getParameter("depth");
			
			vo.setG_no(Integer.parseInt(g_no));
			vo.setO_no(Integer.parseInt(o_no)+1);
			vo.setDepth(Integer.parseInt(depth)+1);
			new BoardDao().reply(vo);

			WebUtils.redirect(request, response, request.getContextPath()+"/board");
		}
	}

}


