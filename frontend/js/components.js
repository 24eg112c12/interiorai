/**
 * InteriorAI - Reusable UI Components & Helpers
 */

function formatINR(amount) {
  if (amount == null || isNaN(amount)) return '₹0';
  const num = Math.round(Number(amount));
  return '₹' + num.toLocaleString('en-IN');
}

function formatDate(dateStr) {
  if (!dateStr) return 'N/A';
  try {
    const d = new Date(dateStr);
    return d.toLocaleDateString('en-IN', { day: 'numeric', month: 'short', year: 'numeric' });
  } catch {
    return dateStr;
  }
}

function showToast(message, type = 'info') {
  let container = document.getElementById('toast-container');
  if (!container) {
    container = document.createElement('div');
    container.id = 'toast-container';
    document.body.appendChild(container);
  }

  const toast = document.createElement('div');
  toast.className = `toast toast-${type}`;

  let icon = 'ℹ️';
  if (type === 'success') icon = '✓';
  if (type === 'error') icon = '⚠️';
  if (type === 'warning') icon = '⚡';

  toast.innerHTML = `
    <span class="toast-icon">${icon}</span>
    <span class="toast-msg">${message}</span>
  `;

  container.appendChild(toast);

  setTimeout(() => {
    toast.style.opacity = '0';
    toast.style.transform = 'translateY(10px)';
    toast.style.transition = 'all 0.3s ease';
    setTimeout(() => toast.remove(), 300);
  }, 4000);
}

/**
 * Interactive Before & After Comparison Slider
 */
function initBeforeAfterSlider(containerElement) {
  if (!containerElement) return;

  const wrapper = containerElement.querySelector('.ba-img-before-wrapper');
  const handle = containerElement.querySelector('.ba-handle');
  if (!wrapper || !handle) return;

  let isDragging = false;

  const setSliderPos = (clientX) => {
    const rect = containerElement.getBoundingClientRect();
    let posX = clientX - rect.left;
    if (posX < 0) posX = 0;
    if (posX > rect.width) posX = rect.width;

    const percentage = (posX / rect.width) * 100;
    wrapper.style.width = `${percentage}%`;
    handle.style.left = `${percentage}%`;
  };

  const onStart = (e) => {
    isDragging = true;
    const clientX = e.touches ? e.touches[0].clientX : e.clientX;
    setSliderPos(clientX);
  };

  const onMove = (e) => {
    if (!isDragging) return;
    const clientX = e.touches ? e.touches[0].clientX : e.clientX;
    setSliderPos(clientX);
  };

  const onEnd = () => {
    isDragging = false;
  };

  containerElement.addEventListener('mousedown', onStart);
  window.addEventListener('mousemove', onMove);
  window.addEventListener('mouseup', onEnd);

  containerElement.addEventListener('touchstart', onStart, { passive: true });
  window.addEventListener('touchmove', onMove, { passive: true });
  window.addEventListener('touchend', onEnd);
}
