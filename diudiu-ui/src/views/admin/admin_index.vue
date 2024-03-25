<template>
  <div style="display:flex;flex-direction: row">
    <admin_left/>
    <div class="main-container">
      <el-descriptions title="管理员信息" column="1" border>
        <template slot="extra">
          <el-button type="primary" @click="addNotice">发布公告</el-button>
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
<!--    发布公告的dialog,可输入标题title,内容context,选择是否需要确认收到confirm(radio,是或否)-->
    <el-dialog title="发布公告" :visible.sync="noticeVisible" width="30%" :close-on-click-modal="false" :close-on-press-escape="false">
      <el-form :model="newNotice" :rules="newNoticeRules" ref="newNoticeForm" label-width="80px">
        <el-form-item label="标题" prop="title">
          <el-input v-model="newNotice.title"></el-input>
        </el-form-item>
        <el-form-item label="内容" prop="context" style="height: 10vh">
          <el-input type="textarea" v-model="newNotice.context" style="height: 10vh"></el-input>
        </el-form-item>
        <el-form-item label="是否需要确认收到" prop="confirm">
          <el-radio-group v-model="newNotice.confirm">
            <el-radio label="是">是</el-radio>
            <el-radio label="否">否</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitNotice">发布</el-button>
          <el-button @click="noticeVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </div>
</template>

<script>
import admin_left from "@/components/admin_left.vue";
import { findAdminById } from "@/api/admin_api";
import {Notification} from "element-ui";
import {Api} from "@/api/Api";

export default {
  components: {
    admin_left
  },
  data() {
    return {
      admin:{adminId:parseInt(localStorage.getItem("id"))},
      newNotice:{
        title:"",
        context:"",
        confirm:"",
        adminId:parseInt(localStorage.getItem("id"))
      },
      newNoticeRules:{
        title:[
          {required:true,message:"请输入标题",trigger:"blur"}
        ],
        context:[
          {required:true,message:"请输入内容",trigger:"blur"}
        ],
        confirm:[
          {required:true,message:"请选择是否需要确认",trigger:"blur"}
        ]
      },
      noticeVisible:false
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

    // 发布公告
    addNotice(){
      this.newNotice = {
        title:"",
        context:"",
        confirm:"",
        adminId:parseInt(localStorage.getItem("id"))
      }
      this.noticeVisible = true;
    },

    // 提交公告
    submitNotice(){
      this.$refs.newNoticeForm.validate(valid => {
        if(valid){
          this.$confirm("确定发布公告吗?","提示",{
            confirmButtonText:"确定",
            cancelButtonText:"取消",
            type:"warning"
          }).then(()=>{
            Api.addNotice(this.newNotice).then(response=>{
              if(response.data.code === "SUCCESS"){
                Notification({
                  title:"发布成功",
                  type:"success"
                });
                this.noticeVisible = false;
              }else if(response.data.code === "ERROR"){
                Notification({
                  title:"发布失败",
                  message:response.data.msg,
                  type:"error"
                });
              }else if(response.data.code === "TIMEOUT"){
                Notification({
                  title:"登录信息已过期，请重新登录",
                  type:"warning"
                });
              }
            });
          }).catch(()=>{
            this.$message({
              type:"info",
              message:"已取消发布"
            });
          });
        }else{
          this.$message({
            type:"error",
            message:"请检查输入"
          });
          return false;
        }
      });
    }

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