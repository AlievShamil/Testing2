import org.assertj.core.internal.bytebuddy.implementation.bind.annotation.Super;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

public class SuperStreamTest {

    @Test
    public void testMethodToList() {
        SuperStream<Integer> ss = new SuperStream<>(asList(1, 2, 3));
        List<Integer> list = ss.toList();

        assertThat(list).contains(1, 2, 3);
        assertThat(ss.toList()).isInstanceOf(List.class);
    }

    @Test
    public void testMethodToSet() {
        SuperStream<Integer> ss = new SuperStream<>(asList(3, 2, 1));
        Set<Integer> set = ss.toSet();

        assertThat(set).contains(1, 2, 3);
        assertThat(ss.toSet()).isInstanceOf(Set.class);
    }

    @Test
    public void testMethodJoinWithoutDelimiter() {
        SuperStream<Integer> ss = new SuperStream<>(asList(5, 6, 7, 8));
        String result = ss.join();

        assertThat(result).isEqualTo("5678");
    }

    @Test
    public void testMethodJoinWithDelimiter() {
        SuperStream<Integer> ss = new SuperStream<>(asList(5, 6, 7, 8));
        String result = ss.join(" | ");

        assertThat(result).isEqualTo("5 | 6 | 7 | 8");
    }

    @Test
    public void testMethodMap() {
        SuperStream<Integer> ss = new SuperStream<>(asList(2, 1, 3));
        String result = ss.map(i -> i * 3).join();

        assertThat(result).isEqualTo("639");
    }

    @Test
    public void testMethodFilter() {
        SuperStream<Integer> ss = new SuperStream<>(asList(1, 2, 3, 1, 2, 5));
        String result = ss.filter(i -> i > 2).join(" * ");

        assertThat(result).isEqualTo("3 * 5");
    }

    @Test
    public void testMethodMapWithFormat() {
        SuperStream<Integer> ss = new SuperStream<>(asList(1, 2, 1, 2, 5));
        List<Integer> resultList = ss.filter(i -> i > 2).map(i -> String.format("This is %s", i)).toList();
        assertThat(resultList.toString()).isEqualTo("This is 5");
    }

    @Test(expected = ClassCastException.class)
    public void testMethodMapWithFormat1() {
        SuperStream<Integer> ss = new SuperStream<>(asList(1, 2, 1, 2, 5));
        ss.filter(i -> i > 2).map(i -> String.format("This is %s", i)).map(i -> i * 2).toList();
    }

    @Test
    public void testIntermediateTermination() {
        // given
        final SuperStream<Integer> ss = new SuperStream<>(asList(1, 2, 3)).map(i -> i + 1);

        final SuperStream<Integer> intermediateStream1 = ss.map(i -> i * 2);
        intermediateStream1.toList();

        final SuperStream<Integer> intermediateStream2 = ss.map(i -> i * 3);

        // when
        List<Integer> result = intermediateStream2.toList();

        // then
        assertThat(result).isEqualTo(asList(6, 9, 12));

    }
}
