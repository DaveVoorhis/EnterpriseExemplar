import React, { useEffect, useMemo, useRef, useState } from 'react'
import Notification, { notifyError } from '../components/Notification'
import { api, apiPost, apiDelete, apiPut, errorMessage } from '../api.js'
import { ADMIN_ROLE_ID, specialRoles } from '../constants.js'
import Table from "../components/Table"
import RolePermissions from "./RolePermissions"

const ROLES_URL = 'users/roles';

function emptyIn() {
  return { name: '', description: '', active: false };
}

export default function Roles() {
  const [items, setItems] = useState([]);
  const [loading, setLoading] = useState(false);
  const [createForm, setCreateForm] = useState(emptyIn());
  const [editForm, setEditForm] = useState(emptyIn())
  const [query, setQuery] = useState('');
  const [editId, setEditId] = useState(null)

  const getRoles = async () => api(ROLES_URL);
  const addRole = async (payload) => apiPost(ROLES_URL, payload);
  const updateRole = async (roleId, payload) => apiPut(`${ROLES_URL}/${roleId}`, payload);
  const deleteRole = async (roleId) => apiDelete(`${ROLES_URL}/${roleId}`);

  const deleteAllowed = (roleId) => !specialRoles.includes(roleId);

  function beginEdit(item) {
    if (editId != null || loading) return
    setEditId(item.roleId)
    setEditForm({ name: item.name, description: item.description })
  }

  function cancelEdit() {
    setEditId(null)
    setEditForm(emptyIn())
  }

  useEffect(() => { refresh() }, []);

  async function refresh() {
    setLoading(true);
    try {
      const data = await getRoles();
      setItems((data || []).sort((a, b) => Number(a.roleId) - Number(b.roleId)));
    } catch (e) {
      notifyError(errorMessage(e));
    } finally {
      setLoading(false);
    }
  }

  const doUpdateRole = async (roleId, payload) => {
    if (roleId === ADMIN_ROLE_ID && !payload.active) {
        if (!confirm('Disabling the admin role may lock you out, with editing the database manually the only way back in! Are you sure wish to do so?')) return;
    }
    try {
        await updateRole(roleId, payload);
        await refresh();
    } catch (e) {
      notifyError(errorMessage(e));
    }
  };

  const removeRole = async (id) => {
    if (!confirm(`Delete role ${id}?`)) return;
    try {
      await deleteRole(id);
      await refresh();
    } catch (e) {
      notifyError(errorMessage(e));
    }
  };

  const handleActivation = async (role, flag) => await doUpdateRole(role.roleId,
      { name: role.name, description: role.description, active: flag });

  function makePayload(form) {
    const payload = {
      name: form.name?.trim(),
      description: form.description?.trim()
    }
    if (!payload.name) throw Error('Name is required')
    if (!payload.description) throw Error('Description is required')
    return payload
  }

  async function handleUpdate(e) {
    if (editId == null) return
    event.preventDefault()
    try {
      const payload = makePayload(editForm)
      await updateRole(editId, payload)
      setItems((prev) =>
        prev.map((it) =>
          it.roleId === editId ? { ...it, ...payload } : it
        )
      )
      cancelEdit()
    } catch (e) {
      notifyError(errorMessage(e))
    }
  }

  async function handleCreate(e) {
    event.preventDefault()
    try {
      const payload = makePayload(createForm)
      const created = await addRole(payload);
      setItems((prev) => [...prev, created].sort((a, b) => a.roleId - b.roleId))
      setCreateForm(emptyIn());
    } catch (e) {
      notifyError(errorMessage(e));
    }
  }

  const filtered = useMemo(() => {
    const q = query.trim().toLowerCase();
    if (!q) return items;
    return items.filter(it =>
      [String(it.roleId), it.name, it.description]
        .filter(Boolean)
        .some(v => String(v).toLowerCase().includes(q))
    );
  }, [items, query]);

  const columns = [
    { header: "ID", accessor: "roleId", className: "mono" },
    { header: "Name",
        accessor: "name",
        className: "mono",
        cell: row => editId === row.roleId
            ? <input
                className="input"
                value={editForm.name}
                onChange={e => setEditForm(f => ({ ...f, name: e.target.value }))}
              />
            : <span>{row.name}</span>,
        onClick: beginEdit
    },
    { header: "Description",
        accessor: "description",
        className: "mono",
        cell: row => editId === row.roleId
            ? <input
                className="input"
                value={editForm.description}
                onChange={e => setEditForm(f => ({ ...f, description: e.target.value }))}
              />
            : <span>{row.description}</span>,
        onClick: beginEdit
    },
    { header: "Active", accessor: "active", className: "mono", cell: row => row.active ? 'Yes' : 'No'  },
    { header: "Permissions", accessor: "permissions", className: "mono", cell: row => <RolePermissions roleId={row.roleId} /> },
    { accessor: "action1", cell: row => editId === row.roleId
        ? undefined
        : row.roleId != ADMIN_ROLE_ID
            ? row.active
               ? <button className="btn btn-primary" onClick={() => handleActivation(row, false)}>Deactivate</button>
               : <button className="btn btn-primary" onClick={() => handleActivation(row, true)}>Activate</button>
            : undefined
    },
    { accessor: "action2", cell: row => editId === row.roleId
        ? <>
            <button className="btn btn-primary" onClick={handleUpdate}>Save</button>
            <button className="btn btn-link" onClick={cancelEdit}>Cancel</button>
          </>
        : deleteAllowed(row.roleId)
            ? <button className="btn btn-danger" onClick={() => removeRole(row.roleId)}>Delete</button>
            : undefined
    }
  ];

  return (
    <main className="content" id="roles_content">
      <section className="card">
        <h2>Create Role</h2>
        <form onSubmit={handleCreate} className="grid-form">
          <label className="label">
            <span className="label-text">Name</span>
            <input
              className="input"
              placeholder="Editor"
              value={createForm.name}
              onChange={(e) => setCreateForm(f => ({ ...f, name: e.target.value }))}
            />
          </label>
          <label className="label">
            <span className="label-text">Description</span>
            <input
              className="input"
              placeholder="Can edit but not add"
              value={createForm.description}
              onChange={(e) => setCreateForm(f => ({ ...f, description: e.target.value }))}
            />
          </label>
          <button className="btn btn-primary" type="submit">Add</button>
        </form>
      </section>

      <section className="card">
        <div className="list-header">
          <h2>All Roles</h2>
          <input
            className="search"
            type="search"
            placeholder="Search by ID, name and description…"
            value={query}
            onChange={(e) => setQuery(e.target.value)}
            aria-label="Search roles"
          />
          <button className="btn btn-secondary" onClick={refresh} disabled={loading}>
            {loading ? 'Refreshing…' : 'Refresh'}
          </button>
        </div>
        <Table
            columns={columns}
            data={filtered}
            emptyMessage="No user roles found."
        />
      </section>
    </main>
  );
}
