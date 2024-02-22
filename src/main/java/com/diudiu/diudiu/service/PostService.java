package com.diudiu.diudiu.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.Post;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

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
@Service
public interface PostService extends IService<Post> {

    R uploadImage(MultipartFile file, HttpServletRequest request);
}

