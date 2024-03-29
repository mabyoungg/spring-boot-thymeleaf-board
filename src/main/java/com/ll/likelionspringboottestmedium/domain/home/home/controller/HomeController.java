package com.ll.likelionspringboottestmedium.domain.home.home.controller;

import com.ll.likelionspringboottestmedium.domain.post.post.entity.Post;
import com.ll.likelionspringboottestmedium.domain.post.post.service.PostService;
import com.ll.likelionspringboottestmedium.domain.post.postLike.entity.PostLike;
import com.ll.likelionspringboottestmedium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class HomeController {
    private final Rq rq;
    private final PostService postService;

    @GetMapping("/")
    public String showMain() {
        List<Post> posts = postService.findTop30ByPublishedOrderByIdDesc(true);

        if (rq.isLogin()) {
            postService.loadLikeMap(posts, rq.getMember());
        }

        rq.attr("posts", posts);

        return "domain/home/home/main";
    }
}
