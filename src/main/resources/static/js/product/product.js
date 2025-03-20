document.addEventListener("DOMContentLoaded", function(){
  /* 스와이퍼 초기화 */
  var galleryThumbs = new Swiper('.gallery-thumbs', {
    spaceBetween: 10,
    slidesPerView: 4,
    slidesPerGroup: 4,
    watchSlidesProgress: true,
    watchOverflow: true,
    navigation: {
      nextEl: '.thumbs-next',
      prevEl: '.thumbs-prev'
    }
  });
  var galleryTop = new Swiper('.gallery-top', {
    spaceBetween: 10,
    navigation: {
      nextEl: '.swiper-button-next',
      prevEl: '.swiper-button-prev'
    },
    thumbs: {
      swiper: galleryThumbs
    },
    autoplay: {
      delay: 2000,
      disableOnInteraction: false
    }
  });

  /* 내일 날짜 자동 계산 */
  const shippingBlock = document.getElementById('shipping-block');
  if(shippingBlock){
    const today = new Date();
    const tomorrow = new Date(today);
    tomorrow.setDate(today.getDate() + 1);
    const mm = String(tomorrow.getMonth() + 1).padStart(2, '0');
    const dd = String(tomorrow.getDate()).padStart(2, '0');
    shippingBlock.innerHTML = `
        <img src="/images/truck.png" alt="트럭 아이콘"/>
        지금 주문 시 내일(${mm}.${dd}) 바로 발송
      `;
  }

  updateTotalPrice(); // 페이지 로드시 합계 계산
});

/* 상세정보 펼쳐보기/접기 */
function toggleDescription(){
  let section = document.getElementById("hidden-description");
  let btn = document.getElementById("toggle-button");
  if(section.classList.contains("show")){
    section.classList.remove("show");
    setTimeout(() => section.style.display = "none", 300);
    btn.innerHTML = '상세정보 펼쳐보기 <i>▼</i>';
  } else {
    section.style.display = "block";
    setTimeout(() => section.classList.add("show"), 10);
    btn.innerHTML = '상세정보 접기 <i>▲</i>';
  }
}

/* 탭 전환 */
function openTab(tabId){
  document.querySelectorAll('.tab-pane-new').forEach(t => t.classList.remove('active'));
  document.querySelectorAll('.tab-btn-new').forEach(b => b.classList.remove('active'));
  document.getElementById(tabId).classList.add('active');
  document.querySelector(`.tab-btn-new[onclick*="${tabId}"]`).classList.add('active');
}

/* 수량 증가/감소 */
function changeQuantityByButton(diff){
  const qtyInput = document.getElementById("quantity");
  let currentVal = parseInt(qtyInput.value) || 1;
  currentVal += diff;
  if(currentVal < 1) currentVal = 1;
  qtyInput.value = currentVal;
  updateTotalPrice();
}

/* 합계 계산 (실시간 업데이트) */
function updateTotalPrice(){
  const productPriceEl = document.getElementById('product-price');
  const quantityInput  = document.getElementById('quantity');
  const totalPriceEl   = document.getElementById('total-price');
  const livePriceRight = document.getElementById('live-price-right');

  if(!productPriceEl || !quantityInput || !totalPriceEl) return;

  const basePrice = parseInt(productPriceEl.innerText.replace(/[^\d]/g, '')) || 0;
  const mainQty   = parseInt(quantityInput.value) || 1;

  const total = basePrice * mainQty;
  const formatted = total.toLocaleString() + '원';

  totalPriceEl.innerText = formatted;
  if(livePriceRight) {
    livePriceRight.innerText = formatted;
  }

  const hiddenQty = document.getElementById('quantity-value');
  if(hiddenQty) hiddenQty.value = mainQty;
}

/* 찜하기 */
document.addEventListener("DOMContentLoaded", function(){
  const wishlistButton = document.getElementById('wishlistButton');
  if(wishlistButton){
    wishlistButton.addEventListener('click', async function(){
      const productId = document.getElementById('productId').value;
      if(!productId){
        alert('상품 정보를 찾을 수 없습니다.');
        return;
      }
      const response = await fetch(`/wishlist/add`, {
        method:'POST',
        headers : {
          "Content-Type" : "application/json"
        },
        body : JSON.stringify({productId: Number(productId)})
      });
      const data = await response.json();
      if(response.status === 400){
        console.log("찜 목록 추가 실패", data);
      }
      if(response.status === 200){
        console.log("찜 하기 성공!", data);
        alert("찜 목록에 추가되었습니다.");
      }
    });
  }
});

/* 장바구니 */
function addToCart() {
  let productId = "[[${product.productId}]]";
  let quantity = document.getElementById("quantity").value;
  fetch(`/cart/items/${productId}/add?quantity=${quantity}`, { method:"POST" })
  .then(response => {
    if(!response.ok){
      throw new Error("네트워크 응답이 올바르지 않습니다.");
    }
    return response.json();
  })
  .then(data => {
    alert("장바구니에 상품이 추가되었습니다!");
    console.log("업데이트된 장바구니:", data);
  })
  .catch(error => {
    console.error("Error:", error);
    alert("오류가 발생했습니다.");
  });
}

/* 이미지 팝업 열기/닫기 */
function openImagePopup(imageSrc){
  const popup = document.getElementById("imagePopup");
  document.getElementById("popupImage").src = imageSrc;
  popup.style.display = "flex";
}
function closeImagePopup(event){
  if(event.target.id === "imagePopup" || event.target.classList.contains("close-btn")){
    document.getElementById("imagePopup").style.display = "none";
  }
}

/* 리뷰 작성 모달 열기/닫기 */
function openReviewModal(){
  document.getElementById('reviewModal').style.display = 'flex';
}
function closeReviewModal(){
  document.getElementById('reviewModal').style.display = 'none';
}

/* 리뷰 수정 모달 열기/닫기 */
function openEditModal(reviewId){
  const reviewElem = document.querySelector(`.review[data-review-id="${reviewId}"]`);
  if(!reviewElem) return;
  const currentRating = reviewElem.getAttribute('data-rating');
  const currentComment = reviewElem.getAttribute('data-comment');
  document.getElementById('editRating').value = currentRating;
  document.getElementById('editComment').value = currentComment;
  const productId = document.getElementById('productId').value;
  document.getElementById('editReviewForm').action = `/products/${productId}/reviews/edit/${reviewId}`;
  document.getElementById('editReviewModal').style.display = 'flex';
}
function closeEditModal(){
  document.getElementById('editReviewModal').style.display = 'none';
}

/* 댓글 모달 열기/닫기 */
function openCommentModal(reviewId){
  const modal = document.getElementById('commentModal');
  modal.style.display = 'flex';
  const productId = document.getElementById('productId').value;
  document.getElementById('commentForm').action = `/products/${productId}/reviews/${reviewId}/comments`;
}
function closeCommentModal(){
  document.getElementById('commentModal').style.display = 'none';
}

/* 모달 바깥 클릭 시 닫기 */
window.onclick = function(e){
  const reviewModal = document.getElementById('reviewModal');
  const editModal = document.getElementById('editReviewModal');
  const commentModal = document.getElementById('commentModal');
  if(e.target === reviewModal){ reviewModal.style.display = 'none'; }
  if(e.target === editModal){ editModal.style.display = 'none'; }
  if(e.target === commentModal){ commentModal.style.display = 'none'; }
}

/* 댓글 등록 AJAX */
function submitCommentAjax(e){
  e.preventDefault();
  const form = e.target;
  const formData = new FormData(form);
  const url = form.action;
  fetch(url, { method:'POST', body:formData })
  .then(resp => resp.json())
  .then(data => {
    console.log("댓글 등록 성공:", data);
    const reviewId = url.match(/reviews\/(\d+)\/comments/)[1];
    const productId = document.getElementById('productId').value;
    fetch(`/${productId}/reviews/${reviewId}/comments`)
    .then(res => res.json())
    .then(updatedComments => {
      const reviewElem = document.querySelector(`.review[data-review-id="${reviewId}"]`);
      if(reviewElem){
        reviewElem.setAttribute('data-comments', JSON.stringify(updatedComments));
        let commentsContainer = reviewElem.querySelector('.review-comments');
        if(!commentsContainer){
          commentsContainer = document.createElement('div');
          commentsContainer.classList.add('review-comments');
          commentsContainer.innerHTML = '<h4>댓글</h4><ul></ul>';
          reviewElem.appendChild(commentsContainer);
        }
        const ul = commentsContainer.querySelector('ul');
        ul.innerHTML = '';
        if(updatedComments.length > 0){
          updatedComments.forEach(c => {
            const li = document.createElement('li');
            li.innerHTML = `
                	  <strong>${c.nickname}</strong> : <span>${c.content}</span>
                	  <small style="margin-left:5px;font-size:12px;color:#888;">
                	    ${new Date(c.createdAt).toLocaleString()}
                	  </small>
                	`;
            ul.appendChild(li);
          });
        } else {
          commentsContainer.remove();
        }
      }
    });
    form.reset();
    closeCommentModal();
  })
  .catch(err => {
    console.error("댓글 등록 실패:", err);
    alert("댓글 등록 중 오류 발생");
  });
  return false;
}