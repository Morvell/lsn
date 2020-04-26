import Vue from 'vue'
import '@babel/polyfill'
import Vuetify from 'vuetify'
import 'api/resource'
import router from "router/router";
import store from 'store/store'
import App from 'pages/App.vue'
import {connect} from './util/ws'
import 'vuetify/dist/vuetify.min.css'

if (frontendData.profile) {
    connect()
}

const vuetifyOptions = {}
Vue.use(Vuetify)


new Vue({
    el: '#app',
    store: store,
    router,
    vuetify: new Vuetify(vuetifyOptions),
    render: a => a(App)
})
