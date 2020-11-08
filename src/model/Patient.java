package model;

public class Patient implements Comparable<Patient> {
    private final int id;
    private final Doctor.Speciality requiredDoctorSpeciality;
    private int priority;

    public Patient(int id, Doctor.Speciality requiredDoctorSpeciality) {
        this.id = id;
        this.requiredDoctorSpeciality = requiredDoctorSpeciality;
        this.priority = 0;
    }

    public int getPriority() {
        return priority;
    }

    public void setPriority(int priority) {
        this.priority = priority;
    }

    public void cure(Doctor doctor) {
        System.out.println("Пациент " + id + /*" с приоритетом " + priority +*/ " вылечен врачом "
                + doctor.getDoctorId() + "!");
    }

    public int getId() {
        return id;
    }

    public Doctor.Speciality getRequiredDoctorSpeciality() {
        return requiredDoctorSpeciality;
    }

    public int compareTo(Patient p) {
        return Integer.compare(priority, p.getPriority());
    }
}
