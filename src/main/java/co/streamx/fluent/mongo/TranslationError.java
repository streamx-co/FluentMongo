package co.streamx.fluent.mongo;

import java.text.MessageFormat;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum TranslationError {

    UNSUPPORTED_EXPRESSION_TYPE("Unsupported operator: {0}"),
    UNMAPPED_FIELD("Cannot translate property: {0}.") {
        @Override
        public RuntimeException getError(Throwable cause,
                                         Object... args) {

            if (args[0].equals("equals")) {
                String format = MessageFormat.format(pattern, args) + ". Use == operator instead";
                return new UnsupportedOperationException(format, cause);
            }
            return super.getError(args);
        }
    },;

    protected final String pattern;

    public RuntimeException getError(Object... args) {
        return new IllegalStateException(MessageFormat.format(pattern, args));
    }

    public RuntimeException getError(Throwable cause,
                                     Object... args) {
        return new IllegalStateException(MessageFormat.format(pattern, args), cause);
    }
}
