package com.ll.likelionspringboottestmedium.domain.post.blog.controller;

import com.ll.likelionspringboottestmedium.domain.memeber.memeber.entity.Member;
import com.ll.likelionspringboottestmedium.domain.memeber.memeber.service.MemberService;
import com.ll.likelionspringboottestmedium.domain.post.post.entity.Post;
import com.ll.likelionspringboottestmedium.domain.post.post.service.PostService;
import com.ll.likelionspringboottestmedium.global.exceptions.GlobalException;
import com.ll.likelionspringboottestmedium.global.rq.Rq;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/b")
@RequiredArgsConstructor
public class BlogController {
    private final Rq rq;
    private final MemberService memberService;
    private final PostService postService;

    @GetMapping("/{username}")
    public String showList(
            @PathVariable String username,
            @RequestParam(defaultValue = "") String kw,
            @RequestParam(defaultValue = "1") int page
    ) {
        List<Sort.Order> sorts = new ArrayList<>();
        sorts.add(Sort.Order.desc("id"));
        Pageable pageable = PageRequest.of(page - 1, 10, Sort.by(sorts));

        Member blogMember = memberService.findByUsername(username).orElseThrow(() -> new GlobalException("404-1", "존재하지 않는 사용자입니다."));

        Page<Post> postPage = postService.search(blogMember, true, kw, pageable);

        rq.attr("username", username);
        rq.attr("postPage", postPage);
        rq.attr("page", page);

        return "domain/post/blog/list";
    }

    @GetMapping("/{username}/{id}")
    public String showList(
            @PathVariable String username,
            @PathVariable long id
    ) {
        Member blogMember = memberService.findByUsername(username).orElseThrow(() -> new GlobalException("404-1", "존재하지 않는 사용자입니다."));
        Post post = postService.findById(id).orElseThrow(() -> new GlobalException("404-2", "존재하지 않는 글입니다."));

        rq.attr("post", post);

        return "domain/post/post/detail";
    }
}
