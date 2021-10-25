import java.util.ArrayList;
import java.util.function.Function;

public class FormattedList<T> extends ArrayList<T> {
    private Function<T, String> format;

    public FormattedList(Function<T, String> format) {
        this.format = format;
    }

    @Override
    public String toString() {
        return StringFormatter.format(this, format, ", ");
    }
}
