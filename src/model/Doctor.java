package model;

import controller.Polyclinic;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class Doctor extends Thread {
    private final int id;
    private final Speciality speciality;

    private final int maxPatientsCount;
    private final BlockingQueue<Patient> patientQueue;
    private Integer curedPatientsCount;
    private Polyclinic polyclinic;

    private Lock lock;

    public Doctor(int id, Speciality speciality, int maxPatientsCount) {
        this.id = id;
        this.speciality = speciality;

        this.maxPatientsCount = maxPatientsCount;
        this.curedPatientsCount = 0;
        this.patientQueue = new PriorityBlockingQueue<>();

        lock = new ReentrantLock();
    }

    public void setPolyclinic(Polyclinic polyclinic) {
        this.polyclinic = polyclinic;
    }

    public void addPatient(Patient p) throws DoctorException {
        lock.lock();
        try {
            if (curedPatientsCount + patientQueue.size() + 1 <= maxPatientsCount) {
                patientQueue.add(p);
            } else {
                throw new DoctorException("Невозможно добавить пациента " + p.getId()
                        + " в очередь к врачу " + id +
                        ": максимальное число пациентов достигнуто!");
            }
        } finally {
            lock.unlock();
        }
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    @Override
    public synchronized void run() {
        while (polyclinic.isOpened()) {
            Patient patient = null;
            try {
                patient = patientQueue.take();
            } catch (InterruptedException e) {
                printSummary("прерывание");
                return;
            }
            patient.cure(this);
            lock.lock();
            curedPatientsCount++;
            if (curedPatientsCount == maxPatientsCount) {
                printSummary("вылечил макс. число пациентов");
                return;
            }
            lock.unlock();
        }
        printSummary("конец рабочего дня");
    }

    public void printSummary(String reason) {
        System.out.println("Доктор " + id + " завершил работу по причине: " + reason +
                ", \n\tвылечив пациентов: " + curedPatientsCount);
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
