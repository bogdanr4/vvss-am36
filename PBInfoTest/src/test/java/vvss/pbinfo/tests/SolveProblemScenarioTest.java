package vvss.pbinfo.tests;

import net.serenitybdd.annotations.Managed;
import net.serenitybdd.annotations.Steps;
import net.serenitybdd.junit5.SerenityJUnit5Extension;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.openqa.selenium.WebDriver;
import vvss.pbinfo.steps.UserSteps;

@ExtendWith(SerenityJUnit5Extension.class)
public class SolveProblemScenarioTest {
    @Managed(uniqueSession = true)
    private WebDriver webDriver;

    @Steps
    private UserSteps user;

    @Test
    public void testSolve_sum00() {
        user.openHomePage();
        user.login("vvss_am36", "qHM79SjB8LGm");
        user.shouldBeLoggedIn();
        user.searchProblem("sum00");
        user.shouldBeOnUrl("https://www.pbinfo.ro/probleme/939/sum00");

        String code = "#include <iostream>\n" +
                "using namespace std;\n" +
                "\n" +
                "int main()\n" +
                "{\n" +
                "    int a, b;\n" +
                "    cin >> a >> b;\n" +
                "    cout << a + b;\n" +
                "    \n" +
                "    return 0;\n" +
                "}";

        user.addSolution(code);
        user.shouldSee100PointsMessage();

        user.logout();
    }
}
