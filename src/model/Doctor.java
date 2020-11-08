package model;

import controller.Polyclinic;

import java.util.Queue;
import java.util.concurrent.PriorityBlockingQueue;

public class Doctor extends Thread {
    private final int id;
    private final Speciality speciality;

    private final int maxPatientsCount;
    private final Queue<Patient> patientQueue;
    private Integer curedPatientsCount;
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

    public void addPatient(Patient p) throws DoctorException {
        synchronized (curedPatientsCount) {
            if (curedPatientsCount + patientQueue.size() + 1 <= maxPatientsCount) {
                patientQueue.add(p);
            } else {
                throw new DoctorException("Невозможно добавить пациента " + p.getId()
                        + " в очередь к врачу " + id +
                        ": максимальное число пациентов достигнуто!");
            }
        }
    }

    public Speciality getSpeciality() {
        return speciality;
    }

    @Override
    public synchronized void run() {
        while (polyclinic.isOpened()) {
            synchronized (curedPatientsCount) {
                if (curedPatientsCount == maxPatientsCount)
                    break;
            }

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
                synchronized (curedPatientsCount) {
                    curedPatientsCount++;
                }
//                try {
//                    Thread.sleep(3000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
            }
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
