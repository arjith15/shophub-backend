package com.shophub.config;

import com.shophub.model.Product;
import com.shophub.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Component
public class DataSeeder implements CommandLineRunner {

    @Autowired
    private ProductRepository productRepository;

    @Override
    public void run(String... args) {
        if (productRepository.count() >= 60) {
            System.out.println("✅ Products already seeded. Skipping.");
            return;
        }
        productRepository.deleteAll();
        System.out.println("🌱 Seeding all products...");
        seedAll();
        System.out.println("✅ Seeded " + productRepository.count() + " products!");
    }

    private void save(int id, String name, String category, String brand,
                      double price, String image,
                      List<String> sizes, List<String> colors,
                      List<String> storage, List<String> ram,
                      Map<String,String> specs) {
        Product p = new Product();
        p.setId(id);
        p.setName(name);
        p.setCategory(category);
        p.setBrand(brand);
        p.setPrice(price);
        p.setImage(image);
        p.setSizes(sizes);
        p.setColors(colors);
        p.setStorage(storage);
        p.setRam(ram);
        p.setSpecs(specs);
        productRepository.save(p);
    }

    private List<String> l(String... v) { return Arrays.asList(v); }
    private Map<String,String> specs(String... kv) {
        Map<String,String> m = new java.util.LinkedHashMap<>();
        for (int i = 0; i + 1 < kv.length; i += 2) m.put(kv[i], kv[i+1]);
        return m;
    }

    private void seedAll() {

        // ── DRESS ──────────────────────────────────────────────
        save(1,  "Summer Dress",   "dress","Zara",      2999,  "https://images.unsplash.com/photo-1595777457583-95e059d581b8?w=400&h=400&fit=crop", l("XS","S","M","L","XL"),       l("Red","White","Yellow"),            null,null,null);
        save(2,  "Formal Dress",   "dress","Mango",     6999,  "https://images.unsplash.com/photo-1566174053879-31528523f8ae?w=400&h=400&fit=crop", l("S","M","L","XL"),            l("Black","Navy","Maroon"),           null,null,null);
        save(3,  "Casual Dress",   "dress","H&M",       1999,  "https://images.unsplash.com/photo-1490481651871-ab68de25d43d?w=400&h=400&fit=crop", l("XS","S","M","L","XL","XXL"), l("Blue","Pink","White"),             null,null,null);
        save(4,  "Floral Dress",   "dress","Forever21", 2499,  "https://images.unsplash.com/photo-1612336307429-8a898d10e223?w=400&h=400&fit=crop", l("XS","S","M","L"),            l("Floral Pink","Floral Blue"),       null,null,null);
        save(22, "Maxi Dress",     "dress","Fabindia",  3499,  "https://images.unsplash.com/photo-1518462592603-0b9e0eb26a66?w=400&h=400&fit=crop", l("S","M","L","XL"),            l("Orange","Teal","Purple"),          null,null,null);
        save(23, "Evening Gown",   "dress","Mango",     12999, "https://images.unsplash.com/photo-1519657337289-077653f724ed?w=400&h=400&fit=crop", l("S","M","L"),                 l("Black","Gold","Wine"),             null,null,null);
        save(24, "Office Dress",   "dress","Zara",      4999,  "https://images.unsplash.com/photo-1548690312-e3b507d8c110?w=400&h=400&fit=crop", l("XS","S","M","L","XL"),       l("Grey","Black","White"),            null,null,null);
        save(101,"Wrap Dress",     "dress","H&M",       2799,  "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=400&h=400&fit=crop", l("XS","S","M","L","XL"),       l("Red","Black","Green"),             null,null,null);
        save(102,"Bodycon Dress",  "dress","Forever21", 1999,  "https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=400&h=400&fit=crop", l("XS","S","M","L"),            l("Black","White","Red"),             null,null,null);
        save(103,"Midi Dress",     "dress","Zara",      3999,  "https://images.unsplash.com/photo-1539008835657-9e8e9680c956?w=400&h=400&fit=crop", l("S","M","L","XL"),            l("Beige","Dusty Rose","Olive"),      null,null,null);
        save(104,"Shirt Dress",    "dress","Mango",     3499,  "https://images.unsplash.com/photo-1583744946564-b52ac1c389c8?w=400&h=400&fit=crop", l("XS","S","M","L","XL"),       l("White","Stripes","Blue"),          null,null,null);
        save(105,"Boho Dress",     "dress","Fabindia",  2999,  "https://images.unsplash.com/photo-1469334031218-e382a71b716b?w=400&h=400&fit=crop", l("S","M","L","XL"),            l("Multicolor","Brown","Cream"),      null,null,null);
        save(106,"Sundress",       "dress","H&M",       1799,  "https://images.unsplash.com/photo-1502716119720-b23a93e5fe1b?w=400&h=400&fit=crop", l("XS","S","M","L"),            l("Yellow","Sky Blue","Coral"),       null,null,null);
        save(107,"Slip Dress",     "dress","Zara",      2299,  "https://images.unsplash.com/photo-1525507119028-ed4c629a60a3?w=400&h=400&fit=crop", l("XS","S","M","L"),            l("Black","Champagne","Sage"),        null,null,null);
        save(108,"Cocktail Dress", "dress","Mango",     7999,  "https://images.unsplash.com/photo-1510553786469-77aaba570a1e?w=400&h=400&fit=crop", l("S","M","L"),                 l("Black","Red","Emerald"),           null,null,null);
        save(201,"Denim Dress",    "dress","Levis",     3299,  "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=400&h=400&fit=crop", l("XS","S","M","L","XL","XXL"), l("Light Blue","Dark Blue","Black"),  null,null,null);
        save(202,"Kurta Dress",    "dress","Fabindia",  1999,  "https://images.unsplash.com/photo-1558769132-cb1aea458c5e?w=400&h=400&fit=crop", l("S","M","L","XL","XXL"),      l("Pink","Blue","Green","Yellow"),    null,null,null);
        save(203,"Linen Dress",    "dress","H&M",       2599,  "https://images.unsplash.com/photo-1619086303291-0ef7699e4b31?w=400&h=400&fit=crop", l("XS","S","M","L","XL"),       l("White","Beige","Light Blue"),      null,null,null);
        save(204,"Printed Maxi",   "dress","Forever21", 2199,  "https://images.unsplash.com/photo-1583496661160-fb5886a0aaaa?w=400&h=400&fit=crop", l("XS","S","M","L"),            l("Tropical Print","Abstract"),       null,null,null);

        // ── SHOES ──────────────────────────────────────────────
        save(301,"Air Max 270",          "shoes","Nike",        8999,  "https://images.unsplash.com/photo-1542291026-7eec264c27ff?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("Black/White","Red/Black","Blue/White"), null,null,null);
        save(302,"Revolution 6",         "shoes","Nike",        5999,  "https://images.unsplash.com/photo-1606107557195-0e29a4b5b4aa?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("Black","White","Grey"),               null,null,null);
        save(303,"Air Force 1",          "shoes","Nike",        7499,  "https://images.unsplash.com/photo-1600185365926-3a2ce3cdb9eb?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("White","Black","Grey"),               null,null,null);
        save(304,"Ultraboost 23",        "shoes","Adidas",      14999, "https://images.unsplash.com/photo-1608231387042-66d1773d3028?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("Core Black","Cloud White","Solar Red"),null,null,null);
        save(305,"Stan Smith",           "shoes","Adidas",      6999,  "https://images.unsplash.com/photo-1525966222134-fcfa99b8ae77?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("White/Green","White/Navy"),           null,null,null);
        save(306,"Superstar Sneakers",   "shoes","Adidas",      7999,  "https://images.unsplash.com/photo-1597248881519-db089f3b5f34?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("White/Black","Black/Gold"),           null,null,null);
        save(307,"RS-X Sneakers",        "shoes","Puma",        6499,  "https://images.unsplash.com/photo-1556906781-9a412961a28c?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("White/Blue","Black/Red"),             null,null,null);
        save(308,"Suede Classic",        "shoes","Puma",        5999,  "https://images.unsplash.com/photo-1514989940723-e8e51635b782?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("Navy","Black","Red"),                 null,null,null);
        save(309,"Nitro Elite",          "shoes","Puma",        9999,  "https://images.unsplash.com/photo-1539185441755-769473a23570?w=400&h=400&fit=crop", l("7","8","9","10","11"),     l("Black","White","Blue"),               null,null,null);
        save(310,"Classic Leather",      "shoes","Reebok",      5499,  "https://images.unsplash.com/photo-1605348532760-6753d2c43329?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("White","Black","Navy"),               null,null,null);
        save(311,"Nano X3 Training",     "shoes","Reebok",      7999,  "https://images.unsplash.com/photo-1595950653106-6c9ebd614d3a?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("Black","Grey","Blue"),                null,null,null);
        save(312,"Fresh Foam 1080",      "shoes","New Balance", 12999, "https://images.unsplash.com/photo-1491553895911-0055eca6402d?w=400&h=400&fit=crop", l("7","8","9","10","11"),     l("Black","White","Grey"),               null,null,null);
        save(313,"574 Lifestyle",        "shoes","New Balance", 7999,  "https://images.unsplash.com/photo-1539185441755-769473a23570?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("Grey","Navy","Brown"),                null,null,null);
        save(314,"GEL-Kayano 30",        "shoes","Asics",       13999, "https://images.unsplash.com/photo-1584735175315-9d5df23be2cd?w=400&h=400&fit=crop", l("7","8","9","10","11"),     l("Black","Blue","White"),               null,null,null);
        save(315,"Flex Advantage",       "shoes","Skechers",    3999,  "https://images.unsplash.com/photo-1560769629-975ec94e6a86?w=400&h=400&fit=crop", l("6","7","8","9","10","11"), l("Black","Navy","Grey"),                null,null,null);
        save(316,"D'Lites Chunky",       "shoes","Skechers",    5499,  "https://images.unsplash.com/photo-1595341888016-a392ef81b7de?w=400&h=400&fit=crop", l("4","5","6","7","8","9"),   l("White/Silver","Black/Gold","Pink"),   null,null,null);
        save(317,"Chuck Taylor All Star","shoes","Converse",    4499,  "https://images.unsplash.com/photo-1494496195158-c3bc975fc97b?w=400&h=400&fit=crop", l("5","6","7","8","9","10","11"),l("White","Black","Red","Navy"),       null,null,null);
        save(318,"Run Swift 3.0",        "shoes","Nike",        4999,  "https://images.unsplash.com/photo-1579338559194-a162d19bf842?w=400&h=400&fit=crop", l("6","7","8","9","10"),     l("Black","White","Volt"),               null,null,null);

        // ── MOBILES ────────────────────────────────────────────
        save(401,"iPhone 15",        "mobiles","Apple",   79999,  "https://images.unsplash.com/photo-1695048133142-1a20484d2569?w=400&h=400&fit=crop", null,l("Black","Blue","Pink","Yellow","Green"),      l("128GB","256GB","512GB"),l("6GB"),   specs("display","6.1\" Super Retina XDR","processor","Apple A16 Bionic","battery","3877 mAh","os","iOS 17","camera","48 MP"));
        save(402,"iPhone 15 Pro",    "mobiles","Apple",   134999, "https://images.unsplash.com/photo-1678685888221-cda773a3dcdb?w=400&h=400&fit=crop", null,l("Natural Titanium","Blue Titanium","Black Titanium"),l("256GB","512GB","1TB"),l("8GB"),specs("display","6.1\" ProMotion OLED","processor","Apple A17 Pro (3nm)","battery","3274 mAh","os","iOS 17","camera","48 MP + 12 MP + 12 MP"));
        save(403,"iPhone 14",        "mobiles","Apple",   69999,  "https://images.unsplash.com/photo-1663499482523-1c0c1bae4ce1?w=400&h=400&fit=crop", null,l("Blue","Purple","Midnight","Starlight","Red"),l("128GB","256GB","512GB"),l("6GB"),   null);
        save(404,"Galaxy S24 Ultra", "mobiles","Samsung", 129999, "https://images.unsplash.com/photo-1709021045338-cd5c7f9edc81?w=400&h=400&fit=crop", null,l("Titanium Black","Titanium Gray","Titanium Violet"),l("256GB","512GB","1TB"),l("12GB"),specs("display","6.8\" Dynamic AMOLED 2X","processor","Snapdragon 8 Gen 3","battery","5000 mAh","camera","200 MP","os","Android 14"));
        save(405,"Galaxy S24+",      "mobiles","Samsung", 99999,  "https://images.unsplash.com/photo-1610945415295-d9bbf067e59c?w=400&h=400&fit=crop", null,l("Cobalt Violet","Onyx Black","Jade Green"),  l("256GB","512GB"),       l("12GB"),   null);
        save(406,"Galaxy A54",       "mobiles","Samsung", 38999,  "https://images.unsplash.com/photo-1610945264803-c22b62d2a7b3?w=400&h=400&fit=crop", null,l("Awesome Black","Awesome White","Awesome Violet"),l("128GB","256GB"),   l("8GB"),    null);
        save(407,"Redmi Note 13 Pro","mobiles","Xiaomi",  26999,  "https://images.unsplash.com/photo-1598327105666-5b89351aff97?w=400&h=400&fit=crop", null,l("Midnight Black","Arctic White","Lavender Purple"),l("128GB","256GB"), l("8GB"),    null);
        save(408,"OnePlus 12",       "mobiles","OnePlus", 64999,  "https://images.unsplash.com/photo-1585060544812-6b45742d762f?w=400&h=400&fit=crop", null,l("Flowy Emerald","Silky Black"),               l("256GB","512GB"),       l("12GB","16GB"),specs("display","6.82\" LTPO3 AMOLED","processor","Snapdragon 8 Gen 3","battery","5400 mAh","charging","100W SUPERVOOC","os","Android 14"));
        save(409,"OnePlus Nord CE4", "mobiles","OnePlus", 24999,  "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400&h=400&fit=crop", null,l("Dark Chrome","Light Green"),                 l("128GB","256GB"),       l("8GB"),    null);
        save(410,"Reno 11 Pro",      "mobiles","Oppo",    39999,  "https://images.unsplash.com/photo-1511707171634-5f897ff02aa9?w=400&h=400&fit=crop", null,l("Misty Green","Rock Grey"),                   l("256GB"),               l("12GB"),   null);
        save(411,"V30 5G",           "mobiles","Vivo",    31999,  "https://images.unsplash.com/photo-1567581935884-3349723552ca?w=400&h=400&fit=crop", null,l("Starry Black","Starry Purple"),               l("128GB","256GB"),       l("8GB"),    null);
        save(412,"GT 5 Pro",         "mobiles","Realme",  29999,  "https://images.unsplash.com/photo-1546054454-aa26e2b734c7?w=400&h=400&fit=crop", null,l("Fluid Silver","Matte Black"),                 l("256GB"),               l("12GB"),   null);
        save(413,"Moto G84",         "mobiles","Motorola",18999,  "https://images.unsplash.com/photo-1601784551446-20c9e07cdbdb?w=400&h=400&fit=crop", null,l("Viva Magenta","Marshmallow Blue"),             l("256GB"),               l("12GB"),   null);
        save(414,"Poco X6 Pro",      "mobiles","Poco",    26999,  "https://images.unsplash.com/photo-1580910051074-3eb694886505?w=400&h=400&fit=crop", null,l("Black","Yellow","Grey"),                     l("256GB","512GB"),       l("12GB"),   null);
        save(415,"ROG Phone 8",      "mobiles","Asus",    89999,  "https://images.unsplash.com/photo-1585338107529-13afc5f02586?w=400&h=400&fit=crop", null,l("Phantom Black","Storm White"),               l("256GB","512GB"),       l("16GB"),   null);
        save(416,"Pixel 8",          "mobiles","Google",  75999,  "https://images.unsplash.com/photo-1567581935884-3349723552ca?w=400&h=400&fit=crop", null,l("Hazel","Obsidian","Rose"),                   l("128GB","256GB"),       l("8GB"),    specs("display","6.2\" Actua OLED","processor","Google Tensor G3","battery","4575 mAh","os","Android 14","camera","50 MP + 12 MP"));

        // ── LAPTOPS ────────────────────────────────────────────
        save(501,"MacBook Air M2",    "laptops","Apple",     114999,"https://images.unsplash.com/photo-1517336714731-489689fd1ca8?w=400&h=400&fit=crop", null,l("Midnight","Starlight","Space Grey","Silver"),l("256GB","512GB","1TB"),l("8GB","16GB","24GB"),specs("display","13.6\" Liquid Retina","processor","Apple M2","battery","18 hrs","os","macOS Sonoma"));
        save(502,"MacBook Air M3",    "laptops","Apple",     134999,"https://images.unsplash.com/photo-1611186871348-b1ce696e52c9?w=400&h=400&fit=crop", null,l("Midnight","Starlight","Space Grey","Silver"),l("256GB","512GB","1TB"),l("8GB","16GB","24GB"),specs("display","13.6\" Liquid Retina","processor","Apple M3","battery","18 hrs","os","macOS Sonoma"));
        save(503,"MacBook Pro M3",    "laptops","Apple",     169999,"https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400&h=400&fit=crop", null,l("Space Black","Silver"),                      l("512GB","1TB","2TB"),   l("18GB","36GB"),       specs("display","14\" Liquid Retina XDR","processor","Apple M3 Pro","battery","18 hrs","os","macOS Sonoma"));
        save(504,"XPS 15",            "laptops","Dell",      149999,"https://images.unsplash.com/photo-1593642632559-0c6d3fc62b89?w=400&h=400&fit=crop", null,l("Platinum Silver","Graphite"),               l("512GB","1TB","2TB"),   l("16GB","32GB","64GB"),specs("display","15.6\" OLED 4K+","processor","Intel Core i7-13700H","gpu","RTX 4060 8GB","os","Windows 11"));
        save(505,"Inspiron 15",       "laptops","Dell",      55999, "https://images.unsplash.com/photo-1588702547919-26089e690ecc?w=400&h=400&fit=crop", null,l("Silver","Black"),                           l("512GB","1TB"),         l("16GB","32GB"),       null);
        save(506,"ThinkPad X1 Carbon","laptops","Lenovo",    139999,"https://images.unsplash.com/photo-1496181133206-80ce9b88a853?w=400&h=400&fit=crop", null,l("Black"),                                    l("512GB","1TB"),         l("16GB","32GB"),       specs("display","14\" IPS Anti-glare","processor","Intel Core i7-1365U","os","Windows 11 Pro","weight","1.12 kg"));
        save(507,"IdeaPad Slim 5",    "laptops","Lenovo",    62999, "https://images.unsplash.com/photo-1593642634524-b40b5baae6bb?w=400&h=400&fit=crop", null,l("Storm Grey","Abyss Blue"),                  l("512GB","1TB"),         l("16GB"),              null);
        save(508,"Spectre x360",      "laptops","HP",        149999,"https://images.unsplash.com/photo-1517694712202-14dd9538aa97?w=400&h=400&fit=crop", null,l("Nightfall Black","Natural Silver"),          l("512GB","1TB"),         l("16GB","32GB"),       specs("display","14\" OLED Touch 2.8K","processor","Intel Core i7-1355U","os","Windows 11 Home"));
        save(509,"Pavilion 15",       "laptops","HP",        54999, "https://images.unsplash.com/photo-1525547719571-a2d4ac8945e2?w=400&h=400&fit=crop", null,l("Natural Silver","Warm Gold"),               l("512GB","1TB"),         l("16GB"),              null);
        save(510,"Surface Pro 9",     "laptops","Microsoft", 124999,"https://images.unsplash.com/photo-1542744173-8e7e53415bb0?w=400&h=400&fit=crop", null,l("Platinum","Graphite","Sapphire"),             l("256GB","512GB","1TB"), l("16GB","32GB"),       specs("display","13\" PixelSense Flow Touch","processor","Intel Core i5/i7","os","Windows 11 Home"));
        save(511,"Predator Helios 16","laptops","Acer",      139999,"https://images.unsplash.com/photo-1603302576837-37561b2e2302?w=400&h=400&fit=crop", null,l("Abyssal Black"),                            l("1TB","2TB"),           l("16GB","32GB"),       specs("display","16\" IPS 165Hz","processor","Intel Core i9-13900HX","gpu","RTX 4080 12GB","os","Windows 11"));
        save(512,"Swift 5",           "laptops","Acer",      79999, "https://images.unsplash.com/photo-1484788984921-03950022c9ef?w=400&h=400&fit=crop", null,l("Mist Green","Obsidian Black"),              l("512GB","1TB"),         l("16GB"),              null);
        save(513,"ROG Zephyrus G14",  "laptops","Asus",      124999,"https://images.unsplash.com/photo-1518770660439-4636190af475?w=400&h=400&fit=crop", null,l("Eclipse Gray","Platinum White"),            l("512GB","1TB"),         l("16GB","32GB"),       specs("display","14\" QHD+ OLED 165Hz","processor","AMD Ryzen 9 7940HS","gpu","RTX 4070 8GB","os","Windows 11"));
        save(514,"VivoBook 15",       "laptops","Asus",      49999, "https://images.unsplash.com/photo-1541807084-5c52b6b3adef?w=400&h=400&fit=crop", null,l("Indie Black","Silver Blue"),                 l("512GB","1TB"),         l("8GB","16GB"),        null);

        // ── ELECTRONICS ────────────────────────────────────────
        save(9,  "WH-1000XM5",     "electronics","Sony",    28999,"https://images.unsplash.com/photo-1505740420928-5e560c06d30e?w=400&h=400&fit=crop", null,l("Black","Silver"),null,null,null);
        save(10, "Galaxy Watch 6", "electronics","Samsung", 29999,"https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=400&h=400&fit=crop", null,l("Graphite","Silver","Gold"),null,null,null);
        save(29, "iPad Air M2",    "electronics","Apple",   59900,"https://images.unsplash.com/photo-1544244015-0df4b3ffc6b0?w=400&h=400&fit=crop", null,l("Blue","Starlight","Purple","Space Grey"),l("64GB","256GB"),null,null);
        save(30, "AirPods Pro 2",  "electronics","Apple",   24900,"https://images.unsplash.com/photo-1606841837239-c5a1a4a07af7?w=400&h=400&fit=crop", null,l("White"),null,null,null);
        save(130,"QuietComfort II","electronics","Bose",    24900,"https://images.unsplash.com/photo-1590658268037-6bf12165a8df?w=400&h=400&fit=crop", null,l("Triple Black","Soapstone"),null,null,null);
        save(131,"Galaxy Tab S9",  "electronics","Samsung", 72999,"https://images.unsplash.com/photo-1561154464-82e9adf32764?w=400&h=400&fit=crop", null,l("Graphite","Beige"),l("128GB","256GB"),null,null);
        save(132,"Apple Watch S9", "electronics","Apple",   41900,"https://images.unsplash.com/photo-1551816230-ef5deaed4a26?w=400&h=400&fit=crop", null,l("Midnight","Starlight","Pink","Silver"),null,null,null);
        save(133,"Sony WF-1000XM5","electronics","Sony",   19990,"https://images.unsplash.com/photo-1608043152269-423dbba4e7e1?w=400&h=400&fit=crop", null,l("Black","Silver"),null,null,null);

        // ── LIFESTYLE ──────────────────────────────────────────
        save(5,  "Yoga Mat",          "lifestyle","Decathlon",1499,"https://images.unsplash.com/photo-1544367567-0f2fcb009e0b?w=400&h=400&fit=crop", null,l("Purple","Blue","Black"),null,null,null);
        save(6,  "Steel Water Bottle","lifestyle","Cello",    599, "https://images.unsplash.com/photo-1602143407151-7111542de6e8?w=400&h=400&fit=crop", null,l("Silver","Black","Blue"),null,null,null);
        save(7,  "Skincare Set",      "lifestyle","Lakme",   2299,"https://images.unsplash.com/photo-1556229010-6c3f2c9bbe0d?w=400&h=400&fit=crop", null,null,null,null,null);
        save(26, "Sunglasses",        "lifestyle","Ray-Ban", 4999,"https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=400&h=400&fit=crop", null,l("Black","Brown","Gold"),null,null,null);
        save(28, "Smart Band 7",      "lifestyle","Mi",      2499,"https://images.unsplash.com/photo-1575311373937-040b8e1fd5b6?w=400&h=400&fit=crop", null,l("Black","Orange","Olive"),null,null,null);
        save(140,"Perfume Noir",      "lifestyle","Fogg",    1299,"https://images.unsplash.com/photo-1541643600914-78b084683702?w=400&h=400&fit=crop", null,null,null,null,null);
        save(141,"Leather Wallet",    "lifestyle","Fastrack",1799,"https://images.unsplash.com/photo-1627123424574-724758594785?w=400&h=400&fit=crop", null,l("Black","Brown"),null,null,null);
        save(142,"Running Backpack",  "lifestyle","Wildcraft",2999,"https://images.unsplash.com/photo-1553062407-98eeb64c6a62?w=400&h=400&fit=crop", null,l("Black","Blue","Grey"),null,null,null);

        // ── GAMING ─────────────────────────────────────────────
        save(13, "BlackWidow V4 Keyboard","gaming","Razer",     9999, "https://images.unsplash.com/photo-1511467687858-23d96c32e4ae?w=400&h=400&fit=crop", null,l("Black"),null,null,null);
        save(14, "DeathAdder V3 Mouse",   "gaming","Razer",     5999, "https://images.unsplash.com/photo-1527864559417-7d286ad861a0?w=400&h=400&fit=crop", null,l("Black","White"),null,null,null);
        save(16, "DualSense Controller",  "gaming","Sony",      5490, "https://images.unsplash.com/photo-1606144042614-b2417e99c4e3?w=400&h=400&fit=crop", null,l("White","Midnight Black","Cosmic Red"),null,null,null);
        save(17, "Meta Quest 3",          "gaming","Meta",     49999, "https://images.unsplash.com/photo-1617802690992-15d3a4c818c8?w=400&h=400&fit=crop", null,l("White"),null,null,null);
        save(33, "Gaming Chair Pro",      "gaming","Green Soul",15999,"https://images.unsplash.com/photo-1598550476439-6847785fcea6?w=400&h=400&fit=crop", null,l("Black/Red","Black/Blue","Black/White"),null,null,null);
        save(150,"Kraken V3 Headset",     "gaming","Razer",     7999, "https://images.unsplash.com/photo-1599669454699-248893623440?w=400&h=400&fit=crop", null,l("Black"),null,null,null);
        save(151,"Xbox Controller",       "gaming","Microsoft", 5490, "https://images.unsplash.com/photo-1612287230202-1ff1d85d1bdf?w=400&h=400&fit=crop", null,l("Carbon Black","Robot White","Shock Blue"),null,null,null);
        save(152,"27\" Gaming Monitor",   "gaming","LG",       29999, "https://images.unsplash.com/photo-1527443224154-c4a3942d3acf?w=400&h=400&fit=crop", null,l("Black"),null,null,null);

        // ── OTHER ──────────────────────────────────────────────
        save(18, "Coffee Maker",       "other","Philips", 5999, "https://images.unsplash.com/photo-1495474472287-4d71bcdd2085?w=400&h=400&fit=crop", null,null,null,null,null);
        save(19, "Air Fryer 4L",       "other","Philips", 6999, "https://images.unsplash.com/photo-1585238341267-1cfec2046952?w=400&h=400&fit=crop", null,l("Black","White"),null,null,null);
        save(20, "Mixer Grinder 750W", "other","Bajaj",   2499, "https://images.unsplash.com/photo-1570222094114-d1a3c138fc1c?w=400&h=400&fit=crop", null,l("White"),null,null,null);
        save(36, "Microwave Oven 20L", "other","LG",      8499, "https://images.unsplash.com/photo-1585659722983-3a675dabf23d?w=400&h=400&fit=crop", null,l("Silver","Black"),null,null,null);
        save(38, "Cyclone V10 Vacuum", "other","Dyson",  29999, "https://images.unsplash.com/photo-1558317374-067fb5f30001?w=400&h=400&fit=crop", null,l("Nickel/Iron Blue"),null,null,null);
        save(160,"Instant Pot 6L",     "other","Instant", 8999, "https://images.unsplash.com/photo-1585837575652-267686107673?w=400&h=400&fit=crop", null,l("Silver"),null,null,null);
        save(161,"LED Smart TV 43\"",  "other","Mi",     29999, "https://images.unsplash.com/photo-1593359677879-a4bb92f4e1a7?w=400&h=400&fit=crop", null,l("Black"),null,null,null);
        save(162,"Room Heater",        "other","Bajaj",   2999, "https://images.unsplash.com/photo-1585771724684-38269d6639fd?w=400&h=400&fit=crop", null,l("White","Black"),null,null,null);
    }
}