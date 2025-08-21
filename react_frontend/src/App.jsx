import React from "react";
import TabbedPanel from "./components/TabbedPanel";
import ErrorBoundary from './components/ErrorBoundary';
import Demo from './demo/Demo';
import Users from './users/Users';
import Roles from './roles/Roles';
import Miscellaneous from './miscellaneous/Miscellaneous';

const tabs = [
    { id: "demo", label: "🎬 Demo", content: <Demo /> },
    { id: "users", label: "👥 Users", content: <Users /> },
    { id: "roles", label: "🛡️ Roles", content: <Roles /> },
    { id: "misc", label: "🌀 Miscellaneous", content: <Miscellaneous /> }
];

export default function App() {
  return (
    <ErrorBoundary>
        <div className="shell">
          <header className="header">
            <div className="header-inner">
              <div className="header-title">
                  <img className="header-image" src="/images/Logo.png"/>&nbsp;
                  <h1>EnterpriseExemplar</h1>
              </div>
              <div className="sub">Java + Spring Boot × React</div>
            </div>
          </header>
          <TabbedPanel tabs={tabs} />
          <footer className="footer">
            <div>For the JavaBackend by DaveVoorhis</div>
            <div className="muted">Frontend by DaveVoorhis and ChatGPT</div>
          </footer>
        </div>
    </ErrorBoundary>
  );
}
