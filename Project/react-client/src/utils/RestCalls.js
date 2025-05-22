import { URL } from './Constants.js';

function status(response) {
    if (response.status >= 200 && response.status < 300) {
        return Promise.resolve(response);
    } else {
        return Promise.reject(new Error(response.statusText));
    }
}

function json(response) {
    return response.json();
}

// GET /api/probe
export function getProbe() {
    let headers = new Headers();
    headers.append('Accept', 'application/json');

    let request = new Request(URL, {
        method: 'GET',
        headers: headers,
        mode: 'cors'
    });

    return fetch(request)
        .then(status)
        .then(json)
        .then(data => {
            console.log('Probe:', data);
            return data;
        })
        .catch(err => {
            console.error('Eroare GET:', err);
            return Promise.reject(err);
        });
}

// DELETE /api/probe/{id}
export function deleteProba(id) {
    let headers = new Headers();
    headers.append('Accept', 'application/json');

    let request = new Request(`${URL}/${id}`, {
        method: 'DELETE',
        headers: headers,
        mode: 'cors'
    });

    return fetch(request)
        .then(status)
        .then(response => response.text())
        .catch(err => {
            console.error('Eroare DELETE:', err);
            return Promise.reject(err);
        });
}

// POST /api/probe
export function addProba(proba) {
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    let request = new Request(URL, {
        method: 'POST',
        headers: headers,
        mode: 'cors',
        body: JSON.stringify(proba)
    });

    return fetch(request)
        .then(status)
        .then(response => response.text())
        .catch(err => {
            console.error('Eroare POST:', err);
            return Promise.reject(err);
        });
}

// PUT /api/probe/{id}
export function updateProba(proba) {
    let headers = new Headers();
    headers.append('Accept', 'application/json');
    headers.append('Content-Type', 'application/json');

    let request = new Request(`${URL}/${proba.id}`, {
        method: 'PUT',
        headers: headers,
        mode: 'cors',
        body: JSON.stringify(proba)
    });

    return fetch(request)
        .then(status)
        .then(response => response.text())
        .catch(err => {
            console.error('Eroare PUT:', err);
            return Promise.reject(err);
        });
}

