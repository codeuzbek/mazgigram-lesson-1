function login() {
    const email = document.getElementById("login-email").value;
    const password = document.getElementById("login-password").value;

    if (!email || !password) {
        alert("Enter all inputs")
    }

    const body = {
        "email": email,
        "password": password
    }

    fetch("http://localhost:8080/api/v1/auth/login", {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(body)
    }).then(response => {
        if (response.ok) {
            return response.json()
        } else {
            return Promise.reject(response.text());
        }
    }).then(data => {
        localStorage.setItem("userDetail", JSON.stringify(data))
        localStorage.setItem("", JSON.stringify(data))
        localStorage.setItem("jwtToken", data.jwt);
        localStorage.setItem("id", data.id);

        window.location.href = "./chat-main.html";
    }).catch(error => {
        error.then(errMessage => {
            alert(errMessage)
        })
    })
}