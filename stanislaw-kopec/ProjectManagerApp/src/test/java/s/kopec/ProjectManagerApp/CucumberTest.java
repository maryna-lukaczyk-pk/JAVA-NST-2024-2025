package s.kopec.ProjectManagerApp;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "summary"},
        features = "src/test/resources/features",
        glue = "s.kopec.ProjectManagerApp.steps"
)
public class CucumberTest {
}
