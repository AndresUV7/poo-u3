package models;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Game {
	private final Integer id = generateId();
		
	private Team team1;
    private Team team2;
    private Team winner;

	private static int counter = 0;
    private static synchronized Integer generateId() {
		return ++counter;
	}
}
