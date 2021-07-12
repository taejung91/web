package project.web.admin;

import lombok.AllArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;
import project.web.repository.product.Restaurant;
import project.web.repository.product.RestaurantRepository;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.UUID;

@Service
@AllArgsConstructor
public class AdminServiceImpl implements AdminService{

    private final RestaurantRepository restaurantRepository;

    /*
     식당 등록
     */
    @Override
    public Restaurant restaurantAdd(Restaurant restaurant) {

        Restaurant newRestaurant = restaurantRepository.insertRestaurant(restaurant);

        return newRestaurant;

    }

    /*
     상품 대표이미지 업로드
     */
    @Override
    public String restaurantUpload(HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest multiFile) {

        PrintWriter printWriter = null;
        OutputStream out = null;
        MultipartFile file = multiFile.getFile("upload");
        String fileUrl = "";

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

                        /*printWriter = response.getWriter();
                        response.setContentType("text/html;charset=utf-8");
                        response.setCharacterEncoding("utf-8");
*/
                        fileUrl = request.getContextPath() + "/product/" + fileName;

                    } catch (IOException e) {

                    } finally {
                        if (out != null) {
                            try {
                                out.close();
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        if (printWriter != null) {
                            printWriter.close();
                        }
                    }
                }
            }
        }

        return fileUrl;
    }
}
