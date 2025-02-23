window.addEventListener("DOMContentLoaded" , async (e) =>{
    try{
        const response = await fetch("/api/user/profile" , {credentials: "include"})
        if(!response.ok) throw new Error("사용자 정보를 불러오지 못했습니다.")

        const data = await response.json();
        const userId = document.querySelector(".profile-user")
        const ninckName = document.querySelector("#nickname")
        const email = document.querySelector("#email")
        const address = document.querySelector("#address")

        userId.innerText = data.username;
        ninckName.innerText = data.nickname;
        email.innerText = data.innerText;
        address.innerText = data.address;


    }catch(e){

    }

})




const profileUpdateForm = document.querySelector("#profileUpdateForm")
const submitButton = document.querySelector(".save-btn")

profileUpdateForm.addEventListener('input' , (e) =>{
    submitButton.disabled = false;
})

profileUpdateForm.addEventListener("submit" , async (e) =>{
    e.preventDefault();

    const redirect = confirm("변경사항을 적용 하시겠습니까?")
    if(redirect){
        const formData = new FormData(e.target);
        const formObj = Object.fromEntries(formData);

        const response = await fetch("/api/user/profile" , {
            method : "PATCH" ,
            headers : {
                "Content-Type" : "application/json"
            },
            body : JSON.stringify(formObj)
        })
        if(!response.ok){
            const nickNameError = document.querySelector("#nickname-error")
            const emailError = document.querySelector("#email-error")
            const result = await response.json();
            console.log(result.status.nickname)
                nickNameError.innerHTML = "";
                emailError.innerHTML = "" ;
                nickNameError.textContent = result.status.nickname
                emailError.textContent = result.status.email


        }else{

            const result = await response.json();
            console.log(result)
            alert("수정되었습니다");
            window.location.href = "/user/profile";

        }
    }

})
