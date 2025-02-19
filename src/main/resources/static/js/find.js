
const findPasswordForm = document.querySelector("#findPasswordForm")

findPasswordForm.addEventListener("submit" , async (e) =>{
    const message = document.querySelector(".error")
    e.preventDefault();
    console.log("폼클릭릭")
    const userId = document.querySelector("#userId").value.trim();
    const email = document.querySelector('#email').value.trim();

    const jsonData = {
        userId : userId,
        email : email
    }

    try{
        const response = await fetch("/find/password" , {
            method : "POST",
            headers : {
                'Content-Type': 'application/json'
            },
            body : JSON.stringify(jsonData)
        })

        if(response.ok){
            const data = await response.json();
            message.innerHTML = "";
            message.textContent = data.message;
        }else{
            const data = await response.json();
            message.innerHTML = "";
            message.textContent = data.message;
        }
    }catch (e){
        console.error("요청 실패 " + e)
    }




})