// Lọc theo type
function filterByType() {
    const typeFilterValue = document.getElementById('typeFilter').value.toLowerCase();
    const table = document.getElementById('questionTable');
    const rows = table.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) {
        const typeCell = rows[i].getElementsByTagName('td')[2].textContent.toLowerCase();

        if (typeFilterValue === '' || typeCell === typeFilterValue) {
            rows[i].style.display = ''; // Hiển thị hàng nếu khớp với bộ lọc
        } else {
            rows[i].style.display = 'none'; // Ẩn hàng nếu không khớp
        }
    }
}

// Lọc theo lesson
function filterByLesson() {
    const lessonFilterValue = document.getElementById('lessonFilter').value.toLowerCase();
    const table = document.getElementById('questionTable');
    const rows = table.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) {
        const lessonCell = rows[i].getElementsByTagName('td')[3].textContent.toLowerCase();

        if (lessonFilterValue === '' || lessonCell === lessonFilterValue) {
            rows[i].style.display = ''; // Hiển thị hàng nếu khớp với bộ lọc
        } else {
            rows[i].style.display = 'none'; // Ẩn hàng nếu không khớp
        }
    }
}

// Lọc theo subject
function filterBySubject() {
    const subjectFilterValue = document.getElementById('subjectFilter').value.toLowerCase();
    const table = document.getElementById('questionTable');
    const rows = table.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) {
        const subjectCell = rows[i].getElementsByTagName('td')[4].textContent.toLowerCase();

        if (subjectFilterValue === '' || subjectCell === subjectFilterValue) {
            rows[i].style.display = ''; // Hiển thị hàng nếu khớp với bộ lọc
        } else {
            rows[i].style.display = 'none'; // Ẩn hàng nếu không khớp
        }
    }
}

// Lọc theo nội dung câu hỏi (content)
function filterByContent() {
    const contentFilterValue = document.getElementById('contentFilter').value.toLowerCase();
    const table = document.getElementById('questionTable');
    const rows = table.getElementsByTagName('tr');

    for (let i = 0; i < rows.length; i++) {
        const contentCell = rows[i].getElementsByTagName('td')[1].textContent.toLowerCase();

        if (contentCell.indexOf(contentFilterValue) > -1 || contentFilterValue === '') {
            rows[i].style.display = ''; // Hiển thị hàng nếu khớp với nội dung tìm kiếm
        } else {
            rows[i].style.display = 'none'; // Ẩn hàng nếu không khớp
        }
    }
}

function filterAll() {
    const typeFilterValue = document.getElementById('typeFilter').value.toLowerCase();
        const lessonFilterValue = document.getElementById('lessonFilter').value.toLowerCase();
        const subjectFilterValue = document.getElementById('subjectFilter').value.toLowerCase();
        const contentFilterValue = document.getElementById('contentFilter').value.toLowerCase();
        const rows = document.getElementById('questionTable').getElementsByTagName('tr');

        for (let i = 0; i < rows.length; i++) {
            const typeCell = rows[i].getElementsByTagName('td')[2].textContent.toLowerCase();
            const lessonCell = rows[i].getElementsByTagName('td')[3].textContent.toLowerCase();
            const subjectCell = rows[i].getElementsByTagName('td')[4].textContent.toLowerCase();
            const contentCell = rows[i].getElementsByTagName('td')[1].textContent.toLowerCase();

            // Kiểm tra xem hàng có phù hợp với tất cả các bộ lọc không
            const matchesType = typeFilterValue === '' || typeCell === typeFilterValue;
            const matchesLesson = lessonFilterValue === '' || lessonCell === lessonFilterValue;
            const matchesSubject = subjectFilterValue === '' || subjectCell === subjectFilterValue;
            const matchesContent = contentCell.indexOf(contentFilterValue) > -1;

            // Hiển thị hàng nếu nó phù hợp với tất cả các bộ lọc
            if (matchesType && matchesLesson && matchesSubject && matchesContent) {
                rows[i].style.display = '';
            } else {
                rows[i].style.display = 'none';
            }
        }
}
