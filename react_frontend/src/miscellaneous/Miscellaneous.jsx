import React, { useEffect, useMemo, useState, useRef } from 'react'
import Notification, { notifyError, notifyInformation } from '../components/Notification'
import { api, apiPost, apiDelete, apiPut, errorMessage } from '../api.js';
import { ADMIN_ROLE_ID } from '../constants.js'
import MiscButton from "./MiscButton"

const DEMO_URL = 'demo'
const ROLES_URL = 'users/roles';

export default function Demo() {
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

    return (
        <main className="content">
          <section className="card">
            <Notification />
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
            </div>
          </section>
        </main>
    );
}