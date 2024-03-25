<template>
  <div style="display:flex;flex-direction: row">
    <admin_left/>
    <div class="main-container">
      <el-row style="margin-top: 10px; margin-bottom: 10px">
        <el-col :span="2">楼层号:</el-col>
        <el-col :span="4">
          <el-select v-model="filter.siteC" placeholder="请选择学生所在楼层" style="width:
         100%" @change="toFilterUsers">
            <el-option
                v-for="item in siteCs"
                :key="item"
                :label="item"
                :value="item"
            >
            </el-option>
          </el-select>
        </el-col>
        <el-col :span="2">房间号:</el-col>
        <el-col :span="4">
          <el-select v-model="filter.siteF" placeholder="请选择学生所在房间" style="width:
          100%" @change="toFilterUsers">
            <el-option
                v-for="item in siteFs"
                :key="item"
                :label="item"
                :value="item"
            >
            </el-option>
          </el-select>
        </el-col>
      </el-row>
      <el-table
          :data="filterUsers"
          style="width: 100%;border-radius: 10px"
          border
          height="90vh"
          stripe
      >
        <!--      使用el-tag作为template渲染属性,姓名(name,success),楼层号(siteC,primary),房间号(siteF,danger),性别(gender,使用v-if判断，男生为primary,女生为danger),手机号(phone,success),邮箱(email,primary)-->
        <el-table-column
            prop="name"
            label="姓名"
            
        >
          <template slot-scope="scope">
            <el-tag type="success">{{ scope.row.name }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="siteC"
            label="楼层"
            
        >
          <template slot-scope="scope">
            <el-tag type="primary">{{ scope.row.siteC }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="siteF"
            label="房间"
            
        >
          <template slot-scope="scope">
            <el-tag type="danger">{{ scope.row.siteF }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="gender"
            label="性别"
            
        >
          <template slot-scope="scope">
            <el-tag v-if="scope.row.gender === '女'" type="danger">{{ scope.row.gender }}</el-tag>
            <el-tag v-else type="primary">{{ scope.row.gender }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="phone"
            label="手机号"
            
        >
          <template slot-scope="scope">
            <el-tag type="success">{{ scope.row.phone }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column
            prop="email"
            label="邮箱"
            width="300"
        >
          <template slot-scope="scope">
            <el-tag type="primary">{{ scope.row.email }}</el-tag>
          </template>
        </el-table-column>
<!--        操作按钮"聊天",若未读消息数不为0则使用template在el-button上使用danger的el-tag展示未读消息数unCheckedMessageCount,为0则仅使用文字-->
        <el-table-column
            label="操作"
            
        >
          <template slot-scope="scope">
            <el-button type="primary" @click="toUserChat(scope.row.id)">聊天
              <template>
                <el-tag v-if="scope.row.unCheckedMessageCount !== 0" type="danger" class="unCheckedMessage">{{ scope.row.unCheckedMessageCount }}</el-tag>
              </template>
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
import {findUserByAdmin} from "@/api/admin_api";

export default {
  components: {
    admin_left
  },
  data() {
    return {
      admin: {
        adminId: parseInt(localStorage.getItem("id")),
      },
      users: [],
      filterUsers: [],
      filter: {
        siteC: '全部',
        siteF: '全部'
      },
      siteCs: ["全部"],
      siteFs: ["全部"]
    }
  },
  created() {
    this.findUserByAdmin();
  },
  methods: {
    findUserByAdmin() {
      findUserByAdmin(this.admin).then(res => {
        if (res.data.code === "SUCCESS") {
          this.users = res.data.data;
          this.filterUsers = this.users;
          // 遍历users数组，将siteC属性值存入siteCs数组,将siteF属性值存入siteFs数组,去重
          this.users.forEach(item => {
            if (!this.siteCs.includes(item.siteC)) {
              this.siteCs.push(item.siteC);
            }
            if (!this.siteFs.includes(item.siteF)) {
              this.siteFs.push(item.siteF);
            }
          });
        } else if (res.data.code === "ERROR") {
          Notification({
            title: "获取用户信息失败",
            type: "error"
          });
        } else if (res.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "warning"
          });
        } else if (res.data.code === "NOPOWER") {
          Notification({
            title: "权限不足",
            type: "warning"
          });
        }
      })
    },
    toFilterUsers() {
      // 考虑为''的情况，全部
      if (this.filter.siteC === "全部" && this.filter.siteF === "全部") {
        this.filterUsers = this.users;
      } else if (this.filter.siteC === "全部" && this.filter.siteF !== "全部") {
        this.filterUsers = this.users.filter(item => item.siteF === this.filter.siteF);
      } else if (this.filter.siteC !== "全部" && this.filter.siteF === "全部") {
        this.filterUsers = this.users.filter(item => item.siteC === this.filter.siteC);
      } else {
        this.filterUsers = this.users.filter(item => item.siteC === this.filter.siteC && item.siteF === this.filter.siteF);
      }
    },
    toUserChat(id) {
      localStorage.setItem("accepterId", id);
      this.$router.push({
        path: "/admin_chat",
      })
    }


  }
}
</script>

<style scoped>
.main-container {
  width: 70%;
  margin: 0 auto;
  align-content: center;
  font-size: 20px;
}

.el-tag {
  font-size: 20px;
}
.unCheckedMessage{
  border-radius: 50%;
}
</style>