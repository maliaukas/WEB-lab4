package controller;

import model.Doctor;
import model.DoctorException;
import model.Patient;
import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.Random;

public class Polyclinic {
    private static final Logger logger = LogManager.getLogger(Polyclinic.class.getName());
    ArrayList<Doctor> doctors;
    private boolean isOpened;

    public Polyclinic(ArrayList<Doctor> doctors) {
        this.doctors = doctors;
        logger.info("Поликлиника создана!");
    }

    public void addPatient(Patient p) {
        Random random = new Random();
        for (var doc : doctors) {
            if (doc.getSpeciality() == p.getRequiredDoctorSpeciality()) {
                try {
                    p.setPriority(random.nextInt(doc.getMaxPatientsCount()));
                    doc.addPatient(p);
                } catch (DoctorException e) {
                    logger.error(e.getMessage());
                    continue;
                }
                logger.debug("Пациент " + p.getId() +
                        " записался в очередь ко врачу " + doc.getDoctorId());
                return;
            }
        }
        logger.debug("Пациент " + p.getId() +
                " не смог попасть к нужному врачу и уходит.");
    }

    public void startWork() {
        isOpened = true;
        for (var doctor : doctors) {
            doctor.setPolyclinic(this);
            doctor.start();
        }
        logger.info("Все доктора начали работу!");
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
        logger.info("Все доктора закончили работу!");
    }
}
