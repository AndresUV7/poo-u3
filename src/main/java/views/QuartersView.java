package views;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public class QuartersView extends StageView {
    private static final String BOX_BORDER = "┌───────────────┐";
    private static final String TEAM_DIVIDER = "├───────────────┤";

    @Override
    public void showStageDetails(String teamName1, String teamName2, int matchNumber) {
        String formattedTeam1 = formatTeamName(teamName1);
        String formattedTeam2 = formatTeamName(teamName2);

        System.out.println("\nCUARTOS - Match #" + matchNumber);
        System.out.println(BOX_BORDER);
        System.out.printf("│ %-14s │%n", formattedTeam1);
        System.out.println(TEAM_DIVIDER);
        System.out.printf("│ %-14s │%n", formattedTeam2);
        System.out.println(BOX_BORDER);
        System.out.println();
    }

    private String formatTeamName(String teamName) {
        if (teamName.length() <= 14) {
            return teamName;
        } else {
            return teamName.substring(0, 11) + "...";
        }
    }
}