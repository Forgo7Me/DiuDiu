<template>
  <div style="display: flex;flex-direction: row">
    <fixer_left/>
    <div class="main-container">
      <el-table :data="filterRecords" class="container-table" stripe border>
        <el-table-column prop="id" label="订单号"></el-table-column>
        <el-table-column prop="userName" label="用户姓名"></el-table-column>
        <el-table-column prop="userGender" label="用户性别"></el-table-column>
        <el-table-column prop="userPhone" label="用户电话"></el-table-column>
        <el-table-column prop="siteY" label="园区"></el-table-column>
        <el-table-column prop="siteD" label="楼栋"></el-table-column>
        <el-table-column prop="siteC" label="楼层"></el-table-column>
        <el-table-column prop="siteF" label="房间"></el-table-column>
        <el-table-column prop="state" label="状态">
          <template slot-scope="scope">
            <el-tag :type="FixStateTag(scope.row.state)">{{ FixStateString(scope.row.state) }}</el-tag>
          </template>
        </el-table-column>
        <!--        操作栏，详情按钮-->
        <el-table-column label="操作">
          <template slot-scope="scope">
            <el-button type="text" @click="toDetail(scope.row)">详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!--    详情dialog,点击旁边区域不自动关闭-->
    <el-dialog title="订单详情" :visible.sync="detailVisible" width="60%" :close-on-click-modal="false">
      <el-descriptions title="订单详情" column="1" border>
        <el-descriptions-item label="订单号">{{ detailRecord.id }}</el-descriptions-item>
        <el-descriptions-item label="用户姓名">{{ detailRecord.userName }}</el-descriptions-item>
        <el-descriptions-item label="用户性别">{{ detailRecord.userGender }}</el-descriptions-item>
        <el-descriptions-item label="用户电话">{{ detailRecord.userPhone }}</el-descriptions-item>
        <el-descriptions-item label="用户地址">
          {{ detailRecord.siteY }}-{{ detailRecord.siteD }}-{{ detailRecord.siteC }}-{{ detailRecord.siteF }}
        </el-descriptions-item>
        <el-descriptions-item label="订单描述">{{ detailRecord.description }}</el-descriptions-item>
        <el-descriptions-item label="创建时间">{{ new Date(parseInt(detailRecord.createTime)).toLocaleString() }}
        </el-descriptions-item>
      </el-descriptions>
      <span slot="footer" class="dialog-footer">
        <el-button @click="detailVisible = false;detailRecord = {}">取 消</el-button>
      </span>
    </el-dialog>

  </div>
</template>


<script>
import fixer_left from "@/components/fixer_left.vue";
import {fixerApi} from "@/api/fixer_api";
import {Notification} from "element-ui";
import {FixStateUtil} from "@/api/config";

export default {
  components: {
    fixer_left
  },
  data() {
    return {
      records: [
        {
          id: "",
          userName: "",
          userGender: "",
          userPhone: "",
          description: "",
          createTime: "",
          userAva: "",
          siteY: "",
          siteD: "",
          siteC: "",
          siteF: "",
          state: ""
        }
      ],
      filterRecords: [],
      detailVisible: false,
      detailRecord: {
        id: "",
        userName: "",
        userGender: "",
        userPhone: "",
        description: "",
        createTime: "",
        userAva: "",
        siteY: "",
        siteD: "",
        siteC: "",
        siteF: "",
        state: ""
      }
    }
  },
  created() {
    this.getFixRecord()
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

    // 获取维修员的订单
    getFixRecord() {
      const param = {
        identity: localStorage.getItem("identity"),
        fixerId: localStorage.getItem("id")
      }
      fixerApi.getFixerOrder(param).then(res => {
        if (res.data.code === "SUCCESS") {
          this.records = res.data.data
          this.filterRecords = this.records
        } else {
          Notification({
            title: "查询失败",
            message: res.data.msg,
            type: "error"
          });
        }
      });
    },

    // 详情按钮
    toDetail(row) {
      this.detailRecord = row
      this.detailVisible = true
    }
  }
}

</script>

<style scoped>
.main-container {
  width: 80%;
  height: 95vh;
  margin-top: 2vh;
}

.container-table {
  overflow-y: auto;
  height: 90vh;
  border-radius: 10px;
}
</style>