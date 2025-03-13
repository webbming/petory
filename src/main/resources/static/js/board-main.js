

document.addEventListener("DOMContentLoaded",function(){

  let page = 0;
  const size = 5;
  let isLoading = false;
  const boardList = document.querySelector(".exposure_tag ul")
  const rigList = document.querySelector(".rig ol")

  async function loadMorePosts(){
    if(isLoading) return;
    isLoading = true;

    try {
      const response = await fetch(`/board/list?page=${page}&size=${size}`, {
        method : "GET",
        headers : {
          "Content-Type" : "application/json"
        },
        credentials : "include"

      });
      const {data} = await response.json();

      if(data.length > 0) {
        data.forEach(post =>{
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
            boardList.appendChild(li)
        });
        page ++;

      }else{
        window.removeEventListener("scroll",handleScroll)
      }

    }catch (e){
      console.error(e + "목록을 불러오지 못했습니다.")
    }finally {
      isLoading = false;
    }
  }

  function handleScroll() {
    const { scrollTop, scrollHeight, clientHeight } = document.documentElement;
    if (scrollTop + clientHeight >= scrollHeight - 10) {
      loadMorePosts(); // 스크롤이 끝에 도달하면 추가 요청
    }
  }

  loadMorePosts();
  window.addEventListener("scroll" , handleScroll)


  async function loadBoardBest(type){
    try{
      const response = await fetch(`/board/board/list/${type}` , {
        method : "GET"
      })

      const {data} = await  response.json()

      if (!response.ok){
        console.log("목록을 불러오지못했습니다.")
      }

      console.log(data)

      if(data.length > 0){
        data.forEach((post,index) => {
          const li = document.createElement("li");
          li.innerHTML = `
                <a>
                  <span class="num">${index + 1}</span>
                  <p>${post.title}</p>
                  <span class="same">변동없음</span>
                </a>
          `;
          rigList.appendChild(li);
        })
      }

    }catch (e){
      console.error(e)
    }
  }

  loadBoardBest('best');
})