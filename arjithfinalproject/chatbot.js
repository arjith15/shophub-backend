/**
 * ShopHub Chatbot — API-connected version
 * Sends user messages to Spring Boot chatbot endpoint
 */

const CHATBOT_API = 'http://localhost:8080/api/chatbot/message';

function addMessage(content, isUser = false) {
    const messagesContainer = document.getElementById('chatMessages');
    const messageDiv = document.createElement('div');
    messageDiv.className = `message ${isUser ? 'user-message' : 'bot-message'}`;

    const avatar = document.createElement('div');
    avatar.className = 'message-avatar';
    avatar.textContent = isUser ? 'You' : 'AI';

    const contentDiv = document.createElement('div');
    contentDiv.className = 'message-content';
    const p = document.createElement('p');
    p.textContent = content;
    contentDiv.appendChild(p);

    messageDiv.appendChild(avatar);
    messageDiv.appendChild(contentDiv);
    messagesContainer.appendChild(messageDiv);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

function addTypingIndicator() {
    const messagesContainer = document.getElementById('chatMessages');
    const div = document.createElement('div');
    div.className = 'message bot-message';
    div.id = 'typingIndicator';
    div.innerHTML = `
        <div class="message-avatar">AI</div>
        <div class="message-content"><p style="color:#64748b;font-style:italic">Typing…</p></div>
    `;
    messagesContainer.appendChild(div);
    messagesContainer.scrollTop = messagesContainer.scrollHeight;
}

function removeTypingIndicator() {
    document.getElementById('typingIndicator')?.remove();
}

async function handleUserMessage(input) {
    const trimmed = input.trim();
    if (!trimmed) return;

    addMessage(trimmed, true);
    addTypingIndicator();

    try {
        const res  = await fetch(CHATBOT_API, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body:    JSON.stringify({ message: trimmed })
        });
        const data = await res.json();
        removeTypingIndicator();
        addMessage(data.data?.reply || "Sorry, I couldn't get a response. Please try again.");
    } catch (err) {
        removeTypingIndicator();
        addMessage("⚠️ Could not connect to the server. Please make sure the backend is running on port 8080.");
        console.error('Chatbot API error:', err);
    }
}

function initChatbot() {
    const toggleBtn = document.getElementById('chatToggle');
    const closeBtn  = document.getElementById('chatClose');
    const container = document.getElementById('chatbotContainer');
    const input     = document.getElementById('chatInput');
    const sendBtn   = document.getElementById('chatSend');

    toggleBtn.addEventListener('click', () => { container.classList.add('open'); input.focus(); });
    closeBtn.addEventListener('click',  () => container.classList.remove('open'));

    sendBtn.addEventListener('click', () => {
        handleUserMessage(input.value);
        input.value = '';
    });

    input.addEventListener('keypress', e => {
        if (e.key === 'Enter') {
            handleUserMessage(input.value);
            input.value = '';
        }
    });
}

window.handleUserMessage = handleUserMessage;
document.addEventListener('DOMContentLoaded', initChatbot);
