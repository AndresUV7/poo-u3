package controllers;

import java.util.Collections;
import java.util.List;

import models.Game;
import models.Stage;
import models.Team;
import views.StageView;

/**
 * Controlador que maneja la generación aleatoria de enfrentamientos entre equipos
 * en una etapa del torneo utilizando un enfoque recursivo
 */
public class MatchDrawController {

    /**
     * Genera el bracket (llaves) de enfrentamientos para una etapa
     * @param teams Lista de equipos participantes
     * @param stage Etapa del torneo donde se generarán los enfrentamientos
     * @param stageView Vista para mostrar los detalles de la etapa
     * @return Stage con los enfrentamientos generados
     */
    public static Stage generateBracket(List<Team> teams, Stage stage, StageView stageView) {
        // Mezcla aleatoriamente el orden de los equipos
        Collections.shuffle(teams);
        // Limpia los juegos existentes en la etapa
        stage.getGames().clear();
        // Inicia la generación recursiva comenzando con el partido #1
        return generateBracketRecursive(teams, stage, 1, stageView);
    }

    /**
     * Método recursivo que genera los enfrentamientos emparejando equipos de dos en dos
     * @param teams Lista de equipos pendientes por emparejar
     * @param stage Etapa donde se agregan los enfrentamientos
     * @param matchNumber Número secuencial del partido
     * @param stageView Vista para mostrar información de los emparejamientos
     * @return Stage actualizada con los nuevos enfrentamientos
     */
    private static Stage generateBracketRecursive(List<Team> teams, Stage stage, int matchNumber, StageView stageView) {
        int teamCount = teams.size();
        // Si quedan al menos 2 equipos, se puede generar un enfrentamiento
        if (teamCount > 1) {
            // Muestra en la vista los equipos del enfrentamiento actual
            stageView.showStageDetails(teams.get(0).getName(), teams.get(1).getName(), matchNumber);
            
            // Crea un nuevo juego/partido con los primeros dos equipos de la lista
            Game game = Game.builder()
                    .team1(teams.get(0))
                    .team2(teams.get(1))
                    .build();

            // Agrega el juego creado a la etapa
            stage.getGames().add(game);

            // Llamada recursiva con los equipos restantes y el siguiente número de partido
            generateBracketRecursive(teams.subList(2, teamCount), stage, matchNumber + 1, stageView);
        }

        return stage;
    }
}