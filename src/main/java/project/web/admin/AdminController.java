package project.web.admin;

import com.google.gson.JsonObject;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import project.web.repository.product.Restaurant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.UUID;

@Controller
@Slf4j
@AllArgsConstructor
@SessionAttributes(value = "login_id")
public class AdminController {

    private final AdminService adminService;

    /*
     상품 등록폼
     */

    @GetMapping("/admin/product")
    public String productForm(HttpSession session, Model model) {

        String id = (String) session.getAttribute("login_id");

        if (id != null) {

            model.addAttribute("writer", id);
            return "admin/productForm";
        } else {

            return "redirect:/members/login";
        }
    }

    /*
     상품 등록
     */
    @PostMapping("/admin/product")
    public String productAdd(@ModelAttribute Restaurant restaurant, HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest multiFile) throws Exception {



        /*PrintWriter printWriter = null;
        OutputStream out = null;
        MultipartFile file = multiFile.getFile("upload");

        if (file != null) {
            if (file.getSize() > 0 && StringUtils.isNotBlank(file.getName())) {
                if (file.getContentType().toLowerCase().startsWith("image/")) {
                    try {
                        String fileName = file.getName();
                        byte[] bytes = file.getBytes();


                        String uploadPath = request.getServletContext().getRealPath("/product");
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

                        String fileUrl = request.getContextPath() + "/product/" + fileName;


                         restaurant.setImage(fileUrl);

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
        }*/

        String imagePath = adminService.restaurantUpload(request, response, multiFile);
        restaurant.setImage(imagePath);
        adminService.restaurantAdd(restaurant);

        return "redirect:/admin/product";
    }

    /*
      상품 이미지 업로드 ckediter
     */
    @RequestMapping(value = "/admin/imageUpload", method = RequestMethod.POST)
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


                        String uploadPath = request.getServletContext().getRealPath("/product/content");
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
                        String fileUrl = request.getContextPath() + "/product/content/" + fileName;

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
