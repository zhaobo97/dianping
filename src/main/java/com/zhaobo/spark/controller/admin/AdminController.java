package com.zhaobo.spark.controller.admin;

import com.zhaobo.spark.service.CategoryService;
import com.zhaobo.spark.service.SellerService;
import com.zhaobo.spark.service.ShopService;
import com.zhaobo.spark.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Controller("/admin/admin")
@RequestMapping("/admin/admin")
public class AdminController {

    public static final String CURRENT_ADMIN_SESSION = "currentAdminSession";

    @Value("${admin.email}")
    private String email;

    @Value("${admin.password}")
    private String adminPassword;

    @Autowired
    private UserService userService;

    @Autowired
    private SellerService sellerService;

    @Autowired
    private ShopService shopService;

    @Autowired
    private CategoryService categoryService;

    @RequestMapping("/index")
    public ModelAndView index(){
        ModelAndView  modelAndView =new ModelAndView("/admin/admin/index");
        modelAndView.addObject("userCount",userService.countAllUser());
        modelAndView.addObject("sellerCount",sellerService.countAllSeller());
        modelAndView.addObject("shopCount",shopService.countAllShop());
        modelAndView.addObject("categoryCount",categoryService.countAllCategory());

        modelAndView.addObject("CONTROLLER_NAME","admin");
        modelAndView.addObject("ACTION_NAME","index");
        return modelAndView;
    }

    @RequestMapping("/loginpage")
    public ModelAndView loginpage(){
        return new ModelAndView("/admin/admin/login");
    }

    @RequestMapping(value = "/login",method = RequestMethod.POST)
    public String login(@RequestParam(name="email")String email,
                        @RequestParam(name="password")String password,
                        HttpServletRequest httpServletRequest)  {
        if (StringUtils.isEmpty(email) || StringUtils.isEmpty(password)) {
            throw new RuntimeException("用户名密码不能为空");
        }
        if(email.equals(this.email) &&
                (DigestUtils.md5DigestAsHex(password.getBytes(StandardCharsets.UTF_8))).equals(this.adminPassword)){
            //登录成功
            httpServletRequest.getSession().setAttribute(CURRENT_ADMIN_SESSION,email);
            return "redirect:/admin/admin/index";
        }else{
            throw new RuntimeException("用户名密码错误");
        }

    }
}
