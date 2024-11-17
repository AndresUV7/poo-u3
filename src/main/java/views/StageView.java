package views;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

@SuperBuilder
@Getter
public abstract class StageView { 
    public abstract void showStageDetails(String teamName1, String teamName2 , int matchNumber);
}
