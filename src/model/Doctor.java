package model;

import java.util.PriorityQueue;
import java.util.Queue;

public class Doctor extends Thread {
    private final int id;
    private final Speciality speciality;

    private final int maxPatientsCount;
    private final Queue<Patient> patientQueue;
    private int curedPatientsCount;
    private Polyclinic polyclinic;

    public void setPolyclinic(Polyclinic polyclinic) {
        this.polyclinic = polyclinic;
    }

    public Doctor(int id, Speciality speciality, int maxPatientsCount) {
        this.id = id;
        this.speciality = speciality;

        this.maxPatientsCount = maxPatientsCount;
        this.curedPatientsCount = 0;
        this.patientQueue = new PriorityQueue<>();
    }

    public void addPatient(Patient p) throws Exception {
        if (curedPatientsCount + patientQueue.size() + 1 <= maxPatientsCount) {
            try {
                patientQueue.add(p);
            } catch (Exception e) {
                System.err.println(e.getMessage());
            }
        } else {
            throw new Exception("Cannot add patient to the queue!");
        }
    }

    public int getMaxPatientsCount() {
        return maxPatientsCount;
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    @Override
    public synchronized void run() {
        while (polyclinic.isOpened()) {
            while (patientQueue.isEmpty()) {
                try {
                    synchronized (this) {
                        wait();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            while (!patientQueue.isEmpty()) {
                var patient = patientQueue.poll();
                patient.cure(this);
                curedPatientsCount++;
            }
            if (curedPatientsCount == maxPatientsCount)
                break;
        }
        System.out.println("Доктор " + id + " завершил работу, " +
                "вылечив пациентов: " + curedPatientsCount);
    }


    public int getDoctorId() {
        return id;
    }

    public enum Speciality {
        THERAPIST,      // терапевт
        RADIOLOGIST,    // рентгенолог
        PSYCHIATRIST,   // психиатр
        GYNECOLOGIST,   // гинеколог
        SURGEON,        // хирург
        DENTIST,        // стоматолог
    }
}
