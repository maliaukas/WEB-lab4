package model;

import controller.Polyclinic;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Doctor extends Thread {
    private final int id;
    private final Speciality speciality;

    private final int maxPatientsCount;
    private final Queue<Patient> patientQueue;
    private int curedPatientsCount;
    private Polyclinic polyclinic;

    public Doctor(int id, Speciality speciality, int maxPatientsCount) {
        this.id = id;
        this.speciality = speciality;

        this.maxPatientsCount = maxPatientsCount;
        this.curedPatientsCount = 0;
        this.patientQueue = new PriorityBlockingQueue<>();
    }

    public void setPolyclinic(Polyclinic polyclinic) {
        this.polyclinic = polyclinic;
    }

    public synchronized void addPatient(Patient p) throws DoctorException {
        if (curedPatientsCount + patientQueue.size() + 1 <= maxPatientsCount) {
            patientQueue.add(p);
        } else {
            throw new DoctorException("Невозможно добавить пациента " + p.getId()
                    + " в очередь к врачу " + id +
                    ": максимальное число пациентов достигнуто!");
        }
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    @Override
    public synchronized void run() {
        while (polyclinic.isOpened()) {
            if (patientQueue.isEmpty()) {
                try {
                    wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                    Thread.currentThread().interrupt();
                }
            }

            while (!patientQueue.isEmpty()) {
                var patient = patientQueue.poll();
                patient.cure(this);
                curedPatientsCount++;
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }

            if (curedPatientsCount == maxPatientsCount)
                break;
        }
        System.out.println("Доктор " + id + " завершил работу, " +
                "вылечив пациентов: " + curedPatientsCount);
    }

    public int getMaxPatientsCount() {
        return maxPatientsCount;
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
