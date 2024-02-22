<template>
  <div class="contianer">
    <el-aside>
      <el-menu>

        <el-menu-item index="1" @click="setActive('1')">我的</el-menu-item>
        <el-menu-item index="2" @click="setActive('2')">学生列表</el-menu-item>
        <el-menu-item index="3" @click="setActive('3')">报修管理</el-menu-item>


        <!-- 放置一个user的头像(图片)，icon下方写上从localStorage中获取的username，username下方放置logout按钮，点击后触发logot，内容全部居中 -->
        <div class="user">
          <img :src="admin.ava" alt="" class="user-icon">
          <br>
          <el-tag class="user-name" v-if="admin.gender === '女'" type="danger">{{ admin.name }}</el-tag>
          <el-tag class="user-name" v-else type="primary">{{ admin.name }}</el-tag>
          <br>
          <el-button type="danger" @click="logout" class="logout">退出</el-button>
        </div>

      </el-menu>
    </el-aside>
  </div>
</template>

<script>
import {Notification} from "element-ui";
import {findAdminById} from "@/api/admin_api";

export default {
  data() {
    return {
      activeIndex: "",
      admin: {
        adminId: 1,
      }
    }
  },
  created() {
    this.findAdmin();
  },
  methods: {
    logout() {
      Notification({
        title: "退出成功",
        type: "success"
      });
    },
    setActive(index) {

      if (index === "1" && this.$route.path !== "/admin_index") {
        this.$router.push("/admin_index");
      } else if (index === "2" && this.$route.path !== "/admin_users") {
        this.$router.push("/admin_users");
      }else if (index === "3" && this.$route.path !== "/user_fix_log") {
        this.$router.push("/user_fix_log");
      }
      this.activeIndex = index;
    },
    findAdmin() {
      findAdminById(this.admin).then(response => {
        if (response.data.code === "SUCCESS") {
          this.admin = response.data.data;
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
          this.$router.push("/login");
        }
      });
    }
  }
}
</script>

<style scoped>
.contianer {
  width: 200px;
  height: 100%;
  background-color: white;
  position: fixed;
  top: 0;
  left: 0;
  z-index: 100;
}

.el-menu {
  width: 200px;
  height: 100%;
}

.el-menu-item {
  margin: 10px;
  font-size: 20px;
  text-align: center;
  border-radius: 10px;
  cursor: pointer;
  font-weight: bold;
  transition: all 0.5s;
  background: white;
}

.el-menu-item:hover {
  background-color: lightgreen;
  color: white;
  transform: scale(1.1);
}

.user {
  text-align: center;
  margin: 10px;
  padding: 15px;
}

.user-icon {
  width: 100px;
  height: 100px;
  border-radius: 50px;
  object-fit: cover;
  transition: all 0.5s;
}

.user-icon:hover {
  transform: scale(1.2);
}

.logout {
  transition: all 0.5s;
}

.logout:hover {
  transform: scale(1.2);
}

.active {
  color: #4b70e2;
  background-color: lightgreen;
}

.user-name {
  margin-top: 15px;
  margin-bottom: 15px;
  font-size: 30px;
}
</style>