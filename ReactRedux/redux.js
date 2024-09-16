/*
  App
    - Search 
    - Movie List
      - Movie
    - Login

  With Sole React, We can only pass downwards, not sidewards,
    - the way to solve is, lift the state
  Say, Search and MovieList --> shared state management - call store

  npm i redux react-redux

  STORE 
    - globalized state
    - sample: movieList, isLogged: false

  ACTION
    - describes what u want to do

  REDUCERS
    - describes how actions transform state into the next state

  DISPATCH
    - dispatch this action to the reducer
*/

/**
 * STEP 1: Create the store, the globalized state
 */
import {createStore} from 'redux';

/**
 * STEP 2: Create the action, a simple function that returns an object
*/
const increment = () => {
  return {
    type: 'INCREMENT'
  }
}

const decrement = () => {
  return {
    type: 'DECREMENT'
  }
}

/**
 * STEP 3: create the reducer, it is a function that returns an object
 * 1st param:
 * 2nd param
 * 
 * // add to the store
 */
const counter = (state = 0, action) => { // 0 was the initial state
  switch(action.type) {
    case "INCREMENT": return state + 1;
    case "DECREMENT": return state - 1;
  }
}

// add to the store
let store = createStore(counter);

// display it in the console
store.subscribe(() => console.log(store.getState()));

/**
 * Step 4: DISPATCH, receives an action
 *  - dispatch the action
 */
store.dispatch(increment());