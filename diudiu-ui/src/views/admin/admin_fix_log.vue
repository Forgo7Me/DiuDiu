<template>
  <div style="display:flex;flex-direction: row">
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
                :label="item.label"
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
<!--      为el-table-column添加高度限制-->
      <el-table
          :data="filterFixLogs"
          border
          height="90vh"
          style="width: 100%;border-radius: 10px"
          header-cell-class-name="table-header">
        <el-table-column
            prop="id"
            label="报修编号"
        >
          <template slot-scope="scope">
            <el-tag type="success">{{ scope.row.id }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="userName"
            label="报修人"
        >
          <template slot-scope="scope">
            <el-tag type="primary">{{ scope.row.userName }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="description"
            label="报修描述"
        >
          <template slot-scope="scope">
            <el-tag type="danger">{{ scope.row.description }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="报修时间"
            
        >
          <template slot-scope="scope">
            <el-tag type="success">{{ timeFormat(scope.row.createTime) }}</el-tag>
          </template>
        </el-table-column>

<!--        开始处理时间takeTime，不为0则显示格式化的时间(primary),为0则显示未开始(danger)-->
        <el-table-column
            prop="takeTime"
            label="开始处理时间"
            
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
            
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.finishTime !== '0'" type="success">{{ timeFormat(scope.row.finishTime) }}</el-tag>
            <el-tag v-else type="danger">未完成</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="state"
            label="报修状态"
            
        >
          <template slot-scope="scope">
            <el-tag :type="FixStateTag(scope.row.state)">{{ FixStateString(scope.row.state) }}</el-tag>
          </template>
        </el-table-column>
        <!--  操作栏，若state为"未处理"则显示一个"开始处理"按钮，其他情况下不显示该按钮-->
        <el-table-column
            label="操作"
            >
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === 0" type="primary" @click="pass(scope.row.id)">审核通过
            </el-button>
            <el-button v-if="scope.row.state === 0" type="danger" @click="reject(scope.row.id)">审核不通过
            </el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import admin_left from "@/components/admin_left.vue";
import {Notification} from "element-ui";
import {findFixLogByAdmin,pass,reject} from "@/api/admin_api"
import {FixStateUtil} from "@/api/config";

export default {
  components: {
    admin_left
  },
  data() {
    return {
      fixLogs: [],
      filterFixLogs: [],
      admin: {
        adminId: parseInt(localStorage.getItem("id"))
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
        fixStates: [],
        fixUsers: ["全部"]
      }
    }
  },
  created() {
    this.findFixLogByAdmin();
    this.filterContent.fixStates = FixStateUtil;
  },
  methods: {
    FixStateTag(param){
      // param对应FixStateUtil的value,返回对应的el-tag的type
      return FixStateUtil.find(item => item.value === param).type;
    },
    FixStateString(param){
      // param对应FixStateUtil的value,返回对应的el-tag的type
      return FixStateUtil.find(item => item.value === param).label;
    },
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
    filterLogs() {
      if (this.filter.fixState.label === "全部" && this.filter.fixUser === "全部") {
        this.filterFixLogs = this.fixLogs;
      } else if (this.filter.fixState.label === "全部" && this.filter.fixUser !== "全部") {
        this.filterFixLogs = this.fixLogs.filter(item => item.userName === this.filter.fixUser);
      } else if (this.filter.fixState.label !== "全部" && this.filter.fixUser === "全部") {
        this.filterFixLogs = this.fixLogs.filter(item => item.state === this.filter.fixState.value);
      } else {
        this.filterFixLogs = this.fixLogs.filter(item => item.state === this.filter.fixState.value && item.userName === this.filter.fixUser);
      }
    },

    // 审核通过
    pass(id){
      const param = {
        id : id
      }
      this.$confirm("确定审核通过吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        pass(param).then(res => {
          if (res.data.code === "SUCCESS") {
            Notification.success({
              title: "成功",
              message: "审核通过"
            })
            this.findFixLogByAdmin()
          } else {
            Notification.error({
              title: "错误",
              message: "服务器错误，请等待恢复"
            })
          }
        })
      }).catch(() => {

      });
    },

    // 审核不通过
    reject(id){
      const param = {
        id : id
      }
      this.$confirm("确定审核不通过吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        reject(param).then(res => {
          if (res.data.code === "SUCCESS") {
            Notification.success({
              title: "成功",
              message: "审核不通过"
            })
            this.findFixLogByAdmin()
          } else {
            Notification.error({
              title: "错误",
              message: "服务器错误，请等待恢复"
            })
          }
        })
      }).catch(() => {

      });
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