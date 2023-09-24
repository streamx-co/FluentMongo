package co.streamx.fluent.mongo;

import static co.streamx.fluent.mongo.grammar.FluentFilters.all;
import static co.streamx.fluent.mongo.grammar.FluentFilters.eq;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;

import co.streamx.fluent.mongo.functions.Functional;
import com.mongodb.client.model.mql.MqlValue;
import org.bson.conversions.Bson;

import co.streamx.fluent.extree.expression.LambdaExpression;
import co.streamx.fluent.mongo.functions.Function1;
import co.streamx.fluent.mongo.functions.Function2;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FluentMongo {

    private static final AtomicBoolean licenseChecked = new AtomicBoolean();
    private static volatile boolean isLicensed = true;
    private static final String DEBUG_MODE = "Debug mode";

    static {
        MongoConfig.registerMethodSubstitution(Object::equals, (a,
                                                                b) -> a == b);
        MongoConfig.registerMethodSubstitution(
                (Function2<Collection<Comparable>, Comparable, Boolean>) Collection::contains, (collection,
                                                                                                item) -> eq(collection,
                                                                                                        item));

        MongoConfig.registerMethodSubstitution(
                (Function2<Collection<Comparable>, Collection<Comparable>, Boolean>) Collection::containsAll,
                (collection,
                 items) -> all(collection, items));
    }

    private static final QueryBuilder<?> typed = new QueryBuilder<Object>() {
    };
//
//    public static <T> Bson filter(Function1<T, Boolean> predicate) {
//        GenericInterpreter interpreter = new FilterInterpreter();
//
//        return process(predicate, interpreter);
//    }

    /**
     * Create type safe collection query builder
     */
    @SuppressWarnings("unchecked")
    public static <T> QueryBuilder<T> queryBuilder(T collection) {
        return (QueryBuilder<T>) typed;
    }

    /**
     * Create type safe collection query builder
     */
    @SuppressWarnings("unchecked")
    public static <T> QueryBuilder<T> queryBuilder(Class<T> collectionType) {
        return (QueryBuilder<T>) typed;
    }

    private static <F, T> T process(Functional lambda,
                            GenericInterpreter<T> interpreter) {
//        if (!checkLicense())
//            throw TranslationError.REQUIRES_LICENSE.getError(DEBUG_MODE);

        LambdaExpression<?> e = LambdaExpression.parse(lambda);
        e = (LambdaExpression<?>) Normalizer.get().visit(e);
        interpreter.visit(e);

        return interpreter.getResult(e.getBody().getResultType());
    }

    static <T> Bson process(Functional lambda,
                            BsonGenericInterpreter interpreter) {
        return process(lambda, (GenericInterpreter<Bson>) interpreter);
    }

    static <T> MqlValue process(Functional lambda,
                                MqlInterpreter interpreter) {
        return process(lambda, (GenericInterpreter<MqlValue>) interpreter);
    }

    /**
     * checks FluentJPA license
     * 
     * @param licStream      if null, will try to load fluent-jpa.lic file from root
     * @param suppressBanner suppresses license banner if license is valid
     * @return true if license is valid
     */
   /* @SneakyThrows
    public static boolean checkLicense(InputStream licStream,
                                       boolean suppressBanner) {
        if (licenseChecked.compareAndSet(false, true)) {

            String versionString = System.getProperty("java.version");
            Pattern p = Pattern.compile("\\D");
            Matcher matcher = p.matcher(versionString);
            if (matcher.find()) {
                int dot = matcher.start();
                versionString = versionString.substring(0, dot);
            }

            int version = Integer.parseInt(versionString);
            if (version > 14) {
                isLicensed = false;
                throw new UnsupportedClassVersionError(
                        "Java " + versionString + " is not supported by this FluentMongo version.");
            }

            boolean needLicense = false;
            List<String> arguments = ManagementFactory.getRuntimeMXBean().getInputArguments();
            for (String arg : arguments) {
                needLicense = arg.startsWith("-Xrunjdwp") || arg.startsWith("-agentlib:jdwp");
                if (needLicense)
                    break;
            }

            if (!needLicense)
                return isLicensed;

            boolean closeStream = licStream == null;
            try {
                if (closeStream)
                    licStream = FluentMongo.class.getClassLoader().getResourceAsStream("fluent-jpa.lic");

                String key;
                if (licStream == null) {
                    key = null;
                } else {
                    Reader r = new InputStreamReader(licStream, StandardCharsets.US_ASCII);
                    BufferedReader bufferedReader = new BufferedReader(r);
                    key = bufferedReader.lines().collect(Collectors.joining());
                }

                License.validate(key);
                if (key != null)
                    License.reportLicenseOk();
                else
                    License.reportNoLicense();
                isLicensed = true;

            } catch (Exception e) {
                log.warn("Licence check failed", e);
                isLicensed = false;
            } finally {
                if (closeStream && licStream != null)
                    licStream.close();
            }
        }

        return isLicensed;
    }*/

    /**
     * A shortcut for {@code checkLicense(null, false)}
     */
   /* public static boolean checkLicense() {
        return checkLicense(null, false);
    }*/
}
