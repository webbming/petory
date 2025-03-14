import {apiClient} from "../common/api.js";

/* findPassword.js 비밀번호 찾기 기능 */
const findPasswordForm = document.querySelector("#findPasswordForm")

findPasswordForm.addEventListener("submit" , async (e) =>{
    e.preventDefault();
    const error = document.querySelector(".error")
    const userId = document.querySelector("#userId").value.trim();
    const email = document.querySelector('#email').value.trim();

    const jsonData = {
        userId : userId,
        email : email
    }
    error.innerHTML = "";
    error.style.display = "block";
    error.textContent = "잠시만 기다려 주세요...";
    try{

        const response = await apiClient.post("/api/users/find/pass", jsonData)

        if(response.status === "success"){
            error.innerHTML = "";
            error.innerHTML = response.data.message;
        }else{
            error.innerHTML = "";
            error.innerHTML = response.message;
        }
    }catch (e){
        console.error("요청 실패 " + e)
    }
})



// findPasswordForm.addEventListener("submit" , async (e) =>{
//     e.preventDefault();
//     const message = document.querySelector(".error")
//     const userId = document.querySelector("#userId").value.trim();
//     const email = document.querySelector('#email').value.trim();
//
//     const jsonData = {
//         userId : userId,
//         email : email
//     }
//     message.innerHTML = "";
//     message.textContent = "잠시만 기다려 주세요...";
//     try{
//         const response = await fetch("/api/users/find/pass" , {
//             method : "POST",
//             headers : {
//                 'Content-Type': 'application/json'
//             },
//             body : JSON.stringify(jsonData)
//         })
//
//         if(response.ok){
//             const data = await response.json();
//             message.innerHTML = "";
//             message.innerHTML = data.message;
//         }else{
//             const data = await response.json();
//             message.innerHTML = "";
//             message.innerHTML = data.message;
//         }
//     }catch (e){
//         console.error("요청 실패 " + e)
//     }
// })

