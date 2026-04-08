/**
 * ShopHub Payment Page — API-connected version
 * Loads cart from server using sessionId, submits order to API
 * No sensitive data stored in localStorage
 */

const PAYMENT_API = 'http://localhost:8080/api';

const UPI_HANDLES = ['shophub@axisbank','shophub@oksbi','shophub@ybl','shophub@paytm','shophub@icici','shophub@hdfcbank'];

function getSessionId() {
    // Try sessionStorage first (set by app.js on checkout), then localStorage
    return sessionStorage.getItem('shopHubSessionId') || localStorage.getItem('shopHubSessionId') || '';
}

function inr(amount) {
    return '₹' + Number(amount).toLocaleString('en-IN');
}

function getRandomUpiHandle() {
    return UPI_HANDLES[Math.floor(Math.random() * UPI_HANDLES.length)];
}

function loadNewQr(amount) {
    const handle    = getRandomUpiHandle();
    const upiString = `upi://pay?pa=${handle}&pn=ShopHub&am=${amount}&cu=INR&tn=ShopHubOrder`;
    const qrUrl     = `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encodeURIComponent(upiString)}&bgcolor=1e293b&color=ffffff&margin=10`;
    const img       = document.getElementById('upiQrImg');
    const handleEl  = document.getElementById('qrUpiHandle');
    if (img) { img.style.opacity = '0.4'; img.src = qrUrl; img.onload = () => { img.style.opacity = '1'; }; }
    if (handleEl) handleEl.textContent = handle;
}

document.addEventListener('DOMContentLoaded', async () => {
    const sessionId  = getSessionId();
    const content    = document.getElementById('paymentContent');
    const emptyState = document.getElementById('emptyState');

    if (!sessionId) {
        content.innerHTML = '';
        emptyState.style.display = 'block';
        return;
    }

    // ── Load cart from API ──────────────────────────────────────────────────
    let items = [], total = 0;
    try {
        const res  = await fetch(`${PAYMENT_API}/cart/${sessionId}`);
        const data = await res.json();
        if (data.success && data.data.items?.length) {
            items = data.data.items;
            total = data.data.total;
        } else {
            content.innerHTML = '';
            emptyState.style.display = 'block';
            return;
        }
    } catch (err) {
        console.error('Failed to load cart:', err);
        content.innerHTML = '<p style="color:#fca5a5;text-align:center;padding:2rem">⚠️ Could not connect to server. Please make sure the backend is running.</p>';
        return;
    }

    // ── Render payment form ─────────────────────────────────────────────────
    content.innerHTML = `
        <style>
            .upi-sub-options { display:flex; gap:.75rem; margin-bottom:1.25rem; }
            .upi-sub-btn { flex:1; padding:.75rem 1rem; border-radius:10px; border:2px solid rgba(99,102,241,.3); background:rgba(99,102,241,.08); color:#94a3b8; font-size:.9rem; font-weight:600; cursor:pointer; transition:all .2s; text-align:center; }
            .upi-sub-btn.active { border-color:#6366f1; background:rgba(99,102,241,.22); color:#c7d2fe; }
            .qr-wrapper { display:flex; flex-direction:column; align-items:center; gap:.85rem; background:rgba(99,102,241,.07); border:1px solid rgba(99,102,241,.25); border-radius:14px; padding:1.5rem; margin-top:.5rem; }
            .qr-img { width:200px; height:200px; border-radius:12px; border:3px solid #6366f1; transition:opacity .3s; background:#1e293b; }
            .qr-note { text-align:center; color:#94a3b8; font-size:.85rem; line-height:1.7; margin:0; }
            .qr-timer { font-size:.78rem; color:#64748b; margin:0; }
            .qr-refresh-btn { background:rgba(99,102,241,.15); border:1px solid rgba(99,102,241,.4); color:#818cf8; padding:.45rem 1.1rem; border-radius:8px; font-size:.82rem; font-weight:600; cursor:pointer; transition:all .2s; }
            .qr-refresh-btn:hover { background:rgba(99,102,241,.3); }
        </style>

        <h2 class="payment-title">Order Summary</h2>
        <div class="payment-order-items" id="orderItems"></div>
        <div class="payment-total-row">
            <span>Total:</span>
            <strong id="orderTotal">${inr(total)}</strong>
        </div>

        <form class="payment-form" id="paymentForm">
            <h3>Personal Details</h3>
            <div class="form-group"><label for="name">Full Name</label><input type="text" id="name" required placeholder="John Doe"></div>
            <div class="form-group"><label for="email">Email</label><input type="email" id="email" required placeholder="john@example.com"></div>
            <div class="form-group"><label for="phone">Phone</label><input type="tel" id="phone" required placeholder="+91 9876543210"></div>

            <h3>Delivery Address</h3>
            <div class="form-group"><label for="addressLine1">Address Line 1</label><input type="text" id="addressLine1" required placeholder="House No., Street Name"></div>
            <div class="form-group"><label for="addressLine2">Address Line 2 <span class="optional">(optional)</span></label><input type="text" id="addressLine2" placeholder="Apartment, Suite, Floor"></div>
            <div class="form-row">
                <div class="form-group"><label for="city">City</label><input type="text" id="city" required placeholder="Mumbai"></div>
                <div class="form-group"><label for="state">State</label><input type="text" id="state" required placeholder="Maharashtra"></div>
            </div>
            <div class="form-row">
                <div class="form-group"><label for="pincode">Pincode</label><input type="text" id="pincode" required placeholder="400001" maxlength="10"></div>
                <div class="form-group"><label for="country">Country</label><input type="text" id="country" required placeholder="India" value="India"></div>
            </div>

            <h3>Payment Method</h3>
            <div class="payment-methods">
                <label class="payment-method-option"><input type="radio" name="paymentMode" value="card" checked><span class="method-label">💳 Card</span></label>
                <label class="payment-method-option"><input type="radio" name="paymentMode" value="upi"><span class="method-label">📱 UPI</span></label>
                <label class="payment-method-option"><input type="radio" name="paymentMode" value="cod"><span class="method-label">💵 Cash on Delivery</span></label>
            </div>

            <!-- CARD -->
            <div id="cardFields" class="payment-mode-fields">
                <div class="form-group"><label for="card">Card Number</label><input type="text" id="card" placeholder="4242 4242 4242 4242" maxlength="19" required></div>
                <div class="form-row">
                    <div class="form-group"><label for="expiry">Expiry</label><input type="text" id="expiry" placeholder="MM/YY" maxlength="5" required></div>
                    <div class="form-group"><label for="cvv">CVV</label><input type="text" id="cvv" placeholder="123" maxlength="4" required></div>
                </div>
            </div>

            <!-- UPI -->
            <div id="upiFields" class="payment-mode-fields" style="display:none;">
                <div class="upi-sub-options">
                    <button type="button" class="upi-sub-btn active" id="btnQr">📷 Scan QR Code</button>
                    <button type="button" class="upi-sub-btn" id="btnUpiId">📝 Enter UPI ID</button>
                </div>
                <div id="upiQrSection">
                    <div class="qr-wrapper">
                        <img id="upiQrImg" src="" alt="UPI QR Code" class="qr-img">
                        <p class="qr-note">Scan using PhonePe, GPay, Paytm or any UPI app<br>
                            <span id="qrUpiHandle" style="color:#818cf8;font-weight:700;font-size:.95rem;"></span></p>
                        <p class="qr-timer">QR refreshes in <span id="qrCountdown">60</span>s</p>
                        <button type="button" class="qr-refresh-btn" id="qrRefreshBtn">🔄 Generate New QR</button>
                    </div>
                </div>
                <div id="upiIdSection" style="display:none;">
                    <div class="form-group"><label for="upiId">UPI ID</label><input type="text" id="upiId" placeholder="yourname@upi"></div>
                </div>
            </div>

            <!-- COD -->
            <div id="codFields" class="payment-mode-fields" style="display:none;">
                <p class="cod-note">Pay when your order is delivered. No advance payment required.</p>
            </div>

            <button type="submit" class="pay-btn" id="payBtn">Pay ${inr(total)}</button>
        </form>
    `;

    // Render order items
    const orderItemsContainer = document.getElementById('orderItems');
    items.forEach(item => {
        const div = document.createElement('div');
        div.className = 'payment-order-item';
        div.innerHTML = `
            <img src="${item.image}" alt="${item.name}">
            <div><span class="payment-item-name">${item.name}</span><span class="payment-item-qty">× ${item.qty}</span></div>
            <span class="payment-item-price">${inr(item.price * item.qty)}</span>
        `;
        orderItemsContainer.appendChild(div);
    });

    // Auto-fill saved address (just address fields, no payment data)
    const savedAddress = JSON.parse(localStorage.getItem('shopHubSavedAddress') || 'null');
    if (savedAddress) {
        ['name','email','phone','addressLine1','addressLine2','city','state','pincode','country'].forEach(id => {
            const el = document.getElementById(id);
            if (el && savedAddress[id]) el.value = savedAddress[id];
        });
    }

    const form       = document.getElementById('paymentForm');
    const payBtn     = document.getElementById('payBtn');
    const cardFields = document.getElementById('cardFields');
    const upiFields  = document.getElementById('upiFields');
    const codFields  = document.getElementById('codFields');

    // ── QR countdown ────────────────────────────────────────────────────────
    let qrTimer = null;

    function startQrCountdown() {
        clearInterval(qrTimer);
        let secs = 60;
        const el = document.getElementById('qrCountdown');
        if (el) el.textContent = secs;
        qrTimer = setInterval(() => {
            secs--;
            const countEl = document.getElementById('qrCountdown');
            if (countEl) countEl.textContent = secs;
            if (secs <= 0) { clearInterval(qrTimer); loadNewQr(total); startQrCountdown(); }
        }, 1000);
    }

    function showQrSection() {
        document.getElementById('upiQrSection').style.display   = 'block';
        document.getElementById('upiIdSection').style.display   = 'none';
        document.getElementById('btnQr').classList.add('active');
        document.getElementById('btnUpiId').classList.remove('active');
        document.getElementById('upiId').required = false;
        loadNewQr(total);
        startQrCountdown();
    }

    function showUpiIdSection() {
        document.getElementById('upiQrSection').style.display   = 'none';
        document.getElementById('upiIdSection').style.display   = 'block';
        document.getElementById('btnUpiId').classList.add('active');
        document.getElementById('btnQr').classList.remove('active');
        document.getElementById('upiId').required = true;
        clearInterval(qrTimer);
    }

    document.getElementById('btnQr').addEventListener('click', showQrSection);
    document.getElementById('btnUpiId').addEventListener('click', showUpiIdSection);
    document.getElementById('qrRefreshBtn').addEventListener('click', () => { loadNewQr(total); startQrCountdown(); });

    // ── Payment mode switch ──────────────────────────────────────────────────
    form.querySelectorAll('input[name="paymentMode"]').forEach(radio => {
        radio.addEventListener('change', () => {
            const mode = form.querySelector('input[name="paymentMode"]:checked').value;
            cardFields.style.display = mode === 'card' ? 'block' : 'none';
            upiFields.style.display  = mode === 'upi'  ? 'block' : 'none';
            codFields.style.display  = mode === 'cod'  ? 'block' : 'none';
            document.getElementById('card').required   = mode === 'card';
            document.getElementById('expiry').required = mode === 'card';
            document.getElementById('cvv').required    = mode === 'card';
            if (mode === 'upi') showQrSection(); else { clearInterval(qrTimer); document.getElementById('upiId').required = false; }
            payBtn.textContent = mode === 'cod' ? `Place Order (${inr(total)})` : `Pay ${inr(total)}`;
        });
    });

    // ── Form submit — sends to API ───────────────────────────────────────────
    form.addEventListener('submit', async (e) => {
        e.preventDefault();
        const mode = form.querySelector('input[name="paymentMode"]:checked').value;

        if (mode === 'card') {
            const card = document.getElementById('card').value.trim();
            if (!card || card.replace(/\s/g,'').length < 16) { alert('Please enter a valid card number'); return; }
        }
        if (mode === 'upi') {
            const isQr = document.getElementById('upiQrSection').style.display !== 'none';
            if (!isQr) {
                const upiId = document.getElementById('upiId').value.trim();
                if (!upiId || !upiId.includes('@')) { alert('Please enter a valid UPI ID (e.g., name@upi)'); return; }
            }
        }

        // Save address for next time (only address — no card/payment details)
        localStorage.setItem('shopHubSavedAddress', JSON.stringify({
            name:         document.getElementById('name').value.trim(),
            email:        document.getElementById('email').value.trim(),
            phone:        document.getElementById('phone').value.trim(),
            addressLine1: document.getElementById('addressLine1').value.trim(),
            addressLine2: document.getElementById('addressLine2').value.trim(),
            city:         document.getElementById('city').value.trim(),
            state:        document.getElementById('state').value.trim(),
            pincode:      document.getElementById('pincode').value.trim(),
            country:      document.getElementById('country').value.trim(),
        }));

        clearInterval(qrTimer);
        payBtn.disabled = true;
        payBtn.innerHTML = '<span class="checkout-loading"></span> Processing...';

        // ── Send order to Spring Boot API ────────────────────────────────────
        try {
            const res  = await fetch(`${PAYMENT_API}/orders/checkout`, {
                method:  'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    sessionId:    sessionId,
                    customerName: document.getElementById('name').value.trim(),
                    customerEmail:document.getElementById('email').value.trim(),
                    customerPhone:document.getElementById('phone').value.trim(),
                    addressLine1: document.getElementById('addressLine1').value.trim(),
                    addressLine2: document.getElementById('addressLine2').value.trim(),
                    city:         document.getElementById('city').value.trim(),
                    state:        document.getElementById('state').value.trim(),
                    pincode:      document.getElementById('pincode').value.trim(),
                    country:      document.getElementById('country').value.trim(),
                    paymentMode:  mode,
                })
            });

            const data = await res.json();
            if (data.success) {
                sessionStorage.removeItem('shopHubSessionId');
                setTimeout(() => {
                    alert(data.data.message + `\n\nOrder ID: ${data.data.orderId}`);
                    window.location.href = 'index.html';
                }, 1500);
            } else {
                payBtn.disabled = false;
                payBtn.textContent = mode === 'cod' ? `Place Order (${inr(total)})` : `Pay ${inr(total)}`;
                alert('Order failed: ' + data.message);
            }
        } catch (err) {
            console.error('Checkout failed:', err);
            payBtn.disabled = false;
            payBtn.textContent = `Pay ${inr(total)}`;
            alert('⚠️ Could not connect to server. Please make sure the backend is running on port 8080.');
        }
    });
});
