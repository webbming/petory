import {apiClient} from "./common/api.js";
import {createPostElement,  createTop9PostElement , scrollTabEffect} from "./common/Util.js";

document.addEventListener("DOMContentLoaded", async function () {
    let isLoading = false;
    let page = 0;
    const size = 5;
    const boardList = document.querySelector(".exposure_tag ul");
    const searchHashtagList = document.querySelector(".exposure_hashtag ul");
    const rigList = document.querySelector(".rig ol");
    let currentCategory = "all"; // 기본적으로 전체 카테고리로 설정
    let searchQuery = ""; // 기본 검색어
    let sortOrder = "최신순"; // 기본적으로 최신순
    let period = "1개월"; // 기본적으로 1개월
    let hashtag = "";
    const hashtagList = [];

    // 기본 게시글 로딩
    await loadMorePosts();

    const finalSearchBtn = document.querySelector(".final-searchBtn");

    finalSearchBtn.addEventListener("click", async function (e) {
        e.preventDefault();

        const searchForm = document.querySelector("#search-form");
        const searchValue = searchForm.querySelector(".search-query").value;
        const sortValue = searchForm.querySelector(".sorted").value;
        const periodValue = searchForm.querySelector(".bydate").value;

        searchQuery = searchValue;
        sortOrder = sortValue;
        period = periodValue;
        hashtag = "";
        // 검색할 때 기존 게시글 제거 & 페이지 초기화
        boardList.innerHTML = "";
        page = 0;

        await loadMorePosts();
    });

    // 게시글을 불러오는 함수
    async function loadMorePosts() {
        if (isLoading) return;
        isLoading = true;

        try {
            const response = await fetch(`/board/list?page=${page}&size=${size}&categoryId=${currentCategory}&sort=${sortOrder}&search=${searchQuery}&period=${period}&hashtag=${hashtag}`, {
                method: "GET",
                headers: {
                    "Content-Type": "application/json"
                },
                credentials: "include"
            });
            const {data} = await response.json();
            console.log(data);

            if (data.length > 0) {
                data.forEach(post => {
                    boardList.appendChild(createPostElement(post));
                });
                page++;

                window.addEventListener("scroll", handleScroll);
            } else {
                window.removeEventListener("scroll", handleScroll); // 더 이상 스크롤 이벤트를 처리하지 않음

                const div = document.createElement("div");
                div.classList.add("myboard-empty");
                div.innerHTML = `<p>검색된 결과가 없습니다.</p>`;
                boardList.appendChild(div);
            }
        } catch (e) {
            console.error(e + " 목록을 불러오지 못했습니다.");
        } finally {
            isLoading = false;
        }
    }

    // 오른쪽 인기글 고정 9개
    async function loadBoardBest(type) {
        try {
            const {data} = await apiClient.get(`/board/board/list/${type}`);

            if (!data) {
                console.log("목록을 불러오지 못했습니다.");
            }

            if (data.length > 0) {
                data.forEach((post, index) => rigList.appendChild(createTop9PostElement(post,index)));
            } else {
                // 처리할 필요 없음
            }
        } catch (e) {
            console.error(e);
        }
    }

    // 'best' 카테고리 불러오기
    await loadBoardBest("best");

    // 카테고리 버튼 클릭 시 이벤트 핸들러
    const categoryBtns = document.querySelectorAll(".keyword");

    categoryBtns.forEach((btn) => {
        btn.addEventListener("click", async (e) => {
            // 모든 버튼에서 'active' 클래스를 제거하고 클릭한 버튼에 'active' 추가
            categoryBtns.forEach(btn => {
                btn.classList.remove("active");
            });
            btn.classList.add("active");

            // 선택한 카테고리 타입을 설정
            currentCategory = e.target.dataset.type;
            console.log(currentCategory);
            // 게시글 리스트 초기화 및 페이지 초기화
            boardList.innerHTML = "";
            page = 0; // 페이지 초기화

            // 해당 카테고리에 맞는 게시글을 불러옴
            await loadMorePosts();
        });
    });

    // 스크롤 이벤트 추가
    window.addEventListener("scroll", handleScroll);

    async function handleScroll() {
        const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
        if (scrollTop + clientHeight >= scrollHeight - 10) {
            await loadMorePosts();
        }
    }

    window.addEventListener("scroll" ,scrollTabEffect);

    searchHashtagList.addEventListener("click", async (e) => {
        console.log(e.target);  // 클릭된 요소 확인

        // X 버튼을 클릭한 경우, 해당 LI만 삭제하고 검색 실행 안 함
        if (e.target.classList.contains("closeBtn")) {
            e.preventDefault();
            e.stopPropagation();

            // X 버튼이 속한 LI가 `searchHashtagList` 내부인지 확인 후 삭제
            const li = e.target.closest("li");
            if (li && searchHashtagList.contains(li)) {
                li.remove();
            }
            return;  // 검색 실행 안 함
        }

        // 클릭된 요소가 LI 또는 LI 내부의 button인 경우 처리
        let clickedHashtag = e.target.textContent.trim();

        // 클릭된 요소가 button이면, 부모 LI에서 텍스트를 가져오기
        if (e.target.closest("button")) {
            clickedHashtag = e.target.closest("li").textContent.trim();
        }

        if (clickedHashtag) {
            hashtag = encodeURIComponent(clickedHashtag);  // URL 인코딩

            // 게시글 목록 초기화
            boardList.innerHTML = "";
            page = 0;  // 페이지 초기화

            // 해당 해시태그에 맞는 게시글 로딩
            await loadMorePosts();

            // 해시태그 초기화 (리셋)
            hashtag = "";
        }
    });


// 게시글 내 해시태그 클릭 시 처리
    boardList.addEventListener("click", async (e) => {
        if (e.target.classList.contains("tagBtn")) {
            e.preventDefault();
            e.stopPropagation();

            const clickedHashtag = e.target.textContent;

            // 해당 해시태그가 최근 검색 목록에 없으면 추가
            if (![...searchHashtagList.children].some(li => li.textContent === clickedHashtag)) {
                const li = document.createElement("li");
                li.classList.add("hashtag-item");  // 클래스 추가 (스타일링용)

                li.innerHTML = `
                <button class="tagBtn">${clickedHashtag}</button>
                <button class="closeBtn">X</button>
            `;
                searchHashtagList.appendChild(li);  // 최근 검색 목록에 해시태그 추가
            }

            // 해당 해시태그를 검색 쿼리로 사용하여 게시글 리스트 초기화 및 페이지 초기화
            hashtag = encodeURIComponent(clickedHashtag);
            boardList.innerHTML = "";
            page = 0;

            await loadMorePosts();  // 게시글 로딩


        }
    });

});