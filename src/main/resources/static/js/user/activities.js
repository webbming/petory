import {loadTopInfo} from "../common/api-declare.js";
import {modalUtil} from "./modal.js";
import {mypageTopInfo} from "../common/api-declare.js";
/* activities.js 유저의 활동 기록을 불러오는 js */


document.addEventListener("DOMContentLoaded", async () => {
    modalUtil();
    await loadTopInfo();

    let currentType = "boards"; // 현재 활성화된 탭
    let currentPage = 0; // 현재 페이지 (0부터 시작)
    const pageSize = 5; // 한 페이지에 표시할 항목 수

    const tabs = document.querySelectorAll(".consult__tab .tab li > a");
    const contentContainer = document.querySelector(".activities_content ul");
    const paginationDiv = document.querySelector(".activities_content > div.pagination");
    const pageContainer = document.querySelector(".activities_content > div.pagination > ul.page");

    // 초기에는 페이지네이션 숨김
    if (paginationDiv) {
        paginationDiv.style.display = "none";
    } else {
        // 페이지네이션 요소가 없다면 생성
        const newPaginationDiv = document.createElement("div");
        newPaginationDiv.className = "pagination";
        newPaginationDiv.style.display = "none";

        const pageUl = document.createElement("ul");
        pageUl.className = "page";

        newPaginationDiv.appendChild(pageUl);
        document.querySelector(".activities_content").appendChild(newPaginationDiv);
    }


    const emptyMessages = {
        boards: "작성한 글이 없습니다.",
        comments: "작성한 댓글이 없습니다.",
        likes: "좋아요한 글이 없습니다."
    };

    // 초기 탭 활성화
    const initialTab = document.querySelector(".consult__tab .tab li > a[data-type='boards']");
    if (initialTab) initialTab.classList.add("active");

    // 초기 데이터 로드
    await fetchData("boards", 0);

    // 탭 클릭 이벤트 처리
    tabs.forEach(tab => {
        tab.addEventListener("click", async function(event) {
            event.preventDefault();

            tabs.forEach(t => t.classList.remove("active"));
            this.classList.add("active");

            const tabType = this.dataset.type;
            currentType = tabType;
            currentPage = 0; // 탭 변경 시 페이지 초기화

            await fetchData(tabType, currentPage);
        });
    });

    async function fetchData(type, page) {
        try {
            showLoading(contentContainer);

            // 백엔드에 페이지와 사이즈 파라미터 전달
            const response = await fetch(`/api/users/me/activities/${type}?page=${page}&size=${pageSize}`);
            const result = await response.json();

            console.log(result)
            if (!response.ok) {
                throw new Error(result.message || "데이터를 불러오는데 실패했습니다.");
            }

            const data = result.data;

            console.log(data)
            // 현재 데이터 저장 (캐싱 효과)
            renderContent(type, data);
            renderPagination(data.totalCount, data.currentPage, data.totalPages);

        } catch (error) {
            console.error("데이터 불러오기 실패:", error);
            contentContainer.innerHTML = `<p>데이터를 불러오는 중 오류가 발생했습니다.</p>`;
            hidePageContainer();
        }
    }

    function renderContent(type, data) {
        contentContainer.innerHTML = "";

        const items = data[type];

        if (!items || items.length === 0) {
            contentContainer.innerHTML = `
                <div class="myboard_empty">
                    <p>${emptyMessages[type]}</p>
                    <a class="btn_line" href="/board/board">커뮤니티 둘러보기</a>
                </div>
            `;
            hidePageContainer();
            return;
        }

        contentContainer.innerHTML = items.map(item => `
            <li>
                <a href="/board/read?boardId=${item.boardId}">
                    <div class="wrap">
                        <div class="top">
                            <span>${item.categoryId}</span>
                            <h3 class="tit">${item.title}</h3>
                        </div>
                        <div class="bottom">
                            <div class="date">
                                <span>${item.nickname}</span>
                                <span>${item.createAt}</span>
                                <span>조회 수 <strong>${item.viewCount}</strong></span>
                            </div>
                            <div class="like">
                                <span>💗 <strong>${item.likeCount}</strong></span>
                                <span>💬 <strong>${item.commentCount}</strong></span>
                            </div>
                        </div>
                    </div>
                </a>
            </li>
        `).join("");
    }

    function renderPagination(totalCount, currentServerPage, totalPages) {
        // 수정된 페이지 컨테이너 선택자
        const pageContainer = document.querySelector(".activities_content > div.pagination > ul.page");
        if (!pageContainer) return;

        // 데이터가 없거나 한 페이지보다 적은 경우 페이지네이션 숨김
        if (!totalCount || totalPages <= 1) {
            hidePageContainer();
            return;
        }

        // 서버에서 넘어온 현재 페이지 사용
        currentPage = currentServerPage || 0;

        // 표시할 페이지 번호 범위 계산
        const displayRange = 5; // 한 번에 표시할 페이지 버튼 수
        let startPage = Math.floor(currentPage / displayRange) * displayRange;
        let endPage = Math.min(startPage + displayRange - 1, totalPages - 1);

        let paginationHTML = "";


        // 페이지 번호
        for (let i = startPage; i <= endPage; i++) {
            const isActive = i === currentPage ? "active" : "";
            paginationHTML += `<li><a href="#" data-page="${i}" class="${isActive}">${i + 1}</a></li>`;
        }



        pageContainer.innerHTML = paginationHTML;
        showPageContainer();

        // 페이지 번호 클릭 이벤트 연결
        const pageLinks = pageContainer.querySelectorAll("a");
        pageLinks.forEach(link => {
            link.addEventListener("click", async function(event) {
                event.preventDefault();

                const newPage = parseInt(this.dataset.page);
                if (currentPage !== newPage) {
                    currentPage = newPage;
                    await fetchData(currentType, currentPage);

                    // 스크롤을 상단으로 이동 (선택사항)
                    window.scrollTo({ top: 0, behavior: 'smooth' });
                }
            });
        });
    }

    function showLoading(container) {
        container.innerHTML = `<div class="loading">데이터를 불러오는 중...</div>`;
    }

    function hidePageContainer() {
        const paginationDiv = document.querySelector(".activities_content > div.pagination");
        if (paginationDiv) {
            paginationDiv.style.display = "none";
        }
    }

    function showPageContainer() {
        const paginationDiv = document.querySelector(".activities_content > div.pagination");
        if (paginationDiv) {
            paginationDiv.style.display = "block";
        }
    }
});