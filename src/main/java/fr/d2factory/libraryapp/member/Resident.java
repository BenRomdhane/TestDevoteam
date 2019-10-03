package fr.d2factory.libraryapp.member;

public class Resident extends Member {


    public Resident() {
        super();
    }

    @Override
    public void payBook(int numberOfDays) {
        if( numberOfDays > 60) {
            setWallet((float) (getWallet() - (((numberOfDays-60) * 0.2) + 6)));
        }
        else {
            setWallet((float) (getWallet() - numberOfDays * 0.10));
        }
    }
}
