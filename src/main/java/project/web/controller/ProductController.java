package project.web.controller;

import com.siot.IamportRestClient.IamportClient;
import com.siot.IamportRestClient.exception.IamportResponseException;
import com.siot.IamportRestClient.response.IamportResponse;
import com.siot.IamportRestClient.response.Payment;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.web.repository.members.Member;
import project.web.repository.product.Reservation;
import project.web.repository.product.Restaurant;
import project.web.service.members.MemberService;
import project.web.service.product.RestaurantService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;

@Slf4j
@Controller
@SessionAttributes(value = "login_id")
public class ProductController {


    private final RestaurantService restaurantService;

    private final MemberService memberService;

    private final IamportClient api;

    public ProductController(RestaurantService restaurantService, MemberService memberService) {
        this.restaurantService = restaurantService;
        this.memberService = memberService;
        this.api = new IamportClient("9090544805619543", "akC0LiHHkCHS2cI2MndLXD8X1VMGzFeKZRKKassu49m7m598tmUq03o1ZaHyPWiOO3zYv1sAbPHxVRFo");
    }

    /*
     식당 리스트
     */
    @GetMapping("/product/list")
    public String restaurantList(@ModelAttribute("params") Restaurant params, Model model) {

        if(params.getSearchKeyword() != null && params.getSearchKeyword() != ""){

            List<Restaurant> restaurants = restaurantService.restaurantSearch(params, params.getSearchKeyword());
            model.addAttribute("restaurants", restaurants);

        }else {

            List<Restaurant> restaurants = restaurantService.restaurantAll(params);
            model.addAttribute("restaurants", restaurants);
        }

        return "product/restaurantList";
    }

    @GetMapping("/product/Detail/{num}")
    public String restaurantDetail(@PathVariable int num, Model model) {

        Restaurant restaurant = restaurantService.restaurantDetail(num);
        model.addAttribute("restaurant", restaurant);

        List<Restaurant> restaurantDate = restaurantService.restaurantDate(num);
        model.addAttribute("restaurantDate", restaurantDate);
        return "product/restaurantDetail";
    }

    /*
     날짜에 맞는 시간
     */
    @ResponseBody
    @GetMapping("/product/time")
    public List<Restaurant> timeSelect(@ModelAttribute Restaurant restaurant) {

        List<Restaurant> restaurantTimes = restaurantService.restaurantTime(restaurant.getNum(), restaurant.getDate());
        return restaurantTimes;
    }

    /*
    시간에 맞는 인원수
     */
    @ResponseBody
    @GetMapping("/product/qtt")
    public Restaurant qttSelect(@ModelAttribute Restaurant restaurant) {

        Restaurant restaurantQtt = restaurantService.restaurantQtt(restaurant.getNum(), restaurant.getDate(), restaurant.getTime());
        return restaurantQtt;

    }

    /*
     예약하기
     */
    @GetMapping("/product/reservation")
    public String reservationForm(@ModelAttribute Restaurant restaurant, Model model, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");

        if (id != null) {

            Member member = memberService.memberInfo(id);
            member.setPhone(member.setPhoneNum(member.getPhone()));
            model.addAttribute("member", member);

            Restaurant selectRestaurant = restaurantService.restaurantQtt(restaurant.getNum(), restaurant.getDate(), restaurant.getTime());
            model.addAttribute("restaurant", selectRestaurant);

            int qtt = restaurant.getQtt();
            model.addAttribute("qtt", qtt);

            return "product/reservationForm";
        } else {

            return "redirect:/members/login";

        }
    }

    /*
     아임포트 결제
     */
    @ResponseBody
    @PostMapping("/payments/complete/{imp_uid}")
    public int paymentByImpUid(@PathVariable String imp_uid, @ModelAttribute Reservation reservation, Model model, Locale locale, HttpSession session) throws IamportResponseException, IOException {

        IamportResponse<Payment> paymentIamportResponse = api.paymentByImpUid(imp_uid);
        
        Payment payment = paymentIamportResponse.getResponse();

        //결제 정보
        String status = payment.getStatus();
        int amount = Integer.parseInt(String.valueOf(payment.getAmount()));

        //결제 여부
        int result = 0;

        log.info(reservation.getReserv_price() + "/" + amount);

        if (status.equals("paid") && amount == reservation.getReserv_price()) {

            restaurantService.reservationAdd(reservation);
            restaurantService.subtractRestaurantQtt(reservation.getRes_num(), reservation.getReserv_time(), reservation.getReserv_qtt());

            result = 1;
        }

        return result;

    }

    /*
     예약 취소
     */
    @PostMapping("/product/reservation/cancel")
    public String reservationCancel(@ModelAttribute Reservation reservation, RedirectAttributes redirectAttributes){

        restaurantService.plusRestaurantQtt(reservation.getRes_num(), reservation.getReserv_time(), reservation.getReserv_qtt());
        restaurantService.reservationCancel(reservation.getReserv_num());

        redirectAttributes.addAttribute("status", true);

        return "redirect:/members/reservation";
    }


}
