import Vue from 'vue'
import Vuex from 'vuex'
import messagesApi from 'api/messages'
import commentApi from 'api/comment'

Vue.use(Vuex)

export default new Vuex.Store({
    state: {
        messages,
        allMessages,
        profile,
        ...frontendData
    },
    getters: {
        sortedMessages: state => (state.messages || []).sort((a, b) => -(a.id - b.id)),
        allMessages: state => (state.allMessages || []).sort((a, b) => -(a.id - b.id))
    },
    mutations: {
        addMessageMutation(state, message) {
            state.messages = [
                ...state.messages,
                message
            ];

            state.allMessages = [
                ...state.allMessages,
                message
            ];

        },
        updateMessageMutation(state, message) {
            const updateIndex = state.messages.findIndex(item => item.id === message.id);
            const updateAllIndex = state.allMessages.findIndex(item => item.id === message.id);

            state.messages = [
                ...state.messages.slice(0, updateIndex),
                message,
                ...state.messages.slice(updateIndex + 1)
            ];
            state.allMessages = [
                ...state.allMessages.slice(0, updateAllIndex),
                message,
                ...state.allMessages.slice(updateAllIndex + 1)
            ]
        },
        removeMessageMutation(state, message) {
            const deletionIndex = state.messages.findIndex(item => item.id === message.id);
            const deletionAllIndex = state.allMessages.findIndex(item => item.id === message.id);

            if (deletionIndex > -1) {
                state.messages = [
                    ...state.messages.slice(0, deletionIndex),
                    ...state.messages.slice(deletionIndex + 1)
                ]
            }

            if (deletionAllIndex > -1) {
                state.allMessages = [
                    ...state.allMessages.slice(0, deletionAllIndex),
                    ...state.allMessages.slice(deletionAllIndex + 1)
                ]
            }
        },
        addCommentMutation(state, comment) {
            const updateIndex = state.messages.findIndex(item => item.id === comment.message.id);
            const updateAllIndex = state.allMessages.findIndex(item => item.id === comment.message.id);

            const message = state.messages[updateIndex];
            const messageAll = state.allMessages[updateAllIndex];

            if (updateIndex !== -1 ) {
                if (message.comments === null)
                    message.comments = [];
                if (!message.comments.find(it => it.id === comment.id)) {
                    state.messages = [
                        ...state.messages.slice(0, updateIndex),
                        {
                            ...message,
                            comments: [
                                ...message.comments,
                                comment
                            ]
                        },
                        ...state.messages.slice(updateIndex + 1)
                    ]
                }
            }
            if(messageAll.comments === null)
                messageAll.comments = [];
            if (!messageAll.comments.find(it => it.id === comment.id)) {
                state.allMessages = [
                    ...state.allMessages.slice(0, updateAllIndex),
                    {
                        ...messageAll,
                        comments: [
                            ...messageAll.comments,
                            comment
                        ]
                    },
                    ...state.allMessages.slice(updateAllIndex + 1)
                ]
            }
        },
        addMessagePageMutation(state, messages) {
            const targetMessages = state.messages
                .concat(messages)
                .reduce((res, val) => {
                    res[val.id] = val
                    return res
                }, {});

            state.messages = Object.values(targetMessages)

            const targetAllMessages = state.allMessages
                .concat(messages)
                .reduce((res, val) => {
                    res[val.id] = val
                    return res
                }, {});

            state.allMessages = Object.values(targetAllMessages)
        },

        addAllMessagePageMutation(state, messages) {
            const targetAllMessages = state.allMessages
                .concat(messages)
                .reduce((res, val) => {
                    res[val.id] = val
                    return res
                }, {});

            state.allMessages = Object.values(targetAllMessages)
        },

        updateTotalPagesMutation(state, totalPages) {
            state.totalPages = totalPages
        },
        updateCurrentPageMutation(state, currentPage) {
            state.currentPage = currentPage
        },

        updateTotalAllPagesMutation(state, totalPages) {
            state.totalAllPages = totalPages
        },
        updateCurrentAllPageMutation(state, currentPage) {
            state.currentAllPage = currentPage
        }
    },
    actions: {
        async addMessageAction({commit, state}, message) {
            const result = await messagesApi.add(message)
            const data = await result.json()
            const index = state.messages.findIndex(item => item.id === data.id)

            if (index > -1) {
                commit('updateMessageMutation', data)
            } else {
                commit('addMessageMutation', data)
            }
        },
        async updateMessageAction({commit}, message) {
            const result = await messagesApi.update(message)
            const data = await result.json()
            commit('updateMessageMutation', data)
        },
        async removeMessageAction({commit}, message) {
            const result = await messagesApi.remove(message.id)

            if (result.ok) {
                commit('removeMessageMutation', message)
            }
        },
        async addCommentAction({commit, state}, comment) {
            const response = await commentApi.add(comment)
            const data = await response.json()
            commit('addCommentMutation', data)
        },
        async loadPageAction({commit, state}) {
            const response = await messagesApi.page(state.currentPage + 1)
            const data = await response.json()

            commit('addMessagePageMutation', data.messages)
            commit('updateTotalPagesMutation', data.totalPages)
            commit('updateCurrentPageMutation', Math.min(data.currentPage, data.totalPages - 1))
        },

        async loadAllPageAction({commit, state}) {
            const response = await messagesApi.pageAll(state.currentPage + 1)
            const data = await response.json()

            commit('addAllMessagePageMutation', data.messages)
            commit('updateTotalAllPagesMutation', data.totalPages)
            commit('updateCurrentAllPageMutation', Math.min(data.currentPage, data.totalPages - 1))
        },
    }
})
