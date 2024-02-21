<template>
  <div>
    <admin_left/>
    <div class="main-container">
      <el-descriptions title="管理员信息" column="1" border>
        <template slot="extra">
          <el-button type="primary">编辑</el-button>
        </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user-solid"></i>
            照片
          </template>
          <img :src="admin.ava" alt="" class="user-icon">
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            姓名
          </template>
          <el-tag type="success">{{ admin.name }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            性别
          </template>
          <el-tag v-if="admin.gender === '女'" type="danger">{{ admin.gender }}</el-tag>
          <el-tag v-else type="primary">{{ admin.gender }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号
          </template>
          <el-tag type="warning">{{ admin.phone }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在园区
          </template>
          <el-tag type="success">{{ admin.siteY }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在楼栋
          </template>
          <el-tag type="danger">{{ admin.siteD }}</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script>
import admin_left from "@/components/admin_left.vue";
import { findAdminById } from "@/api/admin_api";
import {Notification} from "element-ui";

export default {
  components: {
    admin_left
  },
  data() {
    return {
      admin:{adminId:1}
    }
  },
  created() {
    this.findAdmin();
  },
  methods:{
    findAdmin(){
      findAdminById(this.admin).then(response=>{
        if(response.data.code == "SUCCESS"){
          this.admin = response.data.data;
          Notification({
            title: "获取管理员信息成功",
            type: "success"
          });
        }else{
          Notification({
            title: "获取管理员信息失败",
            type: "error"
          });
        }
      })

    },

  }
}
</script>

<style scoped>
.main-container {
  width: 60%;
  margin: 0 auto;
  align-content: center;
  font-size: 20px;
}

.el-descriptions {
  margin: 0 auto;
  align-content: center;
  font-size: 20px;
}

.el-tag {
  font-size: 20px;
}

.user-icon {
  width: 100px;
  height: 100px;
  border-radius: 50px;
  margin: 0 auto;
  object-fit: cover;
}
.el-button{
  transition: all 0.5s;
}
.el-button:hover{
  transform: scale(1.1);
}
</style>