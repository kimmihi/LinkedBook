package com.linkedbook.controller;

import com.linkedbook.dto.user.jwt.JwtOutput;
import com.linkedbook.dto.user.selectprofile.SelectProfileOutput;
import com.linkedbook.dto.user.signin.SignInInput;
import com.linkedbook.dto.user.signin.SignInOutput;
import com.linkedbook.dto.user.signup.SignUpOutput;
import com.linkedbook.dto.user.signup.SignUpInput;
import com.linkedbook.response.Response;
import com.linkedbook.service.JwtService;
import com.linkedbook.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import static com.linkedbook.response.ResponseStatus.*;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    @Autowired
    private JwtService jwtService;

    /**
     * 회원가입 API
     * [POST] /users/signup
     * @return Response<SignUpOutput>
     */
    // Body
    @ResponseBody
    @PostMapping("/signup")
    public Response<SignUpOutput> signUp(@RequestBody SignUpInput signUpInput) {
        System.out.println("[POST] /user/signup");
        return userService.signUp(signUpInput);
    }

    /**
     * 로그인 API
     * [POST] /users/signin
     * @return Response<SignInOutput>
     */
    // Body
    @ResponseBody
    @PostMapping("/signin")
    public Response<SignInOutput> signIn(@RequestBody SignInInput signInInput) {
        System.out.println("[POST] /user/signin");
        return userService.signIn(signInInput);
    }

    /**
     * 유저 프로필 조회 API
     * [GET] /users/:id/profile
     * @return Response<SelectProfileOutput>
     */
    // Path-variable
    @ResponseBody
    @GetMapping("/{id}/profile")
    public Response<SelectProfileOutput> selectProfile(@PathVariable("id") int userId) {
        System.out.println("[GET] /user/{id}/profile");
        return userService.selectProfile(userId);
    }

    @ResponseBody
    @PostMapping("/jwt")
    public Response<JwtOutput> jwt() {
        System.out.println("[POST] /user/jwt");
        int userId = jwtService.getUserId();
        if (userId == -1) return new Response<>(UNAUTHORIZED_TOKEN);
        if (userId == -2) return new Response<>(BAD_ACCESS_TOKEN_VALUE);
        if (userId == -3) return new Response<>(FORBIDDEN_USER_ID);
        JwtOutput jwtOutput = new JwtOutput(userId);
        return new Response<>(jwtOutput, SUCCESS_SIGN_IN);
    }
}
