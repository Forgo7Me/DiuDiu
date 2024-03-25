<template>
  <div style="display:flex;flex-direction: row">
    <fixer_left/>
    <div class="main-container">
      <el-descriptions title="用户信息" column="1" border style="font-size: 20px">
        <el-descriptions-item v-for="column in columns" :label="column.label" :key="column.prop">
          <el-icon :class="column.icon"></el-icon>
          <span>{{fixer[column.prop]}}</span>
        </el-descriptions-item>
      </el-descriptions>
    </div>
  </div>
</template>

<script>
import {Notification} from "element-ui";
import fixer_left from "@/components/fixer_left.vue";
import {fixerApi} from "@/api/fixer_api";

export default {
  components: {
    fixer_left
  },
  data() {
    return {
      fixer: {
        fixerId: localStorage.getItem("id"),
        identity: localStorage.getItem("identity")
      },
      columns:[
        { label: "工号" , prop: "id",icon: "el-icon-user"},
        { label: "姓名" , prop: "name",icon: "el-icon-user"},
        { label: "电话" , prop: "phone",icon: "el-icon-phone"},
        { label: "性别" , prop: "gender",icon: "el-icon-user"},
        { label: "接单数" , prop: "orderCount",icon: "el-icon-s-order"},
      ]
    }
  },
  created(){
    this.findFixer();
  },
  methods:{
    findFixer(){
      fixerApi.findFixer(this.fixer).then(response => {
        if (response.data.code === "SUCCESS") {
          this.fixer = response.data.data;
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "查询失败",
            message: response.data.msg,
            type: "error"
          });
        }
      });
    }
  },
}
</script>

<style>
.main-container{
  width: 60%;
  margin: 0 auto;
  align-content: center;
  font-size: 20px;
}
</style>