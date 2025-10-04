import React, { useEffect, useMemo, useState, useRef } from 'react'
import Notification, { notifyError } from '../components/Notification'
import { api, apiPost, apiDelete, apiPut, errorMessage } from '../utils/api'
import Table from "../components/Table"

const DEMO_URL = 'demo'

function emptyIn() {
  return { name: '', address: '' }
}

export default function Demo() {
  const [items, setItems] = useState([])
  const [loading, setLoading] = useState(false)
  const [createForm, setCreateForm] = useState(emptyIn())
  const [editId, setEditId] = useState(null)
  const [editForm, setEditForm] = useState(emptyIn())
  const [query, setQuery] = useState('')

  const editFirstInputRef = useRef(null)
  const nameInputRef = useRef(null)

  useEffect(() => { refresh() }, [])

  useEffect(() => {
    if (editFirstInputRef.current) {
      editFirstInputRef.current.focus()
    }
  }, [editId])

  const getDemos = async () => api(DEMO_URL)
  const addDemo = async (payload) => apiPost(DEMO_URL, payload)
  const updateDemo = async (payload) => apiPut(`${DEMO_URL}/${editId}`, payload)
  const deleteDemo = async (id) => apiDelete(`${DEMO_URL}/${id}`)

  async function refresh() {
    setLoading(true)
    try {
      const data = await getDemos()
      setItems((data || []).sort((a, b) => Number(a.demoId) - Number(b.demoId)))
    } catch (e) {
      notifyError(errorMessage(e))
    } finally {
      setLoading(false)
    }
  }

  function beginEdit(item) {
    if (editId != null || loading) return
    setEditId(item.demoId)
    setEditForm({ name: item.name, address: item.address })
  }

  function cancelEdit() {
    setEditId(null)
    setEditForm(emptyIn())
  }

  function makePayload(form) {
    const payload = {
      name: form.name.trim(),
      address: form.address.trim()
    }
    if (!payload.name) throw Error('Name is required')
    if (!payload.address) throw Error('Address is required')
    return payload
  }

  function prepareHandler(event) {
    event.preventDefault()
  }

  async function handleUpdate(e) {
    if (editId == null) return
    prepareHandler(e)
    try {
      const payload = makePayload(editForm)
      await updateDemo(payload)
      setItems((prev) =>
        prev.map((it) =>
          it.demoId === editId ? { ...it, ...payload } : it
        )
      )
      cancelEdit()
    } catch (e) {
      notifyError(errorMessage(e))
    }
  }

  async function handleCreate(e) {
    prepareHandler(e)
    try {
      const payload = makePayload(createForm)
      const created = await addDemo(payload)
      setItems((prev) => [...prev, created].sort((a, b) => a.demoId - b.demoId))
      setCreateForm(emptyIn())
      nameInputRef.current?.focus()
    } catch (e) {
      notifyError(errorMessage(e))
    }
  }

  async function handleDelete(id) {
    if (!confirm(`Delete item ${id}?`)) return
    try {
      await deleteDemo(id)
      setItems((prev) => prev.filter((it) => it.demoId !== id))
    } catch (e) {
      notifyError(errorMessage(e))
    }
  }

  const handleCreateChange = (field) => (e) =>
    setCreateForm((prev) => ({ ...prev, [field]: e.target.value }))

  const handleEditChange = (field) => (e) =>
    setEditForm((prev) => ({ ...prev, [field]: e.target.value }))

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase()
    if (!q) return items
    return items.filter((it) =>
      [String(it.demoId), it.name, it.address]
        .filter(Boolean)
        .some((v) => String(v).toLowerCase().includes(q))
    )
  }, [items, query])

  const columns = [
    { header: "ID", accessor: "demoId", className: "mono" },
    {
      header: "Name",
      accessor: "name",
      className: "mono",
      cell: row =>
        editId === row.demoId
            ?
              <input
                className="input"
                value={editForm.name}
                onChange={e => setEditForm(f => ({ ...f, name: e.target.value }))}
              />
            : <span>{row.name}</span>,
      onClick: beginEdit
    },
    {
      header: "Address",
      accessor: "address",
      className: "mono",
      cell: row =>
        editId === row.demoId
            ?
              <input
                className="input"
                value={editForm.address}
                onChange={e => setEditForm(f => ({ ...f, address: e.target.value }))}
              />
            : <span>{row.address}</span>,
      onClick: beginEdit
    },
    {
      header: "",
      accessor: "actions",
      cell: row =>
        editId === row.demoId
            ?
              <>
                <button className="btn btn-primary" onClick={handleUpdate}>Save</button>
                <button className="btn btn-link" onClick={cancelEdit}>Cancel</button>
              </>
            : <button className="btn btn-danger" onClick={() => handleDelete(row.demoId)}>Delete</button>
    }
  ];

  return (
    <main className="content" id="demo_content">
      <section className="card">
        <h2>Create Demo</h2>
        <form onSubmit={handleCreate} className="grid-form">
          <label className="label">
            <span className="label-text">Name</span>
            <input
              ref={nameInputRef}
              className="input"
              placeholder="Sherlock Holmes"
              value={createForm.name}
              onChange={e => setCreateForm(f => ({ ...f, name: e.target.value }))}
            />
          </label>
          <label className="label">
            <span className="label-text">Address</span>
            <input
              className="input"
              placeholder="221B Baker Street"
              value={createForm.address}
              onChange={e => setCreateForm(f => ({ ...f, address: e.target.value }))}
            />
          </label>
          <button className="btn btn-primary" type="submit">Add</button>
        </form>
      </section>

      <section className="card">
        <div className="list-header">
          <h2>All Demos</h2>
          <input
            className="search"
            type="search"
            placeholder="Search by ID, name, address…"
            value={query}
            onChange={e => setQuery(e.target.value)}
            aria-label="Search demos"
          />
          <button className="btn btn-secondary" onClick={refresh} disabled={loading}>
            {loading ? "Refreshing…" : "Refresh"}
          </button>
        </div>
        <Table
          columns={columns}
          data={filtered}
          emptyMessage="No demos found"
        />
      </section>
    </main>
  );
}