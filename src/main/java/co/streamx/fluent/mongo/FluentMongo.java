package co.streamx.fluent.mongo;

import static co.streamx.fluent.mongo.grammar.FluentFilters.all;
import static co.streamx.fluent.mongo.grammar.FluentFilters.eq;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.OptionalInt;
import java.util.concurrent.atomic.AtomicBoolean;

import org.bson.conversions.Bson;

import co.streamx.fluent.extree.expression.LambdaExpression;
import co.streamx.fluent.mongo.functions.Function1;
import co.streamx.fluent.mongo.functions.Function2;
import javax0.license3j.License;
import javax0.license3j.io.IOFormat;
import javax0.license3j.io.LicenseReader;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class FluentMongo {

    private static final AtomicBoolean licenseChecked = new AtomicBoolean();
    private static volatile boolean isLicensed;

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

    private static final TypedCollection<?> typed = new TypedCollection<Object>() {
    };
//
//    public static <T> Bson filter(Function1<T, Boolean> predicate) {
//        GenericInterpreter interpreter = new FilterInterpreter();
//
//        return process(predicate, interpreter);
//    }

    @SuppressWarnings("unchecked")
    public static <T> TypedCollection<T> collection(T collection) {
        return (TypedCollection<T>) typed;
    }

    @SuppressWarnings("unchecked")
    public static <T> TypedCollection<T> collection(Class<T> collectionType) {
        return (TypedCollection<T>) typed;
    }

    static <T> Bson process(Function1<T, ?> lambda,
                            GenericInterpreter interpreter) {
        LambdaExpression<?> e = LambdaExpression.parse(lambda);
        e = (LambdaExpression<?>) Normalizer.get().visit(e);
        interpreter.visit(e);

        return interpreter.popResult();
    }

    /**
     * checks FluentJPA license
     * 
     * @param licStream      if null, will try to load fluent-jpa.lic file from root
     * @param suppressBanner suppresses license banner if license is valid
     * @return true if license is valid
     */
    @SneakyThrows
    public static boolean checkLicense(InputStream licStream,
                                       boolean suppressBanner) {
        if (licenseChecked.compareAndSet(false, true)) {
            boolean closeStream = licStream == null;
            try {
                if (closeStream)
                    licStream = FluentMongo.class.getClassLoader().getResourceAsStream("fluent-jpa.lic");
                if (licStream == null) {
                    reportNoLicense();
                } else {
                    isLicensed = checkLicense0(licStream, suppressBanner);
                }
            } catch (Exception e) {
                log.warn("Licence check failed", e);
                reportNoLicense();
            } finally {
                if (closeStream && licStream != null)
                    licStream.close();
            }
        }

        return isLicensed;
    }

    /**
     * A shortcut for {@code checkLicense(null, false)}
     */
    public static boolean checkLicense() {
        return checkLicense(null, false);
    }

    private static boolean checkLicense0(InputStream licStream,
                                         boolean suppressBanner)
            throws IOException {
        try (InputStream keyStream = FluentMongo.class.getClassLoader().getResourceAsStream("public.key");
                LicenseReader licenseReader = new LicenseReader(licStream)) {
            License lic = licenseReader.read(IOFormat.STRING);

            byte[] key = new byte[298]; // size of the public.lic file
            keyStream.read(key);

            boolean ok = lic.isOK(key);
            if (!ok) {
                reportNoLicense();
                return false;
            }

            Date expiration = lic.get("expiration").getDate();
            long currentTimeMillis = System.currentTimeMillis();
            if (expiration.getTime() < currentTimeMillis) {
                reportLicenseExpired();
                long grace = 3600 * 24 * 30 * 1000; // 1 month
                return expiration.getTime() > (currentTimeMillis - grace);
            }

            if (!suppressBanner) {
                String project = lic.get("project").getString();
                reportLicenseOk(project, expiration);
            }

            return true;
        }
    }

    private static void reportLicenseOk(String project,
                                        Date expiration) {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-MMM-dd");

        printBanner("Thank you for using FluentJPA in the awesome '" + project + "' project!\nLicense expires at "
                + dateFormat.format(expiration) + ".");
    }

    private static void reportNoLicense() {
        printBanner(
                "Thank you for using FluentJPA!\nNo valid FluentJPA license file was found.\nSome FluentJPA features are locked.");
    }

    private static void reportLicenseExpired() {
        printBanner(
                "Thank you for using FluentJPA!\nFluentJPA commercial license has expired, please renew.\nSome FluentJPA features are locked.");
    }

    private static void printBanner(String message) {

        OptionalInt length = Arrays.stream(message.split("\n")).mapToInt(String::length).max();

        char[] dashes = new char[length.orElse(50)];
        Arrays.fill(dashes, '#');
        System.out.println(dashes);
        System.out.println();
        System.out.println(message);
        System.out.println();
        System.out.println(dashes);
        System.out.println();
    }
}
