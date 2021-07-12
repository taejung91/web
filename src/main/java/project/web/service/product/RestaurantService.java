package project.web.service.product;

import project.web.repository.product.Reservation;
import project.web.repository.product.Restaurant;

import java.sql.Date;
import java.sql.Time;
import java.util.List;

public interface RestaurantService {

    List<Restaurant> restaurantAll(Restaurant params);
    Restaurant restaurantDetail(int num);

    List<Restaurant> restaurantSearch(Restaurant params, String searchKeyword);

    List<Restaurant> restaurantDate(int num);
    List<Restaurant> restaurantTime(int num, Date date);
    Restaurant restaurantQtt(int num, Date date, Time time);

    void subtractRestaurantQtt(int num, Time time, int qtt);
    void plusRestaurantQtt(int num, Time time, int qtt);

    Reservation reservationAdd(Reservation reservation);
    void reservationCancel(int reserv_num);

}
