class Doctor{
    private final int name;
    private boolean visiting = false;

    public Doctor(int n){
        this.name = n;
    }

    public synchronized void startVisit(){
        while(visiting){
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        visiting = true;
    }

    public synchronized void endVisit(){
        visiting = false;
        notify();
    }

    public int getName() {
        return name;
    }
}
