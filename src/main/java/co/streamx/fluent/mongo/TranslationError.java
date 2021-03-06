package co.streamx.fluent.mongo;

import java.text.MessageFormat;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
enum TranslationError {

    UNSUPPORTED_EXPRESSION_TYPE("Unsupported operator: {0}"),
    REQUIRES_EXTERNAL_PARAMETER("Parameter method accepts external parameters only, as an object. "
            + "Calculations and expressions must be performed out of Lambda. Received: {0}"),
    REQUIRES_LICENSE("{0} requires a license. Get one at https://fluentjpa.com") {
        @Override
        public RuntimeException getError(Object... args) {
            return new UnsupportedOperationException(MessageFormat.format(pattern, args));
        }
    },
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
