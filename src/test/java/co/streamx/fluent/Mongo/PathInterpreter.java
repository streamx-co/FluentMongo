package co.streamx.fluent.Mongo;

import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import co.streamx.fluent.extree.expression.Expression;
import co.streamx.fluent.extree.expression.MemberExpression;
import co.streamx.fluent.extree.expression.SimpleExpressionVisitor;
import lombok.Getter;

final class PathInterpreter extends SimpleExpressionVisitor {

    @Getter
    private CharSequence path;

    private static final Map<Member, Field> membersToFields = new ConcurrentHashMap<>();

    @Override
    public Expression visit(MemberExpression e) {
        Expression instance = e.getInstance();

        if (instance != null) {
            PathInterpreter pathRetriever = new PathInterpreter();
            instance.accept(pathRetriever);
            path = pathRetriever.getPath();
        }

        String myPath = getField(e.getMember()).getName();

        if (path == null)
            path = myPath;
        else {
            if (!(path instanceof StringBuilder)) {
                path = new StringBuilder(path);
            }

            StringBuilder b = (StringBuilder) path;
            b.append('.').append(myPath);
        }
        return e;
    }

    private static Field getField(Member m1) {

        if (membersToFields.size() > 10000)
            membersToFields.clear();

        return membersToFields.computeIfAbsent(m1, m -> {

            String original = m.getName();

            String name = getFieldName(m);
            String decapitalized = decapitalize(name);
            Class<?> clazz = m.getDeclaringClass();
            for (;;) {
                try {
                    return clazz.getDeclaredField(decapitalized);
                } catch (NoSuchFieldException e) {
                    try {
                        return clazz.getDeclaredField(name);
                    } catch (NoSuchFieldException e1) {
                        try {
                            return clazz.getDeclaredField(original);
                        } catch (NoSuchFieldException e2) {
                            clazz = clazz.getSuperclass();
                            if (clazz == Object.class || clazz == null)
                                throw TranslationError.UNMAPPED_FIELD.getError(e2, original);
                        }
                    }
                }
            }
        });
    }

    private static String getFieldName(Member m) {
        String name = m.getName();
        if (m instanceof Method) {
            if (name.startsWith("is"))
                name = name.substring(2);
            else if (name.startsWith("get"))
                name = name.substring(3);
        }
        return name;
    }

    private static String decapitalize(String name) {

        if (name.length() > 1 && Character.isUpperCase(name.charAt(1)) && Character.isUpperCase(name.charAt(0))) {
            return name;
        }
        char chars[] = name.toCharArray();
        chars[0] = Character.toLowerCase(chars[0]);
        return new String(chars);
    }
}
