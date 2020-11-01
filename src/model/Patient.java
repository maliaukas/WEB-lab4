package model;

public class Patient implements Comparable<Patient> {
    private final int id;
    private final Doctor.Speciality requiredDoctorSpeciality;

    public Patient(int id, Doctor.Speciality requiredDoctorSpeciality) {
        this.id = id;
        this.requiredDoctorSpeciality = requiredDoctorSpeciality;
    }

    public void cure(Doctor doctor) {
        if (doctor.getSpeciality() == requiredDoctorSpeciality) {
            System.out.println("Пациент " + id + " вылечен доктором "
                    + doctor.getDoctorId() + "!");
        }
    }

    public int getId() {
        return id;
    }

    public Doctor.Speciality getRequiredDoctorSpeciality() {
        return requiredDoctorSpeciality;
    }

    public int compareTo(Patient p) {
        return Integer.compare(this.id, p.getId());
    }
}
