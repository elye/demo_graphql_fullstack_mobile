import React from 'react';
import './App.css';
import NormalFetch from "./NormalFetch";
import ApolloFetch from "./ApolloFetch";

function App() {
  return (
    <div className="App">
      <header className="App-header">
          <div>
              <div><ApolloFetch /></div>
              <div><NormalFetch /></div>
          </div>
      </header>
    </div>
  );
}

export default App;
