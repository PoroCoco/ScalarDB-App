<template>
  <div id="app" class="section">
    <div class="container">
      <h1 class="title">Carpooling Application</h1>

      <div v-if="!isLoggedIn">
        <div class="field">
          <label class="label">Username</label>
          <div class="control">
            <input
              class="input"
              type="text"
              v-model="username"
              placeholder="Enter username"
            />
          </div>
        </div>
        <div class="field">
          <label class="label">Password</label>
          <div class="control">
            <input
              class="input"
              type="password"
              v-model="password"
              placeholder="Enter password"
            />
          </div>
        </div>
        <button class="button is-primary" @click="login">Login</button>
        <button class="button is-link" @click="register">Create Account</button>
        <div v-if="loginError" class="notification is-danger mt-2">
          Username or password incorrect
        </div>
      </div>

      <div v-else>
        <h2>Welcome, {{ loggedInUser }}</h2>
        <div class="tabs">
          <ul>
            <li
              :class="{ 'is-active': activeTab === 'myTrips' }"
              @click="activeTab = 'myTrips'"
            >
              <a>My Trips</a>
            </li>
            <li
              :class="{ 'is-active': activeTab === 'searchTrips' }"
              @click="activeTab = 'searchTrips'"
            >
              <a>Search Trips</a>
            </li>
          </ul>
        </div>

        <div v-show="activeTab === 'myTrips'">
          <div class="field">
            <label class="label">Departure City</label>
            <div class="control">
              <input
                class="input"
                type="text"
                v-model="departureCity"
                placeholder="Enter departure city"
              />
            </div>
          </div>
          <div class="field">
            <label class="label">Destination City</label>
            <div class="control">
              <input
                class="input"
                type="text"
                v-model="destinationCity"
                placeholder="Enter destination city"
              />
            </div>
          </div>
          <button class="button is-primary" @click="createTrip">
            Create Trip
          </button>
          <button class="button is-info" @click="fetchMyTrips">
            Get My Trips
          </button>

          <div v-if="myTrips.length > 0" class="mt-4">
            <h3 class="title is-4">Your Trips:</h3>
            <div class="columns is-multiline">
              <div
                v-for="(trip, index) in myTrips"
                :key="index"
                class="column is-one-third"
              >
                <div class="box">
                  <h4 class="subtitle is-5">Trip {{ index + 1 }}</h4>
                  <p><strong>Driver:</strong> {{ trip.driver_name }}</p>
                  <p><strong>Departure:</strong> {{ trip.departure_city }}</p>
                  <p>
                    <strong>Destination:</strong> {{ trip.destination_city }}
                  </p>
                </div>
              </div>
            </div>
          </div>

          <!-- register -->
          <div v-if="registeredTrips.length > 0" class="mt-4">
            <h3 class="title is-4">Registered Trips:</h3>
            <div class="columns is-multiline">
              <div
                v-for="(trip, index) in registeredTrips"
                :key="index"
                class="column is-one-third"
              >
                <div class="box">
                  <h4 class="subtitle is-5">Trip {{ index + 1 }}</h4>
                  <p><strong>Driver:</strong> {{ trip.driver_name }}</p>
                  <p><strong>Departure:</strong> {{ trip.departure_city }}</p>
                  <p>
                    <strong>Destination:</strong> {{ trip.destination_city }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-show="activeTab === 'searchTrips'">
          <button class="button is-info" @click="fetchAllTrips">
            Get All Trips
          </button>

          <div v-if="allTrips.length > 0" class="mt-4">
            <h3 class="title is-4">All Trips:</h3>
            <div class="columns is-multiline">
              <div
                v-for="(trip, index) in allTrips"
                :key="index"
                class="column is-one-third"
              >
                <div class="box trip-box" @click="registerForTrip(trip)">
                  <h4 class="subtitle is-5">Trip {{ index + 1 }}</h4>
                  <p><strong>Driver:</strong> {{ trip.driver_name }}</p>
                  <p><strong>Departure:</strong> {{ trip.departure_city }}</p>
                  <p>
                    <strong>Destination:</strong> {{ trip.destination_city }}
                  </p>
                </div>
              </div>
            </div>
          </div>
        </div>

        <div v-if="errorMessage" class="notification is-danger mt-2">
          <p>{{ errorMessage }}</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from "vue";

export default {
  name: "App",
  setup() {
    const username = ref("");
    const password = ref("");
    const departureCity = ref("");
    const destinationCity = ref("");
    const loggedInUser = ref("");
    const isLoggedIn = ref(false);
    const sessionId = ref("");
    const myTrips = ref([]);
    const registeredTrips = ref([]);
    const allTrips = ref([]);
    const errorMessage = ref("");
    const loginError = ref(false);
    const activeTab = ref("myTrips");

    const login = async () => {
      try {
        const response = await fetch("/api/login", {
          method: "POST",
          headers: {
            "Content-Type": "text/plain",
          },
          body: `login:${username.value}:${password.value}`,
        });
        if (response.status === 200) {
          const data = await response.json();
          loggedInUser.value = data.username;
          sessionId.value = data.sessionId;
          isLoggedIn.value = true;
          errorMessage.value = "";
          loginError.value = false;
        } else {
          loginError.value = true;
          const errorData = await response.json();
          errorMessage.value = errorData.error || "Login failed";
        }
      } catch (error) {
        console.error("Error logging in:", error);
        errorMessage.value = "Error logging in";
        loginError.value = true;
      }
    };

    const register = async () => {
      try {
        const response = await fetch("/api/login", {
          method: "POST",
          headers: {
            "Content-Type": "text/plain",
          },
          body: `register:${username.value}:${password.value}`,
        });
        if (response.status === 200) {
          const data = await response.json();
          loggedInUser.value = data.username;
          sessionId.value = data.sessionId;
          isLoggedIn.value = true;
          errorMessage.value = "";
          loginError.value = false;
        } else {
          const errorData = await response.json();
          errorMessage.value = errorData.error || "Registration failed";
        }
      } catch (error) {
        console.error("Error registering:", error);
        errorMessage.value = "Error registering";
      }
    };

    const createTrip = async () => {
      try {
        const response = await fetch("/api/trips", {
          method: "POST",
          headers: {
            "Content-Type": "application/json",
            "Session-Id": sessionId.value,
          },
          body: JSON.stringify({
            departure_city: departureCity.value,
            destination_city: destinationCity.value,
          }),
        });
        if (response.status === 200) {
          const data = await response.json();
          myTrips.value.push(data.tripDetails);
          departureCity.value = "";
          destinationCity.value = "";
          errorMessage.value = "";
        } else {
          const errorData = await response.json();
          errorMessage.value = errorData.error || "Failed to create trip";
        }
      } catch (error) {
        console.error("Error creating trip:", error);
        errorMessage.value = "Error creating trip";
      }
    };

    const fetchMyTrips = async () => {
      try {
        const response = await fetch(`/api/trips?sessionId=${sessionId.value}`);
        if (response.status === 200) {
          const data = await response.json();
          myTrips.value = data.trips;
          errorMessage.value = "";
        } else {
          const errorData = await response.json();
          errorMessage.value = errorData.error || "Failed to fetch trips";
        }
      } catch (error) {
        console.error("Error fetching trips:", error);
        errorMessage.value = "Error fetching trips";
      }
    };

    const fetchAllTrips = async () => {
      try {
        const response = await fetch(`/api/trips`, {
          method: "PUT",
          headers: {
            "Session-Id": sessionId.value,
          },
        });
        if (response.status === 200) {
          const data = await response.json();
          allTrips.value = data.trips;
          errorMessage.value = "";
        } else {
          const errorData = await response.json();
          errorMessage.value = errorData.error || "Failed to fetch all trips";
        }
      } catch (error) {
        console.error("Error fetching all trips:", error);
        errorMessage.value = "Error fetching all trips";
      }
    };

    // Register
    const registerForTrip = async (trip) => {
      if (confirm("Do you want to register for this trip?")) {
        try {
          const response = await fetch(`/api/passenger`, {
            method: "GET",
            headers: {
              "Content-Type": "text/plain",
              "Session-Id": sessionId.value,
            },
            body: JSON.stringify({
              trip_id: trip.trip_id.toString(),
              username: username.value,
            }),
          });
          if (response.status === 200) {
            const data = await response.json();
            registeredTrips.value.push(data.tripDetails);
            errorMessage.value = "";
          } else {
            const errorData = await response.json();
            errorMessage.value =
              errorData.error || "Failed to register for trip";
          }
        } catch (error) {
          console.error("Error registering for trip:", error);
          errorMessage.value = "Error registering for trip";
        }
      }
    };

    return {
      username,
      password,
      departureCity,
      destinationCity,
      loggedInUser,
      isLoggedIn,
      sessionId,
      myTrips,
      registeredTrips,
      allTrips,
      errorMessage,
      loginError,
      activeTab,
      login,
      register,
      createTrip,
      fetchMyTrips,
      fetchAllTrips,
      registerForTrip,
    };
  },
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
.mt-2 {
  margin-top: 20px;
}
.mt-4 {
  margin-top: 40px;
}
.trip-box {
  cursor: pointer;
}
</style>
