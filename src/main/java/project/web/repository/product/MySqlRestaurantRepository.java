package project.web.repository.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.stereotype.Repository;
import project.web.repository.home.Notice;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Repository
public class MySqlRestaurantRepository implements RestaurantRepository{

    private final DataSource ds;

    public MySqlRestaurantRepository(DataSource ds) {
        this.ds = ds;
    }


    @Override
    public List<Restaurant> selectRestaurantAll(Restaurant params) {

        String sql = "select * from restaurant ORDER BY indate DESC limit ?, ? ";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, params.getPaginationInfo().getFirstRecordIndex());
            pstmt.setInt(2, params.getRecordsPerPage());


            rs = pstmt.executeQuery();

            while (rs.next()) {

                Restaurant restaurant = new Restaurant();

                restaurant.setNum(rs.getInt("num"));
                restaurant.setRes_name(rs.getString("res_name"));
                restaurant.setKind(rs.getString("kind"));
                restaurant.setPrice1(rs.getInt("price1"));
                restaurant.setPrice2(rs.getInt("price2"));
                restaurant.setPrice3(rs.getInt("price3"));
                restaurant.setAddr(rs.getString("addr"));
                restaurant.setTel(restaurant.setTelNum(rs.getString("tel")));
                restaurant.setContent(rs.getString("content"));
                restaurant.setImage(rs.getString("image"));
                restaurant.setBestyn(rs.getString("bestyn"));
                restaurant.setIndate(rs.getDate("indate"));

                restaurants.add(restaurant);
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return restaurants;
    }

    @Override
    public Restaurant selectRestaurant(int num) {
        String sql = "select * from restaurant where num=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Restaurant restaurant = new Restaurant();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();

            if (rs.next()) {

                restaurant.setNum(rs.getInt("num"));
                restaurant.setRes_name(rs.getString("res_name"));
                restaurant.setKind(rs.getString("kind"));
                restaurant.setPrice1(rs.getInt("price1"));
                restaurant.setPrice2(rs.getInt("price2"));
                restaurant.setPrice3(rs.getInt("price3"));
                restaurant.setAddr(rs.getString("addr"));
                restaurant.setTel(restaurant.setTelNum(rs.getString("tel")));
                restaurant.setContent(rs.getString("content"));
                restaurant.setImage(rs.getString("image"));
                restaurant.setBestyn(rs.getString("bestyn"));
                restaurant.setIndate(rs.getDate("indate"));
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return restaurant;
    }

    @Override
    public List<Restaurant> selectRestaurant(Restaurant params, String keyword) {
      //  String sql = "select * from restaurant where res_name or addr like '%" +Keyword+ "%' ORDER BY indate DESC limit ?,?";
        String sql = "select * from restaurant where res_name like '%" + keyword + "%' or addr like '%" + keyword + "%' ORDER BY indate DESC limit ?,?";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Restaurant> restaurants = new ArrayList<>();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, params.getPaginationInfo().getFirstRecordIndex());
            pstmt.setInt(2, params.getRecordsPerPage());
            rs = pstmt.executeQuery();

            while (rs.next()) {

                Restaurant restaurant = new Restaurant();

                restaurant.setNum(rs.getInt("num"));
                restaurant.setRes_name(rs.getString("res_name"));
                restaurant.setKind(rs.getString("kind"));
                restaurant.setPrice1(rs.getInt("price1"));
                restaurant.setPrice2(rs.getInt("price2"));
                restaurant.setPrice3(rs.getInt("price3"));
                restaurant.setAddr(rs.getString("addr"));
                restaurant.setTel(restaurant.setTelNum(rs.getString("tel")));
                restaurant.setContent(rs.getString("content"));
                restaurant.setImage(rs.getString("image"));
                restaurant.setBestyn(rs.getString("bestyn"));
                restaurant.setIndate(rs.getDate("indate"));

                restaurants.add(restaurant);
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return restaurants;
    }

    @Override
    public List<Restaurant> selectRestaurantDate(int num) {
        String sql = "select * from res_view where num=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Restaurant> restaurantDate = new ArrayList<>();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                Restaurant restaurant = new Restaurant();

                restaurant.setNum(rs.getInt("num"));
                restaurant.setRes_name(rs.getString("res_name"));
                restaurant.setKind(rs.getString("kind"));
                restaurant.setPrice1(rs.getInt("price1"));
                restaurant.setPrice2(rs.getInt("price2"));
                restaurant.setPrice3(rs.getInt("price3"));
                restaurant.setAddr(rs.getString("addr"));
                restaurant.setTel(restaurant.setTelNum(rs.getString("tel")));
                restaurant.setContent(rs.getString("content"));
                restaurant.setImage(rs.getString("image"));
                restaurant.setBestyn(rs.getString("bestyn"));
                restaurant.setIndate(rs.getDate("indate"));
                restaurant.setDate(rs.getDate("date"));
                restaurant.setTime(rs.getTime("time"));
                restaurant.setQtt(rs.getInt("qtt"));
             /*   restaurant.setYear(rs.getInt("year"));
                restaurant.setMonth(rs.getInt("month"));
                restaurant.setDay(rs.getInt("day"));*/

                restaurantDate.add(restaurant);

            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return restaurantDate;
    }

    @Override
    public List<Restaurant> selectRestaurantTime(int num, Date date) {
        String sql = "select * from res_view where num=? and date= ? ORDER BY time ASC";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        ArrayList<Restaurant> restaurantTimes = new ArrayList<>();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            pstmt.setDate(2, date);
            rs = pstmt.executeQuery();

            while (rs.next()) {

                Restaurant restaurant = new Restaurant();

                restaurant.setNum(rs.getInt("num"));
                restaurant.setDate(rs.getDate("date"));
                restaurant.setTime(rs.getTime("time"));
                restaurant.setQtt(rs.getInt("qtt"));

                restaurantTimes.add(restaurant);

            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return restaurantTimes;
    }

    @Override
    public Restaurant selectRestaurantQtt(int num, Date date, Time time) {

        String sql = "select * from res_view where num=? and date=? and time=?";
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;
        Restaurant restaurant = new Restaurant();

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            pstmt.setDate(2, date);
            pstmt.setTime(3, time);
            rs = pstmt.executeQuery();

            if (rs.next()) {

                restaurant.setNum(rs.getInt("num"));
                restaurant.setRes_name(rs.getString("res_name"));
                restaurant.setKind(rs.getString("kind"));
                restaurant.setPrice1(rs.getInt("price1"));
                restaurant.setPrice2(rs.getInt("price2"));
                restaurant.setPrice3(rs.getInt("price3"));
                restaurant.setAddr(rs.getString("addr"));
                restaurant.setTel(restaurant.setTelNum(rs.getString("tel")));
                restaurant.setContent(rs.getString("content"));
                restaurant.setImage(rs.getString("image"));
                restaurant.setBestyn(rs.getString("bestyn"));
                restaurant.setIndate(rs.getDate("indate"));
                restaurant.setDate(rs.getDate("date"));
                restaurant.setTime(rs.getTime("time"));
                restaurant.setQtt(rs.getInt("qtt"));
            }

        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }
        return restaurant;
    }

    @Override
    public void updateRestaurantQtt(int num,Time time, int selectQtt, String type) {

        String sql = "";
        if(type.equals("subtract")) {
            sql = "update time_qtt set qtt= qtt - " + selectQtt + " where num_res=? and time=?";
        }else if(type.equals("plus")){
            sql = "update time_qtt set qtt= qtt + " + selectQtt + " where num_res=? and time=?";

        }
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setInt(1, num);
            pstmt.setTime(2, time);
            pstmt.executeUpdate();


        } catch (Exception e) {
            throw new IllegalStateException();
        } finally {
            close(conn, pstmt, rs);
        }

    }

    @Override
    public int selectBoardSearchTotalCount(String table, String keyword) {
        String sql = "select count(*) as count from " + table + " where res_name like '%" + keyword + "%' or addr like '%" + keyword + "%'";
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
    public Restaurant insertRestaurant(Restaurant restaurant) {
        String sql = "insert into restaurant(res_name, kind, price1, addr, tel, content, image) values(?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;

        try {
            conn = getConnection();
            pstmt = conn.prepareStatement(sql);
            pstmt.setString(1, restaurant.getRes_name());
            pstmt.setString(2, restaurant.getKind());
            pstmt.setInt(3, restaurant.getPrice1());
            pstmt.setString(4, restaurant.getAddr());
            pstmt.setString(5, restaurant.getTel());
            pstmt.setString(6, restaurant.getContent());
            pstmt.setString(7, restaurant.getImage());

            pstmt.executeUpdate();

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            close(conn, pstmt, rs);
        }

        return restaurant;
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
