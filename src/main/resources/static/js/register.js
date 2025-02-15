const validationRules = {
  userId: {
    regex: /^[a-zA-Z][a-zA-Z0-9]{5,20}$/, // 아이디: 영문, 숫자 4~12자
    errorMessage: "아이디 : 영문, 숫자로 4~20자여야 합니다.",
    validateErrorMessage : "아이디 : 중복된 아이디가 존재합니다."
  },
  password: {
    regex: /^(?=.*[a-z])(?=.*\d)(?=.*[!@#$%^&*()_+])[A-Za-z\d!@#$%^&*()_+]{8,20}$/, // 비밀번호
    errorMessage: "비밀번호 : 8~16자의 영문 대/소문자, 숫자, 특수문자를 사용해 주세요.",
  },
  email : {
    regex : /^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\.[a-zA-Z]{2,}$/,
    errorMessage : "이메일 : 이메일 형식을 올바르게 입력해주세요.",
  },
  nickname: {
    regex: /^[가-힣]{2,5}$/, // 이름: 한글 2~5자
    errorMessage: "닉네임 : 닉네임은 특수문자를 제외한 2~10자로 입력해주세요.",
  },
  question : {
    required : true,
    errorMessage : "질문 : 보안 질문을 선택해주세요."
  },
  answer : {
    regex : /^[가-힣]{1,20}$/,
    errorMessage : "답변 : 답변을 1~20자로 입력해주세요."
  },
  address : {
    required: true,
    errorMessage : "주소 : 주소를 검색해주세요."
  }
};




const registerForm = document.querySelector("#registerForm")
const errorList1 = document.querySelector("#errorList1")
const errorList2 = document.querySelector("#errorList2")

registerForm.addEventListener("submit" , async (e) =>{
  e.preventDefault()

  const form = e.target;
  const formData = new FormData(form)
  console.log(formData)

  const response = await fetch("/register" , {
    method : "POST",
    headers : {
      "Content-Type" : "application/json"
    },
    body : JSON.stringify(Object.fromEntries(formData))
  })
  // 회원가입 실패 시
  if(!response.ok){
    errorList1.innerHTML = '';
    errorList2.innerHTML = '';
    const errorObject = await response.json()

    Object.keys(errorObject['errors']).forEach(e =>{
      const li =  document.createElement("li")
      li.textContent = errorObject['errors'][e];

      if(e === "userId" || e === "nickname" || e === "email" || e=== "password"){
        errorList1.appendChild(li)
      }else{
        errorList2.appendChild(li)
      }
    })
  }else {
    //회원가입 성공 시
    const result = await response.json()
    console.log(result)
    alert(`${result['userId']} 님 회원가입을 축하합니다 !`);

    const redirect = confirm("로그인 페이지로 이동하시겠습니까?");
    if (redirect) {

      window.location.href = "/login"; // 로그인 페이지 URL로 변경
    } else {
      // 홈페이지로 이동
      window.location.href = "/home"; // 홈페이지 URL로 변경
    }
  }
})
const errorState = {};
const inputs = document.querySelectorAll("input")

inputs.forEach(input =>{
  input.addEventListener('blur' , async (e) =>{
    e.preventDefault();
    console.log("11")
    const fieldName = e.target.name
    const fieldValue = e.target.value
    const fieldRule = validationRules[fieldName]

    if(fieldRule){
      // 필드 규칙이 존재하면 에러를 모아놓는 errorState를 일단 초기화.
      errorState[fieldName] = null;
      if(fieldRule.regex && !fieldRule.regex.test(fieldValue)){
        errorState[fieldName] = fieldRule.errorMessage
      }

    }

    if((fieldName === "userId" || fieldName === "email" || fieldName === "nickname") && !errorState[fieldName]){
      console.log("여기로오나")
      try{
        const response = await fetch("/register/check" , {
          method : "POST",
          headers : {
            "Content-Type" : "application/json",
          },
          body : JSON.stringify({fieldName : fieldValue})
        });

        const result = await response.json();
        console.log(result);


      }catch (err) {
        console.error(err)
      }
    }


  })
})




