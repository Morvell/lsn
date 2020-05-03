import Vue from 'vue'
import VueRouter from 'vue-router'
import MessagesList from 'pages/MessageList.vue'
import Auth from 'pages/Auth.vue'
import Profile from 'pages/Profile.vue'
import Subscriptions from 'pages/Subscriptions.vue'
import AllMessageList from "pages/AllMessageList.vue";

Vue.use(VueRouter)

const routes = [
    { path: '/', component: MessagesList },
    { path: '/all', component: AllMessageList },
    { path: '/auth', component: Auth },
    { path: '/user/:id?', component: Profile },
    { path: '/subscriptions/:id', component: Subscriptions },
    { path: '*', component: MessagesList },
]

export default new VueRouter({
    mode: 'history',
    routes
})
