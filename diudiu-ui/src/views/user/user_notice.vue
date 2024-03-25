<template>
  <div style="display: flex;flex-direction: row">
    <User_left/>
    <div class="main-container">
      <el-table :data="notices" style="width: 100%; border-radius: 10px;overflow-y: auto" stripe border height="90vh">
        <el-table-column prop="notice.id" label="编号"></el-table-column>
        <el-table-column prop="notice.title" label="标题"></el-table-column>
        <el-table-column prop="admin.name" label="发布人"></el-table-column>
        <el-table-column prop="notice.createTime" label="发布时间">
          <template slot-scope="scope">
            {{ new Date(parseInt(scope.row.notice.createTime)).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="primary" size="mini" @click="openNotice(scope.row)">查看</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--    详情-->
    <el-dialog width="60%" :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="noticeVisible"
               title="通知">
      <div class="notice">
        <div class="notice-admin">
          <img :src="notice.admin.ava" alt="" class="notice-admin-img">
          <h1>发布人：{{ notice.admin.name }}</h1>
        </div>
        <div class="notice-context">
          <div class="notice-context-title">
            <h2>{{ notice.notice.title }}</h2>
            <h3>发布时间：{{ new Date(parseInt(notice.notice.createTime)).toLocaleString() }}</h3>
          </div>
          <div class="notice-context-context">
            <p>{{ notice.notice.context }}</p>
          </div>
        </div>
        <div class="notice-footer">
          <el-button type="primary" @click="noticeVisible = false">确定</el-button>
        </div>
      </div>
    </el-dialog>
  </div>

</template>


<script>
import User_left from "@/components/user_left.vue";
import {Api} from "@/api/Api";
import {Notification} from "element-ui";

export default {
  name: "Notice",
  components: {
    User_left
  },
  data() {
    return {
      notices: [
        {
          notice: {},
          admin: {}
        }
      ],
      notice: {
        notice: {},
        admin: {}
      },
      noticeVisible: false,
    }
  },
  created() {
    this.getAllNotice();
  },
  methods: {
    // 获取所有公告
    getAllNotice() {
      const param = {
        userId: parseInt(localStorage.getItem("id"))
      }
      Api.getNotice(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.notices = response.data.data;
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取公告失败",
            message: response.data.msg,
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录过期",
            message: "请重新登录",
            type: "warning"
          });
        }
      });
    },

    // 查看公告详情
    openNotice(row) {
      this.notice = row;
      this.noticeVisible = true;
    }


  }
}
</script>

<style scoped>
.main-container {
  width: 70%;
  margin: 0 auto;
  align-content: center;
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
  border-radius: 20px;
  padding: 10px;
  height: 25vh;
  overflow-y: auto;
}

.notice-footer {
  margin-top: 1vh;
  flex-direction: column;
  height: 10vh;
  display: flex;
  align-items: center;
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