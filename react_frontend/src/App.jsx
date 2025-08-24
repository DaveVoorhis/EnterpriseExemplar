import React, { useEffect, useState } from 'react'
import { api, apiPost, apiDelete, apiPut, errorMessage, setApiUser } from './api.js'
import { login, logout, userManager, handleCallback } from './authService'
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

  useEffect(() => {
    if (window.location.pathname === "/callback") {
      handleCallback().then(user => {
        setUser(user);
        window.history.replaceState({}, document.title, "/");
      });
    } else {
      userManager.getUser().then(user => {
        setUser(user);
      });
    }
  }, []);

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
              <div className="sub">Java + Spring Boot × React &nbsp;
              {user
                ? <button onClick={() => logout()}>Logout {user ? user.profile.name : undefined}</button>
                : undefined}
              </div>
            </div>
          </header>
          {user
            ? <TabbedPanel tabs={tabs} />
            : <h3>Please log in...&nbsp;
                <button onClick={() => login()}>Login</button></h3>
          }
          <footer className="footer">
            <div>For the JavaBackend by DaveVoorhis</div>
            <div className="muted">Frontend by DaveVoorhis and ChatGPT</div>
          </footer>
          <Notification />
        </div>
    </ErrorBoundary>
  );
}
