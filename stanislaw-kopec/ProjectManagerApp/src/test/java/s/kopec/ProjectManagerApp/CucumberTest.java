package s.kopec.ProjectManagerApp;

import io.cucumber.junit.Cucumber;
import io.cucumber.junit.CucumberOptions;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(
        plugin = {"pretty", "summary", "html:target/cucumber-reports.html"},
        features = "src/test/resources/features",
        glue = "s.kopec.ProjectManagerApp.steps"
)
public class CucumberTest {
}
