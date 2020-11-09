package model;

import controller.Polyclinic;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

/**
 * Класс Пациент, которому требуется помощь врача конкретной специализации
 *
 * @author Александра Малявко
 * @version 2020
 */

public class Patient implements Comparable<Patient> {
    private static final Logger logger = LogManager.getLogger(Polyclinic.class.getName());

    private final int id;
    private final Doctor.Speciality requiredDoctorSpeciality;
    private int priority;

    /**
     * Конструктор
     *
     * @param id                       идентификатор пациента
     * @param requiredDoctorSpeciality специализация врача
     */
    public Patient(int id, Doctor.Speciality requiredDoctorSpeciality) {
        this.id = id;
        this.requiredDoctorSpeciality = requiredDoctorSpeciality;
        this.priority = 0;
    }

    /**
     * Геттер приоритета пациента
     *
     * @return приоритет
     */
    public int getPriority() {
        return priority;
    }

    /**
     * Сеттер приоритета пациента
     */
    public void setPriority(int priority) {
        this.priority = priority;
    }

    /**
     * Метод, выполняющий лечение пациента доктором
     *
     * @param doctor доктор
     */
    public void cure(Doctor doctor) {
        logger.debug("Пациент " + id + /*" с приоритетом " + priority +*/ " вылечен врачом "
                + doctor.getDoctorId() + "!");
    }

    /**
     * Геттер идентификатора пациента
     *
     * @return идентификатор
     */
    public int getId() {
        return id;
    }

    /**
     * Геттер необходимой специализации врача
     *
     * @return специализацию
     */
    public Doctor.Speciality getRequiredDoctorSpeciality() {
        return requiredDoctorSpeciality;
    }

    /**
     * Компаратор по приоритету пациентов
     *
     * @param p пациент, с которым происходит сравнение
     * @return результат сравнения
     */
    public int compareTo(Patient p) {
        return Integer.compare(priority, p.getPriority());
    }
}
