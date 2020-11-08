package controller;

import model.Doctor;
import model.DoctorException;
import model.Patient;

import java.util.ArrayList;
import java.util.Random;

public class Polyclinic {
    ArrayList<Doctor> doctors;
    private boolean isOpened;

    public Polyclinic(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    public void addPatient(Patient p) {
        Random random = new Random();
        for (var doc : doctors) {
            if (doc.getSpeciality() == p.getRequiredDoctorSpeciality()) {
                try {
                    p.setPriority(random.nextInt(doc.getMaxPatientsCount()));
                    doc.addPatient(p);
                } catch (DoctorException e) {
                    System.out.println(e.getMessage());
                    continue;
                }
                System.out.println("Пациент " + p.getId() +
                        " записался в очередь ко врачу " + doc.getDoctorId());
                return;
            }
        }
        System.out.println("Пациент " + p.getId() +
                " не смог попасть к нужному врачу и уходит.");
    }

    public void startWork() {
        isOpened = true;
        for (var doctor : doctors) {
            doctor.setPolyclinic(this);
            doctor.start();
        }
        System.out.println("Все доктора начали работу!");
    }

    public boolean isOpened() {
        return isOpened;
    }

    public void endWork() {
        isOpened = false;
        for (var doc : doctors) {
            try {
                doc.interrupt();
                doc.join();



            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Все доктора закончили работу!");
    }
}
