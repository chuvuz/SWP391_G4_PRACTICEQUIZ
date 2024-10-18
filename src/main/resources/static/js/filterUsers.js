function filterUsers() {
    const searchInput = document.getElementById('searchInput').value.toLowerCase();
    const table = document.getElementById('userTable');
    const rows = table.getElementsByTagName('tr');

    for (let i = 1; i < rows.length; i++) {
        const cells = rows[i].getElementsByTagName('td');
        const fullName = cells[0].textContent.toLowerCase();

        if (fullName.indexOf(searchInput) > -1) {
            rows[i].style.display = '';
        } else {
            rows[i].style.display = 'none';
        }
    }
