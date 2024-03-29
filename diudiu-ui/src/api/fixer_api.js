import axios from 'axios';
//用户端口
const baseURL = 'http://localhost:8081';

// 更新axios的默认请求头
function updateAxiosHeader() {
    axios.defaults.headers.common['token'] = localStorage.getItem('token')
    axios.defaults.headers.common['id'] = localStorage.getItem('id')
}

export const fixerApi = {
    // 维修员注册
    register(param) {
        updateAxiosHeader()
        return axios.post(baseURL + '/fixer/register', param)
    },
    // 获取维修员信息
    findFixer(param) {
        updateAxiosHeader()
        return axios.post(baseURL + '/fixer/findById', param)
    },
    // 获取所有可接取订单
    getFixOrder(param) {
        updateAxiosHeader()
        return axios.post(baseURL + '/fixer/getFixOrder', param)
    },
    // 接取订单
    takeOrder(param) {
        updateAxiosHeader()
        return axios.post(baseURL + '/fixer/takeOrder', param)
    },
    // 获取维修员的订单
    getFixerOrder(param) {
        updateAxiosHeader()
        return axios.post(baseURL + '/fixer/getFixerOrder', param)
    },
    // 维修员获取展示在首页的统计图表数据
    getChartData1(param) {
        updateAxiosHeader()
        return axios.post(baseURL + '/fixer/echarts/1', param)
    },

}