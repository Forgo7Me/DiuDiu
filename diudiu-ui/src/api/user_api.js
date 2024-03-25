import axios from 'axios';
//用户端口
const baseURL = 'http://localhost:8081';

// 更新axios的默认请求头
function updateAxiosHeader() {
    axios.defaults.headers.common['token'] = localStorage.getItem('token')
    axios.defaults.headers.common['id'] = localStorage.getItem('id')
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

// 获取所有帖子
export function getAllPost(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/post/getAllPost',param)
}
// 浏览帖子
export function viewPost(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/post/viewPost',param)
}
// 获取帖子的所有评论
export function getPostComment(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/post/getPostComment', param)
}

// 点赞和取消点赞
export function favorPost(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/post/favorPost', param)
}
// 发表评论
export function addComment(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/post/addComment', param)
}
// 回复评论
export function responseComment(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/post/responseComment', param)
}

// 上传新帖子的图片
export function uploadPostImage(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/post/uploadPostImage', param)
}

// 上传新帖子的文本
export function uploadPostContent(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/post/uploadPostContent', param)
}

// 删除帖子
export function deletePost(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/post/delete', param)
}
// 获取用户和ai的聊天记录
export function getAiChatLog(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/ai/getLog', param)
}

// 用户和ai聊天
export function aiChat(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/ai/chat', param)
}

// 用户获取聊天记录
export function userChatLog(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/chat/getLog', param)
}

// 获取到聊天双方的信息
export function getChatInfo(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/chat/getChatInfo', param)
}
// 聊天
export function chat(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/chat/chat', param)
}


// 未读消息数
export function getUncheckedMessageCount(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/chat/getUncheckedMessageCount', param)
}

// 登录
export function login(param){
    return axios.post(baseURL + '/login/', param)
}

// 退出登录
export function logout(param){
    updateAxiosHeader()
    return axios.post(baseURL + '/login/logout',param)
}