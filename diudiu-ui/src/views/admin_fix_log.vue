<template>
  <div>
    <admin_left/>
    <div class="main-container">
<!--      筛选报修状态和报修人的筛选框-->
      <el-row style="margin-top: 10px; margin-bottom: 10px">
        <el-col :span="2">报修状态</el-col>
        <el-col :span="4">
          <el-select v-model="filter.fixState" placeholder="请选择报修处理状态" style="width:
          100%" @change="filterLogs">
            <el-option
                v-for="item in filterContent.fixStates"
                :key="item"
                :label="item"
                :value="item"
            >
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="2">报修人</el-col>
        <el-col :span="4">
          <el-select v-model="filter.fixUser" placeholder="请选择报修人" style="width:
          100%" @change="filterLogs">
            <el-option
                v-for="item in filterContent.fixUsers"
                :key="item"
                :label="item"
                :value="item"
            >
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-table
          :data="filterFixLogs"
          border
          height="95vh"
          style="width: 100%">
        <el-table-column
            prop="id"
            label="报修编号"
            width="180"
        >
          <template slot-scope="scope">
            <el-tag type="success">{{ scope.row.id }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="userName"
            label="报修人"
            width="180"
        >
          <template slot-scope="scope">
            <el-tag type="primary">{{ scope.row.userName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="description"
            label="报修描述"
            width="180"
        >
          <template slot-scope="scope">
            <el-tag type="danger">{{ scope.row.description }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="报修时间"
            width="180"
        >
          <template slot-scope="scope">
            <el-tag type="success">{{ timeFormat(scope.row.createTime) }}</el-tag>
          </template>
        </el-table-column>

<!--        开始处理时间takeTime，不为0则显示格式化的时间(primary),为0则显示未开始(danger)-->
        <el-table-column
            prop="takeTime"
            label="开始处理时间"
            width="180"
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.takeTime !== '0'" type="primary">{{ timeFormat(scope.row.takeTime) }}</el-tag>
            <el-tag v-else type="danger">未开始</el-tag>
          </template>
        </el-table-column>
<!--        完成时间finishTime，不为0则显示格式化的时间(success),为0则显示未完成(danger)-->
        <el-table-column
            prop="finishTime"
            label="完成时间"
            width="180"
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.finishTime !== '0'" type="success">{{ timeFormat(scope.row.finishTime) }}</el-tag>
            <el-tag v-else type="danger">未完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="state"
            label="报修状态"
            width="180"
        >
          <template slot-scope="scope">
            <!--          若state为"未处理"则使用danger的el-tag,"处理中"为primary,"已完成"为success-->
            <el-tag v-if="scope.row.state === '未处理'" type="danger">{{ scope.row.state }}</el-tag>
            <el-tag v-else-if="scope.row.state === '处理中'" type="primary">{{ scope.row.state }}</el-tag>
            <el-tag v-else type="success">{{ scope.row.state }}</el-tag>
          </template>
        </el-table-column>
        <!--  操作栏，若state为"未处理"则显示一个"开始处理"按钮，其他情况下不显示该按钮-->
        <el-table-column
            label="操作"
            width="180">
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === '未处理'" type="primary" @click="clickStart(scope.row.id)">开始处理
            </el-button>
          </template>
        </el-table-column>
      </el-table>
<!--      开始处理的el-dialog，点击开始处理后弹出是否确认开始处理，点击确认后将id传入指定方法-->
      <el-dialog title="确定开始处理?" :visible.sync="visible.startFixVisible" width="300px">
        <el-button type="primary" @click="startFixLog">确认</el-button>
        <el-button @click="visible.startFixVisible = false">取消</el-button>
      </el-dialog>
    </div>
  </div>
</template>

<script>
import admin_left from "@/components/admin_left.vue";
import {Notification} from "element-ui";
import {findFixLogByAdmin,startFix} from "@/api/admin_api"

export default {
  components: {
    admin_left
  },
  data() {
    return {
      fixLogs: [],
      filterFixLogs: [],
      admin: {
        adminId: 1
      },
      visible: {
        startFixVisible: false
      },
      fixId: '',
      filter: {
        fixState: '全部',
        fixUser: '全部'
      },
      filterContent: {
        fixStates: [
          "全部",
          "未处理",
          "处理中",
          "已完成"
        ],
        fixUsers: ["全部"]
      }
    }
  },
  created() {
    this.findFixLogByAdmin();
  },
  methods: {
    findFixLogByAdmin() {
      findFixLogByAdmin(this.admin).then(response => {
        if (response.data.code === "SUCCESS") {
          this.fixLogs = response.data.data;
          this.filterFixLogs = response.data.data;
          // 将userName去重后放入filterContent.fixUsers
          this.fixLogs.forEach(item => {
            if (!this.filterContent.fixUsers.includes(item.userName)) {
              this.filterContent.fixUsers.push(item.userName);
            }
          });
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "获取报修信息失败",
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
    timeFormat(time) {
      const date = new Date(parseInt(time));
      const year = date.getFullYear();
      const month = date.getMonth() + 1;
      const day = date.getDate();
      const hour = date.getHours();
      const minute = date.getMinutes();
      const second = date.getSeconds();
      return year + "-" + month + "-" + day + " " + hour + ":" + minute + ":" + second;
    },
    clickStart(id) {
      this.visible.startFixVisible = true;
      this.fixId = id;
    },
    startFixLog() {
      this.visible.startFixVisible = false;
      const param = {
        id: this.fixId
      }
      startFix(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.findFixLogByAdmin();
          Notification({
            title: "成功",
            message: "开始处理成功",
            type: "success"
          });

        } else if (response.data.code === "ERROR") {
          Notification({
            title: "失败",
            message: "开始处理失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "错误",
            message: "登录信息已过期，请重新登录",
            type: "warning"
          });
        }
      });
    },
    filterLogs() {
      if (this.filter.fixState === "全部" && this.filter.fixUser === "全部") {
        this.filterFixLogs = this.fixLogs;
      } else if (this.filter.fixState === "全部" && this.filter.fixUser !== "全部") {
        this.filterFixLogs = this.fixLogs.filter(item => item.userName === this.filter.fixUser);
      } else if (this.filter.fixState !== "全部" && this.filter.fixUser === "全部") {
        this.filterFixLogs = this.fixLogs.filter(item => item.state === this.filter.fixState);
      } else {
        this.filterFixLogs = this.fixLogs.filter(item => item.state === this.filter.fixState && item.userName === this.filter.fixUser);
      }
    }

  }
}
</script>

<style scoped>
.main-container {
  width: 80%;
  margin: 0 auto;
  align-content: center;
  font-size: 20px;
}
</style>