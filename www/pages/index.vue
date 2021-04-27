<template>
  <v-container fluid>
    <v-row justify="center" align-content="center">
      <v-col cols="8">
        <v-text-field
          v-model="num_of_participants"
          type="number"
          label="人数"
        ></v-text-field>
        <v-btn @click="submit">送信</v-btn>
      </v-col>
      <v-list>
        <v-list-item v-for="i in urls" :key="i">
          <a :href="i">{{ 'http://localhost/join/' + i }}</a>
        </v-list-item></v-list
      >
    </v-row>
  </v-container>
</template>

<script>
import Vue from 'vue'
import axios from 'axios'

export default Vue.extend({
  data() {
    return {
      num_of_participants: 0,
      urls: '',
    }
  },
  methods: {
    async submit() {
      await axios
        .post('http://localhost:8080/room', {
          num_of_participants: this.num_of_participants,
          mail_list: [],
        })
        .then(
          (res) => (
            // eslint-disable-next-line no-sequences
            (this.urls = res.data.invite_links),
            localStorage.setItem('room', res.data.access_token)
          )
        )
        .catch((res) => console.log(res))
    },
  },
})
</script>
