

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
  // 모든 HTML 태그 제거 및 스타일 속성 제거를 위한 개선된 방법
  let contentTextOnly = post.content;
  console.log(post.image);
  // 이미지/figure 태그 제거
  contentTextOnly = contentTextOnly.replace(/<figure.*?>.*?<\/figure>/g, '');

  // 모든 HTML 태그 제거
  contentTextOnly = contentTextOnly.replace(/<[^>]*>/g, ' ');

  // 연속된 공백 제거 및 텍스트 정리
  contentTextOnly = contentTextOnly.replace(/\s+/g, ' ').trim();

  // 필요하다면 텍스트 길이 제한 (예: 100자)
  contentTextOnly = contentTextOnly.length > 100 ? contentTextOnly.substring(0, 100) + '...' : contentTextOnly;
  const hashtags = typeof post.hashtag === "string" ? JSON.parse(post.hashtag) : post.hashtag;
    li.innerHTML = `
    
    <a href="/board/read?boardId=${post.boardId}">
      <div class="wrap">
        <div class="inner">
        
          <span>${post.categoryId}</span>
          
          <div class="tit">${post.title}</div>
          
          <div class="content-preview">
            <p>${contentTextOnly}</p>
            <div>
                ${post.hashtag.map(tag =>
                    `<button class="tagBtn">${tag}</button>`    
                ).join(' ')}
            </div>
          </div>
          
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
          <span>❤️ <strong>${post.likeCount}</strong></span>
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

