<template>
  <div style="display:flex;flex-direction: row">
    <user_left/>
    <div class="main-container">
      <!--      上方加入筛选框和搜索栏，筛选框位于左侧，可筛选报修处理状态，搜索框位于右侧，绑定searchFixLog方法-->
      <el-row style="margin-top: 10px; margin-bottom: 10px">
        <el-col :span="4">
          <el-select v-model="fixState" placeholder="请选择报修处理状态" style="width:
          100%" @change="filterFix">
            <el-option
                v-for="item in fixStates"
                :key="item.value"
                :label="item.label"
                :value="item.value"
            >
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <!--    使用el-table展示fixLogs的内容，包括处理人(adminName),处理状态(state),报修描述(description),保修时间(createTime),开始处理时间(takeTime),完成时间(finishTime),要求使用固定表头和斑马表格-->
      <el-table
          :data="filterFixLogs"
          style="width: 100%; border-radius: 10px"
          stripe
          border
          height="90vh"
          header-cell-class-name="table-header"
          v-loading="loading"
      >
        <el-table-column
            prop="adminName"
            label="处理人"
        ></el-table-column>
        <!--        使用el-tag展示,若处理状态为"未开始"则使用type为danger,处理状态为"处理中"则为primary,处理状态为"已完成"则为success-->
        <el-table-column
            prop="state"
            label="处理状态">
          <template slot-scope="scope">
            <el-tag :type="FixStateTag(scope.row.state)">{{ FixStateString(scope.row.state) }}
            </el-tag>
          </template>
        </el-table-column>
<!--        为报修描述加上高度限制，最高10vh,超出部分overflow-->
        <el-table-column
            prop="description"
            label="报修描述"
        >
<!--          使用text-area来防止高度过高-->
          <template slot-scope="scope">
            <p style="max-height: 10vh;overflow: auto">{{ scope.row.description }}</p>
          </template>
        </el-table-column>
        <el-table-column
            prop="createTime"
            label="报修时间">
          <template slot-scope="scope">
            {{ timeFormat(scope.row.createTime) }}
          </template>
        </el-table-column>
        <el-table-column
            prop="takeTime"
            label="开始处理时间">
          <template slot-scope="scope">
            <el-tag v-if="scope.row.takeTime === '0'" type="danger">未开始</el-tag>
            <el-tag v-else type="primary">{{ timeFormat(scope.row.takeTime) }}</el-tag>
          </template>

        </el-table-column>
        <el-table-column
            prop="finishTime"
            label="完成时间"
            >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.finishTime === '0'" type="danger">未完成</el-tag>
            <el-tag v-else type="success">{{ timeFormat(scope.row.finishTime) }}</el-tag>
          </template>
        </el-table-column>
<!--        操作栏，若state为"处理中"则显示一个success的"完成报修"按钮，其他情况下不显示-->
        <el-table-column
            label="操作"
            >
          <template slot-scope="scope">
            <el-button v-if="scope.row.state === 2" type="success" @click="finishFix(scope.row.id)">完成报修</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script>
import user_left from "@/components/user_left.vue";
import {Notification} from "element-ui";
import {findFixLog,finishFix} from "@/api/user_api";
import {FixStateUtil} from "@/api/config";

export default {
  components: {
    user_left
  },
  data() {
    return {
      user: {userId: parseInt(localStorage.getItem("id"))},
      fixLogs: [],
      filterFixLogs: [],
      fixStates: [],
      fixState: "",
      searchContent: "",
      visible: {
        finishVisible: false
      },
      fixId: ''
    }
  },
  created() {
    this.findFixLog();
    this.fixStates = FixStateUtil;
    // 将全部添加到fixStates中，且放在第一个
    this.fixStates.unshift({label: "全部", value: "全部"});
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
    // 时间格式化，将13位string类型的时间戳转换为yyyy-MM-dd HH:mm:ss格式
    timeFormat(param) {
      const date = new Date(parseInt(param));
      const y = date.getFullYear();
      const m = date.getMonth() + 1;
      const d = date.getDate();
      const h = date.getHours();
      const mm = date.getMinutes();
      const s = date.getSeconds();
      return y + '-' + m + '-' + d + ' ' + h + ':' + mm + ':' + s;
    }
    ,
    findFixLog() {
      const param = {
        userId: this.user.userId,
        search : this.searchContent
      };
      findFixLog(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.fixLogs = response.data.data;
          this.filterFixLogs = this.fixLogs;
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
    filterFix() {
      if (this.fixState === "全部") {
        this.filterFixLogs = this.fixLogs;
      } else {
        this.filterFixLogs = this.fixLogs.filter(item => item.state === this.fixState);
      }
    },
    finishFix(id) {
      this.$confirm("确定完成报修吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        const param = {
          id: id
        };
        finishFix(param).then(response => {
          if (response.data.code === "SUCCESS") {
            Notification({
              title: "完成报修",
              type: "success"
            });
            this.findFixLog();
          } else if (response.data.code === "ERROR") {
            Notification({
              title: "完成报修失败",
              type: "error"
            });
          } else if (response.data.code === "TIMEOUT") {
            Notification({
              title: "登录信息已过期，请重新登录",
              type: "warning"
            });
          }
        });
      });
    },


  }
}

</script>

<style scoped>
.main-container {
  width: 70%;
  margin: 0 auto;
  align-content: center;
}


</style>