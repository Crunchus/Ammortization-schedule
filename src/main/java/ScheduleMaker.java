import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;

public class ScheduleMaker {

    private double annualInterestRateInPercent = 12;
    private double loanAmount = 5000;
    private int numberOfPayments = 24;
    private LocalDate loanWithdrawalDate = LocalDate.of(2017, Month.APRIL, 15);
    private double monthlyInterestRate = annualInterestRateInPercent / 1200;
    private double annuityCoefficient = monthlyInterestRate / (1 - (Math.pow((1 + monthlyInterestRate), -(numberOfPayments))));
    private double termPayment = Math.floor(annuityCoefficient * loanAmount * 100) / 100;

    ArrayList<String> schedule = new ArrayList<String>();

    public void makeSchedule() {
        schedule.add("Payment #,Payment date,Remaining amount,Principal payment,Interest payment,Total payment,Interest rate");
        double balance = this.loanAmount;
        double monthlyInterest;
        double principalPayment;
        LocalDate payoutDate = this.loanWithdrawalDate.plusMonths(1);
        for (int i = 1; i < numberOfPayments; i++) {
            monthlyInterest = (double) Math.round(balance * monthlyInterestRate * 100) / 100;
            principalPayment = (double) Math.round((termPayment - monthlyInterest) * 100) / 100;
            schedule.add(i + "," + payoutDate + "," + balance + "," + principalPayment + "," + monthlyInterest + "," + termPayment + "," + annualInterestRateInPercent);
            balance = (double) Math.round((balance - principalPayment) * 100) / 100;
            payoutDate = payoutDate.plusMonths(1);
        }
        principalPayment = balance;
        monthlyInterest = (double) Math.round(balance * monthlyInterestRate * 100) / 100;
        double lastPayment = (double) Math.round((balance + monthlyInterest) * 100) / 100;
        schedule.add(numberOfPayments + "," + payoutDate + "," + balance + "," + principalPayment + "," + monthlyInterest + "," + lastPayment + "," + annualInterestRateInPercent + "\n");
        Path schedulePath = Paths.get("", "schedule1.csv");
        try {
            Files.write(schedulePath, schedule, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
