<template>
  <div>
    <user_left/>
    <div class="main-container">
      <div class="chat-input">
        <!--        返回按钮，返回上一个路由-->
        <el-button type="primary" @click="back" icon="el-icon-arrow-left" class="back-button">返回</el-button>
        <el-input type="textarea" v-model="chatParam.content" placeholder="请输入聊天内容" class="chat-input-area"
                  v-loading="sendLoading"></el-input>
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
import user_left from "@/components/user_left.vue";
import {getChatInfo, userChatLog, chat} from "@/api/user_api";

export default {
  components: {
    user_left
  },
  data() {
    return {
      user: {
        userId: parseInt(localStorage.getItem("id"))
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
    this.geChatLog();
    this.userGetChatInfo();
  },
  methods: {
    // 聊天记录排序，时间降序
    sortChatLogs() {
      this.chatLogs.sort((a, b) => {
        return new Date(b.createTime) - new Date(a.createTime);
      });
    },
    geChatLog() {
      // 从上一路由中获取到accepterId
      const accepterId = parseInt(localStorage.getItem("accepterId"));
      const accepterIdentity = localStorage.getItem("accepterIdentity");
      const param = {
        senderId: this.user.userId,
        senderIdentity: "用户",
        accepterId: accepterId,
        accepterIdentity: accepterIdentity,
      };
      userChatLog(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.chatLogs = response.data.data;
          this.sortChatLogs();
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取聊天记录失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "error"
          });
        }
      });
    },
    userGetChatInfo() {
      const accepterId = parseInt(localStorage.getItem("accepterId"));
      const accepterIdentity = localStorage.getItem("accepterIdentity");
      const param = {
        senderId: this.user.userId,
        senderIdentity: "用户",
        accepterId: accepterId,
        accepterIdentity: accepterIdentity,
      };
      getChatInfo(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.chatInfo = response.data.data;
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取聊天对象信息失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "error"
          });
        }
      });
    },
    messageSend() {
      // 发送内容不能为空
      if (this.chatParam.content === "") {
        Notification({
          title: "发送消息失败",
          message: "发送内容不能为空",
          type: "error"
        });
        return;
      }
      this.sendLoading = true;
      const accepterId = parseInt(localStorage.getItem("accepterId"));
      const accepterIdentity = localStorage.getItem("accepterIdentity");
      const param = {
        senderId: this.user.userId,
        senderIdentity: "用户",
        accepterId: accepterId,
        accepterIdentity: accepterIdentity,
        content: this.chatParam.content
      };
      chat(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.chatLogs.push(response.data.data);
          this.chatParam.content = "";
          this.sortChatLogs();
          this.sendLoading = false;
          Notification({
            title: "发送消息成功",
            type: "success"
          });
        } else if (response.data.code === "ERROR") {
          this.sendLoading = false;
          Notification({
            title: "发送消息失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          this.sendLoading = false;
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "error"
          });
        }
      });
    },
    back() {
      this.$router.go(-1);
    },


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

.back-button {
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