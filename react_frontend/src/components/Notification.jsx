import React from 'react'
import { ToastContainer, toast } from 'react-toastify';
import 'react-toastify/dist/ReactToastify.css';

export function notifyInformation(msg) {
    toast.info(msg);
}

export function notifySuccess(msg) {
    toast.success(msg);
}

export function notifyError(msg) {
    toast.error(msg);
}

export default function Notification() {
    return (
        <ToastContainer />
    )
}
