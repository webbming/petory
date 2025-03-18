document.addEventListener("DOMContentLoaded" , (e) =>{
  const menuItems = document.querySelectorAll(".page-tab-area ul li a");
  const currentPath = window.location.pathname;

  if (currentPath === "/") {
    menuItems[0].parentElement.classList.add("active");
    return;
  }

  menuItems.forEach(item => {
    if(item.getAttribute("href") === currentPath){
      item.parentElement.classList.add("active");
    }

    item.addEventListener("click", function () {

      // 기존 활성화된 요소의 .active 제거
      document.querySelectorAll(".page-tab-area ul li").forEach(li => {
        li.classList.remove("active");
      });

      // 클릭한 요소의 부모인 li에 .active 추가
      this.parentElement.classList.add("active");
    });
  });
})