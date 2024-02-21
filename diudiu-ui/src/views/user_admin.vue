<template>
  <div>
    <user_left/>
    <div class="main-container">
      <el-descriptions title="管理员信息" column="1" border>
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
      <!--      下方加上三个均匀分布的el按钮，分别为"报修","联系","投诉"-->
      <div class="button">
        <el-button type="primary" @click="visible.fixVisible = true" class = "fun-button">报修</el-button>
        <el-button type="success" class = "fun-button">联系</el-button>
        <el-button type="danger" class = "fun-button">投诉</el-button>
      </div>
      <!--      报修面板，使用el-dialog嵌套form,放入一个输入框，提示“请详细输入报修内容”，下方放置“确定”和“取消”按钮，确定按钮绑定"addFixLog"方法，取消让el-dialog再次隐藏-->
      <el-dialog title="报修" :visible.sync="visible.fixVisible">
        <el-form :model="fixLog" :rules="rules" ref="fixLog" label-width="80px">
          <el-form-item label="报修内容" prop="content">
            <el-input v-model="fixLog.description" placeholder="请详细输入报修内容" type="textarea"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="addFixLog">确定</el-button>
            <el-button @click="visible.fixVisible = false">取消</el-button>
          </el-form-item>
        </el-form>
      </el-dialog>


    </div>

  </div>
</template>

<script>
import user_left from "@/components/user_left.vue";
import {Notification} from "element-ui";
import {findAdmin,addFixLog} from "@/api/user_api";

export default {
  components: {
    user_left
  },
  data() {
    return {
      user: {
        userId: 1,
      },
      admin: {},
      visible: {
        fixVisible: false,
      },
      fixLog: {
        description: "",
      },
    }
  },
  created() {
    this.findAdmin();
  },
  methods: {
    findAdmin() {

      findAdmin(this.user).then(response => {
        if (response.data.code === "SUCCESS") {
          this.admin = response.data.data;
          Notification({
            title: "获取用户信息成功",
            type: "success"
          });
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
    addFixLog(){
      const param = {
        userId: this.user.userId,
        description: this.fixLog.description,
        adminId: this.admin.id
      }
      addFixLog(param).then(response => {
        if (response.data.code === "SUCCESS") {
          Notification({
            title: "报修成功，我们会尽快处理",
            type: "success"
          });
          this.visible.fixVisible = false;
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "报修失败",
            type: "error"
          });
        } else if (response.data.code === "TIMEOUT") {
          Notification({
            title: "登录信息已过期，请重新登录",
            type: "warning"
          });
        }
      });
    }



  },
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

.button {
  display: flex;
  justify-content: space-around;
  margin-top: 20px;
}

.fun-button {
  font-size: 30px;
  width: 200px;
  height: 50px;
  border-radius: 100px;
  transition: all 0.5s;
}

.fun-button:hover {
  font-size: 35px;
  width: 300px;
  height: 75px;
  border-radius: 125px;
}

</style>