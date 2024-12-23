import React, { useState, useContext } from 'react';
import { ThemeContext } from './App';

function ParseScenarioCount() {
  const [jsonInput, setJsonInput] = useState('');
  const [response, setResponse] = useState([]);
  const { isDarkMode, toggleTheme } = useContext(ThemeContext); // Pobierz motyw z kontekstu

  const handleSubmit = async (event) => {
    event.preventDefault();
    try {
      const res = await fetch('/count-steps', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
        },
        body: jsonInput,
      });

      const data = await res.json();
      setResponse(Array.isArray(data) ? data : [JSON.stringify(data, null, 2)]);
    } catch (error) {
      console.error('Błąd połączenia z API:', error);
      setResponse(['Błąd połączenia z API']);
    }
  };

  return (
    <div
      style={{
        backgroundColor: isDarkMode ? '#1e1e1e' : '#f4f4f9',
        color: isDarkMode ? '#e0e0e0' : '#333',
        padding: '20px',
        borderRadius: '8px',
      }}
    >
      <button
        onClick={toggleTheme}
        style={{
          position: 'fixed',
          top: '10px',
          right: '10px',
          padding: '10px 20px',
          backgroundColor: isDarkMode ? '#f4f4f9' : '#333',
          color: isDarkMode ? '#333' : '#f4f4f9',
          border: 'none',
          borderRadius: '5px',
          cursor: 'pointer',
        }}
      >
        Przełącz na {isDarkMode ? 'jasny' : 'ciemny'} tryb
      </button>
      <h1>Analizuj Scenariusz</h1>
      <form
        onSubmit={handleSubmit}
        style={{
          backgroundColor: isDarkMode ? '#333' : '#fff',
          padding: '20px',
          borderRadius: '8px',
          boxShadow: '0 4px 8px rgba(0, 0, 0, 0.1)',
        }}
      >
        <textarea
          rows="10"
          cols="50"
          value={jsonInput}
          onChange={(e) => setJsonInput(e.target.value)}
          placeholder="Wklej JSON"
          style={{
            width: '100%',
            height: '150px',
            padding: '10px',
            fontSize: '16px',
            backgroundColor: isDarkMode ? '#444' : '#fff',
            color: isDarkMode ? '#e0e0e0' : '#333',
            border: `1px solid ${isDarkMode ? '#555' : '#ccc'}`,
            borderRadius: '4px',
          }}
        />
        <br />
        <button
          type="submit"
          style={{
            backgroundColor: isDarkMode ? '#61dafb' : '#4a90e2',
            color: isDarkMode ? '#1e1e1e' : '#fff',
            padding: '12px 20px',
            fontSize: '16px',
            fontWeight: 'bold',
            border: 'none',
            borderRadius: '4px',
            cursor: 'pointer',
          }}
        >
          Wyślij
        </button>
      </form>

      <h2 style={{ color: isDarkMode ? '#bbb' : '#555' }}>Wyniki:</h2>
      <pre
        style={{
          backgroundColor: isDarkMode ? '#2c2c2c' : '#f4f4f9',
          color: isDarkMode ? '#e0e0e0' : '#222',
          padding: '20px',
          fontSize: '16px',
          border: `1px solid ${isDarkMode ? '#444' : '#ddd'}`,
          borderRadius: '4px',
        }}
      >
        {response.join('\n')}
      </pre>
    </div>
  );
}

export default ParseScenarioCount;
