// Gestion de l'auto-agrandissement de la zone de texte
const textarea = document.getElementById('note');
if (textarea) {
    textarea.addEventListener('input', e => {
        textarea.style.height = 'auto';
        textarea.style.height = textarea.scrollHeight + 'px';
    });
}

// Gestion du mode édition
const toggleEditBtn = document.getElementById('toggleEditBtn');
const cancelEditBtn = document.getElementById('cancelEditBtn');
const viewMode = document.getElementById('viewMode');
const editMode = document.getElementById('editMode');

if (toggleEditBtn && viewMode && editMode) {
    toggleEditBtn.addEventListener('click', () => {
        viewMode.classList.toggle('view-mode');
        viewMode.classList.toggle('edit-mode');
        editMode.classList.toggle('edit-mode');
        editMode.classList.toggle('view-mode');

        if (editMode.classList.contains('view-mode')) {
            toggleEditBtn.innerHTML = '<i class="bi bi-pencil"></i> Modifier';
        } else {
            toggleEditBtn.innerHTML = '<i class="bi bi-eye"></i> Visualiser';
        }
    });
}

if (cancelEditBtn && viewMode && editMode && toggleEditBtn) {
    cancelEditBtn.addEventListener('click', () => {
        viewMode.classList.add('view-mode');
        viewMode.classList.remove('edit-mode');
        editMode.classList.add('edit-mode');
        editMode.classList.remove('view-mode');
        toggleEditBtn.innerHTML = '<i class="bi bi-pencil"></i> Modifier';
    });
}