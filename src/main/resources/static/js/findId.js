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

    const response = await fetch("/api/users/find/id" , {
        method : "POST" ,
        headers : {
            "Content-Type" : "application/json"
        },
        body : JSON.stringify(data)
    })
    const result = await response.json();

    userDisplay.textContent = "";
    noIdMessage.style.display = "none";
    resultContainer.style.display = "none";

    if (response.ok){
        // 사용자 아이디를 페이지에 표시
        userDisplay.textContent = `당신의 아이디는: ${result.userId}`;
        resultContainer.style.display = "block"; // 결과 컨테이너 보이기

    }else{
        noIdMessage.textContent = "입력한 정보로 조회된 아이디가 없습니다.";
        noIdMessage.style.display = "block"; // 메시지 보이기
        resultContainer.style.display = "block"; // 결과 컨테이너 보이기
    }
})