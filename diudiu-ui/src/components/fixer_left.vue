<template>
  <div class="fixer-left">
    <div v-for="item in items" :key="item.route"
         :class="active === item.route ? 'fixer-left-item-active' : 'fixer-left-item'" @click="setActive(item.route)">
      <i :class="item.icon"></i>
      {{ item.name }}
    </div>
    <button @click="logout" class="log-out">退出登录</button>
  </div>
</template>

<script>
import {Notification} from "element-ui";

export default {
  name: "fixer_left",
  data() {
    return {
      items: [
        {name: '首页', route: 'fixer_index', icon: 'el-icon-s-home'},
        {name: '报修订单', route: 'fixer_order', icon: 'el-icon-s-order'},
        {name: '维修记录', route: 'fixer_record', icon: 'el-icon-s-cooperation'},
      ],
      active: ''
    }
  },
  created() {
    this.active = this.$route.name;
  },
  methods: {
    logout() {
      this.$confirm("确定退出登录吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        this.$router.push("/fixer_login");
        Notification({
          title: "退出登录成功",
          type: "success"
        });
      }).catch(() => {
        Notification({
          title: "取消退出登录",
          type: "info"
        });
      });
    },

    setActive(route){
      if(this.active!==route){
        this.$router.push({name:route})
      }
    }
  }
}
</script>

<style>
.fixer-left {
  width: 20vh;
  height: 98vh;
  box-shadow: 10px 10px 10px #d1d9e6, -10px -10px 10px #f9f9f9;
  border-radius: 10px;
  justify-content: center;
  flex-direction: column;
}

.fixer-left-item {
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

.fixer-left-item:hover {
  transform: scale(1.2);
}


.fixer-left-item-active {
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

.fixer-left-item-active:hover {
  transform: scale(1.2);
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