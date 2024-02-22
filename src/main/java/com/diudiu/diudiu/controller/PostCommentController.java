package com.diudiu.diudiu.controller;



import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.diudiu.diudiu.entity.PostComment;
import com.diudiu.diudiu.service.PostCommentService;
import org.springframework.web.bind.annotation.*;
import javax.annotation.Resource;
import java.io.Serializable;
import java.util.List;

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
@RequestMapping("/postComment")
@CrossOrigin
public class PostCommentController{
    @Resource
    private PostCommentService postCommentService;
    @GetMapping("")
    public List<PostComment> getAll(){
        return postCommentService.list();
    }
}

