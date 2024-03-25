<template>
  <div class="row-container">
    <div v-for="(item,index) in items" :key="index" :class="item.route === route ? 'left-item-active' : 'left-item'"
         @click="setActive(item.route)">
      <i :class="item.icon"></i>
      {{ item.name }}
    </div>
    <div class="ava">
      <img :src="user.ava" alt="User Avatar" class="ava-img">
    </div>
    <div class="name">
      {{ user.name }}
    </div>
    <div class="log-out" @click="logout">退出登录</div>
  </div>
</template>

<script>
import {Notification} from "element-ui";
import {findUserById, logout} from "@/api/user_api";

export default {
  data() {
    return {
      user: {
        userId: parseInt(localStorage.getItem("id"))
      },
      items: [
        {name: '我的', route: 'user_index', icon: 'el-icon-user'},
        {name: '管理员信息', route: 'user_admin', icon: 'el-icon-user-solid'},
        {name: '报修记录', route: 'user_fix_log', icon: 'el-icon-s-order'},
        {name: '论坛', route: 'user_post', icon: 'el-icon-reading'},
        {name: '额外功能', route: 'user_ai', icon: 'el-icon-loading'},
        {name: '公告栏',route: 'user_notice', icon: 'el-icon-s-flag'}
      ],
      route: this.$route.name,
    }
  },
  created() {
    console.log(this.$route.name);
  },
  mounted() {
    this.findUser();
  },
  methods: {
    logout() {
      logout().then(response => {
        // 询问是否确定退出
        this.$confirm('确定退出登录吗?', '提示', {
          confirmButtonText: '确定',
          cancelButtonText: '取消',
          type: 'warning'
        }).then(() => {
          // 确定退出
          if (response.data.code === "SUCCESS") {
            Notification({
              title: "退出登录成功",
              type: "success"
            });
            localStorage.removeItem("token");
            this.$router.push("/login");
          } else if (response.data.code === "ERROR") {
            Notification({
              title: "退出失败",
              type: "error"
            });
          }
        }).catch(() => {
          // 取消退出
          this.$message({
            type: 'info',
            message: '已取消退出'
          });
        });
      });
    },
    setActive(index) {
      if (this.$route.name === index) {
        return;
      }
      this.$router.push({name: index});
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
.row-container {
  padding-top: 2vh;
  width: 20vh;
  height: 96vh;
  box-shadow: 10px 10px 10px #d1d9e6, -10px -10px 10px #f9f9f9;
  border-radius: 10px;
  justify-content: center;
  flex-direction: column;
}

.left-item {
  width: 80%;
  height: 5vh;
  background-color: #ecf0f3;
  border-radius: 10px;
  align-items: center;
  justify-content: center;
  margin-left: 10%;
  display: flex;
  flex-direction: row;
  margin-top: 1vh;
  transition: all 0.5s;
}

.left-item:hover {
  scale: 1.2;
}

.left-item-active {
  width: 80%;
  height: 5vh;
  background-color: lightgreen;
  border-radius: 10px;
  align-items: center;
  justify-content: center;
  margin-left: 10%;
  display: flex;
  flex-direction: row;
  margin-top: 1vh;
  transition: all 0.5s;
}

.left-item-active:hover {
  scale: 1.2;
}

.ava {
  width: 16vh;
  height: 16vh;
  border-radius: 50%;
  margin-left: 2vh;
  margin-top: 2vh;
  overflow: hidden;
  position: absolute;
  bottom: 15vh;
  transition: all 0.5s;
}

.ava:hover {
  transform: scale(1.2);
}

.ava-img {
  width: 16vh;
  height: 16vh;
  object-fit: cover;
}

.name {
  width: 16vh;
  height: 5vh;
  border-radius: 10px;
  align-items: center;
  justify-content: center;
  display: flex;
  flex-direction: row;
  margin-left: 2vh;
  position: absolute;
  bottom: 10vh;
}

.log-out {
  width: 16vh;
  margin-left: 2vh;
  height: 5vh;
  background-color: red;
  color: whitesmoke;
  border-radius: 10px;
  align-items: center;
  justify-content: center;
  display: flex;
  flex-direction: row;
  margin-top: 1vh;
  transition: all 0.5s;
  position: absolute;
  bottom: 2vh;
}

.log-out:hover {
  transform: scale(1.2);
}
</style>