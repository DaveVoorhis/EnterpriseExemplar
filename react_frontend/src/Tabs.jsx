import React, { useEffect, useState } from 'react'
import { api } from './utils/api'
import TabbedPanel from './components/TabbedPanel'
import Demo from './demo/Demo'
import Users from './users/Users'
import Roles from './roles/Roles'
import Miscellaneous from './miscellaneous/Miscellaneous'

export default function Tabs() {
  const [tabs, setTabs] = useState([])
  const [authFailed, setAuthFailed] = useState(false)
  const [loading, setLoading] = useState(true)

  const hasPermission = (permissionName) => api(`users/permission/${permissionName}`)

  const tabData = [
    { id: "demo", label: "🎬 Demo", content: <Demo />, permit: () => hasPermission('GET_ALL_DEMOS') },
    { id: "users", label: "👥 Users", content: <Users />, permit: () => hasPermission('ADMIN') },
    { id: "roles", label: "🛡️ Roles", content: <Roles />, permit: () => hasPermission('ADMIN') },
    { id: "misc", label: "🌀 Miscellaneous", content: <Miscellaneous /> }
  ]

  useEffect(() => { refresh() }, [])

  const refresh = async () => {
    setLoading(true)
    let hadError = false

    const results = await Promise.all(
      tabData.map(tab =>
        tab.permit
          ? tab.permit()
              .then(permitted => (permitted ? tab : null))
              .catch(() => {
                hadError = true
                return null
              })
          : Promise.resolve(tab)
      )
    )

    const permittedTabs = results.filter(t => t != null)
    setTabs(permittedTabs)

    if (hadError || permittedTabs.length === 0) {
      setAuthFailed(true)
    }

    setLoading(false)
  }

  if (loading) return <div className="content">Loading...</div>
  if (authFailed) return <div className="content">You are no longer authorised. Please log out and log in again.</div>

  return <TabbedPanel tabs={tabs} />
}
