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


  document.getElementById('profilePhotoFile').addEventListener('change', function(event) {
    const file = event.target.files[0]; // 선택한 파일
    const imgElement = document.getElementById('profilePhotoImg'); // 미리보기 이미지 요소
    const defaultImgElement = document.getElementById('profileOriginImg');
    if (file) {
      const reader = new FileReader();
      reader.onload = function(e) {
        imgElement.src = e.target.result; // 미리보기 이미지로 설정
        defaultImgElement.style.display = 'none';
        imgElement.style.display='block';
      }
      reader.readAsDataURL(file); // 파일을 데이터 URL로 읽어옴
    }
  });

// 저장 버튼 클릭 시 닉네임 변경
  saveBtn.addEventListener("click", async (e) => {
    e.preventDefault();
    const formData = new FormData();
    const fileInput = document.getElementById('profilePhotoFile')
    const newNickname = document.getElementById("newNickname").value.trim();

    if(fileInput.files.length > 0){
      formData.append('profilePhotoFile',fileInput.files[0])
    }
    formData.append('nickname' , newNickname);

    if(!newNickname) {
      alert("닉네임을 입력하세요.")
      return;
    }
    try{
      const response = await fetch("/api/users/me/profile" , {
        method:"POST",
        body : formData

      })

      if(response.status === 400){
        error.style.display = "block"
        return;
      }
      error.style.display = "none"
      nicknameField.textContent = newNickname;
      modal.style.display = "none"; // 모달 닫기

    }catch(e){
      console.error("Error" , error)
    }

  });

// 모달 바깥 클릭 시 닫기
  window.addEventListener("click", function (event) {
    if (event.target === modal) {
      modal.style.display = "none";
    }
  });
}

