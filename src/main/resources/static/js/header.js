import {headerCartSize} from "./common/api-declare.js";

document.addEventListener("DOMContentLoaded", async function (){
  const searchInput = document.querySelector(".right-section .search-bar > input");
  const searchButton = document.querySelector(".search-icon")

  let searchValue = null;
  searchInput.addEventListener("input" , (e) =>{
    searchValue = e.target.value;
  })

  searchButton.addEventListener("click" , async (e) =>{
    e.preventDefault();

    if (!searchValue || searchValue.trim() === '') {
      return;
    }
    window.location.href = `/search?keyword=${searchValue}`
  })

  searchInput.addEventListener("keydown", (e) => {
    if (e.key === "Enter") {
      e.preventDefault();  // 폼 제출을 방지
      if (searchValue && searchValue.trim() !== '') {
        window.location.href = `/search?keyword=${searchValue}`;
      }
    }
  });

  await headerCartSize();

})