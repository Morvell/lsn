<template>
    <v-container>
        <v-layout align-space-around justify-start column>
            <message-form :messageAttr="message"/>
            <message-row v-for="message in allMessages"
                         :key="message.id"
                         :message="message"
                         :editMessage="editMessage"/>
            <all-lazy-loader></all-lazy-loader>
        </v-layout>
    </v-container>
</template>

<script>
    import {mapGetters} from 'vuex'
    import MessageRow from 'components/messages/MessageRow.vue'
    import MessageForm from 'components/messages/MessageForm.vue'
    import AllLazyLoader from "components/AllLazyLoader.vue";

    export default {
        components: {
            AllLazyLoader,
            MessageRow,
            MessageForm
        },
        data() {
            return {
                message: null
            }
        },
        computed: mapGetters(['allMessages']),
        methods: {
            editMessage(message) {
                this.message = message
            }
        }
    }
</script>

<style>
</style>
