package com.diudiu.diudiu.controller;


import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.Post;
import com.diudiu.diudiu.entity.PostComment;
import com.diudiu.diudiu.entity.PostUserFavor;
import com.diudiu.diudiu.entity.dto.PostCommentDto;
import com.diudiu.diudiu.entity.dto.PostDto;
import com.diudiu.diudiu.entity.dto.PostSonCommentDto;
import com.diudiu.diudiu.service.PostCommentService;
import com.diudiu.diudiu.service.PostService;
import com.diudiu.diudiu.service.PostUserFavorService;
import com.diudiu.diudiu.service.UserService;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/*
 * 给世界一点小小的二次元震撼
 *
 *  ██   ██         ███████   ████     ██
 * ░██  ██         ██░░░░░██ ░██░██   ░██
 * ░██ ██         ██     ░░██░██░░██  ░██
 * ░████    █████░██      ░██░██ ░░██ ░██
 * ░██░██  ░░░░░ ░██      ░██░██  ░░██░██
 * ░██░░██       ░░██     ██ ░██   ░░████
 * ░██ ░░██       ░░███████  ░██    ░░███
 * ░░   ░░         ░░░░░░░   ░░      ░░░
 *
 * @Author : DRME
 * @Create : com.sjhy.plugin.tool.TimeUtils@48d92da5
 */
@RestController
@RequestMapping("/post")
@CrossOrigin
public class PostController {
    @Resource
    private PostService postService;
    @Resource
    private UserService userService;
    @Resource
    private PostCommentService postCommentService;
    @Resource
    private PostUserFavorService postUserFavorService;

    @PostMapping("/getAllPost")
    public R getAllPost(@RequestBody Map<String, Object> map) {
        Integer userId = (Integer) map.get("userId");
        return R.ok(
                postService.list(
                        new LambdaQueryWrapper<Post>()
                                .eq(Post::getStatus, "正常")
                                .orderByDesc(Post::getCreateTime)
                        )
                        .stream()
                        .map(post -> {
                            PostDto dto = new PostDto();
                            BeanUtils.copyProperties(post, dto);
                            dto.setAva(userService.getById(post.getUserId()).getAva());
                            dto.setUserName(userService.getById(post.getUserId()).getName());
                            try {
                                PostUserFavor postUserFavor = postUserFavorService.getOne(
                                        new LambdaQueryWrapper<PostUserFavor>()
                                                .eq(PostUserFavor::getPostId, post.getId())
                                                .eq(PostUserFavor::getUserId, userId)
                                );
                                if (postUserFavor.getFavor().equals("是")) {
                                    dto.setIsFavor("是");
                                }
                            } catch (Exception e) {
                                dto.setIsFavor("否");
                            }
                            Integer commentCount = Math.toIntExact(postCommentService.count(
                                    new LambdaQueryWrapper<PostComment>()
                                            .eq(PostComment::getPostId, post.getId())
                                            .eq(PostComment::getStatus, "正常")
                                            .eq(PostComment::getResponse, "否")
                            ));
                            dto.setCommentCount(commentCount);
                            postService.updateById(post);
                            return dto;
                        })
        );
    }

    @PostMapping("/viewPost")
    public R viewPost(@RequestBody Map<String, Integer> map) {
        try {
            Integer postId = map.get("postId");
            Post post = postService.getById(postId);
            post.setViewCount(post.getViewCount() + 1);
            postService.updateById(post);
            return R.ok("浏览成功");
        } catch (Exception e) {
            return R.error("浏览失败");
        }
    }


    @PostMapping("/addComment")
    public R addComment(@RequestBody PostComment postComment) {
        try {
            postComment.setId((int) (postCommentService.count() + 1));
            postComment.setCreateTime(String.valueOf(System.currentTimeMillis()));
            postComment.setStatus("正常");
            postComment.setResponse("否");
            postComment.setResponsePostId(0);
            postCommentService.save(postComment);
            return R.ok("评论成功");
        } catch (Exception e) {
            return R.error("评论失败");
        }
    }

    // 回复评论
    @PostMapping("/responseComment")
    public R responseComment(@RequestBody PostComment postComment) {
        try {
            postComment.setId((int) (postCommentService.count() + 1));
            postComment.setCreateTime(String.valueOf(System.currentTimeMillis()));
            postComment.setStatus("正常");
            postComment.setResponse("是");
            postCommentService.save(postComment);
            return R.ok("回复成功");
        } catch (Exception e) {
            return R.error("回复失败");
        }
    }

    @PostMapping("/getPostComment")
    public R getPostComment(@RequestBody Map<String, Object> map) {
        Integer postId = (Integer) map.get("postId");
        Integer userId = (Integer) map.get("userId");
        return R.ok(
                postCommentService.list(
                                new LambdaQueryWrapper<PostComment>()
                                        .eq(PostComment::getPostId, postId)
                                        .eq(PostComment::getStatus, "正常")
                                        .eq(PostComment::getResponse, "否")
                        )
                        .stream()
                        .map(postComment -> {
                            PostCommentDto dto = new PostCommentDto();
                            BeanUtils.copyProperties(postComment, dto);
                            dto.setSonComment(
                                    postCommentService.list(new LambdaQueryWrapper<PostComment>()
                                                    .eq(PostComment::getResponse, "是")
                                                    .eq(PostComment::getResponsePostId, postComment.getId())
                                            ).stream()
                                            .map(postComment1 -> {
                                                        PostSonCommentDto sonDto = new PostSonCommentDto();
                                                        BeanUtils.copyProperties(postComment1, sonDto);
                                                        sonDto.setUserName(userService.getById(postComment1.getUserId()).getName());
                                                        sonDto.setAva(userService.getById(postComment1.getUserId()).getAva());
                                                        return sonDto;
                                                    }
                                            ).collect(Collectors.toList())
                            );
                            dto.setUserName(userService.getById(postComment.getUserId()).getName());
                            dto.setAva(userService.getById(postComment.getUserId()).getAva());
                            return dto;
                        })
        );
    }

    @PostMapping("/favorPost")
    public R favorPost(@RequestBody Map<String, Integer> map) {
        Integer postId = map.get("postId");
        Integer userId = map.get("userId");
        Post post = postService.getById(postId);
        Integer favorCount = post.getFavorCount();
        try {
            PostUserFavor postUserFavor = postUserFavorService.getOne(
                    new LambdaQueryWrapper<PostUserFavor>()
                            .eq(PostUserFavor::getPostId, postId)
                            .eq(PostUserFavor::getUserId, userId)
            );
            if (postUserFavor.getFavor().equals("是")) {
                postUserFavor.setFavor("否");
            } else {
                postUserFavor.setFavor("是");
            }
            postUserFavorService.updateById(postUserFavor);
            if (postUserFavor.getFavor().equals("是")) {
                post.setFavorCount(favorCount + 1);
                postService.updateById(post);
                return R.ok("点赞成功");
            } else {
                post.setFavorCount(favorCount - 1);
                postService.updateById(post);
                return R.ok("取消点赞成功");
            }
        } catch (Exception e) {
            PostUserFavor postUserFavor = new PostUserFavor();
            postUserFavor.setId((int) (postUserFavorService.count() + 1));
            postUserFavor.setPostId(postId);
            postUserFavor.setUserId(userId);
            postUserFavor.setFavor("是");
            postUserFavorService.save(postUserFavor);
            post.setFavorCount(favorCount + 1);
            postService.updateById(post);
            return R.ok("点赞成功");
        }
    }

    @PostMapping("/uploadPostImage")
    public R uploadPostImage(@RequestParam("file")MultipartFile file, HttpServletRequest request) {
        return postService.uploadImage(file, request);
    }
    // 上传帖子文本
    @PostMapping("/uploadPostContent")
    public R uploadPostContent(@RequestBody Map<String,Object> map){
        Integer userId = (Integer) map.get("userId");
        String content = (String) map.get("content");
        Post post = new Post();
        post.setId((int) (postService.count() + 1));
        post.setUserId(userId);
        post.setContent(content);
        post.setCreateTime(String.valueOf(System.currentTimeMillis()));
        post.setStatus("正常");
        post.setViewCount(0);
        post.setFavorCount(0);
        post.setCommentCount(0);
        post.setShareCount(0);
        postService.save(post);
        return R.ok(post.getId());
    }

    // 删除帖子
    @PostMapping("/delete")
    public R delete(@RequestBody Map<String, Integer> map) {
        Integer postId = map.get("postId");
        Post post = postService.getById(postId);
        post.setStatus("删除");
        postService.updateById(post);
        return R.ok("删除成功");
    }
}

