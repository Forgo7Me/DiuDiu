<template>
<div>
  <admin_left/>
  <div class="main-container">
    <div class="chat-input">
      <!--        返回按钮，返回上一个路由-->
      <el-button type="primary" @click="back" icon="el-icon-arrow-left" class = "back-button">返回</el-button>
      <el-input type="textarea" v-model="chatParam.content" placeholder="请输入聊天内容" class="chat-input-area" v-loading="sendLoading"></el-input>
      <el-button type="primary" @click="messageSend" class="chat-send" :loading="sendLoading">发送</el-button>
    </div>
    <div class="chat-log" v-for="log in chatLogs" :key="log.id">
      <!-- Sender's message -->
      <div class="chat-message user-message" v-if="log.type === 'SENDER'">
        <div class="message-time">{{ new Date(log.createTime).toLocaleString() }}</div>
        <div class="message-content">{{ log.content }}</div>
        <img class="avatar" :src="chatInfo.senderAva" alt="User Avatar">
      </div>
      <!-- Accepter's message -->
      <div class="chat-message ai-message" v-if="log.type === 'ACCEPTER'">
        <img class="avatar" :src="chatInfo.accepterAva" alt="AI Avatar">
        <div class="message-content">{{ log.content }}</div>
        <div class="message-time">{{ new Date(log.createTime).toLocaleString() }}</div>
      </div>
    </div>
  </div>
</div>
</template>

<script>
import {Notification} from "element-ui";
import admin_left from "@/components/admin_left.vue";
import { getChatInfo,userChatLog,chat } from "@/api/user_api";

export default {
  components: {
    admin_left
  },
  data() {
    return {
      admin: {
        adminId: 1
      },
      chatInfo: {
        senderName: "",
        senderAva: "",
        accepterName: "",
        accepterAva: "",
      },
      chatLogs: [],
      chatParam: {
        content: "",
      },
      sendLoading: false,
    }
  },
  created() {
    this.getChatLog();
    this.getChatInfo();
  },
  methods: {
    // 聊天记录排序，降序
    sortChatLog(){
      this.chatLogs.sort((a, b) => {
        return b.createTime - a.createTime;
      });
    },
    getChatLog(){
      const param = {
        senderId: this.admin.adminId,
        senderIdentity: "管理员",
        accepterId: parseInt(localStorage.getItem("accepterId")),
        accepterIdentity: "用户",
      };
      userChatLog(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.chatLogs = response.data.data;
          this.sortChatLog();
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取聊天记录失败",
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
    getChatInfo(){
      const param = {
        senderId: this.admin.adminId,
        senderIdentity: "管理员",
        accepterId: parseInt(localStorage.getItem("accepterId")),
        accepterIdentity: "用户",
      };
      getChatInfo(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.chatInfo = response.data.data;
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取聊天信息失败",
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
    back(){
      this.$router.go(-1);
    },
    messageSend() {
      if (this.chatParam.content === "") {
        Notification({
          title: "发送内容不能为空",
          type: "warning"
        });
      } else {
        this.sendLoading = true;
        const param = {
          senderId: this.admin.adminId,
          senderIdentity: "管理员",
          accepterId: parseInt(localStorage.getItem("accepterId")),
          accepterIdentity: "用户",
          content: this.chatParam.content
        };
        chat(param).then(response => {
          if (response.data.code === "SUCCESS") {
            this.chatParam.content = "";
            this.chatLogs.push(response.data.data);
            this.sortChatLog();
            Notification({
              title: "发送成功",
              type: "success"
            });
          } else if (response.data.code === "ERROR") {
            Notification({
              title: "发送失败",
              type: "error"
            });
          } else if (response.data.code === "TIMEOUT") {
            Notification({
              title: "登录信息已过期，请重新登录",
              type: "warning"
            });
          }
          this.sendLoading = false;
        });
      }

    }
  }
}
</script>

<style scoped>
.main-container {
  margin-left: 200px;
  padding: 20px;
}

.chat-input {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
  margin-top: 20px;
  width: 60%;
  margin-left: 20%;
}

.back-button{
  margin-right: 20px;
}
.chat-input-area {
  margin-left: 20px;
  margin-right: 20px;
}

.chat-send {
  margin-left: 20px;
}

.chat-log {
  width: 60%;
  //display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-top: 20px;
  margin-left: 20%;
}

.chat-message {
  display: flex;
  align-items: center;
  margin: 10px 0;
  transition: all 0.5s;
}

.chat-message:hover {
  transform: scale(1.1);
  transition: all 0.5s;
}

.user-message {
  justify-content: flex-end;
}

.ai-message {
  margin-left: 0px;
}

.avatar {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  margin: 0 10px;
}

.message-content {
  padding: 10px;
  border-radius: 10px;
  background-color: #f2f2f2;
  font-size: 20px;
}

.message-time {
  margin: 5px 10px;
  font-size: 10px;
}


</style>