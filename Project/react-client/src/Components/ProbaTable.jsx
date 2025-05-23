import {useEffect, useState} from "react";
import {getProbe} from "../utils/RestCalls.js";
import '../CSS/ProbaTable.css';
import '../CSS/App.css';

function ProbaRow({proba, deleteFunction, updateFunction}) {
    const [distanta, setDistanta] = useState(proba.distanta);

    function handleDelete() {
        console.log(proba.id);
        deleteFunction(proba.id);
    }

    useEffect(() => {
        setDistanta(proba.distanta);
    }, [proba.distanta]);

    function handleUpdate(e) {
        if (e.key === "Enter") {
            e.preventDefault();
            const newVal = Math.max(0, parseInt(e.target.innerText, 10) || 0);
            setDistanta(newVal);
            proba.distanta = newVal;
            updateFunction(proba);
            e.target.blur();
        }
        if (e.key === "Escape") {
            e.target.innerText = distanta;
            e.target.blur();
        }
    }

    return (
        <tr>
            <td>{proba.id}</td>
            <td>{proba.stil}</td>
            <td contentEditable={true} onKeyDown={handleUpdate}>{distanta}</td>
            <td>
                <button onClick={handleDelete}>Delete</button>
            </td>
        </tr>
    );
}

export default function ProbaTable({probe, deleteFunction, updateFunction}) {
    let rows = [];
    probe.forEach((proba) => {
        rows.push(
            <ProbaRow
                key={proba.id}
                proba={proba}
                deleteFunction={deleteFunction}
                updateFunction={updateFunction}
            />
        );
    });

    return (
        <div className="ProbaTable">
            <table className="center">
                <thead>
                <tr>
                    <th>ID</th>
                    <th>Stil</th>
                    <th>Distanță</th>
                    <th>Șterge</th>
                </tr>
                </thead>
                <tbody>{rows}</tbody>
            </table>
        </div>
    );
}
