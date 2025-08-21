import React from "react"

export default function Table({ onClick, prompt, buttonText }) {
    return (
        <p>
        {prompt}:&nbsp;
        <button className="btn btn-secondary" onClick={onClick}>{buttonText}</button>
        </p>
    )
}
