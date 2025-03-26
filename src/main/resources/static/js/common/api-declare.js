/* 마이페이지에 좋아요한 상품을 불러오는 함수  */


export async function headerCartSize(){
    try {
      const response = await fetch("/cart/cartCount" , {
        method : "GET"
      })
      if(!response.ok){
        console.log("요청 불가")
      }
      const data = await response.json()

      const cartCount = document.querySelector(".cart-badge");

      cartCount.textContent =data.body.data.cartCount

    }catch (e){
      console.log(e)
    }
}

export async  function loadWishlist(){
  try{

    const response = await fetch("/wishlist" , {
      method : "GET",
      credentials : "include"
    })


    if(!response.ok){
      console.log("찜한 제품 목록을 받아올수 없어요 ")
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
  }catch (e){
    console.error(e)
  }
}

function formatBirthDate(birthString) {
  if (!birthString || birthString.length !== 6) {
    return "정보 없음";
  }

  const year = birthString.substring(0, 4);
  const month = birthString.substring(4, 6);

  return `${year}년 ${parseInt(month)}월`;
}

// 펫 리스트 렌더링 함수 분리
export async function loadPetList() {
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
            <div class="box" data-item-id="${pet.id}" 
                data-pet-name="${pet.name}" 
                data-pet-gender="${pet.gender || ''}" 
                data-pet-age="${pet.age || ''}" 
                data-pet-birth="${pet.birth || ''}" 
                data-pet-species="${pet.species || ''}"
                data-pet-type="${pet.type || ''}">
              <div class="name">${pet.name}</div>
              <div class="img" style="cursor: pointer">
                <img src="https://img.lifet.co.kr/profile/default.png?w=420&h=420" alt="${pet.name}">
              </div>
              <button type="button" class="layer_open"></button>
              <ul class="layer_modify" style="display: none">
                <li><a class="deleteBtn">삭제</a></li>
              </ul>
            </div>
          `).join('');

        // 펫 목록 다시 추가
        profileAdd.parentElement.insertAdjacentHTML('beforebegin', petElements);

        // 새로 추가된 요소들에 이벤트 리스너 다시 바인딩
        setupPetListeners();

        // 펫 이미지 클릭 시 모달 오픈 기능 추가
        setupPetModalListeners();
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
      const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
      const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
      e.preventDefault();

      const con = confirm("정말 삭제하시겠습니까?");

      if (con) {
        const box = e.currentTarget.closest(".box");
        const petId = box.dataset.itemId;

        const response = await fetch("/api/pets", {
          method: "DELETE",
          credentials: "include",
          headers: {
            "Content-Type": "application/json",
            [csrfHeader] : csrfToken
          },
          body: JSON.stringify({ id: petId })
        });

        if (response.ok) {
          // 삭제 성공 시 펫 리스트 다시 불러오기
          await loadPetList();
        } else {
          alert("펫 삭제에 실패했습니다.");
        }
      }
    });
  });
}

/// 펫 이미지 클릭 시 모달 열기 이벤트 설정
function setupPetModalListeners() {
  const petImages = document.querySelectorAll(".box .img");

  petImages.forEach(imgDiv => {
    imgDiv.addEventListener("click", function(e) {
      const box = this.closest(".box");
      const petId = box.dataset.itemId;
      const petName = box.dataset.petName;
      const petGender = box.dataset.petGender;
      const petAge = box.dataset.petAge;
      const petBirth = box.dataset.petBirth;
      const petSpecies = box.dataset.petSpecies;
      const petType = box.dataset.petType;

      // 모달 열기
      const petModal = document.getElementById("petModal");
      if (petModal) {
        petModal.style.display = "block";

        // 모달에 펫 정보 표시
        const nameElement = petModal.querySelector("#petName");
        const detailsElement = petModal.querySelector("#petDetails");

        if (nameElement) {
          nameElement.textContent = petName;
        }

        if (detailsElement) {
          // 모든 정보를 표시
          let detailsHtml = "";

          // 종류 (강아지/고양이)
          if (petType) {
            const petTypeName = petType === 'dog' ? '강아지' : petType === 'cat' ? '고양이' : petType;
            detailsHtml += `<div><strong>종류 :</strong> ${petTypeName}</div>`;
          }

          // 품종
          if (petSpecies) {
            detailsHtml += `<div><strong>품종 :</strong> ${petSpecies}</div>`;
          }

          // 성별
          if (petGender) {
            const genderText = petGender === 'male' ? '남아' : petGender === 'female' ? '여아' : petGender;
            detailsHtml += `<div><strong>성별 :</strong> ${genderText}</div>`;
          }

          // 나이
          if (petAge) {
            detailsHtml += `<div><strong>나이 :</strong> ${petAge}살</div>`;
          }

          // 생년월일
          if (petBirth) {
            const formattedBirth = formatBirthDate(petBirth);
            detailsHtml += `<div><strong>출생 :</strong> ${formattedBirth}</div>`;
          }

          // HTML 내용으로 설정
          detailsElement.innerHTML = detailsHtml || "상세 정보 없음";
        }
      }
    });
  });
}


/* 마이페이지에 상단 정보를 불러오는 함수 */
export let mypageTopInfo = null;

function updateMyPageTopInfo(data) {
  document.querySelector(".nickname").textContent = data.data.nickname
  document.querySelector(".right_info > .user-pet > span").textContent = data.data.nickname
  document.querySelector(".delivery").textContent = data.data.onDeliveryStatusCount
  document.querySelector(".coupon").textContent = data.data.couponCount
  document.querySelector(".coupon_count").textContent = data.data.couponCount + '개';
  document.querySelector(".cartQuantity").textContent = data.data.cartQuantity
  document.querySelector(".mypage-top .img img").src = data.data.url;
  document.querySelector(".profile-default .default-img img").src = data.data.url;

}


export function bindHeart(){
  const heartBtns = document.querySelectorAll(".heart")

  heartBtns.forEach(btn => {
    btn.addEventListener("click", async (e) => { // ✅ click 이벤트 사용!

      const button = e.target;
      const isLiked = button.classList.contains("active");
      const listItem = button.closest("li"); // 가장 가까운 <li> 찾기
      const productLink = listItem.querySelector("a"); // <a> 태그 찾기
      const productId = productLink?.dataset.productid;
      console.log(productId)


      try{
        if(isLiked) {
          await removeFromWishlist(productId);
        }else{
          await addToWishlist(productId);
        }

        button.classList.toggle("active")
      }catch (error){
        console.log("찜 상태 변경 중 에러")
      }
    });
  });
}


async function addToWishlist(productId) {
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
  const response = await fetch("/wishlist/add", {
    method: "POST",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      [csrfHeader] : csrfToken

    },
    body: JSON.stringify({productId : Number(productId)})
  });
  if (!response.ok) throw new Error("찜 추가 요청 실패");
}

async function removeFromWishlist(productId) {
  const csrfToken = document.querySelector('meta[name="_csrf"]').getAttribute('content');
  const csrfHeader = document.querySelector('meta[name="_csrf_header"]').getAttribute('content');
  const response = await fetch("/wishlist/remove", {
    method: "DELETE",
    credentials: "include",
    headers: {
      "Content-Type": "application/json",
      [csrfHeader] : csrfToken
    },
    body: JSON.stringify({ productId : Number(productId) })
  });

  if (!response.ok) throw new Error("찜 삭제 요청 실패");
}

export async function loadTopInfo(){
  if(mypageTopInfo){
    updateMyPageTopInfo(mypageTopInfo)
    return;
  }
  try{
    const response = await fetch("/api/users/me/profile/MyPageTopInfo" ,{
      method : "GET",
      credentials:"include"
    });

    if(response.status === 200){
      mypageTopInfo = await response.json()
      console.log(mypageTopInfo)
      updateMyPageTopInfo(mypageTopInfo)
    }
  }catch (e){
    console.error(e)
  }
}