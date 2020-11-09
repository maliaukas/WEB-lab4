package view;

import controller.Polyclinic;
import model.Doctor;
import model.Patient;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * Класс Runner, который демонстрирует работу с проектом
 *
 * @author Александра Малявко
 * @version 2020
 */

public class Runner {
    public static void main(String[] args) {
        Doctor gynecologist = new Doctor
                (1, Doctor.Speciality.GYNECOLOGIST, 2);

        Doctor therapist = new Doctor
                (2, Doctor.Speciality.THERAPIST, 3);

        Doctor dentist = new Doctor
                (3, Doctor.Speciality.DENTIST, 2);

        Doctor gynecologist2 = new Doctor
                (4, Doctor.Speciality.GYNECOLOGIST, 3);

        ArrayList<Doctor> doctors =
                new ArrayList<>(Arrays.asList(
                        gynecologist, therapist,
                        dentist, gynecologist2));

        Polyclinic polyclinic = new Polyclinic(doctors);
        polyclinic.startWork();

        polyclinic.addPatient(new Patient(1, Doctor.Speciality.GYNECOLOGIST));
        polyclinic.addPatient(new Patient(8, Doctor.Speciality.DENTIST));
        polyclinic.addPatient(new Patient(2, Doctor.Speciality.GYNECOLOGIST));
        polyclinic.addPatient(new Patient(6, Doctor.Speciality.THERAPIST));
        polyclinic.addPatient(new Patient(3, Doctor.Speciality.GYNECOLOGIST));
        polyclinic.addPatient(new Patient(4, Doctor.Speciality.DENTIST));
        polyclinic.addPatient(new Patient(5, Doctor.Speciality.GYNECOLOGIST));
        polyclinic.addPatient(new Patient(10, Doctor.Speciality.GYNECOLOGIST));
        polyclinic.addPatient(new Patient(9, Doctor.Speciality.THERAPIST));
        polyclinic.addPatient(new Patient(7, Doctor.Speciality.DENTIST));

        polyclinic.endWork();
    }
}
