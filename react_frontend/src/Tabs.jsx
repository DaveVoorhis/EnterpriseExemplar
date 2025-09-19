import React, { useEffect, useState } from 'react'
import { api } from './utils/api'
import TabbedPanel from './components/TabbedPanel'
import Demo from './demo/Demo'
import Users from './users/Users'
import Roles from './roles/Roles'
import Miscellaneous from './miscellaneous/Miscellaneous'

export default function Tabs() {
  const [tabs, setTabs] = useState([])

  const hasPermission = (permissionName) => api(`users/permission/${permissionName}`);

  const tabData = [
     { id: "demo", label: "🎬 Demo", content: <Demo />, permit: () => hasPermission('GET_ALL_DEMOS') },
     { id: "users", label: "👥 Users", content: <Users />, permit: () => hasPermission('ADMIN') },
     { id: "roles", label: "🛡️ Roles", content: <Roles />, permit: () => hasPermission('ADMIN') },
     { id: "misc", label: "🌀 Miscellaneous", content: <Miscellaneous /> }
  ];

  useEffect(() => { refresh() }, [])

  const refresh = () => Promise.all(tabData.map(resolveTab))
     .then(tabs => setTabs(tabs.filter(tab => tab != null)));

  const resolveTab = (tab) => tab.permit
     ? tab.permit().then(permitted => permitted ? tab : null)
     : Promise.resolve(tab);

  if (!tabs.length) return <div align="center">Loading...</div>

  return <TabbedPanel tabs={tabs} />
}
