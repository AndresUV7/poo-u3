import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Scanner;
import java.util.Set;

import controllers.MatchDrawController;
import controllers.TeamFileHandler;
import controllers.TeamFileHandler.TeamValidationException;
import custom_exceptions.DuplicateTeamNameException;
import custom_exceptions.InvalidStageInputException;
import custom_exceptions.InvalidTeamInputException;
import models.Game;
import models.Stage;
import models.Team;
import views.FinalView;
import views.QuartersView;
import views.RoundOf16View;
import views.SemifinalsView;
import views.StageView;

public class FootballLeague {

    private static final Scanner input = new Scanner(System.in).useLocale(Locale.US);

    private static final Map<Integer, String> stageOptions = Map.of(
            1, "octavos",
            2, "cuartos",
            3, "semifinales",
            4, "final");

    public static void main(String[] args) {
        int stageMenuInput = getValidStage();
        // Check if user chose to exit in first menu
        if (stageMenuInput == 5) {
            System.out.println("Programa finalizado.");
            input.close();
            System.exit(0);
        }

        Stage initialStage = Stage.builder().name(stageOptions.get(stageMenuInput)).build();

        int teamsInputChoice = getValidTeamsInputChoice();
        // Check if user chose to exit in second menu
        if (teamsInputChoice == 3) {
            System.out.println("Programa finalizado.");
            input.close();
            System.exit(0);
        }

        int teamsQuantity = (int) Math.pow(2, 5 - stageMenuInput);

        List<Team> teams = new ArrayList<>();
        try {
            teams = teamsInputChoice == 1 ? getTeamsFromUserInput(teamsQuantity)
                    : TeamFileHandler.loadTeamsFromFile(initialStage);
        } catch (TeamValidationException e) {
            if (!e.getDuplicateTeams().isEmpty()) {
                System.err.println("\n ERROR: Se encontraron equipos duplicados en el archivo.");
                System.err.println("Por favor, corrija los siguientes duplicados y vuelva a ejecutar el programa:");
                for (String team : e.getDuplicateTeams()) {
                    System.err.println(" - " + team);
                }
            } else {
                System.err.println("\n ERROR: " + e.getMessage());
            }
            System.err.println("\nEl programa se cerrará. Por favor, corrija el archivo y vuelva a intentarlo.");
            System.exit(1);
        }

        generateStage(initialStage, teams);

        input.close();
    }

    private static int getValidStage() {
        System.out.println("""
                Por favor ingresa la opción de la etapa deseada:
                1. Octavos de final
                2. Cuartos de final
                3. Semifinales
                4. Final
                5. Salir
                """);

        return getUserChoice(1, 5, "Entrada inválida. Debe ingresar un número de la lista opciones.",
                InvalidStageInputException.class);
    }

    private static int getValidTeamsInputChoice() {
        System.out.println("""
                Por favor elige la opción para ingresar los equipos:
                1. Manual por teclado
                2. Cargar desde archivo (files/input-teams.txt)
                3. Salir
                """);

        return getUserChoice(1, 3, "Entrada inválida. Debe ingresar un número de la lista opciones.",
                InvalidTeamInputException.class);
    }

    private static List<Team> getTeamsFromUserInput(int teamsQuantity) {
        List<Team> teams = new ArrayList<>(teamsQuantity);
        Set<String> usedNames = new HashSet<>();

        for (int i = 0; i < teamsQuantity; i++) {
            String teamName;
            boolean validName = false;

            do {
                System.out.println("Por favor ingresa el nombre del equipo " + (i + 1) + ":");
                teamName = input.nextLine().trim();

                try {
                    if (teamName.isEmpty()) {
                        System.out.println("El nombre del equipo no puede estar vacío. Intenta nuevamente.");
                    } else if (usedNames.contains(teamName.toLowerCase())) {
                        throw new DuplicateTeamNameException(
                                "El nombre '" + teamName + "' ya existe. Por favor ingresa un nombre diferente.");
                    } else {
                        validName = true;
                    }
                } catch (DuplicateTeamNameException e) {
                    System.out.println(e.getMessage());
                }
            } while (!validName);

            usedNames.add(teamName.toLowerCase());
            teams.add(Team.builder().name(teamName).build());
        }
        return teams;
    }

    private static int getUserChoice(int min, int max, String errorMessage, Class<? extends Exception> exceptionClass) {
        while (true) {
            try {
                if (input.hasNextInt()) {
                    int choice = input.nextInt();
                    if (choice >= min && choice <= max) {
                        input.nextLine();
                        return choice;
                    }
                }
                throw exceptionClass.getConstructor(String.class).newInstance(errorMessage);
            } catch (Exception e) {
                System.out.println(e.getMessage());
                input.nextLine();
            }
        }
    }

    private static void generateStage(Stage stage, List<Team> teams) {
        // Si el nombre de la etapa está vacío, significa que hemos llegado al final del torneo
        // Se imprime el equipo campeón y se termina la recursión
        if (stage.getName().isEmpty()) {
            System.out.println("El campeón es: " + teams.get(0).getName());
            return;
        }
    
        // Determina qué tipo de vista usar según la etapa del torneo
        // Utiliza un operador ternario anidado para seleccionar la vista apropiada
        StageView stageView = stage.getName().equals(stageOptions.get(1))
                ? RoundOf16View.builder().build()  // Vista para octavos de final
                : stage.getName().equals(stageOptions.get(2))
                        ? QuartersView.builder().build()  // Vista para cuartos de final
                        : stage.getName().equals(stageOptions.get(3))
                                ? SemifinalsView.builder().build()  // Vista para semifinales
                                : stage.getName().equals(stageOptions.get(4))
                                        ? FinalView.builder().build()  // Vista para la final
                                        : null;
    
        // Genera el bracket (emparejamientos) para la etapa actual
        // Utiliza el controlador para crear los enfrentamientos entre equipos
        stage = MatchDrawController.generateBracket(teams, stage, stageView);
    
        // Simula los juegos de la etapa actual y obtiene la lista de equipos ganadores
        // que avanzarán a la siguiente fase
        List<Team> winners = playGamesAndGetWinners(stage);
    
        // Obtiene el nombre de la siguiente etapa del torneo
        String nextStageName = getNextStageName(stage.getName());
    
        // Si no hay siguiente etapa (nextStageName está vacío), se ha llegado al final
        // del torneo y se imprime el campeón
        if (nextStageName.isEmpty()) {
            System.out.println("El campeón es: " + winners.get(0).getName());
        } else {
            // Configura la siguiente etapa y continúa el torneo de forma recursiva
            // con los equipos ganadores
            stage.setName(nextStageName);
            generateStage(stage, winners);
        }
    }
    
    private static List<Team> playGamesAndGetWinners(Stage stage) {
        List<Team> winners = new ArrayList<>();
        for (Game game : stage.getGames()) {
            Team winner = null;
            boolean validInput = false;

            while (!validInput) {
                System.out.println(game.getTeam1().getName() + " vs " + game.getTeam2().getName());
                System.out.println("Por favor ingresa el equipo ganador:");
                System.out.println("1. " + game.getTeam1().getName());
                System.out.println("2. " + game.getTeam2().getName());

                try {
                    int winnerInput = input.nextInt();
                    input.nextLine();

                    if (winnerInput == 1 || winnerInput == 2) {
                        winner = winnerInput == 1 ? game.getTeam1() : game.getTeam2();
                        validInput = true;
                    } else {
                        System.out.println("Error: Debes ingresar 1 o 2. Por favor intenta nuevamente.");
                    }
                } catch (Exception e) {
                    System.out.println("Error: Entrada inválida. Por favor ingresa 1 o 2.");
                    input.nextLine();
                }
            }

            game.setWinner(winner);
            winners.add(winner);
        }
        return winners;
    }

    private static String getNextStageName(String currentStageName) {
        return switch (currentStageName) {
            case "octavos" -> "cuartos";
            case "cuartos" -> "semifinales";
            case "semifinales" -> "final";
            default -> "";
        };
    }
}