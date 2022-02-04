<template>
  <ul id="paddingless">
    <li v-for="message in messageList" :key="message.id">
      <subot-message-item
        :agent="message.agent"
        :message="message.message"
        :spinner="message.spinner"
      ></subot-message-item>
    </li>
  </ul>
</template>

<script>
import MessageItem from "./MessageItem.vue";

export default {
  components: {
    "subot-message-item": MessageItem,
  },
  props: ["messageList"],
};
</script>

<style>
#paddingless {
  padding-left: 0;
}
</style>