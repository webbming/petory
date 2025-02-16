import { validationRules } from './validationRules.js';


const profileUpdateForm = document.querySelector("#profileUpdateForm")
const submitButton = document.querySelector(".save-btn")

profileUpdateForm.addEventListener('input' , (e) =>{
    submitButton.disabled = false;
})

profileUpdateForm.addEventListener("submit" , async (e) =>{

    e.preventDefault();
    const formData = new FormData(e.target);
    const formObj = Object.fromEntries(formData);

    const response = await fetch("/user/profile/update" , {
        method : "PATCH" ,
        headers : {
            "Content-Type" : "application/json"
        },
        body : JSON.stringify(formObj)
    })

    if(!response.ok){
        const result = await response.json();
        console.log(result)
    }else{
        const result = await response.json();
        console.log(result)
    }
})
