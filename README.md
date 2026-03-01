# Ndemi Garden Gym App

A Native Kotlin application that follows the latest architecture, design patterns and best practices.

Download the sample
app [here](https://github.com/mughalasim/NdemiGardenGym/raw/master/release/Ndemi%20Garden%20Gym.apk)

## Features

### Basic App functionality
- Flow attached to user state and app version to update UI if app version changes, user is authenticated or updated
- Supports Light and Dark Theme

### Functionality for Super Admin
- Register and manage new members and non-members: promote/demote member type
- All features other user types have access to

### Functionality for Admin
- Access to a stats dashboard
- Register and manage a new members information
- Track and manage each member's gym attendance
- Track and manage each members membership payment plan
- Search for all members
- View who is in the gym

### Functionality for a Supervisor
- Access to a stats dashboard
- Can view all members
- Can view each member's attendance
- Can view each member's monthly payments
- Can search for all members

### Functionality for Members
- Can sign up and reset their password
- Can update their profile picture
- Can view their profile, membership renewal date and gym attendance
- Can view who is in the gym
- Can view total time spent in the gym each month

### App Screenshots

<p float="left">
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc1.png" width="180" alt=""/>
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc2.png" width="180" alt=""/> 
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc3.png" width="180" alt=""/>
</p>
<p float="left">
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc4.png" width="180" alt=""/>
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc5.png" width="180" alt=""/> 
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc6.png" width="180" alt=""/>
</p>
<p float="left">
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc7.png" width="180" alt=""/>
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc8.png" width="180" alt=""/> 
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc9.png" width="180" alt=""/>
</p>
<p float="left">
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc10.png" width="180" alt=""/>
  <img src="https://github.com/mughalasim/NdemiGardenGym/blob/master/release/sc11.png" width="180" alt=""/>
</p>


### TODO's

- Add app settings to be fetched and refreshed from database
- Add unit tests to all modules - repositories, mappers/ factories, useCases and viewModels
- Running push notifications triggered when session(s) have started for one or more users, will require deep linking