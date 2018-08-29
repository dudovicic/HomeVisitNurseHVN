package hr.ferit.dudovicic.homevisitnursehvn;
public class patients {

    int patient_id;
    String patient_name;
    String patient_surname;
    int oib;
    String address;
    String blood_type;
    String visit_hour;
    String visit_date;
    String visit_therapy;
    String visit_notes;
    String history_of_illness;
    String image;

    patients() {}

    public patients(int patient_id, String patient_name, String patient_surname, int oib,
                    String address, String blood_type, String visit_hour,
                    String visit_date, String visit_therapy, String visit_notes,
                    String history_of_illness, String image) {
        this.patient_id = patient_id;
        this.patient_name = patient_name;
        this.patient_surname = patient_surname;
        this.oib = oib;
        this.address = address;
        this.blood_type = blood_type;
        this.visit_hour = visit_hour;
        this.visit_date = visit_date;
        this.visit_therapy = visit_therapy;
        this.visit_notes = visit_notes;
        this.history_of_illness = history_of_illness;
        this.image = image;
    }

    public int getPatientsId() {
        return patient_id;
    }

    public void setPatientsId(int patient_id) {
        this.patient_id = patient_id;
    }

    public String getPatientsName() {
        return patient_name;
    }

    public void setPatientsName(String patient_name) {
        this.patient_name = patient_name;
    }

    public String getPatientsSurname() {
        return patient_surname;
    }

    public void setPatientsSurame(String patient_surname) {
        this.patient_surname = patient_surname;
    }

    public int getPatientsOib() {
        return oib;
    }

    public void setPatientsOib(int oib) {
        this.oib = oib;
    }

    public String getPatientsAddress() {
        return address;
    }

    public void setPatientsAddress(String address) {
        this.address = address;
    }

    public String getPatientsBlood_type() {
        return blood_type;
    }

    public void setPatientsBlood_type(String blood_type) {
        this.blood_type = blood_type;
    }

    public String getPatientsLast_visit() {
        return visit_hour;
    }

    public void setPatientsLast_visit(String visit_hour) {
        this.visit_hour = visit_hour;
    }

    public String getPatientsOrdered_visit() {
        return visit_date;
    }

    public void setPatientsOrdered_visit(String visit_date) {
        this.visit_date = visit_date;
    }

    public String getTherapy_for_ordered_visit() {
        return visit_therapy;
    }

    public void setTherapy_for_ordered_visit(String visit_therapy) {
        this.visit_therapy = visit_therapy;
    }

    public String getNotes_from_last_visit() {
        return visit_notes;
    }

    public void setNotes_from_last_visit(String visit_notes) {
        this.visit_notes = visit_notes;
    }

    public String getHistory_of_illness() {
        return history_of_illness;
    }

    public void setHistory_of_illness(String history_of_illness) {
        this.history_of_illness = history_of_illness;
    }

    public String getPatientsImage() {
        return image;
    }

    public void setPatientsImage(String image) {
        this.image = image;
    }


}
