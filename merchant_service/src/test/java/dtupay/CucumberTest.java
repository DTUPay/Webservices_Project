package dtupay;

import io.cucumber.junit.CucumberOptions;
import io.cucumber.junit.platform.engine.Cucumber;

import static io.cucumber.junit.CucumberOptions.SnippetType.CAMELCASE;

/* Important:
for Cucumber tests to be recognized by Maven, the class name has to have
either the word Test in the beginning or at the end.
For example, the class name CucumberTests (Test with an s) will be ignored by Maven.
*/
@CucumberOptions(snippets = CAMELCASE)

@Cucumber
public class CucumberTest {

}
