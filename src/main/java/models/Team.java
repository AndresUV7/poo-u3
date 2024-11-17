package models;

import java.io.Serializable;
import java.util.List;

import lombok.Builder;
import lombok.Getter;


@Builder
@Getter
public class Team implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private List<Game> games;

	
}
