import React, { useEffect, useState } from "react"
import Notification, { notifyError } from '../components/Notification'
import { api, apiPost, apiDelete, apiPut, errorMessage } from '../api.js'
import { ADMIN_ROLE_ID } from '../constants.js'
import Table from "../components/Table"

const USERS_URL = 'users'

export default function RolePermissions({ roleId }) {
  const [items, setItems] = useState([]);

  useEffect(() => { refresh() }, [roleId]);

  const ROLES_URL = `${USERS_URL}/role/${roleId}`;

  const obtainAllPermissions = () => api(`${USERS_URL}/permissions`);
  const obtainRolePermissions = () => api(`${ROLES_URL}/permissions`);
  const grantPermission = (permissionName) => apiPost(`${ROLES_URL}/permissions/${permissionName}`);
  const revokePermission = (permissionName) => apiDelete(`${ROLES_URL}/permissions/${permissionName}`);

  const refresh = async () => {
    try {
      const [allPermissions, rolePermissions] = await Promise.all([
        obtainAllPermissions(),
        obtainRolePermissions()
      ]);
      const rolePermissionNames = rolePermissions.map(p => p.name);

      var taggedPermissions = []
      var lastCategory = null;
      allPermissions
        .map(perm => ({ ...perm, checked: rolePermissionNames.includes(perm.name) }))
        .sort((a, b) => a.category.localeCompare(b.category) || a.name.localeCompare(b.name))
        .forEach(perm => {
          if (perm.category != lastCategory) {
              taggedPermissions.push({category: perm.category, heading: true})
              lastCategory = perm.category
          }
          taggedPermissions.push({...perm, heading: false})
        });

      setItems(taggedPermissions);
    } catch (e) {
      notifyError(errorMessage(e));
    }
  };

  const handleChange = async (permissionName, currentCheck) => {
    const granting = !currentCheck;
    try {
      if (granting) {
        await grantPermission(permissionName);
      } else {
        await revokePermission(permissionName);
      }
      await refresh();
    } catch (e) {
      notifyError(errorMessage(e));
    }
  };

  if (items.length === 0) {
      return <span>No permissions.</span>
  }

  const columns = [
    { header: "Permission", accessor: "name", className: "mono" },
    { header: "Description", accessor: "description", className: "mono" },
    { header: "Assigned", accessor: "lastLogin", className: "mono",
      cell: row => roleId === ADMIN_ROLE_ID
          ? row.checked ? <span>✅</span> : undefined
          : <input
                id={`${roleId}:${row.name}`}
                type="checkbox"
                checked={row.checked}
                onChange={() => handleChange(row.name, row.checked)}
            />
    }
  ];
  const subheadingConditionAccessor = "heading"
  const subheadingValueAccessor = "category"

  return (
      <Table
          columns={columns}
          data={items}
          emptyMessage="No role permissions found."
          subheadingConditionAccessor = {subheadingConditionAccessor}
          subheadingValueAccessor = {subheadingValueAccessor}
      />
  );
}
