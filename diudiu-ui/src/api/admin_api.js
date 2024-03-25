import axios from 'axios';
//用户端口
const baseURL = 'http://localhost:8081';

// 更新axios的默认请求头
function updateAxiosHeader() {
    axios.defaults.headers.common['token'] = localStorage.getItem('token')
    axios.defaults.headers.common['id'] = localStorage.getItem('id')
}
// 通过管理员id查找到管理员信息
export function findAdminById(param) {
    updateAxiosHeader()
    return axios.post(baseURL + '/admin/findById', param)
}
// 管理员查找负责用户
export function findUserByAdmin(param) {
    updateAxiosHeader()
    return axios.post(baseURL + '/admin/findUsers', param)
}

// 管理员查找负责报修记录
export function findFixLogByAdmin(param) {
    updateAxiosHeader()
    return axios.post(baseURL + '/admin/findFixLog', param)
}

// 管理员开始处理报修
export function startFix(param) {
    updateAxiosHeader()
    return axios.post(baseURL + '/admin/startFix', param)
}

// 审核通过
export function pass(param) {
    updateAxiosHeader()
    return axios.post(baseURL + '/admin/pass', param)
}

// 审核不通过

export function reject(param) {
    updateAxiosHeader()
    return axios.post(baseURL + '/admin/reject', param)
}