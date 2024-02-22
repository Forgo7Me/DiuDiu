package com.diudiu.diudiu.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.diudiu.diudiu.common.R;
import com.diudiu.diudiu.entity.User;
import com.diudiu.diudiu.mapper.PostMapper;
import com.diudiu.diudiu.entity.Post;
import com.diudiu.diudiu.service.PostService;
import com.diudiu.diudiu.service.UserService;
import com.diudiu.diudiu.util.PathUtils;
import com.google.gson.Gson;
import com.qiniu.common.QiniuException;
import com.qiniu.http.Response;
import com.qiniu.storage.Configuration;
import com.qiniu.storage.Region;
import com.qiniu.storage.UploadManager;
import com.qiniu.storage.model.DefaultPutRet;
import com.qiniu.util.Auth;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.InputStream;

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
@Service("postService")
@Data
@ConfigurationProperties(prefix = "oss")
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {

    @Resource
    private UserService userService;
    private String accessKey;
    private String secretKey;
    private String bucket;
    private String link;
    @Override
    public R uploadImage(MultipartFile file, HttpServletRequest request) {
        if (file.isEmpty()) {
            return R.error("上传失败，请选择文件");
        }
        try {
            // 获取文件名
            String originalFilename = file.getOriginalFilename();
            String filePath = PathUtils.postFilePath(originalFilename);
            String url = uploadOss(file,filePath);
            Post post = this.getById(request.getHeader("postId"));
            post.setImg(url);
            this.updateById(post);
            return R.ok("文件上传成功：" + url);
        } catch (Exception e) {
            e.printStackTrace();
            return R.error("上传失败：" + e.getMessage());
        }
    }

    private String uploadOss(MultipartFile imgFile, String filePath){
        //构造一个带指定 Region 对象的配置类
        Configuration cfg = new Configuration(Region.autoRegion());
        //...其他参数参考类注释
        UploadManager uploadManager = new UploadManager(cfg);
        //默认不指定key的情况下，以文件内容的hash值作为文件名
        String key = filePath;
        try {
            InputStream inputStream = imgFile.getInputStream();
            Auth auth = Auth.create(accessKey, secretKey);
            String upToken = auth.uploadToken(bucket);
            try {
                Response response = uploadManager.put(inputStream,key,upToken,null, null);
                //解析上传成功的结果
                DefaultPutRet putRet = new Gson().fromJson(response.bodyString(), DefaultPutRet.class);
                System.out.println(putRet.key);
                System.out.println(putRet.hash);
                return link+key;
            } catch (QiniuException ex) {
                Response r = ex.response;
                System.err.println(r.toString());
                try {
                    System.err.println(r.bodyString());
                } catch (QiniuException ex2) {
                    ex2.printStackTrace();
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return "出错了捏";
    }
}

