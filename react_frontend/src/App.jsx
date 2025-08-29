import React, { useEffect, useState } from 'react'
import { useLocation } from 'react-router-dom'
import { api, apiPost, apiDelete, apiPut, errorMessage, setApiUser } from './api.js'
import { login, logout, userManager, handleCallback, getUser } from './authService'
import TabbedPanel from './components/TabbedPanel'
import ErrorBoundary from './components/ErrorBoundary'
import Notification from './components/Notification'
import Demo from './demo/Demo'
import Users from './users/Users'
import Roles from './roles/Roles'
import Miscellaneous from './miscellaneous/Miscellaneous'

const tabs = [
    { id: "demo", label: "🎬 Demo", content: <Demo /> },
    { id: "users", label: "👥 Users", content: <Users /> },
    { id: "roles", label: "🛡️ Roles", content: <Roles /> },
    { id: "misc", label: "🌀 Miscellaneous", content: <Miscellaneous /> }
];

export default function App() {
  const [user, setUser] = useState(null);
  const location = useLocation();

  useEffect(() => {
    const params = new URLSearchParams(location.search);
    if (params.get("callback") === "1") {
      handleCallback().then(user => {
        setUser(user);
        window.history.replaceState({}, document.title, "/");
      });
    } else {
      getUser().then(user => setUser(user));
    }
  }, [user]);

  const doLogout = () => {
      logout();
      setUser(null);
  }

  if (user) setApiUser(user);

  return (
    <ErrorBoundary>
        <div className="shell">
          <header className="header">
            <div className="header-inner">
              <div className="header-title">
                  <img className="header-image" src="/images/Logo.png"/>&nbsp;
                  <h1>EnterpriseExemplar</h1>
              </div>
              <div className="sub">
              {user
                ? <button className="btn btn-primary" onClick={() => doLogout()}>Logout</button>
                : <button className="btn btn-primary" onClick={() => login()}>Log In</button>}
              </div>
            </div>
          </header>
          {user ? <TabbedPanel tabs={tabs} /> : undefined}
          <footer className="footer">
            <div>EnterpriseExemplar = (Java + Spring Boot + SQL Server) × React ÷ Docker</div>
            <div className="muted">Backend by DaveVoorhis; Frontend by DaveVoorhis feat. ChatGPT</div>
          </footer>
          <Notification />
        </div>
    </ErrorBoundary>
  );
}
