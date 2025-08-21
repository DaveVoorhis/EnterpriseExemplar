import React from "react";

export default function Table({ columns, data, emptyMessage, subheadingConditionAccessor, subheadingValueAccessor }) {
  return (
    <div className="table-wrap">
      <table className="table">
        <thead>
          <tr>
            {columns.map(col => (
              <th key={col.accessor || col.header} scope="col">
                {col.header}
              </th>
            ))}
          </tr>
        </thead>
        <tbody>
          {data.length === 0
            ? <tr>
                  <td colSpan={columns.length} className="empty">
                    {emptyMessage || "No items"}
                  </td>
                </tr>
            : data.map((row, rowIndex) =>
              <tr key={row.id || rowIndex}>
              {
                 row[subheadingConditionAccessor]
                   ? <td
                       key={row.id + ':' + subheadingConditionAccessor}
                       className="table-subheading"
                       colSpan={columns.length}
                     >
                       {row[subheadingValueAccessor]}
                     </td>
                   : columns.map(column => (
                      <td
                        key={column.accessor || column.header}
                        className={column.className || ""}
                        onClick={column.onClick
                            ? () => column.onClick(row)
                            : undefined}
                      >
                        {column.cell
                            ? column.cell(row)
                            : row[column.accessor]}
                      </td>
                     ))
              }
              </tr>
            )
          }
        </tbody>
      </table>
    </div>
  );
}
