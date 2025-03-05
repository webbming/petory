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


    // 이미지 미리보기 및 삭제
    function previewImages(event) {
        const input = event.target;
        const previewContainer = document.getElementById('previewContainer');

        // 미리보기 이미지 컨테이너 초기화
        previewContainer.innerHTML = ""; // 기존의 미리보기 삭제

        // 이미지 파일이 있는지 확인
        if (input.files) {
            // 여러 이미지에 대해 각각 미리보기 처리
            Array.from(input.files).forEach((file, index) => {
                const reader = new FileReader();

                reader.onload = function (e) {
                    const imgElement = document.createElement("div");
                    imgElement.classList.add("image-preview");

                    const img = document.createElement("img");
                    img.src = e.target.result;
                    img.style.maxWidth = "100px";  // 썸네일 크기 설정
                    img.style.marginBottom = "10px";
                    img.style.borderRadius = "5px";
                    img.style.marginRight = "10px";

                    const caption = document.createElement("div");
                    caption.innerHTML = `이미지 ${index + 1}`;

                    // 삭제 버튼 추가
                    const deleteBtn = document.createElement("button");
                    deleteBtn.textContent = "삭제";
                    deleteBtn.classList.add("btn", "btn-danger", "btn-sm");
                    deleteBtn.onclick = function () {
                        imgElement.remove();  // 이미지 삭제
                    };

                    imgElement.appendChild(img);
                    imgElement.appendChild(caption);
                    imgElement.appendChild(deleteBtn);
                    previewContainer.appendChild(imgElement);
                };

                reader.readAsDataURL(file); // 파일을 Base64 데이터로 변환
            });
        }
    }
    // 이미지 미리보기 및 삭제
    function previewImages(event) {
        const input = event.target;
        const previewContainer = document.getElementById('previewContainer');

        // 미리보기 이미지 컨테이너 초기화
        previewContainer.innerHTML = ""; // 기존의 미리보기 삭제

        // 이미지 파일이 있는지 확인
        if (input.files) {
            // 여러 이미지에 대해 각각 미리보기 처리
            Array.from(input.files).forEach((file, index) => {
                const reader = new FileReader();

                reader.onload = function (e) {
                    const imgElement = document.createElement("div");
                    imgElement.classList.add("image-preview");

                    const img = document.createElement("img");
                    img.src = e.target.result;
                    img.style.maxWidth = "100px";  // 썸네일 크기 설정
                    img.style.marginBottom = "10px";
                    img.style.borderRadius = "5px";
                    img.style.marginRight = "10px";

                    const caption = document.createElement("div");
                    caption.innerHTML = `이미지 ${index + 1}`;

                    // 삭제 버튼 추가
                    const deleteBtn = document.createElement("button");
                    deleteBtn.textContent = "삭제";
                    deleteBtn.classList.add("btn", "btn-danger", "btn-sm");
                    deleteBtn.onclick = function () {
                        imgElement.remove();  // 이미지 삭제
                    };

                    imgElement.appendChild(img);
                    imgElement.appendChild(caption);
                    imgElement.appendChild(deleteBtn);
                    previewContainer.appendChild(imgElement);
                };

                reader.readAsDataURL(file); // 파일을 Base64 데이터로 변환
            });
        }
    }
