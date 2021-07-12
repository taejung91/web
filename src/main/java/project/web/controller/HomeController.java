package project.web.controller;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import project.web.repository.home.Notice;
import project.web.repository.home.Qna;
import project.web.repository.members.MemberRepository;
import project.web.repository.product.Restaurant;
import project.web.service.home.HomeService;
import project.web.service.members.MemberService;
import project.web.service.product.RestaurantService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.List;
import java.util.Map;
import java.util.UUID;


@Controller
@Slf4j
@SessionAttributes(value = "login_id")
@AllArgsConstructor
public class HomeController {

    private final HomeService homeService;
    private final RestaurantService restaurantService;
    private final MemberService memberService;

    @GetMapping("/")
    public String home(@ModelAttribute("params") Restaurant params, Model model) {

        List<Restaurant> restaurants = restaurantService.restaurantAll(params);
        model.addAttribute("restaurants", restaurants);

        return "home";
    }

    @GetMapping("/basic/notice")
    public String notice(@ModelAttribute("params") Notice params, Model model) {


        List<Notice> notices = homeService.noticeAll(params);
        model.addAttribute("notices", notices);

        return "basic/noticeList";
    }

    @GetMapping("/basic/notice/{num}")
    public String noticeDetail(@ModelAttribute("params") Notice params, @PathVariable int num, Model model) {

        Notice notice = homeService.noticeDetail(num);
        String name = "notice";
        homeService.inquiryAdd(name, num);
        model.addAttribute("notice", notice);

        return "basic/noticeDetail";
    }

    @GetMapping("/basic/qna")
    public String qna(@ModelAttribute("params") Qna params, Model model) {

        if (params.getSearchKeyword() != null && params.getSearchKeyword() != "") {

            List<Qna> qnaList = homeService.qnaSearch(params, params.getSearchType(), params.getSearchKeyword());
            model.addAttribute("qnaList", qnaList);
        } else {

            List<Qna> qnaList = homeService.qnaAll(params);
            model.addAttribute("qnaList", qnaList);
        }

        return "basic/qnaList";
    }

    @GetMapping("/basic/qna/{num}")
    public String qnaDetail(@ModelAttribute("params") Qna params, @PathVariable int num, Model model, HttpServletRequest request) {

        Qna qna = homeService.qnaDetail(num);
        String name = "qna";
        homeService.inquiryAdd(name, num);
        model.addAttribute("qna", qna);

        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");

        if (qna.getWriter().equals(id)) {
            model.addAttribute("delete", "delete");
        }

        return "basic/qnaDetail";
    }

    @GetMapping("/basic/qna/{num}/confirm")
    public String qnaConfirmForm(@ModelAttribute("params") Qna params, @PathVariable int num, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");

        Qna qna = homeService.qnaDetail(num);
        model.addAttribute("qna", qna);

        if (qna.getWriter().equals(id)) {
            //   redirectAttributes.addAttribute("paging", qna.makeQueryString(qna.getCurrentPageNo()));
            return "redirect:/basic/qna/{num}" + params.makeQueryString(params.getCurrentPageNo());
        } else {

            return "basic/qnaConfirmForm";
        }
    }

    @PostMapping("/basic/qna/{num}/confirm")
    public String qnaConfirm(@PathVariable int num, @ModelAttribute Qna qna, Model model, RedirectAttributes redirectAttributes) {

        Qna qnaDetail = homeService.qnaDetail(num);

        if (qnaDetail.getPw().equals(qna.getPw())) {

            // redirectAttributes.addAttribute("paging", qna.makeQueryString(qna.getCurrentPageNo()));

            return "redirect:/basic/qna/{num}" + qna.makeQueryString(qna.getCurrentPageNo());
        } else {

            redirectAttributes.addAttribute("status", false);

            return "redirect:/basic/qna/{num}/confirm" + qna.makeQueryString(qna.getCurrentPageNo());
        }
    }

    @GetMapping("/basic/qna/{num}/reply")
    public String qnaReply(@ModelAttribute("params") Qna params, @PathVariable int num, Model model, HttpServletRequest request, RedirectAttributes redirectAttributes) {

        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");


        Qna qnaReply = homeService.qnaDetail(num);

        if (id == null) {

            return "redirect:/members/login";

        } else if (id.equals(qnaReply.getWriter())) {

            String name = "qnaReply";
            homeService.inquiryAdd(name, num);

            model.addAttribute("qna", qnaReply);
            model.addAttribute("reply", "reply");

            return "basic/qnaDetail";
        } else {

            redirectAttributes.addAttribute("status", false);

            return "redirect:/basic/qna" + params.makeQueryString(params.getCurrentPageNo());

        }
    }

    @GetMapping("/basic/qnaAdd")
    public String qnaAddForm(@ModelAttribute("params") Qna params, HttpServletRequest request, Model model) {

        HttpSession session = request.getSession();
        String id = (String) session.getAttribute("login_id");

        if (id != null) {

            model.addAttribute("writer", id);
            return "basic/qnaAddForm";
        } else {

            return "redirect:/members/login";
        }
    }

    @PostMapping("/basic/qnaAdd")
    public String qnaAdd(@ModelAttribute Qna qna) {

        homeService.qnaAdd(qna);

        return "redirect:/basic/qna";
    }

    @GetMapping("/basic/qnaDelete/{num}")
    public String qnaDelete(@ModelAttribute("params") Qna params, @PathVariable int num) {

        homeService.qnaDelete(num);

        return "redirect:/basic/qna" + params.makeQueryString(params.getCurrentPageNo());
    }

    @GetMapping("/basic/qnaDelete/{num}/confirm")
    public String qnaDeleteConfirm(@ModelAttribute("params") Qna params, @PathVariable int num, Model model) {

        Qna qnaDetail = homeService.qnaDetail(num);
        model.addAttribute("qna", qnaDetail);

        return "basic/qnaDeleteConfirmForm";
    }

    @PostMapping("/basic/qnaDelete/{num}/confirm")
    public String qnaConfirm(@PathVariable int num, @ModelAttribute Qna qna, RedirectAttributes redirectAttributes) {

        Qna qnaDetail = homeService.qnaDetail(num);

        if (qnaDetail.getPw().equals(qna.getPw())) {

            return "redirect:/basic/qnaDelete/{num}" + qna.makeQueryString(qna.getCurrentPageNo());
        } else {
            redirectAttributes.addAttribute("status", false);

            return "redirect:/basic/qnaDelete/{num}/confirm" + qna.makeQueryString(qna.getCurrentPageNo());
        }

    }

    /*
     이미지 업로드
     */
    @RequestMapping(value = "/basic/imageUpload", method = RequestMethod.POST)
    @ResponseBody
    public String imageUpload(HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest multiFile) throws Exception {

        JsonObject json = new JsonObject();
        PrintWriter printWriter = null;
        OutputStream out = null;
        MultipartFile file = multiFile.getFile("upload");

        if (file != null) {
            if (file.getSize() > 0 && StringUtils.isNotBlank(file.getName())) {
                if (file.getContentType().toLowerCase().startsWith("image/")) {
                    try {
                        String fileName = file.getName();
                        byte[] bytes = file.getBytes();


                        String uploadPath = request.getServletContext().getRealPath("/img");
                        File uploadFile = new File(uploadPath);

                        if (!uploadFile.exists()) {
                            uploadFile.mkdirs();
                        }
                        fileName = UUID.randomUUID().toString();
                        uploadPath = uploadPath + "/" + fileName;

                        out = new FileOutputStream(new File(uploadPath));
                        out.write(bytes);

                        printWriter = response.getWriter();
                        response.setContentType("text/html;charset=utf-8");
                        response.setCharacterEncoding("utf-8");
                        String fileUrl = request.getContextPath() + "/img/" + fileName;




                        //json 데이터 등록

                        json.addProperty("uploaded", 1);
                        json.addProperty("fileName", fileName);
                        json.addProperty("url", fileUrl);

                        printWriter.println(json);


                    } catch (IOException e) {

                    } finally {
                        if (out != null) {
                            out.close();
                        }
                        if (printWriter != null) {
                            printWriter.close();
                        }
                    }
                }
            }
        }
        return null;
    }
}
