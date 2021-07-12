package project.web.service.product;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import project.web.paging.PaginationInfo;
import project.web.repository.home.HomeRepository;
import project.web.repository.product.Reservation;
import project.web.repository.product.ReservationRepository;
import project.web.repository.product.Restaurant;
import project.web.repository.product.RestaurantRepository;

import java.sql.Date;
import java.sql.Time;
import java.util.Collections;
import java.util.List;

@Service
@AllArgsConstructor
public class RestaurantServiceImpl implements RestaurantService {

    private final RestaurantRepository restaurantRepository;
    private final ReservationRepository reservationRepository;
    private final HomeRepository homeRepository;

    /*
     레스토랑 리스트
     */
    @Override
    public List<Restaurant> restaurantAll(Restaurant params) {

        List<Restaurant> restaurants = Collections.emptyList();

        String table = "restaurant";
        String type = "product";

        int TotalCount = homeRepository.selectBoardTotalCount(table);
        PaginationInfo paginationInfo = new PaginationInfo(params, type);
        paginationInfo.setTotalRecordCount(TotalCount);
        params.setPaginationInfo(paginationInfo);

        if (TotalCount > 0) {

            restaurants = restaurantRepository.selectRestaurantAll(params);
        }

        return restaurants;
    }

    /*
     레스토랑 상세보기
     */
    @Override
    public Restaurant restaurantDetail(int num) {

        Restaurant restaurant = restaurantRepository.selectRestaurant(num);

        return restaurant;
    }

    /*
     레스토랑 검색
     */
    @Override
    public List<Restaurant> restaurantSearch(Restaurant params, String searchKeyword) {

        List<Restaurant> restaurantList = Collections.emptyList();

        String table = "restaurant";

        int TotalCount = restaurantRepository.selectBoardSearchTotalCount(table, searchKeyword);

        PaginationInfo paginationInfo = new PaginationInfo(params);
        paginationInfo.setTotalRecordCount(TotalCount);

        params.setPaginationInfo(paginationInfo);

        if (TotalCount > 0) {

            restaurantList = restaurantRepository.selectRestaurant(params, searchKeyword);
        }
        return restaurantList;

    }

    /*
     레스토랑 예약날짜
     */
    @Override
    public List<Restaurant> restaurantDate(int num) {
        List<Restaurant> restaurantDate = restaurantRepository.selectRestaurantDate(num);
        return restaurantDate;
    }

    /*
     레스토랑 예약시간
     */
    @Override
    public List<Restaurant> restaurantTime(int num, Date date) {
        List<Restaurant> restaurantTime = restaurantRepository.selectRestaurantTime(num, date);
        return restaurantTime;
    }

    /*
     레스토랑 예약정보(날짜, 시간, 인원)
     */
    @Override
    public Restaurant restaurantQtt(int num, Date date, Time time) {

        Restaurant restaurant = restaurantRepository.selectRestaurantQtt(num, date, time);
        return restaurant;
    }

    /*
     레스토랑 인원 감소
     */
    @Override
    public void subtractRestaurantQtt(int num, Time time, int qtt) {

        String type = "subtract";
        restaurantRepository.updateRestaurantQtt(num, time, qtt, type);
    }

    /*
     레스토랑 인원 추가
     */
    @Override
    public void plusRestaurantQtt(int num, Time time, int qtt) {

        String type = "plus";
        restaurantRepository.updateRestaurantQtt(num, time, qtt, type);
    }


    /*
     예약 성공 -추가
     */
    @Override
    public Reservation reservationAdd(Reservation reservation) {

        Reservation reservationAdd = reservationRepository.insertReservation(reservation);

        return reservationAdd;
    }

    /*
     예약 취소
     */
    @Override
    public void reservationCancel(int reserv_num) {
        reservationRepository.deleteReservation(reserv_num);
    }
}

