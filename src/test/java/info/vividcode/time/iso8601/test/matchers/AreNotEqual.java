package info.vividcode.time.iso8601.test.matchers;

import info.vividcode.time.iso8601.test.TwoObjects;
import org.hamcrest.Description;
import org.hamcrest.Factory;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

public class AreNotEqual extends TypeSafeMatcher<TwoObjects<?>> {

    @Override
    protected boolean matchesSafely(TwoObjects<?> twoObjects) {
        return !checkEqualMethods(twoObjects) && !checkHashCodeMethods(twoObjects);
    }

    private static <T> boolean checkEqualMethods(TwoObjects<T> objects) {
        return objects.obj1.equals(objects.obj2);
    }

    private static <T> boolean checkHashCodeMethods(TwoObjects<T> objects) {
        return objects.obj1.hashCode() == objects.obj2.hashCode();
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("two objects are not equal");
    }

    @Override
    protected void describeMismatchSafely(TwoObjects<?> item, Description mismatchDescription) {
        boolean appended = false;
        if (checkEqualMethods(item)) {
            mismatchDescription
                    .appendText("they are equal (`#equals` method returns true)");
            appended = true;
        }
        if (checkHashCodeMethods(item)) {
            if (appended) mismatchDescription.appendText(" and ");
            mismatchDescription
                    .appendText("their hash codes")
                    .appendValueList(" (", ",", ") ", item.obj1.hashCode(), item.obj2.hashCode())
                    .appendText(") are equal");
        }
    }

    @Factory
    public static Matcher<TwoObjects<?>> areNotEqual() {
        return new AreNotEqual();
    }

}
