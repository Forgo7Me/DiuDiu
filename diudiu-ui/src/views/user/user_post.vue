<template>
  <div style="display:flex;flex-direction: row">
    <user_left/>
    <div class="main-container">
      <div class="post-head" style="margin-top: 40px">
        <!--    上方加入排序选项，使用element-ui的radio单选框，可选项有(按时间，按浏览量，按点赞数），选择后调用函数，分别将post按createTime,viewCount,favorCount从大到小排序-->
        <el-radio-group v-model="sortType" @change="sortPost">
          <el-radio-button label="按时间">按时间</el-radio-button>
          <el-radio-button label="按浏览量">按浏览量</el-radio-button>
          <el-radio-button label="按点赞数">按点赞数</el-radio-button>
          <el-radio-button label="按评论数">按评论数</el-radio-button>
        </el-radio-group>
<!--        筛选选项，可与排序同时选择，有两个选择，分别为“全部”和“我的”-->
        <el-radio-group v-model="filterType" @change="filterPost" style="margin-left: 100px">
          <el-radio-button label="全部">全部</el-radio-button>
          <el-radio-button label="我的">我的</el-radio-button>
        </el-radio-group>
        <!--      新增按钮，点击后让新增帖子的el-dialog显示，可上传图片和输入内容(按钮距左边100px,添加el的添加icon-->
        <el-button type="success" @click="visible.addVisible = true" style="margin-left: 100px">
          <i class="el-icon-plus"></i>
          发帖
        </el-button>
      </div>
      <!--    使用矩形渲染帖子，左侧200px宽高的位置放以post.img为src的图片，右侧中心位置放置post.content的前20个字符(utf8),下方贴着矩形放置三个element-ui图标，分别展示浏览量(viewCount),点赞量(favorCount),评论数(commentCount)-->
      <div class="post" v-for="post in filterPosts" :key="post.id" @click="openPost(post)">
        <div class="post-img">
          <img :src="post.img" alt="" class="post-img-img">
        </div>
        <div class="post-content">
          <p>{{ post.content.substring(0, 20) }}</p>
        </div>
        <!--    展示发帖人名字(userName)和格式化后的时间(createTime),从上到下在一列上-->
        <div class="post-user-info">
          <img :src="post.ava" alt="" class="user-icon">
          <p>{{ post.userName }}</p>
          <p>{{ formatDate(post.createTime) }}</p>
        </div>
        <div class="post-info">
          <el-tag type="primary">{{ post.viewCount }}<i class="el-icon-view"></i></el-tag>
          <!--          使用el-tag显示favorCount,如果post.isFavor为"是"则type为danger,isFavor为"否"则type为info-->
          <el-tag v-if="post.isFavor === '是'" type="danger">{{ post.favorCount }}<i class="el-icon-star-on"></i>
          </el-tag>
          <el-tag v-else type="info">{{ post.favorCount }}<i class="el-icon-star-off"></i></el-tag>
          <el-tag type="warning">{{ post.commentCount }}<i class="el-icon-chat-dot-square"></i></el-tag>
        </div>
      </div>
    </div>

    <!--  使用element-ui的drawer(从右往左开),无标题，上方展示post的img,下方展示content(居中),下方展示post-info的浏览量，评论量，点赞量，再下方预留后续展示评论（使用虚线将评论区隔开）-->
    <el-drawer :visible.sync="drawerVisible" direction="rtl" size="50%">
      <div class="drawer-content">
        <img :src="drawerPost.img" alt="" class="drawer-img">
        <p>{{ drawerPost.content }}</p>
        <div class="drawer-post-info">
          <img :src="drawerPost.ava" alt="" class="user-icon">
          <el-tag type="primary">{{ drawerPost.viewCount }}<i class="el-icon-view"></i></el-tag>
          <el-tag type="warning" class="post-comment-commentCount" @click="visible.addCommentVisible=true">
            {{ drawerPost.commentCount }}<i class="el-icon-chat-dot-square"></i></el-tag>
          <el-tag v-if="drawerPost.isFavor === '是'" type="danger" class="user-favor" @click="userFavor">
            {{ drawerPost.favorCount }}<i class="el-icon-star-on"></i></el-tag>
          <el-tag v-else type="info" class="user-favor" @click="userFavor">{{ drawerPost.favorCount }}<i
              class="el-icon-star-off"></i></el-tag>
<!--          若该帖子发帖人是当前用户，则让一个danger的el-button"删除"可见-->
          <el-button v-if="drawerPost.userId === user.userId" type="danger" @click="deletePost(drawerPost.id)">删除</el-button>
        </div>

        <div class="drawer-comment">
          <!--          使用类似于展示上述帖子的形式展示评论，使用自定义template在title上展示post的头像(ava,图片),姓名(userName),格式化后的时间(createTime),内容(content,换行展示)
            ,折叠内容使用el-dialog展示回复的内容(sonComment,集合,渲染ava,userName,格式化后的时间(createTime),内容(content,换行展示)，在点击一个评论后让el-dialog现行-->
          <el-spinner v-if="isLoadingComments" type="fading-circle"></el-spinner>
          <div class="post-comment" v-for="comment in drawerPost.comments" :key="comment.id"
               @click="openSonComments(comment)">
            <div class="post-comment-ava">
              <img :src="comment.ava" alt="" class="user-icon">
            </div>
            <div class="post-comment-name">
              <el-tag type="success">{{ comment.userName }}</el-tag>
            </div>
            <div class="post-comment-content">
              <p>{{ comment.content }}</p>
            </div>
            <div class="post-comment-time">
              <p>{{ formatDate(comment.createTime) }}</p>
            </div>
          </div>

        </div>
      </div>
    </el-drawer>

<!--    发帖模块-->
    <el-dialog title="发帖" :visible.sync="visible.addVisible" width="50%">
      <el-form>
        <el-form-item label="内容">
          <el-input v-model="newPost.content" placeholder="请输入帖子内容" type="textarea"></el-input>
        </el-form-item>
        <el-form-item label="图片(仅第一张有效，请勿多上传)">
          <el-upload
              ref="upload"
              class="upload-post-img"
              action=""
              :on-preview="handlePictureCardPreview"
              :on-remove="handleRemove"
              :on-success="handleUploadSuccess"
              :file-list="newPost.fileList"
              list-type="picture-card"
              :auto-upload="false"
              :before-upload="beforeUpload"
              v-loading="uploadLoading"
          >
            <i class="el-icon-plus"></i>
          </el-upload>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="uploadPost">发帖</el-button>
          <el-button @click="visible.addVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>


    <!--  使用element-ui的el-dialog(宽度50%)，无标题，上方展示content(居中)的文本域，下方放置确认和取消按钮-->
    <el-dialog title="评论" :visible.sync="visible.addCommentVisible" width="50%">
      <el-form>
        <el-form-item label="评论">
          <el-input v-model="newComment.content" placeholder="请输入评论内容" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="addComment">评论</el-button>
          <el-button @click="visible.addCommentVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>


    <el-dialog title="回复" :visible.sync="visible.sonCommentVisible" width="50%">
      <!--              下方加上两个按钮，一个"回复",功能待绑定，一个"关闭",是该dialog关闭-->
      <el-form>
        <el-form-item label="回复">
          <el-input v-model="newSonCommentContent" placeholder="请输入回复内容" type="textarea"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="responseComment">回复</el-button>
          <el-button @click="visible.sonCommentVisible = false">关闭</el-button>
        </el-form-item>
      </el-form>
      <div class="post-comment-area">
        <div v-for="sonComment in sonComments" :key="sonComment.id" class="post-comment">
          <div class="post-comment-ava">
            <img :src="sonComment.ava" alt="" class="user-icon">
          </div>
          <div class="post-comment-name">
            <el-tag type="success">{{ sonComment.userName }}</el-tag>
          </div>
          <div class="post-comment-content">
            <p>{{ sonComment.content }}</p>
          </div>
          <div class="post-comment-time">
            <p>{{ formatDate(sonComment.createTime) }}</p>
          </div>
        </div>
      </div>

    </el-dialog>
  </div>
</template>
<script>
import user_left from "@/components/user_left.vue";
import {Notification} from "element-ui";
import {getAllPost, getPostComment, favorPost, viewPost, addComment, responseComment,uploadPostImage,uploadPostContent,deletePost} from "@/api/user_api";
import axios from "axios";

export default {
  components: {
    user_left
  },
  data() {
    return {
      user: {
        userId: parseInt(localStorage.getItem("id"))
      },
      posts: [],
      filterPosts: [],
      sortType: "按时间",
      filterType: "全部",
      drawerVisible: false,
      drawerPost: {},
      comment: {},
      postComments: [],
      sonComments: [],
      isLoadingComments: false,
      visible: {
        addVisible: false,
        addCommentVisible: false,
        sonCommentVisible: false
      },
      newPost: {
        content: "",
        fileList: []
      },
      newComment: {
        content: ""
      },
      newSonCommentContent: "",
      uploadLoading: false
    }
  },
  created() {
    this.getAllPost();
  },
  methods: {
    formatDate(value) {
      let date = new Date(parseInt(value));
      let year = date.getFullYear();
      let month = ("0" + (date.getMonth() + 1)).slice(-2);
      let day = ("0" + date.getDate()).slice(-2);
      let hour = ("0" + date.getHours()).slice(-2);
      let minute = ("0" + date.getMinutes()).slice(-2);
      let second = ("0" + date.getSeconds()).slice(-2);
      return `${year}-${month}-${day} ${hour}:${minute}:${second}`;
    },
    getAllPost() {
      const param = {
        userId: this.user.userId
      }
      getAllPost(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.posts = response.data.data;
          this.filterPosts = response.data.data;
          this.sortPost()
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取帖子信息失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "warning"
          });
        }
      });
    },
    sortPost() {
      if (this.sortType === "按时间") {
        this.filterPosts.sort((a, b) => {
          return b.createTime - a.createTime;
        });
      } else if (this.sortType === "按浏览量") {
        this.filterPosts.sort((a, b) => {
          return b.viewCount - a.viewCount;
        });
      } else if (this.sortType === "按点赞数") {
        this.filterPosts.sort((a, b) => {
          return b.favorCount - a.favorCount;
        });
      } else if (this.sortType === "按评论数") {
        this.filterPosts.sort((a, b) => {
          return b.commentCount - a.commentCount;
        });
      }
    },
    filterPost() {
      if (this.filterType === "我的") {
        this.filterPosts = this.posts.filter(post => {
          return post.userId === this.user.userId;
        });
      } else {
        this.filterPosts = this.posts;
      }
      this.sortPost();
    },
    getComments() {
      this.isLoadingComments = true;
      const param = {
        postId: this.drawerPost.id,
        userId: this.user.userId
      }
      getPostComment(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.drawerPost.comments = response.data.data;
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取评论信息失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "warning"
          });
        }
        return null;
      }).finally(() => {
        this.isLoadingComments = false;
      });
    },
    openPost(post) {
      const param = {
        postId: post.id,
        userId: this.user.userId
      }
      viewPost(param).then(response => {
        if (response.data.code === "SUCCESS") {
          post.viewCount += 1;
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取评论信息失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "warning"
          });
        }
      });
      this.drawerPost = post;
      this.getComments();
      this.drawerVisible = true;
    },
    openSonComments(comment) {
      this.comment = comment;
      this.sonComments = comment.sonComment;
      this.visible.sonCommentVisible = true;
    },
    userFavor() {
      const param = {
        postId: this.drawerPost.id,
        userId: this.user.userId
      }
      favorPost(param).then(response => {
        if (response.data.code === "SUCCESS") {
          if (this.drawerPost.isFavor === "是") {
            this.drawerPost.isFavor = "否";
            this.drawerPost.favorCount -= 1;
          } else {
            this.drawerPost.isFavor = "是";
            this.drawerPost.favorCount += 1;
          }
          Notification({
            title: "操作成功",
            message: response.data.data,
            type: "success"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "warning"
          });
        }
      });
    },
    addComment() {
      const param = {
        postId: this.drawerPost.id,
        userId: this.user.userId,
        content: this.newComment.content
      }
      addComment(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.newComment.content = "";
          this.getComments();
          this.drawerPost.commentCount += 1;
          this.visible.addCommentVisible = false;
          Notification({
            title: "评论成功",
            type: "success"
          });
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "评论失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "warning"
          });
        }
      });
    },
    responseComment() {
      const param = {
        postId: this.drawerPost.id,
        userId: this.user.userId,
        responsePostId: this.comment.id,
        content: this.newSonCommentContent
      }
      responseComment(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.newSonCommentContent = "";
          this.getComments();
          this.visible.sonCommentVisible = false;
          Notification({
            title: "回复成功",
            type: "success"
          });
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "回复失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "warning"
          });
        }
      });
    },
    // 图片上传需要的方法
    beforeUpload(file) {
      const isJPG = file.type === 'image/jpeg';
      const isPNG = file.type === 'image/png';
      const isGIF = file.type === 'image/gif';
      const isLt2M = file.size / 1024 / 1024 < 1;

      if (!isJPG && !isPNG && !isGIF) {
        this.$message.error('上传头像图片只能是 JPG/PNG/GIF 格式!');
      }
      if (!isLt1M) {
        this.$message.error('上传头像图片大小不能超过 1MB!');
      }
      return (isJPG || isPNG || isGIF) && isLt1M;
    },
    uploadPost(){
      try {
        // 获取要上传的图片
        const file = this.$refs.upload.uploadFiles[0];
        const formData = new FormData();
        formData.append('file', file.raw);
        this.uploadLoading = true; // 显示遮罩层
        // 获取文本
        const ContentParam = {
          userId: this.user.userId,
          content: this.newPost.content
        }
      }catch (e){
        this.uploadLoading = false; // 隐藏遮罩层
        Notification({
          title: "请填写完整信息",
          type: "warning"
        });
        return;
      }
      // 判断文本是否为空且图片是否满足要求
      if (this.newPost.content === "" || file === undefined) {
        this.uploadLoading = false; // 隐藏遮罩层
        Notification({
          title: "请填写完整信息",
          type: "warning"
        });
        return;
      }
      // 先上传文本，待获得响应的data.data后将其放到axios的请求头头"postId"中上传图片
      uploadPostContent(ContentParam).then(response => {
        if (response.data.code === "SUCCESS") {
          const postId = response.data.data;
          axios.defaults.headers.post['postId'] = postId;
          uploadPostImage(formData).then(response => {
            if (response.data.code === "SUCCESS") {
              this.uploadLoading = false; // 隐藏遮罩层
              this.visible.addVisible = false;
              this.getAllPost();
              Notification({
                title: "上传成功",
                type: "success"
              });
            } else if (response.data.code === "ERROR") {
              this.uploadLoading = false; // 隐藏遮罩层
              this.visible.addVisible = false;
              Notification({
                title: "上传失败",
                type: "error"
              });
            } else if (response.data.code === "TIMEOUT") {
              this.uploadLoading = false; // 隐藏遮罩层
              this.visible.addVisible = false;
              Notification({
                title: "登录信息已过期，请重新登录",
                type: "warning"
              });
            }
          });
        }
      });
    },
    // 删除帖子
    deletePost(id) {
      this.$confirm('此操作将永久删除该帖子, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const param = {
          postId: parseInt(id)
        }
        deletePost(param).then(response => {
          if (response.data.code === "SUCCESS") {
            this.posts = this.posts.filter(post => post.id !== id);
            this.filterPosts = this.filterPosts.filter(post => post.id !== id);
            this.drawerVisible = false;
            Notification({
              title: "删除成功",
              type: "success"
            });
          } else if (response.data.code === "ERROR") {
            Notification({
              title: "删除失败",
              type: "error"
            });
          } else if (response.data.code === "TIMEOUT") {
            Notification({
              title: "登录信息已过期，请重新登录",
              type: "warning"
            });
          }
        });
      }).catch(() => {
        this.$message({
          type: 'info',
          message: '已取消删除'
        });
      });
    },


  },

}
</script>
<style scoped>
.main-container {
  width: 70%;
  margin: 0 auto;
  align-content: center;
}

.post {
  width: 90%;
  height: 200px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 40px;
  border: 1px solid #ccc;
  border-radius: 15px;
  box-shadow: 0 0 10px #ccc;
  transition: all 0.7s;
}

.post:hover {
  transform: scale(1.1);
  box-shadow: 0 0 20px #ccc;
  border: 1px solid #000;
  box-shadow: 0 0 10px #ccc, 10px 10px 10px #ccc;
}

.post-img {
  width: 200px;
  height: 200px;
  border-radius: 15px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.post-img-img {
  width: 200px;
  height: 200px;
  border-radius: 15px;
  object-fit: cover;
}

.post-content {
  width: 80%;
  height: 100%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.post-user-info {
  font-weight: bold;
  font-size: 15px;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.user-icon {
  width: 80px;
  height: 80px;
  border-radius: 50%;
  object-fit: cover;
  margin-top: 10px;
}

.post-info {
  width: 50%;
  display: flex;
  justify-content: space-around;
  align-items: center;
}

.el-tag {
  font-size: 25px;
}

.drawer-content {
  display: flex;
  flex-direction: column;
  align-items: center;
}

.drawer-content p {
  font-size: 30px;
  margin-top: 40px;
  margin-bottom: 40px;
}

.drawer-img {
  width: 300px;
  height: 300px;
  object-fit: cover;
  border-radius: 15px;
}

.drawer-post-info {
  display: flex;
  justify-content: space-around;
  width: 50%;
  margin-top: 20px;
  align-items: center;
}

.drawer-comment {
  width: 100%;
  margin-top: 20px;
}

.favor {
  transition: all 0.5s;
}

.favor:hover {
  transform: scale(1.2);
}

.post-comment {
  width: 80%;
  height: 100px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-top: 40px;
  margin-left: 10%;
  border: 1px solid #ccc;
  border-radius: 15px;
  box-shadow: 0 0 10px #ccc;
  transition: all 0.7s;
}

.post-comment:hover {
  transform: scale(1.1);
  box-shadow: 0 0 20px #ccc;
  border: 1px solid #000;
  box-shadow: 0 0 10px #ccc, 10px 10px 10px #ccc;
}

.post-comment-ava {
  height: 100px;
  width: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.post-comment-name {
  width: 100px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.post-comment-name el-tag {
  font-size: 10px;
}

.post-comment-content {
  width: 50%;
  display: flex;
  justify-content: center;
  align-items: center;
}

.post-comment-content p {
  font-size: 15px;
}

.post-comment-time {
  width: 90px;
  display: flex;
  justify-content: center;
  align-items: center;
}

.post-comment-time p {
  font-size: 15px;
}

.user-favor {
  transition: all 0.5s;
}

.user-favor:hover {
  transform: scale(1.3);
}

.post-comment-commentCount {
  transition: all 0.5s;
}

.post-comment-commentCount:hover {
  transform: scale(1.3);
}
</style>