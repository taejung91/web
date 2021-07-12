package project.web.repository.members;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

@Slf4j
@Repository
public class MysqlMemberRepository implements MemberRepository {

    private final DataSource ds;

    public MysqlMemberRepository(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Member save(Member member) {

        String sql = "insert into member(id, pw, name, birth, phone, addr, indate) values(?, ?, ?, ?, ?, ?, now())";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, member.getId());
            pstmt.setString(2, member.getPw());
            pstmt.setString(3, member.getName());
            pstmt.setString(4, member.getBirth());
            pstmt.setString(5, member.getPhone());
            pstmt.setString(6, member.getAddr());

            pstmt.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }

        return member;
    }

    @Override
    public Member checkId(String id) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Member member = new Member();

        String sql = "select id from member where id=?";

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if(rs.next()){


                member.setId(rs.getString("id"));

                return member;
            }

        }catch (Exception e){
            throw new IllegalStateException();
        }finally {
            close(conn, pstmt, rs);
        }

        return null;
    }

    @Override
    public Member loginCheck(String id, String pw) {

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Member member = new Member();

        String sql = "select * from member where id=? and pw=?";

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, pw);
            rs = pstmt.executeQuery();

            if(rs.next()){
                member.setId(rs.getString("id"));
                member.setName(rs.getString("name"));

                return member;
            }
            return null;

        }catch (Exception e){
            throw new IllegalStateException();
        }finally {
            close(conn, pstmt, rs);
        }

    }

    public Member findById(String name, String phone) {

        String sql = "select * from member where name=? and phone=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, name);
            pstmt.setString(2, phone);
            rs = pstmt.executeQuery();

            if(rs.next()){
                Member member = new Member();
                member.setId(rs.getString("id"));
                return member;
            }

        }catch (Exception e){
            throw new IllegalStateException();
        }finally {
            close(conn,pstmt,rs);
        }

        return null;
    }

    @Override
    public Member findByPw(String id, String name) {

        String sql = "select * from member where id=? and name=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            rs = pstmt.executeQuery();

            if(rs.next()){
                Member member = new Member();
                member.setPw(rs.getString("pw"));
                return member;
            }

        }catch (Exception e){
            throw new IllegalStateException();
        }finally {
            close(conn,pstmt,rs);
        }

        return null;
    }

    @Override
    public Member findByMember(String id) {
        String sql = "select * from member where id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);
            rs = pstmt.executeQuery();

            if(rs.next()){
                Member member = new Member();
                member.setId(rs.getString("id"));
                member.setPw(rs.getString("pw"));
                member.setName(rs.getString("name"));
                member.setBirth(rs.getString("birth"));
                member.setPhone(rs.getString("phone"));
                member.setAddr(rs.getString("addr"));
                member.setIndate(rs.getTimestamp("indate"));
                return member;
            }

        }catch (Exception e){
            throw new IllegalStateException();
        }finally {
            close(conn,pstmt,rs);
        }

        return null;
    }

    @Override
    public void update(String id, Member infoEdit) {

        String sql = "update member set pw=?, name=?, birth=?, phone=? where id=? ";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, infoEdit.getPw());
            pstmt.setString(2, infoEdit.getName());
            pstmt.setString(3, infoEdit.getBirth());
            pstmt.setString(4, infoEdit.getPhone());
            pstmt.setString(5, id);

            pstmt.executeUpdate();

        }catch (Exception e){
            throw new IllegalStateException();
        }finally {
            close(conn,pstmt,rs);
        }

    }

    @Override
    public void delete(String id) {
        String sql = "delete from member where id=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try{
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            pstmt.executeUpdate();


        }catch (Exception e){
            throw new IllegalStateException();
        }finally {
            close(conn,pstmt,rs);
        }

    }


    private Connection getConnection() {
        return DataSourceUtils.getConnection(ds);
    }

    private void close(Connection conn, PreparedStatement pstmt, ResultSet rs)
    {
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
