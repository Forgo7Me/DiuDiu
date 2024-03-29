<template>
  <div style="display:flex;flex-direction: row">
    <admin_left/>
    <div class="main-container">
      <!--      TODO:数据统计模块-->
      <div class="data-sort">
        <div class="data-sort-block">
          <!--        四个数据方块，分别使用四个颜色各异的方块展示学生人数(userCount),待审核保修数(waitingCount),维修中报修数(fixingCount),已维修报修数(fixedCount)-->
          <div class="data-sort-block-item-1" @click="toUser">
            <span>用户数量</span>
            <span>{{ dataSort.userCount }}</span>
            <el-tag type="primary">实时</el-tag>
          </div>
          <div class="data-sort-block-item-2" @click="toOrder">
            <span>待审核报修</span>
            <span>{{ dataSort.waitingCount }}</span>
            <el-tag type="info">实时</el-tag>
          </div>
          <div class="data-sort-block-item-3" @click="toOrder">
            <span>维修中报修</span>
            <span>{{ dataSort.fixingCount }}</span>
            <el-tag type="warning">实时</el-tag>
          </div>
          <div class="data-sort-block-item-4" @click="toOrder">
            <span>已完成报修</span>
            <span>{{ dataSort.fixedCount }}</span>
            <el-tag type="success">实时</el-tag>
          </div>
        </div>
        <div class="data-sort-text">
          <div class="data-sort-text-notice">
            <h1>宿舍通告</h1>
            <div class="data-sort-text-notice-context">
              <el-timeline>
                <el-timeline-item
                  v-for="notice in dataSort.notices"
                  :key="notice.title"
                  :timestamp="new Date(parseInt(notice.createTime)).toLocaleString()">
                  {{notice.title}}
                </el-timeline-item>
              </el-timeline>
            </div>
          </div>
          <div class="charts" style="width: 60%;height: 70vh;margin-top: 3vh">
            <!--      切换年份-->
            <el-select v-model="year" placeholder="请选择年份" @change="drawChart">
              <el-option
                  v-for="item in yearList"
                  :key="item"
                  :label="item"
                  :value="item"
              >
              </el-option>
            </el-select>

            <div class="monthChart" style="width: 100%;height: 70vh">
              <!-- 报修数量统计，按年份和月份统计-->
            </div>
          </div>
          <div class="data-sort-text-other">
            <div id="he-plugin-simple"></div>
            <div class="button-addNotice">
            <el-button type="warning" @click="addNotice">发布公告</el-button>
            </div>
            <div class="data-sort-text-calendar">
              <el-calendar v-model="calendar" :first-day-of-week="1">
                <template slot="dateCell" slot-scope="{date,data}">
<!--                  去data.day最后一个-后边的内容-->
                  <p>{{ data.day.slice(data.day.lastIndexOf("-")+1) }}</p>
                </template>
              </el-calendar>
            </div>
          </div>
        </div>
      </div>

    </div>
    <!--    发布公告的dialog,可输入标题title,内容context,选择是否需要确认收到confirm(radio,是或否)-->
    <el-dialog title="发布公告" :visible.sync="noticeVisible" width="30%" :close-on-click-modal="false"
               :close-on-press-escape="false">
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
import {findAdminById} from "@/api/admin_api";
import {Notification} from "element-ui";
import {Api} from "@/api/Api";
import * as echarts from "echarts";

export default {
  components: {
    admin_left
  },
  data() {
    return {
      admin: {adminId: parseInt(localStorage.getItem("id"))},
      newNotice: {
        title: "",
        context: "",
        confirm: "",
        adminId: parseInt(localStorage.getItem("id"))
      },
      newNoticeRules: {
        title: [
          {required: true, message: "请输入标题", trigger: "blur"}
        ],
        context: [
          {required: true, message: "请输入内容", trigger: "blur"}
        ],
        confirm: [
          {required: true, message: "请选择是否需要确认", trigger: "blur"}
        ]
      },
      noticeVisible: false,
      // 统计数据
      dataSort: {
        fixingCount: 0,// 维修中数量
        fixCountTime: [], // 报修的时间，存储的均为13位时间戳
        userCount: 0, // 用户数量
        fixerCount: 0, // 维修员数量
        fixedCount: 0, // 已完成报修数量
        waitingCount: 0, // 待审核数量
        notices: [{title: "测试1", createTime: "1708491534988"}, {title: "测试2", createTime: "1708491534988"}] // 公告
      },
      year: new Date().getFullYear(),
      yearList: [new Date().getFullYear(), new Date().getFullYear() - 1, new Date().getFullYear() - 2],
      calendar: new Date(),
      calendarFixDate:[], // 存储有完成报修的日期
      calendarMap:[{date:"",count:0}]
    }
  },
  mounted() {
    window.WIDGET = {
      "CONFIG": {
        "modules": "01234",
        "background": "1",
        "tmpColor": "FFFFFF",
        "tmpSize": "20",
        "cityColor": "FFFFFF",
        "citySize": "20",
        "aqiColor": "FFFFFF",
        "aqiSize": "20",
        "weatherIconSize": "36",
        "alertIconSize": "18",
        "padding": "20px 10px 20px 10px",
        "shadow": "1",
        "language": "auto",
        "borderRadius": "5",
        "fixed": "yes",
        "vertical": "top",
        "horizontal": "center",
        "key": "7a359ad9c3e547779a3c8eed7cb26b98"
      }
    }
    let script = document.createElement('script')
    script.type = 'text/javascript'
    script.src = 'https://widget.qweather.net/simple/static/js/he-simple-common.js?v=2.0'
    document.getElementsByTagName('head')[0].appendChild(script)
  },
  created() {
    this.findAdmin();
    this.getDataSort();
  },
  methods: {
    findAdmin() {
      findAdminById(this.admin).then(response => {
        if (response.data.code === "SUCCESS") {
          this.admin = response.data.data;
          Notification({
            title: "获取管理员信息成功",
            type: "success"
          });
        } else {
          Notification({
            title: "获取管理员信息失败",
            type: "error"
          });
        }
      })
    },

    // 发布公告
    addNotice() {
      this.newNotice = {
        title: "",
        context: "",
        confirm: "",
        adminId: parseInt(localStorage.getItem("id"))
      }
      this.noticeVisible = true;
    },

    // 提交公告
    submitNotice() {
      this.$refs.newNoticeForm.validate(valid => {
        if (valid) {
          this.$confirm("确定发布公告吗?", "提示", {
            confirmButtonText: "确定",
            cancelButtonText: "取消",
            type: "warning"
          }).then(() => {
            Api.addNotice(this.newNotice).then(response => {
              if (response.data.code === "SUCCESS") {
                Notification({
                  title: "发布成功",
                  type: "success"
                });
                this.noticeVisible = false;
              } else if (response.data.code === "ERROR") {
                Notification({
                  title: "发布失败",
                  message: response.data.msg,
                  type: "error"
                });
              } else if (response.data.code === "TIMEOUT") {
                Notification({
                  title: "登录信息已过期，请重新登录",
                  type: "warning"
                });
              }
            });
          }).catch(() => {
            this.$message({
              type: "info",
              message: "已取消发布"
            });
          });
        } else {
          this.$message({
            type: "error",
            message: "请检查输入"
          });
          return false;
        }
      });
    },

    // 获取统计数据
    getDataSort() {
      const param = {
        adminId: this.admin.adminId
      }
      Api.getDataSort(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.dataSort = response.data.data;
          this.drawChart(this.year);
        } else if (response.data.code === "ERROR") {
          Notification({
            title: "查询失败",
            message: response.data.msg,
            type: "error"
          });
        }
      });
    },


    // 绘制统计图表
    /*
    * monthData存储的都是string类型的13位时间戳，需要将其转化，然后图标的横轴为月份，需要可以切换年份，每一年的数据独立显示，纵轴为接单数
    * */
    drawChart(year) {
      const monthData = this.dataSort.fixCountTime;
      // 默认展示今年的数据
      const filterData = monthData.filter(item => {
        return new Date(parseInt(item)).getFullYear() === year;
      });
      //  自动获取13位时间戳，横轴文本为今年的1-12月，共12个选项，纵轴为在月份内的接单综合
      const xAxisData = [];
      const seriesData = [];
      for (let i = 1; i <= 12; i++) {
        xAxisData.push(i + "月");
        let count = 0;
        filterData.forEach(item => {
          if (new Date(parseInt(item)).getMonth() === i - 1) {
            count++;
          }
        });
        seriesData.push(count);
      }
      const myChart = echarts.init(document.querySelector(".monthChart"));
      const option = {
        title: {
          text: "每月完成报修统计"
        },
        tooltip: {},
        legend: {
          data: ["完成报修数"]
        },
        xAxis: {
          data: xAxisData
        },
        yAxis: {},
        series: [{
          name: "完成报修数",
          type: "bar",
          data: seriesData
        }]
      };
      myChart.setOption(option);
    },

    // 切换年份
    changeYear() {
      this.drawChart(this.year);
    },

    toUser() {
      this.$router.push("/admin_users");
    },
    toOrder() {
      this.$router.push("/admin_fix_log");
    },
    toNotice() {
      this.$router.push("/admin_notice");
    },


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

.el-button {
  transition: all 0.5s;
}

.el-button:hover {
  transform: scale(1.1);
}

.data-sort {
  display: flex;
  flex-direction: column;
  align-items: center;
  height: 98vh;
  //margin-top: 10vh;
}

.data-sort-block {
  display: flex;
  flex-direction: row;
  justify-content: space-around;
  width: 100%;
  height: 20vh;
  margin-top: 1vh;
}

.data-sort-block-item-1 {
  display: flex;
  flex-direction: column;
  width: 22%;
  height: 15vh;
  background: #303F54;
  border-radius: 10px;
  color: white;
  padding: 10px;
  justify-content: space-around;
  font-size: 40px;
  align-items: center;
  transition: all 0.5s;
  cursor: pointer;
}

.data-sort-block-item-1:hover {
  transform: scale(1.1);
}

.data-sort-block-item-2 {
  display: flex;
  flex-direction: column;
  width: 22%;
  height: 15vh;
  background: #3D99FE;
  border-radius: 10px;
  color: white;
  padding: 10px;
  justify-content: space-around;
  font-size: 40px;
  align-items: center;
  transition: all 0.5s;
  cursor: pointer;
}

.data-sort-block-item-2:hover {
  transform: scale(1.1);
}

.data-sort-block-item-3 {
  display: flex;
  flex-direction: column;
  width: 22%;
  height: 15vh;
  background: #32868A;
  border-radius: 10px;
  color: white;
  padding: 10px;
  justify-content: space-around;
  font-size: 40px;
  align-items: center;
  transition: all 0.5s;
  cursor: pointer;
}

.data-sort-block-item-3:hover {
  transform: scale(1.1);
}

.data-sort-block-item-4 {
  display: flex;
  flex-direction: column;
  width: 22%;
  height: 15vh;
  background: #FFA500;
  border-radius: 10px;
  color: white;
  padding: 10px;
  justify-content: space-around;
  font-size: 40px;
  align-items: center;
  transition: all 0.5s;
  cursor: pointer;
}

.data-sort-block-item-4:hover {
  transform: scale(1.1);
}

.data-sort-text {
  display: flex;
  flex-direction: row;
  width: 100%;
  height: 70vh;
  margin-top: 1vh;
}

.data-sort-text-notice {
  width: 15%;
  display: flex;
  flex-direction: column;
}

.data-sort-text-notice-context {
  display: flex;
  flex-direction: column;
  width: 100%;
  height: 65vh;
}

.data-sort-text-notice-context-item {
  box-shadow: 0 0 10px 0 rgba(0, 0, 0, 0.1);
  padding: 1vh;
  flex-direction: column;
  width: 80%;
  margin-top: 1vh;
  border-radius: 10px;
  transition: all 0.5s;
  cursor: pointer;
}

.data-sort-text-notice-context-item:hover {
  transform: scale(1.1);
}

.data-sort-text-other {
  width: 25%;
  display: flex;
  flex-direction: column;
  height: 70vh;
  margin-top: 1vh;
}

.button-addNotice {
  margin-top: 1vh;
}

.data-sort-text-calendar{
  width: 100%;
  margin-top: 15vh;
  height: 30vh;
}

.fixDate{
  background: #FFA500;
  color: red;
}


</style>









