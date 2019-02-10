import { GET_PROJECTS } from "../actions/types";

const initialState = {
  projects: [],
  project: {}
};

export default function(state = initialState, action) {
  switch (action) {
    case GET_PROJECTS:
      return {
        ...state,
        projects: action.payload
      };
    default:
      return state;
  }
}
