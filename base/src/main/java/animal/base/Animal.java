package animal.base;

import animal.linguistics.partofspeech.Noun;
import org.jetbrains.annotations.NotNull;

public class Animal {

    @NotNull
    private final Noun species;

    public Animal(@NotNull Noun species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return species.toString();
    }
}
