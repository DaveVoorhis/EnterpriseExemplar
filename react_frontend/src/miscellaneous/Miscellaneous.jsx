import React, { useEffect, useMemo, useState, useRef } from 'react'
import Notification, { notifyError, notifyInformation } from '../components/Notification'
import { api, apiPost, apiDelete, apiPut, errorMessage } from '../api.js';
import { ADMIN_ROLE_ID } from '../constants.js'
import MiscButton from "./MiscButton"

const DEMO_URL = 'demo'
const ROLES_URL = 'users/roles';

export default function Miscellaneous() {
    const addDemo = async (payload) => apiPost(DEMO_URL, payload)
    const removeRole = async (id) => apiDelete(`${ROLES_URL}/${id}`)

    async function attemptPostWithInvalidPayload() {
        try {
            await addDemo({ zot: 1, zip: 2 })
        } catch (e) {
            notifyError(errorMessage(e));
        }
    }

    async function attemptDeleteThatIsDisallowed() {
        try {
          await removeRole(ADMIN_ROLE_ID)
        } catch (e) {
            notifyError(errorMessage(e));
        }
    }

    async function showNotification() {
        notifyInformation("You have received a notification.");
    }

    const hasPermission = async (permissionName) => await api(`users/permission/${permissionName}`);

    async function checkPermission(permissionName) {
        try {
            alert(await hasPermission(permissionName))
        } catch (e) {
            notifyError(errorMessage(e));
        }
    }

    return (
        <main className="content" id="miscellaneous_content">
          <section className="card">
            <div>
              <h2>Miscellaneous</h2>
              <MiscButton
                prompt='Demonstrate invalid POST payload error by clicking this button'
                buttonText='Make an Error'
                onClick={attemptPostWithInvalidPayload}
              />
              <MiscButton
                prompt='Demonstrate disallowed DELETE error by clicking this button'
                buttonText='Make an Error'
                onClick={attemptDeleteThatIsDisallowed}
              />
              <MiscButton
                prompt='Display an informational notification'
                buttonText='Send Notification'
                onClick={showNotification}
              />
              <MiscButton
                prompt='Check user has permission GET_ALL_DEMOS'
                buttonText='Check permission'
                onClick={() => checkPermission('GET_ALL_DEMOS')}
              />
              <MiscButton
                prompt='Check user has permission ADMIN'
                buttonText='Check permission'
                onClick={() => checkPermission('ADMIN')}
              />
            </div>
          </section>
        </main>
    );
}