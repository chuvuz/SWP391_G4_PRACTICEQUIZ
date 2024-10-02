function validatePassword() {
    const newPassword = document.getElementById("newPassword").value;
    const confirmPassword = document.getElementById("confirmPassword").value;
    const errorMessage = document.getElementById("error-message");

    const passwordRegex = /^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?!.*\s).{8,}$/;

    if (!passwordRegex.test(newPassword)) {
        errorMessage.textContent = "Password must contain at least one lowercase letter, one uppercase letter, one number, be at least 8 characters long, and not contain any spaces.";
        return false;
    }

    if (newPassword !== confirmPassword) {
        errorMessage.textContent = "Passwords do not match.";
        return false;
    }

    errorMessage.textContent = "";
    return true;
}

function togglePasswordVisibility(id) {
    const passwordField = document.getElementById(id);
    const toggleButton = document.getElementById(id + "-toggle");
    const eyeIcon = toggleButton.querySelector("img");

    if (passwordField.type === "password") {
        passwordField.type = "text";
        eyeIcon.src = "/images/view.png"; // Path to the eye-closed icon
    } else {
        passwordField.type = "password";
        eyeIcon.src = "/images/hide.png"; // Path to the eye-open icon
    }
}