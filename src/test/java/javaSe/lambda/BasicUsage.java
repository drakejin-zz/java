package javaSe.lambda;

import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.*;

/**
 * Argument List 	Arrow Token 	Body
  (int x, int y) 	  -> 	         x + y
 **/

public class BasicUsage {
    private interface IntegerMath {
        Integer operation(int a, int b);
    }

    @Test
    public void canImplementInterfaceWithOneMethod() throws Exception {
        //The interface with one method is called SAM - Single Abstract Method
        IntegerMath add = (a, b) -> a + b;
        assertThat(add.operation(2, 3), is(5));

        IntegerMath div = (a, b) -> a / b ;
        assertThat(div.operation(20,5), is(4));

        IntegerMath mult = (a, b) -> a * b;
        assertThat(mult.operation(20, 20), is(400));
    }

    @Test
    public void canTakesNoArgument() throws Exception {
        final boolean[] anonymousClassInvoked = {false};
        final boolean[] lambdaExpressionInvoked = {false};
        
        Runnable anonymousClass = new Runnable() {
            @Override
            public void run() {
                anonymousClassInvoked[0] = true;
            }
        };

        Runnable lambda = () -> {
			lambdaExpressionInvoked[0] = true;
			System.out.println("lambda expression");
		};

        anonymousClass.run();
        lambda.run();
        assertThat(anonymousClassInvoked[0], is(true));
        assertThat(lambdaExpressionInvoked[0], is(true));
    }
}
