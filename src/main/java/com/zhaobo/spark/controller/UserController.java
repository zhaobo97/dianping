package com.zhaobo.spark.controller;

import com.zhaobo.spark.common.BusinessException;
import com.zhaobo.spark.common.ResponseEnum;
import com.zhaobo.spark.common.ResponseVo;
import com.zhaobo.spark.form.LoginForm;
import com.zhaobo.spark.form.RegisterFrom;
import com.zhaobo.spark.model.UserModel;
import com.zhaobo.spark.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Objects;

/**
 * @Auther: bo
 * @Date: 2023/12/4 16:49
 * @Description:
 */
@Controller
@RequestMapping("/user")
@Slf4j
public class UserController {

    public static final String CURRENT_USER_SESSION = "currentUserSession";

    @Autowired
    private UserService userService;

    @GetMapping("/test")
    public ModelAndView test(){
        return new ModelAndView("/test.html");
    }

    @GetMapping("/{userId}")
    @ResponseBody
    public ResponseVo<UserModel> getUserInfo (@PathVariable Integer userId ){
        UserModel user = userService.getUser(userId);
        if(user ==null){
            throw new RuntimeException("对象不存在");
        }
        return ResponseVo.success(user);
    }


    @PostMapping("/register")
    @ResponseBody
    public ResponseVo<UserModel> register(@Valid @RequestBody RegisterFrom registerFrom,
                                          BindingResult bindingResult){
        //参数校验
        if(bindingResult.hasErrors()){
            throw new BusinessException(ResponseEnum.PARAM_ERROR.getCode(),
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(registerFrom,userModel);
        return ResponseVo.success(userService.register(userModel));
    }

    @PostMapping("/login")
    @ResponseBody
    public ResponseVo<UserModel> login(@Valid @RequestBody LoginForm loginForm,
                                          BindingResult bindingResult,HttpServletRequest httpServletRequest){
        //参数校验
        if(bindingResult.hasErrors()){
            throw new BusinessException(ResponseEnum.PARAM_ERROR.getCode(),
                    Objects.requireNonNull(bindingResult.getFieldError()).getDefaultMessage());
        }
        UserModel userModel = new UserModel();
        BeanUtils.copyProperties(loginForm,userModel);
        UserModel userLogin =userService.login(userModel);;
        if(userLogin == null){
            return ResponseVo.error(ResponseEnum.PASSWORD_ERROR);
        }else{
            log.info("用户{}登录成功",userLogin.getNickName());
        }
        userLogin.setPassword("");
        httpServletRequest.getSession().setAttribute(CURRENT_USER_SESSION,userLogin);

        log.info(httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION).toString());
        return ResponseVo.success(userLogin);
    }

    @PostMapping("/logout")
    @ResponseBody
    public ResponseVo<String> logout(HttpServletRequest httpServletRequest){
        httpServletRequest.getSession().invalidate();
        return ResponseVo.success("退出成功！");
    }
    @PostMapping("/getcurrentuser")
    @ResponseBody
    public ResponseVo<UserModel> getCurrentUser(HttpServletRequest httpServletRequest){
        UserModel userModel = (UserModel) httpServletRequest.getSession().getAttribute(CURRENT_USER_SESSION);
        return ResponseVo.success(userModel);
    }
}
