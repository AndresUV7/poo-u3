package views;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class RoundOf16View extends StageView {
    private static final String TOP_BORDER    = "╔════════════════════╗";
    private static final String BOTTOM_BORDER = "╚════════════════════╝";
    private static final String MIDDLE_BORDER = "╠════════════════════╣";
    private static final String VERTICAL_LINE = "║";
    
    @Override
    public void showStageDetails(String teamName1, String teamName2, int matchNumber) {
        String team1Formatted = centerString(teamName1, 16);
        String team2Formatted = centerString(teamName2, 16);
        
        System.out.println("\n     OCTAVOS     ");
        System.out.println("    Match #" + matchNumber);
        System.out.println(TOP_BORDER);
        System.out.println(VERTICAL_LINE + team1Formatted + VERTICAL_LINE);
        System.out.println(MIDDLE_BORDER);
        System.out.println(VERTICAL_LINE + team2Formatted + VERTICAL_LINE);
        System.out.println(BOTTOM_BORDER);
        System.out.println("\n");

    }
    
    private String centerString(String text, int width) {
        if (text == null || width <= text.length()) {
            return text;
        }
        int padding = (width - text.length()) / 2;
        StringBuilder result = new StringBuilder();
        
        result.append(" ".repeat(padding));
        result.append(text);
        result.append(" ".repeat(width - padding - text.length()));
        
        return result.toString();
    }
}