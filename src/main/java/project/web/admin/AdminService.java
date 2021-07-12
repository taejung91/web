package project.web.admin;

import org.springframework.web.multipart.MultipartHttpServletRequest;
import project.web.repository.product.Restaurant;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface AdminService {

    Restaurant restaurantAdd(Restaurant restaurant);

    String restaurantUpload(HttpServletRequest request, HttpServletResponse response, MultipartHttpServletRequest multiFile);
}
