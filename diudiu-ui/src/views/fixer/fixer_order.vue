<template>
<div style="display: flex;flex-direction: row">
  <fixer_left/>
  <div class="main-container">
    <div class="order-item" v-for="order in orders">
      <div class="order-item-info">
        <img :src="order.userAva" alt="order.id" class="order-item-info-img">
        <p>{{ order.userName }}</p>
        <p style="font-size: 10px;margin: 1px">园区:{{ order.siteY }}</p>
        <p style="font-size: 10px;margin: 1px">楼栋:{{ order.siteD }}</p>
        <p style="font-size: 10px;margin: 1px">楼层:{{ order.siteC }}</p>
        <p style="font-size: 10px;margin: 1px">房间:{{ order.siteF }}</p>
        <el-button type="primary" @click="toDetail(order)">详情</el-button>
      </div>
      <div class="order-item-des">
        <p>{{order.description}}</p>
      </div>
    </div>
  </div>

<!--  详情dialog,点击详情后让该dialog可见，使用el-descriptions展示order的所有信息，下方加上"接取"和取消按钮-->
  <el-dialog title="订单详情" :visible.sync="detailVisible" width="60%" :close-on-click-modal="false">
    <el-descriptions title="订单详情" column="1" border>
      <el-descriptions-item label="订单号">{{detailOrder.id}}</el-descriptions-item>
      <el-descriptions-item label="用户姓名">{{detailOrder.userName}}</el-descriptions-item>
      <el-descriptions-item label="用户性别">{{detailOrder.userGender}}</el-descriptions-item>
      <el-descriptions-item label="用户电话">{{detailOrder.userPhone}}</el-descriptions-item>
      <el-descriptions-item label="用户地址">{{detailOrder.siteY}}-{{detailOrder.siteD}}-{{detailOrder.siteC}}-{{detailOrder.siteF}}</el-descriptions-item>
      <el-descriptions-item label="订单描述">{{detailOrder.description}}</el-descriptions-item>
<!--      将创建时间格式化-->
      <el-descriptions-item label="创建时间">{{new Date(parseInt(detailOrder.createTime)).toLocaleString()}}</el-descriptions-item>
    </el-descriptions>
    <span slot="footer" class="dialog-footer">
      <el-button @click="detailVisible = false;detailOrder = {}">取 消</el-button>
      <el-button type="primary" @click="acceptOrder">接 取</el-button>
    </span>
  </el-dialog>
</div>

</template>


<script>
import fixer_left from "@/components/fixer_left.vue";
import {fixerApi} from "@/api/fixer_api";
import {Notification} from "element-ui";

export default {
  components: {
    fixer_left
  },
  data() {
    return {
      orders: [
        {
          id: "",
          userName: "",
          userGender: "",
          userPhone: "",
          description: "",
          createTime: "",
          userAva:"",
          siteY: "",
          siteD: "",
          siteC: "",
          siteF: ""
        }
      ],
      filterOrders: [],
      detailVisible: false,
      detailOrder: {
        id: "",
        userName: "",
        userGender: "",
        userPhone: "",
        description: "",
        createTime: "",
        userAva:"",
        siteY: "",
        siteD: "",
        siteC: "",
        siteF: ""
      }
    }
  },
  created() {
    this.getFixOrder()
  },
  methods: {
    getFixOrder() {
      const param = {
        identity: localStorage.getItem("identity")
      }
      fixerApi.getFixOrder(param).then(res => {
        if (res.data.code === "SUCCESS") {
          this.orders = res.data.data
          this.filterOrders = this.orders
        } else if (res.data.code === "TIMEOUT") {
          Notification.error({
            title: "错误",
            message: "登录过期，请重新登录"
          })
        } else {
          Notification.error({
                title: "错误",
                message: "服务器错误，请等待恢复"
              }
          )
        }
      })
    },

    toDetail(order){
      this.detailOrder = order
      this.detailVisible = true
    },

    // 接取订单
    acceptOrder(){
      const param = {
        fixerId: localStorage.getItem("id"),
        identity: localStorage.getItem("identity"),
        orderId: this.detailOrder.id.toString()
      }
      this.$confirm("确定接取订单吗?", "提示", {
        confirmButtonText: "确定",
        cancelButtonText: "取消",
        type: "warning"
      }).then(() => {
        fixerApi.takeOrder(param).then(res => {
          if (res.data.code === "SUCCESS") {
            Notification.success({
              title: "成功",
              message: "接取订单成功"
            })
            this.detailVisible = false
            this.getFixOrder()
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
  margin-top: 1vh;
  width: 80%;
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
  box-shadow: 0 0 10px #ccc;
  border-radius: 10px;
  height: 88vh;
  overflow-y: auto;
  padding: 4vh;
}

.order-item {
  display: flex;
  flex-direction: row;
  margin-top: 4vh;
  width: calc(24% - 2vh); /* 4个元素所以宽度是25%减去两个元素的间隔 */
  justify-content: center;
  align-items: center;
  box-shadow: 0 0 10px #ccc;
  border-radius: 10px;
}

.order-item-info{
  margin-left: 0;
  width: 9vh;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
}

.order-item-info-img{
  width: 8vh;
  height: 8vh;
  border-radius: 50%;
  object-fit: cover;
}

.order-item-des{
  height: 28vh;
  margin-top: 1vh;
  margin-bottom: 1vh;
  overflow-y: auto;
  box-shadow: 0 0 10px #ccc;
  border-radius: 10px;
}
</style>