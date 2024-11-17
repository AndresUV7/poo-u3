package views;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class SemifinalsView extends StageView {
    private static final String TOP_BORDER = "┌───────────────────┐";
    private static final String BOTTOM_BORDER = "└───────────────────┘";
    private static final String VERTICAL_LINE = "│";
    
    @Override
    public void showStageDetails(String teamName1, String teamName2, int matchNumber) {
        // Center team names (assuming max length of 16 characters)
        String team1Formatted = centerString(teamName1, 16);
        String team2Formatted = centerString(teamName2, 16);
        
        // Print the match box
        System.out.println("\n     SEMIFINALES     ");
        System.out.println("    Match #" + matchNumber);
        System.out.println(TOP_BORDER);
        System.out.println(VERTICAL_LINE + team1Formatted );
        System.out.println(BOTTOM_BORDER);
        System.out.println("\n    VS.    ");
        System.out.println(TOP_BORDER);
        System.out.println(VERTICAL_LINE + team2Formatted);
        System.out.println(BOTTOM_BORDER);
        System.out.println("\n");
    }
    
    private String centerString(String text, int width) {
        if (text == null || width <= text.length()) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        StringBuilder result = new StringBuilder();
        
        // Add left padding
        result.append(" ".repeat(padding));
        // Add text
        result.append(text);
        // Add right padding (accounting for odd lengths)
        result.append(" ".repeat(width - padding - text.length()));
        
        return result.toString();
    }
}