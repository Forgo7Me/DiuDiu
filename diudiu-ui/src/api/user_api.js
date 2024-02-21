import axios from 'axios';
//用户端口
const baseURL = 'http://localhost:8081';

// 更新axios的默认请求头
function updateAxiosHeader() {
    axios.defaults.headers.common['token'] = localStorage.getItem('token')
    axios.defaults.headers.common['userId'] = localStorage.getItem('userId')
}

// 通过用户id查找用户
export function findUserById(param) {
    updateAxiosHeader()
    return axios.post(baseURL + '/user/findById', param)
}

//查找用户所属的管理员
export function findAdmin(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/user/findAdmin', param)
}

// 报修
export function addFixLog(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/user/addFixLog', param)
}

// 查找用户的报修记录
export function findFixLog(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/user/findFixLog', param)
}

// 确认报修完成
export function finishFix(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/user/finishFix', param)
}