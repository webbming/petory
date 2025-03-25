
// 모달 유틸리티 함수 수정
export function modalUtil() {
  const modalBtn = document.querySelector("#saveNickname")

  modalBtn.addEventListener("click",  async e=>{
    e.preventDefault()
    const response = await fetch("/api/users/me/profile" , {
      method : "POST",
      headers : {

      }
    })
  })
  // 공용 모달을 관리할 객체
  const modals = {
    nickname: document.getElementById("nicknameModal"),
    pet: document.getElementById("petModal"),
  };

  // 각 모달을 열기 위한 버튼들 (존재하는 경우에만 추가)
  const buttons = {
    nickname: document.querySelector(".profile_btn"), // 프로필 설정 버튼
  };

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