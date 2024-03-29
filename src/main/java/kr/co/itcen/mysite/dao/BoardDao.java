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
	
	public List<BoardVo> getList(String kwd) {
		List<BoardVo> result = new ArrayList<BoardVo>();
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			String sql = "select board.title, user.no, user.name, "
					+ "board.hit, date_format(board.reg_date, '%Y-%m-%d %h:%i:%s'), "
					+ "board.depth, board.contents, board.no, board.status "
					+ "from board, user "
					+ "where board.user_no = user.no and (title Like ? or contents Like ?) "
					+ "order by g_no desc, o_no asc Limit 0, 5";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, "%"+kwd+"%");
			pstmt.setString(2, "%"+kwd+"%");
			
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
				Boolean status = rs.getBoolean(9);
				
				BoardVo vo = new BoardVo();
				vo.setTitle(title);
				vo.setUser_no(user_no);
				vo.setUser_name(user_name);
				vo.setHit(hit);
				vo.setReg_date(reg_date);
				vo.setDepth(depth);
				vo.setContents(contents);
				vo.setNo(no);
				vo.setStatus(status);
				
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
	//list에서 보여지는 정보
	public BoardVo get(Long no) {
		BoardVo result = new BoardVo();
		
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		
		try {
			connection = getConnection();
			
			String sql = 
				"select user_no, title , hit ,date_format(reg_date, '%Y-%m-%d %h:%i:%s'),depth, o_no, g_no, contents"
				+ " from board a "
				+ "where no=?" ;
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, no);
			
			rs = pstmt.executeQuery();
			
			while(rs.next()){
				Long user_no = rs.getLong(1);
				String title = rs.getString(2);
				int hit = rs.getInt(3);
				String reg_date = rs.getString(4);
				int depth = rs.getInt(5);
				int o_no = rs.getInt(6);
				int g_no = rs.getInt(7);
				String contents = rs.getString(8);
				
				
				BoardVo vo= new BoardVo();
				vo.setUser_no(user_no);
				vo.setTitle(title);
				vo.setHit(hit);
				vo.setReg_date(reg_date);
				vo.setDepth(depth);
				vo.setO_no(o_no);
				vo.setG_no(g_no);
				vo.setContents(contents);
				
				
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
	
	//게시물 작성
	public Boolean insert(BoardVo vo) {
		Boolean result = false;

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "select ifnull(max(g_no)+1,1) from board";
			pstmt = connection.prepareStatement(sql);
			rs = pstmt.executeQuery();
			int g_no_get = 0;
			while(rs.next()){
				g_no_get = rs.getInt(1);	
			}

			String sql1 = "insert into board values(null, ?, ?, 0, now(), ?, 1, 0, ?, true)";
			pstmt = connection.prepareStatement(sql1);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, g_no_get);	
			pstmt.setLong(4, vo.getUser_no());
			
			int count = pstmt.executeUpdate();
			result = (count == 1);


		} catch (SQLException e) {
			e.printStackTrace();
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

		return result;		
	}

	//게시물 내용 보여주기
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
	
	public boolean modify(BoardVo vo) {
		Boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		try {
			connection = getConnection();

			String sql = "update board set title = ?, contents = ? where no = ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setLong(3, vo.getNo());
			
			int count = pstmt.executeUpdate();
			result = (count == 1);
			
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				if (pstmt != null) {
					pstmt.close();
				}
				if (connection != null) {
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

			String sql =" update board set status=false where no = ?";

			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, no);

			pstmt.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
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
	
	//리플남기는 메소드 추가 reply
	public boolean reply(BoardVo vo) {
		Boolean result = false;
		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "update board set o_no = o_no+1 where g_no = ? and o_no >= ?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setInt(1, vo.getG_no());
			pstmt.setInt(2, vo.getO_no());
			pstmt.executeUpdate();

			String sql1 = "insert into board values(null, ?, ?, 0, now(), ?, ?, ?, ?, true)";
			pstmt = connection.prepareStatement(sql1);
			pstmt.setString(1, vo.getTitle());
			pstmt.setString(2, vo.getContents());
			pstmt.setInt(3, vo.getG_no());	
			pstmt.setInt(4, vo.getO_no());
			pstmt.setInt(5, vo.getDepth());
			pstmt.setLong(6, vo.getUser_no());


			int count = pstmt.executeUpdate();
			result = (count == 1);


		} catch (SQLException e) {
			e.printStackTrace();
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

		return result;	

	}
	
	public boolean visit(Long no) {
		Boolean result = false;

		Connection connection = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			connection = getConnection();

			String sql = "update board set hit = hit+1 where no=?";
			pstmt = connection.prepareStatement(sql);
			pstmt.setLong(1, no);
			int count = pstmt.executeUpdate();

			result = (count == 1);


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

		return result;	

	}

}
