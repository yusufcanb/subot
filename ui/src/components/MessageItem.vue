<template>
  <div id="messageItem">
    <b-row v-if="agent">
      <b-col cols="8">
        <div class="float-start">
          <b-card class="card-agent" v-if="spinner">
            <b-spinner small variant="light" type="grow"></b-spinner>
            <b-spinner small variant="light" type="grow"></b-spinner>
            <b-spinner small variant="light" type="grow"></b-spinner>
          </b-card>
          <b-card class="card-agent" v-else-if="messageControl(this.message)">
            <b-card-text v-html="textBoldNumbers"></b-card-text>
            <b-button
              id="card-button"
              :href="this.link"
              target="_blank"
              variant="light"
              >Visit page</b-button
            >
          </b-card>
          <b-card class="card-agent" v-else>
            <b-card-text v-html="textBoldNumbers"></b-card-text>
          </b-card>
        </div>
      </b-col>
      <b-col cols="4"></b-col>
    </b-row>
    <b-row v-else>
      <b-col cols="4"></b-col>
      <b-col>
        <div class="float-end">
          <b-card class="card-user">
            <b-card-text>{{ message }}</b-card-text>
          </b-card>
        </div>
      </b-col>
    </b-row>
  </div>
</template>

<script>
export default {
  props: ["agent", "message", "spinner"],
  computed: {
    textBoldNumbers() {
      let boldReg = /#[a-zA-Z]+$/i;
      let hashTag = boldReg.exec(this.messageRevised);
      return this.messageRevised.replace(boldReg, `<b>${hashTag}</b>`);
    },
  },
  methods: {
    messageControl(text) {
      let reg = RegExp(
        "([a-zA-Z0-9]+://)?([a-zA-Z0-9_]+:[a-zA-Z0-9_]+@)?([a-zA-Z0-9.-]+\\.[A-Za-z]{2,4})(:[0-9]+)?(/.*)?"
      );
      let regTime = RegExp("%&[a-zA-Z]+$");
      if (reg.test(text)) {
        let url = reg.exec(text);
        this.link = url[0];
        this.messageRevised = this.message.split(this.link)[0];
        return true;
      } else if (regTime.test(text)) {
        let currentdate = new Date();
        let hour = "";
        let minute = "";
        if(currentdate.getHours()<10) hour = "0" + currentdate.getHours();
        else hour = currentdate.getHours();
        if(currentdate.getMinutes()<10) minute = "0" + currentdate.getMinutes();
        else minute = currentdate.getMinutes();
        let datetime =
          "The date is " +
          currentdate.getDate() +
          "/" +
          currentdate.getMonth() +
          "/" +
          currentdate.getFullYear() +
          " and the time is " +
          hour +
          ":" +
          minute;
        this.messageRevised = datetime;
      } else {
        this.messageRevised = text;
        return false;
      }
    },
  },
  data() {
    return {
      link: null,
      messageRevised: "",
    };
  },
};
</script>

<style>
.card-agent {
  background-color: #797986de !important;
  color: white;
  border-color: #797986de !important;
}
.card-user {
  background-color: #4990e8 !important;
  color: white;
  border-color: #4991e883 !important;
}
.card-user,
.card-agent {
  word-break: unset;
  border-radius: 1rem !important;
}
#card-button {
  border-radius: 1rem !important;
}
</style>
