package animal.base;

import animal.linguistics.phrase.NounPhrase;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

public class Animal {

    @NotNull
    @Getter
    private final NounPhrase species;

    public Animal(@NotNull NounPhrase species) {
        this.species = species;
    }

    @Override
    public String toString() {
        return species.toString();
    }
}
