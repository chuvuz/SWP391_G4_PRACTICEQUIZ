function filterUsersByRole() {
    const roleFilter = document.getElementById('roleFilter').value;
    const table = document.getElementById('userTable');
    const rows = table.getElementsByTagName('tr');

    for (let i = 1; i < rows.length; i++) { // Skip header row
        const role = rows[i].getElementsByTagName('td')[2].textContent; // Get role cell


        if (roleFilter === 'All' || role === roleFilter) {
            rows[i].style.display = '';
        } else {
            rows[i].style.display = 'none';
        }
    }
}