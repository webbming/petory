document.addEventListener("DOMContentLoaded" , async (e) =>{

    const tabs = document.querySelectorAll(".consult__tab .tab li > a")
    const contentContainer = document.querySelector(".activities_content ul");
    const emptyMessages = {
        boards : "작성한 글이 없습니다.",
        comments : "작성한 댓글이 없습니다.",
        likes : "좋아요한 글이 없습니다."
    }

    await fetchData("boards");

    tabs.forEach(tab =>{
        tab.addEventListener("click" , async function(event){
            event.preventDefault();

            tabs.forEach(t => t.classList.remove("active"));
            this.classList.add("active")

            const tabType = this.dataset.type
            await fetchData(tabType);

        })
    })

    async function fetchData(type) {
        try {
            const response = await fetch(`/api/users/me/activities/${type}`);
            const data = await response.json();

            console.log(data)

            // 기존 콘텐츠 초기화
            contentContainer.innerHTML = "";

            if (!data.data.boards || data.data.boards.length === 0 ) {
                contentContainer.innerHTML = `
                    <div class="myboard_empty">
                        <p>${emptyMessages[type]}</p>
                        <a class="btn_line" href="/board/board">커뮤니티 둘러보기</a>
                    </div>
                `;
            } else {
                contentContainer.innerHTML = data.data.boards.map(item => `
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
        } catch (error) {
            console.error("데이터 불러오기 실패:", error);
            contentContainer.innerHTML = `<p>데이터를 불러오는 중 오류가 발생했습니다.</p>`;
        }
    }
})
