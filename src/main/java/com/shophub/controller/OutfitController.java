package com.shophub.controller;

import com.shophub.dto.ApiResponse;
import com.shophub.dto.OutfitRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * Outfit Studio REST API
 *
 * POST /api/outfit/recommend   — returns outfit product recommendations for a body type
 * GET  /api/outfit/body-types  — lists supported body types
 */
@RestController
@RequestMapping("/api/outfit")
public class OutfitController {

    // ── Static outfit data (ported from outfit.js OUTFIT_CATALOG) ────────────

    record OutfitProduct(
        String id, String brand, String name, String category,
        double price, Double originalPrice, int matchPercent,
        List<String> colors, String image, String styleTip
    ) {}

    private static final Map<String, List<OutfitProduct>> CATALOG = new LinkedHashMap<>();
    private static final Map<String, String> AI_ADVICE = new LinkedHashMap<>();

    static {
        AI_ADVICE.put("Slim",
            "Your slender frame works beautifully with volume and texture. " +
            "Oversized linens, wide-leg trousers, and padded shoulders add visual weight " +
            "in all the right places — making your silhouette look effortlessly curated.");
        AI_ADVICE.put("Athletic",
            "Your V-shaped torso and defined build are your greatest style asset. " +
            "Relaxed-fit tops soften the shoulder line while tapered trousers follow " +
            "your natural shape. The bomber jacket selection is particularly flattering.");
        AI_ADVICE.put("Medium",
            "Your balanced proportions give you incredible styling freedom. " +
            "Versatile pieces emphasise your natural waist while allowing easy transitions " +
            "between casual and smart occasions.");
        AI_ADVICE.put("Heavy",
            "Your curves are a powerful style statement — celebrate them! " +
            "Empire waists, V-necklines, and A-line cuts are your best friends. " +
            "The dark denim bootcut and ponte blazer combination is especially striking.");

        CATALOG.put("Slim", List.of(
            new OutfitProduct("ot1","ZARA","Layered Linen Oversized Shirt","tops",3499,4999.0,98,
                List.of("#F5E6D3","#D4C5A9","#8B7355"),
                "https://images.unsplash.com/photo-1596755094514-f87e34085b2c?w=500&h=650&fit=crop",
                "Adds volume to slender frames"),
            new OutfitProduct("ot2","H&M","Wide-Leg Linen Trousers","bottoms",2299,null,96,
                List.of("#E8DDD0","#2C2C2C","#6B8FAB"),
                "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=500&h=650&fit=crop",
                "Wide leg balances silhouette"),
            new OutfitProduct("ot3","MANGO","Padded Shoulder Blazer","outerwear",6999,8999.0,94,
                List.of("#2C2C2C","#D4C5A9","#8B4513"),
                "https://images.unsplash.com/photo-1594938298603-c8148c4dae35?w=500&h=650&fit=crop",
                "Structured shoulders add definition"),
            new OutfitProduct("ot4","COS","Striped Cotton Knit Sweater","tops",4299,null,92,
                List.of("#FFFFFF","#1A1A2E","#E8DDD0"),
                "https://images.unsplash.com/photo-1434389677669-e08b4cac3105?w=500&h=650&fit=crop",
                "Horizontal stripes add visual width"),
            new OutfitProduct("ot5","UNIQLO","Slim-Fit Chino Trousers","bottoms",1999,2499.0,90,
                List.of("#C8A882","#2C2C2C","#6B7FA0"),
                "https://images.unsplash.com/photo-1473966968600-fa801b869a1a?w=500&h=650&fit=crop",
                "Classic slim silhouette"),
            new OutfitProduct("ot6","ASOS","Floral Midi Wrap Dress","dresses",3799,4499.0,88,
                List.of("#F2C4CE","#7BC8A4","#F7DC6F"),
                "https://images.unsplash.com/photo-1515886657613-9f3515b0c78f?w=500&h=650&fit=crop",
                "Wrap style creates waist definition")
        ));

        CATALOG.put("Athletic", List.of(
            new OutfitProduct("ot9","RALPH LAUREN","Classic Oxford Button-Down","tops",5499,6999.0,98,
                List.of("#FFFFFF","#C6D4E8","#FFC0CB"),
                "https://images.unsplash.com/photo-1602810318383-e386cc2a3ccf?w=500&h=650&fit=crop",
                "Relaxed collar softens broad shoulders"),
            new OutfitProduct("ot10","LEVI'S","502 Taper Fit Jeans","bottoms",4999,null,96,
                List.of("#1C3A5F","#3D2B1F","#2C2C2C"),
                "https://images.unsplash.com/photo-1542272604-787c3835535d?w=500&h=650&fit=crop",
                "Tapered leg balances muscular build"),
            new OutfitProduct("ot11","TOMMY","Structured Bomber Jacket","outerwear",7999,9999.0,94,
                List.of("#2C2C2C","#556B2F","#8B0000"),
                "https://images.unsplash.com/photo-1591047139829-d91aecb6caea?w=500&h=650&fit=crop",
                "Enhances V-shaped torso naturally"),
            new OutfitProduct("ot12","ZARA","V-Neck Fitted Polo","tops",2799,null,92,
                List.of("#1B4F72","#148F77","#2C2C2C"),
                "https://images.unsplash.com/photo-1578662996442-48f60103fc96?w=500&h=650&fit=crop",
                "V-neck elongates chest line"),
            new OutfitProduct("ot15","RAY-BAN","Wayfarer Sunglasses","accessories",6999,null,85,
                List.of("#2C2C2C","#8B4513","#003153"),
                "https://images.unsplash.com/photo-1572635196237-14b3f281503f?w=500&h=650&fit=crop",
                "Classic frame for strong features")
        ));

        CATALOG.put("Medium", List.of(
            new OutfitProduct("ot17","ZARA","Relaxed Linen Suit Set","outerwear",9999,12999.0,98,
                List.of("#E8DDD0","#C8A882","#2C2C2C"),
                "https://images.unsplash.com/photo-1593030761757-71fae45fa0e7?w=500&h=650&fit=crop",
                "Versatile for all occasions"),
            new OutfitProduct("ot18","MANGO","Monochrome Midi Dress","dresses",4499,5499.0,96,
                List.of("#2C2C2C","#FFFFFF","#C4B5A0"),
                "https://images.unsplash.com/photo-1539008835657-9e8e9680c956?w=500&h=650&fit=crop",
                "Vertical lines create elongation"),
            new OutfitProduct("ot19","H&M","Cotton Blend Crew-Neck Tee","tops",999,null,94,
                List.of("#FFFFFF","#2C2C2C","#9B59B6","#E74C3C"),
                "https://images.unsplash.com/photo-1618354691373-d851c5c3a990?w=500&h=650&fit=crop",
                "Timeless staple for any look"),
            new OutfitProduct("ot20","LEVI'S","Classic Straight Jeans","bottoms",4799,5499.0,92,
                List.of("#1C3A5F","#2C2C2C","#8B7355"),
                "https://images.unsplash.com/photo-1604176354204-9268737828e4?w=500&h=650&fit=crop",
                "Universal fit, effortless styling"),
            new OutfitProduct("ot23","FOSSIL","Minimalist Leather Watch","accessories",8999,10999.0,86,
                List.of("#C0A882","#2C2C2C","#FFFFFF"),
                "https://images.unsplash.com/photo-1523275335684-37898b6baf30?w=500&h=650&fit=crop",
                "Elevates any outfit instantly")
        ));

        CATALOG.put("Heavy", List.of(
            new OutfitProduct("ot25","ELOQUII","Empire Waist Maxi Dress","dresses",5999,7499.0,98,
                List.of("#2C2C2C","#1C3A5F","#556B2F"),
                "https://images.unsplash.com/photo-1496747611176-843222e1e57c?w=500&h=650&fit=crop",
                "Empire cut highlights your waist"),
            new OutfitProduct("ot26","UNIVERSAL STANDARD","Tailored Wrap Blouse","tops",4299,4999.0,96,
                List.of("#FFFFFF","#F2C4CE","#C6D4E8"),
                "https://images.unsplash.com/photo-1469334031218-e382a71b716b?w=500&h=650&fit=crop",
                "V-wrap neckline elongates visually"),
            new OutfitProduct("ot27","TORRID","Bootcut Dark Denim","bottoms",3999,4799.0,94,
                List.of("#1C3A5F","#2C2C2C"),
                "https://images.unsplash.com/photo-1541099649105-f69ad21f3246?w=500&h=650&fit=crop",
                "Bootcut balances hips beautifully"),
            new OutfitProduct("ot28","CITY CHIC","A-Line Midi Skirt","bottoms",3499,null,92,
                List.of("#2C2C2C","#8B0000","#1C3A5F"),
                "https://images.unsplash.com/photo-1572804013309-59a88b7e92f1?w=500&h=650&fit=crop",
                "A-line glides over hips with ease"),
            new OutfitProduct("ot29","ADDITION ELLE","Ponte Blazer","outerwear",7499,8999.0,90,
                List.of("#2C2C2C","#36454F","#8B0000"),
                "https://images.unsplash.com/photo-1619086303291-0ef7699e4b31?w=500&h=650&fit=crop",
                "Creates a confident silhouette")
        ));
    }

    // ── Endpoints ─────────────────────────────────────────────────────────────

    @GetMapping("/body-types")
    public ResponseEntity<ApiResponse<List<String>>> getBodyTypes() {
        return ResponseEntity.ok(ApiResponse.ok(List.of("Slim","Athletic","Medium","Heavy")));
    }

    /**
     * Returns outfit recommendations for the given body type and style preferences.
     *
     * Request:  { "bodyType": "Athletic", "stylePrefs": ["Casual","Smart-Casual"] }
     * Response: { products: [...], advice: "...", bodyType: "Athletic" }
     */
    @PostMapping("/recommend")
    public ResponseEntity<ApiResponse<Map<String, Object>>> recommend(
            @Valid @RequestBody OutfitRequest req) {

        String bodyType = req.bodyType;
        List<OutfitProduct> products = CATALOG.getOrDefault(bodyType, CATALOG.get("Medium"));
        String advice = AI_ADVICE.getOrDefault(bodyType, AI_ADVICE.get("Medium"));

        Map<String, Object> payload = new LinkedHashMap<>();
        payload.put("bodyType",   bodyType);
        payload.put("stylePrefs", req.stylePrefs != null ? req.stylePrefs : List.of());
        payload.put("advice",     advice);
        payload.put("products",   products);
        payload.put("count",      products.size());

        return ResponseEntity.ok(ApiResponse.ok("Outfit recommendations ready", payload));
    }
}
