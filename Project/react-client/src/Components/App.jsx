import '../CSS/App.css'
import '../utils/RestCalls.js'
import {useEffect, useState} from "react";
import {addProba, deleteProba, getProbe, updateProba, Login} from "../utils/RestCalls.js";
import ProbaTable from "./ProbaTable.jsx";
import ProbaForm from "./ProbaForm.jsx";
import LoginPage from "./LoginPage.jsx"

function App() {
    const [probe, setProbe] = useState([]);

    async function addFunction(proba) {
        try {
            await addProba(proba);
            const data = await getProbe();
            setProbe(data);
        } catch (err) {
            console.error(err)
        }
    }

    async function deleteFunction(id) {
        try {
            await deleteProba(id);
            const data = await getProbe();
            setProbe(data);
        } catch (err) {
            console.error(err)
        }
    }

    async function updateFunction(proba) {
        try {
            await updateProba(proba);
            const data = await getProbe();
            setProbe(data);
        } catch (err) {
            console.error(err)
        }
    }

    useEffect(() => {
        getProbe()
            .then(data => setProbe(data))
            .catch(error => console.log(error));
    }, []);

    return (
        <>
            <h1>Probele de înot - Management REST</h1>
            <div className="ProbaDiv">
                <div><ProbaForm addFunc={addFunction}/></div>
                <div><ProbaTable probe={probe} deleteFunction={deleteFunction} updateFunction={updateFunction}/></div>
            </div>
        </>
    );
}

export default App;
