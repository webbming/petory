function receiveData(data) {
  document.getElementById('address').value = `${data.address}`;
}

const addrFind = (event) => {
  event.preventDefault();
  let size = "width = 650px, height=550px, top=300px, left=300px, scrollbars=yes";
  let openUrl = '/users/addr';
  window.open(openUrl, 'pop', size);
}

