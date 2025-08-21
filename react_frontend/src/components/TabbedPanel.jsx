import React, { useState, useRef } from "react";

export default function TabbedPanel({ tabs }) {
  /*
  Example:

  const tabs = [
    { id: "demo", label: "🎬 Demo", content: <Demo /> },
    { id: "users", label: "👥 Users", content: <Users /> },
    { id: "roles", label: "🛡️ Roles", content: <Roles /> },
  ];

   ...

  <TabbedPanel tabs={tabs} />
  */

  const [activeTab, setActiveTab] = useState(tabs[0].id);
  const tabRefs = useRef([]);

  const handleKeyDown = (e, index) => {
    let newIndex = index;

    switch (e.key) {
      case "ArrowRight":
        newIndex = (index + 1) % tabs.length;
        break;
      case "ArrowLeft":
        newIndex = (index - 1 + tabs.length) % tabs.length;
        break;
      case "Home":
        newIndex = 0;
        break;
      case "End":
        newIndex = tabs.length - 1;
        break;
      default:
        return; // Exit if it's not a navigation key
    }

    e.preventDefault();
    setActiveTab(tabs[newIndex].id);
    tabRefs.current[newIndex]?.focus();
  };

  return (
    <div className="tab-container">
      {/* Tab Headers */}
      <div className="tab-header" role="tablist">
        {tabs.map((tab, index) => (
          <button
            key={tab.id}
            className={`tab-button ${activeTab === tab.id ? "active" : ""}`}
            onClick={() => setActiveTab(tab.id)}
            role="tab"
            aria-selected={activeTab === tab.id}
            tabIndex={activeTab === tab.id ? 0 : -1}
            ref={(el) => (tabRefs.current[index] = el)}
            onKeyDown={(e) => handleKeyDown(e, index)}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Tab Content */}
      <div
        className="tab-content"
        role="tabpanel"
        aria-labelledby={activeTab}
      >
        {tabs.find((t) => t.id === activeTab)?.content}
      </div>
    </div>
  );
}
