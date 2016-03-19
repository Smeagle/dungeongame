package test;
import org.junit.runner.RunWith;
import org.junit.runners.Suite;
import org.junit.runners.Suite.SuiteClasses;

@RunWith(Suite.class)
@SuiteClasses({
	GridTest.class,
	CoordinatesTest.class,
	LOSUtilitiesTest.class
})

public class AllTests {
}
