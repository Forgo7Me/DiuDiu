<template>
  <div style="display:flex;flex-direction: row">
    <fixer_left/>
    <div class="main-container">
      <el-descriptions title="用户信息" column="1" border style="font-size: 20px">
        <el-descriptions-item v-for="column in columns" :label="column.label" :key="column.prop">
          <el-icon :class="column.icon"></el-icon>
          <span>{{ fixer[column.prop] }}</span>
        </el-descriptions-item>
      </el-descriptions>
      <div class="charts">
        <!--      放置统计表格-->
        <!--      切换年份-->
        <el-select v-model="year" placeholder="请选择年份" @change="drawChart">
          <el-option
              v-for="item in yearList"
              :key="item"
              :label="item"
              :value="item">
          </el-option>
        </el-select>
        <div class="monthChart" style="width: 60%;height: 30vh"></div>
      </div>
    </div>
  </div>
</template>

<script>
import {Notification} from "element-ui";
import fixer_left from "@/components/fixer_left.vue";
import {fixerApi} from "@/api/fixer_api";
import * as echarts from "echarts";

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
      columns: [
        {label: "工号", prop: "id", icon: "el-icon-user"},
        {label: "姓名", prop: "name", icon: "el-icon-user"},
        {label: "电话", prop: "phone", icon: "el-icon-phone"},
        {label: "性别", prop: "gender", icon: "el-icon-user"},
        {label: "接单数", prop: "orderCount", icon: "el-icon-s-order"},
      ],
      monthData: [],
      year: new Date().getFullYear(),
      yearList: [new Date().getFullYear(), new Date().getFullYear() - 1, new Date().getFullYear() - 2]
    }
  },
  created() {
    this.findFixer();
    this.getChartData1();
  },
  methods: {
    findFixer() {
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
    },

    // 获取统计数据
    getChartData1() {
      const param = {
        fixerId: this.fixer.fixerId
      }
      fixerApi.getChartData1(param).then(response => {
        if (response.data.code === "SUCCESS") {
          this.monthData = response.data.data;
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
      const monthData = this.monthData;
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
          text: "每月接单数统计"
        },
        tooltip: {},
        legend: {
          data: ["接单数"]
        },
        xAxis: {
          data: xAxisData
        },
        yAxis: {},
        series: [{
          name: "接单数",
          type: "bar",
          data: seriesData
        }]
      };
      myChart.setOption(option);
    },

    // 切换年份
    changeYear() {
      this.drawChart(this.year);
    }


  },
}
</script>

<style scoped>
.main-container {
  padding-top: 10vh;
  width: 60%;
  margin: 0 auto;
  align-content: center;
  font-size: 20px;
  display: flex;
  flex-direction: column;
}

.charts {
  display: flex;
  flex-direction: column;
  align-items: center;
  margin-top: 10vh;
}
</style>


































