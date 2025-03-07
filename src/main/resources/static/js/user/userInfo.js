import {modalUtil} from "./modal.js";
import {loadTopInfo} from "../common/api-declare.js";

/* 프로필 페이지 안 회원 정보를 불러오는 기능 userInfo.js*/

document.addEventListener("DOMContentLoaded", async (e) => {

    modalUtil();
    await loadTopInfo();

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

