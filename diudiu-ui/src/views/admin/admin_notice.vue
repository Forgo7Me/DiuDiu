<template>
  <div style="display: flex;flex-direction: row;">
    <admin_left/>
    <div class="main-container">
      <el-table :data="notices" style="width: 100%; border-radius: 10px;overflow-y: auto" stripe border height="90vh">
        <el-table-column prop="notice.id" label="编号"></el-table-column>
        <el-table-column prop="notice.title" label="标题"></el-table-column>
        <el-table-column prop="notice.createTime" label="发布时间">
          <template slot-scope="scope">
            {{ new Date(parseInt(scope.row.notice.createTime)).toLocaleString() }}
          </template>
        </el-table-column>
        <el-table-column prop="confirmUserCount" label="确认人数"></el-table-column>
        <el-table-column label="查看率">
<!--          使用el-tag,confirmRate在50以下为danger,50~70为warning,70以上为success-->
          <template slot-scope="scope">
            <el-tag v-if="parseFloat(scope.row.confirmRate) < 50" type="danger">{{ scope.row.confirmRate }}</el-tag>
            <el-tag v-else-if="parseFloat(scope.row.confirmRate) >= 50 && parseFloat(scope.row.confirmRate) < 70"
                    type="warning">{{ scope.row.confirmRate }}</el-tag>
            <el-tag v-else type="success">{{ scope.row.confirmRate }}</el-tag>
          </template>zx
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
          <el-button type="primary" @click="editNotice">编辑</el-button>
          <el-button type="danger" @click="noticeVisible = false">取消</el-button>
        </div>
      </div>
    </el-dialog>

    <!--    编辑公告-->
    <el-dialog width="60%" :close-on-click-modal="false" :close-on-press-escape="false" :visible.sync="editVisible"
               title="编辑公告">
      <div class="notice">
        <div class="notice-admin">
          <img :src="notice.admin.ava" alt="" class="notice-admin-img">
          <h1>发布人：{{ notice.admin.name }}</h1>
        </div>
        <div class="notice-context">
          <div class="notice-context-title">
            <h2>{{ notice.notice.title }}</h2>
            <h3>发布时间：{{ new Date(parseInt(notice.notice.createTime)).toLocaleString() }}</h3>
            <el-radio-group v-model="notice.notice.confirm" size="small">
              <el-radio label="是">需要确认</el-radio>
              <el-radio label="否">不需要确认</el-radio>
            </el-radio-group>
          </div>
          <div class="notice-context-context">
            <el-input v-model="notice.notice.context" type="textarea" placeholder="请输入内容" autosize></el-input>
          </div>
        </div>
        <div class="notice-footer">
          <el-button type="primary" @click="confirmEdit">更新</el-button>
          <el-button type="danger" @click="editVisible = false">取消</el-button>
        </div>
      </div>
    </el-dialog>

  </div>
</template>


<script>
import admin_left from "@/components/admin_left.vue";
import {Notification} from "element-ui";
import {Api} from "@/api/Api";

export default {
  components: {
    admin_left
  },
  data() {
    return {
      notices: [
        {
          notice: {},
          admin: {},
          confirmUserCount: 0, // 确认人数
          userCount: 0 // 用户总数
        }
      ],
      notice: {
        notice: {},
        admin: {},
        confirmUserCount: 0, // 确认人数
        userCount: 0 // 用户总数
      },
      noticeVisible: false,
      editVisible: false
    }
  },
  created() {
    this.getAllNotice();
  },
  methods: {
    // 获取该管理员发布的所有公告
    getAllNotice() {
      const param = {
        adminId: parseInt(localStorage.getItem("id"))
      }
      Api.getNoticeByAdmin(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.notices = response.data.data;
          // 迭代,为notice添加收到率
          this.notices.forEach(item => {
            item.confirmRate = (item.confirmUserCount / item.userCount * 100).toFixed(2) + "%";
          })
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

    // 打开公告
    openNotice(row) {
      this.notice = row;
      this.noticeVisible = true;
    },

    // 编辑公告
    editNotice() {
      this.noticeVisible = false;
      this.editVisible = true;
    },

    // 确认编辑
    confirmEdit(){
      const param = {
        noticeId: this.notice.notice.id,
        context: this.notice.notice.context,
        title: this.notice.notice.title,
        confirm: this.notice.notice.confirm
      }
      Api.editNotice(param).then(response => {
        if (response.data.code === "SUCCESS") {
          Notification({
            title: "编辑成功",
            type: "success"
          });
          this.editVisible = false;
          this.getAllNotice();
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "编辑失败",
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
  margin-top: 2vh;
  box-shadow: 4px 4px 10px #d1d9e6, -4px -4px 10px #f9f9f9;
  border-radius: 20px;
  width: 100%;
  padding: 10px;
  height: 25vh;
  overflow-y: auto;
}


.notice-footer {
  margin-top: 1vh;
  flex-direction: row;
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