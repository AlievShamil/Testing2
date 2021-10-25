import java.util.function.Function;

public class StringFormatter {

    public static <T> String format(Iterable<T> iterable, Function<T, String> format, String delimiter) {
        StringBuilder result = new StringBuilder();
        iterable.forEach((x) -> {
            if (result.length() > 0) {
                result.append(delimiter);
                result.append(x);
            } else {
                result.append(format.apply(x));
            }
        });
        return result.toString();
    }


}
