function validateForm() {
    const fullName = document.getElementById('fullName').value;
    const fullNamePattern = /^[A-Za-z]+(\s[A-Za-z]+)*$/;
    const fullNameError = document.getElementById('fullNameError');
    const fullNameInput = document.getElementById('fullName');

    if (!fullNamePattern.test(fullName)) {
        fullNameError.textContent = 'Full Name must contain only letters and a single space between words.';
        fullNameInput.style.borderColor = 'red';
        return false;
    } else {
        fullNameError.textContent = '';
        fullNameInput.style.borderColor = '';
    }

    return true;
}