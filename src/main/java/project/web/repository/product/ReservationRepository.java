package project.web.repository.product;

import java.util.List;

public interface ReservationRepository {

    Reservation insertReservation(Reservation reservation);

    List<Reservation> selectReservationAll(String id);

    Reservation selectReservation(int num);

    void deleteReservation(int reserv_num);



}
