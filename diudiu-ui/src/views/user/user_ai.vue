<template>
  <div style="display:flex;flex-direction: row">
    <div>
      <user_left/>
    </div>
    <div class="main-container" >
      <div class="chat-log-area" ref="chatLog">
      <div class="chat-log" v-for="log in chatLogs" :key="log.id">
        <!-- User's question -->
        <div class="chat-message user-message">
          <div class="message-time">{{ new Date(log.createTime).toLocaleString() }}</div>
          <div class="message-content">{{ log.content }}</div>
          <img class="avatar" :src="user.ava" alt="User Avatar">
        </div>
        <!-- AI's answer -->
        <div class="chat-message ai-message">
          <img class="avatar" src="http://ayaka.ayaka.fun/diudiu/user/user_2_1.jpg" alt="AI Avatar">
          <div class="message-content">{{ log.answer }}</div>
          <div class="message-time">{{ new Date(log.createTime).toLocaleString() }}</div>
        </div>
      </div>
      </div>
      <!--        问题发送框，固定于容器底部-->
      <div class="chat-input">
        <el-input
            v-model="chatParam.content"
            placeholder="请输入问题"
            type="textarea"
            :rows="2"
            class="chat-input-area"
        ></el-input>
        <el-button
            type="primary"
            @click="aiChat"
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
import user_left from "@/components/user_left.vue";
import {findUserById, getAiChatLog, aiChat} from "@/api/user_api";

export default {
  components: {
    user_left
  },
  data() {
    return {
      user: {
        userId: parseInt(localStorage.getItem("id")),
      },
      chatParam: {
        userId: parseInt(localStorage.getItem("id")),
        content: "",
      },
      chatLogs: [],
      loading: {
        containerLoading: false,
      },
      sendLoading: false,
    }
  },
  created() {
    this.getChatLog();
    this.findUserById();
  },
  watch: {
    chatLogs() {
      this.$nextTick(() => {
        this.$refs.chatLog.scrollTop = this.$refs.chatLog.scrollHeight;
      });
    }
  },
  methods: {
    findUserById() {
      const param = {
        userId: this.user.userId,
      };
      findUserById(param).then(response => {
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
    getChatLog() {
      const param = {
        userId: this.user.userId,
      };
      getAiChatLog(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.chatLogs = response.data.data;
          this.sortLogs();
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
    aiChat() {
      this.sendLoading = true;
      aiChat(this.chatParam).then(response => {
        if (response.data.code === "SUCCESS") {
          this.chatLogs.push(response.data.data);
          this.sortLogs();
          this.chatParam.content = "";
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "发送消息失败",
            message: response.data.msg,
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
    },
    sortLogs() {
      // 将聊天记录按照id排序，保证最新的消息在最上面(id越大，消息越新)
      this.chatLogs.sort((a, b) => a.id - b.id);
    }
  }
}
</script>

<style scoped>
.main-container {
  margin-left: 200px;
  padding: 20px;
}
.chat-log-area{
  border-radius: 10px;
  box-shadow: 0 0 10px #ccc;
  height: 87vh;
  overflow-y: auto;
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
  width: 80%;
  //display: flex;
  flex-direction: column;
  align-items: flex-end;
  margin-top: 20px;
  margin-left: 10%;
}

.chat-message {
  display: flex;
  align-items: center;
  margin: 10px 0;
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