package project.web.repository.product;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface RestaurantRepository {

    List<Restaurant> selectRestaurantAll(Restaurant params);
    Restaurant selectRestaurant(int num);

    List<Restaurant> selectRestaurant(Restaurant params, String keyword);
    List<Restaurant> selectRestaurantDate(int num);
    List<Restaurant> selectRestaurantTime(int num, Date date);
    Restaurant selectRestaurantQtt(int num, Date date, Time time);

    void updateRestaurantQtt(int num, Time time, int qtt, String type);

    int selectBoardSearchTotalCount(String table, String keyword);

    Restaurant insertRestaurant(Restaurant restaurant);





}
