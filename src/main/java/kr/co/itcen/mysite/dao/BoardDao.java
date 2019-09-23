package kr.co.itcen.mysite.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
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
	
	public List<BoardVo> getList() {
		List<BoardVo> result = new ArrayList<BoardVo>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();

			String sql = "select board.title, user.no, user.name, board.hit, date_format(board.reg_date, '%Y-%m-%d %h:%i:%s'), board.depth, board.contents, board.no "
					+ "from board, user "
					+ "where board.user_no=user.no "
					+ "order by g_no desc, o_no asc Limit 0, 5";
			pstmt = connection.prepareStatement(sql);
			
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
			e.printStackTrace();
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
	
	public BoardVo get(Long no) {
		BoardVo result = new BoardVo();
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = 
				"select a.user_no, a.title ,b.name, a.hit ,date_format(reg_date, '%Y-%m-%d %h:%i:%s'),depth"
				+ " from board, user  "
				+ "where board.user_no = user.no"
				+ " order by board.no desc" ;
			pstmt = connection.prepareStatement(sql);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Long user_no = rs.getLong(1);
				String title = rs.getString(2);
				String user_name = rs.getString(3);
				int hit = rs.getInt(4);
				String reg_date = rs.getString(5);
				int depth = rs.getInt(6);
				
				BoardVo vo= new BoardVo();
				vo.setUser_no(user_no);
				vo.setTitle(title);
				vo.setUser_name(user_name);
				vo.setHit(hit);
				vo.setReg_date(reg_date);
				vo.setDepth(depth);
				
				result = vo;
			}
		} catch (SQLException e) {
			e.printStackTrace();
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

	public BoardVo view(Long no) {
		
		BoardVo result = new BoardVo();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = "select no, title, contents, user_no, reg_date "
				+ "from board where no = ?" ;
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1,no);
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Long no1= rs.getLong(1);
				String title =rs.getString(2);
				String contents =rs.getString(3);
				Long user_no = rs.getLong(4);
				String reg_date = rs.getString(5);
				
				BoardVo vo= new BoardVo();
				vo.setNo(no1);
				vo.setTitle(title);
				vo.setContents(contents);
				vo.setUser_no(user_no);
				vo.setReg_date(reg_date);
				
				result = vo;
			}
		} catch (SQLException e) {
			e.printStackTrace();
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
	
	public Boolean write(BoardVo vo) {
		Boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		Statement stmt = null;
		
		try {
			connection = getConnection();
			
			String sql = "insert into board values(null, ?, ?, 0, now(), "
					+ "ifnull((select max(g_no)from board as b) +1,1), 1, 0, ?)"; // 처음값일경우 1삽입
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1,vo.getTitle());
			pstmt.setString(2,vo.getContents());
			pstmt.setLong(3, vo.getUser_no());
			
			int count = pstmt.executeUpdate();
			result = (count == 1);
			
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if(rs != null) {
					rs.close();
				}
				
				if(pstmt != null) {
					pstmt.close();
				}
				
				if(stmt != null) {
					stmt.close();
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
	
	public void delete(Long no) {
		Connection connection = null;
		PreparedStatement pstmt = null;
		
		try {
			connection = getConnection();
			
			String sql =" update board set view=false where no = ?";
			
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, no);
			
			pstmt.executeUpdate();
			
		} catch (SQLException e) {
			System.out.println("error:" + e);
		} finally {
			try {
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
	}

}
