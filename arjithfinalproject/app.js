/**
 * ShopHub — app.js  (API-connected version)
 * Cart, products, filters, search — all backed by Spring Boot REST API
 */

const API = 'http://localhost:8080/api';

// ── Session ID (only harmless ID stored in localStorage, not cart data) ───────
function getSessionId() {
    let sid = localStorage.getItem('shopHubSessionId');
    if (!sid) {
        sid = 'sess_' + Math.random().toString(36).substr(2, 9) + '_' + Date.now();
        localStorage.setItem('shopHubSessionId', sid);
    }
    return sid;
}

let priceChart = null;
let cart = [];           // local cache, synced with API
let PRODUCTS   = [];     // fetched from API on load
let CATEGORIES = [];     // fetched from API on load

// ── Filter state ──────────────────────────────────────────────────────────────
let activeCategory = 'all';
let activeFilters  = {};

// ── INR formatter ─────────────────────────────────────────────────────────────
function inr(amount) {
    return '₹' + Number(amount).toLocaleString('en-IN');
}

// ── Init ──────────────────────────────────────────────────────────────────────
document.addEventListener('DOMContentLoaded', async () => {
    updateAuthUI();                // ← Auth UI: shows greeting or Sign In link
    await loadProductsFromAPI();
    await loadCartFromAPI();
    renderProducts(PRODUCTS);
    renderQuickButtons();
    document.getElementById('openChatBtn').addEventListener('click', openChatbot);
    initPriceModal();
    initCart();
    initSearch();
    document.getElementById('pdClose').addEventListener('click', closeProductDetail);
    document.getElementById('pdOverlay').addEventListener('click', closeProductDetail);
});

// ── Fetch products + categories from API ──────────────────────────────────────
async function loadProductsFromAPI() {
    try {
        const [prodRes, catRes] = await Promise.all([
            fetch(`${API}/products`),
            fetch(`${API}/categories`)
        ]);
        const prodData = await prodRes.json();
        const catData  = await catRes.json();
        PRODUCTS   = prodData.data  || [];
        CATEGORIES = catData.data   || [];
    } catch (err) {
        console.error('Failed to load products from API:', err);
        showToast('⚠️ Could not connect to server. Using offline mode.');
        // Fallback: use products.js globals if available
        if (typeof window.PRODUCTS !== 'undefined') PRODUCTS = window.PRODUCTS;
        if (typeof window.CATEGORIES !== 'undefined') CATEGORIES = window.CATEGORIES;
    }
}

// ── Load existing cart from API on page load ──────────────────────────────────
async function loadCartFromAPI() {
    try {
        const res  = await fetch(`${API}/cart/${getSessionId()}`);
        const data = await res.json();
        if (data.success) {
            cart = data.data.items || [];
            updateCartUI();
        }
    } catch (err) {
        console.error('Failed to load cart from API:', err);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// RENDER PRODUCTS
// ─────────────────────────────────────────────────────────────────────────────
function renderProducts(products) {
    const grid = document.getElementById('productList');
    grid.innerHTML = '';

    const categoryIds = [...new Set(products.map(p => p.category))];
    const categories  = CATEGORIES.filter(c => categoryIds.includes(c.id));

    categories.forEach(category => {
        const catProducts = products.filter(p => p.category === category.id);
        if (!catProducts.length) return;

        const section = document.createElement('div');
        section.className = 'category-block';
        section.setAttribute('data-category', category.id);
        section.innerHTML = `<h3 class="category-title">${category.icon} ${category.label}</h3>
            <div class="product-cards" data-category="${category.id}"></div>`;

        const cardsContainer = section.querySelector('.product-cards');
        catProducts.forEach(product => cardsContainer.appendChild(buildCard(product)));
        grid.appendChild(section);
    });

    document.getElementById('noResults').style.display = products.length ? 'none' : 'block';
}

function buildCard(product) {
    const card   = document.createElement('div');
    card.className = 'product-card';
    const imgUrl = product.image || `https://picsum.photos/seed/${product.id}/400/400`;

    card.innerHTML = `
        <div class="product-image-wrap" style="cursor:pointer" title="Click to view details">
            <img src="${imgUrl}" alt="${product.name}" class="product-image" loading="lazy"
                 onerror="this.src='https://picsum.photos/seed/${product.id}/400/400'">
            ${product.brand ? `<span class="product-brand-badge">${product.brand}</span>` : ''}
            <div class="product-view-overlay">👁 View Details</div>
        </div>
        <div class="product-info" style="cursor:pointer" title="Click to view details">
            <h4 class="product-name">${product.name}</h4>
            <p class="product-price">${inr(product.price)}</p>
        </div>
        ${buildVariants(product)}
        <div class="product-card-buttons">
            <button class="view-details-btn">📋 View Details</button>
            <div class="product-actions">
                <button class="add-to-cart-btn">🛒 Add to Cart</button>
                <button class="buy-now-btn">⚡ Buy Now</button>
            </div>
            <div class="product-card-row3">
                <button class="price-graph-btn" title="View price history">
                    <svg viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2">
                        <path d="M3 3v18h18"/><path d="M18 17V9"/><path d="M13 17V5"/><path d="M8 17v-3"/>
                    </svg>
                    Price History
                </button>
                <button class="ask-chat-btn" data-product="${product.name}">💬 How to use?</button>
            </div>
        </div>
    `;

    const badge = card.querySelector('.product-brand-badge');
    if (badge) {
        badge.style.cssText = 'position:absolute;top:8px;left:8px;background:rgba(0,0,0,.65);color:#e0e7ff;font-size:.65rem;font-weight:700;padding:.2rem .55rem;border-radius:6px;backdrop-filter:blur(4px);';
    }

    card.querySelectorAll('.variant-pill').forEach(pill => {
        pill.addEventListener('click', () => {
            const row = pill.closest('.variant-pills');
            row.querySelectorAll('.variant-pill').forEach(p => p.classList.remove('selected'));
            pill.classList.add('selected');
        });
    });

    card.querySelector('.product-image-wrap').addEventListener('click', () => openProductDetail(product));
    card.querySelector('.product-info').addEventListener('click',       () => openProductDetail(product));
    card.querySelector('.view-details-btn').addEventListener('click',   () => openProductDetail(product));
    card.querySelector('.ask-chat-btn').addEventListener('click',       () => openChatbotWithProduct(product.name));
    card.querySelector('.price-graph-btn').addEventListener('click',    () => showPriceGraph(product));
    card.querySelector('.add-to-cart-btn').addEventListener('click',    () => addToCart(product, card));
    card.querySelector('.buy-now-btn').addEventListener('click',        () => handleBuyNow(product, card));

    return card;
}

function buildVariants(product) {
    const rows = [];
    if (product.sizes?.length) {
        const pills = product.sizes.map((s,i) =>
            `<span class="variant-pill${i===0?' selected':''}" data-type="size" data-val="${s}">${s}</span>`
        ).join('');
        rows.push(`<div class="variant-row"><span class="variant-label">Size</span><div class="variant-pills">${pills}</div></div>`);
    }
    if (product.colors?.length) {
        const pills = product.colors.map((c,i) =>
            `<span class="variant-pill${i===0?' selected':''}" data-type="color" data-val="${c}">${c}</span>`
        ).join('');
        rows.push(`<div class="variant-row"><span class="variant-label">Color</span><div class="variant-pills">${pills}</div></div>`);
    }
    if (product.storage?.length) {
        const pills = product.storage.map((s,i) =>
            `<span class="variant-pill${i===0?' selected':''}" data-type="storage" data-val="${s}">${s}</span>`
        ).join('');
        rows.push(`<div class="variant-row"><span class="variant-label">Storage</span><div class="variant-pills">${pills}</div></div>`);
    }
    if (product.ram?.length) {
        const pills = product.ram.map((r,i) =>
            `<span class="variant-pill${i===0?' selected':''}" data-type="ram" data-val="${r}">${r}</span>`
        ).join('');
        rows.push(`<div class="variant-row"><span class="variant-label">RAM</span><div class="variant-pills">${pills}</div></div>`);
    }
    return rows.length ? `<div class="product-variants">${rows.join('')}</div>` : '';
}

// ─────────────────────────────────────────────────────────────────────────────
// CATEGORY FILTER
// ─────────────────────────────────────────────────────────────────────────────
function filterByCategory(cat, el) {
    activeCategory = cat;
    activeFilters  = {};
    document.querySelectorAll('.cat-pill').forEach(p => p.classList.remove('active-cat'));
    if (el) el.classList.add('active-cat');
    buildFilterDropdowns(cat);
    applyFilters();
}

function buildFilterDropdowns(cat) {
    const container = document.getElementById('filterDropdowns');
    container.innerHTML = '';
    const pool  = cat === 'all' ? PRODUCTS : PRODUCTS.filter(p => p.category === cat);
    const uniq  = arr => [...new Set(arr.filter(Boolean))].sort();
    const brands   = uniq(pool.map(p => p.brand));
    const prices   = ['Under ₹2,000','₹2,000–₹10,000','₹10,000–₹50,000','₹50,000–₹1,00,000','Above ₹1,00,000'];
    const sizes    = uniq(pool.flatMap(p => p.sizes   || []));
    const colors   = uniq(pool.flatMap(p => p.colors  || []));
    const storages = uniq(pool.flatMap(p => p.storage || []));
    const rams     = uniq(pool.flatMap(p => p.ram     || []));

    const makeSelect = (id, label, options) => {
        if (!options.length) return;
        const sel = document.createElement('select');
        sel.className = 'filter-select';
        sel.id = id;
        sel.innerHTML = `<option value="">── ${label} ──</option>` +
            options.map(o => `<option value="${o}">${o}</option>`).join('');
        sel.addEventListener('change', () => {
            if (sel.value) activeFilters[id] = sel.value;
            else delete activeFilters[id];
            applyFilters();
            renderActiveFilterTags();
        });
        container.appendChild(sel);
    };

    makeSelect('brand',   'Brand',   brands);
    makeSelect('price',   'Price',   prices);
    if (sizes.length)    makeSelect('size',    'Size',    sizes);
    if (colors.length)   makeSelect('color',   'Color',   colors);
    if (storages.length) makeSelect('storage', 'Storage', storages);
    if (rams.length)     makeSelect('ram',     'RAM',     rams);
}

function applyFilters() {
    let pool = activeCategory === 'all' ? PRODUCTS : PRODUCTS.filter(p => p.category === activeCategory);
    if (activeFilters.brand)   pool = pool.filter(p => p.brand === activeFilters.brand);
    if (activeFilters.size)    pool = pool.filter(p => p.sizes?.includes(activeFilters.size));
    if (activeFilters.color)   pool = pool.filter(p => p.colors?.includes(activeFilters.color));
    if (activeFilters.storage) pool = pool.filter(p => p.storage?.includes(activeFilters.storage));
    if (activeFilters.ram)     pool = pool.filter(p => p.ram?.includes(activeFilters.ram));

    if (activeFilters.price) {
        const pr = activeFilters.price;
        pool = pool.filter(p => {
            if (pr === 'Under ₹2,000')          return p.price < 2000;
            if (pr === '₹2,000–₹10,000')        return p.price >= 2000  && p.price <= 10000;
            if (pr === '₹10,000–₹50,000')       return p.price > 10000  && p.price <= 50000;
            if (pr === '₹50,000–₹1,00,000')     return p.price > 50000  && p.price <= 100000;
            if (pr === 'Above ₹1,00,000')        return p.price > 100000;
            return true;
        });
    }
    renderProducts(pool);
    document.getElementById('filterResetBtn').style.display = Object.keys(activeFilters).length ? 'block' : 'none';
    document.getElementById('productList').scrollIntoView({ behavior:'smooth', block:'start' });
}

function renderActiveFilterTags() {
    const container = document.getElementById('activeFilters');
    container.innerHTML = '';
    Object.entries(activeFilters).forEach(([key, val]) => {
        const tag = document.createElement('div');
        tag.className = 'filter-tag';
        tag.innerHTML = `<span>${key}: ${val}</span><span class="filter-tag-remove" title="Remove">✕</span>`;
        tag.querySelector('.filter-tag-remove').addEventListener('click', () => {
            delete activeFilters[key];
            const sel = document.getElementById(key);
            if (sel) sel.value = '';
            applyFilters();
            renderActiveFilterTags();
        });
        container.appendChild(tag);
    });
}

function resetAllFilters() {
    activeFilters = {};
    document.querySelectorAll('.filter-select').forEach(s => s.value = '');
    document.getElementById('activeFilters').innerHTML = '';
    document.getElementById('filterResetBtn').style.display = 'none';
    applyFilters();
}

// ─────────────────────────────────────────────────────────────────────────────
// PRICE HISTORY CHART — fetched from API
// ─────────────────────────────────────────────────────────────────────────────
function initPriceModal() {
    const modal = document.getElementById('priceModal');
    document.getElementById('priceModalClose').addEventListener('click', () => modal.classList.remove('open'));
    modal.addEventListener('click', e => { if (e.target === modal) modal.classList.remove('open'); });
}

async function showPriceGraph(product) {
    document.getElementById('priceModalTitle').textContent = `${product.name} — Price History`;
    document.getElementById('priceModal').classList.add('open');

    try {
        const res  = await fetch(`${API}/products/${product.id}/price-history`);
        const data = await res.json();
        const history = data.data.history || [];
        const labels  = history.map(p => p.label);
        const values  = history.map(p => p.price);

        const ctx = document.getElementById('priceChartCanvas').getContext('2d');
        if (priceChart) priceChart.destroy();
        priceChart = new Chart(ctx, {
            type: 'line',
            data: {
                labels,
                datasets: [{
                    label: 'Price (₹)', data: values,
                    borderColor: '#6366f1', backgroundColor: 'rgba(99,102,241,.1)',
                    fill: true, tension: .3
                }]
            },
            options: {
                responsive: true,
                plugins: {
                    legend: { display: false },
                    tooltip: { callbacks: { label: ctx => '₹' + ctx.raw.toLocaleString('en-IN') } }
                },
                scales: {
                    y: {
                        beginAtZero: false,
                        grid: { color: 'rgba(255,255,255,.1)' },
                        ticks: { color: '#94a3b8', callback: v => '₹' + v.toLocaleString('en-IN') }
                    },
                    x: { grid: { color: 'rgba(255,255,255,.1)' }, ticks: { color: '#94a3b8' } }
                }
            }
        });
    } catch (err) {
        console.error('Failed to fetch price history:', err);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// CART — all operations go to API, local array is a display cache
// ─────────────────────────────────────────────────────────────────────────────
function getSelectedVariants(card) {
    const variants = {};
    card.querySelectorAll('.variant-pill.selected').forEach(pill => {
        variants[pill.dataset.type] = pill.dataset.val;
    });
    return variants;
}

function initCart() {
    document.getElementById('cartHeaderBtn').addEventListener('click', openCart);
    document.getElementById('cartClose').addEventListener('click', closeCart);
    document.getElementById('cartOverlay').addEventListener('click', closeCart);
    document.getElementById('checkoutBtn').addEventListener('click', handleCheckout);
}

async function addToCart(product, card) {
    const variants = card ? getSelectedVariants(card) : {};
    try {
        const res  = await fetch(`${API}/cart/add`, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({
                sessionId: getSessionId(),
                productId: product.id,
                qty:       1,
                variants
            })
        });
        const data = await res.json();
        if (data.success) {
            await loadCartFromAPI();   // refresh cart from server
            const variantText = Object.values(variants).join(', ');
            showToast(`Added "${product.name}"${variantText ? ' ('+variantText+')' : ''} to cart`);
        }
    } catch (err) {
        console.error('Add to cart failed:', err);
        showToast('⚠️ Failed to add to cart. Is the server running?');
    }
}

async function removeFromCart(itemKey) {
    try {
        await fetch(`${API}/cart/${getSessionId()}/remove/${encodeURIComponent(itemKey)}`, {
            method: 'DELETE'
        });
        await loadCartFromAPI();
    } catch (err) {
        console.error('Remove from cart failed:', err);
    }
}

async function updateCartQty(itemKey, qty) {
    try {
        await fetch(`${API}/cart/update`, {
            method:  'PUT',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ sessionId: getSessionId(), itemKey, qty })
        });
        await loadCartFromAPI();
    } catch (err) {
        console.error('Update cart qty failed:', err);
    }
}

function updateCartUI() {
    const totalItems = cart.reduce((s, i) => s + i.qty, 0);
    const totalPrice = cart.reduce((s, i) => s + i.price * i.qty, 0);

    document.getElementById('cartBadge').textContent    = totalItems;
    document.getElementById('cartBadge').style.display  = totalItems ? 'flex' : 'none';
    document.getElementById('cartTotal').textContent    = inr(totalPrice);
    document.getElementById('cartFooter').style.display = cart.length ? 'block' : 'none';

    const container = document.getElementById('cartItems');
    container.querySelectorAll('.cart-item').forEach(el => el.remove());
    document.getElementById('cartEmpty').style.display = cart.length ? 'none' : 'block';

    cart.forEach(item => {
        const div = document.createElement('div');
        div.className = 'cart-item';
        const variantText = item.variants && Object.keys(item.variants).length
            ? Object.entries(item.variants).map(([k,v]) => `${k}: ${v}`).join(', ')
            : '';
        div.innerHTML = `
            <img src="${item.image}" alt="${item.name}" class="cart-item-img">
            <div class="cart-item-info">
                <span class="cart-item-name">${item.name}${item.brand ? ' <small style="color:#64748b">'+item.brand+'</small>' : ''}</span>
                ${variantText ? `<span style="font-size:.7rem;color:#818cf8">${variantText}</span>` : ''}
                <span class="cart-item-price">${inr(item.price)}</span>
                <div class="cart-item-qty">
                    <button class="qty-btn">−</button>
                    <span>${item.qty}</span>
                    <button class="qty-btn">+</button>
                </div>
            </div>
            <button class="cart-item-remove" aria-label="Remove">&times;</button>
        `;
        const [minusBtn, plusBtn] = div.querySelectorAll('.qty-btn');
        minusBtn.addEventListener('click', () => updateCartQty(item.itemKey, Math.max(1, item.qty - 1)));
        plusBtn.addEventListener('click',  () => updateCartQty(item.itemKey, item.qty + 1));
        div.querySelector('.cart-item-remove').addEventListener('click', () => removeFromCart(item.itemKey));
        container.appendChild(div);
    });
}

function openCart()  {
    document.getElementById('cartOverlay').classList.add('open');
    document.getElementById('cartSidebar').classList.add('open');
}
function closeCart() {
    document.getElementById('cartOverlay').classList.remove('open');
    document.getElementById('cartSidebar').classList.remove('open');
}

async function handleBuyNow(product, card) {
    await addToCart(product, card);
    openCart();
}

function handleCheckout() {
    if (!cart.length) { showToast('Your cart is empty'); return; }
    // Pass sessionId to payment page via sessionStorage (not the cart data itself)
    sessionStorage.setItem('shopHubSessionId', getSessionId());
    const checkoutBtn = document.getElementById('checkoutBtn');
    checkoutBtn.disabled = true;
    checkoutBtn.innerHTML = '<span class="checkout-loading"></span> Processing...';
    setTimeout(() => {
        window.location.href = 'payment.html';
    }, 800);
}

// ─────────────────────────────────────────────────────────────────────────────
// TOAST
// ─────────────────────────────────────────────────────────────────────────────
function showToast(message) {
    document.querySelector('.toast')?.remove();
    const toast = document.createElement('div');
    toast.className = 'toast';
    toast.textContent = message;
    document.body.appendChild(toast);
    setTimeout(() => toast.classList.add('show'), 10);
    setTimeout(() => { toast.classList.remove('show'); setTimeout(() => toast.remove(), 300); }, 3000);
}

// ─────────────────────────────────────────────────────────────────────────────
// QUICK BUTTONS & CHATBOT
// ─────────────────────────────────────────────────────────────────────────────
function renderQuickButtons() {
    const container = document.getElementById('quickButtons');
    ['iPhone 15','Air Max Running','MacBook Air M2','Air Fryer 4L','AirPods Pro 2','DualSense Controller'].forEach(name => {
        const btn = document.createElement('button');
        btn.className   = 'quick-btn';
        btn.textContent = name;
        btn.addEventListener('click', () => openChatbotWithProduct(name));
        container.appendChild(btn);
    });
}

function openChatbot() {
    document.getElementById('chatbotContainer').classList.add('open');
    document.getElementById('chatInput').focus();
}

function openChatbotWithProduct(productName) {
    document.getElementById('chatbotContainer').classList.add('open');
    const input = document.getElementById('chatInput');
    input.value = productName;
    input.focus();
    setTimeout(() => {
        if (typeof handleUserMessage === 'function' && input.value === productName) {
            handleUserMessage(productName);
            input.value = '';
        }
    }, 500);
}

// ─────────────────────────────────────────────────────────────────────────────
// SEARCH
// ─────────────────────────────────────────────────────────────────────────────
function initSearch() {
    const input     = document.getElementById('searchInput');
    const searchBtn = document.getElementById('searchBtn');
    const clearBtn  = document.getElementById('searchClearBtn');
    const infoEl    = document.getElementById('searchResultsInfo');
    if (!input) return;

    input.addEventListener('input', () => {
        clearBtn.style.display = input.value.trim() ? 'block' : 'none';
    });
    searchBtn.addEventListener('click', () => runSearch(input.value, infoEl));
    input.addEventListener('keydown', e => { if (e.key === 'Enter') runSearch(input.value, infoEl); });
    clearBtn.addEventListener('click', () => {
        input.value = '';
        clearBtn.style.display = 'none';
        infoEl.innerHTML = '';
        activeCategory = 'all';
        activeFilters  = {};
        document.querySelectorAll('.cat-pill').forEach((p,i) => {
            i === 0 ? p.classList.add('active-cat') : p.classList.remove('active-cat');
        });
        buildFilterDropdowns('all');
        renderProducts(PRODUCTS);
        input.focus();
    });
}

async function runSearch(query, infoEl) {
    const q = query.trim();
    if (!q) return;
    try {
        const res     = await fetch(`${API}/products/search?q=${encodeURIComponent(q)}`);
        const data    = await res.json();
        const results = data.data || [];
        renderProducts(results);
        if (infoEl) {
            infoEl.innerHTML = results.length
                ? `Showing <span>${results.length} result${results.length!==1?'s':''}</span> for "<span>${query}</span>"`
                : '';
        }
        if (!results.length) {
            document.getElementById('noResultsQuery').textContent = `"${query}"`;
            document.getElementById('noResults').style.display = 'block';
        }
        document.getElementById('productList').scrollIntoView({ behavior:'smooth', block:'start' });
    } catch (err) {
        console.error('Search failed:', err);
    }
}

// ─────────────────────────────────────────────────────────────────────────────
// PRODUCT DETAIL MODAL
// ─────────────────────────────────────────────────────────────────────────────
const PRODUCT_FEATURES = {
    mobiles:     p => p.specs ? Object.entries(p.specs).map(([k,v]) => `${k}: ${v}`) : [`Brand: ${p.brand}`],
    laptops:     p => p.specs ? Object.entries(p.specs).map(([k,v]) => `${k}: ${v}`) : [`Brand: ${p.brand}`],
    shoes:       p => [`Brand: ${p.brand}`, `Outsole: Rubber with multi-directional traction`, `Fit: True to size`, `Care: Wipe with damp cloth, air dry`],
    dress:       p => [`Fabric: Premium blend`, `Fit: Regular`, `Care: Machine wash cold`, `Available sizes: ${p.sizes?.join(', ') || 'S, M, L, XL'}`],
    electronics: p => [`Brand: ${p.brand}`, `Connectivity: Wireless Bluetooth 5.3`, `Battery: Up to 30 hours`, `ANC: Active Noise Cancellation`],
    gaming:      p => [`Brand: ${p.brand}`, `Connection: USB / Wireless 2.4GHz`, `RGB: Full RGB lighting`, `Warranty: 2 years`],
    lifestyle:   p => [`Brand: ${p.brand}`, `Material: Premium quality`, `Care: Wipe clean with damp cloth`, `Warranty: 1 year`],
    other:       p => [`Brand: ${p.brand}`, `Power: Energy efficient`, `Safety: Overheat protection`, `Warranty: 2 years`],
};

function getProductImages(product) {
    const base = product.image.replace(/\?.*$/, '');
    return [
        base + '?w=600&h=600&fit=crop&crop=center',
        base + '?w=600&h=600&fit=crop&crop=top',
        base + '?w=600&h=600&fit=crop&crop=bottom',
        base + '?w=600&h=600&fit=crop&crop=entropy',
    ];
}

function generateReviews(product) {
    const reviewBanks = [
        { name:'Arjun M.',  rating:5, text:`Absolutely love the ${product.name}! Exceeded my expectations. Build quality is top-notch.`, date:'12 Jan 2025', verified:true },
        { name:'Priya S.',  rating:4, text:`Great product for the price. ${product.brand} quality is reliable. Would recommend.`, date:'28 Feb 2025', verified:true },
        { name:'Rahul K.',  rating:5, text:`Best purchase I've made this year! ${product.name} works flawlessly.`, date:'5 Mar 2025', verified:true },
        { name:'Sneha R.',  rating:4, text:`Really happy with this ${product.brand} product. Does exactly what it promises.`, date:'18 Mar 2025', verified:false },
        { name:'Vikram T.', rating:3, text:`Decent product overall. Good but I expected slightly better at this price point.`, date:'2 Apr 2025', verified:true },
        { name:'Ananya P.', rating:5, text:`Fantastic! The quality from ${product.brand} is exceptional. Will buy again!`, date:'15 Apr 2025', verified:true },
    ];
    const seed = product.id % reviewBanks.length;
    return [0,1,2,3].map(i => reviewBanks[(seed + i) % reviewBanks.length]);
}

function starHTML(rating) {
    return Array.from({length:5}, (_,i) => i < rating ? '★' : '☆').join('');
}

function openProductDetail(product) {
    const modal   = document.getElementById('pdModal');
    const overlay = document.getElementById('pdOverlay');
    const images  = getProductImages(product);
    const reviews = generateReviews(product);
    const features = (PRODUCT_FEATURES[product.category] || PRODUCT_FEATURES.other)(product);
    const labels  = ['Front','Back','Top','Side'];
    const avg     = (reviews.reduce((s,r) => s+r.rating,0) / reviews.length).toFixed(1);

    const mainImg = document.getElementById('pdMainImg');
    mainImg.src = product.image;
    mainImg.alt = product.name + ' - Front';

    const thumbsEl = document.getElementById('pdThumbs');
    thumbsEl.innerHTML = '';
    images.forEach((src, i) => {
        const t = document.createElement('div');
        t.className = 'pd-thumb' + (i===0?' active':'');
        t.title = labels[i];
        t.innerHTML = `<img src="${src}" alt="${labels[i]}" loading="lazy">`;
        t.addEventListener('click', () => {
            mainImg.style.opacity = '0.4';
            setTimeout(() => { mainImg.src = src; mainImg.alt = `${product.name} - ${labels[i]}`; mainImg.style.opacity = '1'; }, 150);
            thumbsEl.querySelectorAll('.pd-thumb').forEach(el => el.classList.remove('active'));
            t.classList.add('active');
        });
        thumbsEl.appendChild(t);
    });

    document.getElementById('pdBrand').textContent = product.brand || '';
    document.getElementById('pdTitle').textContent = product.name;
    document.getElementById('pdPrice').textContent = inr(product.price);
    document.getElementById('pdRating').innerHTML  =
        `<span class="pd-stars">${starHTML(Math.round(avg))}</span>
         <strong style="color:#fbbf24;font-size:.9rem">${avg}</strong>
         <span class="pd-rating-count">(${reviews.length * 48 + product.id} ratings)</span>`;

    const variantsEl = document.getElementById('pdVariants');
    variantsEl.innerHTML = '';
    [['sizes','Size'],['colors','Color'],['storage','Storage'],['ram','RAM']].forEach(([key,label]) => {
        if (!product[key]?.length) return;
        const row = document.createElement('div');
        row.className = 'pd-variant-row';
        row.innerHTML = `<div class="pd-variant-label">${label}</div>
            <div class="pd-variant-pills">${product[key].map((v,i) =>
                `<span class="pd-vpill${i===0?' active':''}">${v}</span>`
            ).join('')}</div>`;
        row.querySelectorAll('.pd-vpill').forEach(pill => {
            pill.addEventListener('click', () => {
                row.querySelectorAll('.pd-vpill').forEach(p => p.classList.remove('active'));
                pill.classList.add('active');
            });
        });
        variantsEl.appendChild(row);
    });

    const featuresSection = document.getElementById('pdFeaturesSection');
    if (product.specs && (product.category === 'mobiles' || product.category === 'laptops')) {
        featuresSection.innerHTML = `
            <h4 class="pd-section-title">📋 Full Specifications</h4>
            <div class="pd-specs-table">
                ${Object.entries(product.specs).map(([k,v]) => `
                <div class="pd-spec-row">
                    <span class="pd-spec-key">${k.replace(/([A-Z])/g,' $1').replace(/^./,s=>s.toUpperCase())}</span>
                    <span class="pd-spec-val">${v}</span>
                </div>`).join('')}
            </div>`;
    } else {
        featuresSection.innerHTML = `
            <h4 class="pd-section-title">✨ Key Features</h4>
            <ul class="pd-features">${features.map(f => `<li>${f}</li>`).join('')}</ul>`;
    }

    const compareBtn = document.getElementById('pdCompareBtn');
    const isCompared = compareList.some(c => c.id === product.id);
    compareBtn.textContent = isCompared ? '✓ Added to Compare' : '⚖️ Add to Compare';
    compareBtn.className   = 'pd-compare-btn' + (isCompared ? ' compared' : '');
    compareBtn.onclick     = () => {
        toggleCompare(product);
        const now = compareList.some(c => c.id === product.id);
        compareBtn.textContent = now ? '✓ Added to Compare' : '⚖️ Add to Compare';
        compareBtn.className   = 'pd-compare-btn' + (now ? ' compared' : '');
    };

    document.getElementById('pdAddCart').onclick = () => { addToCartFromModal(product, variantsEl); closeProductDetail(); };
    document.getElementById('pdBuyNow').onclick  = () => { addToCartFromModal(product, variantsEl); closeProductDetail(); openCart(); };

    document.getElementById('pdReviews').innerHTML = reviews.map(r => `
        <div class="pd-review-card">
            <div class="pd-review-header">
                <span class="pd-reviewer">${r.name}</span>
                <span class="pd-review-stars">${starHTML(r.rating)}</span>
            </div>
            <div class="pd-review-date">${r.date}</div>
            <p class="pd-review-text">${r.text}</p>
            ${r.verified ? '<span class="pd-verified">✔ Verified Purchase</span>' : ''}
        </div>
    `).join('');

    overlay.classList.add('open');
    modal.classList.add('open');
    document.body.style.overflow = 'hidden';
}

async function addToCartFromModal(product, variantsEl) {
    const variants = {};
    variantsEl.querySelectorAll('.pd-variant-row').forEach(row => {
        const label  = row.querySelector('.pd-variant-label').textContent.toLowerCase();
        const active = row.querySelector('.pd-vpill.active');
        if (active) variants[label] = active.textContent;
    });
    try {
        await fetch(`${API}/cart/add`, {
            method:  'POST',
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify({ sessionId: getSessionId(), productId: product.id, qty: 1, variants })
        });
        await loadCartFromAPI();
        const vText = Object.values(variants).join(', ');
        showToast(`Added "${product.name}"${vText ? ' ('+vText+')' : ''} to cart`);
    } catch (err) {
        showToast('⚠️ Failed to add to cart');
    }
}

function closeProductDetail() {
    document.getElementById('pdModal').classList.remove('open');
    document.getElementById('pdOverlay').classList.remove('open');
    document.body.style.overflow = '';
}

// ─────────────────────────────────────────────────────────────────────────────
// PRODUCT COMPARISON
// ─────────────────────────────────────────────────────────────────────────────
let compareList = [];

function toggleCompare(product) {
    const idx = compareList.findIndex(c => c.id === product.id);
    if (idx > -1) { compareList.splice(idx, 1); showToast(`Removed "${product.name}" from compare`); }
    else {
        if (compareList.length >= 4) { showToast('You can compare up to 4 products at once'); return; }
        compareList.push(product);
        showToast(`Added "${product.name}" to compare`);
    }
    updateCompareTray();
}

function updateCompareTray() {
    const tray = document.getElementById('compareTray');
    if (!tray) return;
    if (!compareList.length) { tray.classList.remove('visible'); return; }
    tray.classList.add('visible');
    document.getElementById('compareTrayThumbs').innerHTML = compareList.map(p => `
        <div class="compare-tray-item" title="${p.name}">
            <img src="${p.image}" alt="${p.name}">
            <button class="compare-tray-remove" onclick="toggleCompare(PRODUCTS.find(x=>x.id===${p.id}));return false">✕</button>
        </div>
    `).join('');
    document.getElementById('compareTrayCount').textContent = `${compareList.length} product${compareList.length>1?'s':''}`;
    document.getElementById('compareNowBtn').disabled = compareList.length < 2;
}

function openCompareModal() {
    if (compareList.length < 2) { showToast('Add at least 2 products to compare'); return; }
    renderCompareTable();
    document.getElementById('compareModal').classList.add('open');
    document.getElementById('compareOverlay').classList.add('open');
    document.body.style.overflow = 'hidden';
}

function closeCompareModal() {
    document.getElementById('compareModal').classList.remove('open');
    document.getElementById('compareOverlay').classList.remove('open');
    document.body.style.overflow = '';
}

function renderCompareTable() {
    const products = compareList;
    const specKeys = products[0]?.specs ? Object.keys(products[0].specs) : [];
    const baseRows = [
        { label:'Image',    fn: p => `<img src="${p.image}" alt="${p.name}" style="width:80px;height:80px;object-fit:cover;border-radius:8px;">` },
        { label:'Brand',    fn: p => `<strong>${p.brand}</strong>` },
        { label:'Price',    fn: p => `<strong style="color:#6366f1">${inr(p.price)}</strong>` },
        { label:'Category', fn: p => p.category.charAt(0).toUpperCase()+p.category.slice(1) },
    ];
    const specRows    = specKeys.map(k => ({ label: k.replace(/([A-Z])/g,' $1').replace(/^./,s=>s.toUpperCase()), fn: p => p.specs?.[k] || '—' }));
    const variantRows = [
        { label:'RAM',     fn: p => p.ram?.join(', ')     || '—' },
        { label:'Storage', fn: p => p.storage?.join(', ') || '—' },
        { label:'Colors',  fn: p => p.colors?.join(', ')  || '—' },
        { label:'Sizes',   fn: p => p.sizes?.join(', ')   || '—' },
    ].filter(r => products.some(p => r.fn(p) !== '—'));

    const allRows  = specKeys.length > 0 ? [...baseRows,...specRows,...variantRows] : [...baseRows,...variantRows];
    const headerCells = products.map(p => `<th><div class="cmp-header-cell">
        <div class="cmp-remove" onclick="compareList.splice(compareList.findIndex(x=>x.id===${p.id}),1);updateCompareTray();renderCompareTable()">✕</div>
        <strong>${p.name}</strong></div></th>`).join('');
    const tableRows = allRows.map(row => `<tr><td class="cmp-row-label">${row.label}</td>${products.map(p => `<td>${row.fn(p)}</td>`).join('')}</tr>`).join('');
    const addBtns   = products.map(p => `<td style="padding:.5rem"><button class="cmp-add-cart"
        onclick="addToCart(PRODUCTS.find(x=>x.id===${p.id}),null)">🛒 Add to Cart</button></td>`).join('');

    document.getElementById('compareTableWrap').innerHTML = `
        <div class="cmp-scroll"><table class="cmp-table">
            <thead><tr><th class="cmp-row-label">Specs</th>${headerCells}</tr></thead>
            <tbody>${tableRows}</tbody>
            <tfoot><tr><td class="cmp-row-label"></td>${addBtns}</tr></tfoot>
        </table></div>`;
}

document.addEventListener('DOMContentLoaded', () => {
    document.getElementById('compareOverlay')?.addEventListener('click', closeCompareModal);
    document.getElementById('compareModalClose')?.addEventListener('click', closeCompareModal);
    document.getElementById('compareNowBtn')?.addEventListener('click', openCompareModal);
    document.getElementById('clearCompareBtn')?.addEventListener('click', () => { compareList = []; updateCompareTray(); });
});


// ═══════════════════════════════════════════════════════════════
//  SHOPHUB — AUTH HELPERS
//  Works with Spring Boot JWT backend
// ═══════════════════════════════════════════════════════════════

// ── Read saved token from localStorage or sessionStorage ──────
function getAuthToken() {
    return localStorage.getItem('shopHubToken') || sessionStorage.getItem('shopHubToken') || null;
}

// ── Read saved user profile ───────────────────────────────────
function getLoggedInUser() {
    const raw = localStorage.getItem('shopHubUser') || sessionStorage.getItem('shopHubUser');
    try { return raw ? JSON.parse(raw) : null; } catch { return null; }
}

// ── Attach Bearer token to any fetch call ─────────────────────
//    Usage: fetch(url, { method: 'POST', headers: authHeaders(), body: ... })
function authHeaders(extra = {}) {
    const token = getAuthToken();
    return {
        'Content-Type': 'application/json',
        ...(token ? { 'Authorization': 'Bearer ' + token } : {}),
        ...extra
    };
}

// ── Logout: clear session and redirect to login ───────────────
function logoutUser() {
    ['shopHubToken', 'shopHubUser'].forEach(k => {
        localStorage.removeItem(k);
        sessionStorage.removeItem(k);
    });
    window.location.href = 'login.html';
}

// ── Update header UI: Sign In link  ↔  Greeting + Logout btn ─
function updateAuthUI() {
    const user      = getLoggedInUser();
    const loginBtn  = document.getElementById('loginHeaderBtn');
    const greeting  = document.getElementById('userGreeting');
    const logoutBtn = document.getElementById('logoutBtn');
    if (!loginBtn) return;  // header snippet not yet added to index.html

    if (user && getAuthToken()) {
        loginBtn.style.display  = 'none';
        greeting.style.display  = '';
        greeting.textContent    = '👋 ' + (user.firstName || 'Hi!');
        logoutBtn.style.display = '';
    } else {
        loginBtn.style.display  = '';
        greeting.style.display  = 'none';
        logoutBtn.style.display = 'none';
    }
}
