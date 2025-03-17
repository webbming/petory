import {apiClient} from "./common/api.js";
import {createPostElement ,  createTop9PostElement} from "./common/Util.js";

document.addEventListener("DOMContentLoaded", async function () {

    const size = 5;
    const boardList = document.querySelector(".exposure_tag ul");
    const rigList = document.querySelector(".rig ol");
    let page = 0;
    let isLoading = false;
    let currentCategory = "all"; // 기본적으로 전체 카테고리로 설정
    let sortOrder = "인기순"; // 기본적으로 최신순


    // 기본 게시글 로딩
    await loadMorePosts();

    // 게시글을 불러오는 함수
    async function loadMorePosts() {
        if (isLoading) return;
        isLoading = true;

        try {
            const {data} = await apiClient.get(`/board/list?page=${page}&size=${size}&category=${currentCategory}&sort=${sortOrder}`)



            if (data.length > 0) {
                data.forEach(post => boardList.appendChild(createPostElement(post)));
                page++;

                if(!isLoading){
                    window.addEventListener("scroll", handleScroll);
                }
            } else {
                window.removeEventListener("scroll" , handleScroll);
                const div = document.createElement("div");
                div.classList.add("myboard-empty");
                div.innerHTML = `<p>검색된 결과가 없습니다.</p>`
                boardList.appendChild(div)
            }
        } catch (e) {
            console.error(e + " 목록을 불러오지 못했습니다.");
        } finally {
            isLoading = false;
        }
    }

    // 가장 많이 조회된 게시글
    async function loadBoardBest(type) {
        try {
            const {data} = await apiClient.get(`/board/board/list/${type}`);

            if (!data) {
                console.log("목록을 불러오지 못했습니다.");
            }

            if (data.length > 0) {
                data.forEach((post, index) => {
                    rigList.appendChild(createTop9PostElement(post,index));
                });
            } else {

            }

        } catch (e) {
            console.error(e);
        }
    }

    await loadBoardBest("view");



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
    window.addEventListener("scroll" , handleScroll);

    // 스크롤 함수
    async function handleScroll() {
        const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
        if (scrollTop + clientHeight >= scrollHeight - 10) {
            await loadMorePosts();
        }
    }
});