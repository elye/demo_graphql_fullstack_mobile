import React from 'react';
import './App.css';
import FetchNormal from "./FetchNormal";
import FetchApollo from "./FetchApollo";

function App() {
  return (
    <div className="App">
      <header className="App-header">
          <div>
              <div><FetchApollo /></div>
              <div><FetchNormal /></div>
          </div>
      </header>
    </div>
  );
}

export default App;
