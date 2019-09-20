package kr.co.itcen.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import kr.co.itcen.mysite.vo.BoardVo;

public class BoardDao {
	
	private Connection getConnection() throws SQLException {
		Connection connection = null;

		try {
			Class.forName("org.mariadb.jdbc.Driver");

			String url = "jdbc:mariadb://192.168.1.119:3306/webdb?characterEncoding=utf8";
			connection = DriverManager.getConnection(url, "webdb", "webdb");

		} catch (ClassNotFoundException e) {
			System.out.println("Fail to Loading Driver:" + e);
		}

		return connection;
	}
	
	public List<BoardVo> getList(int page) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();

			String sql = "select board.title, user.no, user.name, board.hit, date_format(board.reg_date, '%Y-%m-%d %h:%i:%s'), board.depth, board.contents, board.no "
					+ "from board, user "
					+ "where board.user_no=user.no and view=true "
					+ "order by g_no desc, o_no asc Limit ?, 5";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, page);
			rs = pstmt.executeQuery();

			while(rs.next()){
				String title = rs.getString(1);
				Long user_no = rs.getLong(2);
				String user_name = rs.getString(3);
				int hit = rs.getInt(4);
				String reg_date = rs.getString(5);
				int depth = rs.getInt(6);
				String contents = rs.getString(7);
				Long no = rs.getLong(8);
				
				BoardVo vo = new BoardVo();
				vo.setTitle(title);
				vo.setUser_no(user_no);
				vo.setUser_name(user_name);
				vo.setHit(hit);
				vo.setReg_date(reg_date);
				vo.setDepth(depth);
				vo.setContents(contents);
				vo.setNo(no);
				
				result.add(vo);
			}
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				if(pstmt != null) {
					pstmt.close();
				}
				if(connection != null) {
					connection.close();
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		return result;
	}	

}
