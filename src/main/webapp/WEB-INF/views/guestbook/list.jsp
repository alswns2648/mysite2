<%@page import="java.util.List"%>
<%@page import="kr.co.itcen.mysite.vo.GuestbookVo"%>
<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>

<%
	List<GuestbookVo> list = (List<GuestbookVo>) request.getAttribute("list"); //다운캐스팅 명시
%>

<!DOCTYPE html>
<html>
<head>
<title>mysite</title>
<meta http-equiv="content-type" content="text/html; charset=utf-8">
<link href="<%=request.getContextPath()%>/assets/css/guestbook.css"
	rel="stylesheet" type="text/css">
</head>
<body>
	<div id="container">
		<jsp:include page="/WEB-INF/views/includes/header.jsp" />
		<div id="content">
			<div id="guestbook">
				<form action="<%=request.getContextPath()%>/gbs" method="post">
					<input type="hidden" name="a" value="add">
					<table>
						<tr>
							<td>이름</td>
							<td><input type="text" name="name"></td>
							<td>비밀번호</td>
							<td><input type="password" name="password"></td>
						</tr>
						<tr>
							<td colspan=4><textarea name="contents" id="content"></textarea></td>
						</tr>
						<tr>
							<td colspan=4 align=right><input type="submit" VALUE=" 확인 "></td>
						</tr>
					</table>
				</form>

				<ul>
					<li>
						<%
						int index = 0;
						int count = list.size();
						for (GuestbookVo vo : list)
						{
					%>
						<table>
							<tr>
								<td>[<%=count - index++%>]
								</td>
								<td>[<%=vo.getName()%>]
								</td>
								<td><%=vo.getRegDate()%></td>
								<td><a
									href="<%=request.getContextPath()%>/gbs?a=deleteform&no=<%=vo.getNo()%>">삭제</a></td>
							</tr>
							<tr>
								<td colspan=4><%=vo.getContents().replaceAll("\n", "<br>")%>
								</td>
							</tr>
						</table> <br> <%
						}
					%>
					</li>
				</ul>
			</div>
		</div>
		<jsp:include page="/WEB-INF/views/includes/navigation.jsp" />
		<jsp:include page="/WEB-INF/views/includes/footer.jsp" />
	</div>
</body>
</html>