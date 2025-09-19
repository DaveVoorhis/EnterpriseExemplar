import React, { useEffect, useState } from "react"
import Notification, { notifyError } from '../components/Notification'
import { api, apiPost, apiDelete, apiPut, errorMessage } from '../utils/api'
import { ADMIN_ROLE_ID } from '../utils/constants'
import Table from "../components/Table"

const USERS_URL = 'users'

export default function UserRoles({ userId }) {
  const [items, setItems] = useState([])

  useEffect(() => { refresh() }, [])

  const obtainCurrentUser = () => api(`${USERS_URL}/current`)
  const obtainAllRoles = () => api(`${USERS_URL}/roles`)
  const obtainUsersRoles = () => api(`${USERS_URL}/${userId}/roles`)
  const grantRole = (roleId) => apiPost(`${USERS_URL}/${userId}/roles/${roleId}`)
  const revokeRole = (roleId) => apiDelete(`${USERS_URL}/${userId}/roles/${roleId}`)

  const refresh = async () => {
    try {
      const [allRoles, usersRoles] = await Promise.all([
        obtainAllRoles(),
        obtainUsersRoles()
      ])
      const userRoleIds = usersRoles.map(r => r.roleId)
      const data = allRoles.map(role => ({
        checked: userRoleIds.includes(role.roleId),
        role
      }))
      setItems(data.sort((a, b) => Number(a.role.roleId) - Number(b.role.roleId)))
    } catch (e) {
      notifyError(errorMessage(e))
    }
  }

  const handleChange = async (roleId, isChecked) => {
    const granting = !isChecked
    if (granting) {
      await grantRole(roleId)
    } else {
      const currentUser = await obtainCurrentUser()
      if (roleId === ADMIN_ROLE_ID && currentUser.userId === userId) {
        if (!confirm(
          `You're trying to remove your own admin role!
          If you proceed, you will lock yourself out and will have to manually edit the database.
          Continue?`
        )) return
      }
      if (!confirm(`Revoke role for user ${userId}?`)) return
      await revokeRole(roleId)
    }
    refresh()
  }

  const columns = [
    { header: "Role", accessor: "role.roleId", className: "mono", cell: row => row.role.roleId },
    { header: "Name", accessor: "role.name", className: "mono", cell: row => row.role.name },
    { header: "Description", accessor: "role.description", className: "mono", cell: row => row.role.description },
    { header: "Assigned", accessor: "assigned", className: "mono",
      cell: row =>
        <input
          id={`${userId}:${row.role.roleId}`}
          type="checkbox"
          checked={row.checked}
          onChange={() => handleChange(row.role.roleId, row.checked)}
        />
    }
  ];

  return (
    <Table
      columns={columns}
      data={items}
      emptyMessage="No roles found."
    />
  )
}
