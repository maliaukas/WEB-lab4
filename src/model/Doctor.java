package model;

import controller.Polyclinic;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Класс Врач-узкий специалист, который может принять ограниченное количество пациентов
 *
 * @author Александра Малявко
 * @version 2020
 */

public class Doctor extends Thread {
    private static final Logger logger = LogManager.getLogger(Polyclinic.class.getName());

    private final int id;
    private final Speciality speciality;
    private final int maxPatientsCount;

    private final PriorityBlockingQueue<Patient> patientQueue;
    private final ReentrantLock lock;
    private Integer curedPatientsCount;
    private Polyclinic polyclinic;

    /**
     * Конструктор
     *
     * @param id               идентификатор врача
     * @param speciality       специализация
     * @param maxPatientsCount максимальное число пациентов
     */
    public Doctor(int id, Speciality speciality, int maxPatientsCount) {
        this.id = id;
        this.speciality = speciality;

        this.maxPatientsCount = maxPatientsCount;
        this.curedPatientsCount = 0;
        this.patientQueue = new PriorityBlockingQueue<>();

        lock = new ReentrantLock();
        logger.info("Доктор " + id + " создан!");
    }

    /**
     * Метод, устанавливающий принадлежность врача к поликлинике
     *
     * @param polyclinic поликлиника, в которой работает врач
     */
    public void setPolyclinic(Polyclinic polyclinic) {
        this.polyclinic = polyclinic;
    }


    /**
     * Метод, добавляющий пацента в очередь к врачу
     *
     * @param p пациент
     * @throws DoctorException в случае, если максимальное число пациентов уже достигнуто
     */
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

    /**
     * Геттер для специализации врача
     *
     * @return специализацию
     */
    public Speciality getSpeciality() {
        return speciality;
    }

    /**
     * Метод, выполняющий работу врача
     */
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

    /**
     * Метод, выводящий в логгер сообщение о завершении работы врача по какой-либо причине
     *
     * @param reason причина завершения работы
     */
    private void printSummary(String reason) {
        logger.info("Доктор " + id + " завершил работу по причине: " + reason +
                ", \n\tвылечив пациентов: " + curedPatientsCount);
    }

    /**
     * Геттер максимального числа пациентов, которое может принять врач
     *
     * @return максимальное число пациентов
     */
    public int getMaxPatientsCount() {
        return maxPatientsCount;
    }

    /**
     * Геттер идентификатора врача
     *
     * @return идентификатор
     */
    public int getDoctorId() {
        return id;
    }

    /**
     * Перечисление возможных специальностей врачей
     */
    public enum Speciality {
        THERAPIST,      // терапевт
        RADIOLOGIST,    // рентгенолог
        PSYCHIATRIST,   // психиатр
        GYNECOLOGIST,   // гинеколог
        SURGEON,        // хирург
        DENTIST,        // стоматолог
    }
}
