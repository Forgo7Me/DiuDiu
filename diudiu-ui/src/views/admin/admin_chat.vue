<template>
  <div style="display:flex;flex-direction: row">
    <admin_left/>
    <div class="main-container">
      <div class="chat-log-area" ref="chatLog">
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
<!--      发送框-->
      <div class="chat-input">
        <el-button
            type="primary"
            @click="back"
            class="back-button"
        >返回
        </el-button>
        <el-input
            v-model="chatParam.content"
            placeholder="请输入消息"
            type="textarea"
            :rows="2"
            class="chat-input-area"
        ></el-input>
        <el-button
            type="primary"
            @click="messageSend"
            :loading="sendLoading"
            class="chat-send"
        >发送
        </el-button>
      </div>
    </div>
  </div>
</template>

<script>
import {Notification} from "element-ui";
import admin_left from "@/components/admin_left.vue";
import {getChatInfo, userChatLog, chat} from "@/api/user_api";

export default {
  components: {
    admin_left
  },
  data() {
    return {
      admin: {
        adminId: parseInt(localStorage.getItem("id"))
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
  watch: {
    chatLogs() {
      this.$nextTick(() => {
        this.$refs.chatLog.scrollTop = this.$refs.chatLog.scrollHeight;
      });
    }
  },
  methods: {
    // 聊天记录排序，降序
    sortChatLog() {
      this.chatLogs.sort((a, b) => {
        return a.createTime - b.createTime;
      });
    },
    getChatLog() {
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
    getChatInfo() {
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
    back() {
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
  display: flex;
  flex-direction: column;
  align-items: center;
  width: 80%;
  margin-top: 5vh;
  margin-left: 20vh;
  margin-right: 20vh;
}


.chat-input {
  display: flex;
  align-items: center;
  margin-bottom: 40px;
  margin-top: 20px;
  width: 80%;
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

.chat-log-area {
  height: 80vh;
  overflow-y: auto;
  box-shadow: 0 0 10px #ccc;
  border-radius: 10px;
}

.chat-log {
  width: 90%;
  margin-left: 5%;
  flex-direction: column;
  align-items: flex-end;
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