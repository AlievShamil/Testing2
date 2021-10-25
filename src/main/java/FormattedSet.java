import java.util.HashSet;
import java.util.function.Function;

public class FormattedSet<T> extends HashSet<T> {
    private Function<T, String> format;

    public FormattedSet(Function<T, String> format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return StringFormatter.format(this, format, ", ");
    }
}
