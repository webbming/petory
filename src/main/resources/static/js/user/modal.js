export function modalUtil(){
  const modal = document.getElementById("nicknameModal");
  const btn = document.querySelector(".profile_btn"); // "설정" 버튼
  const closeBtn = document.querySelector(".close");
  const saveBtn = document.getElementById("saveNickname");
  const nicknameField = document.querySelector(".nickname");
  const error = document.querySelector(".error")


// 모달 열기
  btn.addEventListener("click", function () {
    modal.style.display = "block";
  });

// 모달 닫기
  closeBtn.addEventListener("click", function () {
    modal.style.display = "none";
  });

  const nicknameInput = document.querySelector("#newNickname")
  nicknameInput.addEventListener('change', (e) =>{
    saveBtn.disabled = false;
  })

// 저장 버튼 클릭 시 닉네임 변경
  saveBtn.addEventListener("click", async (e) => {
    e.preventDefault();
    const newNickname = document.getElementById("newNickname").value.trim();
    if (newNickname) {

      const response = await fetch("/api/users/me/profile/nickname" , {
        method:"POST",
        headers:{
          "Content-Type" : "application/json"
        },
        body : JSON.stringify({nickname : newNickname})

      })

      if(response.status === 400){
        error.style.display = "block"
        return;
      }
      error.style.display = "none"
      nicknameField.textContent = newNickname;
      modal.style.display = "none"; // 모달 닫기


    } else {
      alert("닉네임을 입력하세요!");
    }
  });

// 모달 바깥 클릭 시 닫기
  window.addEventListener("click", function (event) {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  });
}

