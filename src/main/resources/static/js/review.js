// 모달 열기
function openModal(button) {
    var purchaseId = button.getAttribute("data-purchase-id");
    document.getElementById("purchaseId").value = purchaseId;
    document.getElementById("reviewModal").style.display = "block";
}

// 모달 닫기
function closeModal() {
    document.getElementById("reviewModal").style.display = "none";
}

// 별 클릭 시 평점 설정
function setRating(event) {
    var stars = document.querySelectorAll("#starRating .star");
    var rating = event.target.getAttribute("data-value");
    
    // 별을 선택 상태로 만들기
    stars.forEach(star => {
        if (star.getAttribute("data-value") <= rating) {
            star.classList.add("selected");
        } else {
            star.classList.remove("selected");
        }
    });

    // hidden input에 평점 값 저장
    document.getElementById("rating").value = rating;
}

// 모달 외부 클릭 시 닫기
window.onclick = function(event) {
    var modal = document.getElementById("reviewModal");
    if (event.target === modal) {
        closeModal();
    }
}
