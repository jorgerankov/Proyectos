// Configuración de la API
const API_BASE_URL = 'http://localhost:8080/todo';

// Estado de la aplicación
let tasks = [];
let currentFilter = 'all';
let editingTaskId = null;

// Elementos del DOM
const taskInput = document.getElementById('taskInput');
const addTaskBtn = document.getElementById('addTaskBtn');
const tasksContainer = document.getElementById('tasksContainer');
const emptyState = document.getElementById('emptyState');
const totalTasksEl = document.getElementById('totalTasks');
const completedTasksEl = document.getElementById('completedTasks');
const pendingTasksEl = document.getElementById('pendingTasks');
const filterButtons = document.querySelectorAll('.filter-btn');
const editModal = document.getElementById('editModal');
const editTaskInput = document.getElementById('editTaskInput');
const editTaskDone = document.getElementById('editTaskDone');
const saveTaskBtn = document.getElementById('saveTaskBtn');
const cancelEditBtn = document.getElementById('cancelEditBtn');
const closeModalBtn = document.querySelector('.close');
const toast = document.getElementById('toast');

// Event listeners
document.addEventListener('DOMContentLoaded', loadTasks);
addTaskBtn.addEventListener('click', addTask);
taskInput.addEventListener('keypress', (e) => {
    if (e.key === 'Enter') addTask();
});

filterButtons.forEach(btn => {
    btn.addEventListener('click', (e) => {
        setFilter(e.target.dataset.filter);
    });
});

saveTaskBtn.addEventListener('click', saveEditedTask);
cancelEditBtn.addEventListener('click', closeEditModal);
closeModalBtn.addEventListener('click', closeEditModal);

// Cerrar modal al hacer clic fuera
editModal.addEventListener('click', (e) => {
    if (e.target === editModal) closeEditModal();
});

// Funciones de la API
async function apiRequest(url, options = {}) {
    try {
        const response = await fetch(url, {
            headers: {
                'Content-Type': 'application/json',
                ...options.headers
            },
            ...options
        });

        if (!response.ok) {
            throw new Error(`HTTP error! status: ${response.status}`);
        }

        // Si es una respuesta DELETE, no intentar parsear JSON
        if (options.method === 'DELETE') {
            return null;
        }

        return await response.json();
    } catch (error) {
        console.error('API Error:', error);
        showToast('Error de conexión con el servidor', 'error');
        throw error;
    }
}

async function loadTasks() {
    try {
        showLoading(true);
        tasks = await apiRequest(API_BASE_URL);
        renderTasks();
        updateStats();
        showLoading(false);
    } catch (error) {
        showLoading(false);
        console.error('Error loading tasks:', error);
    }
}

async function addTask() {
    const title = taskInput.value.trim();

    if (!title) {
        showToast('Ingresa un título para la tarea', 'warning');
        taskInput.focus();
        return;
    }

    if (title.length > 255) {
        showToast('El título es demasiado largo (máximo 255 caracteres)', 'warning');
        return;
    }

    try {
        showLoading(true);
        const newTask = {
            title: title,
            done: false
        };

        const createdTask = await apiRequest(API_BASE_URL, {
            method: 'POST',
            body: JSON.stringify(newTask)
        });

        tasks.push(createdTask);
        taskInput.value = '';
        renderTasks();
        updateStats();
        showToast('Tarea agregada', 'success');
        showLoading(false);
    } catch (error) {
        showLoading(false);
        console.error('Error adding task:', error);
    }
}

async function toggleTask(id) {
    try {
        const task = tasks.find(t => t.id === id);
        if (!task) return;

        const updatedTask = {
            ...task,
            done: !task.done
        };

        await apiRequest(API_BASE_URL, {
            method: 'PUT',
            body: JSON.stringify(updatedTask)
        });

        task.done = !task.done;
        renderTasks();
        updateStats();
        showToast(`Tarea ${task.done ? 'completada' : 'marcada como pendiente'}`, 'success');
    } catch (error) {
        console.error('Error toggling task:', error);
    }
}

async function deleteTask(id) {
    if (!confirm('¿Estás seguro de que quieres eliminar esta tarea?')) {
        return;
    }

    try {
        await apiRequest(`${API_BASE_URL}/${id}`, {
            method: 'DELETE'
        });

        // Animación de eliminación
        const taskElement = document.querySelector(`[data-task-id="${id}"]`);
        if (taskElement) {
            taskElement.classList.add('removing');
            setTimeout(() => {
                tasks = tasks.filter(t => t.id !== id);
                renderTasks();
                updateStats();
                showToast('Tarea eliminada', 'success');
            }, 300);
        } else {
            tasks = tasks.filter(t => t.id !== id);
            renderTasks();
            updateStats();
            showToast('Tarea eliminada', 'success');
        }
    } catch (error) {
        console.error('Error deleting task:', error);
    }
}

function openEditModal(id) {
    const task = tasks.find(t => t.id === id);
    if (!task) return;

    editingTaskId = id;
    editTaskInput.value = task.title;
    editTaskDone.checked = task.done;
    editModal.style.display = 'block';
    editTaskInput.focus();
}

function closeEditModal() {
    editModal.style.display = 'none';
    editingTaskId = null;
    editTaskInput.value = '';
    editTaskDone.checked = false;
}

async function saveEditedTask() {
    const title = editTaskInput.value.trim();

    if (!title) {
        showToast('Por favor ingresa un título para la tarea', 'warning');
        editTaskInput.focus();
        return;
    }

    if (title.length > 255) {
        showToast('El título es demasiado largo (máximo 255 caracteres)', 'warning');
        return;
    }

    try {
        const task = tasks.find(t => t.id === editingTaskId);
        if (!task) return;

        const updatedTask = {
            ...task,
            title: title,
            done: editTaskDone.checked
        };

        await apiRequest(API_BASE_URL, {
            method: 'PUT',
            body: JSON.stringify(updatedTask)
        });

        // Actualizar en el array local
        Object.assign(task, updatedTask);

        closeEditModal();
        renderTasks();
        updateStats();
        showToast('Tarea actualizada', 'success');
    } catch (error) {
        console.error('Error updating task:', error);
    }
}

function renderTasks() {
    const filteredTasks = getFilteredTasks();

    if (filteredTasks.length === 0) {
        tasksContainer.style.display = 'none';
        emptyState.style.display = 'block';
        return;
    }

    tasksContainer.style.display = 'block';
    emptyState.style.display = 'none';

    tasksContainer.innerHTML = filteredTasks.map(task => `
        <div class="task-item ${task.done ? 'completed' : ''}" data-task-id="${task.id}">
            <input type="checkbox" class="task-checkbox"
                   ${task.done ? 'checked' : ''}
                   onchange="toggleTask(${task.id})">
            <div class="task-content">
                <div class="task-title">${escapeHtml(task.title)}</div>
            </div>
            <div class="task-actions">
                <button class="task-btn btn-success" onclick="openEditModal(${task.id})" title="Editar tarea">
                    <i class="fas fa-edit"></i>
                </button>
                <button class="task-btn btn-danger" onclick="deleteTask(${task.id})" title="Eliminar tarea">
                    <i class="fas fa-trash"></i>
                </button>
            </div>
        </div>
    `).join('');
}

function getFilteredTasks() {
    switch (currentFilter) {
        case 'completed':
            return tasks.filter(task => task.done);
        case 'pending':
            return tasks.filter(task => !task.done);
        default:
            return tasks;
    }
}

function setFilter(filter) {
    currentFilter = filter;

    // Actualizar botones de filtro
    filterButtons.forEach(btn => {
        btn.classList.toggle('active', btn.dataset.filter === filter);
    });

    renderTasks();
}

function updateStats() {
    const total = tasks.length;
    const completed = tasks.filter(task => task.done).length;
    const pending = total - completed;

    totalTasksEl.textContent = total;
    completedTasksEl.textContent = completed;
    pendingTasksEl.textContent = pending;
}

function showToast(message, type = 'success') {
    toast.textContent = message;
    toast.className = `toast ${type}`;
    toast.classList.add('show');

    setTimeout(() => {
        toast.classList.remove('show');
    }, 3000);
}

function showLoading(show) {
    if (show) {
        addTaskBtn.disabled = true;
        addTaskBtn.innerHTML = '<div class="spinner"></div> Cargando...';
    } else {
        addTaskBtn.disabled = false;
        addTaskBtn.innerHTML = '<i class="fas fa-plus"></i> Agregar';
    }
}

function escapeHtml(unsafe) {
    return unsafe
        .replace(/&/g, "&amp;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&#039;");
}

// Funciones globales para los event handlers inline
window.toggleTask = toggleTask;
window.deleteTask = deleteTask;
window.openEditModal = openEditModal;

// Manejar errores globales
window.addEventListener('error', (e) => {
    console.error('Global error:', e.error);
    showToast('Ha ocurrido un error inesperado', 'error');
});

// Auto-refresh cada 30 segundos (opcional)
// setInterval(loadTasks, 30000);
