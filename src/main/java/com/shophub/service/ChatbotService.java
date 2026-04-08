package com.shophub.service;

import org.springframework.stereotype.Service;

import java.util.*;

/**
 * Product usage chatbot — ported from chatbot.js.
 * Maps product keywords → usage/care instructions.
 * Uses the same fuzzy matching strategy as the original JS.
 */
@Service
public class ChatbotService {

    private static final Map<String, String> KNOWLEDGE_BASE = new LinkedHashMap<>();
    private static final String FALLBACK =
        "I don't have specific instructions for that product yet. " +
        "Try searching for a similar product (e.g., 'wireless headphones', " +
        "'gaming keyboard', 'summer dress'). You can also describe what you need help with!";

    static {
        // Dress & Fashion
        KNOWLEDGE_BASE.put("dress",
            "How to use your dress: 1) Check the care label for washing instructions. " +
            "2) Most dresses should be washed in cold water on gentle cycle. " +
            "3) Hang or lay flat to dry to prevent stretching. " +
            "4) Iron on low heat if needed, using a cloth between iron and fabric.");
        KNOWLEDGE_BASE.put("summer dress",
            "Summer dress care: Machine wash cold with like colors. Tumble dry low or hang dry. " +
            "Avoid direct sunlight when drying to prevent fading. Store folded or on a hanger.");
        KNOWLEDGE_BASE.put("formal dress",
            "Formal dress care: Dry clean recommended for delicate fabrics. " +
            "Steam to remove wrinkles instead of ironing. Store in a garment bag to protect from dust.");
        KNOWLEDGE_BASE.put("casual dress",
            "Casual dress usage: Wash in cold water, gentle cycle. Can be tumble dried on low. " +
            "Pair with sneakers for daytime or sandals for a relaxed look.");
        KNOWLEDGE_BASE.put("maxi dress",
            "Maxi dress care: Machine wash cold, gentle cycle. Hang dry to maintain length. " +
            "Pair with sandals or sneakers. Can be dressed up with heels for evening.");
        KNOWLEDGE_BASE.put("evening gown",
            "Evening gown care: Dry clean only for delicate fabrics. " +
            "Store in a breathable garment bag. Steam to remove wrinkles before wearing.");
        KNOWLEDGE_BASE.put("office dress",
            "Office dress usage: Follow care label — usually machine wash cold. " +
            "Iron on medium heat. Pair with blazers and professional footwear.");

        // Lifestyle
        KNOWLEDGE_BASE.put("yoga mat",
            "How to use a yoga mat: 1) Unroll with the textured side up for grip. " +
            "2) Clean with a damp cloth after each use. " +
            "3) Store rolled or flat in a cool, dry place. " +
            "4) Avoid direct sunlight to prevent degradation.");
        KNOWLEDGE_BASE.put("water bottle",
            "Water bottle usage: Rinse before first use. Hand wash or top-rack dishwasher safe. " +
            "Avoid hot liquids in plastic bottles. Clean the mouthpiece regularly.");
        KNOWLEDGE_BASE.put("skincare",
            "Skincare product usage: Apply to clean, dry skin. Use sunscreen as the last step " +
            "in morning routine. Patch test new products. " +
            "Follow the order: cleanser → toner → serum → moisturizer.");
        KNOWLEDGE_BASE.put("perfume",
            "How to use perfume: Apply to pulse points (wrists, neck). Spray from 6-8 inches away. " +
            "Don't rub wrists together — it breaks down the scent. Store away from heat and light.");
        KNOWLEDGE_BASE.put("sunglasses",
            "Sunglasses care: Clean with microfiber cloth and lens cleaner. " +
            "Store in a case when not in use. Avoid placing lens-down on surfaces.");
        KNOWLEDGE_BASE.put("backpack",
            "Backpack usage: Distribute weight evenly — heavier items close to your back. " +
            "Adjust straps for comfort. Clean with damp cloth. Don't overpack to avoid strain.");
        KNOWLEDGE_BASE.put("fitness tracker",
            "Fitness tracker setup: Charge fully, download the app, pair via Bluetooth. " +
            "Wear snugly on wrist. Sync data regularly. Clean band with mild soap and water.");
        KNOWLEDGE_BASE.put("smart band",
            "Smart band usage: Charge fully before first use. Pair with the companion app. " +
            "Wear snugly on your wrist. Rinse band with water, avoid submerging the unit.");

        // Electronics
        KNOWLEDGE_BASE.put("wireless headphones",
            "Wireless headphones usage: 1) Charge fully before first use. " +
            "2) Put in pairing mode (usually hold power 5-7 sec). " +
            "3) Connect via Bluetooth in device settings. " +
            "4) Use the buttons for play/pause, volume, and skip.");
        KNOWLEDGE_BASE.put("headphones",
            "Headphones usage: Connect via cable or Bluetooth. " +
            "Adjust volume gradually to protect hearing. " +
            "Clean earpads with a damp cloth. Store in a case when not in use.");
        KNOWLEDGE_BASE.put("airpods",
            "AirPods usage: Place in ears — left and right marked inside each bud. " +
            "They auto-connect to your paired iPhone/iPad. " +
            "Charge in the case. Clean mesh with a dry cotton swab.");
        KNOWLEDGE_BASE.put("smartwatch",
            "Smartwatch setup: 1) Charge the watch fully. " +
            "2) Download the companion app on your phone. " +
            "3) Pair via Bluetooth in the app. " +
            "4) Follow on-screen setup for notifications and health features.");
        KNOWLEDGE_BASE.put("laptop",
            "Laptop usage tips: Keep vents unobstructed for cooling. " +
            "Charge between 20-80% for battery longevity. " +
            "Use on a hard surface, not soft bedding. Update software regularly.");
        KNOWLEDGE_BASE.put("macbook",
            "MacBook usage: Use MagSafe or USB-C charger. " +
            "Enable FileVault for disk encryption. Keep macOS updated. " +
            "Clean screen with a slightly damp microfiber cloth.");
        KNOWLEDGE_BASE.put("phone",
            "Smartphone usage: Charge with original or certified charger. " +
            "Enable automatic backups. Use a screen protector and case. " +
            "Clear cache periodically for performance.");
        KNOWLEDGE_BASE.put("iphone",
            "iPhone usage: Use Apple or MFi-certified chargers. " +
            "Enable iCloud backup. Update iOS regularly. " +
            "Avoid exposure to liquids beyond the stated IP rating.");
        KNOWLEDGE_BASE.put("bluetooth speaker",
            "Bluetooth speaker: Turn on and enter pairing mode. " +
            "Connect from your device's Bluetooth settings. " +
            "Keep away from water unless rated waterproof. Charge when battery is low.");
        KNOWLEDGE_BASE.put("tablet",
            "Tablet usage: Charge before first use. " +
            "Install apps from the app store. Use a case and screen protector. " +
            "Keep software updated.");
        KNOWLEDGE_BASE.put("wireless earbuds",
            "Wireless earbuds: Charge the case fully. Remove from case to power on. " +
            "Pair via Bluetooth in device settings. Ensure proper fit for best sound. " +
            "Clean eartips regularly.");
        KNOWLEDGE_BASE.put("earbuds",
            "Earbuds usage: Insert with gentle twist for secure fit. " +
            "Pair via Bluetooth. Clean eartips with alcohol wipe. Store in case when not in use.");
        KNOWLEDGE_BASE.put("power bank",
            "Power bank usage: Charge fully before first use. " +
            "Use compatible cable for your device. Don't charge in extreme heat. " +
            "Store at 50% if not using for long periods.");
        KNOWLEDGE_BASE.put("camera",
            "Camera usage: Insert memory card and battery. Charge battery fully. " +
            "Set date/time. Use auto mode for beginners. " +
            "Transfer photos via USB or Wi-Fi.");

        // Gaming
        KNOWLEDGE_BASE.put("gaming keyboard",
            "Gaming keyboard usage: 1) Connect via USB or wireless dongle. " +
            "2) Install driver/software for RGB and macro customization. " +
            "3) Mechanical keys — no need to press fully down. " +
            "4) Clean with compressed air between keys.");
        KNOWLEDGE_BASE.put("gaming mouse",
            "Gaming mouse setup: Plug in or pair wirelessly. " +
            "Adjust DPI in software for sensitivity. " +
            "Program side buttons for game shortcuts. Use a mouse pad for consistent tracking.");
        KNOWLEDGE_BASE.put("gaming headset",
            "Gaming headset: Connect audio jack or USB. " +
            "Install software for surround sound. " +
            "Adjust mic position 1-2 inches from mouth. Mute mic when not speaking.");
        KNOWLEDGE_BASE.put("controller",
            "Controller usage: Charge or insert batteries. " +
            "Pair with console/PC via Bluetooth or USB. " +
            "Calibrate sticks in system settings. Store in a dry place when not in use.");
        KNOWLEDGE_BASE.put("dualsense",
            "DualSense controller: Charge via USB-C. " +
            "Pair with PS5 by pressing PS button. " +
            "Adaptive triggers and haptic feedback enhance gameplay. " +
            "Update firmware via PS5 settings.");
        KNOWLEDGE_BASE.put("vr headset",
            "VR headset usage: Adjust straps for comfortable fit. " +
            "Set up guardian/boundary for safe play area. " +
            "Clean lenses with microfiber cloth only. Take breaks every 30 minutes.");
        KNOWLEDGE_BASE.put("gaming chair",
            "Gaming chair usage: Adjust height so feet rest flat on floor. " +
            "Set lumbar support for lower back. " +
            "Recline for breaks. Clean with damp cloth. " +
            "Don't sit for more than 2 hours without stretching.");

        // Home & Kitchen
        KNOWLEDGE_BASE.put("coffee maker",
            "Coffee maker usage: Add water to reservoir. " +
            "Place filter and ground coffee. Turn on and wait for brew cycle. " +
            "Descale monthly with vinegar solution.");
        KNOWLEDGE_BASE.put("air fryer",
            "Air fryer tips: Preheat for 2-3 minutes. " +
            "Don't overcrowd the basket — leave space for air flow. " +
            "Shake basket halfway through cooking. Clean basket after each use.");
        KNOWLEDGE_BASE.put("blender",
            "Blender usage: Add liquid first, then solids. Don't fill above max line. " +
            "Start on low, increase speed gradually. Blend in short bursts for tough ingredients.");
        KNOWLEDGE_BASE.put("microwave",
            "Microwave usage: Use microwave-safe containers only. No metal or foil. " +
            "Cover food to prevent splatter. Stir halfway for even heating. Clean interior regularly.");
        KNOWLEDGE_BASE.put("electric kettle",
            "Electric kettle usage: Fill to max line. Close lid securely. " +
            "Plug in and switch on. Don't boil empty. Descale with vinegar monthly.");
        KNOWLEDGE_BASE.put("vacuum cleaner",
            "Vacuum cleaner usage: Empty dustbin when full. Replace or clean filters regularly. " +
            "Check brush roll for tangles. Use correct setting for floor type. Store cord properly.");
    }

    /**
     * Find usage instructions for a product query using exact + fuzzy matching.
     * Mirrors getProductUsage() in chatbot.js.
     */
    public String getResponse(String userInput) {
        if (userInput == null || userInput.isBlank()) return FALLBACK;

        String normalized = userInput.toLowerCase().trim();

        // 1. Exact match
        if (KNOWLEDGE_BASE.containsKey(normalized)) {
            return KNOWLEDGE_BASE.get(normalized);
        }

        // 2. Fuzzy match — find the most specific (longest) keyword contained in input
        String bestKey   = null;
        String bestValue = null;
        int bestScore    = 0;

        for (Map.Entry<String, String> entry : KNOWLEDGE_BASE.entrySet()) {
            String key = entry.getKey();
            if (normalized.contains(key) || key.contains(normalized)) {
                int score = Math.max(key.length(), normalized.length());
                if (score > bestScore) {
                    bestScore = score;
                    bestKey   = key;
                    bestValue = entry.getValue();
                }
            }
        }

        return bestValue != null ? bestValue : FALLBACK;
    }
}
