<template>
  <div>
    <div>
      <user_left/>
    </div>
    <div class="main-container">
      <!--使用element-ui的el-descriptions将user除了userId的属性渲染在页面上(每个属性前需要带上一个el-icon)-->
      <el-descriptions title="我的信息" :column="1" class="margin-top" border>
        <template slot="extra">
          <el-button type="primary">编辑</el-button>
        </template>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user-solid"></i>
            照片
          </template>
          <img :src="user.ava" alt="" class="user-icon">
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user"></i>
            用户名
          </template>
          <el-tag type="danger">{{ user.name }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-user-solid"></i>
            性别
          </template>
          <!-- user.gender为女则使用el-tag的type为danger，为男则使用primary,使用v-if判断 -->
          <el-tag v-if="user.gender === '女'" type="danger">{{ user.gender }}</el-tag>
          <el-tag v-else type="primary">{{ user.gender }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在园区
          </template>
          <el-tag type="success">{{ user.siteY }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在楼栋
          </template>
          <el-tag type="danger">{{ user.siteD }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在楼层
          </template>
          <el-tag type="warning">{{ user.siteC }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-office-building"></i>
            所在房间
          </template>
          <el-tag type="info">{{ user.siteF }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-mobile-phone"></i>
            手机号
          </template>
          <el-tag type="success">{{ user.phone }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-message"></i>
            邮箱
          </template>
          <el-tag type="primary">{{ user.email }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item>
          <template slot="label">
            <i class="el-icon-s-flag"></i>
            详细住址
          </template>
          <el-tag type="primary">{{ user.siteY }}-{{ user.siteD }}栋-{{ user.siteC }}层-{{ user.siteF }}号</el-tag>
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script>
import user_left from "@/components/user_left.vue";
import {Notification} from "element-ui";
import {findUserById} from "@/api/user_api";

export default {
  components: {
    user_left
  },
  data() {
    return {
      user: {
        userId: 1,
        name: '平沢唯',  //姓名
        siteY: '樱高', //所在园区
        siteD: '1', //所在楼栋
        siteC: '1', //所在楼层
        siteF: '1', //所在房间
        gender: '', //性别
        phone: '', //电话
        email: ''  //邮箱
      }
    }
  },
  created() {
    this.findUser();
  },
  methods: {
    findUser() {
      findUserById(this.user).then(response => {
        if (response.data.code === "SUCCESS") {
          this.user = response.data.data;
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
  font-size: 20px;
}
.el-tag{
  font-size: 20px;
}
.user-icon{
  width: 100px;
  height: 100px;
  border-radius: 50%;
  display: block;
}
.el-button{
  transition: all 0.5s;
}
.el-button:hover{
  transform: scale(1.1);
}
</style>