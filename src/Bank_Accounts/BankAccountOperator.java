package Bank_Accounts;

//class to elaborate the movements of a bank account
public class BankAccountOperator implements Runnable {

    private BankAccount bankAccount;

    public BankAccountOperator(BankAccount bankAccount){
        this.bankAccount = bankAccount;
    }

    @Override
    public void run() {
        //updates global counters
        for(Movement movement: bankAccount.getMovementList()){
            switch (movement.getCause()){
                case F24:
                    GlobalVars.F24_counter++;
                    break;
                case POSTAL:
                    GlobalVars.POST_counter++;
                    break;
                case BANCOMAT:
                    GlobalVars.BANC_counter++;
                    break;
                case TRANSFER:
                    GlobalVars.TRANS_counter++;
                    break;
                case ACCREDITATION:
                    GlobalVars.ACC_counter++;
                    break;
            }
        }
    }
}
