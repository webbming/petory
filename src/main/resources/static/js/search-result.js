import {apiClient} from "./common/api.js";
import {createPostElement} from "./common/Util.js";

document.addEventListener("DOMContentLoaded", async (e) =>{
    const tabs = document.querySelectorAll(".consult__tab .tab li > a");
    let currentType = "all";

    tabs.forEach(tab => {
        tab.addEventListener("click", async function(event) {
            event.preventDefault();
            console.log("ghg")

            tabs.forEach(t => t.classList.remove("active"));
            this.classList.add("active");

            const tabType = this.dataset.type;
            currentType = tabType;



        });
    })

})
