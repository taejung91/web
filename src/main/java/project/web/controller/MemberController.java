package project.web.controller;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.web.repository.members.Member;
import project.web.repository.product.Reservation;
import project.web.service.members.MemberService;
import project.web.service.product.RestaurantService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.List;

@Slf4j
@Controller
@SessionAttributes(value = "login_id")
@RequestMapping("/members")
@AllArgsConstructor
public class MemberController {

    private final MemberService memberService;
    private final RestaurantService restaurantService;


    @GetMapping("/new")
    public String createForm() {
        return "members/createForm";
    }

    @PostMapping("/new")
    public String createMember(@ModelAttribute Member member) {

        memberService.join(member);

        return "redirect:/";
    }

    @ResponseBody
    @GetMapping("/checkId")
    public int idCheck(@ModelAttribute Member member) {

        int result = memberService.validateDuplicateMember(member);

        return result;

    }

    @GetMapping("/login")
    public String loginForm() {
        return "members/loginForm";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute Member member, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        Member loginMember = memberService.login(member);

        if (loginMember != null) {

            session.setAttribute("login_id", loginMember.getId());

            return "redirect:/";
        } else {

            redirectAttributes.addAttribute("status", false);

            return "redirect:/members/login";
        }

    }

    @GetMapping("/logout")
    public String logOut(HttpServletRequest request, SessionStatus sessionStatus) {

        sessionStatus.setComplete();

        return "redirect:/";
    }

    @GetMapping("/searchId")
    public String searchIdForm() {
        return "members/searchIdForm";
    }

    @PostMapping("/searchId")
    public String searchId(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {

        Member searchMember = memberService.searchId(member);
        if (searchMember != null) {
            member.setId(searchMember.getId());
            return "members/searchSuccess";
        } else {

            redirectAttributes.addAttribute("status", false);

            return "redirect:/members/searchId";
        }
    }

    @GetMapping("/searchPw")
    public String searchPwForm() {
        return "members/searchPwForm";
    }

    @PostMapping("/searchPw")
    public String searchPw(@ModelAttribute Member member, RedirectAttributes redirectAttributes) {

        Member searchMember = memberService.searchPw(member);
        if (searchMember != null) {
            member.setPw(searchMember.getPw());
            return "members/searchSuccess";
        } else {
            redirectAttributes.addAttribute("status", false);
            return "redirect:/members/searchPw";
        }
    }

    @GetMapping("/{id}")
    public String memberInfo(@PathVariable String id, Model model, HttpServletRequest request) throws Exception {

        HttpSession session = request.getSession();
        String login_id = (String) session.getAttribute("login_id");
        if (login_id != null) {
            Member memberInfo = memberService.memberInfo(id);
            memberInfo.setPhone(memberInfo.setPhoneNum(memberInfo.getPhone()));
            model.addAttribute("memberInfo", memberInfo);

            return "members/memberInfo";
        }

        return "redirect:/members/login";

    }

    @GetMapping("/{id}/edit")
    public String memberEditForm(@PathVariable String id, Model model) {

        Member memberInfo = memberService.memberInfo(id);
        model.addAttribute("memberInfo", memberInfo);
        return "members/memberEditForm";
    }

    @PostMapping("{id}/edit")
    public String memberEdit(@PathVariable String id, @ModelAttribute Member member) {

        memberService.memberInfoUpdate(id, member);

        return "redirect:/members/{id}";
    }

    @GetMapping("{id}/delete")
    public String memberDeleteForm(@PathVariable String id, Model model) {

        Member memberInfo = memberService.memberInfo(id);
        model.addAttribute("memberInfo", memberInfo);

        return "members/memberDeleteForm";
    }

    @PostMapping("{id}/delete")
    public String memberDelete(@PathVariable String id, @ModelAttribute Member member, SessionStatus sessionStatus, RedirectAttributes redirectAttributes) {

        Member memberInfo = memberService.memberInfo(id);

        if (memberInfo.getPw().equals(member.getPw())) {

            List<Reservation> reservations = memberService.reservationInfo(id);
            for (Reservation reservation :
                    reservations) {

                if(reservation.getResult().equals("1")){

                    restaurantService.plusRestaurantQtt(reservation.getRes_num(), reservation.getReserv_time(), reservation.getReserv_qtt());

                }
                restaurantService.reservationCancel(reservation.getReserv_num());
            }

            memberService.memberDelete(id);
            sessionStatus.setComplete();
            return "redirect:/";

        } else {
            redirectAttributes.addAttribute("status", false);
            return "redirect:/members/{id}/delete";
        }
    }

    /*
     예약상황
     */
    @GetMapping("/reservation")
    public String reservationInfo(Model model, HttpSession session) {

        String login_id = (String) session.getAttribute("login_id");

        if (login_id != null) {

            List<Reservation> reservations = memberService.reservationInfo(login_id);
            model.addAttribute("reservations", reservations);

            return "members/reservationInfo";
        }

        return "redirect:/members/login";

    }

    /*
     예약상황 상세정보
     */
    @GetMapping("/reservation/detail")
    public String reservationDetail(@RequestParam int num, Model model, HttpSession session) {

        String login_id = (String) session.getAttribute("login_id");

        if (login_id != null) {
            Reservation reservation = memberService.reservationDetail(num);
            model.addAttribute("reservation", reservation);

            return "members/reservationDetail";
        }

        return "redirect:/members/login";


    }


}
