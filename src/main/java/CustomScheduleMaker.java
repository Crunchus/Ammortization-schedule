import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.ArrayList;

public class CustomScheduleMaker {

    private double annualInterestRateInPercent;
    private double loanAmount;
    private int numberOfPayments;
    private LocalDate loanWithdrawalDate;
    private double monthlyInterestRate;
    private double annuityCoefficient;
    private double termPayment;

    public void setParameters() throws Exception {
        System.out.println("Input the loan withdrawal date in this order: year, month, day:");
        this.loanWithdrawalDate = LocalDate.of(UtilScanner.scan.nextInt(), UtilScanner.scan.nextInt(), UtilScanner.scan.nextInt());
        System.out.println("Input the amount of money withdrawn:");
        this.loanAmount = UtilScanner.scan.nextDouble();
        if (this.loanAmount <= 0) {
            throw new Exception("Invalid amount");
        }
        System.out.println("Input the annual interest percentage rate:");
        this.annualInterestRateInPercent = UtilScanner.scan.nextDouble();
        if (this.annualInterestRateInPercent < 0 || this.annualInterestRateInPercent > 100) {
            throw new Exception("Invalid interest rate");
        }
        System.out.println("Input the number of return payments:");
        this.numberOfPayments = UtilScanner.scan.nextInt();
        if (this.numberOfPayments <= 0) {
            throw new Exception("Invalid number of payments");
        }
        this.monthlyInterestRate = annualInterestRateInPercent / 1200;
        this.annuityCoefficient = monthlyInterestRate / (1 - (Math.pow((1 + monthlyInterestRate), -(numberOfPayments))));
        this.termPayment = Math.floor(annuityCoefficient * loanAmount * 100) / 100;
    }

    private ArrayList<String> schedule = new ArrayList<String>();

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
        Path schedulePath = Paths.get("", "customSchedule.csv");
        try {
            Files.write(schedulePath, schedule, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
