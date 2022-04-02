import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.IOException;
import java.io.Writer;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Function {
    Csc Csc;
    Log log;
    Ln ln;
    Tan tan;


    public double SystemSolve(double x, double eps) {
        if (x <= 0) {
            return
                    Csc.csc(x, eps) * Csc.csc(x, eps) + tan.tan(x, eps) * tan.tan(x, eps);
        } else {
            return (Math.pow(((ln.ln(x*x,eps))/log.log(2,x,eps) + ln.ln(x,eps)),3)
                    /(log.log(5,x,eps) * log.log(2,x,eps)));
        }
    }

    public double writeResultToCSV(double x, double eps, Writer out) {
        double res = SystemSolve(x, eps);
        try (CSVPrinter printer = CSVFormat.DEFAULT.print(out)) {
            printer.printRecord(x, res);
        } catch (IOException e) {
            System.out.println("Wrong filename");
        }
        return res;
    }
}
