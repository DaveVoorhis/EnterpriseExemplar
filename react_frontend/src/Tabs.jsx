import React, { useEffect, useState } from 'react'
import { api, apiPost, apiDelete, apiPut, errorMessage, setApiUser } from './api.js'
import TabbedPanel from './components/TabbedPanel'
import Demo from './demo/Demo'
import Users from './users/Users'
import Roles from './roles/Roles'
import Miscellaneous from './miscellaneous/Miscellaneous'

export default function Tabs() {
  const [tabs, setTabs] = useState([])

  const hasPermission = async (permissionName) => await api(`users/permission/${permissionName}`);

  useEffect(() => refresh, [])

  // TODO: Rewrite. This is repellently ugly.
  async function refresh() {
      const tabData = [
         { id: "demo", label: "🎬 Demo", content: <Demo />, permit: () => hasPermission('GET_ALL_DEMOS') },
         { id: "users", label: "👥 Users", content: <Users />, permit: () => hasPermission('ADMIN') },
         { id: "roles", label: "🛡️ Roles", content: <Roles />, permit: () => hasPermission('ADMIN') },
         { id: "misc", label: "🌀 Miscellaneous", content: <Miscellaneous /> }
      ];
      const tabs = [];
      for (const tab of tabData) {
          if (!tab.permit) {
              tabs.push(tab);
          } else {
              var permitted = await tab.permit();
              if (permitted) {
                  tabs.push(tab);
              }
          }
      };
      setTabs(tabs);
  }

  return (
    (tabs.length) ? <TabbedPanel tabs={tabs} /> : undefined
  );
}
