

let lastScrollTop = 0;

export function scrollTabEffect(){
  const navbar = document.querySelector(".scrolling-tab")

  const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;

  if(scrollTop > lastScrollTop) {
    navbar.style.opacity = '0';
    navbar.style.transform = 'translateY(-100%)';
  }else{
    navbar.style.opacity = '1'; //
    navbar.style.transform = 'translateY(0)';
  }
  lastScrollTop = scrollTop <= 0 ? 0 : scrollTop
}




// 나열되는 게시글
export function createPostElement(post) {
    const li = document.createElement("li");
    const contentWithoutFigures = post.content.replace(/<figure.*?>.*?<\/figure>/g, '').trim();
    const contentTextOnly = contentWithoutFigures.replace(/<p.*?>(.*?)<\/p>/g, '$1').trim();

    li.innerHTML = `
    <a href="/board/read?boardId=${post.boardId}">
      <div class="wrap">
        <div class="inner">
          <span>${post.categoryId}</span>
          <div class="tit">${post.title}</div>
          <p>${contentTextOnly}</p>
        </div>
        ${post.image ? `<img src="${post.image}" />` : ''}
      </div>
      <div class="bottom">
        <div class="date">
          <span>${post.nickname}</span>
          <span>${post.createAt}</span>
          <span>조회수 <strong>${post.viewCount}</strong></span>
        </div>
        <div class="like">
          <span>💗 <strong>${post.likeCount}</strong></span>
          <span>💬 <strong>${post.commentCount}</strong></span>
        </div>
      </div>
    </a>
  `;
    return li;
}

// Top 9 게시글

export function createTop9PostElement(post,index){
    const li = document.createElement("li");
    li.innerHTML = `
                <a href="/board/read?boardId=${post.boardId}">
                  <span class="num">${index + 1}</span>
                  <p>${post.title}</p>
                  <span class="same">변동없음</span>
                </a>
          `;
    return li;
}

