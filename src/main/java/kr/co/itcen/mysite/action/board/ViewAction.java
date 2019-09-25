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

public class ViewAction implements Action {

	@Override
	public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

		String no =request.getParameter("no");
		BoardVo vo = new BoardDao().view(Long.parseLong(no)); // user.no 로 구분하여 출력
		request.setAttribute("vo", vo);
		
		new BoardDao().visit(Long.parseLong(no)); // 조회수 증가 메소드 가져오기
		
		WebUtils.forward(request, response,"/WEB-INF/views/board/view.jsp" ); // view.jsp로 이동

	}

}

