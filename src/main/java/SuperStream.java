import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.function.Function;
import java.util.function.Predicate;

public class SuperStream<T> {
    private SuperStream<T> prev;
    private List<Object> functionList;
    private final Iterable<T> originalIterable;

    public SuperStream(Iterable<T> it) {
        originalIterable = it;
        reset();

    }

    public SuperStream(SuperStream<T> prev, Iterable<T> it) {
        this(it);
        this.prev = prev;
    }

    public SuperStream<T> filter(Predicate<T> filter) {
        functionList.add(filter);
        return this;
    }

    public <U> SuperStream<T> map(Function<T, U> func) {
        functionList.add(func);

        return new SuperStream<>(this, buildIterable());
    }

    public List<T> toList() {
        List<T> tempList = new FormattedList<>();
        buildIterable().forEach(tempList::add);
        reset();
        return tempList;
    }


    public Set<T> toSet() {
        Set<T> tempSet = new FormattedSet<>();
        buildIterable().forEach(tempSet::add);
        reset();
        System.out.println(tempSet);
        return tempSet;
    }

    public String join() {
        return join("");
    }

    public String join(String delimiter) {
        StringBuilder builder = new StringBuilder();
        buildIterable().forEach(builder::append);

        return builder.substring(0, builder.length()-delimiter.length());
    }

    private void reset() {
        functionList = new ArrayList<>();
        if (prev != null) {
            prev.reset();
        }
    }

    private Iterable<T> buildIterable() {
        Iterable<T> tempIterable = new ArrayList<T>((Collection<? extends T>) originalIterable);
        for (Object func : functionList) {
            if (func instanceof Function<?, ?>) {
                tempIterable = applyMap(tempIterable, (Function<T, ?>) func);
            } else if (func instanceof Predicate<?>) {
                tempIterable = applyFilter(tempIterable, (Predicate<T>) func);
            }
        }

        return tempIterable;
    }

    private Iterable<T> applyMap(Iterable<T> list, Function<T, ?> function) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            result.add((T) function.apply(item));
        }

        return result;
    }

    private Iterable<T> applyFilter(Iterable<T> list, Predicate<T> filter) {
        List<T> result = new ArrayList<>();
        for (T item : list) {
            if (filter.test(item))
                result.add(item);
        }

        return result;
    }
}
