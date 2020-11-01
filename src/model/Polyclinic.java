package model;

import java.util.ArrayList;

public class Polyclinic {
    ArrayList<Doctor> doctors;
    private boolean isOpened;

    public Polyclinic(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
    }

    public void addPatient(Patient p) {
        for (var doc : doctors) {
            if (doc.getSpeciality() == p.getRequiredDoctorSpeciality()) {
                try {
                    doc.addPatient(p);
                } catch (Exception e) {
                    e.printStackTrace();
                    continue;
                }
                System.out.println("Пациент " + p.getId() +
                        " записался в очередь ко врачу " + doc.getDoctorId());
                try {
                    synchronized (doc) {
                        doc.notify();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
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

    public synchronized void endWork() {
        isOpened = false;
        for (var doc: doctors) {
            try {
                doc.join();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        System.out.println("Все доктора закончили работу!");
    }
}
