<template>
  <div class="container">
    <user_left/>
    <div class="main-container">
      <!--使用element-ui的el-descriptions将user除了userId的属性渲染在页面上(每个属性前需要带上一个el-icon)-->
      <el-descriptions title="我的信息" :column="1" class="margin-top" border>
        <template slot="extra">
          <el-button type="primary">编辑</el-button>
        </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user-solid"></i>
            照片
          </template>
          <img :src="user.ava" alt="" class="user-icon">
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            用户名
          </template>
          <el-tag type="danger">{{ user.name }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user-solid"></i>
            性别
          </template>
          <!-- user.gender为女则使用el-tag的type为danger，为男则使用primary,使用v-if判断 -->
          <el-tag v-if="user.gender === '女'" type="danger">{{ user.gender }}</el-tag>
          <el-tag v-else type="primary">{{ user.gender }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在园区
          </template>
          <el-tag type="success">{{ user.siteY }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在楼栋
          </template>
          <el-tag type="danger">{{ user.siteD }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在楼层
          </template>
          <el-tag type="warning">{{ user.siteC }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在房间
          </template>
          <el-tag type="info">{{ user.siteF }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号
          </template>
          <el-tag type="success">{{ user.phone }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱
          </template>
          <el-tag type="primary">{{ user.email }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-flag"></i>
            详细住址
          </template>
          <el-tag type="primary">{{ user.siteY }}-{{ user.siteD }}栋-{{ user.siteC }}层-{{ user.siteF }}号</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>

    <!--    弹出通知栏dialog-->
    <el-dialog width="60%" :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="noticeVisible"
               title="通知" :before-close="closeNotice">
      <div class="notice">
        <div class="notice-admin">
          <img :src="confirmNotice.admin.ava" alt="" class="notice-admin-img">
          <h1>发布人：{{ confirmNotice.admin.name }}</h1>
        </div>
        <div class="notice-context">
          <div class="notice-context-title">
            <h2>{{ confirmNotice.notice.title }}</h2>
            <h3>发布时间：{{ new Date(parseInt(confirmNotice.notice.createTime)).toLocaleString() }}</h3>
          </div>
          <div class="notice-context-context">
            <p>{{ confirmNotice.notice.context }}</p>
          </div>
        </div>
        <div class="notice-footer">
          <div class="notice-footer-box">
            <p style="color:red">该公告需要您确认收到</p>
          </div>
          <el-button type="primary" @click="toConfirmNotice">确认收到</el-button>
        </div>
      </div>
    </el-dialog>
  </div>
</template>

<script>
import user_left from "@/components/user_left.vue";
import {Notification} from "element-ui";
import {findUserById} from "@/api/user_api";
import {Api} from "@/api/Api";

export default {
  components: {
    user_left
  },
  data() {
    return {
      user: {
        userId: parseInt(localStorage.getItem("id")),
      },
      notices: [],
      confirmNotices: [],
      confirmNotice: {
        notice: {},
        admin: {}
      },
      noticeVisible: false,
    }
  },
  created() {
    this.findUser();
    this.getAllNotice();
  },
  watch: {
    // 监控confirmNotices,若其不为空则弹出通知栏
    confirmNotices: {
      handler: function (val) {
        if (val !== null) {
          this.noticeVisible = true;
        }
      },
      deep: true
    }
  },
  methods: {
    findUser() {
      findUserById(this.user).then(response => {
        if (response.data.code === "SUCCESS") {
          this.user = response.data.data;
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取用户信息失败",
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

    // 获取所有通知
    getAllNotice() {
      Api.getNotice(this.user).then(res => {
            if (res.data.code === "SUCCESS") {
              this.notices = res.data.data;
              // 筛选notice.notice.confirm为"是"且confirmed不为"是"的通知
              this.confirmNotices = this.notices.filter(notice => notice.notice.confirm === "是" && notice.notice.confirmed !== "是");
              if (this.confirmNotices.length > 0) {
                this.confirmNotice = this.confirmNotices[0];
              }else{
                this.confirmNotices = null;
              }
            } else if (res.data.code === "ERROR") {
              Notification({
                title: "获取通知失败",
                type: "error"
              });
            } else if (res.data.code === "TIMEOUT") {
              Notification({
                title: "登录信息已过期，请重新登录",
                type: "warning"
              });
              this.$router.push("/");
            }
          }
      )
    },


    // 关闭通知栏
    closeNotice() {
      // 若confirmNotices不为空则将不允许关闭并提示
      if (this.confirmNotices !== null) {
        Notification({
          title: "请确认收到通知",
          type: "warning"
        });
      }
    },

    // 确认收到公告
    toConfirmNotice() {
      const param = {
        userId: parseInt(localStorage.getItem("id")),
        noticeId: this.confirmNotice.notice.id
      }
      Api.confirmNotice(param).then(res => {
            if (res.data.code === "SUCCESS") {
              Notification({
                title: "确认成功",
                type: "success"
              });
              this.noticeVisible = false;
              this.getAllNotice();
            } else if (res.data.code === "ERROR") {
              Notification({
                title: "确认失败",
                message: res.data.msg,
                type: "error"
              });
            } else if (res.data.code === "TIMEOUT") {
              Notification({
                title: "登录信息已过期，请重新登录",
                type: "warning"
              });
              this.$router.push("/");
            }
          }
      )
    }

  },


}
</script>

<style scoped>
.container {
  display: flex;
  flex-direction: row;
  height: 98vh;
}

.left-rows {
}

.main-container {
  width: 80%;
  margin-left: 5%;
  align-content: center;
  font-size: 20px;
}

.el-descriptions {
  font-size: 20px;
}

.el-tag {
  font-size: 20px;
}

.user-icon {
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: block;
}

.el-button {
  transition: all 0.5s;
}

.el-button:hover {
  transform: scale(1.1);
}

.notice {
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  align-items: center;
  width: 80%;
  margin-left: 10%;
  height: 60vh;

}

.notice-admin {
  width: 80%;
  height: 20vh;
  display: flex;
  flex-direction: row;
  justify-content: space-between;
  align-items: center;
  border-bottom: 1px solid #d1d9e6;
}

.notice-admin-img {
  width: 10vh;
  height: 10vh;
  border-radius: 50%;
  margin-top: 5vh;
  object-fit: cover;
}

.notice-context {
  flex-direction: column;
  height: 30vh;
  width: 100%;
  display: flex;
  align-items: center;
}

.notice-context-title {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.notice-context-context {
  box-shadow: 4px 4px 10px #d1d9e6, -4px -4px 10px #f9f9f9;
] border-radius: 20 px;
  padding: 10px;
  height: 25vh;
  overflow-y: auto;
}

.notice-footer {
  flex-direction: column;
  height: 10vh;
  display: flex;
  align-items: center;
}

.notice-footer-box {
  display: inline-block;
  white-space: nowrap;
  animation: 10s wordsLoop linear infinite normal;
}

@keyframes wordsLoop {
  0% {
    transform: translateX(100%);
  }
  100% {
    transform: translateX(-100%);
  }
}

</style>