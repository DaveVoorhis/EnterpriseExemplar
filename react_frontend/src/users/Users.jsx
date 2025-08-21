import React, { useEffect, useMemo, useState } from 'react'
import Notification, { notifyError } from '../components/Notification'
import { api, apiPost, apiDelete, apiPut, errorMessage } from '../api.js'
import Table from "../components/Table"
import UserRoles from './UserRoles'

const USERS_URL = 'users'

export default function Users() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(false)
  const [query, setQuery] = useState('')

  useEffect(() => { refresh() }, [])

  const getCurrentUser = async () => api(`${USERS_URL}/current`)
  const getUsers = async () => api(USERS_URL)
  const updateUser = async (id, flag) => apiPut(`${USERS_URL}/${id}`, { enabled: flag })

  async function refresh() {
    setLoading(true)
    try {
      const data = await getUsers()
      setItems((data || []).sort((a, b) => Number(a.userId) - Number(b.userId)))
    } catch (e) {
      notifyError(errorMessage(e))
    } finally {
      setLoading(false)
    }
  }

  async function handleEnable(id) {
    if (!confirm(`Enable user ${id}?`)) return
    try {
      await updateUser(id, true)
      setItems(prev => prev.map(u => u.userId === id ? { ...u, enabled: true } : u))
    } catch (e) {
      notifyError(errorMessage(e))
    }
  }

  async function handleDisable(id) {
    try {
      const currentUser = await getCurrentUser()
      if (currentUser.userId === id) {
        const proceed = confirm(
          `You're trying to disable yourself! If you proceed, you will lock yourself out and the only way back in will be to manually edit the database! Do you wish to do this?`
        )
        if (!proceed) return
      }
      if (!confirm(`Disable user ${id}?`)) return
      await updateUser(id, false);
      setItems(prev => prev.map(u => u.userId === id ? { ...u, enabled: false } : u))
    } catch (e) {
      notifyError(errorMessage(e))
    }
  }

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase()
    if (!q) return items
    return items.filter(it =>
      [String(it.userId), it.email]
        .filter(Boolean)
        .some(v => String(v).toLowerCase().includes(q))
    )
  }, [items, query])

  const columns = [
    { header: "ID", accessor: "userId", className: "mono" },
    { header: "Email", accessor: "email", className: "mono" },
    { header: "Last login", accessor: "lastLogin", className: "mono" },
    { header: "Enabled", accessor: "enabled", className: "mono", cell: row => row.enabled ? 'Yes' : 'No' },
    { header: "Roles", accessor: "permissions", className: "mono", cell: row => <UserRoles userId={row.userId} /> },
    { header: "", accessor: "actions", className: "aria-label",
      cell: row => row.enabled
            ? <button className="btn btn-primary" onClick={() => handleDisable(row.userId)}>Disable</button>
            : <button className="btn btn-primary" onClick={() => handleEnable(row.userId)}>Enable</button>
    }
  ];

  return (
    <main className="content">
      <section className="card">
        <Notification />
        <div className="list-header">
          <h2>All Users</h2>
          <input
            className="search"
            type="search"
            placeholder="Search by ID and email…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            aria-label="Search users"
          />
          <button className="btn btn-secondary" onClick={refresh} disabled={loading}>
            {loading ? 'Refreshing…' : 'Refresh'}
          </button>
        </div>
        <Table
          columns={columns}
          data={filtered}
          emptyMessage="No users found."
        />
      </section>
    </main>
  )
}
