<template>
  <div id="app" class="section">
    <div class="container">
      <h1 class="title">Carpooling Application</h1>
      
      <div v-if="!isLoggedIn">
        <div class="field">
          <label class="label">Username</label>
          <div class="control">
            <input class="input" type="text" v-model="username" placeholder="Enter username">
          </div>
        </div>
        <div class="field">
          <label class="label">Password</label>
          <div class="control">
            <input class="input" type="password" v-model="password" placeholder="Enter password">
          </div>
        </div>
        <button class="button is-primary" @click="login">Login</button>
      </div>

      <div v-else>
        <h2>Welcome, {{ loggedInUser }}</h2>
        <div class="field">
          <label class="label">Trip Details</label>
          <div class="control">
            <input class="input" type="text" v-model="tripDetails" placeholder="Enter trip details">
          </div>
        </div>
        <button class="button is-primary" @click="createTrip">Create Trip</button>
        <button class="button is-info" @click="fetchTrips">Get Trips</button>

        <div v-if="trips.length > 0" class="notification is-info">
          <h3>Your Trips:</h3>
          <ul>
            <li v-for="(trip, index) in trips" :key="index">{{ trip }}</li>
          </ul>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';

export default {
  name: 'App',
  setup() {
    const username = ref("");
    const password = ref("");
    const tripDetails = ref("");
    const loggedInUser = ref("");
    const isLoggedIn = ref(false);
    const sessionId = ref("");
    const trips = ref([]);

    const login = async () => {
      try {
        const response = await fetch('/api/login', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: `${username.value}:${password.value}`
        });
        if (response.status === 200) {
          const data = await response.json();
          loggedInUser.value = data.username;
          sessionId.value = data.sessionId;
          isLoggedIn.value = true;
        } else {
          console.error('Login failed');
        }
      } catch (error) {
        console.error('Error logging in:', error);
      }
    };

    const createTrip = async () => {
      try {
        const response = await fetch('/api/trips', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Session-Id': sessionId.value
          },
          body: tripDetails.value
        });
        if (response.status === 200) {
          const data = await response.json();
          trips.value.push(data.tripDetails);
          tripDetails.value = "";
        } else {
          console.error('Failed to create trip');
        }
      } catch (error) {
        console.error('Error creating trip:', error);
      }
    };

    const fetchTrips = async () => {
      try {
        const response = await fetch(`/api/trips?sessionId=${sessionId.value}`);
        const data = await response.json();
        trips.value = data.trips;
      } catch (error) {
        console.error('Error fetching trips:', error);
      }
    };

    return {
      username,
      password,
      tripDetails,
      loggedInUser,
      isLoggedIn,
      sessionId,
      trips,
      login,
      createTrip,
      fetchTrips
    };
  }
};
</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  text-align: center;
  color: #2c3e50;
  margin-top: 60px;
}
.notification {
  margin-top: 20px;
}
</style>
