package models;

import java.util.LinkedHashSet;
import java.util.Set;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class Stage {
    private String name;
    @Builder.Default
    private Set<Game> games = new LinkedHashSet<>();
}
