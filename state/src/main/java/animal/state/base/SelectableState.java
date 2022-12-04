package animal.state.base;

import lombok.Getter;

public abstract class SelectableState extends AppState {

    @Getter
    private final String description;

    public SelectableState(String description) {
        this.description = description;
    }
}
