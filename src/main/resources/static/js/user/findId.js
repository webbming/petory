/* findId.js 아이디 찾기  */


import {apiClient} from "../common/api.js";

const findIdForm = document.querySelector("#findIdForm");

findIdForm.addEventListener("submit" , async (e) =>{

    e.preventDefault();
    const question = document.querySelector("#question").value;
    const answer = document.querySelector("#answer").value.trim();
    const userDisplay = document.querySelector("#userIdDisplay")
    const noIdMessage = document.querySelector("#noIdMessage")
    const resultContainer = document.querySelector(".result-container")
    const data = {
        question : question,
        answer : answer
    }

    try{
        const result = await apiClient.post("/api/users/find/id", data)

        userDisplay.textContent = "";
        noIdMessage.style.display = "none";
        resultContainer.style.display = "none";

        if(result.status === "success"){
            userDisplay.textContent = `조회된 아이디 : ${result.data}`;
            resultContainer.style.display = "block"; // 결과 컨테이너 보이기
        }else{
            noIdMessage.textContent = "입력한 정보로 조회된 아이디가 없습니다.";
            noIdMessage.style.display = "block"; // 메시지 보이기
            resultContainer.style.display = "block"; // 결과 컨테이너 보이기
        }
    }catch (error){
        console.error("아이디 찾기 오류:", error);
        noIdMessage.textContent = "요청 처리 중 오류가 발생했습니다.";
        noIdMessage.style.display = "block";
        resultContainer.style.display = "block";
    }
})