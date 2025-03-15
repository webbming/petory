document.addEventListener("DOMContentLoaded", async function () {


  let page = 0;
  const size = 5;
  let isLoading = false;
  const boardList = document.querySelector(".exposure_tag ul");
  const rigList = document.querySelector(".rig ol");
  let currentCategory = "all"; // 기본적으로 전체 카테고리로 설정
  let searchQuery = ""; // 기본 검색어
  let sortOrder = "최신순"; // 기본적으로 최신순
  let period = "1개월"; // 기본적으로 1개월
// 기본 게시글 로딩

  await loadMorePosts();

  const finalSearchBtn = document.querySelector(".final-searchBtn");

  finalSearchBtn.addEventListener("click", async function (e){
    e.preventDefault();
    const searchForm = document.querySelector("#search-form");
    const searchValue = searchForm.querySelector(".search-query").value;
    const sortValue = searchForm.querySelector(".sorted").value;
    const periodValue = searchForm.querySelector(".bydate").value;

    console.log(searchQuery)
    console.log(sortOrder)
    console.log(period)

    searchQuery = searchValue;
    sortOrder = sortValue;
    period = periodValue;

    // 검색할 때 기존 게시글 제거 & 페이지 초기화
    boardList.innerHTML = "";
    page = 0;

    await loadMorePosts()
    searchQuery = ""
    sortOrder = "latest"
    period = "1개월";
  })


  // 게시글을 불러오는 함수
  async function loadMorePosts() {
    if (isLoading) return;
    isLoading = true;

    try {
      const response = await fetch(`/board/list?page=${page}&size=${size}&category=${currentCategory}&sort=${sortOrder}&search=${searchQuery}&period=${period}`, {
        method: "GET",
        headers: {
          "Content-Type": "application/json"
        },
        credentials: "include"
      });
      const { data } = await response.json();
      console.log(data)

      if (data.length > 0) {
        data.forEach(post => {
          const li = document.createElement("li");
          li.innerHTML = `
                  <a href="/board/read?boardId=${post.boardId}">
                    <div class="wrap">
                      <div class="inner">
                        <span>${post.categoryId}</span>
                        <div class="tit">${post.title}</div>
                        <p>${post.content}</p>
                      </div>
                     <img src="">
                    </div>
                    <div class="bottom">
                      <div class="date">
                        <span>${post.nickname}</span>
                        <span>${post.createAt}</span>
                        <span>조회수 <strong>${post.viewCount}</strong></span>
                      </div>
                      <div class="like">
                        <span>좋아요 <strong>${post.likeCount}</strong></span>
                        <span>댓글 <strong>${post.commentCount}</strong></span>
                      </div>
                    </div>
                  </a>
            `;
          boardList.appendChild(li);
        });
        page++;
      } else {
        window.removeEventListener("scroll", handleScroll); // 더 이상 스크롤 이벤트를 처리하지 않음
      }
    } catch (e) {
      console.error(e + " 목록을 불러오지 못했습니다.");
    } finally {
      isLoading = false;
    }
  }

  // 스크롤 이벤트 핸들러
  async function handleScroll() {
    const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
    if (scrollTop + clientHeight >= scrollHeight - 10) {
     await loadMorePosts(); // 스크롤이 끝에 도달하면 추가 요청
    }
  }


  // 오른쪽 인기글 고정 9개
  async function loadBoardBest(type) {
    try {
      const response = await fetch(`/board/board/list/${type}`, {
        method: "GET"
      });

      const { data } = await response.json();

      if (!response.ok) {
        console.log("목록을 불러오지 못했습니다.");
      }

      if (data.length > 0) {
        data.forEach((post, index) => {
          const li = document.createElement("li");
          li.innerHTML = `
                <a>
                  <span class="num">${index + 1}</span>
                  <p>${post.title}</p>
                  <span class="same">변동없음</span>
                </a>
          `;
          rigList.appendChild(li);
        });
      }else {

      }

    } catch (e) {
      console.error(e);
    }
  }

  // 'best' 카테고리 불러오기
  loadBoardBest("best");

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
});