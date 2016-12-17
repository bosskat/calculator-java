import java.util.Scanner;
import java.io.FileWriter;
import java.io.IOException;
import java.io.BufferedWriter;

public class AmortisationCalculator{
    
    private static Scanner scanner = new Scanner(System.in);
    private static double monthlyRate;
    
    public static void main(String[] args){
        System.out.println("Please enter your total loan amount: ");
        double principal = scanner.nextDouble();
        System.out.println("Please enter your APR interest rate (format is 00:00): ");
        double interest = scanner.nextDouble();
        System.out.println("Please enter in your total number of payments: ");
        int numberOfPayments = scanner.nextInt();
        
        monthlyRate = (double) interest / 1200;
        double amortisation = calculate(principal,numberOfPayments);
        System.out.println("=========================================");
        System.out.printf("Your periodic payment amount is %.4f\n", amortisation);
        System.out.println("=========================================");
        printTable(principal,numberOfPayments);
        
    }
    
    private static double calculate(double principal, int numberOfPayments){
        double amortisation = 0.00;
        if(monthlyRate > 0){
            amortisation = (principal * monthlyRate * Math.pow(1 + monthlyRate, (double)numberOfPayments)) /
            (Math.pow(1 + monthlyRate, (double)numberOfPayments) - 1);
        }
        else{
            amortisation = principal / numberOfPayments;
        }
        return amortisation;
    }
    
    public static void printTable(double principal, int numberOfPayments){
        // Begin printing table
        Object[] headers = {"Payment #", "Amount Due", "Interest Due", "Principal Due", "Amount Left"};
        System.out.println("\n========================Your Mortgage Payoff Table=======================");
        System.out.println("==========================--------------------------=======================");
        System.out.format("%5s %12s %18s %18s %16s \n", headers);
        System.out.println("===========================================================================");
        
        // Establish values for the variables that we will use in the calculations
        double monthlyPayment = calculate(principal,numberOfPayments);
        double interestPayment = 0.0;
        double principalDue = 0.0;
        double principalLeft = principal;
        boolean loanPaid = false;
        
        Object[][] rows = new Object[numberOfPayments][];
        
        for(int i = 0; i < numberOfPayments; i++){
            interestPayment = monthlyRate * principalLeft;
            principalDue = monthlyPayment - interestPayment;
            principalLeft = Math.abs(principalLeft - principalDue);
            
            Object[] row = {i + 1, monthlyPayment, interestPayment, principalDue, principalLeft};
            rows[i] = row;
            System.out.format("%5d %17.4f %19.4f %19.4f %17.4f \n", row);
        }
        promptToSaveCSV(rows);
    }
    
    private static void promptToSaveCSV(Object[][] rows){
        System.out.println("\nWould you like to save the results to a CSV file? [Yes/No]: ");
        String response = scanner.next();
        if("yes".equalsIgnoreCase(response)){
            try {
                FileWriter fileWriter = new FileWriter("amortisation_table.csv");
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                StringBuilder stringBuilder = new StringBuilder();
                
                for(Object[] row : rows){
                    for(Object column : row){
                        if(stringBuilder.length() != 0){
                            stringBuilder.append(',');
                        }
                        stringBuilder.append(column);
                    }
                    stringBuilder.append("\n");
                    bufferedWriter.write(stringBuilder.toString());
                    stringBuilder.setLength(0);
                }
                bufferedWriter.close();
                
                System.out.println("Results saved to amortisation_table.csv");
            } catch(IOException e) {
                e.printStackTrace();
            }
        }
        else{
            System.out.println("You chose not to save the results to CSV");
        }
    }
}