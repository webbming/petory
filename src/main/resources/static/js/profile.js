document.addEventListener("DOMContentLoaded", function () {
  const modal = document.getElementById("nicknameModal");
  const btn = document.querySelector(".profile_btn"); // "설정" 버튼
  const closeBtn = document.querySelector(".close");
  const saveBtn = document.getElementById("saveNickname");
  const nicknameField = document.querySelector(".nickname");

  // 모달 열기
  btn.addEventListener("click", function () {
    modal.style.display = "block";
  });

  // 모달 닫기
  closeBtn.addEventListener("click", function () {
    modal.style.display = "none";
  });

  // 저장 버튼 클릭 시 닉네임 변경
  saveBtn.addEventListener("click", function () {
    const newNickname = document.getElementById("newNickname").value.trim();
    if (newNickname) {
      nicknameField.textContent = newNickname + "님";
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
});