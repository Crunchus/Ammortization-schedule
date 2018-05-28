import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class ScheduleMakerWithInterestRateChange {

    private double initialInterestRate = 12;
    private double interestRateAfterChange = 9;
    private double loanAmount = 5000;
    private int numberOfPayments = 24;
    private LocalDate loanWithdrawalDate = LocalDate.of(2017, Month.APRIL, 15);
    private LocalDate dateOfInterestRateChange = LocalDate.of(2017, Month.SEPTEMBER, 02);
    private double initialMonthlyInterestRate = initialInterestRate / 1200;
    private double monthlyInterestRateAfterChange = interestRateAfterChange / 1200;
    private double initialAnnuityCoefficient = initialMonthlyInterestRate / (1 - (Math.pow((1 + initialMonthlyInterestRate), -(numberOfPayments))));
    private double initialTermPayment = Math.floor(initialAnnuityCoefficient * loanAmount * 100) / 100;

    ArrayList<String> schedule = new ArrayList<String>();

    public void makeSchedule() {
        schedule.add("Payment #,Payment date,Remaining amount,Principal payment,Interest payment,Total payment,Interest rate");
        double balance = this.loanAmount;
        double monthlyInterest;
        double principalPayment;
        double currentMonthlyInterestRate = initialMonthlyInterestRate;
        double currentTermPayment = initialTermPayment;
        LocalDate payoutDate = this.loanWithdrawalDate.plusMonths(1);
        Boolean notChanged = true;
        for (int i = 1; i < numberOfPayments; i++) {
            if (payoutDate.isAfter(dateOfInterestRateChange) && notChanged) {
                currentMonthlyInterestRate = monthlyInterestRateAfterChange;
                double annuityCoefficientAfterChange = monthlyInterestRateAfterChange / (1 - (Math.pow((1 + monthlyInterestRateAfterChange), -(numberOfPayments - i + 1))));
                double termPaymentAfterChange = Math.floor(annuityCoefficientAfterChange * balance * 100) / 100;
                currentTermPayment = termPaymentAfterChange;
                notChanged = false;
            }
            monthlyInterest = (double) Math.round(balance * currentMonthlyInterestRate * 100) / 100;
            principalPayment = (double) Math.round((currentTermPayment - monthlyInterest) * 100) / 100;
            schedule.add(i + "," + payoutDate + "," + balance + "," + principalPayment + "," + monthlyInterest + "," + currentTermPayment + "," + currentMonthlyInterestRate * 1200);
            balance = (double) Math.round((balance - principalPayment) * 100) / 100;
            payoutDate = payoutDate.plusMonths(1);
        }
        if (payoutDate.isAfter(dateOfInterestRateChange)) {
            currentMonthlyInterestRate = monthlyInterestRateAfterChange;
        }
        principalPayment = balance;
        monthlyInterest = (double) Math.round(balance * currentMonthlyInterestRate * 100) / 100;
        double lastPayment = (double) Math.round((balance + monthlyInterest) * 100) / 100;
        schedule.add(numberOfPayments + "," + payoutDate + "," + balance + "," + principalPayment + "," + monthlyInterest + "," + lastPayment + "," + currentMonthlyInterestRate * 1200);
        Path schedulePath = Paths.get("", "schedule1.csv");
        try {
            Files.write(schedulePath, schedule, StandardCharsets.UTF_8, StandardOpenOption.APPEND);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

