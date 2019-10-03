package fr.d2factory.libraryapp.member;

public class Student extends Member {

    private boolean isStudentFirstYear;

    public Student() {
        super();
    }

    @Override
    public void payBook(int numberOfDays) {

        if (isStudentFirstYear) {
            if (numberOfDays > 15 && numberOfDays <= 30) {
                setWallet((float) (getWallet() - (numberOfDays - 15) * 0.1));
            } else if (numberOfDays > 30) {
                setWallet((float) (getWallet() - (((numberOfDays - 30) * 0.15) + 1.5)));
            }
        } else {
            if (numberOfDays <= 30) {
                setWallet((float) (getWallet() - numberOfDays * 0.1));
            } else if (numberOfDays > 30) {
                setWallet((float) (getWallet() - (((numberOfDays - 30) * 0.15) + 3)));
            }
        }

    }

    public void setStudentFirstYear(boolean studentFirstYear) {
        isStudentFirstYear = studentFirstYear;
    }
}
