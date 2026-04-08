/**
 * ShopHub — AI Outfit Studio (API-connected version)
 * Fetches outfit recommendations from Spring Boot API
 */

const OUTFIT_API = 'http://localhost:8080/api/outfit';

const outfitState = {
    photo:        null,
    photoBase64:  null,
    bodyType:     null,
    prefs:        ['Casual', 'Smart-Casual'],
    products:     [],
    filtered:     [],
};

// ── Modal Open/Close ───────────────────────────────────────────
function openOutfitStudio() {
    document.getElementById('outfitOverlay').classList.add('open');
    document.getElementById('outfitStudio').classList.add('open');
    document.body.style.overflow = 'hidden';
}

function closeOutfitStudio() {
    document.getElementById('outfitOverlay').classList.remove('open');
    document.getElementById('outfitStudio').classList.remove('open');
    document.body.style.overflow = '';
}

// ── Upload ─────────────────────────────────────────────────────
function handleOutfitUpload(event) {
    const file = event.target.files[0];
    if (!file) return;
    outfitState.photo = file;
    const reader = new FileReader();
    reader.onload = (e) => {
        outfitState.photoBase64 = e.target.result.split(',')[1];
        const img = document.getElementById('outfitPreviewImg');
        img.src = e.target.result;
        img.classList.add('show');
        document.getElementById('outfitUploadPlaceholder').style.display = 'none';
        document.getElementById('outfitUploadDone').classList.add('show');
        checkOutfitReady();
    };
    reader.readAsDataURL(file);
}

// Drag & drop
(function () {
    const zone = document.getElementById('outfitUploadZone');
    if (!zone) return;
    zone.addEventListener('dragover', e => { e.preventDefault(); zone.style.borderColor = '#6366f1'; });
    zone.addEventListener('dragleave', () => { zone.style.borderColor = ''; });
    zone.addEventListener('drop', e => {
        e.preventDefault(); zone.style.borderColor = '';
        const file = e.dataTransfer.files[0];
        if (file?.type.startsWith('image/')) {
            const dt = new DataTransfer();
            dt.items.add(file);
            document.getElementById('outfitPhotoInput').files = dt.files;
            handleOutfitUpload({ target: { files: [file] } });
        }
    });
})();

// ── Body Type ──────────────────────────────────────────────────
function selectOutfitBody(el) {
    document.querySelectorAll('.outfit-body-card').forEach(c => c.classList.remove('selected'));
    el.classList.add('selected');
    outfitState.bodyType = el.dataset.type;
    document.getElementById('ostep2').classList.add('active');
    checkOutfitReady();
}

function toggleOutfitPref(el) {
    el.classList.toggle('active');
    const pref = el.textContent.trim();
    const idx  = outfitState.prefs.indexOf(pref);
    if (idx > -1) outfitState.prefs.splice(idx, 1);
    else outfitState.prefs.push(pref);
}

function checkOutfitReady() {
    const ready = outfitState.photo && outfitState.bodyType;
    const btn   = document.getElementById('outfitGetBtn');
    const val   = document.getElementById('outfitValidation');
    if (btn) btn.disabled = !ready;
    if (val) val.style.display = ready ? 'none' : 'block';
}

// ── Run AI — fetches from API ─────────────────────────────────
async function runOutfitAI() {
    document.getElementById('outfitStep1').style.display = 'none';
    document.getElementById('outfitStep2').style.display = 'flex';
    document.getElementById('ostep2').classList.add('done');
    document.getElementById('ostep3').classList.add('active');

    // Animate loading steps
    for (const id of ['ols1','ols2','ols3','ols4']) {
        await new Promise(r => setTimeout(r, 650));
        document.getElementById(id)?.classList.add('done');
    }

    // ── Fetch recommendations from Spring Boot API ────────────────────────
    let products  = [];
    let aiAdvice  = '';

    try {
        const res  = await fetch(`${OUTFIT_API}/recommend`, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                bodyType:   outfitState.bodyType,
                stylePrefs: outfitState.prefs,
            })
        });
        const data = await res.json();
        if (data.success) {
            products  = data.data.products  || [];
            aiAdvice  = data.data.advice    || '';
        }
    } catch (err) {
        console.error('Outfit API error:', err);
        aiAdvice = 'Could not connect to the server. Please make sure the backend is running on port 8080.';
    }

    // ── If photo uploaded, enhance advice via Claude API ──────────────────
    if (outfitState.photoBase64 && aiAdvice) {
        try {
            const claudeRes = await fetch('https://api.anthropic.com/v1/messages', {
                method:  'POST',
                headers: { 'Content-Type': 'application/json' },
                body: JSON.stringify({
                    model:      'claude-sonnet-4-20250514',
                    max_tokens: 200,
                    messages: [{
                        role: 'user',
                        content: [
                            { type: 'image', source: { type: 'base64', media_type: 'image/jpeg', data: outfitState.photoBase64 } },
                            { type: 'text',  text: `You are a friendly expert fashion stylist at ShopHub. This person has a ${outfitState.bodyType} body type and prefers ${outfitState.prefs.join(', ')} style. In 2 sentences, give warm, specific, actionable style advice. Max 55 words.` }
                        ]
                    }]
                })
            });
            const claudeData = await claudeRes.json();
            if (claudeData.content?.[0]?.text) aiAdvice = claudeData.content[0].text;
        } catch (err) {
            console.warn('Claude API unavailable, using server advice:', err);
        }
    }

    outfitState.products = products;
    outfitState.filtered = [...products];

    await new Promise(r => setTimeout(r, 300));
    document.getElementById('outfitStep2').style.display = 'none';
    document.getElementById('outfitStep3').style.display = 'block';
    document.getElementById('ostep3').classList.add('done');

    document.getElementById('outfitAiText').textContent     = aiAdvice;
    document.getElementById('outfitBodyLabel').textContent  = outfitState.bodyType + ' Body Type';
    document.getElementById('outfitResultCount').textContent = products.length + ' outfits curated for you';

    renderOutfitProducts(outfitState.filtered);
}

// ── Render Products ────────────────────────────────────────────
function renderOutfitProducts(products) {
    const grid = document.getElementById('outfitProductsGrid');
    if (!products.length) {
        grid.innerHTML = `<div style="grid-column:1/-1;text-align:center;padding:3rem;color:#475569">No items in this category</div>`;
        return;
    }
    grid.innerHTML = products.map((p, i) => `
        <div class="outfit-product-card" style="animation-delay:${i*70}ms">
            <div class="outfit-card-img-wrap">
                <img src="${p.image}" alt="${p.name}" loading="lazy"
                     onerror="this.src='https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=500&h=650&fit=crop'">
                <span class="outfit-card-badge">${p.category.toUpperCase()}</span>
                <span class="outfit-card-match">${p.matchPercent}% Match</span>
            </div>
            <div class="outfit-card-body">
                <div class="outfit-card-brand">${p.brand}</div>
                <div class="outfit-card-name">${p.name}</div>
                <div class="outfit-card-style">${p.styleTip}</div>
                <div class="outfit-card-colors">
                    ${p.colors.map(c => `<div class="outfit-color-swatch" style="background:${c}" title="${c}"></div>`).join('')}
                </div>
                <div class="outfit-card-footer">
                    <div class="outfit-card-price">
                        ₹${p.price.toLocaleString('en-IN')}
                        ${p.originalPrice ? `<span class="orig">₹${p.originalPrice.toLocaleString('en-IN')}</span>` : ''}
                    </div>
                    <button class="outfit-add-btn ${isOutfitInCart(p.id) ? 'added' : ''}"
                            id="outfit-addbtn-${p.id}"
                            onclick="addOutfitToCart(event,'${p.id}')">
                        ${isOutfitInCart(p.id) ? '✓ Added' : '+ Add'}
                    </button>
                </div>
            </div>
        </div>
    `).join('');
}

function isOutfitInCart(outfitId) {
    return typeof cart !== 'undefined' && cart.some(c => c.id === outfitId);
}

// ── Add to cart via API ────────────────────────────────────────
async function addOutfitToCart(event, outfitId) {
    event.stopPropagation();
    const product = outfitState.products.find(p => p.id === outfitId);
    if (!product) return;

    try {
        const sessionId = localStorage.getItem('shopHubSessionId') || '';
        const res = await fetch('http://localhost:8080/api/cart/add', {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                sessionId,
                productId: outfitId,
                qty:       1,
                variants:  {}
            })
        });
        const data = await res.json();
        if (data.success) {
            if (typeof loadCartFromAPI === 'function') await loadCartFromAPI();
        }
    } catch (err) {
        console.warn('Add outfit to cart failed, using local cart:', err);
        if (typeof cart !== 'undefined' && typeof updateCartUI === 'function') {
            const existing = cart.find(c => c.id === outfitId);
            if (existing) existing.qty++;
            else cart.push({ id: outfitId, name: product.name, brand: product.brand, price: product.price, image: product.image, qty: 1, variants: {} });
            updateCartUI();
        }
    }

    const btn = document.getElementById('outfit-addbtn-' + outfitId);
    if (btn) { btn.textContent = '✓ Added'; btn.classList.add('added'); }
    if (typeof showToast === 'function') showToast(`✨ Added "${product.name}" to cart`);
}

// ── Filter ─────────────────────────────────────────────────────
function filterOutfits(cat, el) {
    document.querySelectorAll('.outfit-filter').forEach(c => c.classList.remove('active'));
    el.classList.add('active');
    const filtered = cat === 'all'
        ? [...outfitState.products]
        : outfitState.products.filter(p => p.category === cat);
    outfitState.filtered = filtered;
    renderOutfitProducts(filtered);
}

// ── Reset ──────────────────────────────────────────────────────
function resetOutfitStudio() {
    outfitState.photo = null;
    outfitState.photoBase64 = null;
    outfitState.bodyType = null;
    outfitState.products = [];
    outfitState.filtered = [];

    document.getElementById('outfitStep3').style.display = 'none';
    document.getElementById('outfitStep2').style.display = 'none';
    document.getElementById('outfitStep1').style.display = 'block';

    const img = document.getElementById('outfitPreviewImg');
    img.src = ''; img.classList.remove('show');
    document.getElementById('outfitUploadPlaceholder').style.display = '';
    document.getElementById('outfitUploadDone').classList.remove('show');
    document.getElementById('outfitPhotoInput').value = '';

    document.querySelectorAll('.outfit-body-card').forEach(c => c.classList.remove('selected'));
    ['ostep1','ostep2','ostep3'].forEach((id, i) => {
        const el = document.getElementById(id);
        el.classList.remove('done');
        if (i === 0) el.classList.add('active'); else el.classList.remove('active');
    });
    ['ols1','ols2','ols3','ols4'].forEach(id => document.getElementById(id)?.classList.remove('done'));
    document.querySelectorAll('.outfit-filter').forEach(f => f.classList.remove('active'));
    document.querySelector('.outfit-filter')?.classList.add('active');
    checkOutfitReady();
}

document.addEventListener('keydown', e => { if (e.key === 'Escape') closeOutfitStudio(); });
