package javaSe.finalKeyword;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;


public class finalOnObjects {
    @Test
    public void objectsCanBeChangeOnFinal() throws Exception {
//        Arrays is treated as an object
        final boolean[] array = {true, true};
//        array = new boolean[]{false, false}; Compilation error
        array[0] = false;
        array[1] = false;
        assertThat(array[1], is(false));
        assertThat(array[0], is(false));

        final List<Boolean> list = new ArrayList<Boolean>();
        list.add(false);
        list.add(false);
//        list = new ArrayList<Boolean>(); compilation error
        list.set(0,true);
        list.set(1, true);
        assertThat(list.get(0), is(true));
        assertThat(list.get(1), is(true));
    }
}
