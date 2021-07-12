package project.web.repository.product;

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
public class MysqlReservationRepository implements ReservationRepository{

    private final DataSource ds;

    public MysqlReservationRepository(DataSource ds) {
        this.ds = ds;
    }

    @Override
    public Reservation insertReservation(Reservation reservation) {

        String sql = "insert into reservation(id, res_num, reserv_date, reserv_time, reserv_qtt, reserv_price, indate) values(?, ?, ?, ?, ?, ?, now())";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, reservation.getId());
            pstmt.setInt(2, reservation.getRes_num());
            pstmt.setDate(3, reservation.getReserv_date());
            pstmt.setTime(4, reservation.getReserv_time());
            pstmt.setInt(5, reservation.getReserv_qtt());
            pstmt.setInt(6, reservation.getReserv_price());

            pstmt.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }finally {
            close(conn, pstmt, rs);
        }

        return reservation;

    }

    @Override
    public List<Reservation> selectReservationAll(String id) {
        String sql = "select * from reserv_view where id=? ORDER BY reserv_num DESC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Reservation> reservations = new ArrayList<>();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, id);

            rs = pstmt.executeQuery();

            while (rs.next()) {

                Reservation reservation = new Reservation();

                reservation.setReserv_num(rs.getInt("reserv_num"));
                reservation.setId(rs.getString("id"));
                reservation.setName(rs.getString("name"));
                reservation.setPhone(reservation.setPhoneNum(rs.getString("phone")));
                reservation.setReserv_date(rs.getDate("reserv_date"));
                reservation.setReserv_time(rs.getTime("reserv_time"));
                reservation.setReserv_qtt(rs.getInt("reserv_qtt"));
                reservation.setReserv_price(rs.getInt("reserv_price"));
                reservation.setIndate(rs.getTimestamp("indate"));
                reservation.setRes_num(rs.getInt("res_num"));
                reservation.setRes_name(rs.getString("res_name"));
                reservation.setKind(rs.getString("kind"));
                reservation.setAddr(rs.getString("addr"));
                reservation.setTel(reservation.setPhoneNum(rs.getString("tel")));
                reservation.setResult(rs.getString("result"));

                reservations.add(reservation);
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return reservations;
    }

    @Override
    public Reservation selectReservation(int num) {
        String sql = "select * from reserv_view where reserv_num=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Reservation reservation = new Reservation();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);

            rs = pstmt.executeQuery();

            if (rs.next()) {

                reservation.setReserv_num(rs.getInt("reserv_num"));
                reservation.setId(rs.getString("id"));
                reservation.setName(rs.getString("name"));
                reservation.setPhone(reservation.setPhoneNum(rs.getString("phone")));
                reservation.setReserv_date(rs.getDate("reserv_date"));
                reservation.setReserv_time(rs.getTime("reserv_time"));
                reservation.setReserv_qtt(rs.getInt("reserv_qtt"));
                reservation.setReserv_price(rs.getInt("reserv_price"));
                reservation.setIndate(rs.getTimestamp("indate"));
                reservation.setRes_num(rs.getInt("res_num"));
                reservation.setRes_name(rs.getString("res_name"));
                reservation.setKind(rs.getString("kind"));
                reservation.setAddr(rs.getString("addr"));
                reservation.setTel(reservation.setPhoneNum(rs.getString("tel")));
                reservation.setResult(rs.getString("result"));

            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return reservation;
    }

    @Override
    public void deleteReservation(int reserv_num) {

        String sql = "delete from reservation where reserv_num=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, reserv_num);

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
