package com.shophub.data;

import com.shophub.model.Category;
import com.shophub.model.Product;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * In-memory product catalog — ported directly from products.js.
 * In production, replace with a JPA repository backed by a database.
 */
public class ProductDataStore {

    public static final List<Category> CATEGORIES = List.of(
        new Category("dress",       "Dress & Fashion",    "👗"),
        new Category("shoes",       "Shoes & Footwear",   "👟"),
        new Category("mobiles",     "Mobiles",            "📱"),
        new Category("laptops",     "Laptops",            "💻"),
        new Category("electronics", "Electronics",        "🎧"),
        new Category("lifestyle",   "Lifestyle & Fitness","✨"),
        new Category("gaming",      "Gaming",             "🎮"),
        new Category("other",       "Home & Kitchen",     "🏠")
    );

    public static final List<Product> PRODUCTS;

    static {
        // ── DRESS & FASHION ─────────────────────────────────
        Product p1 = new Product(1, "Summer Dress", "dress", "Zara", 2999,
            "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=400&h=400&fit=crop");
        p1.setSizes(List.of("XS","S","M","L","XL"));
        p1.setColors(List.of("Red","White","Yellow"));

        Product p2 = new Product(2, "Formal Dress", "dress", "Mango", 6999,
            "https://images.unsplash.com/photo-1566174053879-31528523f8ae?w=400&h=400&fit=crop");
        p2.setSizes(List.of("S","M","L","XL"));
        p2.setColors(List.of("Black","Navy","Maroon"));

        Product p3 = new Product(3, "Casual Dress", "dress", "H&M", 1999,
            "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=400&h=400&fit=crop");
        p3.setSizes(List.of("XS","S","M","L","XL","XXL"));
        p3.setColors(List.of("Blue","Pink","White"));

        Product p4 = new Product(4, "Floral Dress", "dress", "Forever21", 2499,
            "https://images.unsplash.com/photo-1612336307429-8a898d10e223?w=400&h=400&fit=crop");
        p4.setSizes(List.of("XS","S","M","L"));
        p4.setColors(List.of("Floral Pink","Floral Blue"));

        Product p22 = new Product(22, "Maxi Dress", "dress", "Fabindia", 3499,
            "https://images.unsplash.com/photo-1518462592603-0b9e0eb26a66?w=400&h=400&fit=crop");
        p22.setSizes(List.of("S","M","L","XL"));
        p22.setColors(List.of("Orange","Teal","Purple"));

        Product p23 = new Product(23, "Evening Gown", "dress", "Mango", 12999,
            "https://images.unsplash.com/photo-1519657337289-077653f724ed?w=400&h=400&fit=crop");
        p23.setSizes(List.of("S","M","L"));
        p23.setColors(List.of("Black","Gold","Wine"));

        Product p24 = new Product(24, "Office Dress", "dress", "Zara", 4999,
            "https://images.unsplash.com/photo-1548690312-e3b507d8c110?w=400&h=400&fit=crop");
        p24.setSizes(List.of("XS","S","M","L","XL"));
        p24.setColors(List.of("Grey","Black","White"));

        // ── SHOES ───────────────────────────────────────────
        Product p301 = new Product(301, "Air Max 270", "shoes", "Nike", 8999,
            "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=400&fit=crop");
        p301.setSizes(List.of("6","7","8","9","10","11"));
        p301.setColors(List.of("Black/White","Red/Black","Blue/White"));

        Product p302 = new Product(302, "Revolution 6", "shoes", "Nike", 5999,
            "https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=400&h=400&fit=crop");
        p302.setSizes(List.of("6","7","8","9","10","11"));
        p302.setColors(List.of("Black","White","Grey"));

        Product p304 = new Product(304, "Ultraboost 23", "shoes", "Adidas", 14999,
            "https://images.unsplash.com/photo-1608231387042-66d1773d3028?w=400&h=400&fit=crop");
        p304.setSizes(List.of("6","7","8","9","10","11"));
        p304.setColors(List.of("Core Black","Cloud White","Solar Red"));

        Product p305 = new Product(305, "Stan Smith", "shoes", "Adidas", 6999,
            "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?w=400&h=400&fit=crop");
        p305.setSizes(List.of("6","7","8","9","10","11"));
        p305.setColors(List.of("White/Green","White/Navy"));

        Product p307 = new Product(307, "RS-X Sneakers", "shoes", "Puma", 6499,
            "https://images.unsplash.com/photo-1556906781-9a412961a28c?w=400&h=400&fit=crop");
        p307.setSizes(List.of("6","7","8","9","10","11"));
        p307.setColors(List.of("White/Blue","Black/Red"));

        Product p317 = new Product(317, "Chuck Taylor All Star", "shoes", "Converse", 4499,
            "https://images.unsplash.com/photo-1494496195158-c3bc975fc97b?w=400&h=400&fit=crop");
        p317.setSizes(List.of("5","6","7","8","9","10","11"));
        p317.setColors(List.of("White","Black","Red","Navy"));

        // ── MOBILES ─────────────────────────────────────────
        Product p401 = new Product(401, "iPhone 15", "mobiles", "Apple", 79999,
            "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400&h=400&fit=crop");
        p401.setStorage(List.of("128GB","256GB","512GB"));
        p401.setColors(List.of("Black","Blue","Pink","Yellow","Green"));
        p401.setRam(List.of("6GB"));
        p401.setSpecs(Map.of(
            "processor","Apple A16 Bionic (4nm)",
            "ram","6 GB",
            "storage","128 GB / 256 GB / 512 GB",
            "display","6.1\" Super Retina XDR OLED",
            "resolution","2556 × 1179 px (460 ppi)",
            "refreshRate","60 Hz",
            "rearCamera","48 MP (main) + 12 MP (ultra-wide)",
            "battery","3877 mAh",
            "os","iOS 17",
            "waterResistance","IP68 (6m / 30 min)"
        ));

        Product p402 = new Product(402, "iPhone 15 Pro", "mobiles", "Apple", 134999,
            "https://images.unsplash.com/photo-1678685888221-cda773a3dcdb?w=400&h=400&fit=crop");
        p402.setStorage(List.of("256GB","512GB","1TB"));
        p402.setColors(List.of("Natural Titanium","Blue Titanium","Black Titanium"));
        p402.setRam(List.of("8GB"));
        p402.setSpecs(Map.of(
            "processor","Apple A17 Pro (3nm)",
            "ram","8 GB",
            "storage","256 GB / 512 GB / 1 TB",
            "display","6.1\" Super Retina XDR ProMotion OLED",
            "refreshRate","1–120 Hz ProMotion",
            "rearCamera","48 MP (main) + 12 MP (ultra-wide) + 12 MP (3× telephoto)",
            "battery","3274 mAh",
            "os","iOS 17",
            "waterResistance","IP68 (6m / 30 min)"
        ));

        Product p404 = new Product(404, "Galaxy S24 Ultra", "mobiles", "Samsung", 129999,
            "https://images.unsplash.com/photo-1709021045338-cd5c7f9edc81?w=400&h=400&fit=crop");
        p404.setStorage(List.of("256GB","512GB","1TB"));
        p404.setColors(List.of("Titanium Black","Titanium Gray","Titanium Violet"));
        p404.setRam(List.of("12GB"));
        p404.setSpecs(Map.of(
            "processor","Snapdragon 8 Gen 3 (4nm)",
            "ram","12 GB",
            "storage","256 GB / 512 GB / 1 TB",
            "display","6.8\" Dynamic AMOLED 2X",
            "refreshRate","1–120 Hz",
            "rearCamera","200 MP (main) + 12 MP (ultra-wide) + 10 MP (3×) + 50 MP (5×)",
            "battery","5000 mAh",
            "os","Android 14 (One UI 6.1)",
            "waterResistance","IP68"
        ));

        Product p408 = new Product(408, "OnePlus 12", "mobiles", "OnePlus", 64999,
            "https://images.unsplash.com/photo-1585060544812-6b45742d762f?w=400&h=400&fit=crop");
        p408.setStorage(List.of("256GB","512GB"));
        p408.setColors(List.of("Flowy Emerald","Silky Black"));
        p408.setRam(List.of("12GB","16GB"));
        p408.setSpecs(Map.of(
            "processor","Snapdragon 8 Gen 3 (4nm)",
            "display","6.82\" LTPO3 Fluid AMOLED",
            "refreshRate","1–120 Hz",
            "rearCamera","50 MP (Hasselblad) + 48 MP + 64 MP periscope",
            "battery","5400 mAh",
            "charging","100W SUPERVOOC",
            "os","Android 14 (OxygenOS 14)",
            "waterResistance","IP65"
        ));

        Product p416 = new Product(416, "Pixel 8", "mobiles", "Google", 75999,
            "https://images.unsplash.com/photo-1567581935884-3349723552ca?w=400&h=400&fit=crop");
        p416.setStorage(List.of("128GB","256GB"));
        p416.setColors(List.of("Hazel","Obsidian","Rose"));
        p416.setRam(List.of("8GB"));
        p416.setSpecs(Map.of(
            "processor","Google Tensor G3 (4nm)",
            "display","6.2\" Actua OLED",
            "refreshRate","60–120 Hz",
            "rearCamera","50 MP (main) + 12 MP (ultra-wide)",
            "battery","4575 mAh",
            "os","Android 14 (stock)",
            "waterResistance","IP68"
        ));

        // ── LAPTOPS ─────────────────────────────────────────
        Product p501 = new Product(501, "MacBook Air M2", "laptops", "Apple", 114999,
            "https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400&h=400&fit=crop");
        p501.setStorage(List.of("256GB","512GB","1TB"));
        p501.setColors(List.of("Midnight","Starlight","Space Grey","Silver"));
        p501.setRam(List.of("8GB","16GB","24GB"));
        p501.setSpecs(Map.of(
            "processor","Apple M2 (8-core CPU)",
            "display","13.6\" Liquid Retina IPS",
            "resolution","2560 × 1664 px",
            "refreshRate","60 Hz",
            "battery","52.6 Wh — up to 18 hrs",
            "os","macOS Sonoma",
            "weight","1.24 kg",
            "ports","2× USB-C Thunderbolt 4, MagSafe 3"
        ));

        Product p502 = new Product(502, "MacBook Air M3", "laptops", "Apple", 134999,
            "https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=400&h=400&fit=crop");
        p502.setStorage(List.of("256GB","512GB","1TB"));
        p502.setColors(List.of("Midnight","Starlight","Space Grey","Silver"));
        p502.setRam(List.of("8GB","16GB","24GB"));
        p502.setSpecs(Map.of(
            "processor","Apple M3 (8-core CPU)",
            "display","13.6\" Liquid Retina IPS",
            "battery","52.6 Wh — up to 18 hrs",
            "os","macOS Sonoma",
            "weight","1.24 kg"
        ));

        Product p504 = new Product(504, "XPS 15", "laptops", "Dell", 149999,
            "https://images.unsplash.com/photo-1593642632559-0c6d3fc62b89?w=400&h=400&fit=crop");
        p504.setStorage(List.of("512GB","1TB","2TB"));
        p504.setColors(List.of("Platinum Silver","Graphite"));
        p504.setRam(List.of("16GB","32GB","64GB"));
        p504.setSpecs(Map.of(
            "processor","Intel Core i7-13700H",
            "display","15.6\" OLED InfinityEdge Touch",
            "resolution","3456 × 2160 px (4K+)",
            "gpu","NVIDIA GeForce RTX 4060 8 GB",
            "battery","86 Wh — up to 12 hrs",
            "os","Windows 11 Home",
            "weight","1.86 kg"
        ));

        Product p513 = new Product(513, "ROG Zephyrus G14", "laptops", "Asus", 124999,
            "https://images.unsplash.com/photo-1518770660439-4636190af475?w=400&h=400&fit=crop");
        p513.setStorage(List.of("512GB","1TB"));
        p513.setColors(List.of("Eclipse Gray","Platinum White"));
        p513.setRam(List.of("16GB","32GB"));
        p513.setSpecs(Map.of(
            "processor","AMD Ryzen 9 7940HS (Zen 4)",
            "display","14\" QHD+ OLED 165 Hz",
            "gpu","NVIDIA GeForce RTX 4070 8 GB",
            "battery","73 Wh — up to 10 hrs",
            "os","Windows 11 Home",
            "weight","1.65 kg"
        ));

        // ── ELECTRONICS ─────────────────────────────────────
        Product p9 = new Product(9, "WH-1000XM5 Headphones", "electronics", "Sony", 28999,
            "https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop");
        p9.setColors(List.of("Black","Silver"));

        Product p10 = new Product(10, "Galaxy Watch 6", "electronics", "Samsung", 29999,
            "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=400&fit=crop");
        p10.setColors(List.of("Graphite","Silver","Gold"));

        Product p30 = new Product(30, "AirPods Pro 2", "electronics", "Apple", 24900,
            "https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=400&h=400&fit=crop");
        p30.setColors(List.of("White"));

        Product p29 = new Product(29, "iPad Air M2", "electronics", "Apple", 59900,
            "https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400&h=400&fit=crop");
        p29.setStorage(List.of("64GB","256GB"));
        p29.setColors(List.of("Blue","Starlight","Purple","Space Grey"));

        Product p130 = new Product(130, "Bose QuietComfort II", "electronics", "Bose", 24900,
            "https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=400&h=400&fit=crop");
        p130.setColors(List.of("Triple Black","Soapstone"));

        // ── LIFESTYLE ───────────────────────────────────────
        Product p5 = new Product(5, "Yoga Mat", "lifestyle", "Decathlon", 1499,
            "https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400&h=400&fit=crop");

        Product p6 = new Product(6, "Steel Water Bottle", "lifestyle", "Cello", 599,
            "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=400&h=400&fit=crop");

        Product p7 = new Product(7, "Skincare Set", "lifestyle", "Lakme", 2299,
            "https://images.unsplash.com/photo-1556229010-6c3f2c9bbe0d?w=400&h=400&fit=crop");

        Product p26 = new Product(26, "Sunglasses", "lifestyle", "Ray-Ban", 4999,
            "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400&h=400&fit=crop");

        Product p28 = new Product(28, "Smart Band 7", "lifestyle", "Mi", 2499,
            "https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=400&h=400&fit=crop");

        // ── GAMING ──────────────────────────────────────────
        Product p13 = new Product(13, "BlackWidow V4 Keyboard", "gaming", "Razer", 9999,
            "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=400&h=400&fit=crop");
        p13.setColors(List.of("Black"));

        Product p14 = new Product(14, "DeathAdder V3 Mouse", "gaming", "Razer", 5999,
            "https://images.unsplash.com/photo-1527864559417-7d286ad861a0?w=400&h=400&fit=crop");
        p14.setColors(List.of("Black","White"));

        Product p16 = new Product(16, "DualSense Controller", "gaming", "Sony", 5490,
            "https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=400&h=400&fit=crop");
        p16.setColors(List.of("White","Midnight Black","Cosmic Red"));

        Product p17 = new Product(17, "Meta Quest 3", "gaming", "Meta", 49999,
            "https://images.unsplash.com/photo-1617802690992-15d3a4c818c8?w=400&h=400&fit=crop");
        p17.setColors(List.of("White"));

        Product p33 = new Product(33, "Gaming Chair Pro", "gaming", "Green Soul", 15999,
            "https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=400&h=400&fit=crop");
        p33.setColors(List.of("Black/Red","Black/Blue","Black/White"));

        // ── HOME & KITCHEN ───────────────────────────────────
        Product p18 = new Product(18, "Coffee Maker", "other", "Philips", 5999,
            "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&h=400&fit=crop");

        Product p19 = new Product(19, "Air Fryer 4L", "other", "Philips", 6999,
            "https://images.unsplash.com/photo-1585238341267-1cfec2046952?w=400&h=400&fit=crop");

        Product p20 = new Product(20, "Mixer Grinder 750W", "other", "Bajaj", 2499,
            "https://images.unsplash.com/photo-1570222094114-d1a3c138fc1c?w=400&h=400&fit=crop");

        Product p36 = new Product(36, "Microwave Oven 20L", "other", "LG", 8499,
            "https://images.unsplash.com/photo-1585659722983-3a675dabf23d?w=400&h=400&fit=crop");

        Product p38 = new Product(38, "Cyclone V10 Vacuum", "other", "Dyson", 29999,
            "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=400&h=400&fit=crop");

        PRODUCTS = List.of(
            p1, p2, p3, p4, p22, p23, p24,
            p301, p302, p304, p305, p307, p317,
            p401, p402, p404, p408, p416,
            p501, p502, p504, p513,
            p9, p10, p30, p29, p130,
            p5, p6, p7, p26, p28,
            p13, p14, p16, p17, p33,
            p18, p19, p20, p36, p38
        );
    }
}
