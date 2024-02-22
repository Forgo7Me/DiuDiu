<template>
  <div class="contianer">
    <el-aside>
      <el-menu>
        <el-menu-item
            index="1"
            @click="setActive('1')"
            v-bind:class="{ active: $route.path === '/user_index' }">
          <i class="el-icon-user"></i>我的
        </el-menu-item>
        <el-menu-item
            index="2"
            @click="setActive('2')"
            v-bind:class="{ active: $route.path === '/user_admin'}">
          <i class="el-icon-user-solid"></i>管理员信息
        </el-menu-item>
        <el-menu-item
            index="3"
            @click="setActive('3')"
            v-bind:class="{ active: $route.path === '/user_fix_log' }">
          <i class="el-icon-s-order"></i>报修记录
        </el-menu-item>
        <el-menu-item
            index="4"
            @click="setActive('4')"
            v-bind:class="{ active: $route.path === '/user_post' }">
          <i class="el-icon-reading"></i>论坛
        </el-menu-item>
        <el-menu-item
            index="5"
            @click="setActive('5')"
            v-bind:class="{ active: $route.path === '/user_ai' }">
          <i class="el-icon-loading"></i>额外功能
        </el-menu-item>


        <!-- 放置一个user的头像(图片)，icon下方写上从localStorage中获取的username，username下方放置logout按钮，点击后触发logot，内容全部居中 -->
        <div class="user">
          <img :src="user.ava" alt="" class="user-icon">
          <br>
          <el-tag class="user-name" v-if="user.gender === '女'" type="danger">{{ user.name }}</el-tag>
          <el-tag class="user-name" v-else type="primary">{{ user.name }}</el-tag>
          <br>
          <el-button type="danger" @click="logout" class="logout">退出</el-button>
        </div>

      </el-menu>
    </el-aside>
  </div>
</template>

<script>
import {Notification} from "element-ui";
import {findUserById} from "@/api/user_api";

export default {
  data() {
    return {
      activeIndex: "",
      user: {
        userId: 1,
      }
    }
  },
  mounted() {
    this.findUser();
  },
  methods: {
    logout() {
      Notification({
        title: "退出成功",
        type: "success"
      });
    },
    setActive(index) {
      if (index === "1" && this.$route.path !== "/user_index") {
        this.$router.push("/user_index");
      } else if (index === "2" && this.$route.path !== "/user_admin") {
        this.$router.push("/user_admin");
      } else if (index === "3" && this.$route.path !== "/user_fix_log") {
        this.$router.push("/user_fix_log");
      } else if (index === "4" && this.$route.path !== "/user_post") {
        this.$router.push("/user_post");
      }else if (index === "5" && this.$route.path !== "/user_ai") {
        this.$router.push("/user_ai");
      }
      this.activeIndex = index;
    },
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
          // 跳转到登录页面
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
  background-color: lightgreen;
}

.user-name {
  margin-top: 15px;
  margin-bottom: 15px;
  font-size: 30px;
}
</style>