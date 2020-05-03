import Vue from 'vue'
import '@babel/polyfill'
import Vuetify from 'vuetify'
import 'api/resource'
import router from "router/router";
import store from 'store/store'
import App from 'pages/App.vue'
import {connect} from './util/ws'
import 'vuetify/dist/vuetify.min.css'
import * as Sentry from '@sentry/browser';
import { Vue as VueIntegration } from '@sentry/integrations';

Sentry.init({
    dsn: 'https://d07db411795b4f2dbe5dd37a0eaa5f5b@o386738.ingest.sentry.io/5221322',
    integrations: [new VueIntegration({Vue, attachProps: true})],
});

Sentry.configureScope(scope => scope.setUser({
    id: profile && profile.id,
    username: profile && profile.name
}))

if (profile) {
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
