import React, { useEffect, useRef, createContext, useState } from 'react';
import { BrowserRouter as Router, Route, Routes, Link } from 'react-router-dom';
import WyswietlScenariusz from './WyswietlScenariusz';
import CountConditionalSteps from './CountConditionalSteps';
import ParseScenarioCount from './ParseScenarioCount';
import StepsWithoutActors from './StepsWithoutActors';

function Home() {
  const canvasRef = useRef(null);

  useEffect(() => {
    const canvas = canvasRef.current;
    const ctx = canvas.getContext('2d');
    const particles = [];

    canvas.width = window.innerWidth;
    canvas.height = window.innerHeight;

    function createParticle(x, y) {
      for (let i = 0; i < 50; i++) {
        particles.push({
          x,
          y,
          dx: Math.random() * 4 - 2,
          dy: Math.random() * 4 - 2,
          size: Math.random() * 4 + 1,
          color: `hsl(${Math.random() * 360}, 100%, 50%)`,
        });
      }
    }

    function updateParticles() {
      for (let i = particles.length - 1; i >= 0; i--) {
        const p = particles[i];
        p.x += p.dx;
        p.y += p.dy;
        p.size *= 0.95;
        if (p.size < 0.5) {
          particles.splice(i, 1);
        }
      }
    }

    function drawParticles() {
      ctx.clearRect(0, 0, canvas.width, canvas.height);
      for (const p of particles) {
        ctx.beginPath();
        ctx.arc(p.x, p.y, p.size, 0, Math.PI * 2);
        ctx.fillStyle = p.color;
        ctx.fill();
      }
    }

    function animate() {
      updateParticles();
      drawParticles();
      requestAnimationFrame(animate);
    }

    canvas.addEventListener('click', (e) => {
      const rect = canvas.getBoundingClientRect();
      const x = e.clientX - rect.left;
      const y = e.clientY - rect.top;
      createParticle(x, y);
    });

    animate();

    return () => {
      canvas.removeEventListener('click', createParticle);
    };
  }, []);
  

  return (
    <div className="App">
      <h1 style={{ fontSize: '60px', textAlign: 'center' }}>Scenario Quality Checker</h1>
      <p style={{ textAlign: 'center', fontSize: '20px', margin: '20px 0' }}>
        Witaj na naszej interaktywnej stronie! Kliknij gdziekolwiek, aby zobaczyć magiczne efekty.
      </p>
      <canvas
        ref={canvasRef}
        style={{ position: 'fixed', top: 0, left: 0, width: '100%', height: '100%', zIndex: -1 }}
      ></canvas>
    </div>
  );
}

export const ThemeContext = createContext();

function ThemeProvider({ children }) {
  const [isDarkMode, setIsDarkMode] = useState(true);

  const toggleTheme = () => {
    setIsDarkMode(!isDarkMode);
    const stylesheet = document.getElementById('theme-stylesheet');
    if (stylesheet) {
      stylesheet.setAttribute('href', isDarkMode ? '/App.css' : '/dark.css');
    }
  };

  return (
    <ThemeContext.Provider value={{ isDarkMode, toggleTheme }}>
      {children}
    </ThemeContext.Provider>
  );
}

function App() {
  return (
    <ThemeProvider>
      <Router>
        <nav style={{ padding: '10px', background: '#eee' }}>
          <Link to="/" style={{ marginRight: '20px' }}>Strona Główna</Link>
          <Link to="/wyswietl-scenariusz" style={{ marginRight: '20px' }}>Wyświetl Scenariusz</Link>
          <Link to="/count-conditional-steps" style={{ marginRight: '20px' }}>Policz Kroki Warunkowe</Link>
          <Link to="/parse-scenario-count" style={{ marginRight: '20px' }}>Analizuj Scenariusz</Link>
          <Link to="/steps-without-actors">Kroki Bez Aktora</Link>
        </nav>

        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/wyswietl-scenariusz" element={<WyswietlScenariusz />} />
          <Route path="/count-conditional-steps" element={<CountConditionalSteps />} />
          <Route path="/parse-scenario-count" element={<ParseScenarioCount />} />
          <Route path="/steps-without-actors" element={<StepsWithoutActors />} />
        </Routes>
      </Router>
    </ThemeProvider>
  );
}

export default App;