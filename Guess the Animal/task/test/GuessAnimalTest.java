import org.hyperskill.hstest.dynamic.DynamicTest;
import org.hyperskill.hstest.stage.StageTest;
import org.hyperskill.hstest.testcase.CheckResult;
import org.hyperskill.hstest.testing.TestedProgram;

import java.io.IOException;
import java.util.stream.Stream;

public class GuessAnimalTest extends StageTest<String> {
    private static final int RUNS_COUNT = 10;

    final String[] script = new String[]{
            "animals",
            "positive-answers",
            "negative-answers",
            "unclear-answers"
    };

    @DynamicTest(data = "script")
    CheckResult runScripts(final String script) throws IOException {
        return new Scenario(script).check();
    }

    @DynamicTest()
    CheckResult testRandomFarewell() {
        return checkRandom("cat\nyes\n",
                "You program should use different ways to farewell the user.");
    }

    @DynamicTest
    CheckResult testRandomYesNoClarification() {
        return checkRandom("cat\n#\n",
                "You program should use different ways to ask clarification question.");
    }

    private CheckResult checkRandom(final String input, final String errorMessage) {
        final var isRandom = Stream.generate(() -> {
            final var main = new TestedProgram();
            main.start();
            return main.execute(input);
        }).limit(RUNS_COUNT).distinct().count() > 1;

        return isRandom ? CheckResult.correct() : CheckResult.wrong(errorMessage);
    }
}