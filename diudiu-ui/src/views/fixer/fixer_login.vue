<template>
  <div class="main-container">
    <div class="main-box">
      <div :class="['container', 'container-register', { 'is-txl': isLogin }]">
        <form>
          <h2 class="title">注册</h2>
          <span class="text">欢迎回来</span>
          <input class="form__input" v-model="register.username" type="text" placeholder="账号"/>
          <input class="form__input" v-model="register.password" type="password" placeholder="密码"/>
          <input class="form__input" v-model="register.confirmPassword" type="password" placeholder="确认密码"/>
          <input class="form__input" v-model="register.name" type="text" placeholder="姓名"/>
          <el-radio-group v-model="register.gender">
            性别
            <el-radio label="男">男</el-radio>
            <el-radio label="女">女</el-radio>
          </el-radio-group>
          <input class="form__input" v-model="register.phone" type="password" placeholder="手机号"/>
          <div class="primary-btn" @click="toRegister">注册</div>
        </form>
      </div>
      <div
          :class="['container', 'container-login', { 'is-txl is-z200': isLogin }]"
      >
        <form>
          <h2 class="title">登录</h2>
          <span class="text">or use email for registration</span>
          <input class="form__input" v-model="login.username" type="text" placeholder="账号"/>
          <input class="form__input" v-model="login.password" type="password" placeholder="密码"/>
          <div class="primary-btn" @click="login">立即登录</div>
          <div class="primary-btn" @click="toUser">切换到学生端</div>
        </form>
      </div>
      <div :class="['switch', { login: isLogin }]">
        <div class="switch__circle"></div>
        <div class="switch__circle switch__circle_top"></div>
        <div class="switch__container">
          <h2>欢迎使用报修接单平台</h2>

          <div class="primary-btn" @click="isLogin = !isLogin">
            {{ isLogin ? "注册" : "登录" }}
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import {Notification} from "element-ui";
import {login} from "@/api/user_api";
import {fixerApi} from "@/api/fixer_api";

export default {
  name: "LoginBox",
  data() {
    return {
      isLogin: true,
      loginForm: {
        email: "",
        password: "",
      },
      register:{
        username:"",
        password:"",
        name:"",
        confirmPassword:"",
        gender:"",
        phone:""
      },
      identity:"修理员"
    };
  },
  created() {
    // 清空localStorage
    localStorage.clear();
  },
  methods: {
    login() {
      const param = {
        identity : this.identity,
        username: this.login.username,
        password: this.login.password,
      }
      login(param).then(response => {
        if (response.data.code === "SUCCESS") {
          Notification({
            title: "登录成功",
            type: "success"
          });
          localStorage.setItem("id", response.data.data.id);
          localStorage.setItem("token", response.data.data.token);
          localStorage.setItem("identity", this.identity);
          this.$router.push("/fixer_index");
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "登录失败",
            message: response.data.msg,
            type: "error"
          });
        }
      });
    },

    toRegister() {
      // 密码不一致
      if(this.register.password !== this.register.confirmPassword){
        Notification({
          title: "注册失败",
          message: "两次密码不一致",
          type: "error"
        });
        return;
      }

      // 所有参数不得为空
      const param = {
        username: this.register.username,
        password: this.register.password,
        name: this.register.name,
        gender: this.register.gender,
        phone: this.register.phone,
        identity: this.identity
      }

      if (param.username === "" || param.password === "" || param.gender === "" || param.phone === "" || param.name === "") {
        Notification({
          title: "注册失败",
          message: "请填写完整信息",
          type: "error"
        });
        return;
      }
      // 验证手机号
      const reg = /^1[3|4|5|7|8][0-9]{9}$/;
      if (!reg.test(param.phone)) {
        Notification({
          title: "注册失败",
          message: "手机号格式错误",
          type: "error"
        });
        return;
      }

      fixerApi.register(param).then(response => {
        if (response.data.code === "SUCCESS") {
          Notification({
            title: "注册成功",
            type: "success"
          });
          this.isLogin = true;
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "注册失败",
            message: response.data.msg,
            type: "error"
          });
        }
      });
    },

    // 切换到学生端
    toUser(){
      this.$router.push("/");
    }

  },
};
</script>

<style scoped>


.main-container {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: white;
  display: flex;
  justify-content: center;
  align-items: center;
}
.main-box {
  position: relative;
  width: 1000px;
  min-width: 1000px;
  min-height: 600px;
  height: 600px;
  padding: 25px;
  background-color: #ecf0f3;
  box-shadow: 10px 10px 10px #d1d9e6, -10px -10px 10px #f9f9f9;
  border-radius: 12px;
  overflow: hidden;
}

.main-box .container {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 0;
  width: 600px;
  height: 100%;
  padding: 25px;
  background-color: #ecf0f3;
  transition: all 1.25s;
}

.main-box .container form {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  width: 100%;
  height: 100%;
  color: #a0a5a8;
}

.main-box .container form .title {
  font-size: 34px;
  font-weight: 700;
  color: #181818;
}

.main-box .container form .text {
  margin-top: 20px;
  margin-bottom: 12px;
}

.main-box .container form .form__input {
  width: 350px;
  height: 40px;
  margin: 4px 0;
  padding-left: 25px;
  font-size: 13px;
  letter-spacing: 0.15px;
  border: none;
  outline: none;
  background-color: #ecf0f3;
  transition: 0.25s ease;
  border-radius: 8px;
  box-shadow: inset 2px 2px 4px #d1d9e6, inset -2px -2px 4px #f9f9f9;
}

.main-box .container form .form__input::placeholder {
  color: #a0a5a8;
}

.main-box .container-register {
  z-index: 100;
  left: calc(100% - 600px);
}

.main-box .container-login {
  left: calc(100% - 600px);
  z-index: 0;
}

.main-box .is-txl {
  left: 0;
  transition: 1.25s;
  transform-origin: right;
}

.main-box .is-z200 {
  z-index: 200;
  transition: 1.25s;
}

.main-box .switch {
  display: flex;
  justify-content: center;
  align-items: center;
  position: absolute;
  top: 0;
  left: 0;
  height: 100%;
  width: 400px;
  padding: 50px;
  z-index: 200;
  transition: 1.25s;
  background-color: #ecf0f3;
  overflow: hidden;
  box-shadow: 4px 4px 10px #d1d9e6, -4px -4px 10px #f9f9f9;
  color: #a0a5a8;
}

.main-box .switch .switch__circle {
  position: absolute;
  width: 500px;
  height: 500px;
  border-radius: 50%;
  background-color: #ecf0f3;
  box-shadow: inset 8px 8px 12px #d1d9e6, inset -8px -8px 12px #f9f9f9;
  bottom: -60%;
  left: -60%;
  transition: 1.25s;
}

.main-box .switch .switch__circle_top {
  top: -30%;
  left: 60%;
  width: 300px;
  height: 300px;
}

.main-box .switch .switch__container {
  display: flex;
  justify-content: center;
  align-items: center;
  flex-direction: column;
  position: absolute;
  width: 400px;
  padding: 50px 55px;
  transition: 1.25s;
}

.main-box .switch .switch__container h2 {
  font-size: 34px;
  font-weight: 700;
  line-height: 3;
  color: #181818;
}

.main-box .switch .switch__container p {
  font-size: 14px;
  letter-spacing: 0.25px;
  text-align: center;
  line-height: 1.6;
}

.main-box .login {
  left: calc(100% - 500px);
}

.main-box .login .switch__circle {
  left: 0;
}

.main-box .primary-btn {
  width: 180px;
  height: 50px;
  border-radius: 25px;
  margin-top: 50px;
  text-align: center;
  line-height: 50px;
  font-size: 14px;
  letter-spacing: 2px;
  background-color: #4b70e2;
  color: #f9f9f9;
  cursor: pointer;
  box-shadow: 8px 8px 16px #d1d9e6, -8px -8px 16px #f9f9f9;
}

.main-box .primary-btn:hover {
  box-shadow: 4px 4px 6px 0 rgba(255, 255, 255, 0.5), -4px -4px 6px 0 rgba(116, 125, 136, 0.5), inset -4px -4px 6px 0 rgba(255, 255, 255, 0.2), inset 4px 4px 6px 0 rgba(0, 0, 0, 0.4);
}
</style>