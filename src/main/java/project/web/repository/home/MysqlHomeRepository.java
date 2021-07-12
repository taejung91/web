package project.web.repository.home;

import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Repository
public class MysqlHomeRepository implements HomeRepository {

    private final DataSource ds;

    public MysqlHomeRepository(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public List<Notice> findByNoticeAll(Notice params) {

        String sql = "select * from notice ORDER BY indate DESC limit ?, ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Notice> notices = new ArrayList<>();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, params.getPaginationInfo().getFirstRecordIndex());
            pstmt.setInt(2, params.getRecordsPerPage());

            rs = pstmt.executeQuery();

            while (rs.next()) {

                Notice notice = new Notice();

                notice.setNum(rs.getInt("num"));
                notice.setTitle(rs.getString("title"));
                notice.setContent(rs.getString("content"));
                notice.setInquiry(rs.getInt("inquiry"));
                notice.setWriter(rs.getString("writer"));
                notice.setImage(rs.getString("image"));
                notice.setIndate(rs.getDate("indate"));

                notices.add(notice);
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return notices;
    }

    @Override
    public Notice findByNotice(int num) {
        String sql = "select * from notice where num=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Notice notice = new Notice();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();

            if (rs.next()) {

                notice.setNum(rs.getInt("num"));
                notice.setTitle(rs.getString("title"));
                notice.setContent(rs.getString("content"));
                notice.setInquiry(rs.getInt("inquiry"));
                notice.setWriter(rs.getString("writer"));
                notice.setImage(rs.getString("image"));
                notice.setIndate(rs.getDate("indate"));
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return notice;
    }

    @Override
    public void updateInquiry(String name, int num) {

        String sql = "update " + name + " set inquiry = inquiry+1 where num=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        if (name == "qnaReply") {
            sql = "update qna set reply_inquiry = reply_inquiry+1 where num=?";
        }


        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);

            pstmt.executeUpdate();

        } catch (Exception e) {
           throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
    }


    @Override
    public List<Qna> findByQnaAll(Qna qna) {

        String sql = "select * from qna ORDER BY indate DESC limit ?, ?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Qna> qnaList = new ArrayList<>();
        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
         //   pstmt.setInt(1, qna.getStartPage());
            pstmt.setInt(1, qna.getPaginationInfo().getFirstRecordIndex());
            pstmt.setInt(2, qna.getRecordsPerPage());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Qna qnaFind = new Qna();

                qnaFind.setNum(rs.getInt("num"));
                qnaFind.setPw(rs.getString("pw"));
                qnaFind.setTitle(rs.getString("title"));
                qnaFind.setContent(rs.getString("content"));
                qnaFind.setInquiry(rs.getInt("inquiry"));
                qnaFind.setWriter(rs.getString("writer"));
                qnaFind.setImage(rs.getString("image"));
                qnaFind.setIndate(rs.getDate("indate"));
                qnaFind.setReply(rs.getString("reply"));
                qnaFind.setReply_inquiry(rs.getInt("reply_inquiry"));
                qnaFind.setReply_indate(rs.getDate("reply_indate"));
                qnaList.add(qnaFind);
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return qnaList;
    }

    @Override
    public int selectBoardTotalCount(String table) {

        String sql = "select count(*) as count from " + table + "";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()){

                count = rs.getInt("count");
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return count;
    }

    @Override
    public int selectBoardSearchTotalCount(String table, String searchType, String searchKeyword) {

        String sql = "select count(*) as count from " + table + " where " + searchType + " like '%" + searchKeyword + "%'";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        int count = 0;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            rs = pstmt.executeQuery();

            if (rs.next()){

                count = rs.getInt("count");
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return count;
    }

    @Override
    public List<Qna> findByQna(Qna params, String searchType, String searchKeyword) {

        String sql = "select * from qna where " + searchType + " like '%" + searchKeyword + "%' ORDER BY indate DESC limit ?,?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Qna> qnaList = new ArrayList<>();


        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, params.getPaginationInfo().getFirstRecordIndex());
            pstmt.setInt(2, params.getRecordsPerPage());

            rs = pstmt.executeQuery();

            while (rs.next()) {
                Qna qna = new Qna();

                qna.setNum(rs.getInt("num"));
                qna.setPw(rs.getString("pw"));
                qna.setTitle(rs.getString("title"));
                qna.setContent(rs.getString("content"));
                qna.setInquiry(rs.getInt("inquiry"));
                qna.setWriter(rs.getString("writer"));
                qna.setImage(rs.getString("image"));
                qna.setIndate(rs.getDate("indate"));
                qna.setReply(rs.getString("reply"));
                qna.setReply_inquiry(rs.getInt("reply_inquiry"));
                qna.setReply_indate(rs.getDate("reply_indate"));
                qnaList.add(qna);
            }

        } catch (Exception e) {
           throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return qnaList;
    }

    @Override
    public Qna findByQna(int num) {

        String sql = "select * from qna where num=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Qna qna = new Qna();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();

            if (rs.next()) {

                qna.setNum(rs.getInt("num"));
                qna.setPw(rs.getString("pw"));
                qna.setTitle(rs.getString("title"));
                qna.setContent(rs.getString("content"));
                qna.setInquiry(rs.getInt("inquiry"));
                qna.setWriter(rs.getString("writer"));
                qna.setImage(rs.getString("image"));
                qna.setIndate(rs.getDate("indate"));
                qna.setReply(rs.getString("reply"));
                qna.setReply_indate(rs.getDate("reply_indate"));
                qna.setReply_inquiry(rs.getInt("reply_inquiry"));


            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return qna;
    }

    @Override
    public Qna saveQna(Qna qna) {

        String sql = "insert into qna(pw, title, content, writer, image, indate) values(?, ?, ?, ?, ?, now())";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, qna.getPw());
            pstmt.setString(2, qna.getTitle());
            pstmt.setString(3, qna.getContent());
            pstmt.setString(4, qna.getWriter());
            pstmt.setString(5, qna.getImage());

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }

        return qna;
    }

    @Override
    public void deleteQna(int num) {

        String sql = "delete from qna where num=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);

            pstmt.executeUpdate();


        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }

    }


    private Connection getConnection() {
        return DataSourceUtils.getConnection(ds);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (pstmt != null) {
                pstmt.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        try {
            if (conn != null) {
                close(conn);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void close(Connection conn) throws SQLException {
        DataSourceUtils.releaseConnection(conn, ds);
    }


}
