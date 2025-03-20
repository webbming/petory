import {scrollTabEffect} from "./common/Util.js";

const commentBtn = document.getElementById("commentButton")
commentBtn.addEventListener("click", (e)=>{
    e.preventDefault();
    document.querySelector(".likeContain").scrollIntoView({
        behavior: "smooth",
        block: "start"
    });
})

document.addEventListener("DOMContentLoaded", (e) =>{
    const likesBtn = document.getElementById("likeCount");
    const boardId = likesBtn.dataset.boardId;
    const userId = likesBtn.dataset.id;
    likesBtn.addEventListener("click" , async (e) =>{
        e.preventDefault();

        const data = {
            boardId : boardId,
            userId : userId
        }

        const response = await fetch(`/board/like` , {

            method : "POST" ,
            headers : {
                "Content-Type" : "application/json"
            },
            body : JSON.stringify(data)

        })

        const result = await response.json()

        if(!response.ok){
            console.log("데이터를 못받아왔씁니다")
        }

        if(result.data.contain === "contained"){
            likesBtn.textContent = `❤️ ${result.data.likeCount}`;
        }
        else {
            likesBtn.textContent = `🤍 ${result.data.likeCount}`;
        }


    })


    const deleteBtn = document.querySelector(".deleteBtn")
    console.log(deleteBtn)
    deleteBtn.addEventListener("click", async (e) =>{
        e.preventDefault();
        const boardId = deleteBtn.getAttribute("data-board-id");

        const conn = confirm("정말 삭제하시겠습니까?")
        if (conn){
            const response = await fetch(`/board/delete?boardId=${Number(boardId)}` , {
                method : "DELETE",
                credentials : "include"
            })

            if(!response.ok){
                console.log("삭제 실패했습니다.")
            }

            if(response.ok){
                alert("게시물 삭제 완료")
                window.location.href = "/board/main"
            }
        }
    })
})
document.addEventListener("DOMContentLoaded", (e) =>{
    window.addEventListener("scroll" ,scrollTabEffect )
    const likeButtons = document.querySelectorAll(".commentLikeCount");

    likeButtons.forEach(likesBtn => {
        const commentId = likesBtn.dataset.commentId;
        const userId = likesBtn.dataset.id;

        likesBtn.addEventListener("click" , async (e) =>{
            e.preventDefault();

            const data = {
                commentId : commentId,
                userId : userId
            }
            const response = await fetch(`/board/commentLike` , {

                method : "POST" ,
                headers : {
                    "Content-Type" : "application/json"
                },
                body : JSON.stringify(data)

            })

            const result = await response.json()

            if(!response.ok){
                console.log("데이터를 못받아왔씁니다")
            }

            if(result.data.contain === "contained"){
                likesBtn.textContent = `❤️ ${result.data.commentLikeCount}`;
            }
            else {
                likesBtn.textContent = `🤍 ${result.data.commentLikeCount}`;
            }
        })
    })
})