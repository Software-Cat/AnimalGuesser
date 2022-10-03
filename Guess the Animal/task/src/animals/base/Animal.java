package animals.base;

import animals.linguistics.NounPhrase;
import org.jetbrains.annotations.NotNull;

public class Animal {

    @NotNull
    private final NounPhrase species;

    public Animal(@NotNull String species) {
        this.species = NounPhrase.parsePhrase(species);
    }

    @Override
    public String toString() {
        return species.toString();
    }
}
