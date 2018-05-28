public class Main {
    public static void main(String[] args) {

        ScheduleMaker sc1 = new ScheduleMaker();
        ScheduleMakerWithInterestRateChange sc2 = new ScheduleMakerWithInterestRateChange();
        CustomScheduleMaker sc3 = new CustomScheduleMaker();
        sc1.makeSchedule();
        sc2.makeSchedule();
        try {
            sc3.setParameters();
        } catch (Exception e) {
            e.printStackTrace();
        }
        sc3.makeSchedule();
    }
}
