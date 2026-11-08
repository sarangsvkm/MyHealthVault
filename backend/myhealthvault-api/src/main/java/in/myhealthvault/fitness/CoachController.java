package in.myhealthvault.fitness;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping({"/api/v1/coach", "/api/coach"})
public class CoachController {

    @PostMapping("/ask")
    public ResponseEntity<Map<String, String>> askCoach(@RequestBody Map<String, String> request) {
        String query = request.getOrDefault("query", "").toLowerCase();
        String response = generateCoachResponse(query);
        return ResponseEntity.ok(Map.of("response", response));
    }

    private String generateCoachResponse(String query) {
        if (query.contains("porotta") || query.contains("parotta") || query.contains("beef")) {
            return "⚠️ Porotta & Beef Fry analysis:\n\n• Kerala Porotta is made of refined flour (Maida) and hydrogenated fats (Dalda/Vanaspati), carrying ~250-300 kcal per piece with zero fiber.\n• Beef Fry (Ularthiyathu) is high in protein but usually fried in heavy coconut oil, raising saturated fat content.\n\n💡 Healthy Swap:\n1. Choose Wheat Porotta or Chapati / Appam instead of normal Porotta.\n2. Swap Beef Fry for Beef Curry (gravy based) which uses less oil, or choose Fish Curry / Grilled Chicken.";
        }
        if (query.contains("biryani") || query.contains("mandi") || query.contains("manthi") || query.contains("al faham")) {
            return "🍛 Biryani, Mandi & Al Faham guidelines:\n\n• Chicken Biryani or Kuzhimanthi rice is cooked with ghee, animal fat, and high oil, averaging 650-800 kcal per plate.\n• Al Faham is charcoal-grilled, which makes it a great protein source! But watch out for hotel mayonnaise (which adds ~150 kcal per scoop).\n\n💡 Smart Eating Tips:\n1. Order Al Faham with Kubboos (Arabic flatbread) and Salad. Skip or limit mayonnaise.\n2. If eating Biryani/Mandi: Eat only half the rice, focus on the chicken piece, and ask for green salads.";
        }
        if (query.contains("breakfast") || query.contains("idli") || query.contains("dosa") || query.contains("puttu") || query.contains("appam")) {
            return "🥞 Kerala Breakfast nutritional ranking:\n\n1. Idli & Sambar (Grade: A+): Steamed, fermented, oil-free. High fiber and protein from Sambar lentils.\n2. Appam & Vegetable Stew (Grade: A): Appam is fermented rice batter, very light (~100 kcal). Stew contains coconut milk but is veggie-rich.\n3. Puttu & Kadala Curry (Grade: B+): Puttu is filling (~250 kcal). Kadala (black chickpeas) is an amazing plant-based protein source!\n4. Ghee Dosa / Poori Masala (Grade: C): High in refined oil/fats. Avoid for daily breakfast.";
        }
        if (query.contains("hotel") || query.contains("restaurant") || query.contains("eating out") || query.contains("dining")) {
            return "🏪 Dining Out Healthy in Kerala:\n\n• Look for steamed options like Puttu, Appam, or Idli.\n• Opt for Fish Curry (spicy, sour curry) over deep-fried fish.\n• Order a side of vegetable Thoran (cabbage, beans) or Sambar to ensure you get fiber.\n• Drink warm water (Jeeraka vellom or Karingali) instead of sweetened juices/sodas.";
        }
        if (query.contains("weight loss") || query.contains("diet") || query.contains("fat loss")) {
            return "📉 Weight Loss Blueprint:\n\n1. Calorie Deficit: Ensure you eat less than your daily goal (e.g. 1800 kcal). Log meals in the Food tab.\n2. Protein & Fiber: Increase lentils, fish, chicken breast, egg whites, and vegetable thorans.\n3. Hydration: Drink 3L of water. Water helps boost metabolism and controls hunger pangs.\n4. NEAT: Walk at least 8,000 steps daily. Use the Steps tab to track!";
        }
        if (query.contains("water") || query.contains("hydration")) {
            return "💧 Hydration Advice:\n\nAim for 3 litres of water daily. Drinking warm Jeeraka Vellom (cumin water) in the morning aids digestion. Avoid sugared juices and soft drinks — replace with coconut water or plain water between meals.";
        }
        return "🌴 Kerala Fit AI Coach:\n\nFor a healthy Kerala lifestyle: prioritize steamed foods (Idli, Appam, Puttu), choose fish curry over deep fried items, include Sambar and Avial for micronutrients, and walk at least 30 minutes daily. Log your meals consistently for best insights!";
    }
}
