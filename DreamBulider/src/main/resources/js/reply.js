const deleteButton = document.getElementById('delete-btn');

if(deleteButton) {
    deleteButton.addEventListener('click', event => {
       let id = document.getElementById('reply-id').value;
       fetch(`/api/reply/${id}`, {
           method : 'PATCH'
       })
        .then(() => {
           alert('댓글을 삭제 비설황 했습니다.');
           location.replace('/reply');
        });
    });
}

const createButton = document.getElementById("create-btn");
if(createButton) {
    createButton.addEventListener("click", (event) => {
       fetch("/api/reply", {
           method : "POST",
           headers : {
               "Content-Type" : "application/json"
           },
           body : JSON.stringify({
               comment: document.getElementById("comment")
           }),
       }).then(()=>{
          alert("등록 완료");
          location.replace("/reply");
       })
    });
}