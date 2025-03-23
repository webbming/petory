import {apiClient} from "./common/api.js";
import {createPostElement} from "./common/Util.js";

document.addEventListener("DOMContentLoaded", async (e) =>{
    const tabs = document.querySelectorAll(".consult__tab .tab li > a");
	const storeContainer = document.querySelector('.store-container');
	const communityContainer = document.querySelector('.community-container');
    let currentType = "all";

	 tabs.forEach(tab => {
	        tab.addEventListener("click", function(event) {
	            event.preventDefault();

	            // 활성화된 탭을 업데이트
	            tabs.forEach(t => t.classList.remove("active"));
	            this.classList.add("active");

	            const tabType = this.dataset.type;
	            currentType = tabType;

	            // 탭에 맞는 콘텐츠만 보이도록 업데이트
	            updateContentVisibility(currentType);
	        });
	    });

	    // 콘텐츠 업데이트 함수
	    function updateContentVisibility(type) {
	        // 모든 콘텐츠 숨기기
	        storeContainer.style.display = 'none';
	        communityContainer.style.display = 'none';

	        // type에 맞는 콘텐츠만 보이기
	        if (type === "all") {
	            storeContainer.style.display = 'block';
	            communityContainer.style.display = 'block';
	        } else if (type === "store") {
	            storeContainer.style.display = 'block';
	        } else if (type === "community") {
	            communityContainer.style.display = 'block';
	        }
	    }

	    // 초기 상태 설정
	    updateContentVisibility(currentType);
	});
