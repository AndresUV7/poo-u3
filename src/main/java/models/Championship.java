package models;

import java.io.Serializable;
import java.util.Set;

import lombok.Data;

@Data
public class Championship implements Serializable {
	private static final long serialVersionUID = 1L;
	private String name;
	private Set<Stage> stages;
	private Set<Team> teams;
}
