package model;

import controller.Polyclinic;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

public class Doctor extends Thread {
    private static final Logger logger = LogManager.getLogger(Polyclinic.class.getName());

    private final int id;
    private final Speciality speciality;
    private final int maxPatientsCount;

    private final BlockingQueue<Patient> patientQueue;
    private final ReentrantLock lock;
    private Integer curedPatientsCount;
    private Polyclinic polyclinic;


    public Doctor(int id, Speciality speciality, int maxPatientsCount) {
        this.id = id;
        this.speciality = speciality;

        this.maxPatientsCount = maxPatientsCount;
        this.curedPatientsCount = 0;
        this.patientQueue = new PriorityBlockingQueue<>();

        lock = new ReentrantLock();
        logger.info("Доктор " + id + " создан!");
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
    public void run() {
        while (polyclinic.isOpened()) {
            Patient patient;
            try {
                patient = patientQueue.take();
            } catch (InterruptedException e) {
                printSummary("прерывание");
                //Thread.currentThread().interrupt();
                return;
            }

            lock.lock();
            patient.cure(this);
            curedPatientsCount++;
            if (curedPatientsCount == maxPatientsCount) {
                printSummary("вылечил макс. число пациентов");
                lock.unlock();
                return;
            }
            lock.unlock();
        }
        printSummary("конец рабочего дня");
    }

    public void printSummary(String reason) {
        logger.info("Доктор " + id + " завершил работу по причине: " + reason +
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
