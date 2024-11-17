package views;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class FinalView extends StageView {
    private static final String TOP_BORDER = "╔═══════════════════════════════════════════════╗";
    private static final String BOTTOM_BORDER = "╚═══════════════════════════════════════════════╝";
    private static final String MIDDLE_BORDER = "╠═══════════════════════════════════════════════╣";
    private static final String VERTICAL_LINE = "║";

    @Override
    public void showStageDetails(String teamName1, String teamName2, int matchNumber) {
        // Center team names (assuming max length of 24 characters)
        String team1Formatted = teamName1;
        String team2Formatted = teamName2;

        // Print the match box
        System.out.println("\n      FINAL      ");
        System.out.println("    Match #" + matchNumber);
        System.out.println(TOP_BORDER);
        System.out.println(VERTICAL_LINE + team1Formatted);
        System.out.println(MIDDLE_BORDER);
        System.out.println(VERTICAL_LINE + team2Formatted);
        System.out.println(BOTTOM_BORDER);
        System.out.println("\n");
    }
}