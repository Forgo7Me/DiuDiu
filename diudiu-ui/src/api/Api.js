import axios from "axios";
//用户端口
const baseURL = 'http://localhost:8081';

// 更新axios的默认请求头
function updateAxiosHeader() {
    axios.defaults.headers.common['token'] = localStorage.getItem('token')
    axios.defaults.headers.common['id'] = localStorage.getItem('id')
}

export const Api = {
    // 获取到公告
    getNotice(param){
        updateAxiosHeader();
        return axios.post(baseURL+"/notice",param)
    },
    // 新增公告
    addNotice(param){
        updateAxiosHeader()
        return axios.post(baseURL+"/notice/add",param)
    },
    // 确认收到公告
    confirmNotice(param){
        updateAxiosHeader()
        return axios.post(baseURL+"/notice/confirm",param)
    },
    // 管理员获取所有自己发布的公告
    getNoticeByAdmin(param){
        updateAxiosHeader()
        return axios.post(baseURL+"/notice/admin/all",param)
    },
    // 管理员编辑公告
    editNotice(param){
        updateAxiosHeader()
        return axios.post(baseURL+"/notice/admin/edit",param)
    },
}