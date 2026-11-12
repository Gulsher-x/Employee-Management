
const BASE_URL = "http://localhost:8080";

export async function apiRequest(endpoint, options = {}) {
    const token = sessionStorage.getItem("authToken");

    const response = await fetch(`${BASE_URL}${endpoint}`, {
        ...options,
        headers: {
            "Content-Type": "application/json",
            Authorization: `Basic ${token}`,
            ...options.headers,
        },
    });

    if (!response.ok) {
        throw new Error(`API Error: ${response.status}`);
    }

    if (response.status === 204) {
        return null;
    }
    const contentType = response.headers.get("content-type");

    if (contentType && contentType.includes("application/json")) {
        return response.json();
    }

    return response.text();


}
