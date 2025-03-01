document.addEventListener("DOMContentLoaded", async (e) => {

const response = await fetch("/api/users/me/profile" ,{
    method: "GET",
    credentials: "include"
});

const result = await response.json();

if(response.status === 200){
    document.querySelector(".user-nickname").textContent = result.data.nickname
    document.querySelector(".user-email").textContent = result.data.email
    document.querySelector(".user-address").textContent = result.data.address
    document.querySelector(".user-created").textContent = result.data.createdAt
    document.querySelector(".user-type").textContent = result.data.accountType
}
});

