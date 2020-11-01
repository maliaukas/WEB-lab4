package view;

import model.Doctor;
import model.Patient;
import model.Polyclinic;

import javax.print.Doc;
import java.util.ArrayList;
import java.util.Arrays;

public class Runner {
    public static void main(String[] args) {
        try {
            Doctor gynecologist = new Doctor
                    (1, Doctor.Speciality.GYNECOLOGIST, 4);
            Doctor therapist = new Doctor
                    (2, Doctor.Speciality.THERAPIST, 6);
            Doctor dentist = new Doctor
                    (3, Doctor.Speciality.DENTIST, 10);

            ArrayList<Doctor> doctors =
                    new ArrayList<>(Arrays.asList(gynecologist, therapist, dentist));

            Polyclinic polyclinic = new Polyclinic(doctors);
            polyclinic.startWork();

            polyclinic.addPatient(new Patient(1, Doctor.Speciality.GYNECOLOGIST));
            polyclinic.addPatient(new Patient(2, Doctor.Speciality.GYNECOLOGIST));
            polyclinic.addPatient(new Patient(3, Doctor.Speciality.GYNECOLOGIST));
            polyclinic.addPatient(new Patient(4, Doctor.Speciality.DENTIST));
            polyclinic.addPatient(new Patient(5, Doctor.Speciality.GYNECOLOGIST));
            polyclinic.addPatient(new Patient(6, Doctor.Speciality.THERAPIST));
            polyclinic.addPatient(new Patient(89, Doctor.Speciality.THERAPIST));
            //polyclinic.addPatient(new Patient(7, Doctor.Speciality.DENTIST));
            //polyclinic.addPatient(new Patient(8, Doctor.Speciality.DENTIST));

            polyclinic.endWork();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
