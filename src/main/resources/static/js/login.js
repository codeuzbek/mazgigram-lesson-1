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

function registration() {
    const name = document.getElementById("reg-name").value;
    const surname = document.getElementById("reg-surname").value;
    const email = document.getElementById("reg-email").value;
    const password = document.getElementById("reg-password").value;
    const confirmPassword = document.getElementById("reg-confirm-password").value;

    if (!name || !surname || !email || !password || !confirmPassword) {
        alert("Enter all inputs")
        return;
    }

    if (password !== confirmPassword) {
        alert("Passwords do not match")
        return;
    }

    const body = {
        "name": name,
        "surname": surname,
        "email": email,
        "password": password
    }

    fetch("http://localhost:8080/api/v1/auth/registration", {
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
        alert(data.message)

        document.getElementById("reg-name").value = "";
        document.getElementById("reg-surname").value = "";
        document.getElementById("reg-email").value = "";
        document.getElementById("reg-password").value = "";
        document.getElementById("reg-confirm-password").value = "";

        document.getElementById("login-email").value = email;
        document.getElementById("check").checked = false;
    }).catch(error => {
        error.then(errMessage => {
            alert(errMessage)
        })
    })
}
