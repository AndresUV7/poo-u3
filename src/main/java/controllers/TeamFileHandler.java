package controllers;

import java.io.*;
import java.nio.file.*;
import java.util.*;
import java.util.stream.Collectors;

import models.Stage;
import models.Team;

public class TeamFileHandler {
    
    private static final String FILE_NAME = "input-teams.txt";
    private static final String FOLDER_NAME = "files";
    
    public static class TeamValidationException extends Exception {
		private static final long serialVersionUID = 1L;
		private final List<String> duplicateTeams;
        
        public TeamValidationException(String message, List<String> duplicateTeams) {
            super(message);
            this.duplicateTeams = duplicateTeams;
        }
        
        public List<String> getDuplicateTeams() {
            return duplicateTeams;
        }
    }

    private static int getExpectedTeamCount(Stage stage) {
        switch (stage.getName().toLowerCase()) {
            case "octavos": return 16;
            case "cuartos": return 8;
            case "semifinales": return 4;
            case "final": return 2;
            default: throw new IllegalArgumentException("Etapa no válida: " + stage.getName());
        }
    }
    
    public static List<Team> loadTeamsFromFile(Stage stage) throws TeamValidationException {
        List<Team> teams = new ArrayList<>();
        
        // Intentar diferentes rutas para encontrar el archivo
        Path[] possiblePaths = {
            Paths.get(FOLDER_NAME, FILE_NAME),                    // ./files/input-teams.txt
            Paths.get("src", FOLDER_NAME, FILE_NAME),            // ./src/files/input-teams.txt
            Paths.get("target", FOLDER_NAME, FILE_NAME),         // ./target/files/input-teams.txt
            Paths.get(".", FOLDER_NAME, FILE_NAME),              // ./files/input-teams.txt (explícito)
            Paths.get("..", FOLDER_NAME, FILE_NAME)              // ../files/input-teams.txt
        };
        
        Path filePath = null;
        
        // Buscar el archivo en las posibles rutas
        for (Path path : possiblePaths) {
            if (Files.exists(path)) {
                filePath = path;
                break;
            }
        }
        
        if (filePath == null) {
            throw new TeamValidationException("No se pudo encontrar el archivo de equipos", Collections.emptyList());
        }
        
        try {
            // Leer todas las líneas del archivo y almacenarlas primero en una lista
            List<String> teamNames = Files.lines(filePath)
                                        .map(String::trim)
                                        .filter(line -> !line.isEmpty())
                                        .collect(Collectors.toList());
            
            // Verificar el número correcto de equipos para la etapa
            int expectedTeams = getExpectedTeamCount(stage);
            if (teamNames.size() != expectedTeams) {
                throw new TeamValidationException(
                    String.format("Número incorrecto de equipos para la etapa %s. Se esperaban %d equipos, pero se encontraron %d",
                        stage.getName(), expectedTeams, teamNames.size()),
                    Collections.emptyList()
                );
            }
            
            // Verificar duplicados
            Set<String> uniqueTeams = new HashSet<>();
            List<String> duplicates = new ArrayList<>();
            
            for (String teamName : teamNames) {
                if (!uniqueTeams.add(teamName)) {
                    duplicates.add(teamName);
                }
            }
            
            // Si hay duplicados, lanzar excepción
            if (!duplicates.isEmpty()) {
                String message = String.format("Se encontraron equipos duplicados: %s", 
                    String.join(", ", duplicates));
                throw new TeamValidationException(message, duplicates);
            }
            
            // Si no hay duplicados y el número de equipos es correcto, crear los objetos Team
            teams = teamNames.stream()
                           .map(name -> Team.builder().name(name).build())
                           .collect(Collectors.toList());
                           
            System.out.println("Equipos cargados exitosamente desde " + filePath);
            
        } catch (IOException e) {
            throw new TeamValidationException(
                "Error al leer el archivo " + filePath + ": " + e.getMessage(),
                Collections.emptyList()
            );
        }
        
        return teams;
    }
}