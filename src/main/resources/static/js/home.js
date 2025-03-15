

document.addEventListener("DOMContentLoaded", async function () {


  /* 인기 급 상승 */

  const btns = document.querySelectorAll(".filter button")

  btns.forEach(btn =>{

    btn.addEventListener("click" , (e) =>{
      const btn = e.target;
      btns.forEach(e => {
        e.classList.remove("active")
      })
      btn.classList.add("active")

      const tabType = btn.dataset.type;


      boardContentData(tabType)
    })
  })

  const defaultTab = btns[0]?.dataset.type; // 첫 번째 버튼의 데이터 속성 가져오기
  if (defaultTab) {
    btns[0].classList.add("active"); // 첫 번째 버튼을 활성화
    await boardContentData(defaultTab);
  }


  async function boardContentData(type){
    try{
      const response = await fetch(`/board/board/list/${type}` , {
        method : "GET"
      })

      if(!response.ok){
        console.log("글 불러오기 실패")
      }

      const {data} = await response.json();
      const pupularSlick = document.querySelector(".popular_slick");

      pupularSlick.innerHTML = "";
      let html = "";
      for(let i = 0; i<data.length; i+=3){
        html += "<div>";

        for(let j = i ; j< i + 3 && j <data.length; j++){
          html += `
            <a href="/board/read?boardId=${data[j].boardId}">
              <span class="num">${j + 1}</span>
              <div class="inner ellipsis">
                  <strong class="ellipsis">${data[j].title}</strong>
                  <div class="like">
                    <span>${data[j].createAt}</span>
                    <span>좋아요 ${data[j].likeCount}</span>
                    <span>댓글 ${data[j].commentCount}</span>
                  </div>
              </div>
            </a>
          `;
        }
        html += "</div>";
      }
      pupularSlick.innerHTML = html;

    }catch (e){
      console.error(e)
    }
  }


/* 베스트 상품 */


  async  function productContent(){

    try{
      const response =  await fetch("/api/products" , {
        method : "GET"
      })

      if(!response.ok){
        console.log("베스트 상품 목록 불러오기 실패")
      }

      const {data} = await response.json();

      console.log(data)

      const product_list = document.querySelector(".product_list");
      console.log(product_list)

      product_list.innerHTML = data.map((product) =>
        `
          <li>
            <a href="/products/${product.productId}">
              <div class="img">
                  <img src=${product.imageUrl}>
                  <span>스토어 리뷰</span>
              </div>
              <div class="pro_name">
                  <p>${product.productName}</p>
                  <span class="rate">${product.averageRating}</span>
              </div>
            </a>
          </li>
        `
      ).join('')

    }catch (e){
      console.log(e)
    }

  }

  await productContent();

  const navbar = document.querySelector(".scrolling-tab")
  let lastScrollTop = 0;
  window.addEventListener("scroll" , function (){
    const scrollTop = window.pageYOffset || document.documentElement.scrollTop || document.body.scrollTop;

    if(scrollTop > lastScrollTop) {
      navbar.style.opacity = '0';
      navbar.style.transform = 'translateY(-100%)';
    }else{
      navbar.style.opacity = '1'; //
      navbar.style.transform = 'translateY(0)';
    }
    lastScrollTop = scrollTop <= 0 ? 0 : scrollTop
  })



});

