const validationRules = {
  userId: {
    regex: /^[a-zA-Z][a-zA-Z0-9]{3,20}$/, // 아이디: 영문, 숫자 4~12자
    errorMessage: "아이디 : 영문, 숫자로 4~20자여야 합니다.",
    status : false,
    validateErrorMessage : "아이디 : 중복된 아이디가 존재합니다."
  },
  password: {
    regex: /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,20}$/, // 비밀번호
    errorMessage: "비밀번호 : 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.",
  },
  email : {
    regex : /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    errorMessage : "이메일 : 이메일 형식을 올바르게 입력해주세요.",
    status : false,
    validateErrorMessage : "이메일 : 중복된 이메일이 존재합니다."
  },
  nickname: {
    regex: /^[가-힣]{2,5}$/, // 이름: 한글 2~5자
    errorMessage: "닉네임 : 닉네임은 특수문자를 제외한 2~10자로 입력해주세요.",
    status : false,
    validateErrorMessage : "닉네임 : 중복된 닉네임이 존재합니다."
  },
  question : {
    required : true,
    errorMessage : "질문 : 보안 질문을 선택해주세요."
  },
  answer : {
    regex : /^[가-힣]{2,20}$/,
    errorMessage : "답변 : 답변을 1~20자로 입력해주세요."
  },
  address : {
    required: true,
    errorMessage : "주소 : 주소를 검색해주세요."
  }
};

const errorState = {};
const inputs = document.querySelectorAll("input, select")

function updateErrorMessages(){
  const errorList1 = document.querySelector("#errorList1")
  const errorList2 = document.querySelector("#errorList2")

  errorList1.innerHTML = "";
  errorList2.innerHTML = "";

  Object.keys(errorState).forEach(fieldName =>{

    const inputField = document.querySelector(`[name="${fieldName}"]`)
    if (!errorState[fieldName]){
      inputField.classList.remove("input-error")
      return;
    }

    const li = document.createElement("li");
    li.textContent = errorState[fieldName];

    // 닉네임, 아이디, 비밀번호, 이메일 → errorList1에 추가
    if (["userId", "nickname", "password", "email"].includes(fieldName)) {
      errorList1.appendChild(li);
    } else {
      // 나머지 필드들 → errorList2에 추가
      errorList2.appendChild(li);
    }
    if (inputField) inputField.classList.add("input-error")
  })
}

inputs.forEach(input =>{
  input.addEventListener("blur" , async (e) =>{
    e.preventDefault();
    const fieldName = e.target.name;
    const fieldValue = e.target.value.trim()
    const fieldRule = validationRules[fieldName]

    if(fieldRule){
      errorState[fieldName] = null;

      if(fieldRule.required && !fieldValue){
        errorState[fieldName] = fieldRule.errorMessage;
      }

      if(fieldRule.regex && !fieldRule.regex.test(fieldValue)){
        errorState[fieldName] = fieldRule.errorMessage;
      }
    }
    
    if((fieldName === "userId" || fieldName === "email" || fieldName === "nickname") && !errorState[fieldName]){

      try{
          const data = {
            fieldName : fieldName,
            fieldValue : fieldValue
          }
          const response = await fetch("/register/check" , {
            method : "POST",
            headers : {
              "Content-Type" : "application/json",
            },
            body : JSON.stringify(data)
          });

          const result = await response.json();
          console.log(result)
          if(result.result){
            errorState[fieldName] = fieldRule.validateErrorMessage;
          }else{
            validationState[fieldName] = true;
          }

        }catch (error){
          console.error(error);
        }
    }
    updateErrorMessages();
  })
})

const validationState = {
  userId : false,
  email: false,
  nickname : false
}


document.querySelector("#registerForm").addEventListener("submit" , async (e) =>{

  e.preventDefault();

 console.log(errorState)
  const inputsArray = Array.from(inputs);
  await Promise.all(inputsArray.map(input => {
    return new Promise(resolve => {
      input.addEventListener('blur', resolve, { once: true });
      input.dispatchEvent(new Event('blur'));
    });
  }));

  // 에러가 하나라도 존재한다면 연속적인 폼 제출 차단 ( 백엔드 서버 부하 )
  if (Object.values(errorState).some(error => error)) {
    return;
  }

  // userId , email , nickname 의 중복검사를 전부 완료한 상태인지 다시 확인
  const isValidationComplete = Object.values(validationState).every(status => status === true);
  // 완료하지 않았다면 폼 제출 차단
  if(!isValidationComplete){
    return;
  }


  const formData = new FormData(e.target);
  const formObj = Object.fromEntries(formData);

  try{

    const response = await fetch("/register" , {
      method : "POST",
      headers : {"Content-Type" : "application/json"},
      body : JSON.stringify(formObj)
    });

    if(!response.ok){
      const result = await response.json();
      console.log(result)
    }

    const result = await response.json();
    alert(`${result.userId} 님 회원가입을 환영합니다!`)
    const redirect = confirm("로그인 페이지로 이동하시겠습니까 ?")
    if(redirect){
      window.location.href = "/login";
    }
    else{
      window.location.href = "/home";
    }

  }catch(error){
    console.error(error)
  }
})



/*   */





