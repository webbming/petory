
// 모달 유틸리티 함수 수정
export function modalUtil() {
  const modal = document.getElementById("nicknameModal");
  const saveBtn = document.querySelector("#saveNickname");
  const nicknameField = document.querySelector(".nickname");

  console.log(saveBtn)
  // 공용 모달을 관리할 객체
  const modals = {
    nickname: document.getElementById("nicknameModal"),
    pet: document.getElementById("petModal"),
  };

  // 각 모달을 열기 위한 버튼들 (존재하는 경우에만 추가)
  const buttons = {
    nickname: document.querySelector(".profile_btn"), // 프로필 설정 버튼
  };

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
    const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
    const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
    e.preventDefault();
    console.log("Gg")
    const formData = new FormData();
    const fileInput = document.getElementById('profilePhotoFile')
    const newNickname = document.getElementById("newNickname").value.trim();
    const img = document.querySelector(".mypage-top .user .img img")
    const error = document.querySelector(".error")


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
        headers : {
          [csrfHeader] : csrfToken
        },
        body : formData

      })

      const data = await response.json();

      if(response.status === 400){
        error.style.display = "block"
        return;
      }

      if(response.status === 200){

        error.style.display = "none"
        nicknameField.textContent = newNickname;
        modal.style.display = "none"; // 모달 닫기
        img.src = data.data.url;
      }

    }catch(e){
      console.error("Error" , e)
    }

  });

  // 모달 닫기 버튼
  const closeButtons = document.querySelectorAll(".modal .close");

  // 버튼 클릭 시 해당 모달 열기
  if (buttons.nickname) {
    buttons.nickname.addEventListener("click", () => {
      openModal("nickname");
    });
  }

  // 모달 열기 함수 (외부에서도 호출 가능하도록)
  window.openModal = function(type, petId = null) {
    if (modals[type]) {
      modals[type].style.display = "block";

      // 펫 모달이면 데이터 불러오기 (API 호출 방식)
      if (type === "pet" && petId) {
        fetch(`/api/pets/${petId}`)
            .then((res) => res.json())
            .then((data) => {
              document.getElementById("petName").textContent = data.name;
              document.getElementById("petDetails").textContent = `종: ${data.species || '정보 없음'}, 나이: ${data.age || '정보 없음'}`;
            })
            .catch((err) => console.error("Error fetching pet data", err));
      }
    }
  };

  // 모달 닫기 (X 버튼 클릭 시)
  closeButtons.forEach((btn) => {
    btn.addEventListener("click", () => {
      btn.closest(".modal").style.display = "none";
    });
  });

  // 모달 바깥 영역 클릭 시 닫기
  window.addEventListener("click", (event) => {
    Object.values(modals).forEach((modal) => {
      if (event.target === modal) {
        modal.style.display = "none";
      }
    });
  });
}