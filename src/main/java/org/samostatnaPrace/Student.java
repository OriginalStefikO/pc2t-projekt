package org.samostatnaPrace;

import java.util.ArrayList;
import java.util.List;

public abstract class Student {
    protected int id;
    protected String jmeno;
    protected String prijmeni;
    protected int rokNarozeni;
    protected List<Integer> znamky;

    public Student(String jmeno, String prijmeni, int rokNarozeni) {
        this.jmeno = jmeno;
        this.prijmeni = prijmeni;
        this.rokNarozeni = rokNarozeni;
        this.znamky = new ArrayList<>();
    }

    public void setId(int generatedId) { this.id = generatedId; }
    public void setJmeno(String jmeno) { this.jmeno = jmeno; }
    public void setPrijmeni(String prijmeni) { this.prijmeni = prijmeni; }
    public void setRokNarozeni(int rokNarozeni) { this.rokNarozeni = rokNarozeni; }
    public void setZnamky(List<Integer> znamky) { this.znamky = znamky; }

    public int getId() { return id; }
    public String getJmeno() { return jmeno; }
    public String getPrijmeni() { return prijmeni; }
    public int getRokNarozeni() { return rokNarozeni; }
    public List<Integer> getZnamky() { return znamky; }


    public double getStudijniPrumer() {
        return znamky.isEmpty() ? 0.0 : znamky.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public abstract String zpracujSkill();

    public void pridatZnamku(int znamka) {
        if (znamka >= 1 && znamka <= 5) {
            znamky.add(znamka);
        }
    }

    @Override
    public String toString() {
        return "Student{" +
                "id=" + id +
                ", jmeno='" + jmeno + '\'' +
                ", prijmeni='" + prijmeni + '\'' +
                ", rokNarozeni=" + rokNarozeni +
                ", znamky=" + znamky +
                '}';
    }
}
