let mypageTopInfo = null;
async function loadTopInfo(){
  console.log("히히")
  try{
    const response = await fetch("/api/users/me/profile/MyPageTopInfo" ,{
      method : "GET",
      credentials:"include"
    });

    if(response.status === 200){
      mypageTopInfo = await response.json()
      updateMyPageTopInfo(mypageTopInfo)
    }

  }catch (e){
    console.error(e)
  }
}
function updateMyPageTopInfo(data) {
  document.querySelector(".nickname").textContent = data.data.nickname
  document.querySelector(".coupon").textContent = data.data.couponCount
  document.querySelector(".cartQuantity").textContent = data.data.cartQuantity

}
window.onload = loadTopInfo













document.addEventListener("DOMContentLoaded", async (e) => {
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

  // 펫 리스트 렌더링 함수 분리
  async function renderPetList() {
    try {
      const response = await fetch("/api/pets/list", {
        method: "GET",
        credentials: "include"
      });

      if (response.ok) {
        const result = await response.json();
        const profileAdd = document.querySelector(".profile_add");

        // 기존 펫 목록 초기화
        const existingPets = document.querySelectorAll(".box");
        existingPets.forEach(pet => pet.remove());

        if (result.data.length !== 0) {
          const petElements = result.data.map((pet) => `
            <div class="box" data-item-id="${pet.id}">
              <div class="name">${pet.name}</div>
              <div class="img" style="cursor: pointer">
                <img src="https://img.lifet.co.kr/profile/default.png?w=420&h=420">
              </div>
              <button type="button" class="layer_open"></button>
              <ul class="layer_modify" style="display: none">
                <li><a class="trigger updateBtn">수정</a></li>
                <li><a class="deleteBtn">삭제</a></li>
              </ul>
            </div>
          `).join('');

          // 펫 목록 다시 추가
          profileAdd.parentElement.insertAdjacentHTML('beforebegin', petElements);

          // 새로 추가된 요소들에 이벤트 리스너 다시 바인딩
          setupPetListeners();
        }
      }
    } catch (e) {
      console.error(e);
    }
  }

  // 레이어, 삭제 버튼 등에 대한 이벤트 리스너 설정 함수
  function setupPetListeners() {
    const layerBtns = document.querySelectorAll(".layer_open");
    const deleteBtns = document.querySelectorAll(".deleteBtn");

    // 레이어 버튼 이벤트
    layerBtns.forEach(btn => {
      btn.addEventListener("click", function (e) {
        e.stopPropagation();
        const box = this.closest(".box");
        const layerModify = box.querySelector(".layer_modify");

        document.querySelectorAll(".layer_modify").forEach(layer => {
          if (layer !== layerModify) {
            layer.style.display = "none";
          }
        });

        layerModify.style.display =
            layerModify.style.display === "block" ? "none" : "block";
      });
    });

    // 삭제 버튼 이벤트
    deleteBtns.forEach((btn) => {
      btn.addEventListener("click", async (e) => {
        e.preventDefault();

        const con = confirm("정말 삭제하시겠습니까?");

        if (con) {
          const box = e.currentTarget.closest(".box");
          const petId = box.dataset.itemId;

          const response = await fetch("/api/pets", {
            method: "DELETE",
            credentials: "include",
            headers: {
              "Content-Type": "application/json"
            },
            body: JSON.stringify({ id: petId })
          });

          if (response.ok) {
            // 삭제 성공 시 펫 리스트 다시 불러오기
            await renderPetList();
          } else {
            alert("펫 삭제에 실패했습니다.");
          }
        }
      });
    });
  }

  // 초기 렌더링
  await renderPetList();


});

// 좋아요 한 상품 관련
document.addEventListener("DOMContentLoaded", async function(e){


      const response = await fetch("/wishlist" , {
        method : "GET",
        credentials : "include"
      })


      if(!response.ok){
        console.log("좋아요 목록을 받아올수 없어요 ")
      }
      const result = await response.json();
      console.log(result)
      const productList = document.querySelector(".product_list")

      if(result.data.length !== 0){
        const productElement = result.data.map((product) =>
        `<li>
            <a href="/products/${product.productId}" data-productId="${product.productId}">
              <div class="img">
                <img src="${product.imageUrl}">
              </div>
              <div class="pro_name">
                <span></span>
                <p>${product.productName}</p>
                <span class="rate">${product.averageRating}</span> 
                <span class="review_count">리뷰 ${product.reviewCount}</span>
                <div class="discount">
                  <strong>${product.price}원</strong>
                </div> 
              </div>
            </a>
            <button class="heart active"></button>
        </li>`).join('');

        productList.innerHTML = productElement
      }

})