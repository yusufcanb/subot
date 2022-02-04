<template>
  <div id="app">
    <div id="nav">
      <b-navbar>
        <b-navbar-brand> SU Virtual Assistant (SUbot) </b-navbar-brand>
      </b-navbar>
    </div>
    <b-container fluid id="messageContainer">
      <b-modal id="my-modal" centered size="sm" hide-header hide-footer>
        Your session is expired. :( <br />
        Please start a new session!<br />
        <b-button variant="outline-primary" id="modal-button" v-on:click="newSession()"> OK </b-button>
      </b-modal>
      <subot-message-container
        :messageList="messages"
      ></subot-message-container>
    </b-container>
    <footer id="send-message">
      <b-row>
        <b-col cols="10">
          <b-form-input
            v-model="text"
            placeholder="Enter your message"
            id="messageInput"
            v-on:keyup.enter="onMessage"
          >
          </b-form-input>
        </b-col>
        <b-col cols="2">
          <b-button
            :disabled="!text"
            variant="outline-primary"
            v-on:click="onMessage()"
          >
            <b-icon icon="arrow-right-circle-fill"></b-icon>
          </b-button>
        </b-col>
      </b-row>
    </footer>
  </div>
</template>

<script>
import axios from "axios";
import MessagesContainer from "./components/MessagesContainer.vue";

import { API_BASE_URL, SESSION_EXPIRE_MS } from "./constants";

export default {
  name: "App",
  components: {
    "subot-message-container": MessagesContainer,
  },
  data() {
    return {
      text: null,
      messages: [],
      sessionId: 0,
      timeOut: null,
      isExpired: false,
    };
  },
  async created() {
    let self = this;
    if (localStorage.getItem("sessionId") === null) {
      let response = await axios.post(`${API_BASE_URL}/create`);
      self.sessionId = response.data.id;
      localStorage.setItem("sessionId", self.sessionId);
    } else {
      self.sessionId = localStorage.getItem("sessionId");
      let response = await axios
        .get(`${API_BASE_URL}/get?id=${this.sessionId}`)
        .catch(async function () {
          let responseCreate = await axios.post(`${API_BASE_URL}/create`);
          self.sessionId = responseCreate.data.id;
          localStorage.setItem("sessionId", self.sessionId);
        });
        if(response.data.expired){
          response = await axios.post(`${API_BASE_URL}/create`);
          self.sessionId = response.data.id;
          localStorage.setItem("sessionId", self.sessionId);
        }
      if (response !== undefined) {
        for (const key in response.data.sessionItems) {
          this.pushMessage(response.data.sessionItems[key]);
        }
      }
    }
  },
  updated() {
    this.updateScroll();
  },
  methods: {
    newSession() {
      this.messages= [],
      this.timeOut= null,
      this.isExpired = false,
      this.$bvModal.hide("my-modal"),
      this.$options.created;
    },
    updateScroll() {
      var element = document.getElementById("messageContainer");
      element.scrollTop = element.scrollHeight;
    },
    async askQuestion() {
      if (this.text !== "" && this.text !== null) {
        this.pushMessage({agent: true, spinner:true});
        const question = { sessionId: this.sessionId, message: this.text };
        const response = await axios.post(`${API_BASE_URL}/ask`, question);
        this.messages.pop();
        this.pushMessage(response.data);
      }
    },
    addQuestion() {
      if (this.text !== "" && this.text !== null) {
        let question = { message: this.text, agent: false };
        this.pushMessage(question);
      } else {
        this.pushMessage({ message: "Please enter a question", agent: true });
      }
    },
    removeText() {
      let target = document.getElementById("messageInput");
      target.value = "";
      this.text = null;
    },
    pushMessage(message) {
      this.messages.push(message);
      this.updateScroll();
    },
    onMessage() {
      if (document.getElementById("messageInput").value !== "") {
        this.addQuestion();
        this.askQuestion();
        this.removeText();
        this.timeoutSession();
      }
    },
    timeoutSession() {
      if (this.timeOut === null) {
        this.timeOut = setTimeout(this.expireSession, SESSION_EXPIRE_MS);
      } else {
        clearTimeout(this.timeOut);
        this.timeOut = setTimeout(this.expireSession, SESSION_EXPIRE_MS);
      }
    },
    async expireSession() {
      await axios.post(`${API_BASE_URL}/delete?id=${this.sessionId}`);
      this.isExpired = true;
      this.$bvModal.show("my-modal");
    },
  },
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  width: 100%;
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}
#nav {
  position: fixed;
  width: 100vw;
  text-align: center;
  top: 0;
  background-color: #124b8f;
  z-index: 999999;
}
.navbar.navbar-expand {
  justify-content: center;
}
.navbar.navbar-light .navbar-brand {
  color: white;
}
#messageContainer {
  max-height: 100vh;
  padding-top: 71px;
  padding-bottom: 40px;
  flex: auto;
  overflow-y: scroll;
  background-color: #f5f5f5;
}
#send-message {
  bottom: 0%;
  margin-bottom: 10px;
}
html {
  height: 100%;
}
.row {
  justify-content: center;
}
ol {
  padding-left: 0;
}
ul {
  list-style-type: none;
}
li {
  padding-bottom: 10px;
}
footer {
  position: fixed;
  bottom: 0;
  padding-left: 12px;
  width: 100vw;
}
.row > .col-10 {
  padding-right: 2.5px;
}
.row > .col-2 {
  padding-left: 2.5px;
}
#modal-button {
  float: right;
}
</style>
