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

            if (data.length === 0) {
                contentContainer.innerHTML = `
                    <div class="myboard_empty">
                        <p>${emptyMessages[type]}</p>
                        <a class="btn_line" href="/">커뮤니티 둘러보기</a>
                    </div>
                `;
            } else {
                contentContainer.innerHTML = data.map(item => `
                    <li>
                        <a href="/post/${item.id}">
                            <h3>${item.title}</h3>
                            <p>${item.preview}</p>
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
