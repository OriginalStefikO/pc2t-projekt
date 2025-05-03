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

    public int getId() { return id; }
    public void setId(int generatedId) { this.id = generatedId; }
    public String getJmeno() { return jmeno; }
    public String getPrijmeni() { return prijmeni; }
    public int getRokNarozeni() { return rokNarozeni; }
    public List<Integer> getZnamky() { return znamky; }

    public void pridatZnamku(int znamka) {
        if (znamka >= 1 && znamka <= 5) {
            znamky.add(znamka);
        }
    }

    public double getStudijniPrumer() {
        return znamky.isEmpty() ? 0.0 : znamky.stream().mapToInt(Integer::intValue).average().orElse(0.0);
    }

    public abstract String zpracujIdentitu();

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
