package com.linkedbook.controller;

import com.linkedbook.dto.follow.FollowInput;
import com.linkedbook.dto.follow.FollowOutput;
import com.linkedbook.response.Response;
import com.linkedbook.service.FollowService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/follow")
@AllArgsConstructor
public class FollowController {

    private final FollowService followService;

    /**
     * 팔로우(follower / following) 유저 정보 API
     * [GET] /follow/{info}
     * @param info follower: 유저를 팔로우하는 타 유저 정보를 원하는 상태 / following: 유저가 팔로우하는 타 유저 정보를 원하는 상태
     * @return Response<List<FollowOutput>>
     */
    // Body
    @ResponseBody
    @GetMapping("/{info}")
    public Response<List<FollowOutput>> getFollowList(@PathVariable("info") String info) {
        System.out.println("[GET] /follow/" + info);
        return followService.getFollowList(info);
    }
}