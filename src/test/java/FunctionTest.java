import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.mockito.Mockito;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;

import static java.lang.Double.*;

// variant: 41224
class FunctionTest {

    static double functionEps = 0.1;
    double eps = 0.1;

    static Csc cscMock;
    static Cos cosMock;
    static Sin sinMock;
    static Ln lnMock;
    static Log logMock;
    static Tan tanMock;

    static Reader cscIn;
    static Reader cosIn;
    static Reader sinIn;
    static Reader lnIn;
    static Reader log2In;
    static Reader log5In;
    static Reader tanIn;


    @BeforeAll
    static void init() {
        cscMock = Mockito.mock(Csc.class);
        cosMock = Mockito.mock(Cos.class);
        sinMock = Mockito.mock(Sin.class);
        tanMock = Mockito.mock(Tan.class);
        lnMock = Mockito.mock(Ln.class);
        logMock = Mockito.mock(Log.class);
        try {
            cscIn = new FileReader("src/test/resources/CsvFiles/Inputs/CscIn.csv");
            cosIn = new FileReader("src/test/resources/CsvFiles/Inputs/CosIn.csv");
            sinIn = new FileReader("src/test/resources/CsvFiles/Inputs/SinIn.csv");
            lnIn = new FileReader("src/test/resources/CsvFiles/Inputs/LnIn.csv");
            log2In = new FileReader("src/test/resources/CsvFiles/Inputs/Log2In.csv");
            log5In = new FileReader("src/test/resources/CsvFiles/Inputs/Log5In.csv");
            tanIn = new FileReader("src/test/resources/CsvFiles/Inputs/TanIn.csv");

            Iterable<CSVRecord> records = CSVFormat.DEFAULT.parse(cscIn);
            for (CSVRecord record : records) {
                Mockito.when(cscMock.csc(parseDouble(record.get(0)), functionEps)).thenReturn(valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(cosIn);
            for (CSVRecord record : records) {
                Mockito.when(cosMock.cos(parseDouble(record.get(0)), functionEps)).thenReturn(valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(sinIn);
            for (CSVRecord record : records) {
                Mockito.when(sinMock.sin(parseDouble(record.get(0)), functionEps)).thenReturn(valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(tanIn);
            for (CSVRecord record : records) {
                Mockito.when(tanMock.tan(parseDouble(record.get(0)), functionEps)).thenReturn(valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(lnIn);
            for (CSVRecord record : records) {
                Mockito.when(lnMock.ln(parseDouble(record.get(0)), functionEps)).thenReturn(valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(log2In);
            for (CSVRecord record : records) {
                Mockito.when(logMock.log(2, parseDouble(record.get(0)), functionEps)).thenReturn(valueOf(record.get(1)));
            }
            records = CSVFormat.DEFAULT.parse(log5In);
            for (CSVRecord record : records) {
                Mockito.when(logMock.log(5, parseDouble(record.get(0)), functionEps)).thenReturn(valueOf(record.get(1)));
            }
        } catch (IOException ex) {
            System.err.println("How did you get here?");
            System.out.println(ex.getMessage());
        }

    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Inputs/SystemIn.csv")
    void testSystemWithMocks(double value, double expected) {
        Function function = new Function(cscMock,logMock,lnMock,tanMock);
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Inputs/SystemIn.csv")
    void testWithCsc(double value, double expected) {
        Function function = new Function(new Csc(sinMock), logMock, lnMock, tanMock);
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Inputs/SystemIn.csv")
    void testWithTan(double value, double expected) {
        Function function = new Function(cscMock, logMock, lnMock, new Tan(sinMock, cosMock));
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Inputs/SystemIn.csv")
    void testWithSin(double value, double expected) {
        Function function = new Function(new Csc(new Sin()),logMock, lnMock, new Tan(new Sin(), new Cos(new Sin())));
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Inputs/SystemIn.csv")
    void testWithCos(double value, double expected) {
        Function function = new Function(cscMock, logMock, lnMock, new Tan(sinMock, new Cos(sinMock)));
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Inputs/SystemIn.csv")
    void testWithLog(double value, double expected) {
        Function function = new Function(cscMock, new Log(), lnMock, tanMock);
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Inputs/SystemIn.csv")
    void testWithLn(double value, double expected) {
        Function function = new Function(cscMock, new Log(new Ln()), new Ln(), tanMock);
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps * 50);
    }

    @ParameterizedTest
    @CsvFileSource(resources = "/CsvFiles/Inputs/SystemIn.csv")
    void testWithSinAndLn(double value, double expected) {
        Function function = new Function();
        Assertions.assertEquals(expected, function.SystemSolve(value, functionEps), eps * 50);
    }
}